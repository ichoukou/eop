<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>卖家用户教程</title>
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<script type="text/javascript" src="./images/flowplayer/flowplayer-3.2.8.min.js?d=${str:getVersion() }"></script>
</head>
<body>
<div id="main">
	<div id="main-content">
<%-- 		<embed src='./images/flash/flvplayer.swf?file=<%=basePath %>resources/vip/<s:property value="#request.jsonResult" />.flv' --%>
<!-- 		 scale='ShowAll' play='true' style="width:850px;height:500px;margin:auto;" -->
<!-- 		loop='true' menu='true'  quality='1' type='application/x-shockwave-flash'></embed> -->
		<a href='<%=basePath %>resources/vip/<s:property value="#request.jsonResult" />.flv'
			 style="display:block;width:850px;height:500px"  
			 id="player"> 
		</a>
		<script type="text/javascript">
			flowplayer("player", "./images/flowplayer/flowplayer-3.2.8.swf", {clip: {autoPlay: false}});
		</script>
	</div>
</div><jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
</body>
</html>