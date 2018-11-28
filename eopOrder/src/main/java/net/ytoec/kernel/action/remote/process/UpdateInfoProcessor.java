package net.ytoec.kernel.action.remote.process;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Product;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * UpdateInfo元素处理类.
 * 
 */
public class UpdateInfoProcessor implements XmlFragmentProcessor {
	// root element.
	private static final String UPDATE_INFO_ELE = "UpdateInfo";
	private static final String TX_LOGISTIC_ID_ELE = "txLogisticID";
	private static final String MAIL_NO_ELE = "mailNo";
	private static final String CLIENT_ID_ELE = "clientID";
	private static final String LOGISTIC_PROVIDER_ID_ELE = "logisticProviderID";
	private static final String INFO_TYPE_ELE = "infoType";
	private static final String INFO_CONTENT_ELE = "infoContent";
	private static final String FIELD_LIST_ELE = "fieldList";
	private static final String FIELD_ELE = "field";
	private static final String REMARK_ELE = "remark";
	private static final String FIELD_NAME_ELE = "fieldName";
	private static final String FIELD_VALUE_ELE = "fieldValue";
	private static final String EC_COMPANY_ID_ELE = "ecCompanyId";
	private static final String STATUS = "status";

	// ADD--MGL 2011-09-23
	private static final String NAME = "name";
	private static final String ACCEPT_TIME = "acceptTime";
	private static final String CURRENT_CITY = "currentCity";
	private static final String NEXT_CITY = "nextCity";
	private static final String FACILITY = "facility";
	private static final String CONTACT_INFO = "contactInfo";
	private static final String WEIGHT = "weight";
	private static final String TRACKING_INFO = "trackingInfo";
	private static final String MAILNO = "mailNo";

	private static final String PAY_TIME = "payTime";
	private static final String PAY_AMOUNT = "payAmount";
	private static final String UNITID = "unitId";
	private static final String EMPLOYEEID = "employeeId";

	// status--取消订单
	private static final String WITHDRAW = "WITHDRAW";
	// status--签收成功
	private static final String SIGNED = "SIGNED";
	// status--接单成功
	private static final String ACCEPT = "ACCEPT";
	// status--接单失败
	private static final String UNACCEPT = "UNACCEPT";
	
	// status--签收失败
	private static final String FAILED="FAILED";
	
	// status--派件扫描
	private static final String SENT_SCAN = "SENT_SCAN";
	
	// status--揽收失败
	private static final String NOT_SEND="NOT_SEND";
	// status--接单失败
	private static final String GOT = "GOT";

	// status--更新订单
	private static final String UPDATE = "UPDATE";

	private DocumentReader documentReader = new DocumentReader();

	// taobao1.4.1版本接口使用
	public List<UpdateWaybillInfo> parseTaoBao(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseRootTaoBao(root);
	}

	public UpdateWaybillInfo parse(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseRoot(root);
	}

	public UpdateWaybillInfo parseAlter(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseAlterRoot(root);
	}

	private UpdateWaybillInfo parseRoot(Element root) {
		UpdateWaybillInfo updateInfo = new UpdateWaybillInfo();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (TX_LOGISTIC_ID_ELE.equals(node.getNodeName())) {
				Element txLogisticIdEle = (Element) node;
				if (txLogisticIdEle.getFirstChild() != null)
					updateInfo.setTxLogisticId(txLogisticIdEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (MAIL_NO_ELE.equals(node.getNodeName())) {
				Element mailNoEle = (Element) node;
				if (mailNoEle.getFirstChild() != null) {
					updateInfo.setMailNo(mailNoEle.getFirstChild()
							.getNodeValue().trim());
				}
			}

			if (CLIENT_ID_ELE.equals(node.getNodeName())) {
				Element clientIdEle = (Element) node;
				if (clientIdEle.getFirstChild() != null)
					updateInfo.setClientId(clientIdEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				if (logisticProviderIdEle.getFirstChild() != null)
					updateInfo.setLogisticProviderId(logisticProviderIdEle
							.getFirstChild().getNodeValue().trim());
			}
			if (INFO_TYPE_ELE.equals(node.getNodeName())) {
				Element infoTypeEle = (Element) node;
				if (infoTypeEle.getFirstChild() != null)
					updateInfo.setInfoType(infoTypeEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (INFO_CONTENT_ELE.equals(node.getNodeName())) {
				Element infoContentEle = (Element) node;
				if (infoContentEle.getFirstChild() != null)
					updateInfo.setInfoContent(infoContentEle.getFirstChild()
							.getNodeValue().trim());
			}

			if (REMARK_ELE.equals(node.getNodeName())) {
				Element remarkEle = (Element) node;
				if (remarkEle.getFirstChild() != null)
					updateInfo.setRemark(remarkEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (TRACKING_INFO.equals(node.getNodeName())) {
				Element tracking_info = (Element) node;
				if (tracking_info.getFirstChild() != null)
					updateInfo.setTrackingInfo(tracking_info.getFirstChild()
							.getNodeValue().trim());
			}
			if (NAME.equals(node.getNodeName())) {
				Element name = (Element) node;
				if (name.getFirstChild() != null)
					updateInfo.setName(name.getFirstChild().getNodeValue()
							.trim());
			}
			if (ACCEPT_TIME.equals(node.getNodeName())) {
				Element accept_time = (Element) node;
				if (accept_time.getFirstChild() != null)
					updateInfo.setAcceptTime(accept_time.getFirstChild()
							.getNodeValue().trim());
			}
			if (CURRENT_CITY.equals(node.getNodeName())) {
				Element current_city = (Element) node;
				if (current_city.getFirstChild() != null)
					updateInfo.setCurrentCity(current_city.getFirstChild()
							.getNodeValue().trim());
			}
			if (NEXT_CITY.equals(node.getNodeName())) {
				Element next_city = (Element) node;
				if (next_city.getFirstChild() != null)
					updateInfo.setNextCity(next_city.getFirstChild()
							.getNodeValue().trim());
			}
			if (FACILITY.equals(node.getNodeName())) {
				Element facility = (Element) node;
				if (facility.getFirstChild() != null)
					updateInfo.setFacility(facility.getFirstChild()
							.getNodeValue().trim());
			}
			if (CONTACT_INFO.equals(node.getNodeName())) {
				Element contact_info = (Element) node;
				if (contact_info.getFirstChild() != null)
					updateInfo.setContactInfo(contact_info.getFirstChild()
							.getNodeValue().trim());
			}
			if (WEIGHT.equals(node.getNodeName())) {
				Element weight = (Element) node;
				if (weight.getFirstChild() != null)
					updateInfo.setWeight(Double.valueOf(weight.getFirstChild()
							.getNodeValue().trim()));
			}
			if (PAY_TIME.equals(node.getNodeName())) {
				Element payTime = (Element) node;
				if (payTime.getFirstChild() != null) {
					updateInfo.setPayTime(payTime.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (PAY_AMOUNT.equals(node.getNodeName())) {
				Element payAmount = (Element) node;
				if (payAmount.getFirstChild() != null) {
					updateInfo.setPayAmount(payAmount.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (UNITID.equals(node.getNodeName())) {
				Element unitid = (Element) node;
				if (unitid.getFirstChild() != null) {
					updateInfo.setUnitId(unitid.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (EMPLOYEEID.equals(node.getNodeName())) {
				Element employeeId = (Element) node;
				if (employeeId.getFirstChild() != null) {
					updateInfo.setEmployeeId(employeeId.getFirstChild()
							.getNodeValue().trim());
				}
			}
		}
		return updateInfo;
	}

	// taobao1.4.1接口 订单信息更新数据
	private List<UpdateWaybillInfo> parseRootTaoBao(Element root) {

		List<UpdateWaybillInfo> updateInfoList = new ArrayList<UpdateWaybillInfo>();

		// 物流公司编号
		String logisticProviderID = null;

		// 电商标识
		String ecCompanyId = null;

		// 循环取订单信息更新信息
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			// 获取物流公司编号
			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				if (logisticProviderIdEle.getFirstChild() != null)

					// 获取物流公司编号
					logisticProviderID = logisticProviderIdEle.getFirstChild()
							.getNodeValue().trim();
			}

			// 获取电商标识
			if (EC_COMPANY_ID_ELE.equals(node.getNodeName())) {
				Element txLogisticIdEle = (Element) node;
				if (txLogisticIdEle.getFirstChild() != null)

					// 获取电商标识
					ecCompanyId = txLogisticIdEle.getFirstChild()
							.getNodeValue().trim();
			}

			// 解析fieldList节点对象
			if (FIELD_LIST_ELE.equals(node.getNodeName())) {
				Element fieldListEle = (Element) node;
				NodeList fieldNodeList = fieldListEle.getChildNodes();

				// 获取订单更新实体类
				for (int j = 0; j < fieldNodeList.getLength(); j++) {
					UpdateWaybillInfo updateInfo = new UpdateWaybillInfo();

					// 获取订单更新字段
					Node fieldNode = fieldNodeList.item(j);
					if (fieldNode != null
							&& FIELD_ELE.equals(fieldNode.getNodeName())) {
						Element fieldEle = (Element) fieldNode;

						// 更新字段值
						parseFieldEle1(fieldEle, updateInfo,
								logisticProviderID, ecCompanyId);
					}
					if (updateInfo.getFieldName() != null) {
						updateInfoList.add(updateInfo);
					}
				}
			}
		}
		return updateInfoList;
	}

	private UpdateWaybillInfo parseAlterRoot(Element root) {
		UpdateWaybillInfo updateInfo = new UpdateWaybillInfo();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (TX_LOGISTIC_ID_ELE.equals(node.getNodeName())) {
				Element txLogisticIdEle = (Element) node;
				if (txLogisticIdEle.getFirstChild() != null)
					updateInfo.setTxLogisticId(txLogisticIdEle.getFirstChild()
							.getNodeValue().trim());
			}

			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				if (logisticProviderIdEle.getFirstChild() != null)
					updateInfo.setLogisticProviderId(logisticProviderIdEle
							.getFirstChild().getNodeValue().trim());
			}
			if (INFO_TYPE_ELE.equals(node.getNodeName())) {
				Element infoTypeEle = (Element) node;
				if (infoTypeEle.getFirstChild() != null)
					updateInfo.setInfoType(infoTypeEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (INFO_CONTENT_ELE.equals(node.getNodeName())) {
				Element infoContentEle = (Element) node;
				if (infoContentEle.getFirstChild() != null)
					updateInfo.setInfoContent(infoContentEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (FIELD_LIST_ELE.equals(node.getNodeName())) {
				Element fieldListEle = (Element) node;
				NodeList fieldNodeList = fieldListEle.getChildNodes();

				for (int j = 0; j < fieldNodeList.getLength(); j++) {
					Node fieldNode = fieldNodeList.item(j);

					if ((fieldNode != null)
							&& (FIELD_ELE.equals(fieldNode.getNodeName()))) {
						Element fieldEle = (Element) fieldNode;
						this.parseFieldEle(fieldEle, updateInfo);
					}
				}
			}
		}
		return updateInfo;
	}

	private void parseFieldEle(Element ele, UpdateWaybillInfo updateInfo) {
		NodeList nodeList = ele.getChildNodes();
		String fieldName = null;
		String fieldValue = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (FIELD_NAME_ELE.equals(node.getNodeName())) {
				Element fieldNameEle = (Element) node;

				if (fieldNameEle.getFirstChild() != null) {
					fieldName = fieldNameEle.getFirstChild().getNodeValue()
							.trim();
				}

			}
			if (FIELD_NAME_ELE.equals(node.getNodeName())) {
				Element fieldNameEle = (Element) node;

				if (fieldNameEle.getFirstChild() != null) {
					fieldName = fieldNameEle.getFirstChild().getNodeValue()
							.trim();
				}

			}
			if (FIELD_VALUE_ELE.equals(node.getNodeName())) {
				Element fieldValueEle = (Element) node;

				if (fieldValueEle.getFirstChild() != null) {
					fieldValue = fieldValueEle.getFirstChild().getNodeValue()
							.trim();
				}

			}

			if (StringUtils.equals(fieldName, "mailNo")) {
				if (!StringUtils.isEmpty(fieldValue)) {
					updateInfo.setMailNo(fieldValue);
				}

			} else if (StringUtils.equals(fieldName, "weight")) {
				if (!StringUtils.isEmpty(fieldValue)) {
					updateInfo.setWeight(Double.valueOf(fieldValue));
				}
			}

		}

	}

	/**
	 * 淘宝接口1.4.1版本解析file节点
	 * 
	 * @param Element
	 *            UpdateWaybillInfo
	 * @return
	 * @author liuchunyan
	 */
	private void parseFieldEle1(Element ele, UpdateWaybillInfo updateInfo,
			String logisticProviderID, String ecCompanyId) {
		NodeList nodeList = ele.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			// 获取更新字段名
			if (TX_LOGISTIC_ID_ELE.equals(node.getNodeName())) {
				Element fileNameEle = (Element) node;
				if (fileNameEle.getFirstChild() != null)
					updateInfo.setTxLogisticId(fileNameEle.getFirstChild()
							.getNodeValue().trim());
			}

			// 获取更新字段名
			if (FIELD_NAME_ELE.equals(node.getNodeName())) {
				Element fileNameEle = (Element) node;
				if (fileNameEle.getFirstChild() != null)
					updateInfo.setFieldName(fileNameEle.getFirstChild()
							.getNodeValue().trim());
			}

			// 获取更新字段值
			if (FIELD_VALUE_ELE.equals(node.getNodeName())) {
				Element fileValueEle = (Element) node;
				if (fileValueEle.getFirstChild() != null)
					updateInfo.setFieldValue(fileValueEle.getFirstChild()
							.getNodeValue().trim());
			}

			// 获取备注信息
			if (REMARK_ELE.equals(node.getNodeName())) {
				Element remarkEle = (Element) node;
				if (remarkEle.getFirstChild() != null) {
					updateInfo.setRemark(remarkEle.getFirstChild()
							.getNodeValue().trim());
				}
			}

		}

		if (StringUtils.isNotBlank(updateInfo.getFieldName())) {
			
			// 获取物流公司编号
			if (logisticProviderID != null) {
				updateInfo.setLogisticProviderId(logisticProviderID);
			}

			// 获取电商标识
			if (ecCompanyId != null) {
				updateInfo.setClientId(ecCompanyId);
			}

			// 订单面单号更新字段设置
			if (MAILNO.equals(updateInfo.getFieldName())) {
				if (StringUtils.isNotBlank(updateInfo.getFieldValue())) {

					// 设置新面单号
					updateInfo.setMailNo(updateInfo.getFieldValue());
				}
			}
			// 重量更新
			else if (WEIGHT.equals(updateInfo.getFieldName())) {
				if (StringUtils.isNotBlank(updateInfo.getFieldValue())) {

					// 设置新面单号
					//updateInfo.setWeight(Double.valueOf(updateInfo.getFieldValue()));
					updateInfo.setWeigthStr(updateInfo.getFieldValue());
				}
			}

			// 取消订单
			else if (STATUS.equals(updateInfo.getFieldName())) {

				if (WITHDRAW.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(WITHDRAW);
				}
				// 接单成功
				else if (ACCEPT.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(ACCEPT);
				}
				
				// 接单失败
				else if (UNACCEPT.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(UNACCEPT);
				}
				// 签收成功
				else if (SIGNED.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(SIGNED);
				}
				
				// 签收失败
				else if (FAILED.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(FAILED);
				}
				
				// 揽收成功
				else if (GOT.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(GOT);
				}
				
				// 揽收失败
				else if (NOT_SEND.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(NOT_SEND);
				}
				
				// 派件扫描成功
				else if (SENT_SCAN.equals(updateInfo.getFieldValue())) {
					// 获取物流公司编号
					if (StringUtils.isNotBlank(logisticProviderID)) {
						updateInfo.setLogisticProviderId(logisticProviderID);
					}

					// 获取电商标识
					if (StringUtils.isNotBlank(ecCompanyId)) {
						updateInfo.setClientId(ecCompanyId);
					}

					updateInfo.setInfoContent(SENT_SCAN);
				}
			}
		}
	}
}
