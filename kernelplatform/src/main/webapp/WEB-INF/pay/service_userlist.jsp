<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/acc_admin.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
	<title>服务管理 - admin</title>
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">服务管理 - admin</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a" id="sear_box">
					<div class="box_bd">
					
					    <!-- initValue隐藏标签存储查询条件的初始值，若用户输入的查询条件发生改变，需要调整currentPage的value值为1 -->
			            <input type="hidden" id="def" value="${def}"/>
			            <input type="hidden" id="formName" value="${formName}"/>
			            <input type="hidden" id="myService" value="${serviceIds}"/>
			            <input type="hidden" id="initValue" value="startTime:${startTime};endTime:${endTime};checkbox1:${checkbox1};checkbox2:${checkbox2};checkbox3:${checkbox3};checkbox4:${checkbox4};checkbox5:${checkbox5}"/>
			            
						<form action="serviceAdmin_list.action" class="form" id="sear_form" method="post">
						    <input id="menuFlag" value="${menuFlag}" name="menuFlag" type="hidden"/>
						    <!-- 分页 -->
						    <input id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" type="hidden"/>
							<p>
								<label><font color="red">*</font>开通服务：</label>	 							 						   									    	
								<s:iterator value="#request.payServiceList" status="st" var="psl">
								    <s:set name="myValue" value="#psl.Id" />		
									<label class="reset_label">		
									   <s:if test="%{def==1}"> 						      
				   						   <input type="checkbox" name="serviceIds" class="input_checkbox" value="<s:property value='#psl.Id'/>" checked  />
				            		   </s:if>	
				            		   <s:if test="%{def==99}"> 
				   						    <input type="checkbox" name="serviceIds" class="input_checkbox" value="<s:property value='#psl.Id'/>" 								    
										       <s:iterator value="#request.serviceIds" status="status" var="serviceId">										         
										           <s:if test="%{#serviceId==#myValue}">
				   									   checked
				            					  </s:if>							      
									           </s:iterator>								          
									         />
				            		  </s:if>		  
									  <s:property value="#psl.name"/>
									</label>
								</s:iterator>
								<span id="serviceType"></span>	
							</p>
							<p>
								<label for="date_start">开通时间：</label>
								<input type="text" class="Wdate" id="date_start" value="${startTime}" name="startTime" /> 
								至 
								<input type="text" class="Wdate" id="date_end" value="${endTime}" name="endTime"/>
								
								<span id="dateTip"></span>
							</p>
							<p>
								<label><font color="red">*</font>用户类型：</label>
								<label class="reset_label">
									<input id="checkbox1" type="checkbox" class="input_checkbox" value="1;卖家" name="checkbox1"
										<s:if test='%{checkbox1=="1;卖家" || def==1}'>
		   									   checked
		            					  </s:if>
									/> 卖家
								</label>
								<label class="reset_label">
									<input id="checkbox2" type="checkbox" class="input_checkbox" value="4" name="checkbox2"
										<s:if test="%{checkbox2==4 || def==1}">
	   									   checked
	            					  </s:if>
									/> 平台用户
								</label>
								<label class="reset_label">
									<input id="checkbox3" type="checkbox" class="input_checkbox" value="1;业务账号" name="checkbox3"
										<s:if test='%{checkbox3=="1;业务账号" || def==1}'>
	   									   checked
	            					  </s:if>
									/> 业务账号
								</label>
								<label class="reset_label">
									<input id="checkbox4" type="checkbox" class="input_checkbox" value="2;网点" name="checkbox4"
										<s:if test='%{checkbox4=="2;网点" || def==1}'>
	   									   checked
	            					  </s:if>
									/> 网点
								</label>
								<label class="reset_label">
									<input id="checkbox5" type="checkbox" class="input_checkbox" value="2;承包区" name="checkbox5"
									  <s:if test='%{checkbox5=="2;承包区" || def==1}'>
	   									   checked
	            					  </s:if>
									 /> 承包区
								</label>
								<input type="text" name="formName" class="input_text" id="admin_select" value="${formName}"/>								
								<a title="查 询" class="btn btn_a" id="sear_btn" href="javascript:;"><span>查 询</span></a> 
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
									<div class="th_title"><em>用户名</em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>用户类型</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>服务名称</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>开通时间</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>服务周期</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>备注</em></div>
								</th>
								<th class="th_g">
									<div class="th_title"><em>操作</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody>
					<c:choose>
						<c:when test="${dredgeServiceList!=null && fn:length(dredgeServiceList)>0 }">
						<s:iterator value="#request.dredgeServiceList" status="st" var="usr">
							<tr>
								<input type="hidden" name="dredgeServiceId" class="dre_id" value="${usr.id}"/>
								<td class="td_a">
									<s:property value="#usr.userName"/>
								</td>
								<td class="td_b">
									<s:if test='#usr.userType == "1;卖家"'>淘宝卖家</s:if>
									<s:if test='#usr.userType == "2;网点"'>网点 </s:if>
									<s:if test='#usr.userType == "2;承包区"'>承包区</s:if>
									<s:if test="#usr.userType == 4">平台用户 </s:if>
									<s:if test='#usr.userType == "1;业务账号"'>业务帐号</s:if>
								</td>
								<td class="td_c">
									<s:property value="#usr.serviceName"/>
								</td>
								<td class="td_d">
									<fmt:formatDate value="${usr.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td class="td_e">
									<s:if test="#usr.circle!=null">
										<s:if test="#usr.circle==0">月</s:if>										
										<s:if test="#usr.circle==1">季</s:if>										
										<s:if test="#usr.circle==2">半年</s:if>
										<s:if test="#usr.circle==3">年</s:if>
									</s:if>								
								</td>
								<td class="td_f">								
									<span class="status">		
										<c:if test="${fn:length(usr.remark)>=5}">			
										    <span class="sub_remark">${fn:substring(usr.remark, 0, 5)}...</span>									
												<div class="close_tip" style="">
													<div class="mid"><p>${usr.remark}</p></div>
													<div class="fd" ></div>
												</div>
										</c:if>	
										<c:if test="${fn:length(usr.remark)<5}">
										   <span class="sub_remark">${usr.remark}</span>									
										</c:if>
									</span>								
								</td>
								<td class="td_g">
								    <a href="javascript:;" class="remark">
										<c:if test="${fn:length(usr.remark)==0}">添加备注</c:if>
										<c:if test="${fn:length(usr.remark)>0}">编辑备注</c:if>
									</a>																
								</td>
							</tr>
							</s:iterator>
							
							</c:when>						       
						       <c:otherwise>
						        <c:if test="${dredgeServiceList!=null && fn:length(dredgeServiceList)<=0 }">
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
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript"
	src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/serve_admin.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
			
			
		</div>
		<!-- E Content -->


