<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" type="text/css" href="css/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/common/common.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/module/button.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/error/error.css?d=${str:getVersion() }" media="all" />
	<script type="text/javascript" src="js/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
<%-- 	<script type="text/javascript" src="js/error/error.js?d=${str:getVersion() }"></script> --%>
	<title>没有权限</title>
</head>
<body id="no_permission">
	<div id="content">
		<div class="err_info">
			<h2>您的账号没有权限访问该页面</h2>
			<a href="javascript:history.go(-1);" title="返回上一页" class="btn btn_a"><span>返回上一页</span></a>
		</div>
	</div>
</body>
</html>