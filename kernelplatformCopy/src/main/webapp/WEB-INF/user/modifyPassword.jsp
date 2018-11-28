<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion()}" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion()}" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion()}" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion()}" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/config.css?d=${str:getVersion()}" media="all" />
<!-- E 当前页面 CSS -->
<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js"></script>
<div id="content">

	<div id="content_bd" class="clearfix">
		<div id="psw_process">
					<label for="login_id">登录账号：</label> <input type="text"
						class="input_text" id="login_id" name="loginName" /> <span
						id="login_idTip"></span> 
				
					<a href="javascript:void(0);" class="btn btn_a" title="重置密码" id="submit_btn_resPassword" ><span>重置密码</span>
					</a>
				
		</div>
	</div>
</div>

<script src="${jsPath}/common/common.js?d=${str:getVersion() }"type="text/javascript"></script>
<script type="text/javascript" src="${jsPath}/page/modifyPassword.js"></script>
	

