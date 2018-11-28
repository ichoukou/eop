<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
	<!-- S Footer -->
	<div id="footer">
		<p><a href="noint1_onYto.action" title="关于易通">关于易通</a> | <a href="noint1_contractUs.action" title="联系我们">联系我们</a> | <a href="noint1_toHelp.action" title="帮助中心" target="_blank">帮助中心</a> | <a href="send_openAdviseUI.action?menuFlag=msg_advise" title="建议意见">建议意见</a></p>
		
		<p>圆通速递公司总部：上海青浦区华新镇华徐公路3029弄28号 邮政编码：201705 沪ICP备05004632号</p>
		
		<p>Copyright &copy; 2000-2012 All Right Reserved</p>
	</div>
	<!-- E Footer -->

<!-- S Guide -->
	
	<div id="guide_v2" class="mai_guide_v2">
		<div id="guide_sear">
			<form action="waybill_homeQuery.action" id="guide_sear_form">
				<label for="guide_sear_input">查件</label>
				<input type="hidden" name="currentPage" value="1" />
				<input type="hidden" name="isCheck" value="0" />
				<input type="hidden" name="menuFlag" value="chajian_waybill" />
				<input type="text" name="logisticsIds" id="guide_sear_input" class="input_text" /><a href="javascript:;" title="查询" id="guide_sear_btn">查 询</a>
			</form>
		</div>
		
		<c:choose>
		<c:when test="${str:startsWith(yto:getCookie('userType'),'1')}">
		<div id="guide_help" style="height:90px;">
			<div id="guide_help_box">
				<label style="margin-top:35px;">客服</label>
				<!--a style="position:relative;top:14px;" target="_blank" href="http://web.im.alisoft.com/msg.aw?v=2&amp;uid=ytoyitong%20&amp;site=cnalichn&amp;s=1"><img alt="点击这里给我发消息" src="http://img.im.alisoft.com/actions/wbtx/wangwang/1/online.gif" /></a--> 
				<a style="position:relative;top:14px;" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2294882345&amp;site=qq&amp;menu=yes" target="_blank"><img alt="点击这里给我发消息" src="http://wpa.qq.com/pa?p=2:2294882345:41%20&amp;r=0.658135694570411" /></a>
				
				<div class="qw_qun" style="position:relative;top:20px;">
					<!--em id="w_qun">旺旺交流群：642218871、548569297</em-->
					<!--em id="q_qun">QQ交流群：173184824、240958092</em-->
					<em id="q_qun">QQ交流群：273559167、241711549</em>
				</div>
			</div>
		</div>
		</c:when>
		<c:otherwise>
		<div id="guide_help" style="height:90px;">
			<div id="guide_help_box">
				<label style="margin-top:35px;">客服</label>
				<a style="position:relative;top:14px;" target="_blank" href="http://web.im.alisoft.com/msg.aw?v=2&amp;uid=ytoyitong%20&amp;site=cnalichn&amp;s=1"><img alt="点击这里给我发消息" src="http://img.im.alisoft.com/actions/wbtx/wangwang/1/online.gif" /></a> 
				<a style="position:relative;top:14px;" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2294882345&amp;site=qq&amp;menu=yes" target="_blank"><img alt="点击这里给我发消息" src="http://wpa.qq.com/pa?p=2:2294882345:41%20&amp;r=0.658135694570411" /></a>
				<div class="qw_qun" style="position:relative;top:20px;">
					<em id="q_qun">QQ交流群：171313338</em>
				</div>
			</div>
		</div>
		</c:otherwise>
		</c:choose>
				

		
		<div id="guide_msg">
			<form action="send_suggest.action" id="guide_msg_form">
				<label for="guide_msg_text">反馈</label>
				<input type="hidden" name="messageTheme" id="messageTheme" value="反馈意见" />
				<textarea name="messageContent" id="guide_msg_text"></textarea>
				<a href="javascript:;" id="guide_msg_btn">提 交</a>
			</form>
		</div>
	</div>
	<!-- E Guide -->
<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>

<script>
$(function() {
	$('#guide li').hover(
		function() {
			$('.guide_more', $(this)).show();
		},
		function() {
			$('.guide_more', $(this)).hide();
		}
	);
	
	var els = {
		searBtn:$("#guide_sear_btn"),	
		searForm:$("#guide_sear_form")
	};
	
	els.searBtn.click(function(ev){
		ev.preventDefault();
		if($("#guide_sear_input").val()!=null && $.trim($("#guide_sear_input").val())!=""){
			els.searForm.trigger("submit");
		}else{
			window.location.href="waybill_bill.action?menuFlag=chajian_waybill";
		}
	});
});
</script>