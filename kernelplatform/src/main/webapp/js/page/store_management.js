/**
 * 多店铺管理
**/
$(function() {
	var storeManagerMent = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		var pathParams = window.path || {};
		
		// 全局配置
		var config = {
			ctxPath:pathParams.ctx,
			onStep: winParams.onStep || null,
			userType:winParams.userType || null,
			taobaoEncodeKey:winParams.taobaoEncodeKey || ''
		};
		
		// 表单
		var form = function() {
			
			// 元素集合
			var els = {
				bindBtn:$("#bindTaoBaoAccount"),
				cancelBut: $('#cancelBut')
			};
			
			els.bindBtn.click(function(){
				if(config.onStep != null && config.onStep >= 2){
		 			window.open('login_toTB.action','taobaoCheck','width=704,height=628,left=380,top=0,scrollbars=yes');
		 		}else{
		 			window.location.reload(true);
		 		}
			});
			
			$('.cancel').click(function(){
				var taobaoEncodeKey = $(this).parent().next("input[type='hidden']").val();
				var oDialog = new Dialog();
				oDialog.init({
					contentHtml: "确定要取消绑定吗？",
					yes: function() {
						oDialog.close();
						$.ajax({
							type:"POST",
							dataType:"json",
							cache:false,
							data:{customerID:taobaoEncodeKey},
							url:"cancelAssociationAccount.action",
							success:function(response){
								if(response.status){
									window.location.reload(true);;
								}
								else{
									var cDialog = new Dialog();
									cDialog.init({
										contentHtml: response.infoContent,
										yes: function() {
											cDialog.close();
										},
										closeBtn: true
									});
								}
							}
						});	
					},
					no: function() {
						oDialog.close();
					},
					closeBtn: true
				});
			});
			
//			els.cancelBut.click(function(){
//				var taobaoEncodeKey = $(this).parent().next("input[type='hidden']").val();
//				var oDialog = new Dialog();
//				oDialog.init({
//					contentHtml: "确定要取消绑定吗？",
//					yes: function() {
//						oDialog.close();
//						$.ajax({
//							type:"POST",
//							dataType:"json",
//							cache:false,
//							data:{customerID:taobaoEncodeKey},
//							url:"cancelAssociationAccount.action",
//							success:function(response){
//								if(response.status){
//									window.location.reload(true);;
//								}
//								else{
//									var cDialog = new Dialog();
//									cDialog.init({
//										contentHtml: response.infoContent,
//										yes: function() {
//											cDialog.close();
//										},
//										closeBtn: true
//									});
//								}
//							}
//						});	
//					},
//					no: function() {
//						oDialog.close();
//					},
//					closeBtn: true
//				});
//			});
			
		};
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	storeManagerMent.init();
});