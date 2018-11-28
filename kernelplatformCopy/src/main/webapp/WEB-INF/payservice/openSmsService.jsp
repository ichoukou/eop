<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<meta charset="UTF-8" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/open_service.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	<title>开通服务</title>
	<!-- 根据serviceId 进行判断 是那种服务 -->

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">开通服务</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
				<div class="box box_a">
					<div class="box_bd">
						<form action="payService_openService.action" class="form" id="serve_form">
						  <!-- 短信服务 -->
						  <s:if test="paraMap.serviceid == 5">
				             <!-- S 说明 -->
								<div class="detail_box" id="detail_box_a">
									<label class="p_label">说明：</label>
									<span class="icon_desc" id="icon_desc_a">
										<i></i>
										<strong><s:property value="paraMap.serviceName"/></strong><a href="javascript:;" title="了解详情" class="btn btn_a"><span>了解详情</span></a>
										<span class="desc_p">短信服务是一款专为卖家设置的提醒管理工具，更好的解决包过时效问题，及时发现并解决问题，提升客户物流服务体验，改变物流评价普遍偏低的厄运，增加回头客，店铺信用升级不用愁</span>
									</span>
								</div>
								<!-- E 说明 -->
			             </s:if>
			             
							<!-- S 使用明细 -->
							<div class="detail_box" id="detail_box_c">
								<label class="p_label">使用明细：</label>
								<span class="detail_info">名称：<em><s:property value="paraMap.serviceName"/></em></span>
								<input type="hidden" value="<s:property value="paraMap.serviceName"/>" name="payment.dealName" id="serviceName">
								<span class="detail_info">使用者：<em>
								  <s:if test="paraMap.shopName != null">
								    <s:property value="paraMap.shopName"/>
								  </s:if>
								  <s:else>
								  </s:else>
								</em></span>
								<!-- 服务周期 -->
								<span class="detail_info">当前余额：<em><s:property value="paraMap.useBalance"/></em> 元</span>
								<!-- 
								<span class="detail_info" id="need_pay">应支付金额：<em><s:property value="paraMap.unitPrice"/></em> 元</span>
								 -->
								<input type="hidden" value="50.00" name="money" id="money"/>
								<input type="hidden" value="<s:property value="paraMap.shopName"/>" id="user_name" />
								<input type="hidden" value="<s:property value="paraMap.serviceid"/>" id="serviceId" name="serviceId">
								<ul>
									<li>
										<input type="checkbox" id="read_agree" checked />
										<label for="read_agree" class="clear_label">我已阅读并同意 <a href="javascript:;">易通平台自助服务协议</a></label>
										<span id="read_agreeTip"></span>
									</li>
								</ul>
								
								<textarea id="agreement" name="remark"></textarea>
								
								<div class="btn_box">
									<a href="javascript:;" title="立即使用" class="btn btn_a" id="toOpenSms"><span>立即使用</span></a>
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
		