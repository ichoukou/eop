<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/question.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->


<!-- S Content -->
  <div id="content">
    <div id="content_hd" class="clearfix">
<!--       <h2 id="message_icon">问题件</h2> -->
      <em><font style="font-weight:900">问题件在线快速处理，让卖家离不开圆通服务！</font><a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
      <a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=site_5_questionnaire_manage" target="_blank" >
          <span>快速入门</span>
      </a>
    </div>
    <div id="content_bd" class="clearfix">
      <!-- S Box -->
      <div id="box_form" class="box box_a">
        <div class="box_bd">
			<form action="questionnaire_list.action" id="q_form" class="form" method="post">
				<input type="hidden" id="tabStatus" name="tabStatus" value="${tabStatus == null || '' ? 1 : tabStatus }" />
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<input type="hidden" id="currentPage" name="currentPage" value="1" />
				<input type="hidden" id="autoSkip" name="autoSkip" value="${autoSkip }" />
				<p>
					<label for="start_date" class=""><span class="req">*</span>处理时间：</label>
					<input type="text" class="Wdate" name="starttime" id="start_date" value="${starttime }" /> 
					至 
					<input type="text" class="Wdate" id="end_date" name="endtime" value="${endtime }"/>
					<span id="dateTip"></span>
				</p>
				<p>
					<label for="user_name">客户名称：</label>
					<select name="vipCode" id="user_name">
						<option value="">所有人</option>
						<c:forEach items="${vipThreadList }" var="vipThread">
		                	<c:choose>
		                		<c:when test="${vipCode == vipThread.userCode }">
		                			<option selected value="${vipThread.userCode}">${vipThread.userName}(${vipThread.userCode})</option>
		                		</c:when>
		                		<c:otherwise>
		                			<option value="${vipThread.userCode}">${vipThread.userName}(${vipThread.userCode})</option>
		                		</c:otherwise>
		                	</c:choose>
		                </c:forEach>
		                <c:if test="${tabStatus!=2 }">
		                	<option value="0" <c:if test="${vipCode==0 }">selected</c:if>>其他客户</option>
		                </c:if>
					</select>
				</p>
				
				<p>
					<label for="q_type">问题件类型：</label>
					<input type="text" id="q_type" name="feedbackInfoStr" class="input_text" readonly value="${feedbackInfoStr }" />
					<input type="hidden" name="feedbackInfo" id="q_type_val" value="${feedbackInfo }" />
					
					<label for="j_status" class="reset_label">金刚状态：</label>
					<select name="issueStatus" id="j_status">
						<option value="">所有</option>
						<option <c:if test="${issueStatus == 'PD10'}">selected</c:if> value="PD10">未处理</option>
						<option <c:if test="${issueStatus == 'PD20'}">selected</c:if> value="PD20">处理中</option>
		            	<option <c:if test="${issueStatus == 'PD30'}">selected</c:if> value="PD30">已处理</option>
		            	<option <c:if test="${issueStatus == 'PD40'}">selected</c:if> value="PD40">取消</option>
					</select>
					<span class="ml10">
						<input type="checkbox" id="isShowSigned" name="isShowSigned" <s:if test="isShowSigned==1">checked</s:if> value="${isShowSigned }"/>显示已签收问题件
					</span>
				</p>
				<p>
					<label for="q_type">运单号：</label>
					<input type="text" class="input_text" name="conditionString" id="ship_num" value="${conditionString }" />
					
					<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查 询"><span>查 询</span></a>
					<span id="ship_numTip"></span>
					
				</p>
			</form>
			
			<!-- S 问题件类型 -->
			<div id="simulate_sel" style="display:none;">
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
                <li><a href="javascript:;" id="tab1"><span class="qt"></span>未通知客户 <c:if test="${index==0 }">（${str:defaultIfEmpty(noneAdvisedNum,0)}）</c:if></a></li>
                <li><a href="javascript:;" id="tab2"><span class="cgd"></span>已通知客户<c:if test="${index==0 }">（${str:defaultIfEmpty(advisedNum,0)}）</c:if></a></li>
                <li><a href="javascript:;" id="tab3"><span class="zjz"></span>其它<c:if test="${index==0 }">（${str:defaultIfEmpty(elseNum,0)}）</c:if></a></li>
              </ul>
              <span id="notify"><input type="checkbox" id="autoNotify" <s:if test="autoNotify==1">checked</s:if>/> 开启自动通知客户<i id="question"></i></span>
            </div>
            <div class="tab_panels">
            	<!-- 未通知 -->
              <div id="tab_panel_a" class="tab_panel clearfix" >
                <!-- S Table -->
                <div class="table">
                  <table>
                    <thead>
                      <tr>
                        <th class="th_a"> <div class="th_title"><em>
                            <input type="checkbox" class="checked_all" />
                            </em></div>
                        </th>
                        <th class="th_b"> <div class="th_title"><em>问题件</em></div>
                        </th>
                        <th class="th_c"> <div class="th_title"><em>问题描述</em></div>
                        </th>
                        <th class="th_d"> <div class="th_title"><em>处理信息</em></div>
                        </th>
                        <th class="th_e"> <div class="th_title"><em>客户信息</em></div>
                        </th>
                        <th class="th_f"> <div class="th_title"><em>发送消息</em></div>
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                    	<c:choose>
                    		<c:when test="${index==0 && (questionnaireList==null || fn:length(questionnaireList)<1) }">
                    			<tr align="center">
                    				<td colspan="6">抱歉，暂无您所查找的数据</td>
                    			</tr>
                    		</c:when>
                    		<c:otherwise>
                      <c:forEach items="${questionnaireList}" var="que" varStatus="status">
                      	<tr>
                        <td class="td_a"><input type="checkbox" class="que_checkbox" value="${que.id }" />
                          <a href="javascript:;" class="showSameMail" name="${que.mailNO}"><img src="${imagesPath }/single/lj.png" title="显示所有相同运单" /></a>
                        </td>
                        <td class="td_b">
                          <div class="topdes">
                          	<a href="javascript:;" class="mailno" val="${que.mailNO}" title="运单号" >${que.mailNO}</a>
                          </div>
                          <div class="btdes"> 类型：${dealMap[que.feedbackInfo]}<br />
                           	 金刚状态：<c:choose>
                            	<c:when test="${que.issueStatus == 'PD10'}">未处理</c:when>
                            	<c:when test="${que.issueStatus == 'PD20'}">处理中</c:when>
                            	<c:when test="${que.issueStatus == 'PD30'}">已处理</c:when>
                            	<c:when test="${que.issueStatus == 'PD40'}">取消</c:when>
                            </c:choose>
                            </div>
                        </td>
                        <td class="td_c">
                        	<div class="topdes height_auto">
		                         <span class="que_issue">${que.issueDesc}</span>
								 <input type="hidden" class="issue_desc" value="${que.issueDesc}" />
								 <input type="hidden" class="issue_sub_desc" value="${str:multiSubStr(que.issueDesc,100)}" />
                        	</div>
                        	<div class="btdes"> 
                        		<c:if test="${que.IMG1!=null }"><a href="${que.IMG1 }" target="_black">图片1</a></c:if>
				                <c:if test="${que.IMG2!=null }"><a href="${que.IMG2 }" target="_black">图片2</a></c:if>
				                <c:if test="${que.IMG3!=null }"><a href="${que.IMG3 }" target="_black">图片3</a></c:if>
				                <c:if test="${que.IMG4!=null }"><a href="${que.IMG4 }" target="_black">图片4</a></c:if>
                        	</div>
                       		<div class="btdes"> 问题上报网点：${que.reportBranckText}<br />
                            ${que.issueCreateUserText} <fmt:formatDate value="${que.issueCreateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
                        </td>
                        <td class="td_d">
                        	<div class="topdes unread height_auto">
                        	<span class="que_issue">${que.quesDealString}</span>
                          	</div>
                        </td>
                        <td class="td_d">
							<div class="topdes">
								<c:if test="${que.isElseCustomer==1 }">
									这条属于散客、企业客户、未绑定淘宝客户等的问题件信息
								</c:if>
								<c:if test="${que.isElseCustomer==0 }">
									<p>${que.customer.userName }</p>
		                   		 	<p>电话：${que.customer.mobilePhone }</p>
		                   		 	<p>客户编码：${que.customer.userCode }</p>
	                   		 	</c:if>
                          	</div>
                        </td>
                        <td class="td_f">
	                        <div class="msg_tab tab_trigger2">
								<div class="msg_tab_triggers">
									<ul class="clearfix">
										<c:if test="${que.isElseCustomer==0 }">
											<li class="cur_msg_tab"><a href="javascript:;"><i class="msg_icon_b"></i><span>通知客户</span></a></li>
											<li><a href="javascript:;"><i class="msg_icon_a"></i><span>通知上报网点</span></a></li>
										</c:if>
										<c:if test="${que.isElseCustomer==1 }">
											<li class="cur_msg_tab" style="margin:0 5px 0 60px;"><a href="javascript:;" style="background:none;cursor:default;"><i class="msg_icon_a"></i><span style="cursor:default;">通知上报网点</span></a></li>
										</c:if>
									</ul>
								</div>
								<div class="msg_tab_panels">
									<c:if test="${que.isElseCustomer==0 }">
										<div class="msg_tab_panel">
											<textarea></textarea>
											<div class="msg_btn">
												<a title="发送" class="btn btn_a notifyCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
												<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
											</div>
										</div>
										<div class="msg_tab_panel" style="display:none;">
											<textarea></textarea>
											<div class="msg_btn">
												<a title="保存" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>保 存</span></a>
												<a title="完成" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>完 成</span></a>
											</div>
										</div>
									</c:if>
									<c:if test="${que.isElseCustomer==1 }">
										<div class="msg_tab_panel">
											<textarea></textarea>
											<div class="msg_btn">
												<a title="保存" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>保 存</span></a>
												<a title="完成" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>完 成</span></a>
											</div>
										</div>
									</c:if>
								</div>
							</div>
                      	</tr>
                      </c:forEach>
                    		</c:otherwise>
                    	</c:choose>
                    </tbody>
                  </table>
                </div>
                <!-- E Table -->
                <c:if test="${questionnaireList!=null && fn:length(questionnaireList)>0 }">
                <div class="attention">
                  <input class="checked_all" type="checkbox" />
                  <a href="javascript:;" class="btn btn_f" id="moveToOther" title="移动到其它"><span>移动到其它</span></a>
                 </div>
                 </c:if>
                <!-- S PageNavi -->
                <div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
                <!-- E PageNavi -->
              </div>
              
              <!-- 已通知 -->
              <div id="tab_panel_b" class="tab_panel clearfix" style="display:none;">
                <!-- S Table -->
                <div class="table">
                  <table>
                    <thead>
                      <tr>
                        <th class="th_a"> <div class="th_title"><em>
                            <input type="checkbox" class="checked_all" />
                            </em></div>
                        </th>
                        <th class="th_b"> <div class="th_title"><em>问题件</em></div>
                        </th>
                        <th class="th_c"> <div class="th_title"><em>问题描述</em></div>
                        </th>
                        <th class="th_d"> <div class="th_title"><em>处理信息</em></div>
                        </th>
                        <th class="th_e"> <div class="th_title"><em>反馈信息</em></div>
                        </th>
                        <th class="th_f"> <div class="th_title"><em>发送消息</em></div>
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                  		<c:when test="${index==0 && (questionnaireList==null || fn:length(questionnaireList)<1) }">
                  			<tr align="center">
                  				<td colspan="6">抱歉，暂无您所查找的数据</td>
                  			</tr>
                  		</c:when>
                  		<c:otherwise>
                      <c:forEach items="${questionnaireList}" var="que" varStatus="status">
                      <tr>
                        <td class="td_a">
                        	<input type="checkbox" class="que_checkbox2" value="${que.id }"/>
                        	<input type="hidden" value="${que.mailNO }" id="mailno"/>
                        	<input type="hidden" value="${que.wdIsRead }" id="state"/>
                        	<a href="javascript:;" class="showSameMail" name="${que.mailNO}"><img src="${imagesPath}/single/lj.png" title="显示所有相同运单" /></a>
                        </td>
                        <td class="td_b"><div class="topdes"><a href="javascript:;" class="mailno" val="${que.mailNO}" title="运单号" >${que.mailNO }</a></div>
                          <div class="btdes"> 类型：${dealMap[que.feedbackInfo]}<br />
                           	金刚状态：
                           		<c:choose>
	                            	<c:when test="${que.issueStatus == 'PD10'}">未处理</c:when>
	                            	<c:when test="${que.issueStatus == 'PD20'}">处理中</c:when>
	                            	<c:when test="${que.issueStatus == 'PD30'}">已处理</c:when>
	                            	<c:when test="${que.issueStatus == 'PD40'}">取消</c:when>
                            	</c:choose>
                            </div></td>
                        <td class="td_c">
                        	<div class="topdes height_auto">
	                       		 <p><span class="que_issue">${que.issueDesc}</span></p>
                        	</div>
                        	<div class="btdes"> 
                        		<c:if test="${que.IMG1!=null }"><a href="${que.IMG1 }" target="_black">图片1</a></c:if>
				                <c:if test="${que.IMG2!=null }"><a href="${que.IMG2 }" target="_black">图片2</a></c:if>
				                <c:if test="${que.IMG3!=null }"><a href="${que.IMG3 }" target="_black">图片3</a></c:if>
				                <c:if test="${que.IMG4!=null }"><a href="${que.IMG4 }" target="_black">图片4</a></c:if>
                        	</div>
                         	<div class="btdes"> 问题上报网点：${que.reportBranckText}<br />
                            ${que.issueCreateUserText} <fmt:formatDate value="${que.issueCreateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
                        </td>
                        <td class="td_d">
                        	<div class="topdes height_auto">
                       		 <p><span class="que_issue">${que.quesDealString}</span></p>
                          	</div>
                        </td>
                        <td class="td_e doneNotify">
                     		<c:choose>
                     		<c:when test="${que.wdIsRead == 0}">
		                        <div class="topdes unread height_auto" name="${que.id }">
                        	</c:when>
                        	<c:otherwise>
                            	<div class="topdes height_auto">
                        	</c:otherwise>
                        	</c:choose>
	                               <p><span class="que_issue">${que.questionaireExchangeString }</span></p>
                          	</div>
                          	<p><a class="showQuesDeal" href="javascript:;" name="${que.id }">网点沟通记录</a></p>
                          	<div style="display:none" id="showQuesDeal${que.id }">
								<table>
									<c:choose>
			                     		<c:when test="${que.quesDealList != null && fn:length(que.quesDealList)>0}">
					                        <c:forEach items="${que.quesDealList}" var="quesD">
												<tr>
													<td>${quesD.dealContent }</td>
												</tr>
												<tr>
													<td>${quesD.dealBranckText }</td>
												</tr>
												<tr>
													<td>
														${quesD.dealUserText }<br />
														<fmt:formatDate value="${quesD.dealTime}" pattern="yyyy-MM-dd HH:mm:ss" />
													</td>
												</tr>
											</c:forEach>
			                        	</c:when>
			                        	<c:otherwise>
			                            	<tr><td>暂无沟通记录</td></tr>
			                        	</c:otherwise>
		                        	</c:choose>
								</table>
	                     	</div>
                          	<div class="show_btn">
								<p>
									<a href="javascript:;" class="btn btn_c" title="已读" name="${que.id }" state="${que.wdIsRead }"><span>已读</span></a>
									<a href="javascript:;" class="btn btn_c" title="未读" name="${que.id }" state="${que.wdIsRead }"><span>未读</span></a>
								</p>
							</div>
                        </td>
                        <td class="td_f">
							<div class="msg_tab tab_trigger2">
								<div class="msg_tab_triggers">
									<ul class="clearfix">
										<c:if test="${que.isElseCustomer==0 }">
											<li class="cur_msg_tab"><a href="javascript:;"><i class="msg_icon_b"></i><span>通知客户</span></a></li>
											<li><a href="javascript:;"><i class="msg_icon_a"></i><span>通知上报网点</span></a></li>
										</c:if>
										<c:if test="${que.isElseCustomer==1 }">
											<li class="cur_msg_tab" style="margin:0 5px 0 60px;"><a href="javascript:;"><i class="msg_icon_a"></i><span>通知上报网点</span></a></li>
										</c:if>
									</ul>
								</div>
								<div class="msg_tab_panels">
									<c:if test="${que.isElseCustomer==0 }">
										<div class="msg_tab_panel">
											<textarea></textarea>
											<div class="msg_btn">
												<a title="发送" class="btn btn_a notifyCustomer" href="javascript:;" name="${que.id}"><span>发 送</span></a>
												<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
											</div>
										</div>
										<div class="msg_tab_panel" style="display:none;">
											<textarea></textarea>
											<div class="msg_btn">
												<a title="保存" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>保 存</span></a>
												<a title="完成" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>完 成</span></a>
											</div>
										</div>
									</c:if>
									<c:if test="${que.isElseCustomer==1 }">
										<div class="msg_tab_panel">
											<textarea></textarea>
											<div class="msg_btn">
												<a title="保存" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>保 存</span></a>
												<a title="完成" class="btn btn_a sendJinGang" href="javascript:;" name="${que.id}"><span>完 成</span></a>
											</div>
										</div>
									</c:if>
								</div>
							</div>
                      	</tr>
                      </c:forEach>
                      </c:otherwise>
                      </c:choose>
                    </tbody>
                  </table>
                </div>
                <!-- E Table -->
                <c:if test="${questionnaireList!=null && fn:length(questionnaireList)>0 }">
                <div class="attention">
                  <input class="input_checkbox checked_all" type="checkbox" />
                  <select id="select_a">
                    <option value="">标记为</option>
                    <option value="1">已读</option>
                    <option value="0">未读</option>
                  </select>
                </div>
                </c:if>
                <!-- S PageNavi -->
                <div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
                <!-- E PageNavi -->
              </div>
              
              <!-- 其它 -->
              <div id="tab_panel_c" class="tab_panel clearfix" style="display:none;">
                <!-- S Table -->
                <div class="table">
                  <table>
                    <thead>
                      <tr>
                        <th class="th_a"> <div class="th_title"><em>
                            </em></div>
                        </th>
                        <th class="th_b"> <div class="th_title"><em>问题件</em></div>
                        </th>
                        <th class="th_c"> <div class="th_title"><em>问题描述</em></div>
                        </th>
                        <th class="th_d"> <div class="th_title"><em>处理信息</em></div>
                        </th>
                        <th class="th_e"> <div class="th_title"><em>客户信息</em></div>
                        </th>
                        <th class="th_f"> <div class="th_title"><em>发送消息</em></div>
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                    	<c:choose>
                    		<c:when test="${index==0 && (questionnaireList==null || fn:length(questionnaireList)<1) }">
                    			<tr align="center">
                    				<td colspan="6">抱歉，暂无您所查找的数据</td>
                    			</tr>
                    		</c:when>
                    		<c:otherwise>
                     	<c:forEach items="${questionnaireList}" var="que3" varStatus="status3">
                     		<tr>
		                        <td class="td_a">
		                          <a href="javascript:;" class="showSameMail" name="${que3.mailNO}"><img src="${imagesPath}/single/lj.png" title="显示所有相同运单" /></a></td>
		                        <td class="td_b"><div class="topdes"><a href="javascript:;" class="mailno" val="${que.mailNO}" title="运单号" >${que3.mailNO}</a></div>
		                          <div class="btdes"> 类型：${dealMap[que3.feedbackInfo]}<br />
		                           	 金刚状态：
		                        <c:choose>
	                            	<c:when test="${que3.issueStatus == 'PD10'}">未处理</c:when>
	                            	<c:when test="${que3.issueStatus == 'PD20'}">处理中</c:when>
	                            	<c:when test="${que3.issueStatus == 'PD30'}">已处理</c:when>
	                            	<c:when test="${que3.issueStatus == 'PD40'}">取消</c:when>
                            	</c:choose></div>
		                        </td>
		                        <td class="td_c">
		                        	<div class="topdes height_auto">
				                        <span class="que_issue">${que3.issueDesc}</span> 
										<input type="hidden" class="issue_desc" value="${que3.issueDesc}" />
										<input type="hidden" class="issue_sub_desc" value="${str:multiSubStr(que3.issueDesc,100)}" />
		                        	</div>
		                        	<div class="btdes"> 
		                        		<c:if test="${que3.IMG1!=null }"><a href="${que3.IMG1 }" target="_black">图片1</a></c:if>
						                <c:if test="${que3.IMG2!=null }"><a href="${que3.IMG2 }" target="_black">图片2</a></c:if>
						                <c:if test="${que3.IMG3!=null }"><a href="${que3.IMG3 }" target="_black">图片3</a></c:if>
						                <c:if test="${que3.IMG4!=null }"><a href="${que3.IMG4 }" target="_black">图片4</a></c:if>
		                        	</div>
		                       		<div class="btdes"> 问题上报网点：${que3.reportBranckText}<br />
		                            ${que3.issueCreateUserText} <fmt:formatDate value="${que3.issueCreateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
		                        </td>
		                        <td class="td_d">
		                        	<div class="topdes height_auto"><span class="que_issue">${que3.quesDealString }</span></div>
                        		</td>
		                        <td class="td_e">
		                        	<div class="topdes">
		                        		<c:if test="${que3.isElseCustomer==1 }">
											这条属于散客、企业客户、未绑定淘宝客户等的问题件信息
										</c:if>
										<c:if test="${que3.isElseCustomer==0 }">
											<p>${que3.customer.userName }</p>
				                   		 	<p>电话：${que3.customer.mobilePhone }</p>
				                   		 	<p>客户编码：${que3.customer.userCode }</p>
			                   		 	</c:if>
		                          	</div>
		                        </td>
		                        <td class="td_f">
		                        	<div class="msg_tab tab_trigger2">
										<div class="msg_tab_triggers">
											<ul class="clearfix">
												<c:if test="${que3.isElseCustomer==0 }">
													<li class="cur_msg_tab"><a href="javascript:;"><i class="msg_icon_b"></i><span>通知客户</span></a></li>
													<li><a href="javascript:;"><i class="msg_icon_a"></i><span>通知上报网点</span></a></li>
												</c:if>
												<c:if test="${que3.isElseCustomer==1 }">
													<li class="cur_msg_tab" style="margin:0 5px 0 60px;"><a href="javascript:;" style="background:none;cursor:default;"><i class="msg_icon_a"></i><span style="cursor:default;">通知上报网点</span></a></li>
												</c:if>
											</ul>
										</div>
										<div class="msg_tab_panels">
											<c:if test="${que3.isElseCustomer==0 }">
												<div class="msg_tab_panel">
													<textarea></textarea>
													<div class="msg_btn">
														<a title="发送" class="btn btn_a notifyCustomer" href="javascript:;" name="${que3.id}"><span>发 送</span></a>
														<a title="清空" class="btn btn_a clear" href="javascript:;"><span>清 空</span></a>
													</div>
												</div>
												<div class="msg_tab_panel" style="display:none;">
													<textarea></textarea>
													<div class="msg_btn">
														<a title="保存" class="btn btn_a sendJinGang" href="javascript:;" name="${que3.id}"><span>保 存</span></a>
														<a title="完成" class="btn btn_a sendJinGang" href="javascript:;" name="${que3.id}"><span>完 成</span></a>
													</div>
												</div>
											</c:if>
											<c:if test="${que3.isElseCustomer==1 }">
												<div class="msg_tab_panel">
													<textarea></textarea>
													<div class="msg_btn">
														<a title="保存" class="btn btn_a sendJinGang" href="javascript:;" name="${que3.id}"><span>保 存</span></a>
														<a title="完成" class="btn btn_a sendJinGang" href="javascript:;" name="${que3.id}"><span>完 成</span></a>
													</div>
												</div>
											</c:if>
										</div>
									</div>
		                        </td>
		                      </tr>
                      	</c:forEach>
                      	</c:otherwise>
                      	</c:choose>
                    </tbody>
                  </table>
                </div>
                <!-- E Table -->
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
			userType:2
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
