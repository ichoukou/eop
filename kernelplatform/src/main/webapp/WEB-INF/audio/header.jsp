<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>

<!-- S Header -->
<div id="header">
	<h1><a href="mainPage_home.action" title="易通">易通</a></h1>
	<div id="menu_nav">
		<ul class="clearfix">
<!-- 			<li> -->
<!-- 				<i class="header_icon" id="video_icon"></i><a href="#"  onClick="window.open('jiaocai')" title="视频教程">视频教程</a> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<a href="#" onclick="window.open('mainPage_toHelp.action')" title="帮助中心">帮助中心</a> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<a href="#" id="menu_fav" title="收藏易通">收藏易通</a> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<a id="user_exit" href="#" title="退出">退出</a> -->
<!-- 			</li> -->
		</ul>
	</div>
	<div id="acc_nav">
		<span class="acc_nav_item">您好！欢迎
		 <em>
		 <c:choose>
		 	<c:when test="${str:isNotEmpty(yto:getCookie('userNameText')) }">
		 		${yto:getCookie("userNameText")}
		 	</c:when>
		 	<c:otherwise>
		 		${yto:getCookie("userName")}
		 	</c:otherwise>
		 </c:choose>
		 </em> 
<%-- 		 <c:choose> --%>
<%-- 		 	<c:when test="${yto:getCookie('userState') == 'TBA' }"> --%>
<!-- 				<a href="javascript:;" title="未激活" class="header_icon" id="vip_icon">未激活</a> -->
<%-- 		 	</c:when> --%>
<%-- 		 	<c:otherwise> --%>
<!-- 		 		<a href="javascript:;" title="已激活" class="header_icon" id="vip_acitve_icon">已激活</a> -->
<%-- 		 	</c:otherwise> --%>
<%-- 		 </c:choose> --%>
			<!-- 激活状态用 id="vip_acitve_icon" -->
		</span>
<!-- 		<span class="acc_nav_item"><a href="javascript:;" title="消息" id="acc_nav_msg">消息</a></span> -->
<%-- 		<span class="acc_nav_item"><a href="user!toEdit.action?user.id=${user.id }" title="消息" id="acc_nav_myacc">我的账户</a></span> --%>
<!-- 		<div id="msg_box"> -->
<!-- 			<div id="msg_box_hd"> -->
<!-- 				<a href="javascript:;" class="close" title="关闭">X</a> -->
<!-- 			</div> -->
<!-- 			<div id="msg_box_bd"> -->
<!-- 				<ul> -->
<%-- 					<li id="unReadQuestionNum_li"><em id="unReadQuestionNum">${unReadQuestionNum}</em>条未处理问题件<a href="javascript:;" title="查看">查看</a></li> --%>
<%-- 					<li id="unReadMessageNum_li"><em id="unReadMessageNum">${unReadMessageNum}</em>条未读消息<a href="javascript:;" title="查看">查看</a></li> --%>
<%-- 					<input type="hidden" id="default_unReadQuestionNum" value="${unReadQuestionNum }"> --%>
<%--     				<input type="hidden" id="default_unReadMessageNum" value="${unReadMessageNum }"> --%>
<!-- 				</ul> -->
<!-- 			</div> -->
<!-- 			<div id="msg_box_ft"></div> -->
<!-- 		</div> -->
	</div>
</div>
<!-- E Header -->