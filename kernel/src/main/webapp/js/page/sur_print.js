/**
 * 面单打印
**/
$(function() {
	var surPrint = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			curTab: parseInt($('#cur_tab').val(), 10) || 0,			// 默认 Tab
			bindBranch: winParams.bindBranch,						// 是否绑定了网点
			allowPrintUrl: winParams.allowPrintUrl || '',			// 允许网点打印请求
			iShipNumForm: winParams.iShipNumForm || '',				// 递增运单号请求
			saveShipNum: winParams.saveShipNum || '',				// 保存运单号请求
			getTemplate: winParams.getTemplate || ''				// 获取客户模板
		};
		
		// 元素集合
		var els = {
			// 批量打印 tab
			batchSearForm:$('#batch_search'),batchSearBtn:$('#batch_sear_btn'),orderby:$('#orderby'),mergerOrdersBtn:$('#merger_orders_btn'),mergerSel:$('#merger_select'),mergerOrder:$('#merger_order'),cancelMerger:$('#cancel_merger'),orderIdsManual:$('.order_ids_manual'),orderIdsAuto:$('.order_ids_auto'),mergerFormA:$('#merger_form_a'),fillOutBtn:$('#fill_out_btn'),tabAPanel:$('#tab_panel_a'),dateStart:$('#date_start'),dateEnd:$('#date_end'),batchInputRadio:$('#batch_search .input_radio'),printExpressBtn:$('.print_express_btn'),printInvoiceBtn:$('.print_invoice_btn'),printExpressForm:$('#print_express_form'),printInvoiceForm:$('#print_invoice_form'),expressTemplate:$('#express_template'),expressOrderId:$('#express_orderid'),invoiceOrderId:$('#invoice_orderid'),lastestA:$('#lastest_a'),lastestB:$('#lastest_b'),lastestC:$('#lastest_c'),dateTip:$('#date_tip'),orderNum:$('#order_num'),waybillTemplate:$('#waybill_template'),waybillTemplateTip:$('#waybill_templateTip'),startDateVal:$('.start_date_val'),endDateVal:$('.end_date_val'),orderTypeVal:$('.order_type_val'),selectVal:$('.select_val'),select3Val:$('.select3_val'),select2Val:$('.select2_val'),orderbyVal:$('.orderby_val'),showNumVal:$('.show_num_val'),wayTemVal:$('.waybill_template_val'),orderNumVal:$('.order_num_val'),toPageNum:$('.to_page_num'),select:$('#select'),select2:$('#select2'),select3:$('#select3'),orderType:$('#order_type'),
			// 待发货 tab
			searShipBtn:$('#sear_ship_btn'),searShipForm:$('#sear_ship_form'),orderby2:$('#orderby2'),lastestG:$('#lastest_g'),lastestH:$('#lastest_h'),lastestI:$('#lastest_i'),dateStart3:$('#date_start3'),dateEnd3:$('#date_end3'),dateTip3:$('#date_tip3'),orderIdsB:$('.order_ids_b'),batchBtn:$('.batch_btn'),shipVolForm:$('#ship_vol_form'),delItemForm:$('#del_item_form'),delItemBtn:$('#del_item_btn'),tabBPanel:$('#tab_panel_b'),searShipInput:$('#sear_ship_input'),waitInputRadio:$('#sear_ship_form .input_radio'),startDate3Val:$('.start_date3_val'),endDate3Val:$('.end_date3_val'),orderby2Val:$('.orderby2_val'),showNum2Val:$('.show_num2_val'),searShipInputVal:$('.sear_ship_input_val'),statusTip:$('.status_tip'),
			// 已发货 tab
			sendSearchForm:$('#send_search'),searShipBtn2:$('#sear_ship_btn2'),orderby3:$('#orderby3'),tabCPanel:$('#tab_panel_c'),sendInputRadio:$('#send_search .input_radio'),dateStart2:$('#date_start2'),dateEnd2:$('#date_end2'),lastestD:$('#lastest_d'),lastestE:$('#lastest_e'),lastestF:$('#lastest_f'),dateTip2:$('#date_tip2'),searShipInput2:$('#sear_ship_input2'),startDate2Val:$('.start_date2_val'),endDate2Val:$('.end_date2_val'),orderby3Val:$('.orderby3_val'),showNum3Val:$('.show_num3_val'),searShipInput2Val:$('.sear_ship_input2_val'),
			// 公用元素
			tabTriggerLi:$('.tab_triggers li'),showNumEl:$('#show_num'),tdCheck:$('.td_check'),mergerOrNot:$('.merger_or_not'),modifyShipNum:$('.modify_shipnum'),saveShipNum:$('.save_shipnum'),cancelShipNum:$('.cancel_shipnum'),pageNum:$('.page_num'),pageTxt:$('.page_txt'),showMessage:$('#showMessage')
		};

		// 文案
		var msg = {
			save: '保存',
			cancel: '取消',
			modify: '修改',
			startShipNum: '起始运单号：',
			inValue: '递增值：',
			notBindBranch: '您还未跟网点绑定，请回到“我的易通”→绑定网点',
			noCheck: '您还没有选择订单，默认将<收件地址，收件人姓名，收件联系方式>相同的订单自动合并~',
			oneCheck: '手动合并，至少勾选两条订单哦^_^~',
			notMerger: '该订单不是合并订单，无法取消合并╮(╯_╰)╭',
			hasMergered: '已合并的订单无法重复合并，请先取消合并',
			notSameInfo: '收货地址、联系方式、收件人不一致，确认是否合并?_?',
			cancelDef: '您还没有选择订单，默认将取消所有的合并订单！',
			hasNotMergered: '有未合并的订单，不能取消合并',
			atLeastOne: '至少勾选一条订单',
			overTen: '合并不能超过十条',
			noEmpty: '运单号不能为空',
			shipNumFormatErr: '运单号格式错误',
			allowSure: '确定后，允许网点为您打印快递单，同时能看到您在线下单的订单信息！',
			checkInfo: '请核对您的信息，发货后将无法修改任何信息',
			mergerIconTextA: '已合并',
			mergerIconTextB: '条订单',
			notShowHere: '点击确定后该发货单将不在待发货区域显示！',
			saveToDelivery: '还有运单号未保存哦，请保存后发货~',
			saveToPrint: '还有运单号未保存哦，请保存后打印~',
			dateErr: '请选择日期',
			maxDateErr: '只能查近三个月的数据',
			rangeDateErr: '只能查30天内的数据',
			dateEmptyErr: '请选择开始日期',
			shipOrderFormatErr: '格式错误',
			noSearEmpty: '查询内容不能为空',
			IncrementErr: '递增运单号后超出范围',
			clientEmpty: '1、网点打印需客户绑定 2、客户需勾选“允许网点打印”',
			orderShipNum: '运单号/订单号',
			prevPage: '上一页',
			nextPage: '下一页',
			newTemplate: '请新建快递单模板',
			seller: '(客)',
			notPrint: '网点无法为您打印',
			noOrder: '未找到任何订单哦╮(╯_╰)╭',
			sysErr: '系统繁忙，有部分订单无法发货，请稍后再试！',
			overTimeErr: '淘宝授权已过期，请重新登录！',
			loading: '正在加载中……'
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
		
		// 加载中弹层
		var loadingDialog = function() {
			var loadDialog = new Dialog();
			
			loadDialog.init({
				contentHtml: msg.loading
			});
		};
		
		// 验证方法
		var check = {
			/**
			 * @description 检查运单号是否有空值
			 * @param els {Element} 要检查的 input 输入框元素集合
			 * @return {Boolean} true 有空值 / false 没有空值
			**/
			hasEmptyShipNum: function(els) {
				for (var i=0, l=els.length; i<l; i++) {
					if (els.eq(i).val() == '') {
						return true;
					}
				}
				return false;
			},
			/**
			 * @description 检查运单号格式正确
			 * @param els {Element} 要检查的 input 输入框元素集合
			 * @return {Boolean} true 格式有错 / false 格式没错
			**/
			hasFormatErrShipNum: function(els) {
				// 允许为空 或者 首位为字母或数字，后面9位数字
				var shipmentNumReg = /^(|[A-Za-z0-9]{1}\d{9})$/;
				for (var i=0, l=els.length; i<l; i++) {
					if (!shipmentNumReg.test(els.eq(i).val())) {
						return true;
					}
				}
				return false;
			},
			/**
			 * @description 检查订单是否有已合并
			 * @param els {Element} class="merger_or_not" 隐藏域集合
			 * @return {Boolean} true 有已合并运单 / false 没有已合并运单
			**/
			hasMergered: function(els) {
				for (var i=0, l=els.length; i<l; i++) {
					if (parseInt(els.eq(i).val(), 10) >= 2) {
						return true;
					}
				}
				return false;
			},
			/**
			 * @description 检查订单是否有未合并
			 * @param els {Element} class="merger_or_not" 隐藏域集合
			 * @return {Boolean} true 有未合并运单 / false 没有未合并运单
			**/
			hasNotMergered: function(els) {
				for (var i=0, l=els.length; i<l; i++) {
					if (parseInt(els.eq(i).val(), 10) == 0) {
						return true;
					}
				}
				return false;
			},
			/**
			 * @description 检查运单号是否有未保存
			 * @param els {Element} class="input_text" 元素集合
			 * @return {Boolean} true 有未保存的运单 / false 没有未保存的运单
			**/
			hasNotSavedShipNum: function(els) {
				if (els.length > 0) {
					return true;
				} else {
					return false;
				}
			},
			/**
			 * @description 检查收件地址、姓名、联系方式是否有不一致
			 * @param arr {Array} 要检查的数组
			 * @return {Boolean} true 都一样 / false 有不一样
			**/
			isAllSame: function(arr) {
				var can = arr[0];
				for (var i=1, l=arr.length; i<l; i++) {
					if (arr[i] !== can) {
						return false;
					}
				}
				return true;
			},
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

					if (dateGap > 30) {
						return false;
					} else {
						return true;
					}
				}
			}
		};
		
		// 默认输入框文案
		var defText = function() {
			els.orderNum.defaultTxt(msg.orderShipNum);
			els.searShipInput.defaultTxt(msg.orderShipNum);
			els.searShipInput2.defaultTxt(msg.orderShipNum);
		};
		
		// 全选
		var checkAll = function() {
			// 批量打印 tab
			$('.check_all', els.tabAPanel).checkAll( $('input[type="checkbox"]', els.tabAPanel) );
			// 待发货 tab
			$('.check_all', els.tabBPanel).checkAll( $('input[type="checkbox"]', els.tabBPanel) );
		};
		
		// 表格
		var table = function() {
			// 如果没有查询到订单
			var showMessageVal = els.showMessage.val();
			
			switch(showMessageVal) {
				case '0':
					$('#tab_panel_a .table tbody').html('<tr><td colspan="7" align="center">' + msg.noOrder + '</td></tr>');
				break;
				
				case '1':
					$('#tab_panel_b .table tbody').html('<tr><td colspan="6" align="center">' + msg.noOrder + '</td></tr>');
				break;
				
				case '2':
					$('#tab_panel_c .table tbody').html('<tr><td colspan="5" align="center">' + msg.noOrder + '</td></tr>');
				break;
			}
			
			// 每行 tr
			$('.table tbody tr').each(function() {
				var _this = $(this),
					firstTd = $('td', _this).first(),
					lastTd = $('td', _this).last(),
					thisTdAdd = $('.td_address', _this),
					len = 18,
					showTitle = showHtml = $.trim(thisTdAdd.html());
				
				// 每个 tr 的第一个和最后一个 td 边框
				firstTd.css({
					borderLeft: '1px solid #8CA2D3'
				});
				lastTd.css({
					borderRight: '1px solid #8CA2D3'
				});
				
				// 地址截断
				if (showHtml.length > len) {
					showHtml = showHtml.substr(0, len) + '……';
				}
				
				thisTdAdd.html(showHtml).attr('title', showTitle).show();
			});
			
			// 点击表格行
			$('.tab_panels .list_tr').click(function(ev) {
				var _this = $(this),
					nextTd = $('.detail_td', _this.next()),
					nextTdDisplay = nextTd.css('display'),
					targetClass = ev.target.className;
					//targetTag = ev.target.tagName.toLowerCase();
				if (
					targetClass.indexOf('do_link') == -1 &&
					targetClass.indexOf('td_check') == -1 && 
					targetClass.indexOf('input_text') == -1
					) {
					if (nextTdDisplay == 'none') {
						nextTd.slideDown();
					} else {
						nextTd.slideUp();
					}
				}
			});
			
			// 点击加号全部展开
			$('.tab_panel .fold_unfold').click(function(ev) {
				var _this = $(this),
					curTabPanel = _this.parents('.tab_panel'),
					tdDisplay = $('.detail_td', curTabPanel).css('display');
				
				if (tdDisplay == 'none') {
					$('.table tbody .detail_td', curTabPanel).slideDown('fast', function() {
						_this.removeClass('unfold').addClass('fold');
					});
				} else {
					$('.table tbody .detail_td', curTabPanel).slideUp('fast', function() {
						_this.removeClass('fold').addClass('unfold');
					});
				}
			});
			
			// 运单号操作
			var saveLinkHtml = '<a title="' + msg.save + '" class="save_shipnum do_link" href="javascript:;">' + msg.save + '</a>',
				modifyLinkHtml = '<a title="' + msg.modify + '" class="modify_shipnum do_link" href="javascript:;">' + msg.modify + '</a>',
				cancelLinkHtml = '<a title="' + msg.cancel + '" class="cancel_shipnum do_link" href="javascript:;">' + msg.cancel + '</a>';
				
			// 保存
			els.saveShipNum.live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
					thisTd = _this.parent(),
					thisInput = $('.input_text', thisTd),
					defShipNumEl = $('.def_shipnum', thisTd),
					thisInputVal = thisInput.val(),			// 输入的值
					thisOrderId = $('.order_id', thisTd.parent()).val(),	// 运单 ID
					reg = /^[A-Za-z0-9]{1}\d{9}$/,			// 运单号验证正则
					cancelLink = $('.cancel_shipnum', thisTd);
				
				// 先校验
				if (reg.test(thisInputVal)) {
					thisInput.removeClass('error_status');
					// 发异步请求
					$.ajax({
						type: 'POST',
						url: config.saveShipNum,
						data: {
							saveShipNum: thisOrderId + ',' + thisInputVal
						},
						success: function() {
							// 修改隐藏域的值
							defShipNumEl.val(thisInputVal);
							thisInput.replaceWith('<span class="shipnum">' + defShipNumEl.val() + '</span>');
							cancelLink.remove();
							_this.replaceWith(modifyLinkHtml);
						}
					});
				} else {
					thisInput.addClass('error_status');
				}

			});
			
			// 修改
			els.modifyShipNum.live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
					thisTd = _this.parent(),
					defShipNumVal = $('.def_shipnum', thisTd).val(),		// 默认运单号
					shipNum = $('.shipnum', thisTd);
					
					shipNum.replaceWith('<input type="text" value="' + defShipNumVal + '" class="input_text" />');
					_this.replaceWith(saveLinkHtml + cancelLinkHtml);
			});
			
			// 取消
			els.cancelShipNum.live('click', function(ev) {
				ev.preventDefault();
				var _this = $(this),
					thisTd = _this.parent(),
					defShipNumVal = $('.def_shipnum', thisTd).val(),		// 默认运单号
					thisInput = $('.input_text', thisTd),
					saveLink = $('.save_shipnum', thisTd);
					
					thisInput.replaceWith('<span class="shipnum">' + defShipNumVal + '</span>');
					saveLink.remove();
					_this.replaceWith(modifyLinkHtml);
				
			});
			
			// 是否合并 icon
			els.mergerOrNot.each(function() {
				var _this = $(this),
					val = parseInt(_this.val(), 10);
				
				if (val >= 2) {
					$('<i class="merger_icon" title="' + msg.mergerIconTextA + ' ' + val + ' ' + msg.mergerIconTextB + '">' + val + '</i>').insertAfter($('.ordernum', _this.parent()));
				}
			});
		};
		
		// 允许网点打印
		var allowWdPrint = function() {
			var input = $('#allow_wd');
			
			input.change(function() {
				var _this = $(this),
					checkedVal = _this.prop('checked');
				
				
				if (checkedVal) {	// 选中复选框后
					if (config.bindBranch) {	// 如果已经绑定网点
						// 弹层提示
						var allowPrintDialog = new Dialog();
						allowPrintDialog.init({
							contentHtml: msg.allowSure,
							yes: function() {	// 点击“确定”再发请求
								$.ajax({
									type: 'GET',
									url: config.allowPrintUrl + 'isPrintFlag=1',
									data: 'r=' + Math.random(),
									success: function() {
										allowPrintDialog.close();
									}
								});
							},
							no: function() {	// 点击“取消”
								allowPrintDialog.close();		// 关闭弹层
								_this.prop('checked', false);	// 恢复复选框
							}
						});
					} else {					// 如果尚未绑定网点
						var bindBranchDialog = new Dialog();
						
						bindBranchDialog.init({
							contentHtml: msg.notBindBranch,
							closeBtn: true
						});
						
						_this.prop('checked', false);	// 恢复复选框
					}
				} else {	// 取消复选框后
					// 发送请求
					$.ajax({
						type: 'GET',
						url: config.allowPrintUrl + 'isPrintFlag=0',
						data: 'r=' + Math.random(),
						success: function() {
							var checkDialog = new Dialog();
							
							checkDialog.init({
								contentHtml: msg.notPrint,
								closeBtn: true
							});
						}
					});
				}
			})
		};
		
		// formValidator 向下兼容，易通重构时可以把这段去掉
		var formValidCompatible = function() {
			if ($.formValidatorBeta) {
				$.formValidator = $.formValidatorBeta;
				$.formValidator.initConfig = $.formValidator.initConfigBeta;
				$.fn.formValidator = $.fn.formValidatorBeta;
				$.fn.compareValidator = $.fn.compareValidatorBeta;
				$.fn.regexValidator = $.fn.regexValidatorBeta;
				$.fn.inputValidator = $.fn.inputValidatorBeta;
				$.fn.functionValidator = $.fn.functionValidatorBeta;
				$.fn.ajaxValidator = $.fn.ajaxValidatorBeta;
			}
		};
		
		// 表单
		var form = function() {
			// 设置默认值
			els.showNumEl.val(els.showNumVal.val());
			els.orderby.val(els.orderbyVal.val());
			els.orderby2.val(els.orderby2Val.val());
			els.orderby3.val(els.orderby3Val.val());
			els.waybillTemplate.val(els.wayTemVal.val());
			
			// “批量打印”表单校验
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'batch_search',
				theme: 'yto',
				errorFocus: false
			});
			
			// 订单查询
			els.orderNum.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				functionValidator({fun: function(val) {
					if (val == msg.orderShipNum) {
						els.orderNum.val('');
					}
				}}).
				regexValidator({regExp: ['shipmentNum', 'orderNum', 'empty'], dataType: 'enum', onError: msg.shipOrderFormatErr});
				
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
			
			// “待发货”表单校验
			$.formValidator.initConfig({
				validatorGroup: '2',
				formID: 'sear_ship_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 查询
			els.searShipInput.
				formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).functionValidator({fun: function(val) {
					if (val == msg.orderShipNum) {
						els.searShipInput.val('');
					}
				}}).
				regexValidator({regExp: ['shipmentNum', 'orderNum', 'empty'], dataType: 'enum', onError: msg.shipOrderFormatErr});

			// 开始日期
			els.dateStart3.
				formValidator({validatorGroup:'2', tipID: 'date_tip3', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.maxDate(val)) {
						return false;
					}
				}, onError: msg.maxDateErr});
				
			// 结束日期
			els.dateEnd3.
				formValidator({validatorGroup:'2', tipID: 'date_tip3', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.rangeDate(els.dateStart3.val(), val)) {
						return false;
					}
				}, onError: msg.rangeDateErr});
				
			// “已发货”表单校验
			$.formValidator.initConfig({
				validatorGroup: '3',
				formID: 'send_search',
				theme: 'yto',
				errorFocus: false
			});
			
			// 查询
			els.searShipInput2.
				formValidator({validatorGroup:'3', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).functionValidator({fun: function(val) {
					if (val == msg.orderShipNum) {
						els.searShipInput2.val('');
					}
				}}).
				regexValidator({regExp: ['shipmentNum', 'orderNum', 'empty'], dataType: 'enum', onError: msg.shipOrderFormatErr});

			// 开始日期
			els.dateStart2.
				formValidator({validatorGroup:'3', tipID: 'date_tip2', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.maxDate(val)) {
						return false;
					}
				}, onError: msg.maxDateErr});
				
			// 结束日期
			els.dateEnd2.
				formValidator({validatorGroup:'3', tipID: 'date_tip2', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.rangeDate(els.dateStart2.val(), val)) {
						return false;
					}
				}, onError: msg.rangeDateErr});
				
			// 点击“批量打印查询”
			els.batchSearBtn.click(function(ev) {
				ev.preventDefault();
				
				// 把页数和下单时间插入到表单隐藏域中
				//els.perPageA.val(els.showNumEl.val());
				//els.orderByA.val(els.orderby.val());
				els.startDateVal.val(els.dateStart.val());
				els.endDateVal.val(els.dateEnd.val());
				els.orderTypeVal.val(els.orderType.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.orderbyVal.val(els.orderby.val());
				els.showNumVal.val(els.showNumEl.val());
				els.wayTemVal.val(els.waybillTemplate.val());
				els.orderNumVal.val(els.orderNum.val());

				// 对客户列表进行校验
				els.select.
					formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
					functionValidator({fun: function(val) {
						if (val == 0) {
							return false;
						}
					}, onError: msg.clientEmpty});
				
				$('#cur_tab').val('0');
				var result = $.formValidator.pageIsValid('1');
				if (result) {
					loadingDialog();
				}
				els.batchSearForm.trigger('submit');
			});
			
			// 点击“待发货查询”
			els.searShipBtn.click(function(ev) {
				ev.preventDefault();
				// 把页数和下单时间插入到表单隐藏域中
				els.startDate3Val.val(els.dateStart3.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.endDate3Val.val(els.dateEnd3.val());
				els.orderby2Val.val(els.orderby2.val());
				els.showNum2Val.val(els.showNumEl.val());
				els.searShipInputVal.val(els.searShipInput.val());
						
				$('#cur_tab').val('1');
				
				var result = $.formValidator.pageIsValid('2');
				if (result) {
					loadingDialog();
				}
				els.searShipForm.trigger('submit');
			});
			
			// 点击“已发货查询”
			els.searShipBtn2.click(function(ev) {
				ev.preventDefault();
				
				// 把页数和下单时间插入到表单隐藏域中
				//els.perPageC.val(els.showNumEl.val());
				//els.orderByC.val(els.orderby3.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.startDate2Val.val(els.dateStart2.val());
				els.endDate2Val.val(els.dateEnd2.val());
				els.orderby3Val.val(els.orderby3.val());
				els.showNum3Val.val(els.showNumEl.val());
				els.searShipInput2Val.val(els.searShipInput2.val());
				$('#cur_tab').val('2');
				// console.log(els.perPageC.val(), els.orderByC.val());
				var result = $.formValidator.pageIsValid('3');
				if (result) {
					loadingDialog();
				}
				els.sendSearchForm.trigger('submit');
			});
			
			// 切换“批量打印排序”
			els.orderby.change(function() {
				// 把页数和下单时间插入到表单隐藏域中
				//els.perPageA.val(els.showNumEl.val());
				//els.orderByA.val(els.orderby.val());
				els.startDateVal.val(els.dateStart.val());
				els.endDateVal.val(els.dateEnd.val());
				els.orderTypeVal.val(els.orderType.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.orderbyVal.val(els.orderby.val());
				els.showNumVal.val(els.showNumEl.val());
				els.wayTemVal.val(els.waybillTemplate.val());
				els.orderNumVal.val(els.orderNum.val());

				// console.log(els.perPageA.val(), els.orderByA.val());
				var result = $.formValidator.pageIsValid('1');
				if (result) {
					loadingDialog();
				}
				els.batchSearForm.trigger('submit');
			});
			
			// 切换“待发货排序”
			els.orderby2.change(function() {
				// 把页数和下单时间插入到表单隐藏域中
				els.startDate3Val.val(els.dateStart3.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.endDate3Val.val(els.dateEnd3.val());
				els.orderby2Val.val(els.orderby2.val());
				els.showNum2Val.val(els.showNumEl.val());
				els.searShipInputVal.val(els.searShipInput.val());
				// console.log(els.perPageB.val(), els.orderByB.val());
				var result = $.formValidator.pageIsValid('2');
				if (result) {
					loadingDialog();
				}
				els.searShipForm.trigger('submit');
			});
			
			// 切换“已发货排序”
			els.orderby3.change(function() {
				// 把页数和下单时间插入到表单隐藏域中
				//els.perPageC.val(els.showNumEl.val());
				//els.orderByC.val(els.orderby3.val());
				els.startDate2Val.val(els.dateStart2.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.endDate2Val.val(els.dateEnd2.val());
				els.orderby3Val.val(els.orderby3.val());
				els.showNum3Val.val(els.showNumEl.val());
				els.searShipInput2Val.val(els.searShipInput2.val());
				// console.log(els.perPageC.val(), els.orderByC.val());
				var result = $.formValidator.pageIsValid('3');
				if (result) {
					loadingDialog();
				}
				els.sendSearchForm.trigger('submit');
			});
			
			// 切换“每页显示”
			els.showNumEl.change(function() {
				var curTabIndex = $('.tab_cur').index();
				switch(curTabIndex) {
					case 1:		// 如果在“待发货 tab”
						
						// 把页数和下单时间插入到表单隐藏域中
						//els.perPageB.val(els.showNumEl.val());
						//els.orderByB.val(els.orderby2.val());
						//els.orderIdsB.val(orderIdB);
						els.startDate3Val.val(els.dateStart3.val());
						els.endDate3Val.val(els.dateEnd3.val());
						els.selectVal.val(els.select.val());
						els.select2Val.val(els.select2.val());
						els.select3Val.val(els.select3.val());
						els.orderby2Val.val(els.orderby2.val());
						els.showNum2Val.val(els.showNumEl.val());
						els.searShipInputVal.val(els.searShipInput.val());
						// console.log(els.perPageB.val(), els.orderByB.val());
						
						$('#cur_tab').val('1');
						var result = $.formValidator.pageIsValid('2');
						if (result) {
							loadingDialog();
						}
						els.searShipForm.trigger('submit');
					break;
					
					case 2:		// 如果在“已发货 tab”
						
						// 把页数和下单时间插入到表单隐藏域中
						//els.perPageC.val(els.showNumEl.val());
						//els.orderByC.val(els.orderby3.val());
						els.startDate2Val.val(els.dateStart2.val());
						els.selectVal.val(els.select.val());
						els.select2Val.val(els.select2.val());
						els.select3Val.val(els.select3.val());
						els.endDate2Val.val(els.dateEnd2.val());
						els.orderby3Val.val(els.orderby3.val());
						els.showNum3Val.val(els.showNumEl.val());
						els.searShipInput2Val.val(els.searShipInput2.val());
						// console.log(els.perPageC.val(), els.orderByC.val());
						
						$('#cur_tab').val('2');
						var result = $.formValidator.pageIsValid('3');
						if (result) {
							loadingDialog();
						}
						els.sendSearchForm.trigger('submit');
					break;
					
					default:	// 如果在“批量打印 tab”
						// 把页数和下单时间插入到表单隐藏域中
						//els.perPageA.val(els.showNumEl.val());
						//els.orderByA.val(els.orderby.val());
						els.startDateVal.val(els.dateStart.val());
						els.endDateVal.val(els.dateEnd.val());
						els.orderTypeVal.val(els.orderType.val());
						els.selectVal.val(els.select.val());
						els.select2Val.val(els.select2.val());
						els.select3Val.val(els.select3.val());
						els.orderbyVal.val(els.orderby.val());
						els.showNumVal.val(els.showNumEl.val());
						els.wayTemVal.val(els.waybillTemplate.val());
						els.orderNumVal.val(els.orderNum.val());
						
						$('#cur_tab').val('0');
						// console.log(els.perPageA.val(), els.orderByA.val());
						var result = $.formValidator.pageIsValid('1');
						if (result) {
							loadingDialog();
						}
						els.batchSearForm.trigger('submit');
					break;
				}
			});
			
			
			/**
			 * @description 填写开始和结束日期（时间段）
			 * @param days {Number} 最近 N 天的起止日期
			 * @param startEl {Element} 起始日期的 input 元素
			 * @param endEl {Element} 结束日期的 input 元素
			**/
			var lastestDays = function(days, startEl, endEl) {
				var sysDate = new Date(),
					latestDate = new Date(),
					sysDateY = parseInt(latestDate.getFullYear(), 10),
					sysDateM = parseInt(latestDate.getMonth(), 10) + 1,
					sysDateD = parseInt(latestDate.getDate(), 10),
					newtimems = latestDate.getTime() - (days * 24 * 60 * 60 * 1000);
					
				latestDate.setTime(newtimems);
				
				// 填开始时间
				startEl.val(latestDate.format('yyyy-MM-dd'));
				
				// 填结束时间
				endEl.val(sysDate.format('yyyy-MM-dd'));
			};
			
			/**
			 * @description 填写开始和结束日期（时间点）
			 * @param days {Number} 前 N 天
			 * @param startEl {Element} 起始日期的 input 元素
			 * @param endEl {Element} 结束日期的 input 元素
			**/
			var beforeDays = function(days, startEl, endEl) {
				var sysDate = new Date(),
					BeforeDate = new Date(),
					sysDateY = parseInt(BeforeDate.getFullYear(), 10),
					sysDateM = parseInt(BeforeDate.getMonth(), 10) + 1,
					sysDateD = parseInt(BeforeDate.getDate(), 10),
					newtimems = BeforeDate.getTime() - (days * 24 * 60 * 60 * 1000);
					
				BeforeDate.setTime(newtimems);
				
				// 填开始时间
				startEl.val(BeforeDate.format('yyyy-MM-dd'));
				
				// 填结束时间
				endEl.val(BeforeDate.format('yyyy-MM-dd'));
			};
			
			// “批量打印” 今天
			els.lastestA.change(function() {
				lastestDays(0, els.dateStart, els.dateEnd);
			});
			
			// “批量打印” 昨天
			els.lastestB.change(function() {
				beforeDays(1, els.dateStart, els.dateEnd);
			});
			
			// “批量打印” 近三天
			els.lastestC.change(function() {
				lastestDays(2, els.dateStart, els.dateEnd);
			});
			
			// “批量打印” 默认下单时间，今天
			// lastestDays(0, els.dateStart, els.dateEnd);
			
			// “待发货” 今天
			els.lastestG.change(function() {
				lastestDays(0, els.dateStart3, els.dateEnd3);
			});
			
			// “待发货” 昨天
			els.lastestH.change(function() {
				beforeDays(1, els.dateStart3, els.dateEnd3);
			});
			
			// “待发货” 近三天
			els.lastestI.change(function() {
				lastestDays(2, els.dateStart3, els.dateEnd3);
			});
			
			// “待发货” 默认下单时间，今天
			// lastestDays(0, els.dateStart, els.dateEnd);
			
			// “已发货” 今天
			els.lastestD.change(function() {
				lastestDays(0, els.dateStart2, els.dateEnd2);
			});
			
			// “已发货” 昨天
			els.lastestE.change(function() {
				beforeDays(1, els.dateStart2, els.dateEnd2);
			});
			
			// “已发货” 近三天
			els.lastestF.change(function() {
				lastestDays(2, els.dateStart2, els.dateEnd2);
			});
			
			// “已发货” 默认下单时间，今天
			// lastestDays(0, els.dateStart2, els.dateEnd2);
			
			// “批量打印”日期
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
						
						els.batchInputRadio.prop('checked', false);
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
						
						els.batchInputRadio.prop('checked', false);
					},
					startDate: '#F{$dp.$D(\'date_start\')}',
					minDate: '#F{$dp.$D(\'date_start\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			
			// “待发货”日期
			els.dateStart3.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkMaxDate = check.maxDate(dateVal);
						
						if (checkMaxDate) {
							
							showIcon.correct(els.dateTip3, '');
							els.dateEnd3.prop('disabled', false);
							$dp.$('date_end3').focus();
						} else {
							
							showIcon.error(els.dateTip3, msg.maxDateErr);
							_this.blur();
							_this.focus();
							els.dateEnd3.prop('disabled', true);
						}
						
						els.waitInputRadio.prop('checked', false);
					},
					startDate: '#F{$dp.$D(\'date_end3\')}',
					//minDate: '%y-%M-{%d-31*3}',		// 最小时间：3个月前
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			els.dateEnd3.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkRangeDate = check.rangeDate(els.dateStart3.val(), dateVal);
						
						if (checkRangeDate) {
							
							showIcon.correct(els.dateTip3, '');
						} else {
							
							showIcon.error(els.dateTip3, msg.rangeDateErr);
						}
						
						_this.blur();
						
						els.waitInputRadio.prop('checked', false);
					},
					startDate: '#F{$dp.$D(\'date_start3\')}',
					minDate: '#F{$dp.$D(\'date_start3\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			
			// “已发货”日期
			els.dateStart2.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkMaxDate = check.maxDate(dateVal);
						
						if (checkMaxDate) {
							
							showIcon.correct(els.dateTip2, '');
							els.dateEnd2.prop('disabled', false);
							$dp.$('date_end2').focus();
						} else {
							
							showIcon.error(els.dateTip2, msg.maxDateErr);
							_this.blur();
							_this.focus();
							els.dateEnd2.prop('disabled', true);
						}
						
						els.sendInputRadio.prop('checked', false);
					},
					startDate: '#F{$dp.$D(\'date_end2\')}',
					//minDate: '%y-%M-{%d-31*3}',		// 最小时间：3个月前
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			els.dateEnd2.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkRangeDate = check.rangeDate(els.dateStart2.val(), dateVal);
						
						if (checkRangeDate) {
							
							showIcon.correct(els.dateTip2, '');
						} else {
							
							showIcon.error(els.dateTip2, msg.rangeDateErr);
						}
						
						_this.blur();
						
						els.sendInputRadio.prop('checked', false);
					},
					startDate: '#F{$dp.$D(\'date_start2\')}',
					minDate: '#F{$dp.$D(\'date_start2\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
		};
		
		// 合并订单
		var mergerOrders = function() {
			// 点击“合并订单”按钮
			els.mergerOrdersBtn.click(function(ev) {
				ev.preventDefault();
				var _this = $(this);
				
				if (els.mergerSel.css('display') == 'none') {
					els.mergerSel.slideDown(300, function() {
						$('em', _this).removeClass('select_down').addClass('select_up');
					});
				} else {
					els.mergerSel.slideUp(300, function() {
						$('em', _this).removeClass('select_up').addClass('select_down');
					});
				}
			});
			
			// 点击“合并订单”
			els.mergerOrder.click(function(ev) {
				ev.preventDefault();
				var _this = $(this),
					orderIdA = [],			// 订单 ID
					tdChecked = $('.td_check:checked', els.tabAPanel);
					
				
				// 遍历所有已勾选的复选框
				tdChecked.each(function() {
					var _thisCheck = $(this),
						val = $('.order_id', _thisCheck.parent()).val();
						
					orderIdA.push(val);
				});
				
				// 把勾选中的值存在隐藏域中
				els.orderIdsManual.val(orderIdA);
				els.startDateVal.val(els.dateStart.val());
				els.endDateVal.val(els.dateEnd.val());
				els.orderTypeVal.val(els.orderType.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.orderbyVal.val(els.orderby.val());
				els.showNumVal.val(els.showNumEl.val());
				els.wayTemVal.val(els.waybillTemplate.val());
				els.orderNumVal.val(els.orderNum.val());
				
				if (orderIdA.length == 0) {		// 一个也没有勾选
					
					// 筛选出未合并的运单号
					$('.table tbody tr', els.tabAPanel).each(function() {
						var _this = $(this);
						
						if (parseInt($('.merger_or_not', _this).val(), 10) == 0) {
							orderIdA.push($('.order_id', _this).val());
						}
					});
					// 填入隐藏域
					els.orderIdsAuto.val(orderIdA);
					
					var noChecked = new Dialog();
					
					noChecked.init({
						contentHtml: msg.noCheck,
						yes: function() {
							noChecked.close();
							loadingDialog();
							setTimeout(function() {
								els.mergerFormA.trigger('submit');
							}, 0);
							
						},
						no: function() {
							noChecked.close();
						}
					});
					
				} else {		// 勾选了多条
					var cancelDialogText = '',	// 因错误取消弹层的文案
						yesCallback = null,		// 确定回调
						noCallback = null,		// 取消回调
						showClose = false;		// 显示关闭按钮
					
					if (orderIdA.length == 1) {	// 只勾选了一个
						cancelDialogText = msg.oneCheck;
						showClose = true;
					} else if (orderIdA.length > 10) {	// 勾选超过十条
						cancelDialogText = msg.overTen;
						showClose = true;
					} else {				// 勾选了多个
					
						// 把联系方式、地址、收件人分别放入数组
						var contactArr = [],		// 联系电话
							addressArr = [],	// 地址
							nameArr = [];		// 收件人
						
						tdChecked.each(function() {
							var _thisCheck = $(this),
								thisTr = _thisCheck.parent().parent();
							contactArr.push($.trim($('.contact_mobile', thisTr).html()) + $.trim($('.contact_tel', thisTr).html()));
							addressArr.push($.trim($('.td_address', thisTr).attr('title')));
							nameArr.push($.trim($('.td_name', thisTr).html()));
						});
						
						
						var thisTr = tdChecked.parent().parent();
							inputEls = $('.input_text', thisTr),
							mergerOrNot = $('.merger_or_not', thisTr),
							
							hasFormatErr = check.hasFormatErrShipNum(inputEls),
							hasMerger = check.hasMergered(mergerOrNot),
							isAllSameContact = check.isAllSame(contactArr),
							isAllSameAdd = check.isAllSame(addressArr),
							isAllSameName = check.isAllSame(nameArr);
							
						if (hasMerger) {					// 如果有已合并的运单
							cancelDialogText = msg.hasMergered;
							showClose = true;
						} else if (hasFormatErr) {			// 如果运单格式错误
							cancelDialogText = msg.shipNumFormatErr;
							showClose = true;
						} else if (!isAllSameContact || !isAllSameAdd || !isAllSameName) {	// 如果联系方式、地址、收件人有重复
							cancelDialogText = msg.notSameInfo;
							yesCallback = function() {
								errDialog.close();
								loadingDialog();
								setTimeout(function() {
									els.mergerFormA.trigger('submit');
								}, 0);
							};
							noCallback = function() {
								errDialog.close();
							};
							closeBtn = false;
						}
					}
					
					
					if (cancelDialogText == '') {	// 这时候错误信息还是为空，说明就没有错了
						loadingDialog();
						setTimeout(function() {
							els.mergerFormA.trigger('submit');
						}, 0);
					} else {						// 如果还有错，就弹层
						var errDialog = new Dialog();
						errDialog.init({
							contentHtml: cancelDialogText,
							yes: yesCallback,
							no: noCallback,
							closeBtn: showClose
						});
					}
				}
				
				// 模拟点击“合并订单”
				els.mergerOrdersBtn.trigger('click');
				
				//els.mergerSel.hide();
			});
			
			// 点击“取消”合并
			els.cancelMerger.click(function(ev) {
				ev.preventDefault();
				
				var orderIdA = [],			// 订单 ID
					tdChecked = $('.td_check:checked', els.tabAPanel);
					
				
				// 遍历所有复选框
				tdChecked.each(function() {
					var _this = $(this),
						val = $('.order_id', _this.parent()).val();
						
					orderIdA.push(val);
				});
				
				
				// 把勾选中的值存在隐藏域中
				els.orderIdsManual.val(orderIdA);
				els.startDateVal.val(els.dateStart.val());
				els.endDateVal.val(els.dateEnd.val());
				els.orderTypeVal.val(els.orderType.val());
				els.selectVal.val(els.select.val());
				els.select2Val.val(els.select2.val());
				els.select3Val.val(els.select3.val());
				els.orderbyVal.val(els.orderby.val());
				els.showNumVal.val(els.showNumEl.val());
				els.wayTemVal.val(els.waybillTemplate.val());
				els.orderNumVal.val(els.orderNum.val());
				
				if (orderIdA.length == 0) {		// 一个也没有勾选
				
					// 筛选出已合并的运单号
					$('.table tbody tr', els.tabAPanel).each(function() {
						var _this = $(this);
						
						if (parseInt($('.merger_or_not', _this).val(), 10) >= 2) {
							orderIdA.push($('.order_id', _this).val());
						}
					});
					// 填入隐藏域
					els.orderIdsAuto.val(orderIdA);
				
					var noChecked = new Dialog();
					
					noChecked.init({
						contentHtml: msg.cancelDef,
						yes: function() {
							noChecked.close();
							loadingDialog();
							setTimeout(function() {
								els.mergerFormA.trigger('submit');
							}, 0);
							
						},
						no: function() {
							noChecked.close();
						}
					});
				} else {			// 勾选了多个
					var thisTr = tdChecked.parent().parent(),
						inputEls = $('.input_text', thisTr),
						mergerOrNot = $('.merger_or_not', thisTr),
						hasNotMerger = check.hasNotMergered(mergerOrNot),
						hasFormatErr = check.hasFormatErrShipNum(inputEls);
					
					if (hasNotMerger) {		// 有未合并运单
						var hasNotMergerDialog = new Dialog();
						
						hasNotMergerDialog.init({
							contentHtml: msg.hasNotMergered,
							closeBtn: true
						});
					} else if (hasFormatErr) {				// 如果运单格式错误
						var hasFormatErrDialog = new Dialog();
						
						hasFormatErrDialog.init({
							contentHtml: msg.shipNumFormatErr,
							closeBtn: true
						});
					} else {					// 没有未合并运单，则提交
						loadingDialog();
						setTimeout(function() {
							els.mergerFormA.trigger('submit');
						}, 0);
						
					}
				}
				
				
				// 模拟点击“合并订单”
				els.mergerOrdersBtn.trigger('click');
			});
			
		};
		
		// 打印功能
		var print = function() {
			// 点击“打印快递单”
			els.printExpressBtn.click(function(ev) {
				ev.preventDefault();
				
				var tdChecked = $('.td_check:checked',  els.tabAPanel);
				
				if (tdChecked.length == 0) {		// 如果没有勾选项
					var noChecked = new Dialog();
					
					noChecked.init({
						contentHtml: msg.atLeastOne,
						closeBtn: true
					});
				} else {	// 如果有勾选项
					if (els.waybillTemplate.val() == '0') {		// 如果暂无模板
						showIcon.error(els.waybillTemplateTip, msg.newTemplate);
					} else {		// 有模板
						var orderIdA = [],			// 订单 ID
							thisTr = tdChecked.parent().parent(),
							inputEls = $('.input_text', thisTr),
							hasNotSaved = check.hasNotSavedShipNum(inputEls);

						if (hasNotSaved) {
							var hasNotSavedDialog = new Dialog();
							
							hasNotSavedDialog.init({
								contentHtml: msg.saveToPrint,
								closeBtn: true
							});
						} else {
							// 遍历所有复选框
							tdChecked.each(function() {
								var _thisCheck = $(this),
									val = $('.order_id', _thisCheck.parent()).val();
									
								orderIdA.push(val);
							});
							// 把勾选中的值存在隐藏域中
							els.expressOrderId.val(orderIdA);
							els.expressTemplate.val(els.waybillTemplate.val());
							els.orderbyVal.val(els.orderby.val());
							
							els.printExpressForm.trigger('submit');
						}
					}
				}
			});
			
			// 点击“打印发货单”
			els.printInvoiceBtn.click(function(ev) {
				ev.preventDefault();
				
				var tdChecked = $('.td_check:checked',  els.tabAPanel);
				
				if (tdChecked.length == 0) {		// 如果没有勾选项
					var noChecked = new Dialog();
					
					noChecked.init({
						contentHtml: msg.atLeastOne,
						closeBtn: true
					});
				} else {	// 如果有勾选项

					var orderIdA = [],			// 订单 ID
						thisTr = tdChecked.parent().parent(),
						inputEls = $('.input_text', thisTr),
						hasNotSaved = check.hasNotSavedShipNum(inputEls);

					if (hasNotSaved) {
						var hasNotSavedDialog = new Dialog();
						
						hasNotSavedDialog.init({
							contentHtml: msg.saveToPrint,
							closeBtn: true
						});
					} else {
						// 遍历所有复选框
						tdChecked.each(function() {
							var _thisCheck = $(this),
								val = $('.order_id', _thisCheck.parent()).val();
								
							orderIdA.push(val);
						});
						// 把勾选中的值存在隐藏域中
						els.invoiceOrderId.val(orderIdA);
						els.orderbyVal.val(els.orderby.val());
						
						els.printInvoiceForm.trigger('submit');
					}
				}
			});
		};
		
		// 批量填写运单号
		var fillOut = function() {
			// 点击“批量填写运单号”
			els.fillOutBtn.click(function(ev) {
				ev.preventDefault();
				
				if ($('.td_check:checked', els.tabAPanel).length == 0) {		// 没有勾选
					var noCheck = new Dialog();
					
					noCheck.init({
						contentHtml: msg.atLeastOne,
						closeBtn: true
					});
				} else {
					var shipNumHtml = 	'<form id="shipnum_form" action="' + config.iShipNumForm + '" class="form">' +
										'	<p>' +
										'		<label for="start_shipnum">' + msg.startShipNum + '</label>' +
										'		<input type="text" class="input_text" id="start_shipnum" />' +
										'		<span id="start_shipnumTip"></span>' +
										'	</p>' +
										'	<p>' +
										'		<label for="i_val">' + msg.inValue + '</label>' +
										'		<select name="" id="i_val">' +
										'			<option value="-1">-1</option>' +
										'			<option value="-2">-2</option>' +
										'			<option value="1" selected>1</option>' +
										'			<option value="2">2</option>' +
										'			<option value="3">3</option>' +
										'			<option value="4">4</option>' +
										'			<option value="5">5</option>' +
										'		</select>' +
										'	</p>' +
										'</form>';
					
					var shipNumDialog = new Dialog();
					
					shipNumDialog.init({
						contentHtml: shipNumHtml,
						yesVal: msg.save,
						yes: function() {
							
							// 验证运单号格式
							var result = $.formValidator.pageIsValid('4');
							
							if (result) {
								var inVal = parseInt($('#i_val').val(), 10),							// 递增值
									startShipNumVal = $('#start_shipnum').val(),						// 运单号
									startShipNumValF = startShipNumVal.substr(0, 1),					// 运单号首字符
									startShipNumValInt = parseInt(startShipNumVal.slice(1, 10), 10),	// 运单号剩余数字
									shipOrderArr = [],													// id,运单号
									l = $('.td_check:checked', els.tabAPanel).length;

								for (var i=0; i<l; i++) {
									(function(thisTr) {
										// 模拟点击修改，让所有勾选的运单号处于编辑状态
										$('.modify_shipnum', thisTr).trigger('click');
										
										var shipNumLen = startShipNumValInt.toString().length,
											zeros = '';
											
										if (shipNumLen != 9) {		// 如果不是9位，还要补足前导零
											for (var j=0; j<9-shipNumLen; j++) {
												zeros = '0' + zeros;
											}
										}
										
										$('.input_text', thisTr).val(startShipNumValF + zeros + startShipNumValInt);
										
										// 递增
										startShipNumValInt = startShipNumValInt + inVal;
										
										// 把 id,运单号放入数组
										shipOrderArr.push($('.order_id', thisTr).val() + ',' + $('.input_text', thisTr).val());
										
										
									})($('.td_check:checked', els.tabAPanel).eq(i).parent().parent());
								}
								
								// 发异步请求
								$.ajax({
									type: 'POST',
									url: config.saveShipNum,
									data: {
										saveShipNum: shipOrderArr.join('@')
									},
									success: function() {
										// 修改隐藏域的值
										$('.td_check:checked', els.tabAPanel).each(function() {
											var _this = $(this),
												thisTr = _this.parent().parent(),
												defShipNumEl = $('.def_shipnum', thisTr),
												thisInput = $('.input_text', thisTr),
												cancelLink = $('.cancel_shipnum', thisTr),
												saveLink = $('.save_shipnum', thisTr),
												thisInputVal = thisInput.val();
												
											defShipNumEl.val(thisInputVal);
											thisInput.replaceWith('<span class="shipnum">' + thisInputVal + '</span>');
											cancelLink.remove();
											saveLink.replaceWith('<a title="' + msg.modify + '" class="modify_shipnum do_link" href="javascript:;">' + msg.modify + '</a>');
											
										});
									}
								});
								// 关闭弹层
								shipNumDialog.close();
							}
						},
						no: function() {
							shipNumDialog.close();
						}
					});
					
					$.formValidator.initConfig({
						validatorGroup: '4',
						formID: 'shipnum_form',
						theme: 'yto',
						errorFocus: false
					});
					
					// 起始运单号
					$('#start_shipnum').
						formValidator({validatorGroup:'4', onShow: '', onFocus: '', onCorrect: ' '}).
						inputValidator({min: 1, onErrorMin: msg.noEmpty}).
						regexValidator({regExp: 'shipmentNum', dataType: 'enum', onError: msg.shipNumFormatErr}).
						functionValidator({fun: function() {
							// 验证递增后如果出现运单负数或者后9位大于999999999，则报错
							var inVal = parseInt($('#i_val').val(), 10),							// 递增值
								startShipNumVal = $('#start_shipnum').val(),						// 运单号
								startShipNumValInt = parseInt(startShipNumVal.slice(1, 10), 10),	// 运单号剩余数字
								rval = inVal * ($('.td_check:checked', els.tabAPanel).length-1);

							if (startShipNumValInt > (999999999 - rval) || startShipNumValInt < -rval) {
								return false;
							}
						}, onError: msg.IncrementErr});

				}
			});
			
			
		};
		
		// 批量发货
		var shipVol = function() {
			// 批量发货失败状态
			var statusTipVal = els.statusTip.val();
			
			if (statusTipVal == 'error') {
				var errDialog = new Dialog();
				
				errDialog.init({
					contentHtml: msg.sysErr,
					yes: function() {
						errDialog.close();
					},
					closeBtn: true
				});
			} else if (statusTipVal == 'overTime') {
				var overTimeDialog = new Dialog();
				
				overTimeDialog.init({
					contentHtml: msg.overTimeErr,
					yes: function() {
						overTimeDialog.close();
					},
					closeBtn: true
				});
			}
			
			// 点击“批量发货”按钮
			els.batchBtn.click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					tdChecked = $('.td_check:checked',  els.tabBPanel);
				
				if (tdChecked.length == 0) {		// 如果没有勾选项
					var noChecked = new Dialog();
					
					noChecked.init({
						contentHtml: msg.atLeastOne,
						closeBtn: true
					});
				} else {	// 如果有勾选项

					var orderIdB = [],			// 订单 ID
						thisTr = tdChecked.parent().parent(),
						inputEls = $('.input_text', thisTr),
						hasNotSaved = check.hasNotSavedShipNum(inputEls);
					
					if (hasNotSaved) {		// 是否有未保存
						var hasNotSavedDialog = new Dialog();
						
						hasNotSavedDialog.init({
							contentHtml: msg.saveToDelivery,
							closeBtn: true
						});
					} else {
						// 遍历所有复选框
						tdChecked.each(function() {
							var _thisCheck = $(this),
								val = $('.order_id', _thisCheck.parent()).val();
								
							orderIdB.push(val);
						});
						
						// 把勾选中的值存在隐藏域中
						els.orderIdsB.val(orderIdB);
						els.startDate3Val.val(els.dateStart3.val());
						els.endDate3Val.val(els.dateEnd3.val());
						els.selectVal.val(els.select.val());
						els.select2Val.val(els.select2.val());
						els.select3Val.val(els.select3.val());
						els.orderby2Val.val(els.orderby2.val());
						els.showNum2Val.val(els.showNumEl.val());
						els.searShipInputVal.val(els.searShipInput.val());

						var checkInfoDialog = new Dialog();
						
						checkInfoDialog.init({
							contentHtml: msg.checkInfo,
							yes: function() {
								checkInfoDialog.close();
								loadingDialog();
								els.shipVolForm.trigger('submit');
							},
							no: function() {
								checkInfoDialog.close();
							}
						});
					}
				}
			});
			
			// 点击“不想看到它”
			els.delItemBtn.click(function(ev) {
				ev.preventDefault();
				var tdChecked = $('.td_check:checked',  els.tabBPanel),
					orderIdB = [];			// 订单 ID
				if (tdChecked.length == 0) {		// 如果没有勾选项
					var noChecked = new Dialog();
					
					noChecked.init({
						contentHtml: msg.atLeastOne,
						closeBtn: true
					});
				} else {
					// 遍历所有复选框
					tdChecked.each(function() {
						var _thisCheck = $(this),
							val = $('.order_id', _thisCheck.parent()).val();
							
						orderIdB.push(val);
					});
					
					// 把勾选中的值存在隐藏域中
					els.orderIdsB.val(orderIdB);
					els.startDate3Val.val(els.dateStart3.val());
					els.endDate3Val.val(els.dateEnd3.val());
					els.selectVal.val(els.select.val());
					els.select2Val.val(els.select2.val());
					els.select3Val.val(els.select3.val());
					els.orderby2Val.val(els.orderby2.val());
					els.showNum2Val.val(els.showNumEl.val());
					els.searShipInputVal.val(els.searShipInput.val());
					
					
					var checkInfoDialog = new Dialog();
					
					checkInfoDialog.init({
						contentHtml: msg.notShowHere,
						yes: function() {
							checkInfoDialog.close();
							loadingDialog();
							els.delItemForm.trigger('submit');
						},
						no: function() {
							checkInfoDialog.close();
						}
					});
				}
			});
		};
		
		// 客户模板
		var clientTem = function() {
			// 获取模板
			var getTem = function() {
				$.ajax({
					type: 'GET',
					cache:false,
					url: config.getTemplate + els.select.val(),
					success: function(data) {
						var dataArr = data.replace('"', '').replace('"', '').split(','),
							options = '';
						
						$.each(dataArr, function(index, val) {
							var optionArr = val.split(':'),
								optionVal = optionArr[0],
								optionContent = optionArr[1],
								optionType = optionArr[2] == '1' ? msg.seller : '';
								
							options += '<option value="' + optionVal + '">' + optionContent + optionType + '</option>';
						});
						
						
						els.waybillTemplate.html(options);
					}
				});
			};
			
			// 下拉用户切换
			els.select.change(function() {
				getTem();
			});
			
			getTem();
		};
		
		// 分页
		var pagenavi = function() {
			els.pageTxt.click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					form = _this.parent(),
					curPage = parseInt($.trim($('.page_cur', form).html()), 10),
					toPageNum = $.trim(_this.html());
				
				if (toPageNum == msg.prevPage) {
					toPageNum = curPage - 1;
				} else if (toPageNum == msg.nextPage) {
					toPageNum = curPage + 1;
				}
				
				// 批量打印
				els.startDateVal.val(els.dateStart.val());
				els.endDateVal.val(els.dateEnd.val());
				els.orderTypeVal.val(els.orderType.val());
				els.selectVal.val(els.select.val());
				els.orderbyVal.val(els.orderby.val());
				els.showNumVal.val(els.showNumEl.val());
				els.wayTemVal.val(els.waybillTemplate.val());
				els.orderNumVal.val(els.orderNum.val());
				// 待发货
				els.startDate3Val.val(els.dateStart3.val());
				els.select2Val.val(els.select2.val());
				els.endDate3Val.val(els.dateEnd3.val());
				els.orderby2Val.val(els.orderby2.val());
				els.showNum2Val.val(els.showNumEl.val());
				els.searShipInputVal.val(els.searShipInput.val());
				// 已发货
				els.startDate2Val.val(els.dateStart2.val());
				els.select3Val.val(els.select3.val());
				els.endDate2Val.val(els.dateEnd2.val());
				els.orderby3Val.val(els.orderby3.val());
				els.showNum3Val.val(els.showNumEl.val());
				els.searShipInput2Val.val(els.searShipInput2.val());
				// 页码
				$(els.toPageNum, form).val(toPageNum);
				loadingDialog();
				form.trigger('submit');
			});
		};
		
		return {
			init: function() {
				ytoTab.init(config.curTab);
				defText();
				checkAll();
				table();
				allowWdPrint();
				formValidCompatible();
				form();
				mergerOrders();
				print();
				fillOut();
				shipVol();
				clientTem();
				pagenavi();
			}
		}
	})();
	
	surPrint.init();
});