package net.ytoec.kernel.action.remote;

import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.action.remote.xml.ObjectToXmlFragment;
import net.ytoec.kernel.action.remote.xml.QueryOrder;

/**
 * 
 * 查询响应结果格式.
 * 
 */
public class BatchQueryResponse implements ObjectToXmlFragment {
	/**
	 * 批量查询成功响应的标识.
	 * 注:如果批量查询被成功处理,则完整响应字符串中包含此字符串,此字符串是正确响应的xml片段的根元素名.
	 */
	public static final String BATCH_QUERY_RESPONSE_MARK = "BatchQueryResponse";
	// 物流公司编号
	private String logisticProviderId;
	//

	private List<QueryOrder> orders = new ArrayList<QueryOrder>();

	public String toXmlFragment() {
		StringBuilder sb = new StringBuilder();
		sb.append("<BatchQueryResponse>");
		sb.append("<logisticProviderID>");
		sb.append(this.getLogisticProviderId());
		sb.append("</logisticProviderID>");
		sb.append("<orders>");
		for (QueryOrder order : this.getOrders()) {
			sb.append(order == null ? "" : order.toXmlFragment());
		}
		sb.append("</orders>");
		sb.append("</BatchQueryResponse>");

		return sb.toString();
	}

	public String getLogisticProviderId() {
		return logisticProviderId;
	}

	public void setLogisticProviderId(String logisticProviderId) {
		this.logisticProviderId = logisticProviderId;
	}

	public List<QueryOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<QueryOrder> orders) {
		this.orders = orders;
	}

	public void addOrder(QueryOrder order) {
		this.orders.add(order);
	}
}
