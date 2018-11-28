package net.ytoec.kernel.common;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

public class WebServiceUtils {

	/**
	 * 设置超时时间，单位为毫秒
	 */
	public static int TIMEOUT = 10 * 60 * 1000;

	// ----------------------------------------------------- Constructors

	// ----------------------------------------------------- Methods
	/**
	 * 
	 * 调用axis2的webservice的方法
	 * 
	 * @param url
	 *            访问接口地址
	 * @param nameSpace
	 *            包路径
	 * @param method
	 *            访问接口方法
	 * @param args
	 *            [] 数组参数
	 * @param returnTypes
	 *            [] 接收数组类
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object[] invokeWebService(String url, String nameSpace,
			String method, Object[] args, Class[] returnTypes) throws AxisFault {
		RPCServiceClient serviceClient = new RPCServiceClient();// 此处RPCServiceClient
																// 对象实例建议定义成类中的static变量，否则多次调用会出现连接超时的错误。
		Options options = serviceClient.getOptions();
		EndpointReference targetEPR = new EndpointReference(url);
		options.setTo(targetEPR);
		options.setTimeOutInMilliSeconds(TIMEOUT);
		QName opName = new QName(nameSpace, method);
		Object[] results = serviceClient.invokeBlocking(opName, args,
				returnTypes);
		return results;
	}
}
