/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ProductDao;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.service.ProductService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyong
 * @date 2011-11-16
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class ProductServiceImpl<T extends Product> implements ProductService<T> {
	
	@Inject
	private ProductDao<T> productDao;

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.service.ProductService#getProductListByOrder()
	 */
	@Override
	public List<T> getProductListByOrder(Integer orderId) {
		return productDao.getProductListByOrder(orderId);
	}
	public boolean removeByorderId(Integer orderId) {

		return productDao.removeByorderId(orderId);
	}
}
