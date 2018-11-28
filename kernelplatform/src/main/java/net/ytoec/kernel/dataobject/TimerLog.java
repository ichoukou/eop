package net.ytoec.kernel.dataobject;

import java.util.Date;

public class TimerLog {
	
	/**
	 * 增删改常量定义:0-增
	 */
	public static final int OPERATE_0 = 0;
	/**
	 * 增删改常量定义:1-删
	 */
	public static final int OPERATE_1 = 1;
	/**
	 * 增删改常量定义:2-改
	 */
	public static final int OPERATE_2 = 2;
	/**
	 * 是否错误常量定义:4-应用中
	 */
	public static final int ISERROR_TRUE = 0;
	/**
	 * 是否错误常量定义:5-已禁用
	 */
	public static final int ISERROR_FALSE = 1;
	
	/**
	 * 表名:order
	 */
	public static final String TABLENAME_ORDER = "order";
	
	/**
	 * 数据来源：金刚
	 */
	public static final String DATAFROM_JINGANG = "JINGANG";
	
	// 主键ID
	private Integer id;

	// 表名
	private String tableName;

	// 增删改 ：0 增；1删；2改
	private int operate;

	// 总条数
	private int num;

	// 错误条数
	private int errorNum;

	// 用时
	private long useTime;

	// 创建时间
	private Date createTime;

	// 查询开始时间
	private Date startTime;

	// 查询结束时间
	private Date endTime;

	// timer名
	private String timerNO;

	// 数据来源
	private String dataFrom;

	// 是否错误 :0 成功 ；1失败
	private int isError;

	// 详细信息
	private String message;
	
	// 查询开始时间
	private String startTimeStr;

	// 查询结束时间
	private String endTimeStr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getOperate() {
		return operate;
	}

	public void setOperate(int operate) {
		this.operate = operate;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(int errorNum) {
		this.errorNum = errorNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTimerNO() {
		return timerNO;
	}

	public void setTimerNO(String timerNO) {
		this.timerNO = timerNO;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public int getIsError() {
		return isError;
	}

	public void setIsError(int isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

}
