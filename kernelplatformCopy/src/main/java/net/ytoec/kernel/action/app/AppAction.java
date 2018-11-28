package net.ytoec.kernel.action.app;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.eop.app.bean.App;
import net.ytoec.eop.app.service.AppService;
import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.AppProvider;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.service.AppProviderService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ytoec.uninet.util.HessianUtil;

/**
 * 应用审核管理action类。
 * @author zhengliang
 */
@Controller
@Scope("prototype")
public class AppAction extends AbstractActionSupport {

	@Inject
	private AppService<App> appService;
	@Inject
	private AppProviderService<AppProvider> providerService;

	private static final long serialVersionUID = -5572914485701793113L;

	private static Logger log = LoggerFactory.getLogger(AppAction.class);
	
	private App app;
	private List<App> apps;
	@SuppressWarnings("rawtypes")
	private Pagination pagination;
	private Integer currentPage = 1;
	private String jsonStr;
	private String mediaPath;
	private String starttime,endtime;
	
	public App getApp() {
		return app;
	}
	public List<App> getApps() {
		return apps;
	}
	@SuppressWarnings("rawtypes")
	public Pagination getPagination() {
		return pagination;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public String getJsonStr() {
		return jsonStr;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public void setApps(List<App> apps) {
		this.apps = apps;
	}
	@SuppressWarnings("rawtypes")
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	
	
	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}
	public String getMediaPath() {
		return mediaPath;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getStarttime() {
		return starttime;
	}
	/**
	 * 应用管理
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String search(){
		if (pagination == null) {
			pagination = new Pagination(currentPage, pageNum);
		}
		Map<String, Object> params=new HashMap<String, Object>();
		if(app != null){
			if(app.getUserId() != null){
				params.put("userId", app.getUserId());
			}
			if(StringUtils.isNotBlank(app.getAppName())){
				params.put("appName", app.getAppName());
			}
			if(StringUtils.isNotBlank(app.getAppstatus())){
				params.put("appstatus", app.getAppstatus());
			}
			if(StringUtils.isNotBlank(app.getApptype())){
				params.put("apptype", app.getApptype());
			}
			if(StringUtils.isNotBlank(app.getOperatemode())){
				params.put("operatemode", app.getOperatemode());
			}
		}
		if(StringUtils.isNotBlank(starttime) && StringUtils.isNotBlank(endtime)){
			params.put("startTime", starttime);
			params.put("endTime", endtime);
		}
		pagination=appService.getApplist(pagination,params);
		apps=pagination.getRecords();
		for(App app : apps){
			AppProvider provider = providerService.getByUserId(app.getUserId());
			app.setProvider(provider);
		}
		
		return "search";
	}
	
	/**
	 * 跳转到应用审核页面
	 * @return
	 */
	public String toapproval(){
		if(app != null){
			app = appService.get(app);
			AppProvider provider = providerService.getByUserId(app.getUserId());
			app.setProvider(provider);
			mediaPath = HessianUtil.getMediaPath();
		}
		else{
			log.error("app对象数据丢失");
		}
		return "toApproval";
	}
	
	/**
	 * 应用审核
	 * @return
	 */
	public String approval(){
		if(app != null){
			App oldApp = appService.get(app.getId());
			oldApp.setUpdateTime(new Date());
			//应用审核
			if(App.STATUS_IN_USE.equals(app.getAppstatus())){
				if(oldApp.getAppstatus().equals(App.STATUS_APPROVING)){
					oldApp.setAppstatus(App.STATUS_IN_USE);
					boolean rs = appService.edit(oldApp);
					if(rs){
						putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"应用审核操作已成功！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
					}
					else{
						putMsg(JsonResponse.INFO_TYPE_ERROR,false,"应用审核操作失败！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
					}
				}
				else{
					putMsg(JsonResponse.INFO_TYPE_WARNING,true,"操作未成功，该应用还未提交审核或者已禁用！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
				}
			}
			//禁用应用
			else if(App.STATUS_LOCKED.equals(app.getAppstatus())){
				if(oldApp.getAppstatus().equals(App.STATUS_LOCKED)){
					putMsg(JsonResponse.INFO_TYPE_WARNING,true,"操作未成功，该应用已经是禁用状态！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
				}
				else{
					oldApp.setAppstatus(App.STATUS_LOCKED);
					boolean rs = appService.edit(oldApp);
					if(rs){
						putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"应用禁用操作已成功！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
					}
					else{
						putMsg(JsonResponse.INFO_TYPE_ERROR,false,"应用禁用操作失败！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
					}
				}
			}
			//拒绝审核通过
			else if(App.STATUS_APPROVING.equals(app.getAppstatus())){
				if(oldApp.getAppstatus().equals(App.STATUS_APPROVING)){
					oldApp.setAppstatus(App.STATUS_APPROVING);
					boolean rs = appService.edit(oldApp);
					if(rs){
						putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"应用审核拒绝操作已成功！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
					}
					else{
						putMsg(JsonResponse.INFO_TYPE_ERROR,false,"应用审核拒绝操作失败！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
					}
				}
				else{
					putMsg(JsonResponse.INFO_TYPE_WARNING,true,"操作未成功，该应用还未提交审核或者已禁用！", "toapprovalApp.action?menuFlag=home_app_list&app.id="+oldApp.getId());
				}
			}
			return "jsonRes";
			
		}
		else{
			putMsg(JsonResponse.INFO_TYPE_ERROR,true,"数据丢失，操作失败！", "");
		}
		return "jsonRes";
	}

}
