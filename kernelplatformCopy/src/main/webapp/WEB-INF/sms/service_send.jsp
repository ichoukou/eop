<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link href="${cssPath}/page/mark_home.css?d=${str:getVersion() }" rel="stylesheet" type="text/css">
	<!-- E 当前页面 CSS -->
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">短信营销</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<div id="modify_process">
							<ol>
								<li id="">1.创建营销活动</li>
								<li id="process_second">2.选择参加活动成员</li>
								<li id="process_cur">3.发送促销信息</li>
							</ol>
						</div>
						
						<form action="" id="mark_form3">
							<p>亲，你此次创建的活动共选择了${customerNumber}位客户，可发短信数剩${smsUsecount}，发送短信大约需要5分钟，是否要现在发送短信？</p>
							<input type="hidden" name="menuFlag" value="sms_made"/>
							<input type="hidden" name="searchName" id="searName" value="${searchName}" />
							<input type="hidden" name="theLastTradeTime" id="tradeTime" value="${theLastTradeTime}" />
							<input type="hidden" name="theLastMarketTime" id="lastTrade" value="${theLastMarketTime}" />
							<input type="hidden" name="receiverProvince" id="atprovince" value="${receiverProvince}" />
							<input type="hidden" name="receiverCity" id="atcity" value="${receiverCity}" />
							<input type="hidden" name="receiverDistrict" id="atarea" value="${receiverDistrict}" />
							<input type="hidden" name="provinceName" id="provinceName_" value="${provinceName}" />
							<input type="hidden" name="cityName" id="cityName_" value="${cityName}" />
							<input type="hidden" name="areaName" id="areaName_" value="${areaName}" />
							<input type="hidden" name="userGrade" id="memLevel" value="${userGrade}" />
							<input type="hidden" name="searchInput" id="member" value="${searchInput}" />
							<input type="hidden" name="tradeCountMin" id="tradeAmountA" value="${tradeCountMin}" />
							<input type="hidden" name="tradeCountMax" id="tradeAmountB" value="${tradeCountMax}" />
							<input type="hidden" name="tradeAmountMin" id="tradeCountA" value="${tradeAmountMin}" />
							<input type="hidden" name="tradeAmountMax" id="tradeCountB" value="${tradeAmountMax}" />
							<input type="hidden" name="templateId" id="templateId" value="${templateId}" />
							<input type="hidden" name="customerId" id="customerId" value="${customerId}" />
							<input type="hidden" name="pageChecked" id="pageChecked_" value="${pageChecked}" />
							<input type="hidden" name="pageCount" id="pageCount" value="${pageCount}" />
							<input type="hidden" name="customerNumber" id="customerNumber" value="${customerNumber}" />
							<input type="hidden" name="currentPage" id="currentPage" value="<s:property value='currentPage'/>"/>

						<div id="submit_box">
								<a href="javascript:;" id="send_btn" class="btn btn_a" title="立即发送"><span id="sendText_">立即发送</span></a>
								<a href="javascript:;" id="back_btn" class="btn btn_a" title="上一步"><span>上一步</span></a>
							</div>
						</form>
					</div>
				</div>
			</div>
			
	<script type="text/javascript">
		var params = {
			backAction: 'smsServiceMarket!backToMember.action',					// 返回上一步
			sendAction: 'smsServiceMarket!sendSms.action'					// 立即发送
		}
	</script>

    <script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
    <script type="text/javascript" src="${jsPath}/page/mark_home3.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
	
