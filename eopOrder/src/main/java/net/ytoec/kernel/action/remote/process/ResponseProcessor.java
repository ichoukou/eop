package net.ytoec.kernel.action.remote.process;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.action.remote.xml.Response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * response元素处理类.
 * 
 */
public class ResponseProcessor implements XmlFragmentProcessor {
	// root element.
	private static final String RESPONSE_ELE = "response";
	private static final String TX_LOGISTIC_ID_ELE = "txLogisticID";

	private static final String LOGISTIC_PROVIDER_ID_ELE = "logisticProviderID";
	private static final String FIELDNAME_ELE = "fieldName";
	private static final String CUSTOMER_ID_ELE = "customerId";
	private static final String REQUEST_NO_ELE = "requestNo";

	private static final String SUCCESS_ELE = "success";

	private static final String REASON_ELE = "reason";
	private static final String RESPONSEITEMS_ELE = "responseItems";

	private DocumentReader documentReader = new DocumentReader();

	public Response parse(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseRoot(root);
	}

	public List<Response> parseTaoBao(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseToResponseTaoBao(root);
	}

	private Response parseRoot(Element root) {
		Response response = new Response();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (TX_LOGISTIC_ID_ELE.equals(node.getNodeName())) {
				Element txLogisticIdEle = (Element) node;
				response.setTxLogisticId(txLogisticIdEle.getFirstChild()
						.getNodeValue().trim());
			}
			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				response.setLogisticProviderId(logisticProviderIdEle
						.getFirstChild().getNodeValue().trim());
			}
			if (SUCCESS_ELE.equals(node.getNodeName())) {
				Element successEle = (Element) node;
				response.setSuccess(successEle.getFirstChild().getNodeValue()
						.trim());
			}
			if (REASON_ELE.equals(node.getNodeName())) {
				Element reasonEle = (Element) node;
				response.setReason(reasonEle.getFirstChild().getNodeValue()
						.trim());
			}
		}

		return response;
	}

	/**
	 * 淘宝接口1.4.1版本淘宝and金刚返回的响应文本
	 * 
	 * @param Element
	 *            root
	 * @return
	 * @author lcy
	 */
	private List<Response> parseToResponseTaoBao(Element root) {
		
		List<Response> responseList = new ArrayList<Response>();
		
		// 获取文本节点集合
		NodeList nodeList = root.getChildNodes();
		
		// 获取对象结果集
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			Node node = nodeList.item(i);
			
			if (RESPONSEITEMS_ELE.equals(node.getNodeName())) {
				Element responseItemsEle = (Element) node;
				
				NodeList responseNodeList = responseItemsEle.getChildNodes();
				
				// 获取实体类
				for (int j = 0; j < responseNodeList.getLength(); j++) {
					
					Response response = new Response();
					
					Node responseNode = responseNodeList.item(j);

					if (responseNode != null
							&& RESPONSE_ELE.equals(responseNode.getNodeName())) {
						
						Element responseEle = (Element) responseNode;
						parseFieldEle(responseEle, response);
					}
					if (response.getTxLogisticId() != null) {
						responseList.add(response);
					}
				}
			}
		}
		return responseList;
	}
	
	/**
	 * 淘宝接口1.4.1版本解析file节点
	 * 
	 * @param Element
	 *            UpdateWaybillInfo
	 * @return
	 * @author lcy
	 */
	private void parseFieldEle(Element ele,Response response) {
		NodeList nodeList = ele.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
	
				if (TX_LOGISTIC_ID_ELE.equals(node.getNodeName())) {
					Element fileNameEle = (Element) node;
					if (fileNameEle.getFirstChild() != null)
						response.setTxLogisticId(fileNameEle.getFirstChild()
								.getNodeValue().trim());
				}
	
				if (SUCCESS_ELE.equals(node.getNodeName())) {
					Element fileNameEle = (Element) node;
					if (fileNameEle.getFirstChild() != null)
						response.setSuccess(fileNameEle.getFirstChild()
								.getNodeValue().trim());
				}
	
				if (REASON_ELE.equals(node.getNodeName())) {
					Element fileValueEle = (Element) node;
					if (fileValueEle.getFirstChild() != null)
						response.setReason(fileValueEle.getFirstChild()
								.getNodeValue().trim());
				}
				response.setLogisticProviderId("YTO");
			}
		}

	public Response parseApplyKdbz(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseRootApplyKdbz(root);
	}

	private Response parseRootApplyKdbz(Element root) {
		Response response = new Response();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (LOGISTIC_PROVIDER_ID_ELE.equals(node.getNodeName())) {
				Element logisticProviderIdEle = (Element) node;
				response.setLogisticProviderId(logisticProviderIdEle
						.getFirstChild().getNodeValue().trim());
			}
			if (CUSTOMER_ID_ELE.equals(node.getNodeName())) {
				Element customerIdEle = (Element) node;
				response.setCustomerId(customerIdEle.getFirstChild()
						.getNodeValue().trim());
			}
			if (REQUEST_NO_ELE.equals(node.getNodeName())) {
				Element requestNoEle = (Element) node;
				response.setRequestNo(requestNoEle.getFirstChild()
						.getNodeValue().trim());
			}
			if (SUCCESS_ELE.equals(node.getNodeName())) {
				Element successEle = (Element) node;
				response.setSuccess(successEle.getFirstChild().getNodeValue()
						.trim());
			}
			if (REASON_ELE.equals(node.getNodeName())) {
				Element reasonEle = (Element) node;
				response.setReason(reasonEle.getFirstChild().getNodeValue()
						.trim());
			}
		}

		return response;
	}

}
