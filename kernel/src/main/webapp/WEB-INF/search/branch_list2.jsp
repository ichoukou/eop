<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link href="${cssPath}/page/outlets.css" rel="stylesheet" type="text/css">
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
<!-- 				<h2 id="message_icon">网点查找</h2>
				<em>查找圆通在全国的网点分布信息！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
 -->				
 		<span id="message_icon" style="font-size: 16px;">派送范围</span>&mdash;&mdash;&mdash;&mdash;不用再为超区烦恼，节省了不少快递成本哦~
 				<font color="red" style="padding-top: 5px; display: block;">温馨提示:找出自己的网点信息，点击&lt;占为己有&gt;后，以后可以维护自己网点派送范围了~</font>
			</div>
			<div id="content_bd" class="clearfix">

				<!-- S Box -->
				<div class="box box_a">
					<div class="box_bd">
					<form action="branchsolr.action" id="sear_form" method="post">
						<p>
							<p class="btn_box"><input type="text" class="input_text" name="searchKey" id="input_text_demo"  <c:choose><c:when test="${searchKey==null }">value="请输入关键字"</c:when> <c:otherwise>value="${searchKey }"</c:otherwise> </c:choose> onblur="if(this.value=='') this.value='请输入关键字'" onfocus="if(this.value=='请输入关键字') this.value=''">
							<span class="btn btn_a"><input value="查询" id="sear_btn" type="button"></span> <span class="btn btn_a"><input value="返 回" id="back_btn" type="button"></span><span id="input_text_demoTip"></span></p>
							
						</p>
						<input type="hidden" id="menuFlag" name="menuFlag" value="${menuFlag }" />
						<input name="userType" id="userType" type="hidden" value="${yto:getCookie('userType')}" />
						<input name="userState" id="userState" type="hidden" value="${yto:getCookie('userState')}" />
						<input name="userField003" id="userField003" type="hidden" value="${yto:getCookie('field003')}"/>
						<input name="currentPage" id="currentPage" type="hidden" value="${currentPage }"/>
						<input name="isHave" id="_isHave" type="hidden" value=""/>
						<input name="haveId" id="_haveId" type="hidden" value=""/>
					</form>         
                    	<c:choose>
							<c:when test="${myBranch != null}">
								<input value="${myBranch.id}" id="myBranchId" type="hidden">
								<input value="${myBranch.companyName}" id="myCompanyName" type="hidden">
								<input value="${myBranch.managerPhone}" id="myManagerPhone" type="hidden">
								<input value="${myBranch.servicePhone}" id="myServicePhone" type="hidden">
								<input value="${myBranch.questionPhone}" id="myQuestionPhone" type="hidden">
								<input value="${myBranch.sendScope}" id="mySendScope" type="hidden">
								<input value="${myBranch.unSendScope}" id="myUnSendScope" type="hidden">
								<input value="${myBranch.sendTimeLimit}" id="mySendTimeLimit" type="hidden">
								<div class="web_pop">
								 	<span class="web_span"><span class="btn btn_a"><input value="查看您的网点信息" type="button" id="dialog_demo_my" /></span></span>
								 </div>					
							</c:when>
							<c:otherwise>
									<div class="web_pop">
	                   				 	<span class="web_span">找不到您的网点信息，请</span><span class="btn btn_a"><input value="新增" type="button" id="dialog_demo_c" /></span>
	                    			</div>
	                    		<!--<c:if test="${branchItemList != null && fn:length(branchItemList) == 0 }">
                  				</c:if>-->
							</c:otherwise>
						</c:choose>
                    
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
								<c:if test="${branchItemList!=null && fn:length(branchItemList) > 0 }">
									<c:forEach items="${branchItemList }" var="branch" varStatus="branSt">
										<tr>
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
											<td class="td_f" style="width:88px" onMouseOver="Show('infoDiv2_${branch.id}');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv2_${branch.id}')">
												<c:if test="${fn:length(branch.unSendScope) <= 10 }">${branch.unSendScope }</c:if>
												<c:if test="${fn:length(branch.unSendScope) > 10 }">${str:multiSubStr(branch.unSendScope,10) }</c:if>
												<div id="infoDiv2_${branch.id}" class="article" style="width:150px;">${branch.unSendScope }</div>
											</td>
											<td class="td_g" onMouseOver="Show('infoDiv3_${branch.id}');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv3_${branch.id}')">
												<c:if test="${fn:length(branch.sendTimeLimit) <= 10 }">${branch.sendTimeLimit }</c:if>
												<c:if test="${fn:length(branch.sendTimeLimit) > 10 }">${str:multiSubStr(branch.sendTimeLimit,10) }</c:if>
												<div id="infoDiv3_${branch.id}" class="article" style="width:150px;">${branch.sendTimeLimit }</div>
											</td>
											<td class="td_h" style="width:50px">
												<c:choose>
													<c:when test="${(branch.branchCode == yto:getCookie('id') || (flag2=='isFalse' && branch.branchCode == 0))||(isHave=='qx'&&haveId==branch.id)}">
														<c:choose>
															<c:when test="${branch.branchCode == yto:getCookie('id')&&!(isHave=='qx'&&haveId==branch.id)}">
																无法操作		<!--  <span><a href="javascript:cancelEmploy('${branch.id }')" id="dialog_demo_d">取消占有</a></span>-->
															</c:when>
															<c:otherwise>
																<span><a href="javascript:employ('${branch.id }')" id="dialog_demo_a">占为己有</a></span>
																<span><a href="#" class="list_tr">纠错</a></span>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														无法操作
													</c:otherwise>
												</c:choose>

                                			</td>
										</tr>
										<tr class="detail_tr" style="display:none;">
											<td class="td_a"><textarea name="" class="textarea_text" id="textarea_demo1" cols="100" rows="5">${branch.provice }${branch.city }${branch.companyName }</textarea></td>
											<td class="td_b"><textarea name="" class="textarea_text" id="textarea_demo2" cols="100" rows="5">${branch.managerName }${branch.managerPhone }</textarea></td>
											<td class="td_c"><textarea name="" class="textarea_text" id="textarea_demo3" cols="100" rows="5">${branch.servicePhone }</textarea></td>
											<td class="td_d"><textarea name="" class="textarea_text" id="textarea_demo4" cols="100" rows="5">${branch.questionPhone }</textarea></td>
											<td class="td_e"><textarea name="" class="textarea_text" id="textarea_demo5" cols="100" rows="5">${branch.sendScope }</textarea></td>
											<td class="td_f"><textarea name="" class="textarea_text" id="textarea_demo6" cols="100" rows="5">${branch.unSendScope }</textarea></td>
											<td class="td_g"><textarea name="" class="textarea_text" id="textarea_demo7" cols="100" rows="5">${branch.sendTimeLimit }</textarea></td>
											<td class="td_h">
			                                <span><a href="#" class="list_tr2">关闭</a></span>
			                                <span><a href="#" onclick="save('${branch.id }',this)" id="dialog_demo_b">保存</a></span>
			                                </td>
			                            </tr>
									</c:forEach>
								</c:if>
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
			
		<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
		<%-- <jsp:include page="/activateVipPopup.jsp"></jsp:include> --%>
	
	
	
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
		
	<script type="text/javascript">
		var params = {
			onStep:3
		};
	</script>