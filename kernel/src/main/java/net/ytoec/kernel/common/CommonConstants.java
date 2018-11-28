package net.ytoec.kernel.common;


public class CommonConstants {
	//分页常量
	public final static String PAGE_START = "start";
	public final static String PAGE_LIMIT = "limit";
	public final static String PAGE_SORT = "sort";
	public final static String PAGE_DIR = "dir";
	public final static Integer PAGE_DEFAULT_TOTAL=10000;
	//freemarker 文件后缀
	public final static String FREEMARK_POSTFIX = ".ftl";
	

	/** 
	 * 运单同步job
	 */
	public final static String JOB_WAYBILL = "waybillJob";

	/** 
	 * 密钥同步job
	 */
	public final static String JOB_PARTERN = "parternJob";
	
	/**
	 * 大头笔同步job
	 */
	public final static String JOB_BIGPEN = "bigpenJob";
}