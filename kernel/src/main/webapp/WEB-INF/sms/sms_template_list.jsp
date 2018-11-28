<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/sms_template_list.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>模板管理</title>

<!-- S Content -->
<div id="content">
	<form action="" method="post">
		<input id="tabNum" type="hidden" value="<s:property value="tabNum"/>" />
		<input id="serviceId" type="hidden" value="<s:property value="serviceId"/>" />
	</form>
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">1.添加新模板>2.编辑模板>3.提交审核</h2>
	</div>
	<div id="content_bd" class="clearfix">
		<div id="manager_hd">
			<ul class="thead_select">
				<li class="" id="0">
					<a class="a" st="0"  href="javascript:;">
						发货提醒
					</a>
					 <s:iterator value="smsServices" var="service">
					 	<s:if test="#service.name == '发货提醒'">
							<input class="serviceId" type="hidden" value="<s:property value="#service.id"/>">
						</s:if>
					 </s:iterator>
				</li>
				<li class="" id="1">
					<a class="a" st="1" href="javascript:;">
						派件提醒
					</a>
					<s:iterator value="smsServices" var="service">
					 	<s:if test="#service.name == '派件提醒'">
							<input class="serviceId" type="hidden" value="<s:property value="#service.id"/>">
						</s:if>
					 </s:iterator>
				</li>
				<li class="" id="2">
					<a class="a" st="2"  href="javascript:;">
						签收提醒
					</a>
					<s:iterator value="smsServices" var="service">
					 	<s:if test="#service.name == '签收提醒'">
							<input class="serviceId" type="hidden" value="<s:property value="#service.id"/>">
						</s:if>
					 </s:iterator>
				</li>
<!-- 				<li class="" id="3"> -->
<!-- 					<a class="a" st="3" href="javascript:;"> -->
<!-- 						营销定制 -->
<!-- 					</a> -->
<%-- 					<s:iterator value="smsServices" var="service"> --%>
<%-- 					 	<s:if test="#service.name == '营销定制'"> --%>
<%-- 							<input class="serviceId" type="hidden" value="<s:property value="#service.id"/>"> --%>
<%-- 						</s:if> --%>
<%-- 					 </s:iterator> --%>
<!-- 				</li> -->
				<%-- <li class="" id="3">
					<a class="a" st="3" href="javascript:;">
						营销定制
					</a>
					<s:iterator value="smsServices" var="service">
					 	<s:if test="#service.name == '营销定制'">
							<input class="serviceId" type="hidden" value="<s:property value="#service.id"/>">
						</s:if>
					 </s:iterator>
				</li> --%>
				<%-- <s:iterator value="smsServices" status="st" var="service">
					<li class="" id="<s:property value="#st.index"/>"><a class="a" st="<s:property value="#st.index"/>" val="<s:property value="#service.id"/>" href="javascript:;"><s:property value="#service.name"/></a>
					</li>
				</s:iterator> --%>
			</ul>
<%-- 			<s:if test="templates.size < 6 "> --%>
				<a href="javascript:;" id="new_template">添加新模板</a>
<%-- 			</s:if> --%>
		</div>
		<!-- S Table -->
		<div class="table" id="tem_table">
			<table>
				<thead>
					<tr>
						<th class="th_a">
							<div class="th_title">
								<em>模板标题</em>
							</div></th>
						<th class="th_b">
							<div class="th_title">
								<em>状态</em>
							</div></th>      
						<th class="th_c">
							<div class="th_title">
								<em>已发数（条）</em>
							</div></th>
						<th class="th_d">
							<div class="th_title">
								<em>修改时间</em>
							</div></th>
						<th class="th_e">
							<div class="th_title">
								<em>操作</em>
							</div></th>
					</tr>
				</thead>

				<tbody>
					<s:if test="templates == null || templates.size < 1">
						<tr>
								<td class="td_a" colspan="5" align="center">
								抱歉！没有查询任何短信模板...
						</td></tr>
					</s:if>
					<s:else>
						
						<c:forEach items="${templates }" var="at" varStatus="status">
							<c:if test="${at.status == 'N' && at.isDefault == 'N' }"><tr class="fail_tem"></c:if>
							<c:if test="${at.isDefault == 'Y' }"><tr id="default_tem"></c:if>
								<td class="td_a">
										${at.name}<c:if test="${at.isDefault == 'Y' }"><span class="tem_title" title="默认模板"></span></c:if>
								</td>
								<td class="td_b">
									<c:if test="${at.status == 'Y' || at.status == 'S' }">审核通过</c:if>
									<c:if test="${at.status == 'N' }"><span class="tem_status">审核失败<i class="failImg" title="${at.remark}"></i></span></c:if>
									<c:if test="${at.status == 'M' }">审核中</c:if>
									<input type="hidden" class="tid1" value="${at.id}" />
								</td>
								<td class="td_c"><span class="tem_num">
									<c:if test="${at.sendCount > 0}">${at.sendCount}</c:if>
									<c:if test="${at.sendCount == 0}">暂无</c:if>
								</td>
								<td class="td_d">
									<span class="tem_date"><fmt:formatDate value="${at.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
								</td>
								<td class="td_e">
									
									<a href="javascript:;" <c:if test="${(at.status == 'S' || at.status == 'Y') && at.isDefault != 'Y'}">class="tem_default"</c:if> >设为默认</a> 
									
									<a href="javascript:;" class="tem_view">查看</a> 
									
									<a href="javascript:;" <c:if test="${(at.status == 'N' || at.status == 'Y' || at.status == 'M') && at.isDefault != 'Y'}">class="tem_edit"</c:if> >修改</a> 
									
									<a href="javascript:;" <c:if test="${(at.status == 'N' || at.status == 'Y' || at.status == 'M') && at.isDefault != 'Y'}">class="tem_del"</c:if> >删除</a>
									
									<input type="hidden" class="tid" value="${at.id}" />
								</td>
							</tr>
						</c:forEach>
					</s:else>
				</tbody>
			</table>
		</div>
		<!-- E Table -->

		<div id="manager_ft">
			<p>每种短信类型最多可添加5条模板</p>
			<p>添加或编辑模板需要管理员审核才可使用，审核时间为24小时工作时间，节假日期间顺延</p>
		</div>

	</div>

	<script>
		var params = {
			dialogSubmit : '?dialogSubmit',
			tabIndex : document.getElementById('tabNum').value,
			
		<%-- 	onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
				? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},	// “开始使用” == 1，“绑定网点” == 2 --%>
			userId:${yto:getCookie('id')},						 //当前登录用户的id
			showBindCode:true,
			userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
			infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
			bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
			pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
			
		}
	</script>

	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/sms_template_list.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
	
</div>

<!-- E Content -->

