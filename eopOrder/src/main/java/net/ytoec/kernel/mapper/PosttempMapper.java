package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * 
 * @author 	ChenRen
 * @date 	2011-09-09
 */
public interface PosttempMapper<T extends Posttemp> extends BaseSqlMapper<T> {

	/**
	 * 根据网点Id(在数据库等于创建者Id)查询运费模板
	 * @param userId
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempByBranchId(Map map);
	
	/**
	 * 根据模板类型查询运费模板
	 * @param type	模板类型: 1为系统模板, 其余暂时为空
	 * @author ChenRen/2011-10-21
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getPosttempByType(String type);

}
