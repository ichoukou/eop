/**
 * JgWaybillDaoImpl.java
 * Wangyong
 * 2011-8-18 下午01:17:56
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.JgWaybillDao;
import net.ytoec.kernel.dataobject.JgWaybill;
import net.ytoec.kernel.mapper.JgWaybillMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
/**
 * 金刚运单接口信息数据Dao实现类
 * @author Wangyong
 * @2011-8-18
 * net.ytoec.kernel.dao.impl
 */
@Repository
public class JgWaybillDaoImpl<T extends JgWaybill> implements JgWaybillDao<T> {

	private static Logger logger = LoggerFactory.getLogger(JgWaybillDaoImpl.class);
	@Inject
	private JgWaybillMapper<JgWaybill> mapper;
	
	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.JgWaybillDao#addJgWaybill(java.lang.Object)
	 */
	@Override
	public boolean addJgWaybill(T jgWaybill) throws DataAccessException {
		
		boolean flag = false;
		logger.info("dao add jgWaybill start...");
		mapper.add(jgWaybill);
		logger.info("add jgWaybill end,mailNo="+jgWaybill.getMailNo());
		flag = true;
		return flag;
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.JgWaybillDao#removeJgWaybill(java.lang.Object)
	 */
	@Override
	public boolean removeJgWaybill(T jgWaybill) {
		
		boolean flag = false;
		try{
			mapper.remove(jgWaybill);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;

		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.JgWaybillDao#editJgWaybill(java.lang.Object)
	 */
	@Override
	public boolean editJgWaybill(T jgWaybill) {
		
		boolean flag = false;
		try{
			mapper.edit(jgWaybill);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.JgWaybillDao#getJgWaybillById(java.lang.Integer)
	 */
	@Override
	public T getJgWaybillById(Integer id) {
		
		T entity = null;
		try{
			JgWaybill bill = new JgWaybill();
			bill.setId(id);
			entity = (T)mapper.get(bill);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	/* (non-Javadoc)
	 * @see net.ytoec.kernel.dao.JgWaybillDao#getAllJgWaybill()
	 */
	@Override
	public List<T> getAllJgWaybill() {
		
		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getAllJgWaybill();
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean delJgWaybillFromJG(int limit) {
		boolean result=false;
		try{
			this.mapper.delJgWaybillFromJG(limit);
			result=true;
		} catch(Exception e){
			result=false;
			logger.error("删除数据错误", e);
		}
		return result;
	}

	@Override
	public Integer countJgwaybill() {
		Integer result=null;
		try{
			result=this.mapper.countJgwaybill();
		} catch(Exception e){
			logger.error("删除数据错误", e);
		}
		return result;
	}

}
