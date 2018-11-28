package net.ytoec.kernel.dao;

import net.ytoec.kernel.dataobject.SyncLock;
/**
 * @作者：罗典
 * @时间：2013-09-02
 * @描述：控制并发表处理
 * */
public interface SyncLockDao {
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：控制并发表处理(注：此处使用了merge语法，存在则修改，否则新增)
	 * */
//	public int updateLockState(SyncLock lock);
	
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：新增锁数据
	 * */
	public int insertSyncLock(SyncLock lock);
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：更新锁状态
	 * */
	public int updateByLockState(SyncLock lock);
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：更新锁状态
	 * */
	public boolean isExistsSyncLock(SyncLock lock);
}
