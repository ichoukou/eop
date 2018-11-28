/**
 * 问题件通知
**/
$(function() {
	var problemNotice = (function() {
			// 发送消息
			var sendMsg = function() {
				$('.send_msg1, .send_msg2').click(function(ev) {
					var sendDialog = new Dialog();
					ev.preventDefault();
					var _this = $(this),
						msg = $('.msgbg textarea', _this.parent()).val(),	// 消息内容
						msgLen = msg.length,	// 消息内容字数
						tip = '',				// 消息提示文案
						perMsgLen = 60,			// 每条消息长度
						noCallback = null;		// 弹窗取消回调
					
					if (msgLen == 0) {
						tip = '尚未输入内容';
					} else if (msgLen <= perMsgLen) {
						tip = '短信发送成功';
					} else if (msgLen > perMsgLen) {
						var msgCount = Math.ceil(msgLen / perMsgLen);
						tip = '该消息为' + msgLen + '字符，将分为' + msgCount + '条短信发送，你确定要发送吗？';
						noCallback = function() {
							sendDialog.close();
						}
					}
					
					sendDialog.init({
						yes: function() {			// 确认按钮的回调
							sendDialog.close();
							alert('提交表单');
						},
						no: noCallback,
						maskOpacity: 0,					// 遮罩层的透明度
						contentHtml: tip
					});
				})
			};
			
			
			// 清空消息
			var clearMsg = function() {
				$('.clear_msg1, .clear_msg2').click(function(ev) {
					ev.preventDefault();
					
					var _this = $(this),
						msgEl = $('.msgbg textarea', _this.parent());
						
					msgEl.val('');
				})
			};
			
			return {
				init: function() {
					sendMsg();
					clearMsg();
				}	
			}
	})();
	
	problemNotice.init();
})