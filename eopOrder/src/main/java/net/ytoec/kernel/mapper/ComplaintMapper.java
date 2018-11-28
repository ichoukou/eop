/**
 * net.ytoec.kernel.mapper
 * ComplaintMapper.java
 * 2012-7-10下午02:58:16
 * @author wangyong
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.Complaint;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * @author wangyong
 * 2012-7-10
 */
public interface ComplaintMapper<T extends Complaint> extends BaseSqlMapper<T> {
	
	/**
	 * 根据运单号查询
	 * @param mailNo
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getByMailNo(String mailNo) throws DataAccessException;
}
