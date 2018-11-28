package net.ytoec.kernel.action.sms;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.SMSBuyers;
import net.ytoec.kernel.dataobject.SMSBuyersGrade;
import net.ytoec.kernel.dataobject.SMSBuyersSearch;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.SMSBuyersGradeService;
import net.ytoec.kernel.service.SMSBuyersSearchService;
import net.ytoec.kernel.service.SMSBuyersService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

/**
 * 会员管理Action
 * 
 * @author shitianzeng 2012-07-06
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SmsBuyersAction extends AbstractActionSupport {

	private static final long serialVersionUID = -6396325181757062681L;

	private Logger logger = Logger.getLogger(SmsBuyersAction.class);

	@Inject
	private SMSBuyersService smsBuyersService;

	@Inject
	private UserService<User> userService;
    
    @Inject
    private SMSBuyersSearchService smsBuyersSearchService;
    
    @Inject
    private SMSBuyersGradeService smsBuyersGradeService;
    
    @Inject
	private SMSPortService smsPortService; 
	
	//定义模版类型
//	private static final String TAOBAO = "淘宝模板1.0";
//	private static final String PAIPAI = "拍拍模板1.0";
//	private static final String ALIBABA = "阿里巴巴模板1.0";

//	private static final String DEFAULT = "默认模板1.0";

	// 验证CSV文件单元格
	private static final String SPECIAL_CHAR_A = "[^\",\\n 　]";
	private static final String SPECIAL_CHAR_B = "[^\",\\n]";

	// 邮编
	private static final String REGEX_POSTCODE = " /^[1-9][0-9]{5}$/";

	// 下载导入模版部分
	private String[] downLoadPaths; // 模版下载路径
	private OutputStream res; // 文件输出流
	private ZipOutputStream zos; // 文件输出打包流

	// 文件上传部分
	private File upload;// 上传的文件
	private String uploadContentType; // 文件的内容类型
	private String uploadFileName; // 上传文件名
	private BufferedReader bf;

	// 页面传入的参数
	private String orderIds;// 勾选的临时订单
	private String orderId;// 删除的临时订单编号

	private String tempkey; // 数据上传时的标示符，取用户的ID

	// 错误消息提醒
	private String uploadErrType;
	private List<String> uploadErrlist;

	private String queryMessage;

	private User user = null;

	private Integer currentUserId;

	// 导入时候的参数
	private String note; // 删除/保留原来会员信息
	private String shopName; // 所属店铺
	private String shopKey; //所属店铺对应的用户id
	
	private String shopNames;

	private HashSet<String> set;

	private int indexCount;
	
	
	//搜索器对象
	private SMSBuyersSearch smsBuyersSearch;
	
	//搜索器对象集合
	private List<SMSBuyersSearch> smsBuyersSearchs;	
	//搜索器名称
	private String searName;
	//上次交易时间
	private String tradeTime;	
	//上次活动时间
	private String theLastMarketTime;
	//省
	private String province;
	//市
	private String city;
	//区
	private String area;	
	//会员等级
	private String memLevel;	
	//小交易量
	private String tradeAmountA;
	//大交易量
	private String tradeAmountB;
	//小交易金额
	private String tradeCountA;
	//大交易金额
	private String tradeCountB;	
	//会员号、手机号、联系人
	private String member;
	//要保存的搜索器名称
	private String saveSearInput;
	//搜索器ID
	private Integer smsBuyersSearchId;
	//排序字段
	private String orderByCol;
	//会员Id，删除时使用
	private String memberId;
	/**
	 * 设置会员等级
	 */
	private SMSBuyersGrade grade;
	//会员对象，保存、编辑时使用
	private SMSBuyers buyers;
	//店铺信息
	private List<User> bindUserList;
	//ajax请求返回给页面的信息
	private String jsonStr;
	//旺旺
	private String wawa;
	//会员信息集合
	private List<SMSBuyers> smsBuyers;
	//分页参数
	private Pagination<SMSBuyers> pagination;
	private Integer currentPage = 1;//当前第一页
	
	private Integer id; 
	
	/**
	 * 当前用户所对应的会员总数
	 */
	private String totalBuyers = "0";

	/**
	 * 客户(会员)管理入口
	 * 
	 * @return
	 */
	public String toIndex() {
		
		//判断这个服务是否已经开通过
		boolean flag = smsPortService.supplyStatusOpenUp(getMainUserId());
		if(!flag){
			return "smsWelcome";
		}
		
		user = super.readCookieUser();
		// 卖家所有关联店铺
		List<User> bindedUserList = new ArrayList<User>();
		if (User.BUYER.equals(user.getUserAuthority())) {
			for (String cusId : Resource.getBindedCustomerIdList(user)) {
				bindedUserList.add(userService.getUserByCustomerId(cusId));
			}
		} else if (User.LARGE_SELLER.equals(user.getUserAuthority())) {
			bindedUserList = userService.pingTaiSelect(user, 0);
		}
		
		// 当前用户所关联的店铺
		Map<Integer,String> sNames = new HashMap<Integer,String>();
		
		for (User user : bindedUserList) {
			sNames.put(user.getId(), user.getShopName());
//			shopName.append(user.getShopName() + ",");
		}
		
		StringBuffer shopname = new StringBuffer();
		StringBuffer shopkey = new StringBuffer();
		Set<Entry<Integer, String>> set = sNames.entrySet();
        for (Iterator<Map.Entry<Integer, String>> it = set.iterator(); it.hasNext();) {
            Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) it.next();
            shopkey.append(entry.getKey() + ",");
            shopname.append(entry.getValue() + ",");
        }
		
        shopKey = shopkey.substring(0, shopkey.length() - 1);
        shopNames = shopname.substring(0, shopname.length() - 1);
		
		/**
		 * 获取当前用户对应的会员总数
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIds", Resource.getBindedUserIdList(user));
		params.put("userGrade", "4");
		totalBuyers = smsBuyersService.getCountByUserGrade(params) + "";
		return "toIndex";
	}

	/**
	 * 会员导入指南文件下载
	 * 
	 * @return
	 */
	public String downLoadGuide() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			File file = new File(FileUtil.getClassRoot()
					+ "/template/会员导入指南.doc");
			if (!file.exists()) {
				logger.error("文件不存在!" + file);
			}
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			byte[] buf = new byte[1024];
			int len = 0;
			String fname = file.getName();

			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				fname = URLEncoder.encode(fname, "UTF-8");// IE浏览器
			} else {
				fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
			}
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/msword");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ fname);
			OutputStream out = response.getOutputStream();
			while ((len = bis.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			bis.close();
			out.close();
		} catch (Exception e) {
			logger.error(e);
		}
		return "toIndex";
	}

	/**
	 * 跳转到会员导入层
	 * 
	 * @return
	 */
//	public String toBuyersImoprt() {
//		user = super.readCookieUser();
//		// 卖家所有关联店铺
//		List<User> bindedUserList = new ArrayList<User>();
//		if (User.BUYER.equals(user.getUserAuthority())) {
//			for (String cusId : Resource.getBindedCustomerIdList(user)) {
//				bindedUserList.add(userService.getUserByCustomerId(cusId));
//			}
//		} else if (User.LARGE_SELLER.equals(user.getUserAuthority())) {
//			bindedUserList = userService.pingTaiSelect(user, 0);
//		}
//		return "toBuyersImoprt";
//	}

	/**
	 * 关闭输入输出流，清理垃圾
	 */
	public void afterProcess() throws IOException {
		zos.close();
		res.close();
	}

	/**
	 * 文件下载前的准备，设置文件头等等
	 * 
	 * @throws Exception
	 */
	public void preProcess() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		res = response.getOutputStream();
		String fname = "易通会员导入模板.zip";
		if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
			fname = URLEncoder.encode(fname, "UTF-8");// IE浏览器
		} else {
			fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
		}
		response.reset();// 清空输出流
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fname);// 设定输出文件头
		response.setContentType("application/zip");
		zos = new ZipOutputStream(res);
	}

	/**
	 * 根据文件的地址来压缩文件
	 * 
	 * @param downLoadPaths
	 *            文件地址数组
	 * @throws IOException
	 */
	public void writeZip(String[] downLoadPaths) throws IOException {
		byte[] buf = new byte[4096];
		int len;
		zos.setEncoding("gb2312");
		for (String filename : downLoadPaths) {
			String classpath = FileUtil.getClassRoot();
			logger.error("classpath:" + classpath + filename);
			File file = new File(classpath + filename);
			ZipEntry ze = new ZipEntry(file.getName());// apache的ant.jar的ZipEntry
			zos.putNextEntry(ze);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			while ((len = bis.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			bis.close();
		}
	}

	/**
	 * 文件下载入口
	 * 
	 * @return
	 * @throws Exception
	 */
	public String downLoadZip() throws Exception {
//		if (downLoadPaths.length != 0) {
//			logger.debug(downLoadPaths.length);
//			preProcess();// 进行预处理
//		} else {
//			return "nofile";// 没有文件可以下载，返回
//		}
//		writeZip(downLoadPaths);// 处理
//		afterProcess();// 后处理关闭流
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			File file = new File(FileUtil.getClassRoot()
					+ "/template/会员导入表.csv");
			if (!file.exists()) {
				logger.error("文件不存在!" + file);
			}
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			byte[] buf = new byte[1024];
			int len = 0;
			String fname = file.getName();

			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				fname = URLEncoder.encode(fname, "UTF-8");// IE浏览器
			} else {
				fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
			}
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/msword");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ fname);
			OutputStream out = response.getOutputStream();
			while ((len = bis.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			bis.close();
			out.close();
		} catch (Exception e) {
			logger.error(e);
		}
		
		return "toIndex";
	}

	/**
	 * 会员导入的入口
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upload() throws Exception {
		// user = super.readCookieUser();
		// String userType = user.getUserType();
		String a = note;
		List<List<String>> list = readCsvFile(upload, "gb2312");
		logger.error("上传的文件长度为：" + (upload.length() - 1));
		for (Iterator<List<String>> it = list.iterator(); it.hasNext();) {
			List<String> lsList = it.next();
			if (lsList.isEmpty()) {
				it.remove();
			}
		}
		logger.error("列表长度：" + (list.size() - 1));
		List<SMSBuyers> ls = new ArrayList<SMSBuyers>();
		if (list.size() <= 1) {
			uploadErrType = "无会员信息，一张空表!";
			return toIndex();
		} else if (list.size() > 1001) {
			uploadErrType = "上传数据超过了1000行，请减少数据量!";
			return toIndex();
		}

		// 根据模版将数据转换成对象
		ls = defaultTemp(list);

		if (null != ls && ls.size() > 0) {

			try {
				insertBuyers(ls); // 上传会员信息到数据库表中
				logger.error(uploadErrType);
				return toIndex();
			} catch (Exception e) {
				logger.error(e);
				uploadErrType = "上传失败，请按照模板填写表格，检查后重新上传！";
				return toIndex();
			}
		} else {
			uploadErrType = "上传失败，请按照模板填写表格，检查后重新上传！";
			return toIndex();
		}
	}

	/**
	 * 上传会员信息到数据库表中
	 * 
	 * @return
	 */
	// public String insertBuyers(){
	//
	//
	// return "";
	// }

	public void insertBuyers(List<SMSBuyers> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		User currentUser = super.readCookieUser();
		String userType = currentUser.getUserType();
		int userId = 0;
		int uId = 0;
		int uuId = 0;
		int index = 0;
		User u = userService.getUserById(id);
		List<Integer> listId = new ArrayList();
		if ("1".equals(userType) || "4".equals(userType)) {
			userId = currentUser.getId(); // 主帐号ID
			if (u.getParentId() != null && u.getParentId() != 0) {
				uuId = u.getParentId(); // 关联店铺主帐号ID
			} else {
				uuId = u.getId();
			}
			listId.add(uuId);
		} else if ("11".equals(userType) || "12".equals(userType)
				|| "13".equals(userType) || "41".equals(userType)) {
			// 子帐号登录，传入主帐号ID
			userId = currentUser.getParentId();
			if (u.getParentId() != null && u.getParentId() != 0) {
				uuId = u.getParentId(); // 关联店铺主帐号ID
			} else {
				uuId = u.getId();
			}
			listId.add(uuId);
			uId = u.getId(); // 子帐号ID
		}

		try {
			Map<String, Object> map = new HashMap();
			String userIds = String.valueOf(userId);
			map.put(userIds, listId);
			List<SMSBuyers> buyerList = smsBuyersService
					.getSMSBuyersByPamams(map);
			for (SMSBuyers smsBuyers : list) {
				if (isHave(smsBuyers.getBuyerAccount(), buyerList)) {
					if ("true".equals(note)) {
						SMSBuyers smsb = smsBuyersService
								.getSMSBuyersByBuyerAccount(smsBuyers
										.getBuyerAccount(), smsBuyers.getUserId());
						smsBuyersService.delSMSBuyersById(smsb.getId());

					} else {
						continue;
					}
				}
				SMSBuyers sBuyers = new SMSBuyers();
				String uType = u.getUserType();
				if ("1".equals(uType) || "4".equals(uType)) {
					userId = u.getId();
					sBuyers.setUserId(userId);
					sBuyers.setCreateUserId(userId);
				} else if ("11".equals(uType) || "12".equals(uType)
						|| "13".equals(uType) || "41".equals(uType)) {
					userId = u.getParentId();
					uId = u.getId();
					sBuyers.setUserId(userId);
					sBuyers.setCreateUserId(uId);
				}
				sBuyers.setBuyerAccount(smsBuyers.getBuyerAccount());
				sBuyers.setReceiverName(smsBuyers.getReceiverName());
				sBuyers.setReceiverMobile(smsBuyers.getReceiverMobile());
				sBuyers.setReceiverPostcode(smsBuyers.getReceiverPostcode());
				sBuyers.setReceiverProvince(smsBuyers.getReceiverProvince());
				sBuyers.setReceiverCity(smsBuyers.getReceiverCity());
				sBuyers.setReceiverDistrict(smsBuyers.getReceiverDistrict());
				sBuyers.setReceiverAddress(smsBuyers.getReceiverAddress());
				sBuyers.setSourceStatus(smsBuyers.getSourceStatus());
				sBuyers.setMarketingSendCount(smsBuyers.getMarketingSendCount());
				sBuyers.setTotalTradeCount(smsBuyers.getTotalTradeCount());
				sBuyers.setTotalTradeAmount(smsBuyers.getTotalTradeAmount());
				sBuyers.setTheLastTradeTime(smsBuyers.getTheLastTradeTime());
				sBuyers.setTheLastMarketTime(smsBuyers.getTheLastMarketTime());
				sBuyers.setUpdateTime(smsBuyers.getUpdateTime());
				sBuyers.setUpdateUserId(smsBuyers.getUpdateUserId());
				sBuyers.setCreateTime(smsBuyers.getCreateTime());
				sBuyers.setRemark(smsBuyers.getRemark());

				smsBuyersService.addSMSBuyers(sBuyers);
				index = index + 1;
			}
			indexCount = index;
			uploadErrType = "上传成功，共上传" + indexCount + "条记录!";
		} catch (Exception e) {
			logger.error(e);
			uploadErrType = "导入失败，请检查!";
		}

	}

	/**
	 * 判断字符串在列表中是否已经存在
	 * 
	 */
	public boolean isHave(String buyerAccount, List<SMSBuyers> list) {
		set = new HashSet<String>();
		for (SMSBuyers s : list) {
			set.add(s.getBuyerAccount());
		}
		if (set.contains(buyerAccount)) {
			return true;
		}
		return false;
	}

	/**
	 * 确认特殊字符串是否存在
	 */
	private static boolean isExisted(String argChar, String argStr) {
		boolean blnReturnValue = false;
		if ((argStr.indexOf(argChar) >= 0)
				&& (argStr.indexOf(argChar) <= argStr.length())) {
			blnReturnValue = true;
		}
		return blnReturnValue;
	}

	/**
	 * 正则表达式。
	 * 
	 * @return 匹配csv文件里最小单位的正则表达式。
	 */
	private static String getRegExp() {
		StringBuffer strRegExps = new StringBuffer();
		strRegExps.append("\"((");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*[,\\n 　])*(");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"{2})*)*");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"[ 　]*,[ 　]*");
		strRegExps.append("|");
		strRegExps.append(SPECIAL_CHAR_B);
		strRegExps.append("*[ 　]*,[ 　]*");
		strRegExps.append("|\"((");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*[,\\n 　])*(");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"{2})*)*");
		strRegExps.append(SPECIAL_CHAR_A);
		strRegExps.append("*\"[ 　]*");
		strRegExps.append("|");
		strRegExps.append(SPECIAL_CHAR_B);
		strRegExps.append("*[ 　]*");
		return strRegExps.toString();
	}

	/**
	 * CSV文件解析成List
	 * 
	 * @param file
	 * @param encoding
	 * @return CSV文件数据(List<String[]>)
	 */
	public static List<List<String>> readCsvFile(File file, String encoding) {
		List<List<String>> list = new ArrayList<List<String>>();
		FileInputStream input = null;
		InputStreamReader iReader = null;
		BufferedReader bReader = null;
		String regExp = getRegExp();
		try {
			input = new FileInputStream(file);
			if (encoding == null) {
				iReader = new InputStreamReader(input);
			} else {
				iReader = new InputStreamReader(input, encoding);
			}
			bReader = new BufferedReader(iReader);
			String strLine = " ";
			String str = " ";
			while ((strLine = bReader.readLine()) != null) {
				Pattern pattern = Pattern.compile(regExp);
				Matcher matcher = pattern.matcher(strLine);
				if (strLine.startsWith("#")) { // 去掉带#的注释
					continue;
				}
				List<String> listTemp = new ArrayList<String>();
				while (matcher.find()) { // 读取买个单元格
					str = matcher.group().trim();
					if (str.endsWith(",")) {
						str = str.substring(0, str.length() - 1).trim();
					}
					if (str.startsWith("\" ") && str.endsWith("\" ")) {
						str = str.substring(1, str.length() - 1);
						if (isExisted("\"\" ", str)) {
							str = str.replaceAll("\"\" ", "\" ");
						}
					}

					listTemp.add(str);

				}
				if (isNotNull(listTemp)) {
					list.add(listTemp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != iReader) {
				try {
					iReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != bReader) {
				try {
					bReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	/**
	 * 判断一个list中的元素是否全为空格 不是 true 是 false
	 * */
	public static boolean isNotNull(List<String> ls) {
		for (String string : ls) {
			if (string.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据模版将数据转换成对象
	 * 
	 * @param list
	 * @return
	 */
	public List<SMSBuyers> defaultTemp(List<List<String>> list) {
		uploadErrlist = new ArrayList<String>();
		user = super.readCookieUser();
		tempkey = user.getId().toString();
		StringBuffer sb = new StringBuffer();
		int index = 0; // 出错行计算器
		List<SMSBuyers> buyersList = new ArrayList<SMSBuyers>();

		try {
			for (int i = 1; i < list.size(); i++) {
				sb.setLength(0);
				String buyerAccount = list.get(i).get(0).trim();
				String receiverName = list.get(i).get(1).trim();
				String totalTradeCount = list.get(i).get(2).trim();
				String totalTradeAmount = list.get(i).get(3).trim();
				String theLastTradeTime = list.get(i).get(4).trim();
				String receiverProvince = list.get(i).get(5).trim();
				String receiverCity = list.get(i).get(6).trim();
				String receiverDistrict = list.get(i).get(7).trim();
				String receiverAddress = list.get(i).get(8).trim();
				String receiverPostcode = list.get(i).get(9).trim();
				String receiverMobile = list.get(i).get(10).trim();
				String remark = list.get(i).get(11).trim();

				SMSBuyers smsBuyers = new SMSBuyers();

				if (StringUtils.isBlank(buyerAccount)) {
					sb.append("A");
				} else {
					smsBuyers.setBuyerAccount(buyerAccount);
				}

				if (StringUtils.isBlank(receiverName)) {
					sb.append("B");
				} else {
					smsBuyers.setReceiverName(receiverName);
				}

				if (StringUtils.isNotBlank(totalTradeCount)) {
					smsBuyers.setTotalTradeCount(Integer
							.parseInt(totalTradeCount));
				}

				if (StringUtils.isNotBlank(totalTradeAmount)) {
					smsBuyers.setTotalTradeAmount(Integer
							.parseInt(totalTradeAmount));
				}

				if (StringUtils.isNotBlank(theLastTradeTime)) {
					// SimpleDateFormat sdf = new
					// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					// smsBuyers.setTheLastTradeTime(sdf.parse(theLastTradeTime));
					smsBuyers.setTheLastTradeTime(DateUtil.valueof(
							theLastTradeTime, "yyyy-MM-dd HH:mm:ss"));
				}

				if (StringUtils.isNotBlank(receiverProvince)) {
					smsBuyers.setReceiverProvince(receiverProvince);
				}

				if (StringUtils.isNotBlank(receiverCity)) {
					smsBuyers.setReceiverCity(receiverCity);
				}

				if (StringUtils.isNotBlank(receiverDistrict)) {
					smsBuyers.setReceiverDistrict(receiverDistrict);
				}

				if (StringUtils.isNotBlank(receiverAddress)) {
					smsBuyers.setReceiverAddress(receiverAddress);
				}

				if (StringUtils.isNotBlank(receiverPostcode)) {
					// if (!receiverPostcode.matches(REGEX_POSTCODE)) {
					// sb.append("J");
					// } else
					smsBuyers.setReceiverPostcode(receiverPostcode);
				}

				if (StringUtils.isBlank(receiverMobile)) {
					sb.append("K");
				} else {
					smsBuyers.setReceiverMobile(receiverMobile);
				}

				if (StringUtils.isNotBlank(remark)) {
					smsBuyers.setRemark(remark);
				}
				smsBuyers.setSourceStatus("导入");
				smsBuyers.setMarketingSendCount(0); // 营销活动发送量

				// smsBuyers.setUserId(Integer.parseInt(tempkey));
				smsBuyers.setUpdateTime(new Date());
				smsBuyers.setUpdateUserId(0);
				smsBuyers.setCreateTime(new Date());
				// smsBuyers.setCreateUserId(user.getId());
				logger.error(smsBuyers.toString());
				if (sb.length() > 0) {
					sb.append(":" + (i + 1));
					uploadErrlist.add(sb.toString());
					index = index + 1;
					if (index <= 10) {
						continue;
					} else {
						uploadErrlist = null;
						break;
					}
				} else {
					buyersList.add(smsBuyers);
				}
			}
			if (uploadErrlist.size() > 0) {
				// uploadErrArray = changeListToArray(uploadErrlist);
				for (String s : uploadErrlist) {
					logger.error(s);
				}
				logger.error(uploadErrlist.size());
				buyersList = null;
			}
		} catch (NumberFormatException e) {
			logger.error(e);
		}
		return buyersList;
	}
	/**
	 * 查询会员界面
	 * 
	 * @return
	 */
	public String searchBuyersList() {
		
		// 获取绑定店铺用户
		User currentUser = super.readCookieUser();
		bindUserList = new ArrayList<User>();
		List<String> bindString = Resource.getBindedCustomerIdList(currentUser);
		for(String cur : bindString){
			User u = userService.getUserByCustomerId(cur);
			if(u!=null){
				if(StringUtils.isNotEmpty(u.getShopName())) 
					bindUserList.add(u);
			}
		}
		//获取当前登录用户的所有搜索器，用于前台显示
		List<SMSBuyersSearch> Searchs=getBuyersSearchs();
		if (null!=Searchs && 0!=Searchs.size()) {
			smsBuyersSearchs=Searchs;
		}else{
			smsBuyersSearchs=new ArrayList<SMSBuyersSearch>();
		}
		//判断是否需要条件查询
		Map<String, Object> map=getSearchBuyersListParams();
		if (null!=map && 0!=map.size()) {
			//有查询条件
			pagination = new Pagination(currentPage, pageNum);
			pagination.setTotalRecords(smsBuyersService.getSMSBuyersByPamams(map).size());
			map.put("currentPage",pagination.getCurrentPage());
			map.put("startIndex", (pagination.getCurrentPage()-1)*pageNum);
			map.put("pageNum", pageNum);
			smsBuyers=smsBuyersService.getSMSBuyersByPamams(map);
		}
		return "list";
	}
	/**
	 * 获取当前登录用户的搜索器
	 * @return
	 */
	private List<SMSBuyersSearch> getBuyersSearchs(){
		//获取当前用户ID
		User currentUser = super.readCookieUser();
		List<SMSBuyersSearch> smsBuyersSearchs=smsBuyersSearchService.getSMSBuyersSearchByUserId(currentUser.getId());
		if (null!=smsBuyersSearchs && 0!=smsBuyersSearchs.size()) {
			//重新封装搜索器集合，只保存搜索器Id、名称
			List<SMSBuyersSearch> tempBuyersSearchs=new ArrayList<SMSBuyersSearch>();
			for (int i = 0; i < smsBuyersSearchs.size(); i++) {
				SMSBuyersSearch tempBuyersSearch=new SMSBuyersSearch();
				tempBuyersSearch.setId(smsBuyersSearchs.get(i).getId());//ID
				tempBuyersSearch.setSearchName(smsBuyersSearchs.get(i).getSearchName());//name
				tempBuyersSearchs.add(tempBuyersSearch);
			}
			return tempBuyersSearchs;
		}
		return null;
	}
	/**
	 * 保存、修改搜索器
	 * @return
	 * @throws IOException 
	 */
	public String saveSMSBuyersSearchParams() throws IOException{
		jsonStr=saveSmsBuyerSearchs();//ID,Name,add/edit
		return "jsondata";
	}
	//保存、修改搜索器辅助方法
	private String saveSmsBuyerSearchs(){
		//获取cookie中当前登录用的信息
		User currentUser = super.readCookieUser();
		//拼接参数数组
		String[] paramsStr=new String[]{tradeTime,theLastMarketTime,province,city,area,memLevel,tradeAmountA,tradeAmountB,tradeCountA,tradeCountB,member};
		//拼接搜索器内容
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < paramsStr.length; i++) {
			if (null!=paramsStr[i]) {
				sb.append(paramsStr[i]);
			}else{
				sb.append("");
			}
			sb.append("|");
		}
		sb.deleteCharAt(sb.toString().length()-1);//去掉最后一个"|"
		//封装搜索器对象
		SMSBuyersSearch bs=new SMSBuyersSearch();
		bs.setSearchName(saveSearInput);//搜索器名字
		bs.setSearchCondition(sb.toString());//搜索器内容
		bs.setUpdateUserId(currentUser.getId());//修改人
		bs.setCreateUserId(currentUser.getId());//创建人
		if (null==smsBuyersSearchId) {
			//调用搜索器add方法
			smsBuyersSearchService.saveSMSBuyersSearch(bs);
			return bs.getId()+","+bs.getSearchName()+",add";
		}else{
			//调用搜索器update方法
			bs.setId(smsBuyersSearchId);
			smsBuyersSearchService.editSmsBuyersSearch(bs);
			return bs.getId()+","+bs.getSearchName()+",edit";
		}
		
	}

	/**
	 * 构建查询参数Map
	 * 判断searchParams Bean是否为空，如果为空则不查，
	 * 如果不为空则获取查询项，构建map查询参数
   */
	private Map<String, Object> getSearchBuyersListParams(){
		String receiverMobileReg="\\d{11}";//验证手机号
		String receiverNameReg="[\\u4e00-\\u9fa5]{1,25}";//联系人--全汉字
		String buyerAccountReg="[0-9a-zA-Z\\u4e00-\\u9fa5]{5,25}";//会员名--数字、字母、下划线、汉字
		String[] regs=new String[]{receiverMobileReg,receiverNameReg,buyerAccountReg};
		Map<String, Object> map=new HashMap<String, Object>();
		//(String类型，上次交易时间)
		map.put("theLastTradeTime", tradeTime);
		//(普通会员 ：0， 高级会员 ：1， VIP会员：2，至尊VIP会员 ：３，所有会员 ： 4)
		map.put("userGrade",memLevel);
		//(交易量最小值)
		map.put("tradeCountMin",tradeAmountA);
		//(交易量最大值)
		map.put("tradeCountMax", tradeAmountB);
		//(交易额最小值)
		map.put("tradeAmountMin", tradeCountA);
		//(交易额最大值)
		map.put("tradeAmountMax", tradeCountB);
		//(省)   code
		if (null!=province && !("").equals(province)) {
			map.put("receiverProvince", province.trim());
		}
		//(市)   code
		if (null!=city && !("").equals(city)) {
			map.put("receiverCity", city.trim());
		}
		//(区或者县)   code
		if (null!=area && !("").equals(area)) {
			map.put("receiverDistrict", area.trim());
		}
		//(排序字段 ：交易额：tradeAmount, 交易量: tradeCount)
		if (null!=orderByCol && !("").equals(orderByCol) ) {
			map.put("orderByCol", orderByCol.trim());
		}
		//店铺名称
		/*User u = userService.getUserById(id);
		shopName = u.getShopName();*/
		if (null!=shopName && ("").equals(shopName)) {
			map.put("shopName", shopName.trim());
		}
		//List<Integer>(此处如果为子帐号，请传入主帐号的ID)和关联店铺主帐号的Id
		User currentUser = super.readCookieUser();
		List<Integer> Ids=new ArrayList<Integer>();
		if (null!=super.readCookieUser().getParentId()) {
			//不是主账号登录
			buyers.setUserId(super.readCookieUser().getParentId());
			map.put("userIds", Resource.getBindedUserIdList(currentUser));//Resource.getBindedUserIdList(currentUser)
		}else{
			Ids.add(currentUser.getId());
			map.put("userIds", Ids);
		}
		//判断member可能性属性值
		if (null!=member) {
			//电话
			if (Pattern.matches(receiverMobileReg,member)) {
				map.put("receiverMobile",member);
				return map;
			}
			//(联系人)
			if (Pattern.matches(receiverNameReg, member)) {
				map.put("receiverName",member);
				return map;
			}
			//(会员名)
			if (Pattern.matches(buyerAccountReg, member)) {
				map.put("buyerAccount", member);
			}
		}
		return map;
	}

	//获取单个搜索器对象，返回搜索器内容
	public String findSmsBuyersSearch() throws  Exception{
		smsBuyersSearch=smsBuyersSearchService.getSmsBuyersSearchById(Integer.parseInt(searName));
		smsBuyersSearchId=smsBuyersSearch.getId();
		saveSearInput=smsBuyersSearch.getSearchName();
		jsonStr=smsBuyersSearch.getSearchCondition()+"|"+smsBuyersSearchId+"|"+saveSearInput;
		return "jsondata";
	}
	/**
	 * 删除会员
	 * @return
	 */
	public String delSmsBuyers(){
		try {
			if (memberId.indexOf(",")!=-1) {
				//有多个Id
				String[] mIds=memberId.split(",");
				List<Integer> ids=new ArrayList<Integer>();
				for (String strId : mIds) {
					ids.add(Integer.parseInt(strId.trim()));
				}	
				smsBuyersService.delBatchSMSBuyers(ids);
			}else{
				//删除单个
				smsBuyersService.delSMSBuyersById(Integer.parseInt(memberId));
			}
		} catch (Exception e) {
		}
		return "jsondata";
	}
	//保存、编辑会员信息
	public String saveSmsBuyer(){
		if (null!=super.readCookieUser().getParentId()) {
			//不是主账号登录
			buyers.setUserId(super.readCookieUser().getParentId());
		}else{
			buyers.setUserId(super.readCookieUser().getId());
		}
		buyers.setUpdateUserId(super.readCookieUser().getId());//updateUserId
		buyers.setCreateUserId(super.readCookieUser().getId());//createUserID
		buyers.setSourceStatus("TAOBAO");
		if (null!=buyers.getId()) {
			//修改
			if (smsBuyersService.editSMSBuyers(buyers)==true) {
				return searchBuyersList();
			}
			return "editInput";
		}else{
			//添加
			if (smsBuyersService.addSMSBuyers(buyers)==true) {
				return searchBuyersList();
			}
			return  "addInput";
		}			
	}
	//跳转至添加页面
	public String toAdd(){
		return "toAddInput";
	}
	//会员修改前的查询
	public String toEdit(){
		try {
			buyers=smsBuyersService.getSMSBuyersById(buyers.getId());
		} catch (Exception e) {
			return "list";
		}
		return "toEdit";
	}
	
	/**
	 * 弹出设置会员等级层
	 * @return
	 */
	public String getBuyerByUserId(){
		User curUser = super.readCookieUser();
		//grade =smsBuyersGradeService.getSMSBuyersGradeByUserId(curUser.getParentId() == null ? curUser.getId() : curUser.getParentId());
		try {
			grade =smsBuyersGradeService.getSMSBuyersGradeById(1);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "getBuyerByUserId";
	}
                                   
		//旺旺唯一验证
	   public String checkWangWang() throws IOException{
		if (smsBuyersService.checkWW(buyers.getBuyerAccount(),memberId)==true) {
			jsonStr="true";
		}else{
			jsonStr="false";
		}
		return "jsondata";
	}
	//查看会员详情
	public String MemberDetails(){
		try {
			buyers=smsBuyersService.getSMSBuyersById(buyers.getId());
			return "goSee";
		} catch (Exception e) {
		}
		return "list";
	}


	/**
	 * 设置会员等级
	 * @return
	 */
	public String setUserGrade(){
		
		String type = "edit";
		
		User curUser = super.readCookieUser();
		SMSBuyersGrade grade1 = null;
		try {
			grade1 = smsBuyersGradeService.getSMSBuyersGradeById(grade.getId());
		} catch (Exception e) {
			logger.error("设置会员等级时："+e.getMessage());
		}		
		if (grade1 == null) {
			type = "add";
			grade1 = new SMSBuyersGrade();
		}
		grade1.setHighAccount(grade.getHighAccount());
		grade1.setHighCount(grade.getHighCount());
		grade1.setVipAccount(grade.getVipAccount());
		grade1.setVipCount(grade.getVipCount());
		grade1.setVipHighAccount(grade.getVipHighAccount());
		grade1.setVipHighCount(grade.getVipHighCount());
		if(curUser.getParentId() == null) {
			grade1.setUserId(curUser.getId()); 
		} else {
			grade1.setUpdateUserId(curUser.getId());
			grade1.setUserId(curUser.getParentId());
		}
		
		boolean bool = true;
		
		if (StringUtils.equals(type, "edit")) {
			bool = smsBuyersGradeService.editSMSBuyersGrade(grade1);
		} else {
			bool = smsBuyersGradeService.addSMSBuyersGrade(grade1);
		}
		
		logger.info("设置会员等级成功与否："+bool);
		
		return "setUserGrade";
	}
	
	/**
	 * 获取用户的主账户ID
	 * @return
	 */
	private int getMainUserId() {
		User landUser = super.readCookieUser();
		Integer parentId = landUser.getParentId();
		if(parentId!=null) {
			return parentId;
		}else {
			return landUser.getId();
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public SMSBuyersService getSmsBuyersService() {
		return smsBuyersService;
	}

	public void setSmsBuyersService(SMSBuyersService smsBuyersService) {
		this.smsBuyersService = smsBuyersService;
	}

	public String[] getDownLoadPaths() {
		return downLoadPaths;
	}

	public void setDownLoadPaths(String[] downLoadPaths) {
		this.downLoadPaths = downLoadPaths;
	}

	public OutputStream getRes() {
		return res;
	}

	public void setRes(OutputStream res) {
		this.res = res;
	}

	public ZipOutputStream getZos() {
		return zos;
	}

	public void setZos(ZipOutputStream zos) {
		this.zos = zos;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}


	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public BufferedReader getBf() {
		return bf;
	}

	public void setBf(BufferedReader bf) {
		this.bf = bf;
	}

	public String getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(String orderIds) {
		this.orderIds = orderIds;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUploadErrType() {
		return uploadErrType;
	}

	public void setUploadErrType(String uploadErrType) {
		this.uploadErrType = uploadErrType;
	}

	public List<String> getUploadErrlist() {
		return uploadErrlist;
	}

	public void setUploadErrlist(List<String> uploadErrlist) {
		this.uploadErrlist = uploadErrlist;
	}

	public String getQueryMessage() {
		return queryMessage;
	}

	public void setQueryMessage(String queryMessage) {
		this.queryMessage = queryMessage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

//	public static String getDefault() {
//		return DEFAULT;
//	}

	public static String getSpecialCharA() {
		return SPECIAL_CHAR_A;
	}

	public static String getSpecialCharB() {
		return SPECIAL_CHAR_B;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTempkey() {
		return tempkey;
	}

	public void setTempkey(String tempkey) {
		this.tempkey = tempkey;
	}

	public static String getRegexPostcode() {
		return REGEX_POSTCODE;
	}

	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

	public Integer getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(Integer currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public HashSet<String> getSet() {
		return set;
	}

	public void setSet(HashSet<String> set) {
		this.set = set;
	}

	public String getTotalBuyers() {
		return totalBuyers;
	}

	public void setTotalBuyers(String totalBuyers) {
		this.totalBuyers = totalBuyers;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}

	public void setSmsBuyersSearch(SMSBuyersSearch smsBuyersSearch) {
		this.smsBuyersSearch = smsBuyersSearch;
	}
	
	public List<SMSBuyersSearch> getSmsBuyersSearchs() {
		return smsBuyersSearchs;
	}

	
	public String getSearName() {
		return searName;
	}

	public void setSearName(String searName) {
		this.searName = searName;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getMemLevel() {
		return memLevel;
	}

	public void setMemLevel(String memLevel) {
		this.memLevel = memLevel;
	}

	public String getTradeAmountA() {
		return tradeAmountA;
	}

	public void setTradeAmountA(String tradeAmountA) {
		this.tradeAmountA = tradeAmountA;
	}

	public String getTradeAmountB() {
		return tradeAmountB;
	}

	public void setTradeAmountB(String tradeAmountB) {
		this.tradeAmountB = tradeAmountB;
	}

	public String getTradeCountA() {
		return tradeCountA;
	}

	public void setTradeCountA(String tradeCountA) {
		this.tradeCountA = tradeCountA;
	}

	public String getTradeCountB() {
		return tradeCountB;
	}

	public void setTradeCountB(String tradeCountB) {
		this.tradeCountB = tradeCountB;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getSaveSearInput() {
		return saveSearInput;
	}

	public void setSaveSearInput(String saveSearInput) {
		this.saveSearInput = saveSearInput;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	
	public SMSBuyers getBuyers() {
		return buyers;
	}

	public void setBindUserList(List<User> bindUserList) {
		this.bindUserList = bindUserList;
	}
	
	public List<User> getBindUserList() {
		return bindUserList;
	}

	public void setBuyers(SMSBuyers buyers) {
		this.buyers = buyers;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOrderByCol() {
		return orderByCol;
	}

	public void setOrderByCol(String orderByCol) {
		this.orderByCol = orderByCol;
	}

	public Integer getSmsBuyersSearchId() {
		return smsBuyersSearchId;
	}

	public void setSmsBuyersSearchId(Integer smsBuyersSearchId) {
		this.smsBuyersSearchId = smsBuyersSearchId;
	}
	public SMSBuyersGrade getGrade() {
		return grade;
	}

	public void setGrade(SMSBuyersGrade grade) {
		this.grade = grade;
	}


	public void setSmsBuyers(List<SMSBuyers> smsBuyers) {
		this.smsBuyers = smsBuyers;
	}
    
	public String getWawa() {            
		return wawa;                     
	}                                    
                                         
	public void setWawa(String wawa) {   
		this.wawa = wawa;                
	} 
	public List<SMSBuyers> getSmsBuyers() {
		return smsBuyers;
	}
	
	public String getShopNames() {
		return shopNames;
	}

	public void setShopNames(String shopNames) {
		this.shopNames = shopNames;
	}

	public Pagination<SMSBuyers> getPagination() {
		return pagination;
	}
	
	public void setPagination(Pagination<SMSBuyers> pagination) {
		this.pagination = pagination;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getShopKey() {
		return shopKey;
	}

	public void setShopKey(String shopKey) {
		this.shopKey = shopKey;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
