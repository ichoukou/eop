<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/shixiao_n.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	<title>网点问题件管理</title>
</head>
		<!-- S Content -->
		<div id="content">
			<div id="content_ad">
				
				<div class="step_content">
				<img src="images/single/sx_ad2.jpg">
				<input type="hidden" value="<s:property value="serviceId"/>" id ="serviceId">
				<input type="hidden" value="<s:property value="payment.id"/>" id="paymentId">
				<input type="hidden" value="<s:property value="smsFlag"/>" id="smsFlag">
				<input type="hidden" value="chajian_passManage_warn" id="source">
				<c:if test="${yto:getCookie('userType') == 1 
				                || yto:getCookie('userType') == 11
				                || yto:getCookie('userType') == 12
				                || yto:getCookie('userType') == 13
						  		|| yto:getCookie('userType') == 4
						  		|| yto:getCookie('userType') == 5}">
			   	<input type="button" value="开始使用" id="startApplay"/>
			   	</c:if>
				</div>
				
			</div>
		</div>
		

		<!-- E Content -->
		
	<script>
	var params = {
			onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
				? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},// “开始使用” == 1，“绑定网点” == 2
			userId:${yto:getCookie('id')},						//当前登录用户的id
			userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
			infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
			bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode='			// 绑定客户编码表单 action
		};
	</script>
	
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/serve_home.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
