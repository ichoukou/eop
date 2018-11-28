/**
 * 增加(修改)子配置消息
 */
$(function(){
	var config = (function(){
		
		//元素集合
		var els = {
			pid:$('#pid'),
			confKey:$('#confKey'),
			confValue:$('#confValue'),
			confText:$('#confText'),
			confType:$('#confType'),
			seq:$('#seq'),
			remark:$('#remark'),
			edit:$('#edit'),
			add:$('#add'),
			backBtn:$('#back'),
		};
		
		// 提示文案
		var tipsMsg = {
			input_confKeyErr: '请输入配置名',
			input_confValueErr: '请输入配置值',
			input_confTextErr: '请输入配置显示值',
			input_seqErr: '请输入排序号'
		};
		
		$.formValidator.initConfig({
			validatorGroup: '1',
			formID: 'configFrom',
			theme: 'yto',
			errorFocus: false
		});
		
		els.confKey.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_confKeyErr});
		
		els.confValue.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_confValueErr});
		
		els.confText.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_confTextErr});
		
		els.seq.formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
		inputValidator({min: 1, onErrorMin: tipsMsg.input_seqErr});
		
		//修改子配置消息
		els.edit.click(function(ev){
			ev.preventDefault();
			// 验证结果
			var flag = $.formValidator.pageIsValid('1');
			if(flag){
				var dialog = new Dialog();
				dialog.init({
					contentHtml:'<p>确定要保存所修改的配置消息吗?</p>',
					yes:function(){
						$.ajax({
							type:'post',
							url:'config_keyUnique.action',
							data:{
								key : els.confKey.val()
							},
							success:function(data){
								dialog.close();
								if(data == 1 || els.confKey.val() == $('#originalKey').val()){
									$.ajax({
										type:'post',
										url:'config4_edit.action',
										data:{
											'configCode.id': $('#cId').val(),
											'configCode.pid' : els.pid.val(),
											'configCode.confKey' : els.confKey.val(),
											'configCode.confValue' : els.confValue.val(),
											'configCode.confText' : els.confText.val(),
											'configCode.confType' : els.confType.val(),
											'configCode.seq' : els.seq.val(),
											'configCode.remark' : els.remark.val()
										},
										success:function(data){
											var dialog2 = new Dialog();
											if(data){
												dialog2.init({
													contentHtml:'<p>配置消息修改成功!</p>',
													yes:function(){
														dialog2.close();
														window.location.href = "config_list.action?menuFlag=peizhi_config_index&pid="+els.pid.val();
													}
												});
											}else{
												dialog2.init({
													contentHtml:'<p>配置消息修改失败!请稍后重试!</p>',
													yes:function(){
														dialog2.close();
													}
												});
											}
										}
									});
								}else{
									var dialog3 = new Dialog();
									dialog3.init({
										contentHtml:'<p>配置名已存在，请重新输入!</p>',
										yes:function(){
											dialog3.close();
											els.confKey.val('');
											els.confKey.focus();
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
		
		
		//增加子配置消息
		els.add.click(function(ev){
			ev.preventDefault();
			// 验证结果
			var flag = $.formValidator.pageIsValid('1');
			if(flag){
				var dialog = new Dialog();
				dialog.init({
					contentHtml:'<p>确定要添加此配置消息吗?</p>',
					yes:function(){
						$.ajax({
							type:'post',
							url:'config_keyUnique.action',
							data:{
								key : els.confKey.val()
							},
							success:function(data){
								dialog.close();
								if(data == 1){
									$.ajax({
										type:'post',
										url:'config3_add.action',
										data:{
											levelId : $('#levelId').val(),
											'configCode.pid' : els.pid.val(),
											'configCode.confKey' : els.confKey.val(),
											'configCode.confValue' : els.confValue.val(),
											'configCode.confText' : els.confText.val(),
											'configCode.confType' : els.confType.val(),
											'configCode.seq' : els.seq.val(),
											'configCode.remark' : els.remark.val()
										},
										success:function(data){
											var dialog2 = new Dialog();
											if(data){
												dialog2.init({
													contentHtml:'<p>配置消息添加成功!</p>',
													yes:function(){
														dialog2.close();
														window.location.href = "config_list.action?menuFlag=peizhi_config_index&pid="+els.pid.val();
													}
												});
											}else{
												dialog2.init({
													contentHtml:'<p>配置消息添加失败!请稍后重试!</p>',
													yes:function(){
														dialog2.close();
													}
												});
											}
										}
									});
								}else{
									var dialog3 = new Dialog();
									dialog3.init({
										contentHtml:'<p>配置名已存在，请重新输入!</p>',
										yes:function(){
											dialog3.close();
											els.confKey.val('');
											els.confKey.focus();
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
	
	config.init();
});
