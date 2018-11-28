$(function() {
	var markHome2 = (function() {
		var winParams = window.params || {};

		// 全局配置
		var config = {
			saveSearchAction: winParams.saveSearchAction || '',				// 保存搜索条件 Action
			prevStepAction: winParams.prevStepAction || '',					// 上一步
			nextStepAction: winParams.nextStepAction || '',					// 下一步
			smsCountAction: winParams.smsCountAction || ''					// 短信数量请求
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

		// 表单
		var form = function() {
			// 元素集合
			var els = {
				member: $('#member'),
				markForm2: $('#mark_form2'),
				tradeAmountA: $('#trade_amount_a'),
				tradeAmountB: $('#trade_amount_b'),
				tradeAmountTip: $('#trade_amountTip'),
				tradeCountA: $('#trade_count_a'),
				tradeCountB: $('#trade_count_b'),
				tradeCountTip: $('#trade_countTip'),
				searBtn: $('#sear_btn'),
				saveSear: $('#save_btn'),
				saveSearInput: $('#save_sear'),
				saveSearTip: $('#save_searTip'),
				searName: $('#sear_name'),
				tradeTime: $('#trade_time'),
				lastTrade: $('#last_trade'),
				atProvince: $('#receiverProvince'),
				memLevel: $('#member_level'),
				errTip: $('#errTip'),
				menuFlag:$('#smsMade')
			};
			
			// 文案
			var msg = {
				memberDef: '联系人/手机号/会员名',
				memberFormatErr: '您输入的内容格式有误',
				tradeAmountFormatErr: '请输入正整数',
				tradeAmountRangeErr: '请输入正确的交易量范围',
				tradeCountFormatErr: '您输入的交易额格式有误',
				tradeCountRangeErr: '请输入正确的交易额范围',
				saveSearFormatErr: '格式错误',
				checkedCustomer: '请选择收信人',
				hasNotEnoughSms: '可发短信不足，请重新选择或<a href="smsServiceMarket!inBuyPorts.action?menuFlag=sms_package" target="_blank">购买短信包</a>'
			};
			
			//输入框有查询条件,不做清除处理
			if(els.member.val()==''||(els.member.val()==msg.memberDef)) {
				els.member.defaultTxt(msg.memberDef);
			}
			
			// 地区联动下拉框
			var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#receiverProvince'],
				head: '所有地区',
				autoLink: false
			};
			
			var linkageSel = new LinkageSel(area);
			
			//页面关联地区
			var areaInit = function() {	
				var province = $("#atprovince").val();
				var atcity = $("#atcity").val();
				var atarea = $("#atarea").val();
				linkageSel.changeValues([province,atcity,atarea]);
			}
			
			areaInit();
			
			// 表单验证初始化
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'mark_form2',
				theme: 'yto',
				errorFocus: false
			});
			
			// 输入框
			els.member.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				functionValidator({
					fun: function(val) {
						if (val == msg.memberDef) {
							els.member.val('');
						}
					}
				}).
				regexValidator({regExp: ['empty', 'name', 'accountId', 'mobile'], dataType: 'enum', onError: msg.memberFormatErr});
			
			// 交易量
			els.tradeAmountA.
				formValidator({validatorGroup:'1', tipID: 'trade_amountTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1'], dataType: 'enum', onError: msg.tradeAmountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val !== '' && els.tradeAmountB.val() !== '') {
							return parseFloat($.trim(val)) <= parseFloat($.trim(els.tradeAmountB.val()));   
						}
					},
					onError: msg.tradeAmountRangeErr
				});
				
			els.tradeAmountB.
				formValidator({validatorGroup:'1', tipID: 'trade_amountTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1'], dataType: 'enum', onError: msg.tradeAmountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val !== '' && els.tradeAmountA.val() !== '') {
							return parseFloat($.trim(val)) >= parseFloat($.trim(els.tradeAmountA.val()));   
						}
					},
					onError: msg.tradeAmountRangeErr
				});
				
				
			// 交易额
			els.tradeCountA.
				formValidator({validatorGroup:'1', tipID: 'trade_countTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1', 'decmal4'], dataType: 'enum', onError: msg.tradeCountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val !== '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
							if(els.tradeCountB.val() !== ''){
								return parseFloat($.trim(val)) <= parseFloat($.trim(els.tradeCountB.val()));
							}
						}
					},
					onError: msg.tradeCountRangeErr
				});
				
				
			els.tradeCountB.
				formValidator({validatorGroup:'1', tipID: 'trade_countTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1', 'decmal4'], dataType: 'enum', onError: msg.tradeCountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val !== '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
							if(els.tradeCountA.val() !== ''){
								return parseFloat($.trim(val)) >= parseFloat($.trim(els.tradeCountA.val()));
							}							
						}
					},
					onError: msg.tradeCountRangeErr
				});
			
			
			//当搜索器名称选择：‘请选择’时清空form表单  
			els.searName.change(function(){
				//清空form
				if ($(this).val()=='') {
					$('input[type=text]','#mark_form2')  
					 .not(':button, :submit, :reset')  
					 .val('')
					 .removeAttr('checked')  
					 .removeAttr('selected'); 

					$("#trade_time").val("0");
					$("#last_trade").val("0");
					$("#receiverProvince").val("");
					$("#atprovince").val("");
					$("#atcity").val("");
					$("#atarea").val("");
					$("#member_level").val("4");
					
					$('#save_sear').val('');
					$('#receiverProvince').next().hide();
					//$('#receiverProvince').next().val("");
					$('#receiverProvince').next().next().hide();
					//$('#receiverProvince').next().next().val("");
					return;
				}
				
				//请求数据
				$.get('smsServiceMarket!findSmsBuyersSearch.action?searchName='+$(this).val(),function(data){
					var str=data.split('|');
					//拼接规则tradeTime,last_trade,province,city,area,memLevel,tradeAmountA,tradeAmountB,tradeCountA,tradeCountB,member
					var tradeTime = str[1];
					var lastTrade = str[2];
					if(tradeTime=='') {
						tradeTime = '0';
					}else if(tradeTime=='7'){
						tradeTime = '1';
					}else if(tradeTime=='14'){
						tradeTime = '2';
					}else if(tradeTime=='30'){
						tradeTime = '3';
					}
					if(lastTrade=='') {
						lastTrade = '0';
					}else if(lastTrade=='7'){
						lastTrade = '1';
					}else if(lastTrade=='14'){
						lastTrade = '2';
					}else if(lastTrade=='30'){
						lastTrade = '3';
					}
					$('#trade_time').val(tradeTime);
					$('#last_trade').val(lastTrade);
					$("#receiverProvince").val(str[3]);
					$("#atprovince").val(str[3]);
					$("#atcity").val(str[4]);
					$("#atarea").val(str[5]);
					
					//alert(str[3]+"|"+str[4]+"|"+str[5]);
					$("#receiverProvince").find(":contains('"+str[3]+"')").attr("selected","selected");
                    $("#receiverProvince").change();
                    $("#receiverProvince").next().find(":contains('"+str[4]+"')").attr("selected","selected");
                    $("#receiverProvince").next().change();
                    $("#receiverProvince").next().next().find(":contains('"+str[5]+"')").attr("selected","selected");
					
					linkageSel.changeValues([str[3],str[4],str[5]]);

					$('#member_level').val(str[6]);
					$('#trade_amount_a').val(str[7]);
					$('#trade_amount_b').val(str[8]);
					$('#trade_count_a').val(str[9]);
					$('#trade_count_b').val(str[10]);
					$('#member').val(str[11]);
					//$('#smsBuyersSearchId').val(str[11]);
					$('#save_sear').val(str[12]);
				});
			
			});
			
				
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				$("#currentPage").val(1);
				$("#pageChecked_").val('');
				$("#customerId_").val('');
				$("#customerNumber_").val(0);
				//给省市县区赋值编码
				var selectedArr = linkageSel.getSelectedArr();
				$("#atprovince").val(selectedArr[0]);
				$("#atcity").val(selectedArr[1]);
				$("#atarea").val(selectedArr[2]);
				
				//给省市县区赋值文字
				$("#provinceName_").val(linkageSel.getSelectedData('name',0));
				$("#cityName_").val(linkageSel.getSelectedData('name',1));
				$("#areaName_").val(linkageSel.getSelectedData('name',2));
				
				setTimeout(function(){els.markForm2.trigger('submit');},0);
				
			});
			
			//翻页
			pagination.live("click",function(ev){
				ev.preventDefault();	
				$("#currentPage").val($(this).attr("value"));
				
				//给省市县区赋值编码
				var selectedArr = linkageSel.getSelectedArr();
				$("#atprovince").val(selectedArr[0]);
				$("#atcity").val(selectedArr[1]);
				$("#atarea").val(selectedArr[2]);
				
				//给省市县区赋值文字
				$("#provinceName_").val(linkageSel.getSelectedData('name',0));
				$("#cityName_").val(linkageSel.getSelectedData('name',1));
				$("#areaName_").val(linkageSel.getSelectedData('name',2));
				
				//翻页选中
				/*
				var checkedInput = $('.table tbody .input_checkbox:checked');
				var customerId = $("#customerId_").val();
				var ids = [];
				if(customerId!='') {
					ids = customerId.split(",");	
				}
				if ($('#all_page').prop('checked')) {
					customerId = 'all';
				} else {
					var a = ids;
					var isPop = false;
					checkedInput.each(function() {
						var id = $(this).val();
						if(customerId=='') {
							a.push(id);
						}else {
							var isPush = true;
							for(i=0;i<a.length;i++) {
								alert("a[i]"+a[i]);
								if(a[i]==id) {
									isPush = false;
								}
							}
							if(isPush) {
								a.push(id);
							}
						}
					});
				}
				customerId = a.toString();
				*/
				$("#pageChecked_").val("");
				$("#customerId_").val("");
				$("#customerNumber_").val(0);
				
				
				setTimeout(function(){els.markForm2.trigger('submit');},0);
			});
			
			// 点击“保存”
			els.saveSear.click(function(ev) {
				ev.preventDefault();
				
				var saveSearInputVal = els.saveSearInput.val().length;
				
				if (saveSearInputVal >= 2 && saveSearInputVal <= 20) {
					// 验证表单
					if ($.formValidator.pageIsValid(1)) {
						var selectedArr = linkageSel.getSelectedArr();
						$.ajax({
							url: config.saveSearchAction,
							cache: false,
							type: 'POST',
							data: {
								searchName: els.searName.val(),
								theLastTradeTime: els.tradeTime.val(),
								theLastMarketTime: els.lastTrade.val(),
								receiverProvince: selectedArr[0],
								receiverCity: selectedArr[1],
								receiverDistrict: selectedArr[2],
								userGrade: els.memLevel.val(),
								searchInput: els.member.val(),
								tradeCountMin: els.tradeAmountA.val(),
								tradeCountMax: els.tradeAmountB.val(),
								tradeAmountMin: els.tradeCountA.val(),
								tradeAmountMax: els.tradeCountB.val(),
								searchName2: els.saveSearInput.val(),
								menuFlag:els.menuFlag.val()
							},
							success: function(data) {
								var id = data.infoContent;
								loadSmsSearchData(id);	
								var succDialog = new Dialog();
								succDialog.init({
									contentHtml: '保存成功！',
									iconType: 'success',
									autoClose: 2000
								})
							}
						})
					}
					
					showIcon.correct(els.saveSearTip, ' ');
				} else {
					showIcon.error(els.saveSearTip, msg.saveSearFormatErr);
				}

			});
			
			// 点击下一步
			$('#next_step_b').click(function() {
				var checkedInput = $('.table tbody .input_checkbox:checked');
				if (checkedInput.length != 0) {		// 如果有勾选
					var customerNumber = $("#checked_number").text();
					$.ajax({
						url: config.smsCountAction + "?customerNumber="+customerNumber,
						cache: false,
						type: 'GET',
						success: function(data) {
							if (data=='true') {	// 有足够短信
								var customerId;
								if ($('#all_page').prop('checked')) {
									customerId = 'all';
								} else {
									var a = [];
									checkedInput.each(function() {
										a.push($(this).val());									
									});
									
									customerId = a.toString();
								}
								// 前往下一步							
								nextStep3(customerId);
								
								/*
								
								$.ajax({
									url: config.nextStepAction,
									cache: false,
									type: 'POST',
									data: {
										searchName: els.searName.val(),
										theLastTradeTime: els.tradeTime.val(),
										theLastMarketTime: els.lastTrade.val(),
										receiverProvince: els.atProvince.val(),
										city: els.atProvince.next().val(),
										area: els.atProvince.next().next().val(),
										userGrade: els.memLevel.val(),
										searchInput: els.member.val(),
										tradeCountMin: els.tradeAmountA.val(),
										tradeCountMax: els.tradeAmountB.val(),
										tradeAmountMin: els.tradeCountA.val(),
										tradeAmountMax: els.tradeCountB.val(),
										searchName2: els.saveSearInput.val(),
										
										templateId: $('#template_id').val(),
										customerId: customerId
									},
									success: function() {
										var succDialog = new Dialog();
										
										nextStepC();
										
										succDialog.init({
											contentHtml: '保存成功！',
											iconType: 'success',
											autoClose: 2000			
										})
									}
								})
								
								*/
							} else {	
								// 没有足够短信
								showIcon.error(els.errTip, msg.hasNotEnoughSms);
							}
						}
					});
					
					showIcon.correct(els.errTip, '');
				} else {															// 如果没有勾选
					showIcon.error(els.errTip, msg.checkedCustomer);
				}
			});
			
			// 点击上一步
			$('#prev_step_b').click(function(ev) {
				ev.preventDefault();
				window.location = config.prevStepAction + '?menuFlag=sms_made&templateId=' + $('#template_id').val();
			});
			
			// 下一步
			var nextStep3 = function(customerId) {
				
			 	els.markForm2.attr("action","smsServiceMarket!smsMarketPageStep3.action");
				//给省市县区赋值编码
				var selectedArr = linkageSel.getSelectedArr();
				$("#atprovince").val(selectedArr[0]);
				$("#atcity").val(selectedArr[1]);
				$("#atarea").val(selectedArr[2]);
				
				//给省市县区赋值文字
				$("#provinceName_").val(linkageSel.getSelectedData('name',0));
				$("#cityName_").val(linkageSel.getSelectedData('name',1));
				$("#areaName_").val(linkageSel.getSelectedData('name',2));
				
				$("#customerId_").val(customerId);
				$("#customerNumber_").val($("#checked_number").text());
				setTimeout(function(){$('#mark_form2').trigger('submit');},0);
			};
		};
		
		//选中之前所选的值
		var checkCheck = function() {
			var customerId = $("#customerId_").val();
			var ids = customerId.split(",");
			var checkedInput = $('.table tbody .input_checkbox');
			checkedInput.each(function() {
				var id = $(this).val();
				for(i=0;i<ids.length;i++) {
					if(ids[i]==id) {
						$(this).prop('checked', true);
					}
				}
			});
		}

		checkCheck();
		
		var loadSmsSearchData = function(id) {
			var searchName = $("#searchName_id").val();
			//alert(searchName);
			if((id!='')&&(id!=undefined)) {
				searchName = id;
			}	
			var paraMap={searchName:searchName};
			var url="smsServiceMarket!smsBuyerSearchSelectPage.action";
			$.post(url,paraMap,
				function(data){
					$("#sear_name").html(data);
				} 
		   ); 
		}
		
		
		
		// 选择用户
		var checkedUser = function() {
			var curCheck = $('.cur_check'),
				allCheck = $('#all_page'),
				checkMarkA = $('.check_mark_a'),
				checkMarkB = $('.check_mark_b'),
				tableCheckbox = $('.table tbody .input_checkbox');
			
			//区分页面CHECKBOX的数量
			var allCount = $("#allCount_").val();  //所有数量
			var pageCount = $("#pageCount_").val();  //当前页数量
			var checkNum = $("#checked_number");
			var pageChecked = $("#pageChecked_");
			var customerId = $("#customerId_");
			
			if(pageChecked.val()=='1') {
				checkMarkA.prop('checked', true);
				checkNum.text(pageCount);
			}
			if(customerId.val()=='all') {
				checkMarkB.prop('checked', true);
				checkNum.text(allCount);
			}
			
			// 勾选当前页
			curCheck.change(function() {
				var isAllPageCheck = $('#all_page').prop('checked');
				var propChecked = $(this).prop('checked');			
				if (propChecked) {			// 如果勾选中
					checkMarkA.prop('checked', true);
					if(isAllPageCheck) {
						checkNum.text(allCount);
					}else {
						checkNum.text(pageCount);
					}
					pageChecked.val(1);
				} else {					// 如果不勾选
					checkMarkA.prop('checked', false);
					if(isAllPageCheck) {
						checkNum.text(allCount);
					}else {
						checkNum.text(0);
					}
					pageChecked.val(0);
				}
			});
			
			// 勾选所有页
			allCheck.change(function() {
				var propChecked = $(this).prop('checked');
				if (propChecked) {
					checkNum.text(allCount);
					checkMarkB.prop('checked', true);
					pageChecked.val(1);
					customerId.val("all");
				} else {
					checkNum.text(0);
					checkMarkB.prop('checked', false);
					pageChecked.val(0);
					customerId.val("");
				}
			});
			
			// 勾选单个
			tableCheckbox.change(function() {
				var propChecked = $(this).prop('checked');
				var cNumber = checkNum.text();  //判断页面选择数量
				if(cNumber=='') {
					cNumber = 0;
				}
				if (!propChecked) {
					if(parseInt(cNumber)>parseInt(pageCount)) {
						cNumber = pageCount 
					}	
					cNumber--;
					pageChecked.val(0);
					customerId.val("");
					curCheck.prop('checked', false);
					allCheck.prop('checked', false);
				}else {		
					cNumber++;
				}
				var isAllPageCheck = $('#all_page').prop('checked');
				if(!isAllPageCheck) {
					$("#checked_number").text(cNumber);
				}	
			});
		};
		
		return {
			init: function() {
				form();
				checkedUser();
				loadSmsSearchData();
			}
		}
	})()
	
	markHome2.init();
})