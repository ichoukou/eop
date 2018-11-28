<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/problem_war.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>问题件预警</title>

<!-- S Content -->
<div id="content" style="position:relative;">
<div id="divWarn" style="background:#fff;position:absolute;width:300px;height:300px;z-index:100;left:200px;top:-30px;display:none;"></div>
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">
			时效提醒
		</h2>
		<em>
		<c:choose>
			<c:when test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 4}">
			1.设置时效预警值 >2.查找超过预警值的订单 > 3.发消息提醒网点处理订单
			</c:when>
			<c:when test="${yto:getCookie('userType') == 2}">
			时效提醒，让你及时了解卖家催件信息，及时处理并回复卖家催件消息
			</c:when>
		</c:choose>
		
		<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a>
		</em> <input id="uid" type="hidden" value="${uid}"> 
		<form action="" method="post" id="myform">
			<input type="hidden" name="menuFlag" value="${menuFlag }" />
			<input id="tabNum" type="hidden" value="<s:property value='tabNum'/>" />
			<input id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" type="hidden"/>
			<input type="hidden" name="queryCondition" value="<s:property value="queryCondition"/>" >
			<input id="userId" name="userId" type="hidden" value="${userId }"> <!-- 关联店铺customerId -->
		</form>
	</div>
	<div id="content_bd" class="clearfix">

		<!-- S Tab -->
		<div class="tab tab_a">
			<div class="tab_triggers">
				<ul>
					<li><a href="javascript:;"><span id="wei">时效监控</span></a>
					</li>
					<li><a href="javascript:;"><span id="yi">已通知网点</span>
					</a>
					</li>
				</ul>
			</div>
			<div class="tab_panels">
				<div class="tab_panel" >
					<p style="position:relative;" class="war_p1">1、预警值：预计快件从揽收到签收所用的时间&nbsp;&nbsp;
						 <a class="btn btn_a" title="设置" id="dialog_demo_c" style="position:absolute;top:-1px;left:300px;">
						 	<span>设置预警值</span>
						</a>
					</p>
					<p class="war_p1">2、已用时间：从揽收扫描到现在所用的时间</p>
					<p class="war_p1">3、以下快件的已用时间大于预警值</p>
					<p class="war_p1">
						
					</p>
					<form action="#" id="sear_form">
						<input type="hidden" name="menuFlag" value="${menuFlag }" />
						<div class="war_p1">
							<input type="text" class="input_text" id="input_text_text" name="queryCondition" value="<s:property value='queryCondition'/>" /> <input type="hidden" id="returnQueryCondition" value="<s:property value='queryCondition'/>"/>
							<a
								href="javascript:;" class="btn btn_a" title="筛选" id="sear_btn"><span>筛选</span>
							</a> <span id="input_text_textTip"></span>&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red;">鼠标移动至运单信息行，运单号下方会显示通知网点链接，点击链接发送通知网点信息</span>
						</div>
					</form>
					
					<!-- S Table -->
					<div class="table" >
						<table>
							<thead>
								<tr>
									<th class="th_a">
										<div class="th_title">
											<em>运单号</em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>店铺名称</em>
											<ul class="thead_select">
												<c:forEach items="${shopNames }" var="bind" varStatus="vSta">
													<li value="${bind.id }">
														${bind.shopName }
													</li>
												</c:forEach>
												 <li value=0><a href="#">设置选项</a></li>
											</ul>
										</div></th>
									<th class="th_c">
										<div class="th_title">
											<em>当前位置</em>
										</div></th>
									<th class="th_e">
										<div class="th_title">
											<em>买家姓名</em>
										</div></th>
									<th class="th_f">
										<div class="th_title">
											<em>买家电话</em>
										</div></th>
									<th class="th_g">
										<div class="th_title">
											<em>收货地址</em>
										</div></th>
									<th class="th_h">
										<div class="th_title">
											<em>揽收时间</em>
										</div></th>
									<th class="th_i">
										<div class="th_title">
											<em>已用时间</em>
										</div></th>
								</tr>
							</thead>
							
							<tbody>
								
								<s:if test="passIssueDTOList != null && passIssueDTOList.size() > 0">
									<s:iterator value="passIssueDTOList" status="stuts" var="pass">
										<tr>
											<td class="td_a table_tr">
												<span class="ship_number">
												<a href="javascript:;" title="查看运单详情" style="text-decoration:underline;color:blue" class="mailno" val="<s:property value='#pass.mailNo' />">
												<s:property value="#pass.mailNo"/></a>
												</span>
												<input type="hidden" class="mailNo" value="<s:property value='#pass.mailNo'/>">
												<s:if test="#pass.tips == 0">
													<span
														class="war_c1"><a href="#">通知网点</a>
													</span>
												</s:if>
												<s:if test="#pass.tips == 1">
													<img class="ques" style="cursor:pointer;" src="${imagesPath}/single/ques_order22×22.png">
													<span class="fenQuTime" style="display:none;"><s:date name='#pass.questionFenQuTime' format='yyyy-MM-dd'/></span>
												</s:if>
											</td>
											<td class="td_b table_tr"><s:property value="#pass.shopName"/></td>
											
											<s:if test="#pass.stepInfo.acceptAddress!=null">
	                                               <td class="td_d table_tr">
                                                       <s:property value="#pass.stepInfo.acceptAddress"/>&nbsp;<s:property value="#pass.stepInfo.remark"/><br>
                                           			   <s:property value="#pass.stepInfo.acceptTime.substring(0,10)"/>&nbsp;<s:property value="#pass.stepInfo.acceptTime.substring(10,18)"/>
                                            		   <s:if test="#pass.stepInfo.contactWay!=null && #pass.stepInfo.contactWay!=''"><s:property value="#pass.stepInfo.contactWay"/></s:if>
													   <input type="hidden" class="status" value="<s:property value='#pass.stepInfo.status'/>">
	                                               </td>
	                                         </s:if>
                                             <s:else>
                                                <td class="td_d table_tr" align="center">
                                                     <img src="${imagesPath}/icons/noneStep.PNG" title="暂无走件信息，请稍后查询"/>
                                                 </td>
                                             </s:else>
											
											<td class="td_e table_tr">
												<s:property value="#pass.name"/>
												<input type="hidden" class="name" value="<s:property value='#pass.name'/>">
											</td>
											<td class="td_f table_tr">
												<s:property value="#pass.phone"/>
												<input type="hidden" class="phone" value="<s:property value='#pass.phone'/>">
											</td>
											<td class="td_g table_tr">
												<s:property value="#pass.address"/>
												<input type="hidden" class="address" value="<s:property value='#pass.address'/>">
											</td>
											<td class="td_h table_tr">
											<span><s:property value="#pass.sendTime1"/></span>
											<%-- <span><s:property value="#pass.sendTime2"/></span> --%>
											</td>
											<td class="td_i table_tr"><s:property value="#pass.userTime"/>
													<s:if test="#pass.userTime2 <= 1 && #pass.userTime2 > 0">
														<img src="${imagesPath}/single/1d.png" title="超出预警值<s:property value='#pass.passTime'/>">
													</s:if>
													<s:elseif test="#pass.userTime2 <= 2" >
 														<img src="${imagesPath}/single/2d.png" title="超出预警值<s:property value='#pass.passTime'/>">
 													</s:elseif> 
 													<s:elseif test="#pass.userTime2 <= 3 " > 
 														<img src="${imagesPath}/single/3d.png" title="超出预警值<s:property value='#pass.passTime'/>"> 
 													</s:elseif> 
 													<s:elseif test="#pass.userTime2 <= 4 " > 
 														<img src="${imagesPath}/single/4d.png" title="超出预警值<s:property value='#pass.passTime'/>"> 
 													</s:elseif> 
 													<s:elseif test="#pass.userTime2 <= 5 " > 
														<img src="${imagesPath}/single/5d.png" title="超出预警值<s:property value='#pass.passTime'/>"> 
													</s:elseif> 
													<s:elseif test="#pass.userTime2 <= 6 " > 
														<img src="${imagesPath}/single/6d.png" title="超出预警值<s:property value='#pass.passTime'/>"> 
													</s:elseif> 
													<s:else>
														<img src="${imagesPath}/single/7d.png" title="超出预警值<s:property value='#pass.passTime'/>">
													</s:else> 
												
											</td>
										</tr>  
									</s:iterator> 
								</s:if>
								<s:else>
									<tr><td colspan="9" align="center"><span class="tips" >
									<s:if test="notWarnValueFlag == 'trueStr'">暂无数据，你需要先<a href="#" id="setWarnValue">设置预警值</a></s:if>
									<s:else>亲，现在没有大于预警值的运单。</s:else>
									</span></td></tr>
								</s:else>
							</tbody>
						</table>
					</div>
					
					<!-- E Table -->
					<!-- S PageNavi -->
					<div  class="pagenavi" id="pagenavi1">
						<jsp:include page="/WEB-INF/page.jsp"/>
					</div>
					<!-- E PageNavi -->
				</div>
				<!-- E tab_panel-->
				<div class="tab_panel" style="display: none;"><br>
					<p class="mar1 pad1">
						<input type="text" class="input_text" name="queryCondition" id="input_text_text2" value="<s:property value='queryCondition'/>"/>
						<input type="hidden" id="returnQueryCondition2" value="<s:property value='queryCondition'/>"/> 
						<a
							href="#" class="btn btn_a" title="筛选" id="sear_btn2"><span>筛选</span>
						</a> <span id="input_text_text2Tip"></span>
					</p><br>
					
					<!-- S Table -->
					<div class="table">
						<table >
							<thead>
								<tr>
									<th class="th_a" style="width:100px;">
										<div class="th_title">
											<em>运单号</em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>问题描述</em>
										</div></th>
									<th class="th_c" style="width:300px;">
										<div class="th_title">
											<em>处理信息</em>
										</div></th>
									<th class="th_d" style="width:200px;">
										<div class="th_title">
											<em>发送消息</em>
										</div></th>
								</tr>
							</thead>

							<tbody>
								<s:if test="reportIssueList != null && reportIssueList.size > 0">
									<s:iterator value="reportIssueList" status="stuts" var="report">
										<tr>
											<td class="td_a2">
												<a href="javascript:;" title="查看运单详情" style="text-decoration:underline;color:blue" class="mailno" val="<s:property value='#report.mailNo' />"><s:property value="#report.mailNo" /></a>
											</td>
											<td class="td_b2">
												<div class="war_div">
													<div class="openDiv"><s:property value="#report.issueDesc" />
													</div>
													<div class="b_war">
														<s:property value="#report.buyerName" /><br/>
														<s:property value="#report.buyerPhone" />
														<s:property value="#report.buyerMobile" />
														
													</div>
												</div></td>
											<td class="td_c2" <%-- style="word-wrap: break-word;" --%>>

												<%-- 	<s:if test="#report.allOper == null || report.allOper == '' ">
														<div class="openDiv1">${report.showOper}</div>
													</s:if>
													<s:else>
														<div class="openDiv">${report.showOper}
															<p style="text-align:center;"><a class="open" style="cursor:pointer;"> >>展开<< </a></p>
														</div>
													</s:else>
													<div class="closeDiv">${report.allOper}
														<p style="text-align:center;"><a class="close" style="cursor:pointer;"> >>收起<< </a></p>
													</div> --%>
													
													<div class="openDiv">${report.allOper}</div>
											</td>
											<td class="td_d2">
												<div class="msgbg2">
													<div id="tb_" class="tb_">
														<ul>
														<li class="normaltab"><span class="msg">短信通知买家</span></li> 
														<li class="hovertab"><span class="pen">通知网点</span></li>
														</ul>
													</div>
													<div class="ctt">
														<!-- 短信通知买家 -->
														<div class="tabPanel" style="display:none">
															<p class="ctt_p2 msgbg">
																<textarea class="mstxt default_text" name="" cols="" rows="" scroll="no" value="1" class="mstxt1"></textarea><s:fielderror key="errorMessage"/>
															    <input type="hidden" name="warn_value" value="<s:property value="buyerMobile"/>">
															    <input type="hidden" name="buyername" value="<s:property value="buyerName"/>">
															    <input type="hidden" name="mailNo" value="<s:property value="#report.mailNo" />">
															</p>
															<p class="ctt_p">
																<a href="#" class="fsbtn send_msg1"> 
																	<span class="fs_cust"><img src="images/btn_fs.png" /></span>
																</a> <a href="#" class="qkbtn clear_msg1">
																	<span class="qk_cust"><img src="images/btn_qk.png" /></span>
																</a>
															</p>
														</div> 
													    <form action="" method="post">
															<div class="tabPanel">
																<p class="ctt_p2 msgbg">
																	<textarea class="mstxt" cols="" rows="" scroll="no"><s:property value="#report.branch.text" />
联系电话：<s:property value="#report.branch.phone" />
<s:property value="#report.branch.mobile" /></textarea>
																</p>
																	<input type="hidden" value="<s:property value='#report.id'/>" class="issueId">
																<p class="ctt_p">
																	<a href="#" class="fsbtn send_msg2">
																		<span><img src="images/btn_fs.png"/></span>
																	</a> <a href="#" class="qkbtn clear_msg2">
																		<span><img src="images/btn_qk.png" /></span>
																	</a>
																</p>
															</div>
														</form>
													</div>
												</div>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:else>
									<tr>
										<td colspan="4" align="center"><span class="tips" >对不起！没有查询到任何上报网点信息</span></td>
									</tr>
								</s:else>
							</tbody>
						</table>
					</div>
					<!-- S PageNavi -->
					<div  class="pagenavi" id="pagenavi2">
						<jsp:include page="/WEB-INF/page.jsp"/>
					</div>
					<!-- E PageNavi -->
					<!-- E Table -->
				</div>
				<!-- E tab_panel2 -->
			</div>
		</div>
		<!-- E Tab -->
	</div>
	

<script>
	var params = {
		dialogSubmit : '?dialogSubmit',
		tabIndex : document.getElementById('tabNum').value,
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},	// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	}
</script>

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
	<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/problem_war.js?d=${str:getVersion() }"></script>

<!-- E 当前页面 JS -->
	
</div>
<!-- E Content -->

