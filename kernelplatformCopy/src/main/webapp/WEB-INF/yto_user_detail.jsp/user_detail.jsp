<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户查看</title>
	<link rel="stylesheet" type="text/css" href="css/posttemp.css?d=${str:getVersion() }" />
	<link rel="stylesheet" href="css/reset.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<link rel="stylesheet" type="text/css" href="css/jquery-easyui/easyui.css?d=${str:getVersion() }">
	<link rel="stylesheet" type="text/css" href="css/jquery-easyui/icon.css?d=${str:getVersion() }">
	<link rel="stylesheet" type="text/css" href="css/layoutshow.css?d=${str:getVersion() }">
	<link rel="stylesheet" type="text/css" href="css/layout.css?d=${str:getVersion() }" />
	<link rel="stylesheet" type="text/css" href="css/font.css?d=${str:getVersion() }" />
	<style type="text/css">
		#main-content tbody td {border-bottom:none;}
	</style>
</head>
<body>
<div id="main">
	<div id="main-content">
	  <div id="midtit">
			<span class="loading">客户管理--</span>
			<span class="gnheader f14">EOP会员详细信息</span>&nbsp;&nbsp;&nbsp;&nbsp;
	  </div>
			<div class="content-box-header" style="margin-top:10px; margin-bottom:0px;border:1px solid #ccc;border-bottom:none;"><h3 style="font-size:15px;font-weight:bold;">EOP会员详细信息</h3></div>
			<div id="midfrom_myaccount" style="margin-top:0px;">
			
				<table border="0" cellpadding="2" cellspacing="2">
					<tr>
						<td align="right" width="50px">
							账号名称:
						</td>
						<td align="left" width="120px">
							<s:property value="user.userName"/>
						</td>
						<td align="right" width="50px">
							状态:
						</td>
						<td align="left" width="120px">
							<s:if test="%{user.userState == 1}">活动</s:if>
							<s:elseif test="%{user.userState == 2}">未激活</s:elseif>
							<s:elseif test="%{user.userState == 0}">禁用</s:elseif>
						</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							真实姓名:
						</td>
						<td align="left" width="120px">
							<s:property value="user.userNameText"/>
						</td>
						<td align="right" width="50px">
							<s:if test="user.appProvider.servicesType ==3">
								身份证号码:
							</s:if>
							<s:else>营业执照号码:</s:else>
						</td>
						<td align="left" width="120px">
							<s:if test="user.appProvider.servicesType ==3">
								<s:property value="user.appProvider.identityCard" />
							</s:if>
							<s:else><s:property value="user.appProvider.tradingCertificate" /></s:else>
						</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							会员类型:
						</td>
						<td align="left" width="120px">
							<s:if test="user.appProvider.servicesType == 1">电子商务服务平台</s:if>
							<s:elseif test="user.appProvider.servicesType == 2">软件服务商</s:elseif>
							<s:elseif test="user.appProvider.servicesType == 3">个人开发者</s:elseif>
						</td>
						<td align="right" width="50px">
							<s:if test="user.appProvider.servicesType ==3">
								身份证图片:
							</s:if>
							<s:else>营业执照图片:</s:else>
						</td>
						<td align="left" width="120px" rowspan="6">
							<s:if test="user.appProvider.servicesType == 3">
							<img class="portrait" id="licence_img" 
									src="<s:if test='user.appProvider.identityCardPath != ""'><s:property value="mediaPath" /><s:property value="user.appProvider.identityCardPath" /></s:if><s:else>/images/dimages.png</s:else>" alt="" />
							</s:if>
							<s:else>
							<img class="portrait" id="licence_img" 
									src="<s:if test='user.appProvider.tradingCertificatePath != ""'><s:property value="mediaPath" /><s:property value="user.appProvider.tradingCertificatePath" /></s:if><s:else>/images/dimages.png</s:else>" alt="" />
							</s:else>
						</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							联系方式:
						</td>
						<td align="left" width="120px">
							<s:property value="user.mobilePhone"/>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							联系地址:
						</td>
						<td align="left" width="120px">
							<s:property value="user.addressProvince"/><s:property value="user.addressCity"/>
							<s:property value="user.addressDistrict"/><s:property value="user.addressStreet"/>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							邮箱地址:
						</td>
						<td align="left" width="120px">
							<s:property value="user.mail"/>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							公司名称:
						</td>
						<td align="left" width="120px">
							<s:property value="user.appProvider.companyName"/>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							公司地址:
						</td>
						<td align="left" width="120px">
							<s:property value="user.appProvider.companyAddress"/>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="right" width="50px">
							公司网址:
						</td>
						<td align="left" width="120px">
							<s:property value="user.appProvider.companyUrl"/>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4">
							<input type="button" onclick="javascript:history.back();" value="返回"/>
						</td>
					</tr>
				</table>
			
			</div>
				
        </div>
     </div>
	
</body>
</html>