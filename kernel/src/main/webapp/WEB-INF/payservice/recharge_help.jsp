<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/pay_help.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">支付帮助</h2>
	</div>
	<div id="content_bd" class="clearfix">
				
		<div id="pay_help_hd">
			<h2>您付款遇到问题了？看看是不是由于下面的原因。</h2>			
		</div>
				
		<div id="pay_help_bd">
			<h3>支付宝已扣款，但易通账户余额没有增加？</h3>
			<p>答：这是因为银行的数据没有及时传给我们，我们会在第二个工作日与银行对账后给予确认，请耐心等待一下。</p>
					
			<h3>支付宝页面打不开怎么办？</h3>
			<p>答：建议使用Microsoft IE浏览器。点击IE菜单：工具--Internet选项--安全，将安全中的各项设置恢复到默认级别。</p>
			
			<h3>我支付宝重复多次付款了该怎么办？</h3>
			<p>答：由于您的支付信息支付宝没有及时传输给易通，造成您的支付宝重复扣款。不过请放心，我们会在第二个工作日和支付宝对账，确认您的汇款后，
			重复支付款项将会直接充值到您的支付宝账户</p>
			
		</div>
				
		<div id="pay_help_ft">
		     <input type="hidden" value="<s:property value="serviceId"/>">
		     <c:if test="${yto:getCookie('userType') == 1
				            || yto:getCookie('userType') == 4
				            || yto:getCookie('userType') == 5}">
			     <s:if test="serviceId == null">
				      <em>问题已经解决了：点此返回<a href="payService_getPaymentList.action?menuFlag=caiwu_paymentList&currentPage=1" title="收支明细">收支明细</a></em>
				 </s:if>
				 <s:else>
			          <em>问题已经解决了：点此返回<a href="payService_payOrClose.action?menuFlag=caiwu_paymentList&serviceId=<s:property value="serviceId"/>" title="收支明细">收支明细</a></em>
			     </s:else>
		    </c:if>
		</div>	
		<script type="text/javascript">
			var params = {
				onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
					? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
				userId:${yto:getCookie('id')},						//当前登录用户的id
				userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
				infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
				bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
				pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
			}
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	</div>
</div>
<!-- E Content -->
		
