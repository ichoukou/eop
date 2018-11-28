<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/fsmessage.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>建议意见</title>
	<!-- S Content -->
	<div id="content">
		<div id="content_hd" class="clearfix">
			<h2 id="message_icon">发送建议</h2>
			<em>您的宝贵意见将更加促进平台的服务提升速度，感谢您的反馈</em>
			
		</div>
		<div id="content_bd" class="clearfix">
			<div class="box box_a" id="branch_send">
				<div class="box_bd">
					<form action="" id="branch_send_form" class="form" method="post">
						<p>
							<label for="topic"><span class="req">*</span>主题：</label>
							<input type="text" id="topic" class="input_text" name="messageTheme"/>
							<span id="topicTip"></span>
						</p>
						<s:fielderror cssStyle="color:red;">
							<s:param>messageTheme</s:param>							
						</s:fielderror>
						<p>
							<label for="message"><span class="req">*</span>内容：</label>
							<textarea name="messageContent" id="message" class="textarea_text"></textarea>
							<span id="messageTip"></span>
						</p>
						<s:fielderror cssStyle="color:red;">
							<s:param>messageContent</s:param>							
						</s:fielderror>
						<p>
							<a title="发送" class="btn btn_a" id="send_msg" href="javascript:;"><span>发 送</span></a>
						</p>
					</form>
				</div>
			</div>
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
	<script type="text/javascript" src="${jsPath}/page/suggest.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
		
	</div>
	<!-- E Content -->

