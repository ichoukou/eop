package net.ytoec.kernel.common;

public enum OrderTypeEnum {

    /**
     * 普通订单
     */
    NORMAL("NORMAL", 0),
    /**
     * cod订单
     */
    COD("COD", 1),
    /**
     * material reject bill 退货订单
     */
    MRB("MRB", 3),
    /**
     * 易通打印订单
     */
    PRINT("PRINT", 4);

    private String name;
    private int    value;

    private OrderTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
