<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/add_user.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">添加新会员</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
				<div class="box_bd">
					<form id="addUser_form" class="form" action="">
						<p>
							<span class="half_w">
								<label for="name">卖家姓名：</label>
								<input type="text" id="name" class="input_text" />
								<span id="nameTip"></span>
							</span>
							<span class="half_w">
								<label for="wawa"><em>*</em>旺旺：</label>
								<input type="text" id="wawa" class="input_text" />
							</span>
						</p>
						<p>
							<span class="half_w">
								<label for="volume">交易量：</label>
								<input type="text" id="volume" class="input_text" />
								<span id="volumeTip"></span>
							</span>
							<span class="half_w">
								<label for="money">交易额：</label>
								<input type="text" id="money" class="input_text" />元
								<span id="moneyTip"></span>
							</span>
						</p>
						<p>
							<span class="half_w">
								<label>上次交易时间：</label>
								<input type="text" id="date" class="Wdate" />
								<span id="dateTip"></span>
							</span>
							<span class="half_w">
								<label for="qq">QQ：</label>
								<input type="text" id="qq" class="input_text" />
								<span id="qqTip"></span>
							</span>
						</p>
						<p>
							<span class="half_w">
								<label for="phone"><em>*</em>手机号：</label>
								<input type="text" id="phone" class="input_text" />
								<span id="phoneTip"></span>
							</span>
							<span class="half_w">
								<label for="zip">邮政编码：</label>
								<input type="text" id="zip" class="input_text" />
								<span id="zipTip"></span>
							</span>
						</p>
						<p>
							<label for="address">联系地址：</label>
							<select id="address"></select>
							<span id="addressTip"></span>
						</p>
						<p>
							<input type="text" id="d_address" class="input_text" />
							<span id="d_addressTip"></span>
						</p>
						<p>
							<label for="ps">备注：</label>
							<textarea id="ps"></textarea>
							<span id="psTip"></span>
						</p>
						<p class="opts">
							<a href="javascript:;" class="btn btn_a save" title="保存">
								<span>保 存</span>
							</a>
							<a href="javascript:;" class="btn btn_a return" title="返回">
								<span>返 回</span>
							</a>
						</p>
						
					</form>
				</div>
				</div>
			</div>
			
	<script type="text/javascript">
		var params = {
			checkWawaUrl: ''		// 旺旺唯一性验证地址
		};
	</script>
	<!-- <script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode='			// 绑定客户编码表单 action
	}
	</script> -->
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/add_user.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
	