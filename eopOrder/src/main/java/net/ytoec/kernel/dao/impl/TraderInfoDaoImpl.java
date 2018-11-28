package net.ytoec.kernel.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.TraderInfoDao;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.mapper.TraderInfoMapper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
@Repository
public class TraderInfoDaoImpl<T extends TraderInfo> implements
		TraderInfoDao<T> {

	private static Logger logger = LoggerFactory.getLogger(TraderInfoDaoImpl.class);
	@Inject
	private TraderInfoMapper<TraderInfo> mapper;

	@Override
	public boolean addTraderInfo(T traderInfo) throws DataAccessException {


		boolean flag = false;
		try {
			mapper.add(traderInfo);
			flag = true;
		} catch (DataAccessException e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			throw e;
		}
		return flag;
	}

	@Override
	public boolean batchAddTraderInfo(List<T> traderInfo)
 {

		boolean flag = false;

		try {
			// TODO 批量操作的语句有待优化, 要保证只链接一次DB
			for (T t : traderInfo) {
				mapper.add(t);
			}
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag = false;
		}

		return flag;
	}

	@Override
	public boolean delTraderInfoByOrderId(int orderId) {


		boolean flag = false;

		try {
			// 返回受影响的行数
			int rows =  mapper.delTraderInfoByOrderId(orderId);
			if (rows > 0)
				flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag = false;
		}

		return flag;
	}

	@Override
	public List<T> getTraderInfoByOrderId(int orderId) {
		List<T> list = null;
		try {
			list = (List<T>) this.mapper.getTraderInfoByOrderId(orderId);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}
		return list;
	}
	
	@Override
	public List<T> getTraderInfo4Question(int orderId,String tradeType) {
		List<T> list = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", orderId);
			map.put("tradeType", tradeType);
			list = (List<T>) this.mapper.getTraderInfo4Question(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}
		return list;
	}
	
	@Override
	public List<T> getTraderInfoByPartitiondate(int orderId,String partitiondate) {
		List<T> list = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
	        map.put("partitiondate", partitiondate);
	        map.put("orderId", orderId);
			list = (List<T>) this.mapper.getTraderInfoByPartitiondate(map);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
			return Collections.EMPTY_LIST;
		}
		return list;
	}

	@Override
	public List<T> getAllTraderInfo() {
		List<T> list = null;
		try {
			list = (List<T>) this.mapper.getAllTraderInfo();
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
		}

		return list;
	}

	@Override
	public T getTraderInfoById(int id) {


		TraderInfo traderInfo = new TraderInfo();
		traderInfo.setId(id);
		T entity = null;

		try {
			entity = (T) mapper.get(traderInfo);
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
		}

		return entity;
	}

	@Override
	public boolean editTraderInfo(T entity) {

		boolean flag = false;

		try {
			mapper.edit(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag = false;
		}

		return flag;
	}

	@Override
	public boolean delTraderInfo(T entity) {

		boolean flag = false;

		try {
			mapper.remove(entity);
			flag = true;
		} catch (Exception e) {
			logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(),e);
			flag = false;
		}

		return flag;
	}

	@Override
	public List<T> traderInfoTimerList(Map map){
		List<T> list = null;
		list = (List<T>)mapper.traderInfoTimerList(map);
		return list;
	}
	
	@Override
	public int countTraderInfoTimerList(){
		return mapper.countTraderInfoTimerList();
	}

    @Override
    public List<TraderInfo> getTraderInfo(String partition, Integer limit) {
        // TODO Auto-generated method stub
        
        logger.error("进入Dao");
        if (StringUtils.isEmpty(partition) || limit == null) {
            logger.error("分区参数或者limit为空！");
            return Collections.EMPTY_LIST;
        }
        List<TraderInfo> list = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("partition", partition);
        map.put("limit", limit);
        try {
            list = mapper.getTraderInfo(map);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("查询失败!");
        }
        
        return list;
    }

    @Override
    public List<TraderInfo> getTraderInfoById(String partition, Integer traderId, Integer limit) {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(partition) || limit == null) {
            logger.error("分区参数或者limit为空或者traderId为空！");
            return Collections.EMPTY_LIST;
        }
        List<TraderInfo> list = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("partition", partition);
        map.put("limit", limit);
        map.put("traderId", traderId);
        try {
            list = mapper.getTraderInfo(map);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("查询失败!");
        }
        return list;
    }

    @Override
    public void batchUpdateProvince(List<TraderInfo> traderInfos) {
        // TODO Auto-generated method stub
        try {
            TraderInfo traderInfo = new TraderInfo();
            
            for (int i = 0; i < traderInfos.size(); i++) {
                traderInfo = traderInfos.get(i);
                if (StringUtils.isEmpty(traderInfo.getProv()) || traderInfo.getNumProv() == 0) {
                    continue;
                }

                mapper.updateProviceInfo(traderInfo);
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("更新数据库失败 ");
        }
        
    }

	@Override
	public TraderInfo getTraderInfoByIdAndByTraderType(int orderId,int traderType) {
		// TODO Auto-generated method stub
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("tradeType", traderType);
		map.put("orderId", orderId);
		return mapper.getTraderInfoByIdAndByTraderType(map);
	}
}
