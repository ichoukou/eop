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
			var subFlag = false;			
				
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
				initPsw: $('#init_psw'),
				okBtn: $('#ok_btn'),
				backBtn:$("#back_btn")
			};
			
			// 提示文案
			var tipsMsg = {
				accIdErr: '登录账号格式错误',
				realNameErr: '真实姓名格式错误',
				pswFormatErr: '密码格式不正确',
				accTip: '请填写登录账户，保存后不能修改',
				realNameTip: '请输入真实姓名',
				accTypeTip: '请选择账号类型',
				initPswTip: '请设置账号的初始化密码'
			};
			
			
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
			ajaxValidator({
				dataType : "json",
				cache:false,
				url : "user!checkUserName.action",
				success : function(data){
					return data.status;
				},
				onError : "该账号已被使用，请重新填写登录账号",
				onWait : "正在对用户账号进行校验，请稍候..."
			});
			
			// CUSTOMID
			els.customId.
			formValidator({onShow:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到相应的订单信息",onFocus:"这里设置用户的唯一标识号，请与系统对接时创建订单中的CustomerID保持对应，系统将通过该标识号关联到相应的订单信息",onCorrect:" "})
			.inputValidator({min:6,max:35,onError:"输入的CustomerID长度不符合规则"})
			.regexValidator({regExp:"username", dataType: 'enum',onError:"输入的CustomerID字符不符合规则"})
			.ajaxValidator({
				dataType : "json",
				cache:false,
				url : "user!checkCustomerId.action",
				success : function(res){
					if(res.status && subFlag){
						setTimeout(function(){els.okBtn.click();},5);
					}
					return res.status;
				},
				onError : "该CustomerID已被使用，请重新填写CustomerID",
				onWait : "正在对CustomerID进行校验，请稍候..."
			});
				
				
			// 真实姓名
			els.realName.
				formValidator({validatorGroup:'1', onShow: tipsMsg.realNameTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'name', dataType: 'enum', onError: tipsMsg.realNameErr});
				
			// 初始密码
			els.initPsw.
				formValidator({validatorGroup:'1', onShow: tipsMsg.initPswTip, onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'password', dataType: 'enum', onError: tipsMsg.pswFormatErr});
			
			// 点击“确认”
			els.okBtn.live("click",function(ev) {
				ev.preventDefault();
				subFlag = true;
				els.newAccForm.trigger('submit');
				
			});
			
			//"返回"按钮
			els.backBtn.click(function(){
				history.back();
			});
			
		};
		
		var showPrompt = function(){
			if($("#isValidate").val() == 'true'){
				var showDialog = new Dialog();
				
				showDialog.init({
					contentHtml: '操作已成功！',
					yes: function() {
						showDialog.close();
						window.location.href=config.listUrl;
					},
					yesVal:"管理业务账号",
					no: function() {
						showDialog.close();
						window.location.href=config.addUrl;
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


