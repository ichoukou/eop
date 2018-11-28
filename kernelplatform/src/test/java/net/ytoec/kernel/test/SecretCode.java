package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Constant;
import net.ytoec.kernel.dataobject.MailNoInfo;
import net.ytoec.kernel.dataobject.RequestInfo;

import com.sun.mail.util.XmlJaxbMapper;

public class SecretCode {
	public static void main(String[] args) {
		/*List<String> list = new ArrayList<String>();
		list.add("208609");
		list.add("208606");
		list.add("208605");
		list.add("208602");
//		for (String nnn : list) {
//			String nn = "<MailNoRequest><customerCode>K57500046</customerCode><sequence>209609</sequence><success>false</success></MailNoRequest>"
		String nn = createWaybills();*/
			String password = "123456";
			String parentCode = "parentCode";
			String newDataDigest = Md5Encryption.MD5Encode(password + parentCode,
					Constant.CHARSET_UTF8);
			System.out.println(newDataDigest);

//		}

	}

	public static String createWaybills() {
		RequestInfo info = new RequestInfo();
		List<MailNoInfo> list = info.getMailNoList();
		for (int i = 100000; i < 100050; i++) {
			MailNoInfo Mailinfo = new MailNoInfo();
			Mailinfo.setCreateTime(new Date()); 
			Mailinfo.setCustomerCode("Ke0000132");
			Mailinfo.setWaybillNo("TE" + i);
			list.add(Mailinfo);
		}
		String nnnnn = "";
		try {
			QName _RequestOrder_QNAME = new QName("", "ResponseInfo");
			nnnnn = XmlJaxbMapper.writeValue(info,_RequestOrder_QNAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nnnnn;
	}
}
