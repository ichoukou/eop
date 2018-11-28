<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html>
<body>
	<a href="account!show.action">显示所有</a>
	<br>
	<a href="add.jsp">添加数据</a>
	<br />
	<a href="account!treeData.action">JSON</a>
	<br />

	<hr>
	<b>用户管理</b>
	<div style="font-size: 12px; margin-left: 10px;">
		<a href="user!toAdd.action" target="_blank">用户新增</a> 
		<br /> 
		<a href="user!list.action" target="_blank">用户列表</a>
	</div>

	<hr>
	<b>运单管理</b>
	<div style="font-size: 12px; margin-left: 10px;">
		<a href="/order!toECAccount.action?userType=1" target="_blank">VIP账单(网点用户)</a> 
		<br /> 
		<a href="/order!toECAccount.action?userType=2" target="_blank">卖家电子对账(VIP用户)</a> 
		<br /> 
		<br /> 
	</div>
<a href="region_show.action">省市级联</a>
<br />
</body>

</html>
