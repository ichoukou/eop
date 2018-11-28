/**
 * 
 */
package net.ytoec.kernel.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.RegionService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 订正 联系人表的数据。逻辑是这样的：检查ec_core_traderinfo表某条记录的numProv，如果等于0， 则根据prov的值到
 * region表找到他的id，把id的值赋给numProv，更新ec_core_traderinfo。 要求：1.region加载到缓存里，不要每次都去region去查 2.查询和更新ec_core_traderinfo
 * 表，要使用分区时间 3.每次查询不超过 5000条 4.timer启动时间 凌晨1点，每2分钟启动一次，到凌晨6点结束。
 * 
 * @author wangyong
 * @2011-12-30
 */
public class TraderProviceUpdate extends QuartzJobBean {

    private static final int              PAGENUM         = 5000;                                       // 每次查询记录数

    private TraderInfoService<TraderInfo> traderInfoService;

    private RegionService<Region>         regionService;

    private OrderService<Order>           orderService;

    // region缓存数据
    private static Map                    regionCache     = new HashMap();
    private MailService<Mail>             mailService;
    private String                        receiver        = "yto_yitong1@163.com";

    private List<TraderInfo>              traderInfos     = null;
    private Integer                       limit           = 5000;
    private static boolean                isRunning       = false;
    private static SimpleDateFormat       dateFormat      = new SimpleDateFormat("yyyy-MM-dd");
    private String                        startDay        = "2012-05-15";
    private Logger                        logger          = Logger.getLogger(TraderProviceUpdate.class);

    /**
     * 邮件模版
     */
    private static String                 mailContentTemp = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
                                                            + "<BODY STYLE=\"BORDER-WIDTH:0; MARGIN:12PX;\"><DIV><DIV STYLE=\"LINE-HEIGHT:1.7;COLOR:#000000;FONT-SIZE:14PX;FONT-FAMILY:ARIAL\">"
                                                            + "<DIV> ${mailContent}</DIV>"
                                                            + "<DIV>系统地址：<A HREF=\"http://ec.yto56.net.cn\" TARGET=\"_BLANK\">http://ec.yto56.net.cn</A></DIV>"
                                                            + "<BR>--------------------------------------------------------------------------------------"
                                                            + "</BODY></HTML>";

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public MailService<Mail> getMailService() {
        return mailService;
    }

    public void setMailService(MailService<Mail> mailService) {
        this.mailService = mailService;
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        try {
            if (!isRunning) {
                isRunning = true;

                // if(regionCache==null || regionCache.size()<=0){
                // setRegionCache();
                // }
                //
                // int totalTrader = traderInfoService.countTraderInfoTimerList();
                // for(int startIndex=0; startIndex<totalTrader; startIndex+=PAGENUM){
                // List<TraderInfo> traderInfoList = traderInfoService.traderInfoTimerList(startIndex, PAGENUM);
                // if(traderInfoList!=null && traderInfoList.size()>0){
                // for(TraderInfo trade : traderInfoList){
                // if(trade.getNumProv()==0){
                // TraderInfo traInfo = traderInfoService.getTradeBuId(trade.getId());
                // if(regionCache.get(trade.getProv())!=null){
                // traInfo.setNumProv((Integer)(regionCache.get(trade.getProv())));
                // traderInfoService.editTraderInfo(traInfo);
                // //增加一步操作，更新order表数据，触发solr数据的build
                // Order order = orderService.getOrderById(traInfo.getOrderId());
                // if(order.getRemark()!=null)
                // order.setRemark(order.getRemark());
                // orderService.updateOrder(order);
                // }
                // }
                // }
                // }
                // }
                //

                logger.error("进入时间器");
                Date date = new Date();

                Date updateProviceDate = (Date) Resource.proviceParam.get("updateProvice");

                if (updateProviceDate == null) {

                    updateProviceDate = dateFormat.parse(startDay);

                    Resource.proviceParam.put("updateProvice", updateProviceDate);
                }
                if (!DateUtil.compareDay(updateProviceDate, date)) {
                    isRunning = false;
                    return;
                }

                String proviceDate = DateUtil.format(updateProviceDate, "yyyy-MM-dd");
                String strId = (String) Resource.proviceParam.get("proviceId");

                if (strId == null) {
                    traderInfos = traderInfoService.getTraderInfo(proviceDate, limit);
                } else {
                    traderInfos = traderInfoService.getTraderInfoById(proviceDate,
                                                                      (Integer) Resource.proviceParam.get("traderId"),
                                                                      limit);
                }

                if (traderInfos.size() <= limit && !traderInfos.isEmpty()) {
                    logger.error("开始更新操作!");
                    traderInfoService.updateTraderInfo(traderInfos);

                }
                if (traderInfos.size() < limit) {
                    logger.error("======================此处修改时间=============================");
                    Resource.proviceParam.put("updateProvice", DateUtil.getDateBefore(updateProviceDate, 1));
                    traderInfos.clear();
                    isRunning = false;
                    return;
                }

                // List<TraderInfo> newTraderInfos = new ArrayList<TraderInfo>();
                //
                // for (int i = 0; i < traderInfos.size(); i++) {
                // newTraderInfos.add(newTraderInfos.get(i));
                // if ((i + 1) % limit == 0) {
                //
                // traderInfoService.updateTraderInfo(newTraderInfos);
                // newTraderInfos.clear();
                // }
                // }
                // if (newTraderInfos.size() > 0) {
                // logger.error("更新一条数据");
                // traderInfoService.updateTraderInfo(newTraderInfos);
                // newTraderInfos.clear();
                // }
                isRunning = false;

            }

        } catch (Exception e) {
            // TODO: handle exception
            StackTraceElement ex = e.getStackTrace()[0];
            Mail mail = new Mail();
            mail.setSubject("TraderProviceUpdate出现异常！");
            mail.setSendToMail(this.receiver);
            mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数："
                                                                      + ex.getLineNumber()));
            mailService.sendMail(mail);
        }

    }

    // /**
    // * 设置region缓存：缓存中保存region的key为‘regionName’，value为'id'值
    // */
    // @SuppressWarnings("unchecked")
    // private void setRegionCache(){
    // List<Region> regionList = regionService.getProvinceRegion();
    // if(regionList!=null && regionList.size()>0){
    // for(Region region:regionList){
    // regionCache.put(region.getRegionName().trim(), region.getId());
    // if(region.getRegionName().trim().equals("上海"))
    // regionCache.put("上海市", region.getId());
    // if(region.getRegionName().trim().equals("北京"))
    // regionCache.put("北京市", region.getId());
    // if(region.getRegionName().trim().equals("天津"))
    // regionCache.put("天津市", region.getId());
    // if(region.getRegionName().trim().equals("重庆"))
    // regionCache.put("重庆市", region.getId());
    // }
    // }
    // }

    public TraderInfoService<TraderInfo> getTraderInfoService() {
        return traderInfoService;
    }

    public void setTraderInfoService(TraderInfoService<TraderInfo> traderInfoService) {
        this.traderInfoService = traderInfoService;
    }

    public RegionService<Region> getRegionService() {
        return regionService;
    }

    public void setRegionService(RegionService<Region> regionService) {
        this.regionService = regionService;
    }

    public OrderService<Order> getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService<Order> orderService) {
        this.orderService = orderService;
    }

}
