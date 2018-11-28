package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.QuestionaireExchange;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;

public interface QuestionaireExchangeMapper<T extends QuestionaireExchange> extends BaseSqlMapper<T> {

    /**
     * 根据问题件ids查询交互记录
     * 
     * @param map
     * @return
     */
    public List<T> getListByQuestionaireIds(Map map);
}
