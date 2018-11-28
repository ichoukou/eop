/**
 * 我要发货
**/
$(function() {
	
	var winParams = window.params || {};
	
	var config = {
		userType: winParams.userType || '',
		userState: winParams.userState || '',
		userField003: winParams.userField003 || ''	
		
	};
	
	$('.sendTab').live('click',function(){
		$('.checkTab').val('0');
		//window.location.href="orderPlace!toOrderCreate.action?menuFlag=orderPlace";
	});
	$('.recordTab').live('click',function(){
		$('.checkTab').val('1');
		//window.location.href="orderPlace!tabPass.action?menuFlag=orderPlace";
	});
	
	var delivery = (function() {
		
		// 元素集合
		var els = {
			contact: $('#contact'),
			waybill: $('#waybill'),
			area: $('#area'),
			area2:$('#area2'),
			prov: $('#x_prov'),
			city: $('#x_city'),
			district: $('#x_district'),
			prov2:$('#x_prov2'),
			city2:$('#x_city2'),
			district2:$('#x_district2'),
			areaTip: $('#area_tip'),
			addressDetail: $('#address_detail'),
			addressDetail2: $('#address_detail2'),
			mobileTel: $('#mobile'),
			mobileTelTip: $('#mobileTip'),
			telPartA: $('#fixed1'),
			telPartB: $('#fixed2'),
			telPartC: $('#fixed3'),
			telTip: $('#telTip'),
			waybillTip: $('#waybillTip'),
			cartName: $('#cart_name'),
			quantity: $('#quantity'),
			quantityTip: $('#quantityTip'),
			weight: $('#weight'),
			weightTip: $('#weightTip'),
			attention: $('#attention'),
			attentionTip: $('#attentionTip'),
			start: $('#start'),
			end: $('#end'),
			dateStart: $('#date_start'),
			dateEnd: $('#date_end'),
			consignee: $('#consignee'),
			consigneeTip: $('#consigneeTip'),
			dateTip: $('#dateTip'),
			dateTipB: $('#date_tip'),
			sendForm: $('#send_form'),
			recordForm: $('#record_form'),
			sendBtn: $('#send_btn'),
			searBtn: $('#sear_btn'),
			currentPage: $('#currentPage')
		};
		
		var selectArea = function() {
			//收获地址
			var province = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#area'],
				autoLink: false
			};
					 
			linkageSel = new LinkageSel(province);
			var prov = els.prov.val(), city = els.city.val(), district = els.district.val();
			linkageSel.changeValues([prov, city, district]);
			
			//修改地址
			var province2 = {
					data: districtData,
					selStyle: 'margin-left:3px;',
					select: ['#area2'],
					autoLink: false
				};
			linkageSel2 = new LinkageSel(province2);
			var prov2 = els.prov2.val(), city2 = els.city2.val(), district2 = els.district2.val();
			if(prov2==''){
				setTimeout(function(){linkageSel2.changeValues(['', '', '']);},0);
			}else if(city2!=''&&district2==''){
				setTimeout(function(){linkageSel2.changeValuesByName([prov2, city2]);},0);
			}
			else{
				setTimeout(function(){linkageSel2.changeValuesByName([prov2, city2, district2]);},0);
			}
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
			// 验证初始值
			var mobileResult = false,		// 手机验证结果
				telResult = false,			// 固话验证结果
				waybillResult = true,		// 运单号验证结果
				quantityResult = true,		// 货物数量验证结果
				weightResult = true,		// 货物重量验证结果
				attentionResult = true,		// 注意事项验证结果
				dateResult = true,			// 日期的验证结果
				dateResultB = true,			// 日期的验证结果
				nameResult = true;			// 姓名的验证结果
				
			
			// 验证正则
			var reg = {
				mobileTel: /^1\d{10}$/,									// 验证手机号
				telPartA: /^\d{3,4}$/,									// 验证区号
				telPartB: /^\d{7,8}$/,									// 验证电话
				telPartC: /^\d{0,5}$/,									// 验证分机
				waybill: /^[A-Za-z0-9]{1}\d{9}$/,						// 验证运单号
				cartQuantity: /^[1-9]\d*$/,								// 验证货物数量
				cartWeight: /^([+-]?)\d*\.?\d+$/,						// 验证货物重量
				name: /^[a-zA-Z\u4E00-\u9FA5\uF900-\uFA2D]{2,12}$/		// 验证姓名
			};
			
			// 提示文案
			var tipsMsg = {
				contactErr: '收货联系人格式错误',
				telAEmpty: '区号没填',
				telAErr: '区号填写错误',
				mobileErr: '手机号码格式不正确',
				telBEmpty: '电话号没填',
				telBErr: '电话号填写错误',
				telCErr: '分机号填写错误',
				atLeastOne: '手机号码、固定电话至少填一项',
				provinceErr: '请选择省',
				cityErr: '请选择市',
				areaErr: '请选择区',
				detailEmpty: '街道地址不能为空字符串',
				detailMinErr: '街道地址不能为空',
				detailMaxErr: '收货地址的长度为1-1000个字符之间',
				cartMinErr: '货物名称不能为空',
				cartMaxErr: '货物名称超长',
				quantityFormatErr: '货物数量格式不正确',
				weightFormatErr: '重量格式不正确',
				overWeightErr: '重量范围超出限制',
				waybillErr: '运单号格式错误',
				attentionErr: '注意事项字数超出范围',
				dateErr: '请选择日期',
				maxDateErr: '只能查近三个月的数据',
				rangeDateErr: '只能查30天内的数据',
				dateEmptyErr: '请选择开始日期',
				consigneeErr: '姓名格式错误'
			};
			
			// 默认显示手机和固话的提示层
			showIcon.show(els.mobileTelTip, tipsMsg.atLeastOne);
			showIcon.show(els.telTip, tipsMsg.atLeastOne);
			
			// 验证方法
			var check = {
				// 手机
				mobile: function(mobileVal) {
					if (reg.mobileTel.test(mobileVal)) {
						showIcon.correct(els.mobileTelTip, '');
						mobileResult = true;
					} else {
						showIcon.error(els.mobileTelTip, tipsMsg.mobileErr);
						mobileResult = false;
					}
				},
				// 固定电话
				tel: function(telPartAVal, telPartBVal, telPartCVal) {
					if (telPartAVal != '') {		// 区号填了
						// 验证区号
						if (reg.telPartA.test(telPartAVal)) {	// 区号填写正确
							showIcon.correct(els.telTip, '');
							
							if (telPartBVal != '') {		// 电话填了
								// 验证电话
								if (reg.telPartB.test(telPartBVal)) {		// 电话填写正确
									showIcon.correct(els.telTip, '');
									
									
									if (telPartCVal != '') {			// 分机填了
										// 验证分机
										if (reg.telPartC.test(telPartCVal)) {		// 分机填写正确
											showIcon.correct(els.telTip, '');
											telResult = true;
										} else {												// 分机填写错误
											showIcon.error(els.telTip, tipsMsg.telCErr);
											
											telResult = false;
										}
									} else {
										telResult = true;
									}
								} else {												// 电话填写错误
									showIcon.error(els.telTip, tipsMsg.telBErr);
								}
								
							} else {								// 电话没填
								showIcon.error(els.telTip, tipsMsg.telBEmpty);
							}
							
						} else {											// 区号填写错误
							showIcon.error(els.telTip, tipsMsg.telAErr);
						}
					} else {								// 区号没填
						showIcon.error(els.telTip, tipsMsg.telAEmpty);
					}
				},
				// 运单号
				waybill: function(waybillNum) {
					if (reg.waybill.test(waybillNum)) {
						showIcon.correct(els.waybillTip, '');
						waybillResult = true;
					} else {
						showIcon.error(els.waybillTip, tipsMsg.waybillErr);
						waybillResult = false;
					}
				},
				// 货物数量
				cartQuantity: function(quantity) {
					if (reg.cartQuantity.test(quantity)) {
						showIcon.correct(els.quantityTip, '');
						quantityResult = true;
					} else {
						showIcon.error(els.quantityTip, tipsMsg.quantityFormatErr);
						quantityResult = false;
					}
				},
				// 货物重量
				cartWeight: function(weight, el) {
					if (reg.cartWeight.test(weight)) {
						if (validateWeight(weight, el)) {
							showIcon.correct(els.weightTip, '');
							weightResult = true;
						} else {
							showIcon.error(els.weightTip, tipsMsg.overWeightErr);
							weightResult = false;
						}
					} else {
						showIcon.error(els.weightTip, tipsMsg.weightFormatErr);
						weightResult = false;
					}
				},
				// 注意事项
				attention: function(text) {
					if (text.length <= 200) {
						showIcon.correct(els.attentionTip, '');
						attentionResult = true;
					} else {
						showIcon.error(els.attentionTip, tipsMsg.attentionErr);
						attentionResult = false;
					}
				},
				// 日期
				date: function(dateVal) {
					if (dateVal != '') {
						showIcon.correct(els.dateTip, '');
						dateResult = true;
					} else {
						showIcon.error(els.dateTip, tipsMsg.dateErr);
						dateResult = false;
					}
				},
				// 最多查询近三个月
				maxDate: function(dateVal) {
					if (dateVal == '') {
						showIcon.error(els.dateTipB, tipsMsg.dateEmptyErr);
						dateResultB = false;
					} else {
						// 系统时间
						var sysDate = new Date(),
							sysDateY = parseInt(sysDate.getFullYear(), 10),
							sysDateM = parseInt(sysDate.getMonth(), 10) + 1,
							sysDateD = parseInt(sysDate.getDate(), 10),
							sysUTC = Date.UTC(sysDateY, sysDateM, sysDateD);
						
						//console.log('系统时间： ', sysDateY, sysDateM, sysDateD);
						
						// 用户选择时间
						var formatDate = dateVal.split('-'),
							formatDateY = parseInt(formatDate[0], 10),			// 年
							formatDateM = parseInt(formatDate[1], 10),			// 月
							formatDateD = parseInt(formatDate[2], 10),			// 日
							formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
							
						//console.log('选择时间： ', formatDateY, formatDateM, formatDateD);
						
						// 用户选择时间离系统时间的天数
						var dateGap = parseInt((sysUTC - formatUTC) / 1000 / 60 / 60 / 24, 10);
						
						//console.log(dateGap);
						
						// 如果超过前3个月
						if (dateGap > 31*3) {
							showIcon.error(els.dateTipB, tipsMsg.maxDateErr);
							dateResultB = false;
						} else {
							showIcon.correct(els.dateTipB, '');
							dateResultB = true;
						}
					}
				},
				// 查询范围仅在一个月内
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

						if (dateGap > 30) {
							showIcon.error(els.dateTipB, tipsMsg.rangeDateErr);
							dateResultB = false;
						} else {
							showIcon.correct(els.dateTipB, '');
							dateResultB = true;
						}
					}
				},
				// 收货人
				consignee: function(nameVal) {
					if (reg.name.test(nameVal)) {
						showIcon.correct(els.consigneeTip, '');
						nameResult = true;
					} else {
						showIcon.error(els.consigneeTip, tipsMsg.consigneeErr);
						nameResult = false;
					}
				}
			};
			// 初始化时间范围
			var updateTime = function() {
				var initStart = new Date(),
					initEnd = new Date(),
					initStartDate = initStart.getUTCDate(),						// 当前日期 - 天
					initStartHour = initStart.getHours();						// 当前日期 - 小时
					
				if (initStartHour >= 18) {		// 如果在18点以后
					// 设为第二天
					initEnd.setDate(initStartDate + 1);
				}
				
				// 设定在 18:00
				initEnd.setHours(18);
				initEnd.setMinutes(0);
				
				// 格式化时间
				var initStartFormat = initStart.format('yyyy-MM-dd hh:mm'),
					initEndFormat = initEnd.format('yyyy-MM-dd hh:mm');
				
				//console.log(initStartFormat);
				
				els.start.val(initStartFormat);			// 填充开始日期
				els.end.val(initEndFormat);			// 填充终止日期
			};
			
			//var doUpdateTime = setInterval(updateTime, 1000);
			
			
			// 验证手机
			els.mobileTel.bind({
				blur: function() {
					var val = $(this).val();
					if (val != '') {
						check.mobile(val);
					}
				},
				focus: function() {
					// 清空提示
					els.mobileTelTip.html('');
				}
			});
			
			// 验证固话
			$('#fixed1, #fixed2, #fixed3').bind({
				blur: function() {
					var valA = els.telPartA.val(),
						valB = els.telPartB.val(),
						valC = els.telPartC.val(),
						valAll = valA + valB + valC;
					if (valAll != '') {
						check.tel(valA, valB, valC);
					}
				},
				focus: function() {
					// 清空提示
					els.telTip.html('');
				}
			});
			
			// 验证运单号
			els.waybill.bind({
				blur: function() {
					var val = $(this).val();
					if (val != '') {
						check.waybill(val);
					}
				},
				focus: function() {
					// 清空提示
					els.waybillTip.html('');
				}
			});
			
			// 验证货物数量
			els.quantity.bind({
				blur: function() {
					var val = $(this).val();
					if (val != '') {
						check.cartQuantity(val);
					}
				},
				focus: function() {
					// 清空提示
					els.quantityTip.html('');
				}
			});
			
			// 验证货物重量
			els.weight.bind({
				blur: function() {
					var _this = $(this),
						val = _this.val();
					if (val != '') {
						check.cartWeight(val, _this[0]);
					}
				
				},
				focus: function() {
					// 清空提示
					els.weightTip.html('');
				}
			});
			
			// 验证注意事项
			els.attention.bind({
				blur: function() {
					var val = $(this).val();
					if (val != '') {
						check.attention(val);
					}
				},
				focus: function() {
					// 清空提示
					els.attentionTip.html('');
				}
			});
			
			
			// 日期
			els.start.focus(function() {
				//clearInterval(doUpdateTime);
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
							
						//check.maxDate(dateVal);
						
						$dp.$('end').value = dateVal;
						$dp.$('end').focus();
					},
					dateFmt:'yyyy-MM-dd HH:mm',
					minDate: '%y-%M-%d',
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
			
			els.end.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择终止日期后触发校验
						var _this = this,
							dateVal = _this.value;
							
						check.date(dateVal);
						_this.blur();
					},
					dateFmt:'yyyy-MM-dd HH:mm',
					startDate: '%y-%M-%d',
					minDate: '#F{$dp.$D(\'start\')}',	// 终止日期大于起始日期
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});

			
			
			// “我要发货”表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'send_form',
				theme: 'yto',
				errorFocus: false
			});
			
			$.formValidator.initConfig({
				validatorGroup: '2',
				formID: 'send_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 收货联系人
			els.contact.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'name', dataType: 'enum', onError: tipsMsg.contactErr});
			
			// 收货地址
			els.area.
				formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 省
					els.area.next().
						formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 市
							var area = els.area.next().next();
							if(area.is(':visible')){
								area.
									formValidator({validatorGroup:'1', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: ' '}).	// 区
									inputValidator({min:1, onError: tipsMsg.areaErr});
									return '';
							}
							els.area.parent().remove('select:hidden');
							return ' ';
						}}).
						inputValidator({min:1, onError: tipsMsg.cityErr});
					return '';
				}}).
				inputValidator({min:1, onError: tipsMsg.provinceErr});
			
			// 发货地址
			els.area2.
				formValidator({validatorGroup:'2', tipID: 'area_tip2', onShow: '', onFocus: '', onCorrect: function() {	// 省
					els.area2.next().
						formValidator({validatorGroup:'2', tipID: 'area_tip2', onShow: '', onFocus: '', onCorrect: function() {	// 市
							var area = els.area2.next().next();
							if(area.is(':visible')){
								area.
									formValidator({validatorGroup:'2', tipID: 'area_tip2', onShow: '', onFocus: '', onCorrect: ' '}).	// 区
									inputValidator({min:1, onError: tipsMsg.areaErr});
									return '';
							}
							els.area2.parent().remove('select:hidden');
							/*if(area[0]!=null){
								els.area2.parent().remove(area[0]);
							}*/
							return ' ';
						}}).
						inputValidator({min:1, onError: tipsMsg.cityErr});
					return '';
				}}).
				inputValidator({min:1, onError: tipsMsg.provinceErr});
            
			// 收获人街道地址
			els.addressDetail.formValidator({validatorGroup:'1',tipID:'address_detailTip', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({empty:false, min: 1, max: 100, onErrorMin: tipsMsg.detailMinErr, onErrorMax: tipsMsg.detailMaxErr});
			
			// 发货人街道地址
			$('#senderChangeAddress').formValidator({validatorGroup:'2', tipID:'address_detailTip2', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({empty:false, min: 1, max: 100, onErrorMin: tipsMsg.detailMinErr, onErrorMax: tipsMsg.detailMaxErr});
			
			// 货物名称
			els.cartName.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({empty:false,min: 1, max: 100, onErrorMin: tipsMsg.cartMinErr, onErrorMax: tipsMsg.cartMaxErr});
			
			// 点击“确认发货”
			els.sendBtn.click(function(ev) {
				ev.preventDefault();
				var otherResult1 = $.formValidator.pageIsValid('1');
				var otherResult2 = $.formValidator.pageIsValid('2');
				
				var mobileVal = els.mobileTel.val(),
					telPartAVal = els.telPartA.val(),
					telPartBVal = els.telPartB.val(),
					telPartCVal = els.telPartC.val(),
					telVal = telPartAVal + telPartBVal + telPartCVal,
					waybillVal = els.waybill.val(),
					quantityVal = els.quantity.val(),
					weightVal = els.weight.val(),
					attentionVal = els.attention.val(),
					endVal = els.end.val();
				
				// 再次验证手机和固话
				if ( mobileVal != '' && telVal == '' ) {			// 手机填了，固话没填
					// 验证手机
					check.mobile(mobileVal);
				} else if ( mobileVal != '' && telVal != '' ) {		// 手机填了，固话填了
					// 验证手机
					check.mobile(mobileVal);
					// 验证固话
					check.tel(telPartAVal, telPartBVal, telPartCVal);
				} else if ( mobileVal == '' && telVal == '' ) {		// 手机没填，固话没填
					// 显示提示，至少填一项
					showIcon.show(els.mobileTelTip, tipsMsg.atLeastOne);
					showIcon.show(els.telTip, tipsMsg.atLeastOne);
				} else if ( mobileVal == '' && telVal != '' ) {		// 手机没填，固话填了
					// 验证固话
					check.tel(telPartAVal, telPartBVal, telPartCVal);
				}
				
				// 再次验证运单号
				if ( waybillVal != '' ) {
					check.waybill(waybillVal);
				}
				
				// 再次验证货物数量
				if ( quantityVal != '' ) {
					check.cartQuantity(quantityVal);
				}
				
				// 再次验证货物重量
				if ( weightVal != '' ) {
					check.cartWeight(weightVal, els.weight[0]);
				}
				
				// 再次验证注意事项
				if ( attentionVal != '' ) {
					check.attention(attentionVal);
				}
				
				// 再次验证日期
				if ( endVal != '' ) {
					check.date(endVal);
				}
				
				if(!otherResult2){
					var dialog = new Dialog();
					dialog.init({
						contentHtml:'发货地址修改未完成错误！',
						autoClose:5000,
						yes:function(){
							dialog.close();
							
						}
					});
					setTimeout(function(){
						dialog.close();
					},5000);
					$("#modifyAddress").fadeIn("normal");
				}
				if (
					(mobileResult || telResult) &&
					waybillResult && 
					quantityResult && 
					weightResult && 
					attentionResult && 
					dateResult && 
					otherResult1 &&
					otherResult2
					) {
					
					//订单号的生成
					var currentUserId = $('#currentUserId').val();
					var first = 'YTO';
					var d = new Date();
					var year =  d.getYear()+1900;
					var month = d.getMonth()+1;
					if(month<10){
						month = '0'+month;
					}
					var day =   d.getDate();
					if(day<10){
						day = '0'+day;
					}
					var hour =  d.getHours();
					if(hour<10){
						hour = '0'+hour;
					}
					var min =   d.getMinutes();
					if(min<10){
						min = '0'+min;
					}
					var second = d.getSeconds();
					if(second<10){
						second = '0'+second;
					}
					
					var now = year+month+day+hour+min+second;
					var txLogisticId = first+currentUserId+now;
				   
					var userType=config.userType;
				 	var userState=config.userState;
				 	var userField003=config.userField003; 
				 	if(userType=='1' && userState == 'TBA' && userField003!=9 ) {
					 		window.location.reload();
					}else{
						var art = new Dialog();
						art.init({
								iconImg: 'warning',
								top: '320px',
								contentHtml: '请核对订单信息,并记住<br>订单号'+txLogisticId,
								yesVal:'发货',
								noVal: '取消',
					            closeBtn: true, //为true等价于function(){}
					            lock: false,
					            yes: function () {
					            	//给省市县区赋值文字
									$("#x_prov").val(linkageSel.getSelectedData('name',0));
									$("#x_city").val(linkageSel.getSelectedData('name',1));
									$("#x_district").val(linkageSel.getSelectedData('name',2));
									$("#receiverAddressHid").val(els.addressDetail.val());
									
									//给省市县区赋值编码
									/*var selectedArr = linkageSel.getSelectedArr();
									$("#x_prov").val(selectedArr[0]);
									$("#x_city").val(selectedArr[1]);
									$("#x_district").val(selectedArr[2]);*/
					            	els.sendForm.attr("action","orderPlace!orderCreate.action?logicId="+txLogisticId);
					            	setTimeout(function(){
					            		els.sendForm.trigger('submit');
					    			},0);
					            	art.close();
					            },
					            no: function(){
					            	art.close();
					            }
					            
							});
					}
					
				}
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

				var starttime=$("#startHid0").val();
				var endtime=$("#endHid0").val();
				// 填开始时间
				if(starttime == undefined){
					els.dateStart.val(latestDate.format('yyyy-MM-dd'));
				}else{
					els.dateStart.val(starttime);
				}
				
				
				// 填结束时间
				if(endtime == undefined){
					els.dateEnd.val(sysDate.format('yyyy-MM-dd'));
				}else{
					els.dateEnd.val(endtime);
				}
			};
			lastestDays(6);
			
			// 日期
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
							
						check.maxDate(dateVal);
						
						
						if (dateResultB) {
							els.dateEnd.prop('disabled', false);
							$dp.$('date_end').focus();
						} else {
							_this.blur();
							_this.focus();
							els.dateEnd.prop('disabled', true);
						}
					},
					startDate: '#F{$dp.$D(\'date_end\')}',
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
						
					},
					startDate: '#F{$dp.$D(\'date_start\')}',
					minDate: '#F{$dp.$D(\'date_start\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
			
			// 收货人
			els.consignee.bind({
				blur: function() {
					var val = $(this).val();
					if (val != '') {
						check.consignee(val);
					}
				},
				focus: function() {
					// 清空提示
					els.consigneeTip.html('');
				}
			});
			
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				var startVal = els.dateStart.val(),
					endVal = els.dateEnd.val(),
					consigneeVal = els.consignee.val();
				check.maxDate(startVal);
				check.rangeDate(endVal);
				
				if ( consigneeVal != '' ) {
					check.consignee(consigneeVal);
				}
				
				//卖家完善信息或已激活后方可使用此功能
				var userType="${yto:getCookie('userType')}";
			 	var userState="${yto:getCookie('userState')}";
			 	var userField003="${yto:getCookie('field003')}"; 
			 	if(userType=='1' && userState == 'TBA' && userField003!=9 ) {
				 		window.location.reload();
				}else if ( dateResultB && nameResult ) {
					var actionStr = '';
					if(consigneeVal == ''){
						actionStr = "orderPlace!toMailNoBind.action?startTime="+startVal+"&endTime="+endVal;
					}else{
						actionStr = "orderPlace!toMailNoBind.action?startTime="+startVal+"&endTime="+endVal+"&receiverName="+consigneeVal;
					}
					els.recordForm.attr("action",actionStr);
					els.recordForm.trigger('submit');
				}
			});
			
			//翻页
			pagination.live('click',function(ev){
				els.currentPage.val($(this).attr("value"));
				var starttime=$("#startHid0").val();
				var endtime=$("#endHid0").val();
				var receiverName = $("#nameHid0").val();
				var page = els.currentPage.val();
				var actionStr = '';
				if(receiverName == ''){
					actionStr = "orderPlace!toMailNoBind.action?currentPage="+page+"&startTime="+starttime+"&endTime="+endtime;
				}else{
					actionStr = "orderPlace!toMailNoBind.action?currentPage="+page+"&startTime="+starttime+"&endTime="+endtime+"&receiverName="+receiverName;
				}
				els.recordForm.attr("action",actionStr);
				setTimeout(function(){els.recordForm.trigger('submit');},0);
				
			});
		};
		
		// 
		var saveEditShipNum = function() {
			//修改发货地址
			$('#edit_add').click(function(){
				/*var prov2 = $("#x_prov2").val();
				var city2 = $("#x_city2").val();
				var district2 = $("#x_district2").val();
				
				if(prov2==''){
					linkageSel2.changeValues(['', '', '']);
				}else if(city2!=''&&district2==''){
					linkageSel2.changeValuesByName([prov2, city2]);
				}else {
					linkageSel2.changeValuesByName([prov2, city2, district2]);
				}*/
				
				$("#modifyAddress").fadeIn("normal");
			});
			
			//保存发货地址
			$('#save_add').click(function(){
				var otherResult = $.formValidator.pageIsValid('2');
				
				if (!otherResult) {
			        return false;
			    }
			    $("#senderChangeAddress").val($.trim($("#senderChangeAddress").val()));
			    var prov2 = $("#x_prov2").val(linkageSel2.getSelectedData('name',0));
			    var city2 = $("#x_city2").val(linkageSel2.getSelectedData('name',1));
			    var district2 = $("#x_district2").val(linkageSel2.getSelectedData('name',2));
			    var ads= ($(this).parent().parent().find('select:visible').length === 3) ? prov2.val()+","+city2.val()+","+district2.val()+","+$("#senderChangeAddress").val() : prov2.val()+","+city2.val()+","+$("#senderChangeAddress").val();
			    var tempId = $('#tempId').val();
			    var mobile = $('#senderMobile').val();
			    var senderName = $('#senderName').val();
			    $("#address1").html(ads);
				$("#senderAddress").val(ads);
				$("#modifyAddress").fadeOut("normal");
			    
			    //保存到数据库
			    $.ajax({
			    	dataType:'json',
					async:true,
					url : 'orderPlace!eidtTemp.action',
					type :'post',
					data:{
						address:ads,
						tempId:tempId,
						mobile:mobile,
						senderName:senderName,
						tips:'3'
					},
					success:function(){
						$('.modifyAddressHid').remove();
						$('.modifyEm').html(ads+'&nbsp;');
					}
				});
			});
			
			//取消修改地址
			$('#cancel_add').click(function(){
				$("#modifyAddress").fadeOut("normal");
			});
			
			//禁运品数量
			$('#ban_count').click(function(){
				var art = new Dialog();
				art.init({
					contentHtml: '<div id="ban_dialog">'
					+'<h4>违禁品说明清单：</h4>'
					+'<p>1、 各类武器、弹药：如枪支、子弹、炮弹、手榴弹、地雷、炸弹等。</p>'
					+'<p>2、 各类易爆炸性物品：如雷管、炸药、火药、鞭炮等。</p>'
					+'<p>3、 各类易燃烧性物品，包括液体、气体和固体。如汽油、煤油、桐油、酒精、生漆、柴油、气雾剂、气体打火机、瓦斯气瓶、磷、硫磺、火柴等。</p>'
					+'<p>4、 各类易腐蚀性物品：如火硫酸、盐酸、硝酸、有机溶剂、农药、双氧水、危险化学品等。</p>'
					+'<p>5、 各类放射性元素及容器：如铀、钴、镭、钚等。</p>'
					+'<p>6、 各类烈性毒药：如铊、氰化物、砒霜等。</p>'
					+'<p>7、 各类麻醉药物：如鸦片（包括罂粟壳、花、苞、叶）、吗啡、可卡因、海洛因、大麻、冰毒、麻黄素及其它制品等。</p>'
					+'<p>8、 各类生化制品和传染性物品：如炭疽、危险性病菌、医药用废弃物等。</p>'
					+'<p>9、 各种危害国家安全和社会政治稳定以及淫秽的出版物、宣传品、印刷品等。</p>'
					+'<p>10、各种妨害公共卫生的物品：如尸骨、动物器官、肢体、未经硝制的兽皮、未经药制的兽骨、各种活的动物、鲜鱼、鲜肉、液体等。</p>'
					+'<p>11、国家法律、法规、行政规章明令禁止流通、寄递或进出境的物品，如国家秘密文件和资料、国家货币及伪造的货币和有价证券、烟草、古董、金银珠宝、贵重金属、仿真武器、管制刀具、珍贵文物、濒危野生动物及其制品等。</p>'
					+'<p>12、包装不妥，可能危害人身安全、污染或者损毁其他寄递件、设备的物品等。</p>'
					+'<p>13、各寄达国（地区）禁止寄递进口的物品等。</p>'
					+'<p>14、国家法律法规规定的其他禁止寄递的物品。</p>'
					+'</div>',
				    yesVal: '关闭',
				    yes: function() {
						art.close();
					}
				});
			});
			
			var art = new Dialog();
			if($('#processResultValue').val()=="0"){
				art.init({id:"send_fail",icon: 'error',top:'320px', contentHtml: '发货失败!',lock:false,maskOpacity: 0.1,yes:true,yes: function() {art.close();}
				});
			}else if($('#processResultValue').val()=="1"){
				art.init({id:"send_success",icon: 'succeed',top:'320px', contentHtml: '发货成功!',time:3,lock:false,maskOpacity: 0.1,yes:true,yes: function() {art.close();}});
			}
			
			//保存
			$('#tab_panel_b .td_a .fs02').live('click',function(ev) {
				var _this = $(this),
					inputText = $('.input_text', _this.parent()).val(),
					index = $('.index', _this.parent()).val();
				//运单号的检查
				var art = new Dialog();
				if(inputText==''){
					art.init({
						contentHtml:'请填写运单号!',
						iconType:'warn',
						autoClose: 3000,
						yes:function(){
							art.close();
						}
						
					});
					flag = false;
					return false;
				}
				if(!checkMailNo(inputText)){
					art.init({
						contentHtml:'运单号格式错误!',
						iconType:'warn',
						autoClose: 3000,
						yes:function(){
							art.close();
						}
						
					});
					flag=false;this.focus();
					return false;
				}
				
				//ajax update
				var id = $('#id'+index).val();
				var txLogisticId = $('#txLogisticId'+index).val();
				var clientId = $('#clientId'+index).val();
				//更新数据库
				$.ajax({
					url:'bindedOrUpd.action',
					data:{
						mailNo:inputText,
						id:id,
						txLogisticId:txLogisticId,
						clientId:clientId
					},
					success:function(data){
						$('#mailNo'+index).val(inputText);
						_this.parent().html('<a href="#" class="mailno" val="'+inputText+'">'+inputText+'</a>'+'<a href="#" class="fs01">编辑</a>'+
											'<input type="hidden" value="'+index+'" class="index" />');
					}
				});
			});
			
			//编辑
			$('#tab_panel_b .td_a .fs01').live('click',function(ev){
				var _this = $(this),
					index = $('.index', _this.parent()).val(),
					mailNo = $('#mailNo'+index).val();
				_this.parent().html('<input type="text" class="input_text" value="'+mailNo+'" maxlength="10" size="10" />'+'<a href="#" class="fs02">保存</a>'+
									'<input type="hidden" value="'+index+'" class="index" />');
			
			});
			
			//撤销
			$('.mailNo_cancel').click(function(ev){
				var _this = $(this),
					index = $('.index', _this.parent()).val(),
					orderId = $('#id'+index).val(),
					txLogisticId = $('#txLogisticId'+index).val(),
					clientId = $('#clientId'+index).val(),
					startTime = $('.startTime', _this.parent()).val(),
					endTime = $('.endTime', _this.parent()).val(),
					receiverName =	$('.receiverName', _this.parent()).val();
				
				art.init({
					top: '320px',
					contentHtml:'你确认要撤销吗?',
					yesVal:'确定',
					noVal: '取消',
					yes:function() {
						$('#recordForm').attr("action","cancelOrder!cancelOrder.action?id="+orderId+"&startTime="+startTime+"&endTime="+endTime+"&receiverName="+receiverName+"&txLogisticId="+txLogisticId+"&clientId="+clientId);
						$('#recordForm').trigger('submit');
						art.close();
					},
					no:function(){
						art.close();
					}
				});
			});
	};
	
	var checkMailNo = function(value) {
		if(value=='')return true;
		var singleMailReg =/^[A-Za-z0-9]{1}\d{9}$/;
	    return singleMailReg.test(value);
	};
	
	
	// 修改手机号
	var modifyMobile = function() {
		var mobileReg = /^13[0-9]{9}|15[012356789][0-9]{8}|18[0256789][0-9]{8}|147[0-9]{8}$/;
		
		// 点击“修改”手机
		$('.edit_mobile').live('click', function(ev) {
			ev.preventDefault();
			
			var _this = $(this);
			
			$('em', _this.parents('p')).replaceWith('<input type="text" class="input_text" id="new_mobile_input" />  <span id="new_mobile_inputTip"></span>');
			_this.replaceWith('<a href="javascript:;" class="save_mobile">保存</a>&nbsp;&nbsp;<a href="javascript:;" class="cancel_mobile">取消</a>');
			
		});
		
		// 点击“保存”手机
		$('.save_mobile').live('click', function(ev) {
			ev.preventDefault();
			
			var _this = $(this),
				newMobileVal = $('#new_mobile_input').val(),
				tempId = $('#tempId').val(),
				senderName = $('#senderName').val(),
				address = $('.modifyAddressHid').text();
			
			if (newMobileVal == '') {
				showIcon.error($('#new_mobile_inputTip'), '手机号不能为空');
			} else if (!mobileReg.test(newMobileVal)) {
				showIcon.error($('#new_mobile_inputTip'), '手机号格式错误');
			} else {
				$.ajax({
					url: 'orderPlaceSubmit_eidtTemp.action',
					type: 'POST',
					data:{
						mobile:newMobileVal,
						tempId:tempId,
						senderName:senderName,
						address:address,
						tips:'1'
					},
					cache: false,
					success: function() {
						$('input[name=senderMobile]',_this.parents('p')).val(newMobileVal);
						$('#new_mobile_input').replaceWith('<em>' + newMobileVal + '</em>');
						_this.parent().replaceWith('<span><a href="javascript:;" class="edit_mobile">修改</a></sapn>');
						$('#new_mobile_inputTip').html('');
					}
				})
			}
		});
		
		$('.cancel_mobile').live('click',function(){
			var _this = $(this),
				oldMobile = $('input[name="senderMobile"]',_this.parents('p')).val();
			
			$('#new_mobile_input', _this.parents('p')).replaceWith("<em>"+oldMobile+"</em>");
			$('#new_mobile_inputTip', _this.parents('p')).replaceWith("");
			_this.parent().replaceWith('<span><a href="javascript:;" class="edit_mobile">修改</a></span>');
		});
	};
	
	
	// 修改姓名
	var modifyName = function() {
		
		// 点击“修改”姓名
		$('.edit_name').live('click', function(ev) {
			ev.preventDefault();
			
			var _this = $(this);
			
			$('em', _this.parents('p')).replaceWith('<input type="text" class="input_text" id="new_name_input" />  <span id="new_name_inputTip"></span>');
			_this.replaceWith('<a href="javascript:;" class="save_name">保存</a>&nbsp;&nbsp;<a href="javascript:;" class="cancel_name">取消</a>');
			
		});
		
		// 点击“保存”姓名
		$('.save_name').live('click', function(ev) {
			ev.preventDefault();
			
			var _this = $(this),
				newNameVal = $('#new_name_input').val()
				tempId = $('#tempId').val(),
				mobile = $('#senderMobile').val(),
				address = $('.modifyAddressHid').text();
			
			if (newNameVal == '') {
				showIcon.error($('#new_name_inputTip'), '发货人姓名不能为空');
			} else {
				$.ajax({
					url: 'orderPlaceSubmit_eidtTemp.action',
					type: 'POST',
					data:{
						mobile:mobile,
						tempId:tempId,
						senderName:newNameVal,
						address:address,
						tips:'2'
					},
					cache: false,
					success: function() {
						$('input[name=senderName]',_this.parents('p')).val(newNameVal);
						$('#new_name_input').replaceWith('<em>' + newNameVal + '</em>');
						_this.parent().replaceWith('<span><a href="javascript:;" class="edit_name">修改</a></sapn>');
						$('#new_name_inputTip').html('');
					}
				})
				
			}
		});
		
		$('.cancel_name').live('click',function(){
			var _this = $(this),
				senderName = $('input[name="senderName"]',_this.parents('p')).val();
			
			$('#new_name_input', _this.parents('p')).replaceWith("<em>"+senderName+"</em>");
			$('#new_name_inputTip', _this.parents('p')).replaceWith("");
			_this.parent().replaceWith('<span><a href="javascript:;" class="edit_name">修改</a></span>');
		});
	};
	
	return {
		init: function() {
			var index = $('.checkTab').val();
			ytoTab.init(index);
			selectArea();
			form();
			saveEditShipNum();
			//shipNumDetail();
			modifyMobile();
			modifyName();
		}
	}
	
  })();
	
	var linkageSel;
	var linkageSel2;
	delivery.init();
})



