package net.ytoec.kernel.service.impl;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.TaobaoTaskDao;
import net.ytoec.kernel.dataobject.TaobaoTask;
import net.ytoec.kernel.service.TaobaoTaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 淘宝任务管理service层
 * 
 * @author
 * @param
 */
@Service
@Transactional
@SuppressWarnings("all")
public class TaobaoTaskServiceImpl implements TaobaoTaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaobaoTaskServiceImpl.class);

    @Inject
    private TaobaoTaskDao taobaoTaskDao;

    @Override
    public TaobaoTask getTaobaoTaskByTaskId(Integer taskId) {
        TaobaoTask taobaoTask = null;
        try {
            taobaoTask = (TaobaoTask) taobaoTaskDao.getTaobaoTaskByTaskId(taskId);
        } catch (Exception e) {
            return new TaobaoTask();
        }
        return taobaoTask;
    }

    @Override
    public void addTaobaoTask(TaobaoTask taobaoTask) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delTaobaoTaskById(Integer id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTaobaoTask(TaobaoTask taobaoTask) {
    	taobaoTaskDao.updateTaobaoTask(taobaoTask);
    }

    @Override
    public List<TaobaoTask> getTaobaoTaskByUserId(String flag, Integer userId) {
        List<TaobaoTask> taobaoTasks = taobaoTaskDao.getTaobaoTaskByUserId(flag,userId);
        return taobaoTasks;
    }

	@Override
	public void updateTaskAndDeleteFile(Integer taskId, File taskFiles) {
		TaobaoTask taobaoTaskTemp = null;
		long start2 = System.currentTimeMillis();
		taobaoTaskTemp = (TaobaoTask) taobaoTaskDao.getTaobaoTaskByTaskId(taskId);
		taobaoTaskTemp.setFlag("2");
		taobaoTaskDao.updateTaobaoTask(taobaoTaskTemp);
		logger.info("==>taskId: " + taskId + " 解析完成，状态已经更新为2, 耗时：" + (System.currentTimeMillis() - start2));

		long start3 = System.currentTimeMillis();
		if (taskFiles.exists()) {
			if (taskFiles.isFile()) {
				taskFiles.delete();
			} else if (taskFiles.isDirectory()) {
				File files[] = taskFiles.listFiles();
				for (int j = 0; j < files.length; j++) {
					files[j].delete();
				}
			} else {
				taskFiles.delete();
			}
		}
		logger.info("已经成功删除 " + taskId + " 对应的unzip包, 耗时："+(System.currentTimeMillis() - start3));
		
	}

}
