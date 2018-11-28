<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<html>
<head>
<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
</head>
<body>
	<input type="hidden" id="loginMessage" value="<s:property value='loginMessage'/>"/>
	<input type="hidden" id="resultResponse" value="${resultResponse}"/>
</body>
</html>
<script type="text/javascript">

$(function(){
	var loginMsg = $("#loginMessage").val();
	if(loginMsg != ""){
		alert(loginMsg);
	}
	if($("#resultResponse").val()){
		window.location.href=$("#resultResponse").val()
	}
	else{
		window.location.href = "${ctxPath}/login_goLogin.action";
	}
});
</script>