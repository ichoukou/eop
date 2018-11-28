package net.ytoec.kernel.mapper;

import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.SearchReportBean;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.mapper.base.BaseSqlMapper;
import net.ytoec.kernel.search.dto.MailObjectDTO;

import org.springframework.dao.DataAccessException;

/**
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
@SuppressWarnings("all")
public interface DbExeMapper extends BaseSqlMapper {

	public int getOrderTempAllCount();
	
	public List<Order> getOrderTempAll(Map map);
	
	public List<Product> getProductTempByOrderId(Integer orderId);
	
	public List<TraderInfo> getTraderInfoTempByOrderId(Integer orderId);
	
	public void addOrderFromTemp(Order order);
	
	public void addProductFromTemp(Product product);
	
	public void addTraderInfoFromTemp(TraderInfo traderInfo);

}
