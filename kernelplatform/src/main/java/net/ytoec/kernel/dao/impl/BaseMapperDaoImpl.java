package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.BaseMapperDao;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
@SuppressWarnings("unchecked")
@Repository
public class BaseMapperDaoImpl<T> extends SqlSessionTemplate implements
		BaseMapperDao<T> {
    
	private static Logger logger = LoggerFactory.getLogger(BaseMapperDaoImpl.class);
	@Inject
	public BaseMapperDaoImpl(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	private Class<? extends BaseSqlMapper> mapperClass;

	public void setMapperClass(Class<? extends BaseSqlMapper> mapperClass) {
		this.mapperClass = mapperClass;
	}

	public BaseSqlMapper<T> getMapper() {
		return this.getMapper(mapperClass);
	}

	public boolean add(T entity) throws Exception {
		boolean flag = false;
		try {
			this.getMapper().add(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
			throw e;
		}
		return flag;
	}

	public boolean edit(T entity) throws Exception {
		boolean flag = false;
		try {
			this.getMapper().edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	public T get(T entity) throws Exception {
		return this.getMapper().get(entity);
	}

	public List<T> getAll() throws Exception {
		return this.getMapper().getList(null);
	}

	public boolean remove(T entity) throws Exception {
		boolean flag = false;
		try {
			this.getMapper().remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	public List<T> getAll(T entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
