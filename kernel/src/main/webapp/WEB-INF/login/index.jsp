<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="Shortcut Icon" href="images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/jquery-easyui/easyui.css?d=${str:getVersion() }">
<link rel="stylesheet" href="css/layout.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
<link href="artDialog/skins/blue.css" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
<script type="text/javascript">
$(function(){
	var flag = $("#flag").val();
	var articleId = $("#articleId").val();
	$("#side").attr("src", "mainPage_left.action");
	$("#main").attr("src", "home_home.action?flag="+flag+"&articleId="+articleId);
	if($("#default_unReadQuestionNum").val()==0 && $("#default_unReadMessageNum").val()==0){
		$("#noneReadDiv").attr("style","display:none");
	}
});
function logout(){
	artDialog.confirm("您确定要退出系统吗？",function() {top.location = "login_loginOut.action";});
}
function getUnReadMsgNum(){
	showNoneReadDiv();
}
function getUnReadQuestionNum(){
	showNoneReadDiv();
}
function reinitIframe(){
	var iframe = document.getElementById("main");
	try{
		iframe.height =  iframe.contentWindow.document.documentElement.scrollHeight;
		iframe.height = Math.max(iframe.height, 650);
	}catch (ex){}
}
function goModule(url,id){
	main.location=url;
	side.layerLight(id);
}

window.setInterval("reinitIframe()", 200);

function closeNoneReadDiv(){
	$("#noneReadDiv").attr("style","display:none");
	/**
	 * 监听显示未读信息弹出层事件。如果弹出层关闭了，5分钟后自动弹出
	 */
	window.setInterval("showNoneReadDiv()", 5000*60);
}
function showNoneReadDiv(){
	$.ajax({
		url : 'mainPage_getUnReadNum.action',
		cache:false,
		success : function(data){
			$("#unReadMessageNum").html(data.split(",")[0]);
			$("#unReadQuestionNum").html(data.split(",")[1]);
			$("#noneReadDiv").attr("style","display:block");
			if(data.split(",")[0]==0){
				$("#unReadMessageNum_p").attr("style","display:none");
			}else{
				$("#unReadMessageNum_p").attr("style","display:block");
			}
			if(data.split(",")[1]==0){
				$("#unReadQuestionNum_p").attr("style","display:none");
			}else{
				$("#unReadQuestionNum_p").attr("style","display:block");
			}
			if(data.split(",")[0]==0 && data.split(",")[1]==0){
				$("#noneReadDiv").attr("style","display:none");
			}else{
				$("#noneReadDiv").attr("style","display:block");
			}
		}
	});
}
</script>
<script type="text/javascript"> 
$(document).ready(function(){
	var unReadNum = $.trim($('#unReadQuestionNum').html());
	if (unReadNum.length == 6) {
		var unReadNumWan = unReadNum.substr(0, 2);
		$('#unReadQuestionNum').html(unReadNumWan + '万')
	}
	
	$("ul.siderightnav li").hover(function() {
		$(this).find("div").stop()
		.animate({left: "-280", opacity:1}, "fast")
		.css("display","block")

	}, function() {
		$(this).find("div").stop()
		.animate({left: "0", opacity: 0}, "fast")
	});
	
});
function queryWaybill(){
    var userType="${yto:getCookie('userType')}";
 	var userState="${yto:getCookie('userState')}";
 	var userField003="${yto:getCookie('field003')}"; 
 	
	if(userType==1 && userState == 'TBA'){
		parent.main.location="waybill_homeQuery.action";
	}else{
		var logisticsIds = $.trim($("#logisticsIds").val());
	    if(logisticsIds==''){
	    	art.dialog.alert("请输入查询条件");
	    	$("#logisticsIds").focus();
	    	return;
	    }
	    if(isLogisticsId(logisticsIds)||logisticsIds.indexOf('/')>0){
//	    	art.dialog.alert("dd");
	    	parent.main.location="waybill_homeQuery.action?currentPage=1&logisticsIds="+logisticsIds+"&isCheck=0&flag=1";
	    }
		else{
			var buyerName='';
			var buyerPhone='';
			if(isDigit(logisticsIds))buyerPhone=logisticsIds;
			else buyerName=logisticsIds;
			parent.main.location="waybill_homeQuery.action?currentPage=1&logisticsIds="+logisticsIds+"&isCheck=0&flag=3&homeFlag=1&buyerName="+buyerName+"&buyerPhone="+buyerPhone;
		}
	}
	
	

    parent.side.layerLight("waybill_li");
}
function isLogisticsId(s){
	var patrn=/^(0|1|2|3|4|5|6|7|8|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/;
	if (!patrn.exec(s)) return false
	return true
}
function isDigit(s){
	 var patrn=/^[0-9]{1,20}$/;
	 if (!patrn.exec(s)) return false
	 return true
}
function openMessage(){
	parent.main.location = "sendMessage_openAdviseUI.action";
	parent.side.layerLight("advise_li");
}

function addFavorite(){if(document.all){try{window.external.addFavorite(window.location.href,document.title)}catch(e){alert("加入收藏失败，请使用 Ctrl+D 进行添加")}}else if(window.sidebar){window.sidebar.addPanel(document.title,window.location.href,"")}else{alert("加入收藏失败，请使用 Ctrl+D 进行添加")}}
</script>


<title>易通电子商务物流信息平台</title>
<meta name="description" content="做电商的朋友们，你们的快递物流投诉多吗，遇到问题件不明不白，手工对账又还很麻烦，海量运单依然石沉大海？快来使用易通吧，这些问题统统都可以解决了，还有更多好处等着你哦.http://www.ytoxl.com/index-logistics.htm" />
</head>
<body >
<div id="linebox"> <ul class="siderightnav">
    <li><a class="show">查件<img src="images/ico_013.png"></a>
      <div style="padding-top:0px;height:43px;">
        <p ><input id="logisticsIds" style="padding:4px;border:1px solid #a0b3d6;color:#333;margin-right:2px;" type="text" /><input name="button" type="button" class="submit01"  value="查&nbsp;询 " onClick="queryWaybill()"/><br><span style="color:#b3b3b3;">输入运单号/手机号码/买家姓名</span></p>
      </div>
    </li>
    <li><a class="show">客服<img src="images/ico_012.png"></a>
      <div style="margin-left:70px;">
<!--       http://www.taobao.com/webww/ww.php?ver=3&touid=ytoyitong&siteid=cntaobao&status=1&charset=utf-8 -->
        <p style="padding-top:0px;">
        	<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=ytoyitong&site=cnalichn&s=5" ><img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=ytoyitong&site=cnalichn&s=1" alt="点击这里给我发消息" /></a> 
        	<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2294882345&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:2294882345:41 &r=0.658135694570411" alt="点击这里给我发消息" title="点击这里给我发消息"></a></p>
      </div>
    </li>
    <!--<li><a class="show">分享<img src="images/ico_010.png"></a>
      <div class="bshare-custom">
				<a title="分享到新浪微博" class="bshare-sinaminiblog" href="javascript:void(0);"></a>
				<a title="分享到QQ空间" class="bshare-qzone" href="javascript:void(0);"></a>
				<a title="分享到人人网" class="bshare-renren" href="javascript:void(0);"></a>
				<a title="分享到腾讯微博" class="bshare-qqmb" href="javascript:void(0);"></a>
				<a title="分享到开心网" class="bshare-kaixin001" href="javascript:void(0);"></a>
				<a title="分享到淘江湖" class="bshare-taojianghu" href="javascript:void(0);"></a>
			</div>
			<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=1&amp;lang=zh"></script>
			<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC0.js?d=${str:getVersion() }"></script>
    
    
    </li>
    -->
    <li>
	<a class="show" href="javascript:openMessage();">反馈<img src="images/ico_011.png"></a>
    </li>
   
  </ul></div>
<input type="hidden" id="flag" value="<s:property value='flag'/>"/>
<input type="hidden" id="articleId" value="<s:property value='articleId'/>"/>
<div id="container">
	<div id="header" style="position:relative;">
		
		<a class="fl" href="mainPage_welcome.action" style="width:185px;height:65px;display:block;margin:12px 0 0 23px;"></a>
		
		<div style="color:#fff;float:right;margin-right:10px ">
		<p style="text-align:right;float:right;line-height:46px;*margin-left:168px;">
		<a  onclick="window.open('mainPage_toHelp.action')" >帮助中心</a>&nbsp;&nbsp; 
		<a onClick="window.open('jiaocai')">帮助视频</a>&nbsp;&nbsp;
		<a onClick="addFavorite()">收藏易通</a>&nbsp;&nbsp;
		<a href="javascript:logout()">退出</a></p>
		<p style="text-align:right;float:right;clear:both;"><span class="pagetop">您好！欢迎&nbsp;
		<s:if test="user.userNameText != null && user.userNameText != ''">
			<s:property value="user.userNameText"/>
		</s:if>
		<s:else>
			<s:property value="user.userName"/>
		</s:else>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:showNoneReadDiv()">消息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><a href="javascript:goModule('user!toEdit.action?user.id=<s:property value="user.id" />','user_li')" >我的账号</a></p>
		
		<div id="noneReadDiv" class="notification png_bg"> <a href="javascript:closeNoneReadDiv()" class="close"><img src="images/close.png" title="关闭" alt="关闭" style="position:relative; top:18px; left:152px;"/></a>
	      <div id="tipDiv" style="text-align:center; margin-top:21px;vertical-align:middle;">
	      	<c:if test="${yto:getCookie('userType') == 1
        		|| yto:getCookie('userType') == 11
        		|| yto:getCookie('userType') == 13
        		|| yto:getCookie('userType') == 2
        		|| yto:getCookie('userType') == 21
        		|| yto:getCookie('userType') == 23}">
	      		<p id="unReadQuestionNum_p" <s:if test="unReadQuestionNum==0">style="display:none"</s:if>><span style="text-decoration: none;"><label id="unReadQuestionNum"><s:property value="unReadQuestionNum"/></label>条问题件信息未读</span> &nbsp;<a style="color:#5554bb;" href="javascript:goModule('questionnaire_unReadList.action?isRead=0&currentPage=1','ques_li')">查看</a></p>
	        </c:if>
	        <p id="unReadMessageNum_p" <s:if test="unReadMessageNum==0">style="display:none"</s:if>><span style="text-decoration: none;"><label id="unReadMessageNum"><s:property value='unReadMessageNum'/></label>条收件箱信息未读</span> &nbsp;<a style="color:#5554bb;" href="javascript:goModule('leaveMessage_index.action','msg_li')">查看</a></p>
	        
	      </div>
    	</div>
    	<input type="hidden" id="default_unReadQuestionNum" value="<s:property value='unReadQuestionNum'/>">
    	<input type="hidden" id="default_unReadMessageNum" value="<s:property value='unReadMessageNum'/>">
		 
		</div>
	</div>
	
	<div id="mainntent" style="background-color:#E8F3FA">
		<frameset cols="128,100%" id="frame" style="backgroung:#ccc">
			<iframe allowTransparency="true" id="side"  src="" name="side" noresize="noresize"  scrolling="no" marginwidth="0" marginheight="0" frameborder="0" target="main"></iframe>
 			<iframe allowTransparency="true" id="main" src="" style="background-color:transparency" name="main" marginwidth="0" scrolling="no" marginheight="0" frameborder="0" target="_self" onload="this.height=300" style=></iframe>
		</frameset>	
	</div>
	<div class="clearfloat"></div>
	<div id="footer" align="center">
		<p>

圆通速递公司总部：上海青浦区华新镇华徐公路3029弄28号 邮政编码：201705 Copyright © 2000-2012 All Right Reserved 沪ICP备05004632号 

		</p>
    
</div>
    <c:if test="${yto:getCookie('userType') == 1
        		|| yto:getCookie('userType') == 11
        		|| yto:getCookie('userType') == 12
        		|| yto:getCookie('userType') == 13
        		|| yto:getCookie('userType') == 41
     	|| yto:getCookie('userType') == 2 
   		|| yto:getCookie('userType') == 21
   		|| yto:getCookie('userType') == 22
   		|| yto:getCookie('userType') == 23}">
<style type="text/css">
	#guide_layer{display:none;background:#000;width:100%;height:100%;position:absolute;top:0;left:0; *filter:alpha(opacity=80);opacity:0.8;-moz-opacity:0.8;filter:alpha(opacity=80);}
	#guide_box{margin:0 auto;width:1000px;position:relative;}
	#guide_seller{background:url(images/single/pseller.png) no-repeat;width:650px;height:200px;left:175px;top:200px;position:absolute;}
	#guide_seller a{position:absolute;width:250px;height:42px;display:block;bottom:15px;right:0;overflow:hidden;text-indent:-9999em;}
	#guide_branch{background:url(images/single/pbranch.png) no-repeat;width:660px;height:208px;left:175px;top:200px;position:absolute;}
	#guide_branch a{position:absolute;width:307px;height:42px;display:block;bottom:20px;right:0;overflow:hidden;text-indent:-9999em;}
	.layer_close{position:absolute;top:120px;right:0;width:24px;height:24px;overflow:hidden;background:url(images/single/layer_close.png) no-repeat;text-indent:-9999em;}
	</style>
	<script type="text/javascript">
	$(function() {
		
		$.ajax({			
			url: 'user!printNav.action',
			dataType: 'json',
			success: function(data) {
				var result = data.showLayer;
				if (result == '1') {	// 要弹层 
					$('#guide_layer').show('fast', function() {
						
						$('#guide_layer').css({
							height: $('body').height() + 104
						});
						
						var markPrintLayer = function() {
							$.ajax({
								url: 'user!printClose.action' 
							})
						};
						
						
						// 卖家
						$('#guide_seller a').click(function(ev) {
							ev.preventDefault();
							$('#guide_layer').remove();
							markPrintLayer();
							goModule('orderPrint!orderPrint.action','li_orderprint');
						});
						
						
						// 网点
						$('#guide_branch a').click(function(ev) {
							ev.preventDefault();
							artDialog({
								opacity: 0,
								id: 'Alert',
								//icon: 'warning',
								fixed: true,
								top:'320px',
								//lock: true,
								content: '已发邮件邀请咯~',
								ok: function() {
									$('#guide_layer').remove();
									markPrintLayer();
									$.ajax({
										url: 'user!sendEmail.action' 
									})
									goModule('orderPrint!orderPrint.action','li_orderprint');
								}
							})
						});
						
						
						$('.layer_close').click(function(ev) {		
							ev.preventDefault();
							markPrintLayer();
							// goModule('home_home.action','home_li');
							$('#guide_layer').remove();
						});	
						});
				}
			}
		})
	});
	</script>
	
	<c:if test="${yto:getCookie('userType') == 1 
       		|| yto:getCookie('userType') == 11 
       		|| yto:getCookie('userType') == 12 
        		|| yto:getCookie('userType') == 13 
        		|| yto:getCookie('userType') == 41}"> 
        <c:if test="${(yto:getCookie('userState') == 'TBA' && yto:getCookie('infostate') != 1) || yto:getCookie('userState') == '1'}">
        	<div id="guide_layer">
				<div id="guide_box">
					<a href="javascript:;" class="layer_close">X</a>
					<div id="guide_seller" class="toPrintPage">
						<a href="javascript:;">Go</a>
					</div>
				</div>
			</div>	
		</c:if>	
	</c:if>
	

	<c:if test="${yto:getCookie('userType') == 2 
        		|| yto:getCookie('userType') == 21 
        		|| yto:getCookie('userType') == 22 
        		|| yto:getCookie('userType') == 23}"> 
        <c:if test="${yto:getCookie('userState') == '1'}">
        	<div id="guide_layer">
			<div id="guide_box">
 	        	<a href="javascript:;" class="layer_close">X</a>
				<div id="guide_branch" class="toPrintPage">
					<a href="javascript:;">Go</a>
				</div>
			</div>
		</c:if>
	</c:if>
</c:if>
</div>
<noframes>
</noframes>
</body>
</html>
