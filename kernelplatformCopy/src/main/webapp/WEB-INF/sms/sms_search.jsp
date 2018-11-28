<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/page/note_inquire.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<title>短信查询</title>
<script type="text/javascript">
</script>
	<!-- #include file="公共模块/header.html" -->
	
	<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">短信查询</h2> -->
				<%-- <a href="javascript:window.history.go(-1)" title="返回" class="btn btn_d" id="hd_goback"><span>返 回</span></a> --%>
			</div>
			<div id="content_bd" class="clearfix">
			
				<!-- S 查询条件 -->
				<div class="box box_a" id="sear_box">
					<div class="box_bd">
						<form action="smsSearch!searchPage.action" id="sear_form" class="form">
						<input type="hidden" name="currentPage" id="currentPage" value="<s:property value='currentPage'/>"/>
						<input type="hidden" name="smsTemplateId_" id="smsTemplateId" value="<s:property value='smsTemplateId'/>"/>
						<input type="hidden" name="shopName" id="shopName" value="<s:property value='shopName'/>"/>
						<input type="hidden" name="menuFlag" value="sms_info">
						<input type="hidden" name="isSearch" value="1">
						<input type="hidden" name="bindUserTag" value="<s:property value='bindUserTag'/>">
						<input type="hidden" name="smsId" id="_smsId" value="">
							<p>
								<span class="half_p">
									<label for="sms_type">短信分类：</label>
									<select name="smsTypeId" id="sms_type">
										<!--  
										 <option value="" <c:if test="${smsTypeId==''}">selected</c:if>>所有类型</option>
										 <c:forEach items="${smsTypes}" var="vo">
							  				<option value="${vo.id}" <c:if test="${smsTypeId==vo.id}">selected</c:if> >${vo.name}</option>
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
									<select name="smsTemplateId" id="sms_title">
										<option value="" <c:if test="${smsTemplateId==''}">selected</c:if>>所有标题</option>
									</select>
								</span>
								<span class="half_p">
									<label for="send_status">发送状态：</label>
									<select name="sendStatus" id="send_status">
									    <option value="">所有</option>
										<option value="0" <c:if test="${sendStatus==0}">selected</c:if>>发送成功</option>
										<option value="1" <c:if test="${sendStatus==1}">selected</c:if>>正在发送</option>
										<option value="2" <c:if test="${sendStatus==2}">selected</c:if>>发送失败</option>
										<%-- <option value="3" <c:if test="${sendStatus==3}">selected</c:if>>发送失败,再次发送中</option> --%>
									</select>
								</span>
							</p>
							<p>
								<label for="date_start">发送时间：</label>
								<input type="text" name="dateStart" class="Wdate" id="date_start" value="${dateStart}" /> 
								至 
								<input type="text" name="dateEnd" class="Wdate" id="date_end" value="${dateEnd}" />
								<span id="date_tip"></span>
							</p>
							<p>
								<input type="text" name="searchInput" value="${searchInput}" class="input_text" id="sear_input" />
								<a id="sear_btn" href="javascript:;" class="btn btn_a" title="查询"><span>查 询</span></a>
								<span id="sear_inputTip"></span>
							</p>
						</form>
					</div>
				</div>
				<!-- E 查询条件 -->
				
				
				<!-- S 查询表格 -->
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a">
									<div class="th_title"><em>短信类型</em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>模板标题</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>发送时间</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>发送状态</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>姓名</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>手机号</em></div>
								</th>
								<th class="th_g">
									<div class="th_title">
										<em id="now_shopName">
											<c:choose>
											   <c:when test="${fn:length(bindUserList)==0}">   
											   		店铺名称	 
											   </c:when>
											   <c:otherwise> 
											   		<c:choose>
													   <c:when test="${shopName!=null&&shopName!=''}">   
													   		${shopName}
													   </c:when>
													   <c:otherwise>  
													   		店铺名称 
													   </c:otherwise>
													</c:choose>
											   </c:otherwise>
											</c:choose>		
										</em>
										<c:if test="${fn:length(bindUserList)!=0}">   
											<ul id="shop_name" class="thead_select">
												<li><a href="javascript:;">所属店铺</a></li> 
												<c:forEach items="${bindUserList}" var="vo">
												<li><a href="javascript:;">${vo.shopName }</a></li>
												</c:forEach>
											</ul>
										</c:if>
									</div>
								</th>
								<th class="th_h">
									<div class="th_title"><em>操作</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody>
							<c:if test="${fn:length(smsInfos)==0 }">
								<tr>
									<td colspan="8"><center>${msg}	</center></td>
								</tr>
							</c:if>  
							<c:forEach items="${smsInfos}" var="vo">
								<tr>
									<td class="td_a">
										<span class="note_type">
											<c:if test="${vo.serviceType=='GOT'}"> 
												发货提醒
											</c:if>
											<c:if test="${vo.serviceType=='SENT_SCAN'}"> 
												 派件提醒
											</c:if>
											<c:if test="${vo.serviceType=='SIGNED'}"> 
												 签收提醒
											</c:if>
											<c:if test="${vo.serviceType=='QUEST'}"> 
												问题件提醒
											</c:if>
											<c:if test="${vo.serviceType=='AGING'}"> 
												 时效提醒
											</c:if>
											<c:if test="${vo.serviceType=='AGENT'}"> 
												 智能查件
											</c:if>
										</span>
									</td>
									<td class="td_b">
										<span class="note_title">
											<c:choose>
											   <c:when test="${vo.templateName==null||vo.templateName==''}">   
											   		无标题	 
											   </c:when>
											   <c:otherwise> 
											   		${vo.templateName}
											   </c:otherwise>
											</c:choose>		
										</span>
									</td>
									<td class="td_c">
										<span class="note_date"><fmt:formatDate value='${vo.sendTime}' pattern='yyyy-MM-dd'/></span>
									</td>
									<td class="td_d">
										<span class="note_status">
											<c:if test="${vo.status==0}"> 
												 发送成功
											</c:if>
											<c:if test="${vo.status==1}"> 
												 正在发送
											</c:if>
											<c:if test="${vo.status==2}"> 
												 发送失败<i class="failImg" title="${vo.errorSend}"></i>
											</c:if>
											<c:if test="${vo.status==3}"> 
												 发送失败,再次发送中
											</c:if>
										</span>
									</td>
									<td class="td_e">
										<span class="member_id">
											<img src="http://amos.im.alisoft.com/online.aw?uid=member_id_1&s=6" alt="" />${vo.buyName} 
										</span>
									</td>
									<td class="td_f">
										<span class="mobile_num">${vo.buyMobile}</span>
									</td>
									<td class="td_g">
										<span class="shop_name"> 
											<c:if test="${vo.shopName==null}"> 
												 
											</c:if>
											<c:if test="${vo.shopName!=''}"> 
												 ${vo.shopName}
											</c:if>
										</span>
									</td>
									<td class="td_h">
										<span class="operation">
											<a href="javascript:;" class="view_sms" title="查看">查看</a>
											<a href="javascript:;" class="delete_sms" title="删除">删除</a>
											<input type="hidden" class="full_sms" value="${vo.content}" />
											<input type="hidden" class="shipnum" value="<c:if test="${vo.smsTypeName=='营销定制'}">无运单号</c:if><c:if test="${vo.smsTypeName!='营销定制'}">${vo.mailNo}</c:if> " />
											<input type="hidden" class="contact" value="${vo.buyName}" />
											<input type="hidden" class="createUserName" value="${vo.createUserName}" />
											<input type="hidden" class="sms_id" value="${vo.id}" />
										</span>
									</td>
								</tr>
							</c:forEach>

						</tbody>
					</table>
				</div>
				<!-- E 查询表格 -->
				
				<!-- S PageNavi -->
					<div class="pagenavi">
						<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
					</div>
				<!-- E PageNavi -->
			</div>
			
	<script type="text/javascript">
		var params = {
			delSmsAction: 'smsSearch!deleteSmsInfo.action?menuFlag=sms_info'			// 删除短信
		}
	</script>
	
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/note_inquire.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
	
	