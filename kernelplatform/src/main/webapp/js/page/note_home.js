$(function() {
	var dialog = new Dialog();
	var note = (function() {
		// 元素集合
		var els = {
			pause: $('.pause'),
			useNow: $('.use_now'),
			set: $('.set'),
			serviceId: $('.fun_id'),
			isOn: $('#isOn'),
			isOn2: $('#isOn2'),
			serviceName: $('.fun_name'),
			smsUsecount: $('#smsUsecount'),
			serviceCanFrom: $('#serviceCan_Form'),
			serviceCannotForm: $('#serviceCannot_Form'),
			dateStart: $('#date_start'),
			dateEnd: $('#date_end'),
			searTex: $('#sear_textarea'),
			msgSearForm: $('#msg_sear_form'),
			searMsgBtn: $('#sear_msg_btn')
		};
		
		// 提示文案
		var msg = {
			maxDateErr: '只能查近三个月的数据',
			rangeDateErr: '时间选择跨度不能超过31天',
			searInputDef: '运单号/手机号/姓名',
			searFormatErr: '您输入的内容格式有误'
		};
		
		var textDefault = function() {
			els.searTex.defaultTxt(msg.searInputDef);
		};
		
		var note_home = function(){
			//点击"暂停使用"
			els.pause.live('click',function(){
				var _this = $(this),
					serviceId = $('.fun_id',_this.parent()),
					serviceName = $('.fun_name',_this.parent());
				
				$.ajax({
					url:'smsHomeEvent_popedomVerdict.action',
					data:{
						serviceId:serviceId.val()
					},
					dataType:'json',
					cache:false,
					success:function(data){
						if(data.status){
							dialog.init({
								contentHtml:'你确定要暂停使用'+serviceName.val()+'吗？',
								yes:function(){
									dialog.close();
									els.isOn.val("N");
									els.serviceCanFrom.attr('action','smsHomeEvent_pauseOrOpenService.action?menuFlag=sms_home&serviceId='+serviceId.val()+"&tips=0");
									setTimeout(function(){									
										els.serviceCanFrom.trigger('submit');
									},0);													
								},
								no:function(){
									dialog.close();
								}
							})
						}else{
							var viewDialog = new Dialog();
							viewDialog.init({
								contentHtml: '亲，这功能不是您开启的，您没有权限操作！',
								yes: function() {
									viewDialog.close();
								},
								yesVal: '确定',
								closeBtn: true
							})
						}
					}
				});
			});
			
			//点击"设置"
			els.set.live('click',function(){
				var _this = $(this),
				serviceId = $('.fun_id',_this.parent());
				
				$.ajax({
					url:'smsHomeEvent_popedomVerdict.action',
					method:'post',
					data:{
						serviceId:serviceId.val()
					},
					cache:false,
					dataType:'json',
					success:function(data){
						if(data.status){
							els.serviceCanFrom.attr('action','smsHomeEvent_smsSetting.action?menuFlag=sms_home&serviceId='+serviceId.val()+'&pos=up');
							setTimeout(function() {
								els.serviceCanFrom.trigger('submit');
							}, 1);
						}else{
							var viewDialog = new Dialog();
							viewDialog.init({
								contentHtml: '亲，这功能不是您开启的，您没有权限操作！',
								yes: function() {
									viewDialog.close();
								},
								yesVal: '确定',
								closeBtn: true
							})
						}
					}
				});
			});
			
			//点击"立即使用"
			els.useNow.live('click',function(){
				var _this = $(this),
				serviceId = $('.fun_id',_this.parent()),
				serviceName = $('.fun_name',_this.parent());
				
				if(serviceName.val() == '问题件通知'){
					var msgDialog = new Dialog();
					msgDialog.init({
						contentHtml:'确定立即使用问题件通知？',
						yes:function(){
							msgDialog.close();
							els.isOn2.val("Y");
							els.serviceCannotForm.attr('action','smsHomeEvent_pauseOrOpenService.action?menuFlag=sms_home&serviceId='+serviceId.val()+'&pos=down'+"&tips=1");
							setTimeout(function(){
								els.serviceCannotForm.trigger('submit');
							},0);
						},
						no:function(){
							msgDialog.close();
						}
					});
				} else {
				
					//检查可发短信是否大于0
					if(els.smsUsecount.val() > 0){
					
						els.serviceCannotForm.attr('action','smsHomeEvent_openService.action?menuFlag=sms_home&serviceId='+serviceId.val() + '&pos=down');
						setTimeout(function(){
							els.serviceCannotForm.trigger('submit');
						},0);
						
					} else {
						dialog.init({
							contentHtml:'亲，你的可发短信数为 0，需要先去购买短信!',
							yes:function(){
								dialog.close();
								window.location.href = "smsServiceMarket!inBuyPorts.action?menuFlag=sms_package&serviceId="+serviceId.val() + "&pos=down";
							},
							no:function(){
								dialog.close();
							},
							yesVal:'购买',
							noVal:'暂不购买'
								
						});
					}
				}
				
			});
			
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
		
		
		// form 表单
		var form = function() {
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
							
						$dp.$('date_end').focus();
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
							dateVal = _this.value;
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
			
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'msg_sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 输入框
			els.searTex.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				functionValidator({
					fun: function(val) {
						if (val == msg.searInputDef) {
							els.searTex.val('');
						}
					}
				}).
				regexValidator({regExp: ['empty', 'shipmentNum', 'name', 'accountId', 'mobile'], dataType: 'enum', onError: msg.searFormatErr});
			
			
			els.searMsgBtn.click(function() {
				//查询日期不得超过近3个月
				var checkMaxDate = check.maxDate(els.dateStart.val());
				
				//查询日期最长为31天
				var checkRangeDate = check.rangeDate(els.dateStart.val(), els.dateEnd.val());
				
				
				//查询格式
				var searTexFlg = $.formValidator.pageIsValid('1');
				
				if(!checkRangeDate || !searTexFlg || !checkMaxDate){
					var dialog = new Dialog();
					var contentVar = msg.rangeDateErr;
					
					if(!checkMaxDate){
						contentVar = msg.maxDateErr;
					}else if(checkRangeDate){
						contentVar = msg.searFormatErr;
					}
					dialog.init({
						contentHtml:contentVar,
						yes:function(){
							dialog.close();
							return false;
						}
					});
				}else{
					var dateStart = els.dateStart.val();
					var dateEnd = els.dateEnd.val();
					var smsTypeId = $('#msg_type').val();
					
					if ($.trim(els.searTex.val()) == msg.searInputDef) {
						els.searTex.val('');
					}
					var searchInput = $.trim(els.searTex.val());
					
					$('input[name=dateStart]').val(dateStart);
					$('input[name=dateEnd]').val(dateEnd);
					$('input[name=smsTypeId]').val(smsTypeId);
					$('input[name=searchInput]').val(searchInput);
					els.msgSearForm.attr('action','smsSearch!searchPage.action?menuFlag=sms_info');
					
					setTimeout(function() {
						els.msgSearForm.trigger('submit');
					}, 1);
					
				}
				
				
			})
			
			//输入框
			els.searTex.blur(function(){
				if(els.searTex.val() == ''){
					els.searTex.defaultTxt(msg.searInputDef);
				}
			});
		
		};
		
		
		return {
			init: function() {
				textDefault();			
				note_home();
				form();	
			}
		}
	})();
	
	note.init();
})