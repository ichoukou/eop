<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>申请客户编码视频教程</title>
<link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
<script type="text/javascript" src="./images/flowplayer/flowplayer-3.2.8.min.js?d=${str:getVersion() }"></script>
</head>
<body>
<div id="main">
	<div id="main-content" style="text-align: center;">
		<!-- 卖家帮助视频菜单 -->
		<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType')==4}">
		<a href='<%=basePath %>resources/site/site_${responseString}.flv'
			 style="display:block;width:850px;height:500px"  
			 id="player"> 
		</a> 
		</c:if>
		<!-- 网点帮助视频菜单 -->
		<c:if test="${yto:getCookie('userType')==2}">
		
		<a href='<%=basePath %>resources/site/site_${responseString}.flv'
			 style="display:block;width:850px;height:500px"  
			 id="player"> 
		</a> 
		</c:if>
		<script type="text/javascript">
			flowplayer("player", "./images/flowplayer/flowplayer-3.2.8.swf", {clip: {autoPlay: false}});
		</script>
	</div>
</div><jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
</body>
</html>