$(function() {
	var noteInquire = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			delSmsAction: winParams.delSmsAction || ''		// 删除短信
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
		
		var menu = function(){
			if($.browser.msie && ($.browser.version == "6.0") && !$.support.style){
				$('.menu_level_2').each(function(){
				    var menu  = $(this),
				        h = 28,
				        count = $('li', menu).length;
				    
				    if(count === 0){ return; }
				    h = h * count - 10;
				    var ifm = $('<iframe></iframe>');
				    ifm.css({
				        position: 'absolute',
				        bottom: 0,
				        left: '0px',
				        width: '100px',
				        height: h + 'px',
				        zIndex: '-1'
				    });
				    menu.append(ifm);
				});
			}
		};
		
		// 验证方法
		var check = {
			/**
			 * @description 检查起始日期的距今时间最大值
			 * @param startVal {String} 要检查的时间 yyyy-MM-dd
			 * @return {Boolean} true 日期格式正确 / false 日期格式错误
			**/
			maxDate: function(startVal) {
				// 系统时间
				var sysDate = new Date(),
					sysDateY = parseInt(sysDate.getFullYear(), 10),
					sysDateM = parseInt(sysDate.getMonth(), 10) + 1,
					sysDateD = parseInt(sysDate.getDate(), 10),
					sysUTC = Date.UTC(sysDateY, sysDateM, sysDateD);
				
				// 用户选择时间
				var formatDate = startVal.split('-'),
					formatDateY = parseInt(formatDate[0], 10),			// 年
					formatDateM = parseInt(formatDate[1], 10),			// 月
					formatDateD = parseInt(formatDate[2], 10),			// 日
					formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
				
				// 用户选择时间离系统时间的天数
				var dateGap = parseInt((sysUTC - formatUTC) / 1000 / 60 / 60 / 24, 10);
				
				// 如果超过前3个月
				if (dateGap > 31*3) {
					return false;
				} else {
					return true;
				}
			},
			/**
			 * @description 检查结束日期与起始日期的时间范围
			 * @param startVal {String} 起始日期 yyyy-MM-dd
			 * @param endVal {String} 终止日期 yyyy-MM-dd
			 * @return {Boolean} true 日期格式正确 / false 日期格式错误
			**/
			rangeDate: function(startVal, endVal) {
				//var startVal = els.dateStart.val();
				this.maxDate(startVal);
				if (startVal != '') {
					var startDate = startVal.split('-'),
						startDateY = parseInt(startDate[0], 10),			// 年
						startDateM = parseInt(startDate[1], 10),			// 月
						startDateD = parseInt(startDate[2], 10),			// 日
						startUTC = Date.UTC(startDateY, startDateM, startDateD);
						
					var formatDate = endVal.split('-'),
						formatDateY = parseInt(formatDate[0], 10),			// 年
						formatDateM = parseInt(formatDate[1], 10),			// 月
						formatDateD = parseInt(formatDate[2], 10),			// 日
						formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
						
					var dateGap = parseInt((formatUTC - startUTC) / 1000 / 60 / 60 / 24, 10);

					if (dateGap > 31) {
						return false;
					} else {
						return true;
					}
				}
			}
		};

		// 表单
		var form = function() {
			// 元素集合
			var els = {
				searForm: $('#sear_form'),
				dateStart: $('#date_start'),
				dateEnd: $('#date_end'),
				searInput: $('#sear_input'),
				searBtn: $('#sear_btn'),
				dateTip: $('#date_tip'),
				currentPage: $('currentPage'),
				smsType: $('sms_type'),
				shopName: $('shop_name')
			};
			
			// 提示文案
			var msg = {
				maxDateErr: '只能查近三个月的数据',
				rangeDateErr: '时间选择跨度不能超过31天',
				searInputDef: '运单号/手机号/姓名',
				searFormatErr: '您输入的内容格式有误'
			};
			
			// 选择短信分类按钮
			$('#sms_type').change(function() {
				loadSmsTemplateData();
			});
			
			//输入框有查询条件,不做清除处理
			if(els.searInput.val()==''||(els.searInput.val()==msg.searInputDef)) {
				els.searInput.defaultTxt(msg.searInputDef);
			}
			
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkMaxDate = check.maxDate(dateVal);
						
						if (checkMaxDate) {
							
							showIcon.correct(els.dateTip, '');
							els.dateEnd.prop('disabled', false);
							$dp.$('date_end').focus();
						} else {
							
							showIcon.error(els.dateTip, msg.maxDateErr);
							_this.blur();
							_this.focus();
							els.dateEnd.prop('disabled', true);
						}
					},
					startDate: '#F{$dp.$D(\'date_end\')}',
					//minDate: '%y-%M-{%d-31*3}',		// 最小时间：3个月前
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			els.dateEnd.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkRangeDate = check.rangeDate(els.dateStart.val(), dateVal);
						
						if (checkRangeDate) {
							
							showIcon.correct(els.dateTip, '');
						} else {
							
							showIcon.error(els.dateTip, msg.rangeDateErr);
						}
						
						_this.blur();
						
					},
					startDate: '#F{$dp.$D(\'date_start\')}',
					minDate: '#F{$dp.$D(\'date_start\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			
			// 表单验证初始化
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 开始日期
			els.dateStart.
				formValidator({validatorGroup:'1', tipID: 'date_tip', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.maxDate(val)) {
						return false;
					}
				}, onError: msg.maxDateErr});
				
			// 结束日期
			els.dateEnd.
				formValidator({validatorGroup:'1', tipID: 'date_tip', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.rangeDate(els.dateStart.val(), val)) {
						return false;
					}
				}, onError: msg.rangeDateErr});
				
			// 输入框
			els.searInput.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				functionValidator({
					fun: function(val) {
						if (val == msg.searInputDef) {
							els.searInput.val('');
						}
					}
				}).
				regexValidator({regExp: ['empty', 'shipmentNum', 'name', 'accountId', 'mobile'], dataType: 'enum', onError: msg.searFormatErr});
				
			els.searInput.blur(function(){
				if(els.searInput.val()==''){
					els.searInput.defaultTxt(msg.searInputDef);
				}
			});
			
			// 点击“查询”按钮
			els.searBtn.click(function() {
				$("#currentPage").val('1');	
				setTimeout(function(){els.searForm.trigger('submit');},0);
			});
			
			//翻页
			pagination.live("click",function(ev){
				ev.preventDefault();	
				$("#currentPage").val($(this).attr("value"));	
				setTimeout(function(){els.searForm.trigger('submit');},0);
			});
			
			//网店查询			
			$('#shop_name').live("click",function() {				
				$("#shopName").val($("#now_shopName").text());	
				setTimeout(function(){els.searForm.trigger('submit');},0);
			});
						
		};
		
		return {
			init: function() {
				menu();
				form();
			}
		}
	})();
	
	//loadSmsTemplateData();
	noteInquire.init();
})

function loadSmsTemplateData() {
	var smsTypeId = $("#sms_type").val();
	var smsTemplateId = $("#smsTemplateId").val();
	var paraMap={smsTypeId:smsTypeId,smsTemplateId:smsTemplateId};
	var url="smsSearch!getSmsTemplates.action";
	$.post(url,paraMap,
		function(data){
			$("#sms_title").html(data);
		} 
   ); 
}
