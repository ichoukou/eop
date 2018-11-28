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

<script type="text/javascript">
function Show(divid) {
    if($("#"+divid).text()!="")
        document.getElementById(divid).style.visibility = "visible";
}
function Hide(divid) {
    document.getElementById(divid).style.visibility = "hidden";
}
</script>

<title>短信查询</title>
<script type="text/javascript">
</script>
	<!-- #include file="公共模块/header.html" -->
	
	<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">短信查询</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
			
				<!-- S 查询条件 -->
				<div class="box box_a" id="sear_box">
					<div class="box_bd">
						<form action="smsSearchAdmin_smsSearchAdmin.action" id="sear_form" class="form">
						<input type="hidden" name="currentPage" id="currentPage" value="<s:property value='currentPage'/>"/>
						<input type="hidden" name="smsTemplateId_" id="smsTemplateId" value="<s:property value='smsTemplateId'/>"/>
						<input type="hidden" name="menuFlag" value="sms_search_admin">
							<p>
								<span class="half_p">
									<label for="sms_type">短信分类：</label>
									<select name="smsTypeId" id="sms_type">
										<option value="" <c:if test="${smsTypeId==''}">selected</c:if>>所有类型</option>		
										<option value="GOT" <c:if test="${smsTypeId=='GOT'}">selected</c:if>>发货提醒</option>
										<option value="SENT_SCAN" <c:if test="${smsTypeId=='SENT_SCAN'}">selected</c:if>>派件提醒</option>
										<option value="SIGNED" <c:if test="${smsTypeId=='SIGNED'}">selected</c:if>>签收提醒</option>
										<option value="QUEST" <c:if test="${smsTypeId=='QUEST'}">selected</c:if>>问题件提醒</option>
										<option value="AGING" <c:if test="${smsTypeId=='AGING'}">selected</c:if>>时效提醒</option>
										<option value="AGENT" <c:if test="${smsTypeId=='AGENT'}">selected</c:if>>智能查件</option>
									</select>
								</span>
								<span class="half_p">
									<label for="date_start">发送时间：</label>
									<input type="text" name="dateStart" class="Wdate" id="date_start" value="${dateStart}" /> 
									至 
									<input type="text" name="dateEnd" class="Wdate" id="date_end" value="${dateEnd}" />
									<span id="date_tip"></span>
								</span>
							</p>
							<p>
								<span class="half_p">
									<label for="shopName">店铺名称：</label>
									<input type="text" name="shopName" id="shopName" class="input_text" value="${shopName}" />
								</span>
								<span class="half_p">
									<label for="send_status">发送状态：</label>
									<select name="sendStatus" id="send_status">
									     <option value="">所有</option>
										<option value="0" <c:if test="${sendStatus==0}">selected</c:if>>发送成功</option>
										<option value="1" <c:if test="${sendStatus==1}">selected</c:if>>正在发送</option>
										<option value="2" <c:if test="${sendStatus==2}">selected</c:if>>发送失败</option>
										<!-- <option value="3" <c:if test="${sendStatus==3}">selected</c:if>>发送失败,再次发送中</option> -->
									</select>
								</span>
								
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
												<c:forEach items="${bindUserList}" var="vo">
												<li><a href="javascript:;">${vo.shopName }</a></li>
												</c:forEach>
											</ul>
										</c:if>
									</div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>短信类型</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>发送状态</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>联系人</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>联系电话</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>发送时间</em></div>
								</th>
								<th class="th_g">
									<div class="th_title"><em>短信内容</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody>
							<c:if test="${fn:length(smsInfos)==0 }">
								<tr>
									<td colspan="8"><center>${msg}	</center></td>
								</tr>
							</c:if>  
							<c:forEach items="${smsInfos}" var="vo" varStatus="status">
								<tr>
									<td class="td_a">
										<span class="shop_name"> 
											<c:if test="${vo.shopName==null}"> 
												 
											</c:if>
											<c:if test="${vo.shopName!=''}"> 
												 ${vo.shopName}
											</c:if>
										</span>
									</td>
									<td class="td_b">
										<span class="note_type">
										<!-- ${vo.smsTypeName} -->
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
									<td class="td_c">
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
									<td class="td_d">
										<span class="member_id">
											<img src="http://amos.im.alisoft.com/online.aw?uid=member_id_1&s=6" alt="" />${vo.buyName} 
										</span>
									</td>
									<td class="td_e">
										<span class="mobile_num">${vo.buyMobile}</span>
									</td>
									<td class="td_f">
										<span class="note_date"><fmt:formatDate value='${vo.sendTime}' pattern='yyyy-MM-dd'/></span>
									</td>
									<td class="td_g" <c:if test="${fn:length(vo.content) >= 20 }"> onMouseOver="Show('orderIndex_${status.index}'); this.style.cursor= 'pointer'" onMouseOut="Hide('orderIndex_${status.index}');" </c:if> >
										<span class="sms_content">
											<c:if test="${fn:length(vo.content) < 20 }">
												${vo.content}
											</c:if>
											<c:if test="${fn:length(vo.content) >= 20 }">
												${fn:substring(vo.content,0,15) }...
											</c:if>
										</span>
										<div id="orderIndex_${status.index}" class="article" style="width:140px;">${vo.content}</div>
										
										
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
			

	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/note_inquire_admin.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
	