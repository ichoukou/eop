<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
<!-- S Header -->
	<div id="header_v2">
		<div id="header_inner">
			<h1><a href="mainPage_home.action" title="易通">易通</a></h1>
			<ul id="header_shortcut" class="clearfix">
				<li><a href="javascript:;" id="menu_fav" title="收藏易通">收藏易通</a></li>
				<li><a href="javascript:;" id="video" title="视频教程">视频教程</a></li>
				<li><a href="javascript:;" id="help" title="帮助中心">帮助中心</a></li>
			</ul>
		</div>
	</div>
	<!-- E Header -->
	
	<!-- S Nav -->
	<div id="nav_v2">
		<div id="main_nav" class="clearfix">
			<!-- S 菜单 -->
			<div id="main_nav_l">
				<div id="menu_level_1">
					<ul class="clearfix">
						<c:choose>
							<c:when test="${yto:getCookie('userType') == 3}">
								<li menuFlag="home" id="menu_a" data-menu="menu_a" class="cur_menu"><a href="javascript:;"><i></i><strong>首 页</strong></a></li>
								<li menuFlag="peizhi" id="menu_b" data-menu="menu_b"><a href="javascript:;"><i></i><strong>配置</strong></a></li>
								
								<li menuFlag="sms" id="menu_d" data-menu="menu_d"><a href="javascript:;"><i></i><strong>短 信</strong></a></li>
								<li menuFlag="huiyuan" id="menu_c" data-menu="menu_c"><a href="javascript:;"><i></i><strong>会员管理</strong></a></li>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${str:startsWith(menuFlag,'home') || str:isEmpty(menuFlag) }">
										<li menuFlag="home" id="menu_a" data-menu="menu_a" class="cur_menu"><a href="javascript:;"><i></i><strong>首 页</strong></a></li>
									</c:when>
									<c:otherwise>
										<li menuFlag="home" id="menu_a" data-menu="menu_a"><a href="javascript:;"><i></i><strong>首 页</strong></a></li>
									</c:otherwise>
								</c:choose>
								<c:if test="${yto:getCookie('userType') == 1
			        				|| yto:getCookie('userType') == 11
			        				|| yto:getCookie('userType') == 13
					     			|| yto:getCookie('userType') == 2 
					   				|| yto:getCookie('userType') == 21
					   				|| yto:getCookie('userType') == 23
					   				|| yto:getCookie('userType') == 4
					   				|| yto:getCookie('userType') == 41}">
								<li menuFlag="chajian" id="menu_b" data-menu="menu_b"><a href="javascript:;"><i></i><strong>查 件</strong></a></li>
								</c:if>
								
								<c:if test="${yto:getCookie('userType') == 1
				        			|| yto:getCookie('userType') == 11 
			        				|| yto:getCookie('userType') == 12 
		 	        				|| yto:getCookie('userType') == 13 
		 	        				|| yto:getCookie('userType') == 2 
		 	        				|| yto:getCookie('userType') == 4 
		 	        				|| yto:getCookie('userType') == 21 
		 	        				|| yto:getCookie('userType') == 22 
		 	        				|| yto:getCookie('userType') == 23}">
								<li menuFlag="fahuo" id="menu_e" data-menu="menu_e"><a href="javascript:;"><i></i><strong>发 货</strong></a></li>
								</c:if>
								<c:if test="${yto:getCookie('userType') == 1 
								  	||yto:getCookie('userType') == 11 
								  	||yto:getCookie('userType') == 12 
								  	||yto:getCookie('userType') == 13 
								  	||yto:getCookie('userType') == 4   
								 	||yto:getCookie('userType') == 41 }">
								<li menuFlag="sms" id="menu_d" data-menu="menu_d"><a href="javascript:;"><i></i><strong>短 信</strong></a></li>
								</c:if>
								<c:if test="${yto:getCookie('userType') == 1
									|| yto:getCookie('userType') == 11
				       				|| yto:getCookie('userType') == 12
				        			|| yto:getCookie('userType') == 13
						    		|| yto:getCookie('userType') == 2
						    		|| yto:getCookie('userType') == 22
						    		|| yto:getCookie('userType') == 23
						    		|| yto:getCookie('userType') == 4}">
								   <li menuFlag="caiwu" id="menu_c" data-menu="menu_c"><a href="javascript:;"><i></i><strong>财 务</strong></a></li>
								</c:if>
								<c:if test="${yto:getCookie('userType') == 2}">
								   <li menuFlag="dzmd" id="menu_i" data-menu="menu_dzmd"><a href="javascript:;"><i></i><strong>电子面单</strong></a></li>
								</c:if>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>				
				<!-- S 首页子菜单 -->
				<div class="menu_level_2 menu_a" style="display:none;">
					<i class="menu_arrow"></i>
					<ul>
						<c:choose>
							<c:when test="${yto:getCookie('userType') == 3}">
								<li menuFlag="home_home"><a href="mainPage_home.action?menuFlag=home_home" title="我的易通">我的易通</a></li>
								<li menuFlag="home_msg_index"><a href="message_index.action?menuFlag=home_msg_index" title="查看消息">查看消息</a></li>
								<li menuFlag="home_article_list"><a href="searchArticle.action?article.columnId=1&currentPage=1&menuFlag=home_article_list" title="栏目文章">栏目文章</a></li>
								<li menuFlag="home_app_list"><a href="searchApp .action?currentPage=1&menuFlag=home_app_list" title="应用审核">应用审核</a></li>
							    <li menuFlag="home_account_list"><a href="accountAdmin_index.action?currentPage=1&menuFlag=home_account_list" title="账户管理">账户管理</a></li>
				                <li menuFlag="home_payment_list"><a href="paymentAdmin_index.action?currentPage=1&menuFlag=home_payment_list" title="收支明细">收支明细</a></li>
				                <li menuFlag="home_service_list"><a href="serviceAdmin_index.action?currentPage=1&menuFlag=home_service_list" title="服务管理">服务管理</a></li>		
				                <li menuFlag="home_admin_upload"><a href="admin_upload.action?menuFlag=home_admin_upload" title="admin上传">admin上传</a></li>	
				                <li menuFlag="sjzh_sjzh"><a href="sSellerInfo.action?menuFlag=sjzh_sjzh" title="商家账号">商家信息管理</a></li>			
							</c:when>
							<c:otherwise>
								<li menuFlag="home_home"><a href="mainPage_home.action?menuFlag=home_home" title="我的易通">我的易通</a></li>
								<c:if test="${yto:getCookie('userType') == 2 
		 	        				|| yto:getCookie('userType') == 21 
		 	        				|| yto:getCookie('userType') == 22 
		 	        				|| yto:getCookie('userType') == 23}">
									<li menuFlag="home_orderimport"><a href="orderImport_toOrderImoprt.action?menuFlag=home_orderimport&flag=1" title="订单导入">订单导入</a></li>
								</c:if>
								<c:if test="${yto:getCookie('userType') == 1
			        				|| yto:getCookie('userType') == 11
			        				|| yto:getCookie('userType') == 13
					     			|| yto:getCookie('userType') == 2 
					   				|| yto:getCookie('userType') == 21
					   				|| yto:getCookie('userType') == 23
					   				|| yto:getCookie('userType') == 5}">
									<li menuFlag="home_question"><a href="questionnaire_index.action?menuFlag=home_question" title="问题件管理">问题件管理</a></li>
								</c:if>
								<c:if test="${yto:getCookie('switchEccount') == 0 &&(yto:getCookie('userType') == 12 || yto:getCookie('userType') == 22)}">
									<li menuFlag="home_ecAccount"><a href="order!toECAccount.action?currentPage=1&menuFlag=home_ecAccount" title="电子对账">电子对账</a></li>
								</c:if>
								<c:if test="${yto:getCookie('userType') == 1
		        					|| yto:getCookie('userType') == 11
		        					|| yto:getCookie('userType') == 12
		        					|| yto:getCookie('userType') == 13
				     				|| yto:getCookie('userType') == 2 
				   					|| yto:getCookie('userType') == 21
				   					|| yto:getCookie('userType') == 22
				   					|| yto:getCookie('userType') == 23
				   					|| yto:getCookie('userType') == 4
				   					|| yto:getCookie('userType') == 41}">
									<li menuFlag="home_waybill"><a href="waybill_bill.action?menuFlag=home_waybill" title="智能查件">智能查件</a></li>
								</c:if>
								<li menuFlag="home_branchlist"><a href="branchlist.action?menuFlag=home_branchlist" title="派送范围">派送范围</a></li>
								<c:if test="${yto:getCookie('userType') == 1 
								    || yto:getCookie('userType') == 11
								    || yto:getCookie('userType') == 12
								    || yto:getCookie('userType') == 13
						  			|| yto:getCookie('userType') == 4
						  			|| yto:getCookie('userType') == 5}">
									<li menuFlag="home_payService"><a href="payService_index.action?menuFlag=home_payService" title="服务管理">服务管理</a></li>
								</c:if>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
				<!-- E 首页子菜单 -->
				
				<!-- S 查件子菜单 -->
				<c:if test="${yto:getCookie('userType') == 1
        			|| yto:getCookie('userType') == 11
        			|| yto:getCookie('userType') == 13
		     		|| yto:getCookie('userType') == 2 
		   			|| yto:getCookie('userType') == 21
		   			|| yto:getCookie('userType') == 23
		   			|| yto:getCookie('userType') == 4
		   			|| yto:getCookie('userType') == 41}">
					<div class="menu_level_2 menu_b" style="display:none;">
						<i class="menu_arrow"></i>
						<ul>
							<li menuFlag="chajian_waybill"><a href="waybill_bill.action?menuFlag=chajian_waybill" title="智能查件">智能查件</a></li>
							<c:if test="${yto:getCookie('userType') == 1 ||
							  	yto:getCookie('userType') == 11 ||
							  	yto:getCookie('userType') == 13 ||
							 	yto:getCookie('userType') == 2 ||
							 	yto:getCookie('userType') == 21 ||
							 	yto:getCookie('userType') == 23}">
							<li menuFlag="chajian_question"><a href="questionnaire_index.action?menuFlag=chajian_question" title="问题件管理">问题件管理</a></li>
							</c:if>
							<c:if test="${yto:getCookie('userType') == 1 ||
							  	yto:getCookie('userType') == 11 ||
							  	yto:getCookie('userType') == 13 ||
							 	yto:getCookie('userType') == 2 ||
							 	yto:getCookie('userType') == 21 ||
							 	yto:getCookie('userType') == 23 ||
							 	yto:getCookie('userType') == 41 ||
							 	yto:getCookie('userType') == 4}">
								<li menuFlag="chajian_monitor_index"><a href="monitor_index.action?menuFlag=chajian_monitor_index" title="运单监控">运单监控</a></li>
							</c:if>						
							<c:if test="${(yto:getCookie('userType') == 1) ||
								yto:getCookie('userType') == 11 ||
								yto:getCookie('userType') == 12 ||
								yto:getCookie('userType') == 13 ||
								yto:getCookie('userType') == 4 ||
								yto:getCookie('userType') == 41}">
								<li menuFlag="chajian_passManage_warn"><a href="passManage_warnningIndex.action?menuFlag=chajian_passManage_warn" title="时效提醒">时效提醒</a></li>
							</c:if>
							<c:if test="${(yto:getCookie('userType') == 2 && str:isEmpty(yto:getCookie('parentId')) )||
								yto:getCookie('userType') == 21 ||
								yto:getCookie('userType') == 22 ||
								yto:getCookie('userType') == 23}">
								<li menuFlag="chajian_passManage_list"><a href="passManage_list_site.action?flag=0&menuFlag=chajian_passManage_list" title="时效提醒">时效提醒</a></li>
							</c:if>
							<c:if test="${yto:getCookie('userType') == 1
		        				|| yto:getCookie('userType') == 11
		        				|| yto:getCookie('userType') == 13
		        				|| yto:getCookie('userType') == 2 
							 	|| yto:getCookie('userType') == 21
							 	|| yto:getCookie('userType') == 23}">
								<li menuFlag="chajian_attention"><a href="monitor_attentionlist.action?currentPage=1&tabFlag=2&menuFlag=chajian_attention" title="我的关注">我的关注</a></li>
							</c:if>
						</ul>
					</div>
				</c:if>
				<!-- E 查件子菜单 -->
				
				<!-- S 财务子菜单 -->				
				<div class="menu_level_2 menu_c" style="display:none;">
					<i class="menu_arrow"></i>
					<ul >
					   <c:if test="${yto:getCookie('userType') == 1
				       		|| yto:getCookie('userType') == 12
				        	|| yto:getCookie('userType') == 13
						    || yto:getCookie('userType') == 2
						    || yto:getCookie('userType') == 22
						    || yto:getCookie('userType') == 23
						    || yto:getCookie('userType') == 4}">
							<c:if test="${yto:getCookie('switchEccount') == 0}">
								<li menuFlag="caiwu_ecAccount"><a href="order!toECAccount.action?currentPage=1&menuFlag=caiwu_ecAccount" title="电子对账" style="width:120px;">电子对账</a></li>
								<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
									<li menuFlag="caiwu_mjfreight1"><a href="mjfreight!mjunlikefreight.action?menuFlag=caiwu_mjfreight1" title="特殊账单" style="width:120px;">特殊账单</a></li>
								</c:if>
							</c:if>
							<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
								<li menuFlag="caiwu_posttemp" ><a href="posttemp!toPosttemp.action?currentPage=1&menuFlag=caiwu_posttemp" style="width:120px;">运费模板</a></li>
								<li menuFlag="caiwu_freight2" ><a href="freight!unlikefreight.action?menuFlag=caiwu_freight2" style="width:120px;">运费调整</a></li>
				        	</c:if>
				        	<c:if test="${yto:getCookie('userType') == 4}">
								<li menuFlag="caiwu_posttemp"><a href="posttemp!toPosttemp.action?currentPage=1&menuFlag=caiwu_posttemp" style="width:120px;">运费模板</a></li>
								<li menuFlag="caiwu_mjfreight4"><a href="mjfreight!mjunlikefreight.action?menuFlag=caiwu_mjfreight4" style="width:120px;">特殊账单</a></li>
				        	</c:if>
				        </c:if>
				        <!-- 
					        <li menuFlag="caiwu_rechargeHelp"><a href="payService_rechargeHelp.action?menuFlag=caiwu_rechargeHelp" title="支付帮助">支付帮助</a></li>
						 -->						
						 <c:if test="${yto:getCookie('userType') == 1
				        	|| yto:getCookie('userType') == 11 
				        	|| yto:getCookie('userType') == 12
				        	|| yto:getCookie('userType') == 13
				            || yto:getCookie('userType') == 4
				            || yto:getCookie('userType') == 5}">
				            <li menuFlag="caiwu_alipay"><a href="alipay_toRechargeOnline.action?menuFlag=caiwu_alipay" title="在线充值" style="width:120px;">在线充值</a></li>			           
				            <li menuFlag="caiwu_paymentList"><a href="payService_getPaymentList.action?menuFlag=caiwu_paymentList" title="收支明细" style="width:120px;">收支明细</a></li>
				            <li menuFlag="caiwu_codpay" ><a href="codBillPay_toBillDetailSearch.action?menuFlag=caiwu_codpay" style="width:120px;">代收货款未确认明细</a></li>
							<li menuFlag="caiwu_codsearch" ><a href="codBill_toCompositiveSearch.action?menuFlag=caiwu_codsearch" style="width:120px;">代收货款综合查询</a></li>
						</c:if>
					</ul>
				</div>
				
				<!-- E 财务子菜单 -->
				<!-- S 电子面单子菜单 -->				
				<div class="menu_level_2 menu_dzmd" style="display:none;">
					<i class="menu_arrow"></i>
					<ul>
					   <c:if test="${yto:getCookie('userType') == 2}">
						<li menuFlag="dzmd_dzmd"><a href="firstLoading.action?currentPage=1&menuFlag=dzmd_dzmd" title="面单号详情">面单号详情</a></li>
				        </c:if>
					</ul>
				</div>
				
				<!-- E 电子面单子菜单 -->
				
				<!-- S 发货子菜单 -->
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
					<div class="menu_level_2 menu_e" style="display:none;">
						<i class="menu_arrow"></i>
						<ul>
							<c:if test="${yto:getCookie('userType') == 1 
		        				|| yto:getCookie('userType') == 11 
		        				|| yto:getCookie('userType') == 12 
	 	        				|| yto:getCookie('userType') == 13 
	 	        				|| yto:getCookie('userType') == 2 
	 	        				|| yto:getCookie('userType') == 21 
	 	        				|| yto:getCookie('userType') == 22 
	 	        				|| yto:getCookie('userType') == 23 
	 	        				|| yto:getCookie('userType') == 41}">
								<li menuFlag="fahuo_orderPrint"><a href="orderPrint!orderPrint.action?menuFlag=fahuo_orderPrint" title="面单打印">面单打印</a></li>
								<li menuFlag="fahuo_orderExpress"><a href="waybill_orderExpress.action?menuFlag=fahuo_orderExpress" title="面单模板">面单模板</a></li>
							</c:if>
							<li menuFlag="fahuo_branchlist"><a href="branchlist.action?menuFlag=fahuo_branchlist" title="派送范围">派送范围</a></li>
							<c:if test="${yto:getCookie('userType') == 1
			        			|| yto:getCookie('userType') == 11 
		        				|| yto:getCookie('userType') == 12 
	 	        				|| yto:getCookie('userType') == 13 
	 	        				|| yto:getCookie('userType') == 2 
	 	        				|| yto:getCookie('userType') == 21 
	 	        				|| yto:getCookie('userType') == 22 
	 	        				|| yto:getCookie('userType') == 23}">
								<li menuFlag="fahuo_orderimport"><a href="orderImport_toOrderImoprt.action?menuFlag=fahuo_orderimport&flag=1" title="订单导入">订单导入</a></li>
							</c:if>
							<c:if test="${yto:getCookie('userType') == 1
		        				|| yto:getCookie('userType') == 11
		        				|| yto:getCookie('userType') == 12
		        				|| yto:getCookie('userType') == 13}">
								<li menuFlag="fahuo_orderPlace"><a href="orderPlace!toOrderCreate.action?menuFlag=fahuo_orderPlace" title="我要发货">我要发货</a></li>
							</c:if>
						</ul>
					</div>
				</c:if>
				<!-- E 发货子菜单 -->
				
				<!-- S 短信子菜单 -->
				<c:if test="${yto:getCookie('userType') == 1 ||
					yto:getCookie('userType') == 11 ||
					yto:getCookie('userType') == 12 ||
					yto:getCookie('userType') == 13 ||
					yto:getCookie('userType') == 4  ||
					yto:getCookie('userType') == 41 }">
					<div class="menu_level_2 menu_d" style="display:none;">
						<i class="menu_arrow"></i>
						<ul>
							<li menuFlag="sms_home"><a href="smsHomeEvent_homePage.action?menuFlag=sms_home" title="短信服务">短信服务</a></li>
							<li menuFlag="sms_template_list"><a href="template_list.action?menuFlag=sms_template_list" title="短信模板">短信模板</a></li>
							<li menuFlag="sms_info"><a href="smsSearch!searchPage.action?menuFlag=sms_info" title="短信查询">短信查询</a></li>
							<li menuFlag="sms_package"><a href="smsServiceMarket_inBuyPorts.action?menuFlag=sms_package" title="购买短信">购买短信</a></li>
						</ul>
					</div>
				</c:if>
				<!-- E 短信子菜单 -->
				
				<!-- S Admin配置子菜单 -->
				<c:if test="${yto:getCookie('userType') == 3}">
					<div class="menu_level_2 menu_b" style="display:none;">
						<i class="menu_arrow"></i>
						<ul>
							<li menuFlag="peizhi_channel_list"><a href="channel_list.action?currentPage=1&menuFlag=peizhi_channel_list" title="渠道消息管理">渠道消息管理</a></li>
							<li menuFlag="peizhi_config_index"><a href="config_index.action?currentPage=1&menuFlag=peizhi_config_index" title="配置消息管理">配置消息管理</a></li>
							<li menuFlag="peizhi_changing_passwords"><a href="modify_passwdPage.action?currentPage=1&menuFlag=peizhi_changing_passwords" title="重置密码">重置密码</a></li>
							<li menuFlag="peizhi_timerlog_list"><a href="timelog_list.action?currentPage=1&menuFlag=peizhi_timerlog_list" title="TimeLog">TimeLog</a></li>
							<li menuFlag="peizhi_ordercommand_list"><a href="ordercommand_list.action?currentPage=1&menuFlag=peizhi_ordercommand_list" title="Command">Command</a></li>
						</ul>
					</div>
				</c:if>
				<!-- E Admin配置子菜单 -->

				<!-- S Admin会员管理子菜单 -->
				<c:if test="${yto:getCookie('userType') == 3}">
					<div class="menu_level_2 menu_c" style="display:none;">
						<i class="menu_arrow"></i>
						<ul>
							<li menuFlag="huiyuan_eopUser_list"><a href="eopUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_eopUser_list" title="EOP会员管理">EOP会员管理</a></li>
							<li menuFlag="huiyuan_ytoUser_list"><a href="ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_ytoUser_list" title="易通会员管理">易通会员管理</a></li>
							<li menuFlag="huiyuan_dataInit"><a href="ytoUser!init.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_dataInit" title="数据初始化">数据初始化</a></li>
						</ul>
					</div>
				</c:if>
				<!-- E Admin会员管理子菜单 -->
				
				<!-- S Admin短信子菜单 -->
				<c:if test="${yto:getCookie('userType') == 3}">
					<div class="menu_level_2 menu_d" style="display:none;">
						<i class="menu_arrow"></i>
						<ul>
							<li><a menuFlag="sms_search_admin" href="smsSearchAdmin_smsSearchAdmin.action?menuFlag=sms_search_admin&noQuery=1" title="短信查询">短信查询</a></li>
							<li><a menuFlag="sms_templateAdmin" href="template_templateAdmin.action?menuFlag=sms_templateAdmin" title="模版审核">模版管理</a></li>
							<li><a menuFlag="sms_filterWords_list" href="filterWords_list.action?menuFlag=sms_filterWords_list" title="过滤词管理">过滤词管理</a></li>
						</ul>
					</div>
				</c:if>
				<!-- E Admin短信子菜单 -->
			</div>
			<!-- S 菜单 -->
			
			<div id="main_nav_r">
				<div id="nav_profile">
					<c:choose>
						<c:when test="${yto:getCookie('userType') == 2 && str:isNotEmpty(yto:getCookie('userNameText'))}">
					<em title="${yto:getCookie('userNameText')}">
						</c:when>
						<c:otherwise>
					<em title="${yto:getCookie('userNameText')}">
						</c:otherwise>
					</c:choose>
					<c:choose>
					 	<c:when test="${str:isNotEmpty(yto:getCookie('userNameText')) && yto:getCookie('userType') != 2}">
					 		<c:if test="${fn:length(yto:getCookie('userNameText')) <= 8}">
					 			${yto:getCookie("userNameText")}
					 		</c:if>
					 		<c:if test="${fn:length(yto:getCookie('userNameText')) >8}">
					 			${str:multiSubStr(yto:getCookie('userNameText'),14)}
	 						</c:if>
					 	</c:when>
					 	<c:otherwise>
					 		${yto:getCookie("userName")}
					 	</c:otherwise>
					 </c:choose>
					</em>
					<p>
						<a href="user!toEdit.action?user.id=${yto:getCookie('id')}&menuFlag=user_myacc" title="设置" id="profile_set">设置</a>
						<a href="javascript:;" title="退出" id="user_exit">退出</a>
					</p>
				</div>
				<div id="nav_notice">
					<img class="nav_notice_icon" src="${imagesPath}/single/alarm_icon.png" alt="更新新问题件" title="更新新问题件" />
					<a href="javascript:;" id="unReadQuestionNum" class="nav_num" title="问题件">${unReadQuestionNum}</a>
				</div>
				<div id="nav_msg">
					<img class="nav_notice_icon" src="${imagesPath}/single/mail_icon.png" alt="更新新消息" title="更新新消息" />
					<a href="javascript:;" id="unReadMessageNum" class="nav_num" title="消息">${unReadMessageNum}</a>
				</div>
			</div>
		</div>
		
		<div id="sub_nav">
			<ul class="clearfix">
				<!-- 首页子菜单s -->
				<c:if test="${str:startsWith(menuFlag,'home') || str:isEmpty(menuFlag) || 
					!(str:startsWith(menuFlag,'chajian_') || str:startsWith(menuFlag,'caiwu_') ||
					 str:startsWith(menuFlag,'fahuo_') || str:startsWith(menuFlag,'sms_') || 
					 str:startsWith(menuFlag,'peizhi_') || str:startsWith(menuFlag,'huiyuan_') ||
					 str:startsWith(menuFlag,'user_') || str:startsWith(menuFlag,'msg_') ||
					 str:startsWith(menuFlag,'dzmd_'))}">
					
					<c:choose>
							<c:when test="${yto:getCookie('userType') == 3}">
								<c:choose>
									<c:when test="${str:startsWith(menuFlag,'home') || str:isEmpty(menuFlag)}">
										<li class="cur_sub_menu" menuFlag="home_home"><a href="mainPage_home.action?menuFlag=home_home" title="我的易通">我的易通</a></li>
									</c:when>
									<c:otherwise>
										<li menuFlag="home_home"><a href="mainPage_home.action?menuFlag=home_home" title="我的易通">我的易通</a></li>
									</c:otherwise>
								</c:choose>
								<li menuFlag="home_msg_index"><a href="message_index.action?menuFlag=home_msg_index" title="查看消息">查看消息</a></li>
								<li menuFlag="home_article_list"><a href="searchArticle.action?article.columnId=1&currentPage=1&menuFlag=home_article_list" title="栏目文章">栏目文章</a></li>
								<li menuFlag="home_app_list"><a href="searchApp.action?currentPage=1&menuFlag=home_app_list" title="应用审核">应用审核</a></li>
							    <li menuFlag="home_account_list"><a href="accountAdmin_index.action?currentPage=1&menuFlag=home_account_list" title="账户管理">账户管理</a></li>
				                <li menuFlag="home_payment_list"><a href="paymentAdmin_index.action?currentPage=1&menuFlag=home_payment_list" title="收支明细">收支明细</a></li>
				                <li menuFlag="home_service_list"><a href="serviceAdmin_index.action?currentPage=1&menuFlag=home_service_list" title="服务管理">服务管理</a></li>			
							    <li menuFlag="home_admin_upload"><a href="admin_upload.action?menuFlag=home_admin_upload" title="admin上传">admin上传</a></li>
							    <li menuFlag="sjzh_sjzh"><a href="sSellerInfo.action?menuFlag=sjzh_sjzh" title="商家信息管理">商家信息管理</a></li>					
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${str:startsWith(menuFlag,'home') || str:isEmpty(menuFlag)}">
										<li class="cur_sub_menu" menuFlag="home_home"><a href="mainPage_home.action?menuFlag=home_home" title="我的易通">我的易通</a></li>
									</c:when>
									<c:otherwise>
										<li menuFlag="home_home"><a href="mainPage_home.action?menuFlag=home_home" title="我的易通">我的易通</a></li>
									</c:otherwise>
								</c:choose>
								<c:if test="${yto:getCookie('userType') == 2 
		 	        				|| yto:getCookie('userType') == 21 
		 	        				|| yto:getCookie('userType') == 22 
		 	        				|| yto:getCookie('userType') == 23}">
								<li menuFlag="home_orderimport"><a href="orderImport_toOrderImoprt.action?menuFlag=home_orderimport&flag=1" title="订单导入">订单导入</a></li>
								</c:if>
								<c:if test="${yto:getCookie('userType') == 1
			        				|| yto:getCookie('userType') == 11
			        				|| yto:getCookie('userType') == 13
					     			|| yto:getCookie('userType') == 2 
					   				|| yto:getCookie('userType') == 21
					   				|| yto:getCookie('userType') == 23}">
									<li menuFlag="home_question"><a href="questionnaire_index.action?menuFlag=home_question" title="问题件管理">问题件管理</a></li>
								</c:if>
								<c:if test="${yto:getCookie('switchEccount') == 0 &&(yto:getCookie('userType') == 12 || yto:getCookie('userType') == 22)}">
								<li menuFlag="home_ecAccount"><a href="order!toECAccount.action?currentPage=1&menuFlag=home_ecAccount" title="电子对账">电子对账</a></li>
								</c:if>
								<c:if test="${yto:getCookie('userType') == 1
		        					|| yto:getCookie('userType') == 11
		        					|| yto:getCookie('userType') == 12
		        					|| yto:getCookie('userType') == 13
				     				|| yto:getCookie('userType') == 2 
				   					|| yto:getCookie('userType') == 21
				   					|| yto:getCookie('userType') == 22
				   					|| yto:getCookie('userType') == 23
				   					|| yto:getCookie('userType') == 4
				   					|| yto:getCookie('userType') == 41}">
									<li menuFlag="home_waybill"><a href="waybill_bill.action?menuFlag=home_waybill" title="智能查件">智能查件</a></li>
								</c:if>
								<li menuFlag="home_branchlist"><a href="branchlist.action?menuFlag=home_branchlist" title="派送范围">派送范围</a></li>
								<c:if test="${yto:getCookie('userType') == 1 
								    || yto:getCookie('userType') == 11
								    || yto:getCookie('userType') == 12
								    || yto:getCookie('userType') == 13
						  			|| yto:getCookie('userType') == 4
						  			|| yto:getCookie('userType') == 5}">
									<li menuFlag="home_payService"><a href="payService_index.action?menuFlag=home_payService" title="服务管理">服务管理</a></li>
								</c:if>
							</c:otherwise>
						</c:choose>
				</c:if>
				<!-- 首页子菜单E -->
				
				<!-- 查件子菜单S -->
				<c:if test="${str:startsWith(menuFlag,'chajian_') }">
					<c:if test="${yto:getCookie('userType') == 1
        				|| yto:getCookie('userType') == 11
        				|| yto:getCookie('userType') == 13
		     			|| yto:getCookie('userType') == 2 
		   				|| yto:getCookie('userType') == 21
		   				|| yto:getCookie('userType') == 23
		   				|| yto:getCookie('userType') == 4
		   				|| yto:getCookie('userType') == 41}">
						<li menuFlag="chajian_waybill"><a href="waybill_bill.action?menuFlag=chajian_waybill" title="智能查件">智能查件</a></li>
						<c:if test="${yto:getCookie('userType') == 1
        				|| yto:getCookie('userType') == 11
        				|| yto:getCookie('userType') == 13
		     			|| yto:getCookie('userType') == 2 
		   				|| yto:getCookie('userType') == 21
		   				|| yto:getCookie('userType') == 23}">
						<li menuFlag="chajian_question"><a href="questionnaire_index.action?menuFlag=chajian_question" title="问题件管理">问题件管理</a></li>
						</c:if>
						<li menuFlag="chajian_monitor_index"><a href="monitor_index.action?menuFlag=chajian_monitor_index" title="运单监控">运单监控</a></li>
						<c:if test="${yto:getCookie('userType') == 1
	        				|| yto:getCookie('userType') == 11
	        				|| yto:getCookie('userType') == 13
	        				|| yto:getCookie('userType') == 2 
						 	|| yto:getCookie('userType') == 21
						 	|| yto:getCookie('userType') == 23}">
							<li menuFlag="chajian_attention"><a href="monitor_attentionlist.action?currentPage=1&tabFlag=2&menuFlag=chajian_attention" title="我的关注">我的关注</a></li>
						</c:if>
					</c:if>
				
					<c:if test="${(yto:getCookie('userType') == 1) ||
							yto:getCookie('userType') == 11 ||
							yto:getCookie('userType') == 12 ||
							yto:getCookie('userType') == 13 ||
							yto:getCookie('userType') == 4 ||
							yto:getCookie('userType') == 41}">
						<li menuFlag="chajian_passManage_warn"><a  name="50"  href="passManage_warnningIndex.action?menuFlag=chajian_passManage_warn" title="时效提醒">时效提醒</a></li>
					</c:if>
					<c:if test="${(yto:getCookie('userType') == 2 && str:isEmpty(yto:getCookie('parentId')) )
						|| yto:getCookie('userType') == 21 
						|| yto:getCookie('userType') == 22 
						|| yto:getCookie('userType') == 23}">
						<li menuFlag="chajian_passManage_list"><a name="55"  href="passManage_list_site.action?flag=0&menuFlag=chajian_passManage_list" title="时效提醒">时效提醒</a></li>
					</c:if>
				</c:if>
				<!-- 查件子菜单E -->
				
				<!-- 财务子菜单S -->
				<c:if test="${str:startsWith(menuFlag,'caiwu_') }">
					<c:if test="${yto:getCookie('userType') == 1
        				|| (yto:getCookie('userType') == 12)
        				|| (yto:getCookie('userType') == 13)
		    			|| yto:getCookie('userType') == 2
		    			|| yto:getCookie('userType') == 22
		    			|| yto:getCookie('userType') == 23
		    			|| yto:getCookie('userType') == 4}">
		    			<c:if test="${yto:getCookie('switchEccount') == 0}">
							<li menuFlag="caiwu_ecAccount"><a href="order!toECAccount.action?currentPage=1&menuFlag=caiwu_ecAccount" title="电子对账">电子对账</a></li>
							<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
								<li menuFlag="caiwu_mjfreight1"><a href="mjfreight!mjunlikefreight.action?menuFlag=caiwu_mjfreight1" title="特殊账单">特殊账单</a></li>
<!-- 								<li menuFlag="caiwu_codpay" ><a href="codBillPay_toBillDetailSearch.action?menuFlag=caiwu_codpay">代收货款未确认明细</a></li> -->
<!-- 			        	        <li menuFlag="caiwu_codsearch" ><a href="codBill_toCompositiveSearch.action?menuFlag=caiwu_codsearch">代收货款代收货款综合查询</a></li> -->
							</c:if>
						</c:if>
						<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
							<li menuFlag="caiwu_posttemp" ><a href="posttemp!toPosttemp.action?currentPage=1&menuFlag=caiwu_posttemp">运费模板</a></li>
							<li menuFlag="caiwu_freight2" ><a href="freight!unlikefreight.action?menuFlag=caiwu_freight2">运费调整</a></li>
			        	</c:if>
			        	<c:if test="${yto:getCookie('userType') == 4}">
							<li menuFlag="caiwu_posttemp"><a href="posttemp!toPosttemp.action?currentPage=1&menuFlag=caiwu_posttemp">运费模板</a></li>
							<li menuFlag="caiwu_mjfreight4"><a href="mjfreight!mjunlikefreight.action?menuFlag=caiwu_mjfreight4">特殊账单</a></li>
<!-- 							<li menuFlag="caiwu_codpay" ><a href="codBillPay_toBillDetailSearch.action?menuFlag=caiwu_codpay">代收货款未确认明细</a></li> -->
<!-- 			        	    <li menuFlag="caiwu_codsearch" ><a href="codBill_toCompositiveSearch.action?menuFlag=caiwu_codsearch">代收货款综合查询</a></li> -->
			        	</c:if>

			        	
					</c:if>
					<!--  
				        <li menuFlag="caiwu_rechargeHelp"><a href="payService_rechargeHelp.action?menuFlag=caiwu_rechargeHelp" title="支付帮助">支付帮助</a></li>
					-->
					 <c:if test="${yto:getCookie('userType') == 1
				        	|| yto:getCookie('userType') == 11 
				        	|| yto:getCookie('userType') == 12
				        	|| yto:getCookie('userType') == 13	
				            || yto:getCookie('userType') == 4
				            || yto:getCookie('userType') == 5}">
				            <li menuFlag="caiwu_alipay"><a href="alipay_toRechargeOnline.action?menuFlag=caiwu_alipay" title="在线充值">在线充值</a></li>			           
				            <li menuFlag="caiwu_paymentList"><a href="payService_getPaymentList.action?menuFlag=caiwu_paymentList" title="收支明细">收支明细</a></li>
				            <li menuFlag="caiwu_codpay" ><a href="codBillPay_toBillDetailSearch.action?menuFlag=caiwu_codpay">代收货款未确认明细</a></li>
			        	    <li menuFlag="caiwu_codsearch" ><a href="codBill_toCompositiveSearch.action?menuFlag=caiwu_codsearch">代收货款综合查询</a></li>
					</c:if>
				</c:if>
				<!-- 财务子菜单E -->
				<!-- 面单子菜单S -->
				<c:if test="${str:startsWith(menuFlag,'dzmd_') }">
					<c:if test="${yto:getCookie('userType') == 2}">
					<li menuFlag="dzmd_dzmd"><a href="firstLoading.action?currentPage=1&menuFlag=dzmd_dzmd" title="面单号详情">面单号详情</a></li>
					</c:if>
				</c:if>
				<!-- 面单子菜单E -->
				
				<!-- 发货子菜单S -->
				<c:if test="${str:startsWith(menuFlag,'fahuo_') }">
					<c:if test="${yto:getCookie('userType') == 1 
	        			|| yto:getCookie('userType') == 11 
	        			|| yto:getCookie('userType') == 12 
 	        			|| yto:getCookie('userType') == 13 
 	        			|| yto:getCookie('userType') == 2 
 	        			|| yto:getCookie('userType') == 21 
 	        			|| yto:getCookie('userType') == 22 
 	        			|| yto:getCookie('userType') == 23 
 	        			|| yto:getCookie('userType') == 41}">
						<li menuFlag="fahuo_orderPrint"><a href="orderPrint!orderPrint.action?menuFlag=fahuo_orderPrint" title="面单打印">面单打印</a></li>
						<li menuFlag="fahuo_orderExpress"><a href="waybill_orderExpress.action?menuFlag=fahuo_orderExpress" title="面单模板">面单模板</a></li>
					</c:if>
					<li menuFlag="fahuo_branchlist"><a href="branchlist.action?menuFlag=fahuo_branchlist" title="派送范围">派送范围</a></li>
					<c:if test="${yto:getCookie('userType') == 1
		        		|| yto:getCookie('userType') == 11 
	        			|| yto:getCookie('userType') == 12 
 	        			|| yto:getCookie('userType') == 13 
 	        			|| yto:getCookie('userType') == 2 
 	        			|| yto:getCookie('userType') == 21 
 	        			|| yto:getCookie('userType') == 22 
 	        			|| yto:getCookie('userType') == 23}">
						<li menuFlag="fahuo_orderimport"><a href="orderImport_toOrderImoprt.action?menuFlag=fahuo_orderimport&flag=1" title="订单导入">订单导入</a></li>
					</c:if>
					<c:if test="${yto:getCookie('userType') == 1
	        			|| yto:getCookie('userType') == 11
	        			|| yto:getCookie('userType') == 12
	        			|| yto:getCookie('userType') == 13}">
						<li menuFlag="fahuo_orderPlace"><a href="orderPlace!toOrderCreate.action?menuFlag=fahuo_orderPlace" title="我要发货">我要发货</a></li>
					</c:if>
				</c:if>
				<!-- 发货子菜单E -->
				
				<!-- 短信子菜单S -->
				<c:if test="${yto:getCookie('userType') == 1 
					|| yto:getCookie('userType') == 11 
					|| yto:getCookie('userType') == 12 
					|| yto:getCookie('userType') == 13 
					|| yto:getCookie('userType') == 4 
					|| yto:getCookie('userType') == 41 }">
					<c:if test="${str:startsWith(menuFlag,'sms_') }">
						<li menuFlag="sms_home"><a href="smsHomeEvent_homePage.action?menuFlag=sms_home" title="短信服务">短信服务</a></li>
						<li menuFlag="sms_template_list"><a href="template_list.action?menuFlag=sms_template_list" title="短信模板">短信模板</a></li>
						<li menuFlag="sms_info"><a href="smsSearch!searchPage.action?menuFlag=sms_info" title="短信查询">短信查询</a></li>
						<li menuFlag="sms_package"><a href="smsServiceMarket_inBuyPorts.action?menuFlag=sms_package" title="购买短信">购买短信</a></li>
					</c:if>
				</c:if>
				<!-- 短信子菜单E -->
				
				<!-- 短信子菜单S -->
				<c:if test="${yto:getCookie('userType') == 3 }">
					<c:if test="${str:startsWith(menuFlag,'sms_') }">
						<li menuFlag="sms_search_admin"><a href="smsSearchAdmin_smsSearchAdmin.action?menuFlag=sms_search_admin&noQuery=1" title="短信查询">短信查询</a></li>
						<li menuFlag="sms_templateAdmin"><a href="template_templateAdmin.action?menuFlag=sms_templateAdmin" title="模版审核">模版管理</a></li>
						<li menuFlag="sms_filterWords_list"><a href="filterWords_list.action?menuFlag=sms_filterWords_list" title="过滤词管理">过滤词管理</a></li>
					</c:if>
				</c:if>
				<!-- 短信子菜单E -->
				
				<!-- 设置（用户中心）子菜单 -->
				<c:if test="${str:startsWith(menuFlag,'user_') }">
					<c:if test="${yto:getCookie('userType') == 1}">
					<li menuFlag="user_tobindAccount" ><a href="toBindedAccount.action?menuFlag=user_tobindAccount" title="多店铺管理">多店铺管理</a></li>
					</c:if>
					<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 1 || yto:getCookie('userType') == 4}">
				        <li menuFlag="user_sub_acc_list" >
					        <a href="user!toSubAccountList.action?menuFlag=user_sub_acc_list">
					        	<c:if test="${yto:getCookie('userType') == 4}">客服账号</c:if>
					        	<c:if test="${yto:getCookie('userType') != 4}">子账号管理</c:if>
					        </a>
				        </li>
				        <c:if test="${yto:getCookie('userType') == 2}">
				        <li menuFlag="user_my_custom" ><a href="user!list.action?type=0&menuFlag=user_my_custom" title="我的客户">我的客户</a></li>
				        </c:if>
			        </c:if>
			        <c:if test="${yto:getCookie('userType') == 4}">
			        	<li menuFlag="user_platf_acc_list" ><a href="user!toPlatformSubAccountList.action?menuFlag=user_platf_acc_list">业务账号</a></li>
			        </c:if>
			        <c:if test="${yto:getCookie('userType') == 1
		        		|| yto:getCookie('userType') == 11
		        		|| yto:getCookie('userType') == 13}">
			        	<li menuFlag="user_acc_custom" ><a href="associationAccount_toBindeAccountCustom.action?menuFlag=user_acc_custom">个性化配置</a></li>
			        </c:if>
			        
					<li menuFlag="user_myacc" ><a href="user!toEdit.action?user.id=${yto:getCookie('id')}&menuFlag=user_myacc" title="我的账号">我的账号</a></li>
					 <c:if test="${yto:getCookie('userType') == 1
					  || yto:getCookie('userType') == 4
					  || yto:getCookie('userType') == 5}">
					  <li menuFlag="user_payService"><a href="payService_index.action?menuFlag=user_payService" title="服务管理">服务管理</a></li>
					 </c:if>
				</c:if>
				
				<c:if test="${str:startsWith(menuFlag,'msg_') }">
					<li menuFlag="msg_index" ><a href="message_index.action?menuFlag=msg_index" title="查看消息">查看消息</a></li>
					<c:if test="${yto:getCookie('userType') != 3}">
						<li menuFlag="msg_advise" ><a href="send_openAdviseUI.action?menuFlag=msg_advise" title="建议意见">建议意见</a></li>
					</c:if>
				</c:if>
				
				<!-- admin配置子菜单S -->
				<c:if test="${str:startsWith(menuFlag,'peizhi_') }">
					<li menuFlag="peizhi_channel_list"><a href="channel_list.action?currentPage=1&menuFlag=peizhi_channel_list" title="渠道消息管理">渠道消息管理</a></li>
					<li menuFlag="peizhi_config_index"><a href="config_index.action?currentPage=1&menuFlag=peizhi_config_index" title="配置消息管理">配置消息管理</a></li>
					<li menuFlag="peizhi_changing_passwords"><a href="modify_passwdPage.action?currentPage=1&menuFlag=peizhi_changing_passwords" title="重置密码">重置密码</a></li>
					<li menuFlag="peizhi_timerlog_list"><a href="timelog_list.action?currentPage=1&menuFlag=peizhi_timerlog_list" title="TimeLog">TimeLog</a></li>
					<li menuFlag="peizhi_ordercommand_list"><a href="ordercommand_list.action?currentPage=1&menuFlag=peizhi_ordercommand_list" title="Command">Command</a></li>
				</c:if>
				<!-- admin配置子菜单E -->
				
				<!-- admin会员管理子菜单S -->
				<c:if test="${str:startsWith(menuFlag,'huiyuan_') }">
					<li menuFlag="huiyuan_eopUser_list"><a href="eopUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_eopUser_list" title="EOP会员管理">EOP会员管理</a></li>
					<li menuFlag="huiyuan_ytoUser_list"><a href="ytoUser!list.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_ytoUser_list" title="易通会员管理">易通会员管理</a></li>
					<li menuFlag="huiyuan_dataInit"><a href="ytoUser!init.action?currentPage=1&user.userType=-1&user.userState=-1&menuFlag=huiyuan_dataInit" title="数据初始化">数据初始化</a></li>
				</c:if>
				<!-- admin会员管理子菜单E -->
			</ul>
		</div>
	</div>
	<!-- E Nav -->
	
	<script type="text/javascript">
	//弹出二级菜单单击事件
	$('.menu_level_2 ul li').click(function(){
		var menuFlag = $(this).attr("menuFlag");
		var menu = menuFlag.substring(0,menuFlag.indexOf("_"));
		if(${str:isNotEmpty(menuFlag)} && !!$(".cur_menu")){
			$(".cur_menu").removeClass("cur_menu");
		}
		if(!!menu){
			$("#menu_level_1 ul li[menuFlag='"+menu+"']").addClass("cur_menu");
		}
	});
	
	//列表二级菜单单击事件
	$('#sub_nav ul li').click(function(){
		var menuFlag = $(this).attr("menuFlag");
		if(${str:isNotEmpty(menuFlag)} && !!$(".cur_sub_menu")){
			$(".cur_sub_menu").removeClass("cur_sub_menu");
		}
		$(this).addClass("cur_sub_menu");
	});
	
	if(${str:isNotEmpty(menuFlag)}){
		var menuFlag = "${menuFlag}";
		var menu = menuFlag.substring(0,menuFlag.indexOf("_"));
		
		//刷新页面后，一级菜单高亮样式
		$("#menu_level_1 ul li").each(function(){
			var elsMenuFlag = $(this).attr("menuFlag");
			if(elsMenuFlag == menu){
				if(!!$(".cur_menu")){
					$(".cur_menu").removeClass("cur_menu");
				}
				$(this).addClass("cur_menu");
			}
		});
		
		//刷新页面后，列表二级菜单高亮样式
		$('#sub_nav ul li').each(function(){
			var elsMenuFlag = $(this).attr("menuFlag");
			if(elsMenuFlag == menuFlag){
				if(!!$(".cur_sub_menu")){
					$(".cur_sub_menu").removeClass("cur_sub_menu");
				}
				$(this).addClass("cur_sub_menu");
			}
		});
	}
		
	
</script>