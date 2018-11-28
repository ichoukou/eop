package net.ytoec.kernel.common;

@Deprecated
public interface SDKClientEnum {

	 /**
	  * 亿美软通
     * 注册序列号[不再选此供应商]
     */
    enum  REGISTEX {
        /**
         * 注册成功
         */
    	SUCCESS("SUCCESS","0"),
        /**
         * 客户端注册失败(请检查序列号、密码、key值是否配置正确)
         */
    	NMATCH("NMATCH","911005"),
    	 /**
         * 该序列号已经使用其它key值注册过了。（若无法找回key值请联系销售注销，然后重新注册使用。）
         */
    	ALREADYUSED("ALREADYUSED","911003"),
        /**
         * 客户端网络超时或网络故障
         */
    	NETWORKTIMEOUT("NETWORKTIMEOUT","303"),
        /**
         * 服务器端返回错误，错误的返回值（返回值不是数字字符串）
         */
    	SERVERERROR("SERVERERROR","305"),
    	 /**
         * 操作频繁
         */
    	FREQUENTLY("FREQUENTLY","999");    	

    	 private REGISTEX(String name, String value) {
    	        this.name = name;
    	        this.value = value;
    	    }

	    	    private String name;
	    	    private String  value;
	
	    	    public String getName() {
	    	        return name;
	    	    }
	
	    	    public String getValue() {
	    	        return value;
	    	    }
    	   }
    
    /**
     * 注销序列号
     */
    enum  LOGOUT {
        /**
         * 注销成功
         */
    	SUCCESS("SUCCESS","0"),
        /**
         * 失败
         */
    	FAIL("FAIL","22"),    	
        /**
         * 客户端网络超时或网络故障
         */
    	NETWORKTIMEOUT("NETWORKTIMEOUT","303"),
        /**
         * 服务器端返回错误，错误的返回值（返回值不是数字字符串）
         */
    	SERVERERROR("SERVERERROR","305"),
    	 /**
         * 操作频繁
         */
    	FREQUENTLY("FREQUENTLY","999");    	

    	 private LOGOUT(String name, String value) {
    	        this.name = name;
    	        this.value = value;
    	    }

	    	    private String name;
	    	    private String  value;
	
	    	    public String getName() {
	    	        return name;
	    	    }
	
	    	    public String getValue() {
	    	        return value;
	    	    }
    	   }
    
    /**
     * 查询单价
     */
    enum  EACHFEE {
        /**
         * 查询单条短信费用错误码
         */
    	FAIL("FAIL","27"),
        /**
         * 客户端网络超时或网络故障
         */
    	NETWORKTIMEOUT("NETWORKTIMEOUT","303"),
        /**
         * 服务器端返回错误，错误的返回值（返回值不是数字字符串）
         */
    	SERVERERROR("SERVERERROR","305"),
    	 /**
         * 操作频繁
         */
    	FREQUENTLY("FREQUENTLY","999");    	

    	 private EACHFEE(String name, String value) {
    	        this.name = name;
    	        this.value = value;
    	    }

	    	    private String name;
	    	    private String  value;
	
	    	    public String getName() {
	    	        return name;
	    	    }
	
	    	    public String getValue() {
	    	        return value;
	    	    }
    	   }
    
    /**
     * 发送即时短信
     */
    enum  SENDSMS {
    	/**
         *发送信息失败（短信内容长度越界）
         */
    	CROSSBORDER("CROSSBORDER","-1"),
        /**
         * 短信发送成功
         */
    	SUCCESS("SUCCESS","0"),
        /**
         * 发送信息失败（未激活序列号或序列号和KEY值不对，或账户没有余额等）
         */
    	FAIL("FAIL","17"),
    	 /**
         * 目标电话号码不符合规则，电话号码必须是以0、1开头
         */
    	PHONEERROR("PHONEERROR","307"),
        /**
         * 客户端网络超时或网络故障
         */
    	NETWORKTIMEOUT("NETWORKTIMEOUT","101"),
        /**
         * 服务器端返回错误，错误的返回值（返回值不是数字字符串）
         */
    	SERVERERROR("SERVERERROR","305"),
    	 /**
         * 平台返回找不到超时的短信，该信息是否成功无法确定
         */
    	NFIND("NFIND","997"),
    	 /**
         * 由于客户端网络问题导致信息发送超时，该信息是否成功下发无法确定
         */
    	NSURE("NSURE","303"),
    	 /**
         *  数据不完整
         */
    	OHTER("OHTER","555"),  
    	 /**
         *账号不存在
         */
    	NEXITS("NEXITS","666");  

    	 private SENDSMS(String name, String value) {
    	        this.name = name;
    	        this.value = value;
    	    }

	    	    private String name;
	    	    private String  value;
	
	    	    public String getName() {
	    	        return name;
	    	    }
	
	    	    public String getValue() {
	    	        return value;
	    	    }
    	   }
    
    /**
     *发送定时短信
     */
    enum  SENDSCHEDULEDSMS {
    	/**
         *发送信息失败（短信内容长度越界）
         */
    	CROSSBORDER("CROSSBORDER","0"),
        /**
         * 短信发送成功
         */
    	SUCCESS("SUCCESS","0"),
        /**
         * 发送信息失败（未激活序列号或序列号和KEY值不对，或账户没有余额等）
         */
    	FAIL("FAIL","17"),
    	/**
         * 发送定时信息失败
         */
    	SCHEDULEDFAIL("SCHEDULEDFAIL","18"),
    	 /**
         * 目标电话号码不符合规则，电话号码必须是以0、1开头
         */
    	PHONEERROR("PHONEERROR","307"),
        /**
         * 客户端网络超时或网络故障
         */
    	NETWORKTIMEOUT("NETWORKTIMEOUT","101"),
        /**
         * 服务器端返回错误，错误的返回值（返回值不是数字字符串）
         */
    	SERVERERROR("SERVERERROR","305"),
    	 /**
         * 平台返回找不到超时的短信，该信息是否成功无法确定
         */
    	NFIND("NFIND","997"),
    	 /**
         * 由于客户端网络问题导致信息发送超时，该信息是否成功下发无法确定
         */
    	NSURE("NSURE","303");    	

    	 private SENDSCHEDULEDSMS(String name, String value) {
    	        this.name = name;
    	        this.value = value;
    	    }

	    	    private String name;
	    	    private String  value;
	
	    	    public String getName() {
	    	        return name;
	    	    }
	
	    	    public String getValue() {
	    	        return value;
	    	    }
    	   }
}
