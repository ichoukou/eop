package net.ytoec.kernel.test;

import java.util.Date;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ApiLogDao;
import net.ytoec.kernel.dataobject.ApiLog;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

@ContextConfiguration("classpath*:applicationContext-common.xml")
public class ApiLogDaoImplTest extends AbstractJUnit38SpringContextTests{
	@Inject
	private ApiLogDao<ApiLog> apiLogDao;

	@Test
	public void testInsertApiLog() {
		ApiLog apiLog = new ApiLog();
//		apiLog.setId(1);
		apiLog.setCreateTime(new Date());
		apiLog.setLastUpdateTime(new Date());
		apiLog.setException(true);
		apiLog.setLogType("logType");
		apiLog.setUsedtime(1l);
		apiLog.setRequestMsg("requestMsg");
		apiLog.setResponseMsg("responseMsg");
		apiLog.setExceptionMsg("exceptionMsg");
		apiLog.setRequestIP("requestIP");
		apiLog.setBusinessId("businessId");
		apiLog.setDescription("description");
		apiLogDao.insertApiLog(apiLog);
	}

}
