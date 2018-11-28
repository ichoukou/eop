package net.ytoec.kernel.service;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.WarnValue;

/**
 * @author yangqh
 *
 * @param <T>
 */
public interface WarnValueService<T extends WarnValue>{
	
	/**
	 * 新增一个问题件预警值(先判断该卖家下的该目的地是否存在，若已存在无法添加返回false)
	 * 
	 * @param Map<String,Object>   key:bool(成功/失败)；resultString(错误信息)
	 * @return @
	 */
	public abstract String addWarnValue(T entity);
	
	/**
	 * 删除预警值()
	 * @param
	 * @return
	 */
	public abstract boolean remove(T entity);
	
	/**
	 * 修改记录
	 * @param entity
	 * @return
	 */
	public abstract String edit(T entity);
	
	/**
	 * 加载某卖家下的所有目的地 获取总条数
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public abstract List<T> searchWarnValueBySellerId(T entity) throws Exception;
	
	/**
	 * 设置预警值的时候保存设置
	 * @param map
	 */
	public boolean operatorWarnValue(Map<String, Object> map);

}
