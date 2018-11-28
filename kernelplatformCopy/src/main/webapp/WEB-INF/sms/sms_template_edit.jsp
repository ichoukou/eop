<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css"
	href="${cssPath}/page/sms_template_edit.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>修改短信模板</title>

<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">修改短信模板</h2>
		<em>便捷、安全、可靠的消息功能，助您解决各种业务问题！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em> 
		<!-- <a href="javascript:window.history.go(-1)" title="返回"
			class="btn btn_d" id="hd_goback"><span>返 回</span>
		</a> -->
	</div>
	<div id="content_bd" class="clearfix">

		<!-- S Box -->
		<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<form action="" id="new_sms_form" method="post">
							<div class="line_box">
								<p>
									<label>短信类型:</label> <select name="" id="sms">
										<option selected>${servicedto.name }</option>
									</select> 
									<input id="tabNum" type="hidden" value="<s:property value="tabNum"/>" />
									<input id="id" type="hidden" value="${template.id }" /> 
									<input id="serviceId" type="hidden" value="${servicedto.id }">
								</p>
								
								<!--em>发送短信的内容尽量不要使用疑问句，因为会员是无法回复您的短信的；</em-->
							</div>
							
							<div class="line_box">
								<label for="sms_title">模板标题：</label>
								<input type="text" class="input_text" id="sms_title" value="${template.name }"/>
								<span id="sms_titleTip"></span>
							</div>
							
							<div class="line_box tmp">
								<label for="sms_content" class="block_label">模板内容：</label>
								<!--textarea id="sms_content" data-maxsize="${max }" class="textarea_text" data-output="status1" wrap="virtual">${template.content }</textarea-->
								<div id="sms_content" contenteditable="true"></div>
								<span id="sms_contentTip"></span>
								
								<div id="label_key">
									<span>可插入标签：</span>
									<ul>
										<li><a href="javascript:;" class="shop_name">店铺名称</a></li>
										<li><a href="javascript:;" class="order_time">订单时间</a></li>
										<li><a href="javascript:;" class="send_time">发货时间</a></li>
										<li><a href="javascript:;" class="ticket_id">运单号</a></li>
										<li><a href="javascript:;" class="company_name">快递公司</a></li>
									</ul>
									<input id="shop" type="hidden" value="${shopName}" />
									<!--input id="order" type="hidden" value="订单时间订单时间订单时间订单时间订单时" /-->
									<!--input id="send" type="hidden" value="发货时间发货时间发货时间发货时间发货时" /-->
									<!--input id="ticket" type="hidden" value="运单号运单号运单号运单" /-->
									<!--input id="company" type="hidden" value="快递公司" /-->
									<input id="content_hid" type="hidden" value="${template.content}" />
								</div>
							</div>
						</form>
						<p>已输入<span id="font_count">0</span>字，70个字符算一条短信，超出70个字符，每67个字符算一条短信。</p>
						<p class="red">注意</p>
						<div id="manager_ft">
							<p>1、每种短信类型最多可添加5条模板</p>
							<p>2、模板内容请不要使用疑问句，因为收信人无法回复您的短信</p>
							<p>3、添加或修改模板需要重新审核，审核需要1-2个工作日，节假日顺延</p>
						</div>
						<p class="blue">标签长度</p>
						<p>店铺名称：我的账号店铺名称的实际长度 &nbsp;&nbsp;订单时间/发送时间：08月06日12:45(<span class="red">11字</span>)</p>
						<p>运单号：1111111111(<span class="red">10字</span>)&nbsp;&nbsp;快递公司：圆通速递(<span class="red">4字</span>)</p>
						<div class="submit_btn">
							<a href="javascript:;" class="btn btn_a m_s_msg" title="提交审核"><span>提交审核</span></a>
							<a href="javascript:;" class="btn btn_a m_s_msg1" title="返回"><span>返回</span></a>
						</div>
					</div>
				</div>
			</div>
		<!-- E Box -->

	</div>


	<script>
	var params = {
		dialogSubmit : '?dialogSubmit',
 		tabIndex : document.getElementById('tabNum').value,
		
		onStep:3,	// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		showBindCode:true,
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	}
</script>

	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- 新增时时统计短信字数 -->
	<script type="text/javascript" src="${jsPath}/common/maxlength.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript"
		src="${jsPath}/page/sms_template_edit.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->

</div>

<!-- E Content -->


