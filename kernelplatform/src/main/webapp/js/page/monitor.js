/**
 * 运单监控
**/
$(function() {
	var monitor = (function() {
		var textDefault = function() {
			if($('#input_text_txt').val()=="")
				$('#input_text_txt').defaultTxt('请输入买家姓名/买家电话/运单号');
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
			$('.th_title').has('.arrow_up').css('cursor', 'pointer').toggle(
					function() {
						$('em', $(this)).removeClass('arrow_up').addClass('arrow_down');
					},
					function() {
						$('em', $(this)).removeClass('arrow_down').addClass('arrow_up');
					}
			);
		};
		
		var selectArea = function() {
			var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#province'],
				autoLink: false
			};
			linkageSel = new LinkageSel(area);
			var xProv = els.xProv.val(), xCity = els.xCity.val(), xDistrict = els.xDistrict.val();
			linkageSel.changeValues([xProv, xCity, xDistrict]);
		};
		
		
		// 全选
		var checked = function() {
			$('#tab_panel_e .checked_all').checkAll( $('#tab_panel_e input[type="checkbox"]') );
		};
		
		// 元素集合
		var els = {
			searForm: $('#sear_form'),
			dateStart: $('#date_start'),
			dateEnd: $('#date_end'),
			dateTip: $('#date_tip'),
			searText: $('#input_text_txt'),
			searTextTip: $('#input_text_txtTip'),//输入条件框
			searBtn: $('#sear_btn'),
			lastestA: $('#lastest_a'),
			currentPage: $('#currentPage'),
			lastestB: $('#lastest_b'),
			xProv: $('#x_prov'),
			xCity: $('#x_city'),
			xDistrict: $('#x_district'),
			lastestC: $('#lastest_c'),
			inputRadio: $('.input_radio'),
			mailNoOrderType: $('#mailNoOrderType'),//运单类型
			tabFlag: $('#tabFlag'),//当前所在tab
			status: $('#status'),//当前所在订单状态
			orderType: $('#orderType'),
			bindUserId: $('#bindUserId'),//单个店铺id
			vipId: $('#vipId'),//平台登录时分仓用户
			walkingOrderClass: $(".zjz"),//走件中tab
			scanOrderClass: $(".psj"),//派件中tab
			successOrderClass: $(".cgd"),//成功订单tab
			cancelOrderClass: $(".thd"),//退货订单tab
			attentionOrderClass: $(".gzd"),//关注订单tab
			tabTriggers: $(".tab_triggers"),
			thead_select: $(".thead_select"),//店铺选择处的class
			cancelAttention: $(".cancelAttention"),//关注tab中取消关注图片class
			autoSkip: $("#autoSkip")//自动切换
		};
		
		// 表单
		var form = function() {
			// 验证初始值
			var dateResult = true,			// 日期的验证结果
				searResult = true;			// 运单号验证结果
			
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
				dateErr: '请选择日期',
				maxDateErr: '只能查近三个月的数据',
				rangeDateErr: '只能查30天内的数据',
				dateEmptyErr: '请选择开始日期',
				searFormatErr: '格式错误，请修改',
				searLongErr: '内容超长'
			};
			
			// 验证日期的方法
			var check = {
				date: function(dateVal) {
					if (dateVal != '') {
						showIcon.correct(els.dateTip, '');
						dateResult = true;
					} else {
						showIcon.error(els.dateTip, tipsMsg.dateErr);
						dateResult = false;
					}
				},
				maxDate: function(dateVal) {
					if (dateVal == '') {
						showIcon.error(els.dateTip, tipsMsg.dateEmptyErr);
						dateResult = false;
					} else {
						// 系统时间
						var sysDate = new Date(),
							sysDateY = parseInt(sysDate.getFullYear(), 10),
							sysDateM = parseInt(sysDate.getMonth(), 10) + 1,
							sysDateD = parseInt(sysDate.getDate(), 10),
							sysUTC = Date.UTC(sysDateY, sysDateM, sysDateD);
						// 用户选择时间
						var formatDate = dateVal.split('-'),
							formatDateY = parseInt(formatDate[0], 10),			// 年
							formatDateM = parseInt(formatDate[1], 10),			// 月
							formatDateD = parseInt(formatDate[2], 10),			// 日
							formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
						// 用户选择时间离系统时间的天数
						var dateGap = parseInt((sysUTC - formatUTC) / 1000 / 60 / 60 / 24, 10);
						// 如果超过前3个月
						if (dateGap > 31*3) {
							showIcon.error(els.dateTip, tipsMsg.maxDateErr);
							dateResult = false;
						} else {
							showIcon.correct(els.dateTip, '');
							dateResult = true;
						}
					}
				},
				rangeDate: function(dateVal) {
					var startVal = els.dateStart.val();
					this.maxDate(startVal);
					if (startVal != '') {
						var startDate = startVal.split('-'),
							startDateY = parseInt(startDate[0], 10),			// 年
							startDateM = parseInt(startDate[1], 10),			// 月
							startDateD = parseInt(startDate[2], 10),			// 日
							startUTC = Date.UTC(startDateY, startDateM, startDateD);
							
						var formatDate = dateVal.split('-'),
							formatDateY = parseInt(formatDate[0], 10),			// 年
							formatDateM = parseInt(formatDate[1], 10),			// 月
							formatDateD = parseInt(formatDate[2], 10),			// 日
							formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
							
						var dateGap = parseInt((formatUTC - startUTC) / 1000 / 60 / 60 / 24, 10);

						if (dateGap > 31) {
							showIcon.error(els.dateTip, tipsMsg.rangeDateErr);
							dateResult = false;
						} else {
							showIcon.correct(els.dateTip, '');
							dateResult = true;
						}
					}
				},
				searText: function(searVal) {
				
					var trimVal = searVal.replace(/^\s*/g, "");
					var outputVal = trimVal.replace(/\s*$/g, "");
					
					els.searText.val(outputVal);

					if (outputVal.length > 30) {
						showIcon.correct(els.searTextTip, tipsMsg.searLongErr);
						searResult = false;
					} else if (
						dataType.isMobile(outputVal) ||
						dataType.isShipNum(outputVal) ||
						dataType.isTel(outputVal) ||
						dataType.isName(outputVal)
					) {
						showIcon.correct(els.searTextTip, '');
						searResult = true;
					} else if (outputVal == '') {
						searResult = true;
					} else {
						showIcon.error(els.searTextTip, tipsMsg.searFormatErr);
						searResult = false;
					}
				}
			};
			
			// “查询”表单 不使用验证方法，只是为了加载 yto 的验证皮肤
			$.formValidator.initConfig({
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 查询最近 N 天的数据
			var lastestDays = function(days) {
				var sysDate = new Date(),
					latestDate = new Date(),
					sysDateY = parseInt(latestDate.getFullYear(), 10),
					sysDateM = parseInt(latestDate.getMonth(), 10) + 1,
					sysDateD = parseInt(latestDate.getDate(), 10),
					newtimems = latestDate.getTime() - (days * 24 * 60 * 60 * 1000);
					
				latestDate.setTime(newtimems);
				// 填开始时间
				els.dateStart.val(latestDate.format('yyyy-MM-dd'));
				// 填结束时间
				els.dateEnd.val(sysDate.format('yyyy-MM-dd'));
			};
			
			// 近三天
			els.lastestA.change(function() {
				lastestDays(2);
			});
			
			// 近七天
			els.lastestB.change(function() {
				lastestDays(6);
			});
			
			// 近一个月
			els.lastestC.change(function() {
				lastestDays(29);
			});
			// 验证查询
			els.searText.bind({
				blur: function() {
					var val = $(this).val(),
						defaultText = '请输入买家姓名/买家电话/运单号';
					if (val != defaultText) {
						if(val == ""){
							els.searText.defaultTxt(defaultText);
						} else {
							check.searText(val);
						}
							
					}
				},
				focus: function() {
					// 清空提示
					els.searTextTip.html('');
				}
			});
			
			
			// 日期
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
							
						check.maxDate(dateVal);
						
						
						if (dateResult) {
							els.dateEnd.prop('disabled', false);
							$dp.$('date_end').focus();
						} else {
							_this.blur();
							_this.focus();
							els.dateEnd.prop('disabled', true);
						}
						
						
						els.inputRadio.prop('checked', false)
					},
					startDate: '#F{$dp.$D(\'date_end\')}',
					//minDate: '%y-%M-{%d-31*3}',		// 最小时间：3个月前
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
			els.dateEnd.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择终止日期后触发校验
						var _this = this,
							dateVal = _this.value;
						//check.date(dateVal);
						
						check.rangeDate(dateVal);
						
						_this.blur();
						
						els.inputRadio.prop('checked', false)
					},
					startDate: '#F{$dp.$D(\'date_start\')}',
					minDate: '#F{$dp.$D(\'date_start\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
			
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				var startVal = els.dateStart.val(),
					endVal = els.dateEnd.val(),
					searVal = els.searText.val(),
					defaultText = '请输入买家姓名/买家电话/运单号';
				els.bindUserId.val(0);
				els.currentPage.val(1);
				els.autoSkip.val(1);//自动切换
				check.rangeDate(endVal);
				if(dateResult)
					check.maxDate(startVal);
				//给省市县区赋值
				var selectedArr = linkageSel.getSelectedArr();
				els.xProv.val(selectedArr[0]);
				els.xCity.val(selectedArr[1]);
				els.xDistrict.val(selectedArr[2]);
				if (searVal != defaultText) {
					check.searText(searVal);
				}else{
					els.searText.val("");
				}
				if (dateResult && searResult) {
					if(els.searText){
						var loadingDialog = new Dialog();
						
						loadingDialog.init({
							contentHtml: '加载中...'
						});

						els.searForm.trigger('submit');
					}
				}
			});
			
			//店铺筛选
			els.thead_select.each(function(){
				$(this).children().each(function(){
					$(this).click(function(){
						if($(this).val()==0){
							window.location.href = 'associationAccount_toBindeAccountCustom.action?menuFlag=user_acc_custom';
						}else{
							els.bindUserId.val($(this).val());
							els.currentPage.val(1);
							els.autoSkip.val(0);//不自动切换
							var startVal = els.dateStart.val(),
								endVal = els.dateEnd.val(),
								searVal = els.searText.val(),
								defaultText = '请输入买家姓名/买家电话/运单号';
							check.maxDate(startVal);
							check.rangeDate(endVal);
							//给省市县区赋值
							var selectedArr = linkageSel.getSelectedArr();
							els.xProv.val(selectedArr[0]);
							els.xCity.val(selectedArr[1]);
							els.xDistrict.val(selectedArr[2]);
							if (searVal != defaultText) {
								check.searText(searVal);
							}else{
								els.searText.val("");
							}
							if (dateResult && searResult) {
								if(els.searText)
									els.searForm.trigger('submit');
							}
						}
					});
				});
			});
			
			//tab切换事件
			els.tabTriggers.find("ul").children().each(function(){
				$(this).click(function(){
					var tabFlag = $(this).attr("tabFlag"),status = $(this).attr("status");
					els.tabFlag.val(tabFlag);
					els.status.val(status);
					els.bindUserId.val(0);
					els.currentPage.val(1);
					els.autoSkip.val(0);//不自动切换
					var startVal = els.dateStart.val(),
						endVal = els.dateEnd.val(),
						searVal = els.searText.val(),
						defaultText = '请输入买家姓名/买家电话/运单号';
					check.maxDate(startVal);
					check.rangeDate(endVal);
					//给省市县区赋值
					var selectedArr = linkageSel.getSelectedArr();
					els.xProv.val(selectedArr[0]);
					els.xCity.val(selectedArr[1]);
					els.xDistrict.val(selectedArr[2]);
					if (searVal != defaultText) {
						check.searText(searVal);
					}else{
						els.searText.val("");
					}
					if (dateResult && searResult) {
						if(els.searText) {
							setTimeout(function(){
								var loadingDialog = new Dialog();
								
								loadingDialog.init({
									contentHtml: '加载中...'
								});
								els.searForm.trigger('submit');
							}, 0);
						}
					}
				});
			});
			
			//时间排序
			$(".arrow_down").each(function(){
				$(this).click(function(){
					if($(this).attr("class")=="arrow_down")
						els.orderType.val("1");
					if($(this).attr("class")=="arrow_up")
						els.orderType.val("2");
					var startVal = els.dateStart.val(),
						endVal = els.dateEnd.val(),
						searVal = els.searText.val(),
						defaultText = '请输入买家姓名/买家电话/运单号';
					check.maxDate(startVal);
					check.rangeDate(endVal);
					//给省市县区赋值
					var selectedArr = linkageSel.getSelectedArr();
					els.xProv.val(selectedArr[0]);
					els.xCity.val(selectedArr[1]);
					els.xDistrict.val(selectedArr[2]);
					els.autoSkip.val(0);//不自动切换
					if (searVal != defaultText) {
						check.searText(searVal);
					}else{
						els.searText.val("");
					}
					if (dateResult && searResult) {
						if(els.searText)
							els.searForm.trigger('submit');
					}
				});
			});
			$(".arrow_up").each(function(){
				$(this).click(function(){
					if($(this).attr("class")=="arrow_down")
						els.orderType.val("1");
					if($(this).attr("class")=="arrow_up")
						els.orderType.val("2");
					var startVal = els.dateStart.val(),
						endVal = els.dateEnd.val(),
						searVal = els.searText.val(),
						defaultText = '请输入买家姓名/买家电话/运单号';
					check.maxDate(startVal);
					check.rangeDate(endVal);
					//给省市县区赋值
					var selectedArr = linkageSel.getSelectedArr();
					els.xProv.val(selectedArr[0]);
					els.xCity.val(selectedArr[1]);
					els.xDistrict.val(selectedArr[2]);
					els.autoSkip.val(0);//不自动切换
					if (searVal != defaultText) {
						check.searText(searVal);
					}else{
						els.searText.val("");
					}
					if (dateResult && searResult) {
						if(els.searText)
							els.searForm.trigger('submit');
					}
				});
			});
			
			//关注列表中取消关注
			els.cancelAttention.each(function(){
				$(this).click(function(){
					$.ajax({
				        url : 'attention_delAttention.action',
				        data:{
				            id:$(this).attr("value")
				        },
				        success:function(data){
				        	var searVal = els.searText.val(),
								defaultText = '请输入买家姓名/买家电话/运单号';
							if (searVal != defaultText) {
								check.searText(searVal);
							}else{
								els.searText.val("");
							}
							if (searResult) {
								if(els.searText){
									els.autoSkip.val(0);//不自动切换
									els.searForm.trigger('submit');
								}
							}
				        }
				    });
				});
			});
			
			//批量取消关注
			$(".attention a").click(function(){
				var ids = "";
				$(".input_checkbox:checked").each(function(){
					if($(this).val()!="" && !isNaN($(this).val())){
						ids+=($(this).val()+",");
					}
				});
				if(ids!=""){
					$.ajax({
				        url : 'attention_delAll.action',
				        data:{
				            ids:ids
				        },
				        success:function(data){
				        	var searVal = els.searText.val(),
								defaultText = '请输入买家姓名/买家电话/运单号';
							if (searVal != defaultText) {
								check.searText(searVal);
							}else{
								els.searText.val("");
							}
							if (searResult) {
								if(els.searText){
									els.autoSkip.val(0);//不自动切换
									els.searForm.trigger('submit');
								}
							}
				        }
				    });
				}
			});
			
			//翻页
			pagination.click(function(){
				els.currentPage.val($(this).attr("value"));
				var startVal = els.dateStart.val(),
					endVal = els.dateEnd.val(),
					searVal = els.searText.val(),
					defaultText = '请输入买家姓名/买家电话/运单号';
				check.maxDate(startVal);
				check.rangeDate(endVal);
				//给省市县区赋值
				var selectedArr = linkageSel.getSelectedArr();
				els.xProv.val(selectedArr[0]);
				els.xCity.val(selectedArr[1]);
				els.xDistrict.val(selectedArr[2]);
				if (searVal != defaultText) {
					check.searText(searVal);
				}else{
					els.searText.val("");
				}
				if (dateResult && searResult) {
					if(els.searText){}
					setTimeout(function(){
						var loadingDialog = new Dialog();
						
						loadingDialog.init({
							contentHtml: '加载中...'
						});
						els.searForm.trigger('submit');
					},0);
				}
			});
		};
		
		return {
			init: function() {
				textDefault();
				var tabFlag = $("#tabFlag").val();
				var status = $("#status").val();
				var index = 0;
				if(tabFlag==1 && status==5)
					index = 0;
				else if(tabFlag==1 && status==3)
					index = 1;
				else if(tabFlag==1 && status==2)
					index = 2;
				else if(tabFlag==1 && status==1)
					index = 3;
				else if(tabFlag==2)
					index = 4;
				ytoTab.init(index);
				changeDownUp();
				selectArea();
				checked();
				form();
				//shipNumDetail();
			}
		}
	})();
	var linkageSel;
	monitor.init();
})

function shopManage(){
    window.location="associationAccount_toBindeAccountCustom.action";
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
                        var totalAttention = parseInt($("#totalAttention").html());
                        var sum = 1+totalAttention;
                        $("#totalAttention").html(sum);
                        obj.src="images/icons/attention.png";
                        obj.title="取消关注";
                        $(obj).removeAttr("style");
                    }else{
                    	var alertDialog = new Dialog();
    					alertDialog.init({
    						contentHtml: data,
    						yes: function() {
    							alertDialog.close();
    							$(obj).removeAttr("style");
    						},
    						closeBtn: true
    					});
    					return;
                    }
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
                        var totalAttention = parseInt($("#totalAttention").html());
                        var sum = totalAttention-1;
                        if(sum>=0)
                        	$("#totalAttention").html(sum);
                        obj.src="images/icons/nonatten.png";
                        obj.title="加入关注";
                    }
                    $(obj).removeAttr("style");
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
