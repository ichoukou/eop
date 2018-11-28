package com.ytoec.uninet.util;

import org.junit.Test;


public class HessianUtilTest {
	
	
	@Test
	public void testGetValue(){
		String value = HessianUtil.getMediaHessianService();
		String value2 = HessianUtil.getFileUploadBasePath();
		System.out.println(value+"/"+value2);
	}

}
