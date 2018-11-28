<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/note_set.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>短信设置</title>

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">短信设置</h2>
			</div>
			<div id="content_bd" class="clearfix">
			   <form id="set_fm" action="#">
			   		<input type="hidden" name="menuFlag" value="sms_home"/>
			   		<input type="hidden" id="defaultID" name="defaultID" value="<s:property value='defaultID' />" />
			   		<input type="hidden" id="serviceId" name="serviceId" value="<s:property value='serviceId' />" />
			   		<input type="hidden" id="templateListHid" value="<s:property value='templateList' />" />
					<!-- S 选择发送模板 -->
					<div class="template">
						<label class="title">选择发送模板:</label>
						<ul>
							<s:iterator value="templateList" id="templateForm" status="st">
							<li <c:if test="${templateForm.isDefault == 'Y' }"> class='on' </c:if>>
								<label>
									<input type="radio" name="template" <c:if test="${templateForm.isDefault == 'Y' }"> checked </c:if> />
									<span><s:property value="#templateForm.content"/></span>
									<input type="hidden" class="templateID" value="<s:property value='#templateForm.id' />" />
								</label>
							</li>
							</s:iterator>
							<input type="hidden" name="template"/>
						</ul>
						<div class="iphone">
							<p>
								<s:iterator value="templateList" id="templateForm" status="st">
									<c:if test="${templateForm.isDefault == 'Y' }">
										<s:property value="#templateForm.content"/>
									</c:if>
								</s:iterator>
							</p>
						</div>
						<c:if test="${enough == \"yes\"}">
							<a id="addTemplate"  href="javascript:;" ><span>添加新模板</span></a>
						</c:if>
					</div>
					<!-- E 选择发送模板 -->
					<!-- S 发送时间段 -->
					<div class="timearea">
						<label class="title">发送时间段:</label>
						<p>上午 8:00 - 晚上 8:00</p>
						<p class="green">晚上8点至早上8点不发送，延迟至次日早上8点后发送</p>
					</div>
					<!-- E 发送时间段 -->
					<!-- S 发送区域 -->
					<div class="sendarea">
						<label class="title">发送区域:</label>
						<p>
						<c:if test="${serviceAreaList == null}" >
							山东、江苏、安徽、浙江、福建、上海、宁夏、新疆、青海、陕西、甘肃、湖北、湖南、河南、江西、四川、云南、贵州、西藏、重庆、北京、天津、河北、山西、内蒙古、广东、广西、海南、辽宁、吉林、黑龙江、香港、澳门、台湾
						</c:if>
						<c:if test="${serviceAreaList != null}" >
							<s:property value="area" />
						</c:if>
						</p>
						<a class="choose" href="javascript:;">选择</a>
						<input type="hidden" value="<s:property value='area' />" name="area" />
					</div>
					<!-- E 发送区域 -->
					<!-- S 保存 返回 -->
					<div class="opts">
					<%-- 	<p>
							<label>
								<input type="checkbox" id="resend" <c:if test="${resend == 1}">checked</c:if> />
								短信发送失败，系统自动重发一次
								<input type="hidden" name="resend" value="<s:property value='resend' />" />
							</label>
						</p> --%>
						<input type="hidden" name="pos" value="${pos }">
						<p>
							<c:if test="${pos == \"down\"}">
								<a id="submit" title="立即使用" class="btn btn_a" href="javascript:;">
									<span>立即使用</span>
								</a>
							</c:if>
							<c:if test="${pos == \"up\"}">
								<a id="submit" title="保存" class="btn btn_a" href="javascript:;">
									<span>保存</span>
								</a>
							</c:if>
							<a title="返回" class="btn btn_a returnBack" href="javascript:;">
								<span>返回</span>
							</a>
						</p>
					</div>
				</form>
			</div>
			
			<script type="text/javascript">
				var params = {
					hasMsgAction: '?hasmsg=abc',		// 是否还有短信
					useNowAction: '?usenow=',			// 立即使用
					buyMsgAction: '',					// 购买短信
					pauseAction: '?pause='				// 暂停使用
				};
			</script>

			<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
			<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
			<!-- S 当前页面 JS -->
			<script type="text/javascript" src="${jsPath}/page/note_set.js?d=${str:getVersion() }"></script>
			<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
