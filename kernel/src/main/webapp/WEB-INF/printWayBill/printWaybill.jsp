<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ page import="net.ytoec.kernel.util.ConfigUtilSingle"%>
<c:set var="numServiceUrl" value="<%=ConfigUtilSingle.getInstance().getGEN_NUM_SERVICE_URL() %>" scope="request"/>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
    <!--link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css" media="all" /-->
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link href="${cssPath}/page/print_new.css?d=${str:getVersion() }" rel="stylesheet" type="text/css">
	<!-- E 当前页面 CSS -->
	<title>打印</title>

</head>
<body id="new_prints">
	<!-- #include file="公共模块/header.html" -->
	
	<!-- S Main -->
	<div id="main" class="clearfix">
		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">打印</h2>
			</div>
			<div id="content_bd">
				<div class="content">
				   <div class="notice">
				   		<form id="print_fm" target="content_ifm" action="${ctxPath}/order/printContent!content.action">
							<p>此打印功能仅支持 IE 浏览器，并先<a href="${cssPath}/common/font.zip">下载所需字体</a></p>
							<p>
								<input id="code" class="input_text" name="deliverNo" />
								<input id="isIE" type="hidden" value="0" name="isIE" />
								<a class="manage" href="javascript:;">运单号号段管理</a>
							</p>
						</form>
					</div>
				   <div class="container">
				   		<iframe id="content_ifm" name="content_ifm" frameBorder="0" scrolling="no" style="width:100%;height:600px;"></iframe>
				   </div>
				</div>
			</div>
		</div>
		<!-- E Content -->
		
		<!-- #include file="公共模块/sidebar.html" -->
	</div>
	<!-- E Main -->
	
	<!-- #include file="公共模块/footer.html" -->
	
	
	<script type="text/javascript">
	var params = {
		printsGetUrl: '${ctxPath}/order/way-bill-print!getWayBillPrint.action',  // 获取打印数据 url
		customerGetUrl: '${ctxPath}/order/printWaybill!getBranchSite.action',    // 获取客户 url
		ticketcodeGetUrl: '${numServiceUrl}',      // 获取相关运单号 url			
		rangeSaveUrl: 'http://192.168.4.136:8080/genNo/gen-no!genUserNumRange.action',         // 保存运单号段 url
        rangGetUrl: 'http://192.168.4.136:8080/genNo/gen-no!getUserGenNo.action',                   // 获取运单号段 url
        userId: '82687f5530f47df8e4037cb8ac3a6dca',
        appCode: 'HDYS'
	};
	</script>
	<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${jsPath}/util/position_fixed.js?d=${str:getVersion() }"></script>
		<script type="text/javascript" src="${jsPath}/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('.png');
		</script>
	<![endif]-->
    <script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/dialog.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
   <script type="text/javascript" src="${jsPath}/page/print_new.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
</body>
</html>