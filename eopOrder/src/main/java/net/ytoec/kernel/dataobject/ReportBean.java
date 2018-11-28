package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.List;
/**
 * 报表查询BEAN
 * @author wangjianzhong
 *
 */
public class ReportBean implements Serializable {

	//标题
	private String title;
	//左标题
	private String leftTitle;
	//右标题
	private String rightTitle;
	//下标题
	private String bottomTitle;	
	//报表类型
	private String reportType;
	//开始日期
	private String startDate;
	//结束日期
	private String endDate;
	
	//报表数据结合
	private List<SearchReportBean> reports;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLeftTitle() {
		return leftTitle;
	}

	public void setLeftTitle(String leftTitle) {
		this.leftTitle = leftTitle;
	}

	public String getRightTitle() {
		return rightTitle;
	}

	public void setRightTitle(String rightTitle) {
		this.rightTitle = rightTitle;
	}

	public String getBottomTitle() {
		return bottomTitle;
	}

	public void setBottomTitle(String bottomTitle) {
		this.bottomTitle = bottomTitle;
	}
	
}
