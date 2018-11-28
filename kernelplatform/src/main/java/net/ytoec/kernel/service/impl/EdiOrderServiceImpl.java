/**
 * EdiOrderServiceImpl.java
 * Created at Sep 29, 2013
 * Created by kuiYang
 * Copyright (C) 2013 SHANGHAI YUANTONG XINGLONG E-Business, All rights reserved.
 */
package net.ytoec.kernel.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.ytoec.kernel.action.common.XmlSender;
import net.ytoec.kernel.action.remote.xml.Response;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.client.edi.SynOrderService;
import net.ytoec.kernel.client.util.DesUtils;
import net.ytoec.kernel.client.util.RsaUtils;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ApiLogDao;
import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dao.CoreTaskLogToTBDao;
import net.ytoec.kernel.dao.FailedTaskDao;
import net.ytoec.kernel.dao.JgWaybillDao;
import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dao.OrderLogDao;
import net.ytoec.kernel.dao.ProductDao;
import net.ytoec.kernel.dao.SendTaskToTBDao;
import net.ytoec.kernel.dao.TraderInfoDao;
import net.ytoec.kernel.dataobject.ApiLog;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.CoreTaskLogToTB;
import net.ytoec.kernel.dataobject.FailedTask;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dto.DeliveryInfoDTO;
import net.ytoec.kernel.dto.OrderGoodDTO;
import net.ytoec.kernel.dto.OrderJsonDTO;
import net.ytoec.kernel.search.dto.MailObjectDTO;
import net.ytoec.kernel.service.ConfigCodeService;
import net.ytoec.kernel.service.EdiOrderService;
import net.ytoec.kernel.util.DateUtil;
import net.ytoec.kernel.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * ClassName: EdiOrderServiceImpl
 * </p>
 * <p>
 * Description: 凡客对接业务实现类
 * </p>
 * <p>
 * Author: Kui.Yang
 * </p>
 * <p>
 * Date: Sep 29, 2013
 * </p>
 */
@Service
@Transactional
@SuppressWarnings("all")
public class EdiOrderServiceImpl implements EdiOrderService {

    /**
     * <p>
     * Field LOG: 日志
     * </p>
     */
    private final static Logger LOG = LoggerFactory.getLogger(EdiOrderServiceImpl.class);

    /**
     * <p>
     * Field WEBURL: WebService URL
     * </p>
     */
    private final static String WEBURL = "http://edi.wuliusys.com/SynOrderService.svc?wsdl";
    /**
     * <p>
     * Field LCID: 对接唯一标识
     * </p>
     */
    private final static String LCID = "f16e88fe-6344-47e1-8725-3b84a6542f67";

    /**
     * <p>
     * Field KEY: DESkey
     * </p>
     */
    private static final String KEY = "NUodWPeeBruDdf/TZZQG2ttr23bjSJkX";

    /**
     * <p>
     * Field TASKDESC: 失败任务代号
     * </p>
     */
    private final static String TASKVANCL = "5";

    /**
     * <p>
     * Field CUSTOMER_NO: 凡客客户编码
     * </p>
     */
    private final String CUSTOMER_NO = "VANCL"; // 凡客测试客户编码

    /**
     * <p>
     * Field synOrderService: WebService服务
     * </p>
     */
    private static SynOrderService synOrderService = new SynOrderService();

    /**
     * <p>
     * Field orderLogDao: 订单日志DAO
     * </p>
     */
    @Inject
    private OrderLogDao orderLogDao;

    /**
     * <p>
     * Field jgWaybillDao: 金刚DAO
     * </p>
     */
    @Inject
    private JgWaybillDao<JgWaybill> jgWaybillDao;

    /**
     * <p>
     * Field buildSearchDao: 搜索引擎DAO
     * </p>
     */
    @Inject
    private BuildSearchDao<BuildSearch> buildSearchDao;

    /**
     * <p>
     * Field orderDao: 订单DAO
     * </p>
     */
    @Inject
    private OrderDao<Order> orderDao;

    /**
     * <p>
     * Field traderDao: TraderDao
     * </p>
     */
    @Inject
    private TraderInfoDao<TraderInfo> traderDao;

    /**
     * <p>
     * Field productDao: 商品DAO
     * </p>
     */
    @Inject
    private ProductDao<Product> productDao;

    /**
     * <p>
     * Field dao: 任务数据接口
     * </p>
     */
    @Inject
    private SendTaskToTBDao<SendTaskToTB> sendTaskDao;

    /**
     * <p>
     * Field configCodeService: 配置文件Service
     * </p>
     */
    @Inject
    private ConfigCodeService<ConfigCode> configCodeService;

    /**
     * <p>
     * Field coreTaskLogDaoToTB: 发送日志DAO
     * </p>
     */
    @Inject
    private CoreTaskLogToTBDao<CoreTaskLogToTB> coreTaskLogDao;
    /**
     * <p>
     * Field failedTaskDao: 失败任务DAO
     * </p>
     */
    @Inject
    private FailedTaskDao<FailedTask> failedTaskDao;

    /**
     * <p>
     * Field apiLogDao: 日志记录
     * </p>
     */
    @Inject
    private ApiLogDao<ApiLog> apiLogDao;

    /**
     * <p>
     * Description: 获取服务
     * </p>
     * 
     * @return SynOrderService
     */
    public synchronized static SynOrderService getSingleInstance() {
        if (synOrderService != null) {
            return synOrderService;
        } else {
            return new SynOrderService();
        }
    }

    /**
     * <p>
     * Title: getOrders
     * </p>
     * <p>
     * Description: 获取订单
     * </p>
     * 
     * @param lcid 对接编号
     * @return 订单JSON格式订单拼接字符串
     * @see net.ytoec.kernel.service.SynOrderService#getOrders(java.lang.String)
     */
    @Override
    public String getOrders(String lcid) {
        ApiLog log = new ApiLog(); // 日志
        log.setCreateTime(new Date()); // 创建时间
        log.setLogType("VANCL_SERVICE!");
        long startTime = System.currentTimeMillis();
        if (StringUtils.isBlank(lcid)) {
            LOG.info("lcid is null!");
            log.setDescription("lcid is null!");
            return null;
        }
        // 拉取订单报文
        String orderTxt = getSingleInstance().getBasicHttpBindingISynOrderService().getOrders(lcid);
        LOG.info("VANCL" + orderTxt);
        // 报文解析,获取订单明文JSON串
        String plainText = orderTxt.substring(0, orderTxt.lastIndexOf(','));

        if (StringUtils.isBlank(plainText)) {
            LOG.error("orders is null!");
            log.setDescription("lcid is null!");
            return plainText;
        }

        String desKey = null;

        // 获取数字签名
        String signed = orderTxt.substring(orderTxt.lastIndexOf(',') + 1);
        LOG.error("VANCL_Signed" + signed);
        // 签名验证返回结果
        boolean flag = true;
        // 更新订单编号集合
        List<String> orderNoArray = new ArrayList<String>();

        try {
//            flag = RsaUtils.verify(plainText, signed);// 签名验证
            LOG.error("VANCL_Signed_RESULT:" + flag);
            if (flag) { // 签名验证通过
                JSONArray jsonArray = JSONArray.fromObject(plainText);
                Object[] objects = jsonArray.toArray();
                JSONArray retArray = new JSONArray();
                for (Object obj : objects) {
                    String address = null; // 收件人地址明文
                    String mobilePhone = null; // 收件人移动电话
                    ObjectMapper mapper = new ObjectMapper();
                    JSONObject jsonStr = JSONObject.fromObject(obj);
                    Object orderObj = null;

                    try {
                        if (jsonStr != null) {
                            desKey = KEY + objects.length + LCID; // 生成DESKEY
                            LOG.info("VANCL_DES_KEY:" + desKey);
                            DesUtils.setKeyValue(desKey);// 设置DES KEY 

                            // 订单数据转换
                            orderObj = mapper.readValue(jsonStr.toString(), OrderJsonDTO.class);
                            if (orderObj != null) { // 有效订单内容
                                OrderJsonDTO orderJsonDTO = (OrderJsonDTO) orderObj;
                                if (StringUtils.isNotBlank(orderJsonDTO.getAddress())) {
                                    // 获取收件人详细地址解密成明文
                                    address = DesUtils.decryptMode(orderJsonDTO.getAddress());
                                }
                                if (StringUtils.isNotBlank(orderJsonDTO.getMobilePhone())) {
                                    // 获取收件人联系电话
                                    mobilePhone = DesUtils.decryptMode(orderJsonDTO.getMobilePhone());
                                }
                                /* 插入订单信息 ，操作ec_core_order表 */
                                this.saveOrder(orderJsonDTO);
                                log.setDescription("VANCL_SAVE_ORDER_SUCCESS!:");
                                orderNoArray.add(orderJsonDTO.getOrderNO()); //  保存订单编号
                                DeliveryInfoDTO deliveryInfoDTO = new DeliveryInfoDTO(); // 入站订单反馈信息
                                deliveryInfoDTO.setOrderNo(orderJsonDTO.getOrderNO());
                                deliveryInfoDTO.setOperatorType(1);
                                retArray.add(deliveryInfoDTO); // 保存至JSON数组
                            }
                        }
                        log.setException(false);
                    } catch (JsonParseException e) {
                        LOG.error("error message:" + e.getMessage());
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        LOG.error("error message:" + e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        LOG.error("error message:" + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        mapper = null;
                        jsonStr = null;
                    }
                }

                String deliverInfo = retArray.toString(); // 入站订单
                String sign = RsaUtils.sign(deliverInfo);// 数字签名
                LOG.info("VANCl synchronous  sign:" + sign);
                deliverInfo = deliverInfo + "," + sign; // 发送给EDI的字符串
                LOG.info("VANCl synchronous  deliverInfo:" + deliverInfo);
                String message = this.commitDeliverInfo(LCID, deliverInfo); // 提交入站状态
                LOG.info("VANCL_提交入站信息:" + message);
                if (StringUtils.isBlank(message.split(",")[0])) {
                    LOG.info("VANCl synchronous  ORDERS SUCCESS"); // 订单入站成功
                } else {
                    LOG.info("VANCl synchronous  ORDERS FAILED"); // 订单入站失败
                    log.setDescription(message);
                    log.setException(true);
                }
                this.successOrders(lcid, orderNoArray); // 获取订单更新状态
                jsonArray = null;
                objects = null;
            } else {
                log.setException(true);
                log.setExceptionMsg("VANCl synchronous 签名不通过！");
            }
        } catch (NoSuchAlgorithmException e) {
            LOG.info("error message:" + e.getMessage());
            log.setException(true);
            log.setExceptionMsg(e.getMessage());
        } finally {
            if (log.isException()) {
                log.setUsedtime(System.currentTimeMillis() - startTime); // 记录执行时间
                this.apiLogDao.insertApiLog(log);
            }
            synOrderService = null;
            orderNoArray = null;
        }
        return plainText;
    }

    /**
     * <p>
     * Title: successOrders
     * </p>
     * <p>
     * Description: 获取订单更新状态
     * </p>
     * 
     * @param lcid 对接编号
     * @param orderNos 订单编号集合
     * @return 更新结果
     * @see net.ytoec.kernel.service.EdiOrderService#successOrders(java.lang.String,
     *      java.util.List)
     */
    @Override
    public boolean successOrders(String lcid, List<String> orderNos) {
        LOG.info("successOrders........begin");
        StringBuilder orderNoBuilder = new StringBuilder();
        for (String orderNo : orderNos) {
            LOG.info("VANCL_SAVE_ORDER_NO:" + orderNo);
            if (StringUtils.isNotBlank(orderNo)) {
                orderNoBuilder.append(orderNo);
                orderNoBuilder.append(",");
            }
        }
        String str = orderNoBuilder.toString();
        if (StringUtils.isBlank(str)) {
            return false;
        }
        String waybills = str.substring(0, str.lastIndexOf(","));
        return getSingleInstance().getBasicHttpBindingISynOrderService().successOrders(lcid, waybills);
    }

    /**
     * <p>
     * Title: commitDeliverInfo
     * </p>
     * <p>
     * Description: 提交配送结果
     * </p>
     * 
     * @param lcid 对接编号
     * @param deliverInfo 配送结果json字符串+RSA数字签名
     * @return 提交结果（null 值提交成功,否则提交失败）
     * @see net.ytoec.kernel.service.SynOrderService#commitDeliverInfo(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String commitDeliverInfo(String lcid, String deliverInfo) {
        LOG.info("VANCL commitDeliverInfo starting.................");
        return getSingleInstance().getBasicHttpBindingISynOrderService().commitDeliverInfo(lcid, deliverInfo);
    }

    /**
     * <p>
     * Description: 保存订单
     * </p>
     * 
     * @param source 订单数据传输类
     */
    private void saveOrder(OrderJsonDTO source) {
        LOG.info("VANCL  saveOrder........begin");
        if (source == null) {
            LOG.info(OrderJsonDTO.class.getName() + "is null!");
            return;
        }
        Order order = null; // 订单信息
        TraderInfo receiver = null; // 收获信息
        TraderInfo sender = null; // 发货信息
        List<Product> products = null; // 商品列表信息
        try {

            // 向 ec_core_order_log标准插入物流号，来判断是否重复
            LOG.info("step1");
            this.orderLogDao.addOrderLog(CUSTOMER_NO + source.getOrderNO());
            LOG.info("step2");
            receiver = getReceiver(source); // 获取收件信息
            order = getOrderEntity(source); // 获取订单信息
            sender = getSender(source); // 获取发件信息
            this.orderDao.addOrder(order); // 保存订单信息
            source.setOrderid(String.valueOf(order.getId())); // 设置订单ID
            this.traderDao.addTraderInfo(receiver); // 保存收件人信息
            this.saveProductList(source, order); // 保存商品信息
            this.saveBuildSearch(receiver, sender, order); // 保存订单至搜索引擎
            this.jgWaybillDao.addJgWaybill(getJgWayBill(order, sender, receiver)); // 保存订单信息至金刚扫描表
            LOG.info("VANCL saveOrder........success");

        } catch (DuplicateKeyException e) {
            LOG.error("create orderPrint fail ：" + CUSTOMER_NO + e.getMessage());
            LOG.error("create orderPrint fail 主键重复,物流号：" + CUSTOMER_NO + source.getOrderNO());
        } finally {
            order = null;
            receiver = null;
            sender = null;
            products = null;
        }
    }

    /**
     * <p>
     * Description: 获取发件信息
     * </p>
     * 
     * @param source 订单数据源
     * @return
     */
    private TraderInfo getSender(OrderJsonDTO source) {
        TraderInfo sender = new TraderInfo();
        sender.setName(source.getMerchantName()); // 商家全称
        sender.setTradeType(TraderInfo.SENDE_TYPE);//类型
        //        sender.setPostCode(source.getPostalCode());// 收货邮编
        //        sender.setPhone(source.getReceiveTel());//收货固话
        //        sender.setMobile(source.getMobilePhone());//收货手机
        //        sender.setProv(source.getProvince());//收货省
        //        sender.setCity(source.getCity());//收货城市
        //        sender.setAddress(source.getAddress());//收货地址
        //        sender.setTradeType(TraderInfo.RECEIVE_TYPE);//类型
        //        if(StringUtils.isNotBlank(source.getOrderid())){  //订单ID
        //            sender.setOrderId(Integer.parseInt(source.getOrderid()));
        //        }
        //        sender.setDistrict(source.getArea()); // 收货所在区
        //        sender.setPartitionDate(DateUtil.valueof(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        //        sender.setRemark(source.getRemark());
        //        int numRevProv = 0;
        //        if (sender.getProv().indexOf("省") > -1) {
        //            numRevProv = Resource.getCodeByName(sender.getProv().replace("省", ""));
        //        } else if (sender.getProv().indexOf("特别行政区") > -1) {
        //            numRevProv = Resource.getCodeByName(sender.getProv().replace("特别行政区", ""));
        //        } else {
        //            numRevProv = Resource.getCodeByName(sender.getProv());
        //        }
        //        sender.setNumProv(numRevProv);
        return sender;
    }

    /**
     * <p>
     * Description: 获取订单数据
     * </p>
     * 
     * @param source
     * @return
     */
    private Order getOrderEntity(OrderJsonDTO source) {
        Order order = new Order();
        order.setLogisticProviderId("YTO");//物流公司ID
        order.setTxLogisticId(CUSTOMER_NO + source.getOrderNO());
        order.setTradeNo("0");
        order.setCustomerId(CUSTOMER_NO);
        order.setMailNo(source.getDeliverCode());
        order.setFlag("0");
        order.setInsuranceValue(0.0F);//保价
        order.setClientId(CUSTOMER_NO);
        order.setStatus("0");
        if (StringUtils.isNotBlank(source.getWeight())) {
            order.setWeight(Float.parseFloat(source.getWeight()));
        }
        order.setSignPrice(0);
        order.setLineType("1");
        order.setWeightUpdateFlag("0");
        order.setOrderType(1);
        order.setServiceType(1L);
        order.setGoodsValue(0.0);
        order.setItemsValue(0.0);
        order.setTotalServiceFee(0.0);
        order.setCodSplitFee(0.0);
        order.setPartitionDate(DateUtil.valueof(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        order.setFreightType("1");
        order.setFreight(0.0);
        order.setTrimFreight(0.0);
        return order;
    }

    /**
     * <p>
     * Description: 保存信息至搜索引擎
     * </p>
     * 
     * @param receiver 收获信息
     * @param sender 发货信息
     * @param order 订单信息
     */
    private void saveBuildSearch(TraderInfo receiver, TraderInfo sender, Order order) {
        User user = Resource.getUserByCustomerId(CUSTOMER_NO);
        if (user != null) {
            MailObjectDTO traderinfo = new MailObjectDTO();
            traderinfo.setProv(receiver.getProv());
            traderinfo.setProvF(sender.getProv());
            traderinfo.setName(receiver.getName());
            traderinfo.setPhone(receiver.getPhone());
            traderinfo.setMobile(receiver.getMobile());
            traderinfo.setNumProv(receiver.getNumProv());
            traderinfo.setNumProvF(sender.getNumProv());
            traderinfo.setCity(receiver.getCity());
            traderinfo.setCityF(sender.getCity());
            traderinfo.setAddress(receiver.getAddress());
            traderinfo.setDistrict(receiver.getDistrict());
            BuildSearch buildSearch = setBuildSearch(order, traderinfo);
            buildSearch.setBuildTask("0");
            this.buildSearchDao.addBuildSearchByUpdateOrder(buildSearch);
        }
    }

    /**
     * <p>
     * Description: 保存订单商品信息
     * </p>
     * 
     * @param source 订单数据
     * @param order 订单
     */
    private void saveProductList(OrderJsonDTO source, Order order) {
        for (OrderGoodDTO good : source.getGoods()) {
            if (good != null) {
                Product product = new Product();
                product.setItemName(good.getProductName());
                if (StringUtils.isNotBlank(good.getProductCode())) {
                    product.setItemNumber(new Integer(good.getProductCode()));
                }
                product.setRemark(good.getRemark());
                product.setOrderId(order.getId());
                product.setItemValue(Double.valueOf(good.getSellPrice()));
                product.setLogisticId(order.getTxLogisticId());
                this.productDao.addProduct(product);
            }
        }
    }

    /**
     * <p>
     * Description: 获取金刚WayBill数据
     * </p>
     * 
     * @param order 订单信息
     * @param sender 发货信息
     * @param receiver 收货信息
     * @return WayBill数据
     */
    private JgWaybill getJgWayBill(Order order, TraderInfo sender, TraderInfo receiver) {
        JgWaybill jgWaybill = new JgWaybill();
        if (order.getServiceType() != null) {
            jgWaybill.setServiceType(String.valueOf(order.getServiceType()));
        }
        jgWaybill.setbAddress(receiver.getAddress());
        jgWaybill.setbCity(receiver.getCity());
        jgWaybill.setbDistrict(receiver.getDistrict());
        jgWaybill.setbMobile(receiver.getMobile());
        jgWaybill.setbName(receiver.getName());
        jgWaybill.setbPhone(receiver.getPhone());
        jgWaybill.setbPostCode(receiver.getPostCode());
        jgWaybill.setbProv(receiver.getProv());
        jgWaybill.setMailNo(order.getMailNo());
        jgWaybill.setOrderId(order.getId());
        jgWaybill.setsAddress(sender.getAddress());
        jgWaybill.setsCity(sender.getCity());
        jgWaybill.setsProv(sender.getProv());
        jgWaybill.setsPostCode(sender.getPostCode());
        jgWaybill.setsPhone(sender.getPhone());
        jgWaybill.setsName(sender.getName());
        jgWaybill.setsMobile(sender.getMobile());
        jgWaybill.setsDistrict(sender.getDistrict());
        jgWaybill.setLogisticId(order.getTxLogisticId());
        jgWaybill.setLineType(order.getLineType());
        jgWaybill.setClientID(order.getClientId());
        return jgWaybill;
    }

    /**
     * <p>
     * Description: 获取收件人信息
     * </p>
     * 
     * @param source EDI系统数据源
     * @return 收件人信息
     */
    private TraderInfo getReceiver(OrderJsonDTO source) {
        LOG.info("VANCL  getReceiver........begin");
        TraderInfo receiver = new TraderInfo();
        receiver.setName(source.getUserName()); // 收货人名称
        receiver.setPostCode(source.getPostalCode());// 收货邮编
        receiver.setPhone(source.getReceiveTel());//收货固话
        receiver.setMobile(source.getMobilePhone());//收货手机
        receiver.setProv(source.getProvince());//收货省
        receiver.setCity(source.getCity());//收货城市
        receiver.setAddress(source.getAddress());//收货地址
        receiver.setTradeType(TraderInfo.RECEIVE_TYPE);//类型
        if (StringUtils.isNotBlank(source.getOrderid())) { //订单ID
            receiver.setOrderId(Integer.parseInt(source.getOrderid()));
        }
        receiver.setDistrict(source.getArea()); // 收货所在区
        receiver.setPartitionDate(DateUtil.valueof(DateUtil.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
        receiver.setRemark(source.getRemark());
        int numRevProv = 0;
        if (receiver.getProv().indexOf("省") > -1) {
            numRevProv = Resource.getCodeByName(receiver.getProv().replace("省", ""));
        } else if (receiver.getProv().indexOf("特别行政区") > -1) {
            numRevProv = Resource.getCodeByName(receiver.getProv().replace("特别行政区", ""));
        } else {
            numRevProv = Resource.getCodeByName(receiver.getProv());
        }
        receiver.setNumProv(numRevProv);
        return receiver;
    }

    /**
     * <p>
     * Title: saveSendTaskToVancl
     * </p>
     * <p>
     * Description: 新增一条发送凡客的任务
     * </p>
     * 
     * @param sendTask
     * @return
     * @see net.ytoec.kernel.service.EdiOrderService#saveSendTaskToVancl(net.ytoec.kernel.dataobject.SendTaskToTB)
     */
    @Override
    public boolean saveSendTaskToVancl(SendTaskToTB sendTask) {
        LOG.info("VANCL  saveSendTaskToVancl........begin");
        try {
            sendTask.setRequestURL(WEBURL); // 设置URL
            this.sendTaskDao.addSendTaskToTB(sendTask);
        } catch (DataAccessException e) {
            LOG.error("error message:" + e.getMessage());
            return false;
        } finally {

        }
        return true;
    }

    /**
     * <p>
     * Title: finishedSendTaskToVancl
     * </p>
     * <p>
     * Description: 发送至凡客EDI成功将taskToTB删除，同时写入TASK_LOG表中
     * </p>
     * 
     * @param sendTask
     * @return
     * @see net.ytoec.kernel.service.EdiOrderService#finishedSendTaskToVancl(net.ytoec.kernel.dataobject.SendTaskToTB)
     */
    @Override
    public boolean finishedSendTaskToVancl(SendTaskToTB sendTask) {
        LOG.info("VANCL  finishedSendTaskToVancl........begin");
        try {
            this.sendTaskDao.removeSendTaskToTB(sendTask); // 移除发送成功的任务
            if (sendTask.getRemark() != null) { // 记录失败任务
                FailedTask failedTask = new FailedTask();
                failedTask.setFailedReason(sendTask.getRemark());
                failedTask.setRequestParams(sendTask.getRequestParams());
                failedTask.setRequestUrl(WEBURL);
                failedTask.setTaskDest(TASKVANCL);// 发给凡客
                failedTask.setTaskStatus("open");
                failedTask.setTxLogisticId(sendTask.getTxLogisticId());
                LOG.info(sendTask.getTxLogisticId() + "发送失败");
                this.failedTaskDao.addFailedTask(failedTask);
                LOG.info("VANCL  finishedSendTaskToVancl........failed");
            } else { // 记录任务日志
                CoreTaskLogToTB taskLog = new CoreTaskLogToTB();
                taskLog.setClientId(sendTask.getClientId());
                taskLog.setRequestURL(WEBURL);
                taskLog.setRequestTime(sendTask.getCreateTime());
                taskLog.setFailMessage(sendTask.getRemark());
                taskLog.setRequestParams(sendTask.getRequestParams());
                LOG.info(sendTask.getTxLogisticId() + "发送成功");
                //  测试库中由于表中没有分区字段会有报错的现象需要在生产环境上放开
                this.coreTaskLogDao.addCoreTaskLog(taskLog);
                LOG.info("VANCL  finishedSendTaskToVancl........success");
            }
        } catch (Exception e) {
            LOG.error(LogInfoEnum.PERSISTENCE_FAILED.toString(), e.toString());
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Title: updateSendTaskLastSendTime
     * </p>
     * <p>
     * Description: 更新taskToTB表，最后一 次发送时间，通常是执行任务失败时需要更新操作
     * </p>
     * 
     * @param sendTask
     * @return
     * @see net.ytoec.kernel.service.EdiOrderService#updateSendTaskLastSendTime(net.ytoec.kernel.dataobject.SendTaskToTB)
     */
    @Override
    public boolean updateSendTaskLastSendTime(SendTaskToTB sendTask) {
        return false;
    }

    /**
     * <p>
     * Title: batchAddSendTaskToVancl
     * </p>
     * <p>
     * Description: 批量添加发送至凡客任务记录
     * </p>
     * 
     * @param sendTasks
     * @return
     * @see net.ytoec.kernel.service.EdiOrderService#batchAddSendTaskToVancl(java.util.List)
     */
    @Override
    public boolean batchAddSendTaskToVancl(List<SendTaskToTB> sendTasks) {
        try {
            this.sendTaskDao.batchAddSendTask2TB(sendTasks);
        } catch (DataAccessException e) {
            LOG.error("receipt message:" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * <p>
     * Title: getSendTask
     * </p>
     * <p>
     * Description: 按照时间倒序，获取SendTask
     * </p>
     * 
     * @return
     * @see net.ytoec.kernel.service.EdiOrderService#getSendTask()
     */
    @Override
    public List<SendTaskToTB> getSendTask() {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("clientId", CUSTOMER_NO); // 获取凡客任务类型
        return this.sendTaskDao.getSendTaskByClientId(paramMap);
    }

    /**
     * <p>
     * Title: sendTaskToVancl
     * </p>
     * <p>
     * Description: 处理发送任务
     * </p>
     * 
     * @param sendTask
     * @return
     * @see net.ytoec.kernel.service.EdiOrderService#sendTaskToVancl(net.ytoec.kernel.dataobject.SendTaskToTB)
     */
    @Override
    public boolean sendTaskToVancl(SendTaskToTB sendTask) {
        // 易通表中若有推送到凡客的数据直接删除
        LOG.info("start send task: id--" + sendTask.getId() + "--");
        sendTask.setRemark(null);
        return this.finishedSendTaskToVancl(sendTask);
    }

    /**
     * <p>
     * Title: send
     * </p>
     * <p>
     * Description:发送配送结果
     * </p>
     * 
     * @return
     * @see net.ytoec.kernel.service.EdiOrderService#send()
     */
    @Override
    public boolean send() {
        String deliverInfo = null;
        String message = null;
        List<SendTaskToTB> source = null; // 推送任务集合
        ObjectMapper mapper = new ObjectMapper();
        try {
            source = this.getSendTask(); // 获取推送任务
            if (source != null && source.size() > 0) {
                LOG.info("VANCl synchronous  starting......................");
                JSONArray jsonArray = new JSONArray();
                for (SendTaskToTB taskVancl : source) {
                    JSONObject sendtaskJsonObject = setDeliverys(taskVancl);
                    if (StringUtils.isNotBlank(sendtaskJsonObject.toString())) {
                        jsonArray.add(sendtaskJsonObject);
                    } else {
                        jsonArray.add(new DeliveryInfoDTO());
                    }
                    deliverInfo = jsonArray.toString();
                    String sign = RsaUtils.sign(deliverInfo);// 数字签名
                    LOG.info("VANCl synchronous  sign:" + sign);
                    deliverInfo = deliverInfo + "," + sign; // 发送给EDI的字符串
                    LOG.info("VANCl synchronous  deliverInfo:" + deliverInfo);
                    message = commitDeliverInfo(LCID, deliverInfo); //调用发送服务获取操作结果
                    LOG.info("VANCl synchronous  Messages:" + message);
                    if (StringUtils.isBlank(message.split(",")[0])) {
                        this.sendTaskToVancl(taskVancl); // 删除发送完成的订单
                    }
                    LOG.info("VANCl synchronous  ERROR:" + message);
                }
                LOG.info("VANCl synchronous  end......................");

            }

        } catch (Exception e) {
            LOG.error("submit abnormal!error message:" + e.getMessage());
        } finally {
            LOG.error("receipt message:" + message);
        }
        return StringUtils.isNotBlank(message) ? false : true;
    }

    /**
     * <p>
     * Description: 提交配送结果消息反馈信息
     * </p>
     * 
     * @param messageJson
     * @return
     */
    private String returnMessage(String messageJson) {
        StringBuilder message = new StringBuilder();
        return message.toString();
    }

    /**
     * <p>
     * Description: 设置配送结果
     * </p>
     * 
     * @param taskVancl 当前任务
     */
    private JSONObject setDeliverys(SendTaskToTB taskVancl) {
        LOG.info("VANCl setDeliverys  starting......................");
        DeliveryInfoDTO deliveryInfoDTO = null;
        try {

            String xmlFragment = toXmlFragment(decode(taskVancl.getRequestParams(), XmlSender.UTF8_CHARSET));
            LOG.info("VANCl setDeliverys  xmlFragment======" + xmlFragment);
            UpdateWaybillInfo updateInfo = new UpdateWaybillInfo().toObject(xmlFragment);
            String orderNo = taskVancl.getTxLogisticId();
            String clientId = taskVancl.getClientId();
            String content = taskVancl.getRequestParams();
            deliveryInfoDTO = new DeliveryInfoDTO();
            deliveryInfoDTO.setOrderNo(orderNo.substring(clientId.length())); // 订单号

            String statusValue = "";

            if (StringUtils.isNotBlank(updateInfo.getInfoContent())) {
                statusValue = updateInfo.getInfoContent();
            } else {
                if (StringUtils.isNotBlank(content) && content.indexOf("<State>") > 0
                        && content.indexOf("</State>") > content.indexOf("<State>")) {
                    statusValue = content.substring(content.indexOf("<State>") + 7, content.indexOf("</State>"));
                }
            }

            //            deliveryInfoDTO.setOrderNo(orderNo); // 订单号
            int state = 1; // 操作类型(1=入站，2=分配配送员，3=妥投，5=拒收)
            if ("Buy02".equals(statusValue)) {
                state = 2;
            } else if ("Buy03".equals(statusValue)) {
                state = 3;
            } else if ("Buy05".equals(statusValue)) {
                state = 4;
            } else {
                state = 1;
            }
            LOG.info("VANCl setDeliverys  state======" + state);
            deliveryInfoDTO.setOperatorType(state);// 操作类型
            deliveryInfoDTO.setOrderType(0); // 0为普通订单，1为上门换货单，2为上门退货单，3为签单返回(这个业务3月份上线)
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("submit abnormal!error message:" + e.getMessage());
        }
        return deliveryInfoDTO != null ? JSONObject.fromObject(deliveryInfoDTO) : null;
    }

    /**
     * <p>
     * Description: 格式化为xml片段
     * </p>
     * 
     * @param content
     * @return
     */
    public String toXmlFragment(String content) {
        if (StringUtils.isBlank(content)) {
            LOG.info("VANCL toXmlFragment Error Message :Illegal Orders");
            return null;
        }
        //        String  result = content.substring(content.indexOf("<UpdateInfo>"), content.indexOf("</UpdateInfo>") + 1);
        LOG.info("VANCL toXmlFragment result:" + content);
        return content;
    }

    /**
     * <p>
     * Description: 使用指定的字符集charset对字符串arg进行解码,将解码后的字符串返回.
     * </p>
     * 
     * @param arg 待解码字符串.
     * @param charset
     * @return
     */
    private static String decode(String arg, String charset) {

        try {
            return java.net.URLDecoder.decode(arg, charset);
        } catch (UnsupportedEncodingException e) {
            LOG.error(LogInfoEnum.PARSE_INVALID.getValue(), e);
            throw new RuntimeException(e);
        }

    }

    /**
     * <p>
     * Description: 封装BulidSearch表对象
     * </p>
     * 
     * @param order 订单信息
     * @param traderinfo MailObjectDTO
     * @return BuildSearch
     */
    private BuildSearch setBuildSearch(Order order, MailObjectDTO traderinfo) {
        BuildSearch buildSearch = new BuildSearch();
        if (StringUtils.isBlank(order.getMailNo())) {
            buildSearch.setBuildTask("1");
        } else {
            buildSearch.setBuildTask("2");
            // 面单号设置的是旧的面单号
            if (order.getMailNo() != null) {
                buildSearch.setMailNo(order.getMailNo());
                LOG.info("mylog:mailno=" + order.getMailNo());
            } else
                buildSearch.setMailNo("");
        }

        if (order.getId() != null) {
            buildSearch.setOrderId(order.getId());
            LOG.info("mylog:orderid=" + order.getId());
        } else
            buildSearch.setOrderId(0);

        if (order.getStatus() != null) {
            buildSearch.setStatus(order.getStatus());
            LOG.info("mylog:status=" + order.getStatus());
        } else
            buildSearch.setStatus("");

        if (order.getCustomerId() != null) {
            buildSearch.setCustomerId(order.getCustomerId());
            LOG.info("mylog:CustomerId=" + order.getCustomerId());
        } else
            buildSearch.setCustomerId("");

        if (order.getOrderType() != null) {
            buildSearch.setOrderType(order.getOrderType());
            LOG.info("mylog:OrderType=" + order.getOrderType());
        } else
            buildSearch.setOrderType(0);

        if (order.getTxLogisticId() != null) {
            buildSearch.setTxLogisticId(order.getTxLogisticId());
            LOG.info("mylog:getTxLogisticId=" + order.getTxLogisticId());
        } else
            buildSearch.setTxLogisticId("");
        if (order.getTrimFreight() != null) {
            buildSearch.setTrimFreight(order.getTrimFreight());
            LOG.info("mylog:getTrimFreight=" + order.getTrimFreight());
        } else
            buildSearch.setTrimFreight(0);

        if (order.getFreightType() != null) {
            buildSearch.setFreightType(order.getFreightType());
            LOG.info("mylog:getFreightType=" + order.getFreightType());
        } else
            buildSearch.setFreightType("");

        if (order.getLineType() != null) {
            buildSearch.setLineType(order.getLineType());
            LOG.info("mylog:getLineType=" + order.getLineType());
        } else
            buildSearch.setLineType("");

        if (order.getType() != null) {
            buildSearch.setType(order.getType());
            LOG.info("mylog:getType=" + order.getType());
        } else
            buildSearch.setType("");

        if (order.getWeight() != null) {
            buildSearch.setWeight(order.getWeight());
            LOG.info("mylog:getWeight=" + order.getWeight());
        } else
            buildSearch.setWeight(0);

        if (order.getMailNo() != null) {
            buildSearch.setOldMailNo(order.getMailNo());
            LOG.info("mylog:getMailNo=" + order.getMailNo());
        } else
            buildSearch.setOldMailNo("");

        if (order.getType() != null) {
            buildSearch.setHoldTime(order.getType());
            LOG.info("mylog:getType=" + order.getType());
        } else
            buildSearch.setHoldTime("");

        if (order.getPartitionDate() != null) {
            buildSearch.setPartitiondate(order.getPartitionDate());
            LOG.info("mylog:setPartitiondate=" + order.getPartitionDate());
        } else
            buildSearch.setPartitiondate(new Date());
        if (order.getAcceptTime() != null) {
            buildSearch.setAcceptTime(order.getAcceptTime());
            LOG.info("mylog:getAcceptTime=" + order.getAcceptTime());
        } else
            buildSearch.setAcceptTime(new Date());

        if (traderinfo == null) {
            LOG.info("traderinfo===null==" + order.getId() + "=="
                    + DateUtil.format(order.getPartitionDate(), "yyyy-mm-dd"));
            return buildSearch;
        }
        if (traderinfo.getProv() != null) {
            buildSearch.setProv(traderinfo.getProv());
            LOG.info("mylog:prov=" + traderinfo.getProv());
        } else
            buildSearch.setProv("");

        if (traderinfo.getProvF() != null) {
            buildSearch.setProvF(traderinfo.getProvF());
            LOG.info("mylog:provF=" + traderinfo.getProvF());
        } else
            buildSearch.setProvF("");

        if (traderinfo.getName() != null) {
            buildSearch.setName(traderinfo.getName());
            LOG.info("mylog:Name=" + traderinfo.getName());
        } else
            buildSearch.setName("");

        if (traderinfo.getPhone() != null && !"".equals(traderinfo.getPhone())) {
            buildSearch.setPhone(traderinfo.getPhone());
            LOG.info("mylog:Phone=" + traderinfo.getPhone());
        } else
            buildSearch.setPhone("0");

        if (traderinfo.getMobile() != null) {
            buildSearch.setMobile(traderinfo.getMobile());
            LOG.info("mylog:getMobile=" + traderinfo.getMobile());
        } else
            buildSearch.setMobile("");

        if (traderinfo.getNumProvF() != null) {
            buildSearch.setNumProvF(traderinfo.getNumProvF());
            LOG.info("mylog:setNumProvF=" + traderinfo.getNumProvF());
        } else
            buildSearch.setNumProvF(0);

        if (traderinfo.getNumProv() > 0) {
            buildSearch.setNumProv(traderinfo.getNumProv());
            LOG.info("mylog:setNumProv=" + traderinfo.getNumProv());
        } else
            buildSearch.setNumProv(0);

        if (traderinfo.getCity() != null) {
            buildSearch.setCity(traderinfo.getCity());
            LOG.info("mylog:setCity=" + traderinfo.getCity());
        } else
            buildSearch.setCity("");

        if (traderinfo.getCityF() != null) {
            buildSearch.setCityF(traderinfo.getCityF());
            LOG.info("mylog:setCityF=" + traderinfo.getCityF());
        } else
            buildSearch.setCityF("");

        if (traderinfo.getAddress() != null) {
            buildSearch.setAddress(traderinfo.getAddress());
            LOG.info("mylog:getAddress=" + traderinfo.getAddress());
        } else
            buildSearch.setAddress("");

        if (traderinfo.getDistrict() != null) {
            buildSearch.setDistrict(traderinfo.getDistrict());
            LOG.info("mylog:getDistrict=" + traderinfo.getDistrict());
        } else
            buildSearch.setDistrict("");

        LOG.info("mylog:buildSearch date end");
        return buildSearch;
    }

}
