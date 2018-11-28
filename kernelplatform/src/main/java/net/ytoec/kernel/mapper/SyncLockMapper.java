package net.ytoec.kernel.mapper;

import net.ytoec.kernel.dataobject.SyncLock;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface SyncLockMapper<T extends SyncLock> extends BaseSqlMapper<T> {
//	public int updateLockState(SyncLock lock);
	
	public int insertSyncLock(SyncLock lock);
	public int updateByLockState(SyncLock lock);
	public int isExistsSyncLock(SyncLock lock);
}
