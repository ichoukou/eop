<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关联淘宝店铺</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css"/>
<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<script src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }" type="text/javascript" charset="UTF-8"></script>
<script src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }" type="text/javascript" charset="UTF-8"></script>
<script src="${jsPath}/module/dialog.js?d=${str:getVersion() }" type="text/javascript"></script>
<!--[if IE 6]>
	<script type="text/javascript" src="js/util/position_fixed.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.png');
	</script>
<![endif]-->
<script type="text/javascript">
	$(function(){
		var result = $("#associationResult").val();
		window.opener.location = "toBindedAccount.action?menuFlag=user_tobindAccount";
		removeCookie('action');	// 清空cookie
		var msg="恭喜您，关联成功！";
		if(result=='repeatShop'){
			msg="该店铺已经关联啦，无须再次关联！";
		}
		else if(result=='selfAccount'){
			msg="不能绑定自己本账号！";
		}
		else if(result=="existsAccount"){
			msg="账号关联失败，易通平台系统中已经存在此用户名！";
		}
		else if(result != ''){
			msg=result;
		}
		//$('#msg').text(msg);
		
		var resDialog = new Dialog();
		resDialog.init({
			contentHtml: msg,
			yes: function() {
				resDialog.close();
				window.close();
			},
			closeBtn: true
		});
	});
		
	function getCookieValue(name) {
	    var name = escape(name);
	    //读cookie属性，这将返回文档的所有cookie
	    var allcookies = document.cookie;
	    //查找名为name的cookie的开始位置
	    name += "=";
	    var pos = allcookies.indexOf(name);
	    //如果找到了具有该名字的cookie，那么提取并使用它的值
	    if (pos != -1) {                                             //如果pos值为-1则说明搜索"version="失败
	        var start = pos + name.length;                  //cookie值开始的位置
	        var end = allcookies.indexOf(";", start);        //从cookie值开始的位置起搜索第一个";"的位置,即cookie值结尾的位置
	        if (end == -1) end = allcookies.length;        //如果end值为-1说明cookie列表里只有一个cookie
	        var value = allcookies.substring(start, end);  //提取cookie的值
	        //console.log(decodeURI(value));
	        value=decodeURI(value);
	        value=value.replace('"','');
	        value=value.replace('"','');
	        return unescape(value);                           //对它解码
	    }
	    else return "";                                             //搜索失败，返回空字符串
	}
	/**
	 * 清除cookie
	 * @param {Object} name
	 */
	function removeCookie(name){
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval = getCookieValue(name);
		if (cval != null)
			document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
	}
</script>
</head>
<body>
	<div style="display: none;">
	<input type="hidden" id="resultResponse" value="${resultResponse}" />
	<input type="hidden" id="associationResult" value="${associationResult }" />
	</div>
	
	
</body>
</html>