$(function() {
	var fsmessage = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			searUserAction: winParams.searUserAction || ''			// 查找用户
		};
		
		// 模拟下拉框
		var simulateSel = function() {
			$('#user_sel').click(function(ev) {
				ev.preventDefault();
				$('#simulate_sel').show();
				receiverAjax();
			});
			
			// 搜索查找用户
			$('#sear_user_btn').click(function(ev) {
				ev.preventDefault();
				receiverAjax();
				
			});
			
			// 确定选择用户
			$('#ok_btn').click(function(ev) {
				ev.preventDefault();
				var checkedInput = $('#sel_box input[type="checkbox"]:checked'),
					userCodeArr = [],//用户编码
					userIdArr = [],//网点id
					userNameArr = [],
					subLen = 25;
				/**
				 * 给收件人赋值
				 */	
				for (var i=0,l=checkedInput.length; i<l; i++) {
					if(checkedInput.eq(i).parent().val()==1)
						userCodeArr.push(checkedInput.eq(i).val());
					if(checkedInput.eq(i).parent().val()==2)
						userIdArr.push(checkedInput.eq(i).val());
					userNameArr.push($.trim(checkedInput.eq(i).next().html()));
				}
				var userNameStr = userNameArr.toString();
	
				if (userNameStr.length > subLen) {
					userNameStr = userNameStr.substr(0, subLen) + '……';
				}
				
				if (userCodeArr.length > 0 || userIdArr.length > 0) {	// 如果有勾选的用户
					// 截断的客户姓名填到 span 中
					$('#user_sel').val(userNameStr);
					// 收件人信息插入隐藏域
					$('#receiveUserCodeString').val(userCodeArr.toString());
					$('#receiveIdString').val(userIdArr.toString());
					//给user_codes赋值，用于验证
					if(userCodeArr.length > 0)
						$("#user_codes").val(userCodeArr.toString());
					if(userIdArr.length > 0)
						$("#user_codes").val(userIdArr.toString());
				} else {						// 如果没有勾选用户
					// 重置输入框
					$('#user_sel').val('选择客户');
					
					// 重置隐藏域
					$('#receiveUserCodeString').val('');
					$('#receiveIdString').val('');
					$("#user_codes").val('');
				}
				
				// 关闭模拟下拉
				$('#simulate_sel').hide();
				$.formValidator.pageIsValid('1');
			})
		};
		
		var receiverAjax = function(){
			var selBox = $('#sel_box');
			var userId = encodeURIComponent($('#user_id').val());
			selBox.html('<p>正在加载中...</p>');
			// 异步获取
			$.ajax({
				url: config.searUserAction + userId,
				type: 'GET',
				dataType: 'json',
				cache: false,
				success: function(data) {
					$("#sel_all").removeAttr("checked");
					if(data==null || data.length==0){
						selBox.html('<p>查找不到客户...</p>');
						$('#sel_all').prop('disabled', true);
					}else{
						var dataHtml = '';
						// 拼接 html
						for (var i=0,l=data.length; i<l; i++) {
							var checkBoxVal='';
							if(data[i].userType==1)//直客编码
								checkBoxVal=data[i].userCode;
							if(data[i].userType==2)//网点id
								checkBoxVal=data[i].userId;
							dataHtml += '<li value=' +data[i].userType+ '>' +
										'	<input type="checkbox" value="' + checkBoxVal + '" />' +
										'	<label>' + data[i].userName + '</label>' +
										'</li>';
						}
						if (data.length>0) {
							$('#sel_all').prop('disabled', false);
						}
						
						// 如果超过5个li，定高显示滚动条
						if (data.length>=5) {
							selBox.css({
								height: 24*5,
								overflowY: 'auto',
								overflowX: 'hidden'
							});
						}
						
						// 插入html
						selBox.html('<ul>' + dataHtml + '</ul>');
						
						// label点击触发 checkbox 选中
						$('label', selBox).click(function() {
							$(this).prev().trigger('click');
						});
						
						// 全选
						$('#sel_all').checkAll($('li input[type="checkbox"]', selBox));
					}
				}
			});
		}
		
		// 表单验证
		var form = function() {
			// 元素集合
			var els = {
				topic: $('#topic'),
				userCode: $('#user_codes'),
				message: $('#message'),
				sendMsg: $('#send_msg'),
				branchSendForm: $('#branch_send_form')
			};
			
			// 文案
			var msg = {
				topicMinErr: '主题不能为空',
				topicMaxErr: '主题不能超过20个字',
				userMinErr: '用户不能为空',
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
				
			// 用户
			if(winParams.userType==2 || winParams.userType==3){
				els.userCode.
					formValidator({validatorGroup:'1', tipID: 'user_selTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
					inputValidator({min: 1, onErrorMin: msg.userMinErr});
			}
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
						url : 'send_send.action',
						data : {
							messageTheme : $("#topic").val(),
							receiveIdString : $("#receiveIdString").val(),
							receiveUserCodeString : $("#receiveUserCodeString").val(),
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
							}else{
								window.location.href='send_openUI.action?menuFlag=home_msg_index';
							}
						}
					});
				}
			});
		};
		
		// 全选
		var selectAll = function() {
			/**
			 * input 全选方法
			 * 使用方法（伪代码）：$('全选框元素').checkAll( $('复选框元素集合') );
			 * @param els {Element} 需要全选的 input[type="checkbox"] 复选框元素集合
			**/
			$.fn.checkAll = function(els) {
				var _this = $(this);
				
				// 勾选全选框
				_this.change(function() {
					var propChecked = $(this).prop('checked');
					if (propChecked) {			// 如果勾选中
						els.prop('checked', true);
					} else {					// 如果不勾选
						els.prop('checked', false);
					}
				});
				
				// 勾选其他 input 复选框
				els.change(function() {
					var el = $(this);
					// 如果全选框勾中，但当前复选框不勾中
					if (_this.prop('checked') && !el.prop('checked')) {
						_this.prop('checked', false);
					}
				});
			};
		};
		
		return {
			init: function() {
				selectAll();
				simulateSel();
				form();
			}
		}
	})();
	
	fsmessage.init();
})