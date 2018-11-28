<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/page/recharge_online.css?d=${str:getVersion() }" media="all" />
<script type="text/javascript" src="${jsPath}/page/recharge_online.js?d=${str:getVersion() }"></script>
<!-- S Content -->
<div id="content">
           <div id="content_hd" class="clearfix">
<!-- 		  <h2 id="message_icon">在线充值</h2> -->
	  </div>
	  <div id="content_bd" class="clearfix">
		 <div class="box box_a">
				<div class="box_bd">
				<%--<div>此功能已暂停!</div> --%>	
				 
					<form action="alipay_onlineDealPayment.action?menuFlag=${menuFlag}" id="recharge_form" target="_blank" method="post">
						<div id="money_box">
							<h3>充值金额：</h3>
							<ul class="clearfix">
								<li>
									<input type="radio" class="input_radio" name="rechargeMenoy" id="radio_50" checked="checked" value="50"/>
									<label for="radio_50">50 元</label>
								</li>
								<li>
									<input type="radio" class="input_radio" name="rechargeMenoy" id="radio_100" value="100"/>
									<label for="radio_100">100元</label>
								</li>
								<li>
									<input type="radio" class="input_radio" name="rechargeMenoy" id="radio_500" value="500"/>
									<label for="radio_500">500 元</label>
								</li>
								<li>
									<input type="radio" class="input_radio" name="rechargeMenoy" id="radio_1000" value="1000"/>
									<label for="radio_1000">1000 元</label>
								</li>
								<li id="other">
									<input type="radio" class="input_radio" name="rechargeMenoy" id="radio_other"/>
									<label for="radio_other">其他</label> <input id="diy_value" type="text" class="input_text" name="rechargeOther" maxlength="11"/> 元
									<span id="diy_valueTip"></span>
								</li>
							</ul>
						</div>
						
						<div id="method_box">
							<h3>支付方式：</h3>
							<ul class="clearfix">
								<li>
									<input type="radio" class="input_radio" name="payWay" id="alipay" checked="checked" value="支付宝"/>
									<label for="alipay"><img class="payment_logo" src="${imagesPath}/single/alipay.png" alt="支付宝" /></label>
								</li>
							</ul>
						</div>						
					</form>
					<div id="recharge_btn">
						<a href="javascript:;" class="btn btn_a" title="充值"><span>充 值</span></a>
					</div>	
										
				</div>
		</div>
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
</div>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<!-- E Content -->
