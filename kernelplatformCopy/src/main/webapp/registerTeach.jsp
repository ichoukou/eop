<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无线天利客户端注册页面</title>
<script type="text/javascript" src="<%=basePath%>js/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" language="javascript">
function register() {
	$.ajax({
			type:'post',
			url: 'registexTeach.action',					
			dataType:'Json',
			success:function(msg){
				if(msg=='success')												
					alert("注册成功");
				else
					alert("注册失败");		
			},
			error:function(){
			   alert("注册失败");							   
			}
		});
};
</script>
</head>
<body>
     <div>
	      <h2>无线天利客户端</h2>
	 </div>
	 <div>				
		<font size="100px">点我：<a href="#" onclick="register();">注册</a></font>
	</div>
</body>
</html>