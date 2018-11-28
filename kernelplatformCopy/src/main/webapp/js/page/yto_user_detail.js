/**
 * 我的账号
**/
$(function() {
	var ytoUserDetail = (function() {
		
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		
		// 表单
		var form = function() {
			// 手机和固话的验证结果，默认都是 false
			var mobileResult = false,
				telResult = false;
				
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
			
			// 元素集合
			var els = {
				userForm: $('#userFrom'),
				userFormSubmit: $('#sear_btn'),
				mobileTel: $('#mp'),
				mobileTelTip: $('#mpTip'),
				telPartA: $('#telAreaCode'),
				telPartB: $('#telCode'),
				telPartC: $('#telExtCode'),
				telTip: $('#telAreaCodeTip'),
				email: $('#yaya'),
				shopName: $('#xx'),
			};
			
			// 验证正则
			var reg = {
				mobileTel: /^1\d{10}$/,			// 验证手机号
				telPartA: /^\d{3,4}$/,			// 验证区号
				telPartB: /^\d{7,8}$/,			// 验证电话
				telPartC: /^\d{0,5}$/			// 验证分机
			};
			
			// 提示文案
			var tipsMsg = {
				mobileErr: '手机号码格式不正确',
				emailErr: '邮件格式不正确',
				shopErr: '店铺名称必须是4-30个字符',
				telAEmpty: '区号没填',
				telAErr: '区号填写错误',
				telBEmpty: '电话没填',
				telBErr: '电话填写错误',
				telCErr: '分机填写错误',
				atLeastOne: '手机号码、固定电话至少填一项',
				emailTip: '请输入正确的邮箱地址'
			};
			
			// 默认显示手机和固话的提示层
			showIcon.show(els.mobileTelTip, tipsMsg.atLeastOne);
			showIcon.show(els.telTip, tipsMsg.atLeastOne);
			
			// 验证手机和固话的方法
			var check = {
				mobile: function(mobileVal) {
					if (reg.mobileTel.test(mobileVal)) {
						showIcon.correct(els.mobileTelTip, '');
						mobileResult = true;
					} else {
						showIcon.error(els.mobileTelTip, tipsMsg.mobileErr);
						mobileResult = false;
					}
				},
				
				tel: function(telPartAVal, telPartBVal, telPartCVal) {
					if (telPartAVal != '') {		// 区号填了
						// 验证区号
						if (reg.telPartA.test(telPartAVal)) {	// 区号填写正确
							showIcon.correct(els.telTip, '');
							
							if (telPartBVal != '') {		// 电话填了
								// 验证电话
								if (reg.telPartB.test(telPartBVal)) {		// 电话填写正确
									showIcon.correct(els.telTip, '');
									
									
									if (telPartCVal != '') {			// 分机填了
										// 验证分机
										if (reg.telPartC.test(telPartCVal)) {		// 分机填写正确
											showIcon.correct(els.telTip, '');
											telResult = true;
										} else {												// 分机填写错误
											showIcon.error(els.telTip, tipsMsg.telCErr);
											
											telResult = false;
										}
									} else {
										telResult = true;
									}
								} else {												// 电话填写错误
									showIcon.error(els.telTip, tipsMsg.telBErr);
								}
								
							} else {								// 电话没填
								showIcon.error(els.telTip, tipsMsg.telBEmpty);
							}
							
						} else {											// 区号填写错误
							showIcon.error(els.telTip, tipsMsg.telAErr);
						}
					} else {								// 区号没填
						showIcon.error(els.telTip, tipsMsg.telAEmpty);
					}
				}
			};
			
			// 验证手机
			els.mobileTel.bind({
				blur: function() {
					var val = $(this).val();
					if (val != '') {
						check.mobile(val);
					}
				},
				focus: function() {
					// 清空提示
					els.mobileTelTip.html('');
				}
			});
			
			// 验证固话
			$('#telAreaCode, #telCode, #telExtCode').bind({
				blur: function() {
					var valA = els.telPartA.val(),
						valB = els.telPartB.val(),
						valC = els.telPartC.val(),
						valAll = valA + valB + valC;
					if (valAll != '') {
						check.tel(valA, valB, valC);
					}
				},
				focus: function() {
					// 清空提示
					els.telTip.html('');
				}
			});
			
			// 基本信息表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'userFrom',
				theme: 'yto',
				errorFocus: false
			});
				
			// 邮箱
			els.email.
				formValidator({validatorGroup:'1', onShow: tipsMsg.emailTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'email', dataType: 'enum', onError: tipsMsg.emailErr});
				
			// 店铺名称
			els.shopName.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min:4, max:30, onError: tipsMsg.shopErr});
			
			// 提交表单
			els.userFormSubmit.click(function(ev) {
				ev.preventDefault();
				var mobileVal = els.mobileTel.val(),
					telPartAVal = els.telPartA.val(),
					telPartBVal = els.telPartB.val(),
					telPartCVal = els.telPartC.val(),
					telVal = telPartAVal + telPartBVal + telPartCVal,
					otherResult = $.formValidator.pageIsValid('1');

				if ( mobileVal != '' && telVal == '' ) {			// 手机填了，固话没填
					// 验证手机
					check.mobile(mobileVal);
				} else if ( mobileVal != '' && telVal != '' ) {		// 手机填了，固话填了
					// 验证手机
					check.mobile(mobileVal);
					// 验证固话
					check.tel(telPartAVal, telPartBVal, telPartCVal);
				} else if ( mobileVal == '' && telVal == '' ) {		// 手机没填，固话没填
					// 显示提示，至少填一项
					showIcon.show(els.mobileTelTip, tipsMsg.atLeastOne)
					showIcon.show(els.telTip, tipsMsg.atLeastOne)
				} else if ( mobileVal == '' && telVal != '' ) {		// 手机没填，固话填了
					// 验证固话
					check.tel(telPartAVal, telPartBVal, telPartCVal);
				}
				
				//console.log((mobileResult || telResult), otherResult);
				
				// 如果手机、固话及其他验证都通过，则提交表单
				if ((mobileResult || telResult) && otherResult) {
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: '您确定要保存吗?',
						yes: function() {
							setTimeout(function(){els.userForm.trigger('submit');},0);
							oDialog.close();
						},
						no: function(){
							oDialog.close();
						},
						closeBtn: true
					});
				}
				
			});
		};
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	ytoUserDetail.init();
});