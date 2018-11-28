package net.ytoec.kernel.action.questionnaire;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.StepInfo;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.OrderProductBean;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.ProductService;
import net.ytoec.kernel.service.TraderInfoService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 根据运单号查询物流跟踪信息和订单信息<br>
 * 在实际情况下，一个运单号对应多个订单的情况是存在的，在弹出页面上以分页的形式展示。<br>
 * 客户端传递的参数是mailNo(运单号)。
 * @author wangyong
 * @2011-12-05
 * net.ytoec.kernel.action.questionnaire
 */
@Controller
@Scope("prototype")
public class LogisticOrderInfoAction extends AbstractActionSupport {
	
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private ProductService<Product> productService;
	@Inject
	private TraderInfoService<TraderInfo> traderInfoService;
	
	//客户端传参
	private String mailNo;
	
	private Integer currentPage = 1;
	private static Integer PAGE_NUM = 1; //每页显示一条订单信息
	private Pagination<Questionnaire> pagination;
	private List<OrderProductBean> orderProductList;
	private List<QueryOrder> logisticList;//物流信息
	private List<StepInfo> stepsList;//每个物流中Step信息
	private static List<OrderProductBean> tempOrderProductList = new ArrayList<OrderProductBean>();
	private List<TraderInfo> orderTraderInfoList;
	private static List<TraderInfo>  tempOrdertraderInfoList = new ArrayList<TraderInfo>();
	private static List<QueryOrder> tempLogisticList = new ArrayList<QueryOrder>();
	private static List<StepInfo> tempStepsList = new ArrayList<StepInfo>();
	private Integer isDisplay = 0;//网点用户就看不到卖家的订单详情,默认0可见；1不可见
	
	public String queryInfo() throws Exception{
		/**
		 * 物流信息
		 */
		TaoBaoOrderAction op = new TaoBaoOrderAction();
		String[] logisticsId = {mailNo};
		logisticList = op.ordersSearch(logisticsId);
		/**
		 * 物流steps顺序
		 */
		if(logisticList!=null && logisticList.size()>0){
		    QueryOrder qo = logisticList.get(0);
		    stepsList = new ArrayList<StepInfo>();
		    for(int i=0; i<qo.getSteps().size(); i++){
		        stepsList.add(qo.getSteps().get(i));
		    }
		}
		tempLogisticList = logisticList;
		tempStepsList = stepsList;
		pagination = new Pagination(currentPage, PAGE_NUM);
		List<OrderProductBean> totalOrderProductList = new ArrayList<OrderProductBean>();
		List<TraderInfo> totalOrdertraderInfoList = new ArrayList<TraderInfo>();
		//订单列表：在获取订单信息时首先要判断该运单查询出来的订单是否属于当前用户
		User currentUser = super.readCookieUser();
		if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("2") || currentUser.getUserType().equals("21") || currentUser.getUserType().equals("22") || currentUser.getUserType().equals("23"))){
			/**
			 * 是网点用户就看不到卖家的订单详情
			 */
			isDisplay = 1;
		}
		List<Order> orderList = orderService.queryOrderListByMailNoAndCustomerid(mailNo, currentUser.getId());
		if(orderList!=null && orderList.size()>0){
			pagination.setTotalRecords(orderList.size());
			for(Order order : orderList){
				OrderProductBean bean = new OrderProductBean();
				if(order.getWeight()==null){
					order.setWeight(0f);
				}
				bean.setOrder(order);
				//产品信息
				List<Product> productList = productService.getProductListByOrder(order.getId());
				bean.setProduct(productList);
				totalOrderProductList.add(bean);
				List<TraderInfo> traderInfoList = traderInfoService.getTraderInfoByOrderId(order.getId());
				totalOrdertraderInfoList=traderInfoList;
			}

			tempOrderProductList = totalOrderProductList;
			orderTraderInfoList= totalOrdertraderInfoList;
			tempOrdertraderInfoList = orderTraderInfoList;
			orderProductList = getPaginationList(totalOrderProductList, pagination);
		}else{
			pagination.setTotalRecords(0);
		}
		return "success";
	}
	
	public String paginationQuery(){
		pagination = new Pagination(currentPage, PAGE_NUM);
		if(tempOrderProductList!=null){
			pagination.setTotalRecords(tempOrderProductList.size());
			orderProductList = getPaginationList(tempOrderProductList, pagination);
		}else{
			pagination.setTotalRecords(0);
		}
		logisticList = tempLogisticList;
		stepsList = tempStepsList;
		orderTraderInfoList = tempOrdertraderInfoList;
		return "paginationQuery";
	}
	
	/**
	 * 分页显示，每次显示一条记录
	 * @param sourceList
	 * @param curPage
	 * @return
	 */
	private List<OrderProductBean> getPaginationList(List<OrderProductBean> sourceList, Pagination pagination){
		List<OrderProductBean> resultList = new ArrayList<OrderProductBean>();
		if(sourceList!=null){
			if(pagination.getCurrentPage()==pagination.getTotalPages()){
				for(int index=pagination.getStartIndex(); index<pagination.getTotalRecords(); index++){
					resultList.add(sourceList.get(index));
				}
			}else{
				for(int index=pagination.getStartIndex(); index<pagination.getStartIndex()+PAGE_NUM; index++){
					resultList.add(sourceList.get(index));
				}
			}
		}
		return resultList;
	}
	
	public String getMailNo() {
		return mailNo;
	}

	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination<Questionnaire> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<Questionnaire> pagination) {
		pagination = pagination;
	}

	public List<OrderProductBean> getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(List<OrderProductBean> orderProductList) {
		this.orderProductList = orderProductList;
	}

	public List<QueryOrder> getLogisticList() {
		return logisticList;
	}

	public void setLogisticList(List<QueryOrder> logisticList) {
		this.logisticList = logisticList;
	}

    
    public List<StepInfo> getStepsList() {
        return stepsList;
    }

    
    public void setStepsList(List<StepInfo> stepsList) {
        this.stepsList = stepsList;
    }

	public List<TraderInfo> getOrderTraderInfoList() {
		return orderTraderInfoList;
	}

	public void setOrderTraderInfoList(List<TraderInfo> orderTraderInfoList) {
		this.orderTraderInfoList = orderTraderInfoList;
	}

	public Integer getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}


	
}
