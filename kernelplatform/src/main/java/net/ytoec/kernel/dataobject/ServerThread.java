package net.ytoec.kernel.dataobject;

import java.util.Date;

/**
 * 多线程服务数据对象
 * 
 * @author Wangyong
 */
public class ServerThread {

	private int id;
	
	//ip
	private String ip;
	
	//端口号
	private String port;
	
	//开始数
	private int startNum;
	
	//结束数
	private int endNum;
	
	//开始任务数
	private int startTaskId;
	
	//结束任务数
	private int endTaskId;
	
	//创建时间
	private Date createTime;
	
	//备注
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getEndNum() {
		return endNum;
	}

	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}

	public int getStartTaskId() {
		return startTaskId;
	}

	public void setStartTaskId(int startTaskId) {
		this.startTaskId = startTaskId;
	}

	public int getEndTaskId() {
		return endTaskId;
	}

	public void setEndTaskId(int endTaskId) {
		this.endTaskId = endTaskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
