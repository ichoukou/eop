package net.ytoec.kernel.test;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.RegionDao;
import net.ytoec.kernel.dataobject.Region;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class RegionDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private RegionDao<Region> dao;

// @2011-10-11/ChenRen	为避免有人执行这里的测试方法修改数据库中的数据, 所以注释!	
	
	@Test
	public void testExport(){
	    System.out.println("begin==");
	    System.out.println(dao);
	}
}
