package net.ytoec.kernel.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.mapper.AccountUserMapper;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class AccountUserMapperTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private AccountUserMapper<AccountUser>  mapper;
	
//	@Test
//	public void testAdd() {
//		
//		AccountUser accountUser=new AccountUser();	
//		accountUser.setUserId(10);
//		accountUser.setBalance(100.0);
//		accountUser.setUseBalance(80.0);
//		accountUser.setCellPhone("15021044560");
//		accountUser.setPhoneCode("6754");
//		accountUser.setAllConsume(20.0);
//		accountUser.setAllRecharge(100.0);
//		accountUser.setRemark("测试添加一条记录");
//		accountUser.setFlag("1");
//		accountUser.setPayAccount("tyioooo");
//		
//		mapper.add(accountUser);
//		System.out.println("-----------");
//	}
	
//	@Test
//	public void testEdit() {
//		
//		AccountUser accountUser=new AccountUser();	
//		accountUser.setId(3);
//		accountUser.setUserId(9);
//		accountUser.setBalance(10.8);
//		accountUser.setUseBalance(50.0);
//		accountUser.setCellPhone("15000479773");
//		accountUser.setPhoneCode("YYYY");
//		accountUser.setAllConsume(40.0);
//		accountUser.setAllRecharge(600.0);
//		accountUser.setRemark("更新了信息");
//		accountUser.setFlag("0");
//		accountUser.setPayAccount("taobao");		
//		mapper.edit(accountUser);
//		System.out.println("-----------");
//	}
	
//	@Test
//	public void testRemove() {
//		
//		AccountUser accountUser=new AccountUser();	
//		accountUser.setId(4);
//		mapper.remove(accountUser);
//		System.out.println("-----------");
//	}
	
//	@Test
//	public void testGet() {
//		
//		AccountUser accountUser=new AccountUser();	
//		accountUser.setId(2);		
//		System.out.println(mapper.get(accountUser).getRemark());
//	}
	
	@Test
	public void testGetAccountUserList() throws ParseException {
		
		Map map=new HashMap();
		map.put("startIndex", 0);
		map.put("pageNum", 60);
		//map.put("userId", 10);
//		map.put("payAccount", "tyioooo");
//		map.put("startBalance", 10);
//		map.put("endBalance", 20);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		map.put("startTime", sdf.parse("2012-07-03 00:00:00"));
//		map.put("endTime", sdf.parse("2012-07-06 23:59:59"));
//	    int[] userIdArray =new int[]{10,11,12,13,14,15};
//	    List<Integer> userIdArray =new LinkedList<Integer>();
//	    userIdArray.add(10);
//	    userIdArray.add(12);
//	    userIdArray.add(13);
//	    userIdArray.add(14);
		//map.put("userIdArray", userIdArray);
		map.put("startBalance", 100);
		
		
		
		System.out.println(mapper.getAccountUserList(map).size());
	}
	
//	@Test
//	public void testUpdateAccountUserBalanceLowerByUserId()
//	{
//		Map map=new HashMap();
//		map.put("userId", 9);
//		map.put("money", 10);
//		
//		mapper.updateAccountUserBalanceLowerByUserId(map);
//		System.out.println("执行完毕");
//	}
	
//	@Test
//	public void testUpdateAccountUserBalanceAddByUserId()
//	{
//		Map map=new HashMap();
//		map.put("userId", 9);
//		map.put("money", 10);
//		
//		mapper.updateAccountUserBalanceAddByUserId(map);
//		System.out.println("执行完毕");
//	}
	
//	@Test
//	public void testUpdateSendPhoneCode()
//	{
//		Map map=new HashMap();
//		map.put("userId", 9);
//		map.put("cellPhone", "6943340");
//		map.put("phoneCode", "6666");
//		
//		mapper.updateSendPhoneCode(map);
//		System.out.println("执行完毕");
//	}
	
//	@Test
//	public void testUpdateAccountUserMobileByUserId()
//	{
//		AccountUser accountUser=new AccountUser();
//		accountUser.setUserId(9);
//		accountUser.setCellPhone("5555");
//		mapper.updateAccountUserMobileByUserId(accountUser);
//		System.out.println("执行完毕");
//	}
	
//	@Test
//	public void testgetAccountUserByUserId()
//	{	
//		System.out.println("执行完毕"+mapper.getAccountUserByUserId(9).getAllRecharge());
//	}
	
//	@Test
//	public void testGetSumUseBalance() {
//		System.out.println(mapper.getSumUseBalance());
//		System.out.println(mapper.getSumAllConsume());
//	}
}


