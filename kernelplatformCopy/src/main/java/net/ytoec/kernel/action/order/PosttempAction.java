package net.ytoec.kernel.action.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Message;
import net.ytoec.kernel.dataobject.Postinfo;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.StandardPosttemp;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.service.MessageService;
import net.ytoec.kernel.service.PosttempService;
import net.ytoec.kernel.service.StandardPosttempService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.XmlUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 运费模板
 * 
 * @author ChenRen
 * @date 2011-09-09
 */

@Controller
@Scope("prototype")
public class PosttempAction extends AbstractActionSupport {

	private static final long serialVersionUID = -842866646130025461L;
	private Logger logger = LoggerFactory.getLogger(PosttempAction.class);

	@Inject
	private PosttempService<Posttemp> service;
	@Inject
	private UserService<User> userService;
	@Inject
	private UserThreadService<UserThread> userThreadService;
	@Inject
	private StandardPosttempService<StandardPosttemp> spService;
	@Inject
	private MessageService<Message> messageService;
	private Posttemp posttemp;
	/** 运费模板 */
	private List<Posttemp> postList;

	private Integer currentPage = 1;
	private Pagination<Posttemp> pagination;

	/** 运费模板关联的 VIP用户的id主键字符串; 多个用";"连接 */
	private String vipIds;
	/** 用户类型:网点/VIP=2/1 */
	private String userType;

	/** 用户计费方式 */
	private String calclateType;

	// ->
	// @2011-11-07/ChenRen 新增属性
	/**
	 * 始发地<br>
	 * 根据网点Id去user表中查
	 */
	private String fromAddr;
	/** 标准运费信息 */
	private List<StandardPosttemp> spList;
	private StandardPosttemp standardPosttemp;
	// ->

	private int destId;

	/** 当前方法的请求地址（包括参数） */
	private String url;

	private Postinfo postinfo;
	
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// === actions ===
	/**
	 * 跳转到运费模板列表页<br>
	 * 根据当前用户Id查询所有运费模板; 要区分网点用户和VIP用户
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public String toPosttemp() {
		if (!StringUtils.isEmpty(url)) {
			// 当进入模板新增/修改的入口是 用户管理 的时候
			// 操作完毕需要动态跳回 用户管理界面
			return "dynamicUrl"; // 动态跳转
		}

		pagination = new Pagination(currentPage, super.pageNum);

		// User user = (User) session.get(Constants.SESSION_USER);
		user = super.readCookieUser();
		// 如果是网点财务, 根据pid取运费模板
		Integer userId = user.getId();
		// 网点 财务/财务&客服 只能查看模板
		/*if ("22".equals(user.getUserType()) || "23".equals(user.getUserType())) { 
			userId = user.getParentId();
		}*/

		Map resultMap = service.getPosttempByUserId(userId, pagination,
				user.getUserType());
		pagination.setTotalRecords(Integer.parseInt(resultMap.get(
				"totalRecords").toString()));
		postList = (List<Posttemp>) resultMap.get("list");

		return "toPotsttemp";
	} // toPosttemp

	/** 跳转到运费模板新增页面 */
	public String toPosttempAdd() {
		vipIds = null;
		// User cuser = (User) session.get(Constants.SESSION_USER);
		User cuser = super.readCookieUser();
		// 查数据库中最新数据. 因为需要地区数据，如果用户修改了自己的
		// 地区，缓存中不会更新。
		User dbUser = userService.getUserById(cuser.getId());
		fromAddr = dbUser.getAddressProvince();
		String numProv = dbUser.getField001();
		numProv = "".equals(numProv) ? "0" : numProv;
		int srcId = Integer.parseInt(numProv);
		spList = spService
				.getStandardPosttempListBySourceId(srcId, null, false);
		user = cuser;
		return "toPosttempAdd";
	}

	/** 运费模板 - 保存 */
	public String posttempSave() {
		// User cuser = (User) session.get(Constants.SESSION_USER);
		User cuser = super.readCookieUser();
		Integer createId = cuser.getId();
		if(!("2").equals(cuser.getUserType())){
			User siteU = userService.getUserById(cuser.getParentId());
			createId = siteU.getId();
		}
		posttemp.setCreateUser(createId);
		posttemp.setUpdateUser(cuser.getId());
		posttemp.setUpdateTime(DateUtil.toSeconds(new Date()));
		
		service.addPosttemp(posttemp, vipIds);
		// 发送消息
		String messageContent = "网点已为您完成运费模板设置，你可以尽情享用电子对账功能&nbsp;&nbsp;&nbsp; 请点击“<A  href='javascript:opendzdz(1);' target='main'><B>电子对账</B></A>”链接";
		String messageTheme = "运费模板已设置";
		String uservipid[] = vipIds.split(";");
		Integer[] reveiveIds = new Integer[uservipid.length];
		for (int i = 0; i < uservipid.length; i++) {
			if(uservipid[i]!=null && uservipid[i]!=""){
				User receiveUser = userService.getUserById(Integer.valueOf(uservipid[i]));
				messageService.sendSystemMessage(receiveUser, messageTheme, messageContent);
			}
		}
		return toPosttemp();
	}

	/** 跳转到运费模板编辑页面 */
	public String toPosttempEdit() {
		// User cuser = (User) session.get(Constants.SESSION_USER);
		User cuser = super.readCookieUser();
		Integer id = cuser.getId();
		String utype = cuser.getUserType();
		posttemp = service.toPosttempEdit(posttemp, id, utype);
		List<Postinfo> postinfoList = new ArrayList<Postinfo>();
		try {
            postinfoList = XmlUtil.xmlDecoder2List(posttemp.getPostinfo());
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		for(Postinfo pi : postinfoList){
		    if(pi.getFirstWeight() == 0){
		        pi.setFirstWeight(posttemp.getFirstWeight());
		    }
		}
		posttemp.setPostinfoList(postinfoList);
		user = cuser;
		return "toPosttempEdit";
	}

	/** 编辑-保存 */
	public String posttempEdit() {
		// User cuser = (User) session.get(Constants.SESSION_USER);
		User cuser = super.readCookieUser();
		posttemp.setUpdateUser(cuser.getId());
		posttemp.setUpdateTime(DateUtil.toSeconds(new Date()));

		service.editPosttemp(cuser,posttemp);
		String messageContent = "网点已为您完成运费模板设置，你可以尽情享用电子对账功能&nbsp;&nbsp;&nbsp; 请点击“<A  href='javascript:opendzdz(1);' target='main'><B>电子对账</B></A>”链接";
		String messageTheme = "运费模板已设置";
		String uservipid[] = posttemp.getVipIds().split(";");
		Integer[] reveiveIds = new Integer[uservipid.length];
		for (int i = 0; i < uservipid.length; i++) {
			if(uservipid[i]!=null && uservipid[i]!=""){
				User receiveUser = userService.getUserById(Integer.valueOf(uservipid[i]));
				messageService.sendSystemMessage(receiveUser, messageTheme, messageContent);
			}
		}
		return toPosttemp();
	}

	/** 跳转到运费模板查看页面 */
	public String toPosttempView() {
		// User cuser = (User) session.get(Constants.SESSION_USER);
		User cuser = super.readCookieUser();
		Integer id = cuser.getId();
		String utype = cuser.getUserType();
		posttemp = service.toPosttempEdit(posttemp, id, utype);
		user = cuser;
		return "toPosttempView";
	}

	/**
	 * 卖家在左边菜单点击‘运费模板’调用该方法<br>
	 * 根据卖家的usercode去查直客id，然后查运费模板
	 * 
	 * @return
	 */
	public String toPosttempView2() {
		// User currentUser = (User) session.get(Constants.SESSION_USER);
		User currentUser = super.readCookieUser();
		posttemp = service.toPosttempView2(currentUser.getUserCode());
		user = currentUser;
		return "toPosttempView";
	}

	/** 删除 */
	public String toPosttempDel() {
		service.delPosttemp(posttemp);
		return "json";
	}

	public String querySPByProv() {
		// User cuser = (User) session.get(Constants.SESSION_USER);
		User cuser = super.readCookieUser();
		String numProv = null;
		String userType = cuser.getUserType();
		// vip/大商家
		if ("1".equals(userType) || "11".equals(userType)
				|| "12".equals(userType) || "13".equals(userType)) {
			User tSite = userService.getSiteByVipId(cuser.getId());
			if (tSite == null) {
				logger.error("用户对象为空. 无法根据卖家Id查到对应的网点用户. 参数信息[id:"
						+ cuser.getId() + "]");
				return "json";
			}
			numProv = tSite.getField001();
		}

		if (StringUtils.isEmpty(numProv)) {
			logger.info("没有找到用户所属网点的省份的编码");
			numProv = cuser.getField001(); // 网点的省份Id为空
			if (StringUtils.isEmpty(numProv)) {
				logger.error("没有找到用户所属省份的编码");
				return "json";
			}
		}

		standardPosttemp = spService.getStandardPosttempByProv(
				Integer.parseInt(numProv), destId);
		return "json";
	}

	public String queryPostinfoByProv() {
		User cuser = super.readCookieUser();
		postinfo = service.queryPostinfoByProv(cuser, destId);
		if (postinfo == null) {
			return querySPByProv(); // 取官方价
		} else {
			standardPosttemp = new StandardPosttemp();
			standardPosttemp.setStandardPrice(postinfo.getFwRealPirce() + ""); // 首重价
			standardPosttemp.setContinuationPrice(postinfo.getOwRealPirce()
					+ "");// 超重价
		}
		return "json";
	}

	/**
	 * 我的客户/运费模板管理<br>
	 * 如果用户有运费模板，就跳转到编辑的页面；否则跳转新增的页面
	 * 
	 * @return
	 */
	public String viewPTByVip() {
		posttemp = service.viewPTByVip(vipIds);
		if (posttemp != null) {
			if (posttemp.getId() != 0) {
				return "toPosttempEdit";
			}
		}

		// User user = userService.getUserById(Integer.parseInt(vipIds) );
		UserThread userThread = userThreadService.getUserById(Integer
				.parseInt(vipIds));
		posttemp = posttemp == null ? new Posttemp() : posttemp;
		posttemp.setVipIds(vipIds);
		posttemp.setVipText(userThread.getUserName() + "("
				+ userThread.getUserCode() + ")");
		// 新增
		return toPosttempAdd();
	}

	// === getter && setter ===
	public Posttemp getPosttemp() {
		return posttemp;
	}

	public void setPosttemp(Posttemp posttemp) {
		this.posttemp = posttemp;
	}

	public List<Posttemp> getPostList() {
		return postList;
	}

	public String getVipIds() {
		return vipIds;
	}

	public void setVipIds(String vipIds) {
		this.vipIds = vipIds;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination<Posttemp> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<Posttemp> pagination) {
		this.pagination = pagination;
	}

	public String getUserType() {
		return userType;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public List<StandardPosttemp> getSpList() {
		return spList;
	}

	public int getDestId() {
		return destId;
	}

	public void setDestId(int destId) {
		this.destId = destId;
	}

	public StandardPosttemp getStandardPosttemp() {
		return standardPosttemp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Postinfo getPostinfo() {
		return postinfo;
	}

	public void setPostinfo(Postinfo postinfo) {
		this.postinfo = postinfo;
	}

	public String getCalclateType() {
		return calclateType;
	}

	public void setCalclateType(String calclateType) {
		this.calclateType = calclateType;
	}
}
