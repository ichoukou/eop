/**
 * 子账号管理
 **/
$(function() {
	var myAccount = (function() {
		var winParams = window.params || {};
		var config = {
			onStep:winParams.onStep,
			showBindCode:winParams.showBindCode || false,
			userType:winParams.userType || '',				//用户类型
			banRequest: winParams.banRequest || ''			// “禁用”异步请求
		};
		
		var els = {
			addSubAcc : $("#addSubAcc"),
			searForm: $('#pageForm'),
			currentPage: $('#currentPage')
		};
		
		var form = function(){
			els.addSubAcc.click(function(){
				if(config.onStep < 3 && config.showBindCode){
					window.location.reload(true);
				}
				else{
					window.location.href="user!toAddSubAccount.action?menuFlag=user_sub_acc_list";
				}
			});
			
			//”禁用“，”启用 “两个连接已经被去掉，这个方法已经不起效果了
			$('.disable').live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
				txt = _this.html();
				var userId = _this.parent().find("input").val();
				
				if (txt == '禁用') {		// 如果点击的按钮是“禁用”
				
					// 弹窗
					var offDialog = new Dialog();
					offDialog.init({
						yes: function() {			// 发送异步请求
							$.ajax({
								url: config.banRequest,
								cache: false,
								dataType: "json",
								data:{"user.id":userId,"type":"0"},
								success: function(data) {	// 请求成功的回调
									offDialog.close();
									// 再次弹窗
//									var againDialog = new Dialog();
//									againDialog.init({
//										yes: function() {			// 确认按钮的回调
//											againDialog.close();
//										},
//										maskOpacity: 0,					// 遮罩层的透明度
//										contentHtml: data.infoContent			// 内容 HTML
//									});
									
									if(data.status)
										_this.html('启用');
								}
							})
						},
						no: function() {					// 取消按钮的回调
							offDialog.close();
						},
						maskOpacity: 0,								// 遮罩层的透明度
						contentHtml: '是否确定禁用帐户？'			// 内容 HTML
					});
					
				} else if (txt == '启用') {		// 如果点击的按钮是“启用”
					$.ajax({
						url: config.banRequest,
						cache: false,
						dataType: "json",
						data:{"user.id":userId,"type":"1"},
						success: function(data) {	// 请求成功的回调
							if(data.status){
								_this.html('禁用');
//								var onDialog = new Dialog();
//								onDialog.init({
//									yes: function() {			// 发送异步请求
//										onDialog.close();
//									},
//									maskOpacity: 0,								// 遮罩层的透明度
//									contentHtml: '启用成功'			// 内容 HTML
//								});
							}
							else{
								var onDialog = new Dialog();
								onDialog.init({
									yes: function() {			// 发送异步请求
										onDialog.close();
									},
									maskOpacity: 0,								// 遮罩层的透明度
									contentHtml: data.infoContent			// 内容 HTML
								});
							}
						}
					});
					
				}
			});
			

			//删除链接的点击
			$('.delSubAcc').live('click',function(ev){
				ev.preventDefault();
				var _this = $(this),
				    userId = _this.parent().find("input").val();
				
				var onDialog = new Dialog();
				onDialog.init({
					yes: function() {			
						onDialog.close();
						$.ajax({
							url:'user!toDelSubAccount.action?menuFlag=${menuFlag }',
							cache: false,
							dataType: "json",
							data:{
								"user.id":userId
							},
							success: function(data) {
								window.location.href="user!toSubAccountList.action?menuFlag=user_sub_acc_list";
							}
						})
					},
					no: function() {
						onDialog.close();
	        		},
					maskOpacity: 0,								
					contentHtml: "确认删除子账号吗？删除后客户需重新分配！",
					yesVal: '确定',
	        		noVal:'取消'
				});
				
				
				
			});
			
			//显示所有已分配客户弹出层
			var showUTDiv = function (divid) {
				if($("#utDiv"+divid).text()!=""){
					document.getElementById("utDiv"+divid).style.visibility = "visible";
				}
			}

			//隐藏所有已分配客户弹出层
			var hideUTDiv = function (divid) {
				document.getElementById("utDiv"+divid).style.visibility = "hidden";
			}
			
			//绑定显示事件
			$(".td_e_div").live("mouseover",function(){
				showUTDiv($(this).attr("val"));
			});
			
			//绑定隐藏事件
			$(".td_e_div").live("mouseout",function(){
				hideUTDiv($(this).attr("val"));
			});
			
			//翻页
			pagination.live('click',function(){
				els.currentPage.val($(this).attr("value"));
				//为解决IE6问题而修改
				setTimeout(function(){els.searForm.trigger('submit');},0);
//				els.searForm.trigger('submit');
			});
			
		};
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	myAccount.init();
});

