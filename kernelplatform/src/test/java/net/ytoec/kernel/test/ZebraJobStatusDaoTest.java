package net.ytoec.kernel.test;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ZebraJobStatusDao;
import net.ytoec.kernel.dataobject.ZebraJobStatus;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ZebraJobStatusDaoTest extends AbstractJUnit38SpringContextTests {

	@Inject
	private ZebraJobStatusDao<ZebraJobStatus> dao;

	// 测试更新任务状态
	@Test
	public void testupdateJobStatus() throws Exception {
		ZebraJobStatus jobStatus = new ZebraJobStatus();
		jobStatus.setJobName("waybillJob");
		jobStatus.setStatus(1);
		dao.updateJobStatus(jobStatus);
	}
}