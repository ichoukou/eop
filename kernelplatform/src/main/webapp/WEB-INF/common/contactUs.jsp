<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<div id="content">
	<style type="text/css">
		#contact_title{margin-bottom:20px;}
		#contact_title h2 {font-size:20px;}
		#contact_body p{padding-left:100px;margin-bottom:10px;}
		#contact_body span{line-height:24px;height:24px;}
		#contact_body .contact_label{float:left;display:inline-block;width:100px;margin-left:-100px;text-align:right;}
		#contact_ft{line-height:24px;}
		.wb_ico{background:url(images/single/weibo.png) 0 -2px no-repeat;padding-left:22px;vertical-align:middle;}
	</style>
	<div id="contact_title">
		<h2>联系我们</h2>
	</div>
	
	<div id="contact_body">
		<p>
			<span class="contact_label">技术支持：</span><span class="contact_text">021-64703131-107; 021-69777830</span>
		</p>
		<p>
			<span class="contact_label">易通在线支持：</span>
			<span class="contact_text">
				<a href="http://wpa.qq.com/msgrd?v=3&amp;uin=2366710544&amp;site=qq&amp;menu=yes" target="_blank"><img title="点击这里给我发消息" alt="点击这里给我发消息" src="http://wpa.qq.com/pa?p=2:2366710544:45&amp;r=0.658135694570411"></a>
				<a href="http://wpa.qq.com/msgrd?v=3&amp;uin=2294882345&amp;site=qq&amp;menu=yes" target="_blank"><img title="点击这里给我发消息" alt="点击这里给我发消息" src="http://wpa.qq.com/pa?p=2:2294882345:45&amp;r=0.658135694570411"></a>
				<a href="http://web.im.alisoft.com/msg.aw?v=2&amp;uid=ytoyitong%20&amp;site=cnalichn&amp;s=1" target="_blank" ><img title="点击这里给我发消息"  alt="点击这里给我发消息" src="http://img.im.alisoft.com/actions/wbtx/wangwang/1/online.gif"></a>
			</span>
		</p>
		<p>
		<c:choose>
			<c:when test="${yto:getCookie('userType') == 2 
	   						|| yto:getCookie('userType') == 21
	   						|| yto:getCookie('userType') == 22
	   						|| yto:getCookie('userType') == 23 }">
				<span class="contact_label">QQ交流群：</span><span class="contact_text">171313338</span>
			</c:when>
			<c:otherwise>
				<span class="contact_label">QQ交流群：</span><span class="contact_text">173184824, 240958092</span>
			</c:otherwise>
		</c:choose>
		</p>
		<p>
			<span class="contact_label">关注我们：</span><span class="contact_text"><a class="wb_ico" href="http://weibo.com/ytoec">新浪微博</a></span>
		</p>
	</div>
	
	<div id="contact_ft">
		<p>如果您有独立网站，希望享受易通的服务，可以找我们对接易通</p>
		<p>联系方式：021-64703131-110; 021-64703131-106</p>
	</div>
</div>