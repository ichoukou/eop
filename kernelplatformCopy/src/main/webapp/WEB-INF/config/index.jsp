<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>

	<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/config.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">配置项管理</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<span class="btn btn_a"><a href="config_addUI.action?menuFlag=peizhi_config_index"><input type="button" value="增加配置项"></a></span>
					</div>
				</div>
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a"> <div class="th_title"><em>配置名</em></div></th>
								<th class="th_b"> <div class="th_title"><em>配置值</em></div></th>
								<th class="th_c"> <div class="th_title"><em>显示配置值</em></div></th>
								<th class="th_d"> <div class="th_title"><em>类型</em></div></th>
								<th class="th_e"> <div class="th_title"><em>排序</em></div></th>
								<th class="th_f"> <div class="th_title"><em>备注</em></div></th>
								<th class="th_f"> <div class="th_title"><em>操作</em></div></th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="configList" id="config">
								<tr>
									<td <s:if test="confType == 2">onclick="showNextData(<s:property value='id'/>)" onmouseover="this.style.cursor='pointer';" title="点击显示子配置项"</s:if>><s:property value="confKey"/></td>
						            <td <s:if test="confType == 2">onclick="showNextData(<s:property value='id'/>)" onmouseover="this.style.cursor='pointer';" title="点击显示子配置项"</s:if>><s:property value="confValue"/></td>
						            <td><s:property value="confText"/></td>
						            <td>
						            	<s:if test="confType == 1">单目录配置</s:if>
						            	<s:if test="confType == 2">多目录配置</s:if>
						            </td>
						            <td><s:property value="seq"/></td>
						            <td><s:property value="remark"/></td>
						            <td>
						            	<a href="javascript:edit(<s:property value='id'/>)">修改</a>&nbsp;
						            	<a href="javascript:del(<s:property value='id'/>)">×</a>
						            </td>
								</tr>
							</s:iterator>
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
	<script type="text/javascript" src="${jsPath }/page/config.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
