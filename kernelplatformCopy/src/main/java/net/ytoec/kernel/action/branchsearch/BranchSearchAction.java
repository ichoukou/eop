package net.ytoec.kernel.action.branchsearch;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Branch;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.search.dataobject.BranchItem;
import net.ytoec.kernel.search.service.BranchSearchService;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 网点信息搜索引擎action
 * 
 * @author wangyong 2012-02-15
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class BranchSearchAction extends AbstractActionSupport {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserService<User> userService;
	@Inject
	private MessageService messageService;

	@Inject
	private BranchSearchService<Branch> branchSearchService;

	private Integer currentPage = 1;

	private String searchKey;
	private Pagination pagination;
	private List<BranchItem> branchItemList;
	private int index = 0;

	private Branch branch;
	private String flag;
	private String flag2;
	private String isValidate;
	private String isOk;
	private boolean isSucc = true;
	
	/**
	 * 是否取消占有,占有ID
	 */
	private String isHave;
	private String haveId;
	
	/**
	 * 我的网点信息
	 */
	private Branch myBranch;
	
	//拦截后跳转到此方法
	public String index(){
		pagination = new Pagination(currentPage, super.pageNum);
		pagination.setTotalRecords(0);
		return "list";
	}

	public String list() {
		User currentUser = super.readCookieUser();
		
		myBranch = branchSearchService.findBranchByBranchCode(currentUser.getId());
		
		String userType = currentUser.getUserType();
		pagination = new Pagination(currentPage, super.pageNum);
		pagination.setTotalRecords(0);
		index = 1;
		if (userType != null || "" != userType) {
			if (userType.equals("2") || userType.equals("21") || userType.equals("22") || userType.equals("23")) {
				return "list2";
			}
		}
		return "list";
	}

	public String solr() throws InterruptedException, SolrServerException,
			IOException {
		
		/**
		 * 查看您的网点信息
		 */
		User currentUser = super.readCookieUser();
		myBranch = branchSearchService.findBranchByBranchCode(currentUser.getId());
		
		pagination = new Pagination(currentPage, super.pageNum);
		Map map = new HashMap();
		map.put("searchKey", searchKey);
		pagination.setParams(map);
		pagination = branchSearchService.searchBranchData(pagination,
				ConfigUtilSingle.getInstance().getSolrBranchUrl());
		branchItemList = pagination.getRecords();
		//branch = branchSearchService.findByUserId(currentUser.getId());
		isSucc = branchSearchService.findByBranchCode(currentUser.getId());
		if(isSucc)
			flag2 = "isSuccess";
		else
			flag2 = "isFalse";
		String userType = currentUser.getUserType();
		if (userType != null || "" != userType) {
			if (userType.equals("2") || userType.equals("21") || userType.equals("22") || userType.equals("23")) {
				return "solr2";
			}
		}
		return "solr";
	}

	// 纠错
	public String correction() throws InterruptedException,
			SolrServerException, IOException {
		User currentUser = super.readCookieUser();
		String userType = currentUser.getUserType();
		String message = "用户" + currentUser.getUserNameText() + "通知您对网点编号为"
				+ branch.getId() + "的网点进行了如下纠错:" + "<br>1.公司名称修改为:"
				+ branch.getCompanyName() + "<br>2.经理人修改为:"
				+ branch.getManagerName() + "<br>3.服务电话修改为:"
				+ branch.getServicePhone() + "<br>4.问题件修改为:"
				+ branch.getQuestionPhone() + "<br>5.派送范围修改为:"
				+ branch.getSendScope() + "<br>6.不派送范围修改为:"
				+ branch.getUnSendScope() + "<br>7.派送时限修改为:"
				+ branch.getSendTimeLimit();
		String messageTheme = "网点纠错通知";
		List<User> adminList = userService.getUserListByUserType("3");

		if (userType != null || "" != userType) {
			if (userType.equals("2")) {
				boolean isSuccess = true;
				Branch br = branchSearchService.get(branch.getId());
				br.setCompanyName(branch.getCompanyName());
				br.setManagerPhone(branch.getManagerName());
				br.setServicePhone(branch.getServicePhone());
				br.setQuestionPhone(branch.getQuestionPhone());
				br.setSendScope(branch.getSendScope());
				br.setUnSendScope(branch.getUnSendScope());
				br.setSendTimeLimit(branch.getSendTimeLimit());
				//br.setBranchCode(currentUser.getId());   不再占有
				isSuccess = branchSearchService.edit(br);
				isValidate = isSuccess ? "true" : "false";
				return "correction";
			}else{
				if (null != adminList && adminList.size() > 0) {
					Integer adminId = adminList.get(0).getId();
					Boolean flagState = messageService.sendAdvise(currentUser, messageTheme, message);
					if (flagState == null || !flagState) {
						flag = "false";
					} else {
						flag = "true";
					}
				}
			}
		}
		return "jsondata";
	}
	
	//占位己有
	public String employ(){
		User currentUser = super.readCookieUser();
		String userType = currentUser.getUserType();
		if (userType != null || "" != userType) {
			if (userType.equals("2")) {
				boolean isSuccess = true;
				Branch br = branchSearchService.get(branch.getId());
				br.setBranchCode(currentUser.getId());
				isSuccess = branchSearchService.edit(br);
				isValidate = isSuccess ? "true" : "false";
			}
		}
		return "employ";
	}
	
	//取消占有
	public String cancelEmploy(){
		User currentUser = super.readCookieUser();
		String userType = currentUser.getUserType();
		if (userType != null || "" != userType) {
			if (userType.equals("2")) {
				boolean isSuccess = true;
				Branch br = branchSearchService.get(branch.getId());
				br.setBranchCode(null);
				isSuccess = branchSearchService.edit(br);
				isValidate = isSuccess ? "true" : "false";
			}
		}
		return "cancelEmploy";
	}

	//新增网点信息
	public String addBranch() throws InterruptedException, SolrServerException,
			IOException {
		if (branch != null) {
			boolean isSuccess = true;
			User currentUser = super.readCookieUser();
			branch.setProvice(currentUser.getAddressProvince());
			branch.setCity(currentUser.getAddressCity());
			branch.setCounty(currentUser.getAddressDistrict());
			branch.setBranchCode(currentUser.getId());
		    isSuccess = branchSearchService.addBranch(branch);
			isValidate = isSuccess ? "true" : "false";
		}
		return "addBranch";
	}
	
	//修改网点信息
	public String editBranch() throws InterruptedException, SolrServerException,
			IOException {
		if (branch != null) {
			Branch theBranch = branchSearchService.get(branch.getId());
			theBranch.setCompanyName(branch.getCompanyName());
			theBranch.setManagerPhone(branch.getManagerPhone());
			theBranch.setServicePhone(branch.getServicePhone());
			theBranch.setQuestionPhone(branch.getQuestionPhone());
			theBranch.setSendScope(branch.getSendScope());
			theBranch.setUnSendScope(branch.getUnSendScope());
			theBranch.setSendTimeLimit(branch.getSendTimeLimit());

			boolean isSuccess = true;
		    isSuccess = branchSearchService.edit(theBranch);
			isValidate = isSuccess ? "true" : "false";
		}
		return "editBranch";
	}

	public String checkBranchCode() {
		User user = super.readCookieUser();
		// String userType = currentUser.getUserType();
		
		if (user == null || StringUtils.isBlank((user.getId()).toString())) {
			isSucc = false;
		}
		isSucc = branchSearchService.findByBranchCode(user.getId());
		isValidate = isSucc ? "true" : "false";
		return "checkBranch";
	}

	public String checkCompanyName() {
		boolean isSuccess = true;
//		try {
//			String ss = URLDecoder.decode(branch.getCompanyName(), "utf-8");
//			System.out.println(ss);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		if (branch == null || StringUtils.isBlank(branch.getCompanyName())) {
			isSuccess = false;
		}
		isSuccess = branchSearchService.findByCompanyName(branch);
		isValidate = isSuccess ? "true" : "false";
		return "checkBranch";
	}

	public String checkManagerPhone() {
		boolean isSuccess = true;
		if (branch == null || StringUtils.isBlank(branch.getManagerPhone())) {
			isSuccess = false;
		}
		isSuccess = branchSearchService.findByManagerPhone(branch);
		isValidate = isSuccess ? "true" : "false";
		return "checkBranch";
	}

	public String checkServicePhone() {
		boolean isSuccess = true;
		if (branch == null || StringUtils.isBlank(branch.getServicePhone())) {
			isSuccess = false;
		}
		isSuccess = branchSearchService.findByServicePhone(branch);
		isValidate = isSuccess ? "true" : "false";
		return "checkBranch";
	}

	public String checkQuestionPhone() {
		boolean isSuccess = true;
		if (branch == null || StringUtils.isBlank(branch.getQuestionPhone())) {
			isSuccess = false;
		}
		isSuccess = branchSearchService.findByQuestionPhone(branch);
		isValidate = isSuccess ? "true" : "false";
		return "checkBranch";
	}

	public boolean isSucc() {
		return isSucc;
	}

	public void setSucc(boolean isSucc) {
		this.isSucc = isSucc;
	}
	
	public String getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<BranchItem> getBranchItemList() {
		return branchItemList;
	}

	public void setBranchItemList(List<BranchItem> branchItemList) {
		this.branchItemList = branchItemList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag2() {
		return flag2;
	}

	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}

	public Branch getMyBranch() {
		return myBranch;
	}

	public void setMyBranch(Branch myBranch) {
		this.myBranch = myBranch;
	}

	public String getIsOk() {
		return isOk;
	}

	public void setIsOk(String isOk) {
		this.isOk = isOk;
	}

	public String getIsHave() {
		return isHave;
	}

	public void setIsHave(String isHave) {
		this.isHave = isHave;
	}

	public String getHaveId() {
		return haveId;
	}

	public void setHaveId(String haveId) {
		this.haveId = haveId;
	}
	
}
