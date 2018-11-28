$(function() {
	var openService = (function() {
	
		// 显示提示 
		var showIcon = {
			correct: function(el, text) {
				el.html('<span class="yto_onCorrect">' + text + '</span>');
			},
			error: function(el, text) {
				el.html('<span class="yto_onError">' + text + '</span>');
			},
			show: function(el, text) {
				el.html('<span class="yto_onShow">' + text + '</span>');
			}
		};
		
		var balance = parseFloat($('#balance').val(), 10);
		
		// 更改期限
		var changeExprire = function() {
			
			$('#detail_box_b .input_radio').change(function() {
				var _this = $(this),
					money = parseFloat($('.money', _this.parent()).val(), 10),
					time = $('.time', _this.parent()).val();
				var circle = $('#detail_box_b input[type="radio"]:checked').val();
				//resetPay = resetPay > 0 ? resetPay : 0;
				$('#period_use em').html(time);
				$('#need_pay em').html(money.toFixed(2));
			});
		};
		
		//立即使用
		var openSmsService = function(){
			$('#toOpenSms').live('click',function(ev){
				ev.preventDefault();
				var serviceId = $("#serviceId").val();
				window.location = 'payService_applySmsService.action?serviceId=' + serviceId;
			});
		};
		
		//了解详情
		var knowDetail = function(){
			$("#knowDetail").live('click',function(ev){
				ev.preventDefault();
			var serviceId = $("#serviceId").val();
			var smsFlag = $('#smsFlag').val();
			var menuFlag = '';
			if(serviceId == 5){
				menuFlag = 'sms_home';
			}else if(serviceId == 6){
				menuFlag = 'chajian_passManage_warn';
			}
			if(smsFlag == '1'){
				window.open('alipay_seeDetails.action?menuFlag='+menuFlag+'&serviceId='+serviceId+'&smsFlag='+smsFlag);
			}else{
				window.open('payService_seeDetails.action?menuFlag='+menuFlag+'&serviceId='+serviceId);
			}
			});
		};
		
		// 表单验证
		var form = function() {
			// 点击“付款”
			$('#pay_bill').click(function(ev) {
				ev.preventDefault();
				//wangmindong_message
//				alert("此功能已暂停!");
//				return ;
				
				var result = $('#read_agree').prop('checked'),
				money = parseFloat($('.money', $('#detail_box_b ul .input_radio:checked').parent()).val(), 10),
				resetPay = money - balance;
			    renew = $('#auto_pay').prop("checked");
				var circleId = $('#detail_box_b input[type="radio"]:checked').attr('id');
				if(circleId == 'radio_month'){
					$('#radio_month').val('0');
				}else if(circleId == 'radio_season'){
					$('#radio_season').val('1');
				}else if(circleId == 'radio_half'){
					$('#radio_half').val('2');
				}else if(circleId == 'radio_year'){
					$('#radio_year').val('3');
				}
				var circle = $('#detail_box_b input[type="radio"]:checked').val();
				
	            $("#money").val(money.toFixed(2));
				if(renew){
					$('#isAutoRennew').val('1');
				}else{
					$('#isAutoRennew').val('0');
				}
				
				$.ajax({
					url: 'payService_getBalance.action',
					cache: false,
					success: function(data) {
						resetPay = money - (data.balance+data.useBalance);
						if(data.balance == 0.0 ){
							resetPay = money - data.useBalance;
						}else{
							resetPay = money - (data.useBalance+data.balance);
						}
						var rePay = money - data.useBalance;
						if (result) {
							showIcon.correct($('#read_agreeTip'), ' ');
							
							// 还需支付
							if (resetPay > 0) {
								var resetDialog = new Dialog();
								
								var resetHtml = '<form action="payService_toAliPay.action" id="pay_form" target="_blank">' +
												'	<p class="dialog_p">用户名：' + $('#shopName').val() + '</p>' +
												'	<p class="dialog_p">应付金额：' + money.toFixed(2) + ' 元</p>' +
												'	<p class="dialog_p">账户余额：' + data.useBalance.toFixed(2) + ' 元</p>' +
												'   <input type="hidden" value="' + money +'" name="money" />'+
												'   <input type="hidden" value="' + $("#serviceId").val() +'" name="serviceId" />'+
												'   <input type="hidden" value="' +$("#isAutoRennew").val()+'" name="isAutoRenew" />'+
												'   <input type="hidden" value="'+$("#serviceName").val()+'" name="dealName" />'+
												'   <input type="hidden" value="'+rePay.toFixed(2)+'" name="resetPay" />'+
												'   <input type="hidden" value="'+circle +'" name="circle" />'+
												'	<em id="gudie_desc">您的账户余额不够完成此次支付，易通将引导您至支付宝页面完成剩余款项付款。</em>' +
												'	<p id="to_pay">使用支付宝账户支付<span>' + rePay.toFixed(2) + '</span>元</p>' +
												'</form>';
								resetDialog.init({
									contentHtml: resetHtml,
									yes: function() {
										setTimeout(function(){
											$("#pay_form").trigger('submit');
											resetDialog.close();//关闭  
											var tipsDialog = new Dialog();
											var serviceId = $("#serviceId").val();

											var tipsHtml = '<ul id="help_tips" style="text-align:center;">' +
											'	<li style="font-size:14px;font-weight:bold;margin-right:14px;margin-bottom:10px;background:url(../../images/single/note_zf.png) no-repeat;widht:17px;height:17px;">请在支付宝页面完成付款</li>' +
											'	<li style="color:#ccc;text-align:left">付款完成后请不要关闭此窗口。付款</li>' +
											'	<li style="color:#ccc;text-align:left;">完成后根据情况点击一下按钮。</li>' +
											'</ul>';
											
											tipsDialog.init({
												contentHtml: tipsHtml,
												yes:function(){
													tipsDialog.close();
													window.location='passManage_warnningIndex.action?menuFlag=chajian_passManage_warn';
												},
												no:function(){
													tipsDialog.close();
													window.location='payService_rechargeHelp.action?menuFlag=caiwu_rechargeHelp&serviceId='+serviceId;
												},
												yesVal:'完成付款',
												noVal:'付款遇到问题'
											});
										},1);
									},
									yesVal: '去支付宝付款',
									closeBtn: true
								})
							} else {
								var serviceId = $("#serviceId").val();
								var smsFlag = $('#smsFlag').val();
								if(serviceId == 5){
									var confirmDialog = new Dialog();
									var contentHtml = '您确定开通吗?';
									confirmDialog.init({
										contentHtml: contentHtml,
										yes: function() {
											setTimeout(function(){
												if(smsFlag == '1'){
													window.location = 'alipay_applySmsService.action?menuFlag=home_payService&serviceId=' + serviceId+'&smsFlag='+smsFlag;
												}else{
													window.location = 'payService_applySmsService.action?menuFlag=home_payService&serviceId=' + serviceId;
												}
												confirmDialog.close();
											},1);
										},
										no: function() {
											confirmDialog.close();
										}
									});
								}else{
									var confirmDialog = new Dialog();
									var contentHtml = '<ul id="help_tips">' +
						               '	<li>确定后系统将从您的账户扣除金额'+money.toFixed(2)+'元</li>' +
						               '</ul>';
									confirmDialog.init({
										contentHtml: contentHtml,
										yes: function() {
											var serviceId = $("#serviceId").val();
											setTimeout(function() {
												$('#serve_form').trigger('submit');	
											}, 1);
												
											confirmDialog.close();
										},
										no: function() {
											confirmDialog.close();
										}
									});
								}
								
							}
							
						} else {
							showIcon.error($('#read_agreeTip'), '您必须先同意《服务协议》，才可使用服务');
						}
						}
					 })
					 
				$('#read_agree').change(function() {
					var _this = $(this);
					
					if (_this.prop('checked')) {
						$('#read_agreeTip span').hide();
					}
				})
			});
			
		};
		
		return {
			init: function() {
				changeExprire();
				openSmsService();
				form();
				knowDetail();
			}
		}
	})();
	
	openService.init();
})