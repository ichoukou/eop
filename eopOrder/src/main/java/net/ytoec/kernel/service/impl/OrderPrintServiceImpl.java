package net.ytoec.kernel.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.XmlBuildProcessor;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.JgWaybillDao;
import net.ytoec.kernel.dao.OrderPrintDao;
import net.ytoec.kernel.dao.SendTaskToTBDao;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderPrint;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.service.OrderPrintService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.LogisticsOfflineSendRequest;
import com.taobao.api.response.LogisticsOfflineSendResponse;

/**
 * 面单打印服务类接口实现类
 * 
 * @author wusha
 * 
 * @param <T>
 */
@Service
public class OrderPrintServiceImpl<T extends OrderPrint> implements
		OrderPrintService<T> {

	@Inject
	private OrderPrintDao<T> orderPrintDao;

	@Inject
	private SendTaskToTBDao<SendTaskToTB> sendTaskToTBDao;

	@Inject
	private OrderService<Order> orderService;

	@Inject
	private JgWaybillDao<JgWaybill> jgWaybillDao;

	private static Logger logger = LoggerFactory
			.getLogger(OrderPrintServiceImpl.class);

	/**
	 * 批量打印界面的记录和数量
	 */
	@Override
	public List<T> getAllOrderPrint(Map<String, Object> param)
			throws DataAccessException {
		return orderPrintDao.getAllOrderPrint(param);
	}

	@Override
	public Integer getTotal(Map<String, Object> param)
			throws DataAccessException {
		return orderPrintDao.getTotal(param);
	}

	/**
	 * 代发货界面的记录和数量
	 */
	@Override
	public List<T> getUndeliverOrderPrint(Map<String, Object> param)
			throws DataAccessException {
		return orderPrintDao.getUndeliverOrderPrint(param);
	}

	@Override
	public Integer getUndeliverTotal(Map<String, Object> param)
			throws DataAccessException {
		return orderPrintDao.getUndeliverTotal(param);
	}

	/**
	 * 已发货界面的记录和数量
	 */
	@Override
	public List<T> getDeliveredOrderPrint(Map<String, Object> param)
			throws DataAccessException {
		return orderPrintDao.getDeliveredOrderPrint(param);
	}

	@Override
	public Integer getDeliveredTotal(Map<String, Object> param)
			throws DataAccessException {
		return orderPrintDao.getDeliveredTotal(param);
	}

	/**
	 * 添加面单打印对象
	 */
	@Override
	public boolean addOrderPrint(T entity) throws DuplicateKeyException,
			DataAccessException {
		try {
			return orderPrintDao.addOrderPrint(entity);
		} catch (DuplicateKeyException e) {
			// TODO: handle exception
			throw e;
		}

	}

	/**
	 * 获取面单打印对象
	 */
	@Override
	public Object getOrderPrint(Integer id) throws DataAccessException {
		return orderPrintDao.getOrderPrint(id);
	}

	/**
	 * 更新面单打印对象
	 */
	@Override
	public void updateOrderPrint(OrderPrint orderPrint)
			throws DataAccessException {
		orderPrintDao.updateOrderPrint(orderPrint);
	}

	@Override
	public String unescape(String src) {
		// TODO Auto-generated method stub
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	@Override
	public List<OrderPrint> getServalOrderPrintLists(Map<String, Object> param)
			throws DataAccessException {
		return orderPrintDao.getServalOrderPrintLists(param);
	}

	/**
	 * 通过 parent_id 查出订单列表
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public List<T> getOrderPrintByParentId(Integer parentId)
			throws DataAccessException {
		return orderPrintDao.getOrderPrintByParentId(parentId);
	}

	@Override
	public String convertXMLToJson(String src) {
		JSONObject jsonObject = (JSONObject) new XMLSerializer().read(src);
		return jsonObject.toString();
	}

	@Override
	public void updateOrderPrintIsPrint(Map map) throws DataAccessException {
		orderPrintDao.updateOrderPrintIsPrint(map);
	}

	@Override
	public void updateOrderPrintIsSend(Map map) throws DataAccessException {
		orderPrintDao.updateOrderPrintIsSend(map);
	}

	@Override
	public void updateOrderPrintIsSendPrint(Map map) throws DataAccessException {
		orderPrintDao.updateOrderPrintIsSendPrint(map);
	}

	@Override
	public void updateOrderPrintByIds(Map param) throws DataAccessException {
		orderPrintDao.updateOrderPrintByIds(param);
	}

	@Override
	public Map<String, Object> batchOrderPrintSend(String orderPrintIds,
			String topSession) throws DataAccessException {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> failedMap = new HashMap<String, String>();
		result.put("isSuccess", "true");
		result.put("failedMap", failedMap);
		String[] idsArray = null;
		if (!StringUtils.isEmpty(orderPrintIds)) {
			// 转换ids字符串为数组
			idsArray = orderPrintIds.split(",");
		}
		// 转成List
		if (idsArray != null) {

			String txLogisticId;
			String mailNo;
			String clientId;
			String type;
			boolean isSend; // 1为true,0为false; 当为true的时候推送面单号;
			boolean isPrint; // 1为true,0为false; true 表示打印面单
			for (int i = 0; idsArray != null && i < idsArray.length; i++) {

				OrderPrint orderPrint = (OrderPrint) orderPrintDao
						.getOrderPrint(Integer.parseInt(idsArray[i]));

				// 同步到淘宝
				if (StringUtils.isEmpty(orderPrint.getClientId())
						|| StringUtils.equals("TAOBAO", orderPrint
								.getClientId().toUpperCase())) {
					try {
						TaobaoClient client = new DefaultTaobaoClient(
								ConfigUtilSingle.getInstance().getOFFICALURL(),
								ConfigUtilSingle.getInstance().getTOP_APPKEY(),
								ConfigUtilSingle.getInstance().getTOP_SECRET());

						LogisticsOfflineSendRequest req = new LogisticsOfflineSendRequest();
						req.setTid(Long.parseLong(orderPrint.getTradeNo()
								.trim()));
						req.setOutSid(orderPrint.getMailNo());
						req.setCompanyCode("YTO");
						LogisticsOfflineSendResponse response = client.execute(
								req, topSession);
						if (response.isSuccess()) {
							// 同步数据到ORDER表
							// synchronizedOrder(orderPrint);
							// 根据ID 修改批量打印状态
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("isSend", "Y");
							map.put("id", Integer.parseInt(idsArray[i]));
							updateOrderPrintIsSend(map);
						} else if ("HD06".equals(response.getSubCode())) {
							// 淘宝中已经确认发货，只需要修改修改is_send为Y 不需要更新到order表中
							// 根据ID 修改批量打印状态
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("isSend", "Y");
							map.put("id", Integer.parseInt(idsArray[i]));
							updateOrderPrintIsSend(map);
						} else {
							result.put("isSuccess", "false");
							failedMap.put(idsArray[i], response.getErrorCode()
									+ "," + response.getSubCode() + ","
									+ response.getSubMsg());
							System.out.print(response.getErrorCode() + ","
									+ response.getSubCode() + ","
									+ response.getSubMsg());
						}
					} catch (ApiException e) {
						logger.error("LogisticsOnlineSendRequest failed", e);
						failedMap.put(idsArray[i], "taobao apiException");
					}
				} else {
					// 推送给其他电商平台 (目前只有红孩子)
					txLogisticId = orderPrint.getTxLogisticId();
					mailNo = orderPrint.getMailNo();
					clientId = orderPrint.getClientId(); // "REDBABY";

					if (StringUtils.equals("0", orderPrint.getLineType())) {
						type = "online";
					} else if (StringUtils
							.equals("1", orderPrint.getLineType())) {
						type = "offline";
					} else {
						logger.error("下单类型有误，line_type 的值不为0或者1");
						return null;
					}
					isSend = Resource.getIsSend(clientId);
					if (isSend) {
						// 推送面单号
						logger.debug("推送面单号！ mailNo:" + mailNo);
						try {
							this.sendMailNoToEC(txLogisticId, mailNo, clientId,
									type);

							// 根据ID 修改批量打印状态
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("isSend", "Y");
							map.put("id", Integer.parseInt(idsArray[i]));

							updateOrderPrintIsSend(map);
						} catch (Exception e) {
							// TODO: handle exception
							logger.error("面单号推送失败！ txLogisticId:"
									+ txLogisticId + "mailNo:" + mailNo);
							logger.error("error", e);
							result.put("isSuccess", "false");
							continue;
						}
					}
					// 当点击发货的时候，线下下单
					isPrint = Resource.getIsPrint(clientId);
					if (isPrint) {
						JgWaybill jgWaybill = new JgWaybill();
						if (StringUtils.equals(orderPrint.getServiceType()
								.toString(), "16")) {
							jgWaybill.setCustomerId(orderPrint.getCustomerId());
						}
						jgWaybill.setServiceType(orderPrint.getServiceType()
								.toString());
						jgWaybill.setbAddress(orderPrint.getBuyAddress());
						jgWaybill.setbCity(orderPrint.getBuyCity());
						jgWaybill.setbDistrict(orderPrint.getBuyDistrict());
						jgWaybill.setbMobile(orderPrint.getBuyMobile());
						jgWaybill.setbName(orderPrint.getBuyName());
						jgWaybill.setbPhone(orderPrint.getBuyTelphone());
						jgWaybill.setbPostCode(orderPrint.getBuyPostcode());
						jgWaybill.setbProv(orderPrint.getBuyProv());
						jgWaybill.setMailNo(orderPrint.getMailNo());
						jgWaybill.setOrderId(orderPrint.getId());
						jgWaybill.setsAddress(orderPrint.getSaleAddress());
						jgWaybill.setsCity(orderPrint.getSaleCity());
						jgWaybill.setsProv(orderPrint.getSaleProv());
						jgWaybill.setsPhone(orderPrint.getSaleTelphone());
						jgWaybill.setsName(orderPrint.getSaleName());
						jgWaybill.setsMobile(orderPrint.getSaleMobile());
						jgWaybill.setsDistrict(orderPrint.getSaleDistrict());
						jgWaybill.setLogisticId(orderPrint.getTxLogisticId());
						jgWaybill.setLineType(orderPrint.getLineType());
						jgWaybill.setClientID(orderPrint.getClientId());

						this.jgWaybillDao.addJgWaybill(jgWaybill);
					}
				}
			}
		}

		return result;

	}

	/**
	 * 将面单号推送给电商，目前就只推送给红孩子
	 * 
	 */
	private void sendMailNoToEC(String txLogisticId, String mailNo,
			String clientId, String type) {

		// clientId为空，则不知道此订单推送给哪个厂商
		if (StringUtils.isEmpty(clientId)) {
			return;
		}
		String url = Resource.getChannel(clientId);// 推送给客户的URL地址
		String secretID = Resource.getSecretId(clientId); // 获取客户的parternId

		logger.debug("clientId:" + clientId);
		logger.debug("type:" + type);
		logger.debug("url:" + url);
		logger.debug("secretID:" + secretID);

		String xmlString = null;
		xmlString = XmlBuildProcessor.getEcUpdateXML(clientId, mailNo,
				txLogisticId);
		logger.debug(xmlString.toString());
		String dslogisticsInterface; // 明文信息
		String dsdataDigest; // 密文
		try {
			dslogisticsInterface = java.net.URLEncoder.encode(
					xmlString.toString(), XmlSender.UTF8_CHARSET);
			dsdataDigest = java.net.URLEncoder.encode(
					Md5Encryption.MD5Encode(xmlString.toString() + secretID),
					XmlSender.UTF8_CHARSET);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.error("xml加密并转码失败！");
			return;
		}

		// 将SendTask存到数据库表中.
		SendTaskToTB sendTask = new SendTaskToTB();
		// 保存为淘宝等其它电商平台的访问地址,需要根据clientID查询到对应的Url访问地址.
		sendTask.setClientId(clientId);
		sendTask.setRequestURL(url);// url预读是为了防止服务启动时缓存未加载导致url取不到为空
		sendTask.setOrderId(0);

		sendTask.setRemark("推送面单号");
		sendTask.setTxLogisticId(txLogisticId);

		String s = txLogisticId;
		s = s.substring(s.length() - 1);
		sendTask.setTaskFlagId(Integer.parseInt(s));
		sendTask.setTaskFlag(s);

		sendTask.setRequestParams("logistics_interface=" + dslogisticsInterface
				+ "&" + "data_digest=" + dsdataDigest + "&type=" + type);

		try {
			sendTaskToTBDao.addSendTaskToTB(sendTask);
		} catch (Exception e) {
			logger.error("插入任务表失败，物流号：" + txLogisticId);
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

	}

	/**
	 * 在待发货列表页点批量发货功能 把 合并，取消合并订单信息 同时同步到EC_CORE_ORDER表
	 */
	private void synchronizedOrder(OrderPrint orderPrint) {
		String mailNo = orderPrint.getMailNo();
		Double goodsValue = new Double(orderPrint.getGoodsValue());

		Order _Order = new Order();
		_Order.setId(orderPrint.getId());
		_Order.setMailNo(mailNo);
		_Order.setGoodsValue(goodsValue);
		// 同步主单
		orderService.updateMailNoValue(_Order);

		if (orderPrint.equals("Y")) {
			// 查找是否有合并的运单，如果有把合并前的分单也同步修改掉
			List<T> list = getOrderPrintByParentId(orderPrint.getId());
			if (list != null) {
				for (OrderPrint myOrderPrint : list) {
					Order newOrder = new Order();
					newOrder.setId(myOrderPrint.getId());
					_Order.setMailNo(mailNo);
					_Order.setGoodsValue(goodsValue);
					// 同步主单
					orderService.updateMailNoValue(newOrder);
				}
			}
		}
	}

	@Override
	public void updateOrderNo(Map<String, Object> param)
			throws DataAccessException {
		orderPrintDao.updateOrderNo(param);
	}
}