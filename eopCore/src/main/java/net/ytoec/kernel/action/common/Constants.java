package net.ytoec.kernel.action.common;

/**
 * 定义系统常量
 * 
 * @author Wangyong
 * @2011-8-5 net.ytoec.kernel.action.common
 */
public class Constants {

	public final static String SESSION_USER = "user"; // 保存当前登录人员的User对象

	public final static String LASTLOGINTIME = "lastLoginTime"; // 保存用户上次登录时间

	public final static String TOP_APPKEY = "12440784"; // 第三方应用公钥

	public final static String TOP_SECRET = "90a2675617899db56ad63ded27759266"; // 第三方应用密钥

	public final static String OFFICALURL = "http://gw.api.taobao.com/router/rest";// 淘宝TOP正式环境

	public final static String LOGOFF_URL = "http://container.api.taobao.com/container/logoff";// 淘宝TOP退出登录地址

	public static final String SIGN_METHOD_MD5 = "md5";// MD5签名方式

	public static final String FORMAT_JSON = "json";// TOP JSON 应格式

	public static final String FORMAT_XML = "xml";// TOP XML 应格式

	public static final String TAOBAOUSERMETHOD = "taobao.user.get";// 获取单个淘宝用户信息

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
	public static final String TAOBAO_USER_ENCRYPTEDID = "taobao.logistics.encryptedid.get";

	public static final String FIELDS = "user_id,uid,nick,sex,buyer_credit,seller_credit,location,created,last_visit,birthday,type,status,alipay_no,alipay_account,email,consumer_protection,alipay_bind,has_shop";// 获取用户信息字符串

	/**
	 * 每页显示数据条数
	 */
	public static final Integer PAGE_NUM = 10;

	public static final String BRANCH_SOLR_URL = "http://127.0.0.1:8083/solr/branchcore/";

	public static final String SINGLEMAIL = "(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}";

	public static final String MQ_MSG_SPLIT = "@#$";// MQ 消息的分隔符

	public static final String DATE_FORMAT_yyyyMMdd = "yyyy-MM-dd";//

	public static final String ORDER_CREATE = "ORDERCREATE";

	public static final String ORDER_UPDATE = "UPDATE";

	public static final String COD_SUCCESS = "CODSUCCESS";

	public static final String COD_FAILED = "CODFAILED";
	
	public static final String JINGANG_UPDATE = "JINGANG";
	
	// 金刚更新SOLR
	public static final String JGCOMMAND = "JGCOMMAND";
	
	// 创建订单SOLR
	public static final String SOLR_ADD_ORDER = "SOLRADDORDER";
	
	// SOLR回退标志
	public static final String SOLR_ADD_ORDER_BACK = "SOLRADDORDERBACK";
}
