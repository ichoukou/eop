package net.ytoec.kernel.remind;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import net.ytoec.kernel.common.HttpPostCore;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 定时器异常短信发送的类
 * create by hufei
 * create time 2013-04-10
 */
public class TimerSMSRemind {

	protected static final Logger logger = LoggerFactory.getLogger(TimerSMSRemind.class);
	private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private int smsFormat; // 短信发送格式
	private String sendSMSUrl = "http://58.32.246.70:8088/SMSInterface";// 短信接口url
	String destMobile = "18302125756";

	/**
	 * 定时器异常的短信发送
	 * 
	 * @return
	 */
	public void sendSMS(String messageContent) {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if (hour >= 8 && hour < 20) {
			logger.error("易通发送短信-------start");
			if (messageContent.length() > 70) {
				smsFormat = 32;
			} else {
				smsFormat = 15; // 发送短信的格式
			}
			String sendXml = xmlSend(messageContent, destMobile); // xml 参数
			String sendResult = new HttpPostCore().connect(sendSMSUrl,sendXml.trim(), 1);// 短信发送
			Integer num=parseSMSsendResult(sendResult);
			logger.error("代发序列号为："+num);
			logger.error("易通发送短信-------end");
		}
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
	public String xmlSend(String smsContent, String destMobile) {
		// 当前时间转换成utc时间
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
	 * 
	 * @return
	 */
	public String dateFormatUTC() {

		SimpleDateFormat sdFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		TimeZone zone = TimeZone.getTimeZone("GMT+8");
		sdFormat.setTimeZone(zone);
		Date dateTemp = new Date();
		sdFormat.format(dateTemp);
		String currentTimer = sdFormat.format(dateTemp);
		System.err.println("UTCs时间为：" + currentTimer);
		return currentTimer;
	}

	/**
	 * 当前日期加一再转换成utc时间
	 * 
	 * @param dateTemp
	 * @return
	 */
	public String upTime(Date dateTemp) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTemp);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date date = cal.getTime();
		sdFormat.format(date);
		String endTimer = sdFormat.format(date);
		System.err.println("&&&&&&" + endTimer);
		return endTimer;
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
}
