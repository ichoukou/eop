package net.ytoec.kernel.service;

/**
 * 
 * @author mgl
 * 
 * @param <T>
 */
public interface FailedTaskService<T> {

	/**
	 * 新增一条任务
	 * 
	 * @param task
	 * @return 
	 */
	boolean addFailedTask(T task);
}
