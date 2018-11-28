<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp" %>
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/set_member.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">会员等级设置</h2>
				<a href="javascript:window.history.go(-1)" title="返回" class="btn btn_d" id="hd_goback"><span>返 回</span></a>
			</div>
			<div id="content_bd" class="clearfix">
				<div class="title">参数设置</div>
				<form id="set_fm" method="post" action="buyers!setUserGrade.action">
					<div class="set">
						<p class="common">
							<label>普通会员：</label>所有购买记录的买家
						</p>
						<p class="senior">
							<label>高级会员：</label>交易额大于
							
							<span class="border"><input class="input_text" name="grade.highAccount"  value="<fmt:formatNumber value="${grade.highAccount }" maxFractionDigits="2" minFractionDigits="2" ></fmt:formatNumber>" /></span>元 或 交易量大于
							<span class="border"><input class="input_text" name="grade.highCount" value="${grade.highCount}" /></span>次
						</p>
						<p class="vip">
							<label>VIP会员：</label>交易额大于
							<span class="border"><input class="input_text" name="grade.vipAccount" value="<fmt:formatNumber value="${grade.vipAccount }" maxFractionDigits="2" minFractionDigits="2" ></fmt:formatNumber>" /></span>元 或 交易量大于
							<span class="border"><input class="input_text" name="grade.vipCount" value="${grade.vipCount }" /></span>次
							<span class="errVIP"></span>
						</p>
						<p class="super">
							<label>至尊VIP会员：</label>交易额大于
							<span class="border"><input class="input_text" name="grade.vipHighAccount" value="<fmt:formatNumber value="${grade.vipHighAccount }" maxFractionDigits="2" minFractionDigits="2" ></fmt:formatNumber>" /></span>元 或 交易量大于
							<span class="border"><input class="input_text" name="grade.vipHighCount" value="${grade.vipHighCount }" /></span>次
							<span class="errSuper"></span>
						</p>
					</div>
					<div class="opts">
						<a href="javascript:;" class="btn btn_a reset" title="重置"><span>重置</span></a>
						<a href="javascript:;" class="btn btn_a save" title="保存"><span>保存</span></a>
					</div>
					<div class="disc">
						<dl>
							<dt>说明：</dt>
							<dd>1. 如果设置的数字为空或为0，则视为没有设置。</dd>
							<dd>2. 设置条件后，系统自动按照条件给会员计算等级。</dd>
							<dd>3. 如果您已经设置了交易额，则成交量可以不设，反之亦然。</dd>
							<dd>4. 如果您同时设置了交易额和成交次数的条件，则系统会判断会员按哪个条件计算等级更高，而后取用更高的等级。</dd>
						</dl>
					</div>
					<input name="grade.id" type="hidden" value="${grade.id }">
				</form>
			</div>
		<!-- E Content -->
		


	<script type="text/javascript" src="js/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="js/page/set_member.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
		
		
	</div>
	<!-- E Main -->
	
