<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>最新动态</title>
<link rel="stylesheet" href="css/layout.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
<style type="text/css"> 
.newstop{text-align:center;position:relative; margin:20px 0;}
.newstop .tips{width:200px;position:absolute;top:0px;left:600px;}
.newstop .tips img{padding:0 5px;}
.newstop p{color:#70797e;text-align:center;clear:boht;}
.newscontent{ text-align:left;line-height:24px; margin:0 10px; }
</style>
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
<script type="text/javascript">
	$(function(){
		$("#content").html($("#contentVal").val());
	});
	function openMessage(){
		parent.main.location = "sendMessage_openAdviseUI.action";
		parent.side.layerLight("advise_li");
	}
</script>
</head>
<body>
<div id="main">
	<div id="main-content">
		<div class="newstop">
		</div>
		<div class="newscontent">
			<div align="center"><font size="+3">打印指南</font></div>
<div align="left">
	 <font size="+3">1、如何在线下单</font><br />	
	 1、如何在线下<br />	
	  <img src="images/order//8.gif" /><br />
	  淘宝上批量勾选订单—点击批量发货<br />
	   <img src="images/order//10.jpg" /><br />
		选择物流公司—选择在线下单—点击批量发货<br /><br />
</div>			
<div align="left">
  <font size="+3">Win 7 版：</font><br />
  1、	点击“开始”，找到“控制面板”  <br />
  <img  src="images/order//1.gif" /><br />
  2、	点击“控制面板”，找出“设备和打印机” <br />
   <img  src="images/order//2.gif" /><br />
  3、	点击“设备打印机” <br />
    <img  src="images/order//3.gif" /><br />
  4、	点击“打印服务器属性”，填写内容 <br />
   <img  src="images/order//4.gif" /><br />
  5、	点击确定，完成设置
</div><br /><br />
<div align="left">
  <font size="+3">Xp版：</font><br />
  1、选择“打印和传真”  <br />
  <img  src="images/order//5.gif" /><br />
  2、右击，选择副武器属性 <br />
   <img  src="images/order//6.gif" /><br />
  3、创建新格式，填写宽度高度，点击保存<br />
    <img  src="images/order//7.gif" /><br />
 </div><br /><br />
 <div align="left">
	 <font size="+3">3、易通打印并发货</font><br />	
	批量勾选订单—填写运单号—批量打印快递单—点击发货<br /><br />		
	<font color="#FF9900">注意：记得打印时，选择纸张为“快递单”</font><br /><br />	
</div>
</div>
	</div>
</div>
</body>
</html>