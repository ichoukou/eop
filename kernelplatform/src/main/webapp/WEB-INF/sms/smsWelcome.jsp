<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/note_home.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>短信欢迎页面</title>
<script type="text/javascript">
	var params = {
			role : ${(yto:getCookie('userType') == 1 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 5) ? 1 : 2},
			msg : '',
			winHref : ''
	}
	params.winHref = 'alipay_applyService.action?menuFlag=home_payService&serviceId=5';	
	/* 
	switch (params.role) {
		// 所有有权限开通对心功能的角色
		case 1:
			params.msg = '确定要开通短信功能吗？';
			params.winHref = 'alipay_applyService.action?menuFlag=home_payService&serviceId=5';			
			break;
		case 2:												// 卖家子帐号
			params.msg = '亲，您没有开通短信功能权限。';
			params.winHref = '';			
			break;
		default :
			params.msg = '亲，您没有开通短信功能权限。';
			params.winHref = '';	
	} */
	/* 立即使用短信  */
	$(function(){
		var dialog = new Dialog();
		$(".btn_01").live('click',function(){	
			//wangmindong_message
			//alert("此功能已暂停!");
			//return ;
			 if (params.msg == '亲，您没有开通短信功能权限。') {
				dialog.init({
					contentHtml: params.msg,
					yes:function(){
						dialog.close();
					},
					yesVal:'我知道了'
				});
			} else { 
			 	dialog.init({
					contentHtml: params.msg,
					yes:function(){
						dialog.close();
						
					},
					no:function(){
						dialog.close();
					}
				}); 
				window.location.href = params.winHref;
		 	} 
		});
		$(".btn_02").live('click',function(){
			//wangmindong_message
			//alert("此功能已暂停!");
			//return ;
	 		if (params.msg == '亲，您没有开通短信功能权限。') {
				dialog.init({
					contentHtml: params.msg,
					yes:function(){
						dialog.close();
					},
					yesVal:'我知道了'
				});
			} else {  
				 dialog.init({
					contentHtml: params.msg,
					yes:function(){
						dialog.close();
						window.location.href = params.winHref;
					},
					no:function(){
						dialog.close();
					}
				});  
				window.location.href = params.winHref;
			  }  
		});
	});
</script>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/note_home.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
此功能暂停开放使用，已经购买的，在财务->收支明细页面请不要支付!
		<!-- S Content 
		<div id="content">
			<div class="note_ad">
				<p class="intop">
					<img src="images/single/01_01.jpg" width="948" height="324" alt="" />
					<input type="button" class="btn_01"/>
				</p>
				<p><img src="images/single/01_02.jpg" width="948" height="505" alt="" /></p>
				<p><img src="images/single/01_03.jpg" width="948" height="216" alt="" /></p>
				<p><img src="images/single/01_04.jpg" width="948" height="543" alt="" /></p>
				<p><img src="images/single/01_05.jpg" width="948" height="396" alt="" /></p>
				<p class="intop">
					<img src="images/single/01_06.jpg" width="948" height="183" alt="" />
					<input type="button" class="btn_02"/>
				</p>
			</div>
		</div>-->
		<!-- E Content -->











