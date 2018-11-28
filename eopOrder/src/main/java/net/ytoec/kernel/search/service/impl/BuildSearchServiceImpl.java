package net.ytoec.kernel.search.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.BuildSearchDao;
import net.ytoec.kernel.dao.BuildSearchStatusWeightIndexDao;
import net.ytoec.kernel.dataobject.BuildSearch;
import net.ytoec.kernel.search.service.BuildSearchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BuildSearchServiceImpl<T extends BuildSearch> implements BuildSearchService<T> {

    private static Logger logger = LoggerFactory.getLogger(BuildSearchServiceImpl.class);

    @Inject
    private BuildSearchDao<T> dao;

    @Inject
    private BuildSearchStatusWeightIndexDao<T> statusWeightIndexDao;

    @Override
    @Transactional(readOnly = true)
    public List<T> getBuildSearchByLimit(Integer n) {

        return this.dao.getBuildSearchByLimit(n);
    }

    @Override
    public Boolean removeBuildSearchByLimit(Integer n) {
        if (n == null) {
            logger.error("n is empity");
            return false;
        }

        return dao.removeBuildSearchByLimit(n);
    }

    @Override
    public Integer countBuildSearch() {
        return dao.countBuildSearch();
    }

    @Override
    public Integer countBuildSearchStatusWeightIndex() {
        return statusWeightIndexDao.countBuildSearchStatusWeightIndex();
    }

    @Override
    public Integer countBuildSearchStatusWeightVersion() {
        return statusWeightIndexDao.countBuildSearchStatusWeightVersion();
    }

    @Override
    public Integer countBuildSearchStatusWeightIndexPressure() {
        return statusWeightIndexDao.countBuildSearchStatusWeightIndexPressure();
    }

    /**
     * <p>
     * Title: countBuildSearchStatusWeight
     * </p>
     * <p>
     * Description:监控ec_core_build_search_status_weight表数据
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.search.service.BuildSearchService#countBuildSearchStatusWeight()
     */
    @Override
    public Integer countBuildSearchStatusWeight() {
        logger.error("countBuildSearchStatusWeight  begin........");
        return this.statusWeightIndexDao.countBuildSearchStatusWeight();
    }

    /**
     * <p>
     * Title: countBuildSearchForadd
     * </p>
     * <p>
     * Description:监控ec_core_build_search_foradd表
     * </p>
     * 
     * @return  Integer 记录数
     * @see net.ytoec.kernel.search.service.BuildSearchService#countBuildSearchForadd()
     */
    @Override
    public Integer countBuildSearchForadd() {
        return dao.countBuildSearchForadd();
    }

    /**
     * <p>
     * Title: countBuildSearchForupdate
     * </p>
     * <p>
     * Description: 监控ec_core_build_search_forupdate表
     * </p>
     * 
     * @return  Integer 记录数
     * @see net.ytoec.kernel.search.service.BuildSearchService#countBuildSearchForupdate()
     */
    @Override
    public Integer countBuildSearchForupdate() {
        return dao.countBuildSearchForupdate();
    }

    /**
     * <p>
     * Title: countBuildSearchForupdateTwo
     * </p>
     * <p>
     * Description:监控ec_core_build_search_forupdate2
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.search.service.BuildSearchService#countBuildSearchForupdateTwo()
     */
    @Override
    public Integer countBuildSearchForupdateTwo() {
        return dao.countBuildSearchForupdateTwo();
    }

    /**
     * <p>
     * Title: countBuildSearchForupdateThree
     * </p>
     * <p>
     * Description:监控ec_core_build_search_forupdate3表
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.search.service.BuildSearchService#countBuildSearchForupdateThree()
     */
    @Override
    public Integer countBuildSearchForupdateThree() {
        return dao.countBuildSearchForupdateThree();
    }

    /**
     * <p>
     * Title: countBuildSearchForupdateFour
     * </p>
     * <p>
     * Description: 监控ec_core_build_search_forupdate4表
     * </p>
     * 
     * @return Integer 记录数
     * @see net.ytoec.kernel.search.service.BuildSearchService#countBuildSearchForupdateFour()
     */
    @Override
    public Integer countBuildSearchForupdateFour() {
        return dao.countBuildSearchForupdateFour();
    }

}
