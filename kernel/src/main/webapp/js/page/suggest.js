$(function() {
	var fsmessage = (function() {
		var winParams = window.params || {};
		
		// 表单验证
		var form = function() {
			// 元素集合
			var els = {
				topic: $('#topic'),
				message: $('#message'),
				sendMsg: $('#send_msg'),
				branchSendForm: $('#branch_send_form')
			};
			
			// 文案
			var msg = {
				topicMinErr: '主题不能为空',
				topicMaxErr: '主题不能超过20个字',
				messageMinErr: '内容不能为空',
				messageMaxErr: '内容不能超过500字'
			};
			
			// “发送消息”表单校验
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'branch_send_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 主题
			els.topic.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				inputValidator({min: 1, onErrorMin: msg.topicMinErr, max: 40, onErrorMax: msg.topicMaxErr});
				
			// 内容
			els.message.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				inputValidator({min: 1, onErrorMin: msg.messageMinErr, max: 1000, onErrorMax: msg.messageMaxErr});
			
			// 点击“发送”
			els.sendMsg.click(function(ev) {
				ev.preventDefault();
				if($.formValidator.pageIsValid('1')){
					$.ajax({
						type : 'post',
						url : 'send_suggest.action',
						data : {
							messageTheme : $("#topic").val(),
							messageContent : $("#message").val()
						},
						success : function(data){
							if(data.status==false){
								var alertDialog = new Dialog();
								alertDialog.init({
									contentHtml: data.infoContent,
									yes: function() {
										alertDialog.close();
									},
									closeBtn: true
								});
								return;
							}else if(data.status==true){
								window.location.href='message_index.action?menuFlag=home_msg_index';
							}else
								window.location.href='send_openAdviseUI.action?menuFlag=home_msg_index';
						}
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
	
	fsmessage.init();
})