package net.ytoec.kernel.test;

import java.util.Date;

import javax.inject.Inject;

import net.ytoec.kernel.dao.CountSellerInfoDao;
import net.ytoec.kernel.dataobject.CountSellerInfo;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class CountSellerInfoDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private CountSellerInfoDao<CountSellerInfo> dao;

	@Test
	public void testSelectByPhone() {
		String phone = "1";
		CountSellerInfo countSellerInfo = dao.selectByPhone(phone);
		System.out.println(countSellerInfo);
	}

	@Test
	public void testAddCountSellerInfo() {
		Date date = new Date();
		CountSellerInfo countSellerInfo = new CountSellerInfo();
		countSellerInfo.setBuyNum(1);
		countSellerInfo.setCreateTime(date);
		countSellerInfo.setUpdateTime(date);
		countSellerInfo.setPhone("123333333");
		dao.insertCountSellerInfo(countSellerInfo);
	}

	@Test
	public void testUpdateCountSellerInfo() {
		int sid = 1;
		CountSellerInfo countSellerInfo = new CountSellerInfo();
		countSellerInfo.setSid(sid);
		countSellerInfo.setBuyNum(2);
		countSellerInfo.setUpdateTime(new Date());
		System.out.println(countSellerInfo);
	}

}
