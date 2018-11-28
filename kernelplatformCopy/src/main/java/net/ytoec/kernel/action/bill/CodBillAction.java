package net.ytoec.kernel.action.bill;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.CodBillService;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 代收货款action类
 * @author chenfeng
 * @date 2013-03-06
 */

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class CodBillAction extends AbstractActionSupport {

	/**
	 * 记录日志
	 */
	private static Logger logger = LoggerFactory.getLogger(CodBillAction.class);
	
	public static final String AJAX_RETURN_TYPE = "ajax";
	@Inject
	private CodBillService<BillDetailDto> codBillService ;

	private String curTab; //标识当前tab页
	private String waybillNo;
	private String detailStartTime;
	private String detailEndTime;
	private String timeType ;
	private String pageFlag ;
	private String customerConfirmFlg ;
	private BillDetailDto billDetail;
	private Pagination<BillDetailDto> pagination; //未确认账单明细
	
	//private Pagination<BillDetailDto> billDetailDto; //未确认差异
	private List<BillDetailDto> billPrintList = new ArrayList<BillDetailDto>();
	private List<String> pageList = new ArrayList<String>();
	private String orderByCol = "createDateDown"; //批量打印列表排序
	/**
	 * 当前页数
	 */
	private Integer currentPage = 1;
	/**
	 * 每页显示多少条记录
	 */
	private Integer pageNum = 20;
	/**
	 * 允许打印是否勾选
	 */
	private String checked;
	private User user;
	private Boolean bindBranch;//绑定客户编号
	private String showMessage;
	private Integer index = 0;
	
//	返回订单集合用于统计总数
	private BillAccountAmount result = new BillAccountAmount();
	
//	导出账单时页面传过来的当前页面的运单号
	private String waybillNos;
	/** 电子对账/VIP账单导出功能的导出编码;<br>如果前台不传,后台默认UTF-8 */
	private String outputCode;
	private Map<String, Object> jsonResult = new HashMap<String, Object>();	
	private String json;
	
	
	/**
	 * 查询账单明细的总票数与总代收金额
	 * @return
	 */
	public String queryBillDetailCountAndAmount(){
		index = 1;
		logger.info("代收货款未确认账单明细查询........");
		User currentUser = this.readCookieUser();

		if (pagination == null) {
			pagination = new Pagination(currentPage, pageNum);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
//		String customerPayFlg = billDetail.getCustomerPayFlg();
		String customerConfirmFlg = billDetail.getCustomerConfirmFlg();
		String signoffFlg = billDetail.getSignoffFlg();
		
		params.put("userCode", currentUser.getUserCode());
//		测试usercode
//		params.put("userCode", "K21000318");
		if (StringUtils.isNotBlank(billDetail.getWaybillNo())) {
			String[] waybillNoArr = billDetail.getWaybillNo().replace('，', ',').split(",");
			String waybillNoStr = "";
			for (int i = 0; i < waybillNoArr.length; i++) {
				if (0 == i) {
					waybillNoStr += "'" + waybillNoArr[i] + "'";
				} else {
					waybillNoStr += ",'" + waybillNoArr[i] + "'";
				}
			}
			params.put("waybillNoStr", waybillNoStr);
//			if (null != pageFlag) {
//				if (StringUtils.isNotBlank(customerConfirmFlg)) {
//					params.put("customerConfirmFlg", customerConfirmFlg);
//					params.put("pageFlag", String.valueOf(pageFlag));
//				}
//			}
		} else {
//			if (StringUtils.isNotBlank(customerPayFlg)) {
//				params.put("customerPayFlg", customerPayFlg);
//			}
			if (StringUtils.isNotBlank(detailStartTime) && StringUtils.isNotBlank(detailEndTime)) {
				params.put("detailStartTime", detailStartTime);
				params.put("detailEndTime", detailEndTime);
			}
			if (StringUtils.isNotBlank(customerConfirmFlg)) {
				params.put("customerConfirmFlg", customerConfirmFlg);
			}
			if (StringUtils.isNotBlank(signoffFlg)) {
				params.put("signoffFlg", signoffFlg);
			}
		}
		params.put("orderByCol", orderByCol);
		params.put("startIndex", pagination.getStartIndex());
		params.put("pageNum", pagination.getPageNum());
		params.put("timeType", timeType);
		
//		Integer countNum = codBillService.getTotalCodBill(params);
		List<BillDetailDto> codBill = codBillService.getCodBillDtails(params);
		Integer countNum = codBill.size();
		if(countNum > 0){
			for (int i = (currentPage-1)*pageNum; i < (currentPage)*pageNum && i < countNum; i++) {
				billPrintList.add(codBill.get(i));
			}
		}
		
		// 设置分页参数
		int countPage = 0;
		if (countNum % pageNum == 0) {
			countPage = countNum / pageNum;
		} else {
			countPage = countNum / pageNum + 1;
		}
		for (int num = 1; num <= countPage; num++) {
			pageList.add(num + "");
		}
		pagination.setTotalPages(countPage);
		pagination.setCurrentPage(currentPage);
		pagination.setRecords(billPrintList);
		pagination.setTotalRecords(countNum);
		
		buildAmount(codBill);
		
		return "toSearch";
	}
	
//	导出账单，导出当前页面显示的账单
	public String exportCodBills(){
		HttpServletResponse response = null;
		PrintWriter out = null;
		try {
			response = ServletActionContext.getResponse();
			String x = codBillService.buildExportCodBills(waybillNos);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String nowDate = sdf.format(new Date());
			String preName = "代收货款账单明细信息";
			String fileName =preName + nowDate;
			response.setHeader("Content-Disposition", "attachment; filename="  + new String(fileName.getBytes("utf-8"),"ISO8859-1")+".csv");
			outputCode = StringUtils.defaultIfEmpty(outputCode, "GBK");
			response.setContentType("application/ms-excel;charset="+outputCode);
			response.setCharacterEncoding(outputCode); // GBK
			out = response.getWriter();
			out.print(x);
		} catch (Exception e) {
			logger.error("#电子对账 #导出 导出异常", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}
		return null;
	}
	
//	生成账单统计数据
	public BillAccountAmount buildAmount(List<BillDetailDto> codBill){
//		未支付总票数
		Integer unpaidNumAmount = 0;
//		未支付总金额
		double unpaidMoneyAmount = 0;
//		已支付总票数
		Integer paidNumAmount = 0; 
//		已支付总金额
		double paidMoneyAmount = 0;
//		未确认总票数
		Integer unconfirmedNumAmount = 0;
//		未确认总金额
		double unconfirmedMoneyAmount = 0;
//		已确认总票数
		Integer confirmedNumAmount = 0;
//		已确认总金额
		double confirmedMoneyAmount = 0;
		
//		如果timeType=1,统计未确认
		if(timeType.equals("1")){
//			遍历codBill，统计出未确认和确认订单的总票数和总金额
			for (BillDetailDto billDetailDto : codBill) {
//				已确认订单
				if(billDetailDto.getCustomerConfirmFlg() != null && billDetailDto.getCustomerConfirmFlg().equals("1")){
					confirmedNumAmount++;
					confirmedMoneyAmount += billDetailDto.getCodMoney();
//					未确认订单
				}else if(billDetailDto.getCustomerConfirmFlg() != null && billDetailDto.getCustomerConfirmFlg().equals("0")){
					unconfirmedNumAmount++;
					unconfirmedMoneyAmount += billDetailDto.getCodMoney();
				}
			}
//			将统计结果放入result中
			result.setConfirmedNumAmount(confirmedNumAmount);
			result.setConfirmedMoneyAmount(confirmedMoneyAmount);
			result.setUnconfirmedNumAmount(unconfirmedNumAmount);
			result.setUnconfirmedMoneyAmount(unconfirmedMoneyAmount);
//			如果查询的时间类型为支付时间或录单时间
		} else if (timeType.equals("0") || timeType.equals("2")){
//			遍历codBill，统计出未支付和支付订单的总票数和总金额
			for (BillDetailDto billDetailDto : codBill) {
//				已支付订单
				if(billDetailDto.getCustomerPayFlg() != null && billDetailDto.getCustomerPayFlg().equals("1")){
					paidNumAmount++;
					paidMoneyAmount += billDetailDto.getCodMoney();
//					未支付订单
				}else if(billDetailDto.getCustomerPayFlg() != null && billDetailDto.getCustomerPayFlg().equals("0")){
					unpaidNumAmount++;
					unpaidMoneyAmount += billDetailDto.getCodMoney();
				}
			}
//			将统计结果放入result中
			result.setPaidNumAmount(paidNumAmount);
			result.setPaidMoneyAmount(paidMoneyAmount);
			result.setUnpaidNumAmount(unpaidNumAmount);
			result.setUnpaidMoneyAmount(unpaidMoneyAmount);
		}
		
		return result;
	}
	
	/*
	 * 跳转到综合查询页面
	 */
	public String toCompositiveSearch(){
		pagination = new Pagination(currentPage, super.pageNum);
		pagination.setTotalRecords(0);
		return "toSearch";
	}
	
	/*
	 * 查看总公司驳回理由
	 */
	public String queryCompanyUndoReason() throws JSONException {
		String undoReason = codBillService.getCompanyUndoReason(waybillNo);
		jsonResult.put("undoReason", undoReason);
//		json = JSONUtil.serialize(jsonResult);
		return AJAX_RETURN_TYPE;
	}
	
	/**
	 * 查看分公司驳回理由
	 * @return
	 * @throws JSONException 
	 */
	public String querySubcompanyUndoReason() throws JSONException {
		String undoReason = codBillService.getSubcompanyUndoReason(waybillNo);
		jsonResult.put("undoReason", undoReason);
//		json = JSONUtil.serialize(jsonResult);
		return AJAX_RETURN_TYPE;
	}

	
	/**
	 * 字符串第一个字母大写
	 */
	public String changeUpperFirst(String s) {
		byte[] items = s.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}



	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getDetailStartTime() {
		return detailStartTime;
	}

	public void setDetailStartTime(String detailStartTime) {
		this.detailStartTime = detailStartTime;
	}

	public String getDetailEndTime() {
		return detailEndTime;
	}

	public void setDetailEndTime(String detailEndTime) {
		this.detailEndTime = detailEndTime;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getpageNum() {
		return pageNum;
	}

	public void setpageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCurTab() {
		return curTab;
	}

	public void setCurTab(String curTab) {
		this.curTab = curTab;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public Boolean getBindBranch() {
		return bindBranch;
	}

	public void setBindBranch(Boolean bindBranch) {
		this.bindBranch = bindBranch;
	}


	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public String getCustomerConfirmFlg() {
		return customerConfirmFlg;
	}

	public void setCustomerConfirmFlg(String customerConfirmFlg) {
		this.customerConfirmFlg = customerConfirmFlg;
	}

	public BillDetailDto getBillDetail() {
		return billDetail;
	}

	public void setBillDetail(BillDetailDto billDetail) {
		this.billDetail = billDetail;
	}

	public Pagination<BillDetailDto> getpagination() {
		return pagination;
	}

	public void setpagination(Pagination<BillDetailDto> pagination) {
		this.pagination = pagination;
	}

	public List<String> getPageList() {
		return pageList;
	}

	public void setPageList(List<String> pageList) {
		this.pageList = pageList;
	}

	public List<BillDetailDto> getBillPrintList() {
		return billPrintList;
	}

	public void setBillPrintList(List<BillDetailDto> billPrintList) {
		this.billPrintList = billPrintList;
	}

	public String getOrderByCol() {
		return orderByCol;
	}

	public void setOrderByCol(String orderByCol) {
		this.orderByCol = orderByCol;
	}
	
	public Pagination<BillDetailDto> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<BillDetailDto> pagination) {
		this.pagination = pagination;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public BillAccountAmount getResult() {
		return result;
	}

	public void setResult(BillAccountAmount result) {
		this.result = result;
	}

	public String getWaybillNos() {
		return waybillNos;
	}

	public void setWaybillNos(String waybillNos) {
		this.waybillNos = waybillNos;
	}

	public String getOutputCode() {
		return outputCode;
	}

	public void setOutputCode(String outputCode) {
		this.outputCode = outputCode;
	}

	public Map<String, Object> getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Map<String, Object> jsonResult) {
		this.jsonResult = jsonResult;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	

	
}
