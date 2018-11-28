package net.ytoec.kernel.action.common;

import java.io.InputStream;

import net.ytoec.kernel.action.remote.BatchQueryRequest;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.process.XmlFragmentProcessor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * BatchQueryRequest元素处理类.
 * 
 */
public class BatchQueryRequestProcessor implements XmlFragmentProcessor {
	// root element.
	private static final String BATCH_QUERY_REQUEST_ELE = "BatchQueryRequest";
	//
	private static final String LOGISTIC_PROVIDER_ID_ELE = "logisticProviderID";
	// 欲查询的订单列表
	private static final String ORDERS_ELE = "orders";
	// 欲查询的订单
	private static final String ORDER_ELE = "order";
	// 运单号
	private static final String MAIL_NO_ELE = "mailNo";

	private DocumentReader documentReader = new DocumentReader();

	public BatchQueryRequest parse(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseRoot(root);
	}

	private BatchQueryRequest parseRoot(Element root) {
		BatchQueryRequest batchQueryRequest = new BatchQueryRequest();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				batchQueryRequest.setLogisticProviderId(logisticProviderIdEle
						.getFirstChild().getNodeValue().trim());
			}
			if (ORDERS_ELE.equals(node.getNodeName())) {
				Element orderEle = (Element) node;
				NodeList orderNodeList = orderEle.getChildNodes();
				for (int j = 0; j < orderNodeList.getLength(); j++) {
					Node orderNode = orderNodeList.item(j);
					if (ORDER_ELE.equals(orderNode.getNodeName())) {
						NodeList orderChildNodes = orderNode.getChildNodes();
						for (int m = 0; m < orderChildNodes.getLength(); m++) {
							Node mailNoNode = orderChildNodes.item(m);
							if (MAIL_NO_ELE.equals(mailNoNode.getNodeName())) {
								Element mailNoEle = (Element) mailNoNode;
								batchQueryRequest.setMailNo(mailNoEle
										.getFirstChild().getNodeValue().trim());
							}
						}
					}
				}
			}
		}
		return batchQueryRequest;
		
	}
}
