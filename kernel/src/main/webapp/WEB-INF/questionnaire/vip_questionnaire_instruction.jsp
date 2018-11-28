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
		userId:${yto:getCookie('id')},						//当前登录用户的id
		showBindCode:true,
		userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
<!-- S Content -->
	<div id="content">
		<style type="text/css">
			
			#content{float:none;padding:0;width:960px;}
			#question_seller_guide1{position:relative;}
			#question_seller_guide1 img{display:block;}
			#go_on_guide{width:145px;height:60px;display:block;text-indent:-9999em;overflow:hidden;position:absolute;background:url(about:blank);left:722px;top:555px;}
		</style>
		<div id="question_seller_guide1">
			<img src="images/single/question_seller_guide1.jpg" alt="" />
			<a href="javascript:;" id="go_on_guide">继续看</a>
		</div>
	</div>
<!-- E Content -->
	<script type="text/javascript">
		$(function() {
			// 继续看
			$('#go_on_guide').click(function(ev) {
				ev.preventDefault();
				window.location.href = "questionnaire_index.action?menuFlag=chajian_question&step=2";
			})
		})
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/monitor_n.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
