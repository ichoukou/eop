package net.ytoec.kernel.util;

import org.junit.Test;

public class StringUtilTest {
    
    @Test
    public void test(){
        String test = "圆通推出全国首个电商物流服务平台  ";
        System.out.println("length=="+StringUtil.getLength(test));
    }

}
