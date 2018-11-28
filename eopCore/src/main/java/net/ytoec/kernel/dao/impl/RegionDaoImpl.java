/**
 * 
 */
package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.RegionDao;
import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.mapper.RegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 区域信息数据接口实现
 * @author Wangyong
 */
@Repository
public class RegionDaoImpl<T extends Region> implements RegionDao<T> {

	private static Logger logger=LoggerFactory.getLogger(RegionDaoImpl.class);
	@Inject
	private RegionMapper<Region> mapper;
	
	@Override
	public boolean addRegion(T region) {
		boolean flag = false;
		try{
			mapper.add(region);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean removeRegion(T region) {
		boolean flag = false;
		try{
			mapper.remove(region);
			flag = true;
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean editRegion(T region) {
		boolean flag = false;
		try{
			mapper.edit(region);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			flag = false;
		}
		return flag;
	}

	@Override
	public T getRegionById(Integer id) {
		Region reg = new Region();
		T entity = null;
		try{
			reg.setId(id);
			entity = (T)mapper.get(reg);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getRegionByParentId(Integer parentId)
 {
		List<T> list = null;
		try{
			list =(List<T>) mapper.getRegionListByParentId(parentId);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllRegion() {
		List<T> list = null;
		try{
			list = (List<T>) mapper.getAllRegion();
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return list;
	}

    @Override
    public List<Region> getRegion(List<TraderInfo> traList) {
        // TODO Auto-generated method stub
        logger.error("开始在region表中查数据");
        List<Region> list = new ArrayList<Region>();
        TraderInfo traderInfo = new TraderInfo();
        Region region = new Region();
        try {
            for (int i = 0; i < traList.size(); i++) {
                traderInfo = traList.get(i);
                logger.error(traList.size()+"    ");
                region = mapper.getRegion(traderInfo.getProv());
                if (region == null ) {
                    logger.error("ec_core_region表中不存在region_name为traderinfo表中为Prov字段的记录");
                    continue;
                }
                
                list.add(region);
                logger.error(traderInfo.getProv());
                logger.error("---------------"+region.getId()  +"  "+region.getRegionName());
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("根据region_name查询失败！");
        }
        return list;
    }

    @Override
	public List<T> getAllProvince(List<Integer> list) {
		List<T> listAllProvince = null;
		try{
			listAllProvince =  (List<T>) mapper.getAllProvince(list);
		} catch(Exception e){
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}
		return listAllProvince;
	}

}
