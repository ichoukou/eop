<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<html lang="zh-CN">
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/acc_admin.css?d=${str:getVersion()}" media="all" />
<!-- E 当前页面 CSS -->
<title>上传文件 - admin</title>
<head>
<style>
	.upload{
		width:600px;
		margin:50px auto 0;
	}
	.frist,.second,.third{
		text-align:left;
		margin-top:18px;
		padding:10px;
		border:1px solid #ccc;
	}
	p{
		padding:0 0 10px;
	}
	.loading{
		line-height:100%;
		text-align:center;
		background:url("images/module/lightbox-ico-loading.gif") no-repeat center top;
		height:40px;
		padding-top:38px;
		display:none;
	}
</style>
</head>
<body>
<div id="content">

    <div class="upload" style="text-align:center;margin-top:50px;">
    	<div class="frist">
	    	<p>第一步：下载Excel模板</p>
	    	<form action="admin_download.action?menuFlag=home_admin_upload" id="form" name="form" enctype="multipart/form-data" method="post">
	   			<a  class="btn btn_a">
	   			<input name="downLoadPaths" type="hidden" value="/template/短信发送.xls"/>
	   			<input type="submit" value="下载"/></a>
	   		</form>
    	</div>
   		<div class="second">
	   		<p>第二步：上传Excel文件（只包含一列手机号和一列短信内容）</p>		
			<!-- 若包含中文必须是UTF-8编码，否则会出错  -->
			<form action="admin_upload.action?menuFlag=home_admin_upload" id="form1" name="form1" enctype="multipart/form-data" method="post">
	                             文件:<input type="file" name="file" id="file" />
	                             <a class="btn btn_a">
	                              	<input  type="submit" id="submit1" name="submit1" value="上传" onclick="javascript:loading();"/>
	                             </a>
	               <font color="red"><s:property value="message1"/></font>
	        </form>
	        
	<!--         <p>第二步：输入要发送的短信内容</p> -->
	        <form action="admin_updateContent.action?menuFlag=home_admin_upload" id="form2" name="form2" method="post">
	<!--                                 内容:  -->
	<!--             <textarea name="messageContent" id="messageContent" cols="50" rows="6"></textarea> -->
   		</div>
   		<div class="third">
	   		<p>第三步：选择模块类型</p>
	                               模块类型：
	            <select id="smsType" name="smsType" class="select-text">
	                <option value="99" selected="selected">请选择--</option>
	                <option value="999">新龙直销</option>
	                <option value="18">汉麻世家</option>
	                <option value="15">本草堂</option>               
	            </select>                                   
	            <a class="btn btn_a"><input type="submit" id="submit2" name="submit2" value="提交"/></a>
	            <font color="red"><s:property value="message2"/></font>
	        </form> 
   		</div> 
     </div>       
     
     <div class="loading" id="loading">正在上传请稍侯……</div>
        
     <script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>       
     <!-- S 当前页面 JS -->
	<script type="text/javascript">
		function loading(){
			$("#loading").show();
			$("#loading").attr({"disabled":"disabled"}); 
		}
	</script>
	</div>
	</body>
</html>


