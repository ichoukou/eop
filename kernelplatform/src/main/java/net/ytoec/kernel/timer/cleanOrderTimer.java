package net.ytoec.kernel.timer;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.ProductService;
import net.ytoec.kernel.service.TraderInfoService;
import net.ytoec.kernel.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class cleanOrderTimer extends TimerTask {

    private static Logger                 logger = LoggerFactory.getLogger(cleanOrderTimer.class);
    private OrderService<Order>           orderService;

    private ProductService<Product>       productService;
    private TraderInfoService<TraderInfo> traderInfoService;

    private Integer                       limit;

    private Integer                       numday;

    private String                        cleanDate;

	private MailService<Mail> mailService;
    private String receiver = "yto_yitong1@163.com";
    /**
     * 邮件模版
     */
    private static String                     mailContentTemp   = "<HTML><HEAD><META HTTP-EQUIV=CONTENT-TYPE CONTENT=\"TEXT/HTML; CHARSET=UTF-8\"><STYLE>.tab-1{padding-left:32px; white-space:pre;}</STYLE><META CONTENT=\"MSHTML 6.00.2900.3492\" NAME=GENERATOR></HEAD>"
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
	public void run() {

    	try {
    		logger.info("开始加载删除订单----信息");
//    		System.out.print("-----------开始加载删除订单----信息----------------");

    		String date=DateUtil.toDay(new Date());
//    		String date="2011-11-21";
    		String startTime=null;
    		String endTime=null;
    		if(numday!=null){
    			if(numday == 1){
                    date = cleanDate;
    			}else if(numday>1){			
    			//numday天之内的重复数据 numday>1   备用
    				Date numdate=DateUtil.lastNumDate(new Date(), numday);
    				startTime=DateUtil.toDay(numdate);
    				endTime=date;
    			}
    		}
    		List<Order> orderlist = orderService.cleanOrderList(date, startTime, endTime, limit);
    		for (int i = 0; i < orderlist.size(); i++) {
    			Order order = (Order) orderlist.get(i);
    			orderService.delOrder(order);
    			logger.info("删除--"+order.getId()+"---"+order.getTxLogisticId()+"--信息---");
    		}
    		logger.info("删除重复订单数据==="+orderlist.size()*2+"-----------");
    		System.out.print("删除=="+date+"==重复订单数据==="+orderlist.size()*2+"-----------");
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("error...", e);
			StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("cleanOrderTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
		}


	}

    public OrderService<Order> getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService<Order> orderService) {
        this.orderService = orderService;
    }

    public ProductService<Product> getProductService() {
        return productService;
    }

    public void setProductService(ProductService<Product> productService) {
        this.productService = productService;
    }

    public TraderInfoService<TraderInfo> getTraderInfoService() {
        return traderInfoService;
    }

    public void setTraderInfoService(TraderInfoService<TraderInfo> traderInfoService) {
        this.traderInfoService = traderInfoService;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit == null || limit.intValue() == 0) {
            limit = 200;
        }
        this.limit = limit;
    }

    public Integer getNumday() {
        return numday;
    }

    public void setNumday(Integer numday) {
        this.numday = numday;
    }

    public String getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(String cleanDate) {
        this.cleanDate = cleanDate;
    }

}
