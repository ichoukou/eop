package net.ytoec.kernel.common;

/**
 * 交易名称的枚举类
 * @author guoliang.wang
 */
public enum RechargeSMSFlagEnum {

	
	
	 /**(交易名称)
     * 1 、余额不足提醒 2、交易提醒  3、服务到期提醒 4、短信不足提醒5、问题件预警
     * 6、发货提醒 7派件提醒8、短信体验套餐9短信初级短信套餐
     * 10短信中级套餐11短信高级套餐 0 其它
     * 12 短信服务13超时件服务 14支付操作 
     */
	BALANCE("余额不足提醒 ", "1"),
	TRANSACTION("交易提醒", "2"),
	SERVICE("服务到期提醒", "3"),
	SMS("短信不足提醒", "4"),
	QUESTION("问题件预警","5"),
	SEND("发货提醒","6"),
	SENDPIECES("派件提醒", "7"),
	SMSEXPRIENCE("短信体验套餐","8"),
	SMSELEMENTARY("短信初级短信套餐","9"),
	SMSMIDDLE("短信中级套餐","10"),
	SMSADVANCED("短信高级套餐","11"),
	SMSSERVICE("短信服务", "12"),
	TIMEOUT("超时件服务","13"),
	/**
	 * 支付操作
	 */
	SPAYMENT("在线充值","14"),	
	OTHER("其它", "0");
  
	

	
    private String name;
    private String  value;
    
    private RechargeSMSFlagEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static RechargeSMSFlagEnum getStatusName(int value) {
        switch (value) {
            case 1:
                return RechargeSMSFlagEnum.BALANCE;
            case 2:
                return RechargeSMSFlagEnum.TRANSACTION;
            case 3:
                return RechargeSMSFlagEnum.SERVICE;
            case 4:
                return RechargeSMSFlagEnum.SMS;
            case 5:
                return RechargeSMSFlagEnum.QUESTION;
            case 6:
                return RechargeSMSFlagEnum.SEND;
            case 7:
                return RechargeSMSFlagEnum.SENDPIECES;
            case 8:
                return RechargeSMSFlagEnum.SMSEXPRIENCE;
            case 9:
                return RechargeSMSFlagEnum.SMSELEMENTARY;
            case 10:
                return RechargeSMSFlagEnum.SMSMIDDLE;  
            case 11:
                return RechargeSMSFlagEnum.SMSADVANCED;     
            default:
                return RechargeSMSFlagEnum.OTHER;
        }
    }
    
	public static void main(String[] args) {
		Integer a=1;
		
		System.out.println(a==PayEnumConstants.SERVICE.BALANCELESS.getValue());
	}
}
