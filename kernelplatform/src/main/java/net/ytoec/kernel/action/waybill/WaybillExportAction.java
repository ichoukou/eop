package net.ytoec.kernel.action.waybill;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Constant;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.QuerySequenceCondition;
import net.ytoec.kernel.dataobject.QueryUserCondition;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserWaybillInfo;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.exception.BusinessException;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.service.ZebraSequenceService;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.sun.mail.util.ValidationTool;

/**
 * @作者：罗典
 * @描述：面单下载相关请求
 * @时间：2013-10-22
 * */
@Controller
public class WaybillExportAction extends AbstractActionSupport {
	private static final long serialVersionUID = 1L;
	// 商家编码
	private String customerCode;
	// 商家名称
	private String customerName;
	// 异常时提示信息
	private String errorMsg;
	// 下载箱数
	private int downloadBoxNum;
	// 下载批次号
	private Integer sequenceId;
	// Excel文件名
	private String downloadFileName;
	// Excel文件
	private InputStream excelFile;
	// 日志查询起始时间
	private Date startDate;
	// 日志查询结束时间
	private Date endDate;
	// 页数
	private int pageSize;
	// 每页显示行数
	private int limit;
	// 是否需要关闭风险提醒(0：不是； 1:是)
	private int needCloseRemind = 0 ;
	// 是否是从风险提醒界面过来的请求(0：不是； 1:是)
	private int isFromRemindPage = 0;
	// 用户界面是否需要风险提醒
	private int needRemind = 0;
	// 面单下载记录分页信息
	private Pagination<ZebraSequence> zebrasequencePagination;
	// 商家面单信息集合
	private List<UserWaybillInfo> userWaybillInfoList;
	// 单个商家面单信息统计信息
	private UserWaybillInfo userWaybillInfo;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private ZebraSurfacebillService zebraSurfacebillService;
	@Inject
	private ZebraSequenceService zebraSequenceService;
	@Inject
	private UserCustomService<UserCustom> userCustomService;

	/**
	 * @作者：罗典
	 * @描述：用户界面初始化和商家面单统计信息查询
	 * @时间：2013-10-23
	 * @参数：customerCode 商家编码(编码=0时查询网点编码下所有商家运单统计信息) site 网点编码
	 * @返回：商家面单信息集合
	 * */
	public String userInfoLoad() {
		// 客户编码集合
		List<String> customerList = new ArrayList<String>();
		// 网点下可下载面单商家信息
		List<UserThread> userThreadList = new ArrayList<UserThread>();
		try{
			User user = super.readCookieUser();
			QueryUserCondition condition = new QueryUserCondition();
			condition.setSiteCode(user.getUserName());
			condition.setIsCanDownload(1);
			// 编码=0时查询网点编码下所有商家运单统计信息,则不加入商家编码条件，反之则加入
			if (null != customerCode && !"0".equals(customerCode)) {
				condition.setCustomerCode(customerCode);
			}
			userThreadList = userThreadService.queryUserThread(condition);
			if (userThreadList.size() > 0) {
				// 定义临时map，用于存储用户编码和用户姓名
				Map<String,String> userMap = new HashMap<String,String>();
				for (UserThread userThread : userThreadList) {
					customerList.add(userThread.getUserCode());
					userMap.put(userThread.getUserCode(), userThread.getUserName());
				}
				// 统计面单信息
				userWaybillInfoList = zebraSurfacebillService.countWaybillInfo(customerList);
				// 对于没有面单信息的用户，需要初始化数据到界面上
				for(UserWaybillInfo info : userWaybillInfoList){
					userMap.remove(info.getCustomerCode());
				}
				for(String code:userMap.keySet()){
					UserWaybillInfo info = new UserWaybillInfo();
					info.setCustomerCode(code);
					info.setCustomerName(userMap.get(code));
					userWaybillInfoList.add(info);
				}
				
				UserCustom custom = new UserCustom();
				custom.setBindedUserName(user.getUserName());
				custom.setType(UserCustom.TYPE_REMAIN);
				custom.setUserName(user.getUserName());
				List<UserCustom> userCustomList = userCustomService.searchUserCustom(custom);
				// 如果不存在数据则直接跳转至风险提示页面，存在则进入面单导入流程
				if(userCustomList.size() <= 0 ){
					needRemind = 1;
				}
			}else{
				errorMsg = Constant.MSG_SITE_NOCUSTOMER;
			}
		}catch (Exception e) {
			errorMsg = Constant.MSG_UNKOWN_ERROR+e.getMessage();
		}
		return "success";
	}

	/**
	 * @作者：罗典
	 * @描述：商家面单下载excel功能(sequenceId有值时为重新拉取)
	 * @时间：2013-10-23
	 * @参数：customerCode 商家编码 downloadBoxNum 需下载箱数
	 * @返回：商家面单信息集合
	 * */
	public String waybillExport(){
		try {
			// 必填参数非空校验
			ValidationTool.validateString(customerCode, "customerCode");
			ValidationTool.validateObject(downloadBoxNum, "downloadBoxNum");
			ValidationTool.validateString(customerName, "customerName");
			User user = super.readCookieUser();
			
			// 首先判断是否需要关闭提醒
			if(needCloseRemind == 1){
				// 需要关闭提醒时插入一条数据进去
				UserCustom custom = new UserCustom();
				custom.setBindedUserName(user.getUserName());
				custom.setType(UserCustom.TYPE_REMAIN);
				custom.setUserName(user.getUserName());
				userCustomService.add(custom);
			}
			// 判断此商家是否能够下载
			QueryUserCondition condition = new QueryUserCondition();
			condition.setCustomerCode(customerCode);
			condition.setIsCanDownload(1);
			List<UserThread> userThreadList = userThreadService.queryUserThread(condition);
			if(userThreadList == null || userThreadList.size() <= 0){
				throw new BusinessException(Constant.MSG_SITE_NOCUSTOMER);
			}
			// 新增一条sequence记录，用于标示某次请求生命周期
			ZebraSequence zebraSequence = new ZebraSequence();
			zebraSequence.setSequenceId(sequenceId);
			zebraSequence.setCustomerCode(customerCode);
			zebraSequence.setParternCode("");
			zebraSequence.setCreateTime(new Date());
			zebraSequence.setState(Constant.SEQ_STATE_SUCCESS);
			zebraSequence.setTotalCount(downloadBoxNum * 1000);
			// 拉取面单信息Excel文件获取
			HSSFWorkbook hSSFWorkbook = zebraSurfacebillService.exportWaybills(
					zebraSequence, customerName);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHHmmss");
			downloadFileName = customerName + "_"
					+ zebraSequence.getSequenceId() + "_"
					+ sdf.format(new Date())+".xls";
			// 防止中文乱码
			downloadFileName = new String(downloadFileName.getBytes("gb2312"),"iso8859-1");
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			hSSFWorkbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			output.flush();
			output.close();
		} catch (BusinessException e) {
			errorMsg = e.getMessage();
		} catch (Exception e) {
			errorMsg = Constant.MSG_UNKOWN_ERROR+e.getMessage();
		}
		if(errorMsg == null || "".endsWith(errorMsg)){
			return "excel";
		}else if(sequenceId != null && !"".equals(sequenceId)){
			// 返回下载日志初始化页面
			return queryExportLog();
		}else{
			// 返回下载面单初始化页面
			customerCode = null;
			return userInfoLoad();
		}
		
	}

	/**
	 * @作者：罗典
	 * @描述：查询面单下载sequence日志
	 * @时间：2013-10-24
	 * @参数：商家编码，序列号，查询起始时间，查询结束时间，起始页，每页行数
	 * */
	public String queryExportLog() {
		try{
			// 必填参数非空校验
			ValidationTool.validateString(customerCode, "customerCode");
			QuerySequenceCondition condition = new QuerySequenceCondition();
			condition.setCustomerCode(customerCode);
			condition.setSequence(sequenceId);
			condition.setStartLine((pageSize-1)*limit);
			condition.setLimit(limit);
			condition.setStartDate(startDate);
			condition.setEndDate(endDate);
			List<ZebraSequence> zebraSequenceList = zebraSequenceService.queryByCondition(condition);
			zebrasequencePagination = new Pagination<ZebraSequence>(pageSize, pageNum);
			if (zebraSequenceList == null || zebraSequenceList.size() == 0) {
				zebrasequencePagination.setTotalRecords(0);
			}else{
				zebrasequencePagination.setRecords(zebraSequenceList);
				zebrasequencePagination.setTotalRecords( zebraSequenceService.countByCondition(condition));
			}
			// 统计面单信息
			List<String> customerList = new ArrayList<String>();
			customerList.add(customerCode);
			userWaybillInfoList = zebraSurfacebillService.countWaybillInfo(customerList);
			if(userWaybillInfoList != null && userWaybillInfoList.size()>0){
				userWaybillInfo = userWaybillInfoList.get(0);
			}
		}catch (BusinessException e) {
			zebrasequencePagination = new Pagination<ZebraSequence>(pageSize, pageNum);
			zebrasequencePagination.setTotalRecords(0);
		}catch (Exception e) {
			e.printStackTrace();
			zebrasequencePagination = new Pagination<ZebraSequence>(pageSize, pageNum);
			zebrasequencePagination.setTotalRecords(0);
			errorMsg = Constant.MSG_UNKOWN_ERROR+e.getMessage();
		}
		return "exportLog";
	}

	public List<UserWaybillInfo> getUserWaybillInfoList() {
		return userWaybillInfoList;
	}

	public void setUserWaybillInfoList(List<UserWaybillInfo> userWaybillInfoList) {
		this.userWaybillInfoList = userWaybillInfoList;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public int getNeedCloseRemind() {
		return needCloseRemind;
	}

	public void setNeedCloseRemind(int needCloseRemind) {
		this.needCloseRemind = needCloseRemind;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public InputStream getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(InputStream excelFile) {
		this.excelFile = excelFile;
	}

	public int getDownloadBoxNum() {
		return downloadBoxNum;
	}

	public void setDownloadBoxNum(int downloadBoxNum) {
		this.downloadBoxNum = downloadBoxNum;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Pagination<ZebraSequence> getZebrasequencePagination() {
		return zebrasequencePagination;
	}

	public void setZebrasequencePagination(
			Pagination<ZebraSequence> zebrasequencePagination) {
		this.zebrasequencePagination = zebrasequencePagination;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}


	public int getIsFromRemindPage() {
		return isFromRemindPage;
	}

	public void setIsFromRemindPage(int isFromRemindPage) {
		this.isFromRemindPage = isFromRemindPage;
	}

	public UserWaybillInfo getUserWaybillInfo() {
		return userWaybillInfo;
	}

	public void setUserWaybillInfo(UserWaybillInfo userWaybillInfo) {
		this.userWaybillInfo = userWaybillInfo;
	}

	public int getNeedRemind() {
		return needRemind;
	}

	public void setNeedRemind(int needRemind) {
		this.needRemind = needRemind;
	}

}
