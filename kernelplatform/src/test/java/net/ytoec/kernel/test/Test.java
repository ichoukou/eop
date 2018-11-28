package net.ytoec.kernel.test;

import java.util.Date;
import java.util.Random;

import net.sf.json.JSONObject;


public class Test {
    
    //@org.junit.Test
    public void testTime(){
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime()/1000);
    }
    
//    @org.junit.Test
    public void testShortCut(){
    	String json ="{\"picposition\":\"0,0\",\"item\":[{\"name\":\"收货人-姓名\",\"ucode\":\"ship_name\",\"font\":\"宋体\",\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[448,101,102,20]},{\"name\":\"收货人-地址\",\"ucode\":\"ship_address\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[562,152,143,20]},{\"name\":\"收货人-电话\",\"ucode\":\"ship_tel\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[641,208,77,20]},{\"name\":\"收货人-地区\",\"ucode\":\"ship_regionFullName\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[443,150,80,20]},{\"name\":\"发货人-姓名\",\"ucode\":\"dly_name\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[116,84,81,20]},{\"name\":\"发货人-地址\",\"ucode\":\"dly_address\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[202,142,162,20]},{\"name\":\"发货人-地区\",\"ucode\":\"dly_regionFullName\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[117,143,68,20]},{\"name\":\"发货人-手机\",\"ucode\":\"dly_mobile\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[149,208,108,20]},{\"name\":\"左上\",\"ucode\":\"text\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[2,1,120,40]},{\"name\":\"左下\",\"ucode\":\"text\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[4,426,126,20]},{\"name\":\"右上\",\"ucode\":\"text\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[773,2,35,23]},{\"name\":\"右下\",\"ucode\":\"text\",\"font\":null,\"fontsize\":\"12\",\"fontspace\":\"0\",\"border\":\"0\",\"italic\":\"0\",\"align\":\"left\",\"position\":[775,426,33,20]}],\"background\": \"template_e\"}";
		JSONObject jsonObject = JSONObject.fromObject(json);
		
		System.out.println(jsonObject.toString());
		
    }
    
    public int NextInt(final int min, final int max){
        
        Random rand;
        rand= new Random();
        int tmp = Math.abs(rand.nextInt());
        return tmp % (max - min + 1) + min;
    }
    
    @org.junit.Test
    public void testRandom(){
        System.out.println(NextInt(1000000,9999999));
    }

}
