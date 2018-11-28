<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/help/meta.jsp" %>
	
<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css"/>
<link rel="stylesheet" type="text/css" href="${cssPath}/common/help_common.css" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />

<script>
var path = {
		ctx: '${ctxPath}',
		media:'${mediaPath}' 
}
</script>
<script src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script src="${jsPath}/module/dialog.js?d=${str:getVersion() }" type="text/javascript"></script>
<!--[if IE 6]>
	<script type="text/javascript" src="js/util/position_fixed.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.png');
	</script>
<![endif]-->
<title>易通电子商务物流信息平台-圆通电商物流平台-圆通易通诚信软件电商版</title>
</head>
<body>
	<tiles:insertAttribute name="header"/>
	<div id="main" class="clearfix">
		<tiles:insertAttribute name="content"/>
		<tiles:insertAttribute name="menu"/>
	</div>
	<tiles:insertAttribute name="footer"/>

</body>
</html>