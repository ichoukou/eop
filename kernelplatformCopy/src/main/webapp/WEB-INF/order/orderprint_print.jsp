<%--
	@author ChenRen
	@date 	2012-02-13
	@description
		快递单打印页面
		
--%>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="javax.xml.parsers.DocumentBuilder,
javax.xml.parsers.DocumentBuilderFactory,
javax.xml.parsers.*,
javax.xml.transform.*,
javax.xml.transform.dom.DOMSource,
javax.xml.transform.stream.StreamResult,
org.w3c.dom.*,
java.io.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">

<head>
	<!-- base href="<%=basePath%>"-->
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>单据模板设计</title>
	<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }" ></script>
	<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/printtemp/swfobject.1-1-1.min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/printtemp/swfobject.js?d=${str:getVersion() }"></script>
</head>

<body>
	<div id="main-contentx">	
		<div id="expressFormPrinter" style='width: 869px; height: 978px; margin: 0 auto;'>
		</div>
	</div>
	<script type="text/javascript">
	    
		$(function() {
			$('#expressFormPrinter').css('height',489*<%=request.getAttribute("orderNum")%>);
			$("#expressFormPrinter").flash({
				swf: 'printermode.swf?'+new Date(),
                width: '100%',
                height: '100%',
                wmode: false,
                flashvars: {
                	xml:'<%=request.getAttribute("xml")%>',
                	order_number:'<%=request.getAttribute("orderNum")%>',
                	data:'<%=request.getAttribute("data")%>',
                	bg:'<%=request.getAttribute("bg")%>',
                	//bg:'images/printtemp/yto_08.jpg',
                	copyright:'probiz'
                }
				/*
                flashvars: {
                	xml:'%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Eship_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E487%3A114%3A73%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Eship_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E607%3A171%3A172%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u7535%u8BDD%3C/name%3E%3Cucode%3Eship_tel%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E665%3A114%3A105%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Eship_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E693%3A229%3A88%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Eship_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E501%3A172%3A80%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Edly_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E127%3A98%3A81%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Edly_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E229%3A159%3A162%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Edly_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E134%3A161%3A68%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Edly_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E307%3A228%3A93%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u624B%u673A%3C/name%3E%3Cucode%3Edly_mobile%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E142%3A223%3A108%3A20%3C/position%3E%3C/item%3E%3C/printer%3E',
                	order_number:'3',
                	data:'<data><item><store_name>全联网网店管理系统</store_name><ship_name>micheal0792</ship_name><ship_regionFullName>中国,澳门,澳门</ship_regionFullName><ship_address>12312</ship_address><ship_tel>123123</ship_tel><ship_zip>201705</ship_zip><store_name>藤丝服装商城(演示商城)</store_name><order_no>D120207445693</order_no><shipment_item_count>1</shipment_item_count><shipment_no>F120215066159</shipment_no><mail_no></mail_no><dly_name>张某</dly_name><dly_regionFullName>中国,上海,浦东</dly_regionFullName><dly_address>世纪大道</dly_address><dly_tel></dly_tel><dly_zip>123456</dly_zip><dly_mobile>12345678901</dly_mobile><date_year>2012</date_year><date_moth>2</date_moth><date_day>15</date_day></item><item><store_name>全联网网店管理系统</store_name><ship_name>刘庆</ship_name><ship_regionFullName>中国,四川,巡场</ship_regionFullName><ship_address>21212</ship_address><ship_tel>11111111111</ship_tel><ship_zip>234234</ship_zip><store_name>藤丝服装商城(演示商城)</store_name><order_no>D111205953764</order_no><shipment_item_count>1</shipment_item_count><shipment_no>F111205338182</shipment_no><mail_no>1231231231</mail_no><dly_name>张某</dly_name><dly_regionFullName>中国,上海,浦东</dly_regionFullName><dly_address>世纪大道</dly_address><dly_tel></dly_tel><dly_zip>123456</dly_zip><dly_mobile>12345678901</dly_mobile><date_year>2012</date_year><date_moth>2</date_moth><date_day>15</date_day></item><item><store_name>全联网网店管理系统</store_name><ship_name>靠哥</ship_name><ship_regionFullName>中国,澳门,澳门</ship_regionFullName><ship_address>12312</ship_address><ship_tel>123123</ship_tel><ship_zip>201705</ship_zip><store_name>藤丝服装商城(演示商城)</store_name><order_no>D120207445693</order_no><shipment_item_count>1</shipment_item_count><shipment_no>F120215066159</shipment_no><mail_no></mail_no><dly_name>张某</dly_name><dly_regionFullName>中国,上海,浦东</dly_regionFullName><dly_address>世纪大道</dly_address><dly_tel></dly_tel><dly_zip>123456</dly_zip><dly_mobile>12345678901</dly_mobile><date_year>2012</date_year><date_moth>2</date_moth><date_day>15</date_day></item></data>',
                	bg:'http://static.imall114.com/media//default/order/yto.jpg',
                	//bg:'images/printtemp/yto_08.jpg',
                	copyright:'probiz'
                }
                */
                /*
                flashvars: {
                    xml: '%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Eship_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E487%3A114%3A73%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Eship_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E607%3A171%3A172%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u7535%u8BDD%3C/name%3E%3Cucode%3Eship_tel%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E665%3A114%3A105%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Eship_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E693%3A229%3A88%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Eship_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E501%3A172%3A80%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Edly_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E127%3A98%3A81%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Edly_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E229%3A159%3A162%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Edly_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E134%3A161%3A68%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Edly_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E307%3A228%3A93%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u624B%u673A%3C/name%3E%3Cucode%3Edly_mobile%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E142%3A223%3A108%3A20%3C/position%3E%3C/item%3E%3C/printer%3E',
                    order_number: '1',
                    //data: '<data><item><store_name>全联网网店管理系统</store_name><ship_name>刘庆</ship_name><ship_regionFullName>中国,四川,巡场</ship_regionFullName><ship_address>21212</ship_address><ship_tel>11111111111</ship_tel><ship_zip>234234</ship_zip><store_name>藤丝服装商城(演示商城)</store_name><order_no>D111205953764</order_no><shipment_item_count>1</shipment_item_count><shipment_no>F111205338182</shipment_no><mail_no></mail_no><dly_name>张某</dly_name><dly_regionFullName>中国,上海,浦东</dly_regionFullName><dly_address>世纪大道</dly_address><dly_tel></dly_tel><dly_zip>123456</dly_zip><dly_mobile>12345678901</dly_mobile><date_year>2012</date_year><date_moth>2</date_moth><date_day>13</date_day></item></data>',
                    data: '<data><item><store_name>全联网网店管理系统</store_name><ship_name>micheal0792</ship_name><ship_regionFullName>中国,澳门,澳门</ship_regionFullName><ship_address>12312</ship_address><ship_tel>123123</ship_tel><ship_zip>201705</ship_zip><store_name>藤丝服装商城(演示商城)</store_name><order_no>D120207445693</order_no><shipment_item_count>1</shipment_item_count><shipment_no>F120215066159</shipment_no><mail_no></mail_no><dly_name>张某</dly_name><dly_regionFullName>中国,上海,浦东</dly_regionFullName><dly_address>世纪大道</dly_address><dly_tel></dly_tel><dly_zip>123456</dly_zip><dly_mobile>12345678901</dly_mobile><date_year>2012</date_year><date_moth>2</date_moth><date_day>15</date_day></item><item><store_name>全联网网店管理系统</store_name><ship_name>刘庆</ship_name><ship_regionFullName>中国,四川,巡场</ship_regionFullName><ship_address>21212</ship_address><ship_tel>11111111111</ship_tel><ship_zip>234234</ship_zip><store_name>藤丝服装商城(演示商城)</store_name><order_no>D111205953764</order_no><shipment_item_count>1</shipment_item_count><shipment_no>F111205338182</shipment_no><mail_no>1231231231</mail_no><dly_name>张某</dly_name><dly_regionFullName>中国,上海,浦东</dly_regionFullName><dly_address>世纪大道</dly_address><dly_tel></dly_tel><dly_zip>123456</dly_zip><dly_mobile>12345678901</dly_mobile><date_year>2012</date_year><date_moth>2</date_moth><date_day>15</date_day></item></data>',
                    bg: 'images/printtemp/yto_08.jpg',
                    copyright: 'probiz'
                }
			*/
			});
			
			/*
			<object id="dly_printer_flash" type="application/x-shockwave-flash" data="printermode.swf" height="100%" width="100%">
		   	<param name="quality" value="high">
			<param name="allowScriptAccess" value="always">
		    <param name="movie" value="printermode.swf">
		    <param name="swLiveConnect" value="true">
		    <param name="flashVars" value="%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Email_no%3C/ucode%3E%3Cfont%3Ehttp%3A//static.test.yto56.com.cn/media/apple/order/110822/_-9050425655993134703.jpg%3C/font%3E%3Cfontsize%3E18%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A11%3A276%3A72%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Eorder_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A105%3A120%3A40%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E2%3A106%3A60%3A38%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A19%3A52%3A36%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A162%3A65%3A41%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Eshipment_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E80%3A162%3A120%3A40%3C/position%3E%3C/item%3E%3C/printer%3E">
			</object>
			
			ExpressTplEditor.tplEditor = $("#expressTplEditor");
		    ExpressTplEditor.tplEditor.flash({
		        swf: 'printermode.swf',
		        width: '100%',
		        height: '100%',
		        wmode: 'opaque',
		        flashvars: {
		            data: "%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Email_no%3C/ucode%3E%3Cfont%3Ehttp%3A/images/printtemp/yto_08.jpg%3C/font%3E%3Cfontsize%3E18%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A11%3A276%3A72%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Eorder_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A105%3A120%3A40%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E2%3A106%3A60%3A38%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A19%3A52%3A36%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A162%3A65%3A41%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Eshipment_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E80%3A162%3A120%3A40%3C/position%3E%3C/item%3E%3C/printer%3E",
		            bg: null,
		            copyright: 'probiz'
		        }
		    });*/
		});
	</script>
</body>
</html>

		<!-- 
		background-image: url(images/printtemp/yto_08.jpg)
		
		<div id="x" class="express_tpl_editor">
	     	<object id="dly_printer_flash" type="application/x-shockwave-flash" data="printer.swf" height="100%" width="100%">
	           	<param name="quality" value="high">
				<param name="allowScriptAccess" value="always">
			    <param name="movie" value="printer.swf">
			    <param name="swLiveConnect" value="true">
			    <param name="flashVars" value="%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Email_no%3C/ucode%3E%3Cfont%3Ehttp%3A//static.test.yto56.com.cn/media/apple/order/110822/_-9050425655993134703.jpg%3C/font%3E%3Cfontsize%3E18%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A11%3A276%3A72%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Eorder_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A105%3A120%3A40%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E2%3A106%3A60%3A38%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A19%3A52%3A36%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A162%3A65%3A41%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Eshipment_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E80%3A162%3A120%3A40%3C/position%3E%3C/item%3E%3C/printer%3E">
			</object>
	    </div>
		 -->