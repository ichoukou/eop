/**
 * 新增子账号
**/
$(function() {
	var newSubAccount = (function() {
		
		var winParam = window.params || {};
		// 配置
		var config = {
			addUrl: winParam.addUrl || null,				// 用户激活状态
			listUrl : winParam.listUrl || null
		};
		
		// 表单
		var form = function() {
			// 验证初始值
			var mobileResult = false,		// 手机验证结果
				telResult = false;			// 固话验证结果
				
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
				newAccForm: $('#new_acc_form'),
				accId: $('#acc_id'),
				customId:$("#taobaoEncodeKey"),
				realName: $('#real_name'),
				mobileTel: $('#mobile_tel'),
				mobileTelTip: $('#mobile_telTip'),
				telPartA: $('#tel_part_1'),
				telPartB: $('#tel_part_2'),
				telPartC: $('#tel_part_3'),
				telTip: $('#telTip'),
				email: $('#email'),
				editPsw: $('#edit_psw'),
				okBtn: $('#ok_btn'),
				backBtn:$("#back_btn")
			};
			
			// 验证正则
			var reg = {
				mobileTel: /^1\d{10}$/,									// 验证手机号
				telPartA: /^\d{3,4}$/,									// 验证区号
				telPartB: /^\d{7,8}$/,									// 验证电话
				telPartC: /^\d{0,5}$/									// 验证分机
			};
			
			// 提示文案
			var tipsMsg = {
				accIdErr: '登录账号格式错误',
				realNameErr: '真实姓名格式错误',
				telAEmpty: '区号没填',
				telAErr: '区号填写错误',
				mobileErr: '手机号码格式不正确',
				telBEmpty: '电话号没填',
				telBErr: '电话号填写错误',
				telCErr: '分机号填写错误',
				atLeastOne: '手机号码、固定电话至少填一项',
				emailErr: '邮件格式不正确',
				accTypeErr: '至少选择一项',
				pswFormatErr: '密码格式不正确',
				accTip: '请填写登录账户，保存后不能修改',
				realNameTip: '请输入真实姓名',
				emailTip: '请输入邮箱地址',
				initPswTip: '请设置账号的初始化密码'
			};
			
			// 默认显示手机和固话的提示层
			showIcon.show(els.mobileTelTip, tipsMsg.atLeastOne);
			showIcon.show(els.telTip, tipsMsg.atLeastOne);
			
			// 验证方法
			var check = {
				// 手机
				mobile: function(mobileVal) {
					if (reg.mobileTel.test(mobileVal)) {
						showIcon.correct(els.mobileTelTip, '');
						mobileResult = true;
					} else {
						showIcon.error(els.mobileTelTip, tipsMsg.mobileErr);
						mobileResult = false;
					}
				},
				// 固定电话
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
			$('#tel_part_1, #tel_part_2, #tel_part_3').bind({
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
				formID: 'new_acc_form',
				theme: 'yto',
				errorFocus: false,
				submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
			});
			
			// 登录账号
			/*els.accId.
			formValidator({validatorGroup:'1', onShow: tipsMsg.accTip, onFocus: '', onCorrect: ' '});*/
			
			// customId
//			els.customId.
//			formValidator({onShow:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到相应的订单信息",onFocus:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到相应的订单信息",onCorrect:" "})
//			.inputValidator({min:6,max:35,onError:"输入的CustomerID长度不符合规则"})
//			.regexValidator({regExp:"username", dataType: 'enum',onError:"输入的CustomerID字符不符合规则"})
//			.ajaxValidator({
//				dataType : "json",
//				cache:false,
//				url : "user!checkCustomerId.action",
//				success : function(res){
//					return res.status;
//				},
//				onError : "该CustomerID已被使用，请重新填写CustomerID",
//				onWait : "正在对CustomerID进行校验，请稍候..."
//			});
				
				
			// 真实姓名
			els.realName.
				formValidator({validatorGroup:'1', onShow: tipsMsg.realNameTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'name', dataType: 'enum', onError: tipsMsg.realNameErr});
				
			// 邮箱
			els.email.
				formValidator({validatorGroup:'1', onShow: tipsMsg.emailTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'email', dataType: 'enum', onError: tipsMsg.emailErr});
		
			
			// 编辑密码
			els.editPsw.
				formValidator({validatorGroup:'1', onShow:'如果不填写登录密码则不修改密码', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: ['password','empty'], dataType: 'enum', onError: tipsMsg.pswFormatErr});
				
			// 点击“确认”
			els.okBtn.click(function(ev) {
				ev.preventDefault();
				var otherResult = $.formValidator.pageIsValid('1');
				
				var mobileVal = els.mobileTel.val(), 
					telPartAVal = els.telPartA.val(),
					telPartBVal = els.telPartB.val(),
					telPartCVal = els.telPartC.val(),
					telVal = telPartAVal + telPartBVal + telPartCVal;
					
				// 再次验证手机和固话
				if ( mobileVal != '' && telVal == '' ) {			// 手机填了，固话没填
					// 验证手机
					check.mobile(mobileVal);
					
					telResult = true;
				} else if ( mobileVal != '' && telVal != '' ) {		// 手机填了，固话填了
					// 验证手机
					check.mobile(mobileVal);
					// 验证固话
					check.tel(telPartAVal, telPartBVal, telPartCVal);
				} else if ( mobileVal == '' && telVal == '' ) {		// 手机没填，固话没填
					// 显示提示，至少填一项
					showIcon.show(els.mobileTelTip, tipsMsg.atLeastOne);
					showIcon.show(els.telTip, tipsMsg.atLeastOne);
				} else if ( mobileVal == '' && telVal != '' ) {		// 手机没填，固话填了
					// 验证固话
					check.tel(telPartAVal, telPartBVal, telPartCVal);
					
					mobileResult = true;
				}
				
				if (mobileResult && telResult && otherResult) {
					//新增子账号
					els.newAccForm.trigger('submit');
				}
			});
			
			//"返回"按钮
			els.backBtn.click(function(){
				setTimeout(function(){
					history.back();
				},0);
			});
			
			
			
		};
		
		var showPrompt = function(){
			if($("#isValidate").val() == 'true'){
				var showDialog = new Dialog();
				
				showDialog.init({
					contentHtml: '操作已成功！',
					yes: function() {
						showDialog.close();
						setTimeout(function(){
							window.location.href=config.listUrl;
						},0);
					},
					yesVal:"管理业务账号",
					no: function() {
						showDialog.close();
						setTimeout(function(){
							window.location.href=config.addUrl;
						},0);
					},
					noVal:"新建业务账号"
				});
			}
			else if($("#isValidate").val() == 'false'){
				var showDialog = new Dialog();
				showDialog.init({
					contentHtml: '操作失败，稍后请重新操作！',
					yes: function() {
						showDialog.close();
					},
					closeBtn: true
				});
			}
		}
		
		return {
			init: function() {
				form();
				showPrompt();
			}
		}
	})();

	
	
	
	newSubAccount.init();
	
	
});


