/**
 * JgWaybillDaoImpl.java
 * Wangyong
 * 2011-8-18 下午01:17:56
 */
package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.JgWaybillUpdateDao;
import net.ytoec.kernel.dataobject.JgWaybillUpdate;
import net.ytoec.kernel.mapper.JgWaybillUpdateMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
/**
 * 金刚运单接口信息数据Dao实现类
 * @author Wangyong
 * @2011-8-18
 * net.ytoec.kernel.dao.impl
 */
@Repository
public class JgWaybillUpdateDaoImpl<T extends JgWaybillUpdate> implements JgWaybillUpdateDao<T> {

	private static Logger logger = LoggerFactory.getLogger(JgWaybillDaoImpl.class);
	@Inject
	private JgWaybillUpdateMapper<JgWaybillUpdate> mapper;
	
	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.JgWaybillDao#addJgWaybill(java.lang.Object)
	 */
	@Override
	public boolean addJgWaybillUpdate(T jgWaybillUpdate) {
		
		boolean flag = false;
		try{
			mapper.add(jgWaybillUpdate);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag = false;
		}
		return flag;
	}

	
}
