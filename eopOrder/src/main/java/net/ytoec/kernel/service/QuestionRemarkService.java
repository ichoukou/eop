/**
 * 
 */
package net.ytoec.kernel.service;

import java.util.List;

/**
 * 问题件备注service
 * @author wangyong
 * @date 2012-1-30
 */
public interface QuestionRemarkService<T> {
	
	/**
	 * 添加备注
	 * @param entity
	 * @return
	 */
	public boolean addQuestionRemark(T entity);

	/**
     * 根据问题件id查找问题件的备注信息
     * @param entity
     * @return
     */
    public List<T> findByQuestionId(Integer questionaireId);
    
    /**
     * 添加备注，并将问题件状态改为已处理
     * @param entity
     * @param sendFlag 为1表示修改，为0表示添加
     * @return
     */
    public boolean sendRemark(T entity, int sendFlag);
}
