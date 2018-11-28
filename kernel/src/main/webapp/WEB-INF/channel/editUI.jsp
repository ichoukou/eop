<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>

    <link rel="stylesheet" type="text/css" href="${cssPath }/module/box.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath }/module/tab.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath }/module/table.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/addChannel.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">修改渠道信息</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S Tab -->
				<div class="tab tab_a">
					
					<div class="tab_panels">
						<div class="tab_panel">
							<form action="channel2_edit.action" id="channel_form" class="form" method="post">
								<input type="hidden" name="channel.id" id="cId" value="${channel.id }">
									<p>
										<label for="key" class="form_l"><span class="req">*</span>客户名称 ：</label>
										<input type="text" id="key" name="channel.key" value="${channel.key }" class="input_text"  size="50" />
										<span id="keyTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.key</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="cvalue" class="form_l"><span class="req">*</span>推送标识 ：</label>
										<input type="text" id="cvalue" name="channel.value" value="${channel.value }" class="input_text"  size="50"/>
										<span id="cvalueTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.value</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="ipAddress" class="form_l"><span class="req">*</span>ip_address：</label>
										<input type="text" id="ipAddress" name="channel.ipAddress" value="${channel.ipAddress }" class="input_text"  size="50"/>
										<span id="ipAddressTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.ipAddress</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="clientId" class="form_l"><span class="req">*</span>ClientId：</label>
										<input type="text" id="clientId" name="channel.clientId" value="${channel.clientId }" class="input_text"  size="50"/>
										<span id="clientIdTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.clientId</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="parternId" class="form_l"><span class="req">*</span>ParternId：</label>
										<input type="text" id="parternId" name="channel.parternId" value="${channel.parternId }" class="input_text"  size="50" disabled/>
										<span id="clientIdTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.clientId</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="ip" class="form_l"><span class="req">*</span>ip白名单 ：</label>
										<input type="text" id="ip" name="channel.ip" value="${channel.ip }" class="input_text"  size="50"/>
										<span id="ipTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.ip</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="isPrint" class="form_l"><span class="req">*</span>打印标识 ：</label>
										<input type="text" id="isPrint" name="channel.isPrint" value="${channel.isPrint }" class="input_text"  size="50"/>
										<span id="isPrintTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.isPrint</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="isSend" class="form_l"><span class="req">*</span>单号推送 ：</label>
										<input type="text" id="isSend" name="channel.isSend" value="${channel.isSend }" class="input_text"  size="50"/>
										<span id="isSendTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>channel.isSend</s:param>							
										</s:fielderror>
									</p>
									<p>
										<span class="btn btn_a"><input type="button" value="保存" id="edit"></span>&nbsp;&nbsp;
					   					<span class="btn btn_a"><input type="button" value="返回" id="back"></span>
					   				</p>
							</form>
						</div>
						
					</div>
				
				</div>
			</div>
		
		</div>
		<!-- E Content -->
	
	<!-- #include file="公共模块/footer.html" -->
	
	
	<script type="text/javascript" src="${jsPath }/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath }/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath }/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath }/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath }/page/addChannel.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
