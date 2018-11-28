<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<%@ taglib uri="/WEB-INF/tlds/str.tld" prefix="str" %>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" type="text/css" href="css/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/module/button.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/page/print_express.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="css/page/express_for_printer.css?d=${str:getVersion() }" media="print" />
	<title>打印快递单</title>
</head>
<body>
	<!-- S 分页 -->
	<div id="pagenavi">
		<div id="curr_total_page">
			共 <span id="curr_page">1</span> / <span id="total_page"><s:property value="clickBatchOrderPrintList.size()"></s:property></span> 页
		</div>
		
		<div id="prev_next_page">
			<a href="javascript:;" id="prev_page">上一页</a>
			<a href="javascript:;" id="next_page">下一页</a>
		</div>
	</div>
	<!-- E 分页 -->
	
	<div class="print_btn">
		<a href="javascript:;" class="btn btn_a" title="打印全部"><span>打印全部</span></a>
		<c:if test="${yto:getCookie('userType') == 2 
		   		|| yto:getCookie('userType') == 21
		   		|| yto:getCookie('userType') == 23
		   		|| yto:getCookie('userType') == 22}">
			<span class="tips">打印后，记得让客户发货哦^_^~</span>
		</c:if>
	</div>
	
	${moduleString}
	
	<div class="print_btn">
		<a href="javascript:;" class="btn btn_a" title="打印全部"><span>打印全部</span></a>
	</div>
	<script type="text/javascript">
		var params = {
			json: <s:property value="json" escape="false"/>,
			ids: [<s:property value="orderPrintIds"/>],	
			markPrint: 'orderPrint!updateIsPrintBatch.action'
		}
	</script>
	<script type="text/javascript" src="js/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/page/print_express.js?d=${str:getVersion() }"></script>
</body>
</html>