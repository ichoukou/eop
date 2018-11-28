/**
 * 我的账号
**/
$(function() {
	var myAccountM = (function() {
		
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		var linkageSel;
		var subFlag = false;
		
		// 全局配置
		var config = {
			onStep: winParams.onStep || null,
			userType:winParams.userType || null,
			taobaoEncodeKey:winParams.taobaoEncodeKey || '',
			pointsGetUrl: winParams.pointsGetUrl,               // 获取对应网点 url
			bindFormAction: winParams.bindFormAction || '',	// 绑定网点表单 action 
			custComfUrl: winParams.bindFormAction+"autoGen&user.site=",                 // 自动绑定网点 url
			tickGetUrl: winParams.pointsGetUrl+"?mailNo="                    // 运单号获取 url
		};
		
		var showEditMsg = function(){
			var msg = $("#edit_res_msg").val();
			if(!!msg && msg == '修改成功!'){
				var oDialog = new Dialog();
				oDialog.init({
					contentHtml: '用户信息修改成功!',
					yes: function() {
						oDialog.close();
					},
					closeBtn: true
				});
			}
		}
		
		// 地区多级联动
		var selectArea = function() {
			var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#province'],
				autoLink: false
			};
			 
			linkageSel = new LinkageSel(area);
			setTimeout(function(){
			if($("#addressProvince").val() && $("#addressCity").val() && $("#addressDistrict").val()){
					linkageSel.changeValuesByName([$("#addressProvince").val(), $("#addressCity").val(), $("#addressDistrict").val()]);
				if(config.userType == 2){
					$('#province').attr('disabled', 'disabled');
					$('#province').next().attr('disabled', 'disabled');
				}
			}
			else if($("#addressProvince").val() && $("#addressCity").val()){
				linkageSel.changeValuesByName([$("#addressProvince").val(), $("#addressCity").val()]);
				if(config.userType == 2){
					$('#province').attr('disabled', 'disabled');
				}
			}
			},0);
			
			
			var confirmEl = $('#add_confirm');
			
			linkageSel.onChange(function() {
				var d = this.getSelectedDataArr('name'),
					arr = [];
				for (var i = 0, len = d.length; i < len; i++) {
					arr.push(d[i]);
				}
				$("#addressProvince").val(d[0]);
				$("#addressCity").val(d[1]);
				$("#addressDistrict").val(d[2]);
				confirmEl.parent().show();
				confirmEl.html('您的地址是：' + arr.join(' '));
				if($("#detail_address").val()){
					confirmEl.append(' <span id="add_detail">'+$("#detail_address").val()+'</span>');
				}
			});
			
			$('#basic_info textarea').keyup(function() {
				if ($('#add_detail').length == 0) {
					confirmEl.append('<span id="add_detail"></span>');
				}
				
				$('#add_detail').html(' ' + $(this).val());
			});
		};
		
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
				basicForm: $('#basic_info'),
				basicFormSubmit: $('#basic_info_submit'),
				mobileTel: $('#mobile_tel'),
				mobileTelTip: $('#mobile_telTip'),
				telPartA: $('#tel_part_1'),
				telPartB: $('#tel_part_2'),
				telPartC: $('#tel_part_3'),
				telTip: $('#telTip'),
				email: $('#email'),
				shopName: $('#shop_name'),
				province: $('#province'),
				detailAdd: $('#detail_address'),
				oldPsw: $('#old_psw'),
				newPsw: $('#new_psw'),
				confirmPsw: $('#confirm_psw'),
				pswForm: $('#modify_psw'),
				pswSubmit: $('#modify_psw_submit'),
				bindBtn: $('#binding_btn'),
				checkForm: $('#check_form'),
				customerCode: $('#input_text_demo'),
				cancelBindBut:$("#cancelBindBut"),
				changeBindBut:$("#changeBindBut"),
				subUpdateUC:$("#btn_up_uc_submit"),
				cancelUpdateUC:$("#btn_up_uc_cancel")
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
				provinceErr: '请选择省',
				cityErr: '请选择市',
				areaErr: '请选择区',
				detailMinErr: '街道地址不能为空',
				detailMaxErr: '街道地址超长',
				telAEmpty: '区号没填',
				telAErr: '区号填写错误',
				telBEmpty: '电话没填',
				telBErr: '电话填写错误',
				telCErr: '分机填写错误',
				atLeastOne: '手机号码、固定电话至少填一项',
				oldPsw: '请填写原密码',
				newPsw: '请输入新密码',
				pswRule: '英文、数字、符号，6-16个字符',
				pswFormatErr: '密码格式不正确',
				pswErr: '两次密码不一致，请确认',
				customerCodeEmptyErr: '用户编码不能为空',
				customerCodeFormatErr: '您输入的用户编码错误',
				emailTip: '请输入正确的邮箱地址',
				areaTip: '请选择所在县区',
				detailTip: '请输入详细街道地址',
				pswAgain: '请再次输入新密码'
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
									telResult=false;
								}
								
							} else {								// 电话没填
								showIcon.error(els.telTip, tipsMsg.telBEmpty);
								telResult=false;
							}
							
						} else {											// 区号填写错误
							
							showIcon.error(els.telTip, tipsMsg.telAErr);
							
							telResult=false;
						}
					} else {								// 区号没填
						
						showIcon.error(els.telTip, tipsMsg.telAEmpty);
						
						telResult=false;
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
				formID: 'basic_info',
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
			
			// 发货地址
			els.province.
				formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: tipsMsg.areaTip, onFocus: '', onCorrect: function() {	// 省
					els.province.next().
						formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 市
							var area = els.province.next().next();
							if(area.is(':visible')){
								area.
									formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: ' '}).	// 区
									inputValidator({min:1, onError: tipsMsg.areaErr});
								return '';
							}
							return ' ';
						}}).
						inputValidator({min:1, onError: tipsMsg.cityErr});
					return '';
				}}).
				inputValidator({min:1, onError: tipsMsg.provinceErr});
            
			// 街道地址
			els.detailAdd.
				formValidator({validatorGroup:'1', onShow: tipsMsg.detailTip, onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, max: 100, onErrorMin: tipsMsg.detailMinErr, onErrorMax: tipsMsg.detailMaxErr});
			
			// 提交表单
			els.basicFormSubmit.click(function(ev) {
				ev.preventDefault();
				if(config.onStep < 2){
					window.location.reload(true);;
					return;
				}
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
				
				// 如果手机、固话及其他验证都通过，则提交表单
				var result = false;
				//填了手机没填电话
				if(mobileVal != '' && telPartBVal == '' && telPartAVal == '' && telPartCVal == ''){
					if(mobileResult){
						result = true;
					}
				}
				//填了电话没填手机
				if((telPartBVal != '' || telPartAVal != '' || telPartCVal != '') && mobileVal == ''){
					if(telResult){
						result = true;
					}
				}
				//手机电话都填了
				if(mobileVal != '' && (telPartBVal != '' || telPartAVal != '' || telPartCVal != '')){
					if(mobileResult && telResult){
						result = true;
					}
				}
						
				if (result && otherResult) {
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: '您确定要保存吗?',
						yes: function() {
							setTimeout(function(){els.basicForm.trigger('submit');},0);
							oDialog.close();
						},
						no: function(){
							oDialog.close();
						},
						closeBtn: true
					});
				}
				
			});
			
			
			// “修改密码”表单
			$.formValidator.initConfig({
				validatorGroup: '2',
				formID: 'modify_psw',
				theme: 'yto',
				errorFocus: false,
				submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
			});
			
			// 原密码
			els.oldPsw.
				formValidator({validatorGroup:'2', onShow: tipsMsg.oldPsw, onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, onErrorMin: tipsMsg.oldPsw}).
				ajaxValidator({
					dataType : "json",
                    async : false,
                    cache:false,
                    url : "user!checkPassword.action",
                    success : function(data){
                        return data.status;
                    },
                    onError : "原始密码不对，请重新输入",
                    onWait : "正在对原始密码进行合法性校验，请稍候..."
				});
				
			// 新密码
			els.newPsw.
				formValidator({validatorGroup:'2', onShow: tipsMsg.newPsw, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'password', dataType: 'enum', onError: tipsMsg.pswFormatErr});
			
			// 确认密码
			els.confirmPsw.
				formValidator({validatorGroup:'2', onShow: tipsMsg.pswAgain, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'password', dataType: 'enum', onError: tipsMsg.pswFormatErr}).
				compareValidator({desID: 'new_psw', operateor: '=', onError: tipsMsg.pswErr});
			
			// 提交表单
			els.pswSubmit.click(function(ev) {
				ev.preventDefault();
				if(config.onStep < 2){
					window.location.reload(true);;
					return;
				}
				//表单元素校验
				var validatorResult = $.formValidator.pageIsValid(2);
				if (validatorResult){
					var oldPwd = $.trim($("#old_psw").val()),
						newPwd = $.trim($("#new_psw").val()),
						surePwd = $.trim($("#confirm_psw").val());
					$.ajax({
	                    url: "userEditPwd_editPwd.action",
	                    cache: false,
	                    data: {
	                    	oldPwd: oldPwd,
	                    	newPwd: newPwd,
	                    	surePwd: surePwd
	                    },
	                    dataType:"json",
	                    success:function(data) {
	                        if(data.status) {
	                        	$("#resetButton").click();
	                        	$.formValidator.resetTipState("2");
	                            var content = "密码修改成功!\r\n下次登录请使用新密码!";
	                            var oDialog = new Dialog();
								oDialog.init({
									contentHtml: content,
									yes: function() {
										oDialog.close();
									},
									closeBtn: true
								});
	                        }
	                        else{
	                        	var content = "密码修改失败，请检查后重新操作!";
	                            var oDialog = new Dialog();
								oDialog.init({
									contentHtml: content,
									yes: function() {
										oDialog.close();
									},
									closeBtn: true
								});
	                        }
	                    },
	                    error:  function(XMLHttpRequest, statusText, errorThrown) {
	                        var content = "密码修改失败!\r\n";
	                        var oDialog = new Dialog();
							oDialog.init({
								contentHtml: content,
								yes: function() {
									oDialog.close();
								},
								closeBtn: true
							});
	                    }
	                });
				}
			});
			
			
			$.formValidator.initConfig({
				validatorGroup: '3',
				formID: 'check_form',
				theme: 'yto',
				errorFocus: false,
				submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
			});
			
			// 客户编码
			
			els.customerCode.
				formValidator({validatorGroup:'3', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, onErrorMin: tipsMsg.customerCodeEmptyErr}).
				regexValidator({regExp: 'customerCode', dataType: 'enum', onError: tipsMsg.customerCodeFormatErr}).
				ajaxValidator({
					type: "POST",
                    dataType : "json",
                    cache: false,
                    url : "user!checkNewUserCode.action",
                    success : function(data){
                    	if(data.status) {
                            $("#new_branch_txt").text(data.infoContent);
                            if(subFlag){
                            	setTimeout(function(){
                            		els.subUpdateUC.click();
                            	},5);
                            }
                        }
                        else if(data.infoContent){
                        	setTimeout(function(){showIcon.error($("#input_text_demoTip"), data.infoContent)},10);
                        }
                        return data.status;
                    },
                    onError : "新客户编码不对，请重新输入",
                    onWait : "正在对新客户编码进行合法性校验，请稍候..."
                });
			
			
			// 绑定网点按钮
			els.bindBtn.click(function(ev) {
				ev.preventDefault();
				if(config.onStep == 2){
					//do_bdwd(1);
					bindDialog();
				}
				else{
					window.location.reload(true);
				}
			});
			
			//取消绑定按钮
			els.cancelBindBut.click(function(ev){
				ev.preventDefault();
				do_bdwd(3);
			});
			
			
			//更换网点按钮
			els.changeBindBut.click(function(ev){
				ev.preventDefault();
				do_bdwd(2);
			});
			
			//取消修改，回跳到用户编码查看页面
			els.cancelUpdateUC.click(function(ev){
				ev.preventDefault();
				$("#resetCheckForm").click();
				$("#div_uc_edit").slideUp('normal');
				$("#div_uc_view").slideDown('normal');
			});
			
			//提交修改用户编码
			els.subUpdateUC.click(function(ev){
				ev.preventDefault();
				subFlag = true;
				if($.formValidator.pageIsValid("3")){
					setTimeout(
					function(){
						var cDialog = new Dialog();
						subFlag = false;
						cDialog.init({
							contentHtml: "为了确定您的信息安全，请确认你的服务网点是否为“"+$('#new_branch_txt').text()+"”？",
							yes: function() {
								cDialog.close();
	//							els.checkForm.trigger('submit');
								$.ajax({
									type: "POST",
				                    dataType : "json",
				                    cache: false,
				                    data:{"user.userCode":$("#input_text_demo").val()},
				                    url : "user!editUserCode.action",
				                    success : function(data){
				                    	if(data.status) {
				                    		var eDialog = new Dialog();
				    						eDialog.init({
				    							contentHtml: data.infoContent,
				    							yes: function() {
				    								$("#resetCheckForm").click();
				    								eDialog.close();
				    								window.location.reload(true);;
				    							},
				    							closeBtn: true
				    						});
				                        }
				                        else if(data.infoContent){
				                        	setTimeout(function(){showIcon.error($("#input_text_demoTip"), data.infoContent)},10);
				                        }
				                        return data.status;
				                    }
								});
							},
							no:function(){
								cDialog.close();
								$("#new_branch_txt").text("");
								$("#resetCheckForm").click();
							},
							closeBtn: true
						});
					},10);
				}
			});
			
			var bindDialog = function(){
				var subFlag = 0,
				    currentCall,
			        timer;
				
				var bindContent = '<!-- S 绑定客户编码 -->' +
				'<div id="active_second">' +
				'	<div id="process">' +
				'		<ol>' +
				'			<li id="process_first">1、完善基本信息</li>' +
				'			<li id="process_cur">2、绑定网点（可选）</li>' +
				'			<li id="process_last">3、开始使用易通</li>' +
				'		</ol>' +
				'	</div>' +
				'	<div id="statement">' +
				'		<p>选择圆通服务网点后，您可以使用问题件管理等服务！</p>' +
				'	</div>' +
				'	<form action="' + config.bindFormAction + '" id="act_bind_form" class="form">' +
				'       <div class="act_info">' +
				'	       <div class="act_loading">正在获取网点信息，可能需要花费几分钟，请耐心等待！</div>' +
				'          <a title="网点信息有误，请点击这里" class="act_learn" href="javascript:;">不想等了，使用手动绑定</a>' +
				'       </div>' +
				'       <div class="act_solution">' +
				'          <div class="act_notice clearfix"><a title="上一步" class="act_back" href="javascript:;"></a>两个方法任选其一</div>' +
				'          <div class="act_ticket">' +
				'		      <label for="act_ticket_code">方法一：通过运单号获取您的网点信息</label>' +
				'             <input type="text" id="act_ticket_code" class="input_text" />' +
				'             <a title="获取" class="btn btn_a" href="javascript:;" id="info_get"><span>获取</span></a>' +
				'             <span id="act_ticket_codeTip"></span>' +
				'          </div>' +
				'          <div class="act_customer">' +
				'             <label for="act_customer_code">方法二：向你的圆通服务网点获取客户编码</label>' +
				'			  <input type="text" id="act_customer_code" class="input_text" />' +
				'             <a title="识别" class="btn btn_a" href="javascript:;" id="info_comfirm"><span>识别</span></a>' +
				'             <span id="act_customer_codeTip"></span>' +
				'		   </div>' +
				'          <div class="act_result">' +
				'          </div>' +
				'       </div>' +
				'       <input id="act_point" type="hidden" value="" />' +
				'       <input id="act_type" type="hidden" value="1" />' +
				'	</form>' +
				'</div>' +
				'<!-- E 绑定客户编码 -->';
			
			var bDialog = new Dialog();
			var bFlag = true;
			bDialog.init({
				contentHtml: bindContent,
				closeBtn: true,
				yes: function() {
					// 如果方法一验证通过
					var condition1 = $("#act_type").val() == 1 && els.pointHidden.val() !== '';
					// 如果方法二验证通过
					var condition2 = $("#act_type").val() == 2 && els.pointHidden.val() !== '';
					if (condition1 || condition2) {
						if(!bFlag){
							return;
						}
						els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
						// 异步提交表单
						bFlag = false;
						
						$.ajax({
							type: 'GET',
							url: condition1 ? config.custComfUrl+els.pointHidden.val() : config.bindFormAction+els.pointHidden.val(),
							dataType: "json",
							timeout: 8000,
							success: function(data) {
								
								if(data.status){
									bDialog.close();
									// 弹出“绑定成功”弹层
									var oDialog = new Dialog();
									oDialog.init({
										contentHtml: '绑定成功',
										closeBtn: true,
										iconType: 'success',
										autoClose: 3000
									});
									
									setTimeout(function(){
										window.location.reload(true);
									}, 3000);
									// 删除“绑定网点”按钮
									//$('#act_bind_btn').remove();
								}
								else{
									var oDialog = new Dialog();
									oDialog.init({
										contentHtml: data.infoContent,
										yes: function() {
											oDialog.close();
										},
										closeBtn: true
									});
								}
							},
							error: function() {
//								if (typeof console !== 'undefined') {
//									console.log('Ajax Error!');
//								}
							}
						});
					}
				},
				no: function() {
					bDialog.close();
					
					// “开始使用”替换为“绑定网点”按钮
					//$('#act_start_btn').replaceWith('<a href="javascript:;" id="act_bind_btn" class="btn btn_a" title="绑定网点"><span>绑定网点</span></a>');
				},
				yesVal: '绑定网点',
				noVal: '稍后再说'
			});
			
			// 当前 ajax 请求
			var call = {
				auto: 'auto',
				cust: 'cust',
				tick: 'tick'
			};
			
			// 显示提示
			var showIcon = {
				correct: function(el, text) {
					el.html('<span>' + text + '</span>');
				},
				error: function(el, text) {
					el.html('<span class="yto_onError">' + text + '</span>');
				}
			};
			
			// 默认文案
			var textDefault = {
				ticket: '输入最近发货并已签收的运单号',	
				customer: '输入客户编码'
			};
			
			// 元素集合
			var els = {
				infoDiv: $('#act_bind_form .act_info'),
				solutionDiv: $('#act_bind_form .act_solution'),
				ticketCode: $('#act_ticket_code'),
				ticketCodeTip: $('#act_ticket_codeTip'),
				customerCode: $('#act_customer_code'),
				customerCodeTip: $('#act_customer_codeTip'),
				getBtn: $('#info_get'),
				confirmBtn: $('#info_comfirm'),
				back: $('#act_bind_form .act_back'),
				learn: $('#act_bind_form .act_learn'),
				resultDiv: $('#act_bind_form .act_result'),
				pointHidden: $('#act_point'),
				bindBtn: $('a:eq(0)', $('#active_second').parent().next())
			};
			
			// 提示文案
			var tipsMsg = {
				customerCodeEmptyErr: '客户编码不能为空',
				customerCodeFormatErr: '客户编码格式有误',
				ticketCodeEmptyErr: '运单号不能为空',
				ticketCodeFormatErr: '运单号格式有误',
				noInfo: '无法识别到您的网点信息！请联系客服：电话：021-64703131-107 QQ群：241711549'
			};
			
			// 绑定默认提示
			els.ticketCode.defaultTxt(textDefault.ticket);
			els.customerCode.defaultTxt(textDefault.customer);
			
			// 初始化绑定按钮状态
			els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
			
			// 异步获取对应网店信息
			currentCall = call.auto;
			
			$.ajax({
				type: 'GET',
				url: config.pointsGetUrl,
				dataType: "json",
				success: function(data) {
					
					if($('#active_second').length === 0 || currentCall !== call.auto){
						return;
					}
					
					var _els = els;
					
					if(data.status && data.dataList.length > 0){
						_els.learn.html('网点信息不是我的，请点击这里');
						var pointsHtml = '',
						    _points =data.dataList;
						
						$.each(_points, function(){
							var _p = this;
							if(_points.length==1) {
								pointsHtml += '<p>' + 
							                  '   <input class="point" type="radio" checked name="point" value="' + _p.code + '"/>' + '<span class="point">' +_p.text + '</span>' +
							                  '</p>';
								
								_els.pointHidden.val(_p.code);
								_els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
							} 
							else {
								pointsHtml += '<p>' + 
							                  '   <input class="point" type="radio" name="point" value="' + _p.code + '"/>' + '<span class="point">' +_p.text + '</span>' +
							                  '</p>';
							}
							/*pointsHtml += '<p>' + 
							              '   <input class="point" type="radio" name="point" value="' + _p.code + '"/>' + '<span class="point">' +_p.text + '</span>' +
							              '</p>';*/
						});
						
						$('.act_loading').removeClass().addClass('act_points').html(pointsHtml);
						
						// 选择相应网点
						_els.infoDiv.find('input[type="radio"]').change(function(){
							//标识方法一绑定
							$("#act_type").val("1");
							var _this = $(this);
							_els.pointHidden.val(_this.parent().find('input[type="radio"]:checked').val());
							_els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
						});
						_els.infoDiv.find('span').click(function(){
							//标识方法一绑定
							$("#act_type").val("1");
							var _this = $(this),
								radio = _this.parent().find('input[type="radio"]');
							
							radio.prop("checked", true);
							_els.pointHidden.val(radio.val());
							_els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
						});
					}
					else {
						_els.learn.hide();
						$('.act_loading').html('<a class="next" href="javascript:;">无法识别出您的网点，<span class="manual">请手动绑定~</span></a>');
						$('.act_loading .next').click(function(ev){
							ev.preventDefault();
							
							_els.learn.trigger('click');
						});
						//_els.back.hide();
						//_els.learn.trigger("click");
					}
				},
				error: function() {
					/*if (typeof console !== 'undefined') {
						console.log('Ajax Error!');
					}*/
					els.learn.hide();
					$('.act_loading').html('<a class="next" href="javascript:;">无法识别出您的网点，<span class="manual">请手动绑定~</span></a>');
					$('.act_loading .next').click(function(ev){
						ev.preventDefault();
						
						els.learn.trigger('click');
					});
					//els.back.hide();
					//els.learn.trigger("click");
				}
			});
			
			// 点击解决方法
			els.learn.click(function(){
				var _els = els;
				_els.infoDiv.find('input[type="radio"]').prop("checked", false);
				_els.infoDiv.hide();
				_els.solutionDiv.show();
				_els.pointHidden.val('');
				_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
				showIcon.correct(els.ticketCodeTip, '');
				showIcon.correct(els.customerCodeTip, '');
			});
			
			// 返回
			els.back.click(function(){
				var _els = els;
				_els.infoDiv.show();
				_els.solutionDiv.hide();
				_els.ticketCode.val(textDefault.ticket).addClass('default_status');
				_els.customerCode.val(textDefault.customer).addClass('default_status');
				_els.resultDiv.html('');
				_els.pointHidden.val('');
				_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
				showIcon.correct(els.ticketCodeTip, '');
				showIcon.correct(els.customerCodeTip, '');
			});
			
			// 运单号验证
			els.ticketCode.blur(function(){
				$('#act_customer_codeTip').html('');

				var _val = $(this).val(),
				    _els = els;
				
				showIcon.correct(_els.ticketCodeTip, '');
				
				if(_val === '' || _val === textDefault.ticket){
					showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeEmptyErr);
				} else {
					var reg = /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/g;
					if(!reg.test(_val)){
						showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeFormatErr);
					}
				}
			});
			// 重置搜索结果与按钮状态
			els.ticketCode.change(function(){
				var _els = els;
				
				_els.resultDiv.html('');
				_els.pointHidden.val('');
				_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
			});
			// 运单号获取
			els.getBtn.click(function(){
				$('#act_ticket_codeTip').html('');
				$('#act_customer_codeTip').html('');

				//标识方法一绑定
				$("#act_type").val("1");
				
				var flag = true,
				    _val = els.ticketCode.val(),
				    _els = els;
				showIcon.correct(_els.ticketCodeTip, '');
				
				if(_val === '' || _val === textDefault.ticket){
					flag = false;
					showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeEmptyErr);
				} else {
					var reg = /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/g;
					if(!reg.test(_val)){
						flag = false;
						showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeFormatErr);
					}
				}
				
				if(!flag){
					return;
				}
				
				_els.ticketCode.attr("disabled", "disabled");
				if(timer){
					clearTimeout(timer);
				}
				timer = setTimeout(function(){
					
					_els.resultDiv.html('正在获取信息，请等待...');
					
					currentCall = call.tick;
					$.ajax({
						dataType : "json",
						cache: false,
						type:"post",
						url : config.tickGetUrl + _val,
						success : function(response){
							
							if($('#active_second').length === 0 || currentCall !== call.tick){
								return;
							}
							
							var restDiv = _els.resultDiv,
						        potHidden = _els.pointHidden,
						        bindBtn = _els.bindBtn;
							
							if(response.status){
								restDiv.html('您的网点是：' + '<em>' + response.targetUrl + '</em>');
								potHidden.val(response.infoContent);
								bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
							}
							else {
								restDiv.html(tipsMsg.noInfo);
								potHidden.val('');
								bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
							}
						},
						error : function(){
							var restDiv = _els.resultDiv,
					        potHidden = _els.pointHidden,
					        bindBtn = _els.bindBtn;
							restDiv.html(tipsMsg.noInfo);
							potHidden.val('');
							bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
						},
						complete : function(){
							_els.ticketCode.removeAttr("disabled");
						}
					});
				}, 1000);
			});
			
			// 客户编码验证
			els.customerCode.blur(function(){
				$('#act_ticket_codeTip').html('');

				var _val = $(this).val(),
				    _els = els;
				
				showIcon.correct(_els.customerCodeTip, '');
				
				if(_val === '' || _val === textDefault.customer){
					showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeEmptyErr);
					
				}
				else {
					var reg = /[A-Za-z0-9]{1,100}$/g;
					if(!reg.test(_val)){
						showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeFormatErr);
					}
				}
			});
			// 重置搜索结果与按钮状态
			els.customerCode.change(function(){
				var _els = els;
				
				_els.resultDiv.html('');
				_els.pointHidden.val('');
				_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
			});
			// 客户编码识别
			els.confirmBtn.click(function(){
				$('#act_ticket_codeTip').html('');
				$('#act_customer_codeTip').html('');
				
				//标识方法二绑定
				$("#act_type").val("2");
				
				var flag = true,
				    _val = els.customerCode.val(),
				    _els = els;
				showIcon.correct(_els.customerCodeTip, '');
				
				if(_val === '' || _val === textDefault.customer){
					flag = false;
					showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeEmptyErr);
				}
				var reg = /[A-Za-z0-9]{1,100}$/g;
				if(!reg.test(_val)){
					flag = false;
					showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeFormatErr);
				}
				
				if(!flag){
					return;
				}
				
				_els.customerCode.attr("disabled", "disabled");
				if(timer){
					clearTimeout(timer);
				}
				
				timer = setTimeout(function(){
					_els.resultDiv.html('正在识别信息，请等待...');
					
					currentCall = call.cust;
					$.ajax({
						dataType : "json",
						cache: false,
						type:"POST",
						data:{"user.userCode":_val},
						url : "user!checkNewUserCode.action" ,
						success : function(response){

							if($('#active_second').length === 0 || currentCall !== call.cust){
								return;
							}
							
							var restDiv = _els.resultDiv,
						        potHidden = _els.pointHidden,
						        bindBtn = _els.bindBtn;
							
							if(response.status){
								restDiv.html('您的网点是：' + '<em>' + response.infoContent + '</em>');
								potHidden.val(_val);
								els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
							}
							else {
								restDiv.html(tipsMsg.noInfo);
								potHidden.val('');
								bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
							}
						},
						complete : function(){
							_els.customerCode.removeAttr("disabled");
						}
					});
				}, 1000);
			});

			els.bindBtn.click(function() {
				subFlag++;
			});
			// 重新绑定聚焦事件
			ytoCommon.setStatus();
			};
			
			var initBranchNameText = function(){
				var uc = $("#uc").text();
				$.getJSON('user!queryBranch.action?d='+Math.random(),{'newUserCode': uc},function(data){
					$("#brchTxt").text(data);
                });
			}
			initBranchNameText();
			
			//取消账号关联
			var cancelRelation = function(){
				$.ajax({
            		type:"post",
            		dataType:"json",
            		cache:false,
            		url:"user!cancelRelation.action",
            		data:{"userCustom.customerId":config.taobaoEncodeKey},
            		success:function(response){
            			var cDialog = new Dialog();
						cDialog.init({
							contentHtml: response.infoContent,
							yes: function() {
								cDialog.close();
							},
							closeBtn: true
						});
            		},
            		error:function(response){
            			var cDialog = new Dialog();
						cDialog.init({
							contentHtml: "未知原因导致取消失败，请重新操作！",
							yes: function() {
								cDialog.close();
							},
							closeBtn: true
						});
            		}
            	});
			}
			
			var unbindUserCode = function(){
    			$.ajax({
    				type:"POST",
    				cache:false,
    				dataType:"json",
    				url:"user!unbindUserCode.action",
    				success:function(data){
        				if(data.status) {
                        	var cDialog = new Dialog();
    						cDialog.init({
    							contentHtml: "操作已成功！",
    							yes: function() {
    								cDialog.close();
    								window.location.reload(true);;
    							},
    							closeBtn: true
    						});
                        }
                        else {
                        	var cDialog = new Dialog();
    						cDialog.init({
    							contentHtml: "操作失败，稍后请重新操作！",
    							yes: function() {
    								cDialog.close();
    							},
    							closeBtn: true
    						});
                        }
    				}
    			});
			}
			
			//响应“绑定网点”、“取消绑定”、“更换网点”事件
            var do_bdwd = function(flag){
            	
            	var a1Dialog = new Dialog();
            	var msg = "正在加载中...";
            	/*
            	if(flag==1) {
            		msg = "绑定网点中...";	
            	}
            	if(flag==2) {
            		msg = "更换网点中...";	
            	}
				if(flag==3) {
					msg = "取消绑定中...";
				}
				*/
            	a1Dialog.init({ 			
					contentHtml:msg
				});
            	$.ajax({
            		url:"user!checkUserIsRelated.action",
            		dataType:"json",
            		type:"post",
            		data:{"userCustom.customerId":config.taobaoEncodeKey},
            		cache:false,
            		success:function(data){
            			a1Dialog.close();
            			if(data.status){
            				var aDialog = new Dialog();
    						aDialog.init({
    							contentHtml: "您已经被["+data.infoContent+"]关联，您是否要撤消与["+data.infoContent+"]的关联关系?",
    							yes: function() {
    								aDialog.close();
    								cancelRelation();
    							},
    							yesVal:"是",
    							no: function() {
    								aDialog.close();
    							},
    							noVal:"否",
    							closeBtn: true
    						});
    						
            			}
            			else if(data.infoContent != ''){
            				var bDialog = new Dialog();
    						bDialog.init({
    							contentHtml: data.infoContent,
    							yes: function() {
    								bDialog.close();
    							},
    							closeBtn: true
    						});
            			}
            			else{
			            	if(flag == 1){//跳转到绑定页
	            				var url =  'noint!toBindUC2.action?d='+Math.random();
				            	window.location.href=url;
	            			}
	            			else if(flag == 2){//提示修改网点
	            				var dDialog = new Dialog();
	    						dDialog.init({
	    							contentHtml: "你确定要更换网点吗？",
	    							yes: function() {
	    								dDialog.close();
	    								$("#div_uc_view").slideUp('normal');
	    			                    $("#div_uc_edit").slideDown('normal');
	    							},
	    							no: function() {
	    								dDialog.close();
	    							},
	    							closeBtn: true
	    						});
	            			}
	            			else if(flag == 3){//提示取消绑定
	            				var dDialog = new Dialog();
	    						dDialog.init({
	    							contentHtml: "你确定要取消绑定吗？",
	    							yes: function() {
	    								dDialog.close();
	    								unbindUserCode();
	    							},
	    							no: function() {
	    								dDialog.close();
	    							},
	    							closeBtn: true
	    						});
	            			}
            			}
            		},
            		error:function(data){
            			a1Dialog.close();
            			var dDialog = new Dialog();
						dDialog.init({
							contentHtml: "用户数据验证失败，请重新操作！",
							yes: function() {
								dDialog.close();
							},
							closeBtn: true
						});
            		}
            		
            	});
            }
		};
		
		return {
			init: function() {
				ytoTab.init();
//				showEditMsg();
				selectArea();
				form();
			}
		}
	})();
	
	myAccountM.init();
});