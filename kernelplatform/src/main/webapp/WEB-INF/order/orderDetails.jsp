<%--
	@author ChenRen
	@date 	2011-08-05
	@description
		订单详情页面
		入口：卖家电子对账页面 - 点击订单号查看详情
--%>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml" >

<head>
	<base href="<%=basePath%>" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>订单详细信息</title>
	<style type="text/css">
		.tr-zi {
			height:	20px;
			text-align: center;
			font-size: 9pt;
			padding-top: 1px;/**/
			padding-bottom: 1px;
			vertical-align: middle;
			background-position: center;
		}
		tr:hover {background-color: #ECF0F2; }
		td {font-size: 12px; color: #333333; font-family: "Verdana"; margin: 0; padding: 0; text-align: left;}
		th {margin: 0; padding: 0; height: 25;}
		.stit {
			font-size: 9pt;
			font-weight: bold;
			color: #333333;
			text-align: center;
			background-image: url(/images/titbg.jpg);
			padding-top: 1px;
		}
		.table {
			background-color: #DEDEC8;
			border-top: 1px solid #4F556A;
			border-right: 2px solid #4F556A;
			border-bottom: 2px solid #4F556A;
			border-left: 1px solid #4F556A;
			margin-top: 20px;
			margin-bottom: 20px;
		}
		.table-zi { border: 1px solid #B2C9E0; border-collapse:collapse; background-color: #FFFFFF; }
		.numAlignRight {text-align: right;}
	</style>
	
	<script src="js/jquery-1.4.2.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<link href="artDialog/skins/blue.css?d=${str:getVersion() }" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">


	</script>
</head>

<body>
	<table id="tbTB" width="700" cellspacing="0" cellpadding="8" 
		bordercolor="#b2c9e0" border="1" align="center" class="table-zi">
		
        <tr>
        	<td>物流号</td>
        	<td><s:property value="order.txLogisticId"/></td>
        </tr>
        <tr>
        	<td>业务交易号</td>
        	<td><s:property value="order.tradeNo"/></td>
        </tr>
        <tr>
        	<td>运单号</td>
        	<td><s:property value="order.mailNo"/></td>
        </tr>
        <tr>
        	<td>发送开始时间</td>
        	<td><s:property value="order.sendStartTime"/></td>
        </tr>
        <tr>
        	<td>发送结束时间</td>
        	<td><s:property value="order.sendEndTime"/></td>
        </tr>
        <tr>
        	<td>保价</td>
        	<td><s:property value="order.insuranceValue"/></td>
        </tr>
        <tr>
        	<td>是否打包</td>
        	<td><s:property value="order.packageOrNot"/></td>
        </tr>
        <tr>
        	<td>重量(kg)</td>
        	<td><s:property value="order.weight"/></td>
        </tr>
        <tr>
        	<td>单价</td>
        	<td><s:property value="order.signPrice"/></td>
        </tr>
        <tr>
        	<td>订单状态</td>
        	<td><s:property value="order.status"/></td>
        </tr>
	</table>
</body>
</html>
