package net.ytoec.kernel.search.service;

import java.util.List;

public interface BuildSearchService<T> {

    public List<T> getBuildSearchByLimit(Integer n);

    public Boolean removeBuildSearchByLimit(Integer n);

    /**
     * 统计build_search表的数量
     * 
     * @return
     */
    public Integer countBuildSearch();

    /**
     * 统计build_search_status_weight_index表的数量
     * 
     * @return
     */
    public Integer countBuildSearchStatusWeightIndex();

    /**
     * 统计solr状态增量分压表 version
     * 
     * @return
     */
    public Integer countBuildSearchStatusWeightVersion();

    /**
     * 统计solr状态增量分压表pressure
     * 
     * @return
     */
    public Integer countBuildSearchStatusWeightIndexPressure();
    
    /**
     * <p>
     * Description: 监控ec_core_build_search_status_weight
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchStatusWeight();

    /**
     * <p>
     * Description: 监控ec_core_build_search_foradd表
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForadd();

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
     * Description: 监控ec_core_build_search_forupdate2
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForupdateTwo();

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
     * Description: 监控ec_core_build_search_forupdate4
     * </p>
     * 
     * @return Integer 记录数
     */
    public Integer countBuildSearchForupdateFour();

}
