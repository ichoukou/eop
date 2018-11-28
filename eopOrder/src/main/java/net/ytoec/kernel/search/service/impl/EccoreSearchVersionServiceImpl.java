/**
 * 
 */
package net.ytoec.kernel.search.service.impl;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.dao.BuildSearchStatusVersionDao;
import net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao;
import net.ytoec.kernel.dao.OrderDaoVersion;
import net.ytoec.kernel.dataobject.BuildSearchStatus;
import net.ytoec.kernel.dataobject.BuildSearchStatusWeightIndex;
import net.ytoec.kernel.search.dataobject.EccoreItem;
import net.ytoec.kernel.search.dto.MailObjectDTO;
import net.ytoec.kernel.search.service.EccoreSearchVersionService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyong
 */
@Service
@Transactional
@SuppressWarnings("all")
public class EccoreSearchVersionServiceImpl implements EccoreSearchVersionService {

    protected final Logger              log = LoggerFactory.getLogger(EccoreSearchVersionServiceImpl.class);
    SimpleDateFormat holdTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Inject
    private OrderDaoVersion<MailObjectDTO>   orderDaoVersion;

    @Inject
    private BuildSearchStatusVersionDao<BuildSearchStatus> buildSearchStatusVersionDao;
    
    @Inject
	private BuildSearchStatusWeightIndexDao<BuildSearchStatusWeightIndex> buildSearchStatusWeightIndexDao;
    
    public CommonsHttpSolrServer getSolrServer(String solrUrl) throws MalformedURLException {
        // 远程服务
        return new CommonsHttpSolrServer(solrUrl);
    }

    @Override
    public void buildPartStatusWeightData(String solrUrl, Integer limit) {
        try {
            CommonsHttpSolrServer server = getSolrServer(solrUrl);
            
            log.error("数据库查询数据， 始。。。。。。。");
            long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = simpleDateFormat.format(new Date());
            String endDate = simpleDateFormat.format(DateUtil.getDateBefore(8));//往前推3天，防止积压数据没有写入solr
            
            List<BuildSearchStatusWeightIndex> mailObjectDTOs = buildSearchStatusWeightIndexDao.getResultByLimitWeight2(limit);
            log.error("EccoreSearchIndexPressureServiceImpl-----limit"+limit+" mailObjectDTOs.size()="+mailObjectDTOs.size());
            
            List<EccoreItem> mailNoNull = new ArrayList<EccoreItem>();
            List<EccoreItem> mailNoChange = new ArrayList<EccoreItem>();
            List<EccoreItem> statusChange = new ArrayList<EccoreItem>();

            List<String> delItem = new ArrayList<String>();

            MailObjectDTO mailObjectDTO = new MailObjectDTO();
            String status = "0";

            BuildSearchStatusWeightIndex buildSearch = new BuildSearchStatusWeightIndex();

			String dateStr = "2011-11-19";
			 
			Date originalDate = simpleDateFormat.parse(dateStr);
			int minId = 0, maxId = 0;
			for (int j = 0; j < mailObjectDTOs.size(); j++) {
				buildSearch = mailObjectDTOs.get(j);
				if (j == 0) {
					minId = buildSearch.getId();
					maxId = buildSearch.getId();
				} else {
					if (buildSearch.getId() > maxId) {
						maxId = buildSearch.getId();
					}
					if (buildSearch.getId() < minId) {
						minId = buildSearch.getId();
					}
				}
				EccoreItem bean = new EccoreItem();
				bean.setId(buildSearch.getOrderId());
				if (buildSearch.getStatus() != null)
					status = buildSearch.getStatus();
				if (StringUtils.equals(status, StatusEnum.CREATE.getValue()
						.toString())) {
					status = StatusEnum.CREATE.getName();
				}
				if (StatusEnum.isExitEnum(status) == false)
					continue;

				bean.setNumStatus(StatusEnum.valueOf(status).getValue());

				if (StringUtils.isEmpty(buildSearch.getMailNo())) {
					// 运单号为空 solr不允许搜索此数据
					bean.setMailNo("YTO" + buildSearch.getId());
					bean.setIsDispaly(new Integer(1).shortValue());
				} else {
					// 支持
					bean.setMailNo(buildSearch.getMailNo());
					bean.setIsDispaly(new Integer(0).shortValue());
				}

				bean.setNumCreateTime((int) getNumDay(buildSearch
						.getPartitiondate()));
				bean.setCreateTime(buildSearch.getCreateTime());
				bean.setAcceptTime(buildSearch.getAcceptTime());
				bean.setCustomerId(buildSearch.getCustomerId());
				bean.setName(buildSearch.getName());

				bean.setPhone(new String[] { buildSearch.getMobile(),
						buildSearch.getPhone() });

				bean.setDisplayPhone(buildSearch.getMobile());
				if (StringUtils.isEmpty(bean.getDisplayPhone())) {
					bean.setDisplayPhone(buildSearch.getPhone());
				}
				bean.setNumProv(Integer.valueOf(buildSearch.getNumProv()));

				bean.setNumCity(Resource.getCodeByName(buildSearch.getCity()));

				bean.setNumDistrict(Resource.getCodeByName(buildSearch
						.getDistrict()));
				bean.setAddress(buildSearch.getAddress());
				bean.setWeight(buildSearch.getWeight());
				/**** Added by Johnson,2013-08-06 *****/
				if (buildSearch.getOrderType() == 0) {
					bean.setOrderType(new Integer(0).shortValue());
				} else {
					bean.setOrderType(new Integer(buildSearch.getOrderType())
							.shortValue());
				}
				if (buildSearch.getLineType() == null) { // 如果line_type为null,默认设置为线下订单:1
					bean.setLineType(new Integer(1).shortValue());
				} else {
					bean.setLineType(new Integer(0).shortValue());
				}
				/**************************************/
				bean.setTrimFreight((float) buildSearch.getTrimFreight());
				if (buildSearch.getFreightType() != null) {
					bean.setFreightType(new Integer(1).shortValue());
				} else {
					bean.setFreightType(new Integer(0).shortValue());
				}

				bean.setCityF(buildSearch.getCityF());
				bean.setNumProvF(Resource.getCodeByName(buildSearch.getProvF()));
				
				if (!StringUtils.isEmpty(buildSearch.getHoldTime())) {
					// bean.setHoldTime(Integer.parseInt(buildSearch.getHoldTime()));
					String holdTime = buildSearch.getHoldTime().trim();
					log.info("方法名：buildUpdatePartEccoreData; holdTime :"+buildSearch.getHoldTime() + "orderID:"+buildSearch.getOrderId());
					if (holdTime.length() == 19
							|| holdTime.length() == 10) {
						//log.error("holdTime=============>"+holdTime);
						//holdTime = holdTime.substring(0, 10);
						Date dateTemp = holdTimeFormat.parse(holdTime);
						//log.error("dateTemp=============>"+dateTemp);
						long dayTemp = getNumDay(dateTemp);
						//long dayTemp = getNumDayChange(dateTemp);
						//log.error("dayTemp=============>"+dayTemp);
						bean.setHoldTime((int)dayTemp);
					}else{
						bean.setHoldTime((int) getNumDay(holdTimeFormat
								.parse(holdTimeFormat.format(new Date()))));
					}
					

					// bean.setHoldTime(665);
				}
				bean.setMailNoAndCustomerId(bean.getMailNo()
						+ buildSearch.getCustomerId());

				bean.setTxLogisticId(buildSearch.getTxLogisticId());

				if (StringUtils.equals(buildSearch.getBuildTask(), "1")) {
					// add mailNo
					mailNoNull.add(bean);
					delItem.add(bean.getMailNo() + buildSearch.getCustomerId());
				} else if (StringUtils.equals(buildSearch.getBuildTask(), "2")) {
					// 更新mailNo
					mailNoChange.add(bean);
					delItem.add(buildSearch.getOldMailNo()
							+ buildSearch.getCustomerId());
				} else if (StringUtils.equals(buildSearch.getBuildTask(), "3")) {
					// 删除订单
					delItem.add(bean.getMailNo() + buildSearch.getCustomerId());
				} else {
					// 新增订单
					statusChange.add(bean);
				}

			}
            log.error("EccoreSearchVersionServiceImpl bean set complete!");
            if (delItem.size() > 0) {
                server.deleteById(delItem);
                server.commit();
                log.error("delItem:" + delItem.size());
                delItem.clear();
            }
            if (mailNoNull.size() > 0) {
                server.addBeans(mailNoNull);
                server.commit();
                log.error("mailNonULL:" + mailNoNull.size());
                mailNoNull.clear();

            }
            if (statusChange.size() > 0) {
                server.addBeans(statusChange);
                server.commit();
                log.error("statusChange:" + statusChange.size());
                statusChange.clear();
            }
            if (mailNoChange.size() > 0) {
                server.addBeans(mailNoChange);
                server.commit();
                log.error("mailNoChange:" + mailNoChange.size());
                mailNoChange.clear();
            }
            
            limit = mailObjectDTOs.size();
            if(limit>0){
            	buildSearchStatusWeightIndexDao.removeBuildWeightByLimit2(minId,maxId);
            }
            mailObjectDTOs.clear();
            
            log.error("EccoreSearchVersionServiceImpl-----limit" + limit + " mailObjectDTOs.size()=" + mailObjectDTOs.size() + "build到solr的时间：" + (Calendar.getInstance().getTimeInMillis() - time));

        } catch (Exception e) {
            log.error("build error", e);
        }

        
    }
    
    private  long getNumDay(String ymd) throws ParseException {
        if (StringUtils.isEmpty(ymd)) {           
            return 0;
        }
        Date date = DateUtils.parseDate(ymd, new String[] { "yyyy-MM-dd" });
        return getNumDay(date);
    }
    
    private static long getNumDay(Date ymd) throws ParseException {
        if (ymd==null) {
            return 0;
        }
        Date originalDate = DateUtils.parseDate("2011-11-19", new String[] {"yyyy-MM-dd" });
        long result = (ymd.getTime() - originalDate.getTime()) / (1000 * 60 * 60 * 24);
        if(result<0)
        	result = 0;
        return result;
    }
}
