/**
 * 新增子账号
**/
$(function() {
	var newSubAccount = (function() {
		
		var winParam = window.params || {};
		// 配置
		var config = {
			userType: winParam.userType||"",
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
				var userType = "";
				if($("#acc_type_a").prop("checked")){
					userType = $("#acc_type_b").prop("checked") ? config.userType+"3" : config.userType+"2";
//					if(userType.length == 2){
//						var sy = userType.substring(0,1)
//						userType = userType.substring(1,2) == '1' ? sy+"3" : $("#acc_type_a").val();
//					}
//					else{
//						userType = $("#acc_type_a").val();
//					}
				}
				else{
					userType = $("#acc_type_b").prop("checked") ? config.userType+"1" : "";
//					if(userType.length == 2){
//						var sy = userType.substring(0,1)
//						userType = userType.substring(1,2) == '3' ? sy+"1" : 
//							($("#acc_type_b").prop("checked") ? $("#acc_type_b").val() : "");
//					}
				}
				$("#userType").val(userType);
			});
			
			$("#acc_type_b").change(function(){
				var userType = "";
				if($("#acc_type_b").prop("checked")){
					userType = $("#acc_type_a").prop("checked") ? config.userType+"3" : config.userType+"1";
//					if(userType.length == 2){
//						var sy = userType.substring(0,1)
//						userType = userType.substring(1,2) == '2' ? sy+"3" : $("#acc_type_b").val();
//					}
//					else{
//						userType = $("#acc_type_b").val();
//					}
				}
				else{
					userType = $("#acc_type_a").prop("checked") ? config.userType+"2" : "";
//					if(userType.length == 2){
//						var sy = userType.substring(0,1)
//						userType = userType.substring(1,2) == '3' ? sy+"2" : 
//							($("#acc_type_a").prop("checked") ? $("#acc_type_a").val() : "");
//					}
				}
				$("#userType").val(userType);
			});
			
			els.accTypeC.change(function(){
				if($('#lj3').prop("checked")){
					var changeValue = $('input[name="user.canChangeToContract"]').val();
					if(changeValue == '1'){
						$('#userType').val($('#lj3').val());
						$('#acc_type_a').prop('checked', false);
						$('#acc_type_a').attr('disabled',"true");
						
						$('#acc_type_b').prop('checked', false);
						$('#acc_type_b').attr('disabled',"true");
					}
					$('#userType').val("2");
				}
				else{
					var changeValue = $('input[name="user.canChangeToContract"]').val();
					if(changeValue == '1'){
						$('#acc_type_a').prop('disabled', false);
						$('#acc_type_b').prop('disabled', false);
					}
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
			formValidator({validatorGroup:'1', onShow: tipsMsg.accTip, onFocus: '', onCorrect: ' '})
			
			// 真实姓名
			els.customId.
			formValidator({onShow:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到相应的订单信息",onFocus:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到相应的订单信息",oncorrect:" "})
			.inputValidator({min:6,max:35,onError:"输入的CustomerID长度不符合规则"})
			.regexValidator({regExp:"^\\w+$", dataType: 'enum',onError:"输入的CustomerID字符不符合规则"})
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
					
					//编辑子账号
					//提交承包区的客户
					if(els.pageHid.val() == 'edit'){
						var customersAct = $('input[name="customerHid"]');
						//var customers = $('input[name="customer"]');
						var editUserName = $.trim(els.accId.val());
						var editUserType = $('input[name="user.userType"]').val();
						var ids = '';
						var lj3 = $('#lj3');
						var updateVar = '';
						var updateFlg = false;
						var posttempNames = '';
						var noAgainNames = '';
						
						//检查是否有运费模板的客户被分配给了承包区子账号
						for(var i=0; i<customersAct.length; i++){
							if(customersAct[i].checked == true && customersAct[i].disabled != true){
								var strs = customersAct[i].className.split(' ');
								var val = customersAct[i].getAttribute('val');
								var strs1 = '';
								if(val != null){
									strs1 = val.split(' ');
								}
								if($('#lj3').is(':checked') == true){//勾选了运费模板用户
									if( strs[0] == 'isContractBox'){
										if(posttempNames != ''){
											posttempNames = posttempNames + '，';
										}
										posttempNames = posttempNames + strs[1];
									}
									if(val != null && strs1[0] == 'yesDelivery'){//非模板用户，是已经分配给客服或财务的用户
										if(noAgainNames != ''){
											noAgainNames = noAgainNames + '，'
										}
										noAgainNames = noAgainNames +  strs1[1]; 
									}
									
								}
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
						
						//更新客户区表的accountType JQUERY
						updateVar = {
								dataType : "json",
								async: false,
								cache: "false",
								data:{
									addUserName:editUserName,
									accountType:editUserType
								},
								url : 'user!updateContracts.action'+ '?r=' + Math.random(),
								error:function(data){
									return false;
								}
						}
						
						if(customersAct.length == 0 ){
							updateFlg = true;
						} else if (customersAct.length != 0){
							for(var i=0; i<customersAct.length; i++){
								//新增客户的累加
								var strs = customersAct[i].className.split(' ');
								if(customersAct[i].checked == true && (customersAct[i].className == 'activityChecked' || strs[0] == 'isContractBox')){
									if(ids!=''){
										ids += ',';
									}
									ids += customersAct[i].value;
								}
								
								//删除未勾选的承包用户（原来是本用户的承包用户）
								if(customersAct[i].checked != true && customersAct[i].className == 'canChecked'){
									var conractAreaId = customersAct[i].value;
									var req = {
											dataType : "json",
											async: false,
											cache: "false",
											data:{
												addUserName:editUserName,
												conractAreaId:conractAreaId
											},
											url : 'user!deleteCustomersList.action'+ '?r=' + Math.random(),
											error:function(data){
												return false;
											}
									}
									jQuery.ajax(req);
								}
								
								//因为子账号类型的变动来更新客户区表的accountType
								if(updateFlg != true && customersAct[i].checked == true && customersAct[i].className == 'canChecked'){
									updateFlg = true;
								}
								
							}
						}
						if(updateFlg){
							jQuery.ajax(updateVar);
						}
						if(ids == ''){
							els.newAccForm.attr('action','user!editSubAccount.action'+ '?r=' + Math.random());
						}else{
							els.newAccForm.attr('action','user!editSubAccount.action?ids='+ids+'&addUserName='+editUserName+ '&r=' + Math.random());
						}
					}
					
					
					els.newAccForm.trigger('submit');
				}
			});
			
			//"返回"按钮
			els.backBtn.click(function(){
				setTimeout(function(){history.back();},0);
			});
			
			pagination.click(function(){
				setTimeout(function(){$("#pageForm").submit();},0);
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
				
				
				
				//子账号编辑页面------------------
				if(els.pageHid.val() == 'edit'){
					var smgDialog = new Dialog();
					smgDialog.init({
						contentHtml:'正在加载中...'
					});
					var editUserName = $.trim(els.accId.val());
					
					var userType = $('input[name="user.userType"]').val();
					//Start ajax
					$.ajax({
							url : 'user!editCustomersList.action?editUserName='+ editUserName + '&r=' + Math.random(),
							type:'post',
							async: false,
							cache:false,
							dataType:'json',
							success : function(data) {
								html.append('<div id="small_tab_box"><table id="smallTab"><tbody></tbody></table></div>');
								if(data.noDeliveryList == '' || data.noDeliveryList == '[]' || data.noDeliveryList.length == 0){
									if(data.yesDeliveryList == '' || data.yesDeliveryList == '[]' || data.yesDeliveryList.length == 0){
										$('#smallWindow tbody').append('<tr><td><b>暂无客户分配！</b></td></tr>');
									}
								}
								
								var index = 0;
								if(data.noDeliveryList != '[]' && data.noDeliveryList.length > 0){
									$('#smallWindow tbody').append("<tr><td colspan='2' style='height:30px;'><b>还未分配的客户有：</b></td></tr>");
								}
								
								for (var i=0; i<data.noDeliveryList.length; i++) {
									if(i%2 == 0){
										index = i/2;
										$('#smallWindow tbody').append("<tr class='trNoID"+index+"' style='height:30px;'></tr>");
									}
									
									$('.trNoID'+index).append("<td><span><input name='customer' class='activityChecked' id='customer"+data.noDeliveryList[i].id+"' type='checkbox' value='"+data.noDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.noDeliveryList[i].userName+"</span></td>");
								
									//记录刚才选中的
									var customersHid = $('input[name="customerHid"]');
									for(var j=0; j<customersHid.length; j++){
										if(customersHid[j].value == data.noDeliveryList[i].id){
											if(customersHid[j].checked==true){
												$('#customer'+data.noDeliveryList[i].id).attr('checked',true);
											}else{
												$('#customer'+data.noDeliveryList[i].id).attr('checked',false);
											}
										}
									}
								}
								
								
								index = 0;
								if(data.yesDeliveryList != '[]' && data.yesDeliveryList.length > 0){
									var content = '';
									if($('#lj3').is(':checked')){
										content = "已分配的客户有：";
										$('#smallWindow tbody').append("<tr style='height:30px;'><td colspan='2' style='width:400px;'><b>"+content+"</b></td></tr>");
									}else{
										content = "已分配的客户有：";
										$('#smallWindow tbody').append("<tr style='height:20px;'><td colspan='2' style='width:400px;'><b>"+content+"</b></td></tr>");
										content = "<font color='#436EEE'>注：未分配给承包区的客户可继续分配（灰色为不可分配）</font>";
										$('#smallWindow tbody').append("<tr style='height:30px;'><td colspan='2' style='width:400px;'><b>"+content+"</b></td></tr>");
									}
									
								}
								
								for (var i=0; i<data.yesDeliveryList.length; i++) {
									if(i%2 == 0){
										index = i/2;
										$('#smallWindow tbody').append("<tr class='trYesID"+index+"' style='height:30px;'></tr>");
										$('#smallWindow tbody').append("<tr></tr>");
									}
									
									if(data.yesDeliveryList[i].contractStatus == 'true'){
										if(data.yesDeliveryList[i].editUserName == editUserName){
											$('.trYesID'+index).append("<td><span><input name='customer' class='canChecked' checked=true id='customer"+data.yesDeliveryList[i].id+"' type='checkbox' value='"+data.yesDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.yesDeliveryList[i].userName+"</span></td>");
										}else if(userType == '2' || data.yesDeliveryList[i].isAgain == 'no'){
											$('.trYesID'+index).append("<td><span><input name='customer' disabled=true checked=true id='customer"+data.yesDeliveryList[i].id+"' type='checkbox' value='"+data.yesDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.yesDeliveryList[i].userName+"</span></td>");
										}else if(data.yesDeliveryList[i].mailNoStatus == 'true'){
											$('.trYesID'+index).append("<td><span><input name='customer' class='isContractBox "+data.yesDeliveryList[i].userName+"' id='customer"+data.yesDeliveryList[i].id+"' type='checkbox' value='"+data.yesDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.yesDeliveryList[i].userName+"</span></td>");
										}else {
											$('.trYesID'+index).append("<td><span><input name='customer' val='yesDelivery "+data.yesDeliveryList[i].userName+"' class='activityChecked' id='customer"+data.yesDeliveryList[i].id+"' type='checkbox' value='"+data.yesDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.yesDeliveryList[i].userName+"</span></td>");
										}
									}
									else if(data.yesDeliveryList[i].mailNoStatus == 'true'){
										if($('#lj3').is(':checked') != true){
											$('.trYesID'+index).append("<td><span><input name='customer' class='isContractBox "+data.yesDeliveryList[i].userName+"' id='customer"+data.yesDeliveryList[i].id+"' type='checkbox' value='"+data.yesDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.yesDeliveryList[i].userName+"</span></td>");
										}else{
											$('.trYesID'+index).append("<td><span><input name='customer' disabled=true checked=true id='customer"+data.yesDeliveryList[i].id+"' type='checkbox' value='"+data.yesDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.yesDeliveryList[i].userName+"</span></td>");
										}
									}
									else{
										$('.trYesID'+index).append("<td><span><input name='customer' val='yesDelivery "+data.yesDeliveryList[i].userName+"' class='activityChecked' id='customer"+data.yesDeliveryList[i].id+"' type='checkbox' value='"+data.yesDeliveryList[i].id+ "' /></span><span class='tabtop_span'>"+data.yesDeliveryList[i].userName+"</span></td>");
									}
									
									//记录刚才选中的
									var customersHid = $('input[name="customerHid"]');
									for(var j=0; j<customersHid.length; j++){
										if(customersHid[j].value == data.yesDeliveryList[i].id  && customersHid[j].disabled != true){
											if(customersHid[j].checked==true){
												$('#customer'+data.yesDeliveryList[i].id).attr('checked',true);
											}else{
												$('#customer'+data.yesDeliveryList[i].id).attr('checked',false);
											}
										}
									}
								}
								
								
								//弹窗
								smgDialog.close();
								var setDialog = new Dialog();
								var content = html.html();
								$('#smallWindow').remove();
								setDialog.init({
									contentHtml: content,
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
								
							}
						});
					//End ajax	
				}
				
			});
			
			if(els.pageHid.val() == 'edit'){
				var ut = $('input[name="user.userType"]').val();
				var changeValue = $('input[name="user.canChangeToContract"]').val();
				
				if(ut != '2' && changeValue != '1'){
					$('#lj3').attr('disabled', true).attr('checked', false);
				}
				
			}
			
			
			
		};
		
		var showPrompt = function(){
			if($("#isValidate").val() == 'true'){
				var showDialog = new Dialog();
				
				showDialog.init({
					contentHtml: '操作已成功！',
					yes: function() {
						setTimeout(function(){
							showDialog.close();
							window.location.href=config.listUrl;},0
						);
					},
					yesVal:"管理子账号",
					no: function() {
						setTimeout(function(){
							showDialog.close();
							window.location.href=config.addUrl;},0
						);
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


