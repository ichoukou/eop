<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<html>
<head>
<title>YTO电子商务交易平台管理</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="css/layout.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
<script type="text/javascript">
	$(function(){
		$("#leftmenu ul li").click(function(){
			$(".menuActive").removeClass("menuActive");
			$(this).addClass("menuActive");
			parent.main.location = $(this).children().attr("_href");
		});
		$("#leftmenu ul li").mouseover(function(){
			this.style.cursor='pointer';
		});
	});
</script>
</head>
<body style="background-color:transparent">
<div id="side">
<div id="leftmenu">
<div style="margin-left:4px"><img src="images/menubg.png"></div>
	<ul>
		<dt><span class="modify">帮助视频</span></dt>
		<!-- 卖家帮助视频菜单 -->
		<c:if test="${yto:getCookie('userType')==1 
		|| yto:getCookie('userType')==11
		|| yto:getCookie('userType')==12
		|| yto:getCookie('userType')==13
		|| yto:getCookie('userType')==3
		|| yto:getCookie('userType')==4 
		|| yto:getCookie('userType')==41}">
			<li class="menuActive"><a _href="mainPage_audioVip.action?jsonResult=vip_1_yition_intro" target="main" class="invoices">易通简介</a></li>
			<li><a _href="mainPage_audioVip.action?jsonResult=vip_2_login_activate" target="main" class="invoices">登录激活</a></li>
			<li><a _href="mainPage_audioVip.action?jsonResult=vip_3_intelligent_query" target="main" class="invoices">智能查件</a></li>
			<li><a _href="mainPage_audioVip.action?jsonResult=vip_4_monitor_waybill" target="main" class="invoices">运单监控</a></li>
			<li><a _href="mainPage_audioVip.action?jsonResult=vip_5_questionnaire_manage" target="main" class="invoices">问题件管理</a></li>
			<li><a _href="mainPage_audioVip.action?jsonResult=vip_6_ecaccount" target="main" class="invoices">电子对账</a></li>
			<li><a _href="mainPage_audioVip.action?jsonResult=vip_7_shops_manage" target="main" class="invoices">多店铺管理</a></li>
			<li><a _href="mainPage_audioVip.action?jsonResult=vip_8_childAccount_manage" target="main" class="invoices">子账号管理</a></li>
		</c:if>
		<!-- 网点帮助视频菜单 -->
		<c:if test="${yto:getCookie('userType')==2 
		|| yto:getCookie('userType')==21
		|| yto:getCookie('userType')==22
		|| yto:getCookie('userType')==23
		|| yto:getCookie('userType')==3}">
			<li class="menuActive"><a _href="mainPage_audioSite.action?jsonResult=site_1_yition_intro" target="main" class="invoices">易通简介</a></li>
			<li><a _href="mainPage_audioSite.action?jsonResult=site_2_login_activate" target="main" class="invoices">登录激活</a></li>
			<li><a _href="mainPage_audioSite.action?jsonResult=site_3_customer_code" target="main" class="invoices">客户编码</a></li>
			<li><a _href="mainPage_audioSite.action?jsonResult=site_4_intelligent_query" target="main" class="invoices">智能查件</a></li>
			<li><a _href="mainPage_audioSite.action?jsonResult=site_5_questionnaire_manage" target="main" class="invoices">问题件管理</a></li>
			<li><a _href="mainPage_audioSite.action?jsonResult=site_6_ecaccount" target="main" class="invoices">电子对账</a></li>
			<li><a _href="mainPage_audioSite.action?jsonResult=site_7_carriage_change" target="main" class="invoices">运费调整</a></li>
			<li><a _href="mainPage_audioSite.action?jsonResult=site_8_childAccount_manage" target="main" class="invoices">子账号管理</a></li>
		</c:if>
	</ul>
</div>
</div>
</body>
</html>