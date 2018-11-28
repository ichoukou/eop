package com.sun.mail.util;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

/**
 * @作者：罗典
 * @时间：2013-07-25
 * @功能：object对象与xml格式数据相互转换
 * */
public class XmlJaxbMapper {
	private static Map<String, JAXBContext> jaxbContextMap = new HashMap<String, JAXBContext>();

	/**
	 * @作者：罗典
	 * @时间：2013-07-25
	 * @功能：由对象转换为xml格式字符串
	 * @参数：Obj 需转换对象， QName xml命名空间
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String writeValue(Object obj, QName qName) throws JAXBException {
		StringWriter writer = new StringWriter();
		JAXBContext jaxbContext = getJaxbContext(obj.getClass());
		JAXBElement element = new JAXBElement(qName, obj.getClass(), null, obj);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		jaxbMarshaller.marshal(element, writer);
		writer.flush();
		return writer.getBuffer().toString();
	}

	/**
	 * @作者：罗典
	 * @时间：2013-07-25
	 * @功能：由对象转换为xml格式字符串
	 * @参数：Obj 需转换对象， QName xml命名空间,不传则默认为无命名空间
	 * */
	public static String writeValue(Object obj) throws JAXBException {
		QName qName = new QName(obj.getClass().getSimpleName().toString(), "");
		return writeValue(obj, qName);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-07-25
	 * @功能：由xml格式字符串转换为对象
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object readValue(String xmlString, Class clazz)
			throws JAXBException {
		ByteArrayInputStream stream = new ByteArrayInputStream(
				xmlString.getBytes());
		JAXBContext jaxbContext = getJaxbContext(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		JAXBElement element = jaxbUnmarshaller.unmarshal(new StreamSource(
				stream), clazz);
		return element.getValue();
	}

	@SuppressWarnings({ "rawtypes" })
	private static JAXBContext getJaxbContext(Class clazz) throws JAXBException {
		String className = clazz.getSimpleName();
		JAXBContext jaxbContext = null;
		if (jaxbContextMap.containsKey(className)) {
			return jaxbContextMap.get(className);
		} else {
			jaxbContext = JAXBContext.newInstance(clazz);
			jaxbContextMap.put(className, jaxbContext);
		}
		return jaxbContext;
	}
}
