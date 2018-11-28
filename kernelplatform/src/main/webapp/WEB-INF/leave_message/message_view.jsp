<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/message.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	<title>查看消息</title>
	<!-- S Content -->
	<div id="content">
		<div id="content_hd" class="clearfix">
			<em>便捷、安全、可靠的消息功能，助您解决各种业务问题！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
		</div>
		<div id="content_bd" class="clearfix">
		<!-- S 左栏 -->
		<div class="content_l">
			<div class="col_hd clearfix">
				<h3>我的消息</h3>
			</div>
			<div class="col_bd">
				<!-- S Table -->
				<div id="msg_table" class="table">
					<table>
						<thead>
							<tr>
								<th style="width:9%"><input class="input_checkbox" type="checkbox" id="check_all" /></th>
								<th style="width:91%" colspan="3">
									<span class="btn btn_f" style="margin-top:3px;height:27px;"><button type="button" id="send_message">写消息</button></span>
									<span class="btn btn_f" style="margin-top:3px;height:27px;"><button type="button" id="delete_message">删 除</button></span>
									<select name="" id="msg_type">
										<c:forEach items="${typeList }" var="entity">
											<option value="${entity.value }" <c:if test="${classify==entity.value }">selected</c:if>>${entity.name }</option>
										</c:forEach>
									</select>
									<select name="messageStatus" id="msg_mark">
										<option value="">标记为</option>
										<option value=1>已读</option>
										<option value=0>未读</option>
									</select>
								</th>
							</tr>
						</thead>
						<tbody>
							<input type="hidden" id="currentPage" value="${currentPage }"/>
							<input type="hidden" id="classify" value="${classify }"/>
							<s:iterator value="messageList">
								<tr value="<s:property value='id'/>">
									<td style="width:9%"><input type="checkbox" value="<s:property value='id'/>"/></td>
									<td style="width:61%">
										<div <s:if test='messageType==2'>class="cmts_title"</s:if><s:else>class="cmts_title vip"</s:else>>
											<strong><s:property value="sendUserName"/></strong>
										</div>
										<div <s:if test="messageStatus==1">class="cmts_preview"</s:if><s:else>class="unread"</s:else> id="curMessageTheme">
											<p><s:property value="messageTheme"/></p>
										</div>
										<div style="display:none" class="cmts_preview_content"><p><s:property value="messageContent"/></p></div>
									</td>
									<td style="width:13%"><span class="cmts_num"><s:property value="replyNum"/></span></td>
									<td style="width:17%">
										<span class="cmts_date"><s:date name="sendTime" format="yyyy-MM-dd"/></span>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
					<!-- S PageNavi -->
					<div class="pagenavi">
						<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
					</div>
					<!-- E PageNavi -->
				</div>
				<!-- E Table -->
			</div>
		</div>
		<!-- E 左栏 -->
		<!-- S 右栏 请勿修改以下dom的id、class-->
		<div class="content_r">
			<div class="col_hd clearfix">
				<h3 id="replyTheme"></h3>
				<span class="cmts_num" id="replyNumForTheme"></span>
			</div>
			<div class="col_bd">
				<div class="cmts_detail">
					<div class="cmts_list">
						<!-- 此处动态加载 -->
						<ul id="curReplyList"></ul>
					</div>
					<div class="cmts_reply">
						<h4>回复</h4>
						<form action="send_reply.action" method="post" id="replyForm">
							<input type="hidden" name="messageId" id="messageId"/>
							<textarea name="replyContent" id="replyContent">请填写回复内容</textarea>
						</form>
					</div>
				</div>
			</div>
			<div class="col_ft">
				<span class="btn btn_a"><button type="button" id="replyButton">发 送</button></span>
			</div>
		</div>
		<!-- E 右栏 -->
			
		</div>
		
	<script type="text/javascript">
		var params = {
			<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
				userType : 2,
			</c:if>
			<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
				userType : 1,
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
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/message.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
		
	</div>
	<!-- E Content -->

