package net.ytoec.kernel.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtil {
	
	private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
	public static void attr2Prop(Element elem, Object obj, String attrName, String defaultValue) {
		String attrValue = elem.attributeValue(attrName);
		if (attrValue != null) {
			ObjectUtils.setProperties(obj, attrName, attrValue);
		} else {
			ObjectUtils.setProperties(obj, attrName, defaultValue);
		}
	}

	public static void attr2Prop(Element elem, Object obj) {
		List list = elem.attributes();
		for (int i = 0; i < list.size(); i++) {
			Attribute attr = (Attribute) list.get(i);
			if (attr.getValue() != null) {
				ObjectUtils.setProperties(obj, attr.getName(), attr.getValue());
			}
		}
	}
	
	public static String outputXml(Object obj) {
		if (obj == null) {
			return "ERROR: the object is null.";
		}
		StringBuffer buf = new StringBuffer();
		try {
			Map map = PropertyUtils.describe(obj);
			Set set = map.entrySet();
			buf.append("<?xml version='1.0' encoding='UTF-8'?> \n");
			buf.append("<java version=" + System.getProperty("java.version")
					+ " class=" + obj.getClass().getName() + ">\n");
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				Map.Entry element = (Map.Entry) iter.next();
				if ("class".equalsIgnoreCase(element.getKey().toString())) {
					continue;
				}
				buf.append("  <" + element.getKey().toString() + ">"
						+ element.getValue() + "</"
						+ element.getKey().toString() + ">\n");
			}
			buf.append("</java>");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.debug(buf.toString());
		return buf.toString();
	} // outputXml
	/*
	 * 对象转换成xml
	 * 
	 */
	public static String XmlEncoder(Object obj) {
		// OutputFormatter.outputFields(obj);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder xmlEncoder = new XMLEncoder(baos);
		xmlEncoder.writeObject(obj);
		xmlEncoder.close();

		String xml = "";
		try {
			xml = baos.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		logger.debug(xml);
		return xml;
	}

	public static void objectXmlEncoder(Object obj, String fileName)
			throws FileNotFoundException, IOException, Exception {
		// 创建输出文件
		File fo = new File(fileName);
		// 文件不存在,就创建该文件
		if (!fo.exists()) {
			// 先创建文件的目录
			String path = fileName.substring(0, fileName.lastIndexOf('.'));
			File pFile = new File(path);
			pFile.mkdirs();       //创建此抽象路径名指定的目录，包括创建必需但不存在的父目录
		}
		// 创建文件输出流
		FileOutputStream fos = new FileOutputStream(fo);
		// 创建XML文件对象输出类实例
		XMLEncoder encoder = new XMLEncoder(fos);
		// 对象序列化输出到XML文件
		encoder.writeObject(obj);
		encoder.flush();
		// 关闭序列化工具
		encoder.close();
		// 关闭输出流
		fos.close();
	}

	/*
	 * 将string类型字符串转换成对象
	 */
	public static Object xmlDecoder(String objSource)
			throws FileNotFoundException, IOException, Exception {
/*		FileUtil.writeFile(objSource, FileUtil.getWebRoot() + "/" + fileName+".xml");
		String name = FileUtil.getWebRoot() + "/" + fileName+".xml";
		File fin = new File(name);*/
//		FileInputStream fis = new FileInputStream(fin);
		
		//20100930 liug
		ByteArrayInputStream inputstream = new ByteArrayInputStream(objSource.getBytes("utf-8"));
		
//		StringBufferInputStream inputstream = new StringBufferInputStream(objSource);

		XMLDecoder decoder = new XMLDecoder(inputstream);
		Object obj = (Object)decoder.readObject();


//		fis.close();
		inputstream.close();
		decoder.close();
//		FileUtil.delFile(fin);
		
		return obj;
	}
	
	/**
	 * 将传入的xml字符串解析成对象集合输出
	 * @param xmlStr	
	 * 		xml字符串<b>该xml字符串要遵循一定规律. 见方法内注释. </b>
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static List xmlDecoder2List(String xmlStr)
		throws FileNotFoundException, IOException, Exception {
		/*
		 * xmlStr格式：
		<?xml version="1.0" encoding="UTF-8"?>
		<java version="1.6.0_16" class="java.beans.XMLDecoder">
			<object class="net.ytoec.kernel.dataobject.Postinfo">
				<void property="firstWeightPirce">
					<float>10.0</float>
				</void>
				<void property="overWeightPirce">
					<float>3.0</float>
				</void>
				<void property="provs">
					<string>上海市,北京市</string>
				</void>
			</object>
		</java>
		 */
		ByteArrayInputStream inputstream = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
		
		XMLDecoder decoder = new XMLDecoder(inputstream);
		Object obj = null;//(Object)decoder.readObject();
		List objList = new ArrayList();
		try {
			while ((obj = decoder.readObject()) != null) {
				objList.add(obj);
			}
		} catch (Exception e) {
			
		}
		inputstream.close();
		decoder.close();
		
		return objList;
	} // xmlDecoder2List

	/**
	 * 读取由objSource指定的XML文件中的序列化保存的对象,返回的结果经过了List封装
	 * 
	 * @param objSource
	 *            带全部文件路径的文件全名
	 * @return 由XML文件里面保存的对象构成的List列表(可能是一个或者多个的序列化保存的对象)
	 * @throws FileNotFoundException
	 *             指定的对象读取资源不存在
	 * @throws IOException
	 *             读取发生错误
	 * @throws Exception
	 *             其他运行时异常发生
	 */
	public static List objectXmlDecoder(String objSource)
			throws FileNotFoundException, IOException, Exception {
		List objList = new ArrayList();
		File fin = new File(objSource);
		FileInputStream fis = new FileInputStream(fin);
		XMLDecoder decoder = new XMLDecoder(fis);
		Object obj = null;
		try {
			while ((obj = decoder.readObject()) != null) {
				objList.add(obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		fis.close();
		decoder.close();
		return objList;
	}
	public static void outputXML(Object obj) {
//		OutputFormatter.outputFields(obj);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder xmlEncoder = new XMLEncoder(baos);
		xmlEncoder.writeObject(obj);
		xmlEncoder.close();

		String xml = baos.toString();
		logger.debug(xml);

	}
	public static void main(String[] args) {
//		Order Order=new Order();
//		Postinfo info= new Postinfo();
//		info.setFirstWeightPirce(10);
//		info.setOverWeightPirce(3);
//		info.setProvs("上海市,北京市");
//		Order.setClientId("360buy");
//		Order.setLogisticProviderId("ddd");
////		Order.addItem(new Product());
////		SpecialType ss =;
//		Order.setSpecial(SpecialType.valueOfByCode("0"));
//		String s = XmlEncoder(info);
		String ss="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
+"<java version=\"1.6.0_16\" class=\"java.beans.XMLDecoder\">" 
+"<object class=\"net.ytoec.kernel.dataobject.Postinfo\"> "
+"<void property=\"firstWeightPirce\"> "
+" <float>10.0</float> "
+"</void> "
+"<void property=\"overWeightPirce\">" 
+" <float>3.0</float> "
+"</void> "
+"<void property=\"provs\">" 
+" <string>上海市,北京市</string>" 
+"</void> "
+"</object>" 
+"<object class=\"net.ytoec.kernel.dataobject.Postinfo\"> "
+"<void property=\"firstWeightPirce\"> "
+" <float>111.0</float> "
+"</void> "
+"<void property=\"overWeightPirce\">" 
+" <float>11.0</float> "
+"</void> "
+"<void property=\"provs\">" 
+" <string>湖南省</string>" 
+"</void> "
+"</object>" 
+"</java> ";
		try {
//			List list =xmlDecoder2List(s);
//			for (Object object : list) {
//				logger.debug(object);
//			}
			logger.debug(xmlDecoder(ss).toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}

	}
}
