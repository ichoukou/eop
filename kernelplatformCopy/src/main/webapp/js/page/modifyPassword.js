$(document).ready(function() {
	$("#submit_btn_resPassword").click(function() {
		var loginName=$("#login_id").val();
		$.ajax({
			type:"POST",
			url : "modify_checkFindPass.action",
			data: "loginName="+loginName,
			dataType : "json",
			success : function(response) {
				var value=response.ajaxAlertText;
				if(response.status){
					$("body span[class='yto_onError']").remove();
					getPswd();
				}else{
					$("body span[class='yto_onError']").remove();
					$("#submit_btn_resPassword").after('<span class="yto_onError">' + value + '</span>');
				}
			}
		});
	});
	// 回车提交表单
	
	$('body').live("keypress",function(ev) {
		
		if (ev.keyCode == 13) {
			setTimeout(function(){$("#submit_btn_resPassword").click();},0);
		}
	});
	//重置密码
	var getPswd = function(){
		// 正在加载
		var loadDialog = new Dialog();
		loadDialog.init({
			contentHtml: '请稍后...'
		});
		$.ajax({
			type:"POST",
			url:"modify_modifyPassword.action",
			dataType:"json",
			data:{loginName:$("#login_id").val()},
			success:function(response){
				loadDialog.close();
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: response.ajaxAlertText,
					yes: function() {
						aDialog.close();
					}
				});
			}
		});
	};
});