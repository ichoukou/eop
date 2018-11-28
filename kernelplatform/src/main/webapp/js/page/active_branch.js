$(function() {
	var activeBranch = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		var linkageSel;
		
		// 全局配置
		var config = {
			userType:winParams.userType || null,
			response:$("#response").val() || ''
		};
		
		// 地区多级联动
		var selectArea = function() {
			var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#province'],
				autoLink: false
			};
			linkageSel = new LinkageSel(area);
			if($("#addressProvince").val() && $("#addressCity").val()){
				linkageSel.changeValuesByName([$("#addressProvince").val(), $("#addressCity").val()]);
				if(config.userType == 2){
					$('#province').attr('disabled', 'disabled');
				}
			}
			if($("#addressProvince").val() && $("#addressCity").val() && $("#addressDistrict").val()){
				linkageSel.changeValuesByName([$("#addressProvince").val(), $("#addressCity").val(), $("#addressDistrict").val()]);
				if(config.userType == 2){
					$('#province').attr('disabled', 'disabled');
					$('#province').next().attr('disabled', 'disabled');
				}
			}
		};
		
		// 表单验证
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
				psw: $('#psw'),
				repsw: $('#repsw'),
				mobileTel: $('#mobile'),
				telPartA: $('#tel_part_1'),
				telPartB: $('#tel_part_2'),
				telPartC: $('#tel_part_3'),
				mobileTelTip: $('#mobileTip'),
				email: $('#email'),
				telTip: $('#telTip'),
				province: $('#province'),
				detailAdd: $('#detail_address'),
				saveBtn: $('#save_submit_btn'),
				actBranchForm: $('#act_branch_form'),
				prov: $('#addressProvince'),
				city: $('#addressCity'),
				district: $('#addressDistrict'),
				field: $('#x_field')
			};
			
			// 验证正则
			var reg = {
				mobileTel: /^1\d{10}$/,			// 验证手机号
				telPartA: /^\d{3,4}$/,			// 验证区号
				telPartB: /^\d{7,8}$/,			// 验证电话
				telPartC: /^\d{0,5}$/			// 验证分机
			};
			
			// 提示文案
			var msg = {
				modifyPsw: '为保障您的账户安全，请修改您的初始密码',
				confirmPsw: '请再次输入密码',
				atLeastOne: '手机号码和固定电话必须填写一个（易通承诺不会将您的信息透露给第三方）',
				pswFormatErr: '密码格式不正确',
				mobileErr: '手机号码格式不正确',
				telAEmpty: '区号没填',
				telAErr: '区号填写错误',
				telBEmpty: '电话没填',
				telBErr: '电话填写错误',
				telCErr: '分机填写错误',
				emailTip: '请填写您的邮箱地址',
				emailErr: '邮件格式不正确',
				pswErr: '两次密码不一致，请确认',
				provinceErr: '请选择省',
				cityErr: '请选择市',
				areaErr: '请选择区',
				areaTip: '请选择省、市、区（县）',
				detailMinErr: '街道地址不能为空',
				detailMaxErr: '街道地址超长',
				detailTip: '请输入详细街道地址',
				pswEmptyErr: '密码不能为空',
				emailEmptyErr: '邮箱不能为空'
			};
			
			// 默认显示手机和固话的提示层
			showIcon.show(els.mobileTelTip, msg.atLeastOne);
			showIcon.show(els.telTip, msg.atLeastOne);
			
			// 验证手机和固话的方法
			var check = {
				mobile: function(mobileVal) {
					if (reg.mobileTel.test(mobileVal)) {
						showIcon.correct(els.mobileTelTip, '');
						mobileResult = true;
					} else {
						showIcon.error(els.mobileTelTip, msg.mobileErr);
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
											showIcon.error(els.telTip, msg.telCErr);
											
											telResult = false;
										}
									} else {
										telResult = true;
									}
								} else {												// 电话填写错误
									showIcon.error(els.telTip, msg.telBErr);
								}
								
							} else {								// 电话没填
								showIcon.error(els.telTip, msg.telBEmpty);
							}
							
						} else {											// 区号填写错误
							showIcon.error(els.telTip, msg.telAErr);
						}
					} else {								// 区号没填
						showIcon.error(els.telTip, msg.telAEmpty);
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
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'act_branch_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 登录密码
			els.psw.
				formValidator({validatorGroup:'1', onShow: msg.modifyPsw, onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, onErrorMin: msg.pswEmptyErr}).
				regexValidator({regExp: 'password', dataType: 'enum', onError: msg.pswFormatErr});
				
			// 确认密码
			els.repsw.
				formValidator({validatorGroup:'1', onShow: msg.confirmPsw, onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, onErrorMin: msg.pswEmptyErr}).
				regexValidator({regExp: 'password', dataType: 'enum', onError: msg.pswFormatErr}).
				compareValidator({desID: 'psw', operateor: '=', onError: msg.pswErr});
				
			// 邮箱
			els.email.
				formValidator({validatorGroup:'1', onShow: msg.emailTip, onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, onErrorMin: msg.emailEmptyErr}).
				regexValidator({regExp: 'email', dataType: 'enum', onError: msg.emailErr});
				
			// 发货地址
			els.province.
				formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: msg.areaTip, onFocus: '', onCorrect: function() {	// 省
					els.province.next().
						formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 市
							els.province.next().next().
								formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: ' '}).	// 区
								inputValidator({min:1, onError: msg.areaErr});
							return '';
						}}).
						inputValidator({min:1, onError: msg.cityErr});
					return '';
				}}).
				inputValidator({min:1, onError: msg.provinceErr});
				
			// 详细地址
			els.detailAdd.
				formValidator({validatorGroup:'1', onShow: msg.detailTip, onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, max: 100, onErrorMin: msg.detailMinErr, onErrorMax: msg.detailMaxErr});
				
			// 提交表单
			els.saveBtn.click(function(ev) {
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
					showIcon.show(els.mobileTelTip, msg.atLeastOne);
					showIcon.show(els.telTip, msg.atLeastOne);
				} else if ( mobileVal == '' && telVal != '' ) {		// 手机没填，固话填了
					// 验证固话
					check.tel(telPartAVal, telPartBVal, telPartCVal);
				}
				// 如果手机、固话及其他验证都通过，则提交表单
				if ((mobileResult || telResult) && otherResult) {
					//给省市县区赋值
					var selectedArr = linkageSel.getSelectedArr();
					els.prov.val(linkageSel.getSelectedData('name',0));
					els.city.val(linkageSel.getSelectedData('name',1));
					els.district.val(linkageSel.getSelectedData('name',2));
					els.field.val(selectedArr[0]);
					
					$("#addressProvince").val(linkageSel.getSelectedData('name',0));
					$("#addressCity").val(linkageSel.getSelectedData('name',1));
					$("#addressDistrict").val(linkageSel.getSelectedData('name',2));
					$("#x_field").val(selectedArr[0]);

					els.actBranchForm.trigger('submit');
				}
			});
		};
		
		var showResponse = function(){
			if(config.response!=null && config.response!="" && config.response!="true"){
				var oDialog = new Dialog();
				oDialog.init({
					contentHtml: config.response,
					yes: function() {
						oDialog.close();
						if(config.response=='激活成功，请重新登录')
							window.location.href='login_loginOut.action';
					},
					closeBtn: true
				});
			}
		}
		
		
		return {
			init: function() {
				selectArea();
				form();
				showResponse();
			}
		}
	})();
	activeBranch.init();
	
})