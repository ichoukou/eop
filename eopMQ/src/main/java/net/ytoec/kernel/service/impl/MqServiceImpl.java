package net.ytoec.kernel.service.impl;

import java.util.Date;
import java.util.List;

import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.search.dto.MailObjectDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.OrderCODService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.helper.OrderOperateHelper;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

public class MqServiceImpl extends AbstractMQServiceImpl {

	private static Logger logger = LoggerFactory.getLogger(MqServiceImpl.class);

	@Autowired
	private OrderService<Order> orderService;

	@Autowired
	private OrderCODService orderCODService;
	
	@Autowired
	private EccoreSearchService eccoreSearchService;
	
	@Autowired
	private OrderDao<Order> dao;
	
	@Override
	public void receiveImpl4Solr(List<String> msgs) throws Exception {
		try
		{
			eccoreSearchService.writeOrderChangeInfo2SolrFromMq(msgs);
		}catch(Exception e){
			logger.error("MqServiceImpl receiveImpl4Solr error",e);
			throw e;
		}
	}

	@Override
	public void receiveImpl(String msg) throws Exception {

		if (StringUtils.isBlank(msg)) {
			logger.info("msg is blank");
		}
		String[] msgs = StringUtils.split(msg,net.ytoec.kernel.action.common.Constants.MQ_MSG_SPLIT);
		if (msgs == null || msgs.length < 2) {
			logger.error("msg format is error:" + msg);
			throw new Exception("msg format is error:" + msg);
		}
		if (StringUtils.equalsIgnoreCase(Constants.ORDER_CREATE, msgs[0])) {
			if (msgs.length != 3) {
				logger.error("msg format is error:" + msg);
				throw new Exception("msg format is error:" + msg);
			}
			
			// 订单持久化到易通库
			BuildSearch buildSearch = orderService.addOrderFormInfoByMsg(msgs[1], msgs[2]);
			
			// 写入solr
			if(buildSearch != null){
				
				String message = JSON.toJSONString(buildSearch);
				if (StringUtils.isNotBlank(message)) {
					try {
						if (this.send(message,
										net.ytoec.kernel.common.Constants.SOLR_ADD_ORDER)) {
							logger.error("淘宝订单创建同步到MQ成功！");
						} else {
							logger.error("淘宝创建同步到MQ失败");
						}

					} catch (Exception e) {
						logger.error("淘宝订单创建同步到MQ异常", e);
					}
				}
			}

		} else if (StringUtils
				.equalsIgnoreCase(Constants.ORDER_UPDATE, msgs[0])) {
			List<UpdateWaybillInfo> updateInfoList = new UpdateWaybillInfo()
					.toObjectTaoBao(msgs[1]);

			
			if (updateInfoList != null && updateInfoList.size() > 0) {
				if(!orderService.updOrderMailNoByLogisticIdAndClientId(
						updateInfoList, Constants.JIN_GANG_TAOBAO_ORDER,
						updateInfoList.get(0).getClientId())){
					throw new RuntimeException("淘宝更新------------>没有更新到记录回滚到MQ");
				}else{
					
					// 写入solr
					for(UpdateWaybillInfo bean :updateInfoList){
						 String message = JSON.toJSONString(this.getSolarDto(bean.getTxLogisticId()));
						if (StringUtils.isNotBlank(message)) {
							try {
									
								logger.error("淘宝订单更新同步到MQ物流号："+ bean.getTxLogisticId());
								if (this.send(message,
												net.ytoec.kernel.common.Constants.JGCOMMAND)) {
									logger.error("淘宝订单更新同步到MQ成功");
								} else {
									logger.error("淘宝订单更新同步到MQ失败");
								}
							} catch (Exception e) {
								logger.error("淘宝订单更新同步到MQ异常", e);
							}
						}
					}
				}
			}
		} else if (StringUtils.equalsIgnoreCase(Constants.COD_SUCCESS, msgs[0])
				|| StringUtils.equalsIgnoreCase(Constants.COD_FAILED, msgs[0])) {
			UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
					.toObject(msgs[1]);
				SendTask sendTask = OrderOperateHelper.makeOrderUpdateTask(msgs[1],
						updateInfo);
			orderCODService.addCOD(updateInfo, sendTask);
		}
		
		// 金刚订单更新
		else if(StringUtils
				.equalsIgnoreCase(Constants.DATA_JINGANG, msgs[0])){
			// 报文对象化
			List<UpdateWaybillInfo> updateInfoList = new UpdateWaybillInfo()
					.toObjectTaoBao(msgs[1]);
			
			if(!orderService.orderListStatusNodifyFromMQ(updateInfoList)){
				throw new RuntimeException("金刚更新------------>没有更新到记录回滚到MQ");
			}else{
				for(UpdateWaybillInfo bean :updateInfoList){
					 String message = JSON.toJSONString(this.getSolarDto(bean.getTxLogisticId()));
						if (StringUtils.isNotBlank(message)) {
							try {
									
								logger.error("金刚订单更新同步到MQ物流号："+ bean.getTxLogisticId());
								if (this.send(message,
												net.ytoec.kernel.common.Constants.JGCOMMAND)) {
									logger.error("金刚订单更新同步到MQ成功");
								} else {
									logger.error("金刚订单更新同步到MQ失败");
								}
							} catch (Exception e) {
								logger.error("金刚订单更新同步到MQ异常", e);
							}
						}
				}
			}
			
		} else {
			logger.error("unkonow msg type:" + msgs[0]);
		}
	}
	
	public BuildSearchStatusWeightIndex getSolarDto(String txLogisticID){
		Order order = orderService.getOrderByLogisticId(txLogisticID);
		//order.getsend
		MailObjectDTO traderinfo = new MailObjectDTO();
		if (order != null) {
			logger.error(order.getId() + "==="+ order.getPartitionDate());
			traderinfo = dao.bulidPartEccoreDataByOrderId(
					order.getId(), DateUtil.format(
							order.getPartitionDate(), "yyyy-MM-dd"));
		}
		if (order != null) {
			User user = Resource.getUserByCustomerId(order
					.getCustomerId());
			if (user != null) {
				
				BuildSearchStatusWeightIndex dataObject = setBuildSearchStatusWeightIndex(order,traderinfo);
				
				return dataObject;
			}
		}
		return null;
	}
	
	// 封装BulidSearchStatusWeightIndex表对象
	private BuildSearchStatusWeightIndex setBuildSearchStatusWeightIndex
							(Order order, MailObjectDTO traderinfo) {
		BuildSearchStatusWeightIndex buildSearchStatusWeightIndex = 
							new BuildSearchStatusWeightIndex();
		if (StringUtils.isBlank(order.getMailNo())) {
			buildSearchStatusWeightIndex.setBuildTask("1");
		} else {
			buildSearchStatusWeightIndex.setBuildTask("2");
			// 面单号设置的是旧的面单号
			if (order.getMailNo() != null) {
				buildSearchStatusWeightIndex.setMailNo(order.getMailNo());
				logger.info("mylog:mailno=" + order.getMailNo());
			} else
				buildSearchStatusWeightIndex.setMailNo("");
		}

		if (order.getId() != null) {
			buildSearchStatusWeightIndex.setOrderId(order.getId());
			logger.info("mylog:orderid=" + order.getId());
		} else
			buildSearchStatusWeightIndex.setOrderId(0);

		if (order.getStatus() != null) {
			buildSearchStatusWeightIndex.setStatus(order.getStatus());
			logger.info("mylog:status=" + order.getStatus());
		} else
			buildSearchStatusWeightIndex.setStatus("");
		
		if (order.getCustomerId() != null) {
			buildSearchStatusWeightIndex.setCustomerId(order.getCustomerId());
			logger.info("" + order.getCustomerId());
		} else
			buildSearchStatusWeightIndex.setCustomerId("");

		if (order.getOrderType() != null) {
			buildSearchStatusWeightIndex.setOrderType(order.getOrderType());
			logger.info("mylog:OrderType=" + order.getOrderType());
		} else
			buildSearchStatusWeightIndex.setOrderType(0);
		
		if (order.getTxLogisticId() != null) {
			buildSearchStatusWeightIndex.setTxLogisticId(order.getTxLogisticId());
			logger.info("mylog:getTxLogisticId=" + order.getTxLogisticId());
		} else
			buildSearchStatusWeightIndex.setTxLogisticId("");
		if (order.getTrimFreight() != null) {
			buildSearchStatusWeightIndex.setTrimFreight(order.getTrimFreight());
			logger.info("mylog:getTrimFreight=" + order.getTrimFreight());
		} else
			buildSearchStatusWeightIndex.setTrimFreight(0);

		if (order.getFreightType() != null) {
			buildSearchStatusWeightIndex.setFreightType(order.getFreightType());
			logger.info("mylog:getFreightType=" + order.getFreightType());
		} else
			buildSearchStatusWeightIndex.setFreightType("");

		if (order.getLineType() != null) {
			buildSearchStatusWeightIndex.setLineType(order.getLineType());
			logger.info("mylog:getLineType=" + order.getLineType());
		} else
			buildSearchStatusWeightIndex.setLineType("");

		if (order.getType() != null) {
			buildSearchStatusWeightIndex.setType(order.getType());
			logger.info("mylog:getType=" + order.getType());
		} else
			buildSearchStatusWeightIndex.setType("");

		if (order.getWeight() != null) {
			buildSearchStatusWeightIndex.setWeight(order.getWeight());
			logger.info("mylog:getWeight=" + order.getWeight());
		} else
			buildSearchStatusWeightIndex.setWeight((float)0);

		if (order.getMailNo() != null) {
			buildSearchStatusWeightIndex.setOldMailNo(order.getMailNo());
			logger.info("mylog:getMailNo=" + order.getMailNo());
		} else
			buildSearchStatusWeightIndex.setOldMailNo("");

		if (order.getType() != null) {
			buildSearchStatusWeightIndex.setHoldTime(order.getType());
			logger.info("mylog:getType=" + order.getType());
		} else
			buildSearchStatusWeightIndex.setHoldTime("");

		if (order.getPartitionDate() != null) {
			buildSearchStatusWeightIndex.setPartitiondate(order.getPartitionDate());
			logger.info("mylog:setPartitiondate=" + order.getPartitionDate());
		} else
			buildSearchStatusWeightIndex.setPartitiondate(new Date());
		if (order.getAcceptTime() != null) {
			buildSearchStatusWeightIndex.setAcceptTime(order.getAcceptTime());
			logger.info("mylog:getAcceptTime=" + order.getAcceptTime());
		} else
			buildSearchStatusWeightIndex.setAcceptTime(new Date());
		
		if(traderinfo==null){
			logger.info("traderinfo===null=="+order.getId()+"=="+DateUtil.format(order.getPartitionDate(), "yyyy-mm-dd"));
			return buildSearchStatusWeightIndex;
		}
		if (traderinfo.getProv() != null) {
			buildSearchStatusWeightIndex.setProv(traderinfo.getProv());
			logger.info("mylog:prov=" + traderinfo.getProv());
		} else
			buildSearchStatusWeightIndex.setProv("");

		if (traderinfo.getProvF() != null) {
			buildSearchStatusWeightIndex.setProvF(traderinfo.getProvF());
			logger.info("mylog:provF=" + traderinfo.getProvF());
		} else
			buildSearchStatusWeightIndex.setProvF("");
		
		if (traderinfo.getName() != null) {
			buildSearchStatusWeightIndex.setName(traderinfo.getName());
			logger.info("mylog:Name=" + traderinfo.getName());
		} else
			buildSearchStatusWeightIndex.setName("");

		if (traderinfo.getPhone() != null&&!"".equals(traderinfo.getPhone())) {
			buildSearchStatusWeightIndex.setPhone(traderinfo.getPhone());
			logger.info("mylog:Phone=" + traderinfo.getPhone());
		} else
			buildSearchStatusWeightIndex.setPhone("0");

		if (traderinfo.getMobile() != null) {
			buildSearchStatusWeightIndex.setMobile(traderinfo.getMobile());
			logger.info("mylog:getMobile=" + traderinfo.getMobile());
		} else
			buildSearchStatusWeightIndex.setMobile("");

		if (traderinfo.getNumProvF() !=null) {
			buildSearchStatusWeightIndex.setNumProvF(traderinfo.getNumProvF());
			logger.info("mylog:setNumProvF=" + traderinfo.getNumProvF());
		} else
			buildSearchStatusWeightIndex.setNumProvF(0);

		if (traderinfo.getNumProv() > 0) {
			buildSearchStatusWeightIndex.setNumProv(traderinfo.getNumProv());
			logger.info("mylog:setNumProv=" + traderinfo.getNumProv());
		} else
			buildSearchStatusWeightIndex.setNumProv(0);

		if (traderinfo.getCity() != null) {
			buildSearchStatusWeightIndex.setCity(traderinfo.getCity());
			logger.info("mylog:setCity=" + traderinfo.getCity());
		} else
			buildSearchStatusWeightIndex.setCity("");

		if (traderinfo.getCityF() != null) {
			buildSearchStatusWeightIndex.setCityF(traderinfo.getCityF());
			logger.info("mylog:setCityF=" + traderinfo.getCityF());
		} else
			buildSearchStatusWeightIndex.setCityF("");

		if (traderinfo.getAddress() != null) {
			buildSearchStatusWeightIndex.setAddress(traderinfo.getAddress());
			logger.info("mylog:getAddress=" + traderinfo.getAddress());
		} else
			buildSearchStatusWeightIndex.setAddress("");
		

		if (traderinfo.getDistrict() != null) {
			buildSearchStatusWeightIndex.setDistrict(traderinfo.getDistrict());
			logger.info("mylog:getDistrict=" + traderinfo.getDistrict());
		} else
			buildSearchStatusWeightIndex.setDistrict("");
		
		buildSearchStatusWeightIndex.setCreateTime(new Date());
		logger.error("mylog:buildSearchStatusWeightIndexs date end");
		return buildSearchStatusWeightIndex;
	}
}
