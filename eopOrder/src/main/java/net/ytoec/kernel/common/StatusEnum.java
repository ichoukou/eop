package net.ytoec.kernel.common;

public enum StatusEnum {
    /**
     * 创建订单
     */
    CREATE("CREATE", new Short("0")),
    /**
     * 接单
     */
    ACCEPT("ACCEPT", new Short("1")),
    /**
     * 不接单
     */
    UNACCEPT("UNACCEPT", new Short("2")),
    /**
     * 揽收
     */
    GOT("GOT", new Short("3")),
    /**
     * 揽收失败
     */
    NOT_SEND("NOT_SEND", new Short("4")),

    /**
     * 失败
     */
    FAILED("FAILED", new Short("5")),

    /**
     * 派送扫描，正在派送
     */
    SENT_SCAN("SENT_SCAN", new Short("6")),
    /**
     * 其他未知状态
     */
    UNKNOW("UNKNOW", new Short("7")),
    /**
     * 已签收，成功订单
     */
    SIGNED("SIGNED", new Short("8")),

    /**
     * update 更新运单好
     */
    UPDATE("UPDATE", new Short("9")),
    /**
     * 未打印
     */
    NOPRINT("NOPRINT", new Short("10")),
    /**
     * 已打印
     */
    PRINTED("PRINTED", new Short("11")),
    /**
     * 取消订单
     */
    WITHDRAW("WITHDRAW", new Short("12")),
    /**
     * update 更新运单好
     */
    TRACKING("TRACKING", new Short("13")),
    
    /**
     * COD支付成功
     */
    PAY_SUCCESS("PAY_SUCCESS", new Short("14")),
    
    /**
     * COD支付失败
     */
    PAY_FAILED("PAY_FAILED", new Short("15"));


    private StatusEnum(String name, Short value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Short  value;

    public String getName() {
        return name;
    }

    public Short getValue() {
        return value;
    }

    public static StatusEnum getStatusName(int value) {
        switch (value) {
            case 0:
                return StatusEnum.CREATE;
            case 1:
                return StatusEnum.ACCEPT;
            case 2:
                return StatusEnum.UNACCEPT;
            case 3:
                return StatusEnum.GOT;
            case 4:
                return StatusEnum.NOT_SEND;
            case 5:
                return StatusEnum.FAILED;
            case 6:
                return StatusEnum.SENT_SCAN;
            case 7:
                return StatusEnum.UNKNOW;
            case 8:
                return StatusEnum.SIGNED;
            case 9:
                return StatusEnum.UPDATE;
            case 10:
                return StatusEnum.NOPRINT;
            case 11:
                return StatusEnum.PRINTED;
            case 12:
                return StatusEnum.WITHDRAW;
            case 13:
                return StatusEnum.TRACKING;
            case 14:
            	return StatusEnum.PAY_SUCCESS;
            case 15:
            	return StatusEnum.PAY_FAILED;
            default:
                return StatusEnum.UNKNOW;
        }
    }
    
    //判断枚举类型是否存在
    public static boolean isExitEnum(String str)
    {
    	boolean flag=false;
    	if("CREATE ACCEPT UNACCEPT GOT NOT_SEND FAILED SENT_SCAN UNKNOW SIGNED UPDATE NOPRINT PRINTED WITHDRAW TRACKING PAY_SUCCESS PAY_FAILED".indexOf(str)!=-1)    	
    		flag=true;
    	
    	return flag;
    }


    public static void main(String[] args) {
        System.out.println( StatusEnum.isExitEnum("CREATE"));
    }

}
