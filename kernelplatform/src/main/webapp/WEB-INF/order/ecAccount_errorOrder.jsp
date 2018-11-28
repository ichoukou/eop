<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
	<title>易通电子商务物流信息平台-圆通电商物流平台-圆通易通诚信软件电商版</title>
	<%@ include file="/WEB-INF/common/new_tab_meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<style type="text/css">
		 #table_b {margin-top:10px;}
	</style>
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		$(function(){
			shipNumDetail();
			$(".pagenavi a").click(function(){
				$("#currentPage_error").val($(this).attr("value"));
				var currentPage = $("#currentPage_error").val();
				var sendProv = $("#sendProv").val();
				var tempKey = $("#tempKey_error").val();
				// 请求地址
				$("#userFrom").submit();
			});
		});
	</script>
</head>
<body>
<s:if test="user.userType!=4">
<font color=red size=3>&nbsp;您的服务网点发货地址是：<s:property value='sendProv' />&nbsp;&nbsp;以下是无法计费订单信息，请手工核算实际运费</font>
</s:if>
<div class="table" id="table_b">
	<form id="userFrom" action="order!toErrorOrderView.action" method="post" style="display: none;">
		<input type="hidden" name="currentPage" id="currentPage_error" value='<s:property value="currentPage" />' /> 
		<input type="hidden" name="sendProv" id="sendProv" value='<s:property value='sendProv'/>*' />
		<input type="hidden" name="tempKey" id="tempKey_error" value="<s:property value='tempKey'/>"/>
	</form>
	<table>
		<thead>
			<tr>
				<th class="th_a">
					<div class="th_title">
						<em>运单号</em>
					</div></th>
				<th class="th_b">
					<div class="th_title">
						<em>状态</em>
					</div></th>
				<th class="th_c">
					<div class="th_title">
						<em>重量</em>
					</div></th>
				<th class="th_d">
					<div class="th_title">
						<em>首费</em>
					</div></th>
				<th class="th_e">
					<div class="th_title">
						<em>续费</em>
					</div></th>
				<th class="th_f">
					<div class="th_title">
						<em>订单发件地</em>
					</div></th>
				<th class="th_g">
					<div class="th_title">
						<em>目的地</em>
					</div></th>
				<s:if test="user.userType==4">
					<th class="th_h">
						<div class="th_title">
							<em>网点发货地址</em>
						</div></th>
				</s:if>
				<th class="th_i">
					<div class="th_title">
						<em>无法计费原因</em>
					</div></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="errorOrderList" var="errorOrder" status="st3">
				<tr>
					<s:if test="#errorOrder.remark == \"errorProv\"">
						<td class="td_a">
							<a href="javascript:;" title="订单发件地和运费模板发件地不一致。点击查看运单详情" style="text-decoration: underline; color: red" class="mailno" val="<s:property value='#errorOrder.mailNo'/>"><s:property value="#errorOrder.mailNo" /></a>
						</td>
					</s:if>
					<s:elseif test="#errorOrder.remark == \"errorProvCity\"">
						<td class="td_a">
							<a href="javascript:;" title="订单发件地和网点发件地不一致。点击查看运单详情" style="text-decoration: underline; color: red" class="mailno" val="<s:property value='#errorOrder.mailNo'/>"><s:property value="#errorOrder.mailNo" /></a>
						</td>
					</s:elseif>
					<s:elseif test="#errorOrder.remark == \"errorPostinfo\"">
						<td class="td_a">
							<a href="javascript:;" title="订单目的地不在运费模板中。点击查看运单详情" style="text-decoration: underline; color: red" class="mailno" val="<s:property value='#errorOrder.mailNo'/>"><s:property value="#errorOrder.mailNo" /></a>
						</td>
					</s:elseif>
					<td class="td_b">
						<s:property value="#errorOrder.status" /><br>
						<s:if test="#errorOrder.acceptTime==null">
							<s:date name="#errorOrder.createTime" format="yyyy-MM-dd hh:mm:ss" />
						</s:if>
						<s:else><s:date name="#errorOrder.acceptTime" format="yyyy-MM-dd hh:mm:ss" /></s:else>
					</td>
					<td class="td_c">
						<s:if test="#errorOrder.weight == 0">
							<font title='没有称重信息，默认按首重计费'>≤1</font>
						</s:if>
						<s:else>
							<s:property value="#errorOrder.weight" />
						</s:else>
					</td>
					<td class="td_d">无</td>
					<td class="td_e">无</td>
					<td class="td_f" <s:if test="#errorOrder.remark == \"errorPostinfo\"">style="color:red;"</s:if>><s:property value="#errorOrder.scity" /></td>
					<td class="td_g" <s:if test="#errorOrder.remark == \"errorPostinfo\"">style="color:red;"</s:if>><s:property value="#errorOrder.toAddr" /></td>
					<s:if test="user.userType==4">
						<td class="td_h"><s:property value="#errorOrder.wprovText" /></td>
					</s:if>
					<s:if test="#errorOrder.remark == \"errorProv\"">
						<td class="td_i" style="padding-right: 0px; text-align: center; color: red; cursor: help;" title="订单发件地和运费模板发件地不一致">订单发件地和运费模板发件地不一致</td>
					</s:if>
					<s:elseif test="#errorOrder.remark == \"errorProvCity\"">
						<td class="td_i" style="padding-right: 0px; text-align: center; color: red; cursor: help;" title="订单发件地和网点发件地不一致">订单发件地和网点发件地不一致</td>
					</s:elseif>
					<s:elseif test="#errorOrder.remark == \"errorPostinfo\"">
						<td class="td_i" style="padding-right: 0px; text-align: center; color: red; cursor: help;" title="订单目的地不在运费模板中">订单目的地不在运费模板中</td>
					</s:elseif>
				</tr>
			</s:iterator>
		</tbody>
	</table>
	<!-- S PageNavi -->
	<div class="pagenavi">
		<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
	</div>
	<!-- E PageNavi -->
</div>
</body>
</html>