<%--
	@2011-11-16
	@ChenRen
	
	该页面是淘宝用户激活的页面；
	因为淘宝的应用不在本机，所以用这个页面在本机测试要改版的淘宝用户激活页面。
	
	如果以后要用到vip激活页面，请直接参考 active4vip.jsp.2011-11-16.bak 
	
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>淘宝用户激活</title>
<link rel="stylesheet" href="css/posttemp.css?d=${str:getVersion() }" type="text/css" />
<link rel="stylesheet" href="css/layout.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link href="artDialog/skins/blue.css" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
<link href="css/validationEngine.jquery.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	.button {padding: 2px 5px !important}
	.button:active {padding: 2px 5px !important}
	.x_msg_title {
		PADDING-RIGHT: 0px;
		PADDING-LEFT: 0px;
		FONT-WEIGHT: bold;
		FONT-SIZE: 12px;
		PADDING-BOTTOM: 0px;
		MARGIN: 0px;
		PADDING-TOP: 0px;
		float: left;
	}
	.cont lable {font-weight: bold; }
</style>
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
<script src="js/jquery.validationEngine-cn.js?d=${str:getVersion() }" type="text/javascript"></script>
<script src="js/jquery.validationEngine.js?d=${str:getVersion() }" type="text/javascript"></script>
<script type="text/javascript" src="js/simpla.jquery.configuration.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/facebox.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery.wysiwyg.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/cookie.js?d=${str:getVersion() }"></script>
</head>
<body>
	<div id="main" >
	<div id="main-content">
		<div style="color:red;"></div>
		<div id="midtit">
		 <span class="gnheader f14 loading">激活！</span></div>
			<!-- 
			<h2>
				淘宝用户激活
				<span style="font-size:12px;font-weight:normal;color:red;margin-left:30px;">您的账号未激活，请先激活！</span>
			</h2>
			 -->
			<div aclass="content-box" style="width:100%;">
				<div id="midfrom">
					<form id="mgnm" action="#"  method="post" >
						<p>
							<label for="userCode" style="display:inline;">客户编码*：</label>
							<input id="userCode"  name="user.userCode" class="validate[required,ajax[ajaxUserCode]] text-input"/>
							请填写您的客户编码&nbsp;如果不知道&nbsp;<span style="cursor: help;">1.请联系网点索取&nbsp;2.<lable title="联系方式在页面底部!">联系我们客服</lable>&nbsp;3.<a href="javascript:;" id="apply4site" style="color:#555555; text-decoration: underline;" title="点击发送消息...">给管理员发送消息申请</a></span>
							<input type="hidden" id="id" name="user.id" value='<s:property value="#session.user.id"/>'>
						</p>
					</form>
					
				</div>
				
				<div class="content-box-header" style="margin-top:30px;"><h3 style="font-size:15px;font-weight:bold; padding-bottom:3px;">您的基本信息</h3></div>
        		<div class="content-box-content" >
        			<form>
					<table width="100%" border="0" >
					  <tr>
					    <td><span>登录账号：</span><input title="可以使用该账号和刚才填写的密码直接登录本系统" value="<s:property value="#session.user.userName" />" class="text-input"></td>
					    <td><span>E-Mail&nbsp;&nbsp;：</span><input value="<s:property value="#session.user.mail"/>" class="text-input"></td>
					  </tr>
					  <tr>
					    <td><span>固定电话：</span><input value="<s:property value="#session.user.telePhone" />" class="text-input"></td>
					    <td><span>手机号码：</span><input value="<s:property value="#session.user.mobilePhone"/>" class="text-input"></td>
					  </tr>
					  <tr>
					  	<td colspan="2">
					  	 <input type="button" value="激活" class="button2 active" style="margin:0 50px 0 270px;">
					  	 <input type="button" value="修改信息" class="button2 edit" title="重新填写基本信息" onclick="javascript:location.href='noint!toBindTBUserStep1.action'">
					  	</td>
					  </tr>
					</table></form>
        		</div>
				
				
				<div id="bg" class="bg"></div>
				<div id="x_msg" class="p_city" >
			        <div class="tit bgc_ccc move" onmousedown="drag(event,this)">
			            <h2 class="x_msg_title">申请编码——请确定消息内容</h2>
			        </div>
			        <div class="cls"></div>
			        <div class="cont">
			            <div id="selectSub">
			               <div id="c00">
			               		<div style="margin:3px 10px">
			               		<table>
				               		<tr>
				               			<td>淘宝账号：</td>
				               			<td id="t_tb"><s:property value="#session.user.shopAccount" /></td>
				               		</tr>
				               		<tr>
				               			<td>登录账号：</td>
				               			<td id="t_un"><s:property value="#session.user.userName" /></td>
				               		</tr>
				               		<tr>
				               			<td>固定电话：</td>
				               			<td id="t_t"><s:property value="#session.user.telePhone" /></td>
				               		</tr>
				               		<tr>
				               			<td>手机号码：</td>
				               			<td id="t_m"><s:property value="#session.user.mobilePhone"/></td>
				               		</tr>
				               		<tr>
				               			<td>&nbsp;&nbsp;&nbsp;&nbsp;E-Mail:</td>
				               			<td id="t_ml"><s:property value="#session.user.mail"/></td>
				               		</tr>
			               		</table>
			               		</div>
			               </div>
			               <div id="c02" style="margin-left:120px;">
			               		<input id="x_msg_ok" class="submit01" type="button" value="发　送" />
			               		<span style="margin: 0px 10px;">&nbsp;</span>
			               		<input id="x_msg_cancl" class="submit01" type="button" value="取　消" />
			               </div>
			            </div>
			        </div>
			    </div>
			</div>
		
	</div>
		<script type="text/javascript">
			function killerrors() { 
				return true; 
			} 
			window.onerror = killerrors;
			$(function() {
				$("table input:text").attr("readonly","readonly").css("cursor","default");
				
				var _ = $("#mgnm");
				$(_).validationEngine(
					{
						ajaxSubmit : true,
						ajaxSubmitFile : "noint!bindTaoBaoUserStep2.action", 
						ajaxSubmitMessage : "激活成功!\r\n您以后登录本平台的方式：\r\n    1.使用刚才的账号和密码\r\n    2.仍然使用淘宝账号 从淘宝入口登录",
						success : true,
						beforeSuccess : function() {
							$(_).find(":submit").css({
								color : "gray",
								cursor : "default"
							});
						},
						afterSuccess : function() {
							$(_)[0].reset();
							$(_).find(":submit").css({
								color : "#FFFFFF",
								cursor : "pointer"
							});
							window.location.href = "waybill_bill.action";
							//window.location.href = "leaveMessage_index.action";
						},
						failure : function() {
							art.dialog.alert("激活失败!");
							$(_).find(":submit").css({
								color : "#FFFFFF",
								cursor : "pointer"
							});
						}
					});
		
				$(".active").click(function(){
					if($("#userCode").val()=="") {
						art.dialog.alert("请输入客户编码!");
						$("#userCode").focus();
						return false;
					}else {
						$(_).submit();
					}
				});
				
				var db = document.body, dd = document.documentElement;
				// 显示/隐藏背景层
				_swapBgDiv = function() {
					var _ = $("#bg"), bgDisp = $(_).css("display");
					if(bgDisp == "none") {
						$(_).css("display", "block");
					  	var h = db.offsetHeight > dd.offsetHeight ? db.offsetHeight : dd.offsetHeight;
					  	$(_).css("height", h + "px");
					} else {
						$(_).css("display", "none");
					}
				}; // _swapBgDiv
				
				// 打开/关闭 发送消息层
				_swapMsgDiv = function() {
					var _ = $("#x_msg"), _Disp = $(_).css("display");
					if(_Disp == "none") {
						$(_).css("display", "block");
					  	$(_).css("left", ($("#bg").get(0).offsetWidth - $(_).get(0).offsetWidth) / 2 + "px");
						$(_).css("top", db.scrollTop  + "px");
					} else {
						$(_).css("display", "none");
					}
					
				}; // _swapMsgDiv

				// 取消
				$("#x_msg_cancl").click(function() {
					$("#msg_linkway").val("");
					$("#msg_text").val("");
					
					_swapBgDiv();
					_swapMsgDiv();
				});
				
				// 确定/发送消息
				$("#x_msg_ok").click(function() {
					if(confirm("确定发送?")) {
						var content = "\r\n　　淘宝账号："+$("#t_tb").text()
							  + "\r\n　　登录账号："+$("#t_un").text()
							  + "\r\n　　固定电话："+$("#t_t").text()
							  + "\r\n　　手机号码："+$("#t_m").text()
							  + "\r\n　　  E-Mail："+$("#t_ml").text();
							  
						var params = {"user.id": $("#id").val(), "user.userName": $("#name").val(), content: content};
						$.post("login_applyForCode.action",  params, function(data) {
							art.dialog.alert(data.applyString);
						}, "json");
	
						$("#msg_linkway").val("");
						$("#msg_text").val("");
						_swapBgDiv();
						_swapMsgDiv();
					}
					
				});

				// 免责协议	 
				$("#_cMz").click(function() {
					var t = $("#_tMz"), b = $("#_bA");
					if(!this.checked) {
						$(t).fadeIn("slow");
						$(b).attr("disabled", "disabled").css("cursor", "default");
					}
					else {
						$(t).fadeOut("slow");
						$(b).removeAttr("disabled").css("cursor", "pointer");
					}
				}); // 免责协议
				
				// 网点编码申请
				$("#apply4site").click(function() {
					_swapBgDiv();
					_swapMsgDiv();
					return false;
				}); // 客户编码申请
				
			}); // $.();
			/* 鼠标拖动 */
			var oDrag = "";
			var ox,oy,nx,ny,dy,dx;
			function drag(e, o) {
			 	var e = e ? e : event;
			 	var mouseD = document.all ? 1 : 0;
			 	if(e.button == mouseD) {
			  		oDrag = o.parentNode;
			  		ox = e.clientX;
			  		oy = e.clientY;  
			 	}
			} // drag
			function dragPro(e) {
			 	if(oDrag != "") { 
				  	var e = e ? e : event;
				  	dx = parseInt($(oDrag).css("left"));
				  	dy = parseInt($(oDrag).css("top"));
				  	nx = e.clientX;
				  	ny = e.clientY;
				  	$(oDrag).css("left", (dx + ( nx - ox )) + "px");
				  	$(oDrag).css("top",  (dy + ( ny - oy )) + "px");
				  	ox = nx;
				  	oy = ny;
			 	}
			} // dragPro
			document.onmouseup = function() {oDrag = "";}
			document.onmousemove = function(event) {dragPro(event);}
		</script>
	<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
	</body>
</html>