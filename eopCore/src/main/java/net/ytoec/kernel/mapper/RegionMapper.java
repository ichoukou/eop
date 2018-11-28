/**
 * 
 */
package net.ytoec.kernel.mapper;

import java.util.List;

import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * @author Wangyong
 * 区域信息映射器.
 */
public interface RegionMapper<T extends Region> extends BaseSqlMapper<T> {

	/**
	 * 获取所有区域信息列表
	 * @return
	 */
	public List<T> getAllRegion();
	
	/**
	 * 根据父级id号查询当前该父级Id号下的一级子区域列表
	 * @param parentId 父级id号
	 * @return
	 */
	public List<T> getRegionListByParentId(Integer parentId);
	
	/**
	 * 根据region_name查询region
	 * @param strList
	 * @return
	 */
	public Region getRegion(String strRegionName);
	
	/**
	 * 查询所有的省份，除去港澳台
	 * @param list
	 * @return
	 */
	public List<T> getAllProvince(List list);
}
