$(function() {
	var markHome3 = (function() {
		var winParams = window.params || {};

		// 全局配置
		var config = {
			backAction: winParams.backAction || '',				// 返回上一步
			sendAction: winParams.sendAction || 'smsServiceMarket!sendPorts.action'				// 立即发送
		};
		
		// 元素集合
		var els = {
			member: $('#member'),
			tradeAmountA: $('#tradeAmountA'),
			tradeAmountB: $('#tradeAmountB'),
			tradeCountA: $('#tradeCountA'),
			tradeCountB: $('#tradeCountB'),
			saveSearInput: $('#saveSearInput'),
			searName: $('#searName'),
			tradeTime: $('#tradeTime'),
			lastTrade: $('#lastTrade'),
			atProvince: $('#atprovince'),
			atCity: $('#atcity'),
			atArea: $('#atarea'),
			provinceName: $('#provinceName_'),
			cityName: $('#cityName_'),
			areaName: $('#areaName_'),
			memLevel: $('#memLevel'),
			customerId: $('#customerId'),
			templateId: $('#templateId')
		};
		
		// 返回上一步
		var goBack = function() {
			$('#back_btn').click(function(ev) {
				ev.preventDefault();
				
				prevStep();
				/*
				$.ajax({
					url: config.backAction,
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
						
						templateId: els.templateId.val(),
						customerId: els.customerId.val()
					},
					success: function() {
						window.location = 'smsServiceMarket!smsMarketPageStep2.action?step=2&searchName=44&templateId='+els.templateId.val();
					}
				})
				*/
			})

		};
		
		// 上一步
		var prevStep = function() {
			$('#mark_form3').attr("action","smsServiceMarket!smsMarketPageStep2.action");
			$('#mark_form3').trigger('submit');
		};
		
		// 立即发送
		var sendNow = function() {
			$('#send_btn').click(function(ev) {;
				var txt = "正在发送中...";
				if($("#sendText_").text()==txt) {
					return;
				}
				ev.preventDefault();
				var ajaxData = {};
				if (els.customerId.val() == 'all') {		// 如果勾选所有用户
					ajaxData = {
						searchName: els.searName.val(),
						theLastTradeTime: els.tradeTime.val(),
						theLastMarketTime: els.lastTrade.val(),
						receiverProvince: els.atProvince.val(),
						city: els.atCity.val(),
						area: els.atArea.val(),
						provinceName: els.provinceName.val(),
						cityName: els.cityName.val(),
						areaName: els.areaName.val(),
						userGrade: els.memLevel.val(),
						searchInput: els.member.val(),
						tradeCountMin: els.tradeAmountA.val(),
						tradeCountMax: els.tradeAmountB.val(),
						tradeAmountMin: els.tradeCountA.val(),
						tradeAmountMax: els.tradeCountB.val(),
						templateId: els.templateId.val(),
						customerId: els.customerId.val()						
					}
				} else {									// 如果没有勾选所有用户
					ajaxData = {
						templateId: els.templateId.val(),
						customerId: els.customerId.val()
					}
				}
				
				$.ajax({
					url: config.sendAction,
					data: ajaxData,
					cache: false,
					type: 'POST',
					dataType: "json",
					success: function(data) {
						/*$("#send_btn").removeClass("btn btn_a");
						$("#send_btn").addClass("btn btn_a disable");
						$("#sendText_").text(txt);*/
						
						var msg = '亲，你的短信已提交等待发送中，发送需要一段时间，发送记录请稍后至短信记录查询。';
						var succDialog = new Dialog();						
						succDialog.init({
							contentHtml: msg,
							iconType: 'question',
							yes: function() {
								succDialog.close();
								window.location.href = "smsServiceMarket!smsMarketPageStep1.action?menuFlag=sms_made";
							},
							no: function() {
								succDialog.close();
								window.location.href = "smsSearch!searchPage.action?menuFlag=sms_info";
							},
							yesVal:'继续创建营销活动',
							noVal:'查看发送短信记录'
							// autoClose: 2000			
						}) 
						//AJAX提示信息,不需要跳转
						//window.location = 'smsServiceMarket!smsMarketPageStep3.action';
					}
				});
			});
		};
		
		return {
			init: function() {
				goBack();
				sendNow();
			}
		}
	})()
	
	markHome3.init();
})