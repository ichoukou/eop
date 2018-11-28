<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付宝</title>
</head>
<%
/* *
 功能：支付宝服务器异步通知页面
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
    Boolean bool= (Boolean)request.getAttribute("result");
	if(bool){//验证成功
		//////////////////////////////////////////////////////////////////////////////////////////				
		out.println("success");	//请不要修改或删除——
		//////////////////////////////////////////////////////////////////////////////////////////
	}else{//验证失败
		out.println("fail");//请不要修改或删除——
	}
%>
<body>
</body>
</html>
