<%--
	@2011-09-26/ChenRen
	
	用户激活页面
		
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>激活</title>
<link rel="stylesheet" type="text/css" href="css/jquery-easyui/easyui.css">
<link rel="stylesheet" type="text/css" href="css/jquery-easyui/icon.css">
<link rel="stylesheet" type="text/css" href="css/jquery-easyui/demo.css">
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link href="css/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<link href="css/template.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	a:hover {color: #FFFFFF;}
</style>
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
<script src="js/jquery.validationEngine-cn.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script src="js/jquery.validationEngine.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script type="text/javascript" src="js/simpla.jquery.configuration.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/facebox.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery.wysiwyg.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/cookie.js?d=${str:getVersion() }"></script>
<link href="artDialog/skins/blue.css" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
<script type="text/javascript">
	$(function(){
		
		var _ = $("#mgnm");
		// 美工UI框架有问题。这里要手动改样式以适应界面布局。
		$(_).find("label").css("width", "80px");
		$(_).find("input").addClass("text-input");
		$(_).validationEngine({
			ajaxSubmit: true,
			ajaxSubmitFile: "noint!bindTaoBaoUser.action",
			ajaxSubmitMessage: "激活成功!请重新登录!",
			success : true,
			myValidation: function() {
					var regT = /^\d{3,4}-\d{4}|\d{7,8}-\d{4}|\d{7,8}$/, regM = /^1[3458]\d{9}$/;
					var tv = $.trim($("#tel").val()), mv = $.trim($("#mp").val());
					if(tv == "" && mv == "") {
						art.dialog.alert("固定电话和手机号码必须填写一个!");
						return false;
					}
					if(tv != "") {
						if(!regT.test(tv) ) {
							art.dialog.alert("固定电话格式不对!");
							return false;
						}
					}
					if(mv != "") {
						if(!regM.test(mv) ) {
							art.dialog.alert("手机号码格式不对!");
							return false;
						}
					}
					return true;
				},
			beforeSuccess :  function() {
					// 禁用提交按钮
					$(_).find(":submit").css({color: "gray", cursor: "default"});
				},
			afterSuccess :  function() {
					$(_)[0].reset();
					$(_).find(":submit").css({color: "#FFFFFF", cursor: "pointer"});
					window.location.reload();
				},
			failure : function() {
				art.dialog.alert("激活失败!");
					$(_).find(":submit").css({color: "#FFFFFF", cursor: "pointer"});
				} 
		});
		
		// 用户编码申请
		$("#apply4code").click(function() {
			var __ = $(this).next(), mail = $.trim($("#email").val());
			if(mail == "") {
				art.dialog.alert("请输入邮箱地址! 用户编码将发送指定邮箱地址!");
				$("#email").focus();
				return false;
			}
			$(__).css("display", "inline");
			var params = {"user.id": $("#id").val(), "user.userName": $("#name").val(), "user.mail": mail};
			//params = "?user.id"+$("#id").val()+"user.userName"+$("#name").val()+"user.mail"+mail;
			$.getJSON("login_applyForCode.action", params, function(data) {
				art.dialog.alert(data.applyString);
				  $(__).css("display", "none");
			});
			return false;
		}); // 用户编码申请
		
	}); // $.();
</script>
</head>
<body id="login">
		<div id="login-wrapper" class="png_bg">
			<div id="login-top">
				<img id="logo" src="images/logo.png" alt="Simpla Admin logo" />
			</div>
			<div id="login-content">
				<div class="login-box">
            	<div class=" login-box-header">
            		<ul class="login-box-tabs">
						<li><a href="#tab1" id="a_tab1" class="default-tab">淘宝用户绑定</a></li>
					</ul>
				</div>
				<div class="login-box-content">
					<div class="tab-content default-tab" id="tab1">
						<form id="mgnm" action="#"  method="post" >
                    	<p>
                    	<!-- 
        					<label for="userCode" style="display:inline;">客户编码*：</label>
							<input id="userCode"  name="user.userCode" class="validate[required,ajax[ajaxUserCode]]" style="margin: 0px;"/>
                    	 -->
        					<label for="userCode" style="display:inline;">网点编码*：</label>
							<input id="userCode"  name="user.site" class="validate[required,ajax[ajaxSite]]" style="margin: 0px;"/>
							<input type="hidden" id="name" name="user.shopAccount" value='<s:property value="user.shopAccount"/>'>
							<input type="hidden" id="id" name="user.id" value='<s:property value="user.id"/>'>
                        </p><!-- type="hidden"  -->
                      	<div class="clear"></div>
                        <p>
							<label for="tel" style="display:inline;">固定电话*：</label>
							<input id="tel"  name="user.telePhone" />
                  		</p> 
                  		<div class="clear"></div>
                        <p>
							<label for="mp" style="display:inline;">手机号码*：</label>
							<input id="mp"  name="user.mobilePhone"/>
                  		</p>
                  		<div class="clear"></div>
                  		<p>
							<label for="email" style="display:inline;">邮　　　　箱*：</label>
							<input id="email"  name="user.mail" class="validate[required,custom[email]]" value="<s:property value="user.mail"/>"/>
                  		</p>
                        <div class="clear"></div>
						<p>
							<input class="submit01" type="submit" value="保 　存" style="margin: 0 30px;"/>
							<input class="submit01" type="reset" value="重　置" style="margin: 0 30px;"/>
						</p>
						<div class="clear"></div>
					</form>
				</div>
		</div>
		
		
		<hr>
		
		
		<div id="main-content">
            <h2>用户激活</h2>
            <div aclass="content-box">
                <div>
                    <form action="" method="post" name="reg" id="reg" onsubmit="return checkForm(this);">
                        <p>
                            <label>
                                用户名:
                            </label>
                            <input onblur="" id="username" tabindex="1" onchange="checkOnChange(this);" maxlength="12" size="24" name="username" />不超过12个字节(只能包含: 数字，字母和下划线)
                        </p>
                        <p>
                            <label>
                                昵  称:
                            </label>
                            <input onblur="checkUserName('http://passport.cnfol.com/check');" id="username" tabindex="1" onchange="checkOnChange(this);" maxlength="12" size="24" name="username" />不超过14个字节，昵称不能修改
                        </p>
                        <p>
                            <label>
                                设置手机:
                            </label>
                            <input id="mobile" tabindex="3" onchange="checkOnChange(this);" maxlength="60" name="mobile" />请填写正确的手机号码，成为中金在线认证用户，享有贵宾服务。（不收取任何费用）
                        </p>
                        <p>
                        <label>
                            设置邮箱:
                        </label>
                        <input id="mobile" tabindex="3" onchange="checkOnChange(this);" maxlength="60" name="mobile" />为了更好的为您服务，注册用户需要邮箱验证才能登录，请填写正确邮箱。
                        <p>
                            <label>
                                网点编码:
                            </label>
                            <input onblur="" id="username" tabindex="1" onchange="checkOnChange(this);" maxlength="12" size="24" name="username" />不超过12个字节(只能包含: 数字，字母和下划线)
                        </p>
                        <p>
                            <input class="button" type="submit" value="注 册" /><input class="button" type="submit" value="重 置" />
                        </p>
                    </form>
                </div>
            </div>
            <small>
                &#169; Copyright 2011  | Powered by YTO 
                | <a href="#">Top</a>
            </small>
        </div><jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
</body>
</html>