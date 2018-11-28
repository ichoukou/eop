/**
 * net.ytoec.kernel.timer
 * UserRelationImportTimer.java
 * 2012-10-22上午10:46:34
 * @author wangyong
 */
package net.ytoec.kernel.timer;

import java.util.List;
import java.util.TimerTask;

import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserRelation;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserRelationService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wangyong
 * 2012-10-22
 */
public class UserRelationImportTimer extends TimerTask {
	
	private static Logger logger = LoggerFactory.getLogger(UserRelationImportTimer.class);

	private UserCustomService<UserCustom> userCustomService;
	
	private UserRelationService<UserRelation> userRelationService;
	
	private UserService<User> userService;
	
	@Override
	public void run() {
		logger.info("开始执行");
		List<User> userList = userService.getUserListByUserType("1");
		if(!userList.isEmpty()) {
			for(User u : userList) {
				String bindedCustomerId = u.getBindedCustomerId();
				if(StringUtils.isNotEmpty(bindedCustomerId)) {
					String[] customerIds = bindedCustomerId.split(",");
					for(String customerId : customerIds) {
						User rUser = userService.getUserByCustomerId(customerId);
						if(rUser!=null && ("1").equals(rUser.getUserType())) {
							//判断关联关系在新表中是否存在
							Integer userId1 = u.getId();
							Integer userId2 = rUser.getId();
							List<UserRelation> list = userRelationService.searchRelation(userId1, userId2);
							if(list.isEmpty()) {
								UserRelation ur = new UserRelation();
								ur.setUserId(userId1);
								ur.setRelatedUserId(userId2);
								if(userRelationService.add(ur)) {
									logger.info("添加" + u.getUserName() + " 和 " + rUser.getUserName() + " 关联关系成功");
								} else {
									logger.info("添加" + u.getUserName() + " 和 " + rUser.getUserName() + " 关联关系失败");
								}
							}
						} else {
							continue;
						}
					}
				} else {
					continue;
				}
			}
		}
		
//		UserCustom userCustom = new UserCustom();
//		userCustom.setType("1");
//		List<UserCustom> userCustomList = userCustomService.searchUserCustom(userCustom);
//		if(!userCustomList.isEmpty()) {
//			logger.info("拥有关联关系的数据共有："+userCustomList.size());
//			for(UserCustom uc : userCustomList) {
//				String userName1 = uc.getUserName();
//				String userName2 = uc.getBindedUserName();
//				if(userName1.equals(userName2)) {
//					continue;
//				} else {
//					User user1 = userService.getUserByUserName(userName1);
//					User user2 = userService.getUserByUserName(userName2);
//					//存放卖家主账号的id
//					if(user1 != null && ("1").equals(user1.getUserType())  && user2 != null && ("2").equals(user2.getUserType())) {
//						//判断关联关系在新表中是否存在
//						Integer userId1 = user1.getId();
//						Integer userId2 = user2.getId();
//						List<UserRelation> list = userRelationService.searchRelation(userId1, userId2);
//						if(list.isEmpty()) {
//							UserRelation ur = new UserRelation();
//							ur.setUserId(userId1);
//							ur.setRelatedUserId(userId2);
//							if(userRelationService.add(ur)) {
//								logger.info("添加" + userName1 + " 和 " + userName2 + " 关联关系成功");
//							} else {
//								logger.info("添加" + userName1 + " 和 " + userName2 + " 关联关系失败");
//							}
//						} else {
//							continue;
//						}
//					}
//				}
//			}
//		}
		logger.info("执行结束");
	}


	public UserCustomService<UserCustom> getUserCustomService() {
		return userCustomService;
	}


	public void setUserCustomService(UserCustomService<UserCustom> userCustomService) {
		this.userCustomService = userCustomService;
	}


	public UserRelationService<UserRelation> getUserRelationService() {
		return userRelationService;
	}


	public void setUserRelationService(
			UserRelationService<UserRelation> userRelationService) {
		this.userRelationService = userRelationService;
	}


	public UserService<User> getUserService() {
		return userService;
	}


	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}
	
	
}
