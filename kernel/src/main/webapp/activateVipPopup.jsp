<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/myyto.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<script type="text/javascript">
var acitveParLayer;
var activeLayer2;
var ucode="";
var chkcode_falg = false;
function chkcode(){
	var codeVal = $.trim($("#act_userCode").val());
	if (codeVal == '') {
		$("#act_userCodeTip").removeClass('onCorrect').addClass("onError");
		$("#act_userCodeTip").html("客户编码不能为空");
	} else {
		$("#branchinfo").val("");
		$.ajax({
			datatype : "json",
	    	async : false,
	    	cache: false,
	    	url : "user!checkNewUserCode.action?newUserCode="+codeVal,
			success : function(response){
				if(response.status){
					ucode=$.trim($("#act_userCode").val());
					$("#branchinfo").val(response.infoContent);
					$("#act_userCodeTip").removeClass('onError').addClass('onCorrect');
					$("#act_userCodeTip").html(" ");
					chkcode_falg = true;
				}
				else{
					$("#act_userCodeTip").removeClass('onCorrect').addClass("onError");
					$("#act_userCodeTip").html("客户编码格式错误");
					chkcode_falg = false;
				}
			}
		});
	}
}
function submitUserCode(){
	 var content = '为了确定您的信息安全，请确认你的服务网点是否为“'+$('#branchinfo').val()+'”！' + 
					'<span class="dialog_ft">' +
					'	<a href="javascript:;" class="btn btn_d" id="submit_yes" title="确定"><span>确定</span></a> ' +
					'	<a href="javascript:;" class="btn btn_e" id="submit_no" title="取消"><span>取消</span></a>' +
					'</span>';
	 
	 var ok = function(){

            };
	 var cancel = function() {

		};
		dialog.close();
		dialog.init({
			//yesVal: '确定',
			//noVal: '取消',
			contentHtml: content,
			maskOpacity: 0
			//yes: ok,
			//no: cancel
		});
 }
 
function firstStep(){
	var oDialog = new Dialog();
	oDialog.init({
		closeBtn: true,
		maskOpacity: 0,					// 遮罩层的透明度
		contentHtml:acitveParLayer
	});
	$("#active_parLayer").html('');
} 
function secondStep() {
	dialog.close();
	// 新弹窗
	dialog.init({
// 		yesVal: '确定',
// 		noVal: '取消',
// 		yes: function() {
//			usercode验证通过
// 			chkcode();
// 			if(chkcode_falg){
// 				submitUserCode();
// 			}
// 		},
// 		no: function() {
// 			dialog.close();
// 		},
		maskOpacity: 0,
		contentHtml:activeLayer2
	});
 	$("#active_parLayer2").html('');
};	

$(function(){
	$('#dialog_yes').live('click', function() {
			chkcode();
			if(chkcode_falg){
				submitUserCode();
			}
	});
	
	$('#dialog_no').live('click', function() {
		$('#bind_wd').replaceWith("<input type=\"button\" value=\"绑定网点\" id=\"guide\" class=\"button_02\" style=\"\">");
		dialog.close();
	});
	
	$('#submit_yes').live('click', function() {
                $.ajax({
					type:"post",
					datatype:"json",
					cache: false,
					url: "noint!bindTaoBaoUserStep2.action?user.userCode="+ucode,
					success:function(data) {
						if(data == "true") {
							dialog.close();
							
							dialog.init({
								yesVal: '确定',
								contentHtml: '绑定成功!',
								yes: function() {
									window.location.reload();
								},
								maskOpacity: 0
							});
						}
						else {
							dialog.init({
								contentHtml: '<p style="line-height:1.5">绑定失败! 请通知管理员!</p>'+
											'<p style="line-height:1.5">联系电话：021-64703131-107</p>'+
											'<p style="line-height:1.5">旺旺交流群：642218871</p>'+
											'<p style="line-height:1.5">QQ交流群：173184824</p>',
								yes: function() {
									oDialog.close();
								},
								closeBtn: true
							});
						}
					}
				});
                
               return false;
	});
	
	$('#submit_no').live('click', function() {
		 		dialog.close();
				dialog.init({
					maskOpacity: 0,
					contentHtml: activeLayer2
					
				});
            	$('#act_userCode').val("");
	});
	
	
	$('#active_form_next').live('click', function() {
				$("#addressActVip").blur();
		
				// 验证
				if(!checkFirstStep()) {
					return false;
				}
				var data="user.userName="+$("#act_un").val()+
							"&user.userPassword="+$("#act_psw").val()+
							"&user.shopName="+$("#act_shop_name").val()+
							"&user.mobilePhone="+$("#act_mobile_tel").val()+
							"&user.addressProvince="+$("#act_x_p").val()+
							"&user.addressCity="+$("#act_x_c").val()+
							"&user.addressDistrict="+$("#act_x_d").val()+
							"&user.addressStreet="+$("#act_x_s").val()+
							"&user.field001="+$("#act_x_field").val()+
							"&user.id="+$("#act_id").val();
							
				$.ajax({
					type : "post",
					dataType : "json",
					data : data,
					cache: false,
					url : 'noint!beforerBindTaoBaoUserStep1.action',
						success:function(response){
							var status=response.isValidate;
							if(status == "true"){
								secondStep();
							}else{
								dialog.close();
								
							}
					}
				});
	 });
	// 点击“开始使用”
	$('#bind_wd').live('click', function() {
		firstStep();
	});
	$('#guide').live('click', function() {
		secondStep();
	});
	
	// 缓存弹窗内容
	acitveParLayer = $("#active_parLayer").html();
	activeLayer2 = $("#active_parLayer2").html();

});
$('#act_psw').live("blur",function() {	
	checkPsw();
});	
$('#act_repsw').live("blur",function() {	
	checkRepsw();
});	

$('#act_shop_name').live("blur",function() {	
	checkShopName();
});

$('#act_mobile_tel').live("blur",function() {	
	checkMob();
});
$('#addressActVip').live("blur", function() {
	//获取regionsActVip页面的省市数据并赋值给本页面的隐藏域
	var x = $("#provinceActVip").val();
    var x_c=$("#cityActVip").val();
    var x_d=$("#countryActVip").val();
    var x_s=$("#addressActVip").val();
    $("input[name='user.field001']").val(x);
 	setHiddenValue(x,"provinceActVip","act_x_p");
 	setHiddenValue(x_c,"cityActVip","act_x_c");
 	setHiddenValue(x_d,"countryActVip","act_x_d");
 	$("#act_x_s").val(x_s);
 	checkAddress();
	
});
function checkPsw(){
	var reg = /\w{6,20}/;
	if (reg.test($("#act_psw").val())) {
		$('#act_psw_check').removeClass('check_error').addClass('check_correct').html('密码格式正确');
		return true;
	}else{
		$('#act_psw_check').removeClass('check_correct').addClass('check_error').html('密码格式错误');
		return false;
	}
}

function checkRepsw(){
	if ($("#act_repsw").val() != $('#act_psw').val() || $("#act_repsw").val() == '') {
		$('#act_repsw_check').removeClass('check_correct').addClass('check_error').html('两次密码输入不一致');
		return false;
	} else {
		$('#act_repsw_check').removeClass('check_error').addClass('check_correct').html('确认密码正确');
		return true;
	}
}
	
function checkShopName(){
	if ($("#act_shop_name").val() == '') {
		$('#act_shop_name_check').removeClass('check_correct').addClass('check_error').html('店铺名称格式错误');
		return false;
	} else {
		$('#act_shop_name_check').removeClass('check_error').addClass('check_correct').html('店铺名称格式正确');
		return true;
	}
}
		
function checkMob(){ 
	var reg = /^(13|14|15|18)[0-9]{9}$/;
	if (reg.test($("#act_mobile_tel").val())) {
		$('#act_mobile_tel_check').removeClass('check_error').addClass('check_correct').html('手机号码格式正确');
		return true;
	} else {
		$('#act_mobile_tel_check').removeClass('check_correct').addClass('check_error').html('手机号码格式错误');
		return false;
	}
}	

   
function checkAddress(){
	if($("#provinceActVip").val()=='' || $("#act_x_p").val()=='请选择省份'){
		$('#act_addressTip').removeClass('check_correct').addClass('check_error').html('请选择省份');
		return false;
	}
	else if($("#cityActVip").val()=='' || $("#act_x_c").val()=='请选择所在市'){
		$('#act_addressTip').removeClass('check_correct').addClass('check_error').html('请选择所在市');
		return false;
	}
	else if($("#countryActVip").css("display") != 'none' && ($("#countryActVip").val()=='' || $("#act_x_d").val()=='请选择所在县区')){
		$('#act_addressTip').removeClass('check_correct').addClass('check_error').html('请选择所在县区');
		return false;
	}
	else if($("#addressActVip").val()=='' || $("#act_x_s").val()==''){
		$('#act_addressTip').removeClass('check_correct').addClass('check_error').html('请填写街道地址');
		return false;
	}
	else {
		$('#act_addressTip').removeClass('check_error').addClass('check_correct').html('地址输入正确');
		return true;
	}
	
}

function setHiddenValue(selectValue,selectId,hiddenId){
	var selectText = $("#"+selectId).find("option:selected").text();
	$("#"+hiddenId).val(selectText);
}
		
function checkFirstStep(){
	if(checkPsw() && checkRepsw() && checkShopName() && checkMob() && checkAddress()){
		return true;
	}
	return false;
}
//验证用户编码
$(document).ready(function(){
	$.formValidator.initConfig({validatorgroup:"99",formid:"guideVipForm",debug:false,submitonce:false,
		onerror:function(msg,obj,errorlist){
			var oDialog = new Dialog();
			oDialog.init({
				contentHtml: msg,
				yes: function() {
					oDialog.close();
				},
				closeBtn: true
			});
		},
		submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
	});
	 $("#act_userCode").formValidator({validatorgroup:"99",onshow:"请输入客户编码",onfocus:"请输入您的客户编码",oncorrect:" "})
	 .inputValidator({min:1,onerror:"客户编码长度错误"});
	
	 $("#act_userCode").live("blur",function(){
		chkcode();
	});
	
	<%
	String actionMethod = request.getAttribute("javax.servlet.forward.servlet_path").toString();
	if(null!=actionMethod  && !"".equals(actionMethod.trim())){
	    int subIndex=-1;
	    if(actionMethod.indexOf('!')>1){
	        subIndex=actionMethod.indexOf('!');
	    }
	    if(actionMethod.indexOf('_')>1){
	        subIndex=actionMethod.indexOf('_');
	    }
	    if(subIndex>1){
		    actionMethod=actionMethod.substring(1,subIndex);
	    }
	}
	%>
	
	var userType="${yto:getCookie('userType')}";
	var userState="${yto:getCookie('userState')}";
	var userField003="${yto:getCookie('field003')}"; 
	var userName=$("#act_un").val();
	var infostate="${yto:getCookie('infostate')}"; 
	//不同的左侧菜单所需进行的验证不同
	//1.完善所有信息
	function fullValidate(){
		if(userType == 1 && userState=="TBA"){
			 if(userField003 !=9 || infostate !=1){
				firstStep();
			 }else if(infostate==1){
				secondStep();
			 }
		}
	}
// 	function fullValidate(){
// 			 if(infostate ==1){
// 				secondStep();
// 			 }else{
// 				firstStep();
// 			 }
// 	}
	//2.在完善信息后即可
	function perfValidate(){
		if(userType == 1 && userState=="TBA"){
			 if(userField003 != 9 || infostate !=1){
				 firstStep();
			 } 
		}
	}
// 	function perfValidate(){
// 		 if(infostate == ''){
// 			 firstStep();
// 		 } 
// 	}
	var aname="<%=actionMethod %>";
	//必须为卖家角色
	if(userType==1){
		//问题件管理”，“电子对账”，“发新消息”  必须完成所有步骤
		if(aname == "questionnaire" || aname == "order" || aname == "sendMessage") {
			fullValidate();
		}
		//如果是卖家首页，直接放行
		else if(aname == "home"){
		}
		//其他的必须完善信息后方可放行
		else{
			perfValidate();
		}	
	}
});



</script>
<div id="active_parLayer" style="display:none">
	<!-- S 完善基本信息 -->
	<div id="active_first">
		<div id="process">
			<ol>
				<li id="process_cur">1、完善基本信息</li>
				<li>2、绑定网点（可选）</li>
				<li id="process_last">3、开始使用易通</li>
			</ol>
		</div>
		<div id="statement">
			<ol>
				<li>1、圆通电商客户专享；</li>
				<li>2、目前仅开放给淘宝卖家和B2C电商客户。</li>
			</ol>
		</div>
		<form action=" config.infoFormAction  '" id="info_form" class="form">
			<fieldset>
				<legend>基本信息</legend>
	           <input type="hidden" name="user.id" id="act_id" value="'config.userId'" />'
	           <input type="hidden" name="user.userName" id="act_un" value="'config.userName'" />'
				<p>
					<label for="act_shop_name"><span class="req">*</span>店铺名称：</label>
					<input type="text" class="input_text" id="act_shop_name" name="user.shopName" />
					<span id="act_shop_nameTip"></span>
				</p>
				<p>
					<label for="act_mobile_tel"><span class="req">*</span>手机号码：</label>
					<input type="text" class="input_text" id="act_mobile_tel" name="user.mobilePhone" />
					<span id="act_mobile_telTip" class="check_result"></span>
				</p>
				<p>
					<label for="province"><span class="req">*</span>公司地址：</label>
					<select id="province"></select>
					<span id="area_tip"></span>
				</p>
				<p>
					<textarea cols="51" rows="2" class="textarea_text" id="detail_address" name="user.addressStreet"></textarea>
					<span id="detail_addressTip"></span>
				</p>
			</fieldset>
		</form>
	</div>
	<!-- E 完善基本信息 -->
	
	<!-- S 绑定客户编码 -->
	<div id="active_first">
		<div id="process">
			<ol>
				<li id="process_first">1、完善基本信息</li>
				<li id="process_cur">2、绑定网点（可选）</li>
				<li id="process_last">3、开始使用易通</li>
			</ol>
		</div>
		<div id="statement">
			<p>为了更好的使用易通服务，请先跟您的圆通网点绑定^_^~</p>
		</div>
		<form action=" config.bindFormAction + '" id="bind_form" class="form">
			<p>
				<label for="customer_code">客户编码：</label>
				<input type="text" id="customer_code" name="user.userCode" class="input_text" />
				<span id="customer_codeTip"></span>
			</p>
			<p>如果没有客户编码，请联系当地网点获取。</p>
		</form>
	</div>
	<!-- E 绑定客户编码 -->

</div>
<input type="hidden" name="user.id" id="act_id" value="${yto:getCookie('id')}" />
<input type="hidden" name="user.userName" id="act_un" value="${yto:getCookie('userName')}" />
<input type="hidden" id='branchinfo'> 