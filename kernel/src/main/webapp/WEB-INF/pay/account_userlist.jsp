<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/acc_admin.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
	<title>账户管理 - admin</title>
		<!-- S Content -->
		<div id="content">
			
			<div id="content_bd" class="clearfix">
				<div class="box box_a" id="sear_box">
					<div class="box_bd">
					
					    <!-- initValue隐藏标签存储查询条件的初始值，若用户输入的查询条件发生改变，需要调整currentPage的value值为1 -->
			            <input type="hidden" id="flag" value="${flag}"/>
			            <input type="hidden" id="formName" value="${formName}"/>
			            <input type="hidden" id="initValue" value="startBalance:${startBalance};endBalance:${endBalance};startConsume:${startConsume};endConsume:${endConsume};checkbox1:${checkbox1};checkbox2:${checkbox2};checkbox3:${checkbox3};checkbox4:${checkbox4};checkbox5:${checkbox5}"/>
					
						<form action="accountAdmin_list.action" class="form" id="sear_form" method="post">
						     <input id="menuFlag" value="${menuFlag}" name="menuFlag" type="hidden"/>
						     <!-- 分页 -->
						     <input id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" type="hidden"/>
							<p>
								<label for="balance_range_a">余额：</label>
								<input type="text" name="startBalance" value="${startBalance}" class="input_text" id="balance_range_a" /> 
								至 
								<input type="text" name="endBalance" value="${endBalance}" class="input_text" id="balance_range_b" />
								<span id="balanceTip"></span>
							</p>
							<p>
								<label for="spend_range_a">消费额：</label>
								<input type="text" name="startConsume" value="${startConsume}" class="input_text" id="spend_range_a" /> 
								至 
								<input type="text" name="endConsume" value="${endConsume}" class="input_text" id="spend_range_b" />
								<span id="consumeTip"></span>
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
								 <a href="javascript:;" class="btn btn_a" id="sear_btn" title="查询"><span>查 询</span></a>
								 <span id="admin_selectTip"></span>
								 <span id="userType"></span>	
							</p>
							
							<p>
								<label for="balance_range_a">总余额：</label>
								<font color="red"><fmt:formatNumber value="${sysUserBalance}" type="number" pattern="#,##0.00" /></font>元
								&nbsp;&nbsp;&nbsp;&nbsp;
								<label class="reset_label">总消费额：</label>
								<font color="green"><fmt:formatNumber value="${sysAllConsume}" type="number" pattern="#,##0.00" /></font>元	
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
									<div class="th_title"><em>用户名</em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>用户类型</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>余额（元）</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>消费额（元）</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>备注</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>操作</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody>
					<c:choose>
						<c:when test="${accountUserList!=null && fn:length(accountUserList)>0 }">
							<s:iterator value="#request.accountUserList" status="account" var="usr">
							<tr>
								<input type="hidden" value="${usr.id }" name="accountUserId" class="input_id"/>
								<td class="td_a"><s:property value="#usr.userName"/></td>
								<td class="td_b">
								<s:if test='#usr.userType == "1;卖家"'>淘宝卖家</s:if>
								<s:if test='#usr.userType == "2;网点"'>网点 </s:if>
								<s:if test='#usr.userType == "2;承包区"'>承包区</s:if>
								<s:if test="#usr.userType == 4">平台用户 </s:if>
								<s:if test='#usr.userType == "1;业务账号"'>业务账号</s:if>
								</td>
								<td class="td_c">
								   <fmt:formatNumber value="${usr.useBalance}" type="number" pattern="#,##0.00" />
								</td>
								<td class="td_d">								    
								    <fmt:formatNumber value="${usr.allConsume}" type="number" pattern="#,##0.00" />
								</td>
								<td class="td_e">
								      <s:property value="#usr.remark"/>
								</td>
								<td class="td_f">
									<a href="paymentAdmin_list.action?userId=${usr.userId}&menuFlag=home_payment_list&
									    <s:if test='#usr.userType == "1;卖家"'>checkbox1=1;卖家</s:if>
										<s:if test='#usr.userType == "2;网点"'>checkbox4=2;网点 </s:if>
										<s:if test='#usr.userType == "2;承包区"'>checkbox5=2;承包区</s:if>
										<s:if test="#usr.userType == 4">checkbox2=4</s:if>
										<s:if test='#usr.userType == "1;业务账号"'>checkbox3=1;业务账号</s:if>"										
									 class="detail" target="_blank">交易明细</a>
									 
									 <a href="javascript:;" class="remark">
										<c:if test="${fn:length(usr.remark)==0}">添加备注</c:if>
										<c:if test="${fn:length(usr.remark)>0}">编辑备注</c:if>
									</a>									
								</td>
							</tr>
							</s:iterator>
							
							</c:when>						       
						       <c:otherwise>
						       
						        <c:if test="${accountUserList!=null && fn:length(accountUserList)<=0 }">
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
						<input type="hidden" value="" id="user_type_a" />
						<input type="hidden" value="" id="user_type_b" />
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
<script type="text/javascript" src="${jsPath}/page/acc_admin.js?d=${str:getVersion() }"></script>

<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->


