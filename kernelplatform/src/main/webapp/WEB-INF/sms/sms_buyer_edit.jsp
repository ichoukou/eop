<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp" %>
	<meta charset="UTF-8" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/add_user.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

	
	<!-- S Main -->
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
			<c:choose>
				<c:when test="${not empty buyers.id}"><h2 id="message_icon">修改会员信息</h2></c:when>
				<c:otherwise><h2 id="message_icon">添加新会员</h2></c:otherwise>
			</c:choose>
			
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
				<div class="box_bd">
					<form id="addUser_form" method="post" class="form" action="buyers!saveSmsBuyer.action?menuFlag=buyers_manage">
						<p>
							<span class="half_w">
								<label for="name">卖家姓名：</label>
								<s:textfield id="name" name="buyers.receiverName" cssClass="input_text"></s:textfield>
								<span id="nameTip"></span>
							</span>
							<span class="half_w">
								<label for="wawa"><em>*</em>旺旺：</label>
								<s:textfield id="wawa" name="buyers.buyerAccount" cssClass="input_text"></s:textfield>
								<span id="wawaTip"></span>
							</span>
						</p>
						<p>
							<span class="half_w">
								<label for="volume">交易量：</label>
								<s:textfield id="volume" name="buyers.totalTradeCount" cssClass="input_text" ></s:textfield>&nbsp;次
								<span id="volumeTip"></span>
							</span>
							<span class="half_w">
								<label for="money">交易额：</label>
								<input type="text"  id="money" name="buyers.totalTradeAmount" 
								value="<fmt:formatNumber value="${buyers.totalTradeAmount }" maxFractionDigits="2" minFractionDigits="2" ></fmt:formatNumber>" class="input_text" />&nbsp;元 
								<span id="moneyTip"></span>
							</span>
						</p>
						<p>
							<span class="half_w">
								<label>上次交易时间：</label>
								<input type="text" id="date"  name="buyers.theLastTradeTime" 
								value="<fmt:formatDate value="${buyers.theLastTradeTime }" pattern="yyyy-MM-dd" />" class="Wdate" />
								<span id="dateTip"></span>
							</span>
							<span class="half_w">
								<label for="phone"><em>*</em>手机号：</label>
								<s:textfield id="phone" name="buyers.receiverMobile" cssClass="input_text" ></s:textfield>
								<span id="phoneTip"></span>
							</span>
						</p>
						<p>
							
							<span class="half_w">
								<label for="zip">邮政编码：</label>
								<s:textfield id="zip" name="buyers.receiverPostcode" cssClass="input_text"></s:textfield>
								<span id="zipTip"></span>
							</span>
						</p>
						<p>
							<label for="address"><em>*</em>联系地址：</label>
							<select id="address"></select>
							<span id="addressTip"></span>
						</p>
						<p>
							<s:textfield id="d_address" name="buyers.receiverAddress" cssClass="input_text"></s:textfield>
							<span id="d_addressTip"></span>
						</p>
						<p>
							<label for="ps">备注：</label>
							<s:textarea id="ps" name="buyers.remark"></s:textarea>
							<span id="psTip"></span>
						</p>
						<p class="opts">
						<a href="javascript:;" class="btn btn_a save" title="保存"> <span>保 存</span> 
							</a>
							<a href="javascript:history.go(-1);" class="btn btn_a return" title="返回">
								<span>返 回</span>
							</a>
						</p>
						<!-- 会员Id -->
						<input type="hidden" id="buyersId" name="buyers.id" value="${buyers.id }"/>
						<!-- 省 -->
						<input type="hidden" id="receiverProvince" name="buyers.receiverProvince" value="${buyers.receiverProvince}"/>
						<!-- 市 -->
						<input type="hidden" id="receiverCity" name="buyers.receiverCity" value="${buyers.receiverCity }"/>
						<!-- 区 -->
						<input type="hidden" id="receiverDistrict" name="buyers.receiverDistrict" value="${buyers.receiverDistrict }"/>
					</form>
				</div>
				</div>
			</div>
		<!-- E Content -->
		
		<!-- #include file="公共模块/sidebar.html" -->
	</div>
	<!-- E Main -->
	
	<!-- #include file="公共模块/footer.html" -->

	<script type="text/javascript">
		var params = {
			checkWawaUrl: 'buyers!checkWangWang.action'		// 旺旺唯一性验证地址
		};
	</script>
	
	<!--[if IE 6]>
		<script type="text/javascript" src="js/util/position_fixed.js?d=${str:getVersion() }"></script>
		<script type="text/javascript" src="js/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('.png');
		</script>
	<![endif]-->
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/add_user.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
