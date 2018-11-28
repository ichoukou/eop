package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.QuestionaireRemark;

public interface QuestionaireRemarkDAO<T extends QuestionaireRemark> {

    /**
     * 根据问题件ids ,是否已读 查询 备注信息
     * 
     * @param map
     * @return
     */
    public List<T> getListByQuestionaireIdsAndReadStatus(Map<String, Object> map);

    /**
     * @param entry
     * @return
     */
    public boolean add(T entry);
    
    public boolean edit(T entity);
    
    /**
     * 根据问题件id查找问题件的备注信息
     * @param entity
     * @return
     */
    public List<T> findByQuestionId(T entity);
}
