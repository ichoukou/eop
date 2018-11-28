package net.ytoec.kernel.dao;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.search.dto.MailObjectDTO;

import org.springframework.dao.DataAccessException;

/**
 * 订单相关接口
 * 
 * @author huaiwen
 * 
 * @param <T>
 */
public interface OrderDaoIndexPressure<T> {
	
    /**
     * 增量build数据到solr
     */
    public List<MailObjectDTO> bulidPartEccoreStatusData(Integer limit, String currentDate, String endDate);

}
