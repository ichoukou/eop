package net.ytoec.kernel.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.OrderDao;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dto.OrderWeightUpdateDTO;
import net.ytoec.kernel.search.dto.MailObjectDTO;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * 测试
 * 
 * @author ChenRen
 * @date 2011-7-20
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class OrderDaoImplTest extends AbstractJUnit38SpringContextTests {

    /*
     * 首先报错：OrderDao找不到bean... 注入失败. 因为实现类中没有注解：@Component
     */

    /**
     * 为了让测试不在DB中保留测试数据，且测试的几个方法都是操作的一条数据； 这里设置一个全局Id保存当前表中最大的Id值(Id自增，一般情况下执行add方法后，再次得到的tempId为当前新增的数据)
     */
    private static int      tempId = -1;

    @Inject
    private OrderDao<Order> dao;

    @Test
    public void testGetOrderBytxId() {
        // List list=dao.getAllOrder();
        // dao.getOrderById(139);
        System.out.println(1111);
        try {
            System.out.println(222);
            // Order o=dao.getOrderByLogisticId("LP1110777098225709779");
            // System.out.println(o);
            // dao.updateOrderWeightByLogisticId("LP2005082300225709000", Float.valueOf("500"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Test
    // public void testAdd() {
    // Order order = new Order();
    // order.setVersion("V 0.1.0");
    // order.setLogisticProviderId("lp002");
    // order.setTxLogisticId("lp002号");
    // order.setTradeNo("20110720100");
    // order.setCustomerId("c2");
    // order.setMailNo("0571115987");
    // order.setType("I");
    // order.setFlag("Y");
    // order.setInsuranceValue(2.23f);
    // order.setPackageOrNot("Y");
    // order.setSpecial("dao test...");
    // order.setRemark("dao test. add.");
    // order.setClientId("ip:4.110");
    // order.setStatus("1");
    //
    // dao.addOrder(order );
    // }
    //
    // @Test
    // public void testGetOrderById() {
    // List<Order> list = dao.getAllOrder();
    // for (Order order : list) {
    //
    // // 取当前表中最大的Id值
    // int id = order.getId();
    // if(tempId < id) {
    // tempId = id;
    // continue;
    // }
    // } // for
    //
    // System.out.println(dao.getOrderById(tempId));
    // }
    //
     @Test
     public void testEdit() {
     Order order = dao.getOrderById(tempId);
     System.out.println(order);
    
     if(order == null) return;
    
     order.setVersion("V 0.1.1");
     order.setRemark("dao_test. EDIT.");
     dao.updateOrder(order);
     System.out.println(dao.getOrderById(tempId));
     }
    
     @Test
     public void testRemove() {
     Order order = dao.getOrderById(tempId);
     if(order == null) return;
    
     dao.delOrder(order);
     System.out.println(dao.getOrderById(tempId));
     }

    /**
     * 根据物流号和ClientId更新订单的业务的交易号
     */
    @SuppressWarnings("all")
    @Test
    public void testUpdateOrderTradeNoByLogisticIdAndClientId() {
        /** 取最后一条记录的物流号和 */
        String tempLogisticId = "";
        String tempClientId = "";
        String tempTradeNo = "2011082500000023";

        List<Order> list = dao.getAllOrder();
        for (Order order : list) {
            // 取Id值最大的那个条数据信息
            int id = order.getId();
            if (tempId < id) {
                tempLogisticId = order.getTxLogisticId();
                tempClientId = order.getClientId();
                continue;
            }
        } // for

        Map map = new Hashtable();
        map.put("logisticId", tempLogisticId);
        map.put("clientId", tempClientId);
        map.put("tradeNo", tempTradeNo);
        map.put("status", "GOT");

        System.out.println("更新前订单信息：" + dao.getOrderByLogisticId(tempLogisticId));
        dao.updateOrderMailNoByLogisticIdAndClientId(map);
        System.out.println("更新后订单信息：" + dao.getOrderByLogisticId(tempLogisticId));
    }

    @Test
    public void testBulidEccoreData() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 0);
        map.put("limit", 10);
        List<MailObjectDTO> mailObjectDTOs = dao.bulidEccoreData(map);
        assertNotNull(mailObjectDTOs);
        assertEquals(mailObjectDTOs.size(), 10);
    }
    
    @Test
    public void testBatchUpdateWeight() throws SQLException {
    	List<OrderWeightUpdateDTO> orderWeightUpdateDTOs=new ArrayList<OrderWeightUpdateDTO>();
    	OrderWeightUpdateDTO orderWeightUpdateDTO1=new OrderWeightUpdateDTO(106341910,"2.0",null, "");
    	OrderWeightUpdateDTO orderWeightUpdateDTO2=new OrderWeightUpdateDTO(106341911,"3.0",null, "");
    	OrderWeightUpdateDTO orderWeightUpdateDTO3=new OrderWeightUpdateDTO(106341912,"4.0",null, "");
    	orderWeightUpdateDTOs.add(orderWeightUpdateDTO1);
    	orderWeightUpdateDTOs.add(orderWeightUpdateDTO2);
    	orderWeightUpdateDTOs.add(orderWeightUpdateDTO3);
    	
      dao.batchUpdateWeight(orderWeightUpdateDTOs);
    }
}
