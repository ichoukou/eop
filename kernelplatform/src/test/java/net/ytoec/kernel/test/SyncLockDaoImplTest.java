package net.ytoec.kernel.test;

import java.util.Date;

import net.ytoec.kernel.dataobject.SyncLock;

import org.junit.Test;

public class SyncLockDaoImplTest extends ParentTest {
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：控制并发表处理(注：此处使用了merge语法，存在则修改，否则新增)
	 * */
	@Test
	public void testUpdateLockState() {
		SyncLock lock = new SyncLock();
		lock.setBusinessId("11");
		lock.setCreateTime(new Date());
		lock.setCreator("luodian");
		lock.setLock(false);
		lock.setMethod("Test");
		lock.setModifier("luodian");
		lock.setUpdatetime(new Date());
		int affectCount = syncLockDao.insertSyncLock(lock);
//		boolean affectCount = syncLockDao.isExistsSyncLock(lock);
		System.out.println(affectCount); 
	}
}  
