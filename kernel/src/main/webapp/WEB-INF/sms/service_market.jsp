<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
			<div id="conten	t_bd">
            <!-- S Box -->
            <div class="box box_a">
				<div class="box_bd">
					<div id="modify_process">
						<ol>
							<li id="process_cur">1.创建营销活动</li>
							<li id="">2.选择参加活动成员</li>
							<li id="process_last">3.发送促销信息</li>
						</ol>
					</div>

					<form action="smsServiceMarket!smsMarketPageStep2.action" id="mark_form1">
					<input type="hidden" name="menuFlag" value="sms_made"/>
					<input type="hidden" name="smsTypeId" id="smsTypeId_" value="<s:property value='smsTypeId'/>"/>
						<input type="hidden" name="flag" id="flag" value="0" />
						<p><strong>可发短信：</strong></p>
						<p><span class="big-txt"><s:property value="smsUsecount"/></span>条<a target="_blank" href="alipay_toRechargeOnline.action?menuFlag=alipay">充值</a></p>
						<p><strong>选择模板</strong></p>		
																
						<c:forEach items="${smsTemplateList}" var="vo"> 
						    <p class="temp_box">
						    	<c:if test="${templateId==null}">
									<input class="input_radio" name="templateId" type="radio" <c:if test="${vo.isDefault=='Y'}">checked</c:if> value="${vo.id}"  id="${vo.id}"/>
									<label for="${vo.id}">${vo.content}</label>
								</c:if>
								<c:if test="${templateId!=null}">
									<input class="input_radio" name="templateId" type="radio" <c:if test="${templateId==vo.id}">checked</c:if> value="${vo.id}"  id="${vo.id}"/>
									<label for="${vo.id}">${vo.content}</label>
								</c:if>
							</p>
						</c:forEach>
						<p> 
							<a id="tempCreate_" href="javascript:;">添加新模板</a>
							<span class="veri">新添加模板需要管理员审核才可使用，审核时间24小时工作时间节假期间顺延。</span>
						</p>
						<p class="btn_mid">
							<a href="javascript:;" id="next_step_a" class="btn btn_a" title="下一步"><span>下一步</span></a>
						</p>
					</form>
				</div>
			</div>	
	
			</div>
			
    <script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
    <script type="text/javascript" src="${jsPath}/page/mark_home.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
			
		</div>
		<!-- E Content -->
	
