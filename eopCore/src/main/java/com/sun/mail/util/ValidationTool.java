package com.sun.mail.util;

import java.util.List;

import net.ytoec.kernel.exception.BusinessException;

/**
 * @作者：罗典
 * @时间：2013-08-29
 * @描述：非业务逻辑性校验类（空校验，等值校验等）
 * */
public class ValidationTool {
	public static String EMPTY_CHENK_FAIL = "Null check fail,the variable name is ";

	/** 
	 * @作者：罗典
	 * @时间：2013-08-29
	 * @描述：对象空校验
	 * @参数：obj 需校验的对象；objName 对象的名字，在提示异常信息时使用
	 * @返回：boolean 是否校验成功
	 * */
	public static boolean validateObject(Object obj, String objName) throws BusinessException {
		if (obj == null) {
			throw new BusinessException(EMPTY_CHENK_FAIL + objName );
		}
		return true;
	}
	
	/**
	 * @作者：罗典
	 * @时间：2013-08-29
	 * @描述：字符串进行空值校验
	 * @参数：arg 需校验的字符串；argName 对象的名字，在提示异常信息时使用
	 * @返回：校验通过后的原值信息
	 * */
	public static String validateString(String arg,String argName) throws BusinessException {
		if (arg == null || "".equals(arg)) {
			throw new BusinessException(EMPTY_CHENK_FAIL + argName );
		}
		return arg;
	}
	
	/** 
	 * @作者：罗典
	 * @时间：2013-08-29
	 * @描述：对象空校验
	 * @参数：obj 需校验的对象；objName 对象的名字，在提示异常信息时使用
	 * @返回：boolean 是否校验成功
	 * */
	@SuppressWarnings("rawtypes")
	public static boolean validateList(List list, String objName) throws BusinessException {
		if (list == null || list.size() <= 0) {
			throw new BusinessException(EMPTY_CHENK_FAIL + objName );
		}
		return true;
	}
}
