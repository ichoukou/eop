/**
 * 网点查找
**/

$(function() {
	
	//高亮显示js 
	$.fn.textSearch = function(str,options){
		var defaults = {
			divFlag: true,
			divStr: "",
			markClass: "",
			markColor: "red",
			nullReport: false,
			callback: function(){
				return false;	
			}
		};
		var sets = $.extend({}, defaults, options || {}), clStr;
		if(sets.markClass){
			clStr = "class='"+sets.markClass+"'";	
		}else{
			clStr = "style='color:"+sets.markColor+";'";
		}
		
		//对前一次高亮处理的文字还原
		$("span[rel='mark']").removeAttr("class").removeAttr("style").removeAttr("rel");
		
		
		//字符串正则表达式关键字转化
		$.regTrim = function(s){
			var imp = /[\^\.\\\|\(\)\*\+\-\$\[\]\?]/g;
			var imp_c = {};
			imp_c["^"] = "\\^";
			imp_c["."] = "\\.";
			imp_c["\\"] = "\\\\";
			imp_c["|"] = "\\|";
			imp_c["("] = "\\(";
			imp_c[")"] = "\\)";
			imp_c["*"] = "\\*";
			imp_c["+"] = "\\+";
			imp_c["-"] = "\\-";
			imp_c["$"] = "\$";
			imp_c["["] = "\\[";
			imp_c["]"] = "\\]";
			imp_c["?"] = "\\?";
			s = s.replace(imp,function(o){
				return imp_c[o];					   
			});	
			return s;
		};
		$(this).each(function(){
			var t = $(this);
			str = $.trim(str);
			if(str === ""){
				//alert("关键字为空");	
				return false;
			}else{
				//将关键字push到数组之中
				var arr = [];
				if(sets.divFlag){
					arr = str.split(sets.divStr);	
				}else{
					arr.push(str);	
				}
			}
			var v_html = t.html();
			//删除注释
			v_html = v_html.replace(/<!--(?:.*)\-->/g,"");
			
			//将HTML代码支离为HTML片段和文字片段，其中文字片段用于正则替换处理，而HTML片段置之不理
			var tags = /[^<>]+|<(\/?)([A-Za-z]+)([^<>]*)>/g;
			var a = v_html.match(tags), test = 0;
			if(a){
				$.each(a, function(i, c){
					if(!/<(?:.|\s)*?>/.test(c)){//非标签
						//开始执行替换
						$.each(arr,function(index, con){
							if(con === ""){return;}
							var reg = new RegExp($.regTrim(con), "g");
							if(reg.test(c)){
								//正则替换
								c = c.replace(reg,"♂"+con+"♀");
								test = 1;
							}
						});
						c = c.replace(/♂/g,"<span rel='mark' "+clStr+">").replace(/♀/g,"</span>");
						a[i] = c;
					}
				});
				//将支离数组重新组成字符串
				var new_html = a.join("");
				
				$(this).html(new_html);
				
				if(test === 0 && sets.nullReport){
					alert("没有搜索结果");	
					return false;
				}
			}
			//执行回调函数
			sets.callback();
		});
	};
	
	var outlets = (function() {
		
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		
		// 全局配置
		var config = {
			onStep: winParams.onStep || null
		};
		
		/*//默认隐藏"取消占有"按钮
		$('#dialog_demo_d').style.visibility = "hidden";*/
		
		// 元素集合
		var els = {
			searForm: $('#sear_form'),
			input_test: $('#input_text_demo'),
			searBtn: $('#sear_btn'),
			currentPage: $('#currentPage'),
			backBut:$("#back_btn")
			//companyName:$('#c_name'),
			//mContact:$('mcontact'),
			//searchTel:$('s_tel'),
			//quesTel:$('ques_tel'),
			//sendRange:$('send_r'),
			//nosendRange:$('nosend_r'),
			//sendTime:$('sendtime')
		};
		
		// 表单
		var form = function() {
			var ajaxValidator = {
					company: {valid: false, complete: false},
					contact: {valid: false, complete: false},
					tel:{valid: false, complete: false},
					qtel:{valid: false, complete: false}
				};
			
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
			
			//点击“返回”按钮
			els.backBut.click(function(){
				history.back();
			});
			
			// 提示文案
			var tipsMsg = {
				input_testErr: '关键字不能大于70个字符',
				input_testEmptyErr: '关键字不能为空',
				companyNameErr:'公司名称不能为空',
				mContactErr:'经理联系方式不能为空',
				searchTelErr:'查询电话不能为空',
				quesTelErr:'问题件电话不能为空',
				sendRangeErr:'派送范围不能为空',
				nosendRangeErr:'不派送范围不能为空',
				sendTimeErr:'派送时间不能为空'
			};
			
			// 点击“纠错”
			$('.table tbody .td_h .list_tr').live("click",function(ev){
				ev.preventDefault();
				
				for(var i=0;i<7;i++){
					//获取指定td中元素的值,并去除HTML标签
					var conValue = $(this).parent().parent().parent().next().find("#textarea_demo"+(i+1)).val().replace(/<[^>].*?>/g,"");
//					console.log(conValue);
					$(this).parent().parent().parent().next().find("#textarea_demo"+(i+1)).val(conValue);
				}
				var _this = $(this),
					nextTr = _this.parent().parent().parent().next(),
					nextTrDisplay = nextTr.css('display');
					
				if (nextTrDisplay == 'none') {
					nextTr.show();
				} 
				/*else {
					nextTr.hide();
				}*/
				
			});
			
			// 基本信息表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 输入关键字
			els.input_test.
			   formValidator({validatorGroup:'1', onShow: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
			   inputValidator({min:1, max:70, onErrorMin: tipsMsg.input_testEmptyErr, onErrorMax: tipsMsg.input_testErr}).
			   functionValidator({
			       fun: function(val) {
			           if (val == '请输入关键字') {
			               return false;
			           }
			       },
			       onError: '请输入关键字'
			   });

			
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				els.currentPage.val(1);
				if(config.onStep && config.onStep < 2){
					window.location.reload(true);
					return;
				}
				if($('#userType').val() == 1 && $('#userState').val() == 'TBA' && $('#userField003').val() != 9){
					window.location.reload();
				}else{
					var searchKey = els.input_test.val();
					if(searchKey != null && $.trim(searchKey) != "" && $.trim(searchKey) != "请输入关键字"){
						setTimeout(function() {
							els.searForm.trigger('submit');
						}, 5);
						
					}else{
						els.input_test.val('请输入关键字');
						//art.dialog.alert("请输入关键字查找");
						els.input_test.focus();
					}
					
				}
				
			});
			
			//新增弹出框
			$('#dialog_demo_c').click(function() {

				var dialogD = new Dialog();
				var tab = '<div id="dialog_pop"> <meta charset="UTF-8" />' +
				          '<form id="sear_form2" class="form" action="branchsolr!addBranch.action" method="post">' +
				          '<p><label for="c_name"><span class="req">*</span>公司名称：</label><input type="text" id="c_name" name="branch.companyName" class="input_text" /><span id="c_nameTip"></span></p>'+
						  '<p><label for="mcontact"><span class="req">*</span>经理联系方式：</label><input type="text" id="mcontact" name="branch.managerPhone" class="input_text" /><span id="mcontactTip"></span></p>'+
						  '<p><label for="s_tel"><span class="req">*</span>查询电话：</label><input type="text" id="s_tel" name="branch.servicePhone" class="input_text" /><span id="s_telTip"></span></p>'+
						  '<p><label for="ques_tel"><span class="req">*</span>问题件电话：</label><input type="text" id="ques_tel" name="branch.questionPhone" class="input_text" /><span id="ques_telTip"></span></p>'+
						  '<p><label for="dialog_textarea1"><span class="req">*</span>派送范围：</label><textarea name="branch.sendScope" id="dialog_textarea1" class="textarea_text" cols="100" rows="5"></textarea><span id="dialog_textarea1Tip"></span></p>'+
						  '<p><label for="dialog_textarea2"><span class="req">*</span>不派送范围：</label><textarea name="branch.unSendScope" id="dialog_textarea2" class="textarea_text" cols="100" rows="5"></textarea><span id="dialog_textarea2Tip"></span></p>'+
						  '<p><label for="dialog_textarea3"><span class="req">*</span>派送时限：</label><textarea name="branch.sendTimeLimit" id="dialog_textarea3" class="textarea_text" cols="100" rows="5"></textarea><span id="dialog_textarea3Tip"></span></p>'+
						  '<div class="dialog_btn" style="padding-top:10px;"><a href="#" class="btn btn_d" title="保存" id="dialog_save"><span>保存</span></a></div>'+
						  '</form>' +
						  '	</div>' ;
				
				dialogD.init({
					closeBtn: true,
					//maskOpacity: 0.2,			// 遮罩层的透明度
					contentHtml: tab			// 内容 HTML
				});
	
				//弹出框信息表单
				$.formValidator.initConfig({
					validatorGroup: '2',
					formID: 'sear_form2',
					theme: 'yto',
					errorFocus: false
				});
	
				// 公司名称
				$('#c_name').
					formValidator({validatorGroup:'2', onShow: '请填写公司名称', onFocus: '公司名称不能为空,请正确填写', onCorrect: ''}).
					inputValidator({min: 1, onErrorMin: tipsMsg.companyNameErr}).
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								companyName = $.trim(textBox.val()),
								tip = $('#c_nameTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["company"]["complete"] = false;
							ajaxValidator["company"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkCompanyName.action?branch.companyName=" + companyName,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该公司名称可以添加');
									ajaxValidator["company"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["company"]["complete"] = true;
							  }
							});
						}
					});
					
				$('#mcontact').
					formValidator({validatorGroup:'2', onShow: '请填写经理联系方式', onFocus: '', onCorrect: ''}).
					inputValidator({min: 1, onErrorMin: tipsMsg.mContactErr}).
					regexValidator({regExp:'mobile',dataType:"enum",onError:"你输入的手机格式不正确,请确认"}).
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								managerPhone = $.trim(textBox.val()),
								tip = $('#mcontactTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["contact"]["complete"] = false;
							ajaxValidator["contact"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkManagerPhone.action?branch.managerPhone=" + managerPhone,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该联系方式可以添加');
									ajaxValidator["contact"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["contact"]["complete"] = true;
							  }
							});
						}
					});
	
				$('#s_tel').
					formValidator({validatorGroup:'2', onShow: '请填写查询电话', onFocus: '', onCorrect: ''}).
					inputValidator({min: 1, onErrorMin: tipsMsg.searchTelErr}).
					regexValidator({regExp:"tel",dataType:"enum",onError:"你输入的查询电话格式不正确,请确认"}).
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								servicePhone = $.trim(textBox.val()),
								tip = $('#s_telTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["tel"]["complete"] = false;
							ajaxValidator["tel"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkServicePhone.action?branch.servicePhone=" + servicePhone,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该查询电话可以添加');
									ajaxValidator["tel"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["tel"]["complete"] = true;
							  }
							});
						}
					});
	
				$('#ques_tel').
					formValidator({validatorGroup:'2', onShow: '请填写问题件电话', onFocus: '', onCorrect: ''}).
					inputValidator({min: 1, onErrorMin: tipsMsg.quesTelErr}).
					regexValidator({regExp:"tel",dataType:"enum",onError:"你输入的问题件电话格式不正确,请确认"}).
					//.regexValidator({regexp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onerror:"你输入的问题件电话格式不正确,请确认"})
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								questionPhone = $.trim(textBox.val()),
								tip = $('#ques_telTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["qtel"]["complete"] = false;
							ajaxValidator["qtel"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkQuestionPhone.action?branch.questionPhone=" + questionPhone,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该问题件电话可以添加');
									ajaxValidator["qtel"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["qtel"]["complete"] = true;
							  }
							});
						}
					});
	
				$('#dialog_textarea1').
					formValidator({validatorGroup:'2', onShow: '请填写派送范围', onFocus: '', onCorrect: '该派送范围可以添加 '}).
					inputValidator({min: 1, onErrorMin: tipsMsg.sendRangeErr});
					
				$('#dialog_textarea2').
					formValidator({validatorGroup:'2', onShow: '请填写不派送范围', onFocus: '', onCorrect: '该不派送范围可以添加 '}).
					inputValidator({min: 1, onErrorMin: tipsMsg.nosendRangeErr});
					
				$('#dialog_textarea3').
					formValidator({validatorGroup:'2', onShow: '请填写派送时限', onFocus: '', onCorrect: '该派送时限可以添加 '}).
					inputValidator({min: 1, onErrorMin: tipsMsg.sendTimeErr});
				
				//点击弹出框“保存”
				$('#dialog_save').live('click',function(ev) {
					ev.preventDefault();
					
					if(timer){
						clearInterval(timer);
					}

					var c_name = $('#c_name').val();
					var mcontact = $('#mcontact').val();
					var s_tel = $('#s_tel').val();
					var ques_tel = $('#ques_tel').val();
					var dialog_textarea1 = $('#dialog_textarea1').val();
					var dialog_textarea2 = $('#dialog_textarea2').val();
					var dialog_textarea3 = $('#dialog_textarea3').val();					
					
					// 验证结果
					var flag = $.formValidator.pageIsValid('2');

					var timer = setInterval(function(){
						var complete = true,
							valid = true;

						for(prop in ajaxValidator){
							if(!ajaxValidator[prop]["complete"]){
								complete = false;
							}

							if(!ajaxValidator[prop]["valid"]){
								valid = false;
							}
						}
						
						if(!complete){
							return;
						}
						
						clearInterval(timer);
						if(flag && valid){
							
							dialogD.close();
							//var form1 = $('#sear_form2');
							//form1.appendTo($('body'));
							//form1.hide();
							
							var dialog2 = new Dialog();
							dialog2.init({
								contentHtml: '<p>请确认该信息是您的网点信息<br>保存后：<br></p>' +
											'<p>1、他人将无法修改这条信息</p>' +
											'<p>2、您无法修改其它网点信息</p>',
								yes: function() {
									dialog2.close();
										$.ajax({
										url:'branchsolr2!checkBranchCode.action',
										success:function(data){
											var dialog3 = new Dialog();
											if (data=="false") {
												$.ajax({
													type:'post',
													data:{
														'branch.companyName' :c_name,
														'branch.managerPhone' :mcontact,
														'branch.servicePhone' :s_tel,
														'branch.questionPhone' :ques_tel,
														'branch.sendScope' : dialog_textarea1,
														'branch.unSendScope' :dialog_textarea2,
														'branch.sendTimeLimit' :dialog_textarea3
													},
													url:'branchsolr!addBranch.action',
													success:function(data){
														if(data){
															dialog3.init({
																contentHtml:'<p>新增网点成功!</p>',
																yes:function(){
																	dialog3.close();
																	var searchValue = $("#input_text_demo").val();
																	searchValue = c_name;
																	$("#input_text_demo").val(searchValue);
																	if(searchValue!='请输入关键字'&&searchValue!='') {	
																		setTimeout(function() {
																			$('#sear_form').trigger('submit');
																		}, 1000);
																	}else {	
																		setTimeout(function() {
																			window.location = "branchlist.action?menuFlag=home_branchlist";
																		}, 5);
																	}
																}
															});
														}else{
															dialog3.init({
																contentHtml:'<p>新增网点异常,请稍后再试!</p>',
																yes:function(){
																	dialog3.close();
																}
															});
														}
													}
												});
											} else {
												dialog3.init({
													contentHtml: '<p>对不起，您已存在网点!<br>不能继续添加!</p>',
													yes: function(){
														dialog3.close();
													}
												});
											}
										}
									}); 
								},
								no: function() {
									dialog2.close();
								},
								closeBtn: true
							});
						}
					}, 500);
				});
			
			});
			
			
			//查看网点信息
			$('#dialog_demo_my').click(function() {
				var dialogD = new Dialog();
				
				var myBranchId = $("#myBranchId").val();
				var myCompanyName = $("#myCompanyName").val();
				var myManagerPhone = $("#myManagerPhone").val();
				var myServicePhone = $("#myServicePhone").val();
				var myQuestionPhone = $("#myQuestionPhone").val();
				var mySendScope = $("#mySendScope").val();
				var myUnSendScope = $("#myUnSendScope").val();
				var mySendTimeLimit = $("#mySendTimeLimit").val();
					
				var tab = '<div id="dialog_pop"> <meta charset="UTF-8" />' +
				          '<form id="sear_form3" class="form" action="branchsolr!editBranch.action" method="post"><input value="'+myBranchId+'" name="branch.id" id="c_id" type="hidden" />' +
				          '<p><label for="c_name"><span class="req">*</span>公司名称：</label><input type="text" id="c_name" name="branch.companyName" value="'+myCompanyName+'" class="input_text" /><span id="c_nameTip"></span></p>'+
						  '<p><label for="mcontact"><span class="req">*</span>经理联系方式：</label><input type="text" id="mcontact" name="branch.managerPhone" value="'+myManagerPhone+'" class="input_text" /><span id="mcontactTip"></span></p>'+
						  '<p><label for="s_tel"><span class="req">*</span>查询电话：</label><input type="text" id="s_tel" name="branch.servicePhone" value="'+myServicePhone+'" class="input_text" /><span id="s_telTip"></span></p>'+
						  '<p><label for="ques_tel"><span class="req">*</span>问题件电话：</label><input type="text" id="ques_tel" name="branch.questionPhone" value="'+myQuestionPhone+'" class="input_text" /><span id="ques_telTip"></span></p>'+
						  '<p><label for="dialog_textarea1"><span class="req">*</span>派送范围：</label><textarea name="branch.sendScope" id="dialog_textarea1" class="textarea_text" cols="100" rows="5">'+mySendScope+'</textarea><span id="dialog_textarea1Tip"></span></p>'+
						  '<p><label for="dialog_textarea2"><span class="req">*</span>不派送范围：</label><textarea name="branch.unSendScope" id="dialog_textarea2" class="textarea_text" cols="100" rows="5">'+myUnSendScope+'</textarea><span id="dialog_textarea2Tip"></span></p>'+
						  '<p><label for="dialog_textarea3"><span class="req">*</span>派送时限：</label><textarea name="branch.sendTimeLimit" id="dialog_textarea3" class="textarea_text" cols="100" rows="5">'+mySendTimeLimit+'</textarea><span id="dialog_textarea3Tip"></span></p>'+
						  '<div class="dialog_btn" style="padding-top:10px;"><a href="#" class="btn btn_d" style="margin-right:10px;" title="保存" id="dialog_save"><span>保存</span></a><a href="javascript:;" class="btn btn_e" title="取消" id="dialog_c"><span>取消</span></a></div>'+
						  '<p style="text-align:right;">如果不是您的网点信息，请点击<a href="#" id="dialog_demo_c">取消占有</a></p>'+
						  '</form>' +
						  '	</div>' ;
				
				dialogD.init({
					closeBtn: true,
					//maskOpacity: 0.2,			// 遮罩层的透明度
					contentHtml: tab			// 内容 HTML
				});
	
				//弹出框信息表单
				$.formValidator.initConfig({
					validatorGroup: '3',
					formID: 'sear_form3',
					theme: 'yto',
					errorFocus: false
				});

				// 公司名称
				$('#c_name').
					formValidator({validatorGroup:'3', onShow: '请填写公司名称', onFocus: '公司名称不能为空,请正确填写', onCorrect: ''}).
					inputValidator({min: 1, onErrorMin: tipsMsg.companyNameErr}).
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								companyName = $.trim(textBox.val()),
								tip = $('#c_nameTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["company"]["complete"] = false;
							ajaxValidator["company"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkCompanyName.action?branch.myCompanyName=" + myCompanyName + "&branch.companyName=" + companyName,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该公司名称可以添加');
									ajaxValidator["company"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["company"]["complete"] = true;
							  }
							});
						}
					});
					
				$('#mcontact').
					formValidator({validatorGroup:'3', onShow: '请填写经理联系方式', onFocus: '', onCorrect: ''}).
					inputValidator({min: 1, onErrorMin: tipsMsg.mContactErr}).
					regexValidator({regExp:"mobile",dataType:"enum",onError:"你输入的手机格式不正确,请确认"}).
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								managerPhone = $.trim(textBox.val()),
								tip = $('#mcontactTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["contact"]["complete"] = false;
							ajaxValidator["contact"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkManagerPhone.action?branch.myManagerPhone=" + myManagerPhone + "&branch.managerPhone=" + managerPhone,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该联系方式可以添加');
									ajaxValidator["contact"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["contact"]["complete"] = true;
							  }
							});
						}
					});
	
				$('#s_tel').
					formValidator({validatorGroup:'3', onShow: '请填写查询电话', onFocus: '', onCorrect: '该查询电话可以添加 '}).
					inputValidator({min: 1, onErrorMin: tipsMsg.searchTelErr}).
					regexValidator({regExp:"tel",dataType:"enum",onError:"你输入的查询电话格式不正确,请确认"}).
					//.regexValidator({regexp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onerror:"你输入的查询电话格式不正确,请确认"})
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								servicePhone = $.trim(textBox.val()),
								tip = $('#s_telTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["tel"]["complete"] = false;
							ajaxValidator["tel"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkServicePhone.action?branch.myServicePhone=" + myServicePhone + "&branch.servicePhone=" + servicePhone,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该联系方式可以添加');
									ajaxValidator["tel"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["tel"]["complete"] = true;
							  }
							});
						}
					});
	
				$('#ques_tel').
					formValidator({validatorGroup:'3', onShow: '请填写问题件电话', onFocus: '', onCorrect: ''}).
					inputValidator({min: 1, onErrorMin: tipsMsg.quesTelErr}).
					regexValidator({regExp:"tel",dataType:"enum",onError:"你输入的问题件电话格式不正确,请确认"}).
					//.regexValidator({regexp:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",onerror:"你输入的问题件电话格式不正确,请确认"})
					functionValidator({
						fun: function(val, el) {
							var textBox = $(el),
								questionPhone = $.trim(textBox.val()),
								tip = $('#ques_telTip');
							textBox.attr("disabled","disabled");
							ajaxValidator["qtel"]["complete"] = false;
							ajaxValidator["qtel"]["valid"] = false;

							$.ajax({
							  url: "branchsolr2!checkQuestionPhone.action?branch.myQuestionPhone=" + myQuestionPhone + "&branch.questionPhone=" + questionPhone,
							  datatype : "json",
							  success: function(data) {
						         if(data === 'false'){
								    showIcon.error(tip, '服务器没有通过验证');
								 }
								 else if(data === 'true'){
									showIcon.correct(tip, '该问题件电话可以添加');
									ajaxValidator["qtel"]["valid"] = true;
								 }
							  },
							  cache: false,
							  complete: function(){
								 textBox.removeAttr("disabled");
								 tip.show();
								 ajaxValidator["qtel"]["complete"] = true;
							  }
							});
						}
					});
				
				$('#dialog_textarea1').
					formValidator({validatorGroup:'3', onShow: '请填写派送范围', onFocus: '', onCorrect: '该派送范围可以添加 '}).
					inputValidator({min: 1, onErrorMin: tipsMsg.sendRangeErr});
					
				$('#dialog_textarea2').
					formValidator({validatorGroup:'3', onShow: '请填写不派送范围', onFocus: '', onCorrect: '该不派送范围可以添加 '}).
					inputValidator({min: 1, onErrorMin: tipsMsg.nosendRangeErr});
					
				$('#dialog_textarea3').
					formValidator({validatorGroup:'3', onShow: '请填写派送时限', onFocus: '', onCorrect: '该派送时限可以添加 '}).
					inputValidator({min: 1, onErrorMin: tipsMsg.sendTimeErr});
				
				//取消占有
				$('#dialog_demo_c').live('click',function(ev) {
					dialogD.close();
					cancelEmploy(myBranchId);
				});
				
				//取消
				$('#dialog_c').live('click',function(ev) {
					dialogD.close();
				});
				
				//点击弹出框“保存”
				$('#dialog_save').live('click',function(ev) {
					ev.preventDefault();

					if(timer){
						clearInterval(timer);
					}
					
					var c_id = $('#c_id').val();
					var c_name = $('#c_name').val();
					var mcontact = $('#mcontact').val();
					var s_tel = $('#s_tel').val();
					var ques_tel = $('#ques_tel').val();
					var dialog_textarea1 = $('#dialog_textarea1').val();
					var dialog_textarea2 = $('#dialog_textarea2').val();
					var dialog_textarea3 = $('#dialog_textarea3').val();					
					
					// 验证结果
					var flag = $.formValidator.pageIsValid('3');

					var timer = setInterval(function(){
						var complete = true,
							valid = true;

						for(prop in ajaxValidator){
							if(!ajaxValidator[prop]["complete"]){
								complete = false;
							}

							if(!ajaxValidator[prop]["valid"]){
								valid = false;
							}
						}
						
						if(!complete){
							return;
						}
						
						clearInterval(timer);

						if(flag && valid){
							//var form1 = $('#sear_form3');
							dialogD.close();
							
							$.ajax({
								data:{
									'branch.id' : c_id,
									'branch.companyName' : c_name,
									'branch.managerPhone' :mcontact,
									'branch.servicePhone' :s_tel,
									'branch.questionPhone' :ques_tel,
									'branch.sendScope' :dialog_textarea1,
									'branch.unSendScope' :dialog_textarea2,
									'branch.sendTimeLimit' :dialog_textarea3
								},
								dataType:'json',
								url:'branchsolr!editBranch.action',
								success:function(data){
									var dialog3 = new Dialog();
									if(data){
										dialog3.init({
											contentHtml:'<p>修改网点成功!</p>',
											yes:function(){
												dialog3.close();		
												var searchValue = $("#input_text_demo").val();
												if(searchValue!='请输入关键字'&&searchValue!='') {	
													setTimeout(function() {
														$('#sear_form').trigger('submit');
													}, 1000);
												}else {	
													setTimeout(function() {
														window.location = "branchlist.action?menuFlag=home_branchlist";
													}, 5);
												}
											}
										});
									}else{
										dialog3.init({
											contentHtml:'<p>修改网点异常,请稍后再试!</p>',
											yes:function(){
												dialog3.close();
											}
										});
									}
								}
							});		
						}

					}, 500);
				});
			
			});

			// 关闭
			$('.list_tr2').live("click",function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					thisTr = _this.parent().parent().parent();
			
				thisTr.hide();
			});
			
		};
		
		//高亮显示自定义函数
		var initMark = function(){
			var searchKey = els.input_test.val();
			$(".table tbody").textSearch(searchKey);
		}
		
		//翻页
		pagination.click(function(ev){
			ev.preventDefault();
			els.currentPage.val($(this).attr("value"));
			
			if($('#userType').val() == 1 && $('#userState').val() == 'TBA' && $('#userField003').val() != 9){
				window.location.reload();
			}else{
				var searchKey = $("#input_text_demo").val();
				if(searchKey != null && $.trim(searchKey) != "" && $.trim(searchKey) != "请输入关键字")
					setTimeout(function(){
						els.searForm.trigger('submit');
					},0);
				else{
					$('#searchKey').val('请输入关键字');
					$("#searchKey").focus();
				}
			}
		});
		
		return {
			init: function() {
				ytoTab.init();
				form();
				initMark();
			}
		}
	})();
	
	outlets.init();
});


//通知管理员
function notificationManager(id,obj){
	var conditions=["companyName","managerName","servicePhone","questionPhone","sendScope","unSendScope","sendTimeLimit"];
	var conditionAjaxStr="";
	for(var i=0;i<conditions.length;i++){
		//获取指定td中元素的值,并拼凑成完整的条件
		var conValue = $(obj).parent().parent().parent().find("#textarea_demo"+(i+1)).val(); 
		var conName  = "branch."+conditions[i];
		if(conValue==null || ""==conValue || ""==$.trim(conValue)){
			conValue='';
		}
		if(i==0){
			conditionAjaxStr=conName+"="+$.trim(conValue);
		}else{
			conditionAjaxStr+="&"+conName+"="+$.trim(conValue);
		}
	}
	
	$.ajax({
		type : "post",
		dataType : "json",
		data : conditionAjaxStr+"&branch.id="+id,
		cache: false,
		url : 'branchcorrection.action',
		success:function(response){
			var alertDialog = new Dialog();
			if(response){
				alertDialog.init({
					contentHtml: '该纠错已通知管理员!',
					yes:function(){
						alertDialog.close();
					},
					closeBtn: true
				});
			}else{
				alertDialog.init({
					contentHtml: '该纠错通知管理员中出错,请检查您的纠错信息!',
					yes:function(){
						alertDialog.close();
					},
					closeBtn: true
				});
			}
		}
	});
}

//保存
function save(id,obj){
	var conditions=["companyName","managerName","servicePhone","questionPhone","sendScope","unSendScope","sendTimeLimit"];
	var conditionAjaxStr="";
	for(i=0;i<conditions.length;i++){
		//获取指定td中元素的值,并拼凑成完整的条件
		var conValue = $(obj).parent().parent().parent().find("#textarea_demo"+(i+1)).val(); 
		// console.log(conValue);
		var conName  = "branch."+conditions[i];
		if(conValue==null || ""==conValue || ""==$.trim(conValue)){
			conValue='';
		}
		if(i==0){
			conditionAjaxStr=conName+"="+$.trim(conValue);
		}else{
			conditionAjaxStr+="&"+conName+"="+$.trim(conValue);
		}
	}
	
	/*
	var dialog2 = new Dialog();
	
	dialog2.init({
		contentHtml: '<p>请确认该信息是您的网点信息<br>保存后：<br></p>' +
					'<p>1、他人将无法修改这条信息</p>' +
					'<p>2、您无法修改其它网点信息</p>',
		yes: function() {
			dialog2.close();*/
			$.ajax({
				url:'branchsolr2!checkBranchCode.action',
				success:function(data){
					var dialog3 = new Dialog();
					if (data=="false") {
				  		$.ajax({
							type : "post",
							dataType : "json",
							data : conditionAjaxStr+"&branch.id="+id,
							cache: false,
							url : 'branchcorrection.action',
							success:function(response){
								if(response){
									dialog3.init({
										contentHtml:'<p>该网点已纠错成功!（预计5分钟生效）</p>',
										yes:function(){
											setTimeout(function() {
												$('#sear_form').trigger('submit');
											}, 1000);
										}
									});
								}else{
									dialog3.init({
										contentHtml:'<p>该网点纠错中出错,请检查您的纠错信息!</p>',
										yes:function(){
											dialog3.close();
										}
									});
								}
							}
						});
					}else{
						dialog3.init({
							contentHtml: '<p>对不起，您已存在网点!<br>不能继续此操作!</p>',
							yes: function(){
								dialog3.close();
							}
						});
					}
				}
			});
			/*
		},
		no: function() {
			dialog2.close();
		},
		closeBtn: true
	});*/
}

//占位己有
function employ(id){
	var dialog2 = new Dialog();
	dialog2.init({
		contentHtml: '<p>占为己有后：<br></p>' +
					'<p>1、他人将无法修改这条信息</p>' +
					'<p>2、您无法修改其它网点信息</p>',
		yes: function() {
			dialog2.close();
			$.ajax({
				url:'branchsolr2!checkBranchCode.action',
				success:function(data){
					var dialog3 = new Dialog();
					if (data=="false") {
				  		$.ajax({
							type : "post",
							dataType : "json",
							data : "branch.id="+id,
							cache: false,
							url : 'branchemploy.action',
							success:function(response){
								if(response){
									dialog3.init({
										contentHtml:'<p>该网点已占为己有!</p>',
										yes:function(){							
											setTimeout(function() {
												$('#sear_form').trigger('submit');
											}, 5);
										}
									});
								}else{
									dialog3.init({
										contentHtml:'<p>该网点在占为己有中出错,请稍后再试!</p>',
										yes:function(){
											dialog3.close();
										}
									});
								}
							}
						});
					}else{
						dialog3.init({
							contentHtml: '<p>对不起，您已存在网点!<br>不能继续此操作!</p>',
							yes: function(){
								dialog3.close();
							}
						});
					}
				}
			});
		},
		no: function() {
			dialog2.close();
		},
		closeBtn: true
	});
}

//取消占有
function cancelEmploy(id){
	var dialog2 = new Dialog();
	dialog2.init({
		contentHtml: '<p>取消占有后：<br></p>' +
					'<p>1、您就不再拥有网点信息!</p>' +
					'<p>2、您确定取消吗?</p>',
		yes: function() {
			dialog2.close();
				$.ajax({
					type : "post",
					dataType : "json",
					data : "branch.id="+id,
					cache: false,
					url : 'branchcancelEmploy.action',
					success:function(response){
						var dialog3 = new Dialog();
						if(response){
							$("#_isHave").val("qx");
							$("#_haveId").val(id);
							dialog3.init({
								contentHtml:'<p>该网点已取消占有!</p>',
								yes:function(){
									var searchValue = $("#input_text_demo").val();
									if(searchValue!='请输入关键字'&&searchValue!='') {	
										setTimeout(function() {
											$('#sear_form').trigger('submit');
										}, 5);
									}else {	
										setTimeout(function() {
											window.location = "branchlist.action?menuFlag=home_branchlist";
										}, 5);
									}
								}
							});
						}else{
							dialog3.init({
								contentHtml:'<p>该网点在取消占有中出错,请稍后再试!</p>',
								yes:function(){
									dialog3.close();
								}
							});
						}
					}
				});
		},
		no: function() {
			dialog2.close();
		},
		closeBtn: true
	});
}


//function revised(obj){
//	console.log(obj+"      "+$(obj).parent());
//	var _this = $(obj),
//	nextTr = _this.parent().parent().parent().next(),
//	nextTrDisplay = nextTr.css('display');
//	
//	if (nextTrDisplay == 'none') {
//		nextTr.show();
//	}
//	
//}