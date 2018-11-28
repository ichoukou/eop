package net.ytoec.kernel.dao;

import org.springframework.dao.DataAccessException;

/**
 * 客户营销统计
 * @author mabo
 *
 * @param <T>
 */
public interface CoreEdmDao<T> {

	/*
	 * 添加一条ec_core_edm 表记录
	 */
	public boolean addCoreEdm(T entity) throws DataAccessException;
}
