package net.ytoec.kernel.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ProductDao;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.mapper.ProductMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
/**
 * 产品信息数据访问实现类
 * @author Wangyong
 *
 * @param <T>
 */
@Repository
public class ProductDaoImpl<T extends Product> implements ProductDao<T> {

	private static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
	@Inject
	private ProductMapper<Product> mapper;
	
	@Override
	public boolean addProduct(T product) throws DataAccessException {
		
		boolean flag = false;
		try{
			mapper.add(product);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			throw e;
		}
		return flag;
	}

	//此实现暂使用addProduct做为便利步骤,但此实现的要求必须在同一次数据库连接操作中完成.
	@Override
	public boolean addProducts(List<T> products) {
		
		for (Product product : products) {
			this.addProduct((T)product);
		}
		
		return false;
	}
	
	@Override
	public boolean removeProduct(T product) {
		
		boolean flag = false;
		try{
			mapper.remove(product);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editProduct(T product) {
		
		boolean flag = false;
		try{
			mapper.edit(product);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public T getProductById(Integer id) {
		
		Product pro = new Product();
		T entity = null;
		try{
			pro.setId(id);
			entity = (T)mapper.get(pro);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public List<T> getAllProduct() {
		
		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getAllProduct();
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public List<T> getProductListByOrder(Integer orderId) {
		List<T> list = null;
		try{
			list = (List<T>)mapper.getProductListByOrder(orderId);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}
		return list;
	}
	@Override
	public boolean removeByorderId(Integer orderId) {
		Map map = new HashMap();
		map.put("orderId", orderId);
		Integer rows = this.mapper.removeByorderId(map);
		if (rows > 0) {
			return true;
		}

		return false;
	}
}
