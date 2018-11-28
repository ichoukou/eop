package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.ytoec.kernel.util.DateUtil;
/**
 * 报表查询BEAN
 * @author wangjianzhong
 *
 */
public class SearchReportBean implements Serializable {
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	private DecimalFormat fnum = new DecimalFormat("##0.00");  
	
	/**
	 * 日期
	 */
	private String sendStartTime;
	
	/**
	 * 签收比率
	 */
	private double ratio;
	
	/**
	 * 备注:每日签收
	 */
	private String remark1;
	
	/**
	 * 备注：签收完成比例
	 */
	private String remark2;
	
	/**
	 * 小时
	 */
	private int hours;
	
	/**
	 * 小时对应的类型  <=24：1    <=48：2   <=72： 不在范围之内为 0
	 */
	private int hoursType;
	
	/**
	 * 查询结果数量/总数量
	 */
	private int number;
	
	/**
	 * 总条数
	 */
	private int allCount;
	/**
	 * 签收数量
	 */
	private int accept;
	
	/**
	 * 时间比率
	 */
	private double hoursRatio;

	public String getSendStartTime() {
		return sendStartTime;
	}

	public void setSendStartTime(String sendStartTime) {
		this.sendStartTime = sendStartTime;
	}

	public double getRatio() {
		if(number!=0) {
			ratio = (double)accept/number; 
			ratio = Double.valueOf(fnum.format(ratio));
		}
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getHoursType() {
		return hoursType;
	}

	public void setHoursType(int hoursType) {
		this.hoursType = hoursType;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getAccept() {
		return accept;
	}

	public void setAccept(int accept) {
		this.accept = accept;
	}

	public String getRemark1() {
		return "签收量："+accept+"/"+number+"：总发货量";
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return "签收量："+number+"/"+allCount+"：总签收量";
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}

	public double getHoursRatio() {
		if(allCount!=0) {
			hoursRatio = (double)number/allCount; 
			hoursRatio = Double.valueOf(fnum.format(hoursRatio));
		}
		return hoursRatio;
	}

	public void setHoursRatio(double hoursRatio) {
		this.hoursRatio = hoursRatio;
	}
	

}
