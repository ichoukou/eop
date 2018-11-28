package net.ytoec.kernel.test;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ProductDao;
import net.ytoec.kernel.dataobject.Product;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ProductDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ProductDao<Product> dao;
	
	@Test
	public void testAdd(){
		Product pro = new Product();
		pro.setItemName("orchis");
		pro.setItemNumber(23);
		pro.setOrderId(111);
		pro.setRemark("orchis_remark");
		dao.addProduct(pro);
	}
	
	@Test
	public void testGet(){
		for(Product pro : dao.getAllProduct()){
			System.out.println(dao.getAllProduct().size());
			System.out.println(dao.getProductById(pro.getId()));
		}
	}
	
	@Test
	public void testEdit(){
		for(Product pro : dao.getAllProduct()){
			pro.setItemName("alias");
			pro.setItemNumber(13);
			pro.setRemark("remark_");
			System.out.println("更新结果："+dao.editProduct(pro));
		}
	}
	
	@Test
	public void testRemove(){
		for(Product pro : dao.getAllProduct()){
			System.out.println("删除结果："+dao.removeProduct(pro));
		}
	}
}
