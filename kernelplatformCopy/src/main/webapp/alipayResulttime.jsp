<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付宝返回</title>
</head>
<body>
<%
Boolean bool= (Boolean)request.getAttribute("result");
	if(bool){//验证成功
		out.println("成功");
	}else{
		out.println("失败");
	}
%>
</body>
</html>
