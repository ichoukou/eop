<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/myyto.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>

	<!-- S Content -->
	<div id="content">
		<style type="text/css">
			#back_btn{text-align:center;margin-top:10px;}
		</style>
		<div id="content_hd" class="clearfix">
			<h2 id="message_icon">易通最新动态</h2>
			<em>最新的易通新闻实时跟踪！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a>
			</em>
		</div>
		<div id="content_bd" class="clearfix">
			<div style="text-align: center;" class="newstop">
			<h2 style="font-size: 15px;">${article.title }</h2>
			
			<p style="color: #70797E;">发布时间：<fmt:formatDate value="${article.createTime }" pattern="yyyy-MM-dd HH:mm" /></p>
		</div>
		<div class="newscontent">
			<p id="r_content">
				${article.content }
			</p>
		</div>
		
		<div id="back_btn">
			<a href="javascript:history.go(-1);" class="btn btn_a" title="返回"><span>返 回</span></a>
		</div>
		</div>
		<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
	</div>	
	<!-- E 当前页面 JS -->
	
