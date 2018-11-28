/**
 * 订单导入
**/
$(function() {
	var surPrint = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			curTab: parseInt($('#cur_tab').val(), 10) || 0,			// 默认 Tab
			bindBranch: winParams.bindBranch,				// 是否绑定了网点
			allowPrintUrl: winParams.allowPrintUrl || '',			// 允许网点打印请求
			iShipNumForm: winParams.iShipNumForm || '',				// 递增运单号请求
			saveShipNum: winParams.saveShipNum || ''				// 保存运单号请求
		};
		
		// 元素集合
		var els = {
			
			// 公用元素
			tabTriggerLi: $('.tab_triggers li'),
			showNumEl: $('#show_num'),
			tdCheck: $('.td_check'),
			mergerOrNot: $('.merger_or_not'),
			modifyShipNum: $('.modify_shipnum'),
			saveShipNum: $('.save_shipnum'),
			cancelShipNum: $('.cancel_shipnum')
		};

		// 文案
		var msg = {
			save: '保存',
			cancel: '取消',
			modify: '修改',
			startShipNum: '起始运单号：',
			inValue: '递增值：',
			notBindBranch: '您还未跟网点绑定，请回到“我的易通”→绑定网点',
			//notAgain: '下次不再显示',
			noCheck: '您还没有选择订单，默认将<收件地址，收件人姓名，收件联系方式>相同的订单自动合并~',
			oneCheck: '手动合并，至少勾选两条订单哦^_^~',
			notMerger: '该订单不是合并订单，无法取消合并╮(╯_╰)╭',
			hasMergered: '已合并的订单无法重复合并，请先取消合并',
			notSameInfo: '收货地址、联系方式、收件人不一致，确认是否合并?_?',
			cancelDef: '您还没有选择订单，默认将取消所有的合并订单！',
			hasNotMergered: '有未合并的订单，不能取消合并',
			atLeastOne: '至少勾选一项',
			noEmpty: '运单号不能为空',
			shipNumFormatErr: '运单号格式错误',
			//clearWarn: '翻页后，您在当前页的操作将会被撤销！',
			allowSure: '确定后，允许网点为您打印快递单，同时能看到您在线下单的订单信息！',
			checkInfo: '请核对您的信息，发货后将无法修改任何信息',
			//noEmptyShipNum: '运单号不能为空',
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
			clientEmpty: '请至少勾选一个客户'
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
		

		
		// 全选
		var checkAll = function() {
			// 批量打印 tab
			$('.check_all', els.tabAPanel).checkAll( $('input[type="checkbox"]', els.tabAPanel) );
			// 待发货 tab
			$('.check_all', els.tabBPanel).checkAll( $('input[type="checkbox"]', els.tabBPanel) );
		};
		
		// 表格
		var table = function() {
			// 每行 tr
			$('.table tbody tr').each(function() {
				var _this = $(this),
					firstTd = $('td', _this).first(),
					lastTd = $('td', _this).last(),
					thisTdAdd = $('.td_address', _this),
					len = 20,
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
			$('.table .list_tr').click(function(ev) {
				var _this = $(this),
					nextTr = _this.next(),
					nextTdDisplay = $('.detail_td', nextTr).css('display'),
					targetClass = ev.target.className;
					//targetTag = ev.target.tagName.toLowerCase();
				if (
					targetClass.indexOf('do_link') == -1 &&
					targetClass.indexOf('td_check') == -1 && 
					targetClass.indexOf('input_text') == -1
					) {
					if (nextTdDisplay == 'none') {
						$('td', nextTr).slideDown();
					} else {
						$('td', nextTr).slideUp();
					}
				}
			});
			
			// 点击加号全部展开
			$('.box_bd .fold_unfold').click(function(ev) {
				var _this = $(this),
					curTabPanel = _this.parents('.box_bd'),
					tdDisplay = $('.detail_td', curTabPanel).css('display');
				
				if (tdDisplay == 'none') {
					_this.removeClass('unfold').addClass('fold');
					$('.table tbody tr .detail_td', curTabPanel).slideDown();
				} else {
					_this.removeClass('fold').addClass('unfold');
					$('.table tbody tr .detail_td', curTabPanel).slideUp();
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
						
						els.printInvoiceForm.trigger('submit');
					}
				}
			});
		};
		

		
		// 批量发货
		var shipVol = function() {
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
						
						var checkInfoDialog = new Dialog();
						
						checkInfoDialog.init({
							contentHtml: msg.checkInfo,
							yes: function() {
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
					els.orderIdsC.val(orderIdB);
					
					var checkInfoDialog = new Dialog();
					
					checkInfoDialog.init({
						contentHtml: msg.notShowHere,
						yes: function() {
							els.delItemForm.trigger('submit');
						},
						no: function() {
							checkInfoDialog.close();
						}
					});
				}
			});
		};
		
		return {
			init: function() {
				ytoTab.init(config.curTab);
				checkAll();
				table();
				allowWdPrint();
				formValidCompatible();
				form();
				mergerOrders();
				print();
				fillOut();
				shipVol();
			}
		}
	})();
	
	surPrint.init();
});