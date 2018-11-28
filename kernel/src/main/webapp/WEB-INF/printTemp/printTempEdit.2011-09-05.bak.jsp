<%--
	@author ChenRen
	@date 	2011-08-30
	@description
		快递单打印模板编辑/设计页面



	@2011-09-05/ChenRen
	启用另一个方案;
	使用 电子商务 - flash 方式进行单据设计
	这个页面不再使用!
--%>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">

<head>
	<!-- base href="<%=basePath%>"-->
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>电子对账</title>

	<link rel="stylesheet" href="css/annotation.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<link rel="stylesheet" type="text/css" href="jquery-image-annotation-1.3/1.3.2/css/annotation.css" />
	<style type="text/css">
		a {font-size: 12px;}
	</style>
	
	<script src="js/jquery-1.4.2.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="jquery-image-annotation-1.3/1.3.2/js/jquery-ui-1.7.1.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="jquery-image-annotation-1.3/1.3.2/js/jquery.annotate.x.js?d=${str:getVersion() }"></script>
	
	<script type="text/javascript">
		$(function() {
			$("#x_loadData").click(function() {
				$.getJSON("temp!loadTempDeatils.action", {tempId: 100}, function(json) {
					if(json.detailsList.length > 0) {
						
						//var x = json.detailsList;
						$("#x").annotateImage({
							editable: true,
							useAjax: false,
							notes: json.detailsList
							/*
							{
							"id":1,
							"nameCode":"name",
							"top":26,
							"left":161,
							"height":37,
							"width":52
							"tempId":100,
							"text":"名字",
							"editable":"true",
							}
							
							
							notes: [{ "top": 26,
								   "left": 161,
								   "width": 52,
								   "height": 37,
								   "text": "第二个图",
								   "id": "e69213d0-2eef-40fa-a04b-0ed998f9f1f5",
								   "editable": false 
								 }, { "top": 34,
								   "left": 179,
								   "width": 68,
								   "height": 74,
								   "text": "NATIONAL GALLERY DOME",
								   "id": "e7f44ac5-bcf2-412d-b440-6dbb8b19ffbe",
								   "editable": true 
								 }]
							*/
						}); // anotateImage
					} // if
				}); // $.getJSON
			}); // x_loadData click
			
			$("#x_save").click(function() {
				$("span[type='notes']")
			});// $("#x_save").click()
			
		});
	</script>
</head>

<body>
	<div id="main-content">
		<!-- 
		<select>--请选择模板--</select>
		 -->
		<br>
		<a href="javascript:;" id="x_loadData">[加载模板数据]</a>
		<span> | </span>
		<a href="javascript:;" id="x_save">[保存(全局保存.保存所有的元素)]</a>
		<br>
		<br>
		
		<img id="x" src="images/printtemp_bgimg_yto1.jpg" style="border:1px solid red;" alt="打印模板设计" />
	</div>
	
</body>
</html>