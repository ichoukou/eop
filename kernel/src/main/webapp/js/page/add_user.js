$(function() {
	var addUser = (function() {

		var winParams = window.params || {};
		
		// 配置
		var config = {
			hasMsgAction: winParams.checkWawaUrl || ''				// 旺旺唯一性验证地址
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
		
		var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#address'],
				autoLink: false
			};
			var linkageSel = new LinkageSel(area);
			
		// 表单
		var form = function(){
			// 元素集合
			var els = {
				addForm: $('#addUser_form'),
				userName: $('#name'),
				wawa: $('#wawa'),
				volume: $('#volume'),
				money: $('#money'),
				date: $('#date'),
				qq: $('#qq'),
				phone: $('#phone'),
				zip: $('#zip'),
				address: $('#address'),
				d_address: $('#d_address'),
				ps: $('#ps'),
				saveBtn: $('.opts .save'),
				buyersId:$('#buyersId')
			};

			// 文案
			var msg = {
				wawaEmptyErr: '请重新输入旺旺号',
				phoneEmptyErr: '请重新输入手机号',
				d_addressEmptyErr: '请输入详细地址',
				userNameFormatErr: '卖家姓名输入有误',
				volumeFormatErr: '请输入正整数',
				moneyFormatErr: '请输入正实数',
				dateFormatErr: '日期输入有误',
			    qqFormatErr: '请输入正确格式的qq号',
				phoneFormatErr: '手机号格式有误',
				zipFormatErr: '请输入正确格式的邮编号',
				provinceErr: '请选择省',
				cityErr: '请选择市',
				areaErr: '请选择区'
			};

			els.d_address.hide();
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'addUser_form',
				theme: 'yto',
				errorFocus: false
			});

			// 卖家姓名
			els.userName.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: ['name', 'empty'], dataType: 'enum', onError: msg.userNameFormatErr});

			var wawaResult = null;
			
			// 旺旺
			els.wawa.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min:5,onError:msg.wawaEmptyErr}).
				functionValidator({
					fun: function() {
						
						$.ajax({
							dataType: 'text',
							url: config.hasMsgAction+'?memberId='+ els.buyersId.val() + '&buyers.buyerAccount=' + els.wawa.val(),
							type: 'GET',
							cache: false,
							success: function(data) {
								
								if( data.indexOf("true") > 0 ) {
									wawaResult = true;
								} else {
									wawaResult = false;
								}
								
								setTimeout(function() {
									if (wawaResult) {
										showIcon.correct($('#wawaTip'), ' ');
									} else {
										showIcon.error($('#wawaTip'), '该旺旺号不可用');
									}
								}, 50);
							}
						});
					},
					onError: ''
				});
			
			// 交易量
			els.volume.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: ['intege1', 'empty'], dataType: 'enum', onError: msg.volumeFormatErr});

			// 交易额
			els.money.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['intege1', 'decmal4'], dataType: 'enum', onError: msg.moneyFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val != '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							$(el).val(formatPrice);
						}
					}
				});

			// 上次交易时间
			els.date.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
					},
					//minDate: '%y-%M-{%d-31*3}',		// 最小时间：3个月前
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			els.date.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: ['date', 'empty'], dataType: 'enum', onError: msg.dateFormatErr});

			// QQ
			els.qq.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: ['qq', 'empty'], dataType: 'enum', onError: msg.qqFormatErr});

			// 手机号
			els.phone.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min:1,onError:msg.phoneEmptyErr}).
				regexValidator({regExp: 'mobile', dataType: 'enum', onError: msg.phoneFormatErr});

			// 邮政编号
			els.zip.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: ['zipcode', 'empty'], dataType: 'enum', onError: msg.zipFormatErr});
			// 联系地址
			els.address.
				formValidator({validatorGroup:'1', tipID: 'addressTip', onShow: '', onFocus: '', onCorrect: function() {	// 省
					els.address.next().
						formValidator({validatorGroup:'1', tipID: 'addressTip', onShow: '', onFocus: '', onCorrect: function() {	// 市
							els.address.next().next().
								formValidator({validatorGroup:'1', tipID: 'addressTip', onShow: '', onFocus: '', onCorrect: function() {
									els.d_address.empty().show();
									return " ";
							}}).	// 区
								functionValidator({
									fun: function(val, el){
										if(val === ""){
											els.d_address.empty().hide();
											els.d_address.next('span').hide();
										}
									}
								}).
								inputValidator({min:1, onError: msg.areaErr});
							return '';
						}}).
						functionValidator({
							fun: function(val, el){
								if(val === ""){
									els.d_address.empty().hide();
									els.d_address.next('span').hide();
								}
							}
						}).
						inputValidator({min:1, onError: msg.cityErr});
					return '';
				}}).
				functionValidator({
					fun: function(val, el){
						if(val === ""){
							els.d_address.empty().hide();
							els.d_address.next('span').hide();
						}
					}
				}).
				inputValidator({min:1, onError: msg.provinceErr});
			
			// 详细地址
			els.d_address.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: function(){
					return els.d_address.is(':hidden') ? '' : ' ';
				}}).
				functionValidator({
					fun: function(val, el) {
						if (els.address.parent().find('select:visible').length === 3 && val === '' && $(el).is(':visible')) {
							return false;
						}
					},
					onError: msg.d_addressEmptyErr
				});

			// 保存新会员
			els.saveBtn.click(function(ev){
				ev.preventDefault();
				var data=linkageSel.getSelectedDataArr('name').toString();
				var adds=data.split(',');
				$('#receiverProvince').val(adds[0]);
				$('#receiverCity').val(adds[1]);
				$('#receiverDistrict').val(adds[2]);
				
				var otherResult = $.formValidator.pageIsValid(1);
				
				var timer = setInterval(function(){
					if(wawaResult != null){
						if (otherResult && wawaResult) {
							clearInterval(timer);
							els.addForm.trigger("submit");
						}
					}
				}, 50);
				
			});	
		};
		

		
		
		// 地区多级联动
		var selectArea = function() {

			setTimeout(function(){
				if($("#receiverProvince").val() && $("#receiverCity").val() && $("#receiverDistrict").val()){
						linkageSel.changeValuesByName([$("#receiverProvince").val(), $("#receiverCity").val(), $("#receiverDistrict").val()]);
					//显示详细地址
					$('#d_address').css({'display':'inline'});
				}
				else if($("#receiverProvince").val() && $("#receiverCity").val()){
					linkageSel.changeValuesByName([$("#receiverProvince").val(), $("#receiverCity").val()]);
				}
			},0);
			//省:请选择
			setTimeout(function() {
				$('#address').change(function() {
					if ($(this).val() === '') {
						$('#d_address').hide();
					} 
				});	
			}, 500)
			
			//市:请选择
			setTimeout(function() {
				$('#address').next().change(function() {
					if ($(this).val() === '') {
						$('#d_address').hide();
					} 
				});	
			}, 500);
			//区：请选择
			setTimeout(function() {
				$('#address').next().next().change(function() {
					if ($(this).val() === '') {
						$('#d_address').hide();
					} else {
						
						$('#d_address').val('').show();
					}
				});	
			}, 500);
			
		};
		return {
			init: function() {
				form();
				if(null!=$('#buyersId').val() && $('#buyersId').val()!=''){
					selectArea();
				}
			}
		}
	})();
	
	addUser.init();
})