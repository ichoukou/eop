/**
 * 多店铺管理
**/
$(function() {
	var personalizedSettings = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {},
			winPath = window.path.ctx || '';

		// 配置
		var config = {
			onStep:winParams.onStep || -1,
			userType: winParams.userType || null,				// 用户激活状态
			userState: winParams.userState || null,				// 退出的请求地址
			infoState: winParams.infoState || null,			// 激活 js 请求地址
			userField003: winParams.userField003 || null			// 激活 js 请求地址
		};
		
		// 表单
		var form = function() {
			
			// 元素集合
			var els = {
				saveBut: $('#save')
				,backBut:$("#back")
			};
			
			els.backBut.click(function(){
				history.back();
			});
			
			els.saveBut.click(function(){
				var ids="";
				$("input[name='relatQuery']").each(function(){
					if($(this).attr("checked"))
						ids+=($(this).val()+":1,");
					else
						ids+=($(this).val()+":0,");
				});
				if(ids.indexOf(':1')==-1){
		        	var aDialog = new Dialog();
		        	aDialog.init({
		        		contentHtml: "至少选择一个店铺",
		        		yes: function() {
		        			aDialog.close();
		        		},
		        		closeBtn: true
		        	});
		        	return false;
		        }
				
				var oDialog = new Dialog();
				oDialog.init({
					contentHtml: "确定保存吗?",
					yes: function() {
						oDialog.close();
		    	 		//卖家用户完善信息或已激活后不进行拦截
		    	 		if(config.onStep > 0){
		    	 			$.ajax({
		    	    			async:false,
		    	    			url : 'associationAccount_editUserCustom.action',
		    	    			type :'post',
		    	    			data:{
		    	    				'ids':ids
		    	    			},
		    	    			success:function(data){
		    	    				window.location.reload(true);
		    	    			}
		    	    		});
		    	 		}else{
		    	 			// console.log("用户未填写基本信息。");
		    	 			window.location.reload(true);
		    	 		}
					},
					no: function() {
						oDialog.close();
					},
					closeBtn: true
				});
			});
			
		};
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	personalizedSettings.init();
});