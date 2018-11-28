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

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.dao.ArticleDao;

public class ArticleDaoTest {
	private static ArticleDao<Article> articleDao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-common.xml");
		articleDao = (ArticleDao) ctx.getBean("articleDaoImpl");
	}
	
	//@Test
	public void testAdd() {
		Article article = new Article();
		article.setTitle("EOP文章");
		article.setColumnId(2);
		article.setContent("EOP文章");
		article.setCreateTime(new Date());
		article.setCreateUser(1);
		article.setStatus(Article.STATUS_NORMAL);
		article.setSeq(1);
		boolean rs = articleDao.add(article);
		Assert.assertTrue(rs);
	}
	
	//@Test
	public void testGetMaxId(){
		Integer i = articleDao.getMaxId();
		System.out.println("maxId=="+i);
	}

	//@Test
	public void testUpdate() {
		Article article = new Article();
		article.setArticleId(1);
		article = articleDao.get(article);
		article.setUpdateUser(1);
		article.setRemark("测试修改");
		boolean rs = articleDao.edit(article);
		Assert.assertTrue(rs);
	}

	//@Test
	public void testGet() {
		Article article = new Article();
		article.setArticleId(1);
		article = (Article) articleDao.get(article);
		System.out.println("标题："+article.getTitle());
	}

	
	//@Test
	public void testGetAllArticle() {
		Map<String,Object> params = new HashMap<String, Object>();
		List<Article> articles = articleDao.getAll(params);
		if (null != articles) {
			for (Article article : articles) {
				System.out.println("标题："+article.getTitle());
			}
		}
	}
	
	@Test
	public void testGetPageList(){
		Pagination<Article> page = new Pagination<Article>(1, 1);
		Map<String, Object> params = new HashMap<String, Object>();
		
		page = articleDao.getPageList(page,params);
		System.out.println("共"+page.getTotalRecords()+"条记录，每页"+page.getPageNum()+"条，共"+page.getTotalPages()+"页");
		System.out.println("当前第"+page.getCurrentPage()+"页，数据列表如下：");
		for(Article article : page.getRecords()){
			System.out.println("标题："+article.getTitle());
		}
	}

	
}
