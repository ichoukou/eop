<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
  
	<!-- S 当前页面 CSS -->
	<link href="css/page/mark_home.css" rel="stylesheet" type="text/css">
	<!-- E 当前页面 CSS -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">短信营销</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S Box -->
				<div class="box box_a" id="step_b_form">	
					<div class="box_bd">
						<div id="modify_process">
							<ol>
								<li id="process_first">1.创建营销活动</li>
								<li id="process_cur">2.选择参加活动成员</li>
								<li id="process_last">3.发送促销信息</li>
							</ol>
						</div>
						<!-- S form -->
						<form action="smsServiceMarket!smsMarketPageStep2.action" class="form" id="mark_form2">
						<input type="hidden" id="smsMade" name="menuFlag" value="sms_made"/>
						<input type="hidden" name="currentPage" id="currentPage" value="<s:property value='currentPage'/>"/>
						<input type="hidden" name="templateId" id="template_id" value="<s:property value='templateId'/>"/>
						<input type="hidden" name="searchName_" id="searchName_id" value="<s:property value='searchName'/>"/>
						<input type="hidden" name="pageCount" id="pageCount_" value="<s:property value='pageCount'/>"/>
						<input type="hidden" name="allCount" id="allCount_" value="<s:property value='allCount'/>"/>
						<input type="hidden" name="pageChecked" id="pageChecked_" value="<s:property value='pageChecked'/>"/>
						<input type="hidden" name="customerId" id="customerId_" value="<s:property value='customerId'/>"/>
						<input type="hidden" name="customerNumber" id="customerNumber_" value="<s:property value='customerNumber'/>"/>
						<!-- 搜索器ID -->
						<input type="hidden" name="smsBuyersSearchId" id="smsBuyersSearchId"/>
							<p>
								<label for="sear_name">搜索器名称：</label>
								<select name="searchName" id="sear_name">
								
								</select>
							</p>
							<p>
								<span class="half_w">
									<label for="trade_time">上次交易时间：</label>
									<select name="theLastTradeTime" id="trade_time">
										<option <c:if test="${theLastTradeTime==0}">selected</c:if> value="0">不限</option>
										<option <c:if test="${theLastTradeTime==1}">selected</c:if> value="1">最近一周</option>
										<option <c:if test="${theLastTradeTime==2}">selected</c:if> value="2">最近两周</option>
										<option <c:if test="${theLastTradeTime==3}">selected</c:if> value="3">最近一月</option>
									</select>
								</span>
								<span class="half_w">
									<label for="last_trade">上次活动时间：</label>
									<select name="theLastMarketTime" id="last_trade">
										<option <c:if test="${theLastMarketTime==0}">selected</c:if> value="0">不限</option>
										<option <c:if test="${theLastMarketTime==1}">selected</c:if> value="1">最近一周</option>
										<option <c:if test="${theLastMarketTime==2}">selected</c:if> value="2">最近两周</option>
										<option <c:if test="${theLastMarketTime==3}">selected</c:if> value="3">最近一月</option>
									</select>
								</span>
							</p>
							<p>
								<label for="at_province">所在区域：</label>
								<select name="receiverProvince" id="receiverProvince"></select>
								<input type="hidden" name="province" id="atprovince" value="${receiverProvince}" />
								<input type="hidden" name="receiverCity" id="atcity" value="${receiverCity}" />
								<input type="hidden" name="receiverDistrict" id="atarea" value="${receiverDistrict}" />
								
								<input type="hidden" name="provinceName" id="provinceName_" value="${provinceName}" />
								<input type="hidden" name="cityName" id="cityName_" value="${cityName}" />
								<input type="hidden" name="areaName" id="areaName_" value="${areaName}" />
							</p>
							<p>
								<label for="member_level">会员等级：</label>
								<select name="userGrade" id="member_level">
									<option value="4" <c:if test="${userGrade==4}">selected</c:if>>所有会员</option>
									<option value="0" <c:if test="${userGrade==0}">selected</c:if>>普通会员</option>
									<option value="1" <c:if test="${userGrade==1}">selected</c:if>>高级会员</option>
									<option value="2" <c:if test="${userGrade==2}">selected</c:if>>VIP 会员</option>
									<option value="3" <c:if test="${userGrade==3}">selected</c:if>>至尊 VIP 会员</option>
								</select>
							</p>
							<p>
								<label for="trade_amount_a">交易量：</label>
								<input id="trade_amount_a" class="input_text" type="text" value="${tradeCountMin}" name="tradeCountMin"/> 
								至 
								<input id="trade_amount_b" class="input_text" type="text" value="${tradeCountMax}" name="tradeCountMax"/>
								<span id="trade_amountTip"></span>
							</p>
							<p>
								<label for="trade_count_a">交易额：</label>
								<input id="trade_count_a" class="input_text" type="text" value="${tradeAmountMin}" name="tradeAmountMin"/> 
								至 
								<input id="trade_count_b" class="input_text" type="text" value="${tradeAmountMax}" name="tradeAmountMax"/>
								<span id="trade_countTip"></span>
							</p>
							<p>
								<input name="searchInput" id="member" class="input_text" type="text" value="${searchInput}"/>
								<span id="memberTip"></span>
							</p>
							<p>
								<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查询"><span>查 询</span></a>
								
								<label for="save_sear" class="reset_label">保存为搜索器：</label>
								<input class="input_text" id="save_sear" type="text" value="${searchName2}" name="searchName2"/>
								<a href="javascript:;" id="save_btn" class="btn btn_a" title="保存"><span>保 存</span></a>
								<span id="save_searTip"></span>
							</p>
						</form>
						<!-- E form -->
						
					</div>
				</div>	
				<!-- S Table -->
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a">
									<div class="th_title"><em><input class="input_checkbox cur_check check_mark_a check_mark_b" type="checkbox" /></em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>联系人</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>会员名</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>手机号</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>交易额(元)</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>交易量(次)</em></div>
								</th>
								<th class="th_g">
									<div class="th_title"><em>上次交易时间</em></div>
								</th>
								<th class="th_h">
									<div class="th_title"><em>上次活动时间</em></div>
								</th>
								
							</tr>
						</thead>
						
						<tbody>
								<c:if test="${fn:length(smsBuyersList)==0 }">
								<tr>
									<td colspan="8"><center>${msg}</center></td>
								</tr>
								</c:if>  
							
							<c:forEach items="${smsBuyersList}" var="vo">
							<tr>	
								<td class="td_a"><input class="input_checkbox check_mark_a check_mark_b" type="checkbox" value="${vo.id}" /></td>
								<td class="td_b">${vo.receiverName}</td>
								<td class="td_c"><img src="images/single/wang_online.gif" alt="" />${vo.buyerAccount}</td>
								<td class="td_d">${vo.receiverMobile}</td>
								<td class="td_e">${vo.totalTradeAmount}</td>
								<td class="td_f">${vo.totalTradeCount}</td>
								<td class="td_g"><fmt:formatDate value='${vo.theLastTradeTime}' pattern='yyyy-MM-dd'/></td>
								<td class="td_h"><fmt:formatDate value='${vo.theLastMarketTime}' pattern='yyyy-MM-dd'/></td>
							</tr>	
							</c:forEach>
						</tbody>
					</table>
				</div>
				<!-- E Table -->
				
				<div class="clearfix" id="page_guide">
					<div class="leftman">
						<input id="cur_page" <c:if test="${pageChecked==1}">checked</c:if> class="input_checkbox cur_check check_mark_a check_mark_b" type="checkbox" />
						<label for="cur_page">选择当前页</label>
						
						<input id="all_page" <c:if test="${customerId=='all'}">checked</c:if> class="input_checkbox check_mark_b" type="checkbox" />
						<label for="all_page">选择所有页</label>
						
						
						<span>已选择 <em id="checked_number">${customerNumber}</em> 位客户</span>
					</div>
					
					<!-- S PageNavi -->
					<div class="pagenavi">
						<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
					</div>
					<!-- E PageNavi -->
				</div>
				
				<div class="btn_mid">
					<a href="javascript:;" id="prev_step_b" class="btn btn_a" title="上一步"><span>上一步</span></a>
					<a href="javascript:;" id="next_step_b" class="btn btn_a" title="下一步"><span>下一步</span></a>
					<span id="errTip"></span>
				</div>
			</div>
	<script type="text/javascript">
		var params = {
			saveSearchAction: 'smsServiceMarket!saveBuyersSearch.action',				// 保存搜索条件 Action
			prevStepAction: 'smsServiceMarket!smsMarketPageStep1.action',				// 上一步
			nextStepAction: 'smsServiceMarket!smsMarketPageStep3.action',					// 下一步
			smsCountAction: 'smsServiceMarket!hasEnoughSms.action'					// 短信数量请求
		}
	</script>
	
    <script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
    <script type="text/javascript" src="${jsPath}/page/mark_home2.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
		
	<!-- E Main -->
	
