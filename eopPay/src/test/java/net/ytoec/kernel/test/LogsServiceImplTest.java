package net.ytoec.kernel.test;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.service.LogsService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class LogsServiceImplTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private LogsService<Logs>   logsServiceImpl;
	
	
//	@Test
//	public void testAdd()
//	{
//		Logs logs=new Logs();		
//		logs.setOperName("4");
//		logs.setUserId(66);
//		logs.setOperType("8");
//		logs.setRemark("添加");
//		
//		logsServiceImpl.add(logs);
//		System.out.println("添加完毕");
//	}
	
	@Test
	public void testRemove()
	{
		Logs logs=new Logs();		
		logs.setId(2);
		
		logsServiceImpl.remove(logs);
		System.out.println("shanchu 完毕");
	}
}
