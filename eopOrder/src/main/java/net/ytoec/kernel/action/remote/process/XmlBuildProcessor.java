package net.ytoec.kernel.action.remote.process;

import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.util.StringUtil;

public class XmlBuildProcessor {
	
	//生成通知电商的xml
    public static String getOrderUpdateXML(JgWaybill jb){
    		StringBuilder sb = new StringBuilder();
    		sb.append("<UpdateInfo>");
    		if (jb.getClientID()!=null) {
    			sb.append("<clientID>");
    			sb.append(jb.getClientID());
    			sb.append("</clientID>");
    		}
    		
    		if (jb.getLogisticId()!=null) {
    			sb.append("<txLogisticID>");
    			sb.append(jb.getLogisticId());
    			sb.append("</txLogisticID>");
    		}
    		if(jb.getMailNo()!=null){
    		sb.append("<mailNo>");
			sb.append(jb.getMailNo());
			sb.append("</mailNo>");
    		}
			sb.append("<logisticProviderID>");
			sb.append("YTO");
			sb.append("</logisticProviderID>");
    		sb.append("<infoType>");
    		sb.append("INSTRUCTION");
    		sb.append("</infoType>");
    		sb.append("<infoContent>");
    		sb.append("UPDATE");
    		sb.append("</infoContent>");
    		sb.append("<remark>");
    		sb.append("面单更新通知");
    		sb.append("</remark>");
    		sb.append("</UpdateInfo>");

    		return sb.toString();
     }
    
	//生成推送面单号给电商平台的xml
    public static String getEcUpdateXML(String clientId, String mailNo, String txLogisticId){

    	StringBuilder xmlString = new StringBuilder();
        xmlString.append("<UpdateInfo><txLogisticID>");
        xmlString.append(txLogisticId);
        xmlString.append("</txLogisticID><mailNo>");
        xmlString.append(mailNo);
        xmlString.append("</mailNo><clientID>");
        xmlString.append(clientId);
        xmlString.append("</clientID><logisticProviderID>YTO</logisticProviderID><infoType>INSTRUCTION</infoType><infoContent>UPDATE</infoContent><remark>面单号推送</remark></UpdateInfo>");

    	return xmlString.toString();
     }
    
    
    
    //生成面单绑定xml
    public static String getOrderBindXML(UpdateWaybillInfo updateInfo){
    		StringBuilder sb = new StringBuilder();
    		sb.append("<UpdateInfo>");
    		if (updateInfo.getClientId()!=null) {
    			sb.append("<clientID>");
    			sb.append(updateInfo.getClientId());
    			sb.append("</clientID>");
    		}
    		
    		if (updateInfo.getTxLogisticId()!=null) {
    			sb.append("<txLogisticID>");
    			sb.append(updateInfo.getTxLogisticId());
    			sb.append("</txLogisticID>");
    		}
    		if(updateInfo.getMailNo()!=null){
    		sb.append("<mailNo>");
			sb.append(updateInfo.getMailNo());
			sb.append("</mailNo>");
    		}
			sb.append("<logisticProviderID>");
			sb.append("YTO");
			sb.append("</logisticProviderID>");
    		sb.append("<infoType>");
    		sb.append("INSTRUCTION");
    		sb.append("</infoType>");
    		sb.append("<infoContent>");
    		sb.append("UPDATE");
    		sb.append("</infoContent>");
    		sb.append("<remark>");
    		sb.append("面单更新通知");
    		sb.append("</remark>");
    		sb.append("</UpdateInfo>");
    		return sb.toString();
     }
    
    
    
    
    //生成下单xml
    public static String getOrderCreateXML(OrderFormInfo requestOrder){
    	boolean isTaobao="TAOBAO".equals(requestOrder.getClientId());
    	StringBuilder sb = new StringBuilder();
		sb.append("<RequestOrder>");
		if(isTaobao){
			if(!StringUtil.isBlank(requestOrder.getClientId())){
				sb.append("<ecCompanyId>");
				sb.append(requestOrder.getClientId());
				sb.append("</ecCompanyId>");
			}
		}else{
			if(!StringUtil.isBlank(requestOrder.getClientId())){
				sb.append("<clientID>");
				sb.append(requestOrder.getClientId());
				sb.append("</clientID>");
			}
		}
		if(!StringUtil.isBlank(requestOrder.getLogisticProviderId())){
			sb.append("<logisticProviderID>");
			sb.append(requestOrder.getLogisticProviderId());
			sb.append("</logisticProviderID>");
		}
		if(!StringUtil.isBlank(requestOrder.getCustomerId())){
			sb.append("<customerId>");
			sb.append(requestOrder.getCustomerId());
			sb.append("</customerId>");
		}
		if(!StringUtil.isBlank(requestOrder.getTxLogisticId())){
			sb.append("<txLogisticID>");
			sb.append(requestOrder.getTxLogisticId());
			sb.append("</txLogisticID>");
		}
		if(!StringUtil.isBlank(requestOrder.getTradeNo())){
			sb.append("<tradeNo>");
			sb.append(requestOrder.getTradeNo());
			sb.append("</tradeNo>");
		}
		if(!StringUtil.isBlank(requestOrder.getMailNo())){
			sb.append("<mailNo>");
			sb.append(requestOrder.getMailNo());
			sb.append("</mailNo>");
		}
		if(!StringUtil.isBlank(requestOrder.getTotalServiceFee())){
			sb.append("<totalServiceFee>");
			sb.append(requestOrder.getTotalServiceFee());
			sb.append("</totalServiceFee>");
		}
		if(!StringUtil.isBlank(requestOrder.getCodSplitFee())){
			sb.append("<codSplitFee>");
			sb.append(requestOrder.getCodSplitFee());
			sb.append("</codSplitFee>");
		}
		if(!StringUtil.isBlank(requestOrder.getBuyServiceFee())){
			sb.append("<buyServiceFee>");
			sb.append(requestOrder.getCodSplitFee());
			sb.append("</buyServiceFee>");
		}
		if(!StringUtil.isBlank(requestOrder.getOrderType())){
			sb.append("<orderType>");
			sb.append(requestOrder.getOrderType());
			sb.append("</orderType>");
		}
		if(!StringUtil.isBlank(requestOrder.getServiceType())){
			sb.append("<serviceType>");
			sb.append(requestOrder.getServiceType());
			sb.append("</serviceType>");
		}
		if(!StringUtil.isBlank(requestOrder.getType())){
			sb.append("<type>");
			sb.append(requestOrder.getType());
			sb.append("</type>");
		}
		if(!StringUtil.isBlank(requestOrder.getFlag())){
			sb.append("<flag>");
			sb.append(requestOrder.getFlag());
			sb.append("</flag>");
		}
		if(!StringUtil.isBlank(requestOrder.getSendStartTime())){
			sb.append("<sendStartTime>");
			sb.append(requestOrder.getSendStartTime());
			sb.append("</sendStartTime>");
		}
		if(!StringUtil.isBlank(requestOrder.getSendEndTime())){
			sb.append("<sendEndTime>");
			sb.append(requestOrder.getSendEndTime());
			sb.append("</sendEndTime>");
		}
		if(!StringUtil.isBlank(requestOrder.getGoodsValue())){
			sb.append("<goodsValue>");
			sb.append(requestOrder.getGoodsValue());
			sb.append("</goodsValue>");
		}
		if(!StringUtil.isBlank(requestOrder.getItemsValue())){
			sb.append("<itemsValue>");
			sb.append(requestOrder.getItemsValue());
			sb.append("</itemsValue>");
		}
//		if(!StringUtil.isBlank(requestOrder.getItemsValue())){
//			sb.append("<itemsValue>");
//			sb.append(requestOrder.getItemsValue());
//			sb.append("</itemsValue>");
//		}
//		if(!StringUtil.isBlank(requestOrder.getInsuranceValue())){
			sb.append("<insuranceValue>");
			sb.append(requestOrder.getInsuranceValue());
			sb.append("</insuranceValue>");
//		}
		if(requestOrder.getSpecial()!=null){
			sb.append("<special>");
			sb.append(requestOrder.getSpecial().getCode());
			sb.append("</special>");
		}else{
			sb.append("<special>0</special>");
		}
		if(!StringUtil.isBlank(requestOrder.getRemark())){
			sb.append("<remark>");
			sb.append(requestOrder.getRemark());
			sb.append("</remark>");
		}
		
		
		//发件人
		if(requestOrder.getSender()!=null){
			sb.append("<sender>");
			if(!StringUtil.isBlank(requestOrder.getSender().getName())){
				sb.append("<name>");
				sb.append(requestOrder.getSender().getName());
				sb.append("</name>");
			}
			if(!StringUtil.isBlank(requestOrder.getSender().getPostCode())){
				sb.append("<postCode>");
				sb.append(requestOrder.getSender().getPostCode());
				sb.append("</postCode>");
			}
			if(!StringUtil.isBlank(requestOrder.getSender().getPhone())){
				sb.append("<phone>");
				sb.append(requestOrder.getSender().getPhone());
				sb.append("</phone>");
			}
			if(!StringUtil.isBlank(requestOrder.getSender().getMobile())){
				sb.append("<mobile>");
				sb.append(requestOrder.getSender().getMobile());
				sb.append("</mobile>");
			}
			if(!StringUtil.isBlank(requestOrder.getSender().getProv())){
				sb.append("<prov>");
				sb.append(requestOrder.getSender().getProv());
				sb.append("</prov>");
			}
			if(!StringUtil.isBlank(requestOrder.getSender().getCity())){
				sb.append("<city>");
				sb.append(requestOrder.getSender().getCity());
				sb.append("</city>");
			}
			if(!StringUtil.isBlank(requestOrder.getSender().getAddress())){
				sb.append("<address>");
				sb.append(requestOrder.getSender().getAddress());
				sb.append("</address>");
			}
			sb.append("</sender>");
		}
		//收件人
		if(requestOrder.getReceiver()!=null){
			sb.append("<receiver>");
			if(!StringUtil.isBlank(requestOrder.getReceiver().getName())){
				sb.append("<name>");
				sb.append(requestOrder.getReceiver().getName());
				sb.append("</name>");
			}
			if(!StringUtil.isBlank(requestOrder.getReceiver().getPostCode())){
				sb.append("<postCode>");
				sb.append(requestOrder.getReceiver().getPostCode());
				sb.append("</postCode>");
			}
			if(!StringUtil.isBlank(requestOrder.getReceiver().getPhone())){
				sb.append("<phone>");
				sb.append(requestOrder.getReceiver().getPhone());
				sb.append("</phone>");
			}
			if(!StringUtil.isBlank(requestOrder.getReceiver().getMobile())){
				sb.append("<mobile>");
				sb.append(requestOrder.getReceiver().getMobile());
				sb.append("</mobile>");
			}
			if(!StringUtil.isBlank(requestOrder.getReceiver().getProv())){
				sb.append("<prov>");
				sb.append(requestOrder.getReceiver().getProv());
				sb.append("</prov>");
			}
			if(!StringUtil.isBlank(requestOrder.getReceiver().getCity())){
				sb.append("<city>");
				sb.append(requestOrder.getReceiver().getCity());
				sb.append("</city>");
			}
			if(!StringUtil.isBlank(requestOrder.getReceiver().getAddress())){
				sb.append("<address>");
				sb.append(requestOrder.getReceiver().getAddress());
				sb.append("</address>");
			}
			sb.append("</receiver>");
		}
		//物品
		if(requestOrder.getItems()!=null&&requestOrder.getItems().size()>0){
			sb.append("<items>");
//			Product p=requestOrder.getItems().get(0);
			for(Product p:requestOrder.getItems()){
					if(p!=null){
						sb.append("<item>");
						if(!StringUtil.isBlank(p.getItemName())){
							sb.append("<itemName>");
							sb.append(p.getItemName());
							sb.append("</itemName>");
						}
						if(!StringUtil.isBlank(p.getItemNumber().toString())){
							sb.append("<number>");
							sb.append(p.getItemNumber().toString());
							sb.append("</number>");
						}
		//				if(!StringUtil.isBlank(p.getItemValue())){
							sb.append("<itemValue>");
							sb.append(p.getItemValue());
							sb.append("</itemValue>");
		//				}
						sb.append("</item>");
					}
			}
			sb.append("</items>");
		}
		sb.append("</RequestOrder>");
		return sb.toString();
/*    	!<RequestOrder>
    	!<clientID>SIMDOO</clientID> 
    	!<logisticProviderID>YTO</logisticProviderID>
    	!<customerId>a92266073246b3ed2a2f0ff4d0b2bf5e</customerId>
    	!<txLogisticID>LP07082300225709</txLogisticID>
    	!<tradeNo>2007082300225709</tradeNo>
    	!<mailNo>124579546621</mailNo>
    	!<totalServiceFee>32.0</totalServiceFee>
    	!<codSplitFee>200</codSplitFee>
    	!<orderType>1</orderType>
    	!<serviceType>0</serviceType>
    	!<type>0</type>
    	!<flag>1</flag>
    	!<sender>
    	!<name>张三</name>
    	!<postCode>310013</postCode>
    	!<phone>231234134</phone>
    	!<mobile>13575745195</mobile>
    	!<prov>浙江</prov>
    	!<city>上海,浦东区</city>
    	!<address>新龙科技大厦9层</address>
    	!</sender>
    	!<receiver>
    	!<name>李四</name>
    	!<postCode>100000</postCode>
    	!<phone>231234134</phone>
    	!<prov>北京</prov>
    	!<city>北京市</city>
    	!<address>新龙科技大厦9层</address>
    	!</receiver>
    	!<sendStartTime>2005-08-24 08:00:00.0 CST</sendStartTime>
    	!<sendEndTime>2005-08-24 12:00:00.0 CST</sendEndTime>
    	!<goodsValue>1900</goodsValue>
    	!<itemsValue>2000</itemsValue>
    	!<items>
    	<item>
    	<itemName>Nokia N73</itemName>
    	<number>2</number>
    	<itemValue>2</itemValue>
    	</item>
    	<item>
    	<itemName>Nokia N72</itemName>
    	<number>1</number>
    	<itemValue>2</itemValue>
    	</item>
    	!</items>
    	!<insuranceValue>0.0</insuranceValue>
    	!<special>0</special>
    	!<remark>易碎品</remark>
    	!</RequestOrder> */
		
    }
}
