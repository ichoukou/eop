$(function() {
	
	var winParams = window.params || {};

	// 全局配置
	var config = {
			buyPortsAction: winParams.buyPortsAction || ''			// 保存搜索条件 Action
							// 短信数量请求
	};
	
	var openService = (function() {
		
	 var smsOrder = function(){
		 //var money = $("#portPrice").val();//短信服务高级套餐的价格
		 
		 var flag = true;
		 $('.btn_buy').click(function(ev){
				ev.preventDefault();
				if (flag) {
					flag = false;
			    	 var _this = $(this),
			    	 	flagPackage = $('.flag', _this.parents('.opt')).val();
			    	 $.ajax({
							url: 'smsServicePackage_getPackageByFlag.action',
							cache: false,
							data: {flag : flagPackage},
							dataType:'JSON',
							success: function(data) {
								
								portName = data.portName;
								portNum = data.portNum;
								portPrice = data.portPrice;
								
								if (portName == undefined || portName == '') {
									var wDialog = new Dialog();
									wDialog.init({
										contentHtml: '不存在此短信套餐！',
										closeBtn: true,
										yes: function() {
											wDialog.close();
										}													
									});
								} else {
									
									//返回URL
									var pos = $("#_pos").val();
									var currentPage = $("#_currentPage2").val();
									var smsUrl = "smsHomeEvent_homePage.action?menuFlag=sms_home";
									if('1'==pos) {  //问题件提醒		
										var backStratDate = $("#_backStratDate").val();
										var backEndDate = $("#_backEndDate").val();
										var backShopName = $("#_backShopName").val();
										var backQType = $("#_backQType").val();
										var backQTypeVal = $("#_backQTypeVal").val();
										var backIsShowSigned = $("#_backIsShowSigned").val();
										var backInput = $("#_backInput").val();
										var backTabStatus = $("#_backTabStatus").val();
										var parm = "&currentPage2="+currentPage+"&starttime="+backStratDate+"&endtime="+backEndDate+"&bindUserCustomerId="+backShopName+"&feedbackInfoStr="+backQType+"&feedbackInfo="+backQTypeVal+"&isShowSigned="+backIsShowSigned+"&conditionString="+backInput+"&tabStatus="+backTabStatus+"&pos=1";
										smsUrl = "questionnaire_list.action?menuFlag=chajian_question"+parm;
									}
									if('2'==pos) {   //时效提醒
										var backInput = $("#_backInput").val();
										var parm = "&pos=2&currentPage2="+currentPage+"&queryCondition="+backInput;
										smsUrl = "passManage_list_seller.action?menuFlag=chajian_passManage_warn"+parm;
									}
									if('3'==pos) {   //智能查件
										var logisticsIds = $("#_logisticsIds").val();
										var num = $("#_num").val();
										var isCheck = $("#_isCheck2").val();
										var parm = "&currentPage="+currentPage+"&logisticsIds="+logisticsIds+"&isCheck="+isCheck+"&mailNum="+num+"&pos=3";
										smsUrl = "waybill_list.action?menuFlag=chajian_waybill"+parm;
									}
									if('down'==pos) {
										var serviceId = $("#_serviceId").val();
										smsUrl = "smsHomeEvent_openService.action?menuFlag=sms_home&serviceId="+serviceId + "&pos="+pos;
									}
									
									smsUrl = encodeURI(smsUrl); 
									
									var resetPay = 0;
									$.ajax({
										url: 'smsServicePackage_getUserBalance.action',
										cache: false,
										dataType:'JSON',
										success: function(data) {
											flag = true; 
											if(data.balance == 0.0 ){
												resetPay = portPrice - data.useBalance;
											}else{
												resetPay = portPrice - (data.useBalance+data.balance);
											}
											var rePay = portPrice - data.useBalance;
											//if 开始
										    if(resetPay > 0){
											    var confirmHtml = '<form action="smsServicePackage_toAlipay.action" id="sms_order" method="post" target="_blank">' +
															      '	<p class="dialog_p">用户名：' + data.userName + '</p>' +
																  '	<p class="dialog_p">账户余额：' + data.useBalance.toFixed(2) + ' 元</p>' +
																  '	<p class="dialog_p">应付金额：' + portPrice + ' 元</p>' +
																  ' <input type="hidden" value="'+ flagPackage +'" name="flag">' +
																  ' <input type="hidden" value="5" name="serviceId">'+
																  '	<em id="gudie_desc">您的账户余额不够完成此次支付，易通将引导您至支付宝页面完成剩余款项付款。</em>' +
																  '	<p id="to_pay">使用支付宝账户支付<span>' + rePay.toFixed(2) + '</span>元</p>' +
																  '</form>';
											    
										   var conDialog = new Dialog();
										   conDialog.init({
												contentHtml: confirmHtml,
												closeBtn: true,
												yes: function() {
													$("#sms_order").trigger('submit');
													conDialog.close();
													var tipsDialog = new Dialog();
													
													var tipsHtml = '<ul id="help_tips" style="text-align:center;">' +
																	'	<li style="font-size:14px;font-weight:bold;margin-right:14px;margin-bottom:10px;background:url(../../images/single/note_zf.png) no-repeat;widht:17px;height:17px;">请在支付宝页面完成付款</li>' +
																	'	<li style="color:#ccc;text-align:left">付款完成后请不要关闭此窗口。付款</li>' +
																	'	<li style="color:#ccc;text-align:left;">完成后根据情况点击一下按钮。</li>' +
																
																	'</ul>';

													tipsDialog.init({
														
														contentHtml: tipsHtml,
														yes: function() {											
															window.location.href = smsUrl;
														},
														no: function() {
															window.location.href = 'payService_rechargeHelp.action?menuFlag=caiwu_rechargeHelp';
														},
														yesVal: '完成付款',
														noVal: '付款遇到问题'
													});													
												},
												yesVal: '去支付宝支付'		
												})
											//if 结束
										     } else {
										    	var conDialog = new Dialog();
										    	conDialog.init({
													contentHtml: '<div class="buy_note"><p>是否确定付款？</p><p>确定，系统将从您的账户扣除金额' + portPrice + '元。</p></div>',
													yes: function() {
														conDialog.close();
														$.ajax({
															url: config.buyPortsAction,
															type: 'POST',
															data: {
																portName: portName,
																portNum: portNum,
																portPrice: portPrice
															},
															cache: false,
															dataType:'JSON',
															success: function(portData) {
																if(portData == 0) {
																	var succDialog = new Dialog();
																	succDialog.init({
																		contentHtml: '恭喜你，短信套餐购买成功！',
																		/*yes: function() {
																			succDialog.close();
																			window.location.href = 'payService_getPaymentList.action?menuFlag=caiwu_paymentList';
																		},*/
																		no: function() {
																			succDialog.close();
																			window.location.href = smsUrl;
																		},
																		noVal: '确定'   //返回短信服务
																		//yesVal: '查看收支明细'
																	});
																} else {
																	var errDialog = new Dialog();
																	errDialog.init({
																		contentHtml: '系统繁忙，请稍后再试！',
																		closeBtn: true,
																		yes: function() {
																			errDialog.close();
																		}													
																	});
																}
															}
												    	})
													},
													yesVal: '确定',		
													no: function() {
														conDialog.close();
													},
													noVal: '取消'		
													})
										     }
										}
									});
								}
							}
			    	 });
				}
			});
	  }	
		return {
			init: function() {
				smsOrder();
			}
		}
	})();
	
	openService.init();
})