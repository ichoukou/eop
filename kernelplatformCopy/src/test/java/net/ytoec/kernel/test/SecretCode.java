package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.common.Constant;

public class SecretCode {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("0");
		list.add("");
		for(String nnn :list){
			String nn = "<MailNoRequest><customerCode>K21000844</customerCode><sequence>"+nnn+"</sequence><success>true</success></MailNoRequest>";
			String parentCode = "jNpKcyXrHfNJ";
			String newDataDigest = Md5Encryption.MD5Encode(nn
					+ parentCode, Constant.CHARSET_UTF8);
			System.out.println(nn);
			System.out.println(nnn+"   "+newDataDigest);
			
		}
		
	}
}
