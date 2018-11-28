package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.sf.json.JSONException;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.constains.CachePrefixConstant;
import net.ytoec.kernel.constains.StringConstant;
import net.ytoec.kernel.dao.UserCustomDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dao.UserThreadContractDao;
import net.ytoec.kernel.dao.UserThreadDao;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserRelation;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.MemcacheService;
import net.ytoec.kernel.service.UserRelationService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务接口实现类
 * 
 * @author ChenRen
 * @date 2011-7-26
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class UserServiceImpl<T extends User> implements UserService<T> {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	// VIP用户激活判断编码状态的常量字段
	private static final String CHECK_USERCODE_NOTEXIST = "编码不存在!";
	private static final String CHECK_USERCODE_NOTUNIQUEN = "编码不唯一!";
	private static final String CHECK_USERCODE_ACTIVED = "编码已激活!";
	private static final String CHECK_USERCODE_TOBEACTIVE = "TBA"; // 待激活
	private static final String CHECK_USERCODE_UNKNOWERROE = "未知错误!";

	@Inject
	private UserDao<T> dao;
	@Inject
	private UserThreadDao<UserThread> uhDao;
	@Inject
	private UserThreadContractDao<UserThreadContract> contractDao;
	@Inject
	private UserCustomDao<UserCustom> ucDao;
	@Inject
	private MailService<Mail> mailService;
	@Inject
	private UserRelationService<UserRelation> userRelationService;

	@Inject
	private MemcacheService<User> memcacheService;

	public boolean addUser(T entity) {

		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}

		if (!StringUtils.isEmpty(entity.getUserPassword())) {
			entity.setUserPassword(Md5Encryption.MD5Encode(entity
					.getUserPassword()));
		}
		memcacheService.add(
				CachePrefixConstant.USER + entity.getTaobaoEncodeKey(), entity);
		memcacheService.add(CachePrefixConstant.USER + entity.getUserName(),
				entity);
		// 如果parentId不为空，就认为是新增子账号
		if (entity.getParentId() != null) {
			logger.info("新增子账号.");

			T user = dao.getUserById(entity.getParentId());
			if (user == null) {
				logger.error("新增子账号失败. 父对象不存在. 参数信息[parentId:"
						+ entity.getParentId() + "]");
				return false;
			}
		}

		// 插入新增承包区客户
		List<String> userCodes = new ArrayList<String>();
		List<UserThreadContract> userContractList = new ArrayList<UserThreadContract>();
		userContractList = entity.getUserContractList();
		if (userContractList != null && userContractList.size() != 0) {
			for (int i = 0; i < userContractList.size(); i++) {
				UserThreadContract utc = userContractList.get(i);
				UserThread ut = uhDao.getUserById(utc.getConractAreaId());
				userCodes.add(ut.getUserCode());
				// userThreadContractService.insertContract(utc);
				contractDao.insertContract(utc);
			}
		}

		// 新增网点用户
		if ("2".equals(entity.getUserType())) {

			// 如果是新增承包区客户是某个已经绑定卖家的客户，更新该卖家的site为承包区子账号名称
			if (userCodes != null && userCodes.size() != 0) {
				Map map = new HashMap();
				map.put("userCodes", userCodes);
				map.put("userName", entity.getUserName());
				updateSiteByUserCode(map);
			}

			// 新增网点用户
			dao.add(entity);

			// 设置网点的taobaoEncodeKey
			Map map1 = new HashMap();
			map1.put("id", entity.getId());
			map1.put("taobaoEncodeKey", entity.getId());

			return dao.updateTaobaoEncodeKeyById(map1);
		}

		// 新增子网点用户
		else {

			return dao.add(entity);
		}

	}

	public boolean addDeptUser(T entity) {

		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}

		/*
		 * @2011-09-09/ChenRen
		 * 
		 * @Description 现在核心系统没有用户新增操作, 新增用户实际上是从 金刚 同步过来. 同步的用户只需要用户编码, 用户登录
		 * 核心系统 的时候要先根据 编码 激活才能登录. 所以这里[新增]取消验证. 验证添加到[激活#activateUser]的方法里. //
		 * 在服务器进行用户帐号唯一性的验证 List<T> list = dao.searchUsersByName(entity); if
		 * (list.size() > 0) { logger.error("新增失败![" + entity.getUserName() +
		 * "]已存在!"); } // 对密码进行MD5加密
		 * entity.setUserPassword(Md5Encryption.MD5Encode(entity.getUserPassword
		 * ()));
		 */

		if (!StringUtils.isEmpty(entity.getUserPassword())) {
			entity.setUserPassword(Md5Encryption.MD5Encode(entity
					.getUserPassword()));
		}
		memcacheService.add(CachePrefixConstant.USER + entity.getUserName(),
				entity);
		// 如果parentId不为空，就认为是新增子账号
		if (entity.getParentId() != null) {
			logger.info("新增子账号.");

			T user = dao.getUserById(entity.getParentId());
			if (user == null) {
				logger.error("新增子账号失败. 父对象不存在. 参数信息[parentId:"
						+ entity.getParentId() + "]");
				return false;
			}

			/*
			 * String customerId = user.getTaobaoEncodeKey(); customerId += ","
			 * + entity.getTaobaoEncodeKey();
			 * user.setTaobaoEncodeKey(customerId); // 修改父账号的customerId
			 * 
			 * dao.edit(user);
			 */
		}

		// 新增网点
		if ("2".equals(entity.getUserType())) {

			dao.addDeptUser(entity);

			// 设置网点的taobaoEncodeKey
			Map map1 = new HashMap();
			map1.put("id", entity.getId());
			map1.put("taobaoEncodeKey", entity.getId());
			return dao.updateTaobaoEncodeKeyById(map1);
		} else {
			return dao.addDeptUser(entity);
		}

	}

	public T getUserById(Integer id) {
		return dao.getUserById(id);
	}

	public boolean edit(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}

		// 插入新增承包区客户
		List<String> userCodes = new ArrayList<String>();
		List<UserThreadContract> userContractList = new ArrayList<UserThreadContract>();
		userContractList = entity.getUserContractList();
		if (userContractList != null && userContractList.size() != 0) {
			for (int i = 0; i < userContractList.size(); i++) {
				UserThreadContract utc = userContractList.get(i);
				UserThread ut = uhDao.getUserById(utc.getConractAreaId());
				userCodes.add(ut.getUserCode());
				contractDao.insertContract(utc);
			}
		}

		// 如果是新增承包区客户是某个已经绑定卖家的客户，更新该卖家的site为承包区子账号名称
		if ("2".equals(entity.getUserType()) && userCodes != null
				&& userCodes.size() != 0) {
			Map map = new HashMap();
			map.put("userCodes", userCodes);
			map.put("userName", entity.getUserName());
			updateSiteByUserCode(map);
		}
		memcacheService.add(CachePrefixConstant.USER + entity.getUserName(),
				entity);
		return dao.edit(entity);
	}

	public boolean updateUser(T entity) {
		return edit(entity);
	}

	public boolean delUserById(Integer id) {

		T entity = dao.getUserById(id);
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}

		return dao.delUser(entity);
	}

	@SuppressWarnings("rawtypes")
	public List<T> searchUsers(T entity, Pagination<T> pagination, boolean flag) {

		if (StringUtils.isEmpty(entity.getSite())) {
			logger.error("site为空, 没有指定VIP用户所属网点!");
			return Collections.EMPTY_LIST;
		}

		Map map = new Hashtable();
		if (flag) {
			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());
		}
		map.put("site", entity.getSite());
		map.put("userNameText",
				entity.getUserNameText() == null ? "" : entity
						.getUserNameText());

		return dao.searchUsers(map);
	}

	/*
	 * public List<T> listPosttempUser(T entity, Pagination<T> pagination,
	 * boolean flag) { // field002里设置的是网点的Id
	 * if(StringUtils.isEmpty(entity.getField002() ) ) { // 在action里指定了用户的网点Id，见
	 * userAction.java#listPosttempUser // 设置网点Id是为了根据网点Id去查询已经分配过的模板的用户
	 * logger.error("参数异常! 没有指定用户的网点Id!"); return Collections.EMPTY_LIST; }
	 * List<PosttempUser> puList =
	 * puDao.getPosttempUserByBranchId(Integer.parseInt(entity.getField002() )
	 * ); List<T> uList = searchUsers(entity, pagination, flag); for
	 * (PosttempUser posttempUser : puList) { for (T user : uList) {
	 * if(user.getId().equals(posttempUser.getVipId() ) ) { //
	 * 设置用户的备用字段field003为false，标识当前用户已经分配过模板 // 页面显示用户的时候判断该值，如果为false，不让网点选择该用户
	 * user.setField003("false"); } } } // for posttempUser return uList; }
	 */

	public boolean checkName(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}

		// 在Dao里查询数据集合，如果集合大于0返回false
		List<T> list = dao.searchUsersByName(entity);
		return list.size() > 0 ? false : true;
	}

	public boolean edit_pwd(T entity, String newPwd) {

		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		if (StringUtils.isEmpty(entity.getUserPassword())) {
			logger.error("password is empity");
			return false;
		}

		boolean flag = false;
		// 对密码进行MD5加密
		entity.setUserPassword(Md5Encryption.MD5Encode(entity.getUserPassword()));
		List<T> list = dao.searchUserByNameAndPwd(entity);
		/*
		 * @2011-09-23/ChenRen 修改 操作失败 的提示信息. action里捕捉提示信息显示到页面
		 */
		// 存在多个相同的记录
		if (list.size() > 1) {
			logger.error("数据异常! 存在多个相同的数据对象!");
			return false;
		}
		if (list.size() < 1) {
			logger.error("原密码不对!");
			return false;
		}
		entity = list.get(0);
		entity.setUserPassword(Md5Encryption.MD5Encode(newPwd));
		flag = dao.edit(entity);

		return flag;
	}

	@Override
	public boolean checkPassword(T entity) {
		if (entity == null) {
			return false;
		}
		if (StringUtils.isEmpty(entity.getUserPassword())) {
			logger.error("password is empity");
		}
		boolean flag = false;
		// 对密码进行MD5加密
		entity.setUserPassword(Md5Encryption.MD5Encode(entity.getUserPassword()));
		List<T> list = dao.searchUserByNameAndPwd(entity);
		if (list.size() > 1) {
			flag = false;
		} else if (list.size() < 1) {
			flag = false;
		} else
			flag = true;
		return flag;
	}

	public boolean editSite(T entity) {

		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		if (StringUtils.isEmpty(entity.getSite())) {
			logger.error("参数异常! 网点编码为空!");
			return false;
		}
		if (entity.getId() == null) {
			logger.error("Id is empity");
			return false;
		}

		boolean flag = false;
		T dbUser = dao.getUserById(entity.getId());
		if (dbUser == null) {
			logger.error("dbUser is empity");
			return false;
		}

		dbUser.setSite(entity.getSite());

		flag = dao.edit(dbUser);
		return flag;
	} // editSite

	public List<T> searchUsersBySite(String site) {
		if (site != null) {
			User user = new User();
			user.setSite(site);
			List<T> list = dao.searchUsersBySite((T) user);
			return list;
		}
		return null;
	}

	public List<T> searchUsersBySiteAndUserType(String site, String userType) {
		if (site != null && userType != null) {

			User user = new User();
			user.setSite(site);
			user.setUserType(userType);
			List<T> list = dao.searchUsersBySiteAndUserType((T) user);
			return list;
		}
		return null;
	}

	public T searchUsersByUserName(String userName) {
		if (userName != "") {
			User user = new User();
			user.setUserName(userName);
			return dao.searchUsersByUserName((T) user);
		}
		return null;
	}

	public T searchUsersByShopAccount(String shopAccount) {
		if (shopAccount != "") {

			User user = new User();
			user.setShopAccount(shopAccount);
			return dao.searchUsersByShopAccount((T) user).get(0);
		}
		return null;
	}

	public T searchUsersByLoginName(String loginName) {
		if (loginName != null && !loginName.equals("")) {
			User user = new User();
			user.setUserName(loginName);
			return dao.searchUsersByLoginName((T) user);
		}
		return null;
	}

	public T searchUserByUserNameAndPass(String userName, String password) {
		if (userName != "" && password != "") {
			User user = new User();
			user.setUserName(userName);
			user.setUserPassword(Md5Encryption.MD5Encode(password));
			if (dao.searchUserByNameAndPwd((T) user) != null
					&& dao.searchUserByNameAndPwd((T) user).size() > 0) {
				return dao.searchUserByNameAndPwd((T) user).get(0);
			}
		}
		return null;
	}

	public List<T> searchUsersByCode(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return Collections.EMPTY_LIST;
		}
		if (StringUtils.isEmpty(entity.getUserCode())) {
			logger.error("参数错误：用户编码为空!");
			return Collections.EMPTY_LIST;
		}

		return dao.searchUsersByCode(entity);
	}

	public List<T> searchUsersByDeptCode(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return Collections.EMPTY_LIST;
		}
		if (StringUtils.isEmpty(entity.getDeptCode())) {
			logger.error("参数错误：用户编码为空!");
			return Collections.EMPTY_LIST;
		}

		return dao.searchUsersByDeptCode(entity);
	}

	public String checkUserCode(T entity) {
		List<T> list = searchUsersByCode(entity);
		String msg = "";

		int size = list.size();
		// 编码不存在
		if (size == 0) {
			msg = CHECK_USERCODE_NOTEXIST + "<br> [userCode: "
					+ entity.getUserCode() + "]";
		}
		// 编码不唯一
		else if (size > 1) {
			msg = CHECK_USERCODE_NOTUNIQUEN + "<br> [userCode: "
					+ entity.getUserCode() + "; size: " + size + "]";
		} else if (size == 1) {
			T e = list.get(0);
			// 编码已激活
			if (!"TBA".equalsIgnoreCase(e.getUserState())) {
				msg = CHECK_USERCODE_ACTIVED + "<br> [userCode: "
						+ entity.getUserCode() + "]";
			}
			// 编码未激活
			if ("TBA".equalsIgnoreCase(e.getUserState())) {
				msg = CHECK_USERCODE_TOBEACTIVE;
			}
		}
		// 未知错误
		else {
			msg = CHECK_USERCODE_UNKNOWERROE + "<br> [userCode: "
					+ entity.getUserCode() + "]";
			;
		}

		return msg;
	} // checkUserCode

	public boolean activateUser(T entity) {
		boolean flag = true;

		List<T> list = searchUsersByCode(entity);
		int size = list.size();
		// 编码不存在
		if (size == 0) {
			flag = false;
			logger.error(CHECK_USERCODE_NOTEXIST + "<br> [userCode: "
					+ entity.getUserCode() + "]");
		}
		// 编码不唯一
		else if (size > 1) {
			flag = false;
			logger.error(CHECK_USERCODE_NOTUNIQUEN + "<br> [userCode: "
					+ entity.getUserCode() + "; size: " + size + "]");
		} else if (size == 1) {
			T e = list.get(0);
			// 编码已激活
			if (!"TBA".equalsIgnoreCase(e.getUserState())) {
				flag = false;
				logger.error(CHECK_USERCODE_ACTIVED + "<br> [userCode: "
						+ entity.getUserCode() + "]");
			}
			// 如果编码未激活, 就激活用户
			if ("TBA".equalsIgnoreCase(e.getUserState())) {

				entity.setId(e.getId());
				if (StringUtils.isEmpty(entity.getUserName())) {
					entity.setUserName(entity.getShopAccount());
				}
				// 对密码进行MD5加密
				// 如果密码为空, 默认密码 yto1234
				String pwd = StringUtils.defaultIfEmpty(
						entity.getUserPassword(), "yto1234");
				entity.setUserPassword(Md5Encryption.MD5Encode(pwd));
				entity.setUserState("1"); // 用户状态原来是TBA, 激活后改称1, 正常.
				entity.setUserType(StringUtils.defaultIfEmpty(e.getUserType(),
						"1")); // 用户类型. VIP用户

				/*
				 * @2011-09-09/ChenRen
				 * 
				 * @Description 新增的方法里不做验证. 因为是从金刚同步过来的信息. 改到 修改 的方法里验证
				 */
				// 在服务器进行用户帐号唯一性的验证
				List<T> xx = dao.searchUsersByName(entity);
				if (xx.size() > 0) {
					logger.error("激活失败![" + entity.getUserName() + "]已存在!");
				}

				this.edit(entity);
				flag = true;
			}
		}
		// 未知错误
		else {
			flag = false;
			logger.error(CHECK_USERCODE_UNKNOWERROE + "<br> [userCode: "
					+ entity.getUserCode() + "]");
		}

		return flag;
	}

	public boolean completeSite(T entity) {
		User siteUser = dao.getUserById(entity.getId());
		if (entity.getUserName() != null)
			siteUser.setUserName(entity.getUserName());
		siteUser.setUserPassword(Md5Encryption.MD5Encode(entity
				.getUserPassword()));
		if (entity.getUserNameText() != null)
			siteUser.setUserNameText(entity.getUserNameText());
		if (entity.getMail() != null)
			siteUser.setMail(entity.getMail());
		if (entity.getMobilePhone() != null)
			siteUser.setMobilePhone(entity.getMobilePhone());
		if (entity.getTelePhone() != null)
			siteUser.setTelePhone(entity.getTelePhone());
		siteUser.setUserName(siteUser.getSite());
		siteUser.setUserState("1");// 设置用户状态为1：正常
		siteUser.setAddressCity(entity.getAddressCity());
		siteUser.setAddressDistrict(entity.getAddressDistrict());
		siteUser.setAddressProvince(entity.getAddressProvince());
		siteUser.setAddressStreet(entity.getAddressStreet());
		siteUser.setField001(entity.getField001());// 保存网点激活时网点用户所属省的ID号
		boolean flag = dao.edit((T) siteUser);
		/*
		 * @2012-01-09/ChenRen 　 ChenRen 10:02:17 现在网点激活还要不要自动生成子账号 圆通/李隆勇
		 * 10:02:35 不要了 // 新增子账号(客服帐号/财务帐号) if(flag) { // 客服帐号 User kefu = new
		 * User(); kefu.setParentId(siteUser.getId());
		 * kefu.setUserName(siteUser.getUserCode() + "_kefu"); // 帐号规则：网点编码_kefu
		 * kefu.setUserPassword(Md5Encryption.MD5Encode(siteUser.getUserCode() +
		 * "_kefu") ); kefu.setUserNameText(siteUser.getUserNameText() + "_客服");
		 * kefu.setUserType("21"); // 网点的客服类型 kefu.setUserState("1"); // 已激活
		 * kefu.setField001(siteUser.getField001()); // 省份id // 财务帐号 User caiwu
		 * = new User(); caiwu.setParentId(siteUser.getId());
		 * caiwu.setUserName(siteUser.getUserCode() + "_caiwu"); //
		 * 帐号规则：网点编码_kefu
		 * caiwu.setUserPassword(Md5Encryption.MD5Encode(siteUser.getUserCode()
		 * + "_caiwu") ); caiwu.setUserNameText(siteUser.getUserNameText() +
		 * "_财务"); caiwu.setUserType("22"); // 网点的客服类型 caiwu.setUserState("1");
		 * // 已激活 caiwu.setField001(siteUser.getField001()); // 省份id dao.add((T)
		 * kefu); dao.add((T) caiwu); }
		 */
		return flag;
	}

	public List<T> getUserListByUserType(String userType) {
		if (userType != null) {
			User user = new User();
			user.setUserType(userType);
			List<T> list = dao.searchUsersByUserType((T) user);
			return list;
		}
		return null;
	}

	public List<T> searchUsersByTaoBao(String taobao) {
		return dao.searchUsersByTaoBao(taobao);
	}

	public List<T> searchUsersByUserSource(String userSource) {
		return dao.searchUsersByUserSource(userSource);
	}

	public String checkSite(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
		}

		List<T> list = dao.searchUsersBySite(entity);
		String msg = "";

		int size = list.size();
		if (size < 1) {

			msg = "没有匹配的数据!";
		}

		return msg;
	}

	public boolean bindTaoBaoUser(T entity) {
		boolean flag = true;

		// 服务端参数验证
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		if (StringUtils.isEmpty(entity.getShopAccount())) {
			logger.error("参数错误! 淘宝帐号为空!");
		}
		if (StringUtils.isEmpty(entity.getSite())) {
			logger.error("参数错误! 网点编码为空!");
		}

		List<T> list = dao.searchUsersByTaoBao(entity.getShopAccount());
		int size = list.size();
		if (size == 1) {
			T e = list.get(0);
			// 编码已激活
			if (!"TBA".equalsIgnoreCase(e.getUserState())) {
				flag = false;
				logger.error("用户已绑定!");
			}
			// 如果编码未激活, 就激活用户
			if ("TBA".equalsIgnoreCase(e.getUserState())) {
				e.setUserState("1");// 用户状态原来是TBA, 激活后改称1, 正常.
				e.setSite(entity.getSite());
				this.edit(e);
				flag = true;
			}
		} else if (size > 1) {

			flag = false;
			logger.error("数据异常! 存在多个相同用户! ");
		}
		// 未知错误
		else {

			flag = false;
			logger.error("数据异常! 请联系管理员! ");
		}

		return flag;
	}

	public boolean bindTaoBaoUserStep1(T entity) {
		boolean flag = true;

		// 服务端参数验证
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		if (StringUtils.isEmpty(entity.getShopAccount())) {
			logger.error("参数错误! 淘宝帐号为空!");
		}

		// 淘宝用户
		if (StringUtils.isNotEmpty(entity.getUserSource())
				&& "1".equals(entity.getUserType())) {
			List<T> list = dao.searchUsersByTaoBao(entity.getShopAccount());
			int size = list.size();
			if (size == 1) {
				T e = list.get(0);
				// 编码已激活
				if (!"TBA".equalsIgnoreCase(e.getUserState())) {
					flag = false;
					logger.error("用户已绑定!");
				}
				// 如果编码未激活, 就激活用户
				if ("TBA".equalsIgnoreCase(e.getUserState())) {

					// 保存基本信息
					String pwd = entity.getUserPassword();
					if (!StringUtils.isEmpty(pwd)) {
						e.setUserPassword(Md5Encryption.MD5Encode(pwd));
					}
					e.setMobilePhone(entity.getMobilePhone());
					e.setField003("9"); // 9表示用户只填写了基本信息，还没有激活
					e.setTelePhone(entity.getTelePhone());
					e.setMail(entity.getMail());
					e.setShopName(entity.getShopName());
					e.setUpdateTime(new Date());
					e.setAddressCity(entity.getAddressCity());
					e.setAddressDistrict(entity.getAddressDistrict());
					e.setAddressProvince(entity.getAddressProvince());
					e.setAddressStreet(entity.getAddressStreet());
					e.setField001(entity.getField001());

					this.edit(e);
					flag = true;
				}
			} else if (size > 1) {

				flag = false;
				logger.error("数据异常! 存在多个淘宝帐号相同的用户!" + " 参数信息[shopAccount:"
						+ entity.getShopAccount() + "]");
			} else if (size == 0) {
				flag = false;
				logger.error("数据异常! 指定用户不存在! " + " 参数信息[shopAccount:"
						+ entity.getShopAccount() + "]");
			}
		}
		// 平台用户的分仓账号
		else {
			T dbUser = dao.get(entity);
			// 保存基本信息
			String pwd = entity.getUserPassword();
			if (!StringUtils.isEmpty(pwd)) {
				dbUser.setUserPassword(Md5Encryption.MD5Encode(pwd));
			}
			dbUser.setMobilePhone(entity.getMobilePhone());
			dbUser.setField003("9"); // 9表示用户只填写了基本信息，还没有激活
			dbUser.setTelePhone(entity.getTelePhone());
			dbUser.setMail(entity.getMail());
			dbUser.setShopName(entity.getShopName());
			dbUser.setUpdateTime(new Date());
			dbUser.setAddressCity(entity.getAddressCity());
			dbUser.setAddressDistrict(entity.getAddressDistrict());
			dbUser.setAddressProvince(entity.getAddressProvince());
			dbUser.setAddressStreet(entity.getAddressStreet());
			dbUser.setField001(entity.getField001());

			this.edit(dbUser);
			flag = true;
		}

		return flag;
	}

	public boolean bindTaoBaoUserStep2(T entity) {
		boolean flag = true;

		// 服务端参数验证
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		// if (StringUtils.isEmpty(entity.getShopAccount())) {
		// logger.error("参数错误! 淘宝帐号为空!");
		// return false;
		// }
		// if (StringUtils.isEmpty(entity.getSite())) {
		// logger.error("参数错误! 网点编码为空!");
		// return false;
		// }

		T entity2 = dao.get(entity);
		if (entity2 == null) {
			logger.info("用户不存在!");
			return false;
		}

		entity2.setUserState("1");// 用户状态原来是TBA, 激活后改称1, 正常.

		// 判断该直客编码的客户是不是已经被分配为承包区子账号
		UserThread ut = new UserThread();
		ut.setUserCode(entity.getUserCode());
		ut.setUserState(null);
		List<UserThread> utList = uhDao.searchUsersByCode(ut);
		if (utList != null && utList.size() >= 1) {
			List<UserThreadContract> utcs = contractDao
					.searchContractByConractAreaId(utList.get(0).getId(), "2");
			if (utcs != null && utcs.size() == 1) {
				entity2.setSite(utcs.get(0).getAddUserName()); // user表中某卖家的site修改为该直客已经是承包区客户的新增子账号的名称
			} else {
				entity2.setSite(entity.getSite()); // user表中某卖家的site修改为网点
			}
		} else {
			entity2.setSite(entity.getSite());
		}

		entity2.setUserCode(entity.getUserCode());
		entity2.setUpdateTime(new Date());
		entity2.setLoginTime(new Date());
		if ("9".equals(entity2.getField003())) {
			entity2.setField003(null);
		}

		flag = this.edit(entity2);
		if (flag) {
			// 关联账号绑定同一网点
			List<T> relatedUsers = getUsersByCustomerIds(entity2
					.getBindedCustomerId());
			for (T relUser : relatedUsers) {
				relUser.setUserState("1");// 用户状态原来是TBA, 激活后改称1, 正常.
				relUser.setSite(entity2.getSite());
				relUser.setUserCode(entity.getUserCode());
				relUser.setUpdateTime(new Date());
				relUser.setLoginTime(new Date());
				if ("9".equals(entity2.getField003())) {
					relUser.setField003(null);
				}
				dao.edit(relUser);
			}

			Map subUserMap = this.getUserByParentId(entity.getId(), null); // null
																			// 表示不分页
			// List<User> subUserList = (List) MapUtils.getObject(subUserMap,
			// "listt", Collections.EMPTY_LIST);
			Object obj = subUserMap.get("listt");
			if (obj != null) {
				List<T> subUserList = (List<T>) obj;
				for (T subUser : subUserList) {
					subUser.setUserState("1"); // 更新子账号的状态
					subUser.setSite(entity.getSite());
					subUser.setUserCode(entity.getUserCode());
					subUser.setField003(entity2.getField003());
					entity2.setUpdateTime(new Date());
					dao.edit(subUser);
				}
			}
		}

		/*
		 * List<T> list = dao.searchUsersByTaoBao(entity.getShopAccount()); int
		 * size = list.size(); if (size == 1) { T e = list.get(0); // 编码已激活 if
		 * (!"TBA".equalsIgnoreCase(e.getUserState())) { flag = false;
		 * logger.error("用户已绑定!"); } // 如果编码未激活, 就激活用户 if
		 * ("TBA".equalsIgnoreCase(e.getUserState())) { e.setUserState("1");//
		 * 用户状态原来是TBA, 激活后改称1, 正常. e.setSite(entity.getSite());
		 * e.setUserCode(entity.getUserCode()); e.setUpdateTime(new Date());
		 * e.setLoginTime(new Date()); if ("9".equals(e.getField003())) {
		 * e.setField003(null); }
		 * 
		 * this.edit(e); flag = true; } } else if (size > 1) {
		 * 
		 * flag = false; logger.error("数据异常! 存在多个相同用户! "); } // 未知错误 else {
		 * 
		 * flag = false; logger.error("数据异常! 请联系管理员! "); }
		 */
		return flag;
	}

	public T loginUserCheck(String userName, String password) {
		if (userName != "" && password != "") {
			User user = new User();
			user.setUserName(userName);
			user.setUserCode(userName);
			user.setUserPassword(Md5Encryption.MD5Encode(password));
			List<T> list = dao.loginUserCheck((T) user);
			if (list != null && list.size() > 0) {
				T entity = list.get(0);
				String userType = entity.getUserType();
				if ("11".equals(userType) || "12".equals(userType)
						|| "13".equals(userType) || "21".equals(userType)
						|| "22".equals(userType) || "23".equals(userType)) {
					T parent = this.getUserById(entity.getParentId());
					// 给卖家子账号
					entity.setTaobaoEncodeKey(parent.getTaobaoEncodeKey());
					entity.setUserCode(parent.getUserCode());
					entity.setShopAccount(parent.getShopAccount());
					// entity.setShopName(parent.getShopName());
				}
				return entity;
			}
		}
		return null;
	}

	public T getSiteByVipId(Integer id) {
		List<T> list = dao.getSiteByVipId(id);
		if (list == null || list.size() == 0) {
			logger.error("数据异常! 用户没有匹配的网点信息! 参数信息[userId:" + id + "]");
		}
		if (list.size() > 1) {
			logger.error("数据异常! 当前用户存在多个网点数据! 参数信息[userId:" + id
					+ "; lise.size():" + list.size() + "]");
		}
		return list.get(0);
	}

	@Override
	public boolean add(T entity) {
		return addUser(entity);
	}

	@Override
	public boolean remove(T entity) {
		return dao.remove(entity);
	}

	@Override
	public T get(T entity) {
		return dao.get(entity);
	}

	@Override
	public boolean editUser(T entity) {

		// #我的账号 #修改基本信息
		// 因为只修改下面几个字段值，为了避免清空数据中原有的值，所有先查询再设置值。
		T dbUser = dao.get(entity);
		dbUser.setMobilePhone(entity.getMobilePhone()); // 手机
		dbUser.setTelePhone(entity.getTelePhone()); // 电话
		dbUser.setMail(entity.getMail()); // mail
		dbUser.setShopName(entity.getShopName()); // 店铺名称
		dbUser.setAddressProvince(entity.getAddressProvince());// 省
		dbUser.setAddressCity(entity.getAddressCity());// 市
		dbUser.setAddressDistrict(entity.getAddressDistrict());// 区
		dbUser.setAddressStreet(entity.getAddressStreet());// 详细地址
		dbUser.setField001(entity.getField001());// 省份Id

		return edit(dbUser);
	}

	@Override
	public boolean modifyUser(T entity) {
		if (entity == null) {
			logger.error("entity is empity");
			return false;
		}
		return dao.modifyUser(entity);
	}

	@Override
	public List<T> getAllUser() {
		return dao.getAllUser();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getUserByParentId(Integer pid, Pagination<User> pagination)
			throws DataAccessException {
		boolean flag = true; // 分页翻页
		if (pagination == null)
			flag = false;

		List<T> list;
		Map map = new HashMap();
		map.put("parentId", pid);
		// 分页查询
		if (flag) {
			flag = false;

			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());

			list = dao.getUserByParentId(map);
			map.put("list", list);
		}
		if (!flag) {
			map.remove("startIndex");
			map.remove("pageNum");

			list = dao.getUserByParentId(map);
			map.put("totalRecords", list.size());
			map.put("listt", list);
		}

		return map;
	}

	@Override
	public List<T> getUserByParentId(Integer pid) throws DataAccessException {
		return (List<T>) getUserByParentId(pid, null).get("listt");
	}

	@Override
	public boolean bindTaoBaoAccount(T entity, String flag) {
		if (entity == null) {
			logger.error("用户对象为空! ");
			return false;
		}
		T user = dao.getUserById(entity.getId());
		if (user == null) {
			logger.error("用户对象不存在! 参数信息[user.id:" + entity.getId() + "]");
			return false;
		}

		// 父对象
		T pUser = dao.getUserById(user.getParentId());
		if (pUser == null) {
			logger.error("用户父对象不存在! 参数信息[user.id:" + entity.getId()
					+ "; parentId:" + user.getParentId() + "]");
			return false;
		}

		boolean xx = true;
		boolean isBind = "1".equals(flag) ? true : false;
		// 绑定
		if (isBind) {
			user.setUserState("TBA"); // 用户状态：从禁用(0)改成待激活(TBA)

			String customerId = pUser.getTaobaoEncodeKey();
			// 过滤空值
			if (!StringUtils.isEmpty(customerId)) {
				customerId += "," + user.getTaobaoEncodeKey(); // 重新拼接customerId
				pUser.setTaobaoEncodeKey(customerId);
			}
		}
		// 取消绑定
		else {
			user.setUserState("0"); // 用户状态：禁用

			String[] pCustomerIdArr = pUser.getTaobaoEncodeKey().split(",");
			String newStr = "";
			for (String customerId : pCustomerIdArr) {
				if (!StringUtils.isEmpty(customerId)
						&& !customerId.equals(user.getTaobaoEncodeKey())) {
					// 重新拼接customerId; 过滤当前的customerId
					newStr += "," + customerId;
				}
			}
			newStr = newStr.length() > 0 ? newStr.substring(1) : newStr;
			pUser.setTaobaoEncodeKey(newStr);
		}

		xx = dao.edit(pUser);
		if (xx) {
			xx = dao.edit(user);
		}
		return xx;
	} // bindSubAccount

	@Override
	public T getUserByCustomerId(String customerId) {
		if (StringUtils.isEmpty(customerId)) {
			logger.error("用户customerId为空");
			return (T) new User();
		}

		T TT = dao.getUserByCustomerId(customerId);
		if (TT == null) {
			logger.error("用户对象不存在! 参数信息[customerId:" + customerId + "]");
			return (T) new User();
		}

		// 如果用户存在父对象，就返回父对象
		if (TT.getParentId() != null) {
			T pTT = dao.getUserById(TT.getParentId());

			if (pTT == null) {
				logger.error("用户的父对象不存在! 参数信息[userId:" + TT.getId()
						+ ";parentId:" + TT.getParentId() + "]");
				return (T) new User();
			}
			return pTT;
		}

		return TT;
	}

	@Override
	public boolean bindSubAccount(T entity, String flag) {
		if (entity == null) {
			logger.error("用户对象为空! ");
			return false;
		}
		T user = dao.getUserById(entity.getId());
		if (user == null) {
			logger.error("用户对象不存在! 参数信息[user.id:" + entity.getId() + "]");
			return false;
		}

		// 父对象
		/*
		 * T pUser = dao.getUserById(user.getParentId()); if (pUser == null) {
		 * logger.error("用户父对象不存在! 参数信息[user.id:" + entity.getId() +
		 * "; parentId:" + user.getParentId() + "]"); return false; }
		 */

		String state = "1".equals(flag) ? "1" : "0";
		user.setUserState(state); // 用户状态：禁用(0)/已激活(1)

		/*
		 * boolean xx = dao.edit(pUser); if (xx) { xx = dao.edit(user); }
		 */
		boolean xx = dao.edit(user);
		return xx;
	}

	@Override
	public List<T> searchUsersByCodeTypeState(String userCode, String userType,
			String userState) {
		if (userCode == null || userType == null || userState == null)
			return null;
		if (userCode.trim().equals("") || userType.trim().equals("")
				|| userState.trim().equals(""))
			return null;
		User user = new User();
		user.setUserCode(userCode.trim());
		user.setUserType(userType.trim());
		user.setUserState(userState.trim());
		List<T> list = dao.searchUsersByCodeTypeState((T) user);
		return list;
	}

	@Override
	public boolean bindedAssociationAccount(T targetAccount,
			T associationAccount) {
		boolean result = false;
		if (targetAccount == null || associationAccount == null) {
			return result;
		}
		T t = dao.getUserByCustomerId(associationAccount.getTaobaoEncodeKey());

		// 判断条件中增加对用户名是否重复的验证。
		if (t == null && checkName(associationAccount)) {
			result = dao.add(associationAccount);
			// 新用户 同时也要在个性化配置表内插入一条自己的个性化配置信息
			UserCustom tc = new UserCustom();
			tc.setUserName(associationAccount.getUserName());
			tc.setBindedUserName(associationAccount.getUserName());
			tc.setCustomerId(associationAccount.getTaobaoEncodeKey());
			tc.setType(UserCustom.RELATIONAL);
			tc.setRelationalQuery(UserCustom.RELATIONAL);
			ucDao.add(tc);
		} else if (t == null && !checkName(associationAccount)) {
			return false;// 如果淘宝用户名和系统中已经存在的用户名重复，则不允许关联绑定。
		}
		if (t != null) {
			associationAccount.setId(t.getId());
			result = dao.edit(associationAccount);
		}
		// （一）添加两个账号之间的个性化配置信息：当A关联B时默认A已经勾选了B，因关联是双向的，同时B默认勾选了A
		UserCustom ucA = new UserCustom();
		ucA.setUserName(targetAccount.getUserName());
		ucA.setBindedUserName(associationAccount.getUserName());
		ucA.setCustomerId(associationAccount.getTaobaoEncodeKey());
		ucA.setType(UserCustom.RELATIONAL);
		ucA.setRelationalQuery(UserCustom.RELATIONAL);
		ucDao.add(ucA);
		UserCustom ucB = new UserCustom();
		ucB.setUserName(associationAccount.getUserName());
		ucB.setBindedUserName(targetAccount.getUserName());
		ucB.setCustomerId(targetAccount.getTaobaoEncodeKey());
		ucB.setType(UserCustom.RELATIONAL);
		ucB.setRelationalQuery(UserCustom.RELATIONAL);
		ucDao.add(ucB);
		// （二）添加两个账号的关联关系
		UserRelation uRelation = new UserRelation();
		uRelation.setUserId(targetAccount.getId());
		uRelation.setRelatedUserId(associationAccount.getId());
		userRelationService.add(uRelation);
		{// 同步绑定信息给子帐号
			List<T> subUsersA = dao.getUserByParentId(targetAccount.getId());
			if (!subUsersA.isEmpty()) {
				for (T user : subUsersA) {
					UserCustom uct = new UserCustom();
					uct.setUserName(user.getUserName());
					uct.setBindedUserName(associationAccount.getUserName());
					uct.setCustomerId(associationAccount.getTaobaoEncodeKey());
					uct.setType(UserCustom.RELATIONAL);
					uct.setRelationalQuery(UserCustom.RELATIONAL);
					ucDao.add(uct);
				}
			}
			List<T> subUsersB = dao.getUserByParentId(associationAccount
					.getId());
			if (!subUsersB.isEmpty()) {
				for (T user : subUsersB) {
					UserCustom uct = new UserCustom();
					uct.setUserName(user.getUserName());
					uct.setBindedUserName(targetAccount.getUserName());
					uct.setCustomerId(targetAccount.getTaobaoEncodeKey());
					uct.setType(UserCustom.RELATIONAL);
					uct.setRelationalQuery(UserCustom.RELATIONAL);
					ucDao.add(uct);
				}
			}
		}
		return result;
	}

	@Override
	public List<T> getUsersByCustomerIds(String customerIds) {
		if (StringUtils.isEmpty(customerIds)) {
			return Collections.EMPTY_LIST;
		}
		String[] customerIdArray = StringUtils.split(customerIds,
				StringConstant.COMMA);

		return dao.getUsersByCustomerIds(Arrays.asList(customerIdArray));
	}

	@Override
	public boolean cancelAssociationAccount(T user) {
		boolean result = dao.edit(user);

		// 给子帐号也解除绑定关系
		List<T> subUsers = dao.getUserByParentId(user.getId());
		for (T t : subUsers) {
			t.setBindedCustomerId(user.getBindedCustomerId());
			result = dao.edit(t);
			if (!result) {
				break;
			}
		}
		return result;
	}

	/**
	 * 此方法已经删除不适用了，被重载方法editUserCode(T currentUser, String oldUserCode, String
	 * newUserCode)代替适用
	 */
	@Override
	public boolean editUserCode(String oldUserCode, String newUserCode) {
		logger.info("修改用户编码.原编码：" + oldUserCode + ";新编码：" + newUserCode);

		boolean flag = false;
		// 把原用户改成未激活状态(userthread)
		UserThread uh1 = new UserThread();
		uh1.setUserCode(oldUserCode);
		List<UserThread> uhList = uhDao.searchUsersByCode(uh1);
		for (UserThread Tuh1 : uhList) {
			Tuh1.setUserState("TBA");
			flag = uhDao.editUser(Tuh1);
		}

		if (!flag)
			return false;

		// 修改新用户状态
		UserThread uh2 = new UserThread();
		uh2.setUserCode(newUserCode);
		List<UserThread> uh2List = uhDao.searchUsersByCode(uh2);
		for (UserThread Tuh2 : uh2List) {
			uh2 = Tuh2; // 把查询出来的对象赋值给外层变量. 下面修改user表数据要用到

			Tuh2.setUserState("1");
			// Tuh2.setUserCodeUpteTime(new Date()); // 更新时间戳
			Tuh2.setUserCodeUpteTime(DateUtil.valueOfStandard("2011-11-19")); // 更新时间戳为易通上线日期:2011-11-19

			flag = uhDao.editUser(Tuh2);
		}

		if (!flag)
			return false;

		// 修改user表中的用户编码和网点为新的数据
		T user1 = (T) new User();
		user1.setUserCode(oldUserCode);
		List<T> list = dao.searchUsersByCode(user1);
		for (T tt1 : list) {
			tt1.setUserCode(newUserCode);
			tt1.setSite(uh2.getSiteCode());
			// tt1.setUserNameText(uh2.getUserName());

			dao.edit(tt1);

		}

		return true;
	}

	@Override
	public boolean editUserCode(T currentUser, String oldUserCode,
			String newUserCode) {
		logger.info("修改用户编码.原编码：" + oldUserCode + ";新编码：" + newUserCode);

		boolean flag = false;

		// 修改新用户状态
		UserThread uh2 = new UserThread();
		uh2.setUserCode(newUserCode);
		List<UserThread> uh2List = uhDao.searchUsersByCode(uh2);
		for (UserThread Tuh2 : uh2List) {
			uh2 = Tuh2; // 把查询出来的对象赋值给外层变量. 下面修改user表数据要用到

			Tuh2.setUserState("1");
			// Tuh2.setUserCodeUpteTime(new Date()); // 更新时间戳
			Tuh2.setUserCodeUpteTime(DateUtil.valueOfStandard("2011-11-19")); // 更新时间戳为易通上线日期:2011-11-19

			flag = uhDao.editUser(Tuh2);
		}

		if (!flag) {
			return false;
		}

		// 修改user表中的用户编码和网点为新的数据
		T user1 = (T) new User();
		user1.setUserCode(oldUserCode);
		List<T> list = dao.searchUsersByCode(user1);
		for (T tt1 : list) {
			// 判断当前卖家是被关联的账号
			boolean isMainBusinessFlg = true;
			if (currentUser.getBindedCustomerId() == null) {
				if (currentUser.getUserName().equals(tt1.getUserName())) {
					isMainBusinessFlg = false;
				} else {
					continue;
				}
			} else {
				if (currentUser.getUserName().equals(tt1.getUserName())) {
					// 主账号进来自己需要更新
				} else {
					// 判断当前卖家是已经关联其它卖家的主账号,关联账号进来也需要更新
					String[] customerIds = currentUser.getBindedCustomerId()
							.split(",");
					List<String> customerList = Arrays.asList(customerIds);

					if (!customerList.contains(tt1.getTaobaoEncodeKey())) {
						continue;
					}
				}

			}

			tt1.setUserCode(newUserCode);

			// 判断该直客编码的客户是不是已经被分配为承包区子账号
			UserThread ut = new UserThread();
			ut.setUserCode(newUserCode);
			ut.setUserState(null);
			List<UserThread> utList = uhDao.searchUsersByCode(ut);
			if (utList != null && utList.size() >= 1) {
				List<UserThreadContract> utcs = contractDao
						.searchContractByConractAreaId(utList.get(0).getId(),
								"2");
				if (utcs != null && utcs.size() == 1) {
					tt1.setSite(utcs.get(0).getAddUserName()); // user表中某卖家的site修改为该直客已经是承包区客户的新增子账号的名称
				} else {
					tt1.setSite(uh2.getSiteCode()); // user表中某卖家的site修改为网点
				}
			} else {
				tt1.setSite(uh2.getSiteCode());
			}

			// tt1.setUserNameText(uh2.getUserName());

			dao.edit(tt1);

			// 判断该UserCode还没有卖家绑定，如果没有绑定的卖家了，修改此UserThread的状态为TBA
			UserThread uh1 = new UserThread();
			uh1.setUserCode(oldUserCode);
			List<UserThread> uhList = uhDao.searchUsersByCode(uh1);
			if (!uhList.isEmpty()) {
				User u = new User();
				u.setUserCode(oldUserCode);
				for (UserThread Tuh1 : uhList) {
					List<T> userList = dao.searchUsersByCode((T) u);
					if (userList.isEmpty()) {
						Tuh1.setUserState("TBA");
						flag = uhDao.editUser(Tuh1);
					} else {
						flag = true;
					}
				}
			} else {
				flag = true;
			}

			if (!flag) {
				return false;
			}

			if (!isMainBusinessFlg) {
				break;
			}
		}

		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean unbindUserCode(T entity) {

		if (entity == null)
			return false;

		String tempUserCode = entity.getUserCode();
		List<T> subList = dao.getUserByParentId(entity.getId());
		for (T Tsubuser : subList) {
			Tsubuser.setUserState("TBA");
			Tsubuser.setField003("9"); // 已经完善了第一步基本信息
			Tsubuser.setUserCode(StringUtils.EMPTY);
			Tsubuser.setSite(StringUtils.EMPTY);
			dao.edit(Tsubuser);
		}
		entity.setUserState("TBA");
		entity.setField003("9"); // 已经完善了第一步基本信息
		entity.setUserCode(StringUtils.EMPTY);
		entity.setSite(StringUtils.EMPTY);
		boolean flag = dao.edit(entity);

		if (flag) {
			// 关联账号绑定同一网点
			List<T> relatedUsers = getUsersByCustomerIds(entity
					.getBindedCustomerId());
			for (T relUser : relatedUsers) {
				relUser.setUserState("TBA");
				relUser.setField003("9"); // 已经完善了第一步基本信息
				relUser.setUserCode(StringUtils.EMPTY);
				relUser.setSite(StringUtils.EMPTY);
				dao.edit(relUser);
			}

			// 根据userCode在本表查询使用了该code的用户(属于同一#直客)
			// 如果size < 1; 则认为#直客 没有再帮用户绑定, 把#直客 状态改成未激活状态
			entity.setUserCode(tempUserCode);
			List codeUserList = this.searchUsersByCode(entity);
			if (codeUserList.size() < 1) {
				UserThread uh = new UserThread();
				uh.setUserCode(tempUserCode);
				List<UserThread> uhList = uhDao.searchUsersByCode(uh);
				for (UserThread dbUH : uhList) {
					dbUH.setUserState("TBA");
					uhDao.editUser(dbUH);
				} // for
			}
		}// if flag

		return flag;
	} // unbindUserCode

	@Override
	public List<T> searchUsers(Map<String, Object> params,
			Pagination<T> pagination, boolean flag) {
		// TODO Auto-generated method stub
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		if (flag) {
			params.put("startIndex", pagination.getStartIndex());
			params.put("pageNum", pagination.getPageNum());
		}

		return dao.searchUsers(params);
	}

	@Override
	public T getUserByClientId(String clientId) {
		// TODO Auto-generated method stub
		return dao.getUserByClientId(clientId);
	}

	@SuppressWarnings("all")
	@Override
	public List<T> pingTaiSelect(User curUser, Integer type) {
		List<T> list = new ArrayList<T>();
		Map map = new HashMap();
		if (curUser != null) {
			int uid = curUser.getId();
			// 子账号/ 取父id
			if ("41".equals(curUser.getUserType())) {
				uid = curUser.getParentId();
			}
			map.put("userSource", uid);// 存放平台用户的主键找平台用户下的子账号
			map.put("type", type);
			list = dao.searchDepotHosting(map);
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, Object> pingTaiSelect(User curUser, Integer type,
			Pagination pagination) throws DataAccessException {
		// TODO Auto-generated method stub
		boolean flag = true; // 分页翻页
		if (pagination == null)
			flag = false;

		List<T> list;
		Map<String, Object> map = new HashMap<String, Object>();
		if (!curUser.getUserType().equals("4"))
			map.put("userSource", curUser.getParentId());// 存放平台用户的主键找子账号平台用户的主账号的id
		else
			map.put("userSource", curUser.getId());// 存放平台用户的主键找平台用户下的子账号
		map.put("type", type);
		// 分页查询

		if (flag) {
			flag = false;

			map.put("startIndex", pagination.getStartIndex());
			map.put("pageNum", pagination.getPageNum());

			list = dao.searchDepotHosting(map);
			map.put("list", list);
		}
		if (!flag) {
			map.remove("startIndex");
			map.remove("pageNum");

			list = dao.searchDepotHosting(map);
			map.put("totalRecords", list.size());
			map.put("listt", list);
		}

		return map;
	}

	@Override
	public List<T> pingTaiSelect(User curUser, Integer type, String status) {
		// TODO Auto-generated method stub
		List<T> list = new ArrayList<T>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (curUser != null) {
			if (!curUser.getUserType().equals("4"))
				map.put("userSource", curUser.getParentId());// 存放平台用户的主键找子账号平台用户的主账号的id
			else
				map.put("userSource", curUser.getId());// 存放平台用户的主键找平台用户下的子账号
		}
		if (type != null) {
			map.put("type", type);
		}
		if (StringUtils.isNotBlank(status)) {
			map.put("userState", status);
		}
		list = dao.searchDepotHosting(map);
		return list;
	}

	@Override
	public List<T> getRepeatSiteList() {
		return dao.getRepeatSiteList();
	}

	@Override
	public List<T> searchUsersByCodeAndType(String userCode, String userType) {
		if (!StringUtils.isEmpty(userCode) && !StringUtils.isEmpty(userType)) {
			User user = new User();
			user.setUserCode(userCode);
			user.setUserType(userType);
			return dao.searchUsersByCodeAndType((T) user);
		}
		return null;
	}

	@Override
	public boolean updateSiteByUserCode(Map map) {
		if (map == null) {
			logger.error("map is empity");
			return false;
		}
		return dao.updateSiteByUserCode(map);
	}

	@Override
	public boolean updateUserPrint(Map map) {
		if (map == null) {
			logger.error("map is empity");
			return false;
		}
		return dao.updateUserPrint(map);
	}

	@Override
	public List<T> getisPrintFlag(Map map) {
		return dao.getisPrintFlag(map);
	}

	@Override
	public boolean updateUserPrintNav(Map map) {
		if (map == null) {
			logger.error("map is empity");
			return false;
		}
		return dao.updateUserPrintNav(map);
	}

	@Override
	public String sendEmail(User currentUser) {
		String message = "";
		try {
			List<User> userList = (List<User>) searchUsersBySite(currentUser
					.getSite());
			for (User user : userList) {
				if (user.getMail() != null && !(user.getMail().equals(""))) {
					Mail mail = new Mail();
					mail.setFromMail("yitong@ytoxl.com");
					mail.setFromMailText("核心系统");
					mail.setSendToMail(user.getMail());
					mail.setSubject("易通打印正式上线啦！");
					mail.setContent("<b><b>您好，"
							+ user.getUserNameText()
							+ "<br><a href=\"http://ec.yto.net.cn\"><img border=\"0\" src=\"http://ec.yto.net.cn/images/single/print.jpg\"/></a>");
					if (mailService.sendMail(mail))
						message += user.getUserNameText() + "的邮件发送成功。";
					else
						message += user.getUserNameText() + "的邮件发送失败。";
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public T getUserByUserName(String userName) {
		// TODO Auto-generated method stub
		return dao.getUserByUserName(userName);
	}

	@Override
	public List<User> searchUserByTBKeyAndUserNameAndShopAccount() {
		// TODO Auto-generated method stub
		return dao.searchUserByTBKeyAndUserNameAndShopAccount();
	}

	/**
	 * 根据mobilePhone查找用户(服务管理模块用到) pararms :mobilePhone
	 * 
	 * @return List
	 */
	public List<T> getUserByMobilePhone(String mobilePhone) {
		return dao.getUserByMobilePhone(mobilePhone);
	}

	/**
	 * 通过用户类型查询用户列表(服务管理模块用到)
	 * 
	 * @param List
	 *            <String> userTypes
	 * @return List
	 */
	public List<T> getUserListByUserTypes(List<String> userTypes) {
		return dao.getUserListByUserTypes(userTypes);
	}

	@Override
	public boolean deleSubAccount(Integer userId) {
		User userTmp = dao.getUserById(userId);

		// 删除user表中的子账号
		dao.delUser((T) userTmp);

		// 删除UserThreadContract表中的分配客户的数据
		UserThreadContract utc = new UserThreadContract();
		utc.setAddUserName(userTmp.getUserName());
		contractDao.deleteContractByAddUsername(utc);

		List<User> userLst = (List<User>) dao
				.getUserByParentId(userTmp.getId());
		if (userLst != null && userLst.size() != 0) {
			for (User u : userLst) {
				// 删除user表中的子账号下面的账号
				dao.delUser((T) u);

				// 删除UserThreadContract表中的子账号下面的分配客户的数据
				UserThreadContract utc2 = new UserThreadContract();
				utc2.setAddUserName(u.getUserName());
				contractDao.deleteContractByAddUsername(utc2);
			}
		}

		if ("11".equals(userTmp.getUserType())
				|| "12".equals(userTmp.getUserType())
				|| "13".equals(userTmp.getUserType())) {
			ucDao.delUserCustom(userTmp.getUserName());
		}

		return true;
	}

	public T getUserByUserCode(String userCode) {
		return dao.getUserByUserCode(userCode);

	}

	@Override
	public String searchSiteMailByUserCode(String userCode) {
		return dao.searchSiteMailByUserCode(userCode);
	}
}
