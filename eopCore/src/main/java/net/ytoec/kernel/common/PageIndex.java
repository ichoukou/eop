package net.ytoec.kernel.common;

/**
 * 页面上显示出现的页码：比如当前在页面上显示出2-6页
 * @author Wangyong
 * @2011-8-11
 * net.ytoec.kernel.common
 */
public class PageIndex {

	private Integer startIndex;
	private Integer endIndex;
	
	public PageIndex(Integer startIndex, Integer endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public static PageIndex getPageIndex(Integer viewPage, Integer currentPage, Integer totalPage){
		Integer startPage = currentPage-(viewPage%2==0? viewPage/2-1 : viewPage/2);
		Integer endPage = currentPage+viewPage/2;
		if(startPage<1){
			startPage = 1;
			if(totalPage>=viewPage) endPage = viewPage;
			else endPage = totalPage;
		}
		if(endPage>totalPage){
			endPage = totalPage;
			if((endPage-viewPage)>0) startPage = endPage-viewPage+1;
			else startPage = 1;
		}
		return new PageIndex(startPage, endPage);		
	}
	
	public Integer getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	public Integer getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}
}
