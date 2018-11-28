/**
 * 
 */
package net.ytoec.kernel.dao;

import java.util.List;

import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.dataobject.TraderInfo;

import org.springframework.dao.DataAccessException;

/**
 * 省市县区信息表访问接口
 * @author Wangyong
 */
public interface RegionDao<T> {

	/**
	 * 增加区域信息数据对象
	 * @param region
	 * @return
	 * @throws DataAccessException
	 */
	public boolean addRegion(T region) throws DataAccessException;
	
	/**
	 * 删除区域信息数据对象
	 * @param region
	 * @return
	 * @throws DataAccessException
	 */
	public boolean removeRegion(T region) throws DataAccessException;
	
	/**
	 * 更新区域信息数据对象
	 * @param region
	 * @return
	 * @throws DataAccessException
	 */
	public boolean editRegion(T region) throws DataAccessException;
	
	/**
	 * 根据id查询地市信息
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public T getRegionById(Integer id) throws DataAccessException;
	
	/**
	 * 根据父id号查询属于该id下的地市信息列表
	 * @param parentId
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getRegionByParentId(Integer parentId) throws DataAccessException;
	
	/**
	 * 获取所有信息列表
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> getAllRegion() throws DataAccessException;
	
	/**
	 * 根据prov list 查询provice
	 */
	public List<Region> getRegion(List<TraderInfo> regList) ;
	
	/**
	 * 查询所有的省份，除去港澳台
	 * @param list
	 * @return
	 */
	public List<T> getAllProvince(List<Integer> list);
}
