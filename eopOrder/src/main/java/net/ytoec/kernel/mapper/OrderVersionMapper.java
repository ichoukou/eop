package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;
import net.ytoec.kernel.search.dto.MailObjectDTO;

/**
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
public interface OrderVersionMapper<T extends Order> extends BaseSqlMapper<T> {

    /**
     * 增量build数据到solr,
     */
    public List<MailObjectDTO> bulidPartEccoreStatusData(Map map);
}
