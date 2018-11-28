package net.ytoec.kernel.common;
/*
 * 无线天利
 * 2012-08-07
 * guoliang.wang
 */
public interface TechcenterEnum {

	 /**
     * 无线天利代码
     */
    enum  TECHCENTERFLAG {
    	 /**
         * 待发送
         */
    	WAIT("WAIT","8"),
    	 /**
         * 易通平台发送
         */
    	YTOXL("YTOXL","88"),
        /**
         * 客户接收成功
         */
    	SUCCESS("SUCCESS","0"),
    	  /**
         * 1 网关失败
         */
    	GATEWAY_FAIL("GATEWAY_FAIL","1"),
    	  /**
         * 2 发送失败
         */
    	SEND_FAIL("SEND_FAIL","2"),
        /**
         * 认证错误
         */
    	CERTIFICATION_FAIL("CERTIFICATION_FAIL","-1"),
    	 /**
         * 手机号码错误
         */
    	PHONE_FAIL("PHONE_FAIL","-2"),
        /**
         *消息长度错误
         */
    	CONTEXTLEN_FAIL("CONTEXTLEN_FAIL","-3"),
        /**
         * 消息内容错误
         */
    	CONTEXT_FAIL("CONTEXT_FAIL","-4"),
    	 /**
         * 产品编号错误
         */
    	PRODUCT_FAIL("PRODUCT_FAIL","-5"),
    	
    	 /**
         * 群发设置错误
         */
    	GROUP_FAIL("GROUP_FAIL","-6"),
    	 /**
         * 发送超时错误
         */
    	TIMEOUT_FAIL("TIMEOUT_FAIL","-7"),
    	 /**
         * 流量控制错误
         */
    	FLOW_FAIL("FLOW_FAIL","-8"),
    	
    	 /**
         * 发送失败错误
         */
    	SENDCODE_FAIL("SENDCODE_FAIL","-9"),
    	 /**
         * 黑名单手机号
         */
    	BLACKPHONE_FAIL("BLACKPHONE_FAIL","-10"),
    	 /**
         * 非订购用户
         */
    	ILLEGAL_USER("ILLEGAL_USER","-11"),
    	 /**
         * 路由处理错误
         */
    	ROUTER_FAIL("ROUTER_FAIL","-12"),
    	 /**
         * 程序终止清空滑动窗口
         */
    	END("END","-13");    	

    	 private TECHCENTERFLAG(String name, String value) {
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
