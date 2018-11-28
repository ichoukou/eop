<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/timeout.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>时效提醒</title>

<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">
<!-- 			时效提醒 -->
		</h2>
		<em>让你及时了解卖家催件信息，及时处理并回复卖家催件消息！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a> </em>
		<form action="" method="post" id="myform">
			<input type="hidden" name="menuFlag" value="${menuFlag }" />
			<input id="tabNum" type="hidden" value="<s:property value="tabNum"/>" />
			<input id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" type="hidden"/>
			<input type="hidden" name="queryCondition" value="<s:property value="queryCondition"/>" />
		</form>
	</div>
	<div id="content_bd" class="clearfix">
		<form action="#" id="sear_form">
			<div class="war_p1">
				<input type="text" class="input_text" id="input_text_text" value="<s:property value="queryCondition"/>"/> 
				<input type="hidden" id="returnQueryCondition" value="<s:property value="queryCondition"/>"/>
				<a href="javascript:;" class="btn btn_a" title="筛选" id="sear_btn"><span>筛选</span></a>
				<span id="input_text_textTip"></span>
				<input type="hidden" id="flag" name="flag" value="<s:property value="flag"/>">
			</div>
		</form>
		<!-- S Tab -->
		<div class="tab tab_a">
			<div class="tab_triggers">
				<ul>
					<li><a href="javascript:;"><span id="weiHui">未回复</span></a>
					</li>
					<li><a href="javascript:;"><span id="yiHui">已回复</span></a>
					</li>
				</ul>
			</div>
			<div class="tab_panels">
				<div class="tab_panel">
					<!-- S Table -->
					<div class="table">
						<table style="TABLE-LAYOUT: fixed">
							<thead>
								<tr>
									<th class="th_a">
										<div class="th_title">
											<em>运单号</em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>问题描述</em>
										</div></th>
									<th class="th_c">
										<div class="th_title">
											<em>收件人信息</em>
										</div></th>
									<th class="th_d">
										<div class="th_title">
											<em>处理记录</em>
										</div></th>
									<th class="th_e">
										<div class="th_title">
											<em>回复卖家</em>
										</div></th>
								</tr>
							</thead>

							<tbody>
								<s:if test="reportIssueList != null && reportIssueList.size > 0">
									<s:iterator value="reportIssueList" status="stuts" var="report">
										<tr>
											<td class="td_a"><a href="javascript:;" title="查看运单详情" style="text-decoration:underline;color:blue" class="mailno" val="<s:property value="#report.mailNo" />"><span><s:property
														value="#report.mailNo" />
											</span></a>
											</td>
											<td class="td_b2">
												<div class="war_div">
													<div class="openDiv"><s:property value="#report.issueDesc" /></div>
													<div class="b_war">
														<span>上报人：</span>
														<span><s:property value="#report.seller.shopName" /></span>
														<span><s:property value="#report.seller.mobilePhone" /></span>
													</div>
												</div></td>
											<td class="td_c3">
												<div>
													<span><s:property value="#report.buyerName" />
													</span>
													<div class="b_war2">
														<span><s:property value="#report.buyerMobile" />
														</span>
													</div>
												</div></td>
											<td class="td_d3" style="word-wrap: break-word;">

													<%-- <s:if test="#report.allOper == null || report.allOper == '' ">
														<div class="openDiv1" >${report.showOper}</div>
													</s:if>
													<s:else>
														<div class="openDiv">${report.showOper}
															<p style="text-align:center;"><a class="open" style="cursor:pointer;"> >>展开<< </a></p>
														</div>
													</s:else>
													<div class="closeDiv" style="width:250px;height:210px;display:none;position:absolute;">${report.allOper}
														<p style="text-align:center;"><a class="close" style="cursor:pointer;"> >>收起<< </a></p>
													</div> --%>
													
													<div class="openDiv">${report.allOper}</div>
											</td>
											<td class="td_e2">
												<form action="" method="post">
													<div class="msgbg">
														<textarea class="mstxt" cols="" rows="" scroll="no"></textarea>
														<input type="hidden" class="issueId" value="<s:property value="#report.id"/>">
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
														<a href="javascript:;" class="btn btn_c send_msg1" title="发送"><input type="hidden" class="tabNum" value="0"><span>发送</span></a>
													</div> 
												</form>	
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:else>
									<tr>
										<td colspan="5" align="center"><span id="tips1">对不起！没有查询到任何未回复信息</span></td>
									</tr>
								</s:else>
							</tbody>
						</table>
					</div>
					<!-- E Table -->
					<!-- S PageNavi -->
						<div  class="pagenavi">
							<jsp:include page="/WEB-INF/page.jsp"/>
						</div>
					<!-- E PageNavi -->
				</div>
				<!-- E tab_panel-->
				<div class="tab_panel" style="display: none;">

					<!-- S Table -->
					<div class="table">
						<table style="TABLE-LAYOUT: fixed">
							<thead>
								<tr>
									<th class="th_a">
										<div class="th_title">
											<em>运单号</em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>问题描述</em>
										</div></th>
									<th class="th_c">
										<div class="th_title">
											<em>收件人信息</em>
										</div></th>
									<th class="th_d">
										<div class="th_title">
											<em>处理记录</em>
										</div></th>
									<th class="th_e">
										<div class="th_title">
											<em>发送消息</em>
										</div></th>
								</tr>
							</thead>

							<tbody>
								<s:if test="reportIssueList != null && reportIssueList.size > 0">
									<s:iterator value="reportIssueList" status="stuts" var="report">
										<tr>
											<td class="td_a"><a href="javascript:;" title="查看运单详情" style="text-decoration:underline;color:blue" class="mailno" val="<s:property value="#report.mailNo" />"><span><s:property
														value="#report.mailNo" />
											</span></a>
											</td>
											<td class="td_b2">
												<div class="war_div">
													<div class="openDiv"><s:property value="#report.issueDesc" /></div>
													</span>
													<div class="b_war">
														<span><s:property value="#report.branch.text" />
														</span> <span>联系电话：<s:property
																value="#report.branch.phone" />
														</span>
													</div>
												</div></td>
											<td class="td_c3">
												<div>
													<span><s:property value="#report.buyerName" />
													</span>
													<div class="b_war2">
														<span><s:property value="#report.buyerMobile" />
														</span>
													</div>
												</div></td>
											<td class="td_d3">

													<%-- <s:if test="#report.allOper == null || report.allOper == '' ">
														<div class="openDiv1" >${report.showOper}</div>
													</s:if>
													<s:else>
														<div class="openDiv">${report.showOper}
															<p style="text-align:center;"><a class="open" style="cursor:pointer;"></a></p>
														</div>
													</s:else>
													<div class="closeDiv">${report.allOper}
														<p style="text-align:center;"><a class="close" style="cursor:pointer;"></a></p>
													</div> --%>
													
													<div class="openDiv">${report.allOper}</div>
												</td>
											<td class="td_e2">
												<form action="" method="post">
													<div class="msgbg">
														<textarea class="mstxt" cols="" rows="" scroll="no"></textarea>
														<input type="hidden" class="issueId" value="<s:property value="#report.id"/>">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<a href="javascript:;" class="btn btn_c send_msg2" title="发送"><span>发送</span><input type="hidden" class="tabNum" value="1"></a>
													</div> 
												</form>	
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:else>
									<tr>
										<td colspan="5" align="center"><span id="tips2">对不起！没有查询到任何已回复信息</span></td>
									</tr>
								</s:else>
							</tbody>
						</table>
					</div>
					<!-- E Table -->
					<!-- S PageNavi -->
						<div  class="pagenavi">
							<jsp:include page="/WEB-INF/page.jsp"/>
						</div>
					<!-- E PageNavi -->

				</div>
				<!-- E tab_panel2 -->
			</div>
		</div>
		<!-- E Tab -->
	</div>
	
	<script>
		var params = {
			tabIndex : document.getElementById('tabNum').value
		}
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/timeout.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
</div>
<!-- E Content -->



