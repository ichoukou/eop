/**
 * 智能查件
**/
$(function() {
	var ytosearch = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		// 全局配置
		var config = {
			onStep: winParams.onStep || null,
			userType:winParams.userType || null
		};
		
		var textDefault = function() {
			if($.trim($('#textarea_search').text()) != null && $.trim($('#textarea_search').text()) != ''){
				$('#textarea_search').text($.trim($('#textarea_search').text()));
			}else{
				$('#textarea_search').defaultTxt('请输入买家姓名/买家电话/运单号，查多号时用斜杠"/"、空格键或者回车键分隔');
			}
		};
		
		// 元素集合
		var els = {
			searForm: $('#sear_form'),
			searFreight: $('#textarea_search'),
			num: $('#num'),
			numTip: $('#numTip'),
			searBtn: $('#sear_btn'),
			resetBtn: $('#reset_btn'),
			currentPage: $('#currentPage'),
			thead_select: $('.thead_select'),//店铺选择处的class
			bindUserId: $('#bindUserId')//单个店铺id
		};
		
		// 表单
		var form = function() {
			
			// 验证初始值
			var numResult = true;	// 连续 N 条运单的验证结果
			var otherResult = true;
			
			var mobileReg = /^(13[0-9]{9}|15[012356789][0-9]{8}|18[0256789][0-9]{8}|147[0-9]{8})$/;
				
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
			
			
			// 提示文案
			var tipsMsg = {
				searFormatErr: '格式错误，请修改',
				searEmptyErr: '查询条件不能为空',
				searLongErr: '内容超长',
				numErr: '只能查询20条以内的连续运单',
				numFormatErr: '您填写的不是运单号'
			};
			
			// 验证方法
			var check = {
				num: function(numVal) {
					if (numVal >= 0 && numVal <= 20) {				// 如果是数字且在 0-20 范围内
						if (dataType.isShipNum(els.searFreight.val())) {		// 如果填的是运单
							showIcon.correct(els.numTip, '');
							numResult = true;
						}
					} else {										// 如果不是数字或者超出范围
						showIcon.error(els.numTip, tipsMsg.numErr);
						numResult = false;
					}
				}
			};
			
			// 初始化表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 智能查件
			els.searFreight.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				inputValidator({min: 1, onErrorMin: tipsMsg.searEmptyErr, max: 230, onErrorMax: tipsMsg.searLongErr}).
				functionValidator({
						fun: function(val, el) {
							if(val === '请输入买家姓名/买家电话/运单号，查多号时用斜杠"/"、空格键或者回车键分隔' || $.trim(val) === ''){
									return false;
								}
							},
						onError:tipsMsg.searEmptyErr
				}).
				functionValidator({fun: validateSearch, onError: tipsMsg.searFormatErr});
			
			// 连续 N 条运单
			els.num.bind({
				blur: function() {
					var val = $(this).val();
					if (val != '') {
						check.num(parseInt(val));
					}
				},
				focus: function() {
					// 清空提示
					els.numTip.html('');
				}
			});
			
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				if(config.onStep < 2){
					window.location.reload(true);
					return;
				}
				
				otherResult = $.formValidator.pageIsValid('1');
				els.bindUserId.val(0);
				$('#isCheck').val(0);
				var numVal = els.num.val();
				if (numVal != '') {
					check.num(parseInt(numVal));
					$('#isCheck').val(1);
				}
				
				if (numResult && otherResult) {
					els.searForm.trigger('submit');
				}
			});
			
			// 点击“清空”
			els.resetBtn.click(function(){
				els.num.val('');
				els.searFreight.val('').text('');
				$('#textarea_searchTip').html('');
			});
			
			
			//短信通知
			$(".smsSend").click(function(ev){
				ev.preventDefault();
				var _this = $(this);
				
				//查询参数,返回短信购买使用
				var currentPage = $("#currentPage").val();
				var logisticsIds = $("#textarea_search").val();
				var num = $("#num").val();
				var isCheck = $("#isCheck").val();
				
				//参数
				var feedbackInfo = _this.parent().parent().parent().find("input[name='feedbackInfo']").val();
				var bName = _this.parent().find("input[name='bName']").val();
				var bMobile = _this.parent().find("input[name='bMobile']").val();
				var bMailno = _this.parent().find("input[name='bMailno']").val();
				var serviceType = "AGENT";
				
				if(bMobile==undefined) {
					bMobile = '';
				}
				if(feedbackInfo==undefined) {
					feedbackInfo = '';
				}
				//feedbackInfo = "测试";
				
				var theMobile = bMobile;
				var theFeedbackInfo = feedbackInfo;
				if(theFeedbackInfo=='') {
					theMobile = '';
					theFeedbackInfo = '';
				}else {
					feedbackInfo = "亲，您的运单号“" + bMailno + "” " + feedbackInfo;
					theFeedbackInfo = feedbackInfo;
				}
				
				var lengthContent = theFeedbackInfo.length;
				
				//弹出页面
				var sendDialog = new Dialog();	
				var viewHtml = '<div id="sms_content">' +
				'	<div class="table">' +
				'		<table>' +
				'			<tbody>' +
				'				<tr>' +
				'					<td width="80"><strong>收件人：</strong></td>' +
				'					<td>' + '<input type="text" id="_theMobile" class="input_text" value="' + theMobile + '" /><span id="error_content"></span></td>' +
				'				</tr>' +
				'				<tr>' +
				'					<td width="80"><strong>通知内容：</strong></td>' +
				'					<td>' + '<textarea id="_content">' + theFeedbackInfo + '</textarea>' + '<span id="_contentTip"></span></td>' +
				'				</tr>' +
				'				<tr colspan="4" style="padding:0;">' +
				'					<td colspan="4" style="padding:0 0 0 4px;"><strong>已输入<span id="font_count">' + lengthContent + '</span>字,70个字符算一条短信,超过70个字符,每67个字符算一条短信</strong></td>' +
				'				</tr>' +
				'				<tr colspan="4">' +
				'					<td colspan="4" style="padding:0 0 0 4px;"><strong><div id="_err"></div></strong></td>' +
				'				</tr>' +
				'			</tbody>' +
				'		</table>' +
				'	</div>' +
				'</div>';				
				
				//页面初始化
				sendDialog.init({
					contentHtml: viewHtml,
					yes: function() {
						var cFlag = true,
						    bFlag = true;
						
						//获取DIV中的动态值
						bMobile = $("#_theMobile").val();
						
						var content = $("#_content").val();
						
						if(content === ''){
							showIcon.error($("#_contentTip"), '请输入通知内容');
							cFlag = false;
						}
						
						if(bMobile === ''){
							showIcon.error($("#error_content"), '请输入手机号码');
							bFlag = false;
							//return;
						}
						else{
							if (!mobileReg.test(bMobile)) {
								showIcon.error($("#error_content"), '手机号码格式不正确');
								bFlag = false;
								//return;
							}
						}
						
						if(!(bFlag && cFlag)){
							return;
						}
						
						$("#error_content").html('');
						$("#_contentTip").html('');
						feedbackInfo = $("#_content").val();
						//检查文字长度,判断分多少信息查询
						var n = 1;
						var smsLength = feedbackInfo.length;
						if(smsLength>70) {
							smsLength = smsLength-70;
							n = Math.ceil(smsLength/67) + 1;
						}

						$("#font_count").html(feedbackInfo.length);
						//验证短信服务,是否处于开启状态
						$.ajax({
							url:'smsSearch!isAllowSend.action',
							type:'post',
							data:{
								"sendCount":n
							},
							dataType:'json',
							success:function(data){								
							    var isvalid = data;
							    if(isvalid!="-1") {
							    	sendDialog.close();
									var failureDialog = new Dialog();
									failureDialog.init({
										contentHtml:'亲，你的可发短信数为 '+ isvalid + '，需要先去购买短信!',
										yes:function(){
											failureDialog.close();
											var parm = "&currentPage2="+currentPage+"&logisticsIds="+logisticsIds+"&isCheck2="+isCheck+"&num="+num+"&pos=3";
											setTimeout(function() {
												var url = "payService_openSmsServiceAndToBuy.action?menuFlag=sms_package"+parm;
												url = encodeURI(url); 
												window.location = url;
											}, 0);
											//window.open('payService_openSmsServiceAndToBuy.action?menuFlag=sms_package');
										},
										no:function(){
											failureDialog.close();
										},
										yesVal:'购买',
										noVal:'暂不购买',
										closeBtn: true
									})
							    }else {	//短信发送
							    	if(feedbackInfo.length > 1000 || feedbackInfo==""){
							    		sendDialog.close();
										var oDialog = new Dialog();
										oDialog.init({
											contentHtml: "消息内容字数在1-1000字之间!",
											yes: function() {
												oDialog.close();
											},
											closeBtn: true
										});
										return;
									} 
									
									$("#error_content").html('');
									//关键词验证
									$.ajax({
											url:'smsSearch!toQuestionnaireFilter.action',
											type:'post',
											data:{
												"feedbackInfo" : feedbackInfo
											},
											dataType:'json',
											success:function(data){
											    var isvalid = data;
											    //var invalidWordsStr = data.invalidWordsStr;	 
											    if(isvalid=="false") {
											    	showIcon.error($("#_err"), '消息内容含有非法关键字，请修改！');
											    	//$("#_err").html('消息内容含有非法关键字，请修改！');
											    }else {
											    	$("#_err").html('');
											    	sendDialog.close();
											    	//关键字验证通过
											    	//发送提示
													var loadDialog = new Dialog();
									            	var msg = "正在发送中...";
									            	loadDialog.init({ 			
														contentHtml:msg
													});
													$.ajax({
														type:"POST",
														dataType:"json",
														cache:false,
														url:"smsSearch!sendSms.action",
														data:{
															"sendCount":n,
															"sendBuyerName": bName,
															"sendBuyerMobile": bMobile,	
															"sendMailno":bMailno,
															"feedbackInfo" : feedbackInfo,
															"serviceType":serviceType
														},
														success:function(data){
															loadDialog.close();
															
															var oDialog = new Dialog();
															oDialog.init({
																contentHtml: data,
																yes: function() {
																	oDialog.close();
																},
																closeBtn: true
															});
														}
													});
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
							    }
							}
						});
						
					},
					no: function() {
						//_this.parent().parent().find("textarea").val("");
						sendDialog.close();
					},
					yesVal:'发送',
					noVal:'取消',
					closeBtn: true
				});
				
				$('#_content').keyup(function(ev){
					$('#font_count').html($("#_content").val().length);
				});
				
				$('#_theMobile').change(function(){
					var mobileVal = $(this).val();

					if(mobileVal === ''){
						showIcon.error($("#error_content"), '请输入手机号码');
						return;
					}

					if (!mobileReg.test(mobileVal)) {
						showIcon.error($("#error_content"), '手机号码格式不正确');
						return;
					}
					$("#error_content").html('');
				});
				
				$('#_content').change(function(){
					var content = $(this).val();
					
					if(content === ''){
						showIcon.error($("#_contentTip"), '请输入通知内容');
					}
				});
			});
			
			
			//写备注
			$(".remark").click(function(ev){
				ev.preventDefault();
				
				var loadDialog = new Dialog();
            	var msg = "正在保存中...";
            	loadDialog.init({ 			
					contentHtml:msg
				});
				
				var _this = $(this);
				var questionnaireId = _this.attr("name");
				var feedbackInfo = _this.parent().parent().find("textarea").val();
				if(feedbackInfo.length > 1000 || $.trim(feedbackInfo)==""){
					loadDialog.close();
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "消息内容字数在1-1000字之间!",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				} else{
					$.ajax({
						type:"POST",
						dataType:"json",
						cache:false,
						url:"questionnaire_remark.action",
						data:{
							"questionnaireId" : questionnaireId,
							"feedbackInfo" : feedbackInfo
						},
						success:function(data){
							loadDialog.close();
							_this.parent().parent().find("textarea").val("");
							var oDialog = new Dialog();
							oDialog.init({
								contentHtml: data,
								yes: function() {
									oDialog.close();
									els.qForm.trigger('submit');
								},
								closeBtn: true
							});
						}
					});
				}
			});
			
			
			//店铺筛选
			els.thead_select.each(function(){
				$(this).children().each(function(){
					$(this).click(function(ev){
						ev.preventDefault();
						if($(this).val()==0){
							window.location="associationAccount_toBindeAccountCustom.action";
						}else{
							els.bindUserId.val($(this).val());
							els.currentPage.val(1);
							
							$('#isCheck').val(0);
							var numVal = els.num.val();
							if (numVal != '') {
								check.num(parseInt(numVal));
								$('#isCheck').val(1);
							}
							
							if (numResult && otherResult) {
								els.searForm.trigger('submit');
							}
						}
					});
				});
			});
			
			//翻页
			pagination.click(function(ev){
				ev.preventDefault();
				els.currentPage.val($(this).attr("value"));
				
				$('#isCheck').val(0);
				var numVal = els.num.val();
				if (numVal != '') {
					check.num(parseInt(numVal));
					$('#isCheck').val(1);
				}
				
				if (numResult && otherResult) {
					setTimeout(function(){
						els.searForm.trigger('submit');
					},0);
				}
			});
			
		};
		
		return {
			init: function() {
				textDefault();
				form();
				//shipNumDetail();
			}
		}
	})();
	
	ytosearch.init();
})

//function shopManage(){
//	window.location="associationAccount_toBindeAccountCustom.action";
//}

function searchSlor(key){
	window.location = "branchsolr.action?menuFlag=branchlist&searchKey="+key;
}
function addInAttention(obj,mailNO){
    if(mailNO!=null && mailNO!=""){
    	$(obj).attr("style","display:none");
        if(obj.title=="加入关注"){
            $.ajax({
            	cache:false,
                url : 'attention_addInAttention.action',
                data:{
                    mailNO:mailNO
                },
                success:function(data){
                    if(data=="关注成功"){
                        obj.src="images/icons/u10_normal.png";
                        obj.title="取消关注";
                    }
                    $(obj).removeAttr("style");
                    var alertDialog = new Dialog();
					alertDialog.init({
						contentHtml: "添加成功，到“我的关注”查看",
						yes: function() {
							alertDialog.close();
						},
						closeBtn: true
					});
                    return;
                }
            });
        }else{
            $.ajax({
            	cache:false,
                url : 'attention_cancleAttention.action',
                data:{
                    mailNO:mailNO
                },
                success:function(data){
                    if(data=="取消成功"){
                        obj.src="images/icons/u9_normal.png";
                        obj.title="加入关注";
                    }
                    $(obj).removeAttr("style");
                    var alertDialog = new Dialog();
					alertDialog.init({
						contentHtml: "成功取消关注",
						yes: function() {
							alertDialog.close();
						},
						closeBtn: true
					});
                    return;
                }
            });
        }
    }else{
    	var alertDialog = new Dialog();
		alertDialog.init({
			contentHtml: "该订单没有运单号",
			yes: function() {
				alertDialog.close();
			},
			closeBtn: true
		});
		return;
    }
}

