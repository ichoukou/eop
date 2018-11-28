package net.ytoec.kernel.dao.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import net.ytoec.kernel.dao.ApiLogDao;
import net.ytoec.kernel.dataobject.ApiLog;
import net.ytoec.kernel.mapper.ApiLogMapper;

/**
 * @作者：罗典
 * @时间：2013-09-02
 * @描述：操作接口日志记录
 * */
@Repository
public class ApiLogDaoImpl<T extends ApiLog> implements ApiLogDao<T> {
	
	
    @Inject
    private ApiLogMapper<ApiLog> mapper; 
    
    /**
	 * @作者：罗典
	 * @时间：2013-09-02
	 * @描述：新增接口日志记录
	 * @参数：log 日志
	 * */
	@Override
	public int insertApiLog(ApiLog log) {
		return mapper.insertApiLog(log);
	}

}
