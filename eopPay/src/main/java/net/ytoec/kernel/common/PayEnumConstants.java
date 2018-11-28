package net.ytoec.kernel.common;

/**
 * 充值、支付所用的枚举类
 * @author guoliang.wang
 */
public interface PayEnumConstants {
    /**
     * 充值：是否可用	0 否 1 是
     * 
     */
    enum PAY_FLAG {
        /**
         * 否
         */
    	ISNFLAG("0"),
        /**
         * 是
         */
    	ISFLAG("1");

        public final String value;

        PAY_FLAG(String value) {
            this.value = value;
        }
   }
    
    /**
     * 服务类型	: 0 免费服务 1短信服务 2 其它 3卖家  4网店 5 平台 6业务
     * 
     */
    enum SERVICETYPE_TYPE {
        /**
         * 免费服务
         */
    	SERVICETYPE_FREE("0"),
        /**
         * 短信服务
         */
    	SERVICETYPE_SMS("1"),
    	 /**
         * 其它
         */
    	SERVICETYPE_OTHER("2"),    	
    	 /**
         * 卖家
         */
    	SERVICETYPE_SELLER("3"),
    	 /**
         * 网店
         */
    	SERVICETYPE_SHOP("4"),
    	 /**
         *  平台
         */
    	SERVICETYPE_PLATFORM("5"),
    	 /**
         * 业务
         */
    	SERVICETYPE_BUSINESS("6");

        public final String value;

        SERVICETYPE_TYPE(String value) {
            this.value = value;
        }
   }
    
    /**
     * 开启的服务标示： 	0创建1启用2停用3到期  4 用户删除 5未付款到期删除 6暂时存在(调用支付宝时存在,在支付宝返回结果后就删除掉)
     * 
     */
    enum SERVICEFLAG {
        /**
         * 创建
         */
    	CREATE("CREATE","0"),
        /**
         * 启用
         */
    	ENABLED("ENABLED","1"),
    	 /**
         * 停用
         */
    	DISABLED("DISABLED","2"),
        /**
         * 到期
         */
    	MATURITY("MATURITY","3"),
        /**
         * 用户删除
         */
    	USERDEL("USERDEL","4"),
    	 /**
         * 未付款到期删除
         */
    	NONPAYMENTDEL("NONPAYMENTDEL","5"),
    	
    	 /**
         * 暂时存在(调用支付宝时存在,在支付宝返回结果后就删除掉)
         * 此状态的记录用户不会看到
         */
    	TEMPORARY("TEMPORARY","6");
    	

    	 private SERVICEFLAG(String name, String value) {
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
     * 服务周期	0月、1季、2、半年3、年
     * 
     */
    enum CIRCLE {
        /**
         * 月
         */
    	MONTH("MONTH","0"),
        /**
         * 季
         */
    	SEASON("SEASON","1"),
    	 /**
         * 半年
         */
    	HALFYEAR("HALFYEAR","2"),
        /**
         * 年
         */
    	YEAR("YEAR","3");
       
    	 private CIRCLE(String name, String value) {
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
     * 是否自动续费	0否 1是
     * 
     */
    enum AUTO_FLAG {
        /**
         * 否
         */
    	ISNAUTO("0"),
        /**
         * 是
         */
    	ISAUTO("1");

        public final String value;

        AUTO_FLAG(String value) {
            this.value = value;
        }
   }
    
    /**
     *是否已经提醒	0 否 1 是
     * 
     */
    enum REMIND_FLAG {
        /**
         * 否
         */
    	ISNREMIND("0"),
        /**
         * 是
         */
    	ISREMIND("1");

        public final String value;

        REMIND_FLAG(String value) {
            this.value = value;
        }
   }
    
    /**
     * 交易类型         0  在线充值     1 短信服务、2 时效提醒     3 购买短信 
     */
    enum DEALTYPE {
        /**
         *在线充值
         */
    	ONLINE("ONLINE","0"),
        /**
         * 订购服务    -------------------------- 已废弃
         */
    	ORDERSERVICE("ORDERSERVICE","4"),   
    	 /**
         * 续费服务   -------------------------- 已废弃
         */
    	DISABLED("RENEWALSSERVICE","5"),
    	 /**
         * 短信服务
         */
    	SMSSERVICE("SMSSERVICE","1"),
    	 /**
         * 时效提醒
         */
    	REMAIND("REMAIND","2"),
        /**
         *  购买短信
         */
    	SMS("SMS","3");
    	
    	

    	 private DEALTYPE(String name, String value) {
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
     *  * 交易状态	0 等待支付 1正在充值2已支付4已关闭（超时系统关闭\手动关闭）
     *  
     *  5 逻辑删除（为了处理支付宝返回的交易金额不一致添加）
     * 
     */
    enum DEALSTATUS {
        /**
         *等待支付
         */
    	WAIT("WAIT","0"),
        /**
         * 正在充值[删掉]
         */
    	RECHARGE("RECHARGE","1"),
    	 /**
         * 已支付
         */
    	SUCCESS("SUCCESS","2"),
        /**
         *  失败
         */
    	FAIL("FAIL","3"),
    	 /**
         *  已关闭（超时系统关闭\手动关闭）
         */
    	CLOSED("CLOSED","4"),    
    	/**
         * 5 逻辑删除（为了处理支付宝返回的交易金额不一致添加）
         */
    	LOGDEL("LOGDEL","5");
    	
    	

    	 private DEALSTATUS(String name, String value) {
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
     *交易状态	0 出账  1 进账（相对于账号来说）
     * 
     */
    enum DEALFLAG_FLAG {
        /**
         * 出账
         */
    	OUT("0"),
        /**
         * 进账
         */
    	IN("1");

        public final String value;

        DEALFLAG_FLAG(String value) {
            this.value = value;
        }
   }
    
    /**
     *用户删除状态	0 未关闭 1 已关闭(手动)  2超过时间系统关闭
     * 
     */
    enum DEAL_FLAG {
        /**
         * 未关闭
         */
    	NDEL("0"),
        /**
         *  1 已关闭(手动)
         */
    	ALERADYDEL("1"),
    	
    	 /**
         *  超过时间系统关闭
         */
    	SYSTEMCLOSE("2"),;

        public final String value;

        DEAL_FLAG(String value) {
            this.value = value;
        }
   }
    
    
    /**
     * 操作类型(日志)	0 关闭 1 开启 2其它3 错误日志 4支付宝 5短信调用 6定时器
     *  7(当修改账号表金额时,update_time的时间已经更改所以不能修改账户的金额,
     *  但实际支付宝已经把钱打入我们公司账号,所以需去查看并修改记录[后期定时检查])
     * 8自动续费失败 9自动续费
     */
    enum OPERTYPE {
        /**
         *开启
         */
    	OPEN("OPEN","1"),
        /**
         *  关闭(eg、交易明细关闭)
         */
    	CLOSE("CLOSE","0"),
    	 /**
         * 其它
         */
    	OTHER("OTHER","2"),
    	
    	 /**
         * 错误日志
         */
    	ERROR("ERROR","3"),
    	 /**
         * 支付宝
         */
    	ALIPAY("ALIPAY","4"),
    	 /**
         * 短信调用
         */
    	SMS("SMS","5"),
    	 /**
         * 定时器
         */
    	TIMER("TIMER","6"),
    	
       MUSTCHECK("MUSTCHECK","7"),
       /**
        * 自动续费失败
        */
    	AUTOFAIL("AUTOFAIL","8"),
    	/**
        * 自动续费
        */
    	AUTOFEE("AUTOFEE","9"),
    	/**
         * 发送短信成功,但数量没有更改
         */
     	SMSSUCCESS("SMSSUCCESS","10"),
    	/**
         *发送短信失败
         */
     	SMSFAIL("SMSFAIL","11"),
     	/**
         *交易成功
         */
     	DEALSUCCESS("DEALSUCCESS","12"),
     	/**
         *交易失败
         */
     	DEALFAIL("DEALFAIL","13");

    	 private OPERTYPE(String name, String value) {
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
     * 服务标示(数据服务表记录必须以此为标准)： 	
     * 1 、余额不足提醒 2、交易提醒  3、服务到期提醒 4、短信不足提醒5 短信服务、
     * 6、时效提醒
     *(1、2、3、4 提醒是免费的)
     *[5、短信服务的处理方式跟其它不同]
     */
    enum SERVICE {
        /**
         * 余额不足提醒
         */
    	BALANCELESS("BALANCELESS",1),
        /**
         *交易提醒
         */
    	TRANSACTION("TRANSACTION",2),
    	 /**
         * 服务到期提醒
         */
    	MATURITY("MATURITY",3),
        /**
         * 短信不足提醒
         */
    	SMSLESS("SMSLESS",4),
        /**
         * 短信服务
         */
    	SMS("SMS",5),
    	 /**
         * 问题件预警
         */
    	AGINGREMIND("AGINGREMIND",6);
    	

    	 private SERVICE(String name, int value) {
    	        this.name = name;
    	        this.value = value;
    	    }

    	    private String name;
    	    private int  value;

    	    public String getName() {
    	        return name;
    	    }

    	    public int getValue() {
    	        return value;
    	    }
    	   }
}
