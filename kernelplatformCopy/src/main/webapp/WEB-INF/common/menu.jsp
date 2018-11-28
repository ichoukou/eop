<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
<!-- S Sidebar -->
<div id="sidebar">
	<h4>
	<c:choose>
		<c:when test="${menuFlag == 'home' || str:isEmpty(menuFlag) }">
			<a menuFlag="home" class="current" href="mainPage_home.action?menuFlag=home" title="我的易通">我的易通</a>
		</c:when>
		<c:otherwise>
			<a menuFlag="home" href="mainPage_home.action?menuFlag=home" title="我的易通">我的易通</a>
		</c:otherwise>
	</c:choose>
	</h4>
	<ul class="top_menu">
		<c:if test="${yto:getCookie('userType') == 1
        		|| yto:getCookie('userType') == 11
        		|| yto:getCookie('userType') == 13
     	|| yto:getCookie('userType') == 2 
   		|| yto:getCookie('userType') == 21
   		|| yto:getCookie('userType') == 23
   		|| yto:getCookie('userType') == 4
   		|| yto:getCookie('userType') == 41}">
		<li id="menu_a">
			<div class="menu_hd">
				<i></i><h5>运单管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a  menuFlag="waybill" href="waybill_bill.action?menuFlag=waybill" title="智能查件">智能查件</a></li>
				<li><a  menuFlag="monitor_index" href="monitor_index.action?menuFlag=monitor_index" title="运单监控">运单监控</a></li>
				<c:if test="${yto:getCookie('userType') == 1
	        		|| yto:getCookie('userType') == 11
	        		|| yto:getCookie('userType') == 13
	        		|| yto:getCookie('userType') == 3
	        		|| yto:getCookie('userType') == 4
	        		|| yto:getCookie('userType') == 41}">
					<li><a menuFlag="attention" href="monitor_attentionlist.action?currentPage=1&tabFlag=2&menuFlag=attention" title="我的关注">我的关注</a></li>
				</c:if>
				<li><a menuFlag="question" href="questionnaire_index.action?menuFlag=question" title="问题件管理">问题件管理</a></li>
				<!--  
				<c:if test="${yto:getCookie('userType') == 1 ||
							  yto:getCookie('userType') == 11 ||
							  yto:getCookie('userType') == 12 ||
							  yto:getCookie('userType') == 13 ||
							  yto:getCookie('userType') == 4}">
				<li><a  name="50" menuFlag="passManage_warn" href="passManage_warnningIndex.action?menuFlag=passManage_warn" title="时效提醒">时效提醒</a></li>
				</c:if>
				<c:if test="${yto:getCookie('userType') == 2 ||
							  yto:getCookie('userType') == 21 ||
							  yto:getCookie('userType') == 22 ||
							  yto:getCookie('userType') == 23}">
					<li><a name="55" menuFlag="passManage_list" href="passManage_list_site.action?flag=0&menuFlag=passManage_list" title="时效提醒">时效提醒</a></li>
				</c:if>
				-->
			</ul>
		</li>
		</c:if>
		
		<c:if test="${yto:getCookie('userType') == 1
        		|| (yto:getCookie('userType') == 12)
        		|| (yto:getCookie('userType') == 13)
		    	|| yto:getCookie('userType') == 2
		    	|| yto:getCookie('userType') == 22
		    	|| yto:getCookie('userType') == 23
		    	|| yto:getCookie('userType') == 4}">
		<li id="menu_b">
			<div class="menu_hd">
				<i></i><h5>财务管理</h5>
			</div>
			<ul class="sub_menu">
				<c:if test="${yto:getCookie('switchEccount') == 0}">
					<li><a  menuFlag="ecAccount" href="order!toECAccount.action?currentPage=1&menuFlag=ecAccount" title="电子对账">电子对账</a></li>
					<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
						<li><a  menuFlag="posttemp" href="posttemp!toPosttempView2.action?menuFlag=posttemp" title="运费模板">运费模板</a></li>
						<li><a  menuFlag="mjfreight1" href="mjfreight!mjunlikefreight.action?menuFlag=mjfreight1" title="特殊账单">特殊账单</a></li>
					</c:if>
				</c:if>
				<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
					<li><a  menuFlag="posttemp" href="posttemp!toPosttemp.action?currentPage=1&menuFlag=posttemp">运费模板</a></li>
					<li><a  menuFlag="freight2" href="freight!unlikefreight.action?menuFlag=freight2">运费调整</a></li>
		        </c:if>
		        <c:if test="${yto:getCookie('userType') == 4}">
					<li><a  menuFlag="posttemp" href="posttemp!toPosttemp.action?currentPage=1&menuFlag=posttemp">运费模板</a></li>
					<li><a  menuFlag="mjfreight4" href="mjfreight!mjunlikefreight.action?menuFlag=mjfreight4">特殊账单</a></li>
		        </c:if>
		         <c:if test="${yto:getCookie('userType') == 1 
				  || yto:getCookie('userType') == 4
				  || yto:getCookie('userType') == 5}">
		        <li><a name="51" menuFlag="alipay" href="alipay_toRechargeOnline.action?menuFlag=alipay" title="在线充值">在线充值</a></li>
		        <li><a name="52" menuFlag="rechargeHelp" href="payService_rechargeHelp.action?menuFlag=rechargeHelp" title="支付帮助">支付帮助</a></li>
		         <li><a name="53" menuFlag="paymentList" href="payService_getPaymentList.action?menuFlag=paymentList" title="收支明细">收支明细</a></li>
				 </c:if>	
			</ul>
		</li>
		</c:if>
		
		<c:if test="${yto:getCookie('userType') == 1
        		|| yto:getCookie('userType') == 11
        		|| yto:getCookie('userType') == 12
        		|| yto:getCookie('userType') == 13
        		|| yto:getCookie('userType') == 4
        		|| yto:getCookie('userType') == 41
     	|| yto:getCookie('userType') == 2 
   		|| yto:getCookie('userType') == 21
   		|| yto:getCookie('userType') == 22
   		|| yto:getCookie('userType') == 23}">
		<li id="menu_c">
			<div class="menu_hd">
				<i></i><h5>辅助功能</h5>
			</div>
			<ul class="sub_menu">
				<c:if test="${yto:getCookie('userType') == 1
	        		|| yto:getCookie('userType') == 11
	        		|| yto:getCookie('userType') == 12
	        		|| yto:getCookie('userType') == 13}">
					<li><a  menuFlag="orderPlace" href="orderPlace!toOrderCreate.action?menuFlag=orderPlace">我要发货</a></li>
		        </c:if>
				<c:if test="${yto:getCookie('userType') == 1
		        	|| yto:getCookie('userType') == 11 
	        		|| yto:getCookie('userType') == 12 
 	        		|| yto:getCookie('userType') == 13 
 	        		|| yto:getCookie('userType') == 2 
 	        		|| yto:getCookie('userType') == 21 
 	        		|| yto:getCookie('userType') == 22 
 	        		|| yto:getCookie('userType') == 23}">
		        	<li id="li_orderimport"><a menuFlag="orderimport" href="orderImport_toOrderImoprt.action?menuFlag=orderimport" title="订单导入">订单导入</a></li>
		        </c:if>
		        <c:if test="${yto:getCookie('userType') == 1 
	        		|| yto:getCookie('userType') == 11 
	        		|| yto:getCookie('userType') == 12 
 	        		|| yto:getCookie('userType') == 13 
 	        		|| yto:getCookie('userType') == 2 
 	        		|| yto:getCookie('userType') == 21 
 	        		|| yto:getCookie('userType') == 22 
 	        		|| yto:getCookie('userType') == 23 
 	        		|| yto:getCookie('userType') == 41}"> 
					<li id="li_orderprint"><a  menuFlag="orderPrint" href="orderPrint!orderPrint.action?menuFlag=orderPrint" title="面单打印">面单打印</a></li>
					<li id="li_orderprint_temp"><a  menuFlag="orderExpress" href="waybill_orderExpress.action?menuFlag=orderExpress" title="面单模板">面单模板</a></li>
				</c:if>

				<li><a  menuFlag="branchlist" href="branchlist.action?menuFlag=branchlist" title="网点查找">网点查找</a></li>
			</ul>
		</li>
		</c:if>
				<!-- 面单子菜单S -->
<%-- 				<c:if test="${yto:getCookie('userType') == 1  --%>
<!--   					|| yto:getCookie('userType') == 11  -->
<!--   					|| yto:getCookie('userType') == 12  -->
<!--   					|| yto:getCookie('userType') == 13  -->
<!--   					|| yto:getCookie('userType') == 4  -->
<!--   					|| yto:getCookie('userType') == 41 }">  -->
<%-- 					<c:if test="${str:startsWith(menuFlag,'face_') }"> --%>
<!-- 						<li menuFlag="face_home"><a href="firstLoading.action?menuFlag=face_home" title="电子面单">电子面单</a></li> -->
<%-- 					</c:if> --%>
<%-- 				</c:if> --%>
				<!-- 面单子菜单E -->
				
				<!-- 面单子菜单S -->
<%-- 				<c:if test="${yto:getCookie('userType') == 3 }"> --%>
<%-- 					<c:if test="${str:startsWith(menuFlag,'face_') }"> --%>
<!-- 						<li menuFlag="face_home"><a href="firstLoading.action?menuFlag=face_home" title="电子面单">电子面单</a></li> -->
<!-- 						<li menuFlag="sms_templateAdmin"><a href="sSellerInfo.action?menuFlag=face_home" title="商家账号管理">商家账号管理</a></li> -->
<%-- 					</c:if> --%>
<%-- 				</c:if> --%>
				<!-- 面单子菜单E -->
		
		<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 11 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13   
     	|| yto:getCookie('userType') == 2 || yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23   
     	|| yto:getCookie('userType') == 3
     	|| yto:getCookie('userType') == 4
     	|| yto:getCookie('userType') == 41}">
		<li id="menu_d">
			<div class="menu_hd">
				<i></i><h5>消息管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a  menuFlag="msg_index" href="message_index.action?menuFlag=msg_index" title="查看消息">查看消息</a></li>
				<c:if test="${yto:getCookie('userType') != 4 && yto:getCookie('userType') != 41}">
					<%-- 平台和客服木有发新消息 --%>
					<li><a  menuFlag="msg_send" href="send_openUI.action?menuFlag=msg_send" title="发新消息">发新消息</a></li>
				</c:if>
				<c:if test="${yto:getCookie('userType') != 3}">
					<li><a  menuFlag="msg_advise" href="send_openAdviseUI.action?menuFlag=msg_advise" title="建议意见">建议意见</a></li>
				</c:if>
			</ul>
		</li>
		</c:if>
		
		
		<li id="menu_e">
			<div class="menu_hd">
				<i></i><h5>账号管理</h5>
			</div>
			<ul class="sub_menu">
				<c:if test="${yto:getCookie('userType') == 1}">
				<li><a  menuFlag="tobindAccount" href="toBindedAccount.action?menuFlag=tobindAccount" title="多店铺管理">多店铺管理</a></li>
				</c:if>
				<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 1 || yto:getCookie('userType') == 4}">
			        <li>
				        <a  menuFlag="sub_acc_list" href="user!toSubAccountList.action?menuFlag=sub_acc_list">
				        	<c:if test="${yto:getCookie('userType') == 4}">客服账号</c:if>
				        	<c:if test="${yto:getCookie('userType') != 4}">子账号管理</c:if>
				        </a>
			        </li>
		        </c:if>
		        <c:if test="${yto:getCookie('userType') == 4}">
		        	<li><a  menuFlag="platf_acc_list" href="user!toPlatformSubAccountList.action?menuFlag=platf_acc_list">业务账号</a></li>
		        </c:if>
		        <c:if test="${yto:getCookie('userType') == 1
	        		|| yto:getCookie('userType') == 11
	        		|| yto:getCookie('userType') == 12
	        		|| yto:getCookie('userType') == 13}">
		        	<li><a  menuFlag="acc_custom" href="associationAccount_toBindeAccountCustom.action?menuFlag=acc_custom">个性化配置</a></li>
		        </c:if>
		        
				<li><a  menuFlag="my_acc" href="user!toEdit.action?user.id=${yto:getCookie('id')}&menuFlag=my_acc" title="我的账号">我的账号</a></li>
				 <c:if test="${yto:getCookie('userType') == 1 
				  || yto:getCookie('userType') == 4
				  || yto:getCookie('userType') == 5}">
				  <li><a name="54" menuFlag="payService" href="payService_index.action?menuFlag=payService" title="服务管理">服务管理</a></li>
				   </c:if>
				</ul>
		</li>
		<c:if test="${yto:getCookie('userType') == 1 ||
							  yto:getCookie('userType') == 11 ||
							  yto:getCookie('userType') == 12 ||
							  yto:getCookie('userType') == 13 ||
							  yto:getCookie('userType') == 4}">
			<li id="menu_f">
				<div class="menu_hd">
					<i></i><h5>短信功能</h5>
				</div>
				<ul class="sub_menu">
					<li><a  menuFlag="sms_home" href="smsHomeEvent_homePage.action?menuFlag=sms_home" title="短信服务">短信服务</a></li>
					<!-- <li><a  menuFlag="sms_made" href="smsServiceMarket!smsMarketPageStep1.action?menuFlag=sms_made">营销活动</a></li> -->
					<li><a  menuFlag="template_list" href="template_list.action?menuFlag=template_list"><span>短信模板</span></a></li>
					<li><a  menuFlag="sms_info" href="smsSearch!searchPage.action?menuFlag=sms_info"><span>短信查询</span></a></li>
					<!-- <li><a  menuFlag="buyers_manage" href="buyers!toIndex.action?menuFlag=buyers_manage"><span>会员管理</span></a></li> -->
					<li><a  menuFlag="sms_package" href="smsServiceMarket!inBuyPorts.action?menuFlag=sms_package" title="短信充值">购买短信</a></li>
					
					
				</ul>
			</li>
		</c:if>
		<c:if test="${yto:getCookie('userType') == 2}">
			<li id="menu_g">
				<div class="menu_hd">
					<i></i><h5>客户管理</h5>
				</div>
				<ul class="sub_menu">
					<li><a  menuFlag="my_custom" href="user!list.action?type=0&menuFlag=my_custom" title="我的客户">我的客户</a></li>	
				</ul>
			</li>
		</c:if>
		
		<c:if test="${yto:getCookie('userType') == 3}">
		  <li id="menu_g">
			<div class="menu_hd">
				<i></i><h5>短信管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a menuFlag="sms_search_admin" href="smsSearchAdmin_smsSearchAdmin.action?menuFlag=sms_search_admin&noQuery=1" title="短信查询">短信查询</a></li>
				<li><a menuFlag="sms_templateAdmin" href="template_templateAdmin.action?menuFlag=sms_templateAdmin" title="模版审核">模版管理</a></li>
				<li><a menuFlag="filterWords_list" href="filterWords_list.action?menuFlag=filterWords_list" title="过滤词管理">过滤词管理</a></li>
			</ul>
		</li>

		<li id="menu_g">
			<div class="menu_hd">
				<i></i><h5>渠道管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a  menuFlag="channel_list" href="channel_list.action?currentPage=1&menuFlag=channel_list" title="渠道消息管理">渠道消息管理</a></li>
			</ul>
		</li>
		<li id="menu_g">
			<div class="menu_hd">
				<i></i><h5>配置管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a  menuFlag="config_index" href="config_index.action?currentPage=1&menuFlag=config_index" title="配置消息管理">配置消息管理</a></li>
			</ul>
		</li>
		<li id="menu_g">
			<div class="menu_hd">
				<i></i><h5>CMS管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a  menuFlag="article_list" href="searchArticle.action?article.columnId=1&currentPage=1&menuFlag=article_list" title="栏目文章管理">栏目文章管理</a></li>
			</ul>
		</li>
		<li id="menu_g">
			<div class="menu_hd">
				<i></i><h5>应用管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a  menuFlag="app_list" href="searchApp.action?currentPage=1&menuFlag=app_list" title="应用审核管理">应用审核管理</a></li>
			</ul>
		</li>
		<li id="menu_g">
			<div class="menu_hd">
				<i></i><h5>会员管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a menuFlag="eopUser_list" href="eopUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=eopUser_list">EOP会员管理</a></li>
				<li><a menuFlag="ytoUser_list" href="ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=ytoUser_list">易通会员管理</a></li>
				<li><a menuFlag="dataInit" href="ytoUser!init.action?menuFlag=dataInit">数据初始化</a></li>
			</ul>
		</li>
		<!-- 
		<li id="menu_g">
			<div class="menu_hd">
				<i></i><h5>账户管理</h5>
			</div>
			<ul class="sub_menu">
				<li><a menuFlag="account_list" href="accountAdmin_index.action?currentPage=1&menuFlag=account_list" title="账户管理">账户管理</a></li>
				<li><a menuFlag="payment_list" href="paymentAdmin_index.action?currentPage=1&menuFlag=payment_list" title="收支明细">收支明细</a></li>
				<li><a menuFlag="service_list" href="serviceAdmin_index.action?currentPage=1&menuFlag=service_list" title="服务管理">服务管理</a></li>
			</ul>
		</li>
		-->
	    </c:if>  
	</ul>
</div>
<!-- E Sidebar -->
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