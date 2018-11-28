/**
 * 
 */
package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dao.RegionDao;
import net.ytoec.kernel.dao.TraderInfoDao;
import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.service.TraderInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangyong
 * @date 2011-11-16
 */
@Service
@Transactional
@SuppressWarnings("unchecked")
public class TraderInfoServiceImpl<T extends TraderInfo> implements TraderInfoService<T> {

    @Inject
    private TraderInfoDao<T> TraderInfoDao;
    private static Logger logger = LoggerFactory.getLogger(TraderInfoServiceImpl.class);
    @Inject
    private RegionDao<Region> regionDao ;

    /*
     * (non-Javadoc)
     * @see net.ytoec.kernel.service.TraderInfoService#getTraderInfoListByOrder()
     */

    public boolean delTraderInfoByOrderId(int orderId) {
        return TraderInfoDao.delTraderInfoByOrderId(orderId);
    }

    @Override
    public List<T> traderInfoTimerList(Integer startIndex, Integer pageNum) {
        Map map = new HashMap();
        if (startIndex != null && pageNum != null) {
            map.put("startIndex", startIndex);
            map.put("pageNum", pageNum);
            return TraderInfoDao.traderInfoTimerList(map);
        } else return null;
    }

    @Override
    public int countTraderInfoTimerList() {
        return TraderInfoDao.countTraderInfoTimerList();
    }

    @Override
    public T getTradeBuId(int id) {
        return TraderInfoDao.getTraderInfoById(id);
    }

    @Override
    public boolean editTraderInfo(T entity) {
        return TraderInfoDao.editTraderInfo(entity);
    }

    @Override
    public List<T> getTraderInfoByOrderId(int orderId) {
        return TraderInfoDao.getTraderInfoByOrderId(orderId);
    }
    
    @Override
    public List<T> getTraderInfo4Question(int orderId,String tradeType) {
        return TraderInfoDao.getTraderInfo4Question(orderId,tradeType);
    }

    @Override
    public boolean addTraderInfo(T entity) {
        boolean flag = false;
        try {
            flag = this.TraderInfoDao.addTraderInfo(entity);
        } catch (DataAccessException dae) {
            flag = false;
        }
        return flag;
    }

    @Override
    public List<TraderInfo> getTraderInfo(String partition, Integer limit) {
        return TraderInfoDao.getTraderInfo(partition, limit);
    }

    @Override
    public List<TraderInfo> getTraderInfoById(String partition, Integer traderId, Integer limit) {
        return TraderInfoDao.getTraderInfoById(partition, traderId, limit);
    }

    @Override
    public void updateTraderInfo(List<TraderInfo> traderInfos) {
        if (traderInfos == null || traderInfos.isEmpty()) {
            logger.error("没有要更新的信息");
            return;
        }

        Map<String, Integer> proviceMap = new HashMap<String, Integer>();
        List<TraderInfo> strList = new ArrayList<TraderInfo>();
        TraderInfo traderInfo = new TraderInfo();
        for (int i = 0; i < traderInfos.size(); i++) {
            traderInfo = traderInfos.get(i);
            proviceMap.put(traderInfo.getProv(), traderInfo.getNumProv());

            strList.add(traderInfo);
        }


        List<Region> regList = new ArrayList<Region>();
        
        //查询数据库
        //regList = regionDao.getRegion(strList);
        
        
        for (int i = 0; i < strList.size(); i++) {
            Region region = new Region();
             region.setId(Resource.getCodeByName(strList.get(i).getProv()));
             region.setRegionName(strList.get(i).getProv());
             regList.add(region);
        }

        for (int i = 0; i < regList.size(); i++) {
            proviceMap.put(regList.get(i).getRegionName(), regList.get(i).getId());
        }
        
        for (int i = 0; i < traderInfos.size(); i++) {
            TraderInfo newTraderInfo = new TraderInfo();
            newTraderInfo = traderInfos.get(i);
            if (proviceMap.containsKey(newTraderInfo.getProv())) {
                newTraderInfo.setNumProv(proviceMap.get(newTraderInfo.getProv()));
                newTraderInfo.setPartitionDate((Date)Resource.proviceParam.get("updateProvice"));
            }
            
        }

        TraderInfoDao.batchUpdateProvince(traderInfos);
        logger.error("更新完成！");
    }

	@Override
	public TraderInfo getTraderInfoByIdAndByTraderType(int orderId,int traderType) {
		// TODO Auto-generated method stub
		
		return TraderInfoDao.getTraderInfoByIdAndByTraderType(orderId , traderType);
	}
}
