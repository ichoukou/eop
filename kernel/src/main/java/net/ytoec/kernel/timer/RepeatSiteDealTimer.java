/**
 * 2012-5-10上午10:03:13
 * wangyong
 */
package net.ytoec.kernel.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import net.ytoec.kernel.dao.PosttempUserDao;
import net.ytoec.kernel.dataobject.MailTendency;
import net.ytoec.kernel.dataobject.MessageUser;
import net.ytoec.kernel.dataobject.Posttemp;
import net.ytoec.kernel.dataobject.PosttempUser;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MailTendencyService;
import net.ytoec.kernel.service.MessageUserService;
import net.ytoec.kernel.service.PosttempService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * 删除user表中的重复网点数据，如果重复的网点在模板表  消息表  网点趋势图表中都存在的话，保留用户最近一次登录时的网点，并把删除的网点<br>
 * 对应的id替换为保留的网点id。
 * @author wangyong
 * 2012-5-10
 */
public class RepeatSiteDealTimer extends TimerTask {
	
	private static Logger logger = Logger.getLogger(RepeatSiteDealTimer.class);
	
	private UserService<User> userService;
	
	private PosttempService<Posttemp> posttempService;
 	
	private PosttempUserDao<PosttempUser> posttempUserDao;
	
	private MessageUserService<MessageUser> messageUserService;
	
	private MailTendencyService<MailTendency> mailTendencyService;
	
	private static final String POSTTEMP = "POSTTEMP";//模板用户关联表
	private static final String MESSAGE = "MESSAGE";//消息表
	private static final String TENDENCY = "TENDENCY";//网点走势表

	@Override
	public void run() {
		/**
		 * 首先找出用户表中重复的网点数据：userType="2"且userCode存在相同的记录
		 */
		List<User> list = userService.getRepeatSiteList();
		logger.info(list.size());
		/**
		 * 对重复的数据进行筛选：如果重复的数据id存在于消息表、走势表、运费模板表则保留最近一次登录的用户数据。否则就
		 * 删除没使用过的网点数据
		 */
		
		for(User user : list){
			//在用户编码user.getUserCode()下重复的网点信息
			List<User> repeatUser = userService.searchUsersByCodeAndType(user.getUserCode(), "2");
			
			if(repeatUser!=null && !repeatUser.isEmpty()){
				//repeatSiteMap 键对应网点id，值为该网点id所对应的验证区域（“模板”、“消息”、“走势图”）中对应的记录数目
				Map<Integer,Map<String, Integer>> repeatSiteMap = new HashMap<Integer,Map<String, Integer>>();
				
				//存放重复网点id
				Integer[] siteIdArr = new Integer[repeatUser.size()];
				
				Integer recentlyId = repeatUser.get(0).getId();
				Date recentlyDate = repeatUser.get(0).getLoginTime();
				/**
				 * 获取最近一次登录的某个网点
				 */
				for(int i=0; i<repeatUser.size(); i++){
					siteIdArr[i] = repeatUser.get(i).getId();
					if(DateUtil.compareDay(recentlyDate, repeatUser.get(i).getLoginTime())){
						recentlyDate = repeatUser.get(i).getLoginTime();
						recentlyId = repeatUser.get(i).getId();
					}
				}
				//保留的网点id
				Integer retentionId = recentlyId;
				
				logger.error("用户编码为："+user.getUserCode()+"的网点最近一次登录时间："+recentlyDate+"   网点id是："+recentlyId);
				
				for(User site : repeatUser){
					Map<String, Integer> repeatSite = new HashMap<String, Integer>();
					Integer siteId = site.getId();
					/**
					 * 验证网点是否创建模板
					 */
					List<PosttempUser> posttempUserList = posttempUserDao.getPosttempUserByBranchId(siteId);
					if(posttempUserList!=null)
						repeatSite.put(POSTTEMP, posttempUserList.size());
					else
						repeatSite.put(POSTTEMP, 0);
					/**
					 * 验证网点是否存在消息
					 */
					List<MessageUser> messageList = messageUserService.getByUserId(siteId);
					if(messageList!=null)
						repeatSite.put(MESSAGE, messageList.size());
					else
						repeatSite.put(MESSAGE, 0);
					/**
					 * 验证是否存在走势数据
					 */
					List<MailTendency> tendencyList = mailTendencyService.getRepeatSiteList(siteId.toString());
					if(tendencyList!=null)
						repeatSite.put(TENDENCY, tendencyList.size());
					else
						repeatSite.put(TENDENCY, 0);
					repeatSiteMap.put(siteId, repeatSite);
				}
				
				/**
				 * 执行任务的主要操作：过滤重复数据、更新重复网点
				 */
				filterRepeatSite(retentionId, siteIdArr, repeatSiteMap);
			}
		}
	}
	
	/**
	 * 过滤重复的网点数据
	 * @param retentionId 最近一次登录使用的网点id
	 * @param siteIdArr 同个用户编码下的重复网点id数组
	 * @param repeatSiteMap 不同网点id对应在不同区域内其对应的数据量
	 * @return 如果网点数组内的id不存在或者存在用户模板表数据的话就返回数据量大的某个网点
	 * 该方法的规则。
	 */
	@SuppressWarnings("unused")
	private synchronized void filterRepeatSite(Integer retentionId, Integer[] siteIdArr, Map<Integer, Map<String, Integer>> repeatSiteMap){
		//获取最近一次登录用户的在不同表中的数据量大小
		Map<String, Integer> retentionSiteStatics = repeatSiteMap.get(retentionId);
		Integer retentionPosttempStatics = 0;//模板数据量
		Integer retentionTendencyStatics = 0;//走势数据量
		Integer retentionMessageStatics = 0;//消息数据量
		retentionPosttempStatics = retentionSiteStatics.get(POSTTEMP);
		retentionTendencyStatics = retentionSiteStatics.get(TENDENCY);
		retentionMessageStatics = retentionSiteStatics.get(MESSAGE);
		logger.error("最近一次登录网点id:"+retentionId+"最近一次POSTTEMP数据是："+retentionPosttempStatics+"最近一次TENDENCY数据是："
				+retentionTendencyStatics+"最近一次MESSAGE数据是："+retentionMessageStatics);
		//将非retentionId的数据与retentionId的数据进行比较
		for(int i=0; i<siteIdArr.length; i++){
			if(siteIdArr[i]!=retentionId){
				Map<String, Integer> currentSiteStatics = repeatSiteMap.get(siteIdArr[i]);//当前非retentionId对应的Map数据
				Integer currentPosttempStatics = currentSiteStatics.get(POSTTEMP);
				Integer currentTendencyStatics = currentSiteStatics.get(TENDENCY);
				Integer currentMessageStatics = currentSiteStatics.get(MESSAGE);
				logger.error("当前网点id:"+siteIdArr[i]+"当前POSTTEMP数据是："+currentPosttempStatics+"当前TENDENCY数据是："
						+currentTendencyStatics+"当前MESSAGE数据是："+currentMessageStatics);
				if(retentionPosttempStatics<currentPosttempStatics)//对比用户模板表中的数据
				{
					retentionPosttempStatics = currentPosttempStatics;//将大的数据赋值给最近登录网点的数据
					retentionId = siteIdArr[i];//将大的数据对应的网点赋值给最近登录网点
				}
				logger.error("比较后的结果数据："+retentionPosttempStatics+" 对应的网点id是："+retentionId);
			}
		}
		logger.error("最终返回的结果数据："+retentionPosttempStatics+" 对应的网点id是："+retentionId);
		
		//更新数据和更新重复数据:在非保留的网点用户名后面追加字符串"correct"
		for(int i=0; i<siteIdArr.length; i++){
		    posttempService.clearPostForRepeatUser(siteIdArr[i], retentionId);
		}
	}
	
	
	
	public UserService<User> getUserService() {
		return userService;
	}

	public void setUserService(UserService<User> userService) {
		this.userService = userService;
	}

	public PosttempUserDao<PosttempUser> getPosttempUserDao() {
		return posttempUserDao;
	}

	public void setPosttempUserDao(PosttempUserDao<PosttempUser> posttempUserDao) {
		this.posttempUserDao = posttempUserDao;
	}

	public MessageUserService<MessageUser> getMessageUserService() {
		return messageUserService;
	}

	public void setMessageUserService(
			MessageUserService<MessageUser> messageUserService) {
		this.messageUserService = messageUserService;
	}

	public MailTendencyService<MailTendency> getMailTendencyService() {
		return mailTendencyService;
	}

	public void setMailTendencyService(
			MailTendencyService<MailTendency> mailTendencyService) {
		this.mailTendencyService = mailTendencyService;
	}

    
    public PosttempService<Posttemp> getPosttempService() {
        return posttempService;
    }

    
    public void setPosttempService(PosttempService<Posttemp> posttempService) {
        this.posttempService = posttempService;
    }

}
