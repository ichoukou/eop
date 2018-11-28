/**
 * 
 */
package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

/**
 * @author wangyong
 *
 */
public interface ProductService<T> {

	public List<T> getProductListByOrder(Integer orderId);
	
	public boolean removeByorderId(Integer orderId);
}
