<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>	
<%@ include file="/WEB-INF/audio/meta.jsp" %>
	<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
			<h2 id="message_icon" style="padding:0;">帮助视频</h2>
					<c:if test="${yto:getCookie('userType')==1 
					|| yto:getCookie('userType')==11
					|| yto:getCookie('userType')==12
					|| yto:getCookie('userType')==13
					|| yto:getCookie('userType')==2
					|| yto:getCookie('userType')==21 
					|| yto:getCookie('userType')==22
					|| yto:getCookie('userType')==23}">
				<h1><a href="webFile!helpDownLoad.action?menuFlag=" style="position:relative;top:10px;">点击下载全部教材PPT</a></h1>
			</c:if>
				
			</div>
			<div id="content_bd" class="clearfix">
			<c:if test="${yto:getCookie('userType')==1 
					|| yto:getCookie('userType')==11
					|| yto:getCookie('userType')==12
					|| yto:getCookie('userType')==13
					|| yto:getCookie('userType')==3
					|| yto:getCookie('userType')==4 
					|| yto:getCookie('userType')==41}">
		<a href="<%=basePath %>resources/vip/${str:isNotEmpty(jsonResult) ? jsonResult : 'vip_1_yition_intro'}.flv"
			 style="display:block;width:850px;height:500px"  id="player"> 
		</a>
		</c:if>
		<c:if test="${yto:getCookie('userType')==2 
					|| yto:getCookie('userType')==21
					|| yto:getCookie('userType')==22
					|| yto:getCookie('userType')==23
					|| yto:getCookie('userType')==3}">
			<a href="<%=basePath %>resources/site/${str:isNotEmpty(jsonResult) ? jsonResult : 'site_1_yition_intro'}.flv"
			 style="display:block;height:500px"  id="player"> 
			</a>
		</c:if>
		<script type="text/javascript" src="${jsPath}/module/flowplayer/flowplayer-3.2.8.min.js?d=${str:getVersion() }"></script>
		<script type="text/javascript">
			flowplayer("player", "${jsPath}/module/flowplayer/flowplayer-3.2.8.swf", {clip: {autoPlay: false}});
		</script>
	
			<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
				
			</div>
		</div>
		<!-- E Content -->