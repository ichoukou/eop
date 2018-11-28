package net.ytoec.kernel.action.remote;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.WebUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 用户信息同步接口
 * 
 * @author mgl
 * @2011-8-10 net.ytoec.kernel.action.remote
 */
@Component
@Scope("prototype")
public class UserInfoProcessAction extends AbstractInterfaceAction {
	/**
	 * 
	 */
	private static Logger logger = Logger
			.getLogger(UserInfoProcessAction.class);
	private static final long serialVersionUID = 1L;
	private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";
	private static final String DATA_DIGEST_PARAM = "data_digest";
	private static final String CLIENT_ID_PARAM = "clientID";
	private static final String GET_METHOD_RESPONSE = "Success";
	/** 用户帐号 */
	private static final String userName = "userName";
	/** 使用MD5加密 */
	private static final String userPassword = "userPassword";
	/** 固定电话; 固定电话和手机号码必须填写一个 */
	private static final String telePhone = "homephone";
	/** 手机号码; 固定电话和手机号码必须填写一个 */
	private static final String mobilePhone = "mobile";
	/** 省 */
	private static final String addressProvince = "province";
	/** 市 */
	private static final String addressCity = "city";
	/** 区/县 */
	private static final String addressDistrict = "country";
	/** 详细街道 */
	private static final String addressStreet = "addr";
	/** 男(M)、女(F)、未知(N) */
	private static final String sex = "sex";
	private static final String shopName = "shopName";
	/** 店铺帐号; 如淘宝登录Id */
	private static final String shopAccount = "shopAccount";
	private static final String mail = "email";
	/** 证件类型：1(身份证)、2(其他) */
	private static final String cardType = "cardType";
	private static final String cardNo = "id";
	/** 用户类型：1(VIP用户)、2(网店用户) 3(渠道信息员) */
	private static final String userType = "userType";
	/** 用户来源：1(电商)、2(淘宝) */
	private static final String userSource = "userSource";
	/** 用户状态：0(失效)、1(正常) */
	private static final String userState = "userState";
	/** 用户级别 */
	private static final String userLevel = "userLevel";

	private static final String remark = "remark";
	/** 用户所属网点 */
	private static final String site = "site";
	/** 用户帐号显示名称 */
	private static final String userNameText = "userNameText";
	/** 用户编码 */
	private static final String userCode = "pk_psnbasdoc";

	private static final String VipId = "VipId";
	private static final String VipName = "VipName";
	private static final String Code = "Code";
	private static final String Type = "Type";
	private static final String Status = "Status";
	private static final String Used = "Used";
	private static final String CreateTime = "CreateTime";
	private static final String RequestCustomerInfo = "RequestCustomerInfo";
	// 网点
	private static final String UnitCode = "unitcode";
	private static final String UnitName = "unitname";
	private static final String Phone = "phone1";
	private static final String EMail = "email1";
	private static final String Province = "province";
	private static final String City = "citycounty";
	private static final String Area = "countryarea";
	private static final String Addr = "postaddr";
	private static final String IsSeal = "isseal";

	// 部门
	private static final String DeptName = "deptname";
	private static final String DeptCode = "deptcode";
	private static final String DeptPhone = "phone";
	private static final String DeptAddr = "addr";
	private static final String HrCanceled = "hrcanceled";
	private static final String Canceled = "cancel";
	private static final String Dr = "dr";

	@Inject
	private UserService<User> userService;

	@Inject
	private UserThreadService<UserThread> userThreadService;

	/**
	 * 解析接口调用,金刚调用核心平台.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String userServlet() throws Exception {

		logger.debug("********用户信息同步**********");
		if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
			super.print(GET_METHOD_RESPONSE);
			return null;
		}

		// 1.获取参数值.
		String logisticsInterface = super.request
				.getParameter(LOGISTICS_INTERFACE_PARAM);
		logger.error("===logisticsInterface===" + logisticsInterface);
		String dataDigest = super.request.getParameter(DATA_DIGEST_PARAM);
		logger.debug("===dataDigest===" + dataDigest);

		// 2.转码
		if (request.getParameter("test") != null) {
			logisticsInterface = WebUtil.decode(logisticsInterface,
					XmlSender.UTF8_CHARSET);
			dataDigest = WebUtil.decode(dataDigest, XmlSender.UTF8_CHARSET);
		}
		// 3.验证
		// SHANGHAIEXPRESSYTO 人资确认的加密方式
		if (!ProcessorUtils.validateData(logisticsInterface, dataDigest,
				ConfigUtilSingle.getInstance().getPARTERID_HR())) {
			logger.error("-------Validate Fail-------");
			print(getXmlFragment(false));
			return null;

		}
		// 4.处理
		String xmlFragment = logisticsInterface;

		String responseString = null;

		responseString = userCreate(xmlFragment);

		super.print(responseString);
		logger.debug("信息同步完毕");
		return null;
	}

	private String userCreate(String xmlFragment) {
		User user = null;
		UserThread userThread = null;
		if (xmlFragment.contains("<RequestCustomerInfo")) {
			try {
				logger.error("直客信息同步 xml:"+xmlFragment);
				userThread = this.xmlToVIP(xmlFragment);
				if (StringUtils.equals(userThread.getType(), "CREATE")) {
					//判断此usercode是否一存在
					List<UserThread> userThreads = userThreadService.searchUsersByCode(userThread);
					if (userThreads != null && !userThreads.isEmpty()) {
						return getVIPXmlFragment(false, userThread.getUserCode(),
						"该用户编码已存在，请推送修改请求");
					} else {
						userThread.setUserState("TBA");
						userThread.setSwitchEccount("0");
						boolean result = userThreadService.addUser(userThread);
						if (result == true) {
							return getVIPXmlFragment(true,
									userThread.getUserCode(), null);
						} else {
							return getVIPXmlFragment(false,
									userThread.getUserCode(), "数据持久化失败");
						}
					}
				}
				if (StringUtils.equals(userThread.getType(), "UPDATE")) {
					List<UserThread> list = userThreadService
							.searchUsersByCode(userThread);
					if (list==null||list.isEmpty()) {
					    return getVIPXmlFragment(false,
					                                userThread.getUserCode(),
					                                "更新失败，电商不存在该直客，请先推送创建请求");
                    }
					UserThread tempUserThread = list.get(0);
					if (tempUserThread == null) {
						return getVIPXmlFragment(false,
								userThread.getUserCode(),
								"更新失败，电商不存在该直客，请先推送创建请求");
					}

					List<User> users=  userService.searchUsersByCodeTypeState(tempUserThread.getUserCode(), "1", "1");
					if (StringUtils.equals(userThread.getStatus(), "VALID")
							&& StringUtils.equals(userThread.getUsed(), "Y")) {
					    if (users == null || users.isEmpty()) {
					        tempUserThread.setUserState("TBA");
                        } else {
                            tempUserThread.setUserState("1"); 
                        }
					} else {
						tempUserThread.setUserState("TBA_J");
						//取消与网点的绑定
						if (users != null && !users.isEmpty()) {
							for(User userTemp: users) {
								userTemp.setSite(null);
								userTemp.setUserState("TBA");
								userTemp.setUserCode(null);
								userService.editUser(userTemp);
							}
						} 
					}
					// 如果site code发生变更，记录变更时间。那么新的网点就只能看到变更时间以后的卖家数据
					if (!StringUtils.equals(tempUserThread.getSiteCode(),
							userThread.getSiteCode())) {
						tempUserThread.setSiteCode(userThread.getSiteCode());
						// tempUserThread.setUserCodeUpteTime(new Date());
						// 该业务暂停执行
					}
				    	
	                if (!StringUtils.equals(tempUserThread.getUserName(),
	                                           userThread.getUserName())) {
	                    tempUserThread.setUserName(userThread.getUserName());
	                }
					boolean result = userThreadService.editUser(tempUserThread);
					if (result == true) {
						return getVIPXmlFragment(true,
								userThread.getUserCode(), null);
					} else {
						return getVIPXmlFragment(false,
								userThread.getUserCode(), "数据更新失败");
					}
				}

			} catch (DuplicateKeyException e) {
				e.printStackTrace();
				return getVIPXmlFragment(false, userThread.getUserCode(),
						"该ID已存在");
			} catch (DataAccessException e) {
				e.printStackTrace();
				return getVIPXmlFragment(false, userThread.getUserCode(),
						"数据库连接失败");
			} catch (Exception e) {
				e.printStackTrace();
				return getVIPXmlFragment(false, "sys error", e.getMessage());
			}
		} else if (xmlFragment.contains("<RequestCorp")) {
			try {
				user = this.xmlToObject(xmlFragment);

				if (StringUtils.isEmpty(user.getUserCode())) {
					logger.error("----Error:userCode is null!----");
					return getXmlFragment(false);
				}
			
				if (xmlFragment.contains("proc=\"add\"")) {
				    
				    List<User> users=userService.searchUsersBySiteAndUserType(user.getUserCode(), "2");
				    if (users!=null&&!users.isEmpty()) {
				        return getXmlFragment(true);
                    }
					user.setUserPassword("YTO"
							+ user.getUserCode());// 密码YTO+网点编码
					user.setUserType("2");
					user.setUserState("TBA");// 默认为待激活状态
					user.setSite(user.getUserCode());
					user.setUserName(user.getUserCode());
					
					boolean flag = userService.addUser(user);
					return getXmlFragment(flag);

				} else if (xmlFragment.contains("proc=\"modify\"")) {
					List<User> list = userService.searchUsersBySiteAndUserType(user.getUserCode(), "2");
					if (list==null||list.isEmpty()) {
                        return getXmlFragment(false);
                    }
					User tempUser = list.get(0);
					if (tempUser == null) {
						return getXmlFragment(false);
					}

					if (StringUtils.isNotEmpty(user.getUserNameText())) {
						tempUser.setUserNameText(user.getUserNameText());
					}
					if (StringUtils.isNotEmpty(user.getTelePhone())) {
						tempUser.setTelePhone(user.getTelePhone());
					}
					if (StringUtils.isNotEmpty(user.getMail())) {
						tempUser.setMail(user.getMail());
					}
					if (StringUtils.isNotEmpty(user.getAddressProvince())) {
						tempUser.setAddressProvince(user.getAddressProvince());
					}
					if (StringUtils.isNotEmpty(user.getAddressCity())) {
						tempUser.setAddressCity(user.getAddressCity());
					}
					if (StringUtils.isNotEmpty(user.getAddressDistrict())) {
						tempUser.setAddressDistrict(user.getAddressDistrict());
					}
					if (StringUtils.isNotEmpty(user.getAddressStreet())) {
						tempUser.setAddressStreet(user.getAddressStreet());
					}
					if (StringUtils
							.equalsIgnoreCase("Y", user.getIsSeal())) {
						tempUser.setUserState("2");
						tempUser.setRemark("jingang is seal");
						boolean flag = userService.modifyUser(tempUser);
						return getXmlFragment(flag);
					} else {
						boolean flag = userService.modifyUser(tempUser);
						return getXmlFragment(flag);
					}

				} else if (xmlFragment.contains("proc=\"delete\"")) {
				    List<User> list = userService.searchUsersBySiteAndUserType(user.getUserCode(), "2");
					if (list==null||list.isEmpty()) {
                        return getXmlFragment(false);
                    }
					User tempUser = list.get(0);
					if (tempUser == null) {
						return getXmlFragment(false);
					}
					if (StringUtils.isNotEmpty(user.getUserNameText())) {
						tempUser.setUserNameText(user.getUserNameText());
					}
					if (StringUtils.isNotEmpty(user.getTelePhone())) {
						tempUser.setTelePhone(user.getTelePhone());
					}
					if (StringUtils.isNotEmpty(user.getMail())) {
						tempUser.setMail(user.getMail());
					}
					if (StringUtils.isNotEmpty(user.getAddressProvince())) {
						tempUser.setAddressProvince(user.getAddressProvince());
					}
					if (StringUtils.isNotEmpty(user.getAddressCity())) {
						tempUser.setAddressCity(user.getAddressCity());
					}
					if (StringUtils.isNotEmpty(user.getAddressDistrict())) {
						tempUser.setAddressDistrict(user.getAddressDistrict());
					}
					if (StringUtils.isNotEmpty(user.getAddressStreet())) {
						tempUser.setAddressStreet(user.getAddressStreet());
					}
					tempUser.setUserState("2");
					tempUser.setRemark("jingang del");
					boolean flag = userService.modifyUser(tempUser);
					
					return getXmlFragment(flag);
				}
			} catch (Exception e) {
				logger.error("RequestCorp 同步 error",e);
				return getXmlFragment(false);
			}
		} else if (xmlFragment.contains("<RequestDeptdoc")) {
			try {
				user = this.deptXmlToObject(xmlFragment);
				if (StringUtils.isEmpty(user.getDeptCode())) {
					logger.error("----Error:userCode is null!----");
					return getXmlFragment(false);
				}
				if (xmlFragment.contains("proc=\"add\"")) {
				    
				    List<User> users=userService.searchUsersBySiteAndUserType(user.getUserCode(), "2");
                    if (users!=null&&!users.isEmpty()) {
                        return getXmlFragment(true);
                    }
					user.setUserPassword(Md5Encryption.MD5Encode("YTO"
							+ user.getDeptCode()));
					user.setUserType("2");
					user.setUserState("TBA");// 默认为待激活状态
					user.setSite(user.getDeptCode());
                    user.setUserName(user.getDeptCode());
					boolean flag = userService.addDeptUser(user);
					return getXmlFragment(flag);

				} else if (xmlFragment.contains("proc=\"modify\"")) {

				    List<User> list = userService.searchUsersBySiteAndUserType(user.getUserCode(), "2");
					if (list==null||list.isEmpty()) {
					    return getXmlFragment(false);
                    }
					User tempUser = list.get(0);
					if (tempUser == null) {
						return getXmlFragment(false);
					}
					if (StringUtils.isNotEmpty(user.getDeptName())) {
						tempUser.setUserNameText(user.getDeptName());
					}
					tempUser.setSite(user.getDeptCode());
					if (StringUtils.isNotEmpty(user.getDeptAddr())) {
						tempUser.setAddressProvince(user.getDeptAddr());
					}
					if (StringUtils.isNotEmpty(user.getDeptPhone())) {
						tempUser.setTelePhone(user.getDeptPhone());
					}
					if (StringUtils.equalsIgnoreCase("Y",
							user.getHrCanceled())
							|| StringUtils.equalsIgnoreCase("Y",
									user.getCanceled())
							|| StringUtils.equalsIgnoreCase("1",
									user.getDr())) {
						tempUser.setUserState("2");
						tempUser.setRemark("janggang seal");
						boolean flag = userService.modifyUser(tempUser);
						return getXmlFragment(flag);
					} else {
						
						boolean flag = userService.modifyUser(tempUser);
						return getXmlFragment(flag);
					}

				} else if (xmlFragment.contains("proc=\"delete\"")) {
				    List<User> list = userService.searchUsersBySiteAndUserType(user.getUserCode(), "2");
					if (list==null||list.isEmpty()) {
                        return getXmlFragment(false);
                    }
					User tempUser = list.get(0);
					if (tempUser == null) {
						return getXmlFragment(false);
					}

					if (StringUtils.isNotEmpty(user.getDeptName())) {
						tempUser.setUserNameText(user.getDeptName());
					}
					tempUser.setSite(user.getDeptCode());
					if (StringUtils.isNotEmpty(user.getDeptAddr())) {
						tempUser.setAddressProvince(user.getDeptAddr());
					}
					if (StringUtils.isNotEmpty(user.getDeptPhone())) {
						tempUser.setTelePhone(user.getDeptPhone());
					}
					tempUser.setUserState("2");
					tempUser.setRemark("janggang del");
					boolean flag = userService.modifyUser(tempUser);
					return getXmlFragment(flag);
				}
			} catch (Exception e) {
			    logger.error("RequestDeptdoc 同步 error",e);
				return getXmlFragment(false);
			}

		} else if (xmlFragment.contains("<RqeuestOrg")) {
			try {
				user = this.kgXmlToObject(xmlFragment);
				if (user.getUserCode() == null || "".equals(user.getUserCode())) {
					logger.error("----Error:userCode is null!----");
					return getXmlFragment(false);
				}
				List<User> list = userService.searchUsersByDeptCode(user);
                if (list!=null&&!list.isEmpty()) {
                    return getXmlFragment(false);
                }
				boolean flag = userService.addUser(user);
				return getXmlFragment(flag);
			} catch (Exception e) {
			    logger.error("RqeuestOrg 同步 error",e);
				return getXmlFragment(false);
			}
		}
		return getXmlFragment(false);
	}

	private UserThread xmlToVIP(String xmlFragment) throws Exception {
		try {
			InputStream inputStream = ProcessorUtils
					.getInputStream(xmlFragment);
			DocumentReader documentReader = new DocumentReader();
			Document document = documentReader.getDocument(inputStream);
			Element root = document.getDocumentElement();
			NodeList nodeList = root.getChildNodes();

			// User requestUser = new User();
			UserThread requestUser = new UserThread();
			boolean hasType=false;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (VipId.equals(node.getNodeName())) {
					Element ele = (Element) node;
					requestUser.setUserCode(ele.getFirstChild().getNodeValue()
							.trim());
				} else if (VipName.equals(node.getNodeName())) {
					Element ele = (Element) node;
					requestUser.setUserName(ele.getFirstChild().getNodeValue()
							.trim());
				} else if (Code.equals(node.getNodeName())) {
					Element ele = (Element) node;
					requestUser.setSiteCode(ele.getFirstChild().getNodeValue()
							.trim());
				} else if (Type.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null) {
						requestUser.setType(ele.getFirstChild().getNodeValue()
								.trim());
					}
					hasType=true;
					
					
				} else if (Status.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null) {
						requestUser.setStatus(ele.getFirstChild()
								.getNodeValue().trim());
					}
				} else if (Used.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null) {
						requestUser.setUsed(ele.getFirstChild().getNodeValue()
								.trim());
					}
				} else if (CreateTime.equals(node.getNodeName())) {
					Element ele = (Element) node;
					requestUser.setCreateTime(DateUtil.valueof(ele
							.getFirstChild().getNodeValue().trim(),
							"yyyy-MM-dd HH:mm:ss"));
				}
			}
			if (!hasType) {
				requestUser.setType("CREATE");
			}
			return requestUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("xml转换错误,请检查报文", e);
		}
	}

	private User xmlToObject(String xmlFragment) throws Exception {
		logger.debug("======intoXmlToVIP======");
		logger.debug("[请求报文]:" + xmlFragment);
		try {
			// 以xml开头报错
			// if (xmlFragment.contains("<?xml")) {
			// xmlFragment = xmlFragment.replace("<?xml", "<?aaa");
			// }
			// InputStream inputStream = ProcessorUtils
			// .getInputStream(xmlFragment);

			InputStream inputStream = new ByteArrayInputStream(
					xmlFragment.getBytes("UTF-8"));

			DocumentReader documentReader = new DocumentReader();
			Document document = documentReader.getDocument(inputStream);
			Element root = document.getDocumentElement();
			// NodeList nodeList = root.getChildNodes();

			Node topNode = null;
			NodeList rootList = root.getChildNodes();
			if (rootList.getLength() > 1)
				for (int m = 0; m < rootList.getLength(); m++) {
					if ("RequestCorp".equals(rootList.item(m).getNodeName())) {
						topNode = rootList.item(m);
						break;
					}
				}
			else
				topNode = root.getFirstChild();
			// logger.debug("rootNodeName:" + topNode.getNodeName());
			NodeList nodeList = topNode.getChildNodes();

			User requestUser = new User();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				// logger.debug("======node("+node.getNodeName()+")======");
				if (UnitCode.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setUserCode(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (UnitName.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setUserNameText(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (Phone.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setTelePhone(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (EMail.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setMail(ele.getFirstChild().getNodeValue()
								.trim());
				} else if (Province.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setAddressProvince(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (IsSeal.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setIsSeal(ele.getFirstChild()
								.getNodeValue().trim());

				} else if (City.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setAddressCity(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (Area.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setAddressDistrict(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (Addr.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setAddressStreet(ele.getFirstChild()
								.getNodeValue().trim());
				}
			}

			return requestUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("xml转换错误,请检查报文", e);
		}
	}

	private User deptXmlToObject(String xmlFragment) throws Exception {
		logger.debug("请求报文:" + xmlFragment);
		try {
			InputStream inputStream = new ByteArrayInputStream(
					xmlFragment.getBytes("UTF-8"));

			DocumentReader documentReader = new DocumentReader();
			Document document = documentReader.getDocument(inputStream);
			Element root = document.getDocumentElement();
			Node topNode = null;
			NodeList rootList = root.getChildNodes();
			if (rootList.getLength() > 1)
				for (int m = 0; m < rootList.getLength(); m++) {
					if ("RequestDeptdoc".equals(rootList.item(m).getNodeName())) {
						topNode = rootList.item(m);
						break;
					}
				}
			else
				topNode = root.getFirstChild();
			NodeList nodeList = topNode.getChildNodes();
			User requestUser = new User();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (DeptCode.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setDeptCode(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (DeptName.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setDeptName(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (DeptPhone.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setDeptPhone(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (HrCanceled.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setHrCanceled(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (Canceled.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setCanceled(ele.getFirstChild()
								.getNodeValue().trim());
				} else if (Dr.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setDr(ele.getFirstChild().getNodeValue()
								.trim());
				} else if (DeptAddr.equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setDeptAddr(ele.getFirstChild()
								.getNodeValue().trim());
				}
			}
			return requestUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("xml转换错误,请检查报文", e);
		}
	}

	private User kgXmlToObject(String xmlFragment) throws Exception {
		logger.debug("请求报文:" + xmlFragment);
		try {
			// 以xml开头报错
			if (xmlFragment.contains("<?xml")) {
				xmlFragment = xmlFragment.replace("<?xml", "<?aaa");
			}
			InputStream inputStream = ProcessorUtils
					.getInputStream(xmlFragment);
			DocumentReader documentReader = new DocumentReader();
			Document document = documentReader.getDocument(inputStream);
			Element root = document.getDocumentElement();
			Node topNode = null;
			NodeList rootList = root.getChildNodes();
			if (rootList.getLength() > 1)
				for (int m = 0; m < rootList.getLength(); m++) {
					if ("RqeuestOrg".equals(rootList.item(m).getNodeName())) {
						topNode = rootList.item(m);
						break;
					}
				}
			else
				topNode = root.getFirstChild();

			logger.debug("rootNodeName:" + topNode.getNodeName());

			NodeList nodeList = topNode.getChildNodes();

			User requestUser = new User();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if ("code".equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setUserCode(ele.getFirstChild()
								.getNodeValue().trim());
				} else if ("name".equals(node.getNodeName())) {
					Element ele = (Element) node;
					if (ele.getFirstChild() != null)
						requestUser.setUserNameText(ele.getFirstChild()
								.getNodeValue().trim());
				}
			}
			requestUser.setUserPassword(Md5Encryption.MD5Encode("YTO"
					+ requestUser.getUserCode()));
			requestUser.setUserType("2");
			requestUser.setUserState("TBA");// 默认为待激活状态
			requestUser.setSite(requestUser.getUserCode());
			return requestUser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("xml转换错误,请检查报文", e);
		}
	}

	/**
	 * 将响应对象格式化为xml片段.
	 * 
	 * @return
	 */
	public String getVIPXmlFragment(boolean success, String vipId, String reason) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Response>");
		sb.append("<vipId>");
		sb.append(vipId);
		sb.append("</vipId>");
		sb.append("<success>");
		sb.append(success);
		sb.append("</success>");
		if (!success) {
			sb.append("<reason>");
			sb.append(reason);
			sb.append("</reason>");
		}
		sb.append("</Response>");
		return sb.toString();
	}

	/**
	 * 将响应对象格式化为xml片段.
	 * 
	 * @return
	 */
	public String getXmlFragment(boolean success) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding='UTF-8'?>");
		sb.append("<Response>");
		sb.append("<success>");
		sb.append(success);
		sb.append("</success>");
		if (!success) {
			sb.append("<reason>S01</reason>");
		}
		sb.append("</Response>");
		return sb.toString();
	}

	@Test
	public void test() {
		// String xmlfr= "<RequestCustomerInfo>"
		// +"<VipId>V6052727027</VipId>"
		// +"<VipName京东成都仓</VipName>"
		// +"<Code>210077</Code>"
		// +"<CreateTime>2011-08-03 14:21:39</CreateTime >"
		// +"</RequestCustomerInfo>";

		String xmlfr = "<RequestCustomerInfo><VipId>1</VipId> <VipName>1</VipName><Code>11</Code><CreateTime>2011-09-08</CreateTime ></RequestCustomerInfo>";

		xmlfr = "<?xml version=\"1.0\" encoding='UTF-8'?><ufinterface roottag=\"RequestCorp\" billtype=\"corp\" proc=\"add\"> <RequestCorp><pk_corp>0001</pk_corp><showorder>1</showorder><isuseretail>Y</isuseretail><taxpayertype>0</taxpayertype><maxinnercode>WDSD</maxinnercode><innercode>SEFSC004</innercode><region>行政区划</region><unitdistinction>高</unitdistinction><pk_corpkind>快递</pk_corpkind><chargedeptname>总裁办</chargedeptname><chargedeptcode>ZCB001</chargedeptcode><linkman1>路易斯</linkman1><linkman2>刘福</linkman2><linkman3>安大苏</linkman3><fax1>021-2365-4589</fax1><fax1>201-3652-1458</fax1><phone1>13545689874</phone1><phone2>021-69775486</phone2><phone3>021-69774589</phone3><zipcode>200030</zipcode><postaddr>上海市青浦区</postaddr><saleaddr>上海市青浦区华徐公路</saleaddr><taxcode>DJH25401</taxcode><legalbodycode>21540</legalbodycode><ecotype>现付</ecotype><industry>快递</industry><fathercorp>圆通总部</fathercorp><holdflag>N</holdflag><ownersharerate>90%</ownersharerate><unitshortname>武汉圆通</unitshortname><foreignname>wuhanyuantong</foreignname><regcapital>100万</regcapital><createdate>2004-06-14</createdate><ishasaccount>Y</ishasaccount><unitname>武汉圆通速度</unitname><unitcode>WHYT2541</unitcode><isseal>Y</isseal><isworkingunit>Y</isworkingunit><memo>备注</memo><enddate>2004-06-14</enddate><begindate>2004-06-14</begindate><pk_currency>RMB</pk_currency><idnumber>610265197505167611</idnumber><corptype>直营</corptype><sealeddate>2004-06-14</sealeddate><briefintro>简介</briefintro><citycounty>湖北省武汉市</citycounty><province>湖北省</province><countryarea>中国</countryarea><url>www.yto56.com.cn</url><email1>WHYTO56@163.COM</email1><email2>WHYTO56ZY@163.COM</email2><email3>HBWHYTO56@163.COM</email3><ts>2004-06-14 12:30:15</ts><dr>10005</dr><def1>自定义</def1><def2>自定义</def2><def19>自定义</def19><def20>自定义</def20></RequestCorp></ufinterface>";

		logger.debug(xmlfr);
		try {
			// xmlToVIP(xmlfr);
			User user = xmlToObject(xmlfr);
			logger.debug(user);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}

	}
}
