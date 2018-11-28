<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/search.css?d=${str:getVersion() }" media="all" />
	<script type="text/javascript">
		function queryQues(mailNO,dealTime){
			var url = "questionnaire_list.action?menuFlag=chajian_question&tabStatus=1"
					+"&starttime="+dealTime+"&endtime="+dealTime
					+"&mailNO="+mailNO+"&currentPage=1&autoSkip=1";
			window.location = url;
		}
	</script>
	<!-- E 当前页面 CSS -->
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">智能查件</h2> -->
					<em>
				<c:if test="${yto:getCookie('userType') == 2|| yto:getCookie('userType') == 21|| yto:getCookie('userType') == 22|| yto:getCookie('userType') == 23}">
					便捷、安全、可靠的查询功能，助您解决各种业务问题！
				</c:if>
				<c:if test="${yto:getCookie('userType') == 1|| yto:getCookie('userType') == 11|| yto:getCookie('userType') == 12|| yto:getCookie('userType') == 13}">
					简单、实用、全方位的查询功能，助您工作更轻松！
				</c:if>
					<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a>
					
					</em>
					<a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=${str:startsWith(yto:getCookie('userType'),'2')?'site_4_intelligent_query':'vip_3_intelligent_query'}" target="_blank" >
		                <span>快速入门</span>
		            </a>

				<p>输入运单号或买家名字或买家电话→点击查询</p>
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<c:if test="${yto:getCookie('userType') == 2|| yto:getCookie('userType') == 21|| yto:getCookie('userType') == 22|| yto:getCookie('userType') == 23}">
		 					<p style="color:#f00;padding:10px 10px 0;">温馨提示：使用买家姓名查询时需要客户与网点绑定</p>
						</c:if>
						<form action="waybill_list.action" id="sear_form" method="post">
							<input name="currentPage" id="currentPage" type="hidden" value="${currentPage }"/>
							<input name="isCheck" id="isCheck" type="hidden" value="${isCheck }" />
							<input id="bindUserId" name="bindUserId" type="hidden" value="${bindUserId }">
							<input id="menuFlag" name="menuFlag" type="hidden" value="${menuFlag }" />
							<p>
								<textarea name="logisticsIds" rows="5" class="textarea_text" id="textarea_search"><c:if test="${logisticsIds != null && logisticsIds != '' }">${logisticsIds }</c:if></textarea>
								<span id="textarea_searchTip"></span>
							</p>
							<p>
								以输入的运单号为开始号或查询连续的 <input class="input_text" type="text" name="mailNum" id="num" maxlength="2" value="${mailNum }" /> 条运单
								<a href="javascript:;" id="sear_btn" title="查 询" class="btn btn_a"><span>查 询</span></a>
								<a href="javascript:;" id="reset_btn" title="清 空" class="btn btn_a"><span>清 空</span></a>
								<span id="numTip"></span>
							</p>
						</form>
					</div>
				</div>
				<div class="table">
					<table>
						<thead>
							<tr>
								<th><div class="th_title"><em>运单</em></div></th>
									<c:if test="${yto:getCookie('userType') == 1||yto:getCookie('userType') == 11||yto:getCookie('userType') == 12||yto:getCookie('userType') == 13}">
										<th>
										<div class="th_title"><em>店铺名称</em>
												<ul class="thead_select">
													<c:forEach items="${bindedUserList }" var="bindUser" varStatus="vSta">
														<li value="${bindUser.id }">
															${bindUser.shopName }
														</li>
													</c:forEach>
													<li value="0"><a href="#">设置选项</a></li>
												</ul>
										</div>
										</th>
									</c:if>
								<th><div class="th_title"><em>状态</em></div></th>
								<th><div class="th_title"><em>物流跟踪记录</em></div></th>
							</tr>
						</thead>
						<tbody>
							<s:if test="logisticsIds!=null && orderList==null">
								<c:if test="${fn:length(orderList) <= 0 }">
									<tr>
										<td colspan="5" class="txterror">
											抱歉，找不到您的订单，如有问题请联系我们<br>
                                         	联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
                                        </td>
									</tr>
								</c:if>
							</s:if>
							<s:else>
								<c:if test="${fn:length(orderList) == 1 }">
									<s:iterator value="orderList" var="order" status="orderIndex">
										<tr>
											<td>
												<a href="javascript:;" title="查看运单详情" class="mailno" val="${mailNo }"><s:property value="mailNo"/></a>
												<s:if test="isQuestion ">
													<c:if test="${(yto:getCookie('userType') == 2||yto:getCookie('userType') == 21||yto:getCookie('userType') == 22||yto:getCookie('userType') == 23) || ((yto:getCookie('userType') == 1||yto:getCookie('userType') == 11||yto:getCookie('userType') == 12||yto:getCookie('userType') == 13) && dealStatus == '2')}">
														<a  href="javascript:;" onclick="queryQues('<s:property value="mailNo"/>','<s:date name="dealTime" format="yyyy-MM-dd" />');" title="查看问题件信息"><img src="images/icons/ques_order2222.png" align="center"/></a>
													</c:if>
												</s:if>
												
												<s:if test="#order.weight != null">
													<br />
													<s:if test="#order.weight == '0.0'">
													重量:<font color="blue">未称重</font>
													</s:if>
													<s:else>
														重量:<font color="red">${order.weight }</font>kg
													</s:else>
												</s:if>
												<s:if test="#order.traderInfo!= null">
													<br />
													<font color="blue">
													收件人：${order.traderInfo.name }
													<s:if test="#order.traderInfo.mobile == null">
														${order.traderInfo.phone }
													</s:if>
													<s:else>
														<s:if test="#order.traderInfo.mobile != null">
														${order.traderInfo.mobile }
														</s:if>
													</s:else>
												</font>
												</s:if>
												<c:if test="${yto:getCookie('userType') == 1||yto:getCookie('userType') == 11||yto:getCookie('userType') == 12||yto:getCookie('userType') == 13||yto:getCookie('userType') == 2||yto:getCookie('userType') == 21||yto:getCookie('userType') == 22||yto:getCookie('userType') == 23}">
												<s:if test="#order.isAttention == 2">
													<br>
													<img style="cursor:pointer;" title="加入关注" src="images/icons/u9_normal.png" onclick="addInAttention(this,'<s:property value='mailNo'/>')">
												</s:if>
												<s:if test="#order.isAttention == 1">
													<br>
													<img style="cursor:pointer;" title="取消关注" src="images/icons/u10_normal.png" onclick="addInAttention(this,'<s:property value='mailNo'/>')">
												</s:if>
												</c:if>
											</td>
											<c:if test="${yto:getCookie('userType') == 1||yto:getCookie('userType') == 11||yto:getCookie('userType') == 12||yto:getCookie('userType') == 13}">
												<td><s:property value="shopName"/></td>
											</c:if>
											<td>
												<s:if test="orderStatus == 'ACCEPT'">接单</s:if>
												<s:if test="orderStatus == 'UNACCEPT'">不接单</s:if>
												<s:if test="orderStatus == 'GOT'">揽收成功</s:if>
												<s:if test="orderStatus == 'NOT_SEND'">揽收失败</s:if>
												<s:if test="orderStatus == 'FAILED'">失败</s:if>
												<s:if test="orderStatus == 'SIGNED'">已签收</s:if>
												<s:if test="orderStatus == 'TRANSIT'">运输中</s:if>
												<s:if test="orderStatus == 'NULL'">无</s:if>
											</td>
											<td>
												<div id="fullContent_<s:property value='#orderIndex.index'/>" style="display:block;">
												<span>
													 <s:iterator value="steps" var="step" status="stepIndex">
														<s:if test="remark.indexOf('揽')>-1||remark.indexOf('派')>-1||remark.indexOf('签')>-1">
															<p>
																<s:property value="acceptTime.substring(0,10)"/> 
																<s:property value="acceptTime.substring(10,18)"/> 
																<a onclick="searchSlor('<s:property value="acceptAddress"/>')" href="javascript:;" ><s:property value="acceptAddress"/></a>
																&nbsp;&nbsp;
																<s:if test="#stepIndex.index == steps.size()-1">
																		<s:property value="remark"/> 
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:if>
																<s:else>
																		<s:property value="remark"/>
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:else>
															</p>
														</s:if>
														<s:else>
															<p>
																<s:property value="acceptTime.substring(0,10)"/> 
																<s:property value="acceptTime.substring(10,18)"/> 
																<s:property value="acceptAddress"/>
																&nbsp;&nbsp;
																<s:if test="#stepIndex.index == steps.size()-1">
																		<s:property value="remark"/> 
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:if>
																<s:else>
																		<s:property value="remark"/>
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:else>
															</p>
														</s:else>
														
														<s:if test="#stepIndex.index == steps.size()-1">
															<input id="_feedbackInfo" name="feedbackInfo" type="hidden" value="<s:property value="acceptTime.substring(0,10)"/> <s:property value="acceptTime.substring(10,18)"/> <s:property value="acceptAddress"/> <s:property value="remark"/> <s:property value="name"/> <s:property value="contactWay"/>" />
														</s:if>														
													</s:iterator>
															<!-- 卖家短信通知 -->
															<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType')==11|| yto:getCookie('userType')==12|| yto:getCookie('userType')==13|| yto:getCookie('userType')==14}">
																
																<input id="_bName" name="bName" type="hidden" value="${order.traderInfo.name}" />
																<!-- 
																<s:if test="#order.traderInfo.mobile == null">
																	<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.phone }" />
																</s:if>
																<s:else>
																	<s:if test="#order.traderInfo.mobile != null">
																	<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.mobile }" />
																	</s:if>
																</s:else>
																 -->
																<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.mobile }" />
																<input id="_bMailno" name="bMailno" type="hidden" value="${mailNo}" />											
																<a class="smsSend" style="cursor: pointer;">短信通知</a>
															</c:if>
												</span>
												</div>
											</td>
										</tr>
									</s:iterator>	
								</c:if>
								
								<c:if test="${fn:length(orderList) > 1 }">
									<s:iterator value="orderList" var="order" status="orderIndex">
										<tr>
											<td>
												<a href="javascript:;" title="查看运单详情" class="mailno" val="${mailNo }"><s:property value="mailNo"/></a>
												<s:if test="isQuestion ">
													<c:if test="${(yto:getCookie('userType') == 2||yto:getCookie('userType') == 21||yto:getCookie('userType') == 22||yto:getCookie('userType') == 23) || ((yto:getCookie('userType') == 1||yto:getCookie('userType') == 11||yto:getCookie('userType') == 12||yto:getCookie('userType') == 13) && dealStatus == '2')}">
														<a  href="javascript:;" onclick="queryQues('<s:property value="mailNo"/>','<s:date name="dealTime" format="yyyy-MM-dd" />');" title="查看问题件信息"><img src="images/icons/ques_order2222.png" align="center"/></a>
													</c:if>
												</s:if>
												
												<s:if test="#order.weight != null">
													<br />
													<s:if test="#order.weight == '0.0'">
													重量:<font color="blue">未称重</font>
													</s:if>
													<s:else>
														重量:<font color="red">${order.weight }</font>kg
													</s:else>
												</s:if>
												<s:if test="#order.traderInfo!= null">
													<font color="blue">
													<br/>
													收件人：${order.traderInfo.name }
													<s:if test="#order.traderInfo.mobile == null">
														${order.traderInfo.phone }
													</s:if>
													<s:else>
														<s:if test="#order.traderInfo.mobile != null">
														${order.traderInfo.mobile }
													    </s:if>
													</s:else>
												</font>
												</s:if>
												<c:if test="${yto:getCookie('userType') == 1||yto:getCookie('userType') == 11||yto:getCookie('userType') == 12||yto:getCookie('userType') == 13||yto:getCookie('userType') == 2||yto:getCookie('userType') == 21||yto:getCookie('userType') == 22||yto:getCookie('userType') == 23}">
												<s:if test="#order.isAttention == 2">
													<br>
													<img style="cursor:pointer;" title="加入关注" src="images/icons/u9_normal.png" onclick="addInAttention(this,'<s:property value='mailNo'/>')">
												</s:if>
												<s:if test="#order.isAttention == 1">
													<br>
													<img style="cursor:pointer;" title="取消关注" src="images/icons/u10_normal.png" onclick="addInAttention(this,'<s:property value='mailNo'/>')">
												</s:if>
												</c:if>
											</td>
											<c:if test="${yto:getCookie('userType') == 1||yto:getCookie('userType') == 11||yto:getCookie('userType') == 12||yto:getCookie('userType') == 13}">
												<td><s:property value="shopName"/></td>
											</c:if>
											<td>
												<s:if test="orderStatus == 'ACCEPT'">接单</s:if>
												<s:if test="orderStatus == 'UNACCEPT'">不接单</s:if>
												<s:if test="orderStatus == 'GOT'">揽收成功</s:if>
												<s:if test="orderStatus == 'NOT_SEND'">揽收失败</s:if>
												<s:if test="orderStatus == 'FAILED'">失败</s:if>
												<s:if test="orderStatus == 'SIGNED'">已签收</s:if>
												<s:if test="orderStatus == 'TRANSIT'">运输中</s:if>
												<s:if test="orderStatus == 'NULL'">无</s:if>
											</td>
											<td>
												<div id="smallContent_<s:property value='#orderIndex.index'/>" style="display:block">
												<span>
														<s:if test="steps.size()>0">
															<s:iterator value="steps" var="step" status="stepIndex">
																<s:if test="#stepIndex.index == 0">
																	<p>
																		<s:property value="acceptTime.substring(0,10)"/> <s:property value="acceptTime.substring(10,18)"/>
																		<s:if test="remark.indexOf('揽')>-1||remark.indexOf('派')>-1||remark.indexOf('签')>-1">
																			<a onclick="searchSlor('<s:property value="acceptAddress"/>')" href="javascript:;"><s:property value="acceptAddress"/></a> 
																			  &nbsp;&nbsp;&nbsp;
																			   <s:property value="remark"/>
																			   <s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
				                 											   <s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																		 </s:if>
																		
																		
																		<s:else>
																			<s:property value="acceptAddress"/>
																			 &nbsp;&nbsp;&nbsp;
																			   <s:property value="remark"/>
																			   <s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
				                 											   <s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																		</s:else>  
																	</p>
																</s:if>
																<s:if test="#stepIndex.index > 0 && #stepIndex.index == steps.size()-1">
																	<p>
																		<s:property value="acceptTime.substring(0,10)"/> <s:property value="acceptTime.substring(10,18)"/>
																		<s:if test="remark.indexOf('揽')>-1||remark.indexOf('派')>-1||remark.indexOf('签')>-1">
																			<a onclick="searchSlor('<s:property value="acceptAddress"/>')" href="javascript:;"><s:property value="acceptAddress"/></a>
																			&nbsp;&nbsp;&nbsp;
																			<s:property value="remark"/>
										                 					<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
										                 					<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if> 
																		</s:if>
																		<s:else>
																			<s:property value="acceptAddress"/>
																			&nbsp;&nbsp;&nbsp;
																			<s:property value="remark"/>
										                 					<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
										                 					<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if> 
																		</s:else>  
																	</p>
																</s:if>
															</s:iterator>
														</s:if>
														<s:else>
															无记录
															<!-- 卖家短信通知 -->
															<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType')==11|| yto:getCookie('userType')==12|| yto:getCookie('userType')==13|| yto:getCookie('userType')==14}">
																<!-- 
																<input id="_feedbackInfo" name="feedbackInfo" type="hidden" value="" />
																<input id="_bName" name="bName" type="hidden" value="${order.traderInfo.name}" />
																 -->
																<!-- 
																<s:if test="#order.traderInfo.mobile == null">
																	<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.phone }" />
																</s:if>
																<s:else>
																	<s:if test="#order.traderInfo.mobile != null">
																	<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.mobile }" />
																	</s:if>
																</s:else>
																 -->
																 <!-- 
																<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.mobile }" />
																<input id="_bMailno" name="bMailno" type="hidden" value="${mailNo}" />											
																<a class="smsSend"><font color="blue">短信通知</font></a>
																 -->
															</c:if>
														</s:else>
												</span>
												</div>
												<div id="fullContent_<s:property value='#orderIndex.index'/>" style="display:none;">
												<span>
													 <s:iterator value="steps" var="step" status="stepIndex">
														<s:if test="remark.indexOf('揽')>-1||remark.indexOf('派')>-1||remark.indexOf('签')>-1">
															<p>
																<s:property value="acceptTime.substring(0,10)"/>
																 <s:property value="acceptTime.substring(10,18)"/> 
																 <a onclick="searchSlor('<s:property value="acceptAddress"/>')" href="javascript:;"><s:property value="acceptAddress"/></a>
																&nbsp;&nbsp;&nbsp;
																<s:if test="#stepIndex.index == steps.size()-1">
																		<s:property value="remark"/> 
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:if>
																<s:else>
																		<s:property value="remark"/>
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:else>
															</p>
														</s:if>
														<s:else>
															<p>
																<s:property value="acceptTime.substring(0,10)"/> <s:property value="acceptTime.substring(10,18)"/> 
																<s:property value="acceptAddress"/>
																&nbsp;&nbsp;&nbsp;
																<s:if test="#stepIndex.index == steps.size()-1">
																		<s:property value="remark"/> 
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:if>
																<s:else>
																		<s:property value="remark"/>
																		<s:if test="name!=null && name!=''"><s:property value="name"/></s:if>
																		<s:if test="contactWay!=null && contactWay!=''"><s:property value="contactWay"/></s:if>
																</s:else>
															</p>
														</s:else>
														<s:if test="#stepIndex.index == steps.size()-1">
															<input id="_feedbackInfo" name="feedbackInfo" type="hidden" value="<s:property value="acceptTime.substring(0,10)"/> <s:property value="acceptTime.substring(10,18)"/> <s:property value="acceptAddress"/> <s:property value="remark"/> <s:property value="name"/> <s:property value="contactWay"/>" />
														</s:if>	
													</s:iterator>
												</span>
												</div>
												<div id="smallContent_<s:property value='#orderIndex.index'/>" style="display:block">
													<span>
														<s:if test="steps.size()>1">
															[<a href="javascript:" onclick="document.getElementById('fullContent_<s:property value='#orderIndex.index'/>').style.display=(document.getElementById('fullContent_<s:property value='#orderIndex.index'/>').style.display=='none')?'':'none';
															document.getElementById('smallContent_<s:property value='#orderIndex.index'/>').style.display=(document.getElementById('smallContent_<s:property value='#orderIndex.index'/>').style.display=='none')?'':'none'" ><font color="blue">走件详情↓</font></a>]
														</s:if>
														<!-- 卖家短信通知 -->
														<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType')==11|| yto:getCookie('userType')==12|| yto:getCookie('userType')==13|| yto:getCookie('userType')==14}">
															
																<input id="_bName" name="bName" type="hidden" value="${order.traderInfo.name}" />
																<!-- 
																<s:if test="#order.traderInfo.mobile == null">
																	<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.phone }" />
																</s:if>
																<s:else>
																	<s:if test="#order.traderInfo.mobile != null">
																	<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.mobile }" />
																	</s:if>
																</s:else>
																 -->
																<input id="_bMobile" name="bMobile" type="hidden" value="${order.traderInfo.mobile }" />
																<input id="_bMailno" name="bMailno" type="hidden" value="${mailNo}" />											
																<a class="smsSend"><font color="blue">短信通知</font></a>
														</c:if>
													</span>
												</div>
											</td>
										</tr>
									</s:iterator>
								</c:if>
						
							</s:else>
						</tbody>
					</table>
				</div>
				<!-- S PageNavi -->
				<div class="pagenavi">
					<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
            	</div>
				</div>
				<!-- E PageNavi -->
				
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
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/search.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
	</div>
	

