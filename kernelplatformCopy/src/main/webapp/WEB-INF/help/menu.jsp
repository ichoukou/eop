<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
<!-- S Sidebar -->
<div id="sidebar">
	<ul class="top_menu">
		<li id="menu_a">
			<div class="menu_hd">
				<i></i><h5>帮助中心</h5>
			</div>
			<ul class="sub_menu">
				<c:choose>
					<c:when test="${menuFlag == 'help_home' || str:isEmpty(menuFlag) }">
				<li><a menuFlag="help_home" class="cur_menu_item" href="noint1_eachHelp.action?article.remark=help_home&menuFlag=help_home">易通简介</a></li>
					</c:when>
					<c:otherwise>
				<li><a menuFlag="help_home" href="noint1_eachHelp.action?article.remark=help_home&menuFlag=help_home">易通简介</a></li>
					</c:otherwise>
				</c:choose>
				<li><a menuFlag="help_zhinengchajian" href="noint1_eachHelp.action?article.remark=help_zhinengchajian&menuFlag=help_zhinengchajian">智能查件</a></li>
				<li><a menuFlag="help_monitor_waybill" href="noint1_eachHelp.action?article.remark=help_monitor_waybill&menuFlag=help_monitor_waybill">运单监控</a></li>
				<li><a menuFlag="help_wentijian" href="noint1_eachHelp.action?article.remark=help_wentijian&menuFlag=help_wentijian">问题件管理</a></li>
				<li><a menuFlag="help_dianziduizhang" href="noint1_eachHelp.action?article.remark=help_dianziduizhang&menuFlag=help_dianziduizhang">电子对账</a></li>
				<li><a menuFlag="help_orderPrint" href="noint1_eachHelp.action?article.remark=help_orderPrint&menuFlag=help_orderPrint">面单打印</a></li>
				<li><a menuFlag="help_orderimport" href="noint1_eachHelp.action?article.remark=help_orderimport&menuFlag=help_orderimport">订单导入</a></li>
				<c:if test="${yto:getCookie('userType') == 2
        		|| yto:getCookie('userType') == 21
        		|| yto:getCookie('userType') == 22
        		|| yto:getCookie('userType') == 23}">
				<li><a menuFlag="help_yunfeimoban" href="noint1_eachHelp.action?article.remark=help_yunfeimoban&menuFlag=help_yunfeimoban">运费模板</a></li>
				<li><a menuFlag="help_yunfeitiaozheng" href="noint1_eachHelp.action?article.remark=help_yunfeitiaozheng&menuFlag=help_yunfeitiaozheng">运费调整</a></li>
				</c:if>
				<li><a menuFlag="help_wangdianchazhao" href="noint1_eachHelp.action?article.remark=help_wangdianchazhao&menuFlag=help_wangdianchazhao">网点查找</a></li>
				<li><a menuFlag="help_xiaoxiguanli" href="noint1_eachHelp.action?article.remark=help_xiaoxiguanli&menuFlag=help_xiaoxiguanli">消息管理</a></li>
				<li><a menuFlag="help_wodezhanghao" href="noint1_eachHelp.action?article.remark=help_wodezhanghao&menuFlag=help_wodezhanghao">我的账号</a></li>
				<li><a menuFlag="help_zizhanghaoguanli" href="noint1_eachHelp.action?article.remark=help_zizhanghaoguanli&menuFlag=help_zizhanghaoguanli">子账号管理</a></li>
				<c:if test="${yto:getCookie('userType') == 1
        		|| yto:getCookie('userType') == 11
        		|| yto:getCookie('userType') == 12
        		|| yto:getCookie('userType') == 13}">
				<li><a menuFlag="help_duodianpuguanli" href="noint1_eachHelp.action?article.remark=help_duodianpuguanli&menuFlag=help_duodianpuguanli">多店铺管理</a></li>
				</c:if>
				<c:if test="${yto:getCookie('userType') == 2
        		|| yto:getCookie('userType') == 21
        		|| yto:getCookie('userType') == 22
        		|| yto:getCookie('userType') == 23}">
				<li><a menuFlag="help_wodekehu" href="noint1_eachHelp.action?article.remark=help_wodekehu&menuFlag=help_wodekehu">我的客户</a></li>
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
		if(${str:isNotEmpty(menuFlag)} && '${menuFlag}' == menuFlag){
			$(this).addClass("cur_menu_item");
		}
	});
		
</script>