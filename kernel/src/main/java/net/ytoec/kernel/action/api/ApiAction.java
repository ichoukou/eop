package net.ytoec.kernel.action.api;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.action.remote.AbstractInterfaceAction;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
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

	private static Logger logger = Logger.getLogger(ApiAction.class);
	
	@Inject
	private ZebraSurfacebillService zsebraSurfacebillService;

	/**
	 * 提供仓配通获取面单接口
	 * 
	 * @param params
	 * @param request
	 */
	public void synWaybill() {
		HttpServletRequest request = ServletActionContext.getRequest();
		zsebraSurfacebillService.synWaybill(request);
	}

}