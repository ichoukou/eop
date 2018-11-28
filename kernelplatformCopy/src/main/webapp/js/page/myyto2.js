$(function() {
	var myYto2 = (function() {
		var winParams = window.params || {};

		// 配置
		var config = {
			chartAction: winParams.chartAction || ''					// 图表数据请求
		};
		
		// 导航
		var nav = function() {
			// 鼠标移至主菜单显示/隐藏子菜单
			$('#menu_level_1 li').mouseenter(function() {
				var _this = $(this),
					left = _this.position().left + 10 - 15,
					dataMenu = _this.data('menu'),
					thisClass = '.' + dataMenu;
				$('.menu_level_2').slideUp();
				$(thisClass).css('left', left).slideDown();
			});
			$('.menu_level_2').mouseleave(function() {
				$(this).slideUp();
			});
			$('#main_nav_l').mouseleave(function() {
				$('.menu_level_2').slideUp();
			});
			
			
			// 为子菜单添加 class
			$('.menu_level_2').each(function() {
				var _this = $(this);
				$('li', _this).first().addClass('submenu_top');
				$('li', _this).last().addClass('submenu_btm');
			});
			
			$('#sub_nav li').first().addClass('nav_menu_first');
			
			$('a', $('.cur_sub_menu').next()).css('border-left-color', '#265E91');
		};
		
		// 轮播图
		var slide = function() {
			$('#rec_trigger li a').click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					index = _this.parent().index();
					
				$('#rec_panel li').eq(index).fadeIn().siblings().fadeOut();
				_this.parent().addClass('cur_trigger').siblings().removeClass('cur_trigger');
			});
		};
		
		var viewMsgAndQue = function(){

			//首页右侧中部消息和问题件链接。
			$('#num_show ul li a:lt(2)').live("click",function(ev) {
				ev.preventDefault();
				var _parent = $(this).parent();
				//查看未读问题件
				var url = "#";
				if(_parent.attr("id") == 'unReadQuestionNum_li'){
//					var num = $.trim($(this).find("span").text());
//					if(num!=0)
						url = "questionnaire_unReadList.action?isRead=0&currentPage=1&menuFlag=chajian_question";
				}
				else if(_parent.attr("id") == 'unReadMessageNum_li'){
//					var num = $.trim($(this).find("span").text());
//					if(num!=0)
						url = "message_index.action?menuFlag=msg_index";
				}
				else if(_parent.attr("id") == 'plantKefu'){
					url = "user!toSubAccountList.action?menuFlag=user_sub_acc_list";
				}
				else if(_parent.attr("id") == 'plantYewu'){
					url = "user!toPlatformSubAccountList.action?menuFlag=user_platf_acc_list";
				}
				if(url != '#')
					window.location.href=url;
			});
		}
		
		// 右侧 查件/客服/反馈
		var guide = function() {
			$('#guide_v2').hover(
				function() {
					$(this).css('right', 0);
				},
				function() {
					$(this).css('right', -220);
				}
			);
		};
		
		var els = {
				pieChartSel : $("#pie_sel"),
				pieChartSelPingTai : $("#line_sel2"),
				lineSite:$(".site_line"),
				chartSelFencang : $("#line_selIsFencang")
		};
		
		//卖家：日期的选择
		els.pieChartSel.change(function(){
			chart();
		});
		//平台：日期的选择
		els.pieChartSelPingTai.change(function(){
			if($("#line_sel2").attr("name")=='someDay'){
				chart();
			}
		});
		//平台：分仓的选择
		els.chartSelFencang.change(function(){
			chart();
		});
		//网点
		els.lineSite.change(function(){
			if($("#line_sel").attr("name")=='timeLimit'){
				chart();
			}
			
		});
		
		// 饼图
		var chart = function() {
			var content = '';
			if(config.chartAction == 'viewPiePingTai.action?'){//平台账号，平台客服账号
				content = config.chartAction + 'someDay='+els.pieChartSelPingTai.val() + '&vipName='+els.chartSelFencang.val(); 
			}else if(config.chartAction == 'viewTendency.action?timeLimit='){//网点
				content = config.chartAction+els.lineSite.val();
			}else{//卖家
				content = config.chartAction+els.pieChartSel.val();
			}
			$.ajax({
				url: content,
				cache: false,
				dataType: 'json',
				success: function(response) {
					var boxChart;
					var chartData = eval("("+response.infoContent+")");
					var callbackType = chartData.type,
						callbackData = chartData.data;
					
					
					// 如果数据都为 0
					if (callbackType == 'pie') {
						// 如果数据都为 0
						if (callbackData[0][1] == 0 && callbackData[1][1] == 0 && callbackData[2][1] == 0 && callbackData[3][1] == 0) {
							$('#pie_chart').html('<h4 id="no_data">暂无数据</h4>');
						} else {
							var datas = [];
							$.each(callbackData, function(i,d){
								var color = '';
								if(i === 0){
									color = '#4572ab';
								}
								else if(i === 1){
									color = '#a94841';
								}
								else if(i === 2){
									color = '#836b98';
								}
								else if(i === 3){
									color = '#88a749';
								}
								
								datas.push({
									name: d[0],
									color: color,
									y: d[1]
								});
							});
							
							boxChart = new Highcharts.Chart({
								chart: {
									renderTo: 'pie_chart',
									plotBackgroundColor: null,
									plotBorderWidth: null,
									plotShadow: false
								},
								title: {
									text: null
								},
								tooltip: {
									formatter: function() {
										return this.point.name +': '+ this.percentage.toFixed(2) +' %';
									}
								},
								plotOptions: {
									pie: {
										allowPointSelect: true,
										cursor: 'default',
										dataLabels: {
											enabled: true,		//饼图每块的百分比在图形指明显示
											//enabled: false,         //饼图每块的百分比在图形指明不显示
											color: '#000000',
											connectorColor: '#000000',
											formatter: function() {
												return this.point.name +': '+ this.percentage.toFixed(2) +' %';
											}
										},
										showInLegend: true
									}
								},
								legend: {
									layout: 'vertical',
									align: 'right',
									verticalAlign: 'center',
									x: -10,
									y: 100,
									borderWidth: 1
								},
								series: [{
									type: callbackType,
									data : datas//callbackData
								}]
							});
							$('#pie_chart').children().css('overflow','visible');
						}
					} else if (callbackType == 'line') {
						var numArr = [],
							callbackY = callbackData.y;
						for (var i=0, l=callbackY.length; i<l; i++) {
							numArr.push(parseInt(callbackY[i], 10));
						}
						if(callbackData.z == 0){
							$('#line_chart').html('<h4 id="no_data">暂无数据</h4>')
						}else{
							var newCallbackDataX = [],
								newToolTipX = [];
								
							for (var i=0, l=callbackData.x.length; i<l; i++) {
								callbackData.x[i] = callbackData.x[i].replace('月', '');
								callbackData.x[i] = callbackData.x[i].split('-');
								newCallbackDataX.push(callbackData.x[i][1] + '-' + callbackData.x[i][0]);
							}
							
							//for (var i=0, l=callbackData.x.length; i<l; i++) {
							//	callbackData.x[i] = callbackData.x[i].replace('月', '');
							//	callbackData.x[i] = callbackData.x[i].split('-');
							//	newCallbackDataX.push(callbackData.x[i][1] + '-' + callbackData.x[i][0]);
							//}
							
							boxChart = new Highcharts.Chart({
								chart: {
									renderTo: 'line_chart',
									type: 'line'
								},
								title: {
									text: callbackData.text
								},
								xAxis: {
									categories: newCallbackDataX
								},
								yAxis: {
									title: {
										text: '运单量'
									},
									plotLines: [{
										value: 0,
										width: 1,
										color: '#808080'
									}]
								},
								legend: {
									enabled: false
								},
								tooltip: {
									formatter: function() {
										var dateFormatStr = '',
											dateFormatArr = [];
										
										dateFormatArr = this.x.split('-');
										dateFormatStr = dateFormatArr[0] + '月' + dateFormatArr[1] + '日';
										
										return '<b>'+ dateFormatStr +'</b>'+ this.y + '件';
									}
								},
								series: [{
									data: numArr
								}]
							});
							
						}
						
						
					}
					
				}
			});

		};
		
		return {
			init: function() {
				//nav();
				slide();
				//guide();
				viewMsgAndQue();
				// 管理员不执行图表
				if (window.params && window.params.userType != 3) {
					chart();
				}
				
			}
		}
	})();
	
	myYto2.init();
})