$(function() {
	var retrievePsw = (function() {
		var winParams = window.params || {},subFlag=0,
		winPath = window.path || {};

		var config = {
			loginAjaxUrl: winPath.ctx +'/login_checkFindPass.action'
		};
		
		// 表单验证
		var form = function() {
			var els = {
				loginId: $('#login_id'),
				pswForm: $('#psw_form'),
				submitBtn: $('#submit_btn')
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
			var msg = {
				accFormatErr: '账号格式错误',
				accNoExist: '登录账号不存在，请重新输入'
			};
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'psw_form',
				theme: 'yto',
				errorFocus: false,
				submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
			});
			// 回车提交表单
			$('body').live("keypress",function(ev) {
				if (ev.keyCode == 13) {
					setTimeout(function(){els.submitBtn.click();},0);
				}
			});
			// 校验登录账号
			els.loginId.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'accountId', dataType: 'enum', onError: msg.accFormatErr}).
				ajaxValidator({
					url: config.loginAjaxUrl,
					dataType:"json",
					cache:false,
					type:"POST",
					success: function(response) {
						if(!response.status){
							setTimeout(function(){
								showIcon.error($("#login_idTip"), msg.accNoExist);
							},5);
						}
						else if(subFlag > 0 && subFlag < 3){
							setTimeout(function(){
								getPswd();
							},0);
						}
						return response.status;
					},
					onError: "登录账号不存在，请重新输入",
					onWait:"正在验证账号，请稍候..."
				});
			
			// 点击“找回密码”
			els.submitBtn.click(function(ev) {
				ev.preventDefault();
				subFlag++;
				var validatorResult = $.formValidator.pageIsValid('1');
				
				if (validatorResult && subFlag > 0) {
					getPswd();
				}
			});
			
			var getPswd = function(){
				subFlag = 0;
				// 正在加载
				var loadDialog = new Dialog();
				loadDialog.init({
					contentHtml: '请稍后...'
				});
				$.ajax({
					type:"POST",
					cache:false,
					dataType:"json",
					url:"login_findPassword.action",
					data:{loginName:$("#login_id").val()},
					success:function(response){
						loadDialog.close();
						var aDialog = new Dialog();
						aDialog.init({
							contentHtml: response.infoContent,
							yes: function() {
								aDialog.close();
								if(response.status){
									window.close();
								}
							}
						});
					}
				
				});
			}
			
		};
		
		return {
			init: function() {
				form();
			}
		}
	})()

	retrievePsw.init();
})