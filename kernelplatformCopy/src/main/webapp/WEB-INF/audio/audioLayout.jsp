<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/audio/meta.jsp" %>
	
<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }"/>
<link rel="stylesheet" type="text/css" href="${cssPath}/common/help_common.css?d=${str:getVersion() }" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />

<script>
var path = {
		ctx: '${ctxPath}',
		media:'${mediaPath}' 
}
</script>
<script src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }" type="text/javascript" charset="UTF-8"></script>
<script src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }" type="text/javascript" charset="UTF-8"></script>
<script src="${jsPath}/module/dialog.js?d=${str:getVersion() }" type="text/javascript"></script>
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
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
	<tiles:insertAttribute name="audio_header"/>
	<div id="main" class="clearfix">
		<tiles:insertAttribute name="audio_menu"/>
		<tiles:insertAttribute name="audio_content"/>
	</div>
	<tiles:insertAttribute name="audio_footer"/>

</body>
</html>