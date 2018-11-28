$(function() {
	var rechargeOnline = (function() {
	
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
		
		
		var isNumber = function(o) { return typeof o === 'number' && isFinite(o); };
		
		var check = {
			diyVal: function(el) {
				var val = el.val(),
					reg = /^([+-]?)\d*\.?\d+$/;
				
				// 如果填写的不是数字
				if (!reg.test(val)) {
					el.val('');
					showIcon.error($('#diy_valueTip'), '金额格式不正确');				
					return false;
				} else {
					// 格式化价格
					var formatPrice = parseFloat(val, 10).toFixed(2);					
					el.val(formatPrice);
					
					if (formatPrice >= 50 && formatPrice <= 1000000) {
						showIcon.correct($('#diy_valueTip'), ' ');						
						return true;
					} else {
						if(formatPrice < 50){
							showIcon.error($('#diy_valueTip'), '充值金额不能小于50元');						
							return false;
						}else if(formatPrice > 1000000){
							showIcon.error($('#diy_valueTip'), '充值金额不能大于1000000元');						
							return false;
						}
					}
				}
			}
		};
		
		// 充值
		var recharge = function() {
			
			
			var els = {
				diyValue: $('#diy_value')
			};
			
			var allowChrs = [8,13,37,39,46,48,49,50,51,52,53,54,55,56,57,96,97,98,99,100,101,102,103,104,105,190];
			
			var msg = {
				priceFormatErr: '金额格式不正确',
				overPriceErr: '充值金额不能小于50元'
			};
			
			// 其他金额输入框获得焦点
			els.diyValue.focus(function() {			
				$('#radio_other').prop('checked', true);				
			});
			
//			//其他金额输入框失去焦点
//			$('#diy_value').blur(function() {				
//				var _this = $(this);				
//				check.diyVal(_this);				
//			});
			
			//其他金额输入框的文本发生改变时
			$('#diy_value').bind("keydown", function(ev){
				
				if(navigator.userAgent.toLowerCase().indexOf('chrome') === -1){
					return;
				}
				
				var _this = $(this), 
				    reg = /\./g;
				
				if( (ev.shiftKey) || ($.inArray(ev.which, allowChrs) === -1) ){
					ev.preventDefault();
				}
				if(reg.test($(this).val()) && ev.which === 190){
					ev.preventDefault();
				}
			});
			$('#diy_value').bind("keyup",function(){
				
				var _this = $(this),
                    reg = /^([+-]?)\d*\.?\d+$/;				
				// 如果填写的不是数字(若最后一位是小数点，则将小数点去掉)
				if (!reg.test(_this.val().replace('.',''))) { _this.val(''); return false; }
					
				var index = _this.val().indexOf('.');				
				if(index != -1)
				{
					//判断输入的值是否有超过3位小数，如有，则四舍五入成两位小数	
					var subStr = _this.val().substring(index+1 , _this.val().length);
					if(subStr!= null && subStr.length >= 3 )
					{
						var formatPrice = (Math.round(parseFloat(_this.val(), 10) * 100 ) /100 ).toFixed(2);
						_this.val(formatPrice);// 格式化价格
					}
				}				
             });
			
			
			$("#radio_50").change(function(){
				if($('#radio_50').prop("checked")){
					$('#diy_valueTip').html('');					
					$('#diy_value').val('');
				}					
			});
			$("#radio_100").change(function(){
				if($('#radio_100').prop("checked")){
					$('#diy_valueTip').html('');					
					$('#diy_value').val('');
				}					
			});
			$("#radio_500").change(function(){
				if($('#radio_500').prop("checked")){
					$('#diy_valueTip').html('');					
					$('#diy_value').val('');
				}					
			});
			$("#radio_1000").change(function(){
				if($('#radio_1000').prop("checked")){
					$('#diy_valueTip').html('');					
					$('#diy_value').val('');
				}					
			});
			 
			
			// 点击“充值”
			$('#recharge_btn a').click(function(ev) {
				ev.preventDefault();
				var allowPay = true;
				if ($('#radio_other').prop('checked')) {
					allowPay = check.diyVal($('#diy_value'));
				}
				if (allowPay) {
				
					var tipsDialog = new Dialog();

					var tipsHtml = '<ul id="help_tips" style="text-align:center;">' +
					'	<li style="font-size:14px;font-weight:bold;margin-right:14px;margin-bottom:10px;background:url(../../images/single/note_zf.png) no-repeat;widht:17px;height:17px;">请在支付宝页面完成付款</li>' +
					'	<li style="color:#ccc;text-align:left">付款完成后请不要关闭此窗口。付款</li>' +
					'	<li style="color:#ccc;text-align:left;">完成后根据情况点击一下按钮。</li>' +
				
					'</ul>';
					
					tipsDialog.init({
						contentHtml: tipsHtml,
						yes: function() {
							//history.go(-1);
							window.location.href = 'payService_getPaymentList.action?menuFlag=caiwu_paymentList';
						},
						no: function() {
							window.location.href = 'payService_rechargeHelp.action?menuFlag=caiwu_rechargeHelp';
						},
						yesVal: '完成付款',
						noVal: '付款遇到问题'
						
					});
										
					$('#radio_other').val($('#diy_value').val());					
					// 新窗口提交表单
					$('#recharge_form').trigger('submit');
				}
			});
		};
		
		return {
			init: function() {
				recharge();
			}
		}
	})();
	
	rechargeOnline.init();
})