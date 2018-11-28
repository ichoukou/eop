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

import net.sf.json.JSONObject;
import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.ConfirmLetterDto;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.CodBillService;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 代收货款action类
 * @author dunjie
 * @date 2013-03-06
 */

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class CodBillPayAction extends AbstractActionSupport {

	/**
	 * 记录日志
	 */
	private Logger logger = Logger.getLogger(CodBillPayAction.class);
	@Inject
	private CodBillService<BillDetailDto> codBillService ;
	public static final String AJAX_RETURN_TYPE = "ajax";

	private String curTab; //标识当前tab页
	private String waybillNo;
	private String detailStartTime; //未确认账单明细时间
	private String detailEndTime;
	private Pagination<BillDetailDto> pagination; //未确认账单明细.
	
	
	//private Pagination<BillDetailDto> billDetailDto; //未确认差异【暂无分页】
	private List<BillDetailDto> billPrintList = new ArrayList<BillDetailDto>();
	private List<ConfirmLetterDto> conPrintList = new ArrayList<ConfirmLetterDto>();
	private List<ConfirmLetterDto> tempConPrintList = new ArrayList<ConfirmLetterDto>();
	private List<String> pageList = new ArrayList<String>();
	
	//	导出账单时页面传过来的当前页面的运单号
	private String waybillNos;
	/** 电子对账/VIP账单导出功能的导出编码;<br>如果前台不传,后台默认UTF-8 */
	private String outputCode;
	
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
	private Integer index = 0;
	private String panelIndex; //切换面板判断

	
	//未确认差异实体类
	private TStlConfirmLetterCod confirmLetter;
	
	private String detailIds;//全部选中的ID号用来进行确认账单明细
	
	//未确认差异时间
	private String startTime;
	private String endTime;
	private String waybillNo2;
	private String count;//总金额
	private int countBill;//总票数
	private String searchType="0";//控制显示“按时间”或者按运单号
	
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * 查询代收货款列表
	 * @return
	 */
	public String searchCODList(){
		conPrintList = tempConPrintList;
		logger.info("代收货款未确认账单明细查询........");
		User currentUser = this.readCookieUser();
		
		if (pagination == null) {
			pagination = new Pagination(currentPage, pageNum);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		//如果时间和运单号都不存在
		if(waybillNo.equals("")&&detailStartTime.equals("")&&detailEndTime.equals("")){
			params.put("start", "start"); //无条件查询
		}
		params.put("userCode", currentUser.getUserCode());
		if (StringUtils.isNotBlank(waybillNo)) {
			String[] waybillNoArr = waybillNo.replace('，', ',').split(",");
			String waybillNoStr = "";
			for (int i = 0; i < waybillNoArr.length; i++) {
				if (0 == i) {
					waybillNoStr += "'" + waybillNoArr[i] + "'";
				} else {
					waybillNoStr += ",'" + waybillNoArr[i] + "'";
				}
			}
			params.put("waybillNoStr", waybillNoStr);
		} else if(StringUtils.isNotBlank(detailStartTime) && StringUtils.isNotBlank(detailEndTime)){
			params.put("detailStartTime", detailStartTime);
			params.put("detailEndTime", detailEndTime);
		}
		params.put("startIndex", pagination.getStartIndex());
		params.put("pageNum", pagination.getPageNum());
		
//		Integer countNum = codBillService.getTotalCodBill(params);
		List<BillDetailDto> billList = codBillService.searchCodBillDtails(params);
		Integer countNum = billList.size();
		if(countNum > 0){
			for (int i = (currentPage-1)*pageNum; i < (currentPage)*pageNum && i < countNum; i++) {
				billPrintList.add(billList.get(i));
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
		//总金额
		countBill = billList.size();
		count = codBillService.getCODCounts(billList);
		//未确认差异条件获取 那边的数据保持不变
			if(waybillNo2!=null){
				params.put("waybillNo2", waybillNo2);
			}
			params.put("start", null);
			// 如果有运单号条件
			if (StringUtils.isNotBlank(waybillNo2)) {
				params.put("waybillNo2", waybillNo2);
			}
			// 如果有上报开始时间条件
			if (StringUtils.isNotBlank(startTime)) {
				params.put("startTime", startTime);
			}
			// 如果有上报结束时间条件
			if (StringUtils.isNotBlank(endTime)) {
				params.put("endTime", endTime);
			}
			conPrintList = codBillService.getConfirmLetterCods(params);
		return "toBillDetail"; 
	}
	
	/***
	 * 导出账单，导出当前页面显示的账单
	 * @return
	 */
	public String exportCodBills(){
		HttpServletResponse response = null;
		PrintWriter out = null;
		try {
			response = ServletActionContext.getResponse();
			String x = codBillService.billBuildExportCodBills(waybillNos);
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
	

	/***
	 * 确认账单明细
	 * @return
	 */
	public String checkCodBills(){
		User currentUser = this.readCookieUser();
		String[] detailIdsArr = detailIds.replace('，', ',').split(",");
		String detailIdsStr = "";
		for (int i = 0; i < detailIdsArr.length; i++) {
			if (0 == i) {
				detailIdsStr += "'" + detailIdsArr[i] + "'";
			} else {
				detailIdsStr += ",'" + detailIdsArr[i] + "'";
			}
		}
		//System.out.println("ＩＤ号:"+detailIdsStr);
		codBillService.batchConfirmBillDetails(detailIdsStr, currentUser);
		return "updateDetail";
	}
		/***
	 * 上报差异
	 * @return
	 */
	public String topDiff(){
		//System.out.println("进来上报..........");
		User currentUser = this.readCookieUser();
		//如果已经上报
		if(codBillService.repeatSubmit(confirmLetter.getWaybillNo())!=3){
		
			logger.info("上报差异........");
			confirmLetter.setPubTime(new Date());
			confirmLetter.setPubEmpNo(currentUser.getUserCode());
			confirmLetter.setPubEmpName(currentUser.getUserNameText());
			confirmLetter.setPubReason(confirmLetter.getInformReason());
			confirmLetter.setDiffItemCode(BillInfo.MONEY_TYPE.getKey());
			confirmLetter.setDiffItemName(BillInfo.MONEY_TYPE.getValue());
			confirmLetter.setDiffType(BillInfo.CUSTOMER_TYPE.getKey());
			confirmLetter.setConfirmFlg(BillInfo.DIFF_TO_SUBCOMPANY.getKey());
			confirmLetter.setDeleteFlg(BillInfo.DELETED_NONE.getKey());
			confirmLetter.setInitialValue(confirmLetter.getInitialCodAmount().toString());
			confirmLetter.setInformEmployeeNo(currentUser.getUserCode());
			confirmLetter.setInformEmployeeName(currentUser.getUserNameText());
			confirmLetter.setSendCustomerCode(currentUser.getUserCode());
			confirmLetter.setSendCustomerName(currentUser.getUserNameText());
			confirmLetter.setInformTime(new Date());
			confirmLetter.setCustomerConfFlg(BillInfo.CUSTOMER_NONE_CONF.getKey());
			if(codBillService.saveDiff(confirmLetter)){//保存差异
				codBillService.updateStlBillDetail(confirmLetter.getWaybillNo());//修改该运单号的明细确认状态
				jsonMapResult.put("undoReason", "success");
			}else{
				jsonMapResult.put("undoReason", "error");
			}
			return AJAX_RETURN_TYPE;
		}else{
			jsonMapResult.put("undoReason", "report");
			return AJAX_RETURN_TYPE;
		}
		
		//}
		
			/*searchType="0";
			pagination = new Pagination(currentPage, super.pageNum);
			pagination.setTotalRecords(0);
			Map<String, Object> params = new HashMap<String, Object>();
			
			if(currentUser.getUserCode().equals("") || "".equals(currentUser.getUserCode())){
				logger.info("用户code不存在........");
				return "";
			}
			params.put("userCode", currentUser.getUserCode());
			params.put("start", "start"); //无条件查询
			List<BillDetailDto> billList = codBillService.searchCodBillDtails(params);
			//只显示十条数据
			Integer countNum = billList.size();
			if(countNum > 0){
				for (int i = (currentPage-1)*pageNum; i < (currentPage)*pageNum && i < countNum; i++) {
					billPrintList.add(billList.get(i));
				}
			}
			//System.out.println("Money:"+codBillService.getCODCount(params));
			// 设置分页参数
			int countPage = 0;
			if (countNum % pageNum	 == 0) {
				countPage = countNum / pageNum;
			} else {
				countPage = countNum / pageNum + 1;
			}
			for (int num = 1; num <= countPage; num++) {
				pageList.add(num + "");
			}
			//总金额
			countBill = billList.size();
			count= codBillService.getCODCounts(billList);
			pagination.setTotalPages(countPage);
			pagination.setCurrentPage(currentPage);
			pagination.setRecords(billPrintList);
			pagination.setTotalRecords(countNum);
			panelIndex="1";
			*//***
			 * 预加载未确认差异全部
			 *//*
			conPrintList = codBillService.getConfirmLetterCods(params);
			jsonResult.put("billPrintList", billPrintList);
			jsonResult.put("conPrintList", conPrintList);*/
	}
	private Map<String, Object> jsonMapResult = new HashMap<String, Object>();	

	public Map<String, Object> getJsonResult() {
		return jsonMapResult;
	}

	public void setJsonResult(Map<String, Object> jsonResult) {
		this.jsonMapResult = jsonResult;
	}

	/**
	 * 查询未确认差异
	 * @return
	 */
	public String toSearchDiff(){
		logger.info("代收货款未确认差异查询........");
		User currentUser = this.readCookieUser();
		
		Map<String, Object> params = new HashMap<String, Object>();
	
		params.put("userCode", currentUser.getUserCode());
		if(waybillNo2!=null){
			params.put("waybillNo2", waybillNo2);
			searchType="1";
		}
		params.put("start", null);
		// 如果有运单号条件
		if (StringUtils.isNotBlank(waybillNo2)) {
			params.put("waybillNo2", waybillNo2);
		}
		// 如果有上报开始时间条件
		if (StringUtils.isNotBlank(startTime)) {
			params.put("startTime", startTime);
		}
		// 如果有上报结束时间条件
		if (StringUtils.isNotBlank(endTime)) {
			params.put("endTime", endTime);
		}
		conPrintList = codBillService.getConfirmLetterCods(params);
		//System.out.println("差异总数是:"+conPrintList.size());
		//未确认账单明细条件获取 那边的数据保持不变
			//如果时间和运单号都不存在
			if (pagination == null) {
				pagination = new Pagination(currentPage, pageNum);
			}
			if(StringUtils.isNotBlank(waybillNo)) {
				String[] waybillNoArr = waybillNo.replace('，', ',').split(",");
				String waybillNoStr = "";
				for (int i = 0; i < waybillNoArr.length; i++) {
					if (0 == i) {
						waybillNoStr += "'" + waybillNoArr[i] + "'";
					} else {
						waybillNoStr += ",'" + waybillNoArr[i] + "'";
					}
				}
				params.put("waybillNoStr", waybillNoStr);
			} else {
				if (StringUtils.isNotBlank(detailStartTime) && StringUtils.isNotBlank(detailEndTime)) {
					params.put("detailStartTime", detailStartTime);
					params.put("detailEndTime", detailEndTime);
				}
			}
			if(waybillNo.equals("")&&detailStartTime.equals("")&&detailEndTime.equals("")){
				params.put("start", "start"); //无条件查询
			}
			params.put("startIndex", StartIndex);
			params.put("pageNum", pageNum);
			List<BillDetailDto> billList = codBillService.searchCodBillDtails(params);
			Integer countNum = billList.size();
			if(countNum > 0){
				for (int i = (currentPage-1)*pageNum; i < (currentPage)*pageNum && i < countNum; i++) {
					billPrintList.add(billList.get(i));
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
		return "toBillDetail";
	}
	

	/***
	 * 跳到代收货款页面 
	 * @return
	 */
	public String toBillDetailSearch(){
		searchType="0";
		pagination = new Pagination(currentPage, super.pageNum);
		pagination.setTotalRecords(0);
		User currentUser = this.readCookieUser();
		Map<String, Object> params = new HashMap<String, Object>();
		

		params.put("userCode", currentUser.getUserCode());
		params.put("start", "start"); //无条件查询
		List<BillDetailDto> billList = codBillService.searchCodBillDtails(params);
		//只显示十条数据
		Integer countNum = billList.size();
		if(countNum > 0){
			for (int i = (currentPage-1)*pageNum; i < (currentPage)*pageNum && i < countNum; i++) {
				billPrintList.add(billList.get(i));
			}
		}
		//System.out.println("Money:"+codBillService.getCODCount(params));
		// 设置分页参数
		int countPage = 0;
		if (countNum % pageNum	 == 0) {
			countPage = countNum / pageNum;
		} else {
			countPage = countNum / pageNum + 1;
		}
		for (int num = 1; num <= countPage; num++) {
			pageList.add(num + "");
		}
		//总金额
		countBill = billList.size();
		count= codBillService.getCODCounts(billList);
		pagination.setTotalRecords(countNum);
		pagination.setTotalPages(countPage);
		pagination.setCurrentPage(currentPage);
		pagination.setRecords(billPrintList);
		
		panelIndex="1";
		/***
		 * 预加载未确认差异全部
		 */
		conPrintList = codBillService.getConfirmLetterCods(params);
		return "toBillDetail";
	}
	/**
	 * 字符串第一个字母大写
	 */
	public String changeUpperFirst(String s) {
		byte[] items = s.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	private String StartIndex;
	public String getStartIndex() {
		return StartIndex;
	}

	public void setStartIndex(String startIndex) {
		StartIndex = startIndex;
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
	
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getWaybillNo2() {
		return waybillNo2;
	}

	public void setWaybillNo2(String waybillNo2) {
		this.waybillNo2 = waybillNo2;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public List<ConfirmLetterDto> getConPrintList() {
		return conPrintList;
	}
	public void setConPrintList(List<ConfirmLetterDto> conPrintList) {
		this.conPrintList = conPrintList;
	}

	public List<ConfirmLetterDto> getTempConPrintList() {
		return tempConPrintList;
	}

	public void setTempConPrintList(List<ConfirmLetterDto> tempConPrintList) {
		this.tempConPrintList = tempConPrintList;
	}
	public String getPanelIndex() {
		return panelIndex;
	}

	public void setPanelIndex(String panelIndex) {
		this.panelIndex = panelIndex;
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
	public TStlConfirmLetterCod getConfirmLetter() {
		return confirmLetter;
	}
	public void setConfirmLetter(TStlConfirmLetterCod confirmLetter) {
		this.confirmLetter = confirmLetter;
	}
	public String getDetailIds() {
		return detailIds;
	}
	public void setDetailIds(String detailIds) {
		this.detailIds = detailIds;
	}

	public int getCountBill() {
		return countBill;
	}

	public void setCountBill(int countBill) {
		this.countBill = countBill;
	}
	
}