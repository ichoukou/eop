/**
 * ConfigAction.java
 * Wangyong
 * 2011-8-29 上午11:09:19
 */
package net.ytoec.kernel.action.channel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.ConfigCode;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.ConfigCodeService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 系统配置项目Action
 * @author Wangyong
 * @2011-8-29
 * net.ytoec.kernel.action.channel
 */
@Component
@Scope("prototype")
public class ConfigAction extends AbstractActionSupport {

	@Inject
	private ConfigCodeService<ConfigCode> configService;
	
	private Integer id;
	private Integer pid;
	private Integer levelId; //级别标示符
	private String key;
	
	private List<ConfigCode> configList;
	private List<ConfigCode> dataList;
	private ConfigCode configCode;
	
	//private static Integer pageNum = 5;
	private Integer currentPage = 1;
	private Pagination pagination; //分页对象
	
	private String response;
	
	/**
	 * 显示子配置项信息
	 * @return
	 */
	public String list(){
		this.configCode = null;
		this.dataList = null;
		configCode = configService.getConfigById(pid);
		dataList = configService.getConfByPid(pid);
		return "list";
	}
	
	/**
	 * 显示第一级配置项信息
	 * @return
	 */
	public String index(){
		User currentUser = super.readCookieUser();
		response = null;
		pagination = new Pagination(currentPage, pageNum);
		if(currentUser.getUserType()!=null && currentUser.getUserType().equals("3")){
			configList = configService.getConfigByLevel(pagination, "1", true);
			pagination.setTotalRecords(configService.getConfigByLevel(pagination, "1", false).size());
		}else{
			pagination.setTotalRecords(0);
			response = "您没有查询该项操作的权限!";
		}
		return "index";
	}
	
	/**
	 * 显示增加第一级配置项
	 * @return
	 */
	public String addUI(){
		this.configCode = null;
		return "addUI";
	}
	
	/**
	 * 增加配置信息:这里在成功添加后需要更新oscache缓存
	 * @return
	 */
	public String add(){
		if(configService.addConfig(configCode,levelId)){
			configService.putConfigCodeCache(configCode.getConfKey(), configCode);
			response = "添加成功!";
			configCode = null;
		}else{
			response = "添加失败	!";
			configCode = null;
		}
		return "add";
	}
	
	/**
	 * 显示增加子配置项
	 * @return
	 */
	public String addNextUI(){
		this.configCode = null;
		configCode = configService.getConfigById(pid);
		return "addNextUI";
	}
	
	/**
	 * 显示编辑第一级配置项
	 * @return
	 */
	public String editUI(){
		this.configCode = null;
		configCode = configService.getConfigById(id);
		return "editUI";
	}
	
	/**
	 * 显示编辑子配置项
	 * @return
	 */
	public String editNextUI(){
		this.configCode = null;
		configCode = configService.getConfigById(id);
		return "editNextUI";
	}
	
	/**
	 * 编辑配置项信息：成功编辑之后需要更新oscache缓存
	 * @return
	 */
	public String edit(){
		if(configService.editConfig(configCode)){
			configService.putConfigCodeCache(configCode.getConfKey(), configCode);
			response = "编辑成功!";
		}else{
			response = "编辑失败	!";
		}
		return "edit";
	}
	
	/**
	 * 删除配置项信息：成功删除后需要删除oscache中缓存
	 * @return
	 */
	public String remove(){
		ConfigCode config = configService.getConfigById(id);
		List list = configService.getConfByPid(id);
		if(list.size()>0){
			response = "存在子目录，不可删除!";
		}else{
			if(configService.removeConfig(config)){
				configService.removeConfigCodeCache(config.getConfKey());
				response = "删除成功!";
			}else{
				response = "删除失败	!";
			}
		}
		return "remove";
	}
	
	public String keyUnique(){
		response = null;
		if(configService.getConfByKey(key)!=null){
			response = "0"; //输入的键值已存在
			key = null;
		}else{
			response = "1";
			key = null;
		}
		return "keyUnique";
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public List<ConfigCode> getConfigList() {
		return configList;
	}

	public void setConfigList(List<ConfigCode> configList) {
		this.configList = configList;
	}

	public ConfigCode getConfigCode() {
		return configCode;
	}

	public void setConfigCode(ConfigCode configCode) {
		this.configCode = configCode;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<ConfigCode> getDataList() {
		return dataList;
	}

	public void setDataList(List<ConfigCode> dataList) {
		this.dataList = dataList;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
