package net.ytoec.kernel.action.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserRelation;
import net.ytoec.kernel.dto.UserRelationDTO;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserRelationService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class AssociationAccountAction extends AbstractActionSupport {

	/**
     * 
     */
	private static final long serialVersionUID = -6792683897357143487L;
	private List<User> associationAccountList;
	private List<UserRelationDTO> accountCustomList;
	@Inject
	private UserService<User> userService;
	@Inject
	private UserCustomService<UserCustom> userCustomService;
	@Inject
	private UserRelationService<UserRelation> userRelationService;
	private User currentUser = new User();
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * 要取消的绑定账号 
	 */
	private String customerID;

	/**
	 * 取消绑定的结果
	 */
	private String cancelResult;

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public String toBindedAssociationAccount() {
		currentUser = super.readCookieUser();
		currentUser = userService.get(currentUser);
		if ("1".equals(currentUser.getUserType())) {
			// 如果当前用户是vip用户，取当前用户的关联账号 
			List<Integer> associationIdList = Resource.getUserRelationUserIdList(currentUser);
			if(associationIdList!=null) {
				associationAccountList = new ArrayList<User>();
				for(Integer id : associationIdList) {
					User u = userService.getUserById(id);
					if(u!=null)
						associationAccountList.add(u);
				}
			}
		}
		return "success";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public String toBindeAccountCustom() {
		currentUser = super.readCookieUser();
		currentUser = userService.get(currentUser);
		accountCustomList=new ArrayList<UserRelationDTO>();
		List<Integer> associationIdList = new ArrayList<Integer>();
		if(currentUser.getUserType().equals("1"))
			associationIdList.add(currentUser.getId());//加入自己的id
		else
			associationIdList.add(currentUser.getParentId());//加入主账号的id
		if(Resource.getUserRelationUserIdList(currentUser)!=null)
			associationIdList.addAll(Resource.getUserRelationUserIdList(currentUser));
		Map cusMap=Resource.getRelationAccountCustoms(currentUser);
		for(Integer id : associationIdList) {
			User u = userService.getUserById(id);
			if(u!=null) {
				UserRelationDTO urDTO = new UserRelationDTO();
				urDTO.setUserName(u.getUserName());
				if(StringUtils.isNotEmpty(u.getShopName()))
					urDTO.setShopName(u.getShopName());
				else
					urDTO.setShopName(u.getUserName());
				if(cusMap!=null) {
					if(cusMap.containsKey(u.getUserName())) {
						urDTO.setIsRelated(1);
					} else {
						urDTO.setIsRelated(0);
					}
				} else {
					urDTO.setIsRelated(0);
				}
				accountCustomList.add(urDTO);
			}
		}
		return "toBindedAccoutCustom";
	}
	
	public String editUserCustom(){
		currentUser = super.readCookieUser();
		currentUser = userService.get(currentUser);
         String customStr = request.getParameter("ids");
         if(!StringUtil.isBlank(customStr)){
        	 String[] customs=customStr.split(",");
        	 for(int i=0;i<customs.length;i++){
        		 if(!StringUtil.isBlank(customs[i])){
	        		 String[] custom=customs[i].split(":");
	        		 //如果custom[1]==1则增加数据到user_custom;否则则删除
	        		 UserCustom uc=Resource.getRelationAccountCustoms(currentUser).get(custom[0]);
	        		 if(custom[1].equals("1")) {
	        			 //如果存在则不操作。否则添加数据
	        			 if(uc==null) {
	        				 UserCustom userCustom = new UserCustom();
			        		 userCustom.setUserName(currentUser.getUserName());
			        		 userCustom.setBindedUserName(custom[0]);
			        		 User bindedUser = userService.getUserByUserName(custom[0]);
			        		 userCustom.setCustomerId(bindedUser.getTaobaoEncodeKey());
			        		 userCustom.setType(UserCustom.RELATIONAL);
			        		 userCustom.setRelationalQuery(UserCustom.RELATIONAL);
			        		 userCustomService.add(userCustom);
	        			 }
	        		 } else {
	        			 //如果不存在则不操作，否则删除
	        			 if(uc!=null) {
	        				 UserCustom userCustom = new UserCustom();
	        				 userCustom.setUserName(currentUser.getUserName());
	        				 userCustom.setBindedUserName(custom[0]);
	        				 userCustom.setType(UserCustom.RELATIONAL);
	        				 userCustom.setRelationalQuery(UserCustom.RELATIONAL);
	        				 userCustomService.individuationDelete(userCustom);
	        			 }
	        		 }
        		 }
        	 }
         }
        Resource.setRelationAccountCustoms(currentUser);	// 更新个性化配置的缓存
		return "success";
	}
	
	public String cancelAssociationAccount() {
		cancelResult = "false";
		currentUser = super.readCookieUser();
		currentUser = userService.get(currentUser);
		/**
		 * 关联关系功能改造：关联关系的体现不再使用User对象中的bindedCustomerId。而是使用UserRelation对象
		 * by wangyong
		 */
		User bindedUser = userService.getUserByCustomerId(customerID);//被取消关联的用户。取消关联是双向。
		boolean result = userRelationService.cancelRelation(currentUser, bindedUser);
		if (result) {
	        //更新缓存currentUser/bindedUser
	        Resource.setRelationAccountCustoms(currentUser);
	        Resource.setRelationAccountCustoms(bindedUser);
	        Resource.setUserRelationUserIdList(currentUser);
	        Resource.setUserRelationUserIdList(bindedUser);
	        cancelResult = "true";
	        putMsg(JsonResponse.INFO_TYPE_SUCCESS, true, "操作已成功！", "");
	    }
	    else{
	        putMsg(JsonResponse.INFO_TYPE_ERROR, false, "操作失败，请检查后重新操作", "");
	    }
		return "jsonRes";
	}

	public List<User> getAssociationAccountList() {
		return associationAccountList;
	}

	public void setAssociationAccountList(List<User> associationAccountList) {
		this.associationAccountList = associationAccountList;
	}

	public List<UserRelationDTO> getAccountCustomList() {
		return accountCustomList;
	}

	public void setAccountCustomList(List<UserRelationDTO> accountCustomList) {
		this.accountCustomList = accountCustomList;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}


	public void setUserCustomService(
			UserCustomService<UserCustom> userCustomService) {
		this.userCustomService = userCustomService;
	}

	public String getCancelResult() {
		return cancelResult;
	}

	public void setCancelResult(String cancelResult) {
		this.cancelResult = cancelResult;
	}


    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
