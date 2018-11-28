/**
 * 
 */
package net.ytoec.kernel.dataobject;

import java.util.Date;
import java.util.List;

import net.ytoec.kernel.action.remote.xml.QueryOrder;

/**
 * @author wangyong
 * @date 2011-11-16
 * 订单信息、产品信息的级联javaBean
 */
public class OrderProductBean {
	
	/** 订单信息对象 */
	private Order order;
	
	/** 产品信息对象 */
	private List<Product> product;
	
	/** 联系人信息对象 */
	private List<TraderInfo> traderInfo;
	
	/** 物流信息 */
	private List<QueryOrder> logisticList;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}

	public List<QueryOrder> getLogisticList() {
		return logisticList;
	}

	public void setLogisticList(List<QueryOrder> logisticList) {
		this.logisticList = logisticList;
	}

	public List<TraderInfo> getTraderInfo() {
		return traderInfo;
	}

	public void setTraderInfo(List<TraderInfo> traderInfo) {
		this.traderInfo = traderInfo;
	}


}
