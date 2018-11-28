package net.ytoec.kernel.action.remote.process;

import java.io.InputStream;
import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.ApplyKdbz;
import net.ytoec.kernel.dataobject.Customer;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.SpecialType;
import net.ytoec.kernel.dataobject.TraderInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * requestOrderProcessor元素处理类.
 * 
 */
public class RequestOrderProcessor implements XmlFragmentProcessor {
	// RequestOrder root element.
	private static final String REQUEST_ORDER_ELE = "RequestOrder";
	private static final String VERSION_ATTR = "version";

	private static final String CLIENT_ID_ELE = "clientID";
	private static final String EC_COMPANY_ID_ELE = "ecCompanyId";
	private static final String REQUEST_NO_ELE = "requestNo";
	private static final String SERVICE_TYPE_ELE = "serviceType";
	private static final String CUSTOMER_ELE = "customerInfo";
	private static final String WANG_WANG_ELE = "wangwang";
	private static final String STATUS_ELE = "status";
	private static final String VIP_ELE = "vip";

	/**
	 * 发货单号
	 */
	private static final String DELIVER_NO_ELE = "deliverNo";
	/**
	 * 发货单 代收款
	 */
	private static final String AGENCY_FUND_ELE = "agencyFund";

	/**
	 * 物流公司ID
	 */
	private static final String LOGISTIC_PROVIDER_ID_ELE = "logisticProviderID";
	/**
	 * 物流平台交易生成的物流号
	 */
	private static final String TX_LOGISTIC_ID_ELE = "txLogisticID";

	private static final String CUSTOMER_ID_ELE = "customerId";

	/**
	 * 业务的交易号（可选）
	 */
	private static final String TRADE_NO_ELE = "tradeNo";
	/**
	 * 物流公司的运单号
	 */
	private static final String MAIL_NO_ELE = "mailNo";
	// 20110901 liug
	private static final String TYPE_ELE = "type";
	//
	private static final String FLAG_ELE = "flag";
	/**
	 * 发货方信息
	 */
	private static final String SENDER_ELE = "sender";
	//
	private static final String NAME_ELE = "name";
	//
	private static final String POST_CODE_ELE = "postCode";
	//
	private static final String PHONE_ELE = "phone";
	//
	private static final String MOBILE_ELE = "mobile";
	//
	private static final String PROV_ELE = "prov";
	//
	private static final String CITY_ELE = "city";
	//
	private static final String ADDRESS_ELE = "address";
	//
	private static final String RECEIVER_ELE = "receiver";
	//
	private static final String SEND_START_TIME_ELE = "sendStartTime";
	//
	private static final String SEND_END_TIME_ELE = "sendEndTime";
	// 20110901 liug
	private static final String ITEMS_VALUE_ELE = "itemsValue";
	// 20110901 liug
	private static final String ITEMS_WEIGHT_ELE = "itemsWeight";
	/**
	 * 货物信息
	 */
	private static final String ITEMS_ELE = "items";
	//
	private static final String ITEM_ELE = "item";
	//
	private static final String ITME_NAME_ELE = "itemName";

	private static final String ITME_VALUE_ELE = "itemValue";
	//
	private static final String NUMBER_ELE = "number";
	//
	private static final String REMARK_ELE = "remark";
	/**
	 * 保价
	 */
	private static final String INSURANCE_VALUE_ELE = "insuranceValue";
	//
	private static final String PACKAGE_OR_NOT_ELE = "packageOrNot";
	/**
	 * 特殊商品性质
	 */
	private static final String SPECIAL_ELE = "special";

	private static final String EC_COMPANY_ID = "ecCompanyId";
	private static final String ORDER_TYPE = "orderType";
	private static final String SERVICE_TYPE = "serviceType";
	private static final String GOODS_VALUE = "goodsValue";
	private static final String TOTAL_SERVICE_FEE = "totalServiceFee";
	private static final String BUY_SERVICE_FEE = "buyServiceFee";
	private static final String COD_SPLIT_FEE = "codSplitFee";

	private static final String TRUE_FLAG = "true";
	private static final String FALSE_FALG = "false";

	private DocumentReader documentReader = new DocumentReader();
	private OrderFormInfo requestOrder;
	private ApplyKdbz applyKdbz;
	String[] eleArr = { "", "" };

	public RequestOrderProcessor(OrderFormInfo requestOrder) {
		this.requestOrder = requestOrder;
	}

	public RequestOrderProcessor(ApplyKdbz applyKdbz) {
		this.applyKdbz = applyKdbz;
	}

	public OrderFormInfo parse(String xmlFragment) {

		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseRoot(root);

	}

	private OrderFormInfo parseRoot(Element ele) {
		// OrderFormInfo requestOrder = new OrderFormInfo();

		if (ele.hasAttribute(VERSION_ATTR)) {
			requestOrder.setVersion(ele.getAttribute(VERSION_ATTR));
		}

		NodeList nodeList = ele.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			/*
			 * if(StringUtil.in(node.getNodeName(), allUrl)){
			 * 
			 * }
			 */
			if (CLIENT_ID_ELE.equals(node.getNodeName())) {
				Element clientIdEle = (Element) node;
				if (clientIdEle.getFirstChild() != null) {
					requestOrder.setClientId(clientIdEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				if (logisticProviderIdEle.getFirstChild() != null) {
					requestOrder.setLogisticProviderId(logisticProviderIdEle
							.getFirstChild().getNodeValue().trim());
				}
			}
			if (TX_LOGISTIC_ID_ELE.equals(node.getNodeName())) {
				Element txLogisticIdEle = (Element) node;
				if (txLogisticIdEle.getFirstChild() != null) {
					requestOrder.setTxLogisticId(txLogisticIdEle
							.getFirstChild().getNodeValue().trim());
				}
			}
			if (CUSTOMER_ID_ELE.equals(node.getNodeName())) {
				Element customerIdEle = (Element) node;
				if (customerIdEle.getFirstChild() != null) {
					requestOrder.setCustomerId(customerIdEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (TRADE_NO_ELE.equals(node.getNodeName())) {
				Element tradeNoEle = (Element) node;
				if (tradeNoEle.getFirstChild() != null) {
					requestOrder.setTradeNo(tradeNoEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (MAIL_NO_ELE.equals(node.getNodeName())) {
				Element mailNoEle = (Element) node;
				// liug
				if (mailNoEle.getFirstChild() != null) {
					requestOrder.setMailNo(mailNoEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (DELIVER_NO_ELE.equals(node.getNodeName())) {
				Element deliverEle = (Element) node;
				if (deliverEle.getFirstChild() != null) {
					requestOrder.setDeliverNo(deliverEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (AGENCY_FUND_ELE.equals(node.getNodeName())) {
				Element agencyFundEle = (Element) node;
				if (agencyFundEle.getFirstChild() != null) {
					requestOrder.setAgencyFundEle(Double.valueOf(agencyFundEle
							.getFirstChild().getNodeValue().trim()));
				}
			}
			if (FLAG_ELE.equals(node.getNodeName())) {
				Element flagEle = (Element) node;
				if (flagEle.getFirstChild() != null) {
					requestOrder.setFlag(flagEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (SENDER_ELE.equals(node.getNodeName())) {
				Element senderEle = (Element) node;
				requestOrder.setSender(this.parseSenderEle(senderEle));
			}
			if (RECEIVER_ELE.equals(node.getNodeName())) {
				Element receiverEle = (Element) node;
				requestOrder.setReceiver(this.parseReceiverEle(receiverEle));
			}
			if (SEND_START_TIME_ELE.equals(node.getNodeName())) {
				Element sendStartTimeEle = (Element) node;
				if (sendStartTimeEle.getFirstChild() != null) {
					requestOrder.setSendStartTime(sendStartTimeEle
							.getFirstChild().getNodeValue().trim());
				}
			}
			if (SEND_END_TIME_ELE.equals(node.getNodeName())) {
				Element sendEndTimeEle = (Element) node;
				if (sendEndTimeEle.getFirstChild() != null) {
					requestOrder.setSendEndTime(sendEndTimeEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (ITEMS_ELE.equals(node.getNodeName())) {
				Element itemsEle = (Element) node;
				NodeList itemNodeList = itemsEle.getChildNodes();

				for (int j = 0; j < itemNodeList.getLength(); j++) {
					Node itemNode = itemNodeList.item(j);

					if ((itemNode != null)
							&& (ITEM_ELE.equals(itemNode.getNodeName()))) {
						Element itemEle = (Element) itemNode;
						requestOrder.addItem(this.parseItemEle(itemEle));
					}
				}
			}

			if (INSURANCE_VALUE_ELE.equals(node.getNodeName())) {
				Element insuranceValueEle = (Element) node;
				if (insuranceValueEle.getFirstChild() != null) {
					requestOrder.setInsuranceValue(Double
							.valueOf(insuranceValueEle.getFirstChild()
									.getNodeValue().trim()));
				}
			}
			if (PACKAGE_OR_NOT_ELE.equals(node.getNodeName())) {
				Element packageOrNotEle = (Element) node;
				if (packageOrNotEle.getFirstChild() != null) {
					requestOrder.setPackageOrNot(this
							.getPackageOrNot(packageOrNotEle.getFirstChild()
									.getNodeValue().trim()));
				}
			}
			if (SPECIAL_ELE.equals(node.getNodeName())) {
				Element specialEle = (Element) node;
				if (specialEle.getFirstChild() != null) {
					requestOrder.setSpecial(SpecialType
							.valueOfByCode(specialEle.getFirstChild()
									.getNodeValue().trim()));
				}

			}
			// 20110901 liug
			if (REMARK_ELE.equals(node.getNodeName())) {
				Element remarkEle = (Element) node;
				if (remarkEle.getFirstChild() != null) {
					requestOrder.setRemark(remarkEle.getFirstChild()
							.getNodeValue().trim());
				}
			}

			if (ORDER_TYPE.equals(node.getNodeName())) {
				Element typeEle = (Element) node;
				if (typeEle.getFirstChild() != null) {
					requestOrder.setOrderType(typeEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (SERVICE_TYPE.equals(node.getNodeName())) {
				Element serviceTypeEle = (Element) node;
				if (serviceTypeEle.getFirstChild() != null) {
					requestOrder.setServiceType(serviceTypeEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (GOODS_VALUE.equals(node.getNodeName())) {
				Element goodsValueEle = (Element) node;
				if (goodsValueEle.getFirstChild() != null) {
					requestOrder.setGoodsValue(goodsValueEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (ITEMS_VALUE_ELE.equals(node.getNodeName())) {
				Element itemsValueEle = (Element) node;
				if (itemsValueEle.getFirstChild() != null) {
					requestOrder.setItemsValue(itemsValueEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (TOTAL_SERVICE_FEE.equals(node.getNodeName())) {
				Element totalServiceFeeEle = (Element) node;
				if (totalServiceFeeEle.getFirstChild() != null) {
					requestOrder.setTotalServiceFee(totalServiceFeeEle
							.getFirstChild().getNodeValue().trim());
				}
			}
			if (BUY_SERVICE_FEE.equals(node.getNodeName())) {
				Element buyServiceFeeEle = (Element) node;
				if (buyServiceFeeEle.getFirstChild() != null) {
					requestOrder.setBuyServiceFee(buyServiceFeeEle
							.getFirstChild().getNodeValue().trim());
				}
			}
			if (COD_SPLIT_FEE.equals(node.getNodeName())) {
				Element codSplitFeeEle = (Element) node;
				if (codSplitFeeEle.getFirstChild() != null) {
					requestOrder.setCodSplitFee(codSplitFeeEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (EC_COMPANY_ID.equals(node.getNodeName())) {
				Element ecIdEle = (Element) node;
				if (ecIdEle.getFirstChild() != null) {
					requestOrder.setClientId(ecIdEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (TYPE_ELE.equals(node.getNodeName())) {
				Element typeEle = (Element) node;
				if (typeEle.getFirstChild() != null) {
					requestOrder.setType(typeEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (ITEMS_WEIGHT_ELE.equals(node.getNodeName())) {
				Element itemsWeightEle = (Element) node;
				if (itemsWeightEle.getFirstChild() != null) {
					requestOrder.setItemsWeight(itemsWeightEle.getFirstChild()
							.getNodeValue().trim());
				}
			}

		}

		return requestOrder;

	}

	public ApplyKdbz parseKdbz(String xmlFragment) {

		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseKdbzRoot(root);

	}

	private ApplyKdbz parseKdbzRoot(Element ele) {

		NodeList nodeList = ele.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (EC_COMPANY_ID_ELE.equals(node.getNodeName())) {
				Element ecCompanyIdEle = (Element) node;
				if (ecCompanyIdEle.getFirstChild() != null) {
					applyKdbz.setEcCompanyId(ecCompanyIdEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				if (logisticProviderIdEle.getFirstChild() != null) {
					applyKdbz.setLogisticProvider(logisticProviderIdEle
							.getFirstChild().getNodeValue().trim());
				}
			}
			if (CUSTOMER_ID_ELE.equals(node.getNodeName())) {
				Element customerIdEle = (Element) node;
				if (customerIdEle.getFirstChild() != null) {
					applyKdbz.setCustomerId(customerIdEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (REQUEST_NO_ELE.equals(node.getNodeName())) {
				Element requestNoEle = (Element) node;
				if (requestNoEle.getFirstChild() != null) {
					applyKdbz.setRequestNo(requestNoEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (SERVICE_TYPE_ELE.equals(node.getNodeName())) {
				Element serviceTypeEle = (Element) node;
				if (serviceTypeEle.getFirstChild() != null) {
					applyKdbz.setServiceType(serviceTypeEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (STATUS_ELE.equals(node.getNodeName())) {
				Element StatusEle = (Element) node;
				if (StatusEle.getFirstChild() != null) {
					applyKdbz.setStatus(StatusEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (CUSTOMER_ELE.equals(node.getNodeName())) {
				Element customerEle = (Element) node;
				applyKdbz.setCustomer(this.parseCustomerEle(customerEle));
			}
			if (VIP_ELE.equals(node.getNodeName())) {
				Element vipEle = (Element) node;
				if (vipEle.getFirstChild() != null) {
					applyKdbz.setVip(vipEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (REMARK_ELE.equals(node.getNodeName())) {
				Element remarkEle = (Element) node;
				if (remarkEle.getFirstChild() != null) {
					applyKdbz.setRemark(remarkEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
		}

		return applyKdbz;

	}

	/**
	 * 是否打包，将字符串格式数据转换为boolean类型数据,只有为"true"是，返回true,其它字符串格式全部返回false.
	 * 
	 * @param packageOrNot
	 * @return
	 */
	private boolean getPackageOrNot(String packageOrNot) {
		if (TRUE_FLAG.equals(packageOrNot)) {
			return true;
		}
		return false;
	}

	/**
	 * 解析item元素.
	 * 
	 * @param ele
	 * @return
	 */
	private Product parseItemEle(Element ele) {
		Product commodityInfo = new Product();
		NodeList nodeList = ele.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (ITME_NAME_ELE.equals(node.getNodeName())) {
				Element itemNameEle = (Element) node;
				if (itemNameEle.getFirstChild() != null)
					commodityInfo.setItemName(itemNameEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (NUMBER_ELE.equals(node.getNodeName())) {
				Element numberEle = (Element) node;
				if (numberEle.getFirstChild() != null)
					commodityInfo.setItemNumber(Integer
							.valueOf(numberEle.getFirstChild().getNodeValue()
									.trim() == null ? "0" : numberEle
									.getFirstChild().getNodeValue().trim()));
			}
			if (REMARK_ELE.equals(node.getNodeName())) {
				Element remarkEle = (Element) node;
				if (remarkEle.getFirstChild() != null) {
					commodityInfo.setRemark(remarkEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (ITME_VALUE_ELE.equals(node.getNodeName())) {
				Element itme_value = (Element) node;
				if (itme_value.getFirstChild() != null) {
					commodityInfo.setItemValue(Double
							.valueOf(itme_value.getFirstChild().getNodeValue()
									.trim() == null ? "0" : itme_value
									.getFirstChild().getNodeValue().trim()));
				}
			}

		}

		return commodityInfo;
	}

	/**
	 * 解析sender元素.
	 * 
	 * @param ele
	 * @return
	 */
	private TraderInfo parseSenderEle(Element ele) {
		TraderInfo sender = new TraderInfo();
		NodeList nodeList = ele.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (NAME_ELE.equals(node.getNodeName())) {
				Element nameEle = (Element) node;
				if (nameEle.getFirstChild() != null)
					sender.setName(nameEle.getFirstChild().getNodeValue()
							.trim());
			}
			if (POST_CODE_ELE.equals(node.getNodeName())) {
				Element postCodeEle = (Element) node;
				if (postCodeEle.getFirstChild() != null) {
					sender.setPostCode(postCodeEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (PHONE_ELE.equals(node.getNodeName())) {
				Element phoneEle = (Element) node;
				if (phoneEle.getFirstChild() != null) {
					sender.setPhone(phoneEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (MOBILE_ELE.equals(node.getNodeName())) {
				Element mobileEle = (Element) node;
				if (mobileEle.getFirstChild() != null) {
					sender.setMobile(mobileEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (PROV_ELE.equals(node.getNodeName())) {
				Element provEle = (Element) node;
				if (provEle.getFirstChild() != null)
					sender.setProv(provEle.getFirstChild().getNodeValue()
							.trim());
				// 20111108
				sender.setNumProv(Resource.getCodeByName(provEle
						.getFirstChild().getNodeValue().trim()));
			}
			if (CITY_ELE.equals(node.getNodeName())) {
				Element cityEle = (Element) node;
				if (cityEle.getFirstChild() != null) {
					String[] str = cityEle.getFirstChild().getNodeValue()
							.trim().split(",");
					if (str.length > 1) {
						sender.setCity(str[0]);
						sender.setDistrict(str[1]);
					} else {
						sender.setCity(cityEle.getFirstChild().getNodeValue()
								.trim());
					}
				}

			}
			if (ADDRESS_ELE.equals(node.getNodeName())) {
				Element addressEle = (Element) node;
				if (addressEle.getFirstChild() != null)
					sender.setAddress(addressEle.getFirstChild().getNodeValue()
							.trim());
			}
		}
		return sender;
	}

	private Customer parseCustomerEle(Element ele) {
		Customer customer = new Customer();
		NodeList nodeList = ele.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (NAME_ELE.equals(node.getNodeName())) {
				Element nameEle = (Element) node;
				if (nameEle.getFirstChild() != null)
					customer.setName(nameEle.getFirstChild().getNodeValue()
							.trim());
			}

			if (PHONE_ELE.equals(node.getNodeName())) {
				Element phoneEle = (Element) node;
				if (phoneEle.getFirstChild() != null)
					customer.setPhone(phoneEle.getFirstChild().getNodeValue()
							.trim());
			}
			if (MOBILE_ELE.equals(node.getNodeName())) {
				Element mobileEle = (Element) node;
				if (mobileEle.getFirstChild() != null) {
					customer.setMobile(mobileEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (WANG_WANG_ELE.equals(node.getNodeName())) {
				Element wangwangEle = (Element) node;
				if (wangwangEle.getFirstChild() != null) {
					customer.setWangwang(wangwangEle.getFirstChild()
							.getNodeValue().trim());
				}
			}
			if (ADDRESS_ELE.equals(node.getNodeName())) {
				Element addressEle = (Element) node;
				if (addressEle.getFirstChild() != null)
					customer.setAddress(addressEle.getFirstChild()
							.getNodeValue().trim());
			}
		}
		return customer;
	}

	/**
	 * 解析receiver元素.
	 * 
	 * @param ele
	 * @return
	 */
	private TraderInfo parseReceiverEle(Element ele) {
		TraderInfo receiver = new TraderInfo();
		NodeList nodeList = ele.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (NAME_ELE.equals(node.getNodeName())) {
				Element nameEle = (Element) node;
				if (nameEle.getFirstChild() != null)
					receiver.setName(nameEle.getFirstChild().getNodeValue()
							.trim());
			}
			if (POST_CODE_ELE.equals(node.getNodeName())) {
				Element postCodeEle = (Element) node;
				if (postCodeEle.getFirstChild() != null)
					receiver.setPostCode(postCodeEle.getFirstChild()
							.getNodeValue().trim());
			}
			if (PHONE_ELE.equals(node.getNodeName())) {
				Element phoneEle = (Element) node;
				if (phoneEle.getFirstChild() != null) {
					receiver.setPhone(phoneEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (MOBILE_ELE.equals(node.getNodeName())) {
				Element mobileEle = (Element) node;
				if (mobileEle.getFirstChild() != null) {
					receiver.setMobile(mobileEle.getFirstChild().getNodeValue()
							.trim());
				}
			}
			if (PROV_ELE.equals(node.getNodeName())) {
				Element provEle = (Element) node;
				if (provEle.getFirstChild() != null)
					receiver.setProv(provEle.getFirstChild().getNodeValue()
							.trim());
				// 20111108
				receiver.setNumProv(Resource.getCodeByName(provEle
						.getFirstChild().getNodeValue().trim()));
			}
			if (CITY_ELE.equals(node.getNodeName())) {
				Element cityEle = (Element) node;
				if (cityEle.getFirstChild() != null) {
					String[] str = cityEle.getFirstChild().getNodeValue()
							.trim().split(",");
					if (str.length > 1) {
						receiver.setCity(str[0]);
						receiver.setDistrict(str[1]);
					} else {
						receiver.setCity(cityEle.getFirstChild().getNodeValue()
								.trim());
					}
				}

			}
			if (ADDRESS_ELE.equals(node.getNodeName())) {
				Element addressEle = (Element) node;
				if (addressEle.getFirstChild() != null)
					receiver.setAddress(addressEle.getFirstChild()
							.getNodeValue().trim());
			}
		}
		return receiver;
	}

}
