package net.ytoec.kernel.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.RegionDao;
import net.ytoec.kernel.dao.impl.ConfigCodeDaoImpl;
import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.service.RegionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 地市信息数据访问实现
 * @author Wangyong
 *
 * @param <T>
 */
@Service
@Transactional
public class RegionServiceImpl<T extends Region> implements RegionService<T> {

	private static Logger logger = LoggerFactory.getLogger(ConfigCodeDaoImpl.class);
	@Inject
	private RegionDao<T> dao;
	
	@Override
	public boolean addRegion(T region) {
		if(region == null){			
			logger.error("region is empity");
			return false;
		}
		return dao.addRegion(region);
	}

	@Override
	public boolean removeRegion(T region) {
		if(region == null){			
			logger.error("region is empity");
			return false;
		}
		return dao.removeRegion(region);
	}

	@Override
	public boolean editRegion(T region) {
		if(region == null){			
			logger.error("region is empity");
			return false;
		}
		return dao.editRegion(region);
	}

	@Override
	public T getRegionById(Integer id) {
		return dao.getRegionById(id);
	}

	@Override
	public List<T> getRegionByParentId(Integer parentId)
 {
		return dao.getRegionByParentId(parentId);
	}

	@Override
	public List<T> getProvinceRegion() {
		return dao.getRegionByParentId(0);
	}

    @Override
    public List<T> getAllRegion() {

        return dao.getAllRegion();
    }

	@Override
	public List<T> getAllProvince(List<Integer> list) {
		
		return (List<T>) dao.getAllProvince(list);
	}

}
