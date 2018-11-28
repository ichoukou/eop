<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }"
	media="all" />
<link rel="stylesheet" type="text/css"
	href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css"
	href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css"
	href="${cssPath}/page/problem_war.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>问题件预警</title>

<!-- S Content -->
<div id="content">

	<form action="" method="">
		短信信息：
		<textarea class="content" name="content"></textarea>
		<br>
		<br> 运单号:<input class="mailNo" type="text" name="mailNo"><br>
		<br> 买家姓名：<input class="buyName" type="text" name="buyName"><br>
		<br> 买家手机号码:<input class="buyMobile" type="text" name="buyMobile"><br>
		<br> <input type="button" id="sendMsg" value="发送">
	</form>
	<br> 发送成功与否：
	<s:property value="#result.isvalid" />
</div>
<!-- E Content -->
<script>
	$(function() {

		$("#sendMsg")
				.click(
						function() {

							var content = $('.content').val();
							var mailNo = $('.mailNo').val();
							var buyName = $('.buyName').val();
							var buyMobile = $('.buyMobile').val();
							alert("content=" + content + "-mailNo=" + mailNo
									+ "-buyName=" + buyName + "-buyMobile="
									+ buyMobile);
							$.ajax({
								url : 'issueInform_sendMsg.action',
								type : 'POST',
								data : {
									content : content,
									mailNo : mailNo,
									buyName : buyName,
									buyMobile : buyMobile
								},
								success : function(data) {
									var succDialog = new Dialog();
									succDialog.init({
										contentHtml : data,
										closeBtn : true
									})
								},
								error : function(data) {
									var errorDialog = new Dialog();
									errorDialog.init({
										contentHtml : '抱歉！系統繁忙，请稍后再试。',
										closeBtn : true
									})
								}
							})
						});

	});
</script>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/problem_war.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
