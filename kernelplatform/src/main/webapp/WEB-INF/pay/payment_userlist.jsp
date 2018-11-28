<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css"
	media="all" />
<link rel="stylesheet" type="text/css"
	href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css"
	href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css"
	href="${cssPath}/page/runacc_admin.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>收支记录 - admin</title>
<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
	</div>
	<div id="content_bd" class="clearfix">
		<div class="box box_a" id="sear_box">
			<div class="box_bd">
			
			    <!-- initValue隐藏标签存储查询条件的初始值，若用户输入的查询条件发生改变，需要调整currentPage的value值为1 -->
			    <input type="hidden" id="flag" value="${flag}"/>
			    <input type="hidden" id="formName" value="${formName}"/>
			    <input type="hidden" id="initValue" value="startTime:${startTime};endTime:${endTime};startMoney:${startMoney};endMoney:${endMoney};dealType:${dealType};dealStatus:${dealStatus};checkbox1:${checkbox1};checkbox2:${checkbox2};checkbox3:${checkbox3};checkbox4:${checkbox4};checkbox5:${checkbox5}"/>
					    
				<form action="paymentAdmin_list.action" class="form" id="sear_form" method="post">
				    <!-- 分页 -->
				    <input id="menuFlag" value="${menuFlag}" name="menuFlag" type="hidden"/>
				    <input id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" type="hidden"/>
				    <input id="userId" value="<s:property value='userId'/>" name="userId" type="hidden"/>
					<p>
						<label for="date_start">交易时间：</label> <input type="text"
							class="Wdate" value="${startTime}" name="startTime" id="date_start" /> 至 <input type="text"
							class="Wdate" value="${endTime}" name="endTime" id="date_end" />
					    <span id="dateTip"></span>
					</p>
					<p>
						<label for="tran_range_a">交易额：</label> <input type="text"
							class="input_text" name="startMoney" value="${startMoney}" id="tran_range_a" /> 至 <input type="text"
							class="input_text" name="endMoney" value="${endMoney}" id="tran_range_b" />
						<span id="tranRangeTip"></span>
					</p>
					<p>
						<label for="tran_type">交易类型：</label> <select name="dealType"
							id="tran_type">
							<option value="">请选择</option>
							<!-- 交易类型         0  在线充值     1 短信服务、2 时效提醒     3 购买短信  --> 	
							<option value="0" <s:if test='%{dealType=="0"}'>selected</s:if>>在线充值</option>
							<option value="1" <s:if test='%{dealType=="1"}'>selected</s:if>>短信服务</option>
							<option value="2" <s:if test='%{dealType=="2"}'>selected</s:if>>时效提醒</option>
							<option value="3" <s:if test='%{dealType=="3"}'>selected</s:if>>购买短信</option>
						</select>
					</p>
					<p>
						<label for="pay_status">支付状态：</label> <select name="dealStatus"
							id="pay_status">
							<option value="">请选择</option>
							<option value="2" <s:if test='%{dealStatus=="2"}'>selected</s:if>>已支付</option>
							<option value="0,1,3" <s:if test='%{dealStatus=="0,1,3"}'>selected</s:if>>等待支付</option>
							<!-- 用户删除状态   deal_flag -->
							<option value="4" <s:if test='%{dealStatus=="4"}'>selected</s:if>>已关闭</option>
						</select>
					</p>
					   <p>
						        <label><font color="red">*</font>用户类型：</label> 
						        <label class="reset_label">
									<input id="checkbox1" type="checkbox"  class="input_checkbox" name="checkbox1" value="1;卖家"
										<s:if test='%{checkbox1=="1;卖家" || flag==1}'>
	   									   checked
	            					  </s:if>
										/> 卖家
								</label>
								<label class="reset_label">
									<input id="checkbox2" type="checkbox"  class="input_checkbox"  name="checkbox2" value="4"
									<s:if test="%{checkbox2==4 || flag==1}">
	   									   checked
	            					  </s:if>
										/> 平台用户
								</label>
									
								<label class="reset_label">
									<input id="checkbox3" type="checkbox"  class="input_checkbox" name="checkbox3" value="1;业务账号"
										<s:if test='%{checkbox3=="1;业务账号" || flag==1}'>
	   									   checked
	            					  </s:if>
										/> 业务账号
							   </label>
									
								<label class="reset_label">
									<input id="checkbox4" type="checkbox"  class="input_checkbox"  name="checkbox4" value="2;网点"
										<s:if test='%{checkbox4=="2;网点" || flag==1}'>
	   									   checked
	            					  </s:if>
									/> 网点
								 </label>
								 
								<label class="reset_label">
									<input id="checkbox5" type="checkbox"  class="input_checkbox" name="checkbox5" value="2;承包区"
										<s:if test='%{checkbox5=="2;承包区" || flag==1}'>
	   									   checked
	            					  </s:if>
									 /> 承包区
								 </label>
						    <input type="text" name="formName" class="input_text" id="admin_select" value="${formName}"/>	
							<a href="javascript:;" class="btn btn_a" id="sear_btn" title="查询"><span>查询</span></a>
							<span id="admin_selectTip"></span>
							<span id="userType"></span>						
					  </p>
				</form>
			</div>
		</div>

		<!-- S Table -->
		<div class="table">
			<table>
				<thead>
					<tr>
						<th class="th_a">
							<div class="th_title">
								<em>用户名</em>
							</div></th>
						<th class="th_b">
							<div class="th_title">
								<em>交易号</em>
							</div></th>
						<th class="th_c">
							<div class="th_title">
								<em>交易额（元）</em>
							</div></th>
						<th class="th_d">
							<div class="th_title">
								<em>交易时间</em>
							</div></th>
						<th class="th_e">
							<div class="th_title">
								<em>付款时间</em>
							</div></th>
						<th class="th_f">
							<div class="th_title">
								<em>交易类型</em>
							</div></th>
						<th class="th_g">
							<div class="th_title">
								<em>操作</em>
							</div></th>
					</tr>
				</thead>
				
				<tbody>
			<c:choose>
				<c:when test="${paymentList!=null && fn:length(paymentList)>0 }">
					<s:iterator value="#request.paymentList" status="payment" var="pm">
						<tr>
							<td class="td_a"><s:property value="#pm.userName" />
							</td>
							<td class="td_b"><s:property value="#pm.id" />
							</td>
							<td class="td_c">
							     <c:if test="${pm.dealType == 0}">
										<fmt:formatNumber value="${pm.dealMoney}" type="number" pattern="#,##0.00" />
								</c:if>													
								 <c:if test="${pm.dealType == 1 || pm.dealType == 2 || pm.dealType == 3 }">
										 <fmt:formatNumber value="${pm.promCost}" type="number" pattern="#,##0.00" />
								</c:if>	
								</td>
							
							<td class="td_d">
							     <fmt:formatDate value="${pm.dealTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td class="td_e">
							   <s:if test="#pm.dealStatus==0||#pm.dealStatus==1||#pm.dealStatus==3">
									<span class="red">等待支付</span>
							   </s:if>
							    <s:if test="#pm.dealStatus==2">
									<span class="red">已支付</span>
									<fmt:formatDate value="${pm.payTime }" pattern="yyyy-MM-dd HH:mm:ss"/> 
							   </s:if>
							   <s:if test="#pm.dealStatus==4">
									<span class="red">已关闭</span>
							   </s:if>
							 
							</td>
							<td class="td_f">
							    <!-- 交易类型         0  在线充值     1 短信服务、2 时效提醒     3 购买短信  --> 									 
								<s:if test="#pm.dealType==0">
									<span>在线充值</span>
								</s:if> 
								<s:if test="#pm.dealType==1">
									<span>短信服务</span>
								</s:if>
								<s:if test="#pm.dealType==2">
									<span>时效提醒</span>
								</s:if>
								<s:if test="#pm.dealType==3">
									<span>购买短信</span>
								</s:if>
							</td>
							<td class="td_g">
							    <a href="javascript:;" class="view">查看明细</a>
								<input type="hidden" class="tran_num" value="<s:property value="#pm.id"/>" /> 
								<input type="hidden" class="acc_name" value="<s:property value="#pm.userName"/>" />
								<s:if test='#pm.userType=="1;卖家"'>
									<input type="hidden" class="user_type" value="卖家" />
								</s:if>
								<s:if test="#pm.userType==4">
									<input type="hidden" class="user_type" value="平台用户" />
								</s:if>
								<s:if test='#pm.userType=="1;业务账号"'>
									<input type="hidden" class="user_type" value="业务帐号" />
								</s:if>
								<s:if test='#pm.userType=="2;网点"'>
									<input type="hidden" class="user_type" value="网点" />
								</s:if>
								<s:if test='#pm.userType=="2;承包区"'>
									<input type="hidden" class="user_type" value="承包区" />
								</s:if>
								<c:if test="${pm.dealType == 0}">
								    <input type="hidden" class="money" value="<fmt:formatNumber value="${pm.dealMoney}" type="number" pattern="#,##0.00" />" /> 
								</c:if>
								 <c:if test="${pm.dealType == 1 || pm.dealType == 2 || pm.dealType == 3 }">
									<input type="hidden" class="money" value="<fmt:formatNumber value="${pm.promCost}" type="number" pattern="#,##0.00" />" /> 
								</c:if>	
								<input type="hidden" class="tran_time" value="<fmt:formatDate value="${pm.dealTime}" pattern="yyyy年MM月dd日 HH:mm:ss"/>" />
							    <input type="hidden" class="pay_time"  value="<fmt:formatDate value="${pm.payTime}" pattern="yyyy年MM月dd日 HH:mm:ss"/>" /> 
								<!-- 交易类型         0  在线充值     1 短信服务、2 时效提醒     3 购买短信  --> 	 		
								<s:if test="#pm.dealType==0">
									<input type="hidden" class="tran_type" value="在线充值" /> 
								</s:if>
								<s:if test="#pm.dealType==1">
									<input type="hidden" class="tran_type" value="短信服务" /> 
								</s:if>
								<s:if test="#pm.dealType==2">
									<input type="hidden" class="tran_type" value="时效提醒" /> 
								</s:if>
								<s:if test="#pm.dealType==3">
									<input type="hidden" class="tran_type" value="购买短信" /> 
								</s:if>
								<input type="hidden" class="tran_name" value="<s:property value='#pm.dealName'/>" />
							</td>
						</tr>
					</s:iterator>
					</c:when>			       
					<c:otherwise>						      
						  <c:if test="${paymentList!=null && fn:length(paymentList)<=0 }">
						    <tr onMouseOver="this.style.background='#D1EEEE'" onMouseOut="this.style.background=''">
								<td colspan="7" align="center">
									抱歉，没有找到您要的记录
								</td>
							</tr>
						 </c:if>	
				    </c:otherwise>					       
			</c:choose>	
				</tbody>
			</table>
			
			<div class="pagenavi">
				<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
				<div class="clear"></div>
			</div>
			
		</div>
		<!-- E Table -->
	</div>
	
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript"
	src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/runacc_admin.js?d=${str:getVersion() }"></script>

<!-- E 当前页面 JS -->
	
</div>
<!-- E Content -->


