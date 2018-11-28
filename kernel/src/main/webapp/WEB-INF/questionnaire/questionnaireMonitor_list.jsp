<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/monitor.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<script type="text/javascript">
	function Show(divid) {
    	if($("#"+divid).text()!="")
        	document.getElementById(divid).style.visibility = "visible";
	}
	function Hide(divid) {
    	document.getElementById(divid).style.visibility = "hidden";
	}
</script>

<title>运单监控</title>
	<!-- S Content -->
	<div id="content">
		<div id="content_hd" class="clearfix">
<!-- 			<h2 id="message_icon">运单监控</h2> -->
			<em><font style="font-weight:900">海量运单走件情况，一键查看！</font><a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
			<c:if test="${yto:getCookie('userType') == 1}">
				<a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=${str:startsWith(yto:getCookie('userType'),'2')?'vip_4_monitor_waybill':'vip_4_monitor_waybill'}" target="_blank" >
	                <span>快速入门</span>
	            </a>
            </c:if>
			
		</div>
		<div id="content_bd" class="clearfix">
			<!-- S Box -->
			<div class="box box_a">
				<div class="box_bd">
					<form action="monitor_list.action" id="sear_form" class="form" method="post">
						<input id="bindUserId" name="bindUserId" type="hidden" value="<s:property value='bindUserId'/>">
						<input id="autoSkip" name="autoSkip" type="hidden" value="<s:property value='autoSkip'/>"/>
						<input id="tabFlag" name="tabFlag" type="hidden" value="<s:property value='tabFlag'/>"/>
			            <input id="status" name="status" type="hidden" value="<s:property value='status'/>"/>
			            <input id="orderType" name="orderType" type="hidden" value="<s:property value='orderType'/>"/>
			            <input id="currentPage" name="currentPage" type="hidden" value="<s:property value='currentPage'/>"/>
						<input type="hidden" name="menuFlag" value="${menuFlag }" />
						<p>
							<label>发货时间：</label>
							<label for="lastest_a" class="label_raido"><input type="radio" id="lastest_a" class="input_radio" name="date_range" value="" />近三天</label>
							<label for="lastest_b" class="label_raido"><input type="radio" id="lastest_b" class="input_radio" name="date_range" value="" />近一周</label>
							<label for="lastest_c" class="label_raido"><input type="radio" id="lastest_c" class="input_radio" name="date_range" value="" />近30天</label>
							<input id="date_start" class="Wdate" type="text" name="starttime" value="${starttime }"/> 
							至 
							<input id="date_end" class="Wdate" type="text" name="endtime" value="${endtime }"/>
							<span id="date_tip"></span>
						</p>
                            <!-- 平台用户 || 平台用户客服账号 -->
							<c:if test="${yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
								<p>
									<label>分仓账号：</label>
								 	<select id="all_per" name="vipId">
						 				<option value=0 <c:if test="${vipId == 0 }">selected</c:if>>所有分仓</option>
						 				<c:forEach var="vip" items="${vipList }">
						 					<option <c:if test="${vipId == vip.id }">selected</c:if> value="${vip.id }">${vip.userNameText}(${vip.userName})</option>
						 				</c:forEach>
									</select>
									<span id="all_per_tip"></span>
								</p>
							</c:if>
                            <!-- 网点客户 -->
							<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
								<p>
									<label>客户名称：</label>
								 	<select id="all_per" name="vipCode">
						 				<c:choose>
							       			<c:when test="${fn:length(threadList)<1}">
							       				<option value="">暂无绑定客户</option>
							       			</c:when>
							       			<c:otherwise>
							       				<c:forEach var="userThread" items="${threadList }">
							       					<option <c:if test="${vipCode == userThread.userCode }">selected</c:if> value="${userThread.userCode }">${userThread.userName }(${userThread.userCode })</option>
							       				</c:forEach>
							       			</c:otherwise>
						       			</c:choose>
						       			<option value="-1" <c:if test="${vipCode == -1}">selected</c:if> >其他客户</option>
									</select>
									<span id="all_per_tip"></span>
								</p>
							</c:if>
						
						<p>
							<label for="province">收货地址：</label>
							<select id="province"></select>
							<span id="area_tip"></span>
							<input type="hidden" name="otherUserId" id="_otherUserId" value='<s:property value="otherUserId"/>'/>
							<input type="hidden" name="numProv" id="x_prov" value='<s:property value="numProv"/>'/>
							<input type="hidden" name="numCity" id="x_city" value='<s:property value="numCity"/>'/>
							<input type="hidden" name="numDistrict" id="x_district" value='<s:property value="numDistrict"/>'/>
							<!-- todo样式问题需修改 -->
<!-- 							<label>运单类型：</label> -->
                            <select id="mailNoOrderType" name="mailNoOrderType"  style="width:150px;" panelWidth="200px;">
                                <option value="" <s:if test="%{mailNoOrderType == ''}">selected</s:if>>所有运单</option>
                                <option value="0,1,66,67,99" <s:if test="%{mailNoOrderType == '0,1,66,67,99'}">selected</s:if>>常规运单</option>
								<option value="3" <s:if test="mailNoOrderType == \"3\"">selected</s:if>>退货运单</option>
                            </select>
						</p>
						<div id="sear_box">
							<input class="input_text" id="input_text_txt" type="text" name="conditionString" value="${conditionString }"/>
							<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查 询"><span>查 询</span></a>
							<span id="input_text_txtTip"></span>
						</div>
					</form>
				</div>
			</div>
			<!-- E Box -->
			<!-- S Box -->
			<div class="box box_a ">
				<div class="box_bd" id="box_bd">
					<!-- S Tab -->
					<div class="tab tab_c">
						<div class="tab_triggers">
							<ul>
								<li tabFlag=1 status=5><a href="javascript:;"><span class="jdz"></span>接单中<s:if test="totalOrderCreate == null"></s:if><s:else>（<s:property value="totalOrderCreate"/>）</s:else></a></li>
								<li tabFlag=1 status=3><a href="javascript:;"><span class="zjz"></span>走件中<s:if test="totalOrderFail == null"></s:if><s:else>（<s:property value="totalOrderFail"/>）</s:else></a></li>
								<li tabFlag=1 status=2><a href="javascript:;"><span class="psz"></span>正在派送<s:if test="totalOrderScan == null"></s:if><s:else>（<s:property value="totalOrderScan"/>）</s:else></a></li>
								<li tabFlag=1 status=1><a href="javascript:;"><span class="cgd"></span>成功订单<s:if test="totalOrderSuccess == null"></s:if><s:else>（<s:property value="totalOrderSuccess"/>）</s:else></a></li>
								<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
									<li tabFlag=2 status=0><a href="javascript:;"><span class="gzd"></span>关注运单<s:if test="totalAttention == null"></s:if><s:else>（<label id="totalAttention"><s:property value="totalAttention"/></label>）</s:else></a></li>
								</c:if>
							</ul>
						</div>
						<div class="tab_panels">
							<div id="tab_panel_a" class="tab_panel clearfix">
								<!-- 接单中 S Table -->
								<div class="table">
									<table>
										<thead>
											<tr>
												<th class="th_a">
													<div class="th_title"><em>运单号</em></div>
												</th>
												<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
													<th class="th_b">
														<div class="th_title"><em>店铺名称</em>
															<ul class="thead_select">
																<s:iterator value="bindUserList">
																	<li value="<s:property value='id'/>"><s:property value="shopName"/></li>
																</s:iterator>
																<li value=0><a href="#">设置选项</a></li>
															</ul>
														</div>
													</th>
												</c:if>
												<c:if test="${yto:getCookie('userType')==4 || yto:getCookie('userType') == 41}">
	                                                <th class="th_b">
	                                                  	账号
	                                                </th>
                                            	</c:if>
												<th class="th_c">
													<div class="th_title"><em>当前位置（时间）</em></div>
												</th>
												<!-- 应需求：状态去掉 
												<th class="th_d">
													<div class="th_title"><em>状态</em></div>
												</th>-->
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
													<div class="th_title"><em <c:if test="${orderType==2 }">class="arrow_down"</c:if><c:if test="${orderType==1 }">class="arrow_up"</c:if>>发货时间</em></div>
												</th>
												<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
													<th>
													</th>
                                            	</c:if>
											</tr>
										</thead>
										
										<tbody>
											<s:if test="monitoreList==null || monitoreList.size() == 0">
	                                            <tr>
	                                                <td colspan="9" align="center">
	                                                	<c:if test="${isShow!=0 }">
		                                               		<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
			                                                	抱歉，找不到您的订单，如有问题请联系我们<br>
			                                                	联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
			                                                </c:if>
			                                                <c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
																抱歉，找不到您的订单，如有问题请联系我们<br>
															</c:if>
														</c:if>
	                                                </td>
	                                            </tr>
	                                        </s:if>
	                                        <s:iterator value="monitoreList" id="monitor">
	                                        	<tr>
		                                        	<td class="td_a">
		                                        		<a href="javascript:;" title="查看订单详情" style="text-decoration:underline;color:blue" class="mailno" val="${mailNO }">
	                                                        <s:property value="mailNO"/>
	                                                    </a>
		                                        	</td>
		                                        	<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                        		<td class="td_b"><s:property value="shopName"/></td>
		                                        	</c:if>
													<s:if test="stepInfo.acceptAddress!=null">
	                                                    <td class="td_c">
	                                                        <s:property value="stepInfo.acceptAddress"/>&nbsp;<s:property value="stepInfo.remark"/><br>
	                                                        <s:property value="stepInfo.acceptTime.substring(0,10)"/>&nbsp;<s:property value="stepInfo.acceptTime.substring(10,18)"/>
	                                                        <s:if test="stepInfo.contactWay!=null && stepInfo.contactWay!=''"><s:property value="stepInfo.contactWay"/></s:if>
	                                                    </td>
	                                                </s:if>
	                                                <s:else>
	                                                    <td class="td_c" align="center">
	                                                        <img src="images/icons/noneStep.PNG" title="暂无走件信息，请稍后查询"/>
	                                                    </td>
	                                                </s:else>
	                                                <td class="td_e"><s:property value="userName"/></td>
	                                                <td class="td_f"><s:property value="phone"/></td>
													
													<td id="destination_<s:property value="id"/>" class="td_g" onMouseOver="Show('infoDiv1_<s:property value="id"/>');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv1_<s:property value="id"/>')">
	                                                    <s:if test="%{destination.length() <= 5}" >
	                                                        <s:property value="destination"/>
	                                                    </s:if>
	                                                    <s:if test="%{destination.length() > 5}">
	                                                        <s:property value="destination.substring(0,5)"/>...
	                                                    </s:if>
	                                                    <div id="infoDiv1_<s:property value="id"/>" class="article" style="width:140px;"><s:property value="destination"/></div>
	                                                </td>
	                                                <td class="td_h">
	                                                    <s:date name="senderTime" format="yyyy-MM-dd HH:mm:ss"/>
	                                                </td>
													<c:if test="${yto:getCookie('userType')==1
                                                              || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
                                                      <td>
                                                          <s:if test="isAttention==1">
                                                              <img style="cursor:pointer;" title="取消关注" src="images/icons/attention.png" onClick="addInAttention(this,'<s:property value='mailNO'/>')">
                                                          </s:if>
                                                          <s:else>
                                                              <img style="cursor:pointer;" title="加入关注" src="images/icons/nonatten.png" onClick="addInAttention(this,'<s:property value='mailNO'/>')">
                                                          </s:else>
                                                      </td>
                                                	</c:if>
												</tr>
	                                        </s:iterator>
										</tbody>
									</table>
								</div>
								<!-- E Table -->
								
								<!-- S PageNavi -->
								<div class="pagenavi">
									<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
								</div>
								<!-- E PageNavi -->
							</div>
							<div id="tab_panel_b" class="tab_panel clearfix" style="display:none;">
								<!-- 走件中 S Table -->
								<div class="table">
									<table>
										<thead>
											<tr>
												<th class="th_a">
													<div class="th_title"><em>运单号</em></div>
												</th>
												<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
													<th class="th_b">
														<div class="th_title"><em>店铺名称</em>
															<ul class="thead_select">
																<s:iterator value="bindUserList">
																	<li value="<s:property value='id'/>"><s:property value="shopName"/></li>
																</s:iterator>
																<li><a href="associationAccount_toBindeAccountCustom.action">设置选项</a></li>
															</ul>
														</div>
													</th>
												</c:if>
												<c:if test="${yto:getCookie('userType')==4 || yto:getCookie('userType') == 41}">
	                                                <th class="th_b">
	                                                  	账号
	                                                </th>
                                            	</c:if>
												<th class="th_c">
													<div class="th_title"><em>当前位置（时间）</em></div>
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
													<div class="th_title"><em <c:if test="${orderType==2 }">class="arrow_down"</c:if><c:if test="${orderType==1 }">class="arrow_up"</c:if>>发货时间</em></div>
												</th>
												<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
													<th>
													</th>
                                            	</c:if>
											</tr>
										</thead>
										
										<tbody>
	                                        <s:if test="monitoreList==null || monitoreList.size() == 0">
	                                            <tr>
	                                                <td colspan="9" align="center">
	                                               		<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                                	抱歉，找不到您的订单，如有问题请联系我们<br>
		                                                	联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
		                                                </c:if>
		                                                <c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
															抱歉，找不到您的订单，如有问题请联系我们<br>
														</c:if>
	                                                </td>
	                                            </tr>
	                                        </s:if>
											<s:iterator value="monitoreList" id="monitor">
	                                        	<tr>
		                                        	<td class="td_a">
		                                        		<a href="javascript:;" title="查看订单详情" style="text-decoration:underline;color:blue" class="mailno" val="${mailNO }">
	                                                        <s:property value="mailNO"/>
	                                                    </a>
		                                        	</td>
		                                        	<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                        		<td class="td_b"><s:property value="shopName"/></td>
		                                        	</c:if>
													<s:if test="stepInfo.acceptAddress!=null">
	                                                    <td class="td_c">
															<div class="td_c_add">
																<s:property value="stepInfo.acceptAddress"/>&nbsp;<s:property value="stepInfo.remark"/><br>
																<s:property value="stepInfo.acceptTime.substring(0,10)"/>&nbsp;<s:property value="stepInfo.acceptTime.substring(10,18)"/>
																<s:if test="stepInfo.contactWay!=null && stepInfo.contactWay!=''"><s:property value="stepInfo.contactWay"/></s:if>
															</div>
	                                                    </td>
	                                                </s:if>
	                                                <s:else>
	                                                    <td class="td_c" align="center">
	                                                     	该件已经接单，准备走件。。。
	                                                    </td>
	                                                </s:else>
	                                                <td class="td_e"><s:property value="userName"/></td>
	                                                <td class="td_f"><s:property value="phone"/></td>
													
													<td id="destination_<s:property value="id"/>" class="td_g" onMouseOver="Show('infoDiv2_<s:property value="id"/>');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv2_<s:property value="id"/>')">
	                                                    <s:if test="%{destination.length() <= 5}" >
	                                                        <s:property value="destination"/>
	                                                    </s:if>
	                                                    <s:if test="%{destination.length() > 5}">
	                                                        <s:property value="destination.substring(0,5)"/>...
	                                                    </s:if>
	                                                    <div id="infoDiv2_<s:property value="id"/>" class="article" style="width:140px;"><s:property value="destination"/></div>
	                                                </td>
	                                                <td class="td_h">
	                                                    <s:date name="senderTime" format="yyyy-MM-dd HH:mm:ss"/>
	                                                </td>
													<c:if test="${yto:getCookie('userType')==1
                                                              || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
                                                      <td>
                                                          <s:if test="isAttention==1">
                                                              <img style="cursor:pointer;" title="取消关注" src="images/icons/attention.png" onClick="addInAttention(this,'<s:property value='mailNO'/>')">
                                                          </s:if>
                                                          <s:else>
                                                              <img style="cursor:pointer;" title="加入关注" src="images/icons/nonatten.png" onClick="addInAttention(this,'<s:property value='mailNO'/>')">
                                                          </s:else>
                                                      </td>
                                                	</c:if>
												</tr>
	                                        </s:iterator>
										</tbody>
									</table>
								</div>
								<!-- E Table -->
								<!-- S PageNavi -->
								<div class="pagenavi">
									<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
								</div>
								<!-- E PageNavi -->
							</div>
							<div id="tab_panel_b" class="tab_panel clearfix" style="display:none;">
								<!-- 派件 S Table -->
								<div class="table">
									<table>
										<thead>
											<tr>
												<th class="th_a">
													<div class="th_title"><em>运单号</em></div>
												</th>
												<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
													<th class="th_b">
														<div class="th_title"><em>店铺名称</em>
															<ul class="thead_select">
																<s:iterator value="bindUserList">
																	<li value="<s:property value='id'/>"><s:property value="shopName"/></li>
																</s:iterator>
																<li><a href="associationAccount_toBindeAccountCustom.action">设置选项</a></li>
															</ul>
														</div>
													</th>
												</c:if>
												<c:if test="${yto:getCookie('userType')==4 || yto:getCookie('userType') == 41}">
	                                                <th class="th_b">
	                                                  	账号
	                                                </th>
                                            	</c:if>
												<th class="th_c">
													<div class="th_title"><em>当前位置（时间）</em></div>
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
													<div class="th_title"><em <c:if test="${orderType==2 }">class="arrow_down"</c:if><c:if test="${orderType==1 }">class="arrow_up"</c:if>>发货时间</em></div>
												</th>
												<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
													<th>
													</th>
                                            	</c:if>
											</tr>
										</thead>
										
										<tbody>
	                                        <s:if test="monitoreList==null || monitoreList.size() == 0">
	                                            <tr>
	                                                <td colspan="9" align="center">
	                                               		 <c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                                	抱歉，找不到您的订单，如有问题请联系我们<br>
		                                                	联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
		                                                </c:if>
		                                                <c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
															抱歉，找不到您的订单，如有问题请联系我们<br>
														</c:if>
	                                                </td>
	                                            </tr>
	                                        </s:if>
											<s:iterator value="monitoreList" id="monitor">
	                                        	<tr>
		                                        	<td class="td_a">
		                                        		<a href="javascript:;" title="查看订单详情" style="text-decoration:underline;color:blue" class="mailno" val="${mailNO }">
	                                                        <s:property value="mailNO"/>
	                                                    </a>
		                                        	</td>
		                                        	<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                        		<td class="td_b"><s:property value="shopName"/></td>
		                                        	</c:if>
													<s:if test="stepInfo.acceptAddress!=null">
	                                                    <td class="td_c">
	                                                        <s:property value="stepInfo.acceptAddress"/>&nbsp;<s:property value="stepInfo.remark"/><br>
	                                                        <s:property value="stepInfo.acceptTime.substring(0,10)"/>&nbsp;<s:property value="stepInfo.acceptTime.substring(10,18)"/>
	                                                        <s:if test="stepInfo.contactWay!=null && stepInfo.contactWay!=''"><s:property value="stepInfo.contactWay"/></s:if>
	                                                    </td>
	                                                </s:if>
	                                                <s:else>
	                                                    <td class="td_c" align="center">
	                                                        <img src="images/icons/noneStep.PNG" title="暂无走件信息，请稍后查询"/>
	                                                    </td>
	                                                </s:else>
	                                                <td class="td_e"><s:property value="userName"/></td>
	                                                <td class="td_f"><s:property value="phone"/></td>
													
													<td id="destination_<s:property value="id"/>" class="td_g" onMouseOver="Show('infoDiv3_<s:property value="id"/>');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv3_<s:property value="id"/>')">
	                                                    <s:if test="%{destination.length() <= 5}" >
	                                                        <s:property value="destination"/>
	                                                    </s:if>
	                                                    <s:if test="%{destination.length() > 5}">
	                                                        <s:property value="destination.substring(0,5)"/>...
	                                                    </s:if>
	                                                    <div id="infoDiv3_<s:property value="id"/>" class="article" style="width:140px;"><s:property value="destination"/></div>
	                                                </td>
	                                                <td class="td_h">
	                                                    <s:date name="senderTime" format="yyyy-MM-dd HH:mm:ss"/>
	                                                </td>
													<c:if test="${yto:getCookie('userType')==1
                                                              || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
                                                      <td>
                                                          <s:if test="isAttention==1">
                                                              <img style="cursor:pointer;" title="取消关注" src="images/icons/attention.png" onClick="addInAttention(this,'<s:property value='mailNO'/>')">
                                                          </s:if>
                                                          <s:else>
                                                              <img style="cursor:pointer;" title="加入关注" src="images/icons/nonatten.png" onClick="addInAttention(this,'<s:property value='mailNO'/>')">
                                                          </s:else>
                                                      </td>
                                                	</c:if>
												</tr>
	                                        </s:iterator>
										</tbody>
									</table>
								</div>
								<!-- E Table -->
								<!-- S PageNavi -->
								<div class="pagenavi">
									<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
								</div>
								<!-- E PageNavi -->
							</div>
							<div id="tab_panel_c" class="tab_panel clearfix" style="display:none;">
								<!-- 成功订单 S Table -->
								<div class="table">
									<table>
										<thead>
											<tr>
												<th class="th_a">
													<div class="th_title"><em>运单号</em></div>
												</th>
												<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
													<th class="th_b">
														<div class="th_title"><em>店铺名称</em>
															<ul class="thead_select">
																<s:iterator value="bindUserList">
																	<li value="<s:property value='id'/>"><s:property value="shopName"/></li>
																</s:iterator>
																<li><a href="associationAccount_toBindeAccountCustom.action">设置选项</a></li>
															</ul>
														</div>
													</th>
												</c:if>
												<c:if test="${yto:getCookie('userType')==4 || yto:getCookie('userType') == 41}">
	                                                <th class="th_b">
	                                                  	账号
	                                                </th>
                                            	</c:if>
												<th class="th_d">
													<div class="th_title"><em>买家姓名</em></div>
												</th>
												<th class="th_e">
													<div class="th_title"><em>买家电话</em></div>
												</th>
												<th class="th_f">
													<div class="th_title"><em>收货地址</em></div>
												</th>
												<th class="th_g">
													<div class="th_title"><em <c:if test="${orderType==2 }">class="arrow_down"</c:if><c:if test="${orderType==1 }">class="arrow_up"</c:if>>发货时间</em></div>
												</th>
											</tr>
										</thead>
										
										<tbody>
											<s:if test="monitoreList==null || monitoreList.size() == 0">
	                                            <tr>
	                                                <td colspan="7" align="center"> 
	                                                	<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                                	抱歉，找不到您的订单，如有问题请联系我们<br>
		                                                	联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
		                                                </c:if>
		                                                <c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
															抱歉，找不到您的订单，如有问题请联系我们<br>
														</c:if>
	                                                </td>
	                                            </tr>
	                                        </s:if>
											<s:iterator value="monitoreList" id="monitor">
	                                        	<tr>
		                                        	<td class="td_a">
		                                        		<a href="javascript:;" title="查看订单详情" style="text-decoration:underline;color:blue" class="mailno" val="${mailNO }">
	                                                        <s:property value="mailNO"/>
	                                                    </a>
		                                        	</td>
		                                        	<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                        		<td class="td_b"><s:property value="shopName"/></td>
		                                        	</c:if>
	                                                <td class="td_d"><s:property value="userName"/></td>
	                                                <td class="td_e"><s:property value="phone"/></td>
													<td id="destination_<s:property value="id"/>" class="td_f" onMouseOver="Show('infoDiv4_<s:property value="id"/>');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv4_<s:property value="id"/>')">
	                                                    <s:if test="%{destination.length() <= 5}" >
	                                                        <s:property value="destination"/>
	                                                    </s:if>
	                                                    <s:if test="%{destination.length() > 5}">
	                                                        <s:property value="destination.substring(0,5)"/>...
	                                                    </s:if>
	                                                    <div id="infoDiv4_<s:property value="id"/>" class="article" style="width:140px;"><s:property value="destination"/></div>
	                                                </td>
	                                                <td class="td_g">
	                                                    <s:date name="senderTime" format="yyyy-MM-dd HH:mm:ss"/>
	                                                </td>
												</tr>
	                                        </s:iterator>
										</tbody>
									</table>
								</div>
								<!-- E Table -->
								<!-- S PageNavi -->
								<div class="pagenavi">
									<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
								</div>
								<!-- E PageNavi -->
							</div>
							<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 13 || yto:getCookie('userType')==2 || yto:getCookie('userType')==21 || yto:getCookie('userType')==23}">
							<div id="tab_panel_e" class="tab_panel clearfix" style="display:none;">
								<!-- 我的关注 S Table -->
								<div class="table">
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
													<div class="th_title"><em <c:if test="${orderType==2 }">class="arrow_down"</c:if><c:if test="${orderType==1 }">class="arrow_up"</c:if>>发货时间</em></div>
												</th>
												<th></th>
											</tr>
										</thead>
										
										<tbody>
											<s:if test="monitoreList==null || monitoreList.size() == 0">
	                                            <tr>
	                                                <td colspan="8" align="center">
	                                                	<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 || yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
		                                                	抱歉，找不到您的订单，如有问题请联系我们<br>
		                                                	联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
		                                                </c:if>
		                                                <c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
															抱歉，找不到您的订单，如有问题请联系我们<br>
														</c:if>
	                                                </td>
	                                            </tr>
	                                        </s:if>
	                                        <s:iterator value="monitoreList" id="monitor">
												<tr>
													<td class="td_a"><input class="input_checkbox" type="checkbox" value="<s:property value='id'/>"></td>
													<td class="td_b">
														<a href="javascript:;" title="查看订单详情" style="text-decoration:underline;color:blue" class="mailno" val="${mailNO }">
	                                                        <s:property value="mailNO"/>
	                                                    </a>
													</td>
													<s:if test="stepInfo.acceptAddress!=null">
	                                                    <td class="td_c">
	                                                        <s:property value="stepInfo.acceptAddress"/>&nbsp;<s:property value="stepInfo.remark"/><br>
	                                                        <s:property value="stepInfo.acceptTime.substring(0,10)"/>&nbsp;<s:property value="stepInfo.acceptTime.substring(10,18)"/>
	                                                        <s:if test="stepInfo.contactWay!=null && stepInfo.contactWay!=''"><s:property value="stepInfo.contactWay"/></s:if>
	                                                    </td>
	                                                </s:if>
	                                                <s:else>
	                                                    <td class="td_c" align="center">
	                                                        <img src="images/icons/noneStep.PNG" title="暂无走件信息，请稍后查询"/>
	                                                    </td>
	                                                </s:else>
													<td class="td_e"><s:property value="userName"/></td>
	                                                <td class="td_f"><s:property value="phone"/></td>
													<td id="destination_<s:property value="id"/>" class="td_g" onMouseOver="Show('infoDiv5_<s:property value="id"/>');this.style.cursor= 'pointer'" onMouseOut="Hide('infoDiv5_<s:property value="id"/>')">
	                                                    <s:if test="%{destination.length() <= 5}" >
	                                                        <s:property value="destination"/>
	                                                    </s:if>
	                                                    <s:if test="%{destination.length() > 5}">
	                                                        <s:property value="destination.substring(0,5)"/>...
	                                                    </s:if>
	                                                    <div id="infoDiv5_<s:property value="id"/>" class="article" style="width:140px;"><s:property value="destination"/></div>
	                                                </td>
	                                                <td class="td_h">
	                                                    <s:date name="senderTime" format="yyyy-MM-dd HH:mm:ss"/>
	                                                </td>
													<td><img class="cancelAttention" value="<s:property value='id'/>" title="取消关注" src="images/icons/attention.png"/></td>
												</tr>
											</s:iterator>
										</tbody>
									</table>
								</div>
								<!-- E Table -->
								<s:if test="monitoreList!=null && monitoreList.size() > 0">
									<div class="attention">
										<input class="input_checkbox checked_all" type="checkbox" />
										<a href="javascript:;" class="btn btn_a" title="取消关注"><span>取消关注</span></a>
									</div>
								</s:if>
								<!-- S PageNavi -->
								<div class="pagenavi">
									<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
								</div>
								<!-- E PageNavi -->
							</div>
							</c:if>
						</div>
					</div>
					<!-- E Tab -->
				</div>
			</div>
			<!-- E Box -->
			
		</div>
		
	<script type="text/javascript">
		var params = {
			<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
				userType : 1,
			</c:if>
			<c:if test="${yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
				userType : 2,
			</c:if>
			onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
				? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
			userId:${yto:getCookie('id')},						//当前登录用户的id
			userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
			infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
			bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
			pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
		}
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/monitor.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
	</div>
	<!-- E Content -->

