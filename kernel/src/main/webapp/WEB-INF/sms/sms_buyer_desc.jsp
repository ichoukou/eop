<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp" %>
	<meta charset="UTF-8" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/add_user.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	<!-- #include file="公共模块/header.html" -->
	
	<!-- S Main -->
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">会员详情</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
				<div class="box_bd">
					<form id="addUser_form" method="post" class="form" action="buyers!saveSmsBuyer.action">
						<p>
							<span class="half_w">
								<label for="name">卖家姓名：</label>
								<s:property value="buyers.receiverName"/>
							</span>
							<span class="half_w">
								<label for="wawa"><em>*</em>旺旺：</label>
								<s:property value="buyers.buyerAccount"/>
							</span>
						</p>
						<p>
							<span class="half_w">
								<label for="volume">交易量：</label>
								<s:property value="buyers.totalTradeCount"/>&nbsp;次
							</span>
							<span class="half_w">
								<label for="money">交易额：</label>
								<fmt:formatNumber value="${buyers.totalTradeAmount }" maxFractionDigits="2" minFractionDigits="2" ></fmt:formatNumber>&nbsp;元
							</span>
						</p>
						<p>
							<span class="half_w">
								<label>上次交易时间：</label>
								<fmt:formatDate value="${buyers.theLastTradeTime }" pattern="yyyy-MM-dd hh:mm:ss" />
							</span>
							<span class="half_w">
								<label for="phone"><em>*</em>手机号：</label>
								<s:property value="buyers.receiverMobile"/>
							</span>
						</p>
						<p>
							<span class="half_w">
								<label for="zip">邮政编码：</label>
								<s:property value="buyers.receiverPostcode"/>
							</span>
						</p>
						<p>
							<label for="address"><em>*</em>联系地址：</label>
							<select id="address"></select>
						</p>
						<p>
							<s:property value="buyers.receiverAddress"/>
						</p>
						<p>
							<label for="ps">备注：</label>
							<s:property value="buyers.remark"/>
						</p>
						<p class="opts">
							<a href="buyers!searchBuyersList.action?menuFlag=buyers_manage" class="btn btn_a return" title="返回">
								<span>返 回</span>
							</a>
						</p>
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
	<script type="text/javascript" src="${jsPath}/page/desc_user.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
