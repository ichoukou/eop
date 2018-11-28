package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.mapper.ProductMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ProductMapperTest {
	private static ProductMapper<Product> mapper;
	private static int id = 1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		Thread.sleep(2000);
		mapper = (ProductMapper) ctx.getBean("productMapper");
	}

	@Test
	public void testAdd() {
		Product pro = new Product();
		pro.setItemName("durian");
		pro.setItemNumber(10);
		pro.setOrderId(2);
		pro.setRemark("durian remark");
		mapper.add(pro);
	}
	
	@Test
	public void testGet() {
		List<Product> list = mapper.getAllProduct();
		for(Product pro : list){
			System.out.println(mapper.get(pro));
		}
	}
	
	@Test
	public void testGetByName(){
		List<Product> list = mapper.getProductListByItemName("dur");
		System.out.println(list.size());
	}

	@Test
	public void testEdit() {
		List<Product> list = mapper.getAllProduct();
		for(int i=0; i<list.size(); i++){
			list.get(i).setItemName("itemName_"+i);
			list.get(i).setItemNumber(i+10);
			list.get(i).setRemark("remark_"+i);
			mapper.edit(list.get(i));
		}
	}
	
	@Test
	public void testRemove() {
		List<Product> list = mapper.getAllProduct();
		for(Product pro : list){
			mapper.remove(pro);
		}
	}

}
