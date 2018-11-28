package net.ytoec.kernel.dataobject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.common.Pagination;

/**
 * 配置项表
 * 
 * @author ChenRen
 * @date 2011-08-10
 */
public class JsonResponse implements Serializable {

	private static final long serialVersionUID = -277804841057297555L;

	/**
	 * 消息提示类型：1－提示，2－确认，3－警告，4－错误，5－成功。
	 */
	public static final Integer INFO_TYPE_MSG = 1;
	public static final Integer INFO_TYPE_CONFIRM = 2;
	public static final Integer INFO_TYPE_WARNING = 3;
	public static final Integer INFO_TYPE_ERROR = 4;
	public static final Integer INFO_TYPE_SUCCESS = 5;
	
	private Integer infoType;
	private boolean status;
	private String infoContent;
	private String targetUrl;
	
	/**
	 * 订单 导入专用，后续再优化
	 */
	private String successCount="0";
	private String failedCounct="0";
	@SuppressWarnings("rawtypes")
	private List dataList;
	private Map<String, Object> params;
	@SuppressWarnings("rawtypes")
	private Pagination page;
	
	@SuppressWarnings("rawtypes")
	public List getDataList() {
		return dataList;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	@SuppressWarnings("rawtypes")
	public Pagination getPage() {
		return page;
	}
	@SuppressWarnings("rawtypes")
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	@SuppressWarnings("rawtypes")
	public void setPage(Pagination page) {
		this.page = page;
	}
	public Integer getInfoType() {
		return infoType;
	}
	public boolean isStatus() {
		return status;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
    
    public String getSuccessCount() {
        return successCount;
    }
    
    public void setSuccessCount(String successCount) {
        this.successCount = successCount;
    }
    
    public String getFailedCounct() {
        return failedCounct;
    }
    
    public void setFailedCounct(String failedCounct) {
        this.failedCounct = failedCounct;
    }
	
	
}
