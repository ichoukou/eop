package net.ytoec.kernel.action.remote.process;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.ytoec.kernel.common.Resource;

public class IpWhiteListProcessor {

	private static Logger logger = Logger.getLogger(IpWhiteListProcessor.class);

	public static boolean checkIp(String clientId, String ip) {

		String ips = Resource.getIp(clientId);// 获得渠道信息表中的ip 可能是好几个ip连接在一起的形式使用
												// 封号隔开

		if (StringUtils.isEmpty(ips)) {
			logger.error("渠道信息没有ips信息" + ip + "  clientId:" + clientId);
			return false;
		}

		String[] ipArray = ips.split(";");
		boolean ipFlag = false;
		for (int i = 0; i < ipArray.length; i++) {
			if (StringUtils.equals(ip, ipArray[i])) {
				ipFlag = true;
				break;
			}
		}
		if (!ipFlag) {
			if (StringUtils.equalsIgnoreCase(clientId, ips)) {
				logger.debug("动态Ip用户");
				return true ;
			}
			logger.error("非法用户访问！ip：" + ip + "  clientId:" + clientId);
			return false;
		}
		return true;
	}
}
