<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/help/meta.jsp" %>
<!-- S Content -->
	<div id="content">
			<div class="box_hd">
				<strong style="font-size:16px;color: blue;">${article.title }</strong>
			</div>
			<div class="box_bd">
				${article.content }
			</div>
			<div class="box_hd" style="text-align: center;margin:auto;">
				<p><a href="#" onclick="javascript:window.close();" class="btn btn_a"><span>关闭</span></a></p>
			</div>
	</div>
	<!-- E Main -->
