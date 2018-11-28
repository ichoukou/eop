package net.ytoec.kernel.action.remote;

import java.io.IOException;
import java.io.InputStream;

import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.dataobject.User;

import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.log4j.Logger;

/**
 * 
 * 用户信息同步
 * 
 */
@Controller
@Scope("prototype")
public class UserInfoProcessor extends AbstractInterfaceAction {
	
	private static Logger logger=Logger.getLogger(UserInfoProcessor.class);
	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";
	private static final String DATA_DIGEST_PARAM = "data_digest";
	private static final String CLIENT_ID_PARAM = "clientID";
	private static final String GET_METHOD_RESPONSE = "Success";
	// root element.
	private static final String USER_NAME="userName";
	private static final String USER_PASSWORD="userPassword";
	private static final String TELE_PHONE="telePhone";
	private static final String MOBILE_PHONE="mobilePhone";
	private static final String ADDRESS_PROVINCE="address_province";
	private static final String ADDRESS_CITY="address_city";
	private static final String ADDRESS_DISTRICT="address_district";
	private static final String ADDRESS_STREET="address_street";
	private static final String SEX="sex";
	private static final String SHOP_NAME="shop_name";
	private static final String SHOP_ACCOUNT="shop_account";
	private static final String MAIL="mail";
	private static final String CARD_TYPE="card_type";
	private static final String CARD_NO="card_no";
	private static final String USER_TYPE="user_type";
	private static final String USER_SOURCE="user_source";
	private static final String USER_STATE="user_state";
	private static final String USER_LEVEL="userLevel";
	private static final String REMARK="remark";
	private static final String SITE="site";
	private static final String USER_NAME_TEXT="user_name_text";

	private DocumentReader documentReader = new DocumentReader();
	
	public String userInfoUpdateServlet() throws IOException{
		logger.debug("*************************************************************************");
		if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
			super.print(GET_METHOD_RESPONSE);
			return null;
		}

		// 1.获取参数值.
		String logisticsInterface = super.request
				.getParameter(LOGISTICS_INTERFACE_PARAM);
		String dataDigest = super.request.getParameter(DATA_DIGEST_PARAM);
		// 2.转码
		String decodeLogisticsInterface = ProcessorUtils.decode(logisticsInterface,
				XmlSender.UTF8_CHARSET);
		String decodeDataDigest = ProcessorUtils.decode(dataDigest, XmlSender.UTF8_CHARSET);
		// 3.验证

		if (!ProcessorUtils.validateData(decodeLogisticsInterface, decodeDataDigest,"")) {
			logger.debug("---Validate Fail-------");
			print(Response.DATA_INSECURITY_RESPONSE.toXmlFragment());
			return null;

		}
		return null;	
	}

	public User parse(String xmlFragment) {
		InputStream inputStream = ProcessorUtils.getInputStream(xmlFragment);
		Document document = this.documentReader.getDocument(inputStream);
		Element root = document.getDocumentElement();
		return this.parseRoot(root);
	}

	private  User parseRoot(Element root) {
		User user = new User();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (USER_NAME.equals(node.getNodeName())) {
				Element txLogisticIdEle = (Element) node;
				user.setUserName(txLogisticIdEle.getFirstChild()
						.getNodeValue().trim());
			}
		}
		return user;
	}
	@Test
	public void test(){
		String req="<Users>"+
						"<User>"+
							"<userName>aaa</userName>"+
							"<userPassword>111<userPassword>"+
							"<telePhone>123</telePhone>"+
							"<mobilePhone>321</mobilePhone>"+
							"<sex>1</sex>"+
							"<userLevel></userLevel>"+
							"<type>new</type>"+
						"</User>"+
						"<User>"+
							"<userName>aaa</userName>"+
							"<userPassword>111<userPassword>"+
							"<telePhone>123</telePhone>"+
							"<mobilePhone>321</mobilePhone>"+
							"<sex>1</sex>"+
							"<userLevel></userLevel>"+
							"<type>new</type>"+
						"</User>"+
						"<User>"+
							"<userName>aaa</userName>"+
							"<userPassword>111<userPassword>"+
							"<telePhone>123</telePhone>"+
							"<mobilePhone>321</mobilePhone>"+
							"<sex>1</sex>"+
							"<userLevel></userLevel>"+
							"<type>new</type>"+
						"</User>"+
		           "</Users>";
		
		
	}
}
