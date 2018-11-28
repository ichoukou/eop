package net.ytoec.kernel.dao;

import java.util.List;

import net.ytoec.kernel.dataobject.QuestionaireExchange;

public interface QuestionaireExchangeDAO<T extends QuestionaireExchange> {

    /**
     * 根据问题件ids查询交互记录
     * 
     * @param map
     * @return
     */
    public List<T> getListByQuestionaireIds(List<Integer> questionnaireIds);

    /**
     * @param entry
     * @return
     */
    public boolean add(T entry);

}
