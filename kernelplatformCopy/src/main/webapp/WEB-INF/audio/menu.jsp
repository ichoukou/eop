<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
<!-- S Sidebar -->
<div id="sidebar">
	<ul class="top_menu">
		<li id="menu_a">
			<div class="menu_hd">
				<i></i><h5>帮助视频</h5>
			</div>
			<ul class="sub_menu">
				<c:if test="${yto:getCookie('userType')==1 
					|| yto:getCookie('userType')==11
					|| yto:getCookie('userType')==12
					|| yto:getCookie('userType')==13
					|| yto:getCookie('userType')==3
					|| yto:getCookie('userType')==4 
					|| yto:getCookie('userType')==41}">
					<c:choose>
						<c:when test="${jsonResult == 'vip_1_yition_intro' || str:isEmpty(jsonResult) }">
					<li><a menuFlag="vip_1_yition_intro" class="cur_menu_item" href="noint1_audio.action?jsonResult=vip_1_yition_intro">易通简介</a></li>
						</c:when>
						<c:otherwise>
					<li><a menuFlag="vip_1_yition_intro" href="noint1_audio.action?jsonResult=vip_1_yition_intro">易通简介</a></li>
						</c:otherwise>
					</c:choose>
					<li><a menuFlag="vip_9_quickload" href="noint1_audio.action?jsonResult=vip_9_quickload">卖家首次登录</a></li>
					<li><a menuFlag="vip_2_login_activate" href="noint1_audio.action?jsonResult=vip_2_login_activate">登录激活</a></li>
					<li><a menuFlag="vip_3_intelligent_query" href="noint1_audio.action?jsonResult=vip_3_intelligent_query">智能查件</a></li>
					<li><a menuFlag="vip_4_monitor_waybill" href="noint1_audio.action?jsonResult=vip_4_monitor_waybill">运单监控</a></li>
					<li><a menuFlag="vip_5_questionnaire_manage" href="noint1_audio.action?jsonResult=vip_5_questionnaire_manage">问题件管理</a></li>
					<li><a menuFlag="vip_6_ecaccount" href="noint1_audio.action?jsonResult=vip_6_ecaccount">电子对账</a></li>
					<li><a menuFlag="vip_10_orderPrint" href="noint1_audio.action?jsonResult=vip_10_orderPrint">面单打印</a></li>
					<li><a menuFlag="vip_11_orderimport" href="noint1_audio.action?jsonResult=vip_11_orderimport">订单导入</a></li>
					<!-- <li><a menuFlag="vip_12_orderimport" href="noint1_audio.action?jsonResult=vip_12_orderimport">订单导入2</a></li> -->
					<li><a menuFlag="vip_7_shops_manage" href="noint1_audio.action?jsonResult=vip_7_shops_manage">多店铺管理</a></li>
					<li><a menuFlag="vip_8_childAccount_manage" href="noint1_audio.action?jsonResult=vip_8_childAccount_manage">子账号管理</a></li>
				</c:if>
				<c:if test="${yto:getCookie('userType')==2 
					|| yto:getCookie('userType')==21
					|| yto:getCookie('userType')==22
					|| yto:getCookie('userType')==23
					|| yto:getCookie('userType')==3}">
					<c:choose>
						<c:when test="${jsonResult == 'site_1_yition_intro' || str:isEmpty(jsonResult) }">
					<li><a menuFlag="site_1_yition_intro" class="cur_menu_item" href="noint1_audio.action?jsonResult=site_1_yition_intro">易通简介</a></li>
						</c:when>
						<c:otherwise>
					<li><a menuFlag="site_1_yition_intro" href="noint1_audio.action?jsonResult=site_1_yition_intro">易通简介</a></li>
						</c:otherwise>
					</c:choose>
					<li><a menuFlag="site_9_quickload" href="noint1_audio.action?jsonResult=site_9_quickload">网点首次登录</a></li>
					<li><a menuFlag="site_2_login_activate" href="noint1_audio.action?jsonResult=site_2_login_activate">登录激活</a></li>
					<li><a menuFlag="site_3_customer_code" href="noint1_audio.action?jsonResult=site_3_customer_code">客户编码</a></li>
					<li><a menuFlag="site_4_intelligent_query" href="noint1_audio.action?jsonResult=site_4_intelligent_query">智能查件</a></li>
					<!-- <li><a menuFlag="site_4_monitor_waybill" href="noint1_audio.action?jsonResult=site_4_monitor_waybill">运单监控</a></li> -->
					<li><a menuFlag="site_5_questionnaire_manage" href="noint1_audio.action?jsonResult=site_5_questionnaire_manage">问题件管理</a></li>
					<li><a menuFlag="site_6_ecaccount" href="noint1_audio.action?jsonResult=site_6_ecaccount">电子对账</a></li>
					<li><a menuFlag="site_10_orderPrint" href="noint1_audio.action?jsonResult=site_10_orderPrint">面单打印</a></li>
					<li><a menuFlag="site_11_orderimport" href="noint1_audio.action?jsonResult=site_11_orderimport">订单导入</a></li>
					<!-- <li><a menuFlag="site_12_orderimport" href="noint1_audio.action?jsonResult=site_12_orderimport">订单导入2</a></li> -->
					<li><a menuFlag="site_7_carriage_change" href="noint1_audio.action?jsonResult=site_7_carriage_change">运费调整</a></li>
					<li><a menuFlag="site_8_childAccount_manage" href="noint1_audio.action?jsonResult=site_8_childAccount_manage">子账号管理</a></li>
				</c:if>
			</ul>
		</li>
	</ul>
</div>
<script type="text/javascript">

	$('.top_menu li ul li').each(function() {
		$(this).click(function(){
			if(!!$(".cur_menu_item")){
				$(".cur_menu_item").removeClass("cur_menu_item");
			}
			$(this).addClass("cur_menu_item");
		});
	});
		
	$(".top_menu li ul li a").each(function(){
		var menuFlag = $(this).attr("menuFlag");
		if(${str:isNotEmpty(jsonResult)} && '${jsonResult}' == menuFlag){
			$(this).addClass("cur_menu_item");
		}
	});
		
</script>