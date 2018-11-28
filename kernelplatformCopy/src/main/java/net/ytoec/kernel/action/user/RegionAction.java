/**
 * 
 */
package net.ytoec.kernel.action.user;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dataobject.Region;
import net.ytoec.kernel.service.RegionService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * @author Wangyong
 * @2011-7-26
 * net.ytoec.kernel.action.user
 */
@Controller
@Scope("prototype")
public class RegionAction extends ActionSupport {
	
	@Inject
	private RegionService<Region> regionService;

	private Integer parentId;
	private List<Region> regionList;
	private List<Region> response;
	
	public String show(){
		regionList = regionService.getRegionByParentId(0);
		return "show";
	}
	
	public String getCity(){
		response = regionService.getRegionByParentId(parentId);
		return "success";
	}
	
	public String getCountry(){
		response = regionService.getRegionByParentId(parentId);
		return "success";
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<Region> getResponse() {
		return response;
	}

	public void setResponse(List<Region> response) {
		this.response = response;
	}

	public List<Region> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<Region> regionList) {
		this.regionList = regionList;
	}
	
	
}
