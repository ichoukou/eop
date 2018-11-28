<%@ page language="java" pageEncoding="UTF-8"%>
<html lang="zh-CN">
<head>
<meta charset="utf-8" />
<title>商家信息管理</title>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" type="text/css"
	href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css"
	href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }"
	media="all" />
<script type="text/javascript"
	src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<%--<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>--%>
<%--<script type="text/javascript" src="${jsPath}/module/jquery-1.4.3.js"></script>--%>
<link rel="stylesheet" type="text/css" href="/css/base/reset.css" />
<link rel="stylesheet" type="text/css" href="/css/common/common.css" />
<link rel="stylesheet" type="text/css"
	href="/css/module/button.css?d=v1.0.0" media="all" />
<link rel="stylesheet" type="text/css"
	href="/css/module/dialog.css?d=v1.0.0" media="all" />
<link rel="stylesheet" type="text/css"
	href="/css/module/box.css?d=v1.0.0" media="all" />
<!--------------当前页面css--------------->
<link rel="stylesheet" type="text/css"
	href="${cssPath}/facebill/css/merchant-manege.css" />
<!--------------当前页面js--------------->

</head>

<body>
	<div id="content">
		<div class="clearfix" id="content_bd">
			<!-- S Box -->
			<div id="box_form" class="box box_a">
				<div class="box_bd">
					<form class="form" method="post" id="q_form"
						action="sSellerInfo.action?menuFlag=sjzh_sjzh">
						<p>
							<span class="clearfix">
							 	<label>商家代码：</label> 
							 	<input class="input_text" type="text" value="${sellerUserCode }" name="sellerUserCode" id="sellerUserCode" /> 
							</span> 
							<span class="clearfix"> 
								<label>名称：</label> 
								<input class="input_text" type="text" value="${userName}" name="userName" id="shopName" /> 
							</span> 
							<a id="sear_btn" class="btn btn_a" title="查 询" href="#" onclick="infoSubmit();">
								<span>查 询</span> 
							</a>

						</p>
						<p>
							<span></span> <span></span>
						</p>
					</form>
					<div class="pro_box clearfix">
						<form>
							<ul class="order-box">
								<li><span class="m-span"> 
										<label>秘钥串：</label> 
										<span id="m-span">${zebraPartern}</span> 
									</span> 
									<span class="m-span">
										<label>账号：</label> 
										<span id="un"> ${user.userName} </span> 
									</span> 
									<span class="m-span-last"> 
										<label>密码：</label> 
										<span id="up">${user.userPassword} </span> 
									</span>
								</li>
								<li class="order-btn">
									<a class="btn btn_a" title="生成秘钥" href="#" onclick="createCreateKey();"> 
										<span>生成秘钥</span> </a>
									 <a class="btn btn_a" title="生成账号/密码" href="#" onclick="createUserInfo()"> 
									 	<span>生成账号/密码</span> 
									 </a>
								</li>
							</ul>
						</form>
					</div>
				</div>
			</div>
		</div>

	</div>
<script type="text/javascript">
	window.onload = function() { 
		var userNullPar='<%=request.getAttribute("userNull")%>';
		var seraUserOrParternError='<%=request.getAttribute("seraUserOrParternError")%>';
		
		if(seraUserOrParternError){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : seraUserOrParternError
			});
		}
		if(userNullPar==""){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '商家代码不存在或未同步,请联系管理员！'
			});
		}
		};
		
		//提交商家信息
		function infoSubmit() {
			var isValidate = false;
			var form = $("#q_form");
			var $c = $("#sellerUserCode").val();
			var $u = $("#shopName").val();
			var codereg = /^[A-Za-z0-9]+$/;
			if (!$c) {
				//	alert("商家代码不能为空！");
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '请输入商家代码！'
				});
				isValidate == false;
				return isValidate;
			} else if (codereg.test($c)) {
				var loadingDialog = new Dialog();
				loadingDialog.init({
					contentHtml : '查询中，请稍后......'
				});
				form.submit();
			} else {
				isValidate == false;
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '商家代码格式错误,必须是字母和数字组成！'
				});
				//$("#codeMsg").css("color", "red").html("商家代码格式错误!");
				return isValidate;

			}
		}
		function createCreateKey(){
			var $uCode = $("#sellerUserCode").val();
			if(!$uCode){
				//弹窗提示
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '请输入商家代码！'
				});
				
			}else{
	         var loadingDialog = new Dialog();
	             loadingDialog.init({
	            yes: function() {
	                loadingDialog.close();
	                infoSubmitKey();	
	                
	             },
	            no:function () {
	            loadingDialog.close();
	            //$("#q1_form").submit();
	            },
	            contentHtml: '确定后商家的密钥将会重新生成!'
	        });
			}
	    }

		function infoSubmitKey() {
			
			var $uCode = $("#sellerUserCode").val();
			if (!$uCode) {
				//弹窗提示
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '请输入商家代码！'
				});
			} else {
				//弹窗提示
				var loadingDialogCreate = new Dialog();
				loadingDialogCreate.init({
					contentHtml : '密钥生成中，请稍后......'
				});
				
				$.post("createKeyt.action?menuFlag=sjzh_sjzh", 
				     {"sellerUserCode" : $uCode},
				     function(data) {
					if (data.createKeytError){
						loadingDialogCreate.close();
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml :createKeytError
						});
					}else if(data.zebraPartern == "") {
						//关闭弹窗
						loadingDialogCreate.close();
						
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : '生成密钥失败,请重试！'
						});
					} else if(data.userNull ==""){
						//关闭弹窗
						loadingDialogCreate.close();
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : '生成密钥失败,商家代码不存在或未同步!'
						});
					}else {
						//关闭弹窗
						loadingDialogCreate.close();
						
						$("#m-span").css("color", "red").html(data.zebraPartern);
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : '生成密钥成功,请牢记！'
						});
					}
				});
			}
		}

		function createUserInfo() {
			var $uCode = $("#sellerUserCode").val();
			if (!$uCode) {
				//		alert("商家代码不能为空！");
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '请输入商家代码！'
				});
			} else {
				//弹窗提示
				var loadingDialogCreate = new Dialog();
				loadingDialogCreate.init({
					contentHtml : '账户生成中，请稍后......'
				});
				$.post("pUpdate.action?menuFlag=sjzh_sjzh", {
					"sellerUserCode" : $uCode
				}, function(data) {
					if (data.createUserError){
						//关闭弹窗
						loadingDialogCreate.close();
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : data.createUserError
						});
					}else if(data.userName == "") {
						//关闭弹窗
						loadingDialogCreate.close();
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : '生成账户失败,请重试!'
						});
					} else if(data.userNull ==""){
						//关闭弹窗
						loadingDialogCreate.close();
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : '生成账户失败,商家代码不存在或未同步!'
						});
					}else if(data.aleradyUser){
						//关闭弹窗
						loadingDialogCreate.close();
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : data.aleradyUser
						});
					}else{
						$("#un").html();
						$("#up").html();
						$("#shopName").val();
						$("#un").css("color", "red").html(data.userName);
						$("#up").css("color", "red").html(data.showPwd);
						
						var flagMsg="";
						if(data.flag){
							flagMsg="账户信息已发送至网点邮箱！";
						}else{
							flagMsg="账户信息发送至网点邮箱不成功！";
						}
						
						//关闭弹窗
						loadingDialogCreate.close();
						
						var loadingDialog = new Dialog();
						loadingDialog.init({
							closeBtn : true,
							contentHtml : '新帐户已生成,'+flagMsg
						});
					}
				});
			}
		}
		
		
	function testMail(){
		$.post(
				"testMail.action?menuFlag=sjzh_sjzh",		
				function(data){
					if(data.info == true){
						alert("成功");
					}else if(data.info == false){
						alert("失败");
					}
				}
		);
	}
		//111111111111111111111111111111111
	</script>
</body>