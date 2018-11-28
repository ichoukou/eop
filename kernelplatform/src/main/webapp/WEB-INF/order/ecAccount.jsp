<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/ec_account.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>电子对账</title>
	<!-- S Content -->
	<div id="content">
		<div id="content_hd" class="clearfix">
<!-- 			<h2 id="message_icon">电子对账</h2> -->
			<em><font style="font-weight:900">助您实时了解运费成本，使用电子对账前请先确认您的网点是否为您设置好运费模板。</font><a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
			<a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=${str:startsWith(yto:getCookie('userType'),'2')?'site':'vip'}_6_ecaccount" target="_blank" >
                <span>快速入门</span>
            </a>
			
			<p>选择发货时间段→收货区域(可选)→统计 &nbsp;<font color="blue">电子对账仅提供淘宝和已对接B2C商城账单，其他账单，敬请关注易通动态。</font></p>
		</div>
		<div id="content_bd" class="clearfix">
			<!-- S 搜索条件 -->
			<div id="search_box">
				<form action="order!ecAccount.action" id="sear_form" class="form" method="post">
					<input name="userType" id="userType" type="hidden" value='${yto:getCookie('userType')}'>
					<input type="hidden" id="tempKey" value="<s:property value='tempKey'/>"/>
					<input type="hidden" name="currentPage" id="currentPage" value='<s:property value="currentPage" />'/>
					<input id="bindUserId" name='bindUserId' type="hidden" value="<s:property value='bindUserId'/>">
					<input type="hidden" name="menuFlag" value="${menuFlag }" />
					<input name="action" id="action" type="hidden" >
					<!-- 网点用户 -->
					<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
						<p>
							<label>客户名称：</label>
				        	<select id="all_per" name="customerCode">
				        		<c:choose>
					       			<c:when test="${fn:length(vipThreadList)<1}">
					       				<option value="">暂无绑定客户</option>
					       			</c:when>
					       			<c:otherwise>
					       				<c:forEach var="userThread" items="${vipThreadList }">
					       					<option <c:if test="${customerCode == userThread.userCode }">selected</c:if> value="${userThread.userCode }">${userThread.userName }(${userThread.userCode })</option>
					       				</c:forEach>
					       			</c:otherwise>
				       			</c:choose>
				        	</select>
				        	<span id="all_per_tip"></span>
						</p>
					</c:if>
					<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
						<div style="display:none;">
							<input id="Tusercode" value="<c:out value="${yto:getCookie('userCode')}"/>">
						</div>
					</c:if>
					<!-- 平台用户 || 平台用户客服账号 -->
					<c:if test="${yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
						<p>
							<label>分仓账号：</label>
						 	<select id="all_per" name="fencangUserId">
						 		<c:choose>
						 			<c:when test="${fn:length(fcActivedUserList)<1 }">
						 				<option value="">没有激活的业务账号</option>
						 			</c:when>
						 			<c:otherwise>
						 				<c:forEach items="${fcActivedUserList }" var="fcUser">
						 					<c:choose>
						                		<c:when test="${fcUser.id==fencangUserId }">
						                			<option selected value="${fcUser.id}">分仓：${fcUser.userNameText}</option>
						                		</c:when>
						                		<c:otherwise>
						                			<option value="${fcUser.id}">分仓：${fcUser.userNameText}</option>
						                		</c:otherwise>
						                	</c:choose>
						 				</c:forEach>
						 			</c:otherwise>
						 		</c:choose>
							</select>
							<span id="all_per_tip"></span>
						</p>
					</c:if>
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
					<p>
						<label for="province">收货地址：</label>
						<select id="province"></select>
						<span id="area_tip"></span>
						<input type="hidden" name="prov" id="x_prov" value='<s:property value="prov"/>'/>
						<input type="hidden" name="city" id="x_city" value='<s:property value="city"/>'/>
						<input type="hidden" name="district" id="x_district" value='<s:property value="district"/>'/>
					</p>
					<p>
						<input class="input_text" id="input_text_txt" type="text" name="queryCondition" value="${queryCondition }" style="width:177px"/>
					</p>
					<input type="hidden" name="posttempId" id="posttempId"/>
					<div id="sear_box">
<%-- 						<input type="text" name="queryCondition" class="input_text" id="input_text_text" value="<s:property value="queryCondition"/>"/>  --%>
						<a href="javascript:;" id="sear_btn" class="btn btn_a" title="统 计"><span>统 计</span></a>
						<c:if test="${yto:getCookie('userType') == 1
							 || yto:getCookie('userType') == 12
							 || yto:getCookie('userType') == 13
							 || yto:getCookie('userType') == 2
							 || yto:getCookie('userType') == 22
							 || yto:getCookie('userType') == 23 }">
							<a href="javascript:;" id="export_btn" class="btn btn_a" title="导 出"><span>导 出</span></a>
						</c:if>
						<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13 }">
						<span class="view_monitor">查看<a href="posttemp!toPosttempView2.action?menuFlag=caiwu_ecAccount">运费模板</a></span>
						</c:if>
					</div>
				</form>
			</div>
			<!-- E 搜索条件 -->
			
			<div id="table_stats" class="table">
				<table>
					<thead>
						<tr>
							<th>
							说明：实际支付=已计算的运费+无法计算的运费（需人工核算）
							<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
								<span id="table_tip" style="display:none;">当前使用系统默认运费模板统计，如模板价格存在误差请联系您的服务网点！</span>
							</c:if>
							</th>
						</tr>
					</thead>
				</table>
				
				<div id="table_wp">
					<table>
						<tbody>
							<tr>
								<td colspan="2" class="td_l">
									<span class="font_style_a">已计算的运费（元）：</span>
									<span class="font_style_b">
										<s:if test="#request.totalFreight==null">0.00</s:if><s:else><s:property value="#request.totalFreight"/></s:else>
									</span>
								</td>
								<td colspan="2" class="td_r">
									<span class="font_style_a">总订单数（条）：</span>
									<span class="font_style_b"><s:property value="pagination.totalRecords"/></span>
								</td>
							</tr>
							<tr>
								<td>
									<input id="sendProv" value="<s:property value='sendProv'/>" type="hidden" />
									<span class="font_style_c">无法计费订单（条）：</span>
									<span class="font_style_d errorOrder"><s:if test="errorOrderList == null">0</s:if><s:else><s:property value="errorOrderList.size"/></s:else></span>
								</td>
								<td>
									<span class="font_style_e">标准计费订单（条）：</span>
									<span class="font_style_b"><s:if test="pagination.totalRecords == 0">0</s:if><s:else><s:property value="pagination.totalRecords-ufOrderList.size-errorOrderList.size-thOrderList.size"/></s:else></span>
								</td>
								<td>
									<span class="font_style_e">调整计费订单（条）：</span>
									<span class="font_style_d ufOrder"><s:if test="ufOrderList == null">0</s:if><s:else><s:property value="ufOrderList.size"/></s:else></span>
								</td>
								<td>
									<span class="font_style_e">退货订单（条）：</span>
									<span class="font_style_d thOrder"><s:if test="thOrderList == null">0</s:if><s:else><s:property value="thOrderList.size"/></s:else></span>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<!-- S Table -->
			<div id="search_detail" class="table">
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
							<th class="th_c">
								<div class="th_title"><em>状态</em></div>
							</th>
							<th class="th_d">
								<div class="th_title"><em>重量(kg)</em></div>
							</th>
							<th class="th_e">
								<div class="th_title"><em>收货地址</em></div>
							</th>
							<th class="th_f">
								<div class="th_title"><em>实收运费</em></div>
							</th>
						</tr>
					</thead>
					
					<tbody>
						<s:iterator value="#request.orderList" var="order">
							<tr>
								<s:if test="#order.remark == \"errorProv\"">
									<td class="td_a"><a href="javascript:;" title="订单发件地和运费模板发件地不一致。点击查看运单详情"  style="text-decoration:underline;color:red" class="mailno" val="<s:property value='#order.mailNo'/>"><s:property value="#order.mailNo"/></a></td>
								</s:if>
								<s:elseif test="#order.remark == \"errorProvCity\"">
									<td class="td_a"><a href="javascript:;" title="订单发件地和网点发件地不一致。点击查看运单详情"  style="text-decoration:underline;color:red" class="mailno" val="<s:property value='#order.mailNo'/>"><s:property value="#order.mailNo"/></a></td>
								</s:elseif>
								<s:elseif test="#order.remark == \"errorPostinfo\"">
									<td class="td_a"><a href="javascript:;" title="订单目的地不在运费模板中，不计算运费。点击查看运单详情"  style="text-decoration:underline;color:red" class="mailno" val="<s:property value='#order.mailNo'/>"><s:property value="#order.mailNo"/></a></td>
								</s:elseif>
								<s:elseif test="#order.mailNo ==''">
									<td class="td_a"><a href="javascript:;" title="<s:property value='#order.txLogisticId'/>"> 暂无运单号</a></td>
								</s:elseif>
								<s:else>
									<td class="td_a"><a href="javascript:;" title="查看运单详情" style="text-decoration:underline;color:blue" class="mailno" val="<s:property value='#order.mailNo'/>"><s:property value="#order.mailNo"/></a></td>
								</s:else>
								<c:if test="${yto:getCookie('userType')==1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
							    	<td class="td_b"><s:property value="shopName"/></td>
						      	</c:if>
								<td class="td_c">
									<s:property value="#order.status"/><br>
									<s:if test="#order.acceptTime==null"><s:date name="#order.createTime" format="yyyy-MM-dd HH:mm:ss"/></s:if>
									<s:else><s:date name="#order.acceptTime" format="yyyy-MM-dd HH:mm:ss"/></s:else>
								</td>
								<td class="td_d" title="没有称重信息按小于1公斤计算">
									<s:if test="#order.weight == 0">≤1</s:if>
									<s:else><s:property value="#order.weight"/></s:else>
								</td>
								<td class="td_e">
									<span class="td_e_add">
										<s:property value="#order.toAddr"/>
									</span>
								</td>
								<s:if test="#order.remark == \"errorProv\"">
									<td class="td_f" style="padding-left:0px;padding-right:0px;text-align:center;color:red;cursor:help;" title="订单发件地和运费模板发件地不一致">无法计算运费</td>
								</s:if>
								<s:elseif test="#order.remark == \"errorProvCity\"">
									<td class="td_f" style="padding-left:0px;padding-right:0px;text-align:center;color:red;cursor:help;" title="订单发件地和网点发件地不一致">无法计算运费</td>
								</s:elseif>
								<s:elseif test="#order.remark == \"errorPostinfo\"">
									<td class="td_f" style="padding-left:0px;padding-right:0px;text-align:center;color:red;cursor:help;" title="订单目的地不在运费模板中">无法计算运费</td>
								</s:elseif>
								<s:elseif test="#order.remark == \"thdd\"">
									<td class="td_f" style="padding-left:0px;padding-right:0px;text-align:center;color:red;cursor:help;" title="退货订单不计运费">退货订单不计运费</td>
								</s:elseif>
								<s:else>
									<td class="td_f" style="padding-left: 0px; padding-right: 0px;"><input readonly class="text_input_num numAlignRight" style="background-color:transparent;border:0 none;" value="<s:property value='#order.netFreight'/>" size="7"/>
										<s:if test="#order.freightType == 0">
											<img src="${imagesPath}/qsn.png" id="layerBtn<s:property value="#order.mailNo"/>" onclick="xslay('<s:property value="#order.mailNo"/>')" style="cursor:pointer;">
											<div id="tlayer<s:property value="#order.mailNo"/>" class="tlayer">
										    <ul>
											    <li class="qsn">价格调整原因</li>
											    <li class="btn_txt"><s:property value="#order.remark"/></li>
											    <li class="btn_txt"><s:property value="#order.buyName"/></li>
											    <li class="btn_moble"><s:property value="#order.buyMobile"/></li>
											    <li class="btn_cate">
											    	<s:if test="#order.acceptTime==null"><s:date name="#order.createTime" format="yyyy-MM-dd"/></s:if>
													<s:else><s:date name="#order.acceptTime" format="yyyy-MM-dd"/></s:else>
												</li>
											</ul>
    										<a href="javascript:void(0)" title="关闭" id="closebt<s:property value="#order.mailNo"/>" onclick="gblay('<s:property value="#order.mailNo"/>')" class="closebt">X</a>
											</div>
							 			</s:if>
							 		</td>
							</s:else>
								
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
		
<script type="text/javascript">
var params = {
		<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
			userType : 1,
		</c:if>
		<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
			userType : 2,
		</c:if>
		<c:if test="${yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
			userType : 3,
		</c:if>
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		showBindCode:true,
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
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/ec_account.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
	</div>
	<!-- E Content -->

