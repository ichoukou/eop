<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link href="${cssPath}/page/outlets.css?d=${str:getVersion() }" rel="stylesheet" type="text/css">
	<!-- E 当前页面 CSS -->

	<script type="text/javascript">
		//显示略掉的信息
		function Show(divid) {
			if($("#"+divid).text()!="")
				document.getElementById(divid).style.visibility = "visible";
		}
	
		//隐藏
		function Hide(divid) {
			document.getElementById(divid).style.visibility = "hidden";
		}
	</script>
		<!-- S Content -->
		<div id="content">
		<input type="hidden" name="menuFlag" value="${menuFlag }" />
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">网点查找</h2> -->
				<em>查找圆通在全国的网点分布信息！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
				
			</div>
			<div id="content_bd" class="clearfix">

				<!-- S Box -->
				<div class="box box_a">
					<div class="box_bd">
					<form action="branchsolr.action" id="sear_form" method="post">
						<p>
							<p class="btn_box"><input type="text" class="input_text" name="searchKey" id="input_text_demo"  <c:choose><c:when test="${searchKey==null }">value="请输入关键字"</c:when> <c:otherwise>value="${searchKey }"</c:otherwise> </c:choose> onblur="if(this.value=='') this.value='请输入关键字'" onfocus="if(this.value=='请输入关键字') this.value=''">
							<!-- <a href="#" id="sear_btn" class="btn btn_a" title="查 询"><span>查 询</span></a> -->
							<span class="btn btn_a"><input value="查询" id="sear_btn" type="button"></span> <span class="btn btn_a"><input value="返 回" id="back_btn" type="button"></span><span id="input_text_demoTip"></span></p>
							
						</p>
						<input type="hidden" id="menuFlag" name="menuFlag" value="${menuFlag }" />
						<input name="userType" id="userType" type="hidden" value="${yto:getCookie('userType')}" />
						<input name="userState" id="userState" type="hidden" value="${yto:getCookie('userState')}" />
						<input name="userField003" id="userField003" type="hidden" value="${yto:getCookie('field003')}"/>
						<input name="currentPage" id="currentPage" type="hidden" value="${currentPage }"/>
					</form>
					</div>
				</div>
				<!-- E Box -->


				<!-- S Table -->
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a">
									<div class="th_title"><em>公司名称</em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>经理</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>查询电话</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>问题件电话</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>派送范围</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>不派送范围</em></div>
								</th>
								<th class="th_g">
									<div class="th_title"><em>派送时限</em></div>
								</th>
								<th class="th_h">
									<div class="th_title"><em>操作</em></div>
								</th>
							</tr>
						</thead>
						
						
						<tbody>
							<c:choose>
								<c:when test="${branchItemList!=null && fn:length(branchItemList) > 0 }">
									<c:forEach items="${branchItemList }" var="branch" varStatus="branSt">
										<tr class="defaultTr">
											<td class="td_a">${branch.provice }${branch.city }${branch.companyName }</td>
											<td class="td_b">${branch.managerName }${branch.managerPhone }</td>
											<td class="td_c" onMouseOver="Show('infoDiv5_${branch.id}');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv5_${branch.id}')">
												<c:if test="${fn:length(branch.servicePhone) <= 10 }">${branch.servicePhone }</c:if>
												<c:if test="${fn:length(branch.servicePhone) > 10 }">${str:multiSubStr(branch.servicePhone,10)}</c:if>
												<div id="infoDiv5_${branch.id}" class="article" style="width:150px;">${branch.servicePhone }</div>
											</td>
											<td class="td_d" onMouseOver="Show('infoDiv4_${branch.id}');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv4_${branch.id}')">
												<c:if test="${fn:length(branch.questionPhone) <= 10 }">${branch.questionPhone }</c:if>
												<c:if test="${fn:length(branch.questionPhone) > 10 }">${str:multiSubStr(branch.questionPhone,10) }</c:if>
												<div id="infoDiv4_${branch.id}" class="article" style="width:150px;">${branch.questionPhone }</div>
											</td>
											<td class="td_e" onMouseOver="Show('infoDiv_${branch.id}');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv_${branch.id}')">
												${str:filterSubStr(branch.sendScope,10) }
												<div id="infoDiv_${branch.id}" class="article" style="width:150px;">${branch.sendScope }</div>
											</td>
											<td class="td_f" onMouseOver="Show('infoDiv2_${branch.id}');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv2_${branch.id}')">
												<c:if test="${fn:length(branch.unSendScope) <= 10 }">${branch.unSendScope }</c:if>
												<c:if test="${fn:length(branch.unSendScope) > 10 }">${str:multiSubStr(branch.unSendScope,10) }</c:if>
												<div id="infoDiv2_${branch.id}" class="article" style="width:150px;">${branch.unSendScope }</div>
											</td>
											<td class="td_g" onMouseOver="Show('infoDiv3_${branch.id}');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv3_${branch.id}')">
												<c:if test="${fn:length(branch.sendTimeLimit) <= 10 }">${branch.sendTimeLimit }</c:if>
												<c:if test="${fn:length(branch.sendTimeLimit) > 10 }">${str:multiSubStr(branch.sendTimeLimit,10) }</c:if>
												<div id="infoDiv3_${branch.id}" class="article" style="width:150px;">${branch.sendTimeLimit }</div>
											</td>
											<td class="td_h"><span><a href="#" class="list_tr">纠错</a></span></td>
										</tr>
										<tr class="detail_tr" style="display:none;">
											<td class="td_a"><textarea name="" class="textarea_text1" id="textarea_demo1" cols="100" rows="5">${branch.provice }${branch.city }${branch.companyName }</textarea></td>
											<td class="td_b"><textarea name="" class="textarea_text2" id="textarea_demo2" cols="100" rows="5">${branch.managerName }${branch.managerPhone }</textarea></td>
											<td class="td_c"><textarea name="" class="textarea_text3" id="textarea_demo3" cols="100" rows="5">${branch.servicePhone }</textarea></td>
											<td class="td_d"><textarea name="" class="textarea_text4" id="textarea_demo4" cols="100" rows="5">${branch.questionPhone }</textarea></td>
											<td class="td_e"><textarea name="" class="textarea_text5" id="textarea_demo5" cols="100" rows="5">${branch.sendScope }</textarea></td>
											<td class="td_f"><textarea name="" class="textarea_text6" id="textarea_demo6" cols="100" rows="5">${branch.unSendScope }</textarea></td>
											<td class="td_g"><textarea name="" class="textarea_text7" id="textarea_demo7" cols="100" rows="5">${branch.sendTimeLimit }</textarea></td>
											<td class="td_h">
			                                <span><a href="#" class="list_tr2">关闭</a></span>
			                                <span><a href="#" onclick="notificationManager('${branch.id }',this)" class="list_tr3">通知管理员</a></span>
			                                </td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<c:if test="${index != 1 }">
										<tr onMouseOver="this.style.background='#D1EEEE'" onMouseOut="this.style.background=''">
											<td colspan="8" align="center">
												抱歉，找不到您要找的网点
											</td>
										</tr>
									</c:if>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
				<!-- E Table -->

				<!-- S PageNavi -->
				<div class="pagenavi">
					<!-- <a href="#" title="首页" class="page_txt">&laquo; 首页</a>
					<span class="page_cur">1</span>
					<a href="javascript:;" title="2" class="page_num">2</a>
					<a href="javascript:;" title="3" class="page_num">3</a>
					<a href="javascript:;" title="4" class="page_num">4</a>
					<a href="javascript:;" title="5" class="page_num">5</a>
					<a href="javascript:;" title="下一页" class="page_txt">下一页</a>
					<a href="javascript:;" title="末页" class="page_txt">末页 &raquo;</a>
					<span class="page_total">共 <em>231</em> 页 / 共 <em>2306</em> 条</span> -->
					
					<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
				</div>
				<!-- E PageNavi -->
            
				
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
    <script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
    <script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/outlets.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
			
		</div>
		<!-- E Content -->
		

