package net.ytoec.kernel.action.remote.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * 
 *
 */
public class QueryOrder implements ObjectToXmlFragment {
	private static Logger logger = LoggerFactory.getLogger(QueryOrder.class);
	private static final String TXLOGISTICID = "txLogisticID";
	private static final String LOGISTICPROVIDERID = "logisticProviderID";
	private static final String MAILNO = "mailNo";
	private static final String ORDERSTATUS = "orderStatus";
	private static final String MAILTYPE = "mailType";
	private static final String ACCEPTTIME = "acceptTime";
	private static final String ACCEPTADDRESS = "acceptAddress";
	private static final String STATUS = "status";
	private static final String REMARK = "remark";
	private static final String STEPS = "steps";
	private static final String STEP = "step";
	private static final String NAME = "name";
	private static final String ORGCODE = "orgCode";
	// 邮件号
	private String mailNo;
	// 物流平台订单号，如果有，请提供
	private String txLogisticId;
	// 邮件类型
	private String mailType;
	// 当前订单状态
	private String orderStatus;

	private List<StepInfo> steps = new ArrayList<StepInfo>();

	private String shopName;

	/**
	 * 是否是问题件,true-是问题件，false-正常件。
	 */
	private boolean isQuestion;

	/**
	 * 问题件处理状态
	 */
	private String dealStatus;

	/**
	 * 问题件处理时间
	 * 
	 * @param dealTime
	 */
	private Date dealTime;

	// 排序需要字段
	private int value;
	private String acceptTime;
	// 重量，收件人信息
	private String weight;
	private TraderInfo traderInfo;

	/**
	 * 0:不可关注；1：已关注；2：可关注（表示该运单属于该用户，且未关注）
	 */
	private Integer isAttention;

	public QueryOrder() {
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public TraderInfo getTraderInfo() {
		return traderInfo;
	}

	public void setTraderInfo(TraderInfo traderInfo) {
		this.traderInfo = traderInfo;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setIsQuestion(boolean isQuestion) {
		this.isQuestion = isQuestion;
	}

	public boolean getIsQuestion() {
		return isQuestion;
	}

	public QueryOrder(Node order) {
		// logger.debug("=====new Order BEGIN====");
		NodeList nodeList = order.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (MAILNO.equals(node.getNodeName())) {
				Element clientIdEle = (Element) node;
				if (clientIdEle.getFirstChild() != null)
					this.mailNo = clientIdEle.getFirstChild().getNodeValue()
							.trim();
			} else if (TXLOGISTICID.equals(node.getNodeName())) {
				Element clientIdEle = (Element) node;
				if (clientIdEle.getFirstChild() != null)
					if (clientIdEle.getFirstChild() != null)
						this.txLogisticId = clientIdEle.getFirstChild()
								.getNodeValue().trim();
			} else if (MAILTYPE.equals(node.getNodeName())) {
				Element clientIdEle = (Element) node;
				if (clientIdEle.getFirstChild() != null)
					this.mailType = clientIdEle.getFirstChild().getNodeValue()
							.trim();
			} else if (ORDERSTATUS.equals(node.getNodeName())) {
				Element clientIdEle = (Element) node;
				if (clientIdEle.getFirstChild() != null)
					this.orderStatus = clientIdEle.getFirstChild()
							.getNodeValue().trim();
			} else if (STEPS.equals(node.getNodeName())) {
				NodeList stepsNode = node.getChildNodes();
				for (int k = 0; k < stepsNode.getLength(); k++) {
					NodeList stepNode = stepsNode.item(k).getChildNodes();
					StepInfo step = new StepInfo();
					for (int j = 0; j < stepNode.getLength(); j++) {
						Node stepEle = stepNode.item(j);
						// QueryOrder(stepEle.getNodeName());
						if (ACCEPTTIME.equals(stepEle.getNodeName())) {
							Element ele = (Element) stepEle;
							if (ele.getFirstChild() != null)
								step.setAcceptTime(ele.getFirstChild()
										.getNodeValue());
						} else if (ACCEPTADDRESS.equals(stepEle.getNodeName())) {
							Element ele = (Element) stepEle;
							if (ele.getFirstChild() != null)
								step.setAcceptAddress(ele.getFirstChild()
										.getNodeValue().trim());
						} else if (STATUS.equals(stepEle.getNodeName())) {
							Element ele = (Element) stepEle;
							if (ele.getFirstChild() != null)
								step.setStatus(ele.getFirstChild()
										.getNodeValue().trim());
						} else if (REMARK.equals(stepEle.getNodeName())) {
							Element ele = (Element) stepEle;
							if (ele.getFirstChild() != null)
								step.setRemark(ele.getFirstChild()
										.getNodeValue().trim());
						} else if (NAME.equals(stepEle.getNodeName())) {
							Element ele = (Element) stepEle;
							if (ele.getFirstChild() != null)
								step.setName(ele.getFirstChild().getNodeValue()
										.trim()==null?"":ele.getFirstChild().getNodeValue()
												.trim());
						} else if (ORGCODE.equals(stepEle.getNodeName())) {
							Element ele = (Element) stepEle;
							if (ele.getFirstChild() != null) {
								step.setSiteCode(ele.getFirstChild()
										.getNodeValue().trim());
								/**
								 * 此处同时给contactWay赋值
								 */
								User user = Resource.getUserBySiteCode(step
										.getSiteCode());
								logger.info("金刚返回组织机构编码：" + step.getSiteCode());
								if (user != null)
									step.setContactWay(this.setContact(
											user.getMobilePhone()==null?"":user.getMobilePhone(),
											user.getTelCode()==null?"":user.getTelCode() 
													+ user.getTelePhone()==null?"":user.getTelePhone()));
							}
						}
					}
					this.steps.add(step);

				}
			}
		}

	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;

	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<StepInfo> getSteps() {
		return steps;
	}

	public void setSteps(List<StepInfo> steps) {
		this.steps = steps;
	}

	public void addStep(StepInfo step) {
		this.steps.add(step);
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String toXmlFragment() {
		StringBuilder sb = new StringBuilder();
		sb.append("<order>");
		sb.append("<mailNo>");
		sb.append(this.getMailNo());
		sb.append("</mailNo>");
		sb.append("<txLogisticID>");
		sb.append(this.getTxLogisticId());
		sb.append("</txLogisticID>");
		sb.append("<mailType>");
		sb.append(this.getMailType());
		sb.append("</mailType>");
		sb.append("<orderStatus>");
		sb.append(this.getOrderStatus());
		sb.append("</orderStatus>");
		sb.append("<steps>");
		for (StepInfo step : this.getSteps()) {
			sb.append(step == null ? "" : step.toXmlFragment());
		}
		sb.append("</steps>");
		sb.append("</order>");

		return sb.toString();
	}

	/**
	 * 有固话显示固话，没有则显示手机
	 * 
	 * @param mobile
	 * @param phone
	 * @return
	 */
	private String setContact(String mobile, String phone) {
		if (phone != null && !("").equals(phone))
			return phone;
		else if (mobile != null && !("").equals(mobile))
			return mobile;
		else
			return "";
	}

	public Integer getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(Integer isAttention) {
		this.isAttention = isAttention;
	}

}
