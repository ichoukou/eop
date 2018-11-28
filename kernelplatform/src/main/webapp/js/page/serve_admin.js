$(function() {
	
	var accAdmin = (function() {
	
	
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
				
				// 如果超过一年
				if (dateGap > 365) {
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
				//this.maxDate(startVal);
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
		
			//$('#admin_select').defaultTxt('用户名/电话/店铺名称/网点名称');
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

			
			// 元素集合
			var els = {
				dateTip: $('#dateTip'),
				dateStart: $('#date_start'),
				dateEnd: $('#date_end'),
				searBtn: $('#sear_btn'),
				searForm: $('#sear_form'),
				spendRangeA: $('#spend_range_a'),
				balanceRangeA: $('#balance_range_a')
			};
			
			var msg = {
				balanceFormatErr: '请输入数字',
				balanceCompareErr: '请输入正确的余额范围',
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

			els.balanceRangeA.
				formValidator({validatorGroup:'1', tipID: 'balanceTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['decmal4', 'empty'], dataType: 'enum', onError: msg.balanceFormatErr}).
				compareValidator({desID: 'balance_range_b', operateor: '<=', onError: msg.balanceCompareErr});
			
			els.spendRangeA.
				formValidator({validatorGroup:'1', tipID: 'consumeTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['decmal4', 'empty'], dataType: 'enum', onError: msg.balanceFormatErr}).
				compareValidator({desID: 'spend_range_b', operateor: '<=', onError: msg.balanceCompareErr});
				
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
				
				var count=0;
				var array = document.getElementsByName('serviceIds');
				
				for(i=0;i<array.length;i++) {
					if(array[i].checked)	
					    count++;
				}
				
				if(count==0){					
					showIcon.error($('#serviceType'), '请至少选择一种服务类型');
					return false;
				}
				
				//判断查询条件是否发生更改,若发生更改，将当前表单中currentPage当前页的value值改为1				
//				if(isChanged() && ($('#def').val()!= '1')){					
//					$('#currentPage').val(1);					
//				}
				
				$('#currentPage').val(1);		
				
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
            
		
			var value = 'startTime:'+startTime+';endTime:'+endTime+';checkbox1:'+checkbox1		                
		                +';checkbox2:'+checkbox2+';checkbox3:'
		                +checkbox3+';checkbox4:'+checkbox4+';checkbox5:'+checkbox5;
			
			if(initvalue!= value)
				return true;
			
			return false;

		}
		
		
		//翻页
		pagination.live('click',function(){
			$('#currentPage').val($(this).attr("value"));
			setTimeout(function(){
				$("#sear_form").trigger('submit');
			},0);
		});
		
		
		
		
		var remark = function() {
			$('.remark').click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					userName = $.trim($('.td_a', _this.parents('tr')).html()),
					dredgeServiceId = $.trim($('.dre_id', _this.parents('tr')).val()),
					remark111 = $.trim($('.mid', _this.parents('tr')).html());
				    remark111 =remark111.replace('<p>','');
				    remark111 =remark111.replace('</p>','');
				var lenth=remark111.length;
				
				var remarkDialog = new Dialog();
				
				remarkDialog.init({
					contentHtml: '<form action="serviceAdmin_editRemark.action" id="d_form" class="form">' +
								'	<p>' +
								'		<label>用户名：</label>' +
								'		<span>' + userName+'</span>' +
								'	</p>' +
								'	<p>' +
								'		<label for="d_remark">备注：</label>' +
								'		<textarea name="remark" id="remark">'+remark111+'</textarea>' +
								'		<p><font id="RemarkTip" color="red"></font></p>' +
								'		<input type="hidden" name="dredgeServiceId" value="'+dredgeServiceId+'" id="dredgeServiceId"/>' +
								'	</p>' +
								'</form>',
					yes: function() {
						$('#dialog textarea').val();
						
						if($('#remark').val().length<4 || $('#remark').val().length>200){
							$('#RemarkTip').html('备注信息必须是4-200个字符');
							return false;
						}
						
						//$('#dialog form').submit();   将由form表单提交改为由ajax提交
						$.ajax({
							type:'post',//可选get
							url: 'serviceAdmin_editRemark.action',
							data:'dredgeServiceId='+dredgeServiceId+'&remark='+$('#remark').val(),
							dataType:'Json',//服务器返回的数据类型 可选XML ,Json jsonp script html text等
							success:function(msg){
								if(msg=='修改成功')
								{
									if(lenth==0){
										$('.status', _this.parents('tr')).html('<span class="sub_remark"></span><div class="close_tip" style=""><div class="mid"><p></p></div><div class="fd" ></div></div>');
									}
									var remark = $('#remark').val(),
									    omit = remark.length > 4;
									
									$('.sub_remark', _this.parents('tr')).html(omit ? remark.substring(0,5)+'...' : remark);
									if(omit){
										$('.mid', _this.parents('tr')).html($('#remark').val());
									}
									else{
										$('.close_tip', _this.parents('tr')).remove();
									}
								    _this.html('编辑备注');
							        remarkDialog.close();
								}
							},
							error:function(){
							   alert("修改备注失败");							  
							}
						})
						
					},
					yesVal: '保 存',
					no: function() {
						remarkDialog.close();
					}
				});
			});
		};
		
		return {
			init: function() {
				
				if($('#admin_select').val()== '')				
					$('#admin_select').defaultTxt('用户名/电话/店铺名称/网点名称');
				
				form();
				remark();
			}
		}
	})();
	
	accAdmin.init();

})