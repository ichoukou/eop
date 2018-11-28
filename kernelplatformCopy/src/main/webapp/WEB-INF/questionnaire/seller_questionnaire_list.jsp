<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/page/complaint.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/question.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->


<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<!--h2 id="message_icon">问题件管理</h2-->
		<em><font>包裹出现问题，赶在买家投诉前解决。提升买家的满意度、忠诚度！</font><a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
		<a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=vip_5_questionnaire_manage" target="_blank" >
            <span>快速入门</span>
        </a>
		
	</div>
	<div id="content_bd" class="clearfix">
		<!-- S Box -->
		<div class="box box_a" id="box_form">
			<div class="box_bd">
			<form action="questionnaire_list.action" id="q_form" method="post" class="form">
				<input type="hidden" id="tabStatus" name="tabStatus" value="${tabStatus == null || '' ? 1 : tabStatus }" />
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<input type="hidden" id="currentPage" name="currentPage" value="1" />
				<input type="hidden" id="autoSkip" name="autoSkip" value="${autoSkip }" />
				<input type="hidden" id="tagId" name="tagId" value="${tagId }" />
				
				<c:if test="${yto:getCookie('userCode') != '' && (yto:getCookie('userType') == 1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13)}">
				<p>
					<label for="">揽件网点：</label>
					<span><s:property value="lanjianSite.userNameText"/>&nbsp;&nbsp;&nbsp;联系方式：<s:property value="lanjianSite.telePhone"/> 	<s:property value="lanjianSite.mobilePhone"/></span>
				</p>
					
				</c:if>

				<p>
					<label for="start_date"><span class="req">*</span>处理时间：</label>
					<input name="starttime" class="Wdate" type="text" id="start_date" value="${starttime }" />
					<span class="zhi">至</span><input class="Wdate" name="endtime" type="text" id="end_date" value="${endtime }"/>
					<span id="dateTip"></span>
				</p>
				<p>
					<label for="shop_name">店铺名称：</label>
					<select name="bindUserCustomerId" id="shop_name">
						<option value="">所有店铺</option>
						<c:forEach items="${bindUserList }" var="bUser">
		                	<c:choose>
		                		<c:when test="${bindUserCustomerId == bUser.taobaoEncodeKey }">
		                			<option selected value="${bUser.taobaoEncodeKey}">${bUser.shopName}</option>
		                		</c:when>
		                		<c:otherwise>
		                			<option value="${bUser.taobaoEncodeKey}">${bUser.shopName}</option>
		                		</c:otherwise>
		                	</c:choose>
		                </c:forEach>
					</select>
				</p>
				<p>
					<label for="q_type">问题件类型：</label>
					<input type="text" id="q_type" name="feedbackInfoStr" class="input_text" readonly value="${feedbackInfoStr}" />
					<input type="hidden" name="feedbackInfo" id="q_type_val" value="${feedbackInfo }" />
					
					<span class="ml10">
						<input type="checkbox" id="isShowSigned" name="isShowSigned" <s:if test="isShowSigned==1">checked</s:if> value="${isShowSigned }"/>显示已签收问题件
					</span>
					
				</p>
				<p>
					<c:if test="${yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
						<label for="user_name">分仓账号：</label>
						<select name="vipCode" id="user_name">
							<option value="">所有账号</option>
							<c:forEach items="${vipList }" var="bUser">
			                	<c:choose>
			                		<c:when test="${vipCode == bUser.taobaoEncodeKey }">
			                			<option selected value="${bUser.taobaoEncodeKey}">分仓：${bUser.userNameText}</option>
			                		</c:when>
			                		<c:otherwise>
			                			<option value="${bUser.taobaoEncodeKey}">分仓：${bUser.userNameText}</option>
			                		</c:otherwise>
			                	</c:choose>
			                </c:forEach>
			                <c:if test="${fn:length(vipList) < 1}">
			                	<option value="">暂无分仓账号</option>
			                </c:if>
						</select>
					</c:if>

					<input type="text" name="conditionString" id="ship_num" value="${conditionString }" class="input_text" />
					
					<a href="javascript:;" class="btn btn_a" id="sear_btn" title="查 询"><span>查 询</span></a>
					<span id="ship_numTip"></span>
				</p>
			</form>
				
				<!-- S 问题件类型 -->
				<div id="simulate_sel2" style="display:none;">
					<div id="type_box">
						<ul class="clearfix">
						<s:iterator status="st" value="dealMap.keySet()" id="key">
							<li>
								<input type="checkbox" value="<s:property value='#key'/>" <s:if test="#key in questionTypes">checked</s:if> />
								<label><s:property value="dealMap.get(#key)"/></label>
							</li>
						</s:iterator>
						</ul>
					</div>
					
					<div id="confirm_box">
						<a href="javascript:;" id="type_ok" class="btn btn_d" title="确定"><span>确 定</span></a>
						<a href="javascript:;" id="type_clear" class="btn btn_e" title="清空"><span>清 空</span></a>
					</div>
				</div>
				<!-- E 问题件类型 -->
			</div>
		</div>
		<!-- E Box -->
		<!-- S Box -->
		<div class="box box_a ">
			<div class="box_bd" id="box_bd">
				<!-- S Tab -->
				<div class="tab tab_c tab_d_hd">
					<div class="tab_triggers">
						<ul>
							<li><a href="javascript:;" id="tab1"><span class="qt"></span>新问题件 <c:if test="${index==0 }">（${str:defaultIfEmpty(noneAdvisedNum,'0')}）</c:if></a></li>
							<li><a href="javascript:;" id="tab4"><span class="zjz"></span>${tagHeaderName }<c:if test="${index==0 }">（${str:defaultIfEmpty(handingNum,'0')}）</c:if><i class="tab_arr"></i></a></li>
							<li><a href="javascript:;" id="tab3"><span class="cgd"></span>已处理<c:if test="${index==0 }">（${str:defaultIfEmpty(advisedNum ,'0')}）</c:if></a></li>
						</ul>
						
						<div id="handling" style="display:none;">
							<ul>
								<li value=0><a href="javascript:;" title="处理中（全部）">处理中（全部）</a></li>
								<c:forEach items="${questionnaireTagList }" var="questionTag">
									<c:if test="${questionTag.tagName!='已处理' }">
										<li value="${questionTag.id }"><a href="javascript:;" title="${questionTag.tagName }">${questionTag.tagName }</a></li>
									</c:if>
								</c:forEach>
								<li><a href="javascript:;" title="标签设置" class="set_label set_label_dialog">标签设置</a></li>
							</ul>
						</div>
					</div>
					<div class="tab_panels" >
						<div id="tab_panel_a" class="tab_panel clearfix">
							<div class="pro_box clearfix">
								<select class="move_label">
									<option value="">批量移动到...</option>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<option value="${questionTag.id }">${questionTag.tagName }</option>
									</c:forEach>
									<option value="set_label_dialog">标签设置</option>
								</select>
								
								<div class="export_xls">
									<em>导出所有新问题件：</em>
									<a href="javascript:;" class="btn btn_a exportQuestion" title="导出"><span>导出</span></a>
								</div>
							</div>
							
							<!-- S Table -->
							<div class="table">
								<table>
									<thead>
										<tr>
											<th class="th_a">
												<div class="th_title"><em><input type="checkbox" class="checked_all" /></em></div>
											</th>
											<th class="th_b">
												<div class="th_title"><em>问题件</em></div>
											</th>
											<th class="th_d">
												<div class="th_title"><em>处理信息</em></div>
											</th>
											<!-- 
											<th class="th_e" width="150">
												<div class="th_title"><em>客户信息</em></div>
											</th>
											 -->
											<th class="th_f" width="300">
												<div class="th_title"><em>发送消息</em></div>
											</th>
										</tr>
									</thead>
									
									<tbody>
										<c:choose>
					                  		<c:when test="${index==0 && (questionnaireList==null || fn:length(questionnaireList)<1) }">
					                  			<tr align="center">
					                  				<td colspan="5">抱歉，暂无您所查找的数据</td>
					                  			</tr>
					                  		</c:when>
					                  		<c:otherwise>
										<c:forEach items="${questionnaireList }" var="que" varStatus="queSt">
											<tr style="height:300px;">
												<td class="td_a">
													<input type="checkbox" class="que_checkbox" value="${que.id }"  val="${que.mailNO}"/>
													<input type="hidden" value="${que.mailNO }" id="mailno"/>
                        							<input type="hidden" value="${que.mjIsRead }" id="state"/>
												  	<a href="javascript:;" class="showSameMail" name="${que.mailNO}"><img src="${imagesPath}/single/lj.png" title="显示所有相同运单" /></a>
												</td>
												<td class="td_b">
													<div class="topdes">
														<a href="javascript:;" class="mailno" val="${que.mailNO}" title="运单号" >${que.mailNO}</a><br/>
														<c:if test="${que.taobaoLoginName!=null && que.taobaoLoginName!=''}">
															<em class="wangwang_id">${que.taobaoLoginName }</em>
															<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=${que.taobaoLoginName }&site=cntaobao&s=1&charset=utf-8" ><img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=${que.taobaoLoginName }&site=cntaobao&s=1&charset=utf-8" alt="联系买家" /></a>
														</c:if>
														<c:if test="${que.taobaoLoginName==null || que.taobaoLoginName==''}">
															<span class="wangwang_tips" style="color:#78B067;cursor: pointer;">查看旺旺详情</span>
														</c:if>
													</div>
													<div class="btdes">
														<span class="span_block">店铺名称：${que.shopName }</span>
														<span class="span_block">类型：${dealMap[que.feedbackInfo]}</span>
													</div>
												</td>
												<td class="td_d">
													<c:choose>
								                   		<c:when test="${que.mjIsRead == 0}">
								                        	<div class="topdes height_auto unread" style="width:380px;" name="${que.id }">
								                      	</c:when>
								                      	<c:otherwise>
								                          	<div class="topdes height_auto" style="width:380px;">
								                      	</c:otherwise>
							                      	</c:choose>
						                            <p><span class="que_issue">${que.questionaireExchangeString}</span></p>
							                        <p style="font-size:14px;">
							                        	<c:if test="${str:isNotEmpty(que.questionaireRemark.remakContent) }">
							                        		备注：${que.questionaireRemark.remakContent }<br />
							                        	</c:if>
							                        	${que.questionaireRemark.operatorName }
							                        	<fmt:formatDate value="${que.questionaireRemark.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
							                        </p>
							                        </div>
							                        <div class="btdes"> 
						                        		<c:if test="${que.IMG1!=null }"><a href="${que.IMG1 }" target="_black">图片1</a></c:if>
										                <c:if test="${que.IMG2!=null }"><a href="${que.IMG2 }" target="_black">图片2</a></c:if>
										                <c:if test="${que.IMG3!=null }"><a href="${que.IMG3 }" target="_black">图片3</a></c:if>
										                <c:if test="${que.IMG4!=null }"><a href="${que.IMG4 }" target="_black">图片4</a></c:if>
						                        	</div>
													<div class="btdes"> 问题上报网点：${que.reportBranckText }<br />
													${issueCreateUserText } <fmt:formatDate value="${que.issueCreateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
							                        
												</td>
												<!-- 
												<td class="td_e">
													<div class="topdes">
														<p>姓名：${que.buyerName }</p>
	                                           			<p>电话：${que.contactWay }</p>
													</div>
												</td>
												 -->
												<td class="td_f">
													<div class="msg_tab tab_trigger2">
														<div class="msg_tab_triggers">
															<ul class="clearfix">
																<li class="cur_msg_tab">
																	<a href="javascript:;"><i class="msg_icon_b"></i><span>短信通知买家</span></a>
																</li>
																<li>
																	<a href="javascript:;"><i class="msg_icon_a"></i><span>通知揽件网点</span></a>
																</li>
																<li>
																	<a href="javascript:;"><i class="msg_icon_c"></i><span>写备注</span></a>
																</li>
															</ul>
														</div>
														
														<div class="msg_tab_panels">
															<div class="msg_tab_panel">
																<textarea class="default_text"></textarea>
																<input name="bMailno" type="hidden" value="${que.mailNO }"/>
																<input name="bName" type="hidden" value="${que.buyerName }">
																<input name="bMobile" type="hidden" value="${que.contactWay }">
																<div class="msg_btn">
																	<a title="发送" class="btn btn_a sendCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
															<div class="msg_tab_panel" style="display:none;">
																<textarea></textarea>
																<div class="msg_btn">
																	<a title="发送" class="btn btn_a notifyCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
															<div class="msg_tab_panel" style="display:none;">
																<textarea></textarea>
																<div class="msg_btn">
																	<a title="保存" class="btn btn_a remark" href="javascript:;" name="${que.id}"><span>保 存</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
														</div>
													</div>
												</td>
											  </tr>													
										</c:forEach>
										</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								<!-- 这里的代码请勿轻易更改，包括其位置 -->
								<div class="shortcut_label" style="display:none;">
									<span>移动到：</span>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<a href="javascript:;" class="label_to" title="${questionTag.tagName }" value="${questionTag.id }">${questionTag.tagName }</a>
										<input type="hidden" value=""/>
									</c:forEach>
									<c:if test="${fn:length(questionnaireTagList)<10}">   
										<a href="javascript:;" class="add_label set_label_dialog" title="标签设置">标签设置</a>
									</c:if>
									<c:if test="${userTagType==-1}">   
										<em>点击标签可将该问题件移动到“处理中”</em>
									</c:if>
									<a href="javasscript:;" title="标签设置" class="set_label set_label_dialog">标签设置</a>
									<i></i>
								</div>
							</div>
							<!-- E Table -->
							<c:if test="${questionnaireList!=null && fn:length(questionnaireList)>0 }">
							<div class="attention">
								<input class="input_checkbox checked_all" type="checkbox" /> 全选
								<select class="move_label">
									<option value="">批量移动到...</option>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<option value="${questionTag.id }">${questionTag.tagName }</option>
									</c:forEach>
									<option value="set_label_dialog">标签设置</option>
								</select>
								<select class="mark_status">
									<option value="">标记为</option>
									<option value="1">已读</option>
									<option value="0">未读</option>
								</select>
								网点未及时回复处理问题件，点击<a style="margin:0 5px;" class="complaint" href="javascript:;" title="投诉">投诉</a>上报至管理员
							</div>
							</c:if>
							<!-- S PageNavi -->
							<div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
							<!-- E PageNavi -->
						</div>
						<div id="tab_panel_d" class="tab_panel clearfix" style="display:none;">
							<div class="pro_box clearfix">
								<select class="move_label">
									<option value="">批量移动到...</option>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<option value="${questionTag.id }">${questionTag.tagName }</option>
									</c:forEach>
									<option value="set_label_dialog">标签设置</option>
								</select>
								<div class="export_xls">
									<em>导出所有处理中的问题件：</em>
									<a href="javascript:;" class="btn btn_a exportQuestion" title="导出"><span>导出</span></a>
								</div>
							</div>
							<!-- S Table -->
							<div class="table">
								<table>
									<thead>
										<tr>
											<th class="th_a">
												<div class="th_title"><em><input type="checkbox" class="checked_all" /></em></div>
											</th>
											<th class="th_b">
												<div class="th_title"><em>问题件</em></div>
											</th>
											<th class="th_d">
												<div class="th_title"><em>处理信息</em></div>
											</th>
											<!-- 
											<th class="th_e" width="150">
												<div class="th_title"><em>客户信息</em></div>
											</th>
											 -->
											<th class="th_f" width="300">
												<div class="th_title"><em>发送消息</em></div>
											</th>
											
										</tr>
									</thead>
									
									<tbody>
										<c:choose>
					                  		<c:when test="${index==0 && (questionnaireList==null || fn:length(questionnaireList)<1) }">
					                  			<tr align="center">
					                  				<td colspan="5">抱歉，暂无您所查找的数据</td>
					                  			</tr>
					                  		</c:when>
					                  		<c:otherwise>
										<c:forEach items="${questionnaireList }" var="que" varStatus="queSt">
											<tr style="height:300px;">
												<td class="td_a">
													<input type="checkbox" class="que_checkbox" value="${que.id }"  val="${que.mailNO}"/>
													<input type="hidden" value="${que.mailNO }" id="mailno"/>
                        							<input type="hidden" value="${que.mjIsRead }" id="state"/>
												  	<a href="javascript:;" class="showSameMail" name="${que.mailNO}"><img src="${imagesPath}/single/lj.png" title="显示所有相同运单" /></a>
												</td>
												<td class="td_b">
													<div class="topdes">
														<a href="javascript:;" class="mailno" val="${que.mailNO}" title="运单号" >${que.mailNO}</a><br/>
														<c:if test="${que.taobaoLoginName!=null && que.taobaoLoginName!=''}">
															<em class="wangwang_id">${que.taobaoLoginName }</em>
															<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=${que.taobaoLoginName }&site=cntaobao&s=1&charset=utf-8" ><img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=${que.taobaoLoginName }&site=cntaobao&s=1&charset=utf-8" alt="联系买家" /></a>
														</c:if>
														<c:if test="${que.taobaoLoginName==null || que.taobaoLoginName==''}">
															<span class="wangwang_tips" style="color:#78B067;cursor: pointer;">查看旺旺详情</span>
														</c:if>
													</div>
													<div class="btdes">
														<span class="span_block">店铺名称：${que.shopName }</span>
														<span class="span_block">类型：${dealMap[que.feedbackInfo]}</span>
													</div>
												</td>
												<td class="td_d">
												<c:choose>
							                   		<c:when test="${que.mjIsRead == 0}">
							                        <div class="topdes height_auto unread" style="width:380px;" name="${que.id }">
							                      	</c:when>
							                      	<c:otherwise>
							                          	<div class="topdes height_auto" style="width:380px;">
							                      	</c:otherwise>
							                      	</c:choose>
						                           <p><span class="que_issue">${que.questionaireExchangeString}</span> </p>
							                        <p style="font-size:14px;">
							                        	<c:if test="${str:isNotEmpty(que.questionaireRemark.remakContent) }">
							                        		备注：${que.questionaireRemark.remakContent }<br />
							                        	</c:if>
							                        	${que.questionaireRemark.operatorName }
							                        	<fmt:formatDate value="${que.questionaireRemark.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
							                        </p>
							                        </div>
							                        <div class="btdes"> 
						                        		<c:if test="${que.IMG1!=null }"><a href="${que.IMG1 }" target="_black">图片1</a></c:if>
										                <c:if test="${que.IMG2!=null }"><a href="${que.IMG2 }" target="_black">图片2</a></c:if>
										                <c:if test="${que.IMG3!=null }"><a href="${que.IMG3 }" target="_black">图片3</a></c:if>
										                <c:if test="${que.IMG4!=null }"><a href="${que.IMG4 }" target="_black">图片4</a></c:if>
						                        	</div>
													<div class="btdes"> 问题上报网点：${que.reportBranckText }<br />
													${issueCreateUserText } <fmt:formatDate value="${que.issueCreateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
												</td>
												<!-- 
												<td class="td_e">
													<div class="topdes">
														<p>姓名：${que.buyerName }</p>
	                                           			<p>电话：${que.contactWay }</p>
													</div>
												</td>
												 -->
												<td class="td_f">
													<div class="msg_tab tab_trigger2">
														<div class="msg_tab_triggers">
															<ul class="clearfix">
																<li class="cur_msg_tab">
																	<a href="javascript:;"><i class="msg_icon_b"></i><span>短信通知买家</span></a>
																</li>
																<li>
																	<a href="javascript:;"><i class="msg_icon_a"></i><span>通知揽件网点</span></a>
																</li>
																<li>
																	<a href="javascript:;"><i class="msg_icon_c"></i><span>写备注</span></a>
																</li>
															</ul>
														</div>
														<div class="msg_tab_panels">
															<div class="msg_tab_panel">
																<textarea class="default_text"></textarea>
																<input name="bMailno" type="hidden" value="${que.mailNO }"/>
																<input name="bName" type="hidden" value="${que.buyerName }">
																<input name="bMobile" type="hidden" value="${que.contactWay }">
																
																<div class="msg_btn">
																	<a title="发送" class="btn btn_a sendCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
															<div class="msg_tab_panel" style="display:none;">
																<textarea></textarea>
																<div class="msg_btn">
																	<a title="发送" class="btn btn_a notifyCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
															<div class="msg_tab_panel" style="display:none;">
																<textarea></textarea>
																<div class="msg_btn">
																	<a title="保存" class="btn btn_a remark" href="javascript:;" name="${que.id}"><span>保 存</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
														</div>
													</div>
												</td>
											  </tr>													
										</c:forEach>
										</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								<div class="shortcut_label" style="display:none;">
									<span>移动到：</span>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<a href="javascript:;" class="label_to" title="${questionTag.tagName }" value="${questionTag.id }">${questionTag.tagName }</a>
										<input type="hidden" value=""/>
									</c:forEach>
									<c:if test="${fn:length(questionnaireTagList)<10}">   
										<a href="javascript:;" class="add_label set_label_dialog" title="标签设置">标签设置</a>
									</c:if>
									<c:if test="${userTagType==-1}">   
										<em>点击标签可将该问题件移动到“处理中”</em>
									</c:if>
									<a href="javasscript:;" title="标签设置" class="set_label set_label_dialog">标签设置</a>
									<i></i>
								</div>
							</div>
							<!-- E Table -->
							<c:if test="${questionnaireList!=null && fn:length(questionnaireList)>0 }">
							<div class="attention">
								<input class="input_checkbox checked_all" type="checkbox" /> 全选
								<select class="move_label">
									<option value="">批量移动到...</option>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<option value="${questionTag.id }">${questionTag.tagName }</option>
									</c:forEach>
									<option value="set_label_dialog">标签设置</option>
								</select>
								<select class="mark_status">
									<option value="">标记为</option>
									<option value="1">已读</option>
									<option value="0">未读</option>
								</select>
								网点未及时回复处理问题件，点击<a style="margin:0 5px;" class="complaint" href="javascript:;" title="投诉">投诉</a>上报至管理员
							</div>
							</c:if>
							<!-- S PageNavi -->
							<div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
							<!-- E PageNavi -->
						</div>
						<div id="tab_panel_b" class="tab_panel clearfix" style="display:none;">
							<div class="pro_box clearfix">
								<select class="move_label">
									<option value="">批量移动到...</option>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<option value="${questionTag.id }">${questionTag.tagName }</option>
									</c:forEach>
									<option value="set_label_dialog">标签设置</option>
								</select>
								
								<div class="export_xls">
									<em>导出已处理问题件：</em>
									<a href="javascript:;" class="btn btn_a exportQuestion" title="导出"><span>导出</span></a>
								</div>
							</div>
						
							<!-- S Table -->
							<div class="table">
								<table>
									<thead>
										<tr>
											<th class="th_a">
												<div class="th_title"><em><input type="checkbox" class="checked_all" /></em></div>
											</th>
											<th class="th_b">
												<div class="th_title"><em>问题件</em></div>
											</th>
											<th class="th_d">
												<div class="th_title"><em>处理信息</em></div>
											</th>
											<!-- 
											<th class="th_e" width="150">
												<div class="th_title"><em>客户信息</em></div>
											</th>
											 -->
											<th class="th_f" width="310">
												<div class="th_title"><em>发送消息</em></div>
											</th>
										</tr>
									</thead>
									
									<tbody>
										<c:choose>
					                  		<c:when test="${index==0 && (questionnaireList==null || fn:length(questionnaireList)<1) }">
					                  			<tr align="center">
					                  				<td colspan="5">抱歉，暂无您所查找的数据</td>
					                  			</tr>
					                  		</c:when>
					                  		<c:otherwise>
										<c:forEach items="${questionnaireList }" var="que" varStatus="queSt">
											<tr style="height:300px;">
												<td class="td_a">
													<input type="checkbox" class="que_checkbox" value="${que.id }" val="${que.mailNO}"/>
													<input type="hidden" value="${que.mailNO }" id="mailno"/>
                        							<input type="hidden" value="${que.mjIsRead }" id="state"/>
													<a href="javascript:;" class="showSameMail" name="${que.mailNO}"><img src="${imagesPath}/single/lj.png" title="显示所有相同运单" /></a>
												</td>
												<td class="td_b">
													<div class="topdes">
														<a href="javascript:;" class="mailno" val="${que.mailNO}" title="运单号" >${que.mailNO}</a><br>
														<c:if test="${que.taobaoLoginName!=null && que.taobaoLoginName!=''}">
															<em class="wangwang_id">${que.taobaoLoginName }</em>
															<a target="_blank" href="http://amos.im.alisoft.com/msg.aw?v=2&uid=${que.taobaoLoginName }&site=cntaobao&s=1&charset=utf-8" ><img border="0" src="http://amos.im.alisoft.com/online.aw?v=2&uid=${que.taobaoLoginName }&site=cntaobao&s=1&charset=utf-8" alt="联系买家" /></a>
														</c:if>
														<c:if test="${que.taobaoLoginName==null || que.taobaoLoginName==''}">
															<span class="wangwang_tips" style="color:#78B067;cursor: pointer;">查看旺旺详情</span>
														</c:if>
													</div>
													<div class="btdes">
														<span class="span_block">店铺名称：${que.shopName }</span>
														<span class="span_block">类型：${dealMap[que.feedbackInfo]}</span>
													</div>
												</td>
												<td class="td_d">
												<c:choose>
							                   		<c:when test="${que.mjIsRead == 0}">
							                        <div class="topdes height_auto unread" style="width:380px;" name="${que.id }">
							                      	</c:when>
							                      	<c:otherwise>
							                          	<div class="topdes height_auto" style="width:380px;">
							                      	</c:otherwise>
							                      	</c:choose>
						                               <p><span class="que_issue">${que.questionaireExchangeString}</span></p>
							                        <p style="font-size:14px;">
							                        	<c:if test="${str:isNotEmpty(que.questionaireRemark.remakContent) }">
							                        		备注：${que.questionaireRemark.remakContent }<br />
							                        	</c:if>
							                        	${que.questionaireRemark.operatorName }
							                        	<fmt:formatDate value="${que.questionaireRemark.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
							                        </p>
							                        </div>
							                        <div class="btdes"> 
						                        		<c:if test="${que.IMG1!=null }"><a href="${que.IMG1 }" target="_black">图片1</a></c:if>
										                <c:if test="${que.IMG2!=null }"><a href="${que.IMG2 }" target="_black">图片2</a></c:if>
										                <c:if test="${que.IMG3!=null }"><a href="${que.IMG3 }" target="_black">图片3</a></c:if>
										                <c:if test="${que.IMG4!=null }"><a href="${que.IMG4 }" target="_black">图片4</a></c:if>
						                        	</div>
													<div class="btdes"> 问题上报网点：${que.reportBranckText }<br />
													${issueCreateUserText } <fmt:formatDate value="${que.issueCreateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
												</td>
												<!-- 
												<td class="td_e">
													<div class="topdes">
														<p>姓名：${que.buyerName }</p>
	                                           			<p>电话：${que.contactWay }</p>
													</div>
												</td>
												 -->
												<td class="td_f">
													<div class="msg_tab tab_trigger2">
														<div class="msg_tab_triggers">
															<ul class="clearfix">
																<li class="cur_msg_tab">
																	<a href="javascript:;"><i class="msg_icon_b"></i><span>短信通知买家</span></a>
																</li>
																<li>
																	<a href="javascript:;"><i class="msg_icon_a"></i><span>通知揽件网点</span></a>
																</li>
																<li>
																	<a href="javascript:;"><i class="msg_icon_c"></i><span>写备注</span></a>
																</li>
															</ul>
														</div>
														
														<div class="msg_tab_panels">
															<div class="msg_tab_panel">
																<textarea class="default_text"></textarea>
																<input name="bMailno" type="hidden" value="${que.mailNO }"/>
																<input name="bName" type="hidden" value="${que.buyerName }">
																<input name="bMobile" type="hidden" value="${que.contactWay }">
																<div class="msg_btn">
																	<a title="发送" class="btn btn_a sendCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
															<div class="msg_tab_panel" style="display:none;">
																<textarea></textarea>
																<div class="msg_btn">
																	<a title="发送" class="btn btn_a notifyCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
															<div class="msg_tab_panel" style="display:none;">
																<textarea></textarea>
																<div class="msg_btn">
																	<a title="保存" class="btn btn_a remark" href="javascript:;" name="${que.id}"><span>保 存</span></a>
																	<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
																</div>
															</div>
														</div>
													</div>
												</td>
											  </tr>	
										</c:forEach>
										</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								<!-- 这里的代码请勿轻易更改，包括其位置等等 -->
								<div class="shortcut_label" style="display:none;">
									<span>移动到：</span>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<a href="javascript:;" class="label_to" title="${questionTag.tagName }" value="${questionTag.id }">${questionTag.tagName }</a>
										<input type="hidden" value=""/>
									</c:forEach>
									<c:if test="${fn:length(questionnaireTagList)<10}">   
										<a href="javascript:;" class="add_label set_label_dialog" title="标签设置">标签设置</a>
									</c:if>
									<c:if test="${userTagType==-1}">   
										<em>点击标签可将该问题件移动到“处理中”</em>
									</c:if>
									<a href="javasscript:;" title="标签设置" class="set_label set_label_dialog">标签设置</a>
									<i></i>
								</div>
							</div>
							<!-- E Table -->
							<c:if test="${questionnaireList!=null && fn:length(questionnaireList)>0 }">
							<div class="attention">
								<input class="input_checkbox checked_all" type="checkbox" /> 全选
								
								<select class="move_label">
									<option value="">批量移动到...</option>
									<c:forEach items="${questionnaireTagList }" var="questionTag">
										<option value="${questionTag.id }">${questionTag.tagName }</option>
									</c:forEach>
									<option value="set_label_dialog">标签设置</option>
								</select>
								<select class="mark_status">
									<option value="">标记为</option>
									<option value="1">已读</option>
									<option value="0">未读</option>
								</select>
								网点未及时回复处理问题件，点击<a style="margin:0 5px;" class="complaint" href="javascript:;" title="投诉">投诉</a>上报至管理员
							</div>
							</c:if>
							<!-- S PageNavi -->
							<div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
							<!-- E PageNavi -->
						</div>
						
					</div>
				</div>
				<!-- E Tab -->
			</div>
		</div>
		<!-- E Box -->
		
	</div>
	
	
<script type="text/javascript">
	var params = {
		userType:1,
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		showBindCode:true,
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action',// 获取对应网点 url
		labels: ['紧急件', '跟踪件', '不处理', '待处理'],						    // 已有标签
		deleteLabelAction: 'questionnaire_deleteTag.action',								// 删除标签 action
		saveLabelAction: 'questionnaire_saveTag.action'									// 保存标签 action
	}; 
</script>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/question.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
	<a href="javascript:;" id="to_top" title="回到顶部">回到顶部</a>
</div>
<!-- E Content -->