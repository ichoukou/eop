<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

	<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/special_bill.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

	
		<!-- S Content -->
		<div id="content">
		<input type="hidden" name="menuFlag" value="${menuFlag }" />
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">特殊账单</h2> -->
				<em>查看调整后的价格，来这里！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a>
<!-- 				<a title="快速入门" class="btn btn_d"  href="noint1_eachHelp.action?article.remark=help_home&menuFlag=help_home" target="_blank" > -->
<!-- 	                <span>快速入门</span> -->
<!-- 	            </a> -->
				</em>
				<p>超区件转EMS→网点调整运费→网点把调整过的运单发送给电商→电商客户在这里查看</p>
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<form action="mjfreight!mjunlikefreightlist.action" id="sear_form" method="post">
							<input name="currentPage" id="currentPage" type="hidden" value="${currentPage }"/>
							<input name="flag" id="flag" type="hidden" value="2"/>
							<input type="hidden" name="menuFlag" id="menuFlag" value="${menuFlag }">
							<p>
								<span>发货时间：</span>
								<input id="d4311" name="startTime" class="Wdate" type="text" value="${startTime }" readOnly=""/>
								至
								<input id="d4312" name="endTime" class="Wdate" type="text" value="${endTime }" readOnly=""/>
								<c:if test="${yto:getCookie('userType') == 4 }">
	                                <span>分仓账号：</span>
	                                <select id="vipName" name="vipName" class="easyui-combobox" style="width:200px;">
	                                    <option <s:if test="%{vipName == 0}">selected</s:if> value=0>所有账号</option>
	                                    <s:iterator value="vipList">
	                                        <option <s:if test="%{vipName == id}">selected</s:if> value="<s:property value='id'/>"><s:property value="userNameText"/>(<s:property value="userName"/>)</option>
	                                    </s:iterator>
	                                </select>
                            	</c:if>
							</p>
							<p>
								<textarea name="logisticsIds" rows="5"  class="textarea_text" id="textarea_search" onChange="" ><c:if test="${logisticsIds != null && logisticsIds != '' }">${logisticsIds }</c:if></textarea>
								<!-- <a id="yingcang1" href="#" onClick=""><img style="padding-left:4px" src="images/closebtn.png" /></a> -->
							</p>
							<p>
								<a href="javascript:;" id="sear_btn" title="查 询" class="btn btn_a"><span>查 询</span></a>支持多单号查询,输入数字与字母组合运单号,用斜杠"/"分隔
							</p>
						</form>
					</div>
				</div>
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a"> <div class="th_title"><em>运单号</em></div></th>
								<th class="th_b"> <div class="th_title"><em>目的地</em></div></th>
								<th class="th_c"> <div class="th_title"><em>价格调整原因</em></div></th>
								<th class="th_d"> <div class="th_title"><em>重量</em></div></th>
								<th class="th_e"> <div class="th_title"><em>发货日期</em></div></th>
								<th class="th_f"> <div class="th_title"><em>调整后价格</em></div></th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${flag == 2 }">
							<c:if test="${fn:length(unlikeFreightOrderList) < 1 }">
								<tr id="addufcj" name="addufcj" >
                                	<td colspan="7" align="center"><b>查不到您要的调整运单,请确认时间和输入的运单号是否有误</b></td>
                                </tr>
							</c:if>
							<c:forEach items="${unlikeFreightOrderList }" var="unlikeFreight" varStatus="orderIndex">
								<tr>
									<td class="td_a">
										<a href="javascript:;" title="查看运单详情" class="mailno" val="${unlikeFreight.mailNo }">${unlikeFreight.mailNo }</a>
									</td>
									<td class="td_b">${unlikeFreight.address }</td>
									<td class="td_c">${unlikeFreight.reason }</br><fmt:formatDate value="${unlikeFreight.updateTime }" type="both" pattern="yyyy-MM-dd"/></br>${unlikeFreight.remark }</td>
									<td class="td_d">
										<c:choose>
											<c:when test="${unlikeFreight.weight <= 0 }">
												<font title='重量小于等于1'>≤1</font>
											</c:when>
											<c:otherwise>
												${unlikeFreight.weight }kg
											</c:otherwise>
										</c:choose>
									</td>
									<td class="td_e">
										<fmt:formatDate value="${unlikeFreight.goodsTime }" type="both" pattern="yyyy-MM-dd" />
									</td>
									<td class="td_f"><div class="clearfix"><span>${unlikeFreight.price }元</span><i class="png"></i>
										<div class="ec_acc_tip">
	                <ul>
		                <li class="qsn"></li>
		                <li class="house"></li>
		                <li class="people"></li>
		                <li class="moble"></li>
		                <li class="cate"></li>
	                </ul>
                </div>
									</div>
										<ul class="adjust">
											 <li><input type="hidden" class="ec_acc_tip_1" value="运单调整记录" /></li>
		                                     <li><input type="hidden" class="ec_acc_tip_2" value="${unlikeFreight.address }" /></li>
		                                     <li><input type="hidden" class="ec_acc_tip_3" value="${unlikeFreight.userName }" /></li>
		                                     <li><input type="hidden" class="ec_acc_tip_4" value="${unlikeFreight.mobile }" /></li>
		                                     <li><input type="hidden" class="ec_acc_tip_5" value="<fmt:formatDate value='${unlikeFreight.updateTime }' type='both' pattern='yyyy-MM-dd HH:mm:ss'/>" /></li>
										</ul>
									</td>
								</tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>
				</div>
				<!-- S PageNavi -->
				<div class="pagenavi">
					<!-- <a href="#" title="首页" class="page_txt">« 首页</a>
					<span class="page_cur">1</span>
					<a href="#" title="2" class="page_num">2</a>
					<a href="#" title="3" class="page_num">3</a>
					<a href="#" title="4" class="page_num">4</a>
					<a href="#" title="5" class="page_num">5</a>
					<a href="#" title="下一页" class="page_txt">下一页</a>
					<a href="#" title="末页" class="page_txt">末页 »</a>
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
		showBindCode:true,
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
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/special_bill.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->

