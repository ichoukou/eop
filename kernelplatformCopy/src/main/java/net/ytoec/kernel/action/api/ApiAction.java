package net.ytoec.kernel.action.api;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.action.remote.AbstractInterfaceAction;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 给仓配通提供接口的action
 * 
 * @author tfhuang
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class ApiAction extends AbstractInterfaceAction {

	private static Logger logger = LoggerFactory.getLogger(ApiAction.class);
	
	@Inject
	private ZebraSurfacebillService zsebraSurfacebillService;

	/**
	 * 提供仓配通/第三方商家获取面单接口
	 * 
	 * @param params
	 * @param request
	 */
	public void synWaybill() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		zsebraSurfacebillService.synWaybill(request);
	}

	/**
	 * @作者：罗典
	 * @时间：2013-09-25
	 * @描述：易通接收金刚同步过来的面单信息
	 * */
	public void receiveWaybill()throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		zsebraSurfacebillService.receiveWaybill(request);
	}
}
