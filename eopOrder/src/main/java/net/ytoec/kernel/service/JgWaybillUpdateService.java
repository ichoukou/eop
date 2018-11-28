/**
 * JgWaybillService.java
 * Wangyong
 * 2011-8-18 下午01:28:18
 */
package net.ytoec.kernel.service;


/**
 * 金刚运单接口信息数据Service
 * @author Wangyong
 * @2011-8-18
 * net.ytoec.kernel.service
 */
public interface JgWaybillUpdateService<T> {

	/**
	 * 增加出错信息规范对象数据
	 * 
	 * @param jgWaybill
	 * @return @
	 */
	public boolean addJgWaybillUpdate(T jgWaybillUpdate);

}
