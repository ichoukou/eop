<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>

<body>

	<h5>↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓明文 && 密文 的生成↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓</h5>
	<form method='post' action='test.action'>
		输入需要加密的内容：
		<textarea rows='5' cols='50' name='textContent' id='abc'></textarea>
		加密ID</red><input type='text' name='jiami' id='bbb' value="jNpKcyXrHfNJ"> <input
			type='submit' value='加密' />
	</form>

	<hr>
	<h5>↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓淘宝接口↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓</h5>
	<br>订单创建(线上/下)(TAOBAO)
	<form method='post' action='TaoBaoOrderServlet.action'>
		<red>明文</red>
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		<red>加密文</red>
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='ecCompanyId' id='bbb' value="TAOBAO">
		订单操作<input type='text' name='msg_type' id='bbb' value="ORDERCREATE"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
	
	<br>订单更新(线上/下)(TAOBAO)
	<form method='post' action='TaoBaoOrderServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='ecCompanyId' id='bbb' value="TAOBAO">
		订单操作<input type='text' name='msg_type' id='bbb' value="UPDATE"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
	<br>服务申请/取消(TAOBAO)
	<form method='post' action='TaoBaoOrderServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='ecCompanyId' id='bbb' value="TAOBAO">
		订单操作<input type='text' name='msg_type' id='bbb' value="UPDATE"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
	<br>支付成功/失败(TAOBAO)
	<form method='post' action='TaoBaoOrderServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='ecCompanyId' id='bbb' value="TAOBAO">
		订单操作<input type='text' name='msg_type' id='bbb' value="UPDATE"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
	<h5> ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑淘宝接口↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑</h5>
	
    <h5> ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓金刚接口↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓</h5>
	订单状态通知(TAOBAO)
	<form method='post' action='taoBaoStatusNotify.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb4'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb3'></textarea>
		TP编号<input type="text" value="YTO" name="logistic_provider_id">
		订单操作<input type='text' name='msg_type' id='bbb1' value="UPDATE">  <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
	服务取消
	<form method='post' action='taoBaoStatusNotify.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb4'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb3'></textarea>
		TP编号<input type="text" value="YTO" name="logistic_provider_id">
		订单操作<input type='text' name='msg_type' id='bbb1' value="UPDATE">  <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
  <h5> ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑金刚接口↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑</h5>

	订单状态通知(TAOBAO)
	<font color="red">新版本状态</font>
	<form method='post' action='taobaoLogisticStatusServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='eCompanyId' id='bbb' value="TAOBAO">
		消息类型<input type='text' name='msg_type' id='bcd' value="TRACEPUSH">
		<input type="hidden" value="YTO" name="logistic_provider_id">
		<input type="hidden" value="1" name="test"> <input
			type='submit' value='发送' />
	</form>

	----------------------------------服務保障-----------------------------------------
	<br>订单创建/更新/取消/查询(线上/下)(TAOBAO)
	<form method='post' action='TaobaoServiceApplyServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='ecCompanyId' id='bbb' value="TAOBAO">
		线上/下<input type='text' name='type' id='bbb' value="online"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>

	保障審核消息(TAOBAO)
	<form method='post' action='ServiceApplyNotifyServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='ecCompanyId' id='bbb' value="TAOBAO">
		线上/下<input type='text' name='type' id='bcd' value="online"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
	=========================================Common=============================================
	<br>订单创建/更新/取消/查询(线上/下)(Common 支持批量)
	<form method='post' action='CommonOrderServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='clientId' id='bbb' value="EC">
		线上/下<input type='text' name='type' id='bbb' value="online"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>

	订单状态通知(Common)
	<form method='post' action='commonStatusNotify.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='clientId' id='bbb' value="EC">
		线上/下<input type='text' name='type' id='bcd' value="online"> <input
			type="hidden" value="1" name="test"> <input type='submit'
			value='发送' />
	</form>
	=========================================NOT
	TAOBAO=============================================
	接口测试/VipOrderServlet.action(非TAOBAO电商)
	<form method='post' action='VipOrderServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='clientID' id='bbb' value="360buy">
		<input type="hidden" value="1" name="test"> <input
			type='submit' value='发送' />
	</form>

	接口测试/VipOrderLocalServlet.action(线下下单)
	<form method='post' action='VipOrderLocalServlet.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='clientID' id='bbb' value="360buy">
		<input type="hidden" value="1" name="test"> <input
			type='submit' value='发送' />
	</form>




	接口测试/kingGang2TaoBao.action(状态通知)
	<form method='post' action='kingGang2TaoBao.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		客户代码<input type='text' name='clientID' id='bbb' value="TAOBAO">
		MSGTYPE<input type='text' name='msg_type' id='bbb' value="UPDATE"> 
		<input type="hidden" value="1" name="test"> <input
			type='submit' value='发送' />
	</form>
	</form>


	接口测试/userInfoProcess.action(用户信息同步)
	<form method='post' action='userInfoProcess.action'>
		明文
		<textarea rows='5' cols='50' name='logistics_interface' id='bbb'></textarea>
		加密文
		<textarea rows='5' cols='50' name='data_digest' id='bbb'></textarea>
		<input type="hidden" value="1" name="test"> <input
			type='submit' value='发送' />
	</form>
	

	<form method='post' action='viewMemData.action'>
		<input type='submit' value='查看内存数据' />
	</form>
	<h5>查看服务器ip和端口</h5>
	<hr />

	<%=request.getServerName()%>
    <%=request.getServerPort()%>
</body>
</html>