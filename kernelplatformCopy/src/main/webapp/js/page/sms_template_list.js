$(function() {
	var winParams = window.params || {};
	
	var config = {
		dialogSubmit: winParams.dialogSubmit || '',
		tabIndex: winParams.tabIndex || 0,
		smsTypeResult: winParams.smsTypeResult || false
	};
	
	$('#'+config.tabIndex).attr('class','cur');
	
	var templateList = (function() {	
		
		/**
		 * 查看模板信息
		 */
		var viewTem = function() {
			$('.tem_view').click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					templateId = $('.tid',_this.parent()).val();
				
				$.ajax({
					url:'template_getOneTemplate.action?menuFlag=sms_template_list',
					type:'POST',
					data:{
						id:templateId
					},
					cache: false,
					dataType:'JSON',
					success:function(data){
						var isDefault = "";
						var updateTime = "";
						if(data.isDefault == 'N'){
							isDefault='否';
						}
						else{
							isDefault='是';
						}
						var sysTypeName = '';
						var updateTime = '';
						
						if(data.remark.split("=").length > 1){
							sysTypeName = data.remark.split("=")[0];
							updateTime = data.remark.split("=")[1];
						}else{
							sysTypeName = data.remark.split("=")[0];
						}
						
						
						var viewDialog = new Dialog();
						viewDialog.init({
							contentHtml: '<div id="view_more">' +
										'	<div class="table">' +
										'		<table>' +
										'			<thead>' +
										'				<tr>' +
										'					<th colspan="2"><div class="th_title"><em>模板明细</em></div></th>' +
										'				</tr>' +
										'			</thead>' +
										'			<tbody>' +
										'				<tr>' +
										'					<td width="30%"><strong>短信类型：</strong></td>' +
										'					<td>' + sysTypeName +'</td>' +
										'				</tr>' +
										'				<tr>' +
										'					<td width="30%"><strong>模板标题：</strong></td>' +
										'					<td>' + data.name +'</td>' +
										'				</tr>' +
										'				<tr>' +
										'					<td width="30%"><strong>模板内容：</strong></td>' +
										'					<td><span class="data_content">' + data.content +'</span></td>' +
										'				</tr>' +
										'				<tr>' +
										'					<td width="30%"><strong>是否默认：</strong></td>' +
										'					<td>' + isDefault +'</td>' +
										'				</tr>' +
										'				<tr>' +
										'					<td width="30%"><strong>已发短信：</strong></td>' +
										'					<td>' + data.sendCount +' 条</td>' +
										'				</tr>' +
										'				<tr>' +
										'					<td width="30%"><strong>更新时间：</strong></td>' +
										'					<td>' + updateTime +'</td>' +
										'				</tr>' +
										'			</tbody>' +
										'		</table>' +
										'	</div>' +
										'</div>',
							yes: function() {
								viewDialog.close();
							},
							yesVal: '关 闭',
							closeBtn: true
						});
						
						window.ytoTable && ytoTable.init();
					},
					error:function(){
						var viewDialog = new Dialog();
						viewDialog.init({
							contentHtml: '抱歉！系统繁忙，请稍后再试。',
							yes: function() {
								viewDialog.close();
							},
							yesVal: '关 闭',
							closeBtn: true
						})
					}
				});
			});
		};
		
		/**
		 * 切换Tab
		 */
		var updateTab = function(){
			$('.thead_select .a').live('click',function(ev){
				ev.preventDefault();
				var _this = $(this),
					serviceId = $('.serviceId',_this.parent()).val(),
					tabNum = _this.attr('st');
					setTimeout(function(){
					window.location.href='template_index.action?menuFlag=sms_template_list&serviceId='+serviceId+"&tabNum="+tabNum;
					},0)	
			});
		};
		
		/**
		 * 添加模板
		 */
		var addTemplate = function(){
			$('#new_template').click(function(ev){
				ev.preventDefault();
				var serviceId = $('#serviceId').val(),
					tabNum = $('#tabNum').val();
					//smsTypeResult = config.smsTypeResult;
					setTimeout(function(){
					window.location.href='template_toAdd.action?menuFlag=sms_template_list&serviceId='+serviceId+"&tabNum="+tabNum;
					},0)			
			});
		};
		
		/**
		 * 删除模板
		 */
		var delTemplate = function(){
			$('.tem_del').click(function(ev){
				ev.preventDefault();
				
				var _this = $(this),
				templateId = $('.tid',_this.parent()).val(),
				tabNum = $('#tabNum').val(),
				serviceId = $('#serviceId').val();
				
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: '您确定要删除该模板吗？',
					yes: function() {
						aDialog.close();
						
						$.ajax({
							url:'template_delTemplateById.action?menuFlag=sms_template_list',
							type:'POST',
							data:{
								id:templateId
							},
							dataType:'json',
							cache: false,
							success:function(data){
								
								var viewDialog = new Dialog();
								viewDialog.init({
									contentHtml: data,
									yes: function() {
										viewDialog.close();
										
										//刷新页面
										/*
										if(data != '抱歉！默认模板不能删除！'){
											window.location.href='template_list.action?menuFlag=sms_template_list&serviceId='+serviceId+"&tabNum="+tabNum;
										}	
										*/							
										setTimeout(function(){
											window.location.href='template_list.action?menuFlag=sms_template_list&serviceId='+serviceId+"&tabNum="+tabNum;
										},0)
										
									},
									yesVal: '确定',
									closeBtn: true
								})
							},
							error:function(){
								var viewDialog = new Dialog();
								viewDialog.init({
									contentHtml: '抱歉！系统繁忙，请稍后再试。',
									yes: function() {
										viewDialog.close();
									},
									yesVal: '关 闭',
									closeBtn: true
								})
							}
						})
					},
					yesVal: '确定',
					no:function(){
						aDialog.close();
					},
					noVal:'取消',
					closeBtn: true
				})
			});
		};
		
		/**
		 * 设置默认模板
		 */
		var setDefault = function(){
			$('.tem_default').click(function(ev){
				ev.preventDefault();
				
				var _this = $(this),
				templateId = $('.tid',_this.parent()).val(),
				tabNum = $('#tabNum').val(),
				serviceId = $('#serviceId').val();
				
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: '您确定设置为默认模板吗？',
					yes: function() {
						aDialog.close();
						
						$.ajax({
							url:'template_setDefault.action?menuFlag=sms_template_list',
							type:'POST',
							data:{
								id:templateId,
								serviceId:serviceId
							},
							dataType:'json',
							success:function(data){
								
								var viewDialog = new Dialog();
								viewDialog.init({
									contentHtml: data,
									yes: function() {
										viewDialog.close();
										setTimeout(function(){
										window.location.href='template_list.action?menuFlag=sms_template_list&id='+templateId+'&serviceId='+serviceId+"&tabNum="+tabNum;
										},0)
									},
									yesVal: '确定',
									closeBtn: true
								})
							},
							error:function(){
								var viewDialog = new Dialog();
								viewDialog.init({
									contentHtml: '抱歉！系统繁忙，请稍后再试。',
									yes: function() {
										viewDialog.close();
									},
									yesVal: '关 闭',
									closeBtn: true
								})
							}
						})
					},
					yesVal: '确定',
					no:function(){
						aDialog.close();
					},
					noVal:'取消',
					closeBtn: true
				})
			});
		};
		
		/**
		 * 修改模板
		 */
		var update = function(){
			$('.tem_edit').click(function(ev){
				ev.preventDefault();
				
				var _this = $(this),
					templateId = $('.tid',_this.parent()).val(),
					tabNum = $('#tabNum').val(),
					serviceId = $('#serviceId').val();
					setTimeout(function(){
					window.location.href='template_toUpdate.action?menuFlag=sms_template_list&serviceId='+serviceId+"&id="+templateId+"&&tabNum="+tabNum;
					},0)		
			});
		};
		
		/**
		 * 查看审核失败原因
		 */
		var checkReson = function(){
	/*		$('.failImg').click(function(){
				var _this = $(this),
					templateId = $('.tid1',_this.parents('.td_b')).val();
				$.ajax({
					url:'template_getRemak.action?menuFlag=sms_template_list',
					method:'POST',
					data:{
						id:templateId
					},
					dataType:'JSON',
					success:function(data){
						var viewDialog = new Dialog();
						viewDialog.init({
							contentHtml: data,
							closeBtn: true
						})
					},
					error:function(){
						var viewDialog = new Dialog();
						viewDialog.init({
							contentHtml: '抱歉！系统繁忙，请稍后再试。',
							yes: function() {
								viewDialog.close();
							},
							yesVal: '关 闭',
							closeBtn: true
						})
					}
				});
			});*/
		};
		
		return {
			init: function() {
				viewTem();
				updateTab();
				addTemplate();
				delTemplate();
				setDefault();
				update();
				checkReson();
			}
		}
	})();
	
	templateList.init();
		
	})

