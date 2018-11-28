package net.ytoec.kernel.common;

/**
 * @作者：罗典
 * @时间：2013-08-30
 * @描述：一般常量设置
 * */
public class Constant {
	public static final String XML_TITLE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
	// 面单拉取接口日志类型
	public static final String LOG_TYPE_SYNWAYBILL = "synWaybill";
	// 接收面单接口日志类型
	public static final String LOG_TYPE_RECEIVEWAYBILL = "receiveWaybill";
	// 电子回传单上传日志类型
	public static final String LOG_TYPE_ORDERSERVLET = "vipOrderServlet";
	/** 面单拉取接口使用常量 */
	public static final String LOGISTICS_INTERFACE = "logistics_interface";
	public static final String DATA_DIGEST = "data_digest";
	public static final String CLIENT_ID = "clientId";
	public static final String RESPONSE_SUCCESS = "Success";
	public static final String ADMIN_QQ = "2294882345";// 管理员qq
	public static final int MAX_SIZE = 4000;// 每次给商家下发面单的最大数量
	public static final String CHARSET_UTF8 = "UTF-8";
	// XML格式
	public static final String CONTENT_TYPE_XML = "application/xml";
	// JSON格式
	public static final String CONTENT_TYPE_JSON = "application/json";
	// TEXT格式
	public static final String CONTENT_TYPE_TEXT = "text/html";

	// 序列状态处理中
	public static final int SEQ_STATE_PROCESS = 1;
	// 序列状态失败
	public static final int SEQ_STATE_FAIL = 0;
	// 序列状态成功
	public static final int SEQ_STATE_SUCCESS = 2;

	// 面单状态未使用
	public static final int MAIL_STATE_UNUSERD = 0;
	// 面单状态请求中
	public static final int MAIL_STATE_PROCESS = 2;
	// 面单状态已使用
	public static final int MAIL_STATE_USED = 1;

	// 防止恶意请求的处理时间(分)
	public static final int INTERVAL_TIME = 30;
	// 限制接口处理超时的时间(毫秒)
	public static final int TIMEOUT_TIME = 120000;

	// 面单保存部分异常
	public static final String CODE_PART_EXCPETION = "YTO_01";
	// 面单保存未知异常
	public static final String CODE_UNKOWN_EXCEPTION = "YTO_02";
	// 面单号重复
	public static final String CODE_REPEATED_EXCPETION = "YTO_03";
	// 接口处理超时提示信息
	public static final String MSG_TIMEOUT_WRONG = "sorry,we process it timeout,please try again later ";
	// 控制并发请求的提示信息
	public static final String MSG_FAST_REQUEST = "your request is too fast,please wait for a moment ";
	// 防止恶意请求的提示信息
	public static final String MSG_MORE_REQUEST = "sorry,you have a processing request,"
			+ "please wait for 30 minutes or send the response as soon as possible";
	// 数字签名校验失败
	public static final String MSG_DATADIGEST_FAIL = "dataDigest validate fail,please check it ";
	// 反馈状态时，序列号不正确
	public static final String MSG_SEQ_WRONG = "sorry , we haven't find the sequence,please check it or contact us ";
	// 反馈状态时，面单足够
	public static final String MSG_ENOUGH_WORNG = "your surfaceBill is enough, so i don't give you anymore, you can try agin later";
	// 未知系统异常
	public static final String MSG_UNKONWN_EXCEPTION = "unkonwn system error ,please contact us first";
	// xml格式数据转换失败
	public static final String MSG_CONVERTION_FAIL = "XML format data conversion failure,please check your data or contact us ";
	// 面单不足
	public static final String MSG_NOTENOUGH_MAIL = "this clientId have not enough mailBill";
	// 上传订单不足
	public static final String MSG_NOTENOUGH_ORDER = "this clientId have not enough upload Order";
	// 面单号重复
	public static final String MSG_REPEATED_MAILNO = "repeated mailNo";
	// 部分面单数据异常
	public static final String MSG_PART_EXCPETION = "part of this mailNos appeared excpetion ";

	/***** 易迅订单上传，回传信息  *****/
	//请求参数为空
	public static final String MSG_PARAM_EMPTY = "请求参数为空";
	//客户编码错误
	public static final String MSG_CUSCODE_ERROR = "客户编码错误";
	//xml中含有特殊字符
	public static final String MSG_XML_CHAR_ERROR = "xml中含有特殊字符";
	//物流订单号为空
	public static final String MSG_LOGIS_EMPTY = "物流订单号为空";
	//物流订单号长度过长
	public static final String MSG_LOGIS_LEN_MORE = "物流订单号长度过长";
	//收（发）件人信息中存在超长信息
	public static final String MSG_TRADER_LEN_MORE = "收件人或发件人信息中存在超长信息";
	//货品名称或者备注信息超长
	public static final String MSG_PRODUCT_LEN_MORE = "货品名称或者货品备注信息超长";
	//面单号所在节点为空节点
	public static final String MSG_BILL_NODE_EMPTY = "面单号所在节点为空节点";
	//面单号为空
	public static final String MSG_BILL_EMPTY = "面单号为空";
	//面单号长度过长
	public static final String MSG_BILL_LEN_MORE = "面单号长度过长";
	//面单号格式错误
	public static final String MSG_BILL_FORMAT_ERROR = "面单号格式错误";
	//订单保存成功
	public static final String MSG_ORDER_SAVE_SUCCESS = "订单保存成功";
	//订单重复
	public static final String MSG_ORDER_SAVE_DUPLICATE = "订单重复";
	//数据库插入订单表错误
	public static final String MSG_ORDER_SAVE_DBERROR = "数据库插入订单表错误";
	//数据库插入发件人信息错误
	public static final String MSG_SENDER_SAVE_DBERROR = "数据库插入发件人信息错误";
	//数据库插入收件人信息错误
	public static final String MSG_RECEIVE_SAVE_DBERROR = "数据库插入收件人信息错误";
	//数据库插入商品信息错误
	public static final String MSG_PRODUCT_SAVE_DBERROR = "数据库插入商品信息错误";
	//保存搜索引擎错误
	public static final String MSG_SEARCH_SAVE_DBERROR = "数据保存搜索引擎错误";
	//存入JGWaybill表错误
	public static final String MSG_JGWAYBILL_SAVE_DBERROR = "数据存入上传金刚表错误";
	//订单保存成功标志
	public static final int FLAG_ORDER_SAVE_SUCCESS = 0;
	//订单重复标志
	public static final int FLAG_ORDER_SAVE_DUPLICATE = 1;
	//数据库插入订单表错误标志
	public static final int FLAG_ORDER_SAVE_DBERROR = 2;
	//数据库插入发件人信息错误标志
	public static final int FLAG_SENDER_SAVE_DBERROR = 3;
	//数据库插入收件人信息错误标志
	public static final int FLAG_RECEIVE_SAVE_DBERROR = 4;
	//数据库插入商品信息错误标志
	public static final int FLAG_PRODUCT_SAVE_DBERROR = 5;
	//保存搜索引擎错误标志
	public static final int FLAG_SEARCH_SAVE_DBERROR = 6;
	//存入JGWaybill表错误标志
	public static final int FLAG_JGWAYBILL_SAVE_DBERROR = 7;
	/********************************/


}
