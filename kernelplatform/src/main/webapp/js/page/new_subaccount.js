/**
 * 新增子账号
**/
$(function() {
	var newSubAccount = (function() {
		
		var winParam = window.params || {};
		// 配置
		var config = {
			onStep: winParam.onStep,
			showBindCode:winParam.showBindCode || false,
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
				accType: $('.input_checkbox'),
				accTypeC: $('#lj3'),
				initPsw: $('#init_psw'),
				customersTip:$('#customersTip'),
				editPsw: $('#edit_psw'),
				okBtn: $('#ok_btn'),
				backBtn:$("#back_btn"),
				pageHid: $("#pageHid")
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
				accTypeTip: '请选择账号类型',
				initPswTip: '请设置账号的初始化密码',
				customersTip: '请选择客户,默认为空'
			};
			
			// 默认显示手机和固话的提示层
			showIcon.show(els.mobileTelTip, tipsMsg.atLeastOne);
			showIcon.show(els.customersTip, tipsMsg.customersTip);
			
			// 分配客户
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
			
			$("#acc_type_a").change(function(){
				var userType = $("#userType").val();
				if($("#acc_type_a").prop("checked")){
					if(userType.length == 2){
						var sy = userType.substring(0,1)
						userType = userType.substring(1,2) == '1' ? sy+"3" : $("#acc_type_a").val();
					}
					else{
						userType = $("#acc_type_a").val();
					}
				}
				else{
					if(userType.length == 2){
						var sy = userType.substring(0,1)
						userType = userType.substring(1,2) == '3' ? sy+"1" : 
							($("#acc_type_b").prop("checked") ? $("#acc_type_b").val() : "");
					}
				}
				$("#userType").val(userType);
			});
			
			$("#acc_type_b").change(function(){
				var userType = $("#userType").val();
				if($("#acc_type_b").prop("checked")){
					if(userType.length == 2){
						var sy = userType.substring(0,1)
						userType = userType.substring(1,2) == '2' ? sy+"3" : $("#acc_type_b").val();
					}
					else{
						userType = $("#acc_type_b").val();
					}
				}
				else{
					if(userType.length == 2){
						var sy = userType.substring(0,1)
						userType = userType.substring(1,2) == '3' ? sy+"2" : 
							($("#acc_type_a").prop("checked") ? $("#acc_type_a").val() : "");
					}
				}
				$("#userType").val(userType);
			});
			
			els.accTypeC.change(function(){
				if($('#lj3').prop("checked")){
					$('#userType').val($('#lj3').val());
					$('#acc_type_a').prop('checked', false);
					$('#acc_type_a').attr('disabled',"true");
					
					$('#acc_type_b').prop('checked', false);
					$('#acc_type_b').attr('disabled',"true");
				}
				else{
					$('#acc_type_a').prop('disabled', false);
					$('#acc_type_b').prop('disabled', false);
					$('#userType').val("");
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
			els.accId.
			formValidator({validatorGroup:'1', onShow: tipsMsg.accTip, onFocus: '', onCorrect: ' '}).
			regexValidator({regExp: 'accountId', dataType: 'enum', onError: tipsMsg.accIdErr}).
			functionValidator({
				fun: function(val) {
					return val.indexOf(' ') === -1;
				},
				onError: tipsMsg.accIdErr
			}).
			ajaxValidator({
				dataType : "json",
				cache:false,
				url : "user!checkUserName.action",
				success : function(data){
					//console.log(data+"/"+(eval(data)=="true"));
					return data.status;
				},
				onError : "该账号已被使用，请重新填写登录账号",
				onWait : "正在对用户账号进行校验，请稍候..."
			});
			
			// CUSTOMID
			els.customId.
			formValidator({onShow:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到相应的订单信息",onFocus:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到响应的订单信息",onCorrect:" "})
			.inputValidator({min:6,max:35,onError:"输入的CustomerID长度不符合规则"})
			.regexValidator({regExp:"username", dataType: 'enum',onError:"输入的CustomerID字符不符合规则"})
			.ajaxValidator({
				dataType : "json",
				cache:false,
				url : "user!checkCustomerId.action",
				success : function(res){
					// console.log(res);
					return res.status;
				},
				onError : "该CustomerID已被使用，请重新填写CustomerID",
				onWait : "正在对CustomerID进行校验，请稍候..."
			});
				
				
			// 真实姓名
			els.realName.
				formValidator({validatorGroup:'1', onShow: tipsMsg.realNameTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'name', dataType: 'enum', onError: tipsMsg.realNameErr});
				
			// 邮箱
			els.email.
				formValidator({validatorGroup:'1', onShow: tipsMsg.emailTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'email', dataType: 'enum', onError: tipsMsg.emailErr});
			
			// 账号类型
			els.accType.
				formValidator({validatorGroup:'1', tipID: 'checkTip', onShow: tipsMsg.accTypeTip, onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, onErrorMin: tipsMsg.accTypeErr});
				
			// 初始密码
			els.initPsw.
				formValidator({validatorGroup:'1', onShow: tipsMsg.initPswTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'password', dataType: 'enum', onError: tipsMsg.pswFormatErr});
			
			// 编辑密码
			els.editPsw.
				formValidator({validatorGroup:'1', onShow:'', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: ['password'], dataType: 'enum', onError: tipsMsg.pswFormatErr});
				
			// 点击“确认”
			els.okBtn.click(function(ev) {
				ev.preventDefault();
				//验证当前卖家有没有绑定网点
				if(config.onStep < 3 && config.showBindCode){
					window.location.reload(true);
					return;
				}
				
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
					//提交承包区的客户
					if(els.pageHid.val() == 'add'){
						var customersAct = $('input[name="customerHid"]');
						var ids = '';
						var posttempNames = '';
						var noAgainNames = '';
						for(var i=0; i<customersAct.length; i++){
							if(customersAct[i].checked == true && customersAct[i].disabled != true){
								var strs = customersAct[i].className.split(' ');
								if($('#lj3').is(':checked') == true ){
									if(strs[0] == 'isContractBox'){//勾选了运费模板用户
										if(posttempNames != ''){
											posttempNames = posttempNames + '，'
										}
										posttempNames = posttempNames +  strs[1]; 
									}
									if(strs[0] == 'yesDelivery'){//非模板用户，是已经分配给客服或财务的用户
										if(noAgainNames != ''){
											noAgainNames = noAgainNames + '，'
										}
										noAgainNames = noAgainNames +  strs[1]; 
									}
									
								}
								if(ids!=''){
									ids += ',';
								}
								ids += customersAct[i].value;
							}
						}
						
						if(posttempNames != ''){
							var dialog = new Dialog();
							dialog.init({
								contentHtml:'<b>'+posttempNames+'</b> 已经是运费模板用户,不能分配给承包区账号.请重新选择!',
								autoClose: 999000,
								yes:function(){
									dialog.close();
								}
							});
							
							return false;
						}
						
						if(noAgainNames != ''){
							var dialog = new Dialog();
							dialog.init({
								contentHtml:'<b>'+noAgainNames+'</b> 已经分配,不能再分配给承包区账号.请重新选择!',
								autoClose: 999000,
								yes:function(){
									dialog.close();
								}
							});
							
							return false;
						}
						
						var addUserName = $.trim(els.accId.val());
						if(ids==''){
							els.newAccForm.attr('action','user_addSubAccount.action'+ '?r=' + Math.random());
						}else{
							els.newAccForm.attr('action','user_addSubAccount.action?ids='+ids+'&addUserName='+addUserName+ '&r=' + Math.random());
						}
					}
					
					els.newAccForm.trigger('submit');
				}
			});
			
			//"返回"按钮
			els.backBtn.click(function(){
				setTimeout(function(){
					history.back();
				},0);
			});
			
			pagination.click(function(){
				setTimeout(function(){
					$("#pageForm").submit();
				},0);
			});
			
			//弹窗
			$('#addCustomers').live('click',function(ev){
				ev.preventDefault();
				var msgDialog = new Dialog();
				var T = $('.T'), Ts = T.filter(':checked').size();
				if(!Ts) {
					msgDialog.init({
						contentHtml:'请先选择账号类型',
						yes:function(){
							msgDialog.close();
						}
						
					});
					return false;
				}
				
				els.newAccForm.append('<div id="smallWindow" style="display:none"></div>');
				var _this = $(this),
				    html = $('#smallWindow');
					
				
				//子账号新增页面------------------
				if(els.pageHid.val() == 'add'){
					// Start ajax
					var check=0;
					if($('#lj3').is(':checked')){
						check=1;
					}
					var userType = $("#userType").val();
					
					var content='<div id="popBox" style="height:400px;width:400px;position:relative;"><div style="position:absolute;top:200px;left:130px;"><img src="images/module/lightbox-ico-loading.gif"  style="vertical-align:middle;margin-right:10px;"/>数据正在加载请稍后。。</div></div>';
					var setDialog = new Dialog();
					setDialog.init({
						contentHtml:content,
	                    closeBtn: true,
						yes: function() {
							//保存弹窗的隐藏域
							var customers = $('#dialog_bd input[name=customer]');
							for(var i=0; i<customers.length; i++){
								if(customers[i].checked == true){
									if($('#customerHid'+customers[i].value).length == 0){
										var strs = customers[i].className.split(' ');
										var val = customers[i].getAttribute('val');
										var strs1 = '';
										if(val != null){
											strs1 = val.split(' ');
										}
										if(val != null && strs1[0] == 'yesDelivery'){
											els.newAccForm.append("<input type='checkbox' val='"+val+"' class='activityChecked' style='display:none;' checked=true name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"'  />");
										}
										else if(strs[0] == 'isContractBox'){//勾选了运费模板用户
											els.newAccForm.append("<input type='checkbox' class='"+customers[i].className+"' style='display:none;' checked=true name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"'  />");
										}
										else if(customers[i].className == 'activityChecked'){
											els.newAccForm.append("<input type='checkbox' style='display:none;' checked=true class='activityChecked' name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"' />");
										}
										else if(customers[i].className == 'canChecked'){
											els.newAccForm.append("<input type='checkbox' style='display:none;' checked=true class='canChecked' name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"' />");
										}
									}else{
										$('#customerHid'+customers[i].value).attr('checked',true);
									} 
								}else{
									if($('#customerHid'+customers[i].value).length == 0){
										var strs = customers[i].className.split(' ');
										var val = customers[i].getAttribute('val');
										var strs1 = '';
										if(val != null){
											strs1 = val.split(' ');
										}
										if(val != null && strs1[0] == 'yesDelivery'){
											els.newAccForm.append("<input type='checkbox' val='"+val+"' class='activityChecked' style='display:none;' name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"'  />");
										}
										else if( strs[0] == 'isContractBox'){//勾选了运费模板用户
											els.newAccForm.append("<input type='checkbox' class='"+customers[i].className+"' style='display:none;' name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"'  />");
										}
										else if(customers[i].className=='activityChecked'){
											els.newAccForm.append("<input type='checkbox' style='display:none;' class='activityChecked' name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"' />");
										}
										else if(customers[i].className=='canChecked'){
											els.newAccForm.append("<input type='checkbox' style='display:none;' class='canChecked' name='customerHid' id='customerHid"+customers[i].value+"' value='"+customers[i].value+"' />");
										}
									}else{
										
									}
									$('#customerHid'+customers[i].value).attr('checked',false);
								}
							}
							
							setDialog.close();
						},
						no: function() {
							setDialog.close();
						},
						yesVal: '确定',
						noVal: '取消'
					});
						$.ajax({
							url : 'user!addCustomersList.action?'+'check='+check+'&r='+ Math.random(),
							type:'post',
							cache:false,
							dataType:'json',
							success : function(data) {
								//弹窗
								//smgDialog.close();
								$("#popBox").html('');
								$("#popBox").append(data.delivery);
								var customersHid = $('input[name="customerHid"]');
								for(var i=0;i<customersHid.length;i++){
									var classVal=customersHid.eq(i).attr("class");
									$("#smallTab").find("."+classVal).each(function(){
										if($(this).val()==customersHid.eq(i).val()&&$(this).prop("disabled")!=true){
											if(customersHid.eq(i).prop("checked")==true){											
												$(this).attr('checked',true);
											}else{
											  $(this).attr('checked',false);
											}
										};
									})
								}
							}
						});
					//End ajax
				}
				
			});
			
		};
		
		var showPrompt = function(){
			if($("#isValidate").val() == 'true'){
				var showDialog = new Dialog();
				
				showDialog.init({
					contentHtml: '操作已成功！',
					yes: function() {
						setTimeout(function(){
							showDialog.close();
							window.location.href=config.listUrl;
						},0);
					},
					yesVal:"管理子账号",
					no: function() {
						setTimeout(function(){
							showDialog.close();
							window.location.href=config.addUrl;
						},0);
					},
					noVal:"新建子账号"
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


