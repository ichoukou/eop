package net.ytoec.kernel.test;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.PayServiceDao;
import net.ytoec.kernel.dataobject.PayService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class PayServiceDaoImplTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private PayServiceDao<PayService>  payServiceDao;
	
	@Test
	public void test()
	{
		List<PayService> list=payServiceDao.getFreeServiceList();
		
		for(int i=0;i<list.size();i++)
		{
			System.out.println(list.get(i).getId()+","+list.get(i).getName());
		}
	}
}
