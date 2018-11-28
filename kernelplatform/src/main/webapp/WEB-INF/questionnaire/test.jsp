<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<a href="warnManager_addWarnValue.action">添加预警值</a> </br>
<a href="warnManager_getWarnValueList.action">查询预警值</a> </br>
<a href="warnManager_removeWarnValue.action">删除预警值</a> </br>
<a href="warnManager_editWarnValue.action">修改预警值</a> </br><hr>
<a href="passManage_addReportIssue.action">上报问题件</a> </br>
<a href="passManage_getBranchInfo.action">获取网点信息</a>  </br>
<a href="passManage_getReportedIssue.action">获取问题件处理信息</a> </br>
<a href="passManage_getSite.action">缓存中获取网点信息</a><s:property value="siteNameText"/> </br>


</body>
</html>