<%--
	@author ChenRen
	@date 	2011-09-21
	@description
		用户列表
			只提供简单的用户信息列表
			
			提供[确定]/[取消]操作
		
		窗口返回所选择的用户的名字和账号和用户Id
		
	@ ChenRen/2011-11-03
	@ 功能优化
		#############
		#此处省略xx字	#
		#############
		
	@ ChenRen/2011-11-16
	@ description
		限制只能选择未分配模板的用户；已分配过模板的用户不能选择
		
		本次改版涉及到后台业务逻辑改动。需要判断查出的用户是否同时分配过了运费模板，如果分配了就做个标识。
		现定为user.field003=false为分配过模板的用户标识
		
--%>
<%@page import="com.ytoec.uninet.util.HessianUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/tlds/str.tld" prefix="str" %>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<%@ taglib uri="/WEB-INF/tlds/escape.tld" prefix="escape" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="cssPath" value="${ctxPath}/css" scope="request"/>
<c:set var="imagesPath" value="${ctxPath}/images" scope="request"/>
<c:set var="jsPath" value="${ctxPath}/js" scope="request"/>

<c:set var="mediaPath" value="<%=HessianUtil.getMediaPath() %>" scope="request"/>
<c:set var="tempMediaPath" value="${mediaPath }/temp" scope="request"/>
<c:set var="fileSizeLimit" value="<%=HessianUtil.getUploadFileSizeLimit() %>" scope="request"/>
<html>
<head>


<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css"/>
<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/lookatmb.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<title>选择用户</title>
<script>
var path = {
		ctx: '${ctxPath}',
		media:'${mediaPath}' 
}
</script>
<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
<!--[if IE 6]>
	<script type="text/javascript" src="${jsPath}/util/position_fixed.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.png');
	</script>
<![endif]-->
<script src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }" type="text/javascript" charset="UTF-8"></script>
<script src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }" type="text/javascript" charset="UTF-8"></script>
<script src="${jsPath}/module/dialog.js?d=${str:getVersion() }" type="text/javascript"></script>
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/modelWindow.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
</head>

<body>
<!-- S Content -->
<div id="content" style="width:auto;margin:0;float:left;">
	<div id="content_hd" class="clearfix">
		<s:form id="userFrom" action="user!listPosttempUser.action" method="post"  theme="simple" >&nbsp;用户名称：<s:textfield name="user.userName" id="nameText" cssClass="text-input input_text" theme="simple" title="输入用户名称模糊查询"/><input type="hidden" name="currentPage" id="currentPage" value='<s:property value="currentPage" />' /> <a id="search_btn" class="btn btn_a" type="button" value="查 询"><span>查询</span></a> <a id="subWinOK" class="btn btn_a" type="button" value="确定"><span>确定</span></a> <a onclick="javascript: window.close();" class="btn btn_a" type="button" value="取消"><span>取消</span></a></s:form>
	</div>
	<div id="content_bd" class="clearfix">
		<!-- S Box -->
		<div class="box box_a" style="width: 454px;">
			<div class="box_bd clearfix">
					<!-- S Table -->
					<div class="table" style="display:block;">
						<table id="tbTB">
							<thead>
								<tr id="tbTH">
									<th class="th_a" width="36">
										<div class="th_title" style="text-align:center;">
											<em><input id="thd_ckb" type="checkbox" style="margin-top:10px;" class="synCheckbox" title="全选/全不选" /></em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>用户账号</em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>用户名称</em>
										</div></th>
									<th class="th_c">
										<div class="th_title">
											<em>用户编码</em>
										</div></th>
								</tr>
							</thead>
							<tbody id="tbTB_TBD">
								<input type="hidden" name="currentPage" id="currentPage" value='<s:property value="currentPage" />'/>
								<s:iterator value="#request.userThreadList" status="stuts" var="user">
								<tr>
									<td>
										<input type="checkbox" userId="<s:property value="#user.id"/>" text="<s:property value="#user.userName"/>(<s:property value="#user.userName"/>)"
											<s:if test="#user.backup1 == 'false'">
											disabled title="该用户已经分配过模板! 请直接修改以前的运费模板或者取消以前分配的模板。"
											</s:if>  
										/>
									</td>
									<td> <s:property value="#user.userName"/> </td>
									<td> <s:property value="#user.userName"/> </td>
									<td> <s:property value="#user.userCode"/> </td>
						        </tr>
			          			</s:iterator>
							</tbody>
							
						</table>
					</div>
					<!-- E Table -->
					
					<!-- S PageNavi -->
						<div class="pagenavi">
							<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
						</div>
					<!-- E PageNavi -->
                
			</div>
			
			
		</div>
		<!-- E Box -->
	</div>
</div>
<!-- E Content -->
</body>
</html>
