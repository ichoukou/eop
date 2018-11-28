$(function() {
	(function($) {
		$.fn
				.extend({
					insertContent : function(myValue, t) {
						var $t = $(this)[0];
						if (document.selection) { // ie
							this.focus();
							var sel = document.selection.createRange();
							sel.text = myValue;
							this.focus();
							sel.moveStart('character', -l);
							var wee = sel.text.length;
							if (arguments.length == 2) {
								var l = $t.value.length;
								sel.moveEnd("character", wee + t);
								t <= 0 ? sel.moveStart("character", wee - 2 * t
										- myValue.length) : sel.moveStart(
										"character", wee - t - myValue.length);
								sel.select();
							}
						} else if ($t.selectionStart
								|| $t.selectionStart == '0') {
							var startPos = $t.selectionStart;
							var endPos = $t.selectionEnd;
							var scrollTop = $t.scrollTop;
							$t.value = $t.value.substring(0, startPos)
									+ myValue
									+ $t.value.substring(endPos,
											$t.value.length);
							this.focus();
							$t.selectionStart = startPos + myValue.length;
							$t.selectionEnd = startPos + myValue.length;
							$t.scrollTop = scrollTop;
							if (arguments.length == 2) {
								$t.setSelectionRange(startPos - t,
										$t.selectionEnd + t);
								this.focus();
							}
						} else {
							this.value += myValue;
							this.focus();
						}
					}
				})
	})(jQuery);
	
	var newSms = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			smsTypeResult: winParams.smsTypeResult || false
		};
		
		var labelKeyA = $('a', '#label_key ul li:gt(0)');
		if ($("#sms_type").find('option:selected').text() == '营销定制') {
			labelKeyA.addClass('disable_a');
		}
		
		// 插入标签
		var insertLabel = function() {
			var smsContent = $('#sms_content'),
				fontCount = $('#font_count'),
				shop = $('#shop').val(),
				//order = $('#order').val(),
				//send = $('#send').val(),
				//ticket = $('#ticket').val(),
				//company = $('#company').val(),
				contentTip = $('#sms_contentTip'),
				min = 1,
				max = 200;
			
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
			
			var msg = {
				contentEmptyErr: '模板内容不能为空',
				contentLongErr: '模板内容不能超过200字符'
			};
			
			$('#label_key li a:not(".disable_a")').live('click', function(ev) {
			
				ev.preventDefault();
				/*var smsContent = $('#sms_content').val(),
					label = '[' + $(this).text() + ']';
					
				$('#sms_content').insertContent(label);*/ 
				//$('#sms_content').val(smsContent + label);
				
				var _this = $(this),
		        	count = 0,
		        	type = _this.attr('class'),
		        	lab = '',
		        	reg = /<\/?[^>]+>/gi,
				    content = '';
		
				if(_this.hasClass('shop_name')){
					count = shop.length;
					lab = '<img class="shop" src=".../../images/single/shop.png" />';
				}
				else if(_this.hasClass('order_time')){
					count = 11/*order.length*/;
					lab = '<img class="order" src=".../../images/single/order.png" />';
				}
				else if(_this.hasClass('send_time')){
					count = 11/*send.length*/;
					lab = '<img class="send" src=".../../images/single/send.png" />';
				}
				else if(_this.hasClass('ticket_id')){
					count = 10/*ticket.length*/;
					lab = '<img class="ticket" src=".../../images/single/ticket.png" />';
				}
				else if(_this.hasClass('company_name')){
					count = 4/*company.length*/;
					lab = '<img class="company" src=".../../images/single/company.png" />';
				}
				
				content = smsContent.html().replace(reg, function(html){				
					return (html.indexOf('br') === -1 && html.indexOf('end_image') === -1) ? html : '';
				});
				smsContent.html(content + lab + '<img src="#" class="end_img" />');
				count = parseInt(fontCount.text(), 10) + count;
				fontCount.text(count);
				
				if(count > max){
					showIcon.error(contentTip, msg.contentLongErr);
					smsContent.data('validate', false);
				}
				else{
					showIcon.correct(contentTip, ' ');
					smsContent.data('validate', true);
				}
			});
			
			$('#sms_content').keyup(function(ev){
				var _this = $(this),
			    labs = 0;
			
				_this.find('img').each(function(){
					var sp = $(this);
					if(sp.hasClass('shop')){
						labs += shop.length;						
					}
					else if(sp.hasClass('order')){
						labs += 11/*order.length*/;
					}
					else if(sp.hasClass('send')){
						labs += 11/*send.length*/;
					}
					else if(sp.hasClass('ticket')){
						labs += 10/*ticket.length*/;
					}
					else if(sp.hasClass('company')){
						labs += 4/*company.length*/;
					}
				});
				
				var fonts = _this.text().length + labs;
				fontCount.text(fonts);

				if(fonts < min){
					showIcon.error(contentTip, msg.contentEmptyErr);
					smsContent.data('validate', false);
				}
				else if(fonts > max){
					showIcon.error(contentTip, msg.contentLongErr);
					smsContent.data('validate', false);
				}
				else{
					showIcon.correct(contentTip, ' ');
					smsContent.data('validate', true);
				}
			});
		};
		
		// 表单验证
		var form = function() {
			var els = {
				submit: $('#submit'),
				newSmsForm: $('#new_sms_form'),
				smsTitle: $('#sms_title'),
				smsType: $('#sms_type'),
				smsContent: $('#sms_content'),
				typeTip: $('#sms_typeTip'),
				shop: $('#shop').val()
				//order: $('#order').val(),
				//send: $('#send').val(),
				//ticket: $('#ticket').val(),
				//company: $('#company').val()
			};
			els.smsContent.data('validate', false);
			var msg = {
				titleErr: '2-20个字符，支持中文、字母、数字。',
				smsTypeErr: '请选择短信类型'
			};
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'new_sms_form',
				theme: 'yto',
				errorFocus: false
			});
			
			els.smsTitle.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: '^[a-zA-Z0-9\\u4E00-\\u9FA5\\uF900-\\uFA2D]{2,20}$', onError: msg.titleErr});
			
			/*els.smsContent.
			formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
			inputValidator({min: 1, max: 400, onErrorMin: msg.contentEmptyErr, onErrorMax: msg.contentLongErr});*/
				
			
			els.submit.click(function(ev) {
				ev.preventDefault();
				
				var validatorResult = $.formValidator.pageIsValid(1) && els.smsContent.data('validate');
				
				// 校验通过才提交
				if (validatorResult && config.smsTypeResult) {
					var smsTypeId = $('#sms_type').val(),
						temp = $('#serviceId').val(),
						name = $('#sms_title').val(),
						tabNum = $('#tabNum').val(),
						//content = $('#sms_content').val(),
						content = $('#sms_content').html(),
						reg = /<\/?[^>]+>/gi,
						tips = $('#tips').val();
					
					content = content.replace(reg,function(img){
						var template = '';
						if(img.indexOf('shop') !== -1){
							template = '[店铺名称]';
						}
						else if(img.indexOf('order') !== -1){
							template = '[订单时间]';
						}
						else if(img.indexOf('send') !== -1){
							template = '[发货时间]';
						}
						else if(img.indexOf('ticket') !== -1){
							template = '[运单号]';
						}
						else if(img.indexOf('company') !== -1){
							template = '[快递公司]';
						}
									
						return template;
					});

					//关键词验证
					$.ajax({
							url:'template_toSmsTemplateAddFilter.action',
						//	cache: false,
							type: 'post',
							data:{
								serviceId:smsTypeId,
								name:name,
								content:content
							},
						    dataType:'json',
							success:function(data){
								 	var isvalid = data.isvalid;
								    var invalidWordsStr = data.invalidWordsStr;	 
								    if(!isvalid) {
								    	var viewDialog = new Dialog();
								    	viewDialog.init({
											contentHtml: '模板内容含有非法关键字，请修改！',
											yes: function() {
												viewDialog.close();
												_this.removeClass('disable');
											},
											yesVal: '关 闭',
											closeBtn: true
										})
								    }else {
								    	$.ajax({
											url:'template_add.action',
											type:'post',
											data:{
												serviceId:smsTypeId,
												name:name,
												content:content
											},
											dataType:'json',
											success:function(data){
												var viewDialog = new Dialog();
												viewDialog.init({
													contentHtml: '添加'+data+'模板成功！',
													yes: function() {
														viewDialog.close();
														var id = $('#templateId').val();														
														if(tips == null || tips == ''){														
															var action = 'template_list.action?menuFlag=sms_template_list&serviceId='+smsTypeId+'&tabNum='+tabNum+"&tips="+tips+"&temp="+temp;
															$('#new_sms_form').attr("action",action);
															setTimeout(function(){$('#new_sms_form').trigger('submit');},0);						
															//setTimeout(function(){window.location.href='template_list.action?menuFlag=sms_template_list&serviceId='+smsTypeId+"&tabNum="+tabNum;},0);
														}else{
															if(tips == '4'){
																setTimeout(function(){window.location.href='smsServiceMarket!smsMarketPageStep1.action?menuFlag=sms_made&templateId='+id;},0);
															}
															if(tips == '1'){
																setTimeout(function(){window.location.href='smsHomeEvent_smsSetting.action?menuFlag=sms_home&pos=down'+'&serviceId='+smsTypeId;},0);
															}
														}
														
														/*
														viewDialog.close();
														setTimeout(function(){
															window.location.href='template_toAdd.action?menuFlag=sms_template_list&serviceId='+smsTypeId+"&tabNum="+tabNum+"&tips="+tips+"&temp="+temp;
														},0)	
														*/
													},
													yesVal: '确定',
													closeBtn: true
												})
											},
											error:function(){
												var viewDialog = new Dialog();
												viewDialog.init({
													contentHtml: '抱歉！系统繁忙，请稍后再试。',
													yes: function() {
														viewDialog.close();
													},
													yesVal: '确定',
													closeBtn: true
												})
											}
										})
								    }
							},
							error:function(){
								var viewDialog = new Dialog();
								viewDialog.init({
									contentHtml: '抱歉！系统繁忙，请稍后再试。',
									yes: function() {
										viewDialog.close();
									},
									yesVal: '确定',
									closeBtn: true
								})
							}
					});	
					
					els.newSmsForm.attr('action','');
				}
				
			})
		};
		
		/**
		 * 添加返回首页
		 */
		var returnMain = function(){
			$('#return').click(function(){
				var smsTypeId = $('#serviceId').val(),
				tabNum = $('#tabNum').val(),
				tips = $('#tips').val(),
				id = $('#templateId').val();
				
				if(tips == null || tips == ''){
					setTimeout(function(){window.location.href='template_list.action?menuFlag=sms_template_list&serviceId='+smsTypeId+"&tabNum="+tabNum;},0);
				}else{
					if(tips == '4'){
						setTimeout(function(){window.location.href='smsServiceMarket!smsMarketPageStep1.action?menuFlag=sms_made&templateId='+id;},0);
					}
					if(tips == '1'){
						setTimeout(function(){window.location.href='smsHomeEvent_smsSetting.action?menuFlag=sms_home&pos=down'+'&serviceId='+smsTypeId;},0);
					}
				}
				
			});
		};
		
		/**
		 * 下拉框改变 titleWarn
		 */
		var selectChange = function(){
			var labelKeyA = $('a', '#label_key ul li:gt(0)');
			
			if ($("#sms_type").find('option:selected').text() == '营销定制') {
				labelKeyA.addClass('disable_a');
			}
			
			
			$('#sms_type').change(function(){
				$('.yto_onError,.yto_onCorrect').remove();
				$('#new_sms_form input[type="text"],#new_sms_form textarea').val('');
			
			
				var smsTypeId = $("#sms_type").find('option:selected').val();
				var serviceName = $("#sms_type").find('option:selected').text();
				/*if(serviceName == '发货提醒'){
					$('#resultString').val(0);
					
					labelKeyA.removeClass('disable_a');
				}
				if(serviceName == '派件提醒'){
					$('#resultString').val(1);
					
					labelKeyA.removeClass('disable_a');
				}
				if(serviceName == '签收提醒'){
					$('#resultString').val(2);
					
					labelKeyA.removeClass('disable_a');
				}
				if(serviceName == '营销定制'){
					$('#resultString').val(3);
					
					
					labelKeyA.addClass('disable_a');
				}*/
				//$('#serviceId').val(smsTypeId);
				if(smsTypeId != 0){
					$.ajax({
						url:'template_checkNum.action?menuFlag=sms_template_list&serviceId='+smsTypeId,
						type:'post',
						dataType:'json',
						success:function(data){
							if (data == '') {
								showIcon.correct($('#sms_typeTip'), ' ');
								
								config.smsTypeResult = true;
							} else {
								showIcon.error($('#sms_typeTip'), data);
								
								config.smsTypeResult = false;
							}
						},
						error:function(){
							var viewDialog = new Dialog();
							viewDialog.init({
								contentHtml: '抱歉！系统繁忙，请稍后再试。',
								yes: function() {
									viewDialog.close();
								},
								yesVal: '确定',
								closeBtn: true
							})
						}
					})
				} else {
					showIcon.error($('#sms_typeTip'), '请选择短信模板');
								
					config.smsTypeResult = false;
				}
				
			});
		};
		
		return {
			init: function() {
				insertLabel();
				form();
				returnMain();
				selectChange();
			}
		}
	})();
	
	newSms.init();
})