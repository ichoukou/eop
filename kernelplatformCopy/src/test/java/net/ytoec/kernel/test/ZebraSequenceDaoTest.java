package net.ytoec.kernel.test;

import java.util.Date;

import javax.inject.Inject;

import net.ytoec.kernel.common.Constant;
import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.util.DateUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-common.xml")
public class ZebraSequenceDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ZebraSequenceDao<ZebraSequence> dao;

	@Value("${synWaybilIntervalTime}")
	private int synWaybilIntervalTime;

	private String sdf = "yyyy-MM-dd HH:mm:ss";

	@Test
	public void testIsExistSequence() {
		boolean isExist = dao.isExistSequence("99999900",Constant.SEQ_STATE_PROCESS,500);
		System.out.println(isExist+"-------------------");
	}
	
	@Test
	public void testUpdateStateById(){
		dao.updateStateById("44136", 2);
	}
	
	@Test
	public void testQueryZebraSequence(){
		ZebraSequence sequence = dao.queryZebraSequence("44136");
		System.out.println(sequence.getTotalCount());
	}
	
	// 测试添加请求序列
	@Test
	public void testInsertZebraSequence() throws Exception {
		ZebraSequence zebraSequence = new ZebraSequence();
		String customerCode = "99999900";
		zebraSequence.setCustomerCode(customerCode);
		zebraSequence.setParternCode("1111");
		zebraSequence.setCreateTime(new Date());
		int sequenceId = dao.insertZebraSequence(zebraSequence);
		System.out.println(zebraSequence.getSequenceId());
	}

	// 测试查询最近一次状态为第一次请求中的请求序列的第一次请求时间
	@Test
	public void testSelectLastSyningTime() {
		String customerCode = "K10101010";
		String lastSyningTime = dao.selectLastSyningTime(customerCode);
		System.out.print(lastSyningTime);
		Date date = DateUtil.valueof(lastSyningTime, sdf);
		double intervalTime=DateUtil.minuteInterval(new Date(), date);
		System.out.print("intervalTime:" + intervalTime);

	}

}
