<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>

	<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/channel.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->


		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">渠道信息管理</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<span class="btn btn_a"><a href="channel_addUI.action?menuFlag=peizhi_channel_list"><input type="button" value="增加渠道信息"></a></span>
					</div>
				</div>
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a"><div class="th_title"><em>客户名称</em></div></th>
								<th class="th_b"><div class="th_title"><em>推送标识</em></div></th>
								<th class="th_c"><div class="th_title"><em>IP_Address</em></div></th>
								<th class="th_d"><div class="th_title"><em>clientId</em></div></th>
								<th class="th_f"><div class="th_title"><em>parternId</em></div></th>
								<th class="th_g"><div class="th_title"><em>ip白名单</em></div></th>
								<th class="th_h"><div class="th_title"><em>打印标识</em></div></th>
								<th class="th_i"><div class="th_title"><em>发送单号</em></div></th>
								<th class="th_e"> <div class="th_title"><em>操作</em></div></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items = "${channelList }" var = "channel" varStatus="st">
							<tr>
								<td class="td_a">${channel.key }</td>
								<td class="td_b">${channel.value }</td>
								<td class="td_c">${channel.ipAddress }</td>
								<td class="td_d">${channel.clientId }</td>
								<td class="td_f">${channel.parternId }</td>
								<td class="td_g">${channel.ip }</td>
								<td class="td_h">${channel.isPrint }</td>
								<td class="td_i">${channel.isSend }</td>
								<td class="td_e"><a href="javascript:edit(${channel.id })">修改</a>&nbsp;
								<a href="javascript:del(${channel.id })">×</a></td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<!-- S PageNavi -->
				<div class="pagenavi">
				
					<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
				
				</div>
				<!-- E PageNavi -->
			</div>
		</div>
		<!-- E Content -->
	
	<!-- #include file="公共模块/footer.html" -->
	
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath }/page/channel.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
