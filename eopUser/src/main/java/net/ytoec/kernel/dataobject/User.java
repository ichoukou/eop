package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.core.annotation.Order;

/**
 * VIP用户
 * 
 * @author ChenRen
 * @date 2011-7-25
 */
public class User implements Serializable {

	private static final long serialVersionUID = -3890420154258163507L;
	private Integer id;
	/** 淘宝子账号该项有值，指向父项ID主键 */
	private Integer parentId;
	/** 用户帐号 */
	private String userName;
	/** 使用MD5加密 */
	private String userPassword;
	/** 固定电话; 固定电话和手机号码必须填写一个 */
	private String telePhone;

	/**
	 * 座机区号
	 */
	private String telAreaCode;
	/**
	 * 座机号
	 */
	private String telCode;
	/**
	 * 座机分机号
	 */
	private String telExtCode;

	/** 手机号码; 固定电话和手机号码必须填写一个 */
	private String mobilePhone;
	/** 省 */
	private String addressProvince;
	/** 市 */
	private String addressCity;
	/** 区/县 */
	private String addressDistrict;
	/** 详细街道 */
	private String addressStreet;
	/** 男(M)、女(F)、未知(N) */
	private String sex;
	private String shopName;
	/** 店铺帐号; 如淘宝登录Id */
	private String shopAccount;
	private String mail;
	/** 证件类型：1(身份证)、2(其他) */
	private String cardType;
	private String cardNo;
	/**
	 * 用户类型： 1(VIP卖家)、 2(网店用户) 3(系统管理员)、 4(平台用户：如淘宝大商家(用户来源是淘宝)、京东(用户来源是京东)等等)、
	 * 5(业务管理员) 9(用户只填写了基本信息，尚未激活)...... 11(VIP卖家客服帐号) 12(VIP卖家财务帐号) 21(网点客服帐号)
	 * 22(网点财务帐号)
	 */
	private String userType;
	/** 用户来源：1(电商)、2(淘宝) 3(京东) 4(拍拍)...... */
	private String userSource;
	/** 用户状态：0(失效)、1(正常)、2(开放平台用户注册后未激活状态) */
	private String userState;
	/** 用户级别 */
	private String userLevel;
	private Integer createUser;
	private Date createTime;
	private Date updateTime;
	private String remark;
	/** 用户所属网点,保存网点的编码 */
	private String site;
	/** 用户帐号显示名称 */
	private String userNameText;
	/** 用户编码 */
	private String userCode;

	private String isSeal;
	/** 登录时间 */
	private Date loginTime;

	/**
	 * 是否开过店：不在数据库中存在此字段
	 */
	private String hasShpo;

	/** 是否关闭电子对账功能(1:关闭；0：开启) */
	private String switchEccount = "0";
	// 部门名称
	private String deptName;
	// 部门编码
	private String deptCode;
	// 部门电话
	private String DeptPhone;
	// 部门地址
	private String DeptAddr;

	private String HrCanceled;
	private String Canceled;
	private String Dr;

	// 是否允许网点或承包区帮忙打印面单
	private String isPrint;

	private String printNav;

	/*-- 2012-05-10在userBean中添加属性，不存在数据表中*/
	private int repeatNum;

	private String financialManager;
	
	private String unlikefreight;

	private List<UserThreadContract> userContractList = new ArrayList<UserThreadContract>();
	
	/**
	 * 已分配客户列表。
	 */
	private List<UserThread> userThreadList ;

	private List<Integer> ids;

	// 财务,客服,财务兼客服子账号进行编辑的时候判断它是否有直客已经被分配。如果没有直客，canChangeToContract：1;如果有，canChangeToContract:null
	private String canChangeToContract;

	public String getHasShpo() {
		return hasShpo;
	}

	public void setHasShpo(String hasShpo) {
		this.hasShpo = hasShpo;
	}

	// --> @2011-10-10/ChenRen 新增
	/** 淘宝帐号的密文. 与{@link Order}表中的{@link Order#customerId}一致 */
	private String taobaoEncodeKey;
	/**
	 * @author ChenRen
	 * @date 2011-11-08
	 * @description 字段用于保存网点用户的省份id 取标准运费模板的时候会根据该字段取网点用户的模板运费信息
	 */
	/** 备用字段 */
	private String field001; // 保存网点激活时网点用户所属省的ID号
	private String field002;
	/** 备用字段 */
	private String field003;
	// --> END

	private String bindedCustomerId;

	private String clientId;

	// 平台用户子账号类型：B-平台用户分仓子账号；C-平台用户入驻企业子账号；
	private String childType;

	// 移动到Resource中了
	// private Map<String, UserCustom> relationAccountCustoms;// 关联账号个性化配置

	private AppProvider appProvider;

	public AppProvider getAppProvider() {
		return appProvider;
	}

	public void setAppProvider(AppProvider appProvider) {
		this.appProvider = appProvider;
	}

	public User() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getTelePhone() {
		if (StringUtils.isBlank(telePhone) && StringUtils.isNotBlank(telCode)) {
			telePhone = StringUtils.defaultIfBlank(telAreaCode, "")
					+ (StringUtils.isNotBlank(telAreaCode) ? "-" : "")
					+ telCode + (StringUtils.isNotBlank(telExtCode) ? "-" : "")
					+ StringUtils.defaultIfBlank(telExtCode, "");
		}
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		if (StringUtils.isBlank(telePhone) && StringUtils.isNotBlank(telCode)) {
			telePhone = StringUtils.defaultIfBlank(telAreaCode, "")
					+ (StringUtils.isNotBlank(telAreaCode) ? "-" : "")
					+ telCode + (StringUtils.isNotBlank(telExtCode) ? "-" : "")
					+ StringUtils.defaultIfBlank(telExtCode, "");
		}
		this.telePhone = telePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAddressProvince() {
		return addressProvince;
	}

	public void setAddressProvince(String addressProvince) {
		this.addressProvince = addressProvince;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressDistrict() {
		return addressDistrict;
	}

	public void setAddressDistrict(String addressDistrict) {
		this.addressDistrict = addressDistrict;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getSex() {
		return sex;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopAccount() {
		return shopAccount;
	}

	public void setShopAccount(String shopAccount) {
		this.shopAccount = shopAccount;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getUserNameText() {
		return userNameText;
	}

	public void setUserNameText(String userNameText) {
		this.userNameText = userNameText;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTaobaoEncodeKey() {
		return taobaoEncodeKey;
	}

	public void setTaobaoEncodeKey(String taobaoEncodeKey) {
		this.taobaoEncodeKey = taobaoEncodeKey;
	}

	public String getField001() {
		return field001;
	}

	public void setField001(String field001) {
		this.field001 = field001;
	}

	public String getField002() {
		return field002;
	}

	public void setField002(String field002) {
		this.field002 = field002;
	}

	public String getField003() {
		return field003;
	}

	public void setField003(String field003) {
		this.field003 = field003;
	}

	public String getSwitchEccount() {
		return switchEccount;
	}

	public void setSwitchEccount(String switchEccount) {
		this.switchEccount = switchEccount;
	}

	public String getBindedCustomerId() {
		return bindedCustomerId;
	}

	public void setBindedCustomerId(String bindedCustomerId) {
		this.bindedCustomerId = bindedCustomerId;
	}

	//
	// public Map<String, UserCustom> getRelationAccountCustoms() {
	// return relationAccountCustoms;
	// }
	//
	// public void setRelationAccountCustoms(
	// Map<String, UserCustom> relationAccountCustoms) {
	// this.relationAccountCustoms = relationAccountCustoms;
	// }

	/*
	 * 方法已移动； 见 net.ytoec.kernel.common.Resource#getBindedCustomerIdList(user)
	 * 
	 * // 获取绑定账号的客户编码list public List<String> getBindedCustomerIdList(User user)
	 * { List<String> list = new ArrayList<String>(); if
	 * (this.relationAccountCustoms != null) { Iterator<UserCustom> it =
	 * this.relationAccountCustoms.values() .iterator(); for (; it.hasNext();) {
	 * // Map.Entry entry = (Map.Entry) it.next(); UserCustom uc = (UserCustom)
	 * it.next(); // System.out.println(uc.getRelationalQuery());
	 * if("1".equals(uc.getRelationalQuery())) list.add(uc.getCustomerId()); } }
	 * return list; }
	 */

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public String getChildType() {
		return childType;
	}

	/**
	 * 重写hashcode方法, 返回{@link #id}<br>
	 * 保持对象和反序列化后的对象一致
	 */
	@Override
	public int hashCode() {
		return id;
		// return super.hashCode();
	}

	// 获取用户的权限
	public String getUserAuthority() {
		if (this.userType.length() > 1)
			return userType.substring(0, 1);
		else
			return userType;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptPhone(String deptPhone) {
		DeptPhone = deptPhone;
	}

	public String getDeptPhone() {
		return DeptPhone;
	}

	public void setDeptAddr(String deptAddr) {
		DeptAddr = deptAddr;
	}

	public String getDeptAddr() {
		return DeptAddr;
	}

	public void setIsSeal(String isSeal) {
		this.isSeal = isSeal;
	}

	public String getIsSeal() {
		return isSeal;
	}

	public void setCanceled(String canceled) {
		Canceled = canceled;
	}

	public String getCanceled() {
		return Canceled;
	}

	public void setHrCanceled(String hrCanceled) {
		HrCanceled = hrCanceled;
	}

	public String getHrCanceled() {
		return HrCanceled;
	}

	public void setDr(String dr) {
		Dr = dr;
	}

	public String getDr() {
		return Dr;
	}

	public void setTelAreaCode(String telAreaCode) {
		if (StringUtils.isBlank(telAreaCode)
				&& StringUtils.isNotBlank(telePhone) && telePhone.contains("-")) {
			String areaCodeStr = telePhone.substring(0, telePhone.indexOf("-"));
			if (NumberUtils.isNumber(areaCodeStr) && areaCodeStr.length() <= 4) {
				telAreaCode = areaCodeStr;
			}
		}
		this.telAreaCode = telAreaCode;
	}

	public String getTelAreaCode() {
		if (StringUtils.isBlank(telAreaCode)
				&& StringUtils.isNotBlank(telePhone) && telePhone.contains("-")) {
			String areaCodeStr = telePhone.substring(0, telePhone.indexOf("-"));
			if (NumberUtils.isNumber(areaCodeStr) && areaCodeStr.length() <= 4) {
				telAreaCode = areaCodeStr;
			}
		}
		return telAreaCode;
	}

	public void setTelCode(String telCode) {
		if (StringUtils.isBlank(telCode) && StringUtils.isNotBlank(telePhone)
				&& telePhone.contains("-")) {
			String telCodeStr = telePhone.substring(telePhone.indexOf("-") + 1);
			if (telCodeStr.contains("-")) {
				telCodeStr = telCodeStr.substring(0, telCodeStr.indexOf("-"));
			}
			if (NumberUtils.isNumber(telCodeStr) && telCodeStr.length() <= 8) {
				telCode = telCodeStr;
			}
		}
		this.telCode = telCode;
	}

	public String getTelCode() {
		if (StringUtils.isBlank(telCode) && StringUtils.isNotBlank(telePhone)
				&& telePhone.contains("-")) {
			String telCodeStr = telePhone.substring(telePhone.indexOf("-") + 1);
			if (telCodeStr.contains("-")) {
				telCodeStr = telCodeStr.substring(0, telCodeStr.indexOf("-"));
			}
			if (NumberUtils.isNumber(telCodeStr) && telCodeStr.length() <= 8) {
				telCode = telCodeStr;
			}
		}
		return telCode;
	}

	public void setTelExtCode(String telExtCode) {
		if (StringUtils.isBlank(telExtCode)
				&& StringUtils.isNotBlank(telePhone)
				&& telePhone.indexOf("-", telePhone.indexOf("-") + 1) > 0) {
			String telExtCodeStr = telePhone.substring(telePhone
					.lastIndexOf("-") + 1);
			if (NumberUtils.isNumber(telExtCodeStr)) {
				telExtCode = telExtCodeStr;
			}
		}
		this.telExtCode = telExtCode;
	}

	public String getTelExtCode() {
		if (StringUtils.isBlank(telExtCode)
				&& StringUtils.isNotBlank(telePhone)
				&& telePhone.indexOf("-", telePhone.indexOf("-") + 1) > 0) {
			String telExtCodeStr = telePhone.substring(telePhone
					.lastIndexOf("-") + 1);
			if (NumberUtils.isNumber(telExtCodeStr)) {
				telExtCode = telExtCodeStr;
			}
		}
		return telExtCode;
	}

	public static String BUYER = "1";
	public static String SITE = "2";
	public static String ADMIN = "3";
	public static String LARGE_SELLER = "4";

	public int getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(int repeatNum) {
		this.repeatNum = repeatNum;
	}

	public List<UserThreadContract> getUserContractList() {
		return userContractList;
	}

	public void setUserContractList(List<UserThreadContract> userContractList) {
		this.userContractList = userContractList;
	}

	public String getFinancialManager() {
		return financialManager;
	}

	public void setFinancialManager(String financialManager) {
		this.financialManager = financialManager;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}

	public String getPrintNav() {
		return printNav;
	}

	public void setPrintNav(String printNav) {
		this.printNav = printNav;
	}

	public String getCanChangeToContract() {
		return canChangeToContract;
	}

	public void setCanChangeToContract(String canChangeToContract) {
		this.canChangeToContract = canChangeToContract;
	}

    public void setUserThreadList(List<UserThread> userThreadList) {
        this.userThreadList = userThreadList;
    }

    public List<UserThread> getUserThreadList() {
        return userThreadList;
    }

	public String getUnlikefreight() {
		return unlikefreight;
	}

	public void setUnlikefreight(String unlikefreight) {
		this.unlikefreight = unlikefreight;
	}

}
