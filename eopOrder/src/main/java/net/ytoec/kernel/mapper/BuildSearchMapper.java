package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

/**
 * <p>
 * ClassName: BuildSearchMapper
 * </p>
 * <p>
 * Description: TODO
 * </p>
 * <p>
 * Author:
 * </p>
 * <p>
 * Date: Oct 25, 2013
 * </p>
 */
public interface BuildSearchMapper<T extends BuildSearch> extends BaseSqlMapper<T> {

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     */
    public List<T> getBuildSearchByLimit(int limit);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     */
    public List<T> getBuildSearchForAddByLimit(int limit);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     */
    public Boolean removeBuildSearchByLimit(int limit);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     */
    public Boolean removeBuildSearchForAddByLimit(int limit);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public Boolean removeBuildSearchById(Map<String, String> map);

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param map
     * @return
     */
    public Boolean removeBuildSearchForAddById(Map<String, String> map);

    /**
     * 统计build_search表的数量
     * 
     * @return
     */
    public Integer countBuildSearch();

    /**
     * 更新订单信息时新增一条记录到ec_core_build_search
     * 
     * @param entity
     * @throws DataAccessException
     */
    public void addBuildSearch(T entity) throws DataAccessException;

    /**
     * 更新订单信息时新增一条记录到ec_core_build_search_foradd表
     * 
     * @param entity
     * @throws DataAccessException
     */
    public void addBuildSearchForAdd(T entity) throws DataAccessException;

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @return
     */
    public Integer countBuildSearchForupdateFour();

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @return
     */
    public Integer countBuildSearchForupdateThree();

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @return
     */
    public Integer countBuildSearchForupdateTwo();

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @return
     */
    public Integer countBuildSearchForupdate();

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @return
     */
    public Integer countBuildSearchForadd();

}
