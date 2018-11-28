/**
 * 增加(修改)渠道信息
 */
$(function(){
	var channel = (function(){
		
		//元素集合
		var els = {			
			key:$('#key'),
			cvalue:$('#cvalue'),
			ipAddress:$('#ipAddress'),
			clientId:$('#clientId'),
			parternId:$('#parternId'),
			edit:$('#edit'),
			add:$('#add'),
			backBtn:$('#back'),
			channelFrom:$('#channelFrom'),
			ip:$('#ip'),
			isPrint:$('#isPrint'),
			isSend:$('#isSend'),
			lineType:$('#lineType'),
			userType:$('#userType'),
		};
		
		// 提示文案
		var tipsMsg = {
			input_keyErr: '客户名称不能为空',
			input_valueErr: '属性值不能为空',
			input_ipAddressErr: 'IP地址不能为空',
			input_clientIdErr: 'ClientId不能为空',
			input_parternIdErr: 'parternId不能为空',
			input_ipErr: 'Ip白名单不能为空',
			input_isPrintErr: '打印标识不能为空',
			input_isSendErr: '推送标识不能为空'
		};
		
		$.formValidator.initConfig({
			validatorGroup: '1',
			formID: 'channel_form',
			theme: 'yto',
			errorFocus: false
		});
		
		els.key.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_keyErr});
		
		els.cvalue.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_valueErr});
        
		//由于有的客户不需要物流状态，所以没有推送地址，ip推送地址不做为空jiao      		
//		els.ipAddress.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
//		inputValidator({min: 1, onErrorMin: tipsMsg.input_ipAddressErr});
		
		els.clientId.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_clientIdErr});
		
		els.parternId.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_parternIdErr});
		
		els.ip.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_ipErr});
		
		//修改渠道消息
		els.edit.click(function(ev){
			ev.preventDefault();
			// 验证结果
			var flag = $.formValidator.pageIsValid('1');
			if(flag){
				var dialog = new Dialog();
				dialog.init({
					contentHtml:'<p>确定要保存所修改的渠道消息吗?</p>',
					yes:function(){
						$.ajax({
							type:'post',
							url:'channel2_edit.action',
							data:{
								'channel.id': $('#cId').val(),
								'channel.key' : els.key.val(),
								'channel.value' : els.cvalue.val(),
								'channel.ipAddress' : els.ipAddress.val(),
								'channel.clientId' : els.clientId.val(),
								'channel.ip' : els.ip.val(),
								'channel.isPrint' : els.isPrint.val(),
								'channel.isSend' : els.isSend.val(),
								'channel.lineType' : els.lineType.val(),
								'channel.parternId' : els.parternId.val(),
								'channel.userType' : els.userType.val()
							},
							success:function(data){
								var dialog2 = new Dialog();
								dialog.close();
								if(data){
									dialog2.init({
										contentHtml:'<p>渠道消息修改成功!</p>',
										yes:function(){
											dialog2.close();
											window.location.href = "channel_list.action?menuFlag=peizhi_channel_list";
											/*$.ajax({
												url:'channel_list.action?menuFlag=channel_list'
											});*/
										}
									});
								}else{
									dialog2.init({
										contentHtml:'<p>渠道消息修改失败!请稍后重试!</p>',
										yes:function(){
											dialog2.close();
										}
									});
								}
							}
						});
						/*dialog.close();
						els.channelFrom.trigger('submit');*/
					},
					no:function(){
						dialog.close();
					},
					closeBtn:true
				});
			}
		});
		
		
		//增加渠道消息
		els.add.click(function(ev){
			ev.preventDefault();
			// 验证结果
			var flag = $.formValidator.pageIsValid('1');
			/*var flag = true;*/
			if(flag){
				var dialog = new Dialog();
				dialog.init({
					contentHtml:'<p>确定要添加此渠道消息吗?</p>',
					yes:function(){
						$.ajax({
							type:'post',
							url:'channel1_add.action',
							data:{
								'channel.key' : els.key.val(),
								'channel.value' : els.cvalue.val(),
								'channel.ipAddress' : els.ipAddress.val(),
								'channel.clientId' : els.clientId.val(),
								'channel.ip' : els.ip.val(),
								'channel.isSend' : els.isSend.val(),
								'channel.isPrint' : els.isPrint.val(),
								'channel.lineType' : els.lineType.val(),
								'channel.parternId' : els.parternId.val(),
								'channel.userType' : els.userType.val()
							},
							success:function(data){
								var dialog2 = new Dialog();
								dialog.close();
								if(data){
									dialog2.init({
										contentHtml:'<p>渠道消息添加成功!</p>',
										yes:function(){
											dialog2.close();
											window.location.href = "channel_list.action?currentPage=1&menuFlag=peizhi_channel_list";
											/*$.ajax({
												url:'channel_list.action?menuFlag=channel_list'
											});*/
										}
									});
								}else{
									dialog2.init({
										contentHtml:'<p>渠道消息添加失败!请稍后重试!</p>',
										yes:function(){
											dialog2.close();
										}
									});
								}
							}
						});
					},
					no:function(){
						dialog.close();
					},
					closeBtn:true
				});
			}
		});
		
		els.backBtn.click(function(){
			history.go(-1);
		});
	
		return {
			init: function() {
				
			}
		} 
		
	})();
	
	channel.init();
});
