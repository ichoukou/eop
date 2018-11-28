package net.ytoec.kernel.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.constains.LogInfoEnum;
import net.ytoec.kernel.dao.SendTaskToTBDao;
import net.ytoec.kernel.dataobject.SendTaskToTB;
import net.ytoec.kernel.mapper.SendTaskToTBMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 
 * 发送任务数据访问实现类.
 * 
 * @param <T>
 *            SendTaskToTB
 */

@Repository
public class SendTaskToTBDaoImpl<T extends SendTaskToTB> implements SendTaskToTBDao<T> {

    private static Logger logger = LoggerFactory.getLogger(SendTaskToTBDaoImpl.class);
    @Autowired
    private SendTaskToTBMapper<T> mapper;

    @Override
    public void addSendTaskToTB(T sendTaskToTB) {
        try {
            this.mapper.add(sendTaskToTB);
        } catch (DataAccessException e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
            throw e;
        }
    }

    @Override
    public void editSendTaskToTB(T sendTaskToTB) {
        try {
            this.mapper.edit(sendTaskToTB);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
    }

    @Override
    public List<T> getAll() {

        List<T> list = null;
        try {
            list = (List<T>) mapper.getAllSendTaskToTB();
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return list;
    }

    @Override
    public T getSendTaskToTBById(Integer sendTaskToTBId) {
        try {
            SendTaskToTB st = new SendTaskToTB();
            st.setId(sendTaskToTBId);
            return this.mapper.get((T) st);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return null;
    }

    @Override
    public void removeSendTaskToTB(T sendTaskToTB) {
        try {
            this.mapper.remove(sendTaskToTB);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
    }

    @Override
    public List<T> getSendTaskToTBListByLimit(int limit) {
        List<T> result = null;
        try {
            result = (List<T>) this.mapper.getSendTaskToTBListByLimit(limit);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }

    public List<T> getPaginationSendTaskToTBList(Map map) {
        List<T> result = null;
        try {
            result = (List<T>) this.mapper.getPaginationSendTaskToTBList(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }

    public List<T> getSendTaskToTBListByflag(Map map) {
        List<T> result = null;
        try {
            result = (List<T>) this.mapper.getSendTaskToTBListByflag(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }

    @Override
    public List<T> getSendTaskListByFlagsAndLimit(Map map) {
        // TODO Auto-generated method stub
        List<T> result = new ArrayList();
        try {
            result = this.mapper.getSendTaskListByFlagsAndLimit(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }

    @Override
    public Integer batchDelTask(Map map) {

        return this.mapper.batchDelTask(map);
    }

    @Override
    public Integer countTask() {
        return this.mapper.countTask();
    }

    /**
     * 批量添加发送任务
     * 
     * @author wmd
     * @return
     */
    @Override
    public boolean batchAddSendTask2TB(List<T> sendTaskToTBList) {
        boolean b = false;
        try {
            this.mapper.batchAdd(sendTaskToTBList);
            b = true;
        } catch (Exception e) {
            logger.error("批量添加SendTask2TB失败" + e);
        }
        return b;
    }

    /**
     * <p>
     * Title: getSendTaskByClientId
     * </p>
     * <p>
     * Description: 获取指定客户ID任务
     * </p>
     * 
     * @param map
     *            参数MAP
     * @return 任务列表
     * @throws DataAccessException
     * @see net.ytoec.kernel.dao.SendTaskToTBDao#getSendTaskByClientId(java.util.Map)
     */
    @Override
    public List<T> getSendTaskByClientId(Map map) throws DataAccessException {
        List<T> result = null;
        try {
            result = (List<T>) this.mapper.getSendTaskToTBListByflag(map);
        } catch (Exception e) {
            logger.error(LogInfoEnum.PERSISTENCE_FAILED.getValue(), e);
        }
        return result;
    }
}
