/**
 * Feb 21, 201210:30:53 AM
 * wangyong
 */
package net.ytoec.kernel.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.dataobject.MailTendency;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.MailTendencyService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * 网点运单走势图中数据来源<br>
 * 插入卖家当天的运单入运单走势表。每天凌晨启动该时间器，更新凌晨前24小时内的运单。
 * @author wangyong
 * Feb 21, 2012
 */
@SuppressWarnings("all")
public class TendencyTimer extends QuartzJobBean {
	
	private static Logger logger=Logger.getLogger(TendencyTimer.class);
	
	private OrderService<Order> orderService;
	
	private UserService<User> userService;
	
	private MailTendencyService<MailTendency> mailTendencyService;
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
	/* (non-Javadoc)
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			/**
			 * 获取要查询的时间：当前时间一般是夜里凌晨，则获取之前的哪一天
			 */
	        String yesterday = DateUtil.dateArithmetic(new Date(), 1);
			/**
			 * 获取所有网点用户,并获取每个网点下的买家的运单，然后加入运单走势表中
			 */
			List<User> siteUser = userService.getUserListByUserType("2");
			if(siteUser != null && siteUser.size()>0){
				for(User site : siteUser){
					//获取某个网点下的卖家用户
					List<User> userList = userService.searchUsersBySiteAndUserType(site.getSite(), "1");
					/**
					 * 添加当前网点导入的订单；导入的订单的customerId是网点主键id
					 * 首先判断下该网点导入的订单是否存在，存在的话就删除，以下会重新统计
					 */
					List<MailTendency> siteTendencyList = mailTendencyService.getMailTendencyBySiteUserTime(site.getId(), site.getId(), yesterday);
					if(siteTendencyList!=null && siteTendencyList.size()>0){
						logger.info("网点："+site.getId()+"导入的运单量已经存在，将会删除");
						for(MailTendency obj : siteTendencyList){
							mailTendencyService.removeMailTendecy(obj);
						}
					}
					logger.info("开始统计网点："+site.getId()+"导入的运单量");
					List<String> siteCustomerId = new ArrayList<String>();
					siteCustomerId.add(site.getId().toString());
					int siteMailNoNum = orderService.countCurrentDayOrderByCustormerId(siteCustomerId, yesterday);
					//如果该网点导入的运单数目不为0，则插入一条数据到运单走势表中
					if(siteMailNoNum!=0){
						MailTendency tendency = new MailTendency();
						tendency.setSiteId(site.getId());
						tendency.setUserId(site.getId());
						tendency.setMailNoNum(siteMailNoNum);
						try {
							tendency.setMailNoCreateTime(stringToDate(yesterday));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						boolean flag = mailTendencyService.addMailTendency(tendency);
						if(!flag){
							logger.error("运单走势表插入运单数据出错");
						}
					}
					//下面是插入卖家的订单统计
					if(userList!=null && userList.size()>0){
						for(User user : userList){
							/**
							 * 首先判断下该网点该卖家下在这一天的数据是否存在，存在的话就删除，以下会重新统计
							 */
							List<MailTendency> mailTendencyList = mailTendencyService.getMailTendencyBySiteUserTime(site.getId(), user.getId(), yesterday);
							if(mailTendencyList!=null && mailTendencyList.size()>0){
								logger.info("网点："+site.getId()+",卖家:"+user.getId()+"的运单量已经存在，将会删除");
								for(MailTendency obj : mailTendencyList){
									mailTendencyService.removeMailTendecy(obj);
								}
							}
							List<String> customerIdList = new ArrayList<String>();
							if(user.getTaobaoEncodeKey()!=null && !user.getTaobaoEncodeKey().equals("")){
								customerIdList.add(user.getTaobaoEncodeKey());
							}
							/**
							 * 分别统计不同customerId下在yesterday这一天的运单数据量(除‘UNACCEPT’和‘WITHDRAW’状态的订单)
							 */
							logger.info("开始统计网点："+site.getId()+",卖家:"+user.getId()+"的运单量");
							int mailNoNum = orderService.countCurrentDayOrderByCustormerId(customerIdList, yesterday);
							//如果该卖家的运单数目不为0，则插入一条数据到运单走势表中
							if(mailNoNum!=0){
								MailTendency mailTendency = new MailTendency();
								mailTendency.setSiteId(site.getId());
								mailTendency.setUserId(user.getId());
								mailTendency.setMailNoNum(mailNoNum);
								try {
									mailTendency.setMailNoCreateTime(stringToDate(yesterday));
								} catch (ParseException e) {
									e.printStackTrace();
								}
								boolean flag = mailTendencyService.addMailTendency(mailTendency);
								if(!flag){
									logger.error("运单走势表插入运单数据出错");
								}
							}
							
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			StackTraceElement ex = e.getStackTrace()[0];
			Mail mail = new Mail();
			mail.setSubject("TendencyTimer出现异常！");
			mail.setSendToMail(this.receiver);
			mail.setContent(mailContentTemp.replace("${mailContent}", "Timer监控出现异常！\n异常类型：" + e.getClass() + "\n异常行数：" + ex.getLineNumber()));
			mailService.sendMail(mail);
		}

        // }
	}
	
	private static Date stringToDate(String dateString) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(dateString);
	}
	
	public OrderService<Order> getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService<Order> orderService) {
		this.orderService = orderService;
	}

	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

	public MailTendencyService<MailTendency> getMailTendencyService() {
		return mailTendencyService;
	}

	public void setMailTendencyService(
			MailTendencyService<MailTendency> mailTendencyService) {
		this.mailTendencyService = mailTendencyService;
	}

}
