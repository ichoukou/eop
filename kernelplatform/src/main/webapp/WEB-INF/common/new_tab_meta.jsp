<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
<meta charset="UTF-8" />
<meta name="keywords" content="易通物流,电商版,电子商务,圆通电商物流,物流平台,电商物流平台,电子商务平台,易通电商版,电商物流网,圆通电子商务物流平台,圆通易通,电商物流诚信系统" />
<meta name="description" content="易通诚信软件电商版由圆通速递推出，全国首个电商物流平台。易通诚信软件电商版又称电子商务物流信息平台。主要功能有问题件管理、智能查件、电子对账等物流信息化服务。021-69777830。" />
	
<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css"/>
<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" />
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