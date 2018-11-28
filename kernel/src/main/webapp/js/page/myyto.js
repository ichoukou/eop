/**
 * 我的易通
**/
$(function() {
	var dialog = new Dialog();
	var myYto = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {},
			winPath = window.path || {};

		// 配置
		var config = {
			userType:winParams.userType || '',
			custCodeAction: winParams.custCodeAction,        // 新增客户Action
			site:winParams.site || '',
			getCodeUrl: winParams.getCodeUrl,                // 生成客户编码 url
			delCodeUrl: winParams.delCodeUrl,                // 删除客户编码 url
			unbindClientUrl: winParams.unbindClientUrl       // 未绑定页面 url
		};
		
		// 提示文案
		var tipsMsg = {
			custEmpty: "客户名称不得为空",
			custFormat: "客户名称格式有误"
		};
		
		// 显示提示
		var showIcon = {
			correct: function(el, text) {
				el.html('<span>' + text + '</span>');
			},
			error: function(el, text) {
				el.html('<span class="yto_onError">' + text + '</span>');
			}
		};
		
		// 新增客户
		$('#create_cus_code').click(function(ev){
			ev.preventDefault();
			
			var contentHtml = '<div id="add_cust">' +
				              '   <form action=' + config.custCodeAction + ' id="add_cust_form">' +
				              '      <div class="info">' +
			                  '         <label for="cust_name">客户名称：</label>' +
			                  '         <input type="text" id="cust_name" class="input_text" />' +
			                  '         <a title="生成客户编码" class="btn btn_a" id="build_btn" href="javascript:;"><span>生成客户编码</span></a>' +
			                  '         <span id="cust_nameTip"></span>' +
			                  '      </div>' +
			                  '      <div class="result">' +
			                  //'         <div class="res_code" id="clip_container"><em id="clip_btn">YTO1111111</em>' + '记下编码，把它给您的客户绑定</div>' +
			                  '      </div>' +
			                  '      <input type="hidden" id="cust_code" value="" />' +
			                  '      <input type="hidden" id="cust_id" value="" />' +
			                  '   </form>' +
			                  '</div>';
			
			var aDialog = new Dialog();
			aDialog.init({
				contentHtml: contentHtml,
				closeBtn: false,
				yes: function() {
					var _code = els.custCode.val();
					if(_code !== ''){
						aDialog.close();
						window.location.href = config.unbindClientUrl;
					}
				},
				no: function() {
					
					var _code = els.custCode.val();
					if(_code !== ''){
						$.ajax({
							type: 'POST',
							url: config.delCodeUrl,
							cache: false,
							data: ({
								"userThreadId" : els.custId.val()
							}),
							success: function(response) {
								if(response.status){
									var bDialog = new Dialog();
									bDialog.init({
										contentHtml: '刚刚生成的客户编码已被删除',
										closeBtn: true
									});
								}
							}
						});
					}
					aDialog.close();
				},
				yesVal: '确认',
				noVal: '取消'
			});
			
			// 元素集合
			var els = {
				custName: $('#cust_name'),                                  // 客户名称
				custNameTip: $('#cust_nameTip'),                            // 客户名称提示文案
				buildBtn: $('#build_btn'),                                  // 生成客户编码
				resultDiv: $('#add_cust_form .result'),                     // 客户编码显示
				custCode: $('#cust_code'),
				custId: $('#cust_id'),										// 直客id hidden
				saveBtn: $('a:eq(0)', $('#add_cust').parent().next())       // 确认 button
			};
			
			els.saveBtn.removeClass('btn_d').addClass('btn_e disable');
			
			// 客户名称验证
			els.custName.blur(function(){
				
				var _val = $(this).val(),
				    reg = (regexEnum && regexEnum.accountId) || "^.{1,100}$";
				reg = new RegExp(reg, 'g');
				showIcon.correct(els.custNameTip, '');
				
				if(_val === ''){
					showIcon.error(els.custNameTip, tipsMsg.custEmpty);
				}
				else if(!reg.test(_val)){
					showIcon.error(els.custNameTip, tipsMsg.custFormat);
				}
			});
			
			els.custName.change(function(){
				els.resultDiv.html('');
				els.custCode.val('');
				els.custId.val('');
				els.saveBtn.removeClass('btn_d').addClass('btn_e disable');
			});
			
			els.buildBtn.click(function(ev){
				ev.preventDefault();
				
				els.custName.trigger("blur");
				
				var flag = (els.custNameTip.text().length === 0);
				
				if(flag){
					els.custName.attr('disabled','disabled');
					
					var _name = els.custName.val();
					
					$.ajax({
						type: 'POST',
						url: config.getCodeUrl,
						cache: false,
						data: ({
							"user.site":config.site,
							"user.userCode":"autoGen",
							"user.userName" : _name
						}),
						success: function(response) {
							if(response.status){
								
								els.buildBtn.unbind("click");
								els.buildBtn.removeClass('btn_a').addClass('btn_e disable');
								
								var _code = response.infoContent;
								var _custId = response.targetUrl;
								els.resultDiv.html('<div class="res_code" id="clip_container"><em title="单击可复制" id="clip_btn">' + _code + '</em>' + '记下编码，把它给您的客户绑定</div>');
								els.custCode.val(_code);
								els.custId.val(_custId);
								els.saveBtn.removeClass('btn_e disable').addClass('btn_d');
								
								var $clip_btn = $('.res_code #clip_btn', els.resultDiv);
						            //left = $clip_btn[0].offsetLeft,
							        //top = $clip_btn[0].offsetTop;
					  	
								var clip = new ZeroClipboard.Client();
								//clip.setHandCursor(true);
								clip.setText(_code);
					  	
								clip.addEventListener('complete', function (client, text) {
									//debugstr("Copied text to clipboard: " + text );
									alert("客户编码已经复制，你可以使用Ctrl+V 粘贴。");
								});
					  	
								clip.glue('clip_btn', 'clip_container');
					  	
								var clip_swf = $clip_btn.next('div');
									clip_swf.css({top:'0', left:'0', cursor:'pointer'});
									clip_swf.attr("title", "单击可复制");
							}
							else {
								showIcon.error(els.custNameTip, response.infoContent);
								els.resultDiv.html('');
								els.custCode.val('');
								els.custId.val('');
								els.saveBtn.removeClass('btn_d').addClass('btn_e disable');
							} 
						},
						complete: function(){
							if(!els.buildBtn.hasClass('disable')){
								els.custName.removeAttr("disabled");
							}
						}
					});
				}
			});
		});
		
		var defaultTxt = function() {
			$('#home_sear .input_text').defaultTxt('输入运单号/手机号码/买家姓名');
		};
		
		var slide = function() {
			var slideConfig = {
				imgInterval: 3000,	// 初使化间隔时间
				index:0,			// 焦点所在索引值
				goSwitch: true		// 鼠标悬停时切换状态
			};
			
			/**
			 * 图片切换
			 * @param {Number} index 目标图片的索引值
			**/
			var imgSwitch = function(index) {
				var slideTarget = $('#slide'),
					panelLis = $('#slide_panel li', slideTarget),
					triggerLis = $('#slide_trigger li', slideTarget),
					curClass = 'cur_trigger';

				panelLis.eq(index).fadeIn().siblings().fadeOut();
				triggerLis.eq(index).addClass(curClass).siblings().removeClass(curClass);
				
				// 鼠标移入移出时切换状态
				slideTarget.hover(
					function() {
						slideConfig.goSwitch = false;
					},
					function() {
						slideConfig.goSwitch = true;
					}
				);
			};
			
			/**
			 * 自动切换
			**/
			var autoSwitch = function() {
				var slideTarget = $('#slide'),
					panelLis = $('#slide_panel li', slideTarget),
					imgDataLen = panelLis.length;
				
				// 遍历触发器
				slideConfig.index = $('.cur_trigger', slideTarget).index();
				
				setInterval(function() {
					if (slideConfig.goSwitch) {
						if (slideConfig.index == imgDataLen - 1) {
							slideConfig.index = -1;
						}
						imgSwitch(slideConfig.index + 1);
						slideConfig.index ++;
					}
					
				}, slideConfig.imgInterval);
			};
			
			
			// 初使化 Slide
			imgSwitch(0);
			
			// 点击切换 Slide
			var triggerLiA = $('#slide_trigger li a');
			
			triggerLiA.mouseover(function(ev) {
				var index = $(ev.target).parent().index();
				imgSwitch(index);
			});
			
			// 自动切换
			autoSwitch();
		};
		
		// 表单
		var form = function() {
			// 元素集合
			var els = {
				searForm: $('#sear_form'),
				searText: $('#home_sear_input'),
				searBtn: $('#home_sear_btn'),
				searCheck: $('#isCheck'),
				searFlag: $('#flag'),
				currentPage: $('#currentPage'),
				searBtn2: $('#sear_btn2'),  									//查询签收报表button
				searBtn3: $('#sear_btn3'),  									//查询签收报表button
				reportType: $('#report_type'),
				startDate: $('#d4311'),
				endDate: $('#d4312'),
				startDate2: $('#d4313'),
				endDate2: $('#d4314')
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
			
			// 初始化表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 智能查件
			els.searText.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ''}).
				functionValidator({fun: function(val, el) {
					var defaultText = '输入运单号/手机号码/买家姓名',		// 输入框默认值
					errMsg,
					output;		// 返回结果
					if (val == defaultText) {					// 如果是默认值
						//setTimeout(function(){showIcon.error($("#sear_textTip"), "请输入查询条件");},5);
						//output = false;
					} else if (dataType.isGroup(val)) {					// 如果是运单组合
						if (val.substr(-1, 1) == '/') {		// 如果最后一个字符是 "/"
							// 移除最后一个 "/"
							val = val.substring(0, val.length-1);
						}
						// 拆分成数组
						var arr = [];
						arr = val.split('/');
						
						for (var i=0, len = arr.length; i<len; i++) {
							if (!dataType.isShipNum(arr[i])) {
								output = false;
								setTimeout(function(){showIcon.error($("#sear_textTip"), "请输入正确的查询条件");},5);
								break;
							}
						}
						
					} else {
						if (
							dataType.isMobile(val) ||
							dataType.isShipNum(val) ||
							dataType.isTel(val) ||
							dataType.isName(val)
						) {
							output = true;
						} else {
							output = false;
							setTimeout(function(){showIcon.error($("#sear_textTip"), "请输入正确的查询条件");},5);
						}
					}
					
					
					return output;
				}});
				
			// 点击“查询”按钮
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				var logisticsIds = $.trim(els.searText.val());
				if(logisticsIds == '输入运单号/手机号码/买家姓名'){
					window.location.href = "waybill_bill.action?menuFlag=home_waybill";
					return false;
				}
				
			    if(isLogisticsId(logisticsIds)||logisticsIds.indexOf('/')>0){
			    	els.searCheck.val('0');
			    	els.searFlag.val('1');
			    	els.currentPage.val('1');
			    	els.searForm.attr("action","waybill_homeQuery.action");
			    }
				else{
					var buyerName='';
					var buyerPhone='';
					if(isDigit(logisticsIds))buyerPhone=logisticsIds;
					else buyerName=logisticsIds;
					
					els.searCheck.val('0');
			    	els.searFlag.val('3');
			    	els.currentPage.val('1');
					els.searForm.attr("action","waybill_homeQuery.action");
				}
				els.searForm.trigger('submit');
			});
		
		// 点击“签收查询”
		els.searBtn2.click(function(ev) {	
			$.ajax({
				url: 'searchReport!searchReportView.action',
				cache: false,
				type: 'GET',
				data: {
					reportType: 1,//els.reportType.val(),
					startDate: els.startDate.val(),
					endDate: els.endDate.val()
				},
				success: function(data) {
					var report = data.report;
					var reports = data.report.reports;	
					var categories = [];
					var data = [];
					var remarks = {};
					var theCategorie;
					var theData;
					var remark;
					for(i=0;i<reports.length;i++) {
						theCategorie = reports[i].sendStartTime;
						theData = reports[i].ratio;
						remark = reports[i].remark1;
						categories.push(theCategorie);
						data.push(theData*100);
						//remarks.push(remark);
						//remarks.theData=remark;
						remarks[theCategorie] = remark;
					}	
					//var categories = ['5月1号', '5月2号', '5月3号', '5月4号', '5月5号', '5月6号', '5月7号'];
					//var data = [0.0, 6.9, 9.5, 14.5, 18.4, 21.5, 99];
					lineShape(categories,data,remarks);
				}
			});
		});
		
		// 点击“签收查询”
		//els.searBtn.click(function(ev) {	
		//	$("#pie_chart").attr("src","reportPieShape.action");
		//});
		
		// 点击“签收查询”
		els.searBtn3.click(function(ev) {	
			$.ajax({
				url: 'searchReport!searchReportView.action',
				cache: false,
				type: 'GET',
				data: {
					reportType: 2,//els.reportType.val(),
					startDate: els.startDate2.val(),
					endDate: els.endDate2.val()
				},
				success: function(data) {
					var report = data.report;
					var reports = data.report.reports;	
					var data = [];
					var remarks = {};
					var hoursType;
					var hoursKind;
					var number;
					var hoursRatio;
					var remark;
					for(i=0;i<reports.length;i++) {
						var data1 = [];
						hoursType = reports[i].hoursType;
						number = reports[i].number;
						hoursRatio = reports[i].hoursRatio;
						remark = reports[i].remark2;
						if(hoursType==1) {
							hoursKind = '24小时到达';
						}
						if(hoursType==2) {
							hoursKind = '48小时到达';
						}
						if(hoursType==3) {
							hoursKind = '72小时到达';
						}
						data1.push(hoursKind);
						data1.push(hoursRatio*100);
						data.push(data1);
						remarks[hoursKind] = remark;
					}	
					if(reports.length==0) {
						var data1 = [];
						hoursKind = "查无数据";
						data1.push(hoursKind);
						data1.push(1*100);
						data.push(data1);
						remarks[hoursKind] = "根据日期条件未能查询得出数据";
					}
					//alert(data);
					//var data = [['24小时到达',30.0],['48小时到达',30.0],['72小时到达', 40.0]];
					pieShape(report.title,data,remarks);
				}
			});
		});	
	};
	
	var lineShape = function(categories,data,remarks) {
	    var chart;
	        chart = new Highcharts.Chart({
	            chart: {
	                renderTo: 'container',
	                type: 'line'
	            },
	            title: {
	            	 style: {
	                     color: '#333',
	                     fontWeight: 'bold',
	                     fontSize: '20px',
	                     fontFamily: 'Trebuchet MS, Verdana, sans-serif'
	                  },
	                text: '签收完成进度'
	            },
	            subtitle: {
	                text: ''
	            },
	            xAxis: {
	                categories:categories// ['5月1号', '5月2号', '5月3号', '5月4号', '5月5号', '5月6号', '5月7号']
	            },
	            yAxis: {
	                title: {
	                    text: '百分比'
	                },
	           		lineWidth: 1,min:0,max:100,
	           		labels: {
	           			formatter: function() {
		                    return this.value +'%';
		                },
		                style: {
		                	 color: '#333',
		                     fontWeight: 'bold',
		                     fontSize: '10px'
		                }
	           		}
	            },
	            tooltip: {
	                enabled: true,
	                formatter: function() {
	                	var key = this.x;
	                    return '<b>'+ this.series.name +'</b><br/>'+
	                        this.x +': '+ this.y +'%' + '<br/>备注:' + remarks[key];
	                }
	            },
	            legend: {
	    			layout: 'vertical',
	    			align: 'right',
	    			verticalAlign: 'top',
	    			x: -10,
	    			y: 200,
	    			borderWidth: 0
	    		},
	            plotOptions: {
	                line: {
	                    dataLabels: {
	                        enabled: true,
	                        formatter: function() {
								return '<b>'+ this.y +'%';
							}
	                    },
	                    enableMouseTracking: true
	                }
	            },
	            series: [{
	                name: '签收完成进度',
	                data: data//[0.0, 6.9, 9.5, 14.5, 18.4, 21.5, 99]
	            }]
	        });
	}
	

	var pieShape = function(title,data,remarks) {
		var chart;
		chart = new Highcharts.Chart({
			chart: {
				renderTo: 'container2',
				plotBackgroundColor: null,
				plotBorderWidth: null,
				plotShadow: false
			},
			title: {
				style: {
                     color: '#333',
                     fontWeight: 'bold',
                     fontSize: '20px',
                     fontFamily: 'Trebuchet MS, Verdana, sans-serif'
                },
				text: title
			},
			tooltip: {
				formatter: function() {
					var key = this.point.name;
					return '<b>'+ this.point.name +'</b>: '+ this.percentage +' %'
					+ '<br/>备注:' + remarks[key];
				}
			},
			plotOptions: {
				pie: {
					allowPointSelect: true,
					cursor: 'pointer',
					dataLabels: {
						enabled: true,
						color: '#000000',
						connectorColor: '#000000',
						formatter: function() {
							return '<b>'+ this.point.name +'</b>: '+ this.percentage +' %';
						}
					},
					showInLegend: true
				}
			},
			series: [{
				type: 'pie',
				name: 'Browser share',
				data:data
			}]
		});
	 }
		
		//网点第一次登录弹出层
		var showPrompt = function(){
			if(config.userType == 2){
				var msgHtml = "<!-- 弹出框内容start -->"
					+'<div id="prompt">'
					+'<img src="images/do_what_z.gif" alt="我要做什么" class="gui_img"/>'
					+'	<div class="guide">'
					+'		<ol>'
					+'			<li class="guide_txt_bg">通知卖家绑定网点</li>'
					+'			<li>使用易通</li>'
					+'		</ol>'
					+'	</div>'
					+'	<h5 class="title_a">卖家与网点绑定的方法</h5>'
					+'	<p class="method_p">a、网点在金刚创建客户编码：登入金刚-->系统管理-->数据管理-->组织机构管理-->客户管理-->点击新增→填写客户名称，选择客户类型为电子商务客户-->保存-->自动生成客户编码</p>'
					+'	<p class="method_p">b、告知卖家使用易通并提供客户编码：输入ec.yto.net.cn-->淘宝用户登录-->输入淘宝账号授权-->进入易通-->绑定网点-->输入客户编码-->绑定网点</p>'
					+'	<p class="method_p">c、易通不改变您的淘宝客户下单流程，也无需在易通重复下单。'
					+'	<p class="method_p">'
					+'	<label for="promptCheck"><input type="checkbox" id="promptCheck" />下次登录不再提醒</label>'
					+'	</p>'
					+'</div>'
					+'<!-- 弹出框内容end -->'
					+'';
				
				$.ajax({
					type:"post",
					dataType:"json",
					cache:false,
					url:"user!checkSitePrompt.action",
					success:function(response){
						if(!response.status){
							var oDialog = new Dialog();
							oDialog.init({
								contentHtml: msgHtml,
								yesVal:"我知道了",
								yes: function() {
									if($("#promptCheck").prop("checked")){
										$.ajax({
											type:"post",
											dataType:"json",
											cache:false,
											url:"user!rememberSitePrompt.action",
											success:function(res){
											}
										});
									}
									oDialog.close();
								}
							});
						}
					}
				});
			}
		}
		
		//轮播图(卖家)
		var someDay = $("#someDay").val();
		if(someDay!=undefined){
			$("#chart").attr("src","viewPie.action?someDay="+someDay);
		}
		
		$('#someDay').change(function(){
			var val = $("#someDay").val();
			$("#chart").attr("src","viewPie.action?someDay="+val);
		});
		
		
		//轮播图(（网点）
		var timeLimit = $("#timeLimit").val();
		if(timeLimit!=undefined){
			$("#chart").attr("src","viewTendency.action?timeLimit="+timeLimit);
		}
		
		$('#timeLimit').change(function(){
			var val = $("#timeLimit").val();
			$("#chart").attr("src","viewTendency.action?timeLimit="+val);
		});
		
		
		//轮播图(平台)
		var vipName = $("#vipName").val();
		if(vipName!=undefined){
			$("#chart").attr("src","pingTaiViewPie.action?someDay="+someDay+"&vipName="+vipName);
		}
		
		$('.pingtaiChange').change(function(){
			var someDay = $("#someDay").val();
			var vipName = $("#vipName").val();
			
			$("#chart").attr("src","pingTaiViewPie.action?someDay="+someDay+"&vipName="+vipName);
		});
		//计算轮播的单个li的宽度
		var rec_triggerLen=$("#rec_trigger li").length;
		if(rec_triggerLen==3){
			$("#rec_trigger li").addClass("three");
		}else if(rec_triggerLen==2){
			$("#rec_trigger li").addClass("two");
		}else if(rec_triggerLen==1){
			$("#rec_trigger li").addClass("one");
		}
		
		//运单号验证
		var isLogisticsId = function(s){
			var patrn=/^(0|1|2|3|4|5|6|7|8|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/;
			if (!patrn.exec(s)) return false
			return true
		};
		
		var isDigit = function(s){
			var patrn=/^[0-9]{1,20}$/;
			 if (!patrn.exec(s)) return false
			 return true
		};
		
		return {
			init: function() {
				defaultTxt();
				slide();
				form();
				showPrompt();
				window.ytoTab && ytoTab.init();
			}
		}
	})();
	
	myYto.init();
});

function noneAccount(){
	var oDialog = new Dialog();
	oDialog.init({
		cache:false,
		contentHtml: "您的网点关闭了您的电子对账！",
		yes: function() {
			oDialog.close();
		}
	});
}