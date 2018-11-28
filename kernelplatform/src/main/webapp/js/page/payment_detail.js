$(function() {
	var balanceDetail = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			payBillAction: winParams.payBillAction || ''					// 付款请求
		};
		
		
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
		
		// 验证方法
		var check = {
			/**
			 * @description 检查起始日期的距今时间最大值
			 * @param startVal {String} 要检查的时间 yyyy-MM-dd
			 * @return {Boolean} true 日期格式正确 / false 日期格式错误
			**/
			maxDate: function(startVal) {
				// 系统时间
				var sysDate = new Date(),
					sysDateY = parseInt(sysDate.getFullYear(), 10),
					sysDateM = parseInt(sysDate.getMonth(), 10) + 1,
					sysDateD = parseInt(sysDate.getDate(), 10),
					sysUTC = Date.UTC(sysDateY, sysDateM, sysDateD);
				
				// 用户选择时间
				var formatDate = startVal.split('-'),
					formatDateY = parseInt(formatDate[0], 10),			// 年
					formatDateM = parseInt(formatDate[1], 10),			// 月
					formatDateD = parseInt(formatDate[2], 10),			// 日
					formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
				
				// 用户选择时间离系统时间的天数
				var dateGap = parseInt((sysUTC - formatUTC) / 1000 / 60 / 60 / 24, 10);
				
				// 如果超过一年
				if (dateGap > 365) {
					return false;
				} else {
					return true;
				}
			},
			/**
			 * @description 检查结束日期与起始日期的时间范围
			 * @param startVal {String} 起始日期 yyyy-MM-dd
			 * @param endVal {String} 终止日期 yyyy-MM-dd
			 * @return {Boolean} true 日期格式正确 / false 日期格式错误
			**/
			rangeDate: function(startVal, endVal) {
				//var startVal = els.dateStart.val();
				//this.maxDate(startVal);
				if (startVal != '') {
					var startDate = startVal.split('-'),
						startDateY = parseInt(startDate[0], 10),			// 年
						startDateM = parseInt(startDate[1], 10),			// 月
						startDateD = parseInt(startDate[2], 10),			// 日
						startUTC = Date.UTC(startDateY, startDateM, startDateD);
						
					var formatDate = endVal.split('-'),
						formatDateY = parseInt(formatDate[0], 10),			// 年
						formatDateM = parseInt(formatDate[1], 10),			// 月
						formatDateD = parseInt(formatDate[2], 10),			// 日
						formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
						
					var dateGap = parseInt((formatUTC - startUTC) / 1000 / 60 / 60 / 24, 10);

					// 如果超过一年
					if (dateGap > 365) {
						return false;
					} else {
						return true;
					}
				}
			}
		};
		
		// 表单验证
		var form = function() {
		
			// 元素集合
			var els = {
				dateTip: $('#dateTip'),
				dateStart: $('#date_start'),
				dateEnd: $('#date_end'),
				searBtn: $('#sear_btn'),
				searForm: $('#sear_form')
			};
			
			var msg = {
				maxDateErr: '只能查近一年的数据',
				rangeDateErr: '查询起止日期必须在一年内'
			};
			
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
							//checkMaxDate = check.maxDate(dateVal);
						
						//if (checkMaxDate) {
						//	
						//	showIcon.correct(els.dateTip, '');
						//	els.dateEnd.prop('disabled', false);
							$dp.$('date_end').focus();
						//} else {
						//	
						//	showIcon.error(els.dateTip, msg.maxDateErr);
						//	_this.blur();
						//	_this.focus();
						//	els.dateEnd.prop('disabled', true);
						//}
					},
					startDate: '#F{$dp.$D(\'date_end\')}',
					//minDate: '%y-%M-{%d-365}',		// 最小时间：一年
					//maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			els.dateEnd.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkRangeDate = check.rangeDate(els.dateStart.val(), dateVal);
						
						if (checkRangeDate) {
							
							showIcon.correct(els.dateTip, '');
						} else {
							
							showIcon.error(els.dateTip, msg.rangeDateErr);
						}
						
						_this.blur();
					},
					startDate: '#F{$dp.$D(\'date_start\')}',
					minDate: '#F{$dp.$D(\'date_start\')}',	// 终止日期大于起始日期
					//maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 开始日期
			//els.dateStart.
			//	formValidator({validatorGroup:'1', tipID: 'dateTip', onShow: '', onFocus: '', onCorrect: ' '}).
			//	functionValidator({fun: function(val) {
			//		if (!check.maxDate(val)) {
			//			return false;
			//		}
			//	}, onError: msg.maxDateErr});
				
			// 结束日期
			els.dateEnd.
				formValidator({validatorGroup:'1', tipID: 'dateTip', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.rangeDate(els.dateStart.val(), val)) {
						return false;
					}
				}, onError: msg.rangeDateErr});
				
				
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();	
				
				//判断查询条件是否发生更改,若发生更改，将当前表单中currentPage当前页的value值改为1
				var initvalue = $("#initValue").val();
				var startTime = $("#date_start").val();
				var endTime = $("#date_end").val();
				var dealType = $("#dealType").val();				
				if(initvalue != 'startTime:'+startTime+';endTime:'+endTime+';dealType:'+dealType)	{
					$('#currentPage').val(1);
				}
				
				els.searForm.trigger('submit');
			});
			
			//翻页
			pagination.live('click',function(){			
				$('#currentPage').val($(this).attr("value"));
				setTimeout(function(){
					$("#sear_form").trigger('submit');
				},0);
			});
			
		};
		
		// 付款
		var pay = function() {
			
			$('.pay_bill').click(function(ev) {
				ev.preventDefault();
				//wangmindong_message
				//alert("此功能已暂停!");
				//return ;
				
				var _this = $(this),
					money = parseFloat($.trim($('.tran_amount', _this.parents('tr')).html().replace(',','')), 10),
					dealMoney = parseFloat($.trim($('.dealMoney', _this.parents('tr')).val().replace(',','')), 10),
					dealType = $.trim($('.tran_type', _this.parents('tr')).html());
				    shopName = $.trim($('.shop_name', _this.parents('tr')).html());				   
				    if(shopName.length == 0) {
				    	shopName = $.trim($('.td_f', _this.parents('tr')).html());
				    }
				    
					var resetPay = 0;
				
				if(dealType == '在线充值'){
					var id = _this.prev().val();
					//window.open('alipay_onlinePay.action?id=' + id);
					window.location.href = 'alipay_onlinePay.action?id=' + id ;
//					var tipsDialog = new Dialog();
//					
//					var tipsHtml = '<ul id="help_tips" style="text-align:center;">' +
//					'	<li style="font-size:14px;font-weight:bold;margin-right:14px;margin-bottom:10px;background:url(../../images/single/note_zf.png) no-repeat;widht:17px;height:17px;">请在支付宝页面完成付款</li>' +
//					'	<li style="color:#ccc;text-align:left">付款完成后请不要关闭此窗口。付款</li>' +
//					'	<li style="color:#ccc;text-align:left;">完成后根据情况点击一下按钮。</li>' +
//					'</ul>';
					
//					tipsDialog.init({
//						contentHtml: tipsHtml,
//						yes: function() {							
//							$("#sear_form").trigger('submit');
//						},
//						no: function() {
//							window.location.href = 'payService_rechargeHelp.action?menuFlag=caiwu_rechargeHelp';
//						},
//						yesVal: '完成付款',
//						noVal: '付款遇到问题'
//					});
				}else{
					
					$.ajax({
						url: 'payService_getBalance.action',
						cache: false,
						success: function(data) {
							resetPay = dealMoney - (data.balance+data.useBalance);
							 //如果数据有问题，则弹出提示框
							if(isNaN(resetPay))
							{  var dialog = new Dialog();
							     dialog.init({ contentHtml: '您所造的数据有问题，请检查！',
							    	           yes: function() {dialog.close();},
							    	           yesVal: '确定',closeBtn: false});
							    return false;
							}
							
							if(data.balance == 0.0 ){
								resetPay = dealMoney - data.useBalance;
							}else{
								resetPay = dealMoney - (data.useBalance+data.balance);
							}
							var rePay = dealMoney - data.useBalance;
							
							if (resetPay > 0) {	// 如果余额不够支付
								var resetDialog = new Dialog();
								var id = _this.prev().val();
								var resetHtml = '<form action="alipay_onlinePay.action" id="pay_form" >' +	//target="_blank"
								                '<p class="dialog_p">用户名：'+shopName+'</p>'+
												'	<p class="dialog_p">应付金额：' + money.toFixed(2) + ' 元</p>' +
												'  <input type="hidden" value="' +id +'" name="id"></input>'+
												'  <input type="hidden" value="'+rePay.toFixed(2)+'" name="resetPay"></input>'+
												'	<p class="dialog_p">账户余额：' + data.useBalance.toFixed(2) + ' 元</p>' +
												'	<em id="gudie_desc">您的账户余额不够完成此次支付，易通将引导您至支付宝页面完成剩余款项付款。</em>' +
												'	<p id="to_pay">使用支付宝账户支付<span>' + dealMoney + '</span>元</p>' +
												'</form>';
								resetDialog.init({
									contentHtml: resetHtml,
									yes: function() {							
									$("#pay_form").trigger('submit');
									resetDialog.close();
//									var tiDialog = new Dialog();
									
//									var tipsHtml = '<ul id="help_tips" style="text-align:center;">' +
//									'	<li style="font-size:14px;font-weight:bold;margin-right:14px;margin-bottom:10px;background:url(../../images/single/note_zf.png) no-repeat;widht:17px;height:17px;">请在支付宝页面完成付款</li>' +
//									'	<li style="color:#ccc;text-align:left">付款完成后请不要关闭此窗口。付款</li>' +
//									'	<li style="color:#ccc;text-align:left;">完成后根据情况点击一下按钮。</li>' +
//									'</ul>';
									
//									tiDialog.init({
//										contentHtml: tiHtml,
//										yes: function() {											
//											$("#sear_form").trigger('submit');
//										},
//										no: function() {
//											window.location.href = 'payService_rechargeHelp.action?menuFlag=caiwu_rechargeHelp';
//										},
//										yesVal: '完成付款',
//										noVal: '付款遇到问题'
//									});
									},
									yesVal: '去支付宝付款',
									closeBtn: true
								})
							} else {	// 如果余额足够支付
								var confirmDialog = new Dialog();								
								confirmDialog.init({									
									contentHtml: '确定后系统将从您的账户扣除金额'+dealMoney+'元。',
									yes: function() {							
										var id = _this.prev().val();	
										window.location = 'alipay_onlinePay.action?id='+id+'&resetPay='+rePay;		
									},
									no: function() {
										confirmDialog.close();
									}
								});
							 }
						    }
						 })
				}	//end else 
			
			})
		};
		

		$('.close_bill').click(function(ev) {
			ev.preventDefault();
			
			var _this = $(this);
			
			var confirmDialog = new Dialog();
			
			confirmDialog.init({
				contentHtml: '<p>您确定要关闭该订单吗？</p>'+
					         '<p>关闭订单后，不能恢复</p>',
				yes: function() {	
					var id = _this.prev().prev().val();	
					$.ajax({
							type:'post',//可选get
							url: 'payService_closePayment.action',
							data:'id='+id,
							dataType:'Json',//服务器返回的数据类型 可选XML ,Json jsonp script html text等
							success:function(msg){
								if(msg=='关闭成功')
								{									
									confirmDialog.close();
									$("#sear_form").trigger('submit');
								}
							},
							error:function(){
							   alert("关闭失败");							   
							}
							})
					
				},
				no: function() {
					confirmDialog.close();
				}
			});
		});
		
		
		return {
			init: function() {
				form();
				pay();
				
				var arr = new Array();
			    arr[0] = $('#payment_id').val();
				
				if(arr!=null) {
					var rows = $('table tr');
					rows.each(function(){
					    var _row = $(this),
					        idVal = _row.find('.td_a span').html();
					    if($.inArray(idVal, arr) != -1){
					       _row.css('background','#e6f0ff');
					    }
//					    if(idVal==arr){
//					       _row.css('background','#e6f0ff');
//					    }
					});
				}
			}
		};
		
	})();
	
	
	balanceDetail.init();
})