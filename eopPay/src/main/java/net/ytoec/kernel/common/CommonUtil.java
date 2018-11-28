package net.ytoec.kernel.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断手机号码
 * @author guoliang.wang
 */
public class CommonUtil {

	/**
	  * 是否手机号码号码,不包含区号
	  *
	  * @param phoneNum
	  * @return 
	  */
	 public static boolean isMobilePhone(String phoneNum) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,2,3,4,5-9]))\\d{8}$");     
	    Matcher m = p.matcher(phoneNum);   
	    return m.matches();     
	/*  String cmcc = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[89]{1}))[0-9]{8}$";
	  String cucc = "^[1]{1}(([3]{1}[0-3]{1})|([5]{1}[3]{1}))[0-9]{8}$";
	  String cnc = "^[1]{1}[8]{1}[6-9]{1}[0-9]{8}$";
	  if (phoneNum.length() == 11) {
	   if (phoneNum.matches(cmcc)) {
	    return true;
	   } else if (phoneNum.matches(cucc)) {
	    return true;
	   } else if (phoneNum.matches(cnc)) {
	    return true;
	   } else {
	    return false;
	   }
	  } else {
	   return false;
	  }*/
	 }
	 public static void main(String[] args){
		 System.out.println(CommonUtil.isMobilePhone("18923710734"));
	 }
}
