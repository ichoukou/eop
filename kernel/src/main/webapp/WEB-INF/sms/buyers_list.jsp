<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" type="text/css" href="css/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/common/common.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/module/button.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="css/module/box.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="css/module/dialog.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="css/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="css/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link href="css/page/member_sear.css?d=${str:getVersion() }" rel="stylesheet" type="text/css">
	<!-- E 当前页面 CSS -->
	<title>会员查询</title>
</head>
<body id="member_sear">
	<!-- #include file="公共模块/header.html" -->
	
	<!-- S Main -->
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">会员查询</h2>	
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S Box -->
				<div class="box box_a" id="step_b_form">
					<div class="box_bd">
						<!-- S form -->
						<form action=""  class="form" id="mark_form2">
							<p>
								<label for="sear_name">搜索器名称：</label>
								<s:select id="sear_name" name="searName" list="smsBuyersSearchs" headerKey="" headerValue="请选择" listKey="id" listValue="searchName"></s:select>
							</p>
							<p>
								<label for="trade_time">上次交易时间：</label>
								<s:set name="tradeTime" value="#{'':'请选择','7':'一周以内','30':'一个月以内'}"></s:set>
								<s:select list="#tradeTime" listKey="key"  listValue="value" id="trade_time" name="tradeTime"></s:select>
							</p>
							<p>
								<label for="at_province">所在区域：</label>
								<select id="at_province"></select>
							</p>
							<p>
								<label for="member_level">会员等级：</label>
								<s:set name="memberLevel" value="#{'4':'所有会员','0':'普通会员','1':'高级会员','2':'VIP 会员','3':'至尊 VIP 会员'}"></s:set>
								<s:select name="memLevel" id="member_level" list="#memberLevel" listKey="key" listValue="value"></s:select>
							</p>
							<p>
								<label for="trade_amount_a">交易量：</label>
								<s:textfield id="trade_amount_a" name="tradeAmountA" cssClass="input_text"></s:textfield>
								至 
								<s:textfield id="trade_amount_b" name="tradeAmountB" cssClass="input_text"></s:textfield>
								<span id="trade_amountTip"></span>
							</p>
							<p>
								<label for="trade_count_a">交易额：</label>
								<s:textfield id="trade_count_a" name="tradeCountA" cssClass="input_text" ></s:textfield>
								至 
								<s:textfield id="trade_count_b" name="tradeCountB" cssClass="input_text" ></s:textfield>
								<span id="trade_countTip"></span>
							</p>
							<p>
								<s:textfield id="member" name="member" cssClass="input_text"></s:textfield>
								<span id="memberTip"></span>
							</p>
							<p>
								<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查询"><span>查 询</span></a>
								
								<label for="save_sear" class="reset_label">保存为搜索器：</label>
								<s:textfield cssClass="input_text" id="save_sear"  name="saveSearInput"></s:textfield>
								<a href="javascript:;" id="save_btn" class="btn btn_a" title="保存"><span>保 存</span></a>
								<span id="save_searTip"></span>
							</p>
							<!-- 搜索器ID -->
							<input type="hidden" name="smsBuyersSearchId" id="smsBuyersSearchId"/>
							<!-- 排序字段 -->
							<input type="hidden" name="orderByCol" id="orderByCol"/>
						</form>
						<!-- E form -->
						
					</div>
				</div>	
				<!-- S Table -->
				<div class="table">
				
					<div id="operate_guide">
						<a href="javascript:;" class="btn btn_a import_data" title="导入数据"><span>导入数据</span></a>
						<a href="buyers!toAdd.action" class="btn btn_a add_member" title="添加新会员"><span>添加新会员</span></a>
						<s:set name="orderByStr" value="#{'':'请选择','tradeAmount':'按交易额','tradeCount':'交易量'}"></s:set>
						<s:select id="orderby" name="orderByCol" list="#orderByStr" listKey="key" listValue="value"></s:select>
					</div>
					
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
									<div class="th_title"><em>店铺名称</em>
									<ul class="thead_select">
										<s:iterator value="bindUserList">
											<li value="<s:property value='id'/>><a href=""><s:property value="shopName"/></a></li>
										</s:iterator>
									</ul>
									</div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>交易额（元）</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>交易量（次）</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>上次交易时间</em></div>
								</th>
								<th class="th_g">
									<div class="th_title"><em>会员名</em></div>
								</th>
								<th class="th_h">
									<div class="th_title"><em>联系电话</em></div>
								</th>
								<th class="th_i">
									<div class="th_title"><em>操作</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody>
					<s:iterator value="smsBuyers">
								<tr>
								<td class="td_a"><input class="input_checkbox check_mark_a check_mark_b" type="checkbox" value="1" /></td>
								<td class="td_b"><a href="javascript:;"><s:property value="receiverName"/></a></td>
								<td class="td_c">李宁新蛋店</td>
								<td class="td_d"><s:property value="totalTradeAmount"/></td>
								<td class="td_e"><s:property value="totalTradeCount"/></td>
								<td class="td_f"><s:date name="theLastTradeTime" format="yyyy-MM-dd"/> <s:property value=""/></td>
								<td class="td_g"><img src="images/single/wang_online.gif" alt="" /><s:property value="buyerAccount"/></td>
								<td class="td_h"><s:property value="receiverMobile"/></td>
								<td class="td_i">
									<a href="buyers!toEdit.action?buyers.id=<s:property value="id"/>" class="edit_contact">编辑</a>
									<a href="javascript:;" class="del_contact">删除</a>
									<!-- 会员Id -->
									<input type="hidden" value="<s:property value="id"/>" class="member_id" />
								</td>
							</tr>
					</s:iterator>
						</tbody>
					</table>
				</div>
				<!-- E Table -->
				
				<div class="clearfix" id="page_guide">
					<div class="leftman">
						<input id="cur_page" class="input_checkbox cur_check check_mark_a check_mark_b" type="checkbox" />
						<label for="cur_page">全选</label>
						
						
						<a href="buyers!saveSmsBuyer.action" class="btn btn_a add_member" title="添加新会员"><span>添加新会员</span></a>
						<a href="javascript:;" class="btn btn_a del_member" title="删除会员"><span>删除会员</span></a>
					</div>
					    <form action="buyers!searchBuyersList.action" id="pageForm" method="post">
					    	<input type="hidden" name="menuFlag" value="${menuFlag }" />
					    	<input type="hidden" id="currentPage" name="currentPage" value="${pagination.currentPage}" />
					    </form>
					 <!-- S PageNavi -->
				      <div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
				      <!-- E PageNavi -->
				</div>
			</div>
		<!-- E Content -->
		
		<!-- #include file="公共模块/sidebar.html" -->
	</div>
	<!-- E Main -->
	<!-- #include file="公共模块/footer.html" -->
	
	<script type="text/javascript">
		var params = {
		   downLoadTemplateUrl: '',   // 下载导入模板 Url
			howtoImportfUrl: '',       // 怎样导入？Url
		   importDataAction: '',            // 导入数据 Action
			saveSearchAction: 'buyers!saveSMSBuyersSearchParams.action',// 保存搜索条件 Action
			delMemberAction: 'buyers!delSmsBuyers.action'					// 删除会员 Action
		}
	</script>
	<script type="text/javascript" src="js/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="js/util/position_fixed.js?d=${str:getVersion() }"></script>
		<script type="text/javascript" src="js/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('.png');
		</script>
	<![endif]-->
    <script type="text/javascript" src="js/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/module/select/district-all.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/module/dialog.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
    <script type="text/javascript" src="js/page/member_sear.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
</body>
