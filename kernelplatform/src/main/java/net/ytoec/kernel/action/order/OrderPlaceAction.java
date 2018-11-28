package net.ytoec.kernel.action.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.AbstractInterfaceAction;
import net.ytoec.kernel.action.remote.CommonOrderAction;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.process.XmlBuildProcessor;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.OrderPlaceSenderTemp;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dataobject.SpecialType;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.dto.DtoBranch;
import net.ytoec.kernel.service.JgWaybillService;
import net.ytoec.kernel.service.JgWaybillUpdateService;
import net.ytoec.kernel.service.OrderPlaceSenderTempService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.ProductService;
import net.ytoec.kernel.service.SendTaskService;
import net.ytoec.kernel.service.SendTaskToTBService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class OrderPlaceAction extends AbstractInterfaceAction{
	/**
	 * 通用金刚接口访问地址.
	 */
	private static final String JIN_GANG_ORDER = "JINGANG.COMMON.ORDER";
	private static final String JIN_GANG_TAOBAO_ORDER = "JINGANG.ORDER";


	@Autowired
	private SendTaskService<SendTask> sendTaskService;
	@Autowired
	private JgWaybillService<JgWaybill> jgWaybillService;
	@Autowired
	private SendTaskToTBService<SendTaskToTB> sendTaskToTBService;

	@Autowired
	private OrderService<Order> orderService;

	@Autowired
	private TraderInfoService tradeInfoService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserThreadService userThreadService;
	
	@Inject
	private OrderPlaceSenderTempService orderPlaceSenderTempService;
	
	
	@Autowired
	private JgWaybillUpdateService<JgWaybillUpdate> jgWaybillUpdateService;

	private static Logger logger = LoggerFactory.getLogger(CommonOrderAction.class);
	
	
	private String check="0";
	
	// 物流平台标识
	private String JGtxLogisticID = "0";

	// 物流平台标识
	private String VIPtxLogisticID = "0";
	
	// 版本号主要用于升级
	private String version="";
	// 物流公司ID
	private String logisticProviderId="";
	private String clientId="";
	private String customerId="";
	// 这是物流平台交易生成的物流号
	private String txLogisticId="";
	// 业务的交易号（可选）
	private String tradeNo="";
	
	private Integer id;
	// 物流公司的运单号
	private String mailNo;
	private String flag="0";
	private String sendStartTime="";
	//
	private String sendEndTime="";
	// 保价
	private double insuranceValue;
	// 打包
	private boolean packageOrNot;
	// 特殊商品性质
	private SpecialType special;
	// 备注
	private String remark;

	private Date createTime;
	
	private String vipId="1";
	
	private String lineType="1";
	
	public static String orderType="67";//我要发货默认为67
	
	private String serviceType="1";
	
	private String goodsValue="0";
	
	private String itemsValue="0";
	
	private String totalServiceFee="0";
	
	private String buyServiceFee="0";
	
	private String codSplitFee="0";
	
	private String type="1";
	
	private String itemsWeight="0";
	private String checkTab;
	
	
	private String itemName;
	private String itemNumber;
	private String itemWeight;
	
	
    //发件人
	private String senderName;
	private String senderMobile;
	private String senderAddress;
	private String senderProv;
	private String senderCity;
	private String senderDistrict;
	private String senderDetail;
	
	
	//收件人
	private String receiverName;
	private String receiverMobile;
	private String receiverAddress;
	private String receiverProv;
	private String receiverCity;
	private String receiverDistrict;
	private String receiverPhone1;
	private String receiverPhone2;
	private String telExtCode; //座机号

	//-------------------面单绑定属性------------------------------
	
//	// 物流平台的物流号（不能为空）
//	private String txLogisticId;
//	// 运单号，在没有确定的情况下可能为空，如取消订单
//	private String mailNo;
//	//
//	private String clientId;
//	// 物流公司编号
//	private String logisticProviderId;
	// 通知类型 INSTRUCTION：通知指令

//	// 备注
//	private String remark;
	
	private String infoType;
	// 通知内容 在infoType为INSTRUCTION时：UPDATE ：更新运单号 WITHDRAW：取消订单
	private String infoContent;
	private String name;
	private String acceptTime;
	private String currentCity;
	private String nextCity;
	private String facility;
	private String contactInfo;
	private String weight;
	private String trackingInfo;

    private String processResult;
	
    
    private List<Order> orderList;
	private Integer currentPage = 1;
	private Pagination pagination; //分页对象
	private String startTime;
	private String endTime;
	private String mailNum;
	private int currentUserId;
	private String logicId;
	
	private String updateIds;
	private List<OrderFormInfo> orderFormList;
	
	private String sendTimeStart;
	private String sendTimeEnd;
	
	//发件人临时表
	private Integer tempId;
	private String mobile;
	private String address;
	private String tips; //1-修改手机号 2-修改姓名 3-修改地址
	
	private String menuFlag;
	
	public String getMenuFlag() {
		return menuFlag;
	}

	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}

	public String tabPass(){
		checkTab = "1";
		pagination = new Pagination(currentPage, pageNum);
		pagination.setTotalRecords(0);
		return "toOrderCreate";
	}
	
	public String toOrderCreate(){
		checkTab = "0";
		User curUser = super.readCookieUser();
		
		//根据userId查询发货人信息表  按修改时间倒序
		List<OrderPlaceSenderTemp> senders  = orderPlaceSenderTempService.getByUserId(curUser.getId());
		
		if(senders.size() <= 0){
			setCurrentUser();
		}else{
			OrderPlaceSenderTemp senderTemp = senders.get(0);
			senderName = senderTemp.getSendName();
			senderMobile = senderTemp.getMobile();
			senderAddress = "";
			senderProv = senderTemp.getProvince();
			senderCity = senderTemp.getCity();
			senderDistrict = senderTemp.getDistrict();
			senderDetail = senderTemp.getAddress();
			tempId = senderTemp.getId();
			
			if(!"".equals(senderTemp.getProvince()) && senderTemp.getProvince()!=null){
				senderAddress +=senderTemp.getProvince();
			}
			if(!"".equals(senderTemp.getCity()) && senderTemp.getCity() !=null){
				senderAddress +=","+senderTemp.getCity();
			}
			if(!"".equals(senderTemp.getDistrict()) && senderTemp.getDistrict() != null){
				senderAddress +=","+senderTemp.getDistrict();
			}
			if(!"".equals(senderTemp.getAddress()) && senderTemp.getAddress() !=null){
				senderAddress +=","+senderTemp.getAddress();
			}
			
			setSendTime();//确认发货的预约开始时间和结束时间
			setTime();
		}
		//供“发货记录”用
		pagination = new Pagination(currentPage, pageNum);
		pagination.setTotalRecords(0);
		
		return "toOrderCreate";
	}
	
	/**
	 * 修改临时表 qinghua.yang
	 * 先判断临时表是否存在该卖家用户的记录，若存在匹配修改，若不存在插入
	 * @return
	 */
	public String eidtTemp(){
		User currentUser = super.readCookieUser();
		Integer userId = currentUser.getParentId() == null ? currentUser.getId() : currentUser.getParentId(); 
		
		OrderPlaceSenderTemp temp = new OrderPlaceSenderTemp();
		if(tempId == null || "".equals(tempId)){
			//插入临时表
			temp.setSendName(senderName);
			temp.setMobile(mobile);
			temp.setProvince(address.split(",")[0]);
			temp.setCity(address.split(",")[1]);
			temp.setDistrict(address.split(",")[2]);
			temp.setAddress(address.split(",")[3]);
			temp.setUserId(userId);
			
			orderPlaceSenderTempService.addTemp(temp);
		}else{
			temp.setId(tempId);
			if("1".equals(tips))temp.setMobile(mobile);
			if("2".equals(tips))temp.setSendName(senderName);
			if("3".equals(tips)){
				temp.setProvince(address.split(",")[0]);
				temp.setCity(address.split(",")[1]);
				temp.setDistrict(address.split(",")[2]);
				temp.setAddress(address.split(",")[3]);
			}
			
			orderPlaceSenderTempService.eidtTemp(temp,tips);
		}
		
		return null;
	}
	
	private void setCurrentUser(){
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		pagination.setTotalRecords(0);
		currentUserId = currentUser.getId();
		senderName=currentUser.getUserNameText();
		User user = userService.getUserById(currentUserId);
		senderMobile = user.getMobilePhone();
		senderAddress = "";
		senderProv = currentUser.getAddressProvince();
		senderCity = currentUser.getAddressCity();
		senderDistrict = currentUser.getAddressDistrict();
		senderDetail = currentUser.getAddressStreet();
		
		if(!"".equals(currentUser.getAddressProvince()) && currentUser.getAddressProvince()!=null){
			senderAddress +=currentUser.getAddressProvince();
		}
		if(!"".equals(currentUser.getAddressCity()) && currentUser.getAddressCity() !=null){
			senderAddress +=","+currentUser.getAddressCity();
		}
		if(!"".equals(currentUser.getAddressDistrict()) && currentUser.getAddressDistrict() != null){
			senderAddress +=","+currentUser.getAddressDistrict();
		}
		if(!"".equals(currentUser.getAddressStreet()) && currentUser.getAddressStreet() !=null){
			senderAddress +=","+currentUser.getAddressStreet();
		}
		setSendTime();//确认发货的预约开始时间和结束时间
		setTime();
	}
	
	private void setSendTime() {
		Date sysDate = new Date();
		int a = sysDate.getDay();
		int curYear = sysDate.getYear() +1900;
		int curMonth = sysDate.getMonth()+1;
		String curMonthTemp = curMonth+"";
		if(curMonth<=9){
			curMonthTemp = "0"+curMonth;
		}else{
			curMonthTemp = curMonth+"";
		}
		int curDay = sysDate.getDate();
		String dayTemp = curDay+"";
		if(curDay<=9){
			dayTemp = "0"+curDay;
		}else{
			dayTemp = curDay+"";
		}
		int curHour = sysDate.getHours();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(curHour<18){
			sendTimeStart = sdf.format(sysDate);
			sendTimeEnd = curYear+"-"+curMonthTemp+"-"+dayTemp+" 18:00";
		}else{
			sendTimeStart = sdf.format(sysDate);
			
			Calendar ca = Calendar.getInstance();   
			ca.add(Calendar.DATE, 1);
			Date addOne = ca.getTime();
			int mon = addOne.getMonth()+1;
			String addMonthTemp = mon+"";
			if(mon<=9){
				addMonthTemp = "0"+mon;
			}else{
				addMonthTemp = mon+"";
			}
			int day = addOne.getDate();
			String addDayTemp = day+"";
			if(curDay<=9){
				addDayTemp = "0"+day;
			}else{
				addDayTemp = day+"";
			}
			
			sendTimeEnd = addOne.getYear()+1900+"-"+addMonthTemp+"-"+addDayTemp + " 18:00";
			
		}
		
	}
	
	private void setTime(){
		   /**
	        * 默认情况下取当前时间在内的7天内的数据。
	        */
		if(StringUtil.isBlank(startTime)||StringUtil.isBlank(endTime)){
			startTime = DateUtil.dateArithmetic(new Date(),6);
		    endTime = DateUtil.dateArithmetic(new Date(),0);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		int year = d.getYear()+1900;
		int month = d.getMonth()+1;
		int day = d.getDate(); 
		String monthTemp = String.valueOf(month);
		String dayTemp = String.valueOf(day);
		if(month<10){
			monthTemp = "0"+month;
		}
		if(day<10){
			dayTemp = "0"+day;
		}
		String compareTimeStr = year+"-"+monthTemp+"-"+dayTemp+" 18:00:00";
		String currentTimeStr = sdf.format(d);
		try {
			Date compareTime = sdf.parse(compareTimeStr);
			Date currentTime = sdf.parse(currentTimeStr);
			
			SimpleDateFormat st=new SimpleDateFormat("yyyyMMddHH");
			int comInt = Integer.parseInt(st.format(compareTime));
			int curInt = Integer.parseInt(st.format(currentTime));
			if(curInt<comInt){
				sendStartTime = currentTimeStr;
				sendEndTime = compareTimeStr;
			}else{
				sendStartTime = currentTimeStr;
				long lg = compareTime.getTime()+24*60*60*1000;
				sendEndTime = sdf.format(new Date(lg));;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
	}
	public String toMailNoBind(){	
		//if("输入查找的订单号".equals(mailNum))mailNum=null;
		setCurrentUser();
		checkTab = "1";
		check="1";
		menuFlag = "fahuo_orderPlace";
		User currentUser = super.readCookieUser();
		pagination = new Pagination(currentPage, pageNum);
		orderList = null;
		net.ytoec.kernel.action.order.QueryOrder queryOrder=new net.ytoec.kernel.action.order.QueryOrder(); 
		queryOrder.setCustomerIds(Resource.getBindedCustomerIdList(currentUser));
		queryOrder.setStartTime(startTime);
		queryOrder.setEndTime(endTime);
		if(receiverName!=null){
			queryOrder.setBuyName(receiverName.trim());
			queryOrder.setReceiverName(receiverName.trim());
		}
		queryOrder.setOrderType(orderType);
		orderList=orderService.queryOrderByPage(queryOrder, pagination);
		orderFormList=new ArrayList<OrderFormInfo>();
		for(Order order:orderList){
			OrderFormInfo ofi=new OrderFormInfo();
			ofi.setId(order.getId());
			ofi.setMailNo(order.getMailNo());
			ofi.setTxLogisticId(order.getTxLogisticId());
			ofi.setClientId(order.getClientId());
			ofi.setRemark(order.getId().toString()+"_"+order.getTxLogisticId());
			List<TraderInfo> traders=tradeInfoService.getTraderInfoByOrderId(order.getId());
			
			TraderInfo trader = changeDateToString(traders.get(0));
			if(TraderInfo.RECEIVE_TYPE.equals(traders.get(0).getTradeType())){
				ofi.setReceiver(trader);
			} else {
				ofi.setSender(trader);
			}
			trader = changeDateToString(traders.get(1));
			if(TraderInfo.SENDE_TYPE.equals(traders.get(1).getTradeType())) {
				ofi.setSender(trader);
			} else {
				ofi.setReceiver(trader);
			}
			ofi.setServiceType(order.getStatus());
			ofi.setLineType(order.getLineType());
			
			//网店名称的获取
			User user = userService.getUserByCustomerId(order.getCustomerId());
			String shopName = user.getShopName();
			ofi.setShopName(shopName);
			
			List<Product> products = productService.getProductListByOrder(order.getId());
			String name="";
			Integer num=0;
			for(Product p:products){
				name+=" "+p.getItemName();
				num+=p.getItemNumber();
			}
			ofi.setProduct(name);
			ofi.setNum(num.toString());
			
            
			//查询网带名称
			UserThread uh = new UserThread();
			uh.setUserCode(currentUser.getUserCode());
			List<UserThread> uhList = userThreadService.searchUsersByCode(uh);
			if(uhList.size() > 0 ) {
				String site = uhList.get(0).getSiteCode();
				DtoBranch dtoBranch = Resource.getDtoBranchByCode(site);
				if(dtoBranch!=null) {
					ofi.getSender().setName(dtoBranch.getText());
				}
			}else ofi.getSender().setName(currentUser.getSite());

			orderFormList.add(ofi);
		}
		int totalRecords=orderService.countOrders(queryOrder);
//		pagination.setTotalPages(3);
		pagination.setTotalRecords(totalRecords);
		return "toOrderCreate";
	}
   
	
	// 订单创建.
	public String orderCreate() throws Exception {
		
		try {
		User currentUser = super.readCookieUser();
		OrderFormInfo requestOrder = new OrderFormInfo();
		requestOrder.setMailNo(mailNo);
		requestOrder.setClientId(currentUser.getClientId()!=null?currentUser.getClientId():"TAOBAO");
		requestOrder.setCreateTime(new Date());
		requestOrder.setCustomerId(currentUser.getTaobaoEncodeKey());
		if(sendStartTime!=null &&!sendStartTime.equals("")){
			requestOrder.setSendStartTime(sendStartTime+".0 CST");
		}
		if(sendEndTime!=null &&	!sendEndTime.equals("")){
			requestOrder.setSendEndTime(sendEndTime+".0 CST");
		}
		requestOrder.setFlag(flag);//"0"
		requestOrder.setGoodsValue(goodsValue);//商品金额，包括优惠和运费，但无服务费
		requestOrder.setInsuranceValue(insuranceValue);
		requestOrder.setItemsValue(itemsValue);//支付宝金额/代收货款金额，商品金额+服务费
		requestOrder.setItemsWeight(StringUtil.isBlank(itemWeight)?"0":itemWeight);//重量TAOBAO接口未定义
		requestOrder.setLineType(Order.ONLINE_TYPE);//线上
		requestOrder.setLogisticProviderId("YTO");
		requestOrder.setOrderType(orderType);
		requestOrder.setPackageOrNot(packageOrNot);//FIXME TAOBAO无
		requestOrder.setRemark(remark);
		requestOrder.setServiceType(serviceType);//"0"
		requestOrder.setSpecial(special);//"0" FIXME 暂无用处
		requestOrder.setTradeNo("");
		requestOrder.setTxLogisticId(logicId);//生成
		requestOrder.setType("1");//兼容 默认1
		requestOrder.setVersion("");
		requestOrder.setVipId(currentUser.getId().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		requestOrder.setPartitiondate(sdf.parse(sdf.format(new Date())));
		
		TraderInfo sender = new TraderInfo();
		String[] strs=senderAddress.split(",");
		sender.setAddress(strs[3]);
		sender.setProv(strs[0]);
		sender.setCity(strs[1]);
		sender.setDistrict(strs[2]);
		sender.setMobile(senderMobile);
		sender.setPhone(currentUser.getTelePhone());
		sender.setName(senderName);
		sender.setTradeType(TraderInfo.SENDE_TYPE);
		sender.setNumProv(Resource.getCodeByName(strs[0]));
		sender.setPostCode(Resource.getCodeByName(strs[0]).toString());
		requestOrder.setSender(sender);
		TraderInfo receiver = new TraderInfo();
		receiver.setAddress(receiverAddress.trim());
		receiver.setCity(receiverCity);
		receiver.setCreateTime(new Date());
		receiver.setDistrict(receiverDistrict);
		receiver.setMobile(receiverMobile);
		receiver.setName(receiverName.trim());
		receiver.setProv(receiverProv);
		receiver.setNumProv(Resource.getCodeByName(receiverProv));
		receiver.setTradeType(TraderInfo.RECEIVE_TYPE);
		
		//如果发货地址错误将提示发货失败
		if(receiver.getProv() == null || "".equals(receiver.getProv()) || 
				receiver.getCity() == null || "".equals(receiver.getCity()) || 
				receiver.getDistrict() == null || "".equals(receiver.getDistrict()) || 
				receiver.getAddress() == null || "".equals(receiver.getAddress())){
			processResult="0";
		}
		
		if("".equals(telExtCode.trim())){
			receiver.setPhone(StringUtil.isBlank(receiverPhone1)?receiverPhone2:receiverPhone1+"-"+receiverPhone2);
		}else {
			receiver.setPhone(StringUtil.isBlank(receiverPhone1)?receiverPhone2:receiverPhone1+"-"+receiverPhone2+"-"+telExtCode);
		}
		receiver.setPostCode(Resource.getCodeByName(receiverProv).toString());
		requestOrder.setReceiver(receiver);
		Product p=new Product();
		p.setItemName(itemName.trim());
		p.setCreateTime(new Date());
		if (null!=itemNumber && !("").equals(itemNumber.trim())) {
			p.setItemNumber(Integer.valueOf(itemNumber));
		}else{
			p.setItemNumber(1);//如果前台没有填写发货数量那么这里默认为1
		}
		p.setItemValue(0);//FIXME
		p.setLogisticId(requestOrder.getTxLogisticId());
		p.setMailNo(requestOrder.getMailNo());
		List<Product> items=new ArrayList<Product>();
		items.add(p);
		requestOrder.setItems(items);
		if("0".equals(processResult)){
			this.toOrderCreate();
			this.receiverName = "";
			return "toOrderCreate";
		}
		
		boolean flag = true;
		try {
			requestOrder.setStatus("1");
			flag = this.orderService.addOrderFormInfo(requestOrder);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		// 插入 任务表
		JGtxLogisticID = requestOrder.getTxLogisticId();
		if(flag){
			/**
			 * 20120411为兼容金刚接口修改，修改为获取密文前将orderType修改为1，因为金刚接口只接受0,1,3三种状态。
			 */
			requestOrder.setOrderType("1");
			//获得下单的明文密文
			String logisticsInterface=XmlBuildProcessor.getOrderCreateXML(requestOrder);
			processResult=this.sendTask(logisticsInterface,requestOrder.getClientId())?"1":"0";
		}else{
			processResult="0";
		}
		} catch (Exception e) {
			processResult="0";
		}
		this.toOrderCreate();
		this.receiverName = "";
		menuFlag="fahuo_orderPlace";
		return "toOrderCreate";
	}

	


	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public String getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(String sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	// 订单更新.
	public String orderUpdate() throws Exception {
		User currentUser = super.readCookieUser();
        List<String> ids=StringUtil.split(updateIds, ";");
        for(String str:ids){
        	if(!StringUtil.isBlank(str)){
	        	String[] val=str.split("_");
	        	orderService.updateMailNoById(val[0], val[2]);
	        	UpdateWaybillInfo updateInfo = new UpdateWaybillInfo();
	        	updateInfo.setClientId(currentUser.getClientId()!=null?currentUser.getClientId():"TAOBAO");
	        	updateInfo.setInfoContent("UPDATE");
	        	updateInfo.setInfoType("INSTRUCTION");
	        	updateInfo.setLogisticProviderId("YTO");
	        	updateInfo.setMailNo(val[1]);
	        	updateInfo.setTxLogisticId(val[1]);
	        	updateInfo.setRemark("面单绑定");
				JGtxLogisticID = updateInfo.getTxLogisticId();
				String logisticsInterface=XmlBuildProcessor.getOrderBindXML(updateInfo);
				this.sendTask(logisticsInterface,updateInfo.getClientId());
        	}
        }
        return "json";
	}

	
	private boolean sendTask(String logisticsInterface,String clientId){
		//获取密文
		String key=Resource.getSecretId(clientId);
		String dataDigest=Md5Encryption.MD5Encode(logisticsInterface+key);
		
        //对报文进行UTF8编码
		logisticsInterface=ProcessorUtils.encode(logisticsInterface, XmlSender.UTF8_CHARSET);
		dataDigest=ProcessorUtils.encode(dataDigest, XmlSender.UTF8_CHARSET);
		
		SendTask sendTask = new SendTask();
		sendTask.setOrderId(0);
		sendTask.setClientId(clientId);
		sendTask.setRemark("orderPlace");
		sendTask.setTaskFlagId(ProcessorUtils.getflagid(JGtxLogisticID));
		sendTask.setTaskFlag(ProcessorUtils.getflag(JGtxLogisticID));
		sendTask.setTxLogisticId(JGtxLogisticID);
		if("TAOBAO".equals(clientId))sendTask.setRequestURL(Resource.getChannel(JIN_GANG_TAOBAO_ORDER));
		else sendTask.setRequestURL(Resource.getChannel(JIN_GANG_ORDER));
		sendTask.setRequestParams("logistics_interface="+logisticsInterface+"&data_digest="+ dataDigest);
		return this.sendTaskService.addSendTask(sendTask);
	}
	
	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderMobile() {
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(String itemWeight) {
		this.itemWeight = itemWeight;
	}

	public String getSenderProv() {
		return senderProv;
	}

	public void setSenderProv(String senderProv) {
		this.senderProv = senderProv;
	}

	public String getSenderCity() {
		return senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public String getSenderDistrict() {
		return senderDistrict;
	}

	public void setSenderDistrict(String senderDistrict) {
		this.senderDistrict = senderDistrict;
	}

	public String getReceiverProv() {
		return receiverProv;
	}

	public void setReceiverProv(String receiverProv) {
		this.receiverProv = receiverProv;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}

	public String getReceiverPhone1() {
		return receiverPhone1;
	}

	public void setReceiverPhone1(String receiverPhone1) {
		this.receiverPhone1 = receiverPhone1;
	}

	public String getReceiverPhone2() {
		return receiverPhone2;
	}

	public void setReceiverPhone2(String receiverPhone2) {
		this.receiverPhone2 = receiverPhone2;
	}

	public String getTelExtCode() {
		return telExtCode;
	}

	public void setTelExtCode(String telExtCode) {
		this.telExtCode = telExtCode;
	}

	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getMailNum() {
		return mailNum;
	}

	public void setMailNum(String mailNum) {
		this.mailNum = mailNum;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	
	public String getLogicId() {
		return logicId;
	}

	public void setLogicId(String logicId) {
		this.logicId = logicId;
	}

	public String getUpdateIds() {
		return updateIds;
	}

	public void setUpdateIds(String updateIds) {
		this.updateIds = updateIds;
	}

	public int getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public String getTxLogisticId() {
		return txLogisticId;
	}

	public void setTxLogisticId(String txLogisticId) {
		this.txLogisticId = txLogisticId;
	}

	public List<OrderFormInfo> getOrderFormList() {
		return orderFormList;
	}

	public void setOrderFormList(List<OrderFormInfo> orderFormList) {
		this.orderFormList = orderFormList;
	}
	
	public TraderInfo changeDateToString(TraderInfo traderInfo){
		Date d = traderInfo.getCreateTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = sdf.format(d);
		traderInfo.setCreateTimeStr(timeStr);
		return traderInfo;
	}
	
	public String bindedOrUpdateMailNo(){
		try{
			orderService.bindedOrUpdateMailNo(id,mailNo,txLogisticId,clientId);
		} catch (Exception e){
			logger.error(e.getMessage());
			return "json";
		}
		
		return "toOrderUpdate";
	}
	
	public String cancelOrder(){
		try {
			orderService.cancelOrder(id,txLogisticId,clientId);
		} catch (Exception e){
			logger.info(e.getMessage());
		}
		 
		return "toQueryAgain";
	}
	public String getCheckTab() {
		return checkTab;
	}
	public void setCheckTab(String checkTab) {
		this.checkTab = checkTab;
	}
	public String getSenderDetail() {
		return senderDetail;
	}
	public void setSenderDetail(String senderDetail) {
		this.senderDetail = senderDetail;
	}
	public String getSendTimeStart() {
		return sendTimeStart;
	}
	public void setSendTimeStart(String sendTimeStart) {
		this.sendTimeStart = sendTimeStart;
	}
	public String getSendTimeEnd() {
		return sendTimeEnd;
	}
	public void setSendTimeEnd(String sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}
	public Integer getTempId() {
		return tempId;
	}
	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	
}
