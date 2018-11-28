<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/serve_home.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	<title>服务管理</title>
	
	<!--  -->
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">服务管理</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S 服务 -->
				<div id="l_side">
					<!-- S 在使用服务 -->
					<s:if test="dredgeList.size()>0">
					<div id="in_use" class="box box_a">
						<div class="box_hd">
							<strong>在使用服务</strong>
						</div>
						<div class="box_bd">
							<table>
							  <s:if test="dredgeList.size() > 0">
							    <s:iterator id="dredgeService" value = "dredgeList">
							     <s:if test="serviceId == 5">
								  <tr>
								    <!-- 名称 -->
								 	<td class="td_a">
										<em class="serve_name" id="serve_b"></em>
										<p><s:property value="ServiceName"/></p>
									</td>
									<td class="td_b">
									   <span class="serve_desc"><s:property value="remark"/></span>
									   
									</td>
									<td class="td_c">
										<fmt:formatDate value="${dredgeService.beginDate }" pattern="yyyy年MM月dd日 "/>开通
									</td>
									<td class="td_d">
										<span class="has_pop">
										    <input type="hidden" value="<s:property value="serviceId"/>" id="renewals_serviceId">
										</span>
										
										<!-- 父账号开通 -->
										<s:if test='openFromFlag == "1"'>
											<a href="" class="bta close_serve" title="关闭"><span>关闭</span></a>
										</s:if>
										<s:elseif test='openFromFlag == null || openFromFlag == ""'>
										    <a href="javascript:;" class="bta close_serve" title="关闭"><span>关闭</span></a>
										</s:elseif>
										<s:else>
										  	 <a href="javascript:;" class="bta close_serve" title="关闭"><span>关闭</span></a>
										</s:else>
										
										<input type="hidden" class="openFromFlag" value="<s:property value="openFromFlag"/>">
										<input type="hidden" class="serve_code" value="<s:property value="serviceId"/>" />
										<input type="hidden" class="serve_meta" value="简介:全自动物流提醒（发货提醒、派件提醒、签收提醒）为买家提供最及时的物流状态。营销活动通过发送营销短信，为店铺聚引大量流量。" />
									  <div style="float:right">
										    <input type="hidden" class="serve_id" value="<s:property value="serviceId"/>" />
											<a href="javascript:;" class="btn btn_a toService" title="立即使用"><span>立即使用</span></a>
									  </div>
									</td>
								  </tr> 
								 </s:if>
								 <s:else>
								    <tr>
								    <!-- 名称 -->
								 	<td class="td_a">
								 	  <s:if test="serviceId == 6">
										<em class="serve_name" id="serve_a"></em>
										<p><s:property value="ServiceName"/></p>
									  </s:if>
									  <s:elseif test="serviceId == 7">
									    <em class="serve_name" id="serve_c"></em>
									    <p><s:property value="ServiceName"/></p>
									  </s:elseif>
									  <s:else>
									     <em class="serve_name" id="serve_c"></em>
									     <p><s:property value="ServiceName"/></p>
									  </s:else>
									</td>
									<td class="td_b">
										<span class="serve_desc"><s:property value="remark"/></span>
										
									</td>
									<td class="td_c">
										<fmt:formatDate value="${dredgeService.beginDate }" pattern="yyyy年MM月dd日 "/>开通
										  还有<s:property value="days"/>天到期
									</td>
									<td class="td_d">
										
										<input type="hidden" class="serve_code" value="<s:property value="serviceId"/>" />
										<input type="hidden" class="openFromFlag" value="<s:property value="openFromFlag"/>">
										  <s:set var="result" value='"false"'/>
										 
											  <s:if test="payList != null">
													<s:iterator value="payList" var="dealName">
														<s:if test="#dealName == ServiceName">
														<div style="float:left;position:relative;left:-5px; line-height:1.0">
													 	  <a href="javascript:;" class="renewals" title="续费"><span>续费</span></a><br>
															<span class="pop_tip">
															  <input type="hidden" class="serve_code" value="<s:property value="serviceId"/>" />
																您已订购，请先<a href="javascript:;" class="payorclose">付款（或关闭）</a>！
																<a href="javascript:;" class="f_close"></a>
															</span>
															</div>
															<s:set var="result" value='"true"'/>
														</s:if>
												</s:iterator>
											<div style="float:left; line-height:1.0">
											    <s:if test="#result == 'false'">
											        <s:if test='openFromFlag == "1"'>
													  <a href="javascript:;" class="bta renewals" title="续费">续费</a><br />
													</s:if>
													<s:elseif test='openFromFlag == null || openFromFlag == ""'>
													 	 <a href="javascript:;" class="bta renewals" title="续费">续费</a><br />
													</s:elseif>
													<s:else>
													   <a href="javascript:;" class="bta renewals" title="续费">续费</a><br />
													</s:else>
											     </s:if>
										    </s:if>
											<s:else>
				                               <!-- 
												<a href="javascript:;" class="btn btn_a renewals" title="续费"><span>续费</span></a><br>
											  --->
											  <s:if test='openFromFlag == "1"'>
													  <a href="javascript:;" class="bta renewals" title="续费">续费</a><br />
													</s:if>
													<s:elseif test='openFromFlag == null || openFromFlag == ""'>
													 	<a href="javascript:;" class="bta renewals" title="续费">续费</a><br />
													</s:elseif>
													<s:else>
													   <a href="javascript:;" class="bta renewals" title="续费">续费</a><br />
													</s:else>
											</s:else>
										
										<input type="hidden" class="openFromFlag" value="<s:property value="openFromFlag"/>">
										<input type="hidden" class="serve_code" value="<s:property value="serviceId"/>" />
										<!-- 
										<a href="javascript:;" class="close_serve" title="关闭">关闭</a>
										-->
										<s:if test='openFromFlag == "1"'>
											<a href="" class="bta close_serve" title="关闭">关闭</a>
										</s:if>
										<s:elseif test='openFromFlag == null || openFromFlag == ""'>
										    <a href="javascript:;" class="bta close_serve" title="关闭">关闭</a>
										</s:elseif>
										<s:else>
										  	 <a href="javascript:;" class="bta close_serve" title="关闭">关闭</a>
										</s:else>
										<s:if test="serviceId == 6">
											<input type="hidden" class="serve_meta" value="简介:时效提醒协助您跟踪未按时签收的快件，出现延迟可立即知晓并联系网点处理问题。" />
										</s:if>
										<s:else>
										    <input type="hidden" class="serve_meta" value="暂无" />
										</s:else>
									</div>
									<div style="float:right;">
										    <input type="hidden" class="serve_id" value="<s:property value="serviceId"/>" />
											<a href="javascript:;" class="btn btn_a toService" title="立即使用"><span>立即使用</span></a>
									
									</td>
								  </tr>
								 </s:else>
								</s:iterator>
							</s:if>
							</table>
						</div>
					</div>
				</s:if>
					<!-- E 在使用服务 -->
					
					<!-- S 未使用服务 -->
					<s:if test="payServicesList.size() > 0">
					<div id="not_in_use" class="box box_a">
						<div class="box_hd">
							<strong>未使用服务</strong>
						</div>   
						<div class="box_bd">
							<table>
							   <s:if test="payServicesList.size() > 0">
							    <s:iterator id="payService" value="payServicesList">
							    <s:if test="id==5">
							      <!-- 这个是短信服务 -->
									  <tr>
									    <!-- 服务名称 -->
										<td class="td_a">
											<em class="serve_name" id="serve_b"></em>
											<p><s:property value="name"/></p>
										</td>
										<!-- 服务描述 -->
										<td class="td_b">
											<span class="serve_desc"><s:property value="remark"/></span>
										</td>
										<td class="td_c">
											<span class="has_pop">
											 
											     <input type="hidden" class="serve_code" value="<s:property value="id"/>" />
												<a href="javascript:;" class="btn btn_a use_now" title="立即开通" id="btn_service_a"><span>立即开通</span></a>
											 
											</span>
											<a href="javascript:;" id="detail1" class="btn btn_a get_detail" title="了解功能详情及价格" target=""><span>了解功能详情及价格</span></a>
										</td>
									  </tr> 
								 </s:if>
								 <s:else>
								    <tr>
									    <!-- 服务名称 -->
										<td class="td_a">
										    <input type="hidden" value="<s:property value="id"/>" id="service_b">
										    <s:if test="id == 6">
											  <em class="serve_name" id="serve_a"></em>
											  <p><s:property value="name"/></p>
											</s:if>
											<s:elseif test="id == 7">
											  <em class="serve_name" id="serve_c"></em>
											  <p><s:property value="name"/></p>
											</s:elseif>
											<s:else>
											   <em class="serve_name" id="serve_c"></em>
											   <p><s:property value="name"/></p>
											</s:else>
										</td>
										<!-- 服务描述 -->
										<td class="td_b">
											<span class="serve_desc"><s:property value="remark"/></span>
										</td>
										<td class="td_c"></td>
										<td class="td_d">
											<a href="javascript:;" id="detail2" class="get_detail" title="了解功能详情及价格" style="width:77px;display:inline-block;line-height:1">了解功能详情价格</a>
										
											<span class="has_pop">
											 <s:set var="result" value='"false"'/>
											  <s:if test="payList != null">
													<s:iterator value="payList" var="dealName">
														<s:if test="#dealName == name">
														 <input type="hidden" class="serve_code" value="<s:property value="id"/>" />
														 
													 	  <a href="javascript:;" class="btn btn_e use_now" title="立即开通"><span>立即开通</span></a>
															<span class="pop_tip" >
															  <input type="hidden" class="serve_code" value="<s:property value="id"/>" />
																您已订购，请先<a href="javascript:;" class="payorclose">付款（或关闭）</a>！
																<a href="javascript:;" class="f_close"></a>
															</span>
															
															<s:set var="result" value='"true"'/>
														</s:if>
												</s:iterator>
												
											    <s:if test="#result == 'false'">
											          <input type="hidden" class="serve_code" value="<s:property value="id"/>" />
													  <a href="javascript:;" class="btn btn_a use_now" title="立即开通"><span>立即开通</span></a>
											     </s:if>
										    </s:if>
											<s:else>
											    <input type="hidden" class="serve_code" value="<s:property value="id"/>" />
												<a href="javascript:;" class="btn btn_a use_now" title="立即开通"><span>立即开通</span></a>
											</s:else>
											</span>
											
											
										</td>
									  </tr> 
								 </s:else>
							   </s:iterator>
							 </s:if>
						  </table>
						</div>
					</div>
					</s:if>
					<!-- E 未使用服务 -->
				</div>
				<!-- E 服务 -->
				
				<!-- S 我的账户 -->
				<div id="r_side">
					<div id="my_acc" class="box box_a">
					<!-- 如果等于的0的话 账户是不存在的，这个时候账户余额未null 手机号显示未绑定-->
					
						<div class="box_hd">
							<strong>我的账户</strong>
						</div>
						<div class="box_bd">
						<s:if test="accountUser == null">
							<!-- S 账户余额 -->
							<div id="balance" class="clearfix">
								<em>账户余额：<strong><span id="acc_num">0.00</span> 元</strong></em>
								
								<span class="operate_link">
									<a href="payService_toSupplement.action?menuFlag=caiwu_alipay">充值</a>
									<a href="javascript:;" id="payment_detail">收支明细</a>
								</span>
							</div>
							<!-- E 账户余额 -->
							
							<!-- S 绑定手机 -->
							<div id="mobile" class="clearfix">
								<em>绑定手机号：</em>
								<!--  
								<span id="mobile_num">15878941234</span>
								<span class="operate_link">
									<a href="javascript:;" id="modify_mobile">修改</a>
								</span>
								-->
			                    <span class="operate_link">
								    <span style="padding-right: 18px;" id="no_bind">未绑定</span>
									<a href="javascript:;" id="new_mobile">绑定手机号</a>
								</span> 
								
								<input type="hidden" id="full_mobile" />
							</div>
							<!-- E 绑定手机 -->
							</s:if>
							<s:elseif test="accountUser != null">
							  <div id="balance" class="clearfix">
								<em>账户余额：<s:if test="accountUser.useBalance == null">
								  <strong><span id="acc_num">0.00</span> 元</strong>
								</s:if> </em>
								
								<s:else>
								  <strong><span id="acc_num"><fmt:formatNumber value="${ accountUser.useBalance}" type="number" pattern="#,##0.00#" /></span> 元</strong>
								</s:else>
								<span class="operate_link">
								<!-- 
									<a href="javascript:;">充值</a>
								 -->
								 <a href="payService_toSupplement.action?menuFlag=caiwu_alipay">充值</a>
									<a href="javascript:;" id="payment_detail">收支明细</a>
								</span>
							</div>
							<!-- E 账户余额 -->
							
							<!-- S 绑定手机 -->
							<div id="mobile" class="clearfix">
								<em>绑定手机号：</em>
								 <s:if test="accountUser.cellPhone != null && accountUser.cellPhone != ''">
								  <span id="mobile_num"><s:property value="accountUser.cellPhone"/></span>
								  <span class="operate_link">
									<a href="javascript:;" id="modify_mobile">修改</a>
									<a href="javascript:;" id="cancel_mobile">取消绑定</a>
								  </span>
								</s:if>
								<s:else>
								  <span class="operate_link">
								     <span style="padding-right: 18px;" id="no_bind">未绑定</span>
									 <a href="javascript:;" id="new_mobile">绑定手机号</a>
								  </span> 
								</s:else>
								<input type="hidden" id="full_mobile" />
							</div>
							</s:elseif>
							<!-- S 开启提醒 -->
							<div id="remind" class="clearfix">
								<em>提醒设置：</em>
								<form action="" id="remind_form">
								  <s:if test="payFreeList.size()>0">
									<ul>
									   <s:iterator value="payFreeList" id="dredgeService">
									   <!-- 
									    <s:if test="serviceId == 2">
											<li>
												<input type="checkbox" id="remind_d"  <s:if test="flag == 1">checked </s:if>/>
												<label for="remind_d">交易提醒</label>
											</li>
										</s:if>
									    <s:if test="serviceId == 3">
											<li>											  
												<input type="checkbox" id="remind_a" <s:if test="flag == 1"> checked </s:if> />
												<label for="remind_a">服务到期提醒</label>
											</li>
										</s:if>
										-->
										<s:if test="serviceId == 4">
											<li>											 
												<input type="checkbox" id="remind_b"  <s:if test="flag == 1">checked</s:if> />
												<!-- 
												<label for="remind_b">短信不足提醒</label>
												 -->
												<span class="select_bl">												  
													短信不足
													<select id="sms_remind">																								   
														<option value="50" <s:if test="smsLess == null">selected</s:if> >50</option>
														<option value="100" <s:if test="smsLess != null && smsLess==100">selected</s:if> >100</option>
														<option value="500" <s:if test="smsLess != null && smsLess==500">selected</s:if> >500</option>
														<option value="1000" <s:if test="smsLess != null && smsLess==1000">selected</s:if> >1000</option>
													</select>
													条提醒我
												</span>
											</li>
										</s:if>
										
										<s:if test="serviceId == 1">
											<li>											  
												<input type="checkbox" id="remind_c" <s:if test="flag == 1">checked</s:if>/>	
												<!-- 										 
												<label for="remind_c">余额不足提醒</label>
												 -->
												<span class="select_bl">
													余额不足
													<select id="balance_count">												 
													    <option value="5"  <s:if test="balance == null">selected</s:if> >5</option>
														<option value="10" <s:if test="balance != null && balance==10 ">selected</s:if> >10</option>
														<option value="20" <s:if test="balance != null && balance==20 ">selected</s:if> >20</option>
														<option value="50" <s:if test="balance != null && balance==50 ">selected</s:if> >50</option>													 
													</select>
													元提醒我
												</span>
											</li>
										</s:if>										
									 </s:iterator>
									</ul>
								</s:if>
							    <s:else>
								   <ul>
								   <!-- 
									 <li>
									    <input type="checkbox" id="remind_a" checked/>
										<label for="remind_a">服务到期提醒</label>
									 </li>
									 -->
									 
									 <li>
										<input type="checkbox" id="remind_b" checked/>
										<!-- 
										<label for="remind_b">短信不足提醒</label>
										 -->
										<span class="select_bl">
										短信不足
										<select name="" id="sms_remind">
											 <option value="50" selected>50</option>
											 <option value="100">100</option>
											 <option value="500">500</option>
											 <option value="1000">1000</option>
										</select>
		                                                                                   条提醒我
										</span>
									</li>
									
								    <li>
	                                  <input type="checkbox" id="remind_c" checked/>
	                                  <!--  
									  <label for="remind_c">余额不足提醒</label>
									  -->
									  <span class="select_bl">
										余额不足
										<select name="" id="balance_count">
											 <option value="5">5</option>
											 <option value="10">10</option>
											 <option value="20">20</option>
											 <option value="50">50</option>					  
										 </select>		   
                                                                                                          元提醒我
									   </span>			
									</li>
									<!-- 
									<li>
										<input type="checkbox" id="remind_d" checked/>
										<label for="remind_d">交易提醒</label>
									</li>
									 -->
									</ul>
							    </s:else>
								</form>
								<span class="operate_link">
									<a id="remind_save" href="javascript:;">保存提醒设置</a>
									<span class="free_tip">提醒免费</span>
								</span>
							</div>
							<!-- E 开启提醒 -->
						</div>
						
					</div>
				</div>
				<!-- E 我的账户 -->
			</div>
			
			<script type="text/javascript">
				var param = {
					getValidityAction: 'payService_updatePhoneCode.action',				// 获取效验码请求
					bindMobileAction: 'payService_phoneBind.action',				// 绑定手机请求
					remindAction: 'payService_updateRemindService.action',					// 提醒表单
					closeService: 'payService_closeService.action',					// 关闭服务
					openService: 'payService_applySmsService.action'						// 开启服务
				};
			</script>
			<script type="text/javascript">
				var params = {
					onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
						? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
					userId:${yto:getCookie('id')},						//当前登录用户的id
					userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
					infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
					bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
					pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
				}
		 </script>
			<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
			<!-- S 当前页面 JS -->
			<script type="text/javascript" src="${jsPath}/page/serve_home.js?d=${str:getVersion() }"></script>
			<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
	


