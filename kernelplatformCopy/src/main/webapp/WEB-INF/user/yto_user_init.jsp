<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>

<div id="content">
  	<div id="content_hd" class="clearfix">
<!-- 		<h2 id="message_icon">数据初始化</h2>	 -->
	</div>
	
	<div id="content_bd">
		<div class="box box_a">
			<div class="box_bd">
				<div class="box box_a">
					<div id="box_bd_b" class="box_bd" style="position:relative;">
						<form action="ytoUser!deleteUser.action" method="post">
							用户名：<input type="text" class="input_text" name="userName">&nbsp&nbsp<span class="btn btn_a"><input type="submit" value="提交"></span>
							<div style="display: inline-block; width: 300px;"><s:property value='deleteUser'/></div>
						</form>
						<br/>
						<form action="ytoUser!initBranch.action" method="post">
							网点名：<input type="text" class="input_text" name="branchName">&nbsp&nbsp<span class="btn btn_a"><input type="submit" value="提交"></span>
							<div style="display:inline-block; width: 300px"><s:property value='initBranch'/></div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>