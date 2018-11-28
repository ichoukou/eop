package net.ytoec.kernel.service.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Postinfo;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.service.impl.OrderServiceImpl;
import net.ytoec.kernel.util.XmlUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 电子对账 对账类
 * 
 * @author der
 */
public class EcAccountHelper {
	
	private static Logger logger=LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 运费计算公共接口，接口会根据传入的运费模板和订单信息，去调用对应的计算方法。
     * @param pt 运费模板
     * @param order 需要计算运费的订单
     * @throws Exception 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
	@SuppressWarnings("unchecked")
	public static float ecaccount(Posttemp pt,Order order) throws FileNotFoundException, IOException, Exception{
		float pay = 0;
		List<Postinfo> pfList = new ArrayList<Postinfo>();
		String pfXmlStr = pt.getPostinfo();
		Postinfo pf = null;
		if(StringUtils.isNotBlank(pfXmlStr)){
			pfList = XmlUtil.xmlDecoder2List(pfXmlStr);
		}
		else{ //2012-4-23修改，如果运费模块xml串为空，则取系统默认模块中List定的规则计算。
		    pfList = pt.getPostinfoList();
		}
		
		if("440000".equals(order.getSprovCode()) && "440000".equals(order.getBprovCode())){
			// 如果目的地的市属于珠三角，就取珠三角的运费
			boolean isExist = Resource.orderProvinces.containsKey(order.getScity());	// 市 显示值
			for(Postinfo info : pfList){
				if(isExist){
					if(("449999").equals(info.getDestId())){
						pf = info;
						break;
					}
				}else{
					if(("448888").equals(info.getDestId())){
						pf = info;
						break;
					}
				}
			}
		}else{
			for(Postinfo info : pfList){
				if(info.getDestId().equals(order.getBprovCode())){
					pf = info;
					break;
				}
			}
		}
		
		if(pf == null){
			logger.error("未找到对应目的地的运费模板信息!");
		}
		if("1".equals(pt.getCalclateType())){
			pay = ecaccountA(pf.getFixedPirce());
		}
		else if("2".equals(pt.getCalclateType())){
			pay = ecaccountB(order.getWeight(),pf.getWeightPirce(),pf.getFloorPirce());
		}
		else if("3".equals(pt.getCalclateType())){
			pay = ecaccountC(order.getWeight(),pf.getFirstWeight(),pf.getFwRealPirce(),pf.getOwRealPirce(),pf.getFloorPirce());		
		}
		else{
			//对于续重统计单位收费情况，每个地区的统计单位不同
			pay = ecaccountD(order.getWeight(), pf.getFirstWeight(), pf.getAddWeightChoice(), pf.getFwRealPirce(), pf.getOwRealPirce(), pf.getFloorPirce());
		}
		return pay;
	}
	
	/**
     * A类方式-固定价格
     * 计费方法：每票件都是同一个价格price元。
     * @param price固定价格
     * return price
     */
    private static float ecaccountA(float price) {
    	return price;
    }

    /**
     * B类方式-简单重量规则
     * 计费方式：重量 * 重量单价 x元/公斤 最低收费价格y
     * @param m重量
     * @param price每公斤价格
     * @param limit最低收费
     * @return (x<=y?y:x)
     */
    private static float ecaccountB(float m,float price,float limit) {
    	return (m*price)<=limit?limit:(m*price);
    }
    
    /**
     * C类方式-续重价格规则
     * @param weight 实际重量
     * @param fWeight 首重重量
     * @param fwRealPrice 实际首重价格
     * @param fwRealPrice 实际超重价格
     * @param limit 最低收费
     * @return ((M<=K?Y:(Y+(M-K)*X))<=L) ? L : (M<=K?Y:(Y+(M-K)*X)
     */
    private static float ecaccountC(float weight,float fWeight,float fwRealPrice,float owRealPrice,float limit){
    	return (weight > fWeight ? (fwRealPrice+(float)Math.ceil(weight-fWeight)*owRealPrice) : fwRealPrice) > limit ? (weight > fWeight ? (fwRealPrice+(float)Math.ceil(weight-fWeight)*owRealPrice) : fwRealPrice) : limit;
    }
    
    /**
     * D类方式-续重统计单位规则
     * @param weight 实际重量
     * @param fWeight 首重重量
     * @param module 计费单位
     * @param fwRealPrice 实际首重价格
     * @param owRealPrice 实际超重价格
     * @param limit 最低价格
     */
    private static float ecaccountD(float weight, float fWeight, float module, float fwRealPrice, float owRealPrice, float limit){
    	if(module>0.01){
	    	double ceil = Math.ceil((weight-fWeight)/module);
	    	return (weight > fWeight ? (fwRealPrice + (float)(ceil*module)*owRealPrice) : fwRealPrice) > limit ? (weight > fWeight ? (fwRealPrice + (float)(ceil*module)*owRealPrice) : fwRealPrice) : limit;
    	}else{
    		return (weight > fWeight ? (fwRealPrice + (weight-fWeight)*owRealPrice) : fwRealPrice) > limit ? (weight > fWeight ? (fwRealPrice + (weight-fWeight)*owRealPrice) : fwRealPrice) : limit;
    	}
    }
}
