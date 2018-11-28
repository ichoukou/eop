package net.ytoec.kernel.techcenter.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.ytoec.kernel.common.HttpPostCore;
import net.ytoec.kernel.dataobject.SMSObject;
import net.ytoec.kernel.service.SMSObjectService;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SMSSearchCore {
	protected static final Logger logger = LoggerFactory.getLogger(SMSSenderCore.class);
	private SMSObjectService<SMSObject> SMSObjectService = SMSSender.getSMSObjectService();//发送短信历史记录表  
	private String sendSMSUrl = "http://58.32.246.70:8088/SMSInterface";//短信接口url
	public void smsStatusSearch() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 200);
		logger.error("=====================短信状态查询开始");
		List<SMSObject> sMSObjectLis = SMSObjectService.getStatusList(map);
		if (CollectionUtils.isNotEmpty(sMSObjectLis)) {
			for (int i = 0; i < sMSObjectLis.size(); i++) {
				SMSObject sMSObject = sMSObjectLis.get(i);
				String searchXml = xmlSearch(sMSObject.getSmsBatchNum()); //xml 参数
				String searchResult = new HttpPostCore().connect(sendSMSUrl, searchXml.trim(),2);// 短信状态查询
				Map<String,String> mapValue = parseSMSsearchResult(searchResult);//xml解析
				if(mapValue.size() == 0){
					logger.error("解析出错...");
					continue;
				}
					
				//判断短信发送是否异常 0 正常 , 其他异常
				Integer Numstatus = Integer.parseInt(mapValue.get("Numreportstatus"));//短信状态
				if(Integer.parseInt(mapValue.get("Numresponsestatus"))==0){
					//短信发送成功 2成功 ,0失败
					if(Numstatus == 0 || Numstatus == 2){
						logger.error("==================短信"+sMSObject.getSendMobile()+"状态为"+Numstatus);
						SMSSendSuccess success = new SMSSendSuccess();
						success.smsStatusUpd(sMSObject.getId(),Numstatus,"DELIVRD");
					}else if(Numstatus ==1){
						logger.error("==================短信状态为等待");
					}
				}else{
					SMSSendFailed failed=new SMSSendFailed();
					failed.sendMSMFiled(sMSObject.getId(), mapValue.get("Vc2destmobile"), sMSObject.getSmsType());
				}
				
			}
		}
		logger.error("没有查询到要修改的短信");
	}
/**
 * 解析xml
 * @param sendResult
 * @return
 */
	private Map<String,String> parseSMSsearchResult(String sendResult) {
		Document document=loadDocument(sendResult);
		Element employees=document.getRootElement();
		Map<String,String> map = new HashMap<String, String>();
		
		for (Iterator iterator = employees.elementIterator(); iterator.hasNext();) {
			Element employee = (Element) iterator.next();
			for (Iterator iterator2 = employee.elementIterator(); iterator2.hasNext();) {
				Element node = (Element) iterator2.next();
				System.err.println("node Text = "+node.elementText("Numresponsestatus"));
				System.err.println("node Text = "+node.elementText("Numreportstatus"));
				System.err.println("node Text = "+node.elementText("Vc2destmobile"));
				map.put("Numresponsestatus", node.elementText("Numresponsestatus"));
				map.put("Vc2destmobile", node.elementText("Vc2destmobile"));
				map.put("Numreportstatus", node.elementText("Numreportstatus"));
			}
		}
		return map;
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
	 * 短信状态查询xml参数
	 * @param num
	 * @return
	 */
	private String xmlSearch(Integer num) {
		String xml="<?xml version='1.0' encoding='UTF-8'?>"+
		"<ufinterface>"+
		  "<Result>"+
			"<SmsStateInquiresInfo>"+
				"<Numsendseqid>"+num+"</Numsendseqid>"+
			"</SmsStateInquiresInfo>"+
		  "</Result>" +
	    "</ufinterface>";

		return xml;
	}
}
