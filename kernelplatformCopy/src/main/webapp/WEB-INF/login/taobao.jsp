<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<link href="artDialog/skins/blue.css?d=${str:getVersion() }" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
<html>
  <head>
    <!-- base href="<%=basePath%>"-->
    
    <title>淘宝用户登录</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  <script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
	function apply(){
		$.ajax({
			url : 'login_applyForCode.action',
			data : {
				inactiveUser : inactiveUser
			}
			dataType:'json',
			cache: false,
			success : function(data){
				art.dialog.alert(data);
			}
		});
	}
	</script>  
  <body>
  	淘宝用户：<s:property value="resultResponse"/>
  	<hr>
  	<input type="button" onclick="apply()" value="申请编码"/>
  </body>
</html>
