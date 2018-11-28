<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>每日订单走势</title>
<link rel="stylesheet" type="text/css" href="css/jquery-easyui/easyui.css?d=${str:getVersion() }">
<link rel="stylesheet" type="text/css" href="css/jquery-easyui/icon.css?d=${str:getVersion() }">
<link rel="stylesheet" type="text/css" href="css/layoutshow.css?d=${str:getVersion() }">
<link rel="stylesheet" href="css/layout.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link href="artDialog/skins/blue.css" rel="stylesheet"/>
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery-easyui/easyui-lang-zh_CN.js?d=${str:getVersion() }"></script>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
<script type="text/javascript">
	$(function(){	
		var startDate = $("#starttime").datebox('getValue');
    	var endDate = $("#endtime").datebox('getValue');
		var vipName = $("#vipName").val();
		if(vipName==undefined) vipName = "";

		$("#chart").attr("src","pingTaiViewTendency.action?startDate="+startDate+"&endDate="+endDate+"&vipName="+vipName+"&d="+Math.random());
	});
	function showJfreeChart(){
		var startDate = $("#starttime").datebox('getValue');
    	var endDate = $("#endtime").datebox('getValue');
		var vipName = $("#vipName").val();
		
		var sd = startDate.split("-"), ed = endDate.split("-");
		var x = Date.parse(ed[1]+"/"+ed[2]+"/"+ed[0]) - Date.parse(sd[1]+"/"+sd[2]+"/"+sd[0]), // 时间差，毫秒; 参数格式为 月/日/年
			maxX = 1000 * 60 * 60 * 24 * 31;	// 最大时间差值：30天;
			
		if(startDate == "" || endDate == ""){
			art.dialog.alert("请选择时间段!");
		}else{
			if(startDate > endDate)
				art.dialog.alert("起点时间应在止点时间之前!");
			else if(x > maxX)
				art.dialog.alert("请查询30天之内的数据");
			else{
				if(vipName==undefined) vipName = "";

				$("#chart").attr("src","pingTaiViewTendency.action?vipName="+vipName+"&startDate="+startDate+"&endDate="+endDate+"&d="+Math.random());
			}
		}
	}

</script>
</head>
<body style="padding: 0px;">
<div id="main">
  <div id="main-content">
    <div id="midtit"><span class="loading">每日订单走势</span></div>
    <div id="midfrom">
      <form action="" method="get">
        <p> <span>上报时间：</span>
          <input class="easyui-datebox" type="text" id="starttime" style="width:90px;" value="<s:property value="startDate"/>">
          至　
          <input class="easyui-datebox" type="text" id="endtime" style="width:90px;" value="<s:property value='endDate'/>">
          &nbsp;
			<c:if test="${ yto:getCookie('userType') == 4 }">
				<span>分仓账号：</span>
				<select id="vipName" name="vipName">
					<option <s:if test="%{vipName == 0}">selected</s:if>  value=0>所有分仓</option>
					<s:iterator value="vipList">
						<option <s:if test="%{vipName == id}">selected</s:if> value=<s:property value='id'/>><s:property value="userNameText"/>(<s:property value="userName"/>)</option>
					</s:iterator>
				</select>
			</c:if>	
          <input name="button" type="button" class="submit01"  value="查&nbsp;询 " onClick="showJfreeChart()"/>
        </p>
      </form>
    </div>
    <div style="border: #8db2e3 1px solid; background-color:#fff;" > <img id="chart" src="" style="width:100%;height:400px"></div>
  </div>
</div>
</body>
</html>
