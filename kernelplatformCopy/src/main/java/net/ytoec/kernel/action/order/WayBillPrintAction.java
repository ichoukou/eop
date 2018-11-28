package net.ytoec.kernel.action.order;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.PrintInvoice;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.PrintInvoiceService;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * 运单打印action类
 * 
 * @author Tifancy
 */
@ParentPackage("ssMyBatis")
@ResultPath("/WEB-INF/printWayBill")
@Results({ @Result(name = "json", type = "json", params = { "root", "root" }) })
@Controller
public class WayBillPrintAction extends AbstractActionSupport {

	private static final long serialVersionUID = -8710632609895860372L;

	private final Logger logger = LoggerFactory.getLogger(WayBillPrintAction.class);
	public static final String JSON = "json";
	public final static String RESULTCODE = "resultCode";
	public final static String RESULTCURRNO = "currNo";
	public final static String RESULTLIST = "resultList";
	public final static String APPCODE = "HDYS";

	@Inject
	private PrintInvoiceService<PrintInvoice> printInvoiceService;
	@Inject
	private OrderService<Order> orderService;

	private HashMap<String, Object> jsonMap = new HashMap<String, Object>();
	private String deliverNo;
	private List<UserThread> vipThreadList;
	private Object root;
	private PrintInvoice printInvoice;

	private String print; // 是否打印
	private String resultCode; // 结果状态
	private String isIE; // 是否IE用于传递给内容页

	/**
	 * 加载打印框架页面
	 * 
	 * @return
	 */
	@Action(value = "/printWaybill", results = { @Result(name = "success", location = "WEB-INF.printWayBill/printWaybill.jsp", type = "tiles") })
	public String view() {
		return "success";
	}

	/**
	 * 加载打印内容页面
	 * 
	 * @return
	 */
	@Action(value = "/printContent", results = { @Result(name = "content", location = "content.jsp") })
	public String content() {
		if (StringUtils.isNotEmpty(deliverNo)) {
			printInvoice = printInvoiceService.getInvoiceByDeliverNo(deliverNo);
			if (printInvoice != null) {
				boolean result = orderService.sendMailNoToEC(
						printInvoice.getTxLogisticId(),
						printInvoice.getMailNo(), APPCODE, "offline");
				logger.error("订单发回客户系统状态" + String.valueOf(result));
			}
			print = printInvoice.getStatus() == "E0000002"
					|| StringUtils.isEmpty(printInvoice.getMailNo()) ? "false"
					: "true";
			resultCode = "S0000001";
		} else {
			print = "false";
			resultCode = "E0000001";
		}

		return "content";
	}

	/**
	 * 打印运单,返回的是json格式
	 * 
	 * @return
	 */
	public String getWayBillPrint() {
		if (deliverNo != null) {
			PrintInvoice invoice = printInvoiceService
					.getInvoiceByDeliverNo(deliverNo);
			if (invoice != null) {
				boolean result = true;
				if (result) {
					jsonMap.put("resultCode", "S0000001");
					jsonMap.put("result", invoice);
				} else
					jsonMap.put("resultCode", "E0000002");
			} else
				jsonMap.put("resultCode", "E0000001");
		}
		setRoot(jsonMap);
		return JSON;
	}

	/**
	 * 完善之前没有运单号的订单
	 * 
	 * @return
	 */
	public String fixMailNum() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("appCode", APPCODE);
		// TODO:到底是userid还是custonmerid要和马波确定
		params.put("userId", super.readCookieUser().getId());

		boolean result = false;//printInvoiceService.fixUncompletedMailNo(params);
		if (result) {
			jsonMap.put(RESULTCODE, "S0000001");
		} else {
			jsonMap.put(RESULTCODE, "E0000003");
		}
		setRoot(jsonMap);
		return JSON;
	}

	public String getBranchSite() {
		User currentUser = super.readCookieUser();
		if (currentUser.getUserType() != null
				&& (currentUser.getUserType().equals("2")
						|| currentUser.getUserType().equals("21")
						|| currentUser.getUserType().equals("22") || currentUser
						.getUserType().equals("23"))) {
			vipThreadList = super.getZhiKeUser(currentUser);
		}
		jsonMap.put("result", vipThreadList);
		setRoot(jsonMap);
		return JSON;
	}

	public Object getRoot() {
		return root;
	}

	public void setRoot(Object root) {
		this.root = root;
	}

	public String getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(String deliverNo) {
		this.deliverNo = deliverNo;
	}

	public PrintInvoice getPrintInvoice() {
		return printInvoice;
	}

	public void setPrintInvoice(PrintInvoice printInvoice) {
		this.printInvoice = printInvoice;
	}

	public String getPrint() {
		return print;
	}

	public void setPrint(String print) {
		this.print = print;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getIsIE() {
		return isIE;
	}

	public void setIsIE(String isIE) {
		this.isIE = isIE;
	}

}
