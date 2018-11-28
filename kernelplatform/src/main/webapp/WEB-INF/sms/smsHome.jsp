<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/note_home.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>短信服务</title>


		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">1.购买短信>2.设置短信>3.开始使用</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S 服务 -->
				<div id="l_side">
					<!-- S 使用中的功能 -->
					<c:if test="${openFlg == true }" >
					<div id="in_use" class="box box_a">
						<div class="box_hd">
							<strong>在使用中的短信</strong>
						</div>
						<div class="box_bd">
							<table>
								<form id="serviceCan_Form" action="#" method="post">
								<input type="hidden" name="menuFlag" value="${menuFlag }">
								<input type="hidden" id="isOn" name="isOn" value="<s:property value='isOn' />"/>
								<s:iterator value="serviceList" id="serviceForm" status="st">
								<c:if test="${serviceForm.isOn == 'Y'}">
									<tr>
										<td class="td_a">

											<div class="serve_name">
												<img class="serve_icon" src="<s:property value='#serviceForm.imageUrl'/>" alt="" />
												<em><s:property value='#serviceForm.name'/></em>
												<p><s:property value='#serviceForm.introduction'/></p>
												<span class="expire">上午 8:00 - 晚上 8:00</span>
											</div>
										</td>
										<td class="td_b">
											<c:if test="${serviceForm.name != '问题件通知' }">
												<a title="设置" class="btn btn_a set" href="javascript:;"><span>设 置</span></a>
											</c:if>
<%-- 											${tips }-${serviceForm.createUserId} --%>
											<%-- <c:choose>
											 	<c:when test="${serviceForm.createUserId eq tips }">
											 		<a title="暂停使用" class="btn btn_disable pause" href="javascript:;" disabled="disabled"><span>暂停使用</span></a>
											 	</c:when>
											 	<c:otherwise> --%>
											 		<a title="暂停使用" class="btn btn_a pause" href="javascript:;"><span>暂停使用</span></a>
<%-- 											 	</c:otherwise> --%>
<%-- 											 </c:choose> --%>
											
											<input type="hidden" class="fun_name" value="<s:property value='#serviceForm.name'/>" />
											<input type="hidden" class="fun_id" value="<s:property value='#serviceForm.id'/>" />
										</td>
									</tr>
								</c:if>
								</s:iterator>
								</form>
							</table>
						</div>
						
					</div>
					</c:if>
					<!-- E 使用中的功能 -->
					
					<!-- S 未使用的功能 -->
					<c:if test="${closeFlg == true }" >
					<div id="not_in_use" class="box box_a">
						<div class="box_hd">
							<strong>未使用的短信</strong>
						</div>
						<div class="box_bd">
							<table>
								<form id="serviceCannot_Form" action="#" method="post">
								<input type="hidden" id="smsUsecount" name="smsUsecount" value="<s:property value='smsUsecount'/>"/>
								<input type="hidden" id="isOn2" name="isOn" value="<s:property value='isOn' />"/>
								<% int num = 0;
								%>
								<s:iterator value="serviceList" id="serviceForm" status="st">
								<c:if test="${serviceForm.isOn == 'N'}">
									<c:if test="<%=num%2 == 0%>" > <% num =0; %><tr></c:if>
										<td>
											<% num++;
											%>
											<div class="serve_name">
												<img class="serve_icon" src="<s:property value='#serviceForm.imageUrl'/>" alt="" />
												<em>
													<s:property value='#serviceForm.name'/><a href="javascript:;" class="use_now">设置</a>
													<input type="hidden" class="fun_name" value="<s:property value='#serviceForm.name'/>" />
													<input type="hidden" class="fun_id" value="<s:property value='#serviceForm.id'/>" />
												</em>
												<p><s:property value='#serviceForm.introduction'/></p>
											</div>
										</td>
								    <c:if test="<%=num == 2%>" ></tr></c:if>
								</c:if>
								</s:iterator>
								<c:if test="<%=num == 1%>" ></tr></c:if>
								</form>
							</table>
						</div>
					</div>
					</c:if>
					<!-- E 未使用的功能 -->
				</div>
				<!-- E 服务 -->
				
				<!-- S 我的短信 -->
				<div id="r_side">
					<div id="my_acc" class="box box_a">
						<div class="box_hd">
							<strong>我的短信</strong>
						</div>
						<div class="box_bd">
							<!-- S 可发短信 -->
							<div id="balance" class="clearfix">
								<em>可发短信</em>
								<strong><span id="acc_num"><s:property value="smsMessageCount"/></span> 条</strong>
							</div>
							<!-- E 可发短信 -->
							
							<!-- S 已发送 -->
							<div id="sended">
								<span>今日发送<a target="blank" href="smsSearch!searchPage.action?menuFlag=sms_info&isSearch=1&type=0">(<em><s:property value="todayCount"/></em>)</a></span>
								<span>当月发送<a target="blank" href="smsSearch!searchPage.action?menuFlag=sms_info&isSearch=1&type=1">(<em><s:property value="monthTotal" /></em>)</a></span>
								<span>当月日均发送(<em><s:property value="monthAverage"/></em>)</span>
							</div>
							<!-- E 已发送 -->
							
							<div id="msg_manage">
								<a title="模板设置" class="btn btn_a" href="template_list.action?menuFlag=sms_template_list"><span>短信模板</span></a>
							<%-- 	<a title="客户管理" class="btn btn_a" href="buyers!toIndex.action?menuFlag=buyers_manage"><span>客户管理</span></a> --%>
								<a title="购买短信套餐" class="btn btn_a" href="smsServiceMarket!inBuyPorts.action?menuFlag=sms_package"><span>购买短信</span></a>
							</div>
						</div>
					</div>
					
					<!-- S 短信查询 -->
					<div id="msg_sear" class="box box_a">
						<div class="box_hd">
							<strong>短信查询</strong>
						</div>
						<div class="box_bd">
							<form action="" class="form" id="msg_sear_form" method="post">
								<p>
									<label for="msg_type">短信类型：</label>
									<select name="" id="msg_type">
										<!--  
										<option value="">所有类型</option>
										<c:forEach items="${serviceList}" var="service">
											<option value="${service.id}">${service.name}</option>
										</c:forEach>
										-->
										<option value="" <c:if test="${smsTypeId==''}">selected</c:if>>所有类型</option>		
										<option value="GOT" <c:if test="${smsTypeId=='GOT'}">selected</c:if>>发货提醒</option>
										<option value="SENT_SCAN" <c:if test="${smsTypeId=='SENT_SCAN'}">selected</c:if>>派件提醒</option>
										<option value="SIGNED" <c:if test="${smsTypeId=='SIGNED'}">selected</c:if>>签收提醒</option>
										<option value="QUEST" <c:if test="${smsTypeId=='QUEST'}">selected</c:if>>问题件提醒</option>
										<option value="AGING" <c:if test="${smsTypeId=='AGING'}">selected</c:if>>时效提醒</option>
										<option value="AGENT" <c:if test="${smsTypeId=='AGENT'}">selected</c:if>>智能查件</option>
									</select>
									<input type="hidden" name="smsTypeId" value="<s:property value='smsTypeId' />" />
									<input type="hidden" name="sendStatus" value="" />
								</p>
								<p>
									<label for="">开始时间：</label>
									<input type="text" class="Wdate" id="date_start" value="<s:property value='startTime' />" />
									<input type="hidden" name="dateStart" value="<s:property value='dateStart' />" />
								</p>
								<p>
									<label for="">结束时间：</label>
									<input type="text" class="Wdate" id="date_end" value="<s:property value='endTime' />" />
									<input type="hidden" name="dateEnd" value="<s:property value='dateEnd' />" />
								</p>
								<textarea name="" id="sear_textarea" class="textarea_text"></textarea>
								<input type="hidden" name="searchInput" value="<s:property value='searchInput' />" />
								<input type="hidden" name="isSearch" value="1">
								
								<a title="查询" class="btn btn_a" id="sear_msg_btn" href="javascript:;"><span>查 询</span></a>
							</form>
						</div>
					</div>
					<!-- S 短信查询 -->
				</div>
				<!-- E 我的短信 -->
			</div>
			
			
			<script type="text/javascript">
				var params = {
					hasMsgAction: '?hasmsg=abc',		// 是否还有短信
					useNowAction: '?usenow=',			// 立即使用
					buyMsgAction: 'smsServiceMarketAction_inBuyPorts.action',
					//buyMsgAction: 'smsServiceMarketAction_inBuyPorts.action',
					//smsServiceMarketAction: 'smsServiceMarketAction_inBuyPorts.action',// 购买短信
					pauseAction: '?menuFlag=sms_home&pause='				// 暂停使用
				};
			</script>

			<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
			<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
			<!-- S 当前页面 JS -->
			<script type="text/javascript" src="${jsPath}/page/note_home.js?d=${str:getVersion() }"></script>
			<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
