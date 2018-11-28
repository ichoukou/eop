package net.ytoec.kernel.action.api;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;

public class XMLUtils {

	public static void main(String[] args) {
		// User user = new User();
		// user.setId(1);
		// user.setAddressCity("11");
		// XMLUtils xMLUtils = new XMLUtils(User.class);
		// String xml = xMLUtils.toXml(user);
		// System.out.println(xml);
		//
		// user = xMLUtils.toObject(xml);
		// System.out.println(user);
		XMLUtils xMLUtils = new XMLUtils(MailNoResponse.class);
		MailNoResponse response = new MailNoResponse();
		List<String> mailNoList = new ArrayList<String>();
		mailNoList.add("1");
		mailNoList.add("2");
		response.setMailNoList(mailNoList);

		response.setCustomerCode("1");
		response.setSequence("11");
		response.setMessage("ok");

		String xml = xMLUtils.toXml(response);
		System.out.println(xml);

		Object object = xMLUtils.toObject(xml);
		System.out.println(object);

	}

	private static final String ENCODE = "UTF-8";
	private JaxbBinder binder;

	public XMLUtils(Class<?>... types) {
		binder = new JaxbBinder(types);
	}

	/**
	 * 解析XML内容并生成实体类对象
	 * 
	 * @param xml
	 * @param <T>
	 * @return
	 */
	public <T> T toObject(String xml) {
		T t = null;
		if (!StringUtils.isBlank(xml)) {
			t = binder.fromXml(xml);
		}
		return t;
	}

	/**
	 * 根据实体类对象生成xml
	 * 
	 * @param t
	 * @param <T>
	 * @return
	 */
	public <T> String toXml(T t) {
		String xml = null;
		if (t != null) {
			xml = binder.toXml(t, ENCODE);
		}
		return xml;
	}

	/**
	 * 使用Jaxb2.0实现XML<->Java Object的Binder.
	 */
	private class JaxbBinder {
		private JAXBContext jaxbContext;

		/**
		 * @param types
		 *            所有需要序列化的Root对象的类型.
		 */
		public JaxbBinder(Class<?>... types) {
			try {
				jaxbContext = JAXBContext.newInstance(types);
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Java Object->Xml.
		 */
		public String toXml(Object root, String encoding) {
			try {
				StringWriter writer = new StringWriter();
				createMarshaller(encoding).marshal(root, writer);
				return writer.toString();
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * Xml->Java Object.
		 */
		@SuppressWarnings("unchecked")
		public <T> T fromXml(String xml) {
			try {
				StringReader reader = new StringReader(xml);
				return (T) createUnmarshaller().unmarshal(reader);
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 创建Marshaller, 设定encoding(可为Null).
		 */
		public Marshaller createMarshaller(String encoding) {
			try {
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				if (StringUtils.isNotBlank(encoding)) {
					marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
				}
				return marshaller;
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}

		/**
		 * 创建UnMarshaller.
		 */
		public Unmarshaller createUnmarshaller() {
			try {
				return jaxbContext.createUnmarshaller();
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
