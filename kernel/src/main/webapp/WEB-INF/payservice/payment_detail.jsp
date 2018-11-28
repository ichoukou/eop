<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />	
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/payment_detail.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	
	
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a" id="sear_box">
					<div class="box_bd">
					
					    <!-- initValue隐藏标签存储查询条件的初始值，若用户输入的查询条件发生改变，需要调整currentPage的value值为1 -->
					    <input type="hidden" id="initValue" value="startTime:${startTime};endTime:${endTime};dealType:${dealType}"/>
					    					    
						<form id="sear_form" action="payService_getPaymentList.action" class="form" method="post" >
						    <input id="menuFlag" value="${menuFlag}" name="menuFlag" type="hidden"/>
						    <input id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" type="hidden"/>
						    <input id="userId" value="${userId}" name="userId" type="hidden"/>
							<p>
								<label for="date_start">交易时间：</label>
								<input type="text" class="Wdate" id="date_start"  name="startTime" value="${startTime}"/> 至								 
								<input type="text" class="Wdate" id="date_end"  name="endTime" value="${endTime}"/>												
								<span id="dateTip"></span>
							
								<!-- 交易类型         0  在线充值     1 短信服务、2 时效提醒     3 购买短信  --> 
								交易类型：
								<select id="dealType" name="dealType">
								    <option value="-1" <c:if test='${dealType == "-1"}'>selected</c:if>>请选择</option>
								    <option value="0" <c:if test='${dealType == "0"}'>selected</c:if>>在线充值</option>
								    <option value="1" <c:if test='${dealType == "1"}'>selected</c:if>>短信服务</option>
								    <option value="2" <c:if test='${dealType == "2"}'>selected</c:if>>时效提醒</option>
								    <option value="3" <c:if test='${dealType == "3"}'>selected</c:if>>购买短信</option>
								</select>
								
								<a title="查 询" class="btn btn_a" id="sear_btn" href="javascript:;"><span>查 询</span></a>		
							</p>
						</form>
					</div>
				</div>
				
				<!-- S Table -->
			    <div class="table">
			        <input type="hidden" id="payment_id" value="${id}" /> <!-- 用于高亮显示的交易明细ID -->
					<table>
						<thead>
							<tr>
								<th class="th_a"><div class="th_title"><em>交易号</em></div></th>
								<th class="th_b"><div class="th_title"><em>交易名称</em></div></th>
								<th class="th_c"><div class="th_title"><em>交易时间</em></div></th>
								<th class="th_d"><div class="th_title"><em>金额（元）</em></div></th>
								<th class="th_e"><div class="th_title"><em>交易类型</em></div></th>
								<th class="th_f"><div class="th_title"><em>操作人</em></div></th>
								<th class="th_g"><div class="th_title"><em>状态</em></div></th>
							</tr>
						</thead>
						<tbody>
						    <c:choose>						    
						       <c:when test="${pagination.records!=null && fn:length(pagination.records)>0}">
						            <c:forEach items="${pagination.records}" var="record" varStatus="status">
										<tr>
											<td class="td_a">
											     <span class="tran_num">${record.id}</span>
											</td>
											<td class="td_b">
											     <span class="tran_name">${record.dealName}</span>
											</td>									
											<td class="td_c">
											    <span class="tran_date">
											        <fmt:formatDate value="${record.dealTime}" pattern="yyyy-MM-dd HH:mm:ss" />
											    </span>
											</td>
											<td class="td_d">
											     <span class="tran_amount">
											     	<c:if test="${record.dealType == 0}">
														 <fmt:formatNumber value="${record.dealMoney}" type="number" pattern="#,##0.00" />
													</c:if>													
											         <c:if test="${record.dealType == 1 || record.dealType == 2 || record.dealType == 3 }">
											          <fmt:formatNumber value="${record.promCost}" type="number" pattern="#,##0.00" />
											         </c:if>	
											     </span>
											     <input type="hidden" value="${record.dealMoney }" class="dealMoney">
											</td>
											<td class="td_e">
											    <!-- 交易类型         0  在线充值     1 短信服务、2 时效提醒     3 购买短信  --> 											    
												<span class="tran_type">
													<c:if test="${record.dealType == 0}">
														    在线充值
													</c:if>
												    <c:if test="${record.dealType == 1}">
														     短信服务
													</c:if>
													<c:if test="${record.dealType == 2}">
														    时效提醒
													</c:if>
													<c:if test="${record.dealType == 3}">
														     购买短信
													</c:if>
												 </span>
												 <hidden name="shopName" class="shop_name" value="${record.shopName}">
											</td>
											<td class="td_f">
											   <!-- 操作人  ${record.createrId} -->
											   <c:if test="${record.userNameText!=null}">
												    ${record.userNameText}
											   </c:if>
											    <c:if test="${record.userNameText==null && record.userName!=null}">
												    ${record.userName}
											   </c:if>
											</td>
											<td class="td_g">									   
		                                        <!-- 交易状态	0等待支付、1正在充值、2 已完成、3失败、4已关闭 -->		
		                                        		
											    <c:if test="${record.dealStatus == 0 || record.dealStatus == 1}">	
											     <c:if test="${record.delFlag == 0}">
											            <span class="status">
															<span class="unpaid pay_status">未支付</span>
															<input type="hidden" class="recordId" value="${record.id}">		
															<a href="javascript:;" class="pay_bill">付款</a>
															<a href="javascript:;" class="close_bill">关闭</a>																											
															<span class="close_tip">如果您不想对该订单付款，可以关闭该订单，订单将变为失效状态；订购中心会在一定时间后自动关闭未付款的订单</span>
														</span>
														<span class="limit_date">											
														    <fmt:formatDate value="${record.limitTime}" pattern="yyyy-MM-dd"/>前有效
														</span>
											     </c:if>
											      <c:if test="${record.delFlag == 1 || record.delFlag == 2}">
											         <span class="limit_date">未付款 已关闭</span>									       
												  </c:if>	   							
											    </c:if>		
											    <c:if test="${record.dealStatus == 2}">
												    <span class="status">
														<span class="pay_status">已完成</span>												
													</span>				
											    </c:if>
											    
											    <c:if test="${record.dealStatus == 3}">
												    <span class="status">
														<span class="unpaid pay_status">失败</span>												
													</span>											
											    </c:if>
											    
											    <c:if test="${record.dealStatus == 4}">
												    <span class="status">
												        <span class="limit_date">未支付   已关闭</span>																									
													</span>											
											    </c:if>
											    
		                                    </td>									
										</tr>
									</c:forEach>						
						       </c:when>						       
						       <c:otherwise>
						            <tr onMouseOver="this.style.background='#D1EEEE'" onMouseOut="this.style.background=''">
									    <td colspan="6" align="center">
											抱歉，没有找到您要的记录
										</td>
									</tr>
						       </c:otherwise>						       
						    </c:choose>					
						</tbody>
					</table>
				</div>				
				<!-- E Table -->
				<c:if test="${pagination.records!=null && fn:length(pagination.records)>0}">
					<div class="pagenavi">
					    <jsp:include page="/WEB-INF/page.jsp"></jsp:include>
				   </div>
			   </c:if>			  
			</div>
			<script type="text/javascript">
				var params = {
					onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
						? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
					userId:${yto:getCookie('id')},						//当前登录用户的id
					userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
					infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
					bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
					pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
				}
	        </script>
	        <script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion()}"></script>
	        <script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion()}"></script>
	        <script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion()}"></script>
	        
	        <!-- S 当前页面 JS -->
			<script type="text/javascript" src="${jsPath}/page/payment_detail.js?d=${str:getVersion() }"></script>
			<!-- E 当前页面 JS -->
		</div>
		
		<!-- E Content -->
	

