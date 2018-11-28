package net.ytoec.kernel.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.DateUtil;
import net.ytoec.kernel.dataobject.AccountUser;
import net.ytoec.kernel.service.AccountUserService;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class AccountUserServiceImplTest extends AbstractJUnit38SpringContextTests{

	@Inject
	private AccountUserService<AccountUser> accountUserService;
	
	@Test
	public void testGet() {
		   //2)如果可用余额跟余额不相等[时间超过24小时]
	     Map user_map=new HashMap();
	     SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd hh:24ss:mm");
	     List<AccountUser> accountUserList=accountUserService.getAccountUserListUseTime(user_map);
	     if(CollectionUtils.isNotEmpty(accountUserList)){
	    	 for(AccountUser AccountUser:accountUserList){
	    		 Double diff_day=  DateUtil.dayInterval(new Date(),AccountUser.getUpdateTime());
	    		 if(diff_day>1){
	    			 System.out.println("===================="+sf.format(AccountUser.getUpdateTime()));
	    			 //2.1)超过24小时
	    		 }
	    	 }			    	
	     }
	}
//	
	
//	@Test
//	public void testGetAccountUserList() {
//		
//		Map map=new HashMap();		
//		map.put("userId", 9);
//		map.put("payAccount", "taobao");
//		
//		Pagination<ServiceHistory> pagination=new Pagination<ServiceHistory>(1,100);	
//		
//		System.out.println(accountUserService.getAccountUserList(map,pagination,true).size());
//	}
	
//	@Test
//	public void testUpdateAccountUserBalanceByUserId()
//	{
//		Map map=new HashMap();
//		map.put("userId", 9);
//		map.put("money", 10);
//		map.put("bool", true);
//		
//		accountUserService.updateAccountUserBalanceByUserId(map);
//		System.out.println("执行完毕");
//	}
//	
//	@Test
//	public void testAdd() {
//		
//		AccountUser accountUser=new AccountUser();	
//		accountUser.setUserId(66);
//		accountUser.setBalance(200.0);
//		accountUser.setUseBalance(40.0);
//		accountUser.setCellPhone("18021044560");
//		accountUser.setPhoneCode("6754");
//		accountUser.setAllConsume(20.0);
//		accountUser.setAllRecharge(100.0);
//		accountUser.setRemark("测试添加");
//		accountUser.setFlag("1");
//		accountUser.setPayAccount("myAccount");
//		
//		accountUserService.add(accountUser);
//		System.out.println("-----------");
//	}
	
	@Test
	public void testEdit() {
		
		AccountUser accountUser=new AccountUser();
		accountUser.setId(1);
		accountUser.setUserId(30);
		accountUser.setBalance(0.00);
		accountUser.setUseBalance(0.00);
		accountUser.setCellPhone("18021044560");
		accountUser.setPhoneCode("6754");
		accountUser.setAllConsume(0.00);
		accountUser.setAllRecharge(0.0);
		accountUser.setRemark("更新了记录");
		accountUser.setFlag("0");
		accountUser.setPayAccount("myAccount");
		
		accountUserService.updateByAccountUser(accountUser);
		System.out.println("-----------");
	}
	
//	@Test
//	public void testRemove() {
//		
//		AccountUser accountUser=new AccountUser();
//		accountUser.setId(1);
//		accountUserService.remove(accountUser);
//		System.out.println("-----------");
//	}
	
//	@Test
//	public void testgetAccountUserByUserId() {
//		System.out.println("-----------"+accountUserService.getAccountUserByUserId(88).getPayAccount());
//	}
//	
//	@Test
//	public void testupdateAccountUserMobileByUserId() {
//		AccountUser accountUser=new AccountUser();
//		accountUser.setUserId(88);
//		accountUser.setCellPhone("15021041934");
//		accountUserService.updateAccountUserMobileByUserId(accountUser);
//		System.out.println("-----------");
//	}
	
//	@Test
//	public void testGetSumUseBalance() {
//		System.out.println(accountUserService.getSumUseBalance());
//		System.out.println(accountUserService.getSumAllConsume());
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
//		map.put("startBalance", 10);
//		map.put("endBalance", 20);
//		map.put("startTime", new Date());
//		map.put("endTime", new Date());
//		
//		Pagination<ServiceHistory> pagination=new Pagination<ServiceHistory>(1,100);	
//		
//		System.out.println(accountUserService.getAccountUserList(map,pagination,true).size());
//	}
	
	@Test
	public void testaddUserAndDService() {
		AccountUser accountUser=new AccountUser();
		accountUser.setUserId(10);
		//boolean b=accountUserService.addUserAndDService(accountUser);
		//System.out.print(b);
	}
	
}
