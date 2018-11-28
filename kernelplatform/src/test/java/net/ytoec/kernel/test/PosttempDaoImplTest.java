package net.ytoec.kernel.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.PosttempDao;
import net.ytoec.kernel.dao.impl.PosttempDaoImpl;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.util.XmlUtil;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 测试
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class PosttempDaoImplTest extends AbstractJUnit38SpringContextTests {

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;

	@Inject
	private PosttempDao<Posttemp> dao;

	/**
	 * 测试xml解析
	 * @throws Exception
	 */
	@Test
	public void testGetPosttempById() throws Exception {
		Posttemp post =  dao.getPosttempById(11);
		String xml = post.getPostinfo();
		if(xml == null) return;
		System.out.println("xml解析前：" + xml);
		System.out.println("===开始解析===");
		
		// 读取postinfo（xml字符串），解析成对象测试
		List list = XmlUtil.xmlDecoder2List(xml);
		for (Object obj : list) {
			System.out.println(obj);
		}
	}
	
	/**
	 * 测试新增得到Id
	 */
	@Test
	public void testAddPosttemp() throws Exception {
		Posttemp post =  new Posttemp();
		post.setCreateUser(1);
		post.setPostinfo("ss");
		post.setPtName("xx");
		
		dao.addPosttemp(post);
		System.out.println(post.getId());	// 16
		
	}

}
