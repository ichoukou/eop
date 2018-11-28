<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
	<title>易通电子商务物流信息平台-圆通电商物流平台-圆通易通诚信软件电商版</title>
	<%@ include file="/WEB-INF/common/new_tab_meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		$(function(){
			shipNumDetail();
			$(".pagenavi a").click(function(){
				$("#currentPage_adjust").val($(this).attr("value"));
				var currentPage = $("#currentPage_adjust").val();
				var tempKey = $("#tempKey_adjust").val();
				$("#userFrom").submit();
			});
		});
	</script>
</head>
<body>
<div class="table" id="table_b">
	<form id="userFrom" action="order!toufOrderView.action" method="post" style="display: none;">
		<input type="hidden" name="currentPage" id="currentPage_return" value='<s:property value="currentPage" />' /> 
		<input type="hidden" name="tempKey" id="tempKey_return" value="<s:property value='tempKey'/>"/>
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
						<em>下单渠道</em>
					</div></th>
				<th class="th_g">
					<div class="th_title">
						<em>目的地</em>
					</div></th>
				<th class="th_h">
					<div class="th_title">
						<em>实收运费</em>
					</div></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="ufOrderList" var="errorOrder" status="st3">
				<tr>
					<td class="td_a">
						<a href="javascript:;" title="点击查看运单详情" style="text-decoration: underline; color: red" class="mailno" val="<s:property value='#errorOrder.mailNo'/>"><s:property value="#errorOrder.mailNo" /></a>
					</td>
					<td class="td_b">
						<s:property value="#errorOrder.status"/>
						<s:if test="#errorOrder.freightType == 2">（新增运费）</s:if>
						<s:else>（修改运费）</s:else> <br>
						<s:if test="#errorOrder.acceptTime==null"><s:date name="#errorOrder.createTime" format="yyyy-MM-dd hh:mm:ss"/></s:if>
						<s:else><s:date name="#errorOrder.acceptTime" format="yyyy-MM-dd hh:mm:ss"/></s:else>
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
					<td class="td_f">
						<s:if test="#errorOrder.lineType == 0">
							线上下单
						</s:if>
						<s:elseif test="#errorOrder.lineType == 1">
							线下下单
						</s:elseif>
					</td>
					<td class="td_g"><s:property value="#errorOrder.toAddr"/></td>
					<td class="td_h"><s:property value="#errorOrder.trimFreight"/></td>
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