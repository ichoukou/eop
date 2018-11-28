<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

接口测试/VipOrderServlet.action(TAOBAO)
<form method='post' action='test.action'>
明文<textarea rows='5' cols='50'  name='logistics_interface' id='bbb'></textarea>
加密文<textarea rows='5' cols='50'  name='data_digest' id='bbb'></textarea>
客户代码<input type='text' name='clientID' id='bbb' value="TAOBAO">
<input type="hidden" value="TaoBao14OrderServlet" name="toKG">
<input type='submit' value='发送'/> 
</form>

接口测试/VipOrderServlet.action(非TAOBAO)
<form method='post' action='test.action'>
明文<textarea rows='5' cols='50'  name='logistics_interface' id='bbb'></textarea>
加密文<textarea rows='5' cols='50'  name='data_digest' id='bbb'></textarea>
客户代码<input type='text' name='clientID' id='bbb' value="360buy">
<input type="hidden" value="VipOrderServlet" name="toKG">
<input type='submit' value='发送'/> 
</form>
======================TAOBAO接口================================
<form method='post' action='test.action'>
明文<textarea rows='5' cols='50'  name='logistics_interface' id='bbb'></textarea>
加密文<textarea rows='5' cols='50'  name='data_digest' id='bbb'></textarea>
客户代码<input type='text' name='clientID' id='bbb' value="TAOBAO">
<input type="hidden" value="1" name="toTB">
<input type='submit' value='发送'/> 
</form>
<form method='post' action='test.action'>
<input type="hidden" value="open" name="switch">
<input type='submit' value='打开'/> 
</form>
<form method='post' action='test.action'>
<input type="hidden" value="close" name="switch">
<input type='submit' value='关闭'/> 
</form>

=====================问题件更新开始=======================

<form method='post' action='test2.action'>
<table>
<tr>
	<td>issueId: </td><td><input type="text" name="ques.issueId"></td>
</tr>
<tr>
	<td>dealContent: </td><td><input type="text" name="ques.dealContent"></td>
</tr>
<tr>
	<td>status: </td><td><input type="text" name="ques.status"></td>
</tr>
<tr>
	<td>createOrgCode: </td><td><input type="text" name="ques.createOrgCode"></td>
</tr>
<tr>
	<td>createUserName: </td><td><input type="text" name="ques.createUserName"></td>
</tr>
<tr>
	<td>createUserCode: </td><td><input type="text" name="ques.createUserCode"></td>
</tr>
<tr>
	<td>issueCreateTime: </td><td><input type="text" name="ques.issueCreateTime">(可以为空)</td>
</tr>
</table>

<input type='submit' value='测试问题件更新'/>
<input type="hidden" value="questionnaire" name="type">
</form>

=====================面单查询开始=======================
<form method='post' action='test2.action'>
输入要查询的免单号(用封号隔开)：<br>
<textarea rows='5' cols='50'  name='mailNoStrings' ></textarea>
<input type='submit' value='测试面单查询'/> 
<input type="hidden" value="mailNo" name="type">
</form>
========================面单查询结束============================


<form method='post' action='test.action'>
输入需要加密的内容：<textarea rows='5' cols='50'  name='textContent' id='abc'></textarea>
加密ID<input type='text' name='jiami' id='bbb' value="123456">
<input type='submit' value='加密'/> </form>
<form method='post' action='test.action'>
输入需要解密的内容：<textarea rows='5' cols='50'  name='jiemi' id='cba'></textarea>
<input type='submit' value='解码'/> 
</form>

<form method='post' action='test.action'>
<input type="hidden" value="download" name="download">
<input type='submit' value='下载'/> </form>

</body>
</html>