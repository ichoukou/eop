$(function() {
	var question = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};

		// 全局配置
		var config = {
			onStep: winParams.onStep || null,
			labels: winParams.labels || '',		// 已有标签
			deleteLabelAction: winParams.deleteLabelAction || '',		// 删除标签 action
			saveLabelAction: winParams.saveLabelAction || ''			// 保存标签 action
		};
		
		var textDefault = function() {
			if(winParams.userType==1){
				if($.trim($("#ship_num").val())==""){
					$('#ship_num').defaultTxt('请输入运单号/买家电话/买家姓名');
				}
				if($.trim($("#ship_num").val())=="请输入运单号/买家电话/买家姓名"){
					$("#ship_num").val("");
					$('#ship_num').defaultTxt('请输入运单号/买家电话/买家姓名');
				}
			}
		};
		
		// 全选
		var checked = function() {
			$('#tab_panel_a .checked_all').checkAll( $('#tab_panel_a input[type="checkbox"]') );
			$('#tab_panel_b .checked_all').checkAll( $('#tab_panel_b input[type="checkbox"]') );
			$('#tab_panel_c .checked_all').checkAll( $('#tab_panel_c input[type="checkbox"]') );
			$('#tab_panel_d .checked_all').checkAll( $('#tab_panel_d input[type="checkbox"]') );
		};
		
		// 旺旺号提示弹层
		var wangTip = function(){
			$('.wangwang_tips').live("click", function(){
				var tipDialog = new Dialog();

				tipDialog.init({
					contentHtml: '<div class="w_tip width:350px line-height:20px">登录易通后，系统会在当晚12点开始同步当前时间的订单旺旺号。其它店铺的订单旺旺号需要使用其它店铺的卖家账号登录</div>',
					yesVal: "我知道了",
					yes: function() {
						tipDialog.close();
					}
				});

			});
		};
		
		// 标签设置
		var setLabel = function() {
			var labelSetDialog = new Dialog();
			
			// 标签弹层
			var labelDialog = function() {
				
				var labelArr = config.labels,
				maxLine = 10,			// 最多新增十个标签
				labelHtml = '<input id="edit_id" type="hidden" value="" />',			// 已有标签的 HTML
				emptyTable = '';		// 空表格的 HTML
				var listLen = 0;
				
				$.ajax({
					url: 'questionnaire_getTags.action',
					cache: false,
					type: 'GET',
					success: function(data) {
						var tags = data.jsons.tags;
						listLen = tags.length;
						var id;
						var tagName;
						var tagType;
						// 遍历出已有的标签
						for (var i=0; i<tags.length; i++) {
							id = tags[i].id;
							tagName = tags[i].tagName;
							tagType = tags[i].tagType;
							labelHtml += '<tr>' +
							'	<td class="td_a"><span class="label_name">' + tagName +'</span></td>' +
							'	<td class="td_b"><input id="_id" type="hidden" value="'+id+'" />';
							if('-1'==tagType||'1'==tagType) {
								labelHtml += '<span class="gray">修改</span>\n' + 
										     '<span class="gray">删除</span>\n';
							}else {
								labelHtml += ' <a href="javascript:;" class="edit_label" title="修改">修改</a>' + '\n' +
										     ' <a href="javascript:;" class="del_label" title="删除">删除</a>' + '\n';
							}
							labelHtml +=  '		<input type="hidden" class="cache_tr" />' +
										  '	</td>' +
							              '</tr>';
						}
					
						// 遍历出剩余的空表格
						for (var j=0;j<maxLine-listLen;j++) {
							emptyTable += '<tr>' +
										'	<td class="td_a"><span class="empty_td_a">&nbsp;</span></td>' +
										'	<td class="td_b"><span class="empty_td_b">&nbsp;</span><input type="hidden" class="cache_tr" /></td>' +
										'</tr>';
						}
						// 生成标签弹层内容
						var labelDialogHtml = '<div id="label_dialog">' +
												'	<div id="label_hd" class="clearfix">' +
												'		<h3>标签管理</h3>' +
												'		<div id="text_tip">' +
												'			<em></em>' +
												'			<a href="javascript:;" id="new_label" class="btn btn_d" title="新增"><span>新 增</span></a>' +
												'		</div>' +
												'	</div>' +
												'	<div class="table">' +
												'		<table>' +
												'			<thead>' +
												'				<tr>' +
												'					<th class="th_a" width="70%">' +
												'						<div class="th_title"><em>内容</em></div>' +
												'					</th>' +
												'					<th class="th_b" width="30%">' +
												'						<div class="th_title"><em>操作</em></div>' +
												'					</th>' +
												'				</tr>' +
												'			</thead>' +
												'			<tbody>' +
																labelHtml +
																emptyTable +
												'			</tbody>' +
												'		</table>' +
												'	</div>' +
												'</div>';
						
						
						labelSetDialog.init({
							contentHtml: labelDialogHtml,
							yes: function() {
								setTimeout(function() {
									$('#q_form').trigger('submit');
								}, 0);
								
							},
							no: function() {
								setTimeout(function() {
									$('#q_form').trigger('submit');
								}, 0);
//								labelSetDialog.close();
							}
						});
						
						window.ytoTable && ytoTable.init();
					
					}
				});
			};
			
			// 允许删除、保存标识
			var allowLabelDel = true,
				allowLabelSave = true;
				
			// 标签操作
			var labelProcess = {
				// 保存
				save: function(target) {
					var tr = target.parents('tr');
					var val = $('.td_a input', tr).val();
					val = $.trim(val);
					var val2 = $("#edit_id").val();
					var valLen = val.length;
					var valRepeat = false;
					for (var i=0, l=$('#label_dialog .label_name').length; i<l; i++) {
						if (val == $('#label_dialog .label_name').eq(i).text()) {
							valRepeat = true;
							break;
						}
					}
					if (val == '最多输入十个字符') {
						$('#text_tip em').html('亲，标签名不能为空哟~');
						
						$('.td_a input', tr).addClass('error_status');
					} else if (valRepeat) {
						$('.td_a input', tr).addClass('error_status');
						
						$('#text_tip em').html('亲，标签不能重复哟~');
					} else {
						if (valLen == 0) {
							$('.td_a input', tr).addClass('error_status');
							
							$('#text_tip em').html('亲，标签名不能为空哟~');
						} else if (valLen > 10) {
							$('.td_a input', tr).addClass('error_status');
							
							$('#text_tip em').html('亲，标签名不能超过十个字符哟~');
						} else {
							$('#text_tip em').html('');
							if (allowLabelSave) {
								var tagName = encodeURI(val);
								var tagId = encodeURI(val2);
								allowLabelSave = false;
								var id;
								$.ajax({
									url: config.saveLabelAction + '?tagName='+tagName + '&tagId='+tagId,
									cache: false,
									type: 'POST',
									success: function(data) {
										id = data.jsons.tag.id;
										//alert(id);
										$('.td_a', tr).html('<span class="label_name">' + val + '</span>');
										$('.td_b', tr).html('<input id="_id" type="hidden" value="'+id+'" /><a href="javascript:;" class="edit_label" title="修改">修改</a>' + '\n' +
															'		<a href="javascript:;" class="del_label" title="删除">删除</a>' + '\n' +
															'		<input type="hidden" class="cache_tr" />'
															);
															
										allowLabelSave = true;
									}
								});

							}
						}
					}
					
				},
				
				// 删除
				del: function(target) {
					if (allowLabelDel) {
						$('#text_tip em').html('标签删除中，请稍候...');
						allowLabelDel = false;
						var tr = target.parents('tr');
						var id = $('.td_b input', tr).val();
						$.ajax({
							url: config.deleteLabelAction + '?tagId=' + id,
							cache: false,
							type: 'GET',
							success: function(data) {
								/*
									人造数据，调试用，可删
								*/
								var dataItem = data.jsons.hasItems;// = false;// 如果该标签下有问题件
								if (dataItem=='2') {
									labelSetDialog.close();
									
									var tipsDialog = new Dialog();
									
									tipsDialog.init({
										contentHtml: '该标签下有问题件，请先移除问题件再删除',
										closeBtn: true
									});
								} else {
									// 移除该行
									tr.remove();
									
									$('#label_dialog tbody').append('<tr>' +
																	'	<td class="td_a"><span class="empty_td_a">&nbsp;</span></td>' +
																	'	<td class="td_b"><span class="empty_td_b">&nbsp;</span><input type="hidden" class="cache_tr" /></td>' +
																	'</tr>'
									);
									
									$('#label_dialog tbody tr:last').addClass('last_tr').siblings().removeClass('last_tr');
								}
								
								
								allowLabelDel = true;
								
								$('#text_tip em').html('');
							}
						});
						
						
						//$('#text_tip em').html('');

					}
				},
				
				// 取消
				cancel: function(target) {
					var tr = target.parents('tr');
					
					tr.html($('.cache_tr', tr).val());
					$('#text_tip em').html('');
				},
				
				// 编辑
				edit: function(target) {
					var tr = target.parents('tr');
					// 缓存标签名
					var cacheLabel = $('.td_a', tr).text(),
						cacheTr = tr.html();
					//ID
					var id = $('.td_b input', tr).val();
					$("#edit_id").val(id);
					$('.td_a', tr).html('<input type="text" class="input_text" />');	
					$('.td_a input', tr).val(cacheLabel);
					$('.td_b', tr).html('<a href="javascript:;" class="save_label" title="保存">保存</a>' + '\n' + '<a href="javascript:;" class="cancel_label" title="取消">取消</a>' + '\n' + '<input type="hidden" class="cache_tr" />');
					$('.cache_tr', tr).val(cacheTr);
					$('#text_tip em').html('');
				}
			};
				
				
			// 新增标签
			$('#new_label').live('click', function() {
				var inputText = $('#label_dialog tbody tr .new_label_span');
				
				//清空修改标签ID
				$("#edit_id").val(null);
				
				// 如果已经有输入框了，那就不需要再有输入框了
				if (inputText.length != 0) {
					return;
				}
				// 在弹层的表格中查找最后一个 .label_name
				var labelName = $('#label_dialog tbody tr:last').find('.label_name');
				if (labelName.length != 0) {	// 如果有找到，说明已经无法新增
					$('#text_tip em').html('亲，最多只能十个标签哦~');
				} else {
					var tr = $('.empty_td_a:first').parents('tr'),
						cacheTr = tr.html();
						
					$('.cache_tr', tr).val(cacheTr);
					
					$('.empty_td_a:first').replaceWith('<span class="new_label_span"><input type="text" class="input_text" /></span>');
					$('.empty_td_b:first').replaceWith('<a href="javascript:;" class="save_label" title="保存">保存</a>' + '\n' + '<a href="javascript:;" class="cancel_label" title="取消">取消</a>' + '\n');
					
					var input = $('#label_dialog .new_label_span input');
					
					input.defaultTxt('最多输入十个字符');
					
					
				}
				
			});
			
			$('#label_dialog .td_a input').live('blur', function() {
				if ($(this).val() == '') {
					$(this).val('最多输入十个字符');
					$(this).addClass('default_status').removeClass('focus_status');
				}
			});
			
			$('#label_dialog .td_a input').live('focus', function() {
				if ($(this).val() == '最多输入十个字符') {
					$(this).val('');
					$(this).addClass('focus_status').removeClass('default_status');
				}
			});
			
			// 点击“保存”
			$('.save_label').live('click', function() {
				labelProcess.save($(this));
			});
			
			// 点击“删除”
			$('.del_label').live('click', function() {
				labelProcess.del($(this));
			});
			
			// 点击“取消”
			$('.cancel_label').live('click', function() {
				labelProcess.cancel($(this));
			});
			
			// 点击“编辑”
			$('.edit_label').live('click', function() {
				labelProcess.edit($(this));
			});
			
			
			// 点击“设置标签”
			$('.set_label_dialog').click(function(ev) {
				ev.preventDefault();
				ev.stopPropagation();
				labelDialog();
			});
			
			// 切换“批量移动”
			$('.move_label').change(function() {
				if ($(this).val() == 'set_label_dialog') {
					labelDialog();
				} else{
					var tagId = $(this).val();
					if(tagId == ""){
						return;
					}
					var checkedInput = $('.que_checkbox:checked'),questionnaireIds = "",l=checkedInput.length;
					if(l < 1){
						var oDialog = new Dialog();
						oDialog.init({
							contentHtml: "请先选择要操作的问题件！",
							yes: function() {
								oDialog.close();
								$(".move_label").val("");
							},
							closeBtn: true
						});
						return;
					}
					for (var i=0;i<l; i++) {
						questionnaireIds += $(checkedInput[i]).val();
						if(i < l-1){
							questionnaireIds +=",";
						}
					}
					$.ajax({
						type:"POST",
						dataType:"json",
						cache:false,
						data : {
							questionnaireIds : questionnaireIds,
							tagId : tagId
						},
						url:"questionnaire_tagMove.action",
						success:function(data){
							var oDialog = new Dialog();
							oDialog.init({
								contentHtml: data,
								yes: function() {
									oDialog.close();
									$('#q_form').trigger('submit');
								},
								closeBtn: true
							});
						}
					});
				}
			});
			
			//单个标签移动
			$(".label_to").click(function(){
				$.ajax({
					type:"POST",
					dataType:"json",
					cache:false,
					data : {
						questionnaireIds : $(this).next().val(),
						tagId : $(this).attr("value")
					},
					url:"questionnaire_tagMove.action",
					success:function(data){
						var oDialog = new Dialog();
						oDialog.init({
							contentHtml: data,
							yes: function() {
								oDialog.close();
								setTimeout(function() {
									$('#q_form').trigger('submit');
								}, 0);
							},
							closeBtn: true
						});
					}
				});
			});
		};
		
		// 模拟下拉框
		var simulateSel = function() {
			// 点击问题件类型
			$('#q_type').click(function() {
				$('#simulate_sel').show();

			});
			
			// label点击触发 checkbox 选中
			$('#type_box label').click(function() {
				$(this).prev().trigger('click');
			});
			
			// 点击“确定”
			$('#type_ok').click(function(ev) {
				ev.preventDefault();
				
				var checkedInput = $('#type_box input[type="checkbox"]:checked'),
					qTypeValArr = [],
					qTypeNameArr = [],
					subLen = 20;
					
				for (var i=0,l=checkedInput.length; i<l; i++) {
					qTypeValArr.push(checkedInput.eq(i).val());
					qTypeNameArr.push($.trim(checkedInput.eq(i).next().html()));
				}
				
				var qTypeStr = qTypeValArr.toString(),
					qTypeNameStr = qTypeNameArr.join('，');
	
				if (qTypeNameStr.length > subLen) {
					qTypeNameStr = qTypeNameStr.substr(0, subLen) + '……';
				}
				
				
				// 截断的类型填到 span 中
				$('#q_type').val(qTypeNameStr);
					
				// 类型编码插入隐藏域
				$('#q_type_val').val(qTypeStr);
				
				$('#simulate_sel').hide();
			});
			
			// 点击“清空”
			$('#type_clear').click(function(ev) {
				ev.preventDefault();
				$('#type_box input[type="checkbox"]').prop('checked', false)
				$('#q_type').val('');

				$('#q_type_val').val('');
				$('#simulate_sel').hide();
			});
		};
		
		// 卖家版模拟下拉框
		var simulateSel2 = function() {
			// 点击问题件类型
			$('#q_type').click(function() {
				$('#simulate_sel2').show();

			});
			
			// label点击触发 checkbox 选中
			$('#type_box label').click(function() {
				$(this).prev().trigger('click');
			});
			
			// 点击“确定”
			$('#type_ok').click(function(ev) {
				ev.preventDefault();
				
				var checkedInput = $('#type_box input[type="checkbox"]:checked'),
					qTypeValArr = [],
					qTypeNameArr = [],
					subLen = 20;
					
				for (var i=0,l=checkedInput.length; i<l; i++) {
					qTypeValArr.push(checkedInput.eq(i).val());
					qTypeNameArr.push($.trim(checkedInput.eq(i).next().html()));
				}
				
				var qTypeStr = qTypeValArr.toString(),
					qTypeNameStr = qTypeNameArr.join('，');
	
				if (qTypeNameStr.length > subLen) {
					qTypeNameStr = qTypeNameStr.substr(0, subLen) + '……';
				}
				
				
				// 截断的类型填到 span 中
				$('#q_type').val(qTypeNameStr);
					
				// 类型编码插入隐藏域
				$('#q_type_val').val(qTypeStr);
				
				$('#simulate_sel2').hide();
			});
			
			// 点击“清空”
			$('#type_clear').click(function(ev) {
				ev.preventDefault();
				$('#type_box input[type="checkbox"]').prop('checked', false)
				$('#q_type').val('');

				$('#q_type_val').val('');
				$('#simulate_sel2').hide();
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
				
				if (dateGap > 62) {
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

					if (dateGap > 9) {
						return false;
					} else {
						return true;
					}
				}
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
		
		// 表单验证
		var form = function() {
			// 元素集合
			var els = {
				dateStart: $('#start_date'),
				dateEnd: $('#end_date'),
				dateTip: $('#dateTip'),
				searBtn: $('#sear_btn'),
				shipNum: $('#ship_num'),
				tab1: $("#tab1"),
				tab2: $("#tab2"),
				tab3: $("#tab3"),
				tab4: $("#tab4"),
				currentPage: $('#currentPage'),
				qForm: $('#q_form'),
				autoNotify:$("#autoNotify"),//自动通知
				question:$("#question")
			};
			
			// 文案
			var msg = {
				maxDateErr: '最多可查询到上月处理的问题件',
				rangeDateErr: '处理起止时间不能大于10天',
				shipFormatErr: '运单号格式错误',
				shipFormatErr2: '请输入正确的运单号/买家电话/买家姓名'
			};
			
			// 起始时间
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value,
							checkMaxDate = check.maxDate(dateVal);
						
						if (checkMaxDate) {
							
							showIcon.correct(els.dateTip, '');
							els.dateEnd.prop('disabled', false);
							$dp.$('end_date').focus();
						} else {
							
							showIcon.error(els.dateTip, msg.maxDateErr);
							_this.blur();
							_this.focus();
							els.dateEnd.prop('disabled', true);
						}
						
						els.dateTip.show();
					},
					startDate: '#F{$dp.$D(\'end_date\')}',
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
					startDate: '#F{$dp.$D(\'start_date\')}',
					minDate: '#F{$dp.$D(\'start_date\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				});
			});
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'q_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 开始日期
			els.dateStart.
				formValidator({validatorGroup:'1', tipID: 'dateTip', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.maxDate(val)) {
						return false;
					}
				}, onError: msg.maxDateErr});
				
			// 结束日期
			els.dateEnd.
				formValidator({validatorGroup:'1', tipID: 'dateTip', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({fun: function(val) {
					if (!check.rangeDate(els.dateStart.val(), val)) {
						return false;
					}
				}, onError: msg.rangeDateErr});
			
			// 网点运单号
			if(winParams.userType==2){
				els.shipNum.
					formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
					regexValidator({regExp: ['shipmentNum', 'empty'], dataType: 'enum', onError: msg.shipFormatErr});
			}
			// 卖家运单号
			if(winParams.userType==1){
				els.shipNum.
					formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
					regexValidator({regExp: ['shipmentNum', 'accountId', 'mobile', 'tel', 'empty'], dataType: 'enum', onError: msg.shipFormatErr2});
			}
			// 点击“查询”
			/*els.searBtn.click(function(ev) {
				ev.preventDefault();
				if($("#isShowSigned").is(":checked"))
					$("#isShowSigned").val("1");
				else
					$("#isShowSigned").val("0");
				if(config.onStep && config.onStep < 3){
					window.location.reload(true);
					return;
				}
				$("#autoSkip").val(1);
				$("#tagId").val(0);
				var result = $.formValidator.pageIsValid('1');
				
				if (result) {
					var loadingDialog = new Dialog();
					
					loadingDialog.init({
						contentHtml: '正在加载中……'
					});
				}
			});*/
				//els.qForm.trigger('submit');
			}

			//导出
			$(".exportQuestion").click(function(ev){
				ev.preventDefault();
				if($("#isShowSigned").is(":checked"))
					$("#isShowSigned").val("1");
				else
					$("#isShowSigned").val("0");
				if(config.onStep && config.onStep < 3){
					window.location.reload(true);
					return;
				}
				var result = $.formValidator.pageIsValid('1');
				if (result) {
					var tabStatus = $("#tabStatus").val();
					var tagId = $("#tagId").val();
					var starttime = $("#start_date").val();
					var endtime = $("#end_date").val();
					var bindUserCustomerId = $("#shop_name").val();
					var feedbackInfo = $("#q_type_val").val();
					var isShowSigned = $("#isShowSigned").val();
					var conditionString = $("#ship_num").val();
					var params = "tabStatus="+tabStatus+"&tagId="+tagId+"&starttime="+starttime
					 	+"&endtime="+endtime+"&bindUserCustomerId="+bindUserCustomerId+"&feedbackInfo="+feedbackInfo
					 	+"&isShowSigned="+isShowSigned+"&conditionString="+conditionString+"&outputCode=GBK";
					//window.open("questionnaire_exportQuestion.action?"+encodeURI(params) );
					window.location = "questionnaire_exportQuestion.action?"+encodeURI(params);
				}
			});
			

			//点击Tab1
			els.tab1.click(function(ev){
				ev.preventDefault();
				$("#autoSkip").val(0);
				$("#tabStatus").val("1");
				$("#tagId").val(0);
				els.qForm.trigger('submit');
			});
			
			//点击Tab2
			els.tab2.click(function(ev){
				ev.preventDefault();
				$("#autoSkip").val(0);
				$("#tabStatus").val("2");
				var result = $.formValidator.pageIsValid('1');
				
				if (result) {
					var loadingDialog = new Dialog();
					
					loadingDialog.init({
						contentHtml: '正在加载中……'
					});
				}
				els.qForm.trigger('submit');
			});
			
			//点击Tab3
			els.tab3.click(function(ev){
				ev.preventDefault();
				$("#autoSkip").val(0);
				$("#tabStatus").val("3");
				$("#tagId").val(0);
				els.qForm.trigger('submit');
			});
			
			//点击Tab4
			els.tab4.click(function(ev){
				ev.preventDefault();
				ev.stopPropagation();
				var _this = $(this),
					handling = $('#handling');
				
				if (handling.css('display') == 'none') {
					handling.css({
						width: _this.parent().outerWidth() - 2,
						left: $('#tab4').parent().position().left + 10
					});
					
					handling.slideDown();
					_this.parent().addClass('tab_cur');
				} else {
					handling.slideUp();
				}
				
			});
			// 鼠标移到 tr 显示标签层
			$('.tab_panel tbody tr td').mouseover(
				function() {
					var _this = $(this);
					if(_this.text()!='抱歉，暂无您所查找的数据'){
						$('.shortcut_label').css({
							top: _this.position().top + _this.height() - $('.shortcut_label', _this.parents('.tab_panel')).height() + 19,
							display: 'block'
						});
						var questionId = _this.parent().children().eq(0).find(".que_checkbox").attr("value");
						$(".shortcut_label").find("input").val(questionId);
					}
				}
			);
			// 鼠标移开 table 隐藏标签层
			$('.tab_panel .table').mouseleave(
				function() {
					var _this = $(this);
					
					$('.shortcut_label').css({
						//top: _this.position().top + _this.height() - $('.shortcut_label').height(),
						display: 'none'
					});
				}
			);
			$('.tab_panel .table .td_d').mouseover(
				function() {
					var _this = $(this);
					
					$('.shortcut_label').css({
						//top: _this.position().top + _this.height() - $('.shortcut_label').height(),
						display: 'none'
					});
				}
			);
			
			$('.msg_tab_panel textarea').focus(function() {
				$('.shortcut_label').hide();
			});
			
			
			// 点击处理中的“标签”
			$('#handling a').click(function(ev) {
				ev.preventDefault();
				ev.stopPropagation();
				if (!$(this).hasClass('set_label_dialog')) {
					$("#autoSkip").val(0);
					$("#tabStatus").val("2");
					if($(this).parent().val()!="")
						$("#tagId").val($(this).parent().val());
					else
						$("#tagId").val(0);
					els.qForm.trigger('submit');
				} else {
					$('#handling').slideUp();
				}
				
			});
			
			/*//翻页
			pagination.live("click",function(ev){
				ev.preventDefault();
				$("#autoSkip").val(0);
				els.currentPage.val($(this).attr("value"));
				setTimeout(function(){
					els.qForm.trigger('submit');
				},0);
			});*/
			
			//显示相同的运单号
			$(".showSameMail").click(function(ev){
				ev.preventDefault();
				var _this = $(this);
				var mailNo = _this.attr("name");
				$("#ship_num").val(mailNo);
				$("#autoSkip").val(0);
				els.qForm.trigger('submit');
			});
			
			//点击“未读”消息
			$(".unread").click(function(ev){
				
				if (!$(ev.target).hasClass('fold_desc') && !$(ev.target).hasClass('unfold_desc')) {
					$(this).removeClass("unread");
					var questionnaireIds = $(this).attr("name");
					markQue("1",questionnaireIds);
				}
			});
			
			//通知揽件网点
			$(".notifyCustomer").click(function(ev){
				ev.preventDefault();	
				var _this = $(this);
				var questionnaireId = _this.attr("name");
				var feedbackInfo = _this.parent().parent().find("textarea").val();
				if(feedbackInfo.length > 1000 || $.trim(feedbackInfo)==""){
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "消息内容字数在1-1000字之间!",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				} else{
					
					//关键词验证
					/*
					$.ajax({
							url:'questionnaire_toQuestionnaireFilter.action',
							type:'post',
							data:{
								"feedbackInfo" : feedbackInfo
							},
							dataType:'json',
							success:function(data){
							    var isvalid = data;
							    //var invalidWordsStr = data.invalidWordsStr;	 
							    if(isvalid=="false") {
							    	var viewDialog = new Dialog();
							    	viewDialog.init({
										contentHtml: '消息内容含有非法关键字，请修改！',
										yes: function() {
											viewDialog.close();
										},
										yesVal: '关 闭',
										closeBtn: true
									})
							    }else {
							    	//关键字验证通过
							    	*/
							    	var loadDialog = new Dialog();
					            	var msg = "正在发送中...";
					            	loadDialog.init({ 			
										contentHtml:msg
									});
							    	
							    	$.ajax({
										type:"POST",
										dataType:"json",
										cache:false,
										url:"questionnaire_send.action",
										data:{
											"questionnaireId" : questionnaireId,
											"feedbackInfo" : feedbackInfo
										},
										success:function(data){
											loadDialog.close();
											_this.parent().parent().find("textarea").val("");
											var oDialog = new Dialog();
											oDialog.init({
												contentHtml: data,
												yes: function() {
													oDialog.close();
													els.qForm.trigger('submit');
												},
												closeBtn: true
											});
										}
									});
							   /* 	
							    }	
							},
							error:function(){
								var viewDialog = new Dialog();
								viewDialog.init({
									contentHtml: '抱歉！系统繁忙，请稍后再试。',
									yes: function() {
										viewDialog.close();
									},
									yesVal: '确定',
									closeBtn: true
								})
							}
					});		
					*/
				}
			});
			
			//通知买家
			$(".sendCustomer").click(function(ev){
				ev.preventDefault();
				
				//查询返回参数
				var backStratDate = $("#start_date").val();
				var backEndDate = $("#end_date").val();
				var backShopName = $("#shop_name").val();
				var backQType = $("#q_type").val();
				var backQTypeVal = $("#q_type_val").val();
				var backIsShowSigned = $("#isShowSigned").val();
				var backInput = $("#ship_num").val();
				var backTabStatus = $("#tabStatus").val();
				var currentPage = $("#currentPage").val();
				
				var _this = $(this);
				var questionnaireId = _this.attr("name");
				var text = _this.parent().parent().find("textarea");
				var feedbackInfo = text.data('changed') ? text.val() : '';
				var bName = _this.parent().parent().find("input[name='bName']").val();
				var bMobile =_this.parent().parent().find("input[name='bMobile']").val();
				var bMailno =_this.parent().parent().find("input[name='bMailno']").val();
				
				feedbackInfo = feedbackInfo == text.data("default_value") ? '' : feedbackInfo; 
				feedbackInfo = $.trim(feedbackInfo);
				//检查文字长度,判断分多少信息查询
				var n = 1;
				var smsLength = feedbackInfo.length;
				if(smsLength>70) {
					smsLength = smsLength-70;
					n = Math.ceil(smsLength/67) + 1;
				}
				
				var sendDialog = new Dialog();
            	var msg = "正在发送中...";
            	sendDialog.init({ 			
					contentHtml:msg
				});
				
				//验证短信服务,是否处于开启状态
				$.ajax({
					url:'questionnaire_isAllowSend.action',
					type:'post',
					data:{
						"sendCount":n
					},
					dataType:'json',
					success:function(data){
						sendDialog.close();
					    var isvalid = data;
					    //var invalidWordsStr = data.invalidWordsStr;	 
					    if(isvalid!="-1") {
							var failureDialog = new Dialog();
							failureDialog.init({
								contentHtml:'亲，你的可发短信数为 '+isvalid+'，需要先去购买短信!',
								yes:function(){
									failureDialog.close();
									//window.open('payService_openSmsServiceAndToBuy.action?menuFlag=sms_package');
									var parm = "&currentPage2="+currentPage+"&backStratDate="+backStratDate+"&backEndDate="+backEndDate+"&backShopName="+backShopName+"&backQType="+backQType+"&backQTypeVal="+backQTypeVal+"&backIsShowSigned="+backIsShowSigned+"&backInput="+backInput+"&backTabStatus="+backTabStatus+"&pos=1";
									setTimeout(function() {
										var url = "payService_openSmsServiceAndToBuy.action?menuFlag=sms_package"+parm;
										url = encodeURI(url); 
										window.location = url;
									}, 0);
								},
								no:function(){
									failureDialog.close();
								},
								yesVal:'购买',
								noVal:'暂不购买',
								closeBtn: true
							})
					    	
					    }else {
					    	if(feedbackInfo.length > 1000 || feedbackInfo==""){
								var oDialog = new Dialog();
								oDialog.init({
									contentHtml: "消息内容字数在1-1000字之间!",
									yes: function() {
										oDialog.close();
									},
									closeBtn: true
								});
								return;
							} else{
								
								//验证手机号
					    		var reg = /^0?1[358]\d{9}$/;
								if (!reg.test(bMobile)) {
									var seDialog = new Dialog();
									seDialog.init({
										contentHtml: '抱歉，不是正确的手机号，不能发送短信！',
										yes: function() {			// 确认按钮的回调
											seDialog.close();
										}
									});
									return;
								}
								
								//关键词验证
								$.ajax({
										url:'questionnaire_toQuestionnaireFilter.action',
										type:'post',
										data:{
											"feedbackInfo" : feedbackInfo
										},
										dataType:'json',
										success:function(data){
										    var isvalid = data;
										    //var invalidWordsStr = data.invalidWordsStr;	 
										    if(isvalid=="false") {
										    	var viewDialog = new Dialog();
										    	viewDialog.init({
													contentHtml: '消息内容含有非法关键字，请修改！',
													yes: function() {
														viewDialog.close();
													},
													yesVal: '关 闭',
													closeBtn: true
												})
										    }else {
										    	//关键字验证通过
												if(n>1) {   //超过1条短信  //超过1条,发送确认
													var confirmDel = new Dialog();
													confirmDel.init({
														contentHtml: '该信息为'+feedbackInfo.length+'字符，将分'+n+'条短信发送，确定要发送吗?',
														iconType: 'question',
														yes: function() {
															confirmDel.close();
															//发送提示
															var loadDialog = new Dialog();
											            	var msg = "正在发送中...";
											            	loadDialog.init({ 			
																contentHtml:msg
															});
											            	//此处应与下面保持一直
															$.ajax({
																type:"POST",
																dataType:"json",
																cache:false,
																url:"questionnaire_sendCustomer.action",
																data:{
																	"sendCount":n,
																	"sendBuyerName": bName,
																	"sendBuyerMobile": bMobile,	
																	"sendMailno":bMailno,
																	"questionnaireId" : questionnaireId,
																	"feedbackInfo" : feedbackInfo
																},
																success:function(data){
																	loadDialog.close();
																	_this.parent().parent().find("textarea").val("");
																	var oDialog = new Dialog();
																	oDialog.init({
																		contentHtml: data,
																		yes: function() {
																			oDialog.close();
																			//els.qForm.trigger('submit');
																		},
																		closeBtn: true
																	});
																}
															});
														},
														no: function() {
															confirmDel.close();
														}
													});	
												}else {       //一条短信,无确认,直接发送
													//发送提示
													var loadDialog = new Dialog();
									            	var msg = "正在发送中...";
									            	loadDialog.init({ 			
														contentHtml:msg
													});
									            	//此处应与上面保持一直
													$.ajax({
														type:"POST",
														dataType:"json",
														cache:false,
														url:"questionnaire_sendCustomer.action",
														data:{
															"sendNumber":1,
															"sendBuyerName": bName,
															"sendBuyerMobile": bMobile,	
															"sendMailno":bMailno,
															"questionnaireId" : questionnaireId,
															"feedbackInfo" : feedbackInfo
														},
														success:function(data){
															loadDialog.close();
															_this.parent().parent().find("textarea").val("");
															var oDialog = new Dialog();
															oDialog.init({
																contentHtml: data,
																yes: function() {
																	oDialog.close();
																	//els.qForm.trigger('submit');
																},
																closeBtn: true
															});
														}
													});
												}	
										    }
										},
										error:function(){
											var viewDialog = new Dialog();
											viewDialog.init({
												contentHtml: '抱歉！系统繁忙，请稍后再试。',
												yes: function() {
													viewDialog.close();
												},
												yesVal: '确定',
												closeBtn: true
											})
										}
								});		
							}
					    	
					    }
					},
					error:function(){
						var viewDialog = new Dialog();
						viewDialog.init({
							contentHtml: '抱歉！系统繁忙，请稍后再试。',
							yes: function() {
								viewDialog.close();
							},
							yesVal: '确定',
							closeBtn: true
						})
					}
			});	
				
			});
			
			//写备注
			$(".remark").click(function(ev){
				ev.preventDefault();
				
				var loadDialog = new Dialog();
            	var msg = "正在保存中...";
            	loadDialog.init({ 			
					contentHtml:msg
				});
				
				var _this = $(this);
				var questionnaireId = _this.attr("name");
				var feedbackInfo = _this.parent().parent().find("textarea").val();
				if(feedbackInfo.length > 1000 || $.trim(feedbackInfo)==""){
					loadDialog.close();
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "消息内容字数在1-1000字之间!",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				} else{
					$.ajax({
						type:"POST",
						dataType:"json",
						cache:false,
						url:"questionnaire_remark.action",
						data:{
							"questionnaireId" : questionnaireId,
							"feedbackInfo" : feedbackInfo
						},
						success:function(data){
							loadDialog.close();
							_this.parent().parent().find("textarea").val("");
							var oDialog = new Dialog();
							oDialog.init({
								contentHtml: data,
								yes: function() {
									oDialog.close();
									els.qForm.trigger('submit');
								},
								closeBtn: true
							});
						}
					});
				}
			});
			
			//清空
			$(".clear").click(function(){
				var questionnaireId = $(this).attr("name");
				$(this).parent().parent().find("textarea").val("");
			});
			
			//网点通知上报网点
			$(".sendJinGang").click(function(){
				var _this = $(this);
				var questionnaireId = _this.attr("name");
				var feedbackJinGangContent = _this.parent().parent().find("textarea").val();
				var title = _this.attr("title");
				var status = 0;
				if(title=='保存')
					status = 0;
				else if(title=='完成')
					status = 1;
				if(feedbackJinGangContent.length > 1000 || $.trim(feedbackJinGangContent)==""){
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "消息内容字数在1-1000字之间!",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				} else{
					$.ajax({
						url:'questionnaire_updateJinGangQuestion.action',
						data:{
							questionnaireId : questionnaireId,
							feedbackJinGangContent : feedbackJinGangContent,
							statusJinGang : status
						},
						success:function(data){
							var oDialog = new Dialog();
							oDialog.init({
								contentHtml: data,
								yes: function() {
									_this.parent().parent().find("textarea").val("");
									oDialog.close();
								},
								closeBtn: true
							});
						}
					});
				}
			});
			
			//网点移动到其它
			$("#moveToOther").click(function(ev){
				ev.preventDefault();
				var checkedInput = $('.que_checkbox:checked'),questionnaireIds = "",l=checkedInput.length;
				if(l < 1){
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "请先选择要操作的问题件！",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				}
				for (var i=0;i<l; i++) {
					questionnaireIds += $(checkedInput[i]).val();
					if(i < l-1){
						questionnaireIds +=",";
					}
				}
				
				$.ajax({
					type:"POST",
					dataType:"json",
					cache:false,
					data : {
						questionnaireIds : questionnaireIds,
						tabStatus : 3
					},
					url:"questionnaire_move.action",
					success:function(data){
						var oDialog = new Dialog();
						oDialog.init({
							contentHtml: data,
							yes: function() {
								oDialog.close();
								els.qForm.trigger('submit');
							},
							closeBtn: true
						});
					}
				});
			});
			
			//网点标记为已读、未读
			$("#select_a").change(function(ev){
				ev.preventDefault();
				var checkedInput = $('.que_checkbox2:checked'),questionnaireIds = "",l=checkedInput.length;
				var readFlag = $(this).val();
				
				var readMailNos = "",unreadMailNos="";
				for (var i=0;i<l; i++) {
					questionnaireIds += $(checkedInput[i]).val();
					if(i < l-1){
						questionnaireIds +=",";
					}
					var mailno = $(checkedInput[i]).parent().find("#mailno").val();
					var state = $(checkedInput[i]).parent().find("#state").val();
					if(readFlag==1){//标记为已读
						if(state==1){//如果当前是已读状态则无法标记
							readMailNos += (mailno + ",");
						}
					}else if(readFlag==0){//标记未读
						if(state==0){//如果当前是未读状态则无法标记
							unreadMailNos += (mailno + ",");
						}
					}
				}
				if(readMailNos!=""){
					var oDialog = new Dialog();
					oDialog.init({
						cache:false,
						contentHtml: "您选择的运单("+readMailNos.substring(0,readMailNos.length-1)+")已是已读状态，请重新选择",
						yes: function() {
							oDialog.close();
							$("#select_a").val("");
						},
						closeBtn: true
					});
					return;
				}else if(unreadMailNos!=""){
					var oDialog = new Dialog();
					oDialog.init({
						cache:false,
						contentHtml: "您选择的运单("+unreadMailNos.substring(0,unreadMailNos.length-1)+")已是未读状态，请重新选择",
						yes: function() {
							oDialog.close();
							$("#select_a").val("");
						},
						closeBtn: true
					});
					return;
				}
				markQue(readFlag,questionnaireIds);
			});
			
			//卖家批量标记已读未读
			$(".mark_status").change(function(ev){
				ev.preventDefault();
				var checkedInput = $('.que_checkbox:checked'),questionnaireIds = "",l=checkedInput.length;
				var readFlag = $(this).val();
				var readMailNos = "",unreadMailNos="";
				for (var i=0;i<l; i++) {
					questionnaireIds += $(checkedInput[i]).val();
					if(i < l-1){
						questionnaireIds +=",";
					}
					var mailno = $(checkedInput[i]).parent().find("#mailno").val();
					var state = $(checkedInput[i]).parent().find("#state").val();
					if(readFlag==1){//标记为已读
						if(state==1){//如果当前是已读状态则无法标记
							readMailNos += (mailno+",");
						}
					}else if(readFlag==0){//标记未读
						if(state==0){//如果当前是未读状态则无法标记
							unreadMailNos += (mailno+",");
						}
					}
				}
				if(readMailNos!=""){
					var oDialog = new Dialog();
					oDialog.init({
						cache:false,
						contentHtml: "您选择的运单("+readMailNos.substring(0,readMailNos.length-1)+")已是已读状态，请重新选择",
						yes: function() {
							oDialog.close();
							$(".mark_status").val("");
						},
						closeBtn: true
					});
					return;
				}else if(unreadMailNos!=""){
					var oDialog = new Dialog();
					oDialog.init({
						cache:false,
						contentHtml: "您选择的运单("+unreadMailNos.substring(0,unreadMailNos.length-1)+")已是未读状态，请重新选择",
						yes: function() {
							oDialog.close();
							$(".mark_status").val("");
						},
						closeBtn: true
					});
					return;
				}
				if(questionnaireIds.length < 1){
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "请先选择要操作的问题件！",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					$(".mark_status").val("");
					return;
				}
				markQue(readFlag,questionnaireIds);
			});
			
			//执行标记操作
			var markQue = function(readFlag,qeIds){
				if(qeIds.length > 0){
					$.ajax({
						cache:false,
						url : "questionnaire_mark.action",
						data : {
							questionnaireIds : qeIds,
							tabStatus : readFlag
						},
						success : function(data){
							//$("#select_a").val("");
							
							var result = $.formValidator.pageIsValid('1');
							
							if (result) {
								var loadingDialog = new Dialog();
								
								loadingDialog.init({
									contentHtml: '正在加载中……'
								});
							}
							els.qForm.trigger('submit');
						}
					});
				} else{
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "请先选择要操作的问题件！",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					$("#select_a").val("");
					return;
				}
			}
			
			/**
			 * 问题件投诉
			 */
			$(".complaint").click(function(){
				var checkedInput = $('.que_checkbox:checked'),questionnaireIds = "",len=checkedInput.length;
				var mailNoStr = "";
				if(len<=0){
					var oDialog = new Dialog();
					oDialog.init({
						cache:false,
						contentHtml: "请先选择要操作的问题件！",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				}
				for (var i=0;i<len; i++) {
					questionnaireIds += $(checkedInput[i]).val();
					mailNoStr += $(checkedInput[i]).attr("val");
					if(i < len-1){
						questionnaireIds +=",";
						mailNoStr += ",";
					}
				}
				var complaintDialog = new Dialog();
				var tab = '<div id="dialog_pop"> <meta charset="UTF-8" />' +
		          '<form id="complaint_form" action="" method="post">' +
		          '<p>运单号：'+mailNoStr+'</p>'+
		          '<p>未回复投诉：</p>'+
				  '<p><textarea name="complaintContent" id="complaintContent" class="textarea_text" cols="100" rows="5"></textarea><span id="complaintContentTip"></span></p>'+
				  '<p class="dialog_btn"><a href="javascript:;" class="btn btn_f" title="提交" id="dialog_save"><span>提交</span></a>'+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				  '<a href="javascript:;" class="btn btn_f" title="取消" id="dialog_cancel"><span>取消</span></a>'+'</p>'+
				  '</form>' +
				  '	</div>' ;
				
				complaintDialog.init({
					cache:false,
					contentHtml: tab,
					closeBtn: false
				});
				
				//弹出框信息表单
				$.formValidator.initConfig({
					validatorGroup: '2',
					formID: 'complaint_form',
					theme: 'yto',
					errorFocus: false
				});
				
				$('#complaintContent').
					formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ''}).
					inputValidator({min: 1, max:1000, onErrorMin: '投诉内容不能为空', onErrorMax: '投诉内容不能超过500字'});
				
				$('#dialog_save').live('click',function(ev) {
					var flag = $.formValidator.pageIsValid('2');
					if(flag){
						var complaintContent = $("#complaintContent").val();
						$.ajax({
							url : 'questionnaire_complaint.action',
							cache: false,
							data:{
								questionnaireIds : questionnaireIds,
								complaintContent : complaintContent
							},
							dataType:"json",
							success : function(data){
								complaintDialog.close();
								var completeDialog = new Dialog();
								completeDialog.init({
									cache:false,
									contentHtml: data,
									yes : function(){
										completeDialog.close();
										els.qForm.trigger('submit');
									},
									closeBtn: false
								});
								return;
							}
						});
					}
				});
				
				$('#dialog_cancel').live('click',function(ev) {
					complaintDialog.close();
				});
				
			});
			
			$(".doneNotify").each(function(){
				$(this).mouseover(function(ev){
					var content = $(this).find(".topdes").text();
					if($.trim(content)!='')
						$(this).find(".show_btn").css("display","block");
				});
				$(this).mouseout(function(ev){
					ev.stopPropagation();
					$(this).find(".show_btn").css("display","none");
				});
				//网点沟通记录
				$(this).find(".showQuesDeal").click(function(){
					var val = $(this).attr("name");
					var dialog = new Dialog();
					dialog.init({
						contentHtml: $("#showQuesDeal"+val).html(),
						closeBtn: true
					});
					return;
				});
				//标记未读、已读
				$(this).find(".show_btn").find("a").click(function(){
					var questionnaireIds = $(this).attr("name");
					var title = $(this).attr("title");
					var state = $(this).attr("state");
					var readFlag=0;
					if(title=='已读'){
						readFlag = 1;
						if(state==1){//如果当前是已读状态则无法标记
							var oDialog = new Dialog();
							oDialog.init({
								cache:false,
								contentHtml: "当前已是已读状态，无法标记",
								yes: function() {
									oDialog.close();
								},
								closeBtn: true
							});
							return;
						}
					}
					if(title=='未读'){
						readFlag = 0;
						if(state==0){//如果当前是未读状态则无法标记
							var oDialog = new Dialog();
							oDialog.init({
								cache:false,
								contentHtml: "当前已是未读状态，无法标记",
								yes: function() {
									oDialog.close();
								},
								closeBtn: true
							});
							return;
						}
					}
					markQue(readFlag,questionnaireIds);
				});
			});
			
			//开启自动通知客户
			els.autoNotify.click(function(){
				var _this = $(this);
				var autoNotify = 0;
				if($(this).attr("checked")=="checked"){
					autoNotify = 1;
				}
				if($(this).attr("checked")=="checked"){
					var content = "勾选后系统将问题件自动通知客户，30分钟后生效，如果有些问题件不想通知客户，请不要勾选，否则后果自负！";
					var dialog = new Dialog();
					dialog.init({
						contentHtml: content,
						yes:function(){
							$.ajax({
								url:'questionnaire_openAutoNotify.action',
								data:{
									autoNotify : autoNotify
								},
								success:function(data){
									dialog.close();
								}
							});
						},
						no:function(){
							if(_this.attr("checked")=="checked"){
								_this.removeAttr("checked");
							}else{
								_this.attr("checked","checked");
							}
							dialog.close();
						},
						closeBtn: false
					});
				}else{
					var content = "取消后，系统将不再将问题件自动通知客户，30分钟后生效。";
					var dialog = new Dialog();
					dialog.init({
						contentHtml: content,
						yes:function(){
							$.ajax({
								url:'questionnaire_openAutoNotify.action',
								data:{
									autoNotify : autoNotify
								},
								success:function(data){
									dialog.close();
								}
							});
						},
						no:function(){
							if(_this.attr("checked")=="checked"){
								_this.removeAttr("checked");
							}else{
								_this.attr("checked","checked");
							}
							dialog.close();
						},
						closeBtn: false
					});
				}
			});
			
			// 自动推送类型说明层
			els.question.click(function(){
				var content = '<div class="note">' +
					          '   <p class="green">以下问题件类型系统已自动推送给卖家，勾选自动通知客户，所有问题件类型都将自动推送给卖家，如果有些问题件不想通知客户，请不要勾选，否则后果自负！</p>' +
							  '   <table class="types">' +
							  '      <tr>' +
							  '          <td class="td_a">1. 面单填写不规范</td>' +
							  '          <td class="td_b">2. 超区运单</td>' +
							  '          <td class="td_c">3. 快件爆仓</td>' +
							  '      </tr>' +
							  '      <tr>' +
							  '          <td class="td_a">4. 收件客户拒收件</td>' +
							  '          <td class="td_b">5. 节假日客户休息</td>' +
							  '          <td class="td_c">6. 收件客户已离职</td>' +
							  '      </tr>' +
							  '      <tr>' +
							  '           <td class="td_a">7. 收件客户要求更改收件地址</td>' +
							  '           <td class="td_b">8. 地址不详，且电话联系不上</td>' +
							  '           <td class="td_c">9. 地址不详，且电话为传真或无人接听</td>' +
							  '      </tr>' +
							  '      <tr>' +
							  '           <td class="td_a">10. 地址不详，且电话与收件客户本人不符</td>' +
							  '           <td class="td_b">11. 地址不详，且无收件人的电话</td>' +
							  '           <td class="td_c">12. 违禁品</td>' +
							  '      </tr>' +
							  '      <tr>' +
							  '           <td class="td_a">13. 到付费</td>' +
							  '           <td class="td_b">14. 代收款</td>' +
							  '           <td class="td_c">15. 签收失败</td>' +
							  '      </tr>' +
							  '      <tr>' +
							  '          <td class="td_a">16. 其他原因</td>' + 
							  '          <td class="td_b"></td>' +
							  '          <td class="td_c"></td>' +
							  '      </tr>' +
							  '   </table>' +
							  '<div>';
				
				var dialog = new Dialog();
				dialog.init({
					contentHtml: content,
					yes:function(){
						dialog.close();
					},
					yesVal:'我知道了',
					closeBtn: false
				});
			});

		// 通知用户
	    var note_user = function(){
			$('.default_text').each(function(){
				var _this = $(this),
					name = _this.siblings('input[name="bName"]').val(),
					mobile = _this.siblings('input[name="bMobile"]').val();

				_this.defaultTxt('姓名: ' + name + '   电话: ' + mobile);
				_this.data('default_value', '姓名: ' + name + '   电话: ' + mobile);
				_this.data('changed', false);
				
				_this.change(function(){
					_this.data('changed', true);
				});
			});
		};
		
		// 通知客户切换
		var note_tab = function(){
			$('.note_tab .note_tab_triggers li a').click(function() {
				var index = $(this).parent().index();
				$(this).parent().addClass('tab_cur').siblings().removeClass('tab_cur');
				$('.note_tab_panels li',$(this).parents('.note_tab')).eq(index).show().siblings().hide();
			});
			
			
			$('.msg_tab_triggers li').click(function(ev) {
				ev.preventDefault();
				var _this = $(this),
					index = _this.index();
				_this.addClass('cur_msg_tab').siblings().removeClass('cur_msg_tab');
				$('.msg_tab_panel', _this.parents('.msg_tab')).eq(index).show().
					siblings().hide();
			});
			
		};
		
		// 回到顶部
		var toTop = function() {
			var toTop = $('#to_top');
			var winEl = $(window);
			var posLeft = $('#main').offset().left + 960;
			var posTop = winEl.outerHeight() - 54;
			// 计算按钮位置
			if ($.browser.msie && ($.browser.version == '6.0') && !$.support.style) {
				setTimeout(function() {
					toTop.PositionFixed({
						x: 960,
						y: winEl.height() - $('#main').scrollTop
					});

				}, 1000)
			} else {
				toTop.css({
					left: posLeft
				});
			}
			
			// 监听滚动条
			winEl.scroll(function() {
				var scrollTop = $(this).scrollTop();
				
				
				if (scrollTop > 10) {
					toTop.fadeIn(300);
				} else {
					toTop.fadeOut(300);
				}
			});
			
			// 点击“回到顶部”
			toTop.click(function(ev) {
				ev.preventDefault();
				
				$('html, body').animate({scrollTop:0}, 'fast');

			});
		};
		
		
		// 展开收拢
		/*var fold = function() {
			
			// 点击展开
			$('.fold_desc').live('click', function(ev) {
				ev.preventDefault();

				var _this = $(this),
				    que = _this.closest('.topdes').find('.que_issue');
				
				que.html(que.data("orgin"));
				_this.replaceWith('<a href="javascript:;" class="unfold_desc">收拢</a>');
			});
			
			// 点击收拢
			$('.unfold_desc').live('click', function(ev) {
				ev.preventDefault();

				var _this = $(this), 
				que = _this.closest('.topdes').find('.que_issue');
				
				que.html(que.data("cut"));
				_this.replaceWith('<a href="javascript:;" class="fold_desc">展开</a>');
			});
			
			$('.que_issue').each(function(){
				var _this = $(this),
				    reg = /<\/?[^>]+>/gi,
				    q_html = _this.html(),
				    q_val = q_html.replace(reg,'');
				    
				if(q_val.length > 100){
					
					q_val = q_html.split(reg)[0];
					q_val = (q_val.length > 100 ? q_val.substr(0,100) : q_val) + '...';
					
					_this.parent().append('<a class="fold_desc" href="javascript:;">展开</a>');
					_this.data("cut", q_val);
					_this.data("orgin", q_html);
					_this.html(_this.data("cut"));
				}
				
				_this.show();
			});
			
			
		};*/
		
		//String.prototype.trim = function () {
		//	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
		//} 
		
		return {
			init: function() {
				textDefault();
				note_user();
				ytoTab.init($("#tabStatus").val()-1);
				checked();
				wangTip();
				//shipNumDetail();
				simulateSel();
				simulateSel2();
				form();
				note_tab();
				toTop();
				setLabel();
				//fold();
			}
		}
	});
	//question.init();
})