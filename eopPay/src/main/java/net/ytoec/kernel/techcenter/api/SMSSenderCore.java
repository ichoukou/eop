package net.ytoec.kernel.techcenter.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.ytoec.kernel.common.HttpPostCore;
import net.ytoec.kernel.common.RegistexTeach;
import net.ytoec.kernel.common.TechcenterEnum;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSObjectService;
import net.ytoec.kernel.service.SMSOtherWaitService;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 短信发送
 * @author wmd
 * 2013-03-11
 *
 */
public class SMSSenderCore {

	protected static final Logger logger = LoggerFactory.getLogger(SMSSenderCore.class);
	private SMSObjectService<SMSObject> SMSObjectService = SMSSender.getSMSObjectService();//发送短信历史记录表    
	private SMSOtherWaitService<SMSObject> smsOtherWaitService=SMSSender.getSMSOtherWaitService();//待发送短信表（供其他代理商（如：本草堂）使用
    private SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private int smsFormat ; //短信发送格式
    private String sendSMSUrl = "http://58.32.246.70:8088/SMSInterface";//短信接口url
	/**
	 * 短信发送
	 * @return
	 */
	public Object searchSendSMS() {
		// 2)第二种方式,单条查询
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", TechcenterEnum.TECHCENTERFLAG.WAIT.getValue());
		map.put("limit", 100);

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour >= 8 && hour < 20) {
			logger.error("易通发送短信-------start");
			List<SMSObject> sMSObjectLis = SMSObjectService.getList(map);
			if (CollectionUtils.isNotEmpty(sMSObjectLis)) {
				for (int i = 0; i < sMSObjectLis.size(); i++) {
					SMSObject sMSObject = sMSObjectLis.get(i);
					//SMSParamsBean ssmBean = new SMSParamsBean();
					if (sMSObject.getMessageContent().length() > 70) {
						smsFormat = 32;
					} else {
						smsFormat = 15; // 发送短信的格式
					}
					//ssmBean.setMessageContent(sMSObject.getMessageContent()); // 短信内容
					int count = sMSObject.getMessageContent().length();
					if (count <= 70) {
						sMSObject.setPkTotal(1);
					} else {
						sMSObject.setPkTotal((count / 67 + 1));
					}

					/*ssmBean.setSrcNumber("");//发送长号码
					ssmBean.setMessagePriority((short) 3);//消息的优先级: 0 最低 — 15最高
					ssmBean.setReportType((short) 1);//是否需要状态报告: 0 不需要 1 需要
					ssmBean.setSequenceId(sMSObject.getId()); //其他模块短信的id(大于零是易通平台发送小于零是代运营发送短信,发送短信之后要用到这个状态)
					ssmBean.setDestMobile(sMSObject.getDestMobile()); //目的地手机号码
					//ssmBean.setProductID(0); //产品编号 生产环境 20216101,测试环境 2401
					ssmBean.setProductID(0); //产品编号 易通平台0*/
					sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.YTOXL.getValue());//易通平台发送
					sMSObject.setSendTime(new Date());
					
					String sendXml = xmlSend(sMSObject.getMessageContent(),sMSObject.getDestMobile()); //xml 参数
					String sendResult = new HttpPostCore().connect(sendSMSUrl, sendXml.trim(),1);// 短信发送
					System.err.println("短信发送的请求参数xml为："+sendXml);
					System.err.println("短信发送返回的xml为 : "+sendResult);
					Integer smsNum = parseSMSsendResult(sendResult);//xml解析
					sMSObject.setSmsBatchNum(smsNum); //发送短信的批次号
					SMSObjectService.edit(sMSObject);
					logger.info("==========易通短信批次号插入数据库成功===========");
					//return ssmBean;
				}
			}
			logger.error("易通发送短信-------end");
		}

		logger.error("代运营发送短信-------------start");
		List<SMSObject> list = smsOtherWaitService.getList(map);
		if (CollectionUtils.isNotEmpty(list)) {
			SMSObject sMSObject = list.get(0);
			//SMSParamsBean ssmBean = new SMSParamsBean();
			if (sMSObject.getMessageContent().length() > 70) {
				smsFormat = 32;
			} else {
				smsFormat = 15;
			}
			//ssmBean.setMessageContent(sMSObject.getMessageContent());
			int count = sMSObject.getMessageContent().length();
			if (count <= 70) {
				sMSObject.setPkTotal(1);
			} else {
				sMSObject.setPkTotal((count / 67 + 1));
			}

			/*ssmBean.setSrcNumber("");
			ssmBean.setMessagePriority((short) 3);
			ssmBean.setReportType((short) 1);
			ssmBean.setSequenceId(sMSObject.getId() * (-1));
			ssmBean.setDestMobile(sMSObject.getDestMobile());
			//ssmBean.setProductID(20216101);
			ssmBean.setProductID(1);*/
			sMSObject.setStatus(TechcenterEnum.TECHCENTERFLAG.YTOXL.getValue());
			sMSObject.setSendTime(new Date());
			String sendXml = xmlSend(sMSObject.getMessageContent(),sMSObject.getDestMobile()); //xml 参数
			String sendResult = new HttpPostCore().connect(sendSMSUrl, sendXml.trim(),1);// 短信发送
			System.err.println("短信发送的请求参数xml为："+sendXml);
			System.err.println("短信发送返回的xml为 : "+sendResult);
			Integer smsNum = parseSMSsendResult(sendResult);//xml解析
			sMSObject.setSmsBatchNum(smsNum); //发送短信的批次号
			smsOtherWaitService.edit(sMSObject);
			logger.info("==========代运营短信批次号插入数据库成功===========");
			//logger.error("SubmitSenderOther========Standard_Submit对象发生"+ ssmBean.getSequenceId());
			//return ssmBean;
		}
		logger.error("代运营发送短信-------------end");
		return null;
	}
	/**
	 * xml 解析
	 * @param result
	 * @return
	 */
	private Integer parseSMSsendResult(String result) {
		// TODO Auto-generated method stub
		Document document = loadDocument(result);
		/*Element root = ((org.dom4j.Document) document).getRootElement();
		System.out.println(root.elementText("int"));
		return Integer.parseInt(root.elementText("int"));*/
		Integer value = null;
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				System.out.println("the xml NodeName is : "+node.getName() + ", the NodeValue is : "+ node.getText());
				value = Integer.parseInt(node.getText());
			}
		}
		if(value == null){
			logger.error("发送短信返回的待发序列号为："+value);
			return null;
		}
		return value;
	}
	/**
	 * 将xml字符串转变成Document
	 * @param obj
	 * @return
	 */
	public Document loadDocument(String xml) {
		try{
			Document document = (Document) DocumentHelper.parseText(xml.trim());
			return document;
		} catch (Exception e) {
			logger.error("no excel.xml");
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 短信发送xml参数
	 * @param
	 * Datcreatetime:创建时间
	 * Datsrcsendtime:发送起始时间
	 * Datsrcendtime:发起结束时间
	 * Nummessageformat:短信发送格式
	 * Nummessagepriority:消息的优先级: 0 最低 — 15最高
	 * Numproductid:产品编号 生产环境 20216101,测试环境 2401
	 * Numsendgroupid:下发批次（默认填写 0）
	 * Nummessagetype:消息的下发类型: 0 免费下发 1 按条下发 2 包月下发 3 订阅请求 4 取消请求 5 包月扣费(默认为0)
	 * Vc2messagecontent:消息
	 * 的内容
	 * Numreporttype:是否需要状态报告: 0 不需要 1 需要
	 * @return
	 */
	public String xmlSend(String smsContent , String destMobile){
		//当前时间转换成utc时间
		
		String currentTimer = dateFormatUTC();
		String nextTimer = upTime(new Date());
		
		String xml =
			 "<?xml version='1.0' encoding='UTF-8'?>"
            +"<ufinterface>"
            +"<Result>"
            +"<SmsSendInfo>"
            +"<Datcreatetime>"+currentTimer+"</Datcreatetime>"
            +"<Datsrcsendtime>"+currentTimer+"</Datsrcsendtime>"
            +"<Datsrcendtime>"+nextTimer+"</Datsrcendtime>"
            +"<Nummessageformat>"+smsFormat+"</Nummessageformat>"
            +"<Nummessagepriority>"+3+"</Nummessagepriority>"
            +"<Numproductid>1</Numproductid>"
            +"<Numsendgroupid>0</Numsendgroupid>"
            +"<Nummessagetype>1</Nummessagetype>"
            +"<Vc2messagecontent>"+smsContent+"</Vc2messagecontent>"
            +"<Numreporttype>1</Numreporttype>"
            +"<Vc2destmobile>"+destMobile+"</Vc2destmobile>"
            +"<Numstatus>0</Numstatus>"
            +"</SmsSendInfo>"
            +"</Result>"
            +"</ufinterface>";
		return xml;
	}
	/**
	 * 将当前时间装换成UTC时间
	 * @return
	 */
	public String dateFormatUTC(){
		
		SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		sdFormat.setTimeZone(zone);
		Date dateTemp=new Date();
		sdFormat.format(dateTemp);
		String currentTimer = sdFormat.format(dateTemp);
		System.err.println("UTCs时间为："+currentTimer);
		return currentTimer;
	}
	
	/**
	 * 当前日期加一再转换成utc时间
	 * @param dateTemp
	 * @return
	 */
	public String upTime(Date dateTemp){
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(dateTemp);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date date=cal.getTime();
		sdFormat.format(date);
		String endTimer=sdFormat.format(date);
		System.err.println("&&&&&&"+endTimer);
		return endTimer;
	}
	
	

}
