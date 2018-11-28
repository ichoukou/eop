<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>	
<%@ include file="/WEB-INF/help/meta.jsp" %>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<!-- S Content -->
	<div id="content">
			<div class="box_hd">
				<strong style="font-size:16px;color: blue;">${article.title }</strong>
			</div>
			<div class="box_bd">
				${article.content }
				<div style="position:fixed;left:50%;bottom:80px;margin-left:430px;width:42px;height:22px;"><a href="#" style="display: block;width:42px;height:22px;" onclick="window.scrollTo(0,0);" title="回到顶部"></a></div>
			</div>
			<div class="box_hd" style="text-align: center;margin:auto;">
				<p><a href="#" onclick="javascript:window.close();" class="btn btn_a"><span>关闭</span></a></p>
			</div>
	</div>
		<!-- E Content -->