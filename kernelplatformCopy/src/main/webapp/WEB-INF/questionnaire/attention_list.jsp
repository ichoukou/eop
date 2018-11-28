<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/attention.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

	
	<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">我的关注</h2> -->
				<em>在运单监控时将运单加关注，减少您的重复工作。<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a>
				</em>
			</div>
			<div id="content_bd" class="clearfix">
				
				
				<!-- S Box -->
				<div class="box box_a">
					<div class="box_bd">
						<form action="#" id="sear_form">
							<input type="hidden" name="menuFlag" value="${menuFlag }" />
							<p>
								<input type="text" class="input_text" id="input_text_text" />
								<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查 询"><span>查 询</span></a>
								<span id="input_text_textTip"></span>
							</p>
						</form>
				
					</div>
				</div>
				<!-- E Box -->
				<!-- S Table -->
				<div class="table" id="table_list">
					<table>
						<thead>
							<tr>
								<th class="th_a">
									<div class="th_title"><em><input class="input_checkbox checked_all" type="checkbox"></em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>运单号</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>当前位置（时间）</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>状态</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>买家姓名</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>买家电话</em></div>
								</th>
								<th class="th_g">
									<div class="th_title"><em>收货地址</em></div>
								</th>
								<th class="th_h">
									<div class="th_title"><em class="arrow_down">发货时间</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody id="tbody">
							<c:choose>
								<c:when test="${fn:length(monitoreList) > 0}">
									<c:forEach items="${monitoreList }" var="monitor">
									<tr>
										<td class="td_a"><input class="input_checkbox" name="checkAttention" value="${monitor.id}" type="checkbox"></td>
										<td class="td_b">
											<a href="javascript:;" class="mailno" title="查看订单详情" style="text-decoration:underline;color:blue">
												${monitor.mailNO }
											</a>
										</td>
										<td class="td_c" title="${monitor.stepInfo.acceptAddress}">
											<c:choose>
												<c:when test="${str:isNotEmpty(monitor.stepInfo.acceptAddress)}">
													${monitor.stepInfo.acceptAddress}&nbsp;${monitor.stepInfo.remark}&nbsp;${monitor.stepInfo.acceptTime}
												</c:when>
												<c:otherwise>
													<img src="${imagesPath}/icons/noneStep.PNG" title="暂无走件信息，请稍后查询"/>
												</c:otherwise>
											</c:choose>
										</td>
										<td class="td_d">
											<c:choose>
												<c:when test="${monitor.status == 0}">
													接单中 <fmt:formatDate value="${monitor.senderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
												</c:when>
												<c:otherwise>
												<c:choose>
												<c:when test="${monitor.status == 'UPDATE'}">
													更新
												</c:when>
												<c:when test="${monitor.status == 'WITHDRAW'}">
													取消
												</c:when>
												<c:when test="${monitor.status == 'ACCEPT'}">
													接单
												</c:when>
												<c:when test="${monitor.status == 'GOT'}">
													揽收成功
												</c:when>
												<c:when test="${monitor.status == 'NOT_SEND'}">
													揽收失败
												</c:when>
												<c:when test="${monitor.status == 'SENT_SCAN'}">
													派件扫描
												</c:when>
												<c:when test="${monitor.status == 'FAILED'}">
													失败
												</c:when>
												<c:when test="${monitor.status == 'SIGNED'}">
													已签收
												</c:when>
												</c:choose>
												<fmt:formatDate value="${monitor.acceptTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
												</c:otherwise>
											</c:choose>
										</td>
										<td class="td_e">${monitor.userName }</td>
										<td class="td_f">${monitor.phone }</td>
										<td class="td_g">${str:multiSubStr(monitor.destination,12) }</td>
										<td class="td_h"><fmt:formatDate value="${monitor.senderTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="8" style="text-align:center;">您还没有关注运单！</td>
									</tr>
								</c:otherwise>
							</c:choose>
							
						</tbody>
					</table>
				</div>
				<!-- E Table -->
				<div class="attention">
				<input class="input_checkbox checked_all" type="checkbox" />
				<a href="javascript:;" id="cancelBtn" class="btn btn_a" title="取消关注"><span>取消关注</span></a></div>
						<!-- S PageNavi -->
						<div class="pagenavi">
<%-- 							<%@ include file="/WEB-INF/page.jsp" %> --%>
								共<s:property value='monitoreList.size()'/>条
								
 						</div>
						<!-- E PageNavi -->
			</div>
			
<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/page/attention.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
	
		</div>
		<!-- E Content -->