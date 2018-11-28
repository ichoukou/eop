package net.ytoec.kernel.dao.impl;

import net.ytoec.kernel.dao.DFAOutCallBack;



public class DFAOutCallBackImpl implements DFAOutCallBack {

	//@Override
	public void CallBack(String keyword) {
		System.out.println("关键字："+keyword);
	}

}
