package net.ytoec.kernel.user;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.Md5Encryption;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserThread;
import net.ytoec.kernel.mapper.UserMapper;
import net.ytoec.kernel.mapper.UserThreadMapper;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 
 * @author ChenRen
 * @date 2011-7-25
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class UserThreadMapperTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private UserThreadMapper<UserThread> mapper;

	/**
	 * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据；
	 * 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
	 */
	private static int tempId = -1;

	@Test
	public void testAdd() {
		List<UserThread> list=mapper.getAllUser();
	    System.out.println(list.size());
	    System.out.println(mapper.getUserThreadByPostId(90).size());
	    
	}


}
