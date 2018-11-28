/**
 * HomeAction.java
 * Wangyong
 * 2012-02-24 下午04:09:06
 * 2012-6-26此类由MainPageAction完全替代。
 */
package net.ytoec.kernel.action.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.UserCustom;
import net.ytoec.kernel.service.UserCustomService;
import net.ytoec.kernel.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.service.ArticleService;
import com.ytoec.uninet.util.HessianUtil;

/**
 * @author Wangyong
 * @2012-02-24
 * net.ytoec.kernel.action.home
 */
@Controller
@Scope("prototype")
public class HomeAction extends AbstractActionSupport {
	private static final long serialVersionUID = -5178047121530675868L;
	@Inject
	private ArticleService<Article> articleService;
	@Inject
	private UserService<User> userService;
	@Inject
	private UserCustomService<UserCustom> userCustomService;
	
	private User user;
	private List<String> recentFiveDay;
	private Integer articleId;
	private Article article;
	private Integer flag;//1表示查询最新动态
	private String instruction;
	private List<User> vipList;
	public List<User> getVipList() {
		return vipList;
	}

	public void setVipList(List<User> vipList) {
		this.vipList = vipList;
	}

	//最新动态列表
	private List<Article> newsList;
	
	public String home(){
		user = super.readCookieUser();
//		if(flag!=null && flag == 1 && articleId!=null){
//			article = articleService.get(articleId);
//			return "news";
//		}
		String resStr = "home";
		//取最新动态列表
		if(user.getUserType()!=null){
			newsList = new ArrayList<Article>();
			if(user.getUserType().equals("1") || user.getUserType().equals("11") || user.getUserType().equals("12") || user.getUserType().equals("13")){
				newsList = articleService.getArticlesByCol("yitong_01", "1","2", Constants.NEW_COLUMN_NUM);
				//获取关联店铺名称
				UserCustom userCustom = new UserCustom();
				userCustom.setUserName(user.getUserName());
				userCustom.setType("1");
				userCustom.setRelationalQuery("1");
				List<UserCustom> list = userCustomService.searchUserCustom(userCustom);
				if(list!=null&&list.size()>0){
					StringBuffer relativeShop = new StringBuffer();
					for(UserCustom cus : list){
						User uObj = userService.searchUsersByUserName(cus.getBindedUserName());
						if(uObj!=null&&uObj.getShopName()!=null)
							relativeShop.append(uObj.getShopName()+",");
					}
					if(!relativeShop.toString().equals(""))
						user.setShopName(relativeShop.toString().substring(0, relativeShop.toString().length()-1));
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
				resStr = "home_vip";
			}
			else if(user.getUserType().equals("2") || user.getUserType().equals("21") || user.getUserType().equals("22") || user.getUserType().equals("23")){
				newsList = articleService.getArticlesByCol("yitong_01", "2","2", Constants.NEW_COLUMN_NUM);
				resStr = "home_site";
			}
			else if(user.getUserType().equals("4") || user.getUserType().equals("41")){
				//获取最近五天的时间列表
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				GregorianCalendar gc = new GregorianCalendar();
				recentFiveDay = new ArrayList<String>();
				for(int i=1; i<=5; i++){
					gc.setTime(new Date());
					gc.add(Calendar.DAY_OF_MONTH, -i);
					recentFiveDay.add(sdf.format(gc.getTime()));
				}
				
				vipList = userService.pingTaiSelect(user,1);
				
				newsList = articleService.getArticlesByCol("yitong_01", "1","2", Constants.NEW_COLUMN_NUM);
				resStr = "home_pingtai";
			}else{
				newsList = articleService.getArticlesByCol("yitong_01", "","2", Constants.NEW_COLUMN_NUM);
			}
			/**
			 * 添加所有用户可见动态
			 */
//			List<Article> publicArtList = articleService.getArticlesByCol("col_news", "0","2", Constants.NEW_COLUMN_NUM);
//			if(publicArtList!=null && publicArtList.size() > 0){
//				newsList.addAll(publicArtList);
//			}
//			newsList = ObjectUtils.sort(newsList, "createTime", true);
//			if(newsList.size() > Constants.NEW_COLUMN_NUM){
//				newsList = newsList.subList(0, 4);
//			}
		}
		return resStr;
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

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
}
