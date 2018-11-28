<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
for(int i=0;i<90;i++){
    Cookie c = new Cookie("cookie_key_"+i,"cookie_value_"+i);
    response.addCookie(c);
}



%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>cookie测试</title>
</head>
<body>
<%
	Cookie[] cs = request.getCookies();
	for(int i=0;i<cs.length;i++){
	    Cookie c = cs[i];
%>
		Cookie项名称：<%=c.getName() %>，对应值：<%=c.getValue() %><br><br>
<%    
	}
%>
测试结果显示，IE6、7、8、9均可以设置90个cookie项（类似键值对）
</body>
</html>