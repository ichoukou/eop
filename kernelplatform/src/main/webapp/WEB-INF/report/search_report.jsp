<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>

<!-- S 当前页面 CSS -->

<!-- E 当前页面 CSS -->

<title>短信查询</title>

	
	<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">物流数据统计报表</h2>
			</div>
			<div id="content_bd" class="clearfix">

				<!-- S 查询表格 -->
				<div class="table">
				
				<c:forEach items="${reports}" var="vo">
						${vo.partitionDate}
						${vo.ratio} <br>
				</c:forEach>
				
				
				   <body bgcolor="#ffffff">
			      <OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase=http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0" width="600" height="500" id="Column3D" >
			         <param name="movie" value="../FusionCharts/FCF_Column3D.swf" />
			         <param name="FlashVars" value="&dataURL=Data.xml&chartWidth=600&chartHeight=500">
			         <param name="quality" value="high" />
			         <embed src="../FusionCharts/FCF_Column3D.swf" flashVars="&dataURL=Data.xml&chartWidth=600&chartHeight=500" quality="high" width="600" height="500" name="Column3D" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
			      	</object>
				   </body>
				
				
			</div>
				<!-- E 查询表格 -->
				
		
			</div>
		</div>
		<!-- E Content -->



	<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="js/util/position_fixed.js?d=${str:getVersion() }"></script>
		<script type="text/javascript" src="js/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('.png');
		</script>
	<![endif]-->
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/dialog.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->

	<!-- E 当前页面 JS -->