<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.ytoec.uninet.util.HessianUtil"%>
<%@ include file="/WEB-INF/common/taglibs.jsp" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="cssPath" value="${ctxPath}/css" scope="request"/>
<c:set var="imagesPath" value="${ctxPath}/images" scope="request"/>
<c:set var="jsPath" value="${ctxPath}/js" scope="request"/>
<c:set var="domain" value="http://ec.yto.net.cn"></c:set>

<c:set var="mediaPath" value="<%=HessianUtil.getMediaPath() %>" scope="request"/>
<c:set var="tempMediaPath" value="${mediaPath }/temp" scope="request"/>
<c:set var="fileSizeLimit" value="<%=HessianUtil.getUploadFileSizeLimit() %>" scope="request"/>
