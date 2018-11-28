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
		
		var form = function() {
		
			//$('#admin_select').defaultTxt('用户名/电话/店铺名字/网点名称');
			
			// 元素集合
			var els = {
				dateTip: $('#dateTip'),
				dateStart: $('#date_start'),
				dateEnd: $('#date_end'),
				searBtn: $('#sear_btn'),
				searForm: $('#sear_form'),
				spendRangeA: $('#spend_range_a'),
				spendRangeB: $('#spend_range_b'),
				balanceRangeA: $('#balance_range_a'),
				balanceRangeB: $('#balance_range_b'),
				checkBox:$('#checkBox'),
				currentPage: $('#currentPage'),//分页当前页
				remark: $('#RemarkTip')
			};
			
			var msg = {
				balanceFormatErr: '请输入非负数字',
				balanceCompareErr: '请输入正确的余额范围',
				spendCompareErr: '请输入正确的消费额范围'	
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
				regexValidator({regExp: ['empty', 'num1', 'decmal'], dataType: 'enum', onError: msg.balanceFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val !== '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
						}
						if(els.balanceRangeB.val() !== ''){
							return (!isNaN(els.balanceRangeB.val())) && parseFloat(els.balanceRangeB.val()) >= 0;
						}
					},
					onError: msg.balanceFormatErr
				}).
				functionValidator({
					fun: function(val, el) {
						if(val !== '' && els.balanceRangeB.val() !== ''){
							return  parseFloat(val) <= parseFloat(els.balanceRangeB.val());
						}
					},
					onError: msg.balanceCompareErr
				});
			
		   els.balanceRangeB.
			 formValidator({validatorGroup:'1', tipID: 'balanceTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
			 regexValidator({regExp: ['empty', 'num1', 'decmal'], dataType: 'enum', onError: msg.balanceFormatErr}).
			 functionValidator({
					fun: function(val, el) {
						if (val !== '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
						}
						if(els.balanceRangeA.val() !== ''){
							return (!isNaN(els.balanceRangeA.val())) &&parseFloat(els.balanceRangeA.val()) >= 0;
						}
					},
					onError: msg.balanceFormatErr
				}).
				functionValidator({
					fun: function(val, el) {
						if(val !== '' && els.balanceRangeA.val() !== ''){
							return  parseFloat(val) >= parseFloat(els.balanceRangeA.val());							
						}
					},
					onError: msg.balanceCompareErr
				});
			
			els.spendRangeA.
				formValidator({validatorGroup:'1', tipID: 'consumeTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'num1', 'decmal'], dataType: 'enum', onError: msg.balanceFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val !== '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
						}
						if(els.spendRangeB.val() !== ''){
							return (!isNaN(els.spendRangeB.val())) && parseFloat(els.spendRangeB.val()) >= 0; 
						}
					},
					onError: msg.balanceFormatErr
				}).
				functionValidator({
					fun: function(val, el) {
						if(val !== '' && els.spendRangeB.val() !== ''){
							return  parseFloat(val) <= parseFloat(els.spendRangeB.val());
						}
					},
					onError: msg.spendCompareErr
				});
			
			els.spendRangeB.
				formValidator({validatorGroup:'1', tipID: 'consumeTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'num1', 'decmal'], dataType: 'enum', onError: msg.balanceFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val !== '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
						}
						if(els.spendRangeA.val() !== ''){
							return (!isNaN(els.spendRangeA.val())) &&parseFloat(els.spendRangeA.val()) >= 0;
						}
					},
					onError: msg.balanceFormatErr
				}).
				functionValidator({
					fun: function(val, el) {
						if(val !== '' && els.spendRangeA.val() !== ''){
							return  parseFloat(val) >= parseFloat(els.spendRangeA.val());
						}
					},
					onError: msg.spendCompareErr
				});
			
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
						
						els.dateEnd.prop('disabled', false);
						$dp.$('date_end').focus();
					},
					startDate: '#F{$dp.$D(\'date_end\')}',
					minDate: '%y-%M-{%d-365}',		// 最小时间：一年
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
							dateVal = _this.value;
						
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
			var startBalance = $("#balance_range_a").val();
			var endBalance = $("#balance_range_b").val();
			var startConsume = $("#spend_range_a").val();
			var endConsume = $("#spend_range_b").val();

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
            
			
			var value = 'startBalance:'+startBalance+';endBalance:'+endBalance+';startConsume:'+startConsume+';endConsume:'
		                +endConsume+';checkbox1:'+checkbox1
		                +';checkbox2:'+checkbox2+';checkbox3:'
		                +checkbox3+';checkbox4:'+checkbox4+';checkbox5:'+checkbox5;
			
			if(initvalue!= value)
				return true;
			
			return false;

		}
		
		//翻页
		pagination.live('click',function(){
		
			$('#currentPage').val($(this).attr("value"));
			
			setTimeout(function(){$("#sear_form").trigger('submit');},0);
			
			
		});
		
		
		
		
		var remark = function() {
			$('.remark').click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					userName = $.trim($('.td_a', _this.parents('tr')).html());
				var _this2 = $(this),
					accountUserId = $.trim($('.input_id', _this.parents('tr')).val());	
				
				var remark111 =  $.trim($('.td_e', _this.parents('tr')).html());
							
				var remarkDialog = new Dialog();
				
				remarkDialog.init({
					contentHtml: '<form action="accountAdmin_editRemark.action" id="d_form" class="form">' +
								'	<p>' +
								'		<label>用户名：</label>' +
								'		<span>' + userName+'</span>' +
								'	</p>' +
								'	<p>' +
								'		<label for="d_remark">备注：</label>' +
								'		<textarea name="remark" id="remark">'+remark111+'</textarea>' +
								'		<p><font id="RemarkTip" color="red"></font></p>' +
								'		<input type="hidden" id="acc_user_id" name="accountUserId" value="' + accountUserId + '" />' +
								'	</p>' +
								'</form>',
					yes: function() {
						$('#dialog textarea').val();
						
						if ($('#remark').val().length < 4 || $('#remark').val().length > 200) {
							$('#RemarkTip').html('备注信息必须是4-200个字符');
							return false;
						}
						
						
						//$('#dialog form').submit();    将由form表单提交改为由ajax提交
						$.ajax({
							type:'post',//可选get
							url: 'accountAdmin_editRemark.action',
							data:'accountUserId='+accountUserId+'&remark='+$('#remark').val(),
							dataType:'Json',//服务器返回的数据类型 可选XML ,Json jsonp script html text等
							success:function(msg){
								if(msg=='修改成功')
								{
									_this.parent().prev().html($('#remark').val());
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
			})
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