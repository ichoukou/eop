$(function() {
	var templateAudit = (function() {
		var winParams = window.params || {};
		
		var config = {
			passTemplate: winParams.passTemplate || ''			// 审核短信模板 Action
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
		
		// 通过
		var doPass = function() {
			$('a.do_pass').live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
					status = $('.status', _this.parents('tr')),
					doNotPass = $('.do_not_pass', _this.parent()),
					templateId = $('.template_id', _this.parent()).val();
					
				var passDialog = new Dialog();
				passDialog.init({
					contentHtml: '你确定要通过该模板吗？',
					yes: function() {
						$.ajax({
							url: 'template_passByAdmin.action',
							type: 'POST',
							data: {
								id: templateId,
								flag:'YES'
							},
							cache: false,
							success: function() {
								
								// 修改审核状态
								status.removeClass('wait not_pass').addClass('pass').html('审核通过');
								
								_this.replaceWith('<span class="do_pass gray">通过</span>');
								
								doNotPass.replaceWith('<a href="javascript:;" class="do_not_pass">不通过</a>');
								
								/*els.searForm.attr('action','template_queryList.action');
								els.searForm.trigger('submit');*/
								
								// 关闭弹层
								passDialog.close();
							}
						})
					},
					no: function() {
						passDialog.close();
					}
				});
			})
		};
		
		// 不通过
		var doNotPass = function() {
			$('a.do_not_pass').live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
					status = $('.status', _this.parents('tr')),
					doPass = $('.do_pass', _this.parent()),
					doNotPass = $('.do_not_pass', _this.parent()),
					templateId = $('.template_id', _this.parent()).val();
					
				var reasonDialog = new Dialog();
				reasonDialog.init({
					contentHtml: '<div id="reason_dialog">' +
								'	<h4>不通过原因：</h4>' +
								'	<textarea id="reason_text"></textarea>' +
								'	<span id="reason_textTip"></span>' +
								'</div>',
					yes: function() {
						$('#reason_textTip').css('display', 'block');
						if ($('#reason_text').val().length > 200 || $('#reason_text').val().length <= 0) {
							showIcon.error($('#reason_textTip'), '内容必须在 1-200 字之间');
						} else {
							$('#reason_textTip').hide();
							$.ajax({
								url: 'template_passByAdmin.action',
								cache: false,
								type: 'POST',
								data: {
									remark: $('#reason_text').val(),
									id: templateId,
									flag:'NO'
								},
								success: function() {
									// 修改审核状态
									status.removeClass('wait pass').addClass('not_pass').html('审核失败');

									_this.replaceWith('<span class="do_not_pass gray">不通过</span>');
								
									doPass.replaceWith('<a href="javascript:;" class="do_pass">通过</a>');
									
									// 关闭弹层
									reasonDialog.close();
								}
							})
						}
					},
					yesVal: '提 交',
					no: function() {
						reasonDialog.close();
					}
				})
			})
		};
		
		// 表单验证
		var form = function() {
			// 元素集合
			var els = {
				shopName: $('#shop_name'),
				searBtn: $('#sear_btn'),
				searForm: $('#sear_form')
			};
			
			// 文案
			var msg = {
				shopErr: '店铺名称最多30个字符'
			};
			
			// 完善基本信息表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
				
			// 店铺名称
			els.shopName.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({max:30, onErrorMax: msg.shopErr});
			
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				
				els.searForm.attr('action','template_queryList.action?menuFlag=sms_templateAdmin&flag=check');
				els.searForm.trigger('submit');
			});
		};
		
		var checkByShopName = function(){
			//店铺筛选
			$('.thead_select').each(function(){
				$(this).children().each(function(){
					$(this).click(function(ev){
						ev.preventDefault();
						
						if($(this).val()==0){
							window.location="associationAccount_toBindeAccountCustom.action?menuFlag=sms_templateAdmin";
						}else{
							
							els.searForm.attr('action','template_queryList.action?menuFlag=sms_templateAdmin');
							els.searForm.trigger('submit');
						}
					});
				});
			});
		}
		
		return {
			init: function() {
				doPass();
				doNotPass();
				form();
				checkByShopName();
				
				//翻页
				pagination.click(function(){
					
					$('#currentPage').val($(this).attr("value"));
					$('#sear_form').attr('action','template_queryList.action?menuFlag=sms_templateAdmin');
					$('#sear_form').trigger('submit');
				});
			}
		}
	})();
	
	templateAudit.init();
})