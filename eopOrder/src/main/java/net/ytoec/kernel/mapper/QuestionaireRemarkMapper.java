package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.QuestionaireRemark;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface QuestionaireRemarkMapper<T extends QuestionaireRemark> extends BaseSqlMapper<T> {

    /**
     * 根据问题件ids，是否已读 查询备注信息
     * 
     * @param map
     * @return
     */
    public List<T> getListByQuestionaireIdsAndReadStatus(Map map);
    
    /**
     * 根据问题件id查找问题件的备注信息
     * @param entity
     * @return
     */
    public List<T> findByQuestionId(T entity);
}
