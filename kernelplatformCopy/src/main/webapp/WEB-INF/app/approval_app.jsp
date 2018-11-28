<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />


	<script type="text/javascript">
		$(function() {
			$('#refusemessage').defaultTxt('拒绝审核，请输入拒绝原因，再点“拒绝通过”!');
		})
	
		//审核通过	
		function approval(){
			//window.location.href = "approvalApp.action?app.appstatus=4&app.id="+$("#appId").val()
			var scrollTop = window.parent.document.documentElement.scrollTop;
			if(scrollTop==0){
			  	scrollTop = window.parent.document.body.scrollTop;
			  	scrollTop = scrollTop==0?100:scrollTop;
			}
			$.ajax({
				url : 'approvalApp.action',
				type : "post",
				data : "app.appstatus=4&app.id="+$("#appId").val()+"&menuFlag=home_app_list",
				cache: false,
				success:function(response){
					var aDialog = new Dialog();
					aDialog.init({
						contentHtml: response.infoContent,
						yes: function() {
							aDialog.close();
							if(response.status)
					    		location.href=response.targetUrl;
						}
					});
				}
			});
		}
		
		//审核拒绝	
		function refuse(){
			if($("#refusemessage").val() == "" || $("#refusemessage").val() == "拒绝审核，请输入拒绝原因，再点“拒绝通过”!"){
				var oDialog = new Dialog();
				oDialog.init({
					contentHtml: '请输入拒绝原因，再点“拒绝通过”!',
					yes: function() {
						oDialog.close();
					}
				});
				return;
			}
			var scrollTop = window.parent.document.documentElement.scrollTop;
			if(scrollTop==0){
			  	scrollTop = window.parent.document.body.scrollTop;
			  	scrollTop = scrollTop==0?100:scrollTop;
			}
			$.ajax({
				type : "post",
				dataType : "json",
				data : "app.appstatus=3&app.id="+$("#appId").val()+"&menuFlag=home_app_list",
				cache: false,
				url : 'approvalApp.action',
				success:function(response){
					var aDialog = new Dialog();
					aDialog.init({
						contentHtml: response.infoContent,
						yes: function() {
							aDialog.close();
							if(response.status)
					    		location.href=response.targetUrl;
						}
					});
				}
			});
		}
		
		//禁用应用 	
		function forbidden(){
			//window.location.href = "approvalApp.action?app.appstatus=5&app.id="+$("#appId").val()
			var scrollTop = window.parent.document.documentElement.scrollTop;
			if(scrollTop==0){
			  	scrollTop = window.parent.document.body.scrollTop;
			  	scrollTop = scrollTop==0?100:scrollTop;
			}
			$.ajax({
				type : "post",
				dataType : "json",
				data : "app.appstatus=5&app.id="+$("#appId").val()+"&menuFlag=home_app_list",
				cache: false,
				url : 'approvalApp.action',
				success:function(response){
					var aDialog = new Dialog();
					aDialog.init({
						contentHtml: response.infoContent,
						yes: function() {
							aDialog.close();
							if(response.status)
					    		location.href=response.targetUrl;
						}
					});
				}
			});
		}
		
		function openMessage(){
			parent.main.location = "sendMessage_openAdviseUI.action";
			parent.side.layerLight("advise_li");
		}
	</script>

<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">应用审核管理</h2>
		<em>易通系统还处于试运行阶段，迫切需要您的宝贵建议！<a href="send_openAdviseUI.action?menuFlag=home_msg_advise">我有想法</a></em>
	</div>
	<div id="content_bd" class="clearfix">
		<div class="box box_a">
			<div class="box_bd">
				<input id="appId" type="hidden" value="<s:property value='app.id'/>" />
			  	<form class='nimei'>
					<div>
						<p>
							<label>应用类型:</label>
							<s:if test="app.provider.servicesType == 1">电子商务服务平台</s:if>
							<s:elseif test="app.provider.servicesType == 2">软件服务商</s:elseif>
							<s:elseif test="app.provider.servicesType == 3">个人开发者</s:elseif>
							<label style="margin-left:50px;">真实姓名:</label>
							<s:property value="app.provider.linkman"/>
							<label style="margin-left:50px;">公司名称:</label>
							<s:property value="app.provider.companyName"/>
						</p>
					</div>
					<br/>	
					<div>
						<p>
							<label>应用名称:</label>
							<s:property value="app.appName"/>
							<label style="margin-left:50px;">联系方式:</label>
							<s:property value="app.provider.customerPhone"/>
							<label style="margin-left:50px;">公司地址:</label>
							<s:property value="app.provider.companyAddress"/>
						</p>
					</div>
					<br/>	
					<div>
						<p>
							<s:if test="app.provider.servicesType == 3"><label>身份证号码:</label></s:if>
							<s:else><label>营业执照号码:</label></s:else>
							<s:if test="app.provider.servicesType == 3"><s:property value="app.provider.identityCard" /></s:if>
							<s:else><s:property value="app.provider.tradingCertificate" /></s:else>
							<label style="margin-left:50px;">邮箱地址:</label>
							<s:property value="app.provider.customerMail"/>
							<label style="margin-left:50px;">公司网址:</label>
							<s:property value="app.provider.companyUrl"/>
						</p>
					</div>
					<br/>	
					<div>
						<p>
							<s:if test="app.provider.servicesType == 3"><label>身份证图片:</label></s:if>
							<s:else><label>营业执照图片:</label></s:else>
							<s:if test="app.provider.servicesType == 3">
								<img class="portrait" id="licence_img" 
									src="<s:if test='app.provider.identityCardPath != ""'><s:property value="mediaPath" /><s:property value="app.provider.identityCardPath" /></s:if><s:else><s:property value="#request.get('javax.servlet.forward.context_path')" />/images/dimages.png</s:else>" alt="" />
							</s:if>
							<s:else>
								<img class="portrait" id="licence_img" 
									src="<s:if test='app.provider.tradingCertificatePath != ""'><s:property value="mediaPath" /><s:property value="app.provider.tradingCertificatePath" /></s:if><s:else><s:property value="#request.get('javax.servlet.forward.context_path')" />/images/dimages.png</s:else>" alt="" />
							</s:else>
							<label style="margin-left:50px;">联系地址:</label>
							<s:property value="app.provider.customerContact"/>
						</p>
					</div>
					<br/>
					<div>
						<P>
							<label>应用状态:</label>
							<s:if test="app.appstatus == 1"><font><strong>开发中</strong></font></s:if>
							<s:elseif test="app.appstatus == 2"><font><strong>测试中</strong></font></s:elseif>
							<s:elseif test="app.appstatus == 3"><font><strong>审核中</strong></font></s:elseif>
							<s:elseif test="app.appstatus == 4"><font><strong>应用中</strong></font></s:elseif>
							<s:elseif test="app.appstatus == 5"><font><strong>已禁用</strong></font></s:elseif>
						</P>
					</div>
					<br/>
					<div>
						<p>
							<label>应用简介:</label>
							<textarea id="detailmessage" class="textarea_text" cols="100" rows="5"><s:property value="app.appdetails" /></textarea>
						</p>
					</div>
					<br/>
					<div>
						<p>
							<label>拒绝原因:</label>
							<textarea name="app.message" id="refusemessage" class="textarea_text" cols="100" rows="5"></textarea>
						</p>
					</div>
					<br/>
					<div class="table_footer clearfix">
						<div class="print_box">						
							<a href="javascript:;" id="btn_approval" class="btn btn_a" title="通过审核" onclick="approval();"><span>通过审核</span></a>
							<a href="javascript:;" id="btn_refuse" class="btn btn_a" title="拒绝通过" onclick="refuse();"><span>拒绝通过</span></a>	
							<a href="javascript:;" id="btn_forbidden" class="btn btn_a" title="禁用应用" onclick="forbidden();"><span>禁用应用</span></a>
							<a href="javascript:;" id="btn_back" class="btn btn_a" title="返回" onclick="history.back();"><span>返回</span></a>										
						</div>
					</div>
				</form>
        	</div>
    	</div>
	</div>
</div>