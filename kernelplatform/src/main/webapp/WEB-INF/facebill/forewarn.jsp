<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<html lang="zh-CN">
<head>
    <%@ include file="/WEB-INF/common/meta.jsp" %>
    <%@ include file="/WEB-INF/common/taglibs.jsp"%>
    <script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/css/order-detail.css" />
    <!--------------当前页面js--------------->
<%--    <script type="text/javascript" src="${jsPath}/facebill/forewarn.js"></script> --%>
</head>

<script type="text/javascript">
window.onload = function() { 
	var phonePar='<%=request.getAttribute("phoneWarnMsg")%>';
	var emailPar='<%=request.getAttribute("emailWarnMsg")%>';
	var loadErrorMessage='<%=request.getAttribute("loadErrorMessage")%>';
	var loadForewarnMessage='<%=request.getAttribute("loadForewarnMessage")%>';
	var searchForewarnMessage='<%=request.getAttribute("searchForewarnMessage")%>';
	var insertForewarnMessage='<%=request.getAttribute("insertForewarnMessage")%>';
	var updateForewarnMessage='<%=request.getAttribute("updateForewarnMessage")%>';
	var saveSuccess='<%=request.getAttribute("saveSuccess")%>';
	var loadNullData='<%=request.getAttribute("loadNullData")%>';
	
	if(phonePar==""){
		$("#SMSAlerts").removeAttr("checked");
	}
	if(emailPar==""){
		$("#EmaiAlerts").removeAttr("checked");
	}
	
	if(loadErrorMessage){
		var loadingDialog = new Dialog();
        	loadingDialog.init({
        	closeBtn : true,
        	contentHtml: loadErrorMessage
        });
	}
	if(loadForewarnMessage){
		var loadingDialog = new Dialog();
       	 	loadingDialog.init({
        	closeBtn : true,
        	contentHtml: loadForewarnMessage
        });
	}
	if(searchForewarnMessage){
		var loadingDialog = new Dialog();
   	 		loadingDialog.init({
    		closeBtn : true,
    		contentHtml: searchForewarnMessage
    	});
	}
	if(insertForewarnMessage){
		var loadingDialog = new Dialog();
	 		loadingDialog.init({
			closeBtn : true,
			contentHtml: insertForewarnMessage
		});
	}
	if(updateForewarnMessage){
		var loadingDialog = new Dialog();
 			loadingDialog.init({
			closeBtn : true,
			contentHtml: updateForewarnMessage
		});
	}
	if(saveSuccess){
		var loadingDialog = new Dialog();
		loadingDialog.init({
		closeBtn : true,
		contentHtml: saveSuccess
	});
	}
	if(loadNullData){
		var loadingDialog = new Dialog();
		loadingDialog.init({
		closeBtn : true,
		contentHtml: loadNullData
	});
	}
	
	};
	
	
	$(function(){
		$("#save").click(function(){
			checkUserCode();
		});
		
		$("#user").change(function(){
			 search();
		});
		$("#sear_btn").click(function(){
			search();
		});
		
		$("#edit").click(function(){
			$("#branckWarnValue").removeAttr("disabled");
			$("#customerWarnValue").removeAttr("disabled");
			$("#phone").removeAttr("disabled");
			$("#phoneWarn").removeAttr("disabled");
			$("#remarkPhone").removeAttr("disabled");
			$("#email").removeAttr("disabled");
			$("#emailWarn").removeAttr("disabled");
			$("#SMSAlerts").removeAttr("disabled");
			$("#EmaiAlerts").removeAttr("disabled");
		});
		
		$("#branckWarnValue").blur(function(){
			var $a=$("#branckWarnValue").attr("value");
			var $cv=$("#customerWarnValue").attr("value");
			var h=/^\d{0,8}$/;//匹配数字
			if(!$a){
				$("#branckWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值不得为空!");
			}
			else if(!h.test($a)){
				$("#branckWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("网点采购预警值必须是8位以内正整数组成!");
				
			}
			else if(Number($a)==Number(0)){
				$("#branckWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值不得设为"+Number($a)+"值!");
			}
			else if(Number($a) < Number($cv)){
				$("#branckWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值不得小于商家号码池预警值!");
			}
			else if(Number($a) >= Number($cv) && h.test($a) && !(Number($a)==Number(0))){
				var $b=$("#customerWarnValue").attr("value");
				var h=/^\d{0,8}$/;//匹配数字
				$("#branckWarnValueMsg").empty().removeAttr("style");
				if($b && h.test($b)){
					$("#customerWarnValueMsg").empty().removeAttr("style");
				}
			}
			
		});
		
		$("#customerWarnValue").blur(function(){
			var $b=$("#customerWarnValue").attr("value");
			var $bv=$("#branckWarnValue").attr("value");
			var h=/^\d{0,8}$/;//匹配数字
			if(!$b){
				$("#customerWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("商家号码池预警值不得为空!");
			}
			else if(!h.test($b)){
				$("#customerWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("商家采购预警值必须是8位以内正整数组成!");
			}
			else if(Number($b) > Number($bv)){
				$("#customerWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("商家号码池预警值不得大于网点采购预警值!");
			}else if(Number($b)==Number(0)){
				$("#customerWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("商家号码缓冲值不得设为"+Number($b)+"值!");
			}
			else if(Number($b) <= Number($bv) && h.test($b) && !(Number($b)==Number(0))){
				var $a=$("#branckWarnValue").attr("value");
				var $h=/^\d{0,8}$/;//匹配数字
				$("#customerWarnValueMsg").empty().removeAttr("style");
				if($a && h.test($a)){
					$("#branckWarnValueMsg").empty().removeAttr("style");
				}
			}
			
		});

		$("#phone").blur(function(){
			var $c=$("#phone").attr("value");
			var g=/^1[3|4|5|8|9][0-9]\d{8}$/;//手机号
			if(!$c){
				$("#phoneMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("手机号码不得为空!");
			}else if(g.test($c)){
				$("#phoneMsg").empty().removeAttr("style");
			}else{
				$("#phoneMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("手机号码格式不正确(支持11位手机号)!");
			}
		});

		$("#remarkPhone").blur(function(){
			var $e=$("#remarkPhone").attr("value");
			var g=/^1[3|4|5|8|9][0-9]\d{8}$/;//手机号
			if(!$e){
				f4=true;
				$("#remarkPhoneMsg").empty().removeAttr("style");
			}else if(g.test($e)){
				f4=true;
				$("#remarkPhoneMsg").empty().removeAttr("style");
			}else{
				$("#remarkPhoneMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("备用手机号码格式不正确(支持11位手机号)!");
				f4=false;
			}
		});

		$("#email").blur(function(){
			var $f=$("#email").attr("value");
			var i=/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;//email地址
			if(!$f){
				$("#emailMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("邮箱不得为空!");
				f5=false;
			}else if(i.test($f)){
				f5=true;
				$("#emailMsg").empty().removeAttr("style");
			}else{
				$("#emailMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("邮箱格式不正确!");
				f5=false;
			}
		});
		
		
		
	});

	function checkUserCode(){
		var userCode=$("#user").val();
		var uc=true;
		if(!userCode){
			uc=false;
	         var loadingDialog = new Dialog();
	         loadingDialog.init({
	         closeBtn : true,
	         contentHtml: '商家代码不存在,请重新选择!'
	        });
		}else{
			checkForm();
		}
	}


	function search(){
		var userCode=$("#user").val();
		if(!userCode){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '商家代码不存在，请重新选择!'
			});		
		}else{
			var loadingDialog = new Dialog();
			loadingDialog.init({
				contentHtml : '加载中，请稍后......'
			});
			document.form.action = "search.action?userCode="+userCode+"&currentPage=1&menuFlag=dzmd_dzmd"; 
			$("#q_form").submit();
		}
		
		
	}


	function checkForm(){
		var $a=$("#branckWarnValue").attr("value");
		var $b=$("#customerWarnValue").attr("value");
		var $c=$("#phone").attr("value");
		var $d=$("#phoneWarn").is(':checked');	
		var $e=$("#remarkPhone").attr("value");
		var $f=$("#email").attr("value");
		var $g=$("#emailWarn").is(':checked');
		var $cv=$("#customerWarnValue").attr("value");
		var $bv=$("#branckWarnValue").attr("value");

		var h=/^\d{0,8}$/;//匹配数字
		var i=/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;//email地址
		var g=/^1[3|4|5|8|9][0-9]\d{8}$/;//手机号
		var f1=true;
		var f2=true;
		var f3=true;
		var f4=true;
		var f5=true;
		var f6=true;
		
		if(!$a){
			
			$("#branckWarnValueMsg").css(
					{"height":"23px",
					"border":"1px solid #E05C5C",
					"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
					"color":"#DF5151",
					"display":"inline-block",
					"padding":"0 10px 0 30px",
					"vertical-align":"top"}
					).html("网点采购预警值不得为空!");
			f1=false;
		}
	
		if(!h.test($a)){
			$("#branckWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值必须是8位以内正整数组成!");
			f1=false;
		}
		
		if(Number($a) < Number($cv)){
			$("#branckWarnValueMsg").css(
					{"height":"23px",
					"border":"1px solid #E05C5C",
					"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
					"color":"#DF5151",
					"display":"inline-block",
					"padding":"0 10px 0 30px",
					"vertical-align":"top"}
					).html("网点采购预警值不得小于商家号码池预警值!");
			f1=false;
		}
		if($a && Number($a)==Number(0)){
			$("#branckWarnValueMsg").css(
					{"height":"23px",
					"border":"1px solid #E05C5C",
					"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
					"color":"#DF5151",
					"display":"inline-block",
					"padding":"0 10px 0 30px",
					"vertical-align":"top"}
					).html("网点采购预警值不得设为"+Number($a)+"值!");
			f1=false;
		}
		if(Number($a) >= Number($cv) && h.test($a) && $a && !(Number($a)==Number(0))){
			var $b=$("#customerWarnValue").attr("value");
			var h=/^\d{0,8}$/;
			$("#branckWarnValueMsg").empty().removeAttr("style");
			f1=true;
			if($b && h.test($b)){
				$("#customerWarnValueMsg").empty().removeAttr("style");
				f2=true;
			}
		}
		
		if(!$b){
			$("#customerWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("商家号码池预警值不得为空!");
			f2=false;
		}
		
		if(!h.test($b)){
			$("#customerWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("商家采购预警值必须是8位以内正整数组成!");
			f2=false;
		}
		if(Number($b) > Number($bv)){
			$("#customerWarnValueMsg").css(
					{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("商家号码池预警值不得大于网点采购预警值!");
			f2=false;
		}
		if(Number($b) <= Number($bv) && h.test($b) && $b && !(Number($b)==Number(0))){
			var $a=$("#branckWarnValue").attr("value");
			var $h=/^\d{0,8}$/;//匹配数字
			$("#customerWarnValueMsg").empty().removeAttr("style");
			f2=true;
			if($a && h.test($a)){
				$("#branckWarnValueMsg").empty().removeAttr("style");
				f1=true;
			}
		}
		if($b && Number($b)==Number(0)){
			$("#customerWarnValueMsg").css(
					{"height":"23px",
					"border":"1px solid #E05C5C",
					"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
					"color":"#DF5151",
					"display":"inline-block",
					"padding":"0 10px 0 30px",
					"vertical-align":"top"}
					).html("商家号码缓冲值不得设为"+Number($b)+"值!");
			f2=false;
		}
		
		if(!$c){
			$("#phoneMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("手机号码不得为空!");
			f3=false;
		}else if(g.test($c)){
			f3=true;
			$("#phoneMsg").empty().removeAttr("style");
		}else{
			$("#phoneMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("手机号码格式不正确(支持11位手机号)!");
			f3=false;
		}
		if(!$e){
			f4=true;
			$("#remarkPhoneMsg").empty().removeAttr("style");
		}else if(g.test($e)){
			f4=true;
			$("#remarkPhoneMsg").empty().removeAttr("style");
		}else{
			$("#remarkPhoneMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("备用手机号码格式不正确(支持11位手机号)!");
			f4=false;
		}
		
		
		if(!$f){
			$("#emailMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("邮箱不得为空!");
			f5=false;
		}else if(i.test($f)){
			f5=true;
			$("#emailMsg").empty().removeAttr("style");
		}else{
			$("#emailMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("邮箱格式不正确!");
			f5=false;
		}
		
		if(f1&&f2&&f3&&f4&&f5){
			infoSubmit();
		}

	}
	function infoSubmit(){
			var uu=$("#userName1").val();
			var dd=$("#id1").val();
			var userCode=$("#user").val();
			var $action = "addForewarn.action?userCode="+userCode+"&userId="+dd+"&userName="+uu
																			+"&currentPage=1&menuFlag=dzmd_dzmd";
			$("#q1_form").attr("action",$action);
			
	         var loadingDialog = new Dialog();
	             loadingDialog.init({
	            yes: function() {
	                loadingDialog.close();
	                	
	                $("#q1_form").submit();
	             },
	            no:function () {
	            loadingDialog.close();
	            //$("#q1_form").submit();
	            },
	            contentHtml: '您确认要提交预警信息吗!'
	        });
//	     setTimeout(function () {
//	         loadingDialog.close();
//	         $("#q1_form").submit();
//	     }, 2000);
	}
	

	


</script>
<body>
<input id="userName1" type="hidden" value="${yto:getCookie('userName')}"/>
<input id="id1" type="hidden" value="${yto:getCookie('id')}"/>
<div id="content">
    <div class="clearfix" id="content_bd">
<!--     S Box -->
        <div id="box_form" class="box box_a">
            <div class="box_bd">
                <form class="form" method="post" id="q_form"  name="form" action="">
                    <p>
                        <span class="clearfix">
                            <label>商家代码：</label>
                            <select id="user">
                            	<s:iterator value="userList" id="user">
                            	     <s:if test="%{#user.userCode==selectedUserCode}">
        							    <option value='<s:property value="userCode"/>' selected="selected">
        							    
        							    	<s:if test="userName!=null">
       								 	 		<s:property value="userName"/>
       								 	 	</s:if>
       								 	 	<s:else>
       								 	 		名称暂无
       								 	 	</s:else>
       								 	 	
        							    &nbsp;&nbsp;(<s:property value="userCode"/>)</option>
    								 </s:if>
   									 <s:else>
       								 	 <option value='<s:property value="userCode"/>' >
       								 	 
       								 	 	<s:if test="userName!=null">
       								 	 		<s:property value="userName"/>
       								 	 	</s:if>
       								 	 	<s:else>
       								 	 		名称暂无
       								 	 	</s:else>
       								 	 	
       								 	 &nbsp;&nbsp;(<s:property value="userCode"/>)</option>
   									 </s:else>
                            	</s:iterator>
                            </select>
                        </span>
                        <a id="sear_btn" class="btn btn_a" title="查 询" >
                            <span>查 询</span>
                        </a>
                    </p>
                </form>
            </div>
        </div>
    </div>
    <div class="box box_a ">
        <div id="box_bd" class="box_bd">
            <div class="tab tab_c tab_d_hd">
                <div class="tab_triggers">
                    <ul>
                        <li class="tab_cur"><a id="tab1" href="javascript:;">商家面单号统计 </a></li>
                    </ul>
                </div>
                <div class="tab_panels">
                    <div id="tab_panel_a" class="tab_panel clearfix">
                        <div class="pro_box clearfix">
                            <ul class="order-bar">
                                <li>
                                    <label>已用面单号总数:</label>
                                    <span style="font:700 16px/1.4 Arial;color:#61B610;text-decoration: underline;font-weight: bold;" id="alreadyFaceNum">${alreadyFaceNum}</span>
                                </li>
                                <li>
                                    <label>剩余面单号总数:</label>
                                    <span style="font:700 16px/1.4 Arial;color:#61B610;text-decoration: underline;font-weight: bold;">${surplusFaceNum }</span>
                                </li>
                                <li>
                                    <label>商家面单号总数:</label>
                                    <span style="font:700 16px/1.4 Arial;color:#1E579E;text-decoration: underline;font-weight: bold;">${toteFaceNum }</span>
                                </li>
                            </ul>
                            <ul class="order-bar">
                                <li>
                                    <label>面单号回收总数:</label>
                                    <span class="red" style="font-weight: bold;">已关闭</span>
                                </li>
                                <li>
                                    <label>网点采购值报警:</label>
                                    <span class="red" style="font-weight: bold;">${warning }</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="box_bd order-hd">
            <div class="tab tab_c tab_d_hd">
                <div class="tab_panels">
                    <div class="tab_panel clearfix">
                        <div class="pro_box clearfix">
                            <form action="" method="post" id="q1_form">
                                   <ul class="order-box">
                                    <li>
                                        <span class="order-pro">
                                            <label>网点采购预警值&nbsp;<i class="red">*</i>&nbsp;:</label>
                                            <span><input id="branckWarnValue" class="input_text" type="text"
                                            value="${forewarn.branckWarnValue}" name="branckWarnValue" disabled="true"></span>

                                        </span>

                                        <span>
                                            <label>温馨提示:</label>
                                            <span class="red">为确保紧急报警信息及时查收，建议您填写2个手机号码</span>
                                        </span>
                                    </li>
                                    <li>
                                    	<span id="branckWarnValueMsg"></span>
                                    </li>
                                    <li>
                                        <span class="order-pro">
                                            <label>商家号码缓冲值<i class="red">*</i>：</label>
                                            <span><input id="customerWarnValue" class="input_text" type="text"
                                            value="${forewarn.customerWarnValue}" name="customerWarnValue" disabled="true" id="customerWarnValue"/></span>

                                        </span>

                                        <span  class="order-tip">
                                           <input id="SMSAlerts" type="checkbox" name="phoneWarnMsg" checked="checked" value="SMSAlerts" disabled="true"/>
                                            <span>短信提醒</span>
                                        </span>
                                        <span>
                                            <label>手机号码<i class="red">*</i>：</label>
                                            <span><input id="phone" class="input_text" type="text" value="${forewarn.phone}" name="phone" disabled="true"/></span>
                                        </span>

                                         <span>
                                            <label>备用号码：</label>
                                            <span><input id="remarkPhone" class="input_text default_status" type="text" value="${forewarn.remarkPhone}" name="remarkPhone" disabled="true"/></span>

                                        </span>

                                    </li>
                                    <li>
                                    	<span id="customerWarnValueMsg"></span>
                                    	<span id="phoneMsg"></span>
                                    	<span id="remarkPhoneMsg"></span>
                                    </li>
                                    <li>
                                        <span class="order-pro">
                                            <span class="red">该值表示分配给商家使用的面单号预警值</span>
                                        </span>
                                        <span class="order-tip">
                                           <input id="EmaiAlerts" type="checkbox" name="emailWarnMsg" checked="checked" value="EmaiAlerts" disabled="true"/>
        									<span>邮件提醒</span>
                                        </span>
                                        <span>
                                            <label>邮箱地址<i class="red">*</i>：</label>
                                            <span><input id="email" class="input_text" type="text" value="${forewarn.email}" name="email" disabled="true"/></span>
                                        </span>
                                    </li>
                                    <li>
                                    	<span id="emailMsg"></span>
                                    </li>
                                    <li class="order-btn">
                                        <a class="btn btn_a" title="编 辑" id="edit">
                                            <span>编 辑</span>
                                        </a>
                                        <a class="btn btn_a" title="保 存" id="save">
                                            <span>保 存</span>
                                        </a>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>


