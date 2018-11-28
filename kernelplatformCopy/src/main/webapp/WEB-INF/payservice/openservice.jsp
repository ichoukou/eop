<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/open_service.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">
				<s:if test='paraMap.flag == "1"'>续费</s:if>
				<s:else>
				  开通服务
				</s:else>
				</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<form action="payService_openService.action?menuFlag=payService" class="form" id="serve_form" target="_blank">
			            
				                <!-- S 说明 -->
								<div class="detail_box" id="detail_box_a">
									<label class="p_label">说明：</label>
									<!-- 短信服务 -->
									 <s:if test="paraMap.serviceid == 5">
									   <span class="icon_desc" id="icon_desc_b">		
										    <i></i>								
											<strong><s:property value="paraMap.serviceName"/></strong><a href="javascript:;" title="了解详情" class="btn btn_a" id="knowDetail"><span>了解详情</span></a>																					
											<span class="desc_p"><s:property value="paraMap.remark"/></span>
										</span>
									 </s:if>
									 <!-- 问题件预警 -->
									 <s:elseif test="paraMap.serviceid == 6">
										<span class="icon_desc" id="icon_desc_a">		
										    <i></i>								
											<strong><s:property value="paraMap.serviceName"/></strong><a href="javascript:;" title="了解详情" class="btn btn_a" id="knowDetail"><span>了解详情</span></a>																					
											<span class="desc_p"><s:property value="paraMap.remark"/></span>
										</span>
									 </s:elseif>									 
									 <!-- 超时件服务 -->
									  <s:elseif test="paraMap.serviceid == 7">
										 <span class="icon_desc" id="icon_desc_c">	
										    <i></i>										
											<strong><s:property value="paraMap.serviceName"/></strong><a href="javascript:;" title="了解详情" class="btn btn_a" id="knowDetail"><span>了解详情</span></a>
											<span class="desc_p"><s:property value="paraMap.remark"/></span>
										 </span>
									 </s:elseif>
									 <s:else>
									   <span class="icon_desc" id="icon_desc_c">	
										    <i></i>										
											<strong><s:property value="paraMap.serviceName"/></strong><a href="javascript:;" title="了解详情" class="btn btn_a" id="knowDetail"><span>了解详情</span></a>
											<span class="desc_p"><s:property value="paraMap.remark"/></span>
										 </span>
									 </s:else>								 
								</div>
								<!-- E 说明 -->
								<s:if test="paraMap.serviceid != 5">
								<!-- S 期限 -->
								<div class="detail_box" id="detail_box_b">
									<label class="p_label">期限：</label>
									<input type="hidden" value="<s:property value="paraMap.useBalance"/>" id="balance" />
									<ul>
										<li>
										  
											<input type="radio" class="input_radio" name="circle" value="0" id="radio_month" <s:if test='paraMap.circle == "0" '> checked</s:if>/>
											<label for="radio_month" class="clear_label">一个月[<fmt:formatNumber value="${ paraMap.unitPrice}" type="number" pattern="#,##0.00#" />元]</label>
												<span class="date_range">
													<s:property value="paraMap.now"/> 至 <s:property value="paraMap.month"/>
												</span>
											<input type="hidden" class="money" value="<s:property value="paraMap.unitPrice"/>" />
											<input type="hidden" class="time" value="一个月（<s:property value='paraMap.now'/> 至 <s:property value='paraMap.month'/>）" />
										</li>
										<li>
											<input type="radio" class="input_radio" name="circle" value="1" id="radio_season" <s:if test='paraMap.circle == "1" '> checked</s:if> />
											<label for="radio_season" class="clear_label">一季度[<fmt:formatNumber value="${ paraMap.unitPrice*3-10}" type="number" pattern="#,##0.00#" />元]</label>
												<span class="date_range">
												    <s:property value="paraMap.now"/> 至 <s:property value="paraMap.season"/>
												</span>
											<input type="hidden" class="money" value="<s:property value="paraMap.unitPrice*3-10"/>" />
											<input type="hidden" class="time" value="一季度（<s:property value='paraMap.now'/> 至 <s:property value='paraMap.season'/>）" />
										</li>
										<li>
											<input type="radio" class="input_radio" name="circle" value="2" id="radio_half" <s:if test='paraMap.circle == "2" '> checked</s:if> />
											<label for="radio_half" class="clear_label">半年[<fmt:formatNumber value="${ paraMap.unitPrice*6-40}" type="number" pattern="#,##0.00#" />元]</label>
												<span class="date_range">
												      <s:property value="paraMap.now"/> 至 <s:property value="paraMap.half"/>
												</span>
											<input type="hidden" class="money" value="<s:property value="paraMap.unitPrice*6-40"/>" />
											<input type="hidden" class="time" value="半年（<s:property value='paraMap.now'/> 至 <s:property value='paraMap.half'/>）" />
										</li>
										<li>
											<input type="radio" class="input_radio" name="circle" value="3" id="radio_year" <s:if test='paraMap.circle == "3" '> checked</s:if><s:elseif test="paraMap.circle == null || paraMap.circle == ''">checked</s:elseif>/>
											<label for="radio_year" class="clear_label">一年[<fmt:formatNumber value="${ paraMap.unitPrice*12-100}" type="number" pattern="#,##0.00#" />元<span style="padding-left:5px;color:red">首次开通免费送三个月</span>]</label>										 
												<span class="date_range">
												    <s:property value="paraMap.now"/> 至 <s:property value="paraMap.year"/>
												</span>
											<input type="hidden" class="money" value="<s:property value="paraMap.unitPrice*12-100"/>" />
											<input type="hidden" class="time" value="一年（<s:property value='paraMap.now'/> 至 <s:property value='paraMap.year'/>）" />
											
										</li>
									</ul>
								</div>
								<!-- E 期限 -->
			             	</s:if>
							 <!-- S 使用明细 -->
							 <div class="detail_box" id="detail_box_c">
									<label class="p_label">使用明细：</label>
									<span class="detail_info">名称：<em><s:property value="paraMap.serviceName"/></em></span>
									<input type="hidden" value="<s:property value="paraMap.serviceName"/>" name="dealName" id="serviceName">
								    <input type="hidden" value="<s:property value="paraMap.shopName"/>" name="shopName" id="shopName">
									<span class="detail_info">使用者：
										<em>
										    <s:if test="payList.size() > 0">
										      <s:iterator value="payList" var="shopName">
											    <s:property value="shopName"/>
											   </s:iterator>
											</s:if>	
											<s:else>
											  <s:property value="paraMap.shopName"/>
											</s:else>									
										</em>
									</span>
									<s:if test="paraMap.serviceid != 5">
									   <span class="detail_info" id="period_use">使用期限：
									    <s:if test='paraMap.circle == null || paraMap.circle==""'>
								    	     <em> 一年(<s:property value='paraMap.now'/> 至 <s:property value='paraMap.year'/>)</em>
								    	</s:if>
								    	<s:else>
										     <s:if test='paraMap.circle == "0" '>
										         <em>一个月(<s:property value='paraMap.now'/> 至 <s:property value='paraMap.month'/>)</em>
										     </s:if>
										      <s:if test='paraMap.circle == "1" '>
										         <em>一季度(<s:property value='paraMap.now'/> 至 <s:property value='paraMap.season'/>)</em>
										     </s:if>
										      <s:if test='paraMap.circle == "2" '>
										         <em> 半年(<s:property value='paraMap.now'/> 至 <s:property value='paraMap.half'/>)</em>
										     </s:if>
										      <s:if test='paraMap.circle == "3" '>
										         <em> 一年(<s:property value='paraMap.now'/> 至 <s:property value='paraMap.year'/>)</em>
										     </s:if>	
									     </s:else>								     
									   </span>
									</s:if>
									<!-- 服务周期 -->
									<s:if test="paraMap.serviceid != 5">
									    <span class="detail_info">当前余额：<em><fmt:formatNumber value="${ paraMap.useBalance}" type="number" pattern="#,##0.00#" /></em> 元</span>
								    	<span class="detail_info" id="need_pay">应支付金额：<em>
								    	<s:if test='paraMap.circle == null || paraMap.circle==""'>
								    	  <fmt:formatNumber value="${ paraMap.unitPrice*12-100}" type="number" pattern="#,##0.00#" />
								    	</s:if>
								    	<s:else>
								    	  <s:if test='paraMap.circle == "0" '><fmt:formatNumber value="${ paraMap.unitPrice}" type="number" pattern="#,##0.00#" /></s:if>
								    	  <s:elseif test='paraMap.circle == "1"'><fmt:formatNumber value="${ paraMap.unitPrice*3-10}" type="number" pattern="#,##0.00#" /></s:elseif>
								    	  <s:elseif test='paraMap.circle == "2"'><fmt:formatNumber value="${ paraMap.unitPrice*6-40}" type="number" pattern="#,##0.00#" /></s:elseif>
								    	  <s:elseif test='paraMap.circle == "3"'><fmt:formatNumber value="${ paraMap.unitPrice*12-100}" type="number" pattern="#,##0.00#" /></s:elseif>
								    	</s:else>
								    	</em> 元</span>
									</s:if>
									<input type="hidden" value="50.00" name="money" id="money"/>
									<input type="hidden" value="<s:property value="paraMap.shopName"/>" id="user_name" />
									<input type="hidden" value="<s:property value="paraMap.serviceid"/>" id="serviceId" name="serviceId">
									<ul>
									  <s:if test="paraMap.serviceid != 5">
										<li>
											<input type="checkbox" id="auto_pay"/>
											<input type="hidden" id="isAutoRennew" value="" name="isAutoRenew">
											<label for="auto_pay" class="clear_label">自动续费</label>
										</li>
										</s:if>
										<li>
											<input type="checkbox" id="read_agree" checked />
											<label for="read_agree" class="clear_label">
											            我已阅读并同意 
											    <a href="javascript:;">易通平台自助服务协议</a>
											</label>
											<span id="read_agreeTip"></span>
										</li>
									</ul>
									
									<textarea id="agreement" readOnly="true">在订购使用易通服务平台任何服务前，请您仔细阅读下述许可协议条款。一旦您点击确认本协议，即表示您已接受了以下所述的条款和条件，同意受本协议约束。如果您不同意接受全部的条款和条件，那么您将无法订购服务。
一、本许可协议的缔约方为服务商（易通服务平台）和用户（“您”），本许可协议具有合同效力。本协议是缔约方就用户通过易通服务平台购买服务功能和相关服务（本协议中统称“服务”）的协议。
二、应用订购方式
2.1 用户需凭其根据用户名和密码访问易通服务平台，并订购使用服务。对于用户通过其他手段登录、使用所造成的后果，易通服务平台将不承担任何责任。
2.2 易通服务平台仅根据用户登录名和密码来确认使用服务的用户身份。用户应妥善保管用户登录名及密码，并对其使用及其遗失自行承担责任。
三、软件许可费
3.1 用户通过易通服务平台订购服务，应当按约定向易通支付软件许可费。
3.2 软件许可费公示于易通服务平台的服务订购页面。
3.3 易通服务平台可以向用户提供免费服务应用的使用许可。提供免费服务不应视为易通服务平台全部放弃服务收费的权利。
四、用户的权利和义务
4.1 用户保证其使用服务商应用的各项行为均符合国家法律法规的规定，其通过应用所从事的一切活动都是合法的、真实的，不侵害服务商和任何第三方的合法权益，包括但不限于：
1）禁止出售、转售或复制、开发易通服务平台授予的使用权限；
2）禁止复制和模仿易通服务平台的设计理念、界面、功能和图表；
3）禁止未经易通服务平台许可基于易通服务平台应用或其内容进行修改或制造派生其他产品；
4.2 用户不得对易通服务平台应用或易通服务平台应用任何部分（软件产品、页面标识、服务品牌、资讯、信息）进行复制、翻译、修改、适应、增强、反编译、反汇编、反向工 程、分解拆卸、出售、转租或作任何商业目的的使用。用户同意约束其有必要使用服务商应用的员工、代理、咨询者或顾问遵守前述之义务，并就其违反前述规定的 行为对易通服务平台负责就如同用户自身违反一样。
4.3 用户应按时足额向易通服务平台支付软件许可费用，否则易通服务平台保留随时终止用户关于易通服务平台应用使用许可的权利，并对因终止协议而可能造成的损害不承担任何责任。
4.4用户承诺通过服务商应用进行的活动所引发的一切法律后果，由用户承担全部责任。如因用户使用易通服务平台服务的行为，导致易通服务平台或任何第三方为此承担了相关的责任，则用户需全额赔偿服务商或任何第三方的相关支出及损失，包括合理的律师费用。
4.5 用户同意在使用易通服务平台服务的同时，接受易通服务平台提供的各类信息服务。
4.6 用户理解并同意，易通服务平台服务因现有技术限制，可能存在瑕疵，因使用易通服务平台服务所致的任何损害，或因网络问题或产品瑕疵导致易通服务平台服务无法按指定的时间正常使用的，则用户了解并同意，服务商不承担任何责任。
4.7 用户同意将易通服务平台服务用于其功能所指向的合法的目的和用途，并不进行任何竞争性、破坏性或非法的使用。
4.8 用户理解并同意，易通服务平台服务的唯一责任人为易通服务平台所有者。
五、易通服务平台的权利和义务
5.1 易通服务平台保证其有合法权利向用户提供易通服务平台服务的使用许可，并保证与易通服务平台服务运行相关的软件产品版权的合法性。

5.3 易通服务平台负责及时对应用进行更新、维护和管理。
5.4如因易通服务平台的原因，导致用户对于易通服务平台服务的使用许可需提前终止的，易通服务平台应对用户履行相应的退款义务。
六、用户数据安全
6.1 易通服务平台应在其网络系统内建立合理的安全体系，包括身份识别体系、内部安全防范体系，以使用户数据完整，并且保密。本协议项下用户数据尤其指用户基于服务商应用功能的使用而自行导入的与服务商应用功能相关的用户业务数据。
6.2 为服务用户的目的，服务可能通过使用用户数据，向用户提供服务，包括但不限于向用户发出产品和服务信息。
6.3 用户数据将在下述情况下部分或全部被披露：
1）经用户同意，向第三方披露；
2）根据法律的有关规定，或者行政或司法机构的要求，向第三方或者行政、司法机构 披露；
3）如果用户出现违反中国有关法律法规的情况，需要向第三方披露；
4）为提供用户所要求的软件或服务，而必须和第三方分享用户数据。

七、服务产品在线交易须知
7.1 用户理解并同意，对于服务产品的选择和交易的内容，应由用户自行判断，且审慎交易。用户需自行了解应用的适用性、功能、收费标准、退款规则、服务有效期等情况并注意交易风险。
7.2为保障交易安全，用户支付的软件许可费用将由易通服务平台指定的账户收取。

八、终止
8.1出现下列情况之一的，易通服务平台有权在提前三个工作日通知用户的情况下，终止用户使用服务商应用的权利，而无需承担任何责任：
1）用户在应用软件许可期限届满后未继续付费购买后续期间的软件许可的；
2）用户违反本协议有关保证、同意、承诺条款的约定，且自易通服务平台通知其纠正后仍未纠正的；
九、协议修改
9.1 如本许可协议的任何内容发生变动，易通服务平台应通过适当方式向用户提示修改内容。
9.2 如用户不同意易通服务平台对本许可协议相关条款所做的修改，用户有权停止使用易通服务平台服务。此等情况下，易通服务平台应向用户退回剩余许可费用（如有），并向用户提供其业务数据的下载服务。如用户继续使用易通服务平台应用，则视为用户接受易通服务平台对本许可协议相关条款所做的修改。
十、不可抗力
10.1如果由于战争、自然灾害、罢工以及黑客攻击或政府管制和网络通讯瘫痪等对其发生和后果不能预见的事件，易通服务平台和用户均确认此属不可抗力；在不可抗力下，用户同意易通服务平台及第三方无须对合同的延期履行、不能履行承担任何责任。
十一、法律及争议解决
11.1 本协议适用中华人民共和国法律。
11.2 因双方就本协议的签订、履行或解释发生争议，双方应努力友好协商解决。如协商不成，任何一方均应向服务商所在地人民法院起诉。
十二、其他
12.1 本协议构成用户和服务商间就购买、使用服务应用的完整的协议，并取代双方就有关本协议所载任何事项于先前以口头及书面达成的共识。
12.2 如本协议的任何条款被视作无效或无法执行，则上述条款可被分离，其余部分则仍具有法律效力。
12.3 本协议的标题仅为方便阅读所设，非对条款的定义、限制、解释或描述其范围或界限。
</textarea>
																	
									<div class="btn_box">	
									 <s:if test="paraMap.serviceid != 5">							
										<a href="javascript:;" title="付款" id="pay_bill" class="btn btn_a"><span>付款</span></a>
									 </s:if>
									 <s:else>
									   <input type="hidden" value="<s:property value="paraMap.smsFlag"/>" id="smsFlag"/>
									    <a href="javascript:;" title="立即开通" id="pay_bill" class="btn btn_a"><span>立即开通</span></a>
									 </s:else>
										<!-- 
										 <a href="payService_openService.action" title="付款" id="pay_bill" class="btn btn_a"><span>付 款</span></a>
										 -->										 
										 <input type="hidden" value="${source}" id="source" name="source"/>
										 <s:if test='paraMap.smsFlag == "1"'>
											 <a href="smsHomeEvent_homePage.action?menuFlag=sms_home" title="返回" class="btn btn_a"><span>返回</span></a>
										 </s:if>
										 <s:elseif test="source=='chajian_passManage_warn'">										    
											 <a href="passManage_warnningIndex.action?menuFlag=chajian_passManage_warn" title="返回" class="btn btn_a"><span>返回</span></a>
										 </s:elseif>	
										 <s:elseif test='paraMap.smsFlag =="2"'>
										   <a href="passManage_list_seller.action?menuFlag=chajian_passManage_warn" title="返回"  class="btn btn_a"><span>返回</span></a>
										 </s:elseif>																												 										 
										 <s:else>
										     <a href="payService_index.action?menuFlag=home_payService" title="返回" class="btn btn_a"><span>返回</span></a>
										 </s:else>										 									 
									</div>
								</div>
								<!-- E 使用明细 -->
						</form>
					</div>
				</div>
			</div>
			
			
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/open_service.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->

			
		</div>
		<!-- E Content -->
		
