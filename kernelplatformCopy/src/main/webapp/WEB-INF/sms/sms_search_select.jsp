<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<!-- 
<c:if test="${fn:length(templates)==0 }">
	<option value="" <c:if test="${smsTemplateId==''}">selected</c:if>>所有模版</option>
</c:if> 
 -->
<option value="" <c:if test="${smsTemplateId==''}">selected</c:if>>所有标题</option>
<c:forEach items="${templates}" var="vo">
	<option value="${vo.id}" <c:if test="${smsTemplateId==vo.id}">selected</c:if> >	
		<c:choose>
			 <c:when test="${fn:length(vo.name)>6}">   
				${fn:substring(vo.name, 0, 6)} ...
			</c:when>
			 <c:otherwise> 
			 	${vo.name}
			 </c:otherwise>
		 </c:choose>
	</option>
</c:forEach>