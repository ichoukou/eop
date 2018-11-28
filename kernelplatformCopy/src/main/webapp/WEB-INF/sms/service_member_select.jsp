<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<option value="" <c:if test="${searchName==''}">selected</c:if>>-请选择-</option>
<c:forEach items="${smsBuyerSearchList}" var="vo">
	<option value="${vo.id}" <c:if test="${searchName==vo.id}">selected</c:if> >${vo.searchName}</option>
</c:forEach>
