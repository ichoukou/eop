/**
 * AttentionMailAction.java
 * 2011 2011-12-15 下午04:50:24
 * wangyong
 */
package net.ytoec.kernel.action.questionnaire;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.AttentionMailService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


/**
 * 关注运单Action.处理加入关注等业务
 * @author wangyong
 * TODO
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class AttentionMailAction extends AbstractActionSupport {

    @Inject
    private OrderService<Order> orderService;
    @Inject
    private AttentionMailService<AttentionMail> attentionMailService;
	@Inject
	private UserService<User> userService;
    
    //加入关注参数：mailNOs是运单号的字符串形式，以","分隔
    private String mailNO;
    private Integer id;
    private String ids;
    
    private String responseString;
    private int countAttention;
    
    /**
     * 加入关注
     * @return
     */
	public String addInAttention(){
        User currentUser = super.readCookieUser();
        List bindedId = new ArrayList();
        if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("2") || currentUser.getUserType().equals("21") || currentUser.getUserType().equals("23"))){
			List<User> userList = userService.searchUsersBySiteAndUserType(currentUser.getSite(), "1");
			if(userList!=null) {
				for(User u : userList) {
					if(StringUtils.isNotEmpty(u.getTaobaoEncodeKey())) 
						bindedId.add(u.getTaobaoEncodeKey());
				}
			}
        }else if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("13"))){
        	bindedId = Resource.getBindedCustomerIdList(currentUser);
        }
        AttentionMail am = new AttentionMail();
        Order order = orderService.getOrderbyMailNoAndCustomerId(mailNO, bindedId);
        if(order==null){
        	responseString = "数据库中不存在此订单";
        }else{
        	am.setMailNo(order.getMailNo());
            am.setDestination(order.getToAddr());
            am.setAcceptTime(order.getAcceptTime());
            am.setBuyerName(order.getBuyName());
            if(order.getBuyMobile()!=null&&order.getBuyMobile()!="")
            	am.setBuyerPhone(order.getBuyMobile());
            else
            	am.setBuyerPhone(order.getBuyPhone());
            am.setStatus(order.getStatus());
            am.setLineType(order.getLineType());
            am.setSendTime(order.getCreateTime());
            if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("13"))){
            	am.setCustomerId(currentUser.getTaobaoEncodeKey());
            }
            if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("2") || currentUser.getUserType().equals("21") || currentUser.getUserType().equals("23"))){
            	if(currentUser.getUserType().equals("2"))
            		am.setCustomerId(currentUser.getId().toString());
            	else
            		am.setCustomerId(currentUser.getParentId().toString());
            }
            if(attentionMailService.add(am)) {
            	responseString = "关注成功";
            }
            else {
                responseString = "关注失败";
            }
        }
        return "addInAttention";
    }
    
    public String delAttention(){
        if(attentionMailService.remove(id)){
            responseString = "删除成功";
        }else
            responseString = "删除失败";
        return "delAttention";
    }
    
    public String delAll(){
        String[] idArr = {};
        if(StringUtils.isNotBlank(ids)){
             idArr = ids.split(",");
        }
        for(int i=0; i<idArr.length; i++){
            attentionMailService.remove(Integer.valueOf(idArr[i]));
        }
        responseString = "删除成功";
        return "delAll";
    }
    
    public String cancleAttention(){
    	User currentUser = super.readCookieUser();
    	AttentionMail am = new AttentionMail();
    	String customerId = "";
    	if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("1") || currentUser.getUserType().equals("11") || currentUser.getUserType().equals("13"))){
    		customerId = currentUser.getTaobaoEncodeKey();
        }
        if(currentUser.getUserType()!=null && (currentUser.getUserType().equals("2") || currentUser.getUserType().equals("21") || currentUser.getUserType().equals("23"))){
        	if(currentUser.getUserType().equals("2"))
        		customerId = currentUser.getId().toString();
        	else
        		customerId = currentUser.getParentId().toString();
        }
		if(attentionMailService.searchByMailNoAndCustomerId(mailNO ,customerId)!=null){
			am = attentionMailService.searchByMailNoAndCustomerId(mailNO ,customerId).get(0);
			if(attentionMailService.remove(am.getId())){
				responseString = "取消成功";
			}else
				responseString = "取消失败";
		}else
			responseString = "取消失败";
    	return "cancleAttention";
    }

    
    public String getMailNO() {
	    return mailNO;
    }

    
    public void setMailNO(String mailNO) {
        this.mailNO = mailNO;
    }


    
    public String getResponseString() {
        return responseString;
    }


    
    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public int getCountAttention() {
        return countAttention;
    }

    
    public void setCountAttention(int countAttention) {
        this.countAttention = countAttention;
    }

    
    public String getIds() {
        return ids;
    }

    
    public void setIds(String ids) {
        this.ids = ids;
    }

}
