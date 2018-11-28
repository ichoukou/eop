package net.ytoec.kernel.test;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.AccountUserDao;
import net.ytoec.kernel.dataobject.AccountUser;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class AccountUserDaoImplTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private AccountUserDao<AccountUser>  accountUserDao;
	
//	@Test
//	public void testGet() {
//		AccountUser accountUser=new AccountUser();	
//		accountUser.setId(2);		
//		System.out.println(accountUserDao.get(accountUser).getRemark());
//	}
//	
//	@Test
//	public void testGetAccountUserList() {
//		
//		Map map=new HashMap();
//		map.put("startIndex", 0);
//		map.put("pageNum", 60);
//		map.put("userId", 10);
//		map.put("payAccount", "tyioooo");
//		
//		System.out.println(accountUserDao.getAccountUserList(map).size());
//	}
	
//	@Test
//	public void testUpdateAccountUserBalanceLowerByUserId()
//	{
//		Map map=new HashMap();
//		map.put("userId", 9);
//		map.put("money", 10);
//		
//		accountUserDao.updateAccountUserBalanceLowerByUserId(map);
//		System.out.println("执行完毕");
//	}
//	
//	@Test
//	public void testUpdateAccountUserBalanceAddByUserId()
//	{
//		Map map=new HashMap();
//		map.put("userId", 9);
//		map.put("money", 10);
//		
//		accountUserDao.updateAccountUserBalanceAddByUserId(map);
//		System.out.println("执行完毕");
//	}
	
//	@Test
//	public void testUpdateSendPhoneCode()
//	{
//		Map map=new HashMap();
//		map.put("userId", 9);
//		map.put("cellPhone", "1864568213");
//		map.put("phoneCode", "4444");
//		
//		accountUserDao.updateSendPhoneCode(map);
//		System.out.println("执行完毕");
//	}
	
//	@Test
//	public void testUpdateAccountUserMobileByUserId()
//	{
//		AccountUser accountUser=new AccountUser();
//		accountUser.setUserId(9);
//		accountUser.setCellPhone("13663078236");
//		accountUserDao.updateAccountUserMobileByUserId(accountUser);
//		System.out.println("执行完毕");
//	}
//	
	@Test
	public void testgetAccountUserByUserId()
	{	
		System.out.println("执行完毕"+accountUserDao.getAccountUserByUserId(9).getBalance());
	}
	
}
