/**
 * MainPageAction.java
 * Wangyong
 * 2011-8-4 下午04:09:06
 */
package net.ytoec.kernel.action.login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.MessageUser;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserBean;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.dataobject.UserThreadContract;
import net.ytoec.kernel.dto.DtoQuestion;
import net.ytoec.kernel.service.MessageUserService;
import net.ytoec.kernel.service.QuestionDtoService;
import net.ytoec.kernel.service.UserBeanService;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.service.UserThreadContractService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.service.ArticleService;
import com.ytoec.uninet.util.HessianUtil;
/**
 * @author Wangyong
 * @2011-8-4
 * net.ytoec.kernel.action.login
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class MainPageAction extends AbstractActionSupport {
	
	private Logger logger = Logger.getLogger(MainPageAction.class);
	
	@Inject
	private MessageUserService<MessageUser> messageUserService;
	@Inject
	private QuestionDtoService<DtoQuestion> dtoQuestionService;
	@Inject
	private UserThreadContractService<UserThreadContract> userThreadContractService;
	@Inject
	private ArticleService<Article> articleService;
	@Inject
	private UserService<User> userService;
	@Inject
	private UserCustomService<UserCustom> userCustomService;
	@Inject
	private UserBeanService<UserBean> userBeanService;
	private User user;
	//未读消息数目
	private Integer unReadMessageNum = 0;
	//未读问题件
	private Integer unReadQuestionNum = 0;
	//子帐号数目
	private Integer userNum;
	//我的客户的数目
	private Integer myCustomNum;
	//卖家未处理问题件
	private Integer unHandleQuestionNum = 0;
	private List<ConfigCode> secondMenuList;
	private Integer pid;
	private List<ConfigCode> thirdMenuList;
	private List<User> vipList;
	private List<User> fencangList;
	
	private List<String> recentFiveDay;
	private Integer articleId;
	private Article article;
	private Integer flag;//1表示查询最新动态
	
	//最新动态列表
	private List<Article> newsList;
	
	//客户账号列表
	private List<User> associationAccountList;
	
	private Integer currentPage = 1;
	private Pagination<User> pagination;
	
	private Date lastLoginTime;
	
	private String jsonResult;

	public String home(){
		user = super.readCookieUser();
		unReadMessageNum = unReadQuestionNum = 0;
		Integer userId = null;
		if(user.getUserType().equals("1") || user.getUserType().equals("2") || user.getUserType().equals("3") || user.getUserType().equals("4"))
			userId = user.getId();
		else
			userId = user.getParentId();
		List<MessageUser> msglist = messageUserService.getByStatusAndUser(userId, 0);
		if(msglist != null && msglist.size() > 0)
		    unReadMessageNum = msglist.size();
		try {
		    if(user.getUserType()!=null && !user.getUserType().equals("12") && !user.getUserType().equals("22")){
		        unReadQuestionNum = dtoQuestionService.noneReadQuestion(user, "0");
		    }
		    else{
		        unReadQuestionNum = 0;
		    }
		} catch (ParseException e) {
			logger.error("读取问题件消息失败！");
		}
		
		//取最新动态列表
		if(user.getUserType()!=null){
			if(user.getUserType().equals("1") || user.getUserType().equals("11") || user.getUserType().equals("12") || user.getUserType().equals("13")){
				newsList = articleService.getArticlesByCol("yitong_01", "1", Constants.NEW_COLUMN_NUM);
				//关联账号数量
				myCustomNum = 0;
				List<Integer> list = Resource.getUserRelationUserIdList(user);
				if(list!=null&&list.size()>0){
					myCustomNum = list.size();
				}
				//获取最近五天的时间列表
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				GregorianCalendar gc = new GregorianCalendar();
				recentFiveDay = new ArrayList<String>();
				for(int i=1; i<=5; i++){
					gc.setTime(new Date());
					gc.add(Calendar.DAY_OF_MONTH, -i);
					recentFiveDay.add(sdf.format(gc.getTime()));
				}
				//获取子帐号的数目
                userNum = userService.getUserByParentId(userId).size();
				return "home_vip";
			}
			if(user.getUserType().equals("2") || user.getUserType().equals("21") || user.getUserType().equals("22") || user.getUserType().equals("23")){
				
				//获取子帐号的数目
				userNum = userService.getUserByParentId(userId).size();
				//获取主帐号绑定客户的数目
				if(user.getParentId() != null){//网点子帐号
					List<UserThreadContract> contractList = new ArrayList<UserThreadContract>();
					contractList = userThreadContractService.getContractersByUserNameAndType(user.getUserName(),user.getUserType());
					if(contractList!=null && contractList.size() !=0){
						myCustomNum = contractList.size();
					}else{
						myCustomNum = 0;
					}
				}else{                         //网点主帐号
					myCustomNum = userBeanService.countUserList(
							null,user.getSite(), null, null, "1", null, false);
				}
				newsList = articleService.getArticlesByCol("yitong_01", "2", Constants.NEW_COLUMN_NUM);
				return "home_site";
			}
			if(user.getUserType().equals("4") || user.getUserType().equals("41")){
				//获取最近五天的时间列表
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				GregorianCalendar gc = new GregorianCalendar();
				recentFiveDay = new ArrayList<String>();
				for(int i=1; i<=5; i++){
					gc.setTime(new Date());
					gc.add(Calendar.DAY_OF_MONTH, -i);
					recentFiveDay.add(sdf.format(gc.getTime()));
				}
				
				//业务账号列表
				fencangList = userService.pingTaiSelect(user,0);
				
				//业务账号-分仓账号列表
				vipList = userService.pingTaiSelect(user,1);
				
				newsList = articleService.getArticlesByCol("yitong_01", "1","2", Constants.NEW_COLUMN_NUM);
				
				//客户账号列表
				pagination = new Pagination(currentPage, super.pageNum);
				Map map = userService.getUserByParentId(user.getId(), pagination);
				associationAccountList = (List) map.get("list");
				return "home_pingtai";
			}
			newsList = articleService.getArticlesByCol("yitong_01", null, Constants.NEW_COLUMN_NUM);
		}
		return "home";
	}
	
	public String getUnReadMsgNum(){
		unReadMessageNum = 0;
		user = super.readCookieUser();
		Integer userId = null;
		if(user.getUserType().equals("1") || user.getUserType().equals("2") || user.getUserType().equals("3") || user.getUserType().equals("4"))
			userId = user.getId();
		else
			userId = user.getParentId();
		List<MessageUser> list = messageUserService.getByStatusAndUser(userId, 0);
		if(list != null && list.size() > 0)
			unReadMessageNum = list.size();
		return "getUnReadMsgNum";
	}
	
	public String getUnReadQuesNum(){
		unReadQuestionNum = 0;
		user = super.readCookieUser();
		try {
			if(user.getUserType()!=null && !user.getUserType().equals("12") && !user.getUserType().equals("22")){
				unReadQuestionNum = dtoQuestionService.noneReadQuestion(user, "0");
				unHandleQuestionNum = dtoQuestionService.noneHandleQuestion(user, "1");
			}
			else{
				unReadQuestionNum = 0;
				unHandleQuestionNum = 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "getUnReadQuesNum";
	}
	
	/**
	 * 实时获取未读问题件和未读消息数目
	 * @return
	 */
	public String getUnReadNum(){
		user = super.readCookieUser();
		unReadMessageNum = 0;
		Integer userId = null;
		if(user.getUserType().equals("1") || user.getUserType().equals("2") || user.getUserType().equals("3") || user.getUserType().equals("4"))
			userId = user.getId();
		else
			userId = user.getParentId();
		List<MessageUser> list = messageUserService.getByStatusAndUser(userId, 0);
		if(list != null && list.size() > 0) {
				unReadMessageNum = list.size();
		}
		try {
			if(user.getUserType()!=null && !user.getUserType().equals("12") && !user.getUserType().equals("22")){
				unReadQuestionNum = dtoQuestionService.noneReadQuestion(user, "0");
				unHandleQuestionNum = dtoQuestionService.noneHandleQuestion(user, "1");
			}
			else{
				unReadQuestionNum = 0;
				unHandleQuestionNum = 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		jsonResult = "";
		
		if (unReadMessageNum > 999) {
			jsonResult += "N,";
		} else {
			jsonResult += unReadMessageNum.toString() + ",";
		}
		if (unReadQuestionNum > 999) {
			jsonResult += "N,";
		} else {
			jsonResult += unReadQuestionNum.toString() + ",";
		}
		if (unHandleQuestionNum > 999) {
			jsonResult += "N,";
		} else {
			jsonResult += unHandleQuestionNum.toString();
		}
		return "getUnReadNum";
	}

    /**
     * 查看最新动态详细
     * 
     * @return
     */
    public String detailArticle() {
        if (article != null && article.getArticleId() > 0) {
            article = articleService.get(article.getArticleId());
            article.setContent(addPathToImg(article.getContent()));
        }
        return "detailArticle";
    }

	/**
     * 给文章内容中的图片等文件添加域名，以便显示
     * 
     * @param content
     * @return
     */
    private String addPathToImg(String content) {
        if (!StringUtils.isNotEmpty(content)) {
            return "";
        }
        List<String> imgUris = getImgUris(content, false);
        for (String uri : imgUris) {
            content = content.toLowerCase().replace(uri, getAbsImgUrl(uri));
        }
        return content;
    }
    
    /**
     * 获取新增的或修改的图片uri以上传（原有的附件不作任何操作）
     * 
     * @param content 文章内容
     * @param onlyTemp 是否只取临时图片URI(需要当前上传的图片)
     * @return
     */
    private List<String> getImgUris(String content, boolean onlyTemp) {
        List<String> imgUris = new ArrayList<String>();
        if (!StringUtils.isNotEmpty(content)) {
            return imgUris;
        }
        if (content.toLowerCase().contains("<img")) {
            int start = content.toLowerCase().indexOf("<img");
            int end = content.toLowerCase().indexOf("/>", start);
            String tmpAbsURL = HessianUtil.getMediaPath() + "/temp";
            while (start > -1 && end > -1) {
                start = content.toLowerCase().indexOf("src=", start) + 5;
                end = content.toLowerCase().indexOf("\"", start);
                String uri = content.substring(start, end);
                start = content.toLowerCase().indexOf("<img", end);
                end = content.toLowerCase().indexOf("/>", start);
                if (!uri.contains(tmpAbsURL) && onlyTemp) {
                    continue;
                }
                if (uri.contains(tmpAbsURL)) {
                    uri = uri.replace(tmpAbsURL, "");
                }
                imgUris.add(uri);
            }
        }
        return imgUris;
    }

    /**
     * 给图片添加服务器域名，以便页面显示
     * 
     * @param uri
     * @return
     */
    private String getAbsImgUrl(String uri) {
        if (StringUtils.isNotEmpty(uri) && uri.startsWith("/")) {
            return HessianUtil.getMediaPath() + uri;
            // return HessianUtil.getDomain() + uri;
        } else {
            return uri;
        }
    }
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUnReadMessageNum() {
		return unReadMessageNum;
	}

	public void setUnReadMessageNum(Integer unReadMessageNum) {
		this.unReadMessageNum = unReadMessageNum;
	}

	public Integer getUnReadQuestionNum() {
		return unReadQuestionNum;
	}

	public void setUnReadQuestionNum(Integer unReadQuestionNum) {
		this.unReadQuestionNum = unReadQuestionNum;
	}

	public Integer getUnHandleQuestionNum() {
		return unHandleQuestionNum;
	}

	public void setUnHandleQuestionNum(Integer unHandleQuestionNum) {
		this.unHandleQuestionNum = unHandleQuestionNum;
	}

	public List<ConfigCode> getSecondMenuList() {
		return secondMenuList;
	}

	public void setSecondMenuList(List<ConfigCode> secondMenuList) {
		this.secondMenuList = secondMenuList;
	}

	public List<ConfigCode> getThirdMenuList() {
		return thirdMenuList;
	}

	public void setThirdMenuList(List<ConfigCode> thirdMenuList) {
		this.thirdMenuList = thirdMenuList;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public List<Article> getNewsList() {
		return newsList;
	}

	public List<String> getRecentFiveDay() {
		return recentFiveDay;
	}

	public void setRecentFiveDay(List<String> recentFiveDay) {
		this.recentFiveDay = recentFiveDay;
	}

	public void setNewsList(List<Article> newsList) {
		this.newsList = newsList;
	}
	public List<User> getVipList() {
		return vipList;
	}

	public void setVipList(List<User> vipList) {
		this.vipList = vipList;
	}

	public List<User> getAssociationAccountList() {
		return associationAccountList;
	}

	public void setAssociationAccountList(List<User> associationAccountList) {
		this.associationAccountList = associationAccountList;
	}

	public Integer getUserNum() {
		return userNum;
	}

	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}

	public Integer getMyCustomNum() {
		return myCustomNum;
	}

	public void setMyCustomNum(Integer myCustomNum) {
		this.myCustomNum = myCustomNum;
	}

	public List<User> getFencangList() {
		return fencangList;
	}

	public void setFencangList(List<User> fencangList) {
		this.fencangList = fencangList;
	}
	
	
}
