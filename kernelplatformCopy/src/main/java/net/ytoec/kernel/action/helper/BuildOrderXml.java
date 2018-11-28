package net.ytoec.kernel.action.helper;

import net.ytoec.kernel.dataobject.Trace;
import net.ytoec.kernel.dataobject.TracesElement;
import net.ytoec.kernel.dataobject.TracesList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

public class BuildOrderXml {

	public static Logger logger = LoggerFactory.getLogger(BuildOrderXml.class);
	
		
	public TracesList getObjectFromXml(String xml){
		logger.error(xml);
		XStream xStream = new XStream();
		xStream.alias("tracesList", TracesList.class);
		xStream.alias("tracesElement", TracesElement.class);
		xStream.alias("trace", Trace.class);
		
		TracesList tracesList = (TracesList)xStream.fromXML(xml);
		return tracesList;
	}
	
//	public static void main(String[] args){
//		BuildOrderXml xml = new BuildOrderXml();
//		TracesList tracesList = xml.getObjectFromXml("<tracesList><tracesList><tracesElement><logisticProviderID>STO</logisticProviderID><mailNos>20126754450,20126754501</mailNos><traces><trace><time>2010-05-13 22:49:41</time><desc>到达申通快递杭州中转站</desc><city>杭州</city><facilityType>2</facilityType><facilityNo>WT26684562</facilityNo><facilityName>杭州中转站</facilityName><action>ARRIVAL</action></trace><trace><time>2010-05-14 02:49:41</time><desc>到达申通快递杭州文三网点</desc><city>杭州</city><facilityType>1</facilityType><facilityNo>HZ26456741</facilityNo><facilityName>杭州中转站</facilityName><action>SENT_SCAN</action></trace></traces></tracesElement><tracesElement><logisticProviderID>STO</logisticProviderID><mailNos>20125212578</mailNos><traces><trace><time>2010-09-08 12:31:22</time><desc>到达北京王府井网点</desc><city>北京</city><facilityType>1</facilityType><facilityNo>BJ08964829</facilityNo><facilityName>王府井网点</facilityName><action>GOT</action></trace></traces></tracesElement></tracesList></tracesList>");
//		logger.error(tracesList);
//		TracesElement trace = tracesList.getTracesList().get(0);
//		Trace s = trace.getTraces().get(0);
//		logger.error(s.getTime());
//	}
	
	
}
