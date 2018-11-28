<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/retrieve_psw.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	<title>找回密码</title>
</head>
<body id="retrieve_psw">
	<div id="psw_hd">
		<h1><a href="/" title="易通电子商务物流信息平台">易通</a></h1>
		<div id="contact">
			<span style="font-size:16px;">系统支持：</span>
			
			<span id="qq"><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2366710544&amp;site=qq&amp;menu=yes"><img src="http://wpa.qq.com/pa?p=2:2366710544:45&amp;r=0.658135694570411" alt="点击这里给我发消息" title="点击这里给我发消息" /></a><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2294882345&amp;site=qq&amp;menu=yes"><img src="http://wpa.qq.com/pa?p=2:2294882345:45&amp;r=0.658135694570411" alt="点击这里给我发消息" title="点击这里给我发消息" /></a></span>
			<span id="wangwang"><a href="http://web.im.alisoft.com/msg.aw?v=2&amp;uid=ytoyitong%20&amp;site=cnalichn&amp;s=1" target="_blank" title="点击这里给我发消息"><img src="${imagesPath}/single/wang_online.gif" alt="点击这里给我发消息" /></a></span>
			<span id="tel">021-69777830</span>
		</div>
	</div>
	
	<div id="psw_bd" class="clearfix">
		<div id="psw_process">
			<ol>
				<li>填写登录账号</li>
				<li>点击找回密码</li>
				<li>密码发送到您的注册邮箱</li>
			</ol>
		</div>
		
		<form action="#" class="form" id="psw_form">
			<p>
				<label for="login_id">登录账号：</label>
				<input type="text" class="input_text" id="login_id" name="loginName" />
				<span id="login_idTip"></span>
			</p>
			<p>
				<a href="javascript:;" class="btn btn_a" title="找回密码" id="submit_btn"><span>找回密码</span></a>
<!-- 				<span class="btn btn_a" id="submit_btn"><input type="submit" value="找回密码"  /></span> -->
				
			</p>
		</form>
	</div>
	
	<div id="psw_ft">
		<p>圆通速递公司总部：上海青浦区华新镇华徐公路3029弄28号 邮政编码：201705 Copyright &copy; 2000-2012 All Right Reserved 沪ICP备05004632号</p>
	</div>
	<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
	
	<script>
		var path = {
			ctx: '${ctxPath}'
		}
	</script>
	<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
<%-- 	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script> --%>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>
	<script src="${jsPath}/module/dialog.js?d=${str:getVersion() }" type="text/javascript"></script>
<!--[if IE 6]>
	<script type="text/javascript" src="${jsPath}/util/position_fixed.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.png');
	</script>
<![endif]-->
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/retrieve_psw.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
</body>
</html>