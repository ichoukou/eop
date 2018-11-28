$(function(){
	$(function(){
		$("#save").click(function(){
			checkUserCode();
		});
		
		$("#user").change(function(){
			 search();
		});
		
		$("#edit").click(function(){
			$("#branckWarnValue").removeAttr("disabled");
			$("#customerWarnValue").removeAttr("disabled");
			$("#phone").removeAttr("disabled");
			$("#phoneWarn").removeAttr("disabled");
			$("#remarkPhone").removeAttr("disabled");
			$("#email").removeAttr("disabled");
			$("#emailWarn").removeAttr("disabled");
			$("#SMSAlerts").removeAttr("disabled");
			$("#EmaiAlerts").removeAttr("disabled");
		});
		
		$("#branckWarnValue").blur(function(){
			var $a=$("#branckWarnValue").attr("value");
			var h=/^\d{0,8}$/;//匹配数字
			if(!$a){
				$("#branckWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值不得为空!");
			}else if(h.test($a)){
				$("#branckWarnValueMsg").empty().removeAttr("style");
			}else{
				$("#branckWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("网点采购预警值必须是0-8位正整数组成!");
			}
			
		});
		
		$("#customerWarnValue").blur(function(){
			var $b=$("#customerWarnValue").attr("value");
			var h=/^\d{0,8}$/;//匹配数字
			if(!$b){
				$("#customerWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("商家号码池预警值不得为空!");
			}else if(h.test($b)){
				$("#customerWarnValueMsg").empty().removeAttr("style");
			}else{
				$("#customerWarnValueMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("网点采购预警值必须是0-8位正整数组成!");
			}
		});

		$("#phone").blur(function(){
			var $c=$("#phone").attr("value");
			var g=/^1[3|4|5|8|9][0-9]\d{4,8}$/;//手机号
			if(!$c){
				$("#phoneMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("手机号码不得为空!");
			}else if(g.test($c)){
				$("#phoneMsg").empty().removeAttr("style");
			}else{
				$("#phoneMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("手机号码格式不正确(支持7-11位手机号)!");
			}
		});

		$("#remarkPhone").blur(function(){
			var $e=$("#remarkPhone").attr("value");
			var g=/^1[3|4|5|8|9][0-9]\d{4,8}$/;//手机号
			if(!$e){
				f4=true;
				$("#remarkPhoneMsg").empty().removeAttr("style");
			}else if(g.test($e)){
				f4=true;
				$("#remarkPhoneMsg").empty().removeAttr("style");
			}else{
				$("#remarkPhoneMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("备用手机号码格式不正确(支持7-11位手机号)!");
				f4=false;
			}
		});

		$("#email").blur(function(){
			var $f=$("#email").attr("value");
			var i=/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;//email地址
			if(!$f){
				$("#emailMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("邮箱不得为空!");
				f5=false;
			}else if(i.test($f)){
				f5=true;
				$("#emailMsg").empty().removeAttr("style");
			}else{
				$("#emailMsg").css(
						{"height":"23px",
							"border":"1px solid #E05C5C",
							"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
							"color":"#DF5151",
							"display":"inline-block",
							"padding":"0 10px 0 30px",
							"vertical-align":"top"}
							).html("邮箱格式不正确!");
				f5=false;
			}
		});
		
		
		
	});

	function checkUserCode(){
		var userCode=$("#user").val();
		var uc=true;
		if(!userCode){
			uc=false;
	         var loadingDialog = new Dialog();
	         loadingDialog.init({
	         closeBtn : true,
	         contentHtml: '商家代码不存在,请重新选择!'
	        });
		}else{
			checkForm();
		}
	}


	function search(){
		var userCode=$("#user").val();
		if(!userCode){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '商家代码不存在，请重新选择!'
			});		
		}else{
			var loadingDialog = new Dialog();
			loadingDialog.init({
				contentHtml : '加载中，请稍后......'
			});
			document.form.action = "search.action?userCode="+userCode+"&currentPage=1&menuFlag=dzmd_dzmd"; 
			$("#q_form").submit();
		}
		
		
	}


	function checkForm(){
		var $a=$("#branckWarnValue").attr("value");
		var $b=$("#customerWarnValue").attr("value");
		var $c=$("#phone").attr("value");
		var $d=$("#phoneWarn").is(':checked');	
		var $e=$("#remarkPhone").attr("value");
		var $f=$("#email").attr("value");
		var $g=$("#emailWarn").is(':checked');

		var h=/^\d{0,8}$/;//匹配数字
		var i=/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/;//email地址
		var g=/^1[3|4|5|8|9][0-9]\d{4,8}$/;//手机号
		var f1=true;
		var f2=true;
		var f3=true;
		var f4=true;
		var f5=true;
		var f6=true;
		
		if(!$a){
			$("#branckWarnValueMsg").css(
					{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值不得为空!");
			f1=false;
		}else if(h.test($a)){
			f1=true;
			$("#branckWarnValueMsg").empty().removeAttr("style");
		}else{
			$("#branckWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值必须是0-8位正整数组成!");
			f1=false;
		}
		
		if(!$b){
			$("#customerWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("商家号码池预警值不得为空!");
			f2=false;
		}else if(h.test($b)){
			f2=true;
			$("#customerWarnValueMsg").empty().removeAttr("style");
		}else{
			$("#customerWarnValueMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("网点采购预警值必须是0-8位正整数组成!");
			f2=false;
		}
		
		if(!$c){
			$("#phoneMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("手机号码不得为空!");
			f3=false;
		}else if(g.test($c)){
			f3=true;
			$("#phoneMsg").empty().removeAttr("style");
		}else{
			$("#phoneMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("手机号码格式不正确(支持7-11位手机号)!");
			f3=false;
		}
		if(!$e){
			f4=true;
			$("#remarkPhoneMsg").empty().removeAttr("style");
		}else if(g.test($e)){
			f4=true;
			$("#remarkPhoneMsg").empty().removeAttr("style");
		}else{
			$("#remarkPhoneMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("备用手机号码格式不正确(支持7-11位手机号)!");
			f4=false;
		}
		
		
		if(!$f){
			$("#emailMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("邮箱不得为空!");
			f5=false;
		}else if(i.test($f)){
			f5=true;
			$("#emailMsg").empty().removeAttr("style");
		}else{
			$("#emailMsg").css(
						{"height":"23px",
						"border":"1px solid #E05C5C",
						"background":"url(images/module/form_error.png) no-repeat 6px 3px #FFF2F2",
						"color":"#DF5151",
						"display":"inline-block",
						"padding":"0 10px 0 30px",
						"vertical-align":"top"}
						).html("邮箱格式不正确!");
			f5=false;
		}
		
		if(f1&&f2&&f3&&f4&&f5){
			infoSubmit();
		}

	}
	function infoSubmit(){
			var uu=$("#userName1").val();
			var dd=$("#id1").val();
			var userCode=$("#user").val();
			var $action = "addForewarn.action?userCode="+userCode+"&userId="+dd+"&userName="+uu
																			+"&currentPage=1&menuFlag=dzmd_dzmd";
			$("#q1_form").attr("action",$action);
			
	         var loadingDialog = new Dialog();
	             loadingDialog.init({
	            yes: function() {
	                loadingDialog.close();
	                	
	                $("#q1_form").submit();
	             },
	            no:function () {
	            loadingDialog.close();
	            //$("#q1_form").submit();
	            },
	            contentHtml: '您确认要提交预警信息吗!'
	        });
//	     setTimeout(function () {
//	         loadingDialog.close();
//	         $("#q1_form").submit();
//	     }, 2000);
	}
	
})