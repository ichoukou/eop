package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.xml.WaiteSendSMSInfo;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.OrderStatusSMSService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.util.SystemHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 批量添加待发送短信到短信表
 * create by wangmindong
 * create timer 2013-04-08
 */
public class AddWaitInfo2WaitSMSTimer extends TimerTask {

	private static Logger logger = LoggerFactory.getLogger(AddWaitInfo2WaitSMSTimer.class);
	@Inject
	private SMSPortService smsPortService;
	@Inject
	private SMSServiceService smsServiceService;
	@Inject
	private SMSInfoService smsInfoService;
	@Inject
	private TraderInfoService<TraderInfo> traderInfoService;
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private OrderStatusSMSService orderStatusSMSService;

	/** 线程启动标识 */
	private Integer limit = 2000;
	private Integer flag;
	private String tomcatId;
	private static boolean isRunning = false;
	private MailService<Mail> mailService;
	private String receiver = "851713352@qq.com";
	
	/**
	 * 邮件模版
	 */
	private static String mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
			+ "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
			+ "<DIV> ${mailContent}</DIV>"
			+ "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
			+ "<BR>--------------------------------------------------------------------------------------"
			+ "</BODY></HTML>";

	@Override
	public void run() {
		if (!isRunning) {
			try {
				List<WaiteSendSMSInfo> order2SmsInfoList = new ArrayList<WaiteSendSMSInfo>();
				isRunning = true;
				logger.error("=============处理ec_core_orderstatus_sms表数据开始=======");
				long t1=System.currentTimeMillis();
				
				initSearchParams();//初始化查询flag
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
				String dateString=dateFormat.format(new Date());
				Date date=java.sql.Date.valueOf(dateString);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("flag", flag);
				map.put("date", date);
				map.put("limit", limit);
				logger.error("====查询flag为"+flag+"的数据，查询日期===="+date);
				
				//查询将要存入信息表中的订单信息
				
				order2SmsInfoList=orderStatusSMSService.batchSearchOrdForSMS(map);
				logger.error("查询耗时"+(System.currentTimeMillis()-t1));
				
				/**添加发送记录到信息表中**/
				if(order2SmsInfoList.size()>0){
					isSendSMStoSeller(order2SmsInfoList);
				}
				//存入后删除订单信息
				orderStatusSMSService.batchDeleteOrd(order2SmsInfoList);
				logger.error("=============共删除"+order2SmsInfoList.size()+"条数据");
				long t2=System.currentTimeMillis();
				logger.error("=============处理ec_core_orderstatus_sms表数据结束,用时为"+(t2-t1));
			} catch (Exception e) {
				isRunning = false;
				logger.error("添加代发短信失败!", e);
				StackTraceElement ex = e.getStackTrace()[0];
				Mail mail = new Mail();
				mail.setSubject("AddWaitInfo2WaitSMSTimer出现异常！");
				mail.setSendToMail(this.receiver);
				mail.setContent(mailContentTemp.replace(
						"${mailContent}",
						"Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
								+ ex.getLineNumber()));
				mailService.sendMail(mail);
			} finally {
				isRunning = false;
			}
		}
	}
	/**
	 * 如果揽收，派送扫描，签收 可能需要给买家发短信
	 * 短信方法处理
	 * @author wmd
	 * @throws Exception 
	 */
	private void isSendSMStoSeller(List<WaiteSendSMSInfo> updateInfoList) throws Exception {
	// TODO Auto-generated method stub
	for(int i = 0 ; i < updateInfoList.size();i++){
		String status = updateInfoList.get(i).getStatus();   //状态
        String mailNo = updateInfoList.get(i).getMailNo();   //运单号
        String txLogisticId = updateInfoList.get(i).getTxLogisticID();
        //通过物流号，获取订单
        Order order = orderService.getOrderByLogisticId(txLogisticId);
        if (order == null || order.getId() == null) {
        	continue ;
        }
      //通过订单的customer_id 获取订单对应的用户
        User user = Resource.getUserByCustomerId(order.getCustomerId());
        if (user == null || user.getId() == null) {
        	continue ;
        }
        String childType=user.getChildType();
        Map<String, Object> map = new HashMap<String, Object>();
        
        if (StringUtils.equalsIgnoreCase("B", childType)) {
		    if(StringUtils.isNotBlank(user.getUserSource())) {        //若：B:分仓 C:企业  的平台ID为NULL ,则使用自身ID,注意：此情况实际属于,错误数据
				 map.put("userId",Integer.parseInt(user.getUserSource()));
			}else {
				 map.put("userId", user.getId());
			}   
        }else  {
        	Integer parentId = user.getParentId();
			if(parentId!=null) {    //主ID,或子ID
			  map.put("userId", parentId);
			}else {
			  map.put("userId", user.getId());
			}
        }
        
        map.put("status", StringUtils.upperCase(status));
        map.put("mailNo", mailNo);
        map.put("txLogisticId", txLogisticId);
        map.put("createUserId", user.getId());
        //判断订单用户，是否有短信可发。  且判断服务是否开通
       if(smsPortService.supplyStatusOpenUp(user.getId())&&smsServiceService.isOpeanService(user.getId(), status)){
            TraderInfo traderInfo = traderInfoService.getTraderInfoByIdAndByTraderType(order.getId(),1);//1为收件人
            String buyMobile = traderInfo.getMobile();//收件人手机号
            String buyName = traderInfo.getName();//收件人姓名
            
           logger.error(" smsInfoService.saveInfoToQueue(map);" + map.toString());
            if (!StringUtils.isEmpty(buyMobile)) {
                map.put("buyMobile", buyMobile);
                map.put("buyName", buyName);
                logger.error(" START  --->   smsInfoService.saveInfoToQueue(map);" + map.toString());
                smsInfoService.saveInfoToQueue(map); 
                logger.error(" END --> smsInfoService.saveInfoToQueue(map);");
            }
        }
	}
	}
	private void initSearchParams() {
		// TODO Auto-generated method stub

		tomcatId = SystemHelper.TOMCAT_ID;
		if (StringUtils.isEmpty(tomcatId)) {
			logger.error("tomcat id is Empty");
			isRunning = false;
			return;
		}
		if (!StringUtils.isNumeric(tomcatId)) {
			logger.error("tomcat id is not is Numeric");
			isRunning = false;
			return;
		}
		logger.error("tomcat id:" + tomcatId);
		if(tomcatId.equals("33")){
			flag = 0;
		}else if (tomcatId.equals("34")){
			flag = 1;
		}else if (tomcatId.equals("43")){
			flag = 2;
		}else if (tomcatId.equals("44")){
			flag = 3;
		}else if (tomcatId.equals("640")){
			flag = 4;
		}else if (tomcatId.equals("650")){
			flag = 5;
		}else if (tomcatId.equals("660")){
			flag = 6;
		}else if (tomcatId.equals("35")){
			flag = 7;
		}else if (tomcatId.equals("45")){
			flag = 8;
		}else if (tomcatId.equals("36")){
			flag = 9;
		}else if (tomcatId.equals("37")){
			flag = 10;
		}else if (tomcatId.equals("46")){
			flag = 11;
		}else if (tomcatId.equals("47")){
			flag = 12;
		}
		
	}
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public MailService<Mail> getMailService() {
		return mailService;
	}

	public void setMailService(MailService<Mail> mailService) {
		this.mailService = mailService;
	}

}
