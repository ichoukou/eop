<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="pagination.totalPages > 1">
	<a value="1" href="javascript:;" class="page_txt">&laquo; 首页</a>
</s:if>
<%-- <s:if test="%{currentPage > 1}"><a href="javascript:toPage(<s:property value='currentPage'/>-1)" class="page_txt">上一页</a></s:if> --%>
<s:if test="%{pagination.pageIndex.startIndex > 1}">...</s:if>
<s:iterator begin="pagination.pageIndex.startIndex" end="pagination.pageIndex.endIndex" var="vp">
	<s:if test="currentPage == #vp">
		<span class="page_cur" title="<s:property value="#vp"/>"><s:property value="#vp"/></span>
	</s:if>
	<s:else>
		<a value=<s:property value="#vp"/> href="javascript:;" class="page_num" title="<s:property value="#vp"/>"><s:property value="#vp"/></a>
	</s:else>
</s:iterator>
<s:if test="%{pagination.pageIndex.endIndex < pagination.totalPages}">...</s:if>
<s:if test="%{currentPage < pagination.totalPages}"><a value=${currentPage+1 } href="javascript:;" class="page_txt">下一页</a></s:if>
<s:if test="pagination.totalPages > 1">
	<a value=<s:property value='pagination.totalPages'/> href="javascript:;" class="page_txt">末页 &raquo;</a>
</s:if>
<span class="page_total">共 <em><s:property value="pagination.totalPages"/></em> 页/<em><s:property value="pagination.totalRecords"/></em> 条</span>
