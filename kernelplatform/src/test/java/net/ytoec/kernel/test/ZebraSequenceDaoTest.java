package net.ytoec.kernel.test;

import java.util.Date;
import java.util.List;

import net.ytoec.kernel.common.Constant;
import net.ytoec.kernel.dataobject.QuerySequenceCondition;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.util.DateUtil;

import org.junit.Test;

public class ZebraSequenceDaoTest extends  ParentTest{

	private String sdf = "yyyy-MM-dd HH:mm:ss";

	@Test
	public void testQueryByCondition(){
		QuerySequenceCondition querySequenceCondition = new QuerySequenceCondition();
//		querySequenceCondition.setSequence("sequence");
		querySequenceCondition.setCustomerCode("K10101011");
//		querySequenceCondition.setStartDate(new Date());
		querySequenceCondition.setEndDate(new Date());
		querySequenceCondition.setStartLine(1);
		querySequenceCondition.setLimit(1);
		List<ZebraSequence> sequenceList = zebraSequenceDao.queryByCondition(querySequenceCondition);	
		System.out.println(sequenceList.size());
	}
	
	@Test 
	public void testIsExistSequence() {
		boolean isExist = zebraSequenceDao.isExistSequence("99999900",Constant.SEQ_STATE_PROCESS,500);
		System.out.println(isExist+"-------------------");
	}
	 
	@Test
	public void testUpdateStateById(){
		zebraSequenceDao.updateStateById("44136", 2);
	}
	
	@Test
	public void testQueryZebraSequence(){
		ZebraSequence sequence = zebraSequenceDao.queryZebraSequence("44136");
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
		int sequenceId = zebraSequenceDao.insertZebraSequence(zebraSequence);
		System.out.println(sequenceId);
	}

	// 测试查询最近一次状态为第一次请求中的请求序列的第一次请求时间
	@Test
	public void testSelectLastSyningTime() {
		String customerCode = "K10101010";
		String lastSyningTime = zebraSequenceDao.selectLastSyningTime(customerCode);
		System.out.print(lastSyningTime);
		Date date = DateUtil.valueof(lastSyningTime, sdf);
		double intervalTime=DateUtil.minuteInterval(new Date(), date);
		System.out.print("intervalTime:" + intervalTime);

	}

}
