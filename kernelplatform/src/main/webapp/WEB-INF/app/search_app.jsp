<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- 当前页面js -->
<script type="text/javascript" src="${jsPath}/page/search_app.js?d=${str:getVersion() }"></script>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	
	<script type="text/javascript">
	
		function synCheckbox(obj, containerId) {
			//var obj = event.srcElement || event.target;	// 事件源对象 		
			$("#"+containerId).find(":checkbox").each(function() {
				$(this).attr("checked", obj.checked);
			});
		}
		
		// 跳转到审核页面
		function approval(appId) {
			setTimeout(function(){
				window.location.href = "toapprovalApp.action?app.id=" + appId +"&menuFlag=home_app_list";
			},0);
		}
		
		function toPage(cp){
			$("#currentPage").val(cp);
			$("#appForm").submit();
		}
	</script>
	

<div id="content">
	<div id="content_hd" class="clearfix">
<!--   		<h2 id="message_icon">应用审核管理--应用列表</h2> -->
		<em>易通系统还处于试运行阶段，迫切需要您的宝贵建议！<a href="send_openAdviseUI.action?menuFlag=home_msg_index">我有想法</a></em>
	</div>
	<div id="content_bd" class="clearfix">
		<div class="box box_a">
			<div class="box_bd">
				<div class="box box_a">
					<div id="box_bd_b" class="box_bd" style="position:relative;">
						<s:form id="appForm" action="searchApp.action" method="post"  theme="simple" >
							<input type="hidden" id="currentPage" value="<s:property value='currentPage'/>" name="currentPage"/>
		    				<p>
								<span>发布时间：</span>
		        				<input class="Wdate" type="text" name="starttime" id="starttime" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true})" value="<s:property value='starttime'/>">　至　
								<input class="Wdate" type="text" name="endtime" id="endtime" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true,startDate:'#F{$dp.$D(\'starttime\')}',minDate:'#F{$dp.$D(\'starttime\')}'})" value="<s:property value='endtime'/>">
								<span>应用状态：</span>
								<select id="dealStatus" name="app.appstatus" style="height:26px;">
									<option value="">所有</option>
									<option <s:if test="%{app.appstatus == 1}">selected</s:if> value="1">开发中</option>
									<option <s:if test="%{app.appstatus == 2}">selected</s:if> value="2">测试中</option>
									<option <s:if test="%{app.appstatus == 3}">selected</s:if> value="3">审核中</option>
									<option <s:if test="%{app.appstatus == 4}">selected</s:if> value="4">应用中</option>
									<option <s:if test="%{app.appstatus == 5}">selected</s:if> value="5">已禁用</option>
								</select>
								<a href="javascript:;" id="sear_btn" title="查 询" class="btn btn_a" onclick="toPage(1);"><span>查 询</span></a>
							</p>
							<!-- 左侧菜单选中样式 -->
							<input type="hidden" name="menuFlag" value="${menuFlag }" />	
						</s:form>
					</div>
				</div>
		
			<div class="table">	
				<table id="tbTB">
					<thead>
						<tr id="tbTH">
			    			<th><div class="th_title"><em>应用名称</em></div></th>
							<th><div class="th_title"><em>应用发布时间</em></div></th>
							<th><div class="th_title"><em>联系人</em></div></th>
							<th><div class="th_title"><em>类型</em></div></th>
							<th><div class="th_title"><em>联系方式</em></div></th>
							<th><div class="th_title"><em>状态</em></div></th>
			    		</tr>
					</thead>
					<tbody id="tbTB_TBD">
						<s:iterator value="#request.apps" status="stuts" var="ap">
							<tr>
								<td>
									<a href="javascript:approval(<s:property value='#ap.id'/>);" ><s:property value="#ap.appName"/></a>
								</td>
								<td>
									<s:date name="createTime" format="yyyy-MM-dd"/>
								</td>
								<td>
									<s:property value="#ap.provider.linkman"/>
								</td>
								<td>
									<s:if test="#ap.provider.servicesType == 1">电子商务服务平台</s:if>
									<s:elseif test="#ap.provider.servicesType == 2">软件服务商</s:elseif>
									<s:elseif test="#ap.provider.servicesType == 3">个人开发者</s:elseif>
								</td>
								<td>
									<s:property value="#ap.provider.customerPhone"/>
								</td>
								<td>
									<s:if test="#ap.appstatus == 1">开发中</s:if>
									<s:elseif test="#ap.appstatus == 2">测试中</s:elseif>
									<s:elseif test="#ap.appstatus == 3">审核中</s:elseif>
									<s:elseif test="#ap.appstatus == 4">应用中</s:elseif>
									<s:elseif test="#ap.appstatus == 5">已禁用</s:elseif>
								</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
				<div class="table_footer clearfix">
					<div class="pagenavi">
						<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
					</div>
				</div>
			</div>
			</div>
		</div>
	</div>
</div>
