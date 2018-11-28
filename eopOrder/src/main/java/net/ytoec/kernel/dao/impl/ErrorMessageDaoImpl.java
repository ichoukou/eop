/**
 * 
 */
package net.ytoec.kernel.dao.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.ErrorMessageDao;
import net.ytoec.kernel.dataobject.ErrorMessage;
import net.ytoec.kernel.mapper.ErrorMessageMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
/**
 * 出错信息数据访问实现类
 * @author Wangyong
 *
 * @param <T>
 */
@Repository
public class ErrorMessageDaoImpl<T extends ErrorMessage> implements ErrorMessageDao<T> {

	private static Logger logger = LoggerFactory.getLogger(ErrorMessageDaoImpl.class);
	@Inject
	private ErrorMessageMapper<ErrorMessage> mapper;
	
	@Override
	public boolean addErrorMessage(T errorMessage) {

		boolean flag = false;
		try{
			mapper.add(errorMessage);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public T getErrorMessageById(Integer id) {

		ErrorMessage errMsg = new ErrorMessage();
		T entity = null;
		try{
			errMsg.setId(id);
			entity = (T)mapper.get(errMsg);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@Override
	public List<T> getAllErrorMessage() {

		List<T> list = null;
		try{
			list = (List<T>)this.mapper.getAllErrorMessage();
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@Override
	public boolean removeErrorMessage(T errorMessage) {

		boolean flag = false;
		try{
			mapper.remove(errorMessage);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editErrorMessage(T errorMessage) {

		boolean flag = false;
		try{
			mapper.edit(errorMessage);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

}
