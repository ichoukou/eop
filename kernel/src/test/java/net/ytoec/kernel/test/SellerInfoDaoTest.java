package net.ytoec.kernel.test;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SellerInfoDao;
import net.ytoec.kernel.dataobject.Mail;
import net.ytoec.kernel.service.MailService;
import net.ytoec.kernel.service.SellerInfoService;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class SellerInfoDaoTest extends AbstractJUnit38SpringContextTests {
	@Inject
	private MailService<Mail> mailService;
	@Inject
	private SellerInfoDao dao;
	@Inject
	private SellerInfoService sellerInfoService;
	// 测试查询密钥分页列表
	@Test
	public void testGetPageList() throws Exception {
		/*List list=new ArrayList();
		User user=new User();
		 User s = dao.findUserBySellerUserName("1223");
		System.out.println("密钥:"+s);*/
		//String s=sellerInfoService.searchParternBysellerUserCode("K87800004");
		
		//sellerInfoService.searchParternBysellerUserName("K87800004");
//		List list=sellerInfoService.searchUserByUserCode("xs001");
//		System.out.println(list.get(0));
		//sellerInfoService.searchUserByUserName("K87800004");
//		ZebraPartern z=new ZebraPartern();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//		Date d = df.parse(df.format(new Date()));
//		z.setCustomerCode("k432432432");
//		z.setParternCode("k432432");
//		z.setUpdateTime(d);
//		sellerInfoService.updateByPrimaryKeySelective(z);
		
		
//		User u=new User();
//		u.setUserName("fds");
//		u.setUserPassword("fd");
//		u.setUserType("1");
//		u.setUserCode("fdsaf");
//		u.setUserState("TBA");
		//sellerInfoService.updateOrInsertUser(u);
		//sellerInfoService.insertUser(u);
		
//		char[] ss = new char[8];
//		int i = 0;
//		while (i < ss.length) {
//			int f = (int) (Math.random() * 3);
//			if (f == 0)
//				ss[i] = (char) ('A' + Math.random() * 26);
//			else if (f == 1)
//				ss[i] = (char) ('a' + Math.random() * 26);
//			else
//				ss[i] = (char) ('0' + Math.random() * 10);
//			i++;
//		}
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//		Date d = df.parse(df.format(new Date()));
//		ZebraPartern z = new ZebraPartern();
//		z.setUpdateTime(d);
//		String s = new String(ss);
//		z.setCustomerCode("123456");
//		z.setParternCode(s);
//		sellerInfoService.updateByPrimaryKeySelective(z);
//		User u=sellerInfoService.searchUserByUserName("华硕品质");
//		System.out.print(u.getUserName()+u.getUserPassword());
//		User u=new User();
//		u.setUserName("hfdhscb");
//		u.setUserPassword("4QrcOUm6Wau+VuBX8g+IPg==");
//		u.setUserCode("KGDG00009");
//		sellerInfoService.updateOrInsertUser(u);
//		User u=new User();
		//String u=sellerInfoService.findParternByUserCodeAndShopName("我爱洗澡", "123456");
		//System.out.println(u);
		
//		u=sellerInfoService.findUserByUserCodeAndShopName("我爱洗澡", "123456");
//		System.out.println(u);
		
//		String u=sellerInfoService.findUserNameByUserCode("K10512263");
//		System.out.println(u);
//		List<UserThread> l=new ArrayList<UserThread>();
//		l=sellerInfoService.findUserAll("100008");
//		System.out.println(l.get(0));
//		
//		 //发送邮件
		 Mail mail = new Mail();
		 mail.setFromMail("yto.ec.core@gmail.com");
		 mail.setFromMail("1987853317@qq.com");
//		 
		 mail.setFromMailText("");
		 mail.setSendToMail("1987853317@qq.com");
		 mail.setSubject("密码修改邮件提示");
		 String mailContent = "测试邮件";
		 mail.setContent(mailContent);
		 boolean flag = mailService.sendMail(mail);
		 System.out.print(flag);
		
//		List list=new ArrayList();
//		list= sellerInfoService.searchUserByUserCode("KH4857001");
//		User u=new User();
//		u=(User) list.get(0);
//		System.out.println(u.getId());
		
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//		Date d = df.parse(df.format(new Date()));
//		Channel c=new Channel();
//		c.setUserCode("1234");
//		c.setParternId("321");
//		c.setUpdateTime(d);
		
		//String s=sellerInfoService.searchParternBysellerUserCode("122334");
		//System.out.print(s);
//		sellerInfoService.inserteByPrimaryKeySelective(c);
		
		
		
	}
	
}