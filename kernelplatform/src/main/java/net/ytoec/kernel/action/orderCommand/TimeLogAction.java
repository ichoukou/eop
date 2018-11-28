package net.ytoec.kernel.action.orderCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.TimerLog;
import net.ytoec.kernel.service.TimerLogService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class TimeLogAction extends AbstractActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inputstartTime;
	private String inputendTime;
	private String inputoperate;
	private String inputtimerNO;
	private String inputisError;
	private int num;
	
	private String currentPage;
	private Pagination<TimerLog> pagination;
	protected static Integer    pageNum = 10; 
	@Inject
	private TimerLogService<TimerLog> timerLogService;
	private List<TimerLog>  timeLogList=new ArrayList<TimerLog>();
	public String list(){
		if(StringUtils.isEmpty(currentPage)){
			currentPage="1";
		}
		pagination=new Pagination<TimerLog>(Integer.parseInt(currentPage), 10);
		HashMap<String, String> map=new HashMap<String,String>();
		map.put("startTime", inputstartTime);
		map.put("endTime", inputendTime);
		map.put("operate", inputoperate);
		map.put("timerNO", inputtimerNO);
		map.put("isError", inputisError);
		pagination.setParams(map);
		timeLogList=timerLogService.get(map,pagination);
		num=timerLogService.countNum(map, pagination);
		int size=timerLogService.count(map,pagination);
		if (pagination == null) {
			pagination = new Pagination<TimerLog>(Integer.parseInt(currentPage), pageNum);
		}
		pagination.setTotalRecords(size);
		return "success";
	}
	
	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination<TimerLog> getPagination() {
		return pagination;
	}
	public void setPagination(Pagination<TimerLog> pagination) {
		this.pagination = pagination;
	}
	public static Integer getPageNum() {
		return pageNum;
	}
	public static void setPageNum(Integer pageNum) {
		TimeLogAction.pageNum = pageNum;
	}
	public TimerLogService<TimerLog> getTimerLogService() {
		return timerLogService;
	}
	public void setTimerLogService(TimerLogService<TimerLog> timerLogService) {
		this.timerLogService = timerLogService;
	}
	public List<TimerLog> getTimeLogList() {
		return timeLogList;
	}
	public void setTimeLogList(List<TimerLog> timeLogList) {
		this.timeLogList = timeLogList;
	}

	public String getInputstartTime() {
		return inputstartTime;
	}

	public void setInputstartTime(String inputstartTime) {
		this.inputstartTime = inputstartTime;
	}

	public String getInputendTime() {
		return inputendTime;
	}

	public void setInputendTime(String inputendTime) {
		this.inputendTime = inputendTime;
	}

	public String getInputoperate() {
		return inputoperate;
	}

	public void setInputoperate(String inputoperate) {
		this.inputoperate = inputoperate;
	}

	public String getInputtimerNO() {
		return inputtimerNO;
	}

	public void setInputtimerNO(String inputtimerNO) {
		this.inputtimerNO = inputtimerNO;
	}

	public String getInputisError() {
		return inputisError;
	}

	public void setInputisError(String inputisError) {
		this.inputisError = inputisError;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	
}
