package com.ytoec.cms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.service.ArticleService;

public class ArticleServiceTest {
	
	private static ArticleService<Article> articleService;
	
	@BeforeClass
	@SuppressWarnings("unchecked")
	public static void setBeforeClass(){
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
		"classpath*:spring/spring-*.xml");
		articleService = (ArticleService<Article>) ctx.getBean("articleServiceImpl");
	}
	
	//@Test
	public void testAdd() {
		Article article = new Article();
		article.setTitle("测试插入");
		article.setContent("测试内容");
		article.setCreateTime(new Date());
		articleService.add(article);
	}

	//@Test
	public void testGetMaxId(){
		Integer i = articleService.getMaxId();
		System.out.println("maxId=="+i);
	}

	//@Test
	public void testUpdate() {
		Article article = new Article();
		article = articleService.get(1);
		article.setTitle("测试修改");
		article.setContent("bbbbbb");
		article.setUpdateTime(new Date());
		articleService.edit(article);
	}

	@Test
	public void testGet() {
		Article article = (Article) articleService.get(1);
		System.out.println("标题："+article.getTitle());
	}
	
	
	//@Test
	public void testGetPageList(){
		Pagination<Article> page = new Pagination<Article>(1, 2);
		//page.setCurrentPage(2);
		Map<String, Object> params = new HashMap<String, Object>();
		
		page = articleService.getPageList(page,params);
		System.out.println("共"+page.getTotalRecords()+"条记录，共"+page.getTotalPages()+"页");
		for(Article article : page.getRecords()){
			System.out.println("标题："+article.getTitle());
		}
	}
	

}
