package net.ytoec.kernel.service;

import java.util.List;

/**
 * 区域信息service接口
 * 
 * @author Wangyong
 * @param <T>
 */
public interface RegionService<T> {

    /**
     * 增加区域信息数据对象
     * 
     * @param region
     * @return @
     */
    public boolean addRegion(T region);

    /**
     * 删除区域信息数据对象
     * 
     * @param region
     * @return @
     */
    public boolean removeRegion(T region);

    /**
     * 更新区域信息数据对象
     * 
     * @param region
     * @return @
     */
    public boolean editRegion(T region);

    /**
     * 根据id查询地市信息
     * 
     * @param id
     * @return @
     */
    public T getRegionById(Integer id);

    /**
     * 根据id号查询属于该id下的地市信息列表
     * 
     * @param parentId
     * @return @
     */
    public List<T> getRegionByParentId(Integer parentId);

    /**
     * 查询所有省级区域，用于在初始显示时显示全部省级区域或者直辖市、自治区等
     * 
     * @return @
     */
    public List<T> getProvinceRegion();


    /**
     * 获取所有地区（省，市，地区）
     * 
     * @return
     */
    public List<T> getAllRegion();
    
    
    /**
     * 查询所有的省份，除去港澳台
     * @param list
     * @return
     */
    public List<T> getAllProvince(List<Integer> list);
    
}
