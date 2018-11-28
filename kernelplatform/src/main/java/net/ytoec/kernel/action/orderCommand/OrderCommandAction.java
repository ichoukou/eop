package net.ytoec.kernel.action.orderCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dto.JGOrderDTO;
import net.ytoec.kernel.search.service.JgOrderCommandService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class OrderCommandAction extends AbstractActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inputStartTime;
	private String inputEndTime;
	private String inputcommandType;
	private String inputIsOffline;//线上线下
	private String inputOrderType;//类型
	private String currentPage;
	private Pagination<JGOrderDTO> pagination;
	protected static Integer    pageNum = 10;
	@Inject
	private JgOrderCommandService<JGOrderDTO> jgOrderCommandService;
	private List<JGOrderDTO> jgOrderDTOList=new ArrayList<JGOrderDTO>();
	public String list(){
		if(StringUtils.isEmpty(currentPage)){
			currentPage="1";
		}
		pagination=new Pagination<JGOrderDTO>(Integer.parseInt(currentPage), 10);
		HashMap<String, String> map=new HashMap<String,String>();
		map.put("startTime", inputStartTime);
		map.put("endTime", inputEndTime);
		map.put("commandType", inputcommandType);
		map.put("isOffline", inputIsOffline);
		map.put("orderType", inputOrderType);
		pagination.setParams(map);
		jgOrderDTOList=jgOrderCommandService.getJGOrder(map, pagination);
		int size=jgOrderCommandService.count(map, pagination);
		pagination.setTotalRecords(size);
		return "success";
	}

	public String getInputStartTime() {
		return inputStartTime;
	}

	public void setInputStartTime(String inputStartTime) {
		this.inputStartTime = inputStartTime;
	}

	public String getInputEndTime() {
		return inputEndTime;
	}

	public void setInputEndTime(String inputEndTime) {
		this.inputEndTime = inputEndTime;
	}

	public String getInputIsOffline() {
		return inputIsOffline;
	}

	public void setInputIsOffline(String inputIsOffline) {
		this.inputIsOffline = inputIsOffline;
	}


	public String getInputOrderType() {
		return inputOrderType;
	}

	public void setInputOrderType(String inputOrderType) {
		this.inputOrderType = inputOrderType;
	}

	public List<JGOrderDTO> getJgOrderDTOList() {
		return jgOrderDTOList;
	}

	public void setJgOrderDTOList(List<JGOrderDTO> jgOrderDTOList) {
		this.jgOrderDTOList = jgOrderDTOList;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination<JGOrderDTO> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<JGOrderDTO> pagination) {
		this.pagination = pagination;
	}

	public static Integer getPageNum() {
		return pageNum;
	}

	public static void setPageNum(Integer pageNum) {
		OrderCommandAction.pageNum = pageNum;
	}

	public JgOrderCommandService<JGOrderDTO> getJgOrderCommandService() {
		return jgOrderCommandService;
	}

	public void setJgOrderCommandService(
			JgOrderCommandService<JGOrderDTO> jgOrderCommandService) {
		this.jgOrderCommandService = jgOrderCommandService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getInputcommandType() {
		return inputcommandType;
	}

	public void setInputcommandType(String inputcommandType) {
		this.inputcommandType = inputcommandType;
	}
	
}
