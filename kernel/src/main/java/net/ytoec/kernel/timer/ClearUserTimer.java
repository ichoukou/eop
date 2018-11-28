package net.ytoec.kernel.timer;

import java.util.ArrayList;
import java.util.List;

import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.UserService;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import org.apache.log4j.Logger;

public class ClearUserTimer extends QuartzJobBean{

	private UserService<User> userService ;

	private List<User> userList = new ArrayList<User>();
    private static Logger logger = Logger.getLogger(ClearUserTimer.class);
    
    
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		userList = userService.searchUserByTBKeyAndUserNameAndShopAccount();
		logger.debug("满足条件的记录数："+userList.size());
		boolean flag = false;
		User user = null;
		for (int i = 0; i < userList.size(); i++) {
			user = userList.get(i);
			try {
				flag = userService.delUserById(user.getId());
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("删除失败，用户Id是："+user.getId());
			}
			
		}


	}
	
	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}


}
