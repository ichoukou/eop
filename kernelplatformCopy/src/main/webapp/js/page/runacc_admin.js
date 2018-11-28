$(function() {
	var runaccAdmin = (function() {
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
		
		// 验证方法
		var check = {			
			/**
			 * @description 检查结束日期与起始日期的时间范围
			 * @param startVal {String} 起始日期 yyyy-MM-dd
			 * @param endVal {String} 终止日期 yyyy-MM-dd
			 * @return {Boolean} true 日期格式正确 / false 日期格式错误
			**/
			rangeDate: function(startVal, endVal) {				
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

					// 如果超过一年
					if (dateGap > 365) {
						return false;
					} else {
						return true;
					}
				}
			}
		};
		
		var form = function() {
		
			// 元素集合
			var els = {
				dateTip: $('#dateTip'),
				dateStart: $('#date_start'),
				dateEnd: $('#date_end'),
				searBtn: $('#sear_btn'),
				searForm: $('#sear_form'),
				tranRangeA: $('#tran_range_a'),
				tranRangeB: $('#tran_range_b')
			};
			
			var msg = {
					balanceFormatErr: '请输入非负数字',
					balanceCompareErr: '请输入正确的交易额范围',
					rangeDateErr: '查询起止日期必须在一年内'
				};
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			$('#admin_select').
			formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
			functionValidator({
				fun: function(val) {
					if (val == '用户名/电话/店铺名称/网点名称') {
						$('#admin_select').val('');
					}
				}
			});
			
		els.tranRangeA.
			formValidator({validatorGroup:'1', tipID: 'tranRangeTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
			regexValidator({regExp:  ['empty', 'num1', 'decmal'], dataType: 'enum', onError: msg.balanceFormatErr}).
			functionValidator({
				fun: function(val, el) {
					if (val !== '') {
						var formatPrice = parseFloat(val, 10).toFixed(2);
						el.value = formatPrice;
					}
					if(els.tranRangeB.val() !== ''){
						return (!isNaN(els.tranRangeB.val())) && parseFloat(els.tranRangeB.val()) >= 0;
					}
				},
				onError: msg.balanceFormatErr
			}).
			functionValidator({
				fun: function(val, el) {
					if(val !== '' && els.tranRangeB.val() !== ''){
						return  parseFloat(val) <= parseFloat(els.tranRangeB.val());
					}
				},
				onError: msg.balanceCompareErr
			});
		
		els.tranRangeB.
			formValidator({validatorGroup:'1', tipID: 'tranRangeTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
			regexValidator({regExp:  ['empty', 'num1', 'decmal'], dataType: 'enum', onError: msg.balanceFormatErr}).
			functionValidator({
				fun: function(val, el) {
					if (val !== '') {
						var formatPrice = parseFloat(val, 10).toFixed(2);
						el.value = formatPrice;
					}
					if(els.tranRangeA.val() !== ''){
						return (!isNaN(els.tranRangeA.val())) &&parseFloat(els.tranRangeA.val()) >= 0;
					}
				},
				onError: msg.balanceFormatErr
			}).
			functionValidator({
				fun: function(val, el) {
					if(val !== '' && els.tranRangeA.val() !== ''){
						return  parseFloat(val) >= parseFloat(els.tranRangeA.val());							
					}
				},
				onError: msg.balanceCompareErr
			});
			
		    // 结束日期
		   els.dateEnd.
			formValidator({validatorGroup:'1', tipID: 'dateTip', onShow: '', onFocus: '', onCorrect: ' '}).
			functionValidator({fun: function(val) {
				if (!check.rangeDate(els.dateStart.val(), val)) {
					return false;
				}
			}, onError: msg.rangeDateErr});
		
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
						
						//els.dateEnd.prop('disabled', false);
						$dp.$('date_end').focus();
					},
					startDate: '#F{$dp.$D(\'date_end\')}',
					//minDate: '%y-%M-{%d-365}',		// 最小时间：1年前
					//maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			els.dateEnd.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
						
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
					//maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			
				
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				
				if($('#checkbox1').attr("checked")!='checked' && $('#checkbox2').attr("checked")!='checked' &&
						   $('#checkbox3').attr("checked")!='checked' && $('#checkbox4').attr("checked")!='checked'&&
						   $('#checkbox5').attr("checked")!='checked'){
							
					        showIcon.error($('#userType'), '请最少选择一种用户类型');
							return false;
						}							
				//判断查询条件是否发生更改,若发生更改，将当前表单中currentPage当前页的value值改为1				
				if(isChanged() && ($('#flag').val()!= '1')){					
					$('#currentPage').val(1);
					$('#userId').val('');
				}
				
				els.searForm.trigger('submit');
			});
		};
		
		//判断查询条件是否发生更改,若发生更改,返回true，否则返回false
		var isChanged = function() {
			
			//用户名/电话/店铺名称/网点名称 (判断此输入项是否发生变化)
			var initformName = $("#formName").val();
			var valueformName = $("#admin_select").val();
			if(initformName != valueformName)
				return true;
			
			var initvalue = $("#initValue").val();
			var startTime = $("#date_start").val();
			var endTime = $("#date_end").val();
			var startMoney = $("#tran_range_a").val();
			var endMoney = $("#tran_range_b").val();
			var dealType = $("#tran_type").val();
			var dealStatus = $("#pay_status").val();
			var dealStatus = $("#pay_status").val();
			
			var checkbox1,checkbox2,checkbox3,checkbox4,checkbox5;
			
			if($('#checkbox1').attr("checked")=='checked')
			     checkbox1 = '1;卖家';
			if($('#checkbox2').attr("checked")=='checked')
			     checkbox2 = '4';
			if($('#checkbox3').attr("checked")=='checked')
			     checkbox3 = '1;业务账号';
			if($('#checkbox4').attr("checked")=='checked')
			     checkbox4 = '2;网点';
			if($('#checkbox5').attr("checked")=='checked')
			     checkbox5 = '2;承包区';

			var value = 'startTime:'+startTime+';endTime:'+endTime+';startMoney:'+startMoney+';endMoney:'
		                +endMoney+';dealType:'+dealType+';dealStatus:'+dealStatus+';checkbox1:'+checkbox1
		                +';checkbox2:'+checkbox2+';checkbox3:'
		                +checkbox3+';checkbox4:'+checkbox4+';checkbox5:'+checkbox5;
			
			if(initvalue!= value)
				return true;
			else
				return false;

		}
		
		//翻页
		pagination.live('click',function(){
			$('#currentPage').val($(this).attr("value"));
			setTimeout(function(){
				$("#sear_form").trigger('submit');
			},0);
		});
		
		// 查看明细
		var viewDetail = function() {
			$('.view').click(function(ev) {
				ev.preventDefault();
				
				var detailDialog = new Dialog();
				var _this = $(this),
					tranNum = $('.tran_num', _this.parent()).val(),
					accName = $('.acc_name', _this.parent()).val(),
					userType = $('.user_type', _this.parent()).val(),
					money = $('.money', _this.parent()).val(),
					tranTime = $('.tran_time', _this.parent()).val(),
					payTime = $('.pay_time', _this.parent()).val(),
					tranType = $('.tran_type', _this.parent()).val(),
					tranName = $('.tran_name', _this.parent()).val(),					
					
					detailHtml = '<div id="detail_list">' +
									'	<ul>' +
									'		<li>' +
									'			<label>交易号：</label>' +
												tranNum +
									'		</li>' +
									'		<li>' +
									'			<label>用户名：</label>' +
												accName +
									'		</li>' +
									'		<li>' +
									'			<label>用户类型：</label>' +
												userType +
									'		</li>' +
									'		<li>' +
									'			<label>金额：</label>' +
												money +'元'+
									'		</li>' +
									'		<li>' +
									'			<label>交易时间：</label>' +
												tranTime +
									'		</li>' +
									'		<li>' +
									'			<label>付款时间：</label>' +
												payTime +
									'		</li>' +
									'		<li>' +
									'			<label>交易类型：</label>' +
												tranType +
									'		</li>' +
									'		<li>' +
									'			<label>交易名称：</label>' +
												tranName +
									'		</li>' +
									'	</ul>' +
									'</div>';
				detailDialog.init({
					contentHtml: detailHtml,
					yes: function() {
						detailDialog.close();
					},
					yesVal: '关闭'
				});
			})
		};
		
		return {
			init: function() {
				
				if($('#admin_select').val()== '')				
					$('#admin_select').defaultTxt('用户名/电话/店铺名称/网点名称');
				
				form();
				viewDetail();
			}
		}
	})();
	
	runaccAdmin.init();
})