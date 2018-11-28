package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import net.ytoec.kernel.dao.SyncLockDao;
import net.ytoec.kernel.dataobject.SyncLock;
import net.ytoec.kernel.mapper.SyncLockMapper;

/**
 * @作者：罗典
 * @时间：2013-09-02
 * @描述：控制并发表处理
 * */
@Repository
public class SyncLockDaoImpl implements SyncLockDao {
	
	@Inject
	public SyncLockMapper<SyncLock> mapper;

	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：控制并发表处理(注：此处使用了merge语法，存在则修改，否则新增)
	 * */
	/*@Override
	public int updateLockState(SyncLock lock) {
		return mapper.updateLockState(lock);
	}*/
	
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：新增锁数据
	 * */
	public int insertSyncLock(SyncLock lock){
		return mapper.insertSyncLock(lock);
	}
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：更新锁状态
	 * */
	public int updateByLockState(SyncLock lock){
		return mapper.updateByLockState(lock);
	}
	/**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：更新锁状态
	 * */
	public boolean isExistsSyncLock(SyncLock lock){
		return mapper.isExistsSyncLock(lock) > 0 ? true :false;
	}

}
