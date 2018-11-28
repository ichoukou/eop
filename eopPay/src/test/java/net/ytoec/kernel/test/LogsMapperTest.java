package net.ytoec.kernel.test;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.dataobject.Logs;
import net.ytoec.kernel.mapper.LogsMapper;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class LogsMapperTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private LogsMapper<Logs>  mapper;
	
//	@Test
//	public void testAdd() {
//		
//		Logs logs=new Logs();
//		logs.setId(1);
//		logs.setOperName("4");
//		logs.setUserId(10);
//		logs.setOperType("7");
//		logs.setRemark("备注信息");
//		
//		mapper.add(logs);
//		System.out.println("-----------");
//	}
	
	@Test
	public void testRemove() {
		
		Logs logs=new Logs();
		logs.setId(1);
		mapper.remove(logs);
		System.out.println("-----------");
	}
}

