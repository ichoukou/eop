package net.ytoec.kernel.service;

import java.io.File;
import java.util.List;

import net.ytoec.kernel.dataobject.TaobaoTask;

/**
 * 淘宝任务管理service层
 * @author 
 *
 * @param 
 */
public interface TaobaoTaskService {

	/**
	 * 根据任务ID获取对象
	 * @param taskId
	 * @return
	 */
	public TaobaoTask getTaobaoTaskByTaskId(Integer taskId);
	
	/**
	 * 添加淘宝获取订单任务
	 * @param taobaoTask
	 */
	public void addTaobaoTask(TaobaoTask taobaoTask);

	/**
	 * 根据ID删除对象
	 * @param id
	 */
	public void delTaobaoTaskById(Integer id);

	/**
	 * 更新淘宝任务
	 * @param grade
	 */
	public void updateTaobaoTask(TaobaoTask taobaoTask);

	/**
	 * 根据userID获取淘宝任务list
	 * @param userId
	 * @param flag 任务处理的状态 0：等待淘宝做数据 1：数据已下载 2：数据已解析且删除
	 * @return
	 */
	public List<TaobaoTask> getTaobaoTaskByUserId(String flag, Integer userId);
	
	public void updateTaskAndDeleteFile(Integer taskId,File taskFiles);
}
