<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/fsmessage.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>发送消息</title>
	<!-- S Content -->
	<div id="content">
		<div id="content_hd" class="clearfix">
			<h2 id="message_icon">发新消息</h2>
			<em>网点和卖家管理人员专属的沟通渠道<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
			
		</div>
		<div id="content_bd" class="clearfix">
			<!-- S 网点发消息 -->
			<div class="box box_a" id="branch_send">
				<div class="box_bd">
					<form action="send_send.action" id="branch_send_form" class="form" method="post">
						<c:if test="${yto:getCookie('userType') == 1
	        						|| yto:getCookie('userType') == 11
	        						|| yto:getCookie('userType') == 13
	        						|| yto:getCookie('userType') == 4
	        						|| yto:getCookie('userType') == 41}">
	        				<s:if test="siteName != ''">	
								<p>
									<label for="topic">发送给：</label>
									<s:property value="siteName"/>
								</p>
							</s:if>	
						</c:if>
						<p>
							<label for="topic"><span class="req">*</span>主题：</label>
							<input type="text" id="topic" class="input_text" name="messageTheme"/>
							<span id="topicTip"></span>
						</p>
						<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23 || yto:getCookie('userType') == 3}">
							<p>
								<label for="user_sel"><span class="req">*</span>用户：</label>
								<input type="text" id="user_sel" class="input_text" readonly <s:if test="receiveUserNameString!=null && receiveUserNameString!=''">value="${receiveUserNameString }"</s:if><s:else>value="选择客户"</s:else>/>
								<span id="user_selTip"></span>
								<input type="hidden" id="user_codes" value="${receiveUserCodeString }"/>
								<input type="hidden" id="receiveIdString" name="receiveIdString"/>
								<input type="hidden" id="receiveUserCodeString" name="receiveUserCodeString" value="${receiveUserCodeString }"/>
							</p>
						</c:if>
						<p>
							<label for="message"><span class="req">*</span>内容：</label>
							<textarea name="messageContent" id="message" class="textarea_text"></textarea>
							<span id="messageTip"></span>
						</p>
						<p>
							<a title="发送" class="btn btn_a" id="send_msg" href="javascript:;"><span>发 送</span></a>
						</p>
					</form>
					
					<!-- S 模拟下拉框 -->
					<div id="simulate_sel">
						<form action="" id="sear_user">
							<input type="checkbox" id="sel_all" disabled />
							<label for="sel_all" class="reset_label">全选</label>
							
							<input type="text" id="user_id" class="input_text" />
							<a title="查找" class="btn btn_a" id="sear_user_btn" href="javascript:;"><span>查 找</span></a>
							<a title="确定" class="btn btn_a" id="ok_btn" href="javascript:;"><span>确 定</span></a>
							
						</form>
						<div id="sel_box">
							<p>暂无客户...</p>
						</div>
					</div>
					<!-- E 模拟下拉框 -->
				</div>
			</div>
			<!-- E 网点发消息 -->
		</div>
		
	<script type="text/javascript">
		var params = {
			searUserAction: 'send_receiver.action?receiverCondition=',			// 查找用户
			<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
				userType : 1,
			</c:if>
			<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
				userType : 2,
			</c:if>
			<c:if test="${yto:getCookie('userType') == 3}">
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
    <script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/fsmessage.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
		
	</div>
	<!-- E Content -->

