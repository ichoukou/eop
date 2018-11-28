package net.ytoec.kernel.test;

import net.ytoec.kernel.dao.SyncLockDao;
import net.ytoec.kernel.dao.ZebraForewarnDao;
import net.ytoec.kernel.dao.ZebraSequenceDao;
import net.ytoec.kernel.dao.ZebraSurfacebillDao;
import net.ytoec.kernel.dataobject.ZebraForewarn;
import net.ytoec.kernel.dataobject.ZebraSequence;
import net.ytoec.kernel.dataobject.ZebraSurfacebill;
import net.ytoec.kernel.service.ZebraSurfacebillService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ParentTest {
	public static ZebraSequenceDao<ZebraSequence> zebraSequenceDao ;
	public static SyncLockDao syncLockDao;
	public static ZebraSurfacebillDao<ZebraSurfacebill> zebraSurfacebillDao;
	public static ZebraForewarnDao<ZebraForewarn> zebraForewarnDao;
	public static ZebraSurfacebillService zebraSurfacebillService;
	
	static{
		ApplicationContext context =new ClassPathXmlApplicationContext("applicationContext-test.xml");
		zebraSequenceDao = (ZebraSequenceDao<ZebraSequence>) context.getBean("zebraSequenceDao");
		syncLockDao = (SyncLockDao)context.getBean("syncLockDao");
		zebraSurfacebillDao = (ZebraSurfacebillDao<ZebraSurfacebill>) context.getBean("zebraSurfacebilldaoImpl");
		zebraForewarnDao = (ZebraForewarnDao<ZebraForewarn>) context.getBean("zebraForewarnDaoImpl");
		zebraSurfacebillService = (ZebraSurfacebillService) context.getBean("zebraSurfacebillServiceImpl");
	}
}
  
