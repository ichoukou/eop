package com.ytoec.cms.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ytoec.cms.bean.Column;
import com.ytoec.cms.dao.ColumnDao;

public class ColumnDaoTest {
	private static ColumnDao<Column> columnDao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-common.xml");
		columnDao = (ColumnDao) ctx.getBean("columnDaoImpl");
	}
	
	//@Test
	public void testAdd() {
		Column column = new Column();
		//column.setColumnId(1);
		column.setColumnName("测试栏目2");
		column.setColumnCode("col_test");
		column.setCreateTime(new Date());
		column.setCreateUser(1);
		column.setUpdateTime(new Date());
		column.setUpdateUser(1);
		column.setLevel(0);
		column.setRootId(1);
		column.setRemark("测试栏目可随意修改。");
		boolean re = columnDao.add(column);
		Assert.assertTrue(re);
	}
	
	//@Test
	public void testUpdate() {
		Column column = new Column();
		column.setColumnId(3);
		column = columnDao.get(column);
		column.setSeq(1);
		column.setColumnCode("col_test2");
		boolean rs = columnDao.edit(column);
		Assert.assertTrue(rs);
	}

	//@Test
	public void testGet() {
		Column column = new Column();
		column.setColumnId(1);
		column = (Column) columnDao.get(column);
		System.out.println("栏目名称："+column.getColumnName());
	}

	
	//@Test
	public void testGetAllColumn() {
		Map<String,Object> params = new HashMap<String, Object>();
		List<Column> columns = columnDao.getAll(params);
		if (null != columns) {
			for (Column column : columns) {
				System.out.println("栏目名称："+column.getColumnName());
			}
		}
	}
	
	@Test
	public void testGetPageList(){
		Pagination<Column> page = new Pagination<Column>(1, 1);
		//page.setCurrentPage(2);
		Map<String, Object> params = new HashMap<String, Object>();
		
		page = columnDao.getPageList(page,params);
		System.out.println("共"+page.getTotalRecords()+"条记录，共"+page.getTotalPages()+"页");
		System.out.println("当前第"+page.getCurrentPage()+"页，数据列表如下：");
		for(Column column : page.getRecords()){
			System.out.println("栏目名称："+column.getColumnName());
		}
	}

	
}
