<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>

    <link rel="stylesheet" type="text/css" href="${cssPath }/module/box.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath }/module/tab.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath }/module/table.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath }/page/addSubConfig.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">增加<s:property value="configCode.confKey"/>子配置信息</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S Tab -->
				<div class="tab tab_a">
					
					<div class="tab_panels">
						<div class="tab_panel">
							<form action="config_add.action" id="configFrom" method="post" class="form">
								<input type="hidden" id="levelId" name="levelId" value=<s:property value='configCode.confLevel+1'/>> <!-- 第N级配置 -->
        						<input type="hidden" id="pid" name="configCode.pid" value=<s:property value='configCode.id'/>> <!-- 父配置id -->
									<p>
										<label for="confKey" class="form_l"><span class="req">*</span>配置名 ：</label>
										<input type="text" id="confKey" name="configCode.confKey" class="input_text" size="50" />
										<span id="confKeyTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>configCode.confKey</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="confValue" class="form_l"><span class="req">*</span>配置值 ：</label>
										<input type="text" id="confValue" name="configCode.confValue" class="input_text"  size="50"/>
										<span id="confValueTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>configCode.confValue</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="confText" class="form_l"><span class="req">*</span>配置显示值：</label>
										<input type="text" id="confText" name="configCode.confText" class="input_text"  size="50"/>
										<span id="confTextTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>configCode.confText</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="confType" class="form_l"><span class="req">*</span>配置类型：</label>
										<select id="confType" name="configCode.confType" style="width:290px;">
					   						<option value="1">单目录配置</option>
					   						<option value="2">多目录配置</option>
					   					</select>
									</p>
									<p>
										<label for="seq" class="form_l"><span class="req">*</span>排序：</label>
										<input type="text" id="seq" name="configCode.seq" class="input_text"  size="50"/>
										<span id="seqTip"></span>
										<s:fielderror cssStyle="color: red">
											<s:param>configCode.seq</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="remark" class="form_l"><span class="req"></span>备注：</label>
										<input type="text" id="remark" name="configCode.remark" class="input_text"  size="50"/>
										<span id="remarkTip"></span>
									</p>
									<p>
										<span class="btn btn_a"><input type="button" value="保存" id="add"></span>&nbsp;&nbsp;
					   					<span class="btn btn_a"><input type="reset" value="清空" ></span>&nbsp;&nbsp;
					   					<span class="btn btn_a"><input type="button" value="返回" id = "back" ></span>
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
	<script type="text/javascript" src="${jsPath }/page/addSubConfig.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
</body>
</html>