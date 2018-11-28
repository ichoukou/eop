<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/template_audit.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>模板审核 - admin</title>

<!-- S Content -->

<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">模板审核 - admin</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
				<div id="sear_box" class="box box_a">
					<div class="box_bd">
						<form action="" method="POST" class="form" id="sear_form">
							<p>
								<span class="half_p">
									<label for="remind_type">提醒类型：</label>
									<select id="remind_type" name="serviceName">
										<option value="发货提醒" <c:if test="${serviceName eq '发货提醒' }">selected</c:if> >发货提醒</option>
										<option value="派件提醒" <c:if test="${serviceName eq '派件提醒' }">selected</c:if> >派件提醒</option>
										<option value="签收提醒" <c:if test="${serviceName eq '签收提醒' }">selected</c:if> >签收提醒</option>
<%-- 										<option value="营销定制" <c:if test="${serviceName eq '营销定制' }">selected</c:if> >营销定制</option> --%>
<%-- 										<option value="问题件通知" <c:if test="${serviceName eq '问题件通知' }">selected</c:if>>问题件通知</option> --%>
									</select>
								</span>
								<span class="half_p">
									<label for="status">审核状态：</label>
									<select id="status" name="status">
										<option value="" <c:if test="${status eq '' }">selected</c:if> >所有状态</option>
										<option value="M" <c:if test="${status eq 'M' }">selected</c:if> >等待审核</option>
										<option value="N" <c:if test="${status eq 'N' }">selected</c:if> >审核失败</option>
										<option value="Y" <c:if test="${status eq 'Y' }">selected</c:if> >审核通过</option>
									</select>
								</span>
							</p>
							<p>
								<label for="shop_name">店铺名称：</label>
								<input type="text" id="shop_name" name="shopName" class="input_text" value="${shopName }"/>
								<a title="查询" class="btn btn_a" href="javascript:;" id="sear_btn"><span>查 询</span></a>
								<span id="shop_nameTip"></span>
							</p>
							<input type="hidden" id="currentPage" name="currentPage" value="<s:property value="currentPage"/>">
						</form>
					</div>
				</div>
				
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a first_th">
									<div class="th_title"><em>模板内容</em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>提醒类型</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>提交时间</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>所属店铺</em>
									</div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>审核状态</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>操作</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody>
						<c:choose>
							<c:when test="${resultString eq 'init'}"></c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${templates == null || fn:length(templates) <= 0 }"><tr><td colspan="6" align="center">抱歉！无记录！</td></tr></c:when>
									<c:otherwise>
										<c:forEach items="${templates }" var="template" varStatus="status">
											<tr>
												<td class="td_a">
													<span class="temp_content">${template.content }</span>
												</td>
												<td class="td_b"><span <c:if test="${template.isDefault eq 'Y' }">class="tem_title"</c:if>>${template.serviceName }</span></td>
												<td class="td_c">
													<span class="submit_time"><fmt:formatDate value="${template.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
												</td>
												<td class="td_d">${template.shopName }</td>
												<td class="td_e">
													<c:if test="${template.status eq 'Y' }"><span class="wait status">审核通过</span></c:if>
													<c:if test="${template.status eq 'N' }"><span class="wait status">审核失败</span></c:if>
													<c:if test="${template.status eq 'M' }"><span class="wait status">等待审核</span></c:if>
												</td>
												<td class="td_f">
													<span class="operate">
														
														<c:if test="${template.status eq 'M' || template.status eq 'N' }">
															<a href="javascript:;" class="do_pass">通过</a>
														</c:if>
														<c:if test="${template.status eq 'Y' }">
															<span class="do_pass gray">通过</span>
														</c:if>
														
														<c:if test="${template.status eq 'M' || template.status eq 'Y' }">
															<a href="javascript:;" class="do_not_pass">不通过</a>
														</c:if>
														<c:if test="${template.status eq 'N' }">
															<span class="do_not_pass gray">不通过</span>
														</c:if>

													<!-- 
													<a href="javascript:;" 
													<c:if test="${template.status eq 'M' || template.status eq 'N' }">class="do_pass"</c:if>
													<c:if test="${template.status eq 'Y' }">class="do_pass gray"</c:if>
													>通过</a>
													<a href="javascript:;" 
													<c:if test="${template.status eq 'M' || template.status eq 'Y' }">class="do_not_pass"</c:if>
													<c:if test="${template.status eq 'N' }">class="do_not_pass gray"</c:if>
													>不通过</a>
													 -->
													<input type="hidden" class="template_id" value="${template.id }" />
													
													</span>
												</td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
				</div>
				<c:choose>
					<c:when test="${resultString eq 'init'}"></c:when>
					<c:otherwise>
						<!-- S PageNavi -->
						 <div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
						<!-- E PageNavi -->
					</c:otherwise>
				</c:choose>
			</div>
			

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript"
	src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<script type="text/javascript"
	src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript"
	src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<script type="text/javascript"
	src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/template_audit.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->

			
			
			
		</div>

<!-- E Content -->
