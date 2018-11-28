<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/question_n.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},							//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode='			// 绑定客户编码表单 action
	};
</script>
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/monitor_n.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
<!-- S Content -->
		<div id="content">
			<style type="text/css">
				#content{float:none;padding:0;width:960px;}
				#question_branch_guide{position:relative;}
				#question_branch_guide img{display:block;}
				#start_cus{width:130px;height:46px;display:block;position:absolute;text-indent:-9999em;overflow:hidden;left:419px;top:492px;background:url(about:blank);}
			</style>
			<form action="noint!iKnow.action" id="site_start_form">
				 <input type="hidden" name="menuFlag" value="${menuFlag }" />
				 <input name="nextAction" value="questionnaire_index" type="hidden"/>
				 <input name="methodName" value="setField003" type="hidden"/>
			</form>
			<div id="question_branch_guide">
				<img src="images/single/question_branch_guide.jpg" alt="" />
				<a href="javascript:;" id="start_cus">开始使用</a>
			</div>
		</div>
		<!-- E Content -->
	<script type="text/javascript">
		$(function() {
			// 开始使用
			$('#start_cus').click(function(ev) {
				ev.preventDefault();
				$("#site_start_form").submit();
			})
		})
	</script>


<!-- <div id="content_ad"> -->
<!-- 	<div class="step_content"> -->
<!-- 		<form action="noint!iKnow.action" id="site_start_form"> -->
<%-- 			 <input type="hidden" name="menuFlag" value="${menuFlag }" /> --%>
<!-- 			 <input name="nextAction" value="questionnaire_index" type="hidden"/> -->
<!-- 			 <input name="methodName" value="setField003" type="hidden"/> -->
<!-- 		</form> -->
<!-- 		<img src="images/single/step02.gif" width="858" height="650"> -->
<!-- 		<input type="button" value="开始使用" id="site_start"/> -->
<!-- 	</div> -->
<!-- </div> -->
<%-- 	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script> --%>
<%-- 	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script> --%>
<%-- 	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script> --%>
<%-- 	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script> --%>
<%-- 	<script type="text/javascript" src="${jsPath}/page/monitor_n.js?d=${str:getVersion() }"></script> --%>
