package net.ytoec.kernel.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dao.QuestionnaireDao;
import net.ytoec.kernel.dao.SMSBuyersDao;
import net.ytoec.kernel.dao.SMSBuyersGradeDao;
import net.ytoec.kernel.dao.TaobaoTaskDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.SMSBuyers;
import net.ytoec.kernel.dataobject.SMSBuyersGrade;
import net.ytoec.kernel.dataobject.TaobaoTask;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.SMSBuyersService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Task;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.parser.json.ObjectJsonParser;
import com.taobao.api.internal.util.AtsUtils;
import com.taobao.api.request.TopatsResultGetRequest;
import com.taobao.api.request.TopatsTradesSoldGetRequest;
import com.taobao.api.response.TopatsResultGetResponse;
import com.taobao.api.response.TopatsTradesSoldGetResponse;
import com.taobao.api.response.TradeFullinfoGetResponse;

@Service
@Transactional
@SuppressWarnings("all")
public class SmsBuyersServiceImpl implements SMSBuyersService {

    private static final Logger           logger     = LoggerFactory.getLogger(SmsBuyersServiceImpl.class);

    @Inject
    private SMSBuyersDao            smsBuyersdao;

    @Inject
    private SMSBuyersGradeDao       smsBuyersGradeDao;

    @Inject
    private TaobaoTaskDao           taobaoTaskDao;

    @Inject
    private OrderDao                orderDao;

    @Inject
    private UserDao                 userDao;

    @Inject
    private QuestionnaireDao        questionaireDao;

    @Inject
    private EccoreSearchService     eccoreSearchService;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    private static final String req = "\\\\";
    

    @Override
    public void taobaoDataToDB(File taskFiles, TaobaoTask taobaoTask, String tempString) throws Exception   {

    	ObjectJsonParser<TradeFullinfoGetResponse> parser = new ObjectJsonParser<TradeFullinfoGetResponse>(TradeFullinfoGetResponse.class);

        TradeFullinfoGetResponse rsp = null;
        try {
            rsp = parser.parse(tempString);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        Trade trade = rsp.getTrade();

        logger.info("SmsBuyersServiceImpl ==> begin smsBuyersdao.getSMSBuyersByBuyerAccount(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", startTime = "+System.currentTimeMillis());
        SMSBuyers buyers = smsBuyersdao.getSMSBuyersByBuyerAccount(trade.getBuyerNick(),
                                                                   taobaoTask.getUserId());
        logger.info("SmsBuyersServiceImpl ==> end smsBuyersdao.getSMSBuyersByBuyerAccount(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", endTime = "+System.currentTimeMillis());
        
        String mobile = trade.getReceiverMobile();

        if (buyers != null) {
            logger.info("SmsBuyersServiceImpl ==> begin smsBuyersdao.updateInfoByTaobao(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", startTime = "+System.currentTimeMillis());
        	smsBuyersdao.updateInfoByTaobao(Double.parseDouble(trade.getPayment() + ""),
                                            trade.getCreated(), buyers.getId());
            logger.info("SmsBuyersServiceImpl ==> end smsBuyersdao.updateInfoByTaobao(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", endTime = "+System.currentTimeMillis());
        } else {
            buyers = new SMSBuyers();
            // 淘宝号
            buyers.setBuyerAccount(trade.getBuyerNick());

            // 此用户的消费总额
            buyers.setTotalTradeAmount(Double.parseDouble(trade.getPayment() + ""));
            buyers.setTotalTradeCount(1);
            buyers.setTheLastTradeTime(trade.getCreated());

            // 收件人电话
            buyers.setReceiverName(trade.getReceiverName());
            buyers.setReceiverMobile(mobile);
            /**
             * 收件人地址
             */
            buyers.setReceiverProvince(trade.getReceiverState()); // 省
            buyers.setReceiverCity(trade.getReceiverCity()); // 市
            buyers.setReceiverDistrict(trade.getReceiverDistrict()); // 区
            buyers.setReceiverAddress(trade.getReceiverAddress()); // 具体街道
            buyers.setReceiverPostcode(trade.getReceiverZip()); // 邮编

            buyers.setSourceStatus("TAOBAO");
            buyers.setUserId(taobaoTask.getUserId());

            logger.info("SmsBuyersServiceImpl ==> begin smsBuyersdao.addSMSBuyers(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", startTime = "+System.currentTimeMillis());
            // 保存进入数据库
            smsBuyersdao.addSMSBuyers(buyers);
            logger.info("SmsBuyersServiceImpl ==> end smsBuyersdao.addSMSBuyers(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", endTime = "+System.currentTimeMillis());
        }

        // 获取当前用户的customerId
        String customerId = "";
        User currentUser = (User) userDao.getUserById(taobaoTask.getUserId());
        if (currentUser != null) {
            customerId = currentUser.getTaobaoEncodeKey();
        }
        if (StringUtils.isBlank(mobile)) {
            mobile = trade.getReceiverPhone();
        }
        List<String> orderIds = new ArrayList<String>();
        
        if (StringUtils.isNotBlank(mobile)) {
        	 Pagination<EccoreSearchResultDTO> searchPage = new Pagination<EccoreSearchResultDTO>(1, 100);
             Map<String, String> searchParams = new HashMap<String, String>();
             Date date = new Date();
             searchParams.put("startDate", dateFormat.format(DateUtil.getDateBefore(date, 30)));
             searchParams.put("endDate", dateFormat.format(date));
             if (StringUtils.startsWith(mobile, "-")) {
                 mobile=mobile.replaceFirst("-", req + "-");
             }
             if (StringUtils.startsWith(mobile, "+")) {
                 mobile=mobile.replaceFirst("+", req + "+");
             }
             if (StringUtils.contains(mobile, "(")) {
                 mobile=mobile.replace("(", req + "(");
             }
             if (StringUtils.contains(mobile, "}")) {
                 mobile=mobile.replace(")", req + ")");
             }
             if (StringUtils.contains(mobile, "{")) {
                 mobile=mobile.replace("{", req + "{");
             }
             if (StringUtils.contains(mobile, "}")) {
                 mobile=mobile.replace("}", req + "}");
             }
             searchParams.put("mobile", mobile);
             searchPage.setParams(searchParams);
             logger.info("SmsBuyersServiceImpl ==> begin eccoreSearchService.searchOrderByMobile(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", startTime = "+System.currentTimeMillis());
             eccoreSearchService.searchOrderByMobile(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),
                                                     searchPage);
             logger.info("SmsBuyersServiceImpl ==> end eccoreSearchService.searchOrderByMobile(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", endTime = "+System.currentTimeMillis());
             List<EccoreSearchResultDTO> eccoreSearchResultDTOs = searchPage.getRecords();
             for (EccoreSearchResultDTO eccoreSearchResultDTO : eccoreSearchResultDTOs) {
                 orderIds.add(eccoreSearchResultDTO.getTxLogisticId());
             }
        }
        // 如果solr没有订单数据，即认为订单未进入易通，无需更新
        if (!orderIds.isEmpty()&& StringUtils.isNotBlank(trade.getBuyerNick())) {
        	 List<String> quesIds = new ArrayList<String>();
        	 Map<String, Object> params = new HashMap<String, Object>();
             params.put("buyerNick", trade.getBuyerNick());
             params.put("buyerMobile", trade.getReceiverMobile());
             params.put("buyerPhone", trade.getReceiverPhone());
             params.put("customerId", customerId);
             
             /** Added by Johnson,2013-08-07 **/
             List list = questionaireDao.selectQuesByCustomerId(params);
             logger.info("SmsBuyersServiceImpl ==> list.size ="+list.size()+","+taobaoTask.getUserId()+","+trade.getBuyerNick());
             if(list != null && list.size() > 0){
            	 for(int i=0;i<list.size();i++){
            		 Questionnaire ques = (Questionnaire)list.get(i);
                	 quesIds.add(ques.getId().toString());
                	 logger.info("Questionnaire id = "+ ques.getId());
            	 }
            	 params.put("quesIds", quesIds);
                 // 更新淘宝号到问题件表中
                 logger.info("SmsBuyersServiceImpl ==> begin updateBuyerNickToQuestionaire(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", startTime = "+System.currentTimeMillis());
                 updateBuyerNickToQuestionaire(params);
                 logger.info("SmsBuyersServiceImpl ==> end updateBuyerNickToQuestionaire(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", startTime = "+System.currentTimeMillis());
             }
             /*******************************/
      
            params.put("orderIds", orderIds);

            logger.info("SmsBuyersServiceImpl ======> begin updateBuyerNickToOrder(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", startTime = "+System.currentTimeMillis());
            // 更新淘宝号到订单表中
            updateBuyerNickToOrder(params);
            logger.info("SmsBuyersServiceImpl ======> end updateBuyerNickToOrder(),"+taobaoTask.getUserId()+","+trade.getBuyerNick()+","+taobaoTask.getTaskId()+", endTime = "+System.currentTimeMillis());
        }
       
    }
    
    

    @Override
    public boolean downZipFromTaobao() throws DataAccessException {
        logger.error("淘宝获取会员信息ZIP包下载始。。。。");
        try {
            List<TaobaoTask> taskLists = taobaoTaskDao.getTaobaoTaskByUserId("0", null);
            TaobaoTask taskTemp = null;
            if (taskLists != null && taskLists.size() > 0) {
                for (int i = 0; i < taskLists.size(); i++) {
                	taskTemp = taskLists.get(i);
                    if (StringUtils.equals(taskTemp.getFlag(), "0")) {
                        TaobaoClient client = new DefaultTaobaoClient(ConfigUtilSingle.getInstance().getOFFICALURL(),
                                                                      ConfigUtilSingle.getInstance().getTOP_APPKEY(),
                                                                      ConfigUtilSingle.getInstance().getTOP_SECRET());
                        TopatsResultGetRequest req = new TopatsResultGetRequest();
                        req.setTaskId(Long.parseLong(taskTemp.getTaskId() + ""));
                        TopatsResultGetResponse response = client.execute(req);
                        if (response.isSuccess()) {
                            Task task = response.getTask();
                            if (StringUtils.equals(task.getStatus(), "done")) {
                                // 下载同步的订单信息的URL地址
                                String downUrl = task.getDownloadUrl();
                                File taskFile = AtsUtils.download(downUrl,
                                                                  new File(
                                                                           ConfigUtilSingle.getInstance().getTAOBAO_MEMBER_ZIP_URL()));
                                File resultFile = null;
                                try {
                                    resultFile = new File(ConfigUtilSingle.getInstance().getTAOBAO_MEMBER_UNZIP_URL());
                                } catch (Exception e) {
                                    logger.error(ConfigUtilSingle.getInstance().getTAOBAO_MEMBER_UNZIP_URL() + "不存在！",
                                                 e);
                                }
                                AtsUtils.unzip(taskFile, resultFile);

                                // 更新下载状态为已下载 即flag = 1；
                                TaobaoTask taobaoTask = (TaobaoTask) taobaoTaskDao.getTaobaoTaskByTaskId(taskTemp.getTaskId());
                                taobaoTask.setFlag("1");
                                taobaoTaskDao.updateTaobaoTask(taobaoTask);

                                logger.error("淘宝获取会员信息ZIP包下载结束。。。。");
                            }
                        } else {
                        	// 异常处理，例如此卖家不存在店铺等错误！直接更新任务为3；
                            // 更新错误状态 即flag = 3；
                            TaobaoTask taobaoTask = (TaobaoTask) taobaoTaskDao.getTaobaoTaskByTaskId(taskTemp.getTaskId());
                            taobaoTask.setFlag("3");
                            taobaoTaskDao.updateTaobaoTask(taobaoTask);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("从淘宝下载会员信息ZIP包下载时出错", e);
            return false;
        }
        return true;
    }

    /**
     * 更新淘宝号到问题件表中
     * 
     * @param mobile
     * @param buyerNick
     */
    private void updateBuyerNickToQuestionaire(Map<String, Object> params) {
//        questionaireDao.updateBuyerNickToQuestionaire(params);
    	questionaireDao.updateQuesById(params);
    }

    /**
     * 更新淘宝号到订单表中
     * 
     * @param mobile
     * @param buyerNick
     */
    private void updateBuyerNickToOrder(Map<String, Object> params) {
        // orderDao.updateBuyerNickToOrder(params);
        orderDao.updateOrderByIds(params);
    }

    @Override
    public boolean createTaskByTaobao(Integer userId, String topSession) throws DataAccessException {
        try {
            if (StringUtils.equals(DateUtil.toShortDay(DateUtil.getDateBefore(new Date(), 1)), getEndTimeByTask(userId))) {
                return false;
            }
            // 获取淘宝客户端
            TaobaoClient client = new DefaultTaobaoClient(ConfigUtilSingle.getInstance().getOFFICALURL(),
                                                          ConfigUtilSingle.getInstance().getTOP_APPKEY(),
                                                          ConfigUtilSingle.getInstance().getTOP_SECRET());
            TopatsTradesSoldGetRequest req = new TopatsTradesSoldGetRequest();
            req.setFields("tid,oid,seller_nick,buyer_nick,receiver_mobile,receiver_phone,receiver_name,payment,price,num_iid,seller_rate,buyer_rate,created,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip");
            req.setStartTime(getEndTimeByTask(userId));
            req.setEndTime(DateUtil.toShortDay(DateUtil.getDateBefore(new Date(), 1)));
            TopatsTradesSoldGetResponse rsp = client.execute(req, topSession);
            if (rsp.isSuccess()) {
                // 任务记录插入到数据库
                TaobaoTask taobaoTask = new TaobaoTask();
                taobaoTask.setTaskId(Integer.parseInt(rsp.getTask().getTaskId() + ""));
                taobaoTask.setStatus(rsp.getTask().getStatus());
                taobaoTask.setStatus("status");
                taobaoTask.setStartDate(getEndTimeByTask(userId));
                taobaoTask.setEndDate(DateUtil.toShortDay(DateUtil.getDateBefore(new Date(), 1)));
                taobaoTask.setFlag("0");
                taobaoTask.setUserId(userId);
                taobaoTaskDao.addTaobaoTask(taobaoTask);
            }
        } catch (ApiException e) {
            logger.error("TopatsTradesSoldGetRequest failed", e);
            return false;
        }
        return true;
    }

    /**
     * 获取当前用户淘宝订单任务获取的最后时间
     */
    private String getEndTimeByTask(Integer userId) {
        List<TaobaoTask> taskLists = new ArrayList<TaobaoTask>();
        taskLists = taobaoTaskDao.getTaobaoTaskByUserId("0", userId);
        // 此用户以前获取过订单a
        if (taskLists != null && taskLists.size() > 0) {
            return taskLists.get(taskLists.size() - 1).getEndDate();
        }
        // 第一次从淘宝获取订单 获取一个月的订单
        return DateUtil.toShortDay(DateUtil.getDateBefore(new Date(), 30));
    }

    @Override
    public List<SMSBuyers> getSMSBuyersByPamams(Map<String, Object> params) {
        return smsBuyersdao.getSMSBuyersByPamams(getParams(params));
    }

    @Override
    public Integer getSMSBuyersCountByPamams(Map<String, Object> params) {
        return smsBuyersdao.getSMSBuyersCountByPamams(getParams(params));
    }

    /**
     * 构造参数
     * 
     * @param params
     * @return
     */
    private Map<String, Object> getParams(Map<String, Object> params) {
        if (params.get("userIds") != null && ((List<Integer>) params.get("userIds")).size() > 0) {
            SMSBuyersGrade smsBuyersGrade = (SMSBuyersGrade) smsBuyersGradeDao.getSMSBuyersGradeByUserId(((List<Integer>) params.get("userIds")).get(0));
            if (smsBuyersGrade != null && params.get("userGrade") != null && !params.get("userGrade").equals("")) {
                String userGrade = params.get("userGrade").toString().trim();
                //设置各级会员查询条件
                setUserGradeParams(smsBuyersGrade,userGrade,params);
            }
        }
        return params;
    }

    @Override
    public Integer getCountByUserGrade(Map<String, Object> params) {
        if (params == null) {
            logger.error("params参数不能为空!");
            return 0;
        }

        // 获取 userIds
        List<Integer> userIds = (List<Integer>) params.get("userIds");

        if (userIds == null) {
            logger.error("userIds参数不能为空!");
            return 0;
        }

        // 根据用户主帐号ID,获取等级对象
        SMSBuyersGrade smsBuyersGrade = null;
        if (userIds != null && userIds.size() > 0) {
            smsBuyersGrade = (SMSBuyersGrade) smsBuyersGradeDao.getSMSBuyersGradeByUserId(userIds.get(0));
        }

        // 获取 userGrade
        String userGrade = (String) params.get("userGrade");
        if (StringUtils.isEmpty(userGrade)) {
            logger.error("userGrade参数不能为空!");
            return 0;
        }

        if (smsBuyersGrade == null) {
            if (StringUtils.equals(userGrade, "0") || StringUtils.equals(userGrade, "4")) {
                return smsBuyersdao.getCountByUserGrade(params);
            } else {
                return 0;
            }

        }
        //设置各级会员查询条件
        setUserGradeParams(smsBuyersGrade,userGrade,params);

        return smsBuyersdao.getCountByUserGrade(params);
    }
    
    /**
     * 设置各级会员查询条件
     * @param smsBuyersGrade
     * @param userGrade  0：普通会员 1：高级会员 2：VIP会员：3：至尊VIP会员
     * @param params 参数集合
     * @return 
     */
    private void setUserGradeParams(SMSBuyersGrade smsBuyersGrade,String userGrade,Map<String, Object> params) {
    	 //清空查询条件
    	 params.put("found", 0);  //将不被查询得出
    	 params.put("tradeAmountMin",null);
		 params.put("tradeCountMin", null);
	     params.put("tradeAmountMax",null);
	     params.put("tradeCountMax", null);
    	
    	//高级会员
    	double highAccount = smsBuyersGrade.getHighAccount();
    	int highCount = smsBuyersGrade.getHighCount();
    	
    	//VIP会员
    	double vipAccount = smsBuyersGrade.getVipAccount();
    	int vipCount = smsBuyersGrade.getVipCount();
    	
    	//至尊VIP会员
    	double vipHighAccount = smsBuyersGrade.getVipHighAccount();
    	int vipHighCount = smsBuyersGrade.getVipHighCount();
    	
    	if("0".equals(userGrade)) {       //普通会员
    		if(highAccount>0) {          //以高级会员作为准则                                    交易额
    			 params.put("tradeAmountMax",highAccount);
    		}else {     //如果高级会员设置不存在,普通会员转向判断,VIP会员标准
    			if(vipAccount>0) {  //以VIP会员作为准则
   				 params.put("tradeAmountMax",vipAccount);
    			}else {  //VIP会员设置不存在,普通VIP会员转向判断,至尊VIP会员标准
    				if(vipHighAccount>0) { //以至尊VIP会员标准
   					 params.put("tradeAmountMax",vipHighAccount);
    				}else {
    					return;   //如果未设置,表示所有的会员为普通会员,无需条件
    				}
    			}
    		}	
    		if(highCount>0) {           //以高级会员作为准则                                        交易量
    	         params.put("tradeCountMax", highCount);
    		}else {     //如果高级会员设置不存在,普通会员转向判断,VIP会员标准
    			if(vipCount>0) {  //以VIP会员作为准则
        	         params.put("tradeCountMax", vipCount);
    			}else {  //VIP会员设置不存在,普通VIP会员转向判断,至尊VIP会员标准
    				if(vipHighCount>0) { //以至尊VIP会员标准
    	    	         params.put("tradeCountMax", vipHighCount);
    				}else {
    					return;   //如果未设置,表示所有的会员为普通会员,无需条件
    				}
    			}
    		}
    	}else if("1".equals(userGrade)) {  //高级会员
    		if(highAccount>0||highCount>0) {  //存在,表示有高级会员,不存在,表示未定义高级会员
    			params.put("tradeAmountMin", highAccount);
    			params.put("tradeCountMin", highCount);
    			
    			if(vipAccount>0) {
    				params.put("tradeAmountMax",vipAccount);
    			}else if(vipHighAccount>0){
    				params.put("tradeAmountMax",vipHighAccount);
    			}
    			
    			if(vipCount>0) {
    				params.put("tradeCountMax", vipCount);
    			}else if(vipHighCount>0){
    				params.put("tradeCountMax", vipHighCount);
    			}
    		}else {
    			 params.put("found", -1);  //将不被查询得出
    		}
    	}else if("2".equals(userGrade)) {  //VIP会员
    		if(vipAccount>0||vipCount>0) {  //存在,表示有。不存在,表示未定义
    			params.put("tradeAmountMin", vipAccount);
    			params.put("tradeCountMin", vipCount);
    	        params.put("tradeAmountMax", vipHighAccount);
    	        params.put("tradeCountMax", vipHighCount);
    		}else {
    			 params.put("found", -1);  //将不被查询得出
    		}	
    	}else if("3".equals(userGrade)) {  //至尊VIP会员
    		if(vipHighAccount>0||vipHighCount>0) {  //存在,表示有。不存在,表示未定义
    			params.put("tradeAmountMin", vipHighAccount);
    	        params.put("tradeCountMin", vipHighCount);
    		}else {
    			 params.put("found", -1);  //将不被查询得出
    		}
    	}
    }
    
    @Override
    public boolean delBatchSMSBuyers(List<Integer> idLists) {
        return smsBuyersdao.delBatchSMSBuyers(idLists);
    }

    @Override
    public boolean addSMSBuyers(SMSBuyers buyers) {

        boolean flag = false;
        if (buyers == null) {
            logger.error("##添加卖家联系人时传入参数为空！");
            return false;
        }
        return smsBuyersdao.addSMSBuyers(buyers);
    }

    @Override
    public boolean delSMSBuyersById(Integer id) {
        if ("".equals(id) || id == null) {
            logger.error("##删除卖家联系人时传入参数为空！");
            return false;
        }
        return smsBuyersdao.delSMSBuyersById(id);
    }

    @Override
    public boolean editSMSBuyers(SMSBuyers buyers) {

        if (buyers == null) {
            logger.error("##修改卖家联系人时传入参数为空！");
            return false;
        }
        return smsBuyersdao.editSMSBuyers(buyers);
    }

    @Override
    public SMSBuyers getSMSBuyersById(Integer id) throws Exception {
        if (id == null) {
            throw new Exception("##根据ID查询卖家联系人时传入参数为空！");
        }
        return (SMSBuyers) smsBuyersdao.getSMSBuyersById(id);
    }

    @Override
    public boolean addSMSBuyersList(List<SMSBuyers> list) {
        if (null == list || list.size() == 0) {
            logger.error("上传数据为空!");
        }
        return smsBuyersdao.addSMSBuyersList(list);
    }

    @Override
    public SMSBuyers getSMSBuyersByBuyerAccount(String buyerAccount, Integer userId) {
        if (buyerAccount == null) {
            logger.error("买家的登录帐号为空!");
        }
        return smsBuyersdao.getSMSBuyersByBuyerAccount(buyerAccount, userId);
    }

    @Override
    public boolean checkWW(String buyerAccount, String memberId) {
        try {
            SMSBuyers buyers = smsBuyersdao.checkWW(buyerAccount);
            if (null != memberId && !("").equals(memberId)) {// 有ID
                if (null != buyers && buyers.getId() != Integer.parseInt(memberId)) {
                    return false;
                }
            } else {// 无ID
                if (null != buyers) {
                    return false;
                }
            }
        } catch (Exception e) {

        }
        return true;
    }
}
