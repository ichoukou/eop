/**
 * 客户编码修改
**/
/**
 * 网点查找
**/
$(function() {
	var m_network = (function() {
		var flag = 0;
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};

		// 全局配置
		var config = {
			onStep: winParams.onStep || null,
			userType:winParams.userType || null,
			taobaoEncodeKey:winParams.taobaoEncodeKey || '',
			userId:winParams.userId || ''
		};
		// 表单
		var form = function() {
				
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
				customerCode: $('#input_text_demo'),
				bindBtn: $('#bind_btn_a'),
				backBtn: $("#bind_btn_b"),
				checkForm: $('#check_form')
			};
			
			
			// 提示文案
			var tipsMsg = {
				customerCodeEmptyErr: '不能为空',
				customerCodeFormatErr: '您输入的用户编码错误'
			};
			
			// 完善基本信息表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'check_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 客户编码
			els.customerCode.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, onErrorMin: tipsMsg.customerCodeEmptyErr}).
				regexValidator({regExp: 'customerCode', dataType: 'enum', onError: tipsMsg.customerCodeFormatErr}).
				ajaxValidator({
					type: "POST",
                    dataType : "json",
                    cache: false,
                    url : "user!checkNewUserCode.action",
                    success : function(data){
                    	if(data.status) {
                            $("#new_branch_txt").text(data.infoContent);
                            if(flag > 0 && flag < 3){
                            	setTimeout(function(){els.bindBtn.click();},10);
                            }
                        }
                        else if(data.infoContent){
                        	setTimeout(function(){showIcon.error($("#input_text_demoTip"), data.infoContent)},10);
                        }
                        return data.status;
                    },
                    onError : " ",
                    onWait : "正在对新客户编码进行合法性校验，请稍候..."
                });
			
			// 点击“绑定”按钮
			els.bindBtn.click(function(ev) {
				ev.preventDefault();
//				els.checkForm.trigger('submit');
				setTimeout(
					function(){
						flag++;
						if($.formValidator.pageIsValid("1")){
							flag = 0;	
							var cDialog = new Dialog();
								cDialog.init({
									contentHtml: "为了确定您的信息安全，请确认你的服务网点是否为“"+$('#new_branch_txt').text()+"”？",
									yes: function() {
										cDialog.close();
//									els.checkForm.trigger('submit');
										$.ajax({
											type: "POST",
											dataType : "json",
											cache: false,
											data:{"user.userCode":$("#input_text_demo").val()},
											url : "noint!bindTaoBaoUserStep2.action",
											success : function(data){
												if(data.status) {
													var eDialog = new Dialog();
													eDialog.init({
														contentHtml: data.infoContent,
														yes: function() {
															$("#resetForm").click();
															eDialog.close();
															setTimeout(function(){
																window.location.href="user!toEdit.action?user.id="+config.userId;
															},0);
														},
														closeBtn: true
													});
												}
												else if(data.infoContent){
													setTimeout(function(){showIcon.error($("#input_text_demoTip"), data.infoContent)},10);
												}
											}
										});
									},
									no:function(){
										cDialog.close();
										$("#new_branch_txt").text("");
										$("#resetForm").click();
									},
									closeBtn: true
								});
							}
					},20);
			});
			
			//返回按钮
			els.backBtn.click(function(ev){
				ev.preventDefault();
				$("#resetForm").click();
				history.back();
			});
		};
		
		return {
			init: function() {
				form();
				
			}
		}
	})();

	m_network.init();
});