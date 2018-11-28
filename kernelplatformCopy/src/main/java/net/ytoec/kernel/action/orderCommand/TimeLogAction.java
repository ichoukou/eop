package net.ytoec.kernel.action.orderCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.TimerLog;
import net.ytoec.kernel.service.TimerLogService;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.oscache.util.StringUtil;

@Controller
@Scope("prototype")
public class TimeLogAction {
	private String startTime;
	private String endTime;
	private String operate;
	private String timerNO;
	private String isError;
	private int num;
	
	private Integer currentPage = 0;
	private Pagination<TimerLog> pagination=new Pagination(currentPage, 10);
	protected static Integer    pageNum = 10; 
	@Inject
	private TimerLogService<TimerLog> timerLogService;
	private List<TimerLog>  timeLogList=new ArrayList<TimerLog>();
	public String list(){
		HashMap<String, String> map=new HashMap<String,String>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("operate", operate);
		map.put("timerNO", timerNO);
		map.put("isError", isError);
		pagination.setParams(map);
		timeLogList=timerLogService.get(map,pagination);
		num=timerLogService.countNum(map, pagination);
		
		if (pagination == null) {
			pagination = new Pagination(currentPage, pageNum);
		}
		pagination.setTotalRecords(timeLogList.size());
		return "success";
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public String getTimerNO() {
		return timerNO;
	}
	public void setTimerNO(String timerNO) {
		this.timerNO = timerNO;
	}
	public String getIsError() {
		return isError;
	}
	public void setIsError(String isError) {
		this.isError = isError;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
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
	
	
}
