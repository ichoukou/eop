package net.ytoec.kernel.test;

import java.util.Date;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dataobject.ZebraSequence;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ZebraSequenceDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ZebraSequenceDao<ZebraSequence> dao;

	// 测试根据商家代码查询密钥
	@Test
	public void testInsertZebraSequence() throws Exception {
		ZebraSequence zebraSequence = new ZebraSequence();
		String customerCode="999999";
		zebraSequence.setCustomerCode(customerCode);
		zebraSequence.setParternCode("1111");
		zebraSequence.setCreateTime(new Date());
		int sequenceId = dao.insertZebraSequence(zebraSequence);
		System.out.println(sequenceId);
	}

}
