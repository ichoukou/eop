/**
 * Pagination.java
 * Wangyong
 * 2011-8-10 下午02:29:40
 */
package net.ytoec.kernel.common;

import java.util.List;
import java.util.Map;

/**
 * 分页对象:初始化分页对象时，只需要用带参构造方法初始化对象，参数为当前页currentPage和每页显示记录数pageNum。
 * @author Wangyong
 * @2011-8-10
 * net.ytoec.kernel.common
 */
public class Pagination<T> {
	
	/** 分页数据  */
	private List<T> records;
	
	/** 页码开始索引和结束索引 **/
	private PageIndex pageIndex;
	
	/** 查询开始索引位置 **/
	private Integer startIndex;
	
	/** 每页显示记录数 */
	private Integer pageNum = 10; //默认每页显示10条
	
	/** 记录总数 */
	private Integer totalRecords;
	
	/** 总页数 */
	private Integer totalPages;
	
	/** 当前页 */
	private Integer currentPage = 1;
	
	/** 在页面上显示出现的页面 */
	private static Integer viewPage = 4;
	
	/** 页面上传入的参数 */
	private Map<String, String> params;
	
	public Pagination(Integer currentPage, Integer pageNum){
		this.currentPage = currentPage;
		this.pageNum = pageNum;
	}
	
	public Integer getStartIndex() {
		return (this.currentPage - 1)*this.pageNum;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
		setTotalPages(this.totalRecords%this.pageNum==0 ? this.totalRecords/this.pageNum : this.totalRecords/this.pageNum+1);
		this.pageIndex = PageIndex.getPageIndex(viewPage, currentPage, totalPages);
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public PageIndex getPageIndex() {
		return pageIndex;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public static Integer getViewPage() {
		return viewPage;
	}

	public static void setViewPage(Integer viewPage) {
		Pagination.viewPage = viewPage;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setPageIndex(PageIndex pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	

}
