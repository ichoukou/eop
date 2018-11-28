/**
 * 
 */
package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

/**
 * 产品信息数据访问接口
 * @author Wangyong
 */
public interface ProductDao<T> {

	/**
	 * 增加产品信息对象
	 * @param product
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addProduct(T product) throws DataAccessException;
	
	public boolean addProducts(List<T> products) throws DataAccessException;
	
	/**
	 * 删除产品信息对象
	 * @param product
	 * @return
	 * @throws DataAccessException
	 */
	public boolean removeProduct(T product) throws DataAccessException;
	
	/**
	 * 更新产品信息对象
	 * @param product
	 * @return
	 * @throws DataAccessException
	 */
	public boolean editProduct(T product) throws DataAccessException;
	
	/**
	 * 根据产品信息id号查询产品信息对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getProductById(Integer id) throws DataAccessException;
	
	/**
	 * 根据orderID查询产品信息列表
	 * @param itemName 货物名称
	 * @return
	 */
	public List<T> getProductListByOrder(Integer orderId) throws DataAccessException;
	
	/**
	 * 查询所有产品信息对象
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllProduct() throws DataAccessException;
	
	public boolean removeByorderId(Integer orderId);
}
