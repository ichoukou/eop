package net.ytoec.kernel.common;

public class Constants {

	public final static String LASTLOGINTIME = "lastLoginTime"; // 保存用户上次登录时间
																// 应格式

	/**
	 * 淘宝接口 # 获取加密的用户ID<br>
	 * 
	 * @author ChenRen/2011-10-17
	 * @param 看淘宝API
	 *            : {@link http
	 *            ://api.taobao.com/apidoc/api.htm?path=categoryId:7
	 *            -apiId:10923#}
	 * @return 返回加密后的 customerId
	 */

	public static final String FIELDS = "user_id,uid,nick,sex,buyer_credit,seller_credit,location,created,last_visit,birthday,type,status,alipay_no,alipay_account,email,consumer_protection,alipay_bind,has_shop"; // 获取用户信息字符串

	// 对外接口类常量
	public static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";
	public static final String DATA_DIGEST_PARAM = "data_digest";
	public static final String CLIENT_ID_PARAM = "clientId";
	public static final String GET_METHOD_RESPONSE = "Success";
	public static final String TYPE = "type";
	// 物流平台标识
	public static String JGtxLogisticID = "0";
	// 物流平台标识
	public static String VIPtxLogisticID = "0";

	// 首页最新动态显示记录数目
	public static final Integer NEW_COLUMN_NUM = 6;

	/**
	 * 电子对账计算运费中使用到的运费信息键的定义
	 * 
	 * @author wangyong
	 */
	public static final String POSTINFO = "POSTINFO";
	/**
	 * 电子对账计算运费中使用到的模板键的定义
	 * 
	 * @author wangyong
	 */
	public static final String POSTTEMP = "POSTTEMP";
	/**
	 * 电子对账计算运费中使用到的始发地省份编码的定义
	 * 
	 * @author wangyong
	 */
	public static final String SRCPROVCODE = "SRCPROVCODE";

	/**
	 * 问题件中自动通知客户的问题件类型
	 * 按顺序分别对应：面单填写不规范、超区、收件客户拒收、地址不详电话联系不上、节假日客户信息、收件客户已离职、收件客户要求更改地址
	 * 、地址不详且电话为传真或无人接听、地址不详电话与收件客户本人不符、地址不详无收件人的电话、违禁品、到付费、代收款、签收失败、其他
	 * 
	 * @author wangyong
	 */
	public static final String[] AUTO_NOTIFIER = { "PR002", "PR20", "PR210",
			"PR211", "R70", "PR212", "PR213", "PR214", "PR215", "PR216",
			"PR250", "PR50", "PR60", "PR140", "PR270" };

	/**
	 * 金刚问题件中图片访问地址：需要传入参数
	 */
	// public static final String JINGANGPHOTO_ADDRESS =
	// "http://jingang.yto56.com.cn/pbl/downloadFile.action?fileId=";
	public static final String JINGANGPHOTO_ADDRESS = "http://jingang.yto56.com.cn";

	// 网点消息分类
	public static final String[] SITE = { "所有消息", "管理员", "系统消息", "其他消息" };

	// 卖家消息分类
	public static final String[] SELLER = { "所有消息", "管理员", "系统消息", "其他消息" };

	// 平台用户消息分类
	public static final String[] PLATFORM = { "发件箱", "分仓账号" };

	// 管理员消息分类
	public static final String[] ADMIN = { "所有消息", "管理员", "网点", "卖家" };

	/** 问题件导出格式 */
	public static final String EXPORTFORMATONE = "旺旺号, 运单号, 问题件上报时间, 问题件类型, 问题件描述, 买家姓名, 买家电话, 上报网点, 店铺名称";

	/** 问题件导出格式 */
	public static final String EXPORTFORMATTWO = "旺旺号, 运单号, 处理中类型, 问题件上报时间, 问题件类型, 问题件描述, 买家姓名, 买家电话, 上报网点, 店铺名称";

	public static final String ORDER_CREATE = "ORDERCREATE";

	public static final String ORDER_UPDATE = "UPDATE";

	public static final String COD_SUCCESS = "CODSUCCESS";

	public static final String COD_FAILED = "CODFAILED";

	public static final String SERVICE_APPLY = "SERVICEAPPLY";

	public static final String SERVICE_CANCEL = "SERVICECANCEL";

	// 淘宝访问金刚接口
	public static final String JIN_GANG_SERVICEAPPLY_ORDER = "JINGGANG.SERVICEAPPLY.ORDER";
	public static final String JIN_GANG_TAOBAO_ORDER = "JINGANG.ORDER";
	// 通用的访问金刚接口
	public static final String JIN_GANG_COMMON_ORDER = "JINGANG.COMMON.ORDER";

	// 在线下单
	public static final String ONLINE_TYPE = "online";
	
	// 淘宝更新订单
	public static final String UPDATE_TYPE = "update";

	// 自己联系物流
	public static final String OFFLINE_TYPE = "offline";

	// 金刚更新订单
	public static final String DATA_JINGANG = "JINGANG";
	
	// 线上订单优先级
	public static final int ONLINE_PRIORITY = 5;
	   
	// 线下订单 /订单更新/订单支付 优先级
	public static final int OFFLINE_AND_UPDATE_PRIORITY = 1;
	// 金刚更新SOLR
	public static final String JGCOMMAND = "JGCOMMAND";

	// 创建订单SOLR
	public static final String SOLR_ADD_ORDER = "SOLRADDORDER";

	// SOLR回退标志
	public static final String SOLR_ADD_ORDER_BACK = "SOLRADDORDERBACK";

}
