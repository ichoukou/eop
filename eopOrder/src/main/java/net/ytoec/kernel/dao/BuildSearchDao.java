package net.ytoec.kernel.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 * <p>
 * ClassName: BuildSearchDao
 * </p>
 * <p>
 * Description: 订单BuildSearchDao
 * </p>
 * <p>
 * Author: 
 * </p>
 * <p>
 * Date: Oct 24, 2013
 * </p>
 */
public interface BuildSearchDao<T> {

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     */
    public List<T> getBuildSearchByLimit(Integer limit) throws DataAccessException;

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     */
    public Boolean removeBuildSearchByLimit(Integer limit) throws DataAccessException;

    /**
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param minId
     * @param maxId
     * @return
     * @throws DataAccessException
     */
    public Boolean removeBuildSearchById(int minId, int maxId) throws DataAccessException;

    /**
     * 统计build_search表的数量
     * 
     * @return
     */
    public Integer countBuildSearch();

    /**
     * 创建build_search信息
     * 
     * @param param
     * @return
     * @author liuchunyan
     */
    public boolean addBuildSearch(T entity);

    /**
     * 订单更新时创建build_search信息
     * 
     * @param param
     * @return
     * @author liuchunyan
     */
    public boolean addBuildSearchByUpdateOrder(T entity);

    /**
     * 订单更新时创建build_search_foradd信息
     * 
     * @param param
     * @return
     * @author liuchunyan
     */
    public boolean addBuildSearchForAddByOrder(T entity);

    /**
     * 根据条件获取订单信息
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     */
    public List<T> getBuildSearchForAddByLimit(Integer limit) throws DataAccessException;

    /**
     * 根据条件删除数据
     * 
     * @param limit
     * @return
     * @throws DataAccessException
     */
    public Boolean removeBuildSearchForAddByLimit(Integer limit) throws DataAccessException;

    /**
     * 根据ID删除数据
     * 
     * @param minId
     * @param maxId
     * @return
     * @throws DataAccessException
     */
    public Boolean removeBuildSearchForAddById(int minId, int maxId) throws DataAccessException;

    /**
     * <p>
     * Description: 监控ec_core_build_search_forupdate4
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForupdateFour();

    /**
     * <p>
     * Description: 监控ec_core_build_search_forupdate3
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForupdateThree();

    /**
     * <p>
     * Description: 监控ec_core_build_search_forupdate2
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForupdateTwo();

    /**
     * <p>
     * Description: 监控ec_core_build_search_forupdate
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForupdate();

    /**
     * <p>
     * Description: 监控ec_core_build_search_foradd表
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForadd();

}
