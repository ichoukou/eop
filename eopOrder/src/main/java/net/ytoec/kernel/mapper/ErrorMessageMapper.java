/**
 * 
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.ErrorMessage;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * @author Wangyong
 * 出错信息映射器
 */
public interface ErrorMessageMapper<T extends ErrorMessage> extends	BaseSqlMapper<T> {

	/**
	 * 查询所有出错信息数据列表
	 * @return
	 */
	public List<T> getAllErrorMessage();
	
	/**
	 * 根据出错信息类型查询出错信息列表
	 * @param errorType 出错信息类型
	 * @return
	 */
	public List<T> getErrMsgListByType(String errorType);
}
