<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
	<meta charset="UTF-8" />
	<link href="artDialog/skins/blue.css?d=${str:getVersion() }" rel="stylesheet"/>
	<script type="text/javascript" src="js/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
	<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
	<title>请创建业务子账号</title>
	<script type="text/javascript">
		$(document).ready(function(){
			artDialog({
		        id: 'Alert',
		        icon: 'warning',
		        fixed: true,
		        top:'10%',
		        content: "您尚未创建业务子账号，暂不能使用该功能，系统将导向到创建业务子账号页面！",
		        ok: function(){
			        this.close();
			    },
			    close:function(){
			    	location.href = "user!toAddPlatformSubAccount.action";
			    }
		    });
		});
	</script>
</head>
<body>
	<div>
		<div>
<!-- 			<h2>您的账号没有权限访问该页面</h2> -->
<%-- 			<span class="btn btn_a"><a href="javascript:history.go(-1);" title="返回上一页">返回上一页</a></span> --%>
		</div>
	</div>
</body>
</html>