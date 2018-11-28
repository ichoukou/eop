package net.ytoec.kernel.action.test;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.remote.AbstractInterfaceAction;

@Component
public class LongyongAction extends AbstractInterfaceAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String xmlString;
	private String xmlResult;

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}

	public String getXmlResult() {
		return xmlResult;
	}

	public void setXmlResult(String xmlResult) {
		this.xmlResult = xmlResult;
	}

	public String encryptionXml() throws IOException {
		if (StringUtils.isEmpty(xmlString)) {
			xmlResult="empty";
		}
		xmlResult = Md5Encryption.MD5Encode(xmlString.trim());
		
		xmlResult= java.net.URLEncoder.encode(xmlResult, "UTF-8");
		
		return "success";

	}
	
	
}
