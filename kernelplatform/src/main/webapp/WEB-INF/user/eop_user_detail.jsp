<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />

<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">客户管理--EOP会员详细信息</h2>
	</div>
	<div id="content_bd" class="clearfix">
		<div class="box box_a">
			<div class="box_bd">	
				<div>
					<p>
						<label>账号名称:</label>
						<s:property value="user.userName"/>
						<label style="margin-left:50px;">状态:</label>
						<s:if test="%{user.userState == 1}">活动</s:if>
						<s:elseif test="%{user.userState == 2}">未激活</s:elseif>
						<s:elseif test="%{user.userState == 0}">禁用</s:elseif>
					</p>
				</div>
				<br/>
				<div>
					<p>
						<label>真实姓名:</label>
						<s:property value="user.userNameText"/>
						<s:if test="user.appProvider.servicesType ==3">
							<label style="margin-left:50px;">身份证号码:</label>
						</s:if>
						<s:else><label style="margin-left:50px;">营业执照号码:</label></s:else>
						<s:if test="user.appProvider.servicesType ==3">
							<s:property value="user.appProvider.identityCard" />							
						</s:if>
						<s:else>
							<s:property value="user.appProvider.tradingCertificate" />
						</s:else>
					</p>
				</div>
				<br/>
				<div>
					<p>
						<label>会员类型:</label>
						<s:if test="user.appProvider.servicesType == 1">电子商务服务平台</s:if>
						<s:elseif test="user.appProvider.servicesType == 2">软件服务商</s:elseif>
						<s:elseif test="user.appProvider.servicesType == 3">个人开发者</s:elseif>
						<s:if test="user.appProvider.servicesType ==3">
							<label style="margin-left:50px;">身份证图片:</label>
						</s:if>
						<s:else><label style="margin-left:50px;">营业执照图片:</label></s:else>
						<s:if test="user.appProvider.servicesType == 3">
							<img class="portrait" id="licence_img" 
									src="<s:if test='user.appProvider.identityCardPath != ""'><s:property value="mediaPath" /><s:property value="user.appProvider.identityCardPath" /></s:if><s:else>/images/dimages.png</s:else>" alt="" />
							</s:if>
							<s:else>
							<img class="portrait" id="licence_img" 
									src="<s:if test='user.appProvider.tradingCertificatePath != ""'><s:property value="mediaPath" /><s:property value="user.appProvider.tradingCertificatePath" /></s:if><s:else>/images/dimages.png</s:else>" alt="" />
						</s:else>
					</p>
				</div>
				<br/>
				<div>
					<p>
						<label>联系方式:</label>
						<s:property value="user.mobilePhone"/>
					</p>
				</div>
				<br/>
				<div>
					<p>
						<label>联系地址:</label>
						<s:property value="user.addressProvince"/><s:property value="user.addressCity"/>
						<s:property value="user.addressDistrict"/><s:property value="user.addressStreet"/>
					</p>
				</div>
				<br/>
				<div>
					<p>
						<label>邮箱地址:</label>
						<s:property value="user.mail"/>
					</p>
				</div>
				<br/>
				<div>
					<p>
						<label>公司名称:</label>
						<s:property value="user.appProvider.companyName"/>
					</p>
				</div>
				<br/>					
				<div>
					<p>
						<label>公司网址:</label>
						<s:property value="user.appProvider.companyAddress"/>
					</p>
				</div>
				<br/>
				<div>
					<p>
						<label>公司地址:</label>
						<s:property value="user.appProvider.companyUrl"/>
					</p>
				</div>
				<br/>				
				<div>
					<a href="javascript:;" id="btn_back" class="del_btn btn btn_a" title="返回" onclick="history.back();"><span>返回</span></a>
				</div>	
			</div>
		</div>		
	</div>	
</div>