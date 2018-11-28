package net.ytoec.kernel.action.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.ReportBean;
import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.UserService;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


/**
 * 查询报表Action 
 * @author wangjianzhong
 * @since 2012-08-01
 */

@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class SearchReportAction extends AbstractActionSupport{
	/*
	 * LOG日志
	 */
	private static Logger logger = LoggerFactory.getLogger(SearchReportAction.class);
	
	@Inject
	private UserService<User> userService;
	
	@Inject
	private OrderService<Order> orderService;
	
	/**
	 * 查询条件,报表类型,开始日期,结束日期
	 */
	private String reportType;  //报表类型     1:每日成功签收比率 2：签收完成时间比率
	
	private String startDate;   //开始日期
	
	private String endDate;     //结束日期
	
	private Map jsons = new HashMap();  //页面JSON集合
	
	private ReportBean report = new ReportBean();
	
	private List<SearchReportBean> reports;
	
	private JFreeChart chart;
	
	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<SearchReportBean> getReports() {
		return reports;
	}

	public void setReports(List<SearchReportBean> reports) {
		this.reports = reports;
	}

	public Map getJsons() {
		return jsons;
	}

	public void setJsons(Map jsons) {
		this.jsons = jsons;
	}
	
	public ReportBean getReport() {
		return report;
	}

	public void setReport(ReportBean report) {
		this.report = report;
	}

	/**
	 * 报表查询显示[页面]
	 */
	public String searchReportView() {
		User landUser = super.readCookieUser();
		int userType = getUserType();
		String theId = landUser.getTaobaoEncodeKey();
		
		/*测试参数
		startDate = "2012-2-5";
		endDate = "2012-3-11";	
		theId = "00010b14bd4ad914162e922b958b70cd";
		*/
		
		report.setReportType(reportType);
		report.setStartDate(startDate);
		report.setEndDate(endDate);
		
		//区分用户类型,分别做查询
		if(userType==1) {  //卖家
			theId = landUser.getTaobaoEncodeKey(); //customerId
			if("1".equals(reportType)) {
				/*
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date sDate;
				Date eDate;
				try {
					sDate = format.parse(startDate);
					eDate = DateUtil.getDateAfter(sDate, 7);
					endDate = format.format(eDate);
				} catch (ParseException e) {
					logger.error("IN ACTION:SearchReportAction METHOD:searchReportView() parse startDate:"+ startDate + "ERR");
				}
				reports = orderService.getSignRatioByPamams(userType,theId, startDate, endDate);
				*/
				reports = orderService.getSellSignRatioForDayByPamams(theId, startDate, 7);
			}
			if("2".equals(reportType)) {
				reports = orderService.getSellHourRatioByPamams(theId, startDate, endDate);
			}
		}
		if(userType==4) {  //平台用户
			theId = landUser.getId()+"";  //主键ID
			if("1".equals(reportType)) {
				reports = orderService.getPlatformSignRatioForDayByPamams(theId, startDate, 7);
			}
			if("2".equals(reportType)) {
				reports = orderService.getPlatformHourRatioByPamams(theId, startDate, endDate);
			}
		}

		setReportBean(report,reports);
		
		jsons.put("reports", reports);
		return "json";
	} 
	
	public JFreeChart getChart() {
		return chart;
	 }
	
	/**
	 * 将查询结果,设置到ReportBean用户前台展现
	 * @param report
	 * @param reports
	 */
	private void setReportBean(ReportBean report,List<SearchReportBean> reports) {
		if("1".equals(report.getReportType())) {
			report.setTitle("签收完成进度");
			report.setLeftTitle("百分比");
			report.setRightTitle("签收完成进度");
			report.setBottomTitle("签收完成进度");
			report.setReports(reports);
		}
		if("2".equals(report.getReportType())) {
			int allCount = 0;
			//计算总值
			for(SearchReportBean reportBean:reports) {
				allCount += reportBean.getNumber();
			}
			//设置总值
			for(SearchReportBean reportBean:reports) {
				reportBean.setAllCount(allCount);
			}
			report.setTitle(report.getStartDate() + " 至 " + report.getEndDate() + " 签收完成比例表");
			report.setLeftTitle("签收完成时间比率");
			report.setRightTitle("签收完成时间比率");
			report.setBottomTitle("签收完成时间比率");
			report.setReports(reports);
		}
	}
	
	/**
	 * 获取登录用户类型 0:未知的类型  1：卖家 2：网点 3：admin 4:平台用户
	 * @return
	 */
	private int getUserType() {
		User landUser = super.readCookieUser();
		String userType = landUser.getUserType();
		if("1".equals(userType)||"11".equals(userType)||"12".equals(userType)||"13".equals(userType)) {
			return 1;
		}else if("2".equals(userType)||"21".equals(userType)||"22".equals(userType)||"23".equals(userType)){
			return 2;
		}else if("3".equals(userType)) {
			return 3;
		}else if("4".equals(userType)) {
			return 4;
		}
		return 0;
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
	
	private String exePwd = "ADFE1223FC211CFSA23";
	
	private String pwd;
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	

	/**
	 * 跳转执行
	 * @return
	 */
	public String toImportDb() {
		if(exePwd.equals(pwd)) {
			orderService.insertImportDb();
		}
		return null;
	}
}

