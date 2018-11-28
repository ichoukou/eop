package net.ytoec.kernel.action.remote;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.action.api.XMLUtils;
import net.ytoec.kernel.action.common.DocumentReader;
import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.process.IpWhiteListProcessor;
import net.ytoec.kernel.action.remote.process.ProcessorUtils;
import net.ytoec.kernel.action.remote.process.XmlBuildProcessor;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Constant;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.common.StatusEnum;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.SendTask;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.dto.GoodInfo;
import net.ytoec.kernel.dto.TabUniteOrder;
import net.ytoec.kernel.service.EdiOrderService;
import net.ytoec.kernel.service.JgWaybillService;
import net.ytoec.kernel.service.JgWaybillUpdateService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.SMSInfoService;
import net.ytoec.kernel.service.SMSPortService;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SendTaskService;
import net.ytoec.kernel.service.SendTaskToTBService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.service.ZebraForewarnService;
import net.ytoec.kernel.service.ZebraSurfacebillService;
import net.ytoec.kernel.util.ConfigUtilSingle;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opensymphony.xwork2.ActionContext;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 * 提供给TAOBAO的接口地址Action.
 * 
 */
@Controller
@Scope("prototype")
public class CommonOrderAction extends AbstractInterfaceAction {

    private static final long serialVersionUID = 4091305073349353650L;
    private static final String LOGISTICS_INTERFACE_PARAM = "logistics_interface";
    private static final String DATA_DIGEST_PARAM = "data_digest";
    private static final String CLIENT_ID_PARAM = "clientId";
    private static final String GET_METHOD_RESPONSE = "Success";
    private static final String TYPE = "type";
    private static final String YTOXLNEW = "YTOXLNEW";

    public static String ORDER_BIND = "CommonOrderBindSchema.xsd";
    public static String ORDER_CANCEL = "CommonOrderCancelSchema.xsd";
    public static String ORDER_CREATE = "CommonOrderCreateSchema.xsd";
    public static String ORDER_QUERY = "CommonOrderQuerySchema.xsd";

    private static String charset = "UTF-8";
    
    /**
     * <p>
     * Field ediOrderService: 凡客对接Service
     * </p>
     */
    @Autowired
    private EdiOrderService ediOrderService;

    /**
     * 通用金刚接口访问地址.
     */
    private static final String JIN_GANG_ORDER = "JINGANG.COMMON.ORDER";
    private static final String JIN_GANG_QOERY = "JINGANG.COMMON.QUERY";

    @Autowired
    private SendTaskService<SendTask> sendTaskService;
    @Autowired
    private JgWaybillService<JgWaybill> jgWaybillService;
    @Autowired
    private SendTaskToTBService<SendTaskToTB> sendTaskToTBService;

    @Autowired
    private OrderService<Order> orderService;

    @Autowired
    private TraderInfoService<TraderInfo> traderInfoService;

    @Autowired
    private SMSInfoService smsInfoService;

    @Autowired
    private SMSPortService smsPortService;

    @Autowired
    private SMSServiceService smsServiceService;

    @Autowired
    private JgWaybillUpdateService<JgWaybillUpdate> jgWaybillUpdateService;

    @Autowired
    private ZebraSurfacebillService zebraSurfacebillService;

    @Autowired
    private ZebraForewarnService<ZebraForewarn> zebraForewarnService;

    private static Logger logger = LoggerFactory
            .getLogger(CommonOrderAction.class);

    // 物流平台标识
    private String JGtxLogisticID = "0";

    // 物流平台标识
    private String VIPtxLogisticID = "0";

    private static String splitStr = "\01\10";

    // 日期格式化:年-月-日
    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd");

    // 面单号校验的正则表达式(电子面单单号规则号码为10位数字，且前两位不能是特殊字符)
    private static final String maiNoRegex = "^([a-z]|[A-Z]|[0-9]){2}[0-9]{8}$";

    // 规则匹配器
    private static final Pattern pattern = Pattern.compile(maiNoRegex);

    /**
     * 解析接口调用,电商调用金刚.
     * 
     * @return
     * @throws Exception
     */
    public String vipOrderServlet() throws Exception {

        // 商家代码
        StringBuilder customerCode = new StringBuilder();
        // 仓配通调用易通的订单上传接口时，传递的电子面单号
        List<Map<String, String>> waybillNos = new ArrayList<Map<String, String>>();

        logger.info("=======================commonOrderAction start=======================");
        if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
            super.print(GET_METHOD_RESPONSE);
            return null;
        }
        // String classpath = Thread.currentThread().getContextClassLoader()
        // .getResource("").toString();

        URL base = this.getClass().getResource("");
        String classpath = new File(base.getFile(), "../../../../../")
                .getCanonicalPath() + File.separator;

        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().startsWith("windows")) {
            classpath = classpath.replace("file:/", "");
        } else {
            classpath = classpath.replace("file:", "");
        }

        String ip = null;
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }

        // 1.获取参数值.
        String clientID = request.getParameter(CLIENT_ID_PARAM);
        String logisticsInterface = null;
        
        // 电商平台为YTOXLNEW的报文不做解码处理
        if(StringUtils.equalsIgnoreCase(YTOXLNEW, clientID)){
             logisticsInterface = 
                    request.getParameter(LOGISTICS_INTERFACE_PARAM);
        }else{
         logisticsInterface = decode(
                request.getParameter(LOGISTICS_INTERFACE_PARAM), charset);
        }
        
        String dataDigest = request.getParameter(DATA_DIGEST_PARAM);
        
        if (logisticsInterface == null || dataDigest == null
                || clientID == null) {
            print(Response.DATA_INSECURITY_RESPONSE_4.toXmlFragment());
            return null;
        }
        
//      String type = request.getParameter(TYPE);
        String type = getType(Resource.getLineType(clientID));
        type="online";
        if (StringUtils.isEmpty(type)) {
            type = "offline";
        }
        request.setAttribute(TYPE, type);
        
        String secretID = Resource.getSecretId(clientID);

        logger.debug("logisticsInterface-->" + logisticsInterface);
        logger.debug("dataDigest-->" + dataDigest);
        logger.debug("clientID-->" + clientID);
        logger.debug("type-->" + type);
        logger.debug("secretID-->" + secretID);
        logger.debug("ip:" + ip);
        /*
         * if ("offline".equals(type) && !ProcessorUtils.PROCESS_SWITCH) {
         * return null; }
         */

        
        boolean ipFlag = IpWhiteListProcessor.checkIp(clientID, ip);
        /*if (!ipFlag) {
            logger.error("非法用户访问！ip：" + ip + "  clientId:" + clientID);
            print("您好，此IP为非法IP，如有疑问请与管理员联系 QQ：2294882345！");
            return null;
        }*/

        if (request.getParameter("test") != null
                || StringUtils.equalsIgnoreCase("LYF", clientID)) {
            logisticsInterface = decode(logisticsInterface,
                    XmlSender.UTF8_CHARSET);
            dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
        }
        // liug编码---发送给金刚
        String jglogisticsInterface = null;
        String jgdataDigest = null;

        jglogisticsInterface = encode(logisticsInterface,
                XmlSender.UTF8_CHARSET);
        jgdataDigest = encode(dataDigest, XmlSender.UTF8_CHARSET);

        // 验证
        logger.debug("logisticsInterface===" + logisticsInterface);
        logger.debug("dataDigest===" + dataDigest);
        // 验证数字签名
        if (!validateData(logisticsInterface, dataDigest, secretID)) {
            print(Response.DATA_INSECURITY_RESPONSE.toXmlFragment());
            logger.error("数字签名验证失败!");
            logger.error(LogInfoEnum.PARAM_INVALID.getValue() + ",S02");
            return null;

        }

        String responseString = null;

        // 处理
        String xmlFragment = logisticsInterface;

        // 批量下单标记
        boolean isBatch = false;
        try {
            if (xmlFragment.contains("<RequestOrder")
                    && xmlFragment.contains("</RequestOrder>")) {
                String[] xmllist = xmlFragment.split(splitStr);
                if (xmllist.length > 1) {
                    isBatch = true;
                    // 批量处理
                    String reason = "";
                    for (int i = 0; i < xmllist.length; i++) {
                        if (!ProcessorUtils.validateXML(xmllist[i], classpath
                                + ORDER_CREATE)) {
                            reason += i + ",S01;";
                        } else {
                            String crtRs = batchOrderCreate(xmllist[i],
                                    waybillNos, customerCode);
                            if ("online".equals(type) && "".equals(crtRs)) {
                                SendTask sendTask = new SendTask();
                                sendTask.setOrderId(0);
                                sendTask.setClientId(clientID);
                                sendTask.setRemark("batchOrder");
                                sendTask.setTaskFlagId(getflagid(JGtxLogisticID));
                                sendTask.setTaskFlag(getflag(JGtxLogisticID));
                                sendTask.setTxLogisticId(JGtxLogisticID);
                                sendTask.setRequestURL(Resource
                                        .getChannel(JIN_GANG_ORDER));
                                sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM
                                        + "="
                                        + encode(xmllist[i],
                                                XmlSender.UTF8_CHARSET)
                                        + "&"
                                        + DATA_DIGEST_PARAM
                                        + "="
                                        + encode(
                                                Md5Encryption
                                                        .MD5Encode(xmllist[i]
                                                                + ConfigUtilSingle
                                                                        .getInstance()
                                                                        .getPARTERID_COMMON()),
                                                XmlSender.UTF8_CHARSET));
                                if (!this.sendTaskService.addSendTask(sendTask)) {
                                    reason += i + ",数据入库失败;";
                                }

                            } else {
                                reason += i + "," + crtRs + ";";
                            }
                        }
                    }
                    // 构造返回报文
                    Response batchResp = new Response();
                    if ("".equals(reason)) {
                        batchResp.setSuccess("true");
                        batchResp.setLogisticProviderId("YTO");
                        // batchResp.setTxLogisticId("all success");

                    } else {
                        batchResp.setSuccess("false");
                        batchResp.setReason(reason);
                        batchResp.setLogisticProviderId("YTO");
                        // batchResp.setTxLogisticId(txLogisticId);

                    }
                    responseString = batchResp.toXmlFragment();
                } else {
                    // 单条处理
                    if (!ProcessorUtils.validateXML(xmlFragment, classpath
                            + ORDER_CREATE)) {
                        print(Response.DATA_INSECURITY_RESPONSE_1
                                .toXmlFragment());
                        return null;
                    }
                    logger.error("======>orderCreate: xmlFragment="
                            + xmlFragment);
                    logger.error("======>orderCreate: waybillNos=" + waybillNos);
                    logger.error("======>orderCreate: customerCode="
                            + customerCode);
                    responseString = orderCreate(xmlFragment, waybillNos,
                            customerCode);
                    logger.error("======>orderCreate out");

                }

            } else if (xmlFragment.contains("<BatchQueryRequest")
                    && xmlFragment.contains("</BatchQueryRequest")) {
                if (!ProcessorUtils.validateXML(logisticsInterface, classpath
                        + ORDER_QUERY)) {
                    print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
                    return null;
                }

                // 判断查询用户是不是新龙vip用户，如果是就走新龙渠道(Y:是，N:不是)
                boolean xlVip = Resource.getQueryVip(clientID);
                if (xlVip) {
                    String xml = decode(jglogisticsInterface,
                            XmlSender.UTF8_CHARSET);

                    xml = xml.replace(">" + clientID + "<", ">YTOXL<");
                    jglogisticsInterface = encode(xml, XmlSender.UTF8_CHARSET);
                    String string = xml + Resource.getSecretId("YTOXL");
                    jgdataDigest = Md5Encryption.MD5Encode(string);
                    jgdataDigest = encode(jgdataDigest, XmlSender.UTF8_CHARSET);
                }
                responseString = orderSearch(jglogisticsInterface, jgdataDigest);
            } else if (xmlFragment.contains("<UpdateInfo")
                    && xmlFragment.contains("</UpdateInfo")
                    && xmlFragment.contains("INSTRUCTION")
                    && xmlFragment.contains("WITHDRAW")) {
                if (!ProcessorUtils.validateXML(logisticsInterface, classpath
                        + ORDER_CANCEL)) {
                    print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
                    return null;
                }
                responseString = orderCancel(xmlFragment);
            } else if (xmlFragment.contains("<UpdateInfo")
                    && xmlFragment.contains("</UpdateInfo")
                    && xmlFragment.contains("INSTRUCTION")
                    && xmlFragment.contains("UPDATE")) {
                if (!ProcessorUtils.validateXML(logisticsInterface, classpath
                        + ORDER_BIND)) {
                    print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
                    return null;
                }
                responseString = orderUpdate(xmlFragment);
            }
        } catch (Exception re) {
            logger.error(re.getMessage());
            responseString = Response.DATA_INSECURITY_RESPONSE_9
                    .toXmlFragment();
            print(responseString);
            return null;
        }
        if (responseString == null) {
            logger.error("order create failed:" + logisticsInterface);
            responseString = Response.DATA_INSECURITY_RESPONSE_9
                    .toXmlFragment();
            print(responseString);
            return null;
        }

        boolean flag = true;
        if ("online".equals(type)) {
            if (responseString.contains(Response.SUCCESS_TRUE)
                    && !responseString
                            .contains(BatchQueryResponse.BATCH_QUERY_RESPONSE_MARK)
                    && !isBatch) {
                SendTask sendTask = new SendTask();
                sendTask.setOrderId(0);
                sendTask.setClientId(clientID);
                sendTask.setRemark(responseString);
                sendTask.setTaskFlagId(getflagid(JGtxLogisticID));
                sendTask.setTaskFlag(getflag(JGtxLogisticID));
                sendTask.setTxLogisticId(JGtxLogisticID);
                sendTask.setRequestURL(Resource.getChannel(JIN_GANG_ORDER));
                sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
                        + jglogisticsInterface + "&" + DATA_DIGEST_PARAM + "="
                        + jgdataDigest);
                flag = this.sendTaskService.addSendTask(sendTask);
            }
        }

        logger.info("responseString:" + responseString);

        if (flag) {
            super.print(responseString);
        } else {
            super.print("false");
        }

        // 如果商家有预警设置，更新电子面单表，并检查预警
        ZebraForewarn zebraForewarn = zebraForewarnService
                .selectByCustomerCode(customerCode.toString());
        if (zebraForewarn != null && !waybillNos.isEmpty()) {
            // 1:批量更新打印状态
            logger.info("=======================更新电子面单打印状态 start=======================");
            zebraSurfacebillService.batchUpdatePrintState(waybillNos);
            logger.info("=======================更新电子面单打印状态 end=======================");
            // 2:电子面单不以8开头，可能是恶意面单，需要删除
            List<Map<String, String>> removeWaybills = new ArrayList<Map<String, String>>();
            String waybill = null;
            for (Map<String, String> waybillMap : waybillNos) {
                waybill = waybillMap.get("waybillNo");
                if (StringUtils.isNotEmpty(waybill) && !pattern.matcher(waybill).matches()) {
                    logger.info("removeWaybills start...");
                    removeWaybills.add(waybillMap);
                    logger.info("removeWaybills end...");
                }

            }
            waybillNos.removeAll(removeWaybills);
            // 3:批量插入电子面单号到面单回传表
            if (!waybillNos.isEmpty()) {
                logger.error("batchInsert start...");
                zebraSurfacebillService.addBatch(waybillNos,
                        customerCode.toString());
                logger.error("batchInsert end...");
            }
        }
        logger.info("=======================commonOrderAction end=======================");

        return null;
    }

    // 订单创建.
    private String orderCreate(String xmlFragment,
            List<Map<String, String>> waybillNos, StringBuilder customerCode)
            throws Exception {
        OrderFormInfo requestOrder = new OrderFormInfo().toObject(xmlFragment);
        requestOrder.getOrderType();

        // 添加电子面单号(即：运单号)
        Map<String, String> map = new HashMap<String, String>();
        // 对面单进行表示式校验
        String maillNo = requestOrder.getMailNo();
        if (StringUtils.isNotEmpty(maillNo)) {
            Matcher matcher = pattern.matcher(maillNo);
            boolean isMatch = matcher.matches();
            if (!isMatch) {// 单号没有通过表达式校验
                requestOrder.getResponse().setTxLogisticId(
                        requestOrder.getTxLogisticId());
                requestOrder.getResponse().setLogisticProviderId(
                        requestOrder.getLogisticProviderId());
                requestOrder.getResponse().setSuccess("false");
                requestOrder.getResponse().setReason("S01");

                return requestOrder.getResponse().toXmlFragment();
            }
        }
        map.put("waybillNo", maillNo);
        map.put("orderNo", requestOrder.getTxLogisticId());
        waybillNos.add(map);
        // 设置商家代码
        customerCode.append(requestOrder.getCustomerId());

        TraderInfo traderInfo = requestOrder.getSender();

        logger.error("=====>customerCode:" + customerCode.toString());

        TraderInfo receiver = requestOrder.getReceiver();
        // logger.error("orderCreate-" + traderInfo);

        boolean isPrint = Resource.getIsPrint(requestOrder.getClientId());// 判断是否面单打印
        if (StringUtils.isEmpty(requestOrder.getLogisticProviderId())
                || StringUtils.isEmpty(requestOrder.getTxLogisticId())
                || StringUtils.isEmpty(traderInfo.getName())
                || StringUtils.isEmpty(traderInfo.getAddress())
                || StringUtils.isEmpty(traderInfo.getPostCode())
                || (StringUtils.isEmpty(traderInfo.getMobile()) && StringUtils
                        .isEmpty(traderInfo.getPhone()))
                || StringUtils.isEmpty(traderInfo.getCity())
                || StringUtils.isEmpty(traderInfo.getProv())
                || (StringUtils.isEmpty(receiver.getMobile()) && StringUtils
                        .isEmpty(receiver.getPhone()))
                || ("offline".equals(request.getAttribute(TYPE))
                        && StringUtils.isEmpty(requestOrder.getMailNo()) && true != isPrint)) {
            requestOrder.getResponse().setTxLogisticId(
                    requestOrder.getTxLogisticId());
            requestOrder.getResponse().setLogisticProviderId(
                    requestOrder.getLogisticProviderId());
            requestOrder.getResponse().setSuccess("false");
            requestOrder.getResponse().setReason("S05");

            return requestOrder.getResponse().toXmlFragment();
        }

        logger.error("=====>requestOrder.setLineType");
        // 线上下单
        // 通过serviceType来判
        if ("online".equals(request.getAttribute(TYPE))){
            requestOrder.setLineType(Order.ONLINE_TYPE);
        }else{
            requestOrder.setLineType(Order.OFFLINE_TYPE);
        }

        logger.error("=====>requestOrder.setClientId");
        requestOrder.setClientId(request.getParameter(CLIENT_ID_PARAM));

        boolean flag = true;

        try {
            requestOrder.setStatus("2");
            logger.error("orderService.addOrderFormInfo in");
            try {
                requestOrder
                        .setPartitiondate(sdf.parse(sdf.format(new Date())));
            } catch (ParseException e) {
                logger.error("parse date exception", e);
            }
            flag = this.orderService.addOrderFormInfo(requestOrder);
            logger.error("orderService.addOrderFormInfo out");
            logger.error("flag:" + flag);
        } catch (DuplicateKeyException de) {
            logger.error("物流号不能重复！物流号：" + requestOrder.getTxLogisticId());
        } catch (Exception e) {
            logger.error("order create exception", e);
            flag = false;
        }

        requestOrder.getResponse().setTxLogisticId(
                requestOrder.getTxLogisticId());
        requestOrder.getResponse().setLogisticProviderId(
                requestOrder.getLogisticProviderId());
        if (flag) {
            logger.debug("order create success");
            requestOrder.getResponse().setSuccess("true");
            requestOrder.getResponse().setReason("Order Create Success");
        } else {
            logger.debug("order create failure");
            requestOrder.getResponse().setSuccess("false");
            requestOrder.getResponse().setReason("数据入库失败");
        }

        JGtxLogisticID = requestOrder.getTxLogisticId();
        logger.error("JGtxLogisticID：" + JGtxLogisticID);

        return requestOrder.getResponse().toXmlFragment();
    }

    // 订单创建.
    private String batchOrderCreate(String xmlFragment,
            List<Map<String, String>> waybillNos, StringBuilder customerCode)
            throws Exception {
        OrderFormInfo requestOrder = new OrderFormInfo().toObject(xmlFragment);

        // 添加电子面单号(即：运单号)
        Map<String, String> map = new HashMap<String, String>();
        map.put("waybillNo", requestOrder.getMailNo());
        map.put("orderNo", requestOrder.getTxLogisticId());
        waybillNos.add(map);
        // 设置商家代码
        customerCode.append(requestOrder.getCustomerId());

        TraderInfo traderInfo = requestOrder.getSender();

        logger.debug("batchorderCreate" + traderInfo);

        TraderInfo receiver = requestOrder.getReceiver();
        logger.debug("batchorderCreate-" + traderInfo);

        if (StringUtils.isEmpty(requestOrder.getLogisticProviderId())
                || StringUtils.isEmpty(requestOrder.getTxLogisticId())
                || StringUtils.isEmpty(traderInfo.getName())
                || StringUtils.isEmpty(traderInfo.getAddress())
                || StringUtils.isEmpty(traderInfo.getPostCode())
                || (StringUtils.isEmpty(traderInfo.getMobile()) && StringUtils
                        .isEmpty(traderInfo.getPhone()))
                || StringUtils.isEmpty(traderInfo.getCity())
                || StringUtils.isEmpty(traderInfo.getProv())
                || (StringUtils.isEmpty(receiver.getMobile()) && StringUtils
                        .isEmpty(receiver.getPhone()))
                || ("offline".equals(request.getAttribute(TYPE)) && StringUtils
                        .isEmpty(requestOrder.getMailNo()))) {
            return "S05";

        }
        // 线上下单
        // 通过serviceType来判
        if ("online".equals(request.getAttribute(TYPE)))
            requestOrder.setLineType(Order.ONLINE_TYPE);
        else if ("offline".equals(request.getAttribute(TYPE)))
            requestOrder.setLineType(Order.OFFLINE_TYPE);
        else
            requestOrder.setLineType((String) request.getAttribute(TYPE));

        requestOrder.setClientId(request.getParameter(CLIENT_ID_PARAM));
        boolean flag = true;
        try {
            requestOrder.setStatus("2");
            flag = this.orderService.addOrderFormInfo(requestOrder);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Order Create Exception";
        }

        // 插入数据库
        if (flag) {
            logger.debug("batchOrderCreate" + "订单创建成功"
                    + requestOrder.getTxLogisticId());
        } else {
            logger.debug("batchOrderCreate" + "订单创建失败"
                    + requestOrder.getTxLogisticId());
            return "Order Create failed";
        }

        JGtxLogisticID = requestOrder.getTxLogisticId();
        return "";
    }

    /**
     * 订单查询. 对于订单查询操作,系统不做实际的查询处理,直接将此请求转发给金刚系统,并由从金刚得到的响应结果返回给电商.
     * 此操作对于电商、核心平台、金刚三者是一个同步操作.
     * 
     * @param logisticsInterface
     * @param dataDigest
     * @return 返回响应信息.
     * @throws Exception
     */
    private String orderSearch(String logisticsInterface, String dataDigest)
            throws Exception {
        String url = Resource.getChannel(JIN_GANG_QOERY);
        XmlSender xmlSender = new XmlSender();
        xmlSender.setUrlString("http://jingang.yto56.com.cn/ordws/TaoBao14Servlet");
        xmlSender.setRequestMethod(XmlSender.POST_REQUEST_METHOD);
        xmlSender.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
                + logisticsInterface + "&" + DATA_DIGEST_PARAM + "="
                + dataDigest);
        return xmlSender.send();
    }

    /**
     * 订单查询
     * 
     * @param logisticsInterface
     * @param dataDigest
     * @return 返回响应信息.
     * @throws Exception
     */
    public List<QueryOrder> ordersSearch(String[] logisticsId) throws Exception {
        if (logisticsId == null || logisticsId.length < 1)
            return null;
        List<QueryOrder> orderList = null;
        try {
            // 构建XML
            String logisticsInterface = "";
            String dataDigest = "";

            StringBuilder sb = new StringBuilder();
            sb.append("<BatchQueryRequest><logisticProviderID>YTO</logisticProviderID><orders>");
            for (int i = 0; i < logisticsId.length; i++) {
                sb.append("<order><mailNo>");
                sb.append(logisticsId[i]);
                sb.append("</mailNo></order>");
            }
            sb.append("</orders></BatchQueryRequest>");
            logisticsInterface = sb.toString();
            logger.debug(logisticsInterface);
            dataDigest = Md5Encryption.MD5Encode(logisticsInterface
                    + ConfigUtilSingle.getInstance().getPARTERID_COMMON());
            String utf8LogisticsInterface = encode(logisticsInterface, "UTF-8");
            String utf8DataDigest = encode(dataDigest, "UTF-8");

            // 发送查询请求
            String xmlFragment = orderSearch(utf8LogisticsInterface,
                    utf8DataDigest);
            // 去除相应报文里的空格
            if (xmlFragment != null)
                xmlFragment = xmlFragment.replace(" ", "");
            logger.debug(xmlFragment);
            // 解析XML
            InputStream inputStream = ProcessorUtils
                    .getInputStream(xmlFragment);
            DocumentReader documentReader = new DocumentReader();
            Document document = documentReader.getDocument(inputStream);
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            orderList = new ArrayList<QueryOrder>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if ("orders".equals(node.getNodeName())) {
                    NodeList order = node.getChildNodes();
                    logger.debug("****" + order.getLength());
                    for (int j = 0; j < order.getLength(); j++) {
                        // QueryOrder(order.item(j).getTextContent());
                        QueryOrder queryOrder = new QueryOrder(order.item(j));
                        orderList.add(queryOrder);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orderList;
    }

    // 订单取消.
    private String orderCancel(String xmlFragment) throws Exception {
        UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
                .toObject(xmlFragment);

        logger.debug("订单取消");

        // ADD---MGL
        orderService.updateOrderMailNoByLogisticIdAndClientId(
                updateInfo.getTxLogisticId(),
                request.getParameter(CLIENT_ID_PARAM), updateInfo.getMailNo(),
                updateInfo.getInfoContent());

        updateInfo.getResponse().setTxLogisticId(updateInfo.getTxLogisticId());
        updateInfo.getResponse().setLogisticProviderId(
                updateInfo.getLogisticProviderId());
        updateInfo.getResponse().setSuccess("true");
        updateInfo.getResponse().setReason("orderCancel Success");

        JGtxLogisticID = updateInfo.getTxLogisticId();
        String toXmlFragment = updateInfo.getResponse().toXmlFragment();
        return toXmlFragment;
    }

    // 订单更新.
    private String orderUpdate(String xmlFragment) throws Exception {
        UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
                .toObject(xmlFragment);

        logger.debug("订单更新");
        // ADD---MGL
        orderService.updateOrderMailNoByLogisticIdAndClientId(
                updateInfo.getTxLogisticId(),
                request.getParameter(CLIENT_ID_PARAM), updateInfo.getMailNo(),
                updateInfo.getInfoContent());
        // }

        if ("offline".equals(request.getParameter(TYPE))) {
            JgWaybillUpdate jgWaybillUpdate = new JgWaybillUpdate();
            jgWaybillUpdate.setLogisticId(updateInfo.getTxLogisticId());
            jgWaybillUpdate.setMailNo(updateInfo.getMailNo());
            jgWaybillUpdate.setClientID(request.getParameter(CLIENT_ID_PARAM));
            jgWaybillUpdateService.addJgWaybillUpdate(jgWaybillUpdate);
        }
        updateInfo.getResponse().setTxLogisticId(updateInfo.getTxLogisticId());
        updateInfo.getResponse().setLogisticProviderId(
                updateInfo.getLogisticProviderId());
        updateInfo.getResponse().setSuccess("true");
        updateInfo.getResponse().setReason("orderUpdate Success");

        JGtxLogisticID = updateInfo.getTxLogisticId();
        String toXmlFragment = updateInfo.getResponse().toXmlFragment();
        return toXmlFragment;
    }

    /**
     * 验证参数值.
     * 
     * @param logisticsInterface
     * 
     * @param dataDigest
     * @return
     */
    private static boolean validateData(String logisticsInterface,
            String dataDigest, String parternId) {
        String newDataDigest = Md5Encryption.MD5Encode(logisticsInterface
                + parternId);
        logger.debug("dataDigest:" + dataDigest);
        logger.debug("newDataDigest:" + newDataDigest);
        if (dataDigest.equals(newDataDigest)) {
            return true;
        }
        return false;

    }

    /**
     * 使用指定的字符集charset对字符串arg进行解码,将解码后的字符串返回.
     * 
     * @param arg
     *            待解码字符串.
     * @param charset
     * @return
     */
    private static String decode(String arg, String charset) {

        try {
            return java.net.URLDecoder.decode(arg, charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
            throw new RuntimeException(e);
        }

    }

    /**
     * 使用指定的字符集charset对字符串arg进行编码,将编码后的字符串返回.
     * 
     * @param arg
     *            待编码字符串.
     * @param charset
     * @return
     */
    private static String encode(String arg, String charset) {
        try {
            return java.net.URLEncoder.encode(arg, charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
            throw new RuntimeException(e);
        }
    }

    private static int getflagid(String arg) {
        String s = arg;
        // String s="klllll123456";
        s = s.substring(s.length() - 1);
        logger.debug(s);
        return Integer.parseInt(s);

    }

    private static String getflag(String arg) {
        String sid = arg;
        // String s="klllll123456";
        sid = sid.substring(sid.length() - 1);
        String s = sid;
        // 按照对应关系取配置文件
        /*
         * if(sid){ s= }
         */
        logger.debug(s);
        return s;

    }

    /**
     * 解析接口调用,金刚调用淘宝接口.
     * 
     * @return
     * @throws Exception
     */
    public String kingGang2TaoBao() throws Exception {
        logger.debug("into kingGang2common");
        if (XmlSender.GET_REQUEST_METHOD.equals(request.getMethod())) {
            super.print(GET_METHOD_RESPONSE);
            return null;
        }

        logger.debug("kinggang2common" + super.request.getRemoteAddr() + ","
                + super.request.getRemoteHost() + "---------------"
                + System.getProperty("file.encoding") + "---------------"
                + super.request.getCharacterEncoding() + "---------------");

        // // 1.获取参数值.
        String logisticsInterface = super.request
                .getParameter(LOGISTICS_INTERFACE_PARAM);// 明文
        String dataDigest = super.request.getParameter(DATA_DIGEST_PARAM);// 密文
        String clientId = super.request.getParameter(CLIENT_ID_PARAM);// 客户ID
        String type = super.request.getParameter(TYPE);// 线上线下参数
        String url = Resource.getChannel(clientId);// 推送给客户的URL地址
        String secretId = Resource.getSecretId(clientId);// 密钥
        logger.error("logisticsInterface-->" + logisticsInterface);
        logger.error("dataDigest-->" + dataDigest);
        logger.error("clientId-->" + clientId);
        logger.error("url-->" + url);

        if (logisticsInterface == null || dataDigest == null
                || clientId == null || type == null) {
            print(Response.DATA_INSECURITY_RESPONSE_5.toXmlFragment());
            return null;
        }

        // liug编码
        String dslogisticsInterface = null;
        String dsdataDigest = null;
        if (request.getParameter("test") != null) {
            dslogisticsInterface = logisticsInterface;
            dsdataDigest = dataDigest;
        } else {
            dslogisticsInterface = encode(logisticsInterface,
                    XmlSender.UTF8_CHARSET);
            dsdataDigest = encode(dataDigest, XmlSender.UTF8_CHARSET);
        }

        // 2.转码
        if (request.getParameter("test") != null) {
            logisticsInterface = decode(logisticsInterface,
                    XmlSender.UTF8_CHARSET);
            dataDigest = decode(dataDigest, XmlSender.UTF8_CHARSET);
        }

        // 3.验证

        if (!validateData(logisticsInterface, dataDigest, secretId)) {
            logger.error("数字签名验证失败!");
            print(Response.DATA_INSECURITY_RESPONSE.toXmlFragment());
            return null;

        }
        logger.debug("验证通过!");
        String xmlFragment = logisticsInterface;
        UpdateWaybillInfo updateInfo = new UpdateWaybillInfo()
                .toObject(xmlFragment);

        if (StringUtils.isEmpty(updateInfo.getTxLogisticId())
                || StringUtils.isEmpty(updateInfo.getInfoType())
                || StringUtils.isEmpty(updateInfo.getInfoContent())
                || StringUtils.isEmpty(updateInfo.getLogisticProviderId())
                || ("SIGNED".equals(updateInfo.getInfoContent().toUpperCase())
                        && StringUtils.isEmpty(updateInfo.getAcceptTime()) && StringUtils
                        .isEmpty(updateInfo.getMailNo()))) {
            logger.error("为空判断验证失败!");
            print(Response.DATA_INSECURITY_RESPONSE_1.toXmlFragment());
            return null;
        }

        updateInfo.getResponse().setLogisticProviderId(
                updateInfo.getLogisticProviderId());
        updateInfo.getResponse().setSuccess("true");
        updateInfo.getResponse().setTxLogisticId(updateInfo.getTxLogisticId()); //
        updateInfo.setClientId(request.getParameter(CLIENT_ID_PARAM));

        VIPtxLogisticID = updateInfo.getTxLogisticId();

        // 将SendTask存到数据库表中.
        SendTaskToTB sendTask = new SendTaskToTB();
        // 保存为淘宝等其它电商平台的访问地址,需要根据clientID查询到对应的Url访问地址.
        sendTask.setClientId(clientId);
        sendTask.setRequestURL(url);// url预读是为了防止服务启动时缓存未加载导致url取不到为空
        sendTask.setOrderId(0);
        // liug
        sendTask.setRemark(updateInfo.getResponse().toXmlFragment());
        sendTask.setTaskFlagId(getflagid(VIPtxLogisticID));
        sendTask.setTaskFlag(getflag(VIPtxLogisticID));
        sendTask.setTxLogisticId(VIPtxLogisticID);

        //判断clientId，如果是"YIXUN"，则单独拼易迅需要的参数
        if(clientId.equals("YIXUN")){
            String yixunParams = createYixunParamStr(updateInfo);
            sendTask.setRequestParams(yixunParams);
        }else if ("VANCL".equals(clientId)) {  // Last modified by Kui.Yang on 2013-10-10
            //判断clientId，如果是"VANCL"，则单独拼凡客需要的参数
            String vanclxml = createVanclXml(updateInfo);
            sendTask.setRequestParams(vanclxml);
        }else{
            sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
                + dslogisticsInterface + "&" + DATA_DIGEST_PARAM + "="
                + dsdataDigest + "&type=" + type);
        }
        
        try {
            boolean flag = true;
            // if (Resource.verifyDispatch(clientId)) {
            if (Resource.getIsSend(clientId)) {
                // Last modified by Kui.Yang on 2013-10-10
                if ("VANCL".equals(sendTask.getClientId())) {
                    // 保存任务
                    flag = this.ediOrderService.saveSendTaskToVancl(sendTask);
                } else if (StringUtils.isNotEmpty(sendTask.getRequestParams())) {
                    flag = this.sendTaskToTBService.addSendTaskToTB(sendTask);
                }
            }
            if (flag) {
                super.print(updateInfo.getResponse().toXmlFragment());
            } else {
                super.print("<Response><logisticProviderID></logisticProviderID><txLogisticID></txLogisticID><success>false</success></Response>");
            }
            logger.debug("已存入任务表!");
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            super.print("<Response><logisticProviderID></logisticProviderID><txLogisticID></txLogisticID><success>false</success></Response>");
        }

        // 当揽收成功，或者失败的时候，将揽收时间记录到order表中的type字段中。
        if (StringUtils.equals("GOT", updateInfo.getInfoContent())
                || StringUtils.equals("NOT_SEND", updateInfo.getInfoContent())) {
            updateInfo.setType(updateInfo.getAcceptTime());
        }

        try {
            orderService.orderStatusNodify(updateInfo);
        } catch (Exception e) {
            logger.error(LogInfoEnum.STATE_EXCEPTION.getValue(), e);
        }
        logger.debug("核心数据更新完毕!");

        // 揽收，派送扫描，签收 可能需要给买家发短信哦
        if (StringUtils.equalsIgnoreCase(StatusEnum.GOT.getName(),
                updateInfo.getInfoContent())
                || StringUtils.equalsIgnoreCase(StatusEnum.SENT_SCAN.getName(),
                        updateInfo.getInfoContent())
                || StringUtils.equalsIgnoreCase(StatusEnum.SIGNED.getName(),
                        updateInfo.getInfoContent())) {
        	
        	if(clientId.equals("YIXUN")){
        		return null;
        	}

            String status = updateInfo.getInfoContent(); // 状态
            String mailNo = updateInfo.getMailNo(); // 运单号
            String txLogisticId = updateInfo.getTxLogisticId();
            if (StringUtils.isEmpty(status) || StringUtils.isEmpty(mailNo)
                    || StringUtils.isEmpty(txLogisticId)) {
                logger.error("订单状态、物流号、运单号不能为空！");
                return null;
            }
            // 通过物流号，获取订单
            Order order = orderService.getOrderByLogisticId(txLogisticId);
            if (order == null || order.getId() == null) {
                return null;
            }
            // 通过订单的customer_id 获取订单对应的用户
            User user = Resource.getUserByCustomerId(order.getCustomerId());
            if (user == null || user.getId() == null) {
                return null;
            }

            String childType = user.getChildType();
            Map<String, Object> map = new HashMap<String, Object>();

            if (StringUtils.equalsIgnoreCase("B", childType)) {
                if (StringUtils.isNotBlank(user.getUserSource())) { // 若：B:分仓
                                                                    // C:企业
                                                                    // 的平台ID为NULL
                                                                    // ,则使用自身ID,注意：此情况实际属于,错误数据
                    map.put("userId", Integer.parseInt(user.getUserSource()));
                } else {
                    map.put("userId", user.getId());
                }
            } else {
                Integer parentId = user.getParentId();
                if (parentId != null) { // 主ID,或子ID
                    map.put("userId", parentId);
                } else {
                    map.put("userId", user.getId());
                }
            }

            map.put("status", StringUtils.upperCase(status));
            map.put("mailNo", mailNo);
            map.put("txLogisticId", txLogisticId);
            map.put("createUserId", user.getId());

            // 判断订单用户，是否有短信可发。 且判断服务是否开通
            if (smsPortService.supplyStatusOpenUp(user.getId())
                    && smsServiceService.isOpeanService(user.getId(), status)) {
                TraderInfo traderInfo = traderInfoService
                        .getTraderInfoByIdAndByTraderType(order.getId(), 1);// 1为收件人
                String buyMobile = traderInfo.getMobile();// 收件人手机号
                String buyName = traderInfo.getName();// 收件人姓名

                logger.error(" smsInfoService.saveInfoToQueue(map);"
                        + map.toString());
                if (!StringUtils.isEmpty(buyMobile)) {
                    map.put("buyMobile", buyMobile);
                    map.put("buyName", buyName);
                    logger.error(" START  --->   smsInfoService.saveInfoToQueue(map);"
                            + map.toString());
                    smsInfoService.saveInfoToQueue(map);
                    logger.error(" END --> smsInfoService.saveInfoToQueue(map);");
                }
            } else {
                logger.error("未能。。。。通过 判断订单用户，是否有短信可发。  且判断服务是否开通  订单物流号："
                        + txLogisticId + "用户ID：" + user.getId());
            }

        }

        return null;
    }

    // private Map isOpen(Map<String, Object> params) {
    // Map<String,Object> map = new HashMap<String,Object>();
    // map.put("userId", params.get("sellerId"));
    // String name = "";
    // String status = (String)params.get("status");
    // if("GOT".equals(status))name="发货提醒";
    // if("SENT_SCAN".equals(status))name="派件提醒";
    // if("SIGNED".equals(status))name="签收提醒";
    // map.put("name", name);
    //
    // return map;
    // }

    /**
     * 根据金刚回传的订单状态，拼出易迅用的xml串
     */
    private String createYixunParamStr(UpdateWaybillInfo updateInfo) throws Exception{
        StringBuffer sb = new StringBuffer();	//原始串
        StringBuffer sb1 = new StringBuffer();	//中文做u8编码后的串
        //易迅apiKey保存在ec_core_channel_info表中，以channel_key表示
        String apiKey = Resource.getYixunApikey(updateInfo.getClientId());
        sb.append("apikey="+apiKey);
        sb1.append("apikey="+apiKey);
        sb.append("&format=xml");
        sb1.append("&format=xml");
        
        //易迅Secret保存在ec_core_channel_info表中，以parternId表示
        String secret = Resource.getSecretId(updateInfo.getClientId());
        
        String clientId = updateInfo.getClientId();
        String txLogisId = updateInfo.getTxLogisticId();
        int index = txLogisId.indexOf(clientId);
        String realLogis = txLogisId.substring(index+clientId.length());
        
        String acceptTime = updateInfo.getAcceptTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr = sdf.format(sdf.parse(acceptTime));
        
        String status = updateInfo.getInfoContent();
        if(status.equals(StatusEnum.GOT.getName()) || status.equals(StatusEnum.SENT_SCAN.getName())){
            sb.append("&method=reportabnormal");
            sb.append("&packageno="+realLogis);
            sb1.append("&method=reportabnormal");
            sb1.append("&packageno="+realLogis);
            if(status.equals(StatusEnum.GOT.getName())){
                sb.append("&type=已取货&desc=已取货");
                sb1.append("&type="+encode("已取货",XmlSender.UTF8_CHARSET));
                sb1.append("&desc="+encode("已取货",XmlSender.UTF8_CHARSET));
            }else{
                sb.append("&type=配送中&desc=配送中");
                sb1.append("&type="+encode("配送中",XmlSender.UTF8_CHARSET));
                sb1.append("&desc="+encode("配送中",XmlSender.UTF8_CHARSET));
            }
            sb.append("&createtime="+datestr);
            sb1.append("&createtime="+datestr);
        }else if(status.equals(StatusEnum.SIGNED.getName()) || status.equals(StatusEnum.FAILED.getName())){
            sb.append("&method=reportsignstatus");
            sb.append("&packageno="+realLogis);
            sb1.append("&method=reportsignstatus");
            sb1.append("&packageno="+realLogis);
            if(status.equals(StatusEnum.SIGNED.getName())){
                sb.append("&status=1");
                sb1.append("&status=1");
            }else{
                sb.append("&status=2");
                sb1.append("&status=2");
            }
            sb.append("&paysysno=1&amt=0&note=");
            sb.append("&signtime="+datestr);
            sb1.append("&paysysno=1&amt=0&note=");
            sb1.append("&signtime="+datestr);
        }
        String statusXml = createStatusXml(updateInfo,apiKey,realLogis,0);
        String statusXmlEncode = createStatusXml(updateInfo,apiKey,realLogis,1);
        logger.error("====易迅状态xml:"+statusXml);
        if(StringUtils.isEmpty(statusXml)){
            return "";
        }else{
            sb.append("&logicdata="+statusXml);
            sb1.append("&logicdata="+statusXmlEncode);
        }

        //参数按首字母排序
        String sortParams = createSortParams(sb.toString());
        
        //获得参数签名字符串
        String signStr = createSign(sortParams,secret);
        
        //第二次参数排序
		String sortParams2 = createSortParams(sb1.toString());
        
        String retstr = sortParams2+"&sign="+signStr;
        logger.error("====向易迅发送订单配送状态字符串:"+retstr);
        return retstr;
    }

    /**
     * URL参数按照首字母排序
     * @param string
     * @return
     */
    private String createSortParams(String params) {
        String [] paramArr = params.split("&");
        Arrays.sort(paramArr);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<paramArr.length;i++){
            String [] arr = paramArr[i].split("=");
            if(arr.length == 2){
                if(i == (paramArr.length-1)){
                    sb.append(paramArr[i]);
                }else{
                    sb.append(paramArr[i]+"&");
                }   
            }   
        }
        return sb.toString();
    }

    /**
     * 拼出参数签名字符串
     * @param string
     * @param secret
     * @return
     */
    private String createSign(String params, String secret) {
        StringBuffer sb = new StringBuffer();
        String [] paramArr = params.split("&");
        for(int i=0;i<paramArr.length;i++){
            String [] arr = paramArr[i].split("=");
            sb.append(arr[0]);
            sb.append(arr[1]);
        }
        sb.append(secret);
        String retStr = Md5Encryption.MD5(sb.toString()).toLowerCase();
        return retStr;
    }

    /**
     * 拼出订单状态xml串
     * @param updateInfo
     * @param apiKey 
     * @param realLogis 
     * @param flag 是否需要url编码（针对中文处理）
     * @return
     */
    private String createStatusXml(UpdateWaybillInfo updateInfo, String apiKey, String realLogis,int flag) {
        try{
            StringBuffer sb = new StringBuffer();
            sb.append("<UpdateInfo>");
            sb.append("<LogisticProviderID>");
            sb.append(apiKey);
            sb.append("</LogisticProviderID>");
            sb.append("<OrderStates>");
            sb.append("<OrderState>");
            sb.append("<TxLogisitcId>");
            sb.append(realLogis);
            sb.append("</TxLogisitcId>");
            sb.append("<State>");
            String status = updateInfo.getInfoContent();
            if(status.equals(StatusEnum.GOT.getName())){
                sb.append("Buy01");
            }else if(status.equals(StatusEnum.SENT_SCAN.getName())){
                sb.append("Buy02");
            }else if(status.equals(StatusEnum.SIGNED.getName())){
                sb.append("Buy03");
            }else if(status.equals(StatusEnum.FAILED.getName())){
                sb.append("Buy05");
            }else{
                return "";
            }
            sb.append("</State>");
            sb.append("<StateTime>");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datestr = sdf.format(sdf.parse(updateInfo.getAcceptTime()));
            sb.append(datestr);
            sb.append("</StateTime>");
            sb.append("<OperName>圆通快递</OperName>");
            sb.append("<OperateTime>"+datestr+"</OperateTime>");
            sb.append("<OperatorUnit>圆通快递</OperatorUnit>");
            sb.append("<DeliveryMan></DeliveryMan>");
            sb.append("<DeliveryMobile></DeliveryMobile>");
            sb.append("<Reason></Reason>");
            sb.append("<Remark></Remark>");
            sb.append("</OrderState>");
            sb.append("</OrderStates>");
            sb.append("</UpdateInfo>");
            
            String result = null;
            if(flag == 0){
            	result = sb.toString();
            }else{
            	result = encode(sb.toString(),XmlSender.UTF8_CHARSET);
            } 
            return result;
        }catch(Exception e){
        	logger.error("====易迅xml错误:"+e.getMessage());
            return "";
        }
    }

    public boolean sendPrintOrders(List<JgWaybill> jgWaybill) {
        // 存入jgwaybill表
        if (jgWaybillService.addJgWaybills(jgWaybill)) {
            // 生成电商通知task记录
            for (JgWaybill jb : jgWaybill) {
                String key = Resource.getSecretId(jb.getClientID());
                String dslogisticsInterface = XmlBuildProcessor
                        .getOrderUpdateXML(jb);
                String dsdataDigest = Md5Encryption
                        .MD5Encode(dslogisticsInterface + key);
                dslogisticsInterface = encode(dslogisticsInterface,
                        XmlSender.UTF8_CHARSET);
                dsdataDigest = encode(dsdataDigest, XmlSender.UTF8_CHARSET);
                // 将SendTask存到数据库表中.
                SendTaskToTB sendTask = new SendTaskToTB();
                sendTask.setClientId(jb.getClientID());
                sendTask.setRequestURL(Resource.getChannel(jb.getClientID()));
                sendTask.setOrderId(0);
                sendTask.setRemark("订单打印,面单更新通知");
                sendTask.setTaskFlagId(getflagid(VIPtxLogisticID));
                sendTask.setTaskFlag(getflag(VIPtxLogisticID));
                sendTask.setTxLogisticId(VIPtxLogisticID);
                sendTask.setRequestParams(LOGISTICS_INTERFACE_PARAM + "="
                        + dslogisticsInterface + "&" + DATA_DIGEST_PARAM + "="
                        + dsdataDigest);
                try {
                    if (!this.sendTaskToTBService.addSendTaskToTB(sendTask))
                        return false;
                    logger.debug("打印订单已存入任务表:" + sendTask.getId());
                } catch (Exception e) {
                    logger.debug("打印订单存任务表失败!");
                    logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 易迅订单上传.
     * 
     * @return
     * @throws Exception
     */
    public String yixunOrderServlet() throws Exception {
        logger.info("============进入易迅订单导入接口=============");
        
        ActionContext context = ActionContext.getContext();  
        HttpServletRequest request = (HttpServletRequest) context.get(ServletActionContext.HTTP_REQUEST);
        
        String logisticsInterface = decode(request.getParameter(LOGISTICS_INTERFACE_PARAM),charset);
        String dataDigest = request.getParameter(DATA_DIGEST_PARAM);
        String clientID = request.getParameter(CLIENT_ID_PARAM);
        
        logger.info("=====易迅订单：[logisticsInterface]:"+logisticsInterface+",[dataDigest]:"+dataDigest+",[clientID]:"+clientID);
        
        /******需要配置易迅商家订单上传密钥,存入channel_info表的parternId字段****/
        String secretID = Resource.getSecretId(clientID);
        logger.info("=====易迅订单,内存中读取上传密钥："+secretID);
        
        /******需要金刚系统配置易迅ClientId****/
        String clientCode = "YIXUN";    
        String xml = null;
        
        // 检查参数
        if (logisticsInterface == null || dataDigest == null || clientID == null) {
            logger.info("=====易迅订单："+Constant.MSG_PARAM_EMPTY);
            xml = responseStr("false",Constant.MSG_PARAM_EMPTY);
            printxml(xml);
            return null;
        }
        
        // 检查clientId
        if(!clientID.equals(clientCode)){
            logger.info("=====易迅订单："+Constant.MSG_CUSCODE_ERROR);
            xml = responseStr("false",Constant.MSG_CUSCODE_ERROR);
            printxml(xml);
            return null;
        }
        
        // 检查特殊字符
        String regex = "[^!@#$%&*……￥]{1,}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(logisticsInterface);
        if(!m.matches()){
            logger.info("=====易迅订单："+Constant.MSG_XML_CHAR_ERROR);
            xml = responseStr("false",Constant.MSG_XML_CHAR_ERROR);
            printxml(xml);
            return null;
        }
        
        // 验证数字签名
        boolean validateData = validateData(logisticsInterface,dataDigest,secretID);
        if (!validateData) {
            logger.info("=====易迅订单："+Constant.MSG_DATADIGEST_FAIL);
            xml = responseStr("false",Constant.MSG_DATADIGEST_FAIL);
            printxml(xml);
            return null;
        }
        
        //解析订单字符串
        XMLUtils xMLUtils = new XMLUtils(TabUniteOrder.class);
        TabUniteOrder obj = xMLUtils.toObject(logisticsInterface);
        
        /***********主要数据非空验证*******/
        //1、物流订单号
        if(StringUtils.isEmpty(obj.getTxLogisitcId())){
            logger.info("=====易迅订单："+Constant.MSG_LOGIS_EMPTY);
            xml = responseStr("false",Constant.MSG_LOGIS_EMPTY);
            printxml(xml);
            return null;
        }
        //2、面单号
        List<String> subPags = obj.getSubPackages();    
        //list为空
        if(subPags == null || subPags.size() == 0){
            logger.info("=====易迅订单："+Constant.MSG_BILL_NODE_EMPTY);
            xml = responseStr("false",Constant.MSG_BILL_NODE_EMPTY);
            printxml(xml);
            return null;
        }
        String waybill = subPags.get(0);
        
        //检查面单是否为空
        if(StringUtils.isEmpty(waybill)){
            logger.info("=====易迅订单："+Constant.MSG_BILL_EMPTY);
            xml = responseStr("false",Constant.MSG_BILL_EMPTY);
            printxml(xml);
            return null;
        }
        
        //检查面单号长度是否符合要求
        if(waybill.length() > 64){
            logger.info("=====易迅订单："+Constant.MSG_BILL_LEN_MORE);
            xml = responseStr("false",Constant.MSG_BILL_LEN_MORE);
            printxml(xml);
            return null;
        }
        
        //检查面单号格式是否符合要求
        Matcher matcher = pattern.matcher(waybill);
        boolean isMatch = matcher.matches();
        if (!isMatch) {// 单号没有通过表达式校验
            logger.info("=====易迅订单："+Constant.MSG_BILL_FORMAT_ERROR);
            xml = responseStr("false",Constant.MSG_BILL_FORMAT_ERROR);
            printxml(xml);
            return null;
        }
        
        /***********数据长度校验********/
        //1、物流订单号
        String tempLogis = clientCode+obj.getTxLogisitcId();
        if(tempLogis.length() > 50){
            logger.info("=====易迅订单："+Constant.MSG_LOGIS_LEN_MORE);
            xml = responseStr("false",Constant.MSG_LOGIS_LEN_MORE);
            printxml(xml);
            return null;
        }
        //2、收件人和发件人信息
        if(obj.getSendName().length() > 200         || 
            obj.getSendPostCode().length() > 200    || 
            obj.getSendPhone().length() > 200       || 
            obj.getSendMobile().length() > 200      || 
            obj.getSendProv().length() > 200        || 
            obj.getSendCity().length() > 200        || 
            obj.getSendAddress().length() > 200     || 
            obj.getSendCounty().length() > 200      || 
            obj.getReceiveName().length() > 200     || 
            obj.getReceivePostCode().length() > 200 || 
            obj.getReceivePhone().length() > 200    || 
            obj.getReceiveProv().length() > 200     || 
            obj.getReceiveCity().length() > 200     || 
            obj.getReceiveAddress().length() > 200  || 
            obj.getReceiveCounty().length() > 200){
            logger.info("=====易迅订单："+Constant.MSG_TRADER_LEN_MORE);
            xml = responseStr("false",Constant.MSG_TRADER_LEN_MORE);
            printxml(xml);
            return null;
        }
        //3、产品信息
        List<GoodInfo> goods = obj.getGoods();
        for(GoodInfo good:goods){
            if(good.getGoodName().length() > 200 || 
                good.getRemark().length() > 200){
                logger.info("=====易迅订单："+Constant.MSG_PRODUCT_LEN_MORE);
                xml = responseStr("false",Constant.MSG_PRODUCT_LEN_MORE);
                printxml(xml);
                return null;
            }   
        }

        //保存订单信息
        int saveFlag = orderService.addOrderForYixun(obj, clientCode);
        if(saveFlag == Constant.FLAG_ORDER_SAVE_SUCCESS){
            xml = responseStr("true",Constant.MSG_ORDER_SAVE_SUCCESS);
        }else if(saveFlag == Constant.FLAG_ORDER_SAVE_DUPLICATE){
            xml = responseStr("false",Constant.MSG_ORDER_SAVE_DUPLICATE);
        }else if(saveFlag == Constant.FLAG_ORDER_SAVE_DBERROR){
            xml = responseStr("false",Constant.MSG_ORDER_SAVE_DBERROR);
        }else if(saveFlag == Constant.FLAG_SENDER_SAVE_DBERROR){
            xml = responseStr("false",Constant.MSG_SENDER_SAVE_DBERROR);
        }else if(saveFlag == Constant.FLAG_RECEIVE_SAVE_DBERROR){
            xml = responseStr("false",Constant.MSG_RECEIVE_SAVE_DBERROR);
        }else if(saveFlag == Constant.FLAG_PRODUCT_SAVE_DBERROR){
            xml = responseStr("false",Constant.MSG_PRODUCT_SAVE_DBERROR);
        }else if(saveFlag == Constant.FLAG_SEARCH_SAVE_DBERROR){
            xml = responseStr("false",Constant.MSG_SEARCH_SAVE_DBERROR);
        }else if(saveFlag == Constant.FLAG_JGWAYBILL_SAVE_DBERROR){
            xml = responseStr("false",Constant.MSG_JGWAYBILL_SAVE_DBERROR);
        }else{
            xml = responseStr("false",Constant.MSG_UNKONWN_EXCEPTION);
        }
        printxml(xml);  
        logger.info("============离开易迅订单导入接口=============");
        return null;
    }
    
    private String responseStr(String flag,String message){
        StringBuffer sb = new StringBuffer();
        sb.append("<Response>");
        sb.append("<success>");
        sb.append(flag);
        sb.append("</success>");
        sb.append("<message>");
        sb.append(message);
        sb.append("</message>");
        sb.append("</Response>");
        return sb.toString();
    }
    
    /**
     * <p>
     * Description: 根据金刚回传的订单状态，拼出凡客用的xml串
     * </p>
     * 
     * @param updateInfo 源数据
     * @return
     * @throws Exception
     */
    private String createVanclXml(UpdateWaybillInfo updateInfo) throws Exception {
        //凡客apiKey保存在ec_core_channel_info表中，以channel_value表示
        String apiKey = Resource.getYixunApikey(updateInfo.getClientId());
        String clientId = updateInfo.getClientId(); // 客户编号
        String txLogisId = updateInfo.getTxLogisticId(); // 物流订单号
        int index = txLogisId.indexOf(clientId);
        String realLogis = txLogisId.substring(index + clientId.length()); // 订单号
        String acceptTime = updateInfo.getAcceptTime();
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("<UpdateInfo>");
            sb.append("<LogisticProviderID>");
            sb.append(apiKey);
            sb.append("</LogisticProviderID>");
            sb.append("<OrderStates>");
            sb.append("<OrderState>");
            sb.append("<TxLogisitcId>");
            sb.append(realLogis);
            sb.append("</TxLogisitcId>");
            sb.append("<State>");
            String status = updateInfo.getInfoContent();
            if (status.equals(StatusEnum.GOT.getName())) {
                sb.append("Buy01");
            } else if (status.equals(StatusEnum.SENT_SCAN.getName())) {
                sb.append("Buy02");
            } else if (status.equals(StatusEnum.SIGNED.getName())) {
                sb.append("Buy03");
            } else if (status.equals(StatusEnum.FAILED.getName())) {
                sb.append("Buy05");
            } else {
                return "";
            }
            sb.append("</State>");
            sb.append("<StateTime>");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datestr = sdf.format(sdf.parse(acceptTime));
            sb.append(datestr);
            sb.append("</StateTime>");
            sb.append("<OperName>圆通快递</OperName>");
            sb.append("<OperateTime>" + datestr + "</OperateTime>");
            sb.append("<OperatorUnit>圆通快递</OperatorUnit>");
            sb.append("<DeliveryMan></DeliveryMan>");
            sb.append("<DeliveryMobile></DeliveryMobile>");
            sb.append("<Reason></Reason>");
            sb.append("<Remark></Remark>");
            sb.append("</OrderState>");
            sb.append("</OrderStates>");
            sb.append("</UpdateInfo>");
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    private static String getType(String lineType) {

		if (StringUtils.equals("0", lineType)) {
			return "online";
		} else if (StringUtils.equals("1", lineType)) {
			return "offline";
		}
		return "offline";
	}
}
