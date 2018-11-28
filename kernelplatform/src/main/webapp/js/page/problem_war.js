$(function() {
	var winParams = window.params || {};
	
	var config = {
		dialogSubmit: winParams.dialogSubmit || '',
		tabIndex: winParams.tabIndex || 0,
		onStep : winParams.onStep
	};
	var problem_war = (function() {	
		var textDefault = function() {
			$('#input_text_text').defaultTxt('买家姓名/电话/运单号');
			$('#input_text_text2').defaultTxt('买家姓名/电话/运单号');
			
			if($("#returnQueryCondition").val()!="")
				$('#input_text_text').val($("#returnQueryCondition").val());
			if($("#returnQueryCondition2").val()!="")
				$('#input_text_text2').val($("#returnQueryCondition2").val());
		};
	
		var changeDownUp = function() {
			$('.th_title').has('.arrow_down').css('cursor', 'pointer').toggle(
				function() {
					$('em', $(this)).removeClass('arrow_down').addClass('arrow_up');
				},
				function() {
					$('em', $(this)).removeClass('arrow_up').addClass('arrow_down');
				}
			);
		};
		
		// 全选
		var check = function() {
			$('.table thead .input_checkbox').checkAll( $('.table tbody .input_checkbox') );
		};
		
		// 表单
		var form = function() {
			// 元素集合
			var els = {
				searForm: $('#sear_form'),
				searInput: $('#input_text_text'),
				searBtn: $('#sear_btn'),
				searForm2: $('#sear_form2'),
				searInput2: $('#input_text_text2'),
				searBtn2: $('#sear_btn2')
			};
			
			// 提示文案
			var tipsMsg = {
				searFormatErr: '格式错误，请修改',
				searLongErr: '内容超长',
				searShortErr: '内容太短'
			};
			
			
			// "查询"表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 智能查件
			els.searInput.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min: 4, max: 30, onErrorMin: tipsMsg.searShortErr, onErrorMax: tipsMsg.searLongErr});
			
			// "查询"表单
			$.formValidator.initConfig({
				validatorGroup: '2',
				formID: 'sear_form2',
				theme: 'yto',
				errorFocus: false    
			});
			
			// 智能查件
			els.searInput2.
				formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min: 4, max: 30, onErrorMin: tipsMsg.searShortErr, onErrorMax: tipsMsg.searLongErr});
				
			
			
				
			// 点击"筛选"
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				var validatorResult = $.formValidator.pageIsValid(1);
				if(validatorResult){
					var _this = $(this),condition = $(".input_text",_this.parents('form')).val();
					
					window.location.href="passManage_warnningIndex.action?menuFlag=chajian_passManage_warn&tips=0&queryCondition="+condition;
				}
				
			});
			
			// 点击"筛选"
			els.searBtn2.click(function(ev) {
				ev.preventDefault();
				
				var validatorResult1 = $.formValidator.pageIsValid(2);
				if(validatorResult1){
				var condition = $("#input_text_text2").val();
					window.location.href="passManage_list_seller.action?menuFlag=chajian_passManage_warn&tips=0&queryCondition="+condition;
				}
			});
		};
		
		// 通知用户
	    var note_user = function(){
			$('.default_text').each(function(){
				var _this = $(this),
					name = _this.siblings('input[name="buyername"]').val(),
					mobile = _this.siblings('input[name="warn_value"]').val();

				_this.defaultTxt('姓名: ' + name +'\n'+ '电话: ' + mobile);
				_this.data('default_value', '姓名: ' + name +'\n'+ '电话: ' + mobile);
				_this.data('changed', false);
				
				_this.change(function(){
					_this.data('changed', true);
				});
			});
		};
		
		// 发送消息
			var sendMsg = function() {
				$('.send_msg2').click(function(ev) {
					var sendDialog = new Dialog();
					ev.preventDefault();
					var _this = $(this),
						msg = $('.msgbg textarea', _this.parent().parent()).val(),	// 消息内容
						msgLen = msg.length,	// 消息内容字数
						tip = '',				// 消息提示文案
						perMsgLen = 200,			// 每条消息长度
						noCallback = null;		// 弹窗取消回调
					var curPage = $('#currentPage').val();
					
					/*var realLength = 0, len = msg.length, charCode = -1;
				    for (var i = 0; i < len; i++) {
				        charCode = msg.charCodeAt(i);
				        if (charCode >= 0 && charCode <= 128) realLength += 1;
				        else realLength += 2;
				    }	*/
					
					var realLength = msg.length;
					if (realLength <= perMsgLen && realLength >= 4) {
						
						$.ajax({
							url: 'passManage_sendToSite.action',
							type: 'POST',
							data: {
								operMsg: msg,
								issueId: $('.issueId',_this.parents('form')).val(),
								flag: '1'
							},
							success: function() {
								var succDialog = new Dialog();
								succDialog.init({
									contentHtml: '您的消息已成功发送给网点！',
									yes: function() {			// 确认按钮的回调
										succDialog.close();
										
										window.location.href="passManage_list_seller.action?menuFlag=chajian_passManage_warn&currentPage="+curPage;
									}
								})
							}
							
						})
					} else {
						
						if (msgLen == 0) {
							tip = '尚未输入内容';
						}else if((realLength > 0 && realLength < 4) || (realLength > perMsgLen)){
							tip = '对不起！消息内容长度必须为4-200个字符';
						} 
						/*else if (msgLen > perMsgLen) {
							var msgCount = Math.ceil(msgLen / perMsgLen);
							//tip = '该消息为' + msgLen + '字符，将分为' + msgCount + '条短信发送，你确定要发送吗？';
							noCallback = function() {
								sendDialog.close();
							}
						}*/
						
						sendDialog.init({
							yes: function() {			// 确认按钮的回调
								sendDialog.close();
							},
							no: noCallback,
							maskOpacity: 0,					// 遮罩层的透明度
							contentHtml: tip
						});
					}

				})
				//通知客户
				$('.send_msg1').click(function(ev) {
					ev.preventDefault();
					
					//查询返回参数
					var backInput = $("#input_text_text2").val();
					var currentPage = $("#currentPage").val();
					var parm = "&pos=2&currentPage2="+currentPage+"&backInput="+backInput;

					var sendDialog = new Dialog();
					var _this = $(this),
						msgText = _this.parent().parent().find('textarea'),
						tip = '',// 消息提示文案
						mailNo=$('.msgbg input[name=mailNo]',_this.parent().parent()).val(),
						buer_name=$('.msgbg input[name=buyername]',_this.parent().parent()).val(),
						buer_mobile=$('.msgbg input[name=warn_value]', _this.parent().parent()).val();
					var msg = msgText.data('changed') ? msgText.val() : '';
					msg = msg == msgText.data("default_value") ? '' : msg;
					var realLength = msg.length;
					
					if(realLength > 0){
						//关键词验证
						$.ajax({
								url:'passManage_toQuestionnaireFilter.action',
								type:'post',
								data:{
									operMsg:msg
								},
								success:function(data){
								    var isvalid = data;
								    if(!isvalid) {
								    	var viewDialog = new Dialog();
								    	viewDialog.init({
											contentHtml: '短信内容含有非法关键字，请修改！',
											yes: function() {
												viewDialog.close();
											},
											yesVal: '关 闭',
											closeBtn: true
										})
								    }else{
								    	if(realLength > 1000 ||  $.trim(msg) == ''){
								    		var bigDialog = new Dialog();
								    		bigDialog.init({
												contentHtml: '消息内容字数在1-1000字之间!',
												yes: function() {// 确认按钮的回调
													bigDialog.close();
												}
											})
											return;
								    	}else{
								    	if(realLength > 70 && realLength <= 1000){//长度大于70的
								    		realLength = realLength-70;
								    		
								    		//验证手机号
								    		var reg = /^0?1[358]\d{9}$/;
											if (!reg.test(buer_mobile)) {
												var seDialog = new Dialog();
												seDialog.init({
													contentHtml: '抱歉，不是正确的手机号，不能发送短信！',
													yes: function() {			// 确认按钮的回调
														seDialog.close();
													}
												});
												return;
											}
										var n = Math.ceil(realLength/67) + 1;
								    	var confirmDel = new Dialog();
								    	confirmDel.init({
								    		contentHtml: '该信息为'+msg.length+'字符，将分'+n+'条短信发送，确定要发送吗?',
								    		yes:function(){
								    			confirmDel.close();
								    			var loadDialog = new Dialog();
								            	var mssg = "正在发送中...";
								            	loadDialog.init({ 			
													contentHtml:mssg
												});
															            						
								            	$.ajax({
													url: 'passManage_sendMessageToCustomer.action',
													type: 'POST',
													data: {
														operMsg: msg,
														buyerPhone:buer_mobile,
														mailNo:mailNo,
														buyerName:buer_name,
														sendMessageCount:n
													},
													success: function(data) {
														loadDialog.close();
													    	var succDialog = new Dialog();
													    	if(data){
													    	succDialog.init({
																contentHtml: '您的信息已经通过短信形式发送给客户！',
																yes: function() {// 确认按钮的回调
																	succDialog.close();
																}
															})
														}else{
															var failureDialog = new Dialog();
															failureDialog.init({
																contentHtml:'亲，你的可发短信数为 0，需要先去购买短信!',
																yes:function(){
																	failureDialog.close();
																	setTimeout(function() {
																		var url = "payService_openSmsServiceAndToBuy.action?menuFlag=sms_package"+parm;
																		url = encodeURI(url); 
																		window.location = url;
																	}, 0);
																},
																no:function(){
																	failureDialog.close();
																},
																yesVal:'购买',
																noVal:'暂不购买',
																closeBtn: true
															})
														}
													   
													}
												})
								    		},
								    		no: function() {
												confirmDel.close();
											}
								    	});
								    }else{//长度小于70的
								    	if(realLength > 0 && realLength < 70){
								    		var loadDialog = new Dialog();
							            	var mssg = "正在发送中...";
							            	loadDialog.init({ 			
												contentHtml:mssg
											});
								    		
								    		//手机号验证
								    		var reg = /^0?1[358]\d{9}$/;
											if (!reg.test(buer_mobile)) {
												loadDialog.close();
												var seDialog = new Dialog();
												seDialog.init({
													contentHtml: '抱歉，不是正确的手机号，不能发送短信！',
													yes: function() {			// 确认按钮的回调
														seDialog.close();
													}
												});
												return;
											}
								    		$.ajax({
												url: 'passManage_sendMessageToCustomer.action',
												type: 'POST',
												data: {
													operMsg: msg,
													buyerPhone:buer_mobile,
													mailNo:mailNo,
													buyerName:buer_name,
													sendMessageCount:1
												},
												success: function(data) {
													loadDialog.close();
												   if(data){
												       var succDialog = new Dialog();
												    	succDialog.init({
															contentHtml: '您的信息已经通过短信形式发送给客户！',
															yes: function() {// 确认按钮的回调
																succDialog.close();
															}
														})
													}else{
														var failureDialog = new Dialog();
														failureDialog.init({
															contentHtml:'亲，你的可发短信数为 0，需要先去购买短信!',
															yes:function(){
																failureDialog.close();
																//window.open('smsServiceMarket_inBuyPorts.action?menuFlag=sms_package');				  
																setTimeout(function() {
																	var url = "payService_openSmsServiceAndToBuy.action?menuFlag=sms_package"+parm;
																	url = encodeURI(url); 
																	window.location = url;
																}, 0);
															},
															no:function(){
																failureDialog.close();
															},
															yesVal:'购买',
															noVal:'暂不购买',
															closeBtn: true
														})

													}
												}
												
											})
								    	}
								    }
								     }	
								      }
								},
								error:function(){
									var viewDialog = new Dialog();
									viewDialog.init({
										contentHtml: '抱歉！系统繁忙，请稍后再试。',
										yes: function() {
											viewDialog.close();
										},
										yesVal: '确定',
										closeBtn: true
									})
								}
						});	
					}else{
						tip = '消息内容字数在1-1000字之间!';
						sendDialog.init({
							contentHtml: tip,
							yes: function() {			// 确认按钮的回调
								sendDialog.close();
							}
						});
					}
				})
			};
			
			
		// 清空消息
			var clearMsg = function() {
				$('.clear_msg1, .clear_msg2').click(function(ev) {
					ev.preventDefault();
					
					var _this = $(this),
						msgEl = $('.msgbg textarea', _this.parent().parent());
						
					msgEl.val('');
				})
			};
			
		var shipNum = function() {
			$('.table table tbody tr .table_tr').mouseover(function(ev) {
				ev.stopPropagation();
				$('.war_c1', $(this).parent()).show();
			});
			$('.table table tbody tr .table_tr').mouseout(function(ev) {
				ev.stopPropagation();
				$('.war_c1', $(this).parent()).hide();
			});
			$('.war_c1 a').click(function(ev) {
				
				
				var _this = $(this),
				    mailNo = $('.mailNo',_this.parents('tr')).val(),
				    name = $('.name',_this.parents('tr')).val(),
				    phone = $('.phone',_this.parents('tr')).val(),
				    status = $('.status',_this.parents('tr')).val(),
				    destination = $('.address',_this.parents('tr')).val();
				
				$.ajax({
					url:'passManage_getBranchInfo.action?menuFlag=chajian_passManage_warn',
					data:{
						mailNo:mailNo
					},
					type:'GET',
					async: false,
					cache:false,
					dataType:'json',
					success:function(data){
						var tab = '<div class="dialog_c">' +
									  '<form id="dialog_form">' +
										  '<p><span class="dialog_p">运单号：</span><span>'+mailNo+'</span></p>'+
										  '<p><span class="dialog_p">接受网点名称：</span><span>'+data.split(',')[1]+'</span></p>'+
										  '<p><span class="dialog_p">问题件描述：</span><span><textarea class="textarea_text" id="textarea_demo" cols="100" rows="5"></textarea><span id="textarea_demoTip"></span></span></p>'+
										  '<p><span class="dialog_p">添加到我的关注：</span><input type="radio" name="attentionFlag" class="input_radio" value="1" /><span class="radio_txt">是</span><input type="radio" name="attentionFlag" class="input_radio" value="2" checked/><span class="radio_txt">否</span></p>' +
										  '<input type="hidden" site="60"  class="queryCondition" value="'+name+','+phone+','+status+','+destination+','+mailNo+','+data.split(',')[0]+','+data.split(',')[2]+','+data.split(',')[3]+','+data.split(',')[4]+','+data.split(',')[5]+'">' +	
									  '</form>' +
								  '</div>';
						
						var dialogD = new Dialog();
						dialogD.init({
							closeBtn: true,
							contentHtml: tab,			// 内容 HTML
							yes: function() {
								if ($.formValidator.pageIsValid('2')) {
									$.ajax({
										url: 'passManage_addReportIssue.action?menuFlag=chajian_passManage_warn',
										type:'POST',
										dataType:'JSON',
										data:{
											queryCondition:$('.queryCondition').val()+','+$('input:radio:checked').val()+','+$('.textarea_text').val()
										},
										success: function() {
											dialogD.close();
											var dialogSucc = new Dialog();
											dialogSucc.init({
												contentHtml: '你的问题已反馈给网点，感谢支持！',
												yes: function() {
													dialogSucc.close();
												}
											})
										},
										error: function() {
											dialogD.close();
											var dialogErr = new Dialog();
											dialogErr.init({
												contentHtml: '抱歉，系统繁忙，请稍后再试！',
												yes: function() {
													dialogErr.close();	
												}	
											});
										}
									})
								}

							},
							no: function() {
								dialogD.close();	
							}
						});
						
						// 元素集合
						var dialogEl = {
							textareaDemo: $('#textarea_demo')
						};
						
						// 提示文案
						var msg = {
								textMinErr: '字数不能少于6个字',
								textMaxErr: '字数不能大于200个字'
						};
						
						$.formValidator.initConfig({
							validatorGroup: '2',
							formID: 'dialog_form',
							theme: 'yto',
							errorFocus: false
						});
						
						dialogEl.textareaDemo.
							formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' '}).
							inputValidator({min: 12, max: 368, onErrorMin: msg.textMinErr, onErrorMax: msg.textMaxErr});
						
						
						
					},error:function(){
						var dialogErr = new Dialog();
						
						dialogErr.init({
							contentHtml: '抱歉，系统繁忙，请稍后再试！',
							yes: function() {
								dialogErr.close();	
							}
						})
					}
				});
				
				// 重新绑定 tab 事件
				ytoTab.init();
			});
		};  
		
		/**
		 * 展开DIV
		 */
		/*var openDiv = function(){
			$('.open').live('click',function(){
				var _this = $(this),
					openDiv = $('.openDiv',_this.parents('.td_c2')),
					closeDiv = $('.closeDiv',_this.parents('.td_c2'));
				openDiv.attr('style','display:none');
				closeDiv.attr('style','display:inline');
			});
		};*/
		
		/**
		 * 收起DIV
		 */
		/*var closeDiv = function(){
			$('.close').live('click',function(){
				var _this = $(this),
					openDiv = $('.openDiv',_this.parents('.td_c2')),
					closeDiv = $('.closeDiv',_this.parents('.td_c2'));
				openDiv.attr('style','display:inline');
				closeDiv.attr('style','display:none');
			});
		};*/
		
		// 展开收拢
		var fold = function() {
			
			// 点击展开
			$('.fold_desc').live('click', function(ev) {
				ev.preventDefault();

				var _this = $(this),
				open = _this.parent().find('.openDiv');
				
				open.html(open.data("orgin"));
				//open.css({'height':'auto','overflow':'visible'});
				_this.replaceWith('<a href="javascript:;" class="unfold_desc">收拢</a>');
			});
			
			// 点击收拢
			$('.unfold_desc').live('click', function(ev) {
				ev.preventDefault();

				var _this = $(this), 
				open = _this.parent().find('.openDiv');
				
				open.html(open.data("cut"));
				//open.css({'height':'220px','overflow':'hidden'});
				
				_this.replaceWith('<a href="javascript:;" class="fold_desc">展开</a>');
			});
			
			$('.openDiv').each(function(){
				
				var _this = $(this),
				    reg = /<\/?[^>]+>/gi,
				    o_html = _this.html(),
				    o_val = o_html.replace(reg,'');
				    
				if(o_val.length > 70){
					
					o_val = o_html.split(reg)[0];
					o_val = (o_val.length > 70 ? o_val.substr(0,70) : o_val) + '...';
					//_this.css({'height':'220px','overflow':'hidden'});
					
					$('<a class="fold_desc" href="javascript:;">展开</a>').insertAfter(_this);
					_this.data("cut", o_val);
					_this.data("orgin", o_html);
					_this.html(_this.data("cut"));
				}
				
				_this.show();
			});
		};
		
		return {
			init: function() {
				ytoTab.init(config.tabIndex);
				textDefault();
				changeDownUp();
				check();
				form();
				sendMsg();
				shipNum();
				clearMsg();
				//shipNumDetail();
				/*openDiv();
				closeDiv();*/
				fold();
				note_user();
				
				$('.ctt_p2 textarea').each(function() {
					var _this = $(this),
						cttVal = _this.val();
				
					_this.css('color', '#999').bind({
						focus: function() {
							if ($(this).val() == cttVal) {
								$(this).val('').css('color', '#000');
							}
							
						},
						
						blur: function() {
							if ($(this).val() == '') {
								$(this).val(cttVal).css('color', '#999');
							}
						}
					});
				});
				
				
				
				//设置预警值
				$('#dialog_demo_c , #setWarnValue').click(function(ev) {
					ev.preventDefault();
					//需要判断权限
					/*$.ajax({
						url:'passManage_rightControl.action?menuFlag=chajian_passManage_warn',
						dataType:'json',
						cache:false,
						success:function(data){
							if(data.status){*/
								if(config.onStep<2){
									window.location.reload(true);
									return;
								}
								//window.location='passManage_searchWarnValueList.action?menuFlag=chajian_passManage_warn&tips=0';
								window.location='passManage_setWarnValue.action?menuFlag=chajian_passManage_warn&tips=0';
								//发送异步请求[参数tips判断是初始化进入层_0还是点击上/下页_1]
//								$.ajax({
//									url:"passManage_searchWarnValueList.action?menuFlag=chajian_passManage_warn&tips=0",
//									type:"GET",
//									cache: false,
//									dataType: 'html',
//									success:function(data){
//										
//										var sndDialog = new Dialog();
//										sndDialog.init({
//											contentHtml: data,
//											closeBtn: true,
//											yes:function(){
//												sndDialog.close();
//											},
//											yesVal:'确定'
//										});
//									},
//									error:function(){
//										// 关闭弹层
//										loadDialog.close();
//										
//										var errorDialog = new Dialog();
//										errorDialog.init({
//											contentHtml: '抱歉，系统繁忙，请稍后再试！',
//											closeBtn: true
//										});
//									}
//								});
							/*}else{
								var viewDialog = new Dialog();
								viewDialog.init({
									contentHtml: '亲，这功能不是您开启的，您没有权限操作！',
									yes: function() {
										viewDialog.close();
									},
									yesVal: '确定',
									closeBtn: true
								})
							}
						}
					})*/
				});
				
				$('#wei').click(function(){
					$('.tips').html('正在查询，请稍后...');
					$('#pagenavi1').attr('style','display:none');
					$('#pagenavi2').attr('style','display:none');
					window.location.href="passManage_warnningIndex.action?menuFlag=chajian_passManage_warn";
//					$('#pagenavi1').attr('style','display:inline');
				});
				
				//已上报面板
				$("#yi").click(function(){
					$('.tips').html('正在查询，请稍后...');
					$('#pagenavi2').attr('style','display:none');
					$('#pagenavi1').attr('style','display:none');
					window.location.href="passManage_list_seller.action?menuFlag=chajian_passManage_warn"; //无tips值
//					$('#pagenavi2').attr('style','display:inline');
				});
				
				// tab切换
				$('.msgbg2 .tb_ li').click(function() {
					var _this = $(this),
						index = _this.index();
					_this.removeClass('normaltab').addClass('hovertab');
					_this.siblings().removeClass('hovertab').addClass('normaltab');
					
					$('.ctt div', _this.parents('.msgbg2')).hide();
					$('.ctt div', _this.parents('.msgbg2')).eq(index).show();
				});
				
				//未上报筛选
				$("#weiChoose").click(function(ev){
					ev.preventDefault()
					var _this = $(this),condition = $(".input_text",_this.parents('form')).val();
					
					/*if(condition == '姓名/电话/运单'){
						$('#input_text_textTip').html('姓名/电话/运单号必输其一');
					}else{*/
						window.location.href="passManage_warnningIndex.action?menuFlag=chajian_passManage_warn&tips=0&queryCondition="+condition;
					//}					
				});
				
				//翻页
				pagination.click(function(){
					$('#currentPage').val($(this).attr("value"));
					var tabNum = $('#tabNum').val();
					if(tabNum == '0')$('#myform').attr('action','passManage_warnningIndex.action?menuFlag=chajian_passManage_warn');
					if(tabNum == '1')$('#myform').attr('action','passManage_list_seller.action?menuFlag=chajian_passManage_warn&tips=0');
					
					setTimeout(function(){
						$("#myform").submit();
					},0);
				});
				
				//跳转问题件页面
				$('.ques').live('click',function(){
					var _this = $(this),
						time = $('.fenQuTime',_this.parent()).text(),
						mailNo = $('.mailNo',_this.parents('tr')).val();
					window.location.href="questionnaire_list.action?menuFlag=chajian_question&conditionString="+mailNo+"&starttime="+time+"&endtime="+time+"&autoSkip=1";
				});
				
				//店铺筛选
				$('.thead_select').each(function(){
					$(this).children().each(function(){
						$(this).click(function(ev){
							ev.preventDefault();
							if($(this).val()==0){
								window.location="associationAccount_toBindeAccountCustom.action?menuFlag=user_acc_custom";
							}else{
								$('#userId').val($(this).val());
								var userId = $(this).val();
								var queryCondition = $('#queryCondition').val();
								$('#currentPage').val(1);
								//if(queryCondition == null){
									$('#myform').attr('name','menuFlag=passManage_warn');
									$('#myform').attr('action','passManage_warnningIndex.action');
								/*}
								else{
									$('#myform').attr('action','passManage_warnningIndex.action');
								}*/
								
								$('#myform').trigger('submit');
							}
						});
					});
				});
			}
		}
		
	})();
	
	problem_war.init();
})

