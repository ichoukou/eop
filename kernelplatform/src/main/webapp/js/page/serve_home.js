$(function() {
	var serveHome = (function() {
		var winParams = window.param || {};
		var winDetailParam = window.params || {};
		// 配置
		var config = {
			getValidityAction: winParams.getValidityAction || '',				// 获取效验码请求
			bindMobileAction: winParams.bindMobileAction || '',					// 绑定手机请求
			remindAction: winParams.remindAction || '',							// 提醒表单
			closeService: winParams.closeService || '',							// 关闭服务
			openService: winParams.openService || '',							// 开启服务
			onStep:winDetailParam.onStep
		};
		
		// 显示提示
		var showIcon = {
			correct: function(el, text) {
				el.html('<span class="yto_onCorrect">' + text + '</span>').show();
			},
			error: function(el, text) {
				el.html('<span class="yto_onError">' + text + '</span>').show();
			},
			show: function(el, text) {
				el.html('<span class="yto_onShow">' + text + '</span>').show();
			}
		};
		
		// 验证正则
		var reg = {
			mobileTel: /^1\d{10}$/,			// 验证手机号
			valCode: /^\S+$/				// 校验码
		};
		
		// 验证手机
		var check = {
			mobile: function(mobileVal) {
				if (reg.mobileTel.test(mobileVal)) {
					showIcon.correct($('#mobile_telTip'), '');
					
					return true;
				} else {
					showIcon.error($('#mobile_telTip'), '手机号码格式不正确');
					
					return false;
				}
			},
			valCode: function(codeVal) {
				if (!reg.valCode.test(codeVal)) {
					showIcon.error($('#validity_numTip'), '校验码格式错误');
					return false;
				} else {
					return true;
				}
			}
		};
		
		// 绑定手机
		var bindMobile = function() {
			var secInter,
				focusInter,
				initSec = 60;
			var mobileForm = '<form class="form" id="mobile_form">' +
									'	<p>' +
									'		<label for="mobile_tel" name="phone">手机号：</label>' +
									'		<input type="text" class="input_text" name="mobileTel" id="mobile_tel" />' +
									'		<span id="mobile_telTip"></span>' +
									'	</p>' +
									'	<p>' +
									'		<label for="validity_num">校验码：</label>' +
									'		<a title="免费获取" id="get_validity" class="btn btn_a" href="javascript:;"><span>免费获取</span></a>' +
									'		<input type="text" class="input_text" id="validity_num" />' +
									'		<span id="validity_numTip"><span class="yto_onShow">校验码<em>60</em>秒内有效</span></span>' +
									'	</p>' +
									'</form>';
			
			// 获取效验码
			$('#get_validity').live('click', function(ev) {
				ev.preventDefault();
			//	startDate = + new Date();
				// 先校验手机号
				var result = check.mobile($('#mobile_tel').val());
				if (result) {
					// 异步发送校验码
					$.ajax({
						url: config.getValidityAction + '?phone=' + $('#mobile_tel').val()+'&isCheck='+$('#isCheck').val(),
						cache: false,
						success: function(data) {
//							if(data){
//								window.location = 'payService_index.action?menuFlag=payService';
//								return;
//							} 
							if(data != null && data != '' && typeof data == 'string'){
								window.location = 'payService_index.action?menuFlag=home_payService';
							return;
							}
							clearInterval(secInter);
							initSec = 60;
							showIcon.show($('#validity_numTip'), '校验码<em>' + initSec + '</em>秒内有效');
							$('#validity_num').trigger('focus');
							// 有效时间递减
							secInter = setInterval(function() {
								if (initSec >= 0) {
									initSec--;
								}
							}, 1000);
						}
					})
				}
			});
			
			$('#validity_num').live('focus', function(ev) {
				focusInter = setInterval(function() {
					if (initSec >= 0) {
						showIcon.show($('#validity_numTip'), '校验码<em>' + initSec + '</em>秒内有效');
					}
				}, 1000);
			});
			
			// 未绑定手机
			$('#new_mobile').live('click', function(ev) {
				ev.preventDefault();
				
				var newMobileDialog = new Dialog();
				var flag=false;
				var initHtml = mobileForm+'<input type="hidden" value="0" name="isCheck" id="isCheck"></input>';
				newMobileDialog.init({
					contentHtml: initHtml,
					yes: function() {
						var mobileVal = $('#mobile_tel').val(),
							codeVal = $('#validity_num').val(),
							mobileR = check.mobile(mobileVal),
							codeR = check.valCode(codeVal);
			
						var mobileReplace=mobileVal.substring(0,2);
					      if(mobileReplace ==18){
					       mobileVal='15'+mobileVal.substring(2,mobileVal.length);
					       flag=true;
					      }
					     clearInterval(focusInter);
						if (mobileR && codeR) {
							$.ajax({
								url: config.bindMobileAction + '?menuFlag=home_payService&phone=' + mobileVal + '&flag='+flag+'&phoneCode=' + codeVal+'&initStartDate='+initSec,
								cache: false,
								success: function(data) {
									var validatorResult = data;
									
									if(typeof validatorResult == 'string'){
										window.location = 'payService_index.action?menuFlag=home_payService';
										return;
									}
									if (validatorResult) {
										newMobileDialog.close();
										$('#full_mobile').val(mobileVal);
										
										var mobileReplace=mobileVal.substring(0,2);
									      if(flag){
									    	  if(mobileReplace == 15){
									             mobileVal='18'+mobileVal.substring(2,mobileVal.length);
									    	  }
									      }
										var qian = mobileVal.substr(0, 3),
											hou = mobileVal.substr(mobileVal.length-4, 4),
											y = qian + '****' + hou;
										$('<span id="mobile_num">' + y +'</span>').insertBefore($('#new_mobile').parent());
										$('#no_bind').replaceWith('');
										$('#new_mobile').replaceWith('<a href="javascript:;" id="modify_mobile" >修改</a>'+
												                  '<a href="javascript:;" id="cancel_mobile" style="margin-left:5px;">取消绑定</a>');
										window.location = 'payService_index.action?menuFlag=home_payService';
											
									}else{
										$.ajax({
											dataType: 'JSON',
											url:'payService_phoneCodeCompare.action?phoneCode='+codeVal,
											type: 'GET',
											cache: false,
											success: function(data) {
												//alert(data+'\t'+'phoneCode')
												if(!data){
													showIcon.error($('#validity_numTip'), '输入校验码不正确');
												}
											}
										});	
										
										$.ajax({
											dataType: 'JSON',
											url:'payService_overTimePhoneCode.action?&phoneCode='+codeVal+'&initStartDate='+initSec,
											type: 'GET',
											cache: false,
											success: function(data) {
												//alert(data+'\t'+'校验码已经过期啦')
												if(data){
													showIcon.error($('#validity_numTip'), '校验码已过期.');
												}
											}
										});
									}
									
								}
							})
						}
					},
					yesVal: '绑 定',
					closeBtn: true
				});
				
				$.formValidator.initConfig({
					validatorGroup: '1',
					formID: 'mobile_form',
					theme: 'yto',
					errorFocus: false
				});
				
				// 手机号
				$('#mobile_tel').
					formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
					regexValidator({regExp: '^1\\d{10}$', onError: '手机号码格式不正确'});
				
				
			});
			
			// 修改绑定手机
			$('#modify_mobile').live('click', function(ev) {
				ev.preventDefault();
				
				var modifyMobileHtml = '<div id="modify_process">' +
										'	<ol>' +
										'		<li id="process_cur">1.核对手机号</li>' +
										'		<li id="">2.输入新手机号</li>' +
										'		<li id="process_last">3.修改成功</li>' +
										'	</ol>' +
										'</div>' +
										'<div id="modify_box">' +
											mobileForm + 
										'<input type="hidden" value="1" id="isCheck" name="isCheck"></input>'+
										'</div>';
				
				var modifyMobileDialog = new Dialog();
				
				modifyMobileDialog.init({
					contentHtml: modifyMobileHtml,
					closeBtn: true,
					yes: function() {
						var mobileVal = $('#mobile_tel').val(),
							codeVal = $('#validity_num').val(),
							mobileR = check.mobile(mobileVal),
							codeR = check.valCode(codeVal);					
						clearInterval(focusInter);
						if (mobileR && codeR) {
							$.ajax({
								//url: config.bindMobileAction + '?phone=' + mobileVal + '&phoneCode=' + codeVal,
								url: 'payService_checkPhone.action?phone=' + mobileVal + '&phoneCode=' + codeVal+'&initStartDate='+initSec,
								cache: false,
								success: function(data) {
									var validatorResult = data;
									
									if(typeof validatorResult == 'string'){
										window.location = 'payService_index.action?menuFlag=home_payService';
										return;
									}
									// 如果验证正确
									if (validatorResult) {
										modifyMobileDialog.close();

										var newMobileHtml = '<div id="modify_process">' +
															'	<ol>' +
															'		<li id="process_first">1.核对手机号</li>' +
															'		<li id="process_cur">2.输入新手机号</li>' +
															'		<li id="process_last">3.修改成功</li>' +
															'	</ol>' +
															'</div>' +
															'<div id="modify_box">' +
																mobileForm + 
															'<input type="hidden" value="0" id="isCheck" name="isCheck"></input>'+
															'</div>';
										clearInterval(secInter);
										
										initSec = 60;
										clearInterval(focusInter);
										var newMobile = new Dialog();
										
										newMobile.init({
											contentHtml: newMobileHtml,
											closeBtn: true,
											yes: function() {
											
												var mobileVal = $('#mobile_tel').val(),
													codeVal = $('#validity_num').val(),
													mobileR = check.mobile(mobileVal),
													codeR = check.valCode(codeVal);
												var flag=false;
												var mobileReplace=mobileVal.substring(0,2);
											      if(mobileReplace ==18){
											       mobileVal='15'+mobileVal.substring(2,mobileVal.length);
											       flag=true;
											      }
												if (mobileR && codeR) {
													$.ajax({
														url: config.bindMobileAction + '?phone=' + mobileVal + '&flag='+flag+'&phoneCode=' + codeVal+'&initStartDate='+initSec,
														cache: false,
														success: function(data) {
															var validatorResult = data;
															
															// 如果验证正确
															if (validatorResult) {
																newMobile.close();
																
																$('#full_mobile').val(mobileVal);
																
																var mobileReplace=mobileVal.substring(0,2);
															      if(flag){
															    	  if(mobileReplace == 15){
															             mobileVal='18'+mobileVal.substring(2,mobileVal.length);
															    	  }
															      }
																var qian = mobileVal.substr(0, 3),
																	hou = mobileVal.substr(mobileVal.length-4, 4),
																	y = qian + '****' + hou;
																$('#mobile_num').html(y);
																var confirmHtml = '<div id="modify_process">' +
																					'	<ol>' +
																					'		<li id="">1.核对手机号</li>' +
																					'		<li id="last_cur">2.输入新手机号</li>' +
																					'		<li id="last_pro">3.修改成功</li>' +
																					'	</ol>' +
																					'</div>' +
																					'<div id="modify_tips">' +
																					'	<p>手机绑定修改成功，新绑定手机号为：</p>' +
																					'	<p>' + y + '</p>' +
																					'</div>';
																// 第三步
																var confirmDialog = new Dialog();
																
																confirmDialog.init({
																	contentHtml: confirmHtml,
																	closeBtn: true,
																	yes: function() {
																		confirmDialog.close();
																	},
																	yesVal: '完 成'
																})
															}else{
																$.ajax({
																	dataType: 'JSON',
																	url:'payService_phoneCodeCompare.action?phoneCode='+codeVal,
																	type: 'GET',
																	cache: false,
																	success: function(data) {
																		//alert(data+'\t'+'phoneCode')
																		if(!data){
																			showIcon.error($('#validity_numTip'), '输入校验码不正确');
																		}
																	}
																});	
																
																$.ajax({
																	dataType: 'JSON',
																	url:'payService_overTimePhoneCode.action?&phoneCode='+codeVal,
																	type: 'GET',
																	cache: false,
																	success: function(data) {
																		//alert(data+'\t'+'校验码已经过期啦')
																		if(data){
																			showIcon.error($('#validity_numTip'), '校验码已过期.');
																		}
																	}
																});
																
															}
														   
														}
													})
												}
											},
											yesVal: '确认修改'
										});
										
										$('#mobile_tel').
										formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
										regexValidator({regExp: '^1\\d{10}$', onError: '手机号码格式不正确'});
										
									}else{			
										$.ajax({
											dataType: 'JSON',
											url:'payService_phoneCompare.action?phone='+mobileVal,
											type: 'GET',
											cache: false,
											success: function(data) {
												//alert(data+'\t'+'phone');
												if(!data){
													showIcon.error($('#mobile_telTip'), '手机号和原来不一致');
												}
											}
										});		
										
										$.ajax({
											dataType: 'JSON',
											url:'payService_phoneCodeCompare.action?&phoneCode='+codeVal,
											type: 'GET',
											cache: false,
											success: function(data) {
												//alert(data+'\t'+'phoneCode')
												if(!data){
													showIcon.error($('#validity_numTip'), '输入校验码不正确');
												}
											}
										});	
										
										$.ajax({
											dataType: 'JSON',
											url:'payService_overTimePhoneCode.action?&phoneCode='+codeVal+'&initStartDate='+initSec,
											type: 'GET',
											cache: false,
											success: function(data) {
												//alert(data+'\t'+'校验码已经过期啦')
												if(data){
													showIcon.error($('#validity_numTip'), '校验码已过期.');
												}
											}
										});
										
									}
									
								}
							})
						}
					},
					yesVal: '下一步'
				});
				
				
				
				$.formValidator.initConfig({
					validatorGroup: '1',
					formID: 'mobile_form',
					theme: 'yto',
					errorFocus: false
				});
				
				
				// 手机号
				$('#mobile_tel').
					formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
					regexValidator({regExp: '^1\\d{10}$', onError: '手机号码格式不正确'}).
					functionValidator({
						fun: function(val, el) {				
							$.ajax({
								dataType: 'JSON',
								url:'payService_phoneCompare.action?phone='+val,
								type: 'GET',
								cache: false,
								success: function(data) {
									//console.log(typeof data)
									if(!data){
										//alert("手机号是不是不匹配");
										showIcon.error($('#mobile_telTip'), '手机号和原来不一致');
									}
								}
							});
						},
						onError: '手机号不匹配'
					});
			});
		};
		
	//取消绑定
		var cancelPhontBind = function(){
		 $('#cancel_mobile').live('click', function(ev) {
			 ev.preventDefault(); 
			  var confirmDialog = new Dialog();
				confirmDialog.init({
					contentHtml: '取消绑定以后将不能收到任何短信提醒,你确定取消吗?',
					yes: function() {							
						//window.location = 'payService_cancelPhoneBind.action?menuFlag=payService';	
						$.ajax({
							 url: 'payService_cancelPhoneBind.action?menuFlag=home_payService',
							 cache: false,
							 success:function(data){
								 if(data){
									 confirmDialog.close();//关闭弹出的层
									 window.location='payService_index.action?menuFlag=home_payService'
								 }
							 }
						});
					},
					no: function() {
						confirmDialog.close();
					}
				});
		    });
		 };
		
		// 修改提醒
		var remindSave = function() {
			var flag = true;
			$('#remind_save').click(function(ev) {
				ev.preventDefault();	
				  if(flag){
					  flag = false;
					  $.ajax({
							url: config.remindAction,
							cache: false,
							type: 'POST',
							data: {
								serveExpir: true,//服务到期提醒
								smsInadequate: $('#remind_b').prop('checked'),//短信不足
								smsRemindCount: $('#sms_remind').val(),//短信不足条数
								balanceRemind: $('#remind_c').prop('checked'),//余额不足提醒
								balanceCound: $('#balance_count').val(),//余额不足
								transactionRemind: true//交易提醒
							},
						  success:function(data){
							  flag = true;
							  var confirmDialog = new Dialog();
							  if(typeof data == 'string'){
									window.location = 'payService_index.action?menuFlag=home_payService';
									return;
								}
							  if(data){
								  confirmDialog.init({
										contentHtml: '修改成功.',
										yes: function() {							
											confirmDialog.close();
										},
										no: function() {
											confirmDialog.close();
										}
									});
							  }
						  }
						})
				  }	
			})
		};
		//收支明细
		var paymentDetail = function(){
			$("#payment_detail").click(function(ev){
				ev.preventDefault();
				window.location = 'payService_getPaymentList.action?menuFlag=caiwu_paymentList&currentPage=1';
				//window.open('payService_getPaymentList.action?menuFlag=caiwu_paymentList&currentPage=1');
			})
		};
		
		//立即使用
		var toService = function(){
			$('.btn_a.toService').live('click',function(ev){
				ev.preventDefault();
				var _this = $(this),
				serveCode = $('.serve_id', _this.parent()).val(),
				tr = _this.parents('tr'); 
				if(serveCode == 6){
					window.location='passManage_warnningIndex.action?menuFlag=chajian_passManage_warn';
				}else if(serveCode == 5){
					window.location='payService_applySmsService.action?serviceId=5';
				}
			});
			
		}
		
		// 开通/关闭服务
		var onOffServe = function() {
			$('.btn_a.close_serve').live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
					serveCode = $('.serve_code', _this.parent()).val(),
					openFromFlag = $('.openFromFlag',_this.parent()).val(),
					hasPayed = $('.has_payed', _this.parent()).val(),
					serveMeta = $('.serve_meta', _this.parent()).val(),
					tr = _this.parents('tr');
				
				if(openFromFlag == '1'){
					var aDialog = new Dialog();
					aDialog.init({
							contentHtml: '亲，这功能不是您开启的，您没有权限操作！',
							yes: function() {
								aDialog.close();
							  return;
							},
							no: function() {
								aDialog.close();
							}
						});
					return;
				}
				
				var confirmDialog = new Dialog();
				
				confirmDialog.init({
					contentHtml: '服务关闭后,需要重新订购才可使用,你确定要关闭吗？',
					yes: function() {
						$.ajax({
							url: config.closeService + '?serviceId=' + serveCode,
							cache: false,
							success: function(data) {
								// 关闭弹层
								confirmDialog.close();
								window.location = 'payService_index.action?menuFlag=home_payService';
							}
						});
					},
					no: function() {
						confirmDialog.close();
					},
					iconType: 'question'
				});
			
			});
			//关闭
			$('.bta.close_serve').live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
					serveCode = $('.serve_code', _this.parent()).val(),
					openFromFlag = $('.openFromFlag',_this.parent()).val(),
					hasPayed = $('.has_payed', _this.parent()).val(),
					serveMeta = $('.serve_meta', _this.parent()).val(),
					tr = _this.parents('tr');
				
				if(openFromFlag == '1'){
					var aDialog = new Dialog();
					aDialog.init({
							contentHtml: '亲，这功能不是您开启的，您没有权限操作！',
							yes: function() {
								aDialog.close();
							  return;
							},
							no: function() {
								aDialog.close();
							}
						});
					return;
				}
				
				var confirmDialog = new Dialog();
				
				confirmDialog.init({
					contentHtml: '服务关闭后,需要重新订购才可使用,你确定要关闭吗？',
					yes: function() {
						$.ajax({
							url: config.closeService + '?serviceId=' + serveCode,
							cache: false,
							success: function(data) {
								// 关闭弹层
								confirmDialog.close();
								window.location = 'payService_index.action?menuFlag=home_payService';
							}
						});
					},
					no: function() {
						confirmDialog.close();
					},
					iconType: 'question'
				});
			
			});
		
	   //立即使用
			$('.btn_a.use_now').live('click', function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
				   serveCode = $('.serve_code', _this.parent()).val(),
				   tr = _this.parents('tr');
				
					window.location = 'payService_applyService.action?menuFlag=home_payService&serviceId=' + serveCode;		
				
			});
		//续费
			$('.bta.renewals').live('click',function(ev){
				ev.preventDefault();
				var _this = $(this),
				   serveCode = $('.serve_code', _this.parent()).val(),
				   openFromFlag = $('.openFromFlag',_this.parent()).val(),
				   tr = _this.parents('tr');

				if(openFromFlag == '1'){
					var aDialog = new Dialog();
					aDialog.init({
							contentHtml: '亲，这功能不是您开启的，您没有权限操作！',
							yes: function() {
								aDialog.close();
							  return;
							},
							no: function() {
								aDialog.close();
							}
						});
					return;
				}
				
				window.location = 'payService_applyService.action?menuFlag=home_payService&serviceId='+serveCode;
			});
			
		//服务详情
			$('#detail1').live('click',function(ev){
				ev.preventDefault();
				var _this = $(this),
				   serveCode = $('.serve_code', _this.parent()).val(),
				   tr = _this.parents('tr');
				var smsFlag = $('#smsFlag').val();
				var menuFlag = '';
				if(serveCode == 5){
					menuFlag = 'sms_home';
				}else if(serveCode == 6){
					menuFlag = 'chajian_passManage_warn';
				}
				window.open('payService_seeDetails.action?menuFlag='+menuFlag+'&serviceId='+serveCode);
			});
			
			//时效提醒详情
			$('#detail2').live('click',function(ev){
				ev.preventDefault();
				var _this = $(this),
				   serveCode = $('.serve_code', _this.parent()).val(),
				   tr = _this.parents('tr');
				var smsFlag = $('#smsFlag').val();
				var menuFlag = '';
				if(serveCode == 5){
					menuFlag = 'sms_home';
				}else if(serveCode == 6){
					menuFlag = 'chajian_passManage_warn';
				}
				window.open('payService_seeDetails.action?menuFlag='+menuFlag+'&serviceId='+serveCode);
			});
			
	   //付款或关闭
			$('.payorclose').live('click',function(ev){
				ev.preventDefault();
				var _this = $(this),
				   serveCode = $('.serve_code', _this.parent()).val(),
				   tr = _this.parents('tr'); 
				//alert(serveCode +"\t"+'aaa');
				window.location = 'payService_payOrClose.action?menuFlag=caiwu_paymentList&serviceId='+serveCode;
			});
			
		};
	    
		//开始使用
		 $("#startApplay").live('click',function(ev){
			 ev.preventDefault();
			 alert("此功能已暂停!");
			return ;
			
			 var serviceId = $('#serviceId').val();
			 var paymentId = $('#paymentId').val();
			 var smsFlag = $('#smsFlag').val();
			 var source = $('#source').val();
			 if(paymentId == null || paymentId == ''){
				// window.location = 'payServiceDetil_applyServiceFromDetail.action?menuFlag=home_payService&serviceId=' + serviceId;
				 if(smsFlag == '1'){
						window.location = 'alipay_applyServiceFromDetail.action?menuFlag=home_payService&serviceId=' + serviceId+'&smsFlag='+smsFlag;
					}else{
						if(config.onStep < 3){
							
							window.location = 'payServiceDetil_applyServiceFromDetail.action?menuFlag=chajian_passManage_warn&serviceId=' + serviceId;
						}else{
							window.location = 'payServiceDetil_applyServiceFromDetail.action?menuFlag=home_payService&serviceId=' + serviceId+'&source='+source;
						}
					}
			 }else{
				 var confDialog = new Dialog();
				 confDialog.init({
						contentHtml: '你已有开通时效提醒的订单，请到收支明细付款或关闭订单',
						yes: function() {
							confDialog.close();
							window.location = 'payService_getPaymentList.action?menuFlag=caiwu_paymentList&id='+paymentId;
						},
						no: function() {
							confDialog.close();
						}
					});
			 }
		 });
		 var f_close = function() {
			 $(".f_close").click(function(){
				 $(".pop_tip").css({display:"none"});
			 });
		 };		 
		
		return {
			init: function() {
				bindMobile();
				remindSave();
				paymentDetail();
				onOffServe();
				toService();
				cancelPhontBind();
				f_close();
			}
		}
	})()
	
	serveHome.init();
})