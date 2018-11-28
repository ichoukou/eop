$(function() {
	var filter_keys = (function() {
		
		var winParams = window.params || {};
		// 配置
		var config = {
			keywordsAction: winParams.keywordsAction || ''			// 添加/删除关键词
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
		
		// 文案
		var msg = {
			confirmAddText: '你确定要添加该关键词吗？',
			confirmDelText: '你确定要删除该关键词吗？',
			keywordsSameErr: '该关键词已存在',
			keywordsMissErr: '该关键词不存在',
			keywordsEmptyErr: '关键词不能为空',
			keywordsLongErr: '关键词不能超过20个字符',
			addSucc: '添加成功',
			delSucc: '删除成功',
			inputText: '输入关滤词'
		};
		
		// 表单
		var form = function() {
			$('#keywords').defaultTxt(msg.inputText);
		};
		
		
		// 添加关键词
		var addKeywords = function() {
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			$('#keywords').
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({
					fun: function(val, el) {
						if (val == msg.inputText) {
							return false;
						}
					},
					onError: msg.keywordsEmptyErr
				}).
				inputValidator({min: 1, max: 20, onErrorMin: msg.keywordsEmptyErr, onErrorMax: msg.keywordsLongErr});
				
			$('#add_keyword').click(function(ev) {
				ev.preventDefault();
				
				
				var keywordsInput = $('#keywords').val(),
					filterRuleId = $('#filterRuleId').val(),
					keywordsTextarea = $.trim($('#key_textarea').text());
				if ( $.formValidator.pageIsValid('1') ) {
					var keywordsArr = keywordsTextarea.split('、'),		// 分割关键词字符串
						hasSame = false;								// 默认关键词不存在
					
					// 遍历查找关键词是否存在
					for (var i=0,l=keywordsArr.length; i<l; i++) {
						if (keywordsInput == keywordsArr[i]) {
							hasSame = true;
						}
					}
					
					
					if (!hasSame) {					// 如果关键词不存在
						// 确认弹窗
						var confirmAdd = new Dialog();
						
						confirmAdd.init({
							contentHtml: msg.confirmAddText,
							iconType: 'question',
							yes: function() {
								$.ajax({
									url: 'filterWords_add.action',
									type: 'GET',
									data: {
										keyWords: keywordsInput,
										filterRuleId: filterRuleId
									},
									dataType: 'json',
									cache: false,
									success: function(data) {
										if (data) {
											$('#key_textarea').html(data);
											
											confirmAdd.close();
											
											var addSuccDialog = new Dialog();
											
											addSuccDialog.init({
												contentHtml: msg.addSucc,
												iconType: 'success',
												autoClose: 2000
											});
										}
									}
								})
							},
							no: function() {
								confirmAdd.close();
							}
						});
						
						showIcon.correct($('#keywordsTip'), ' ');
					} else {					// 如果关键词已存在
						showIcon.error($('#keywordsTip'), msg.keywordsSameErr);
					}
				}
				


			})
		};
		
		// 删除关键词
		var delKeywords = function() {
			$('#del_keyword').click(function(ev) {
				ev.preventDefault();
				
				var keywordsInput = $('#keywords').val(),
					filterRuleId = $('#filterRuleId').val(),
					keywordsTextarea = $.trim($('#key_textarea').text());
				
				if (keywordsInput != msg.inputText) {		// 如果输入内容不为空
				
					var keywordsArr = keywordsTextarea.split('、'),		// 分割关键词字符串
						hasSame = false;								// 默认关键词不存在
					
					// 遍历查找关键词是否存在
					for (var i=0,l=keywordsArr.length; i<l; i++) {
						if (keywordsInput == keywordsArr[i]) {
							hasSame = true;
						}
					}
					
					
					if (!hasSame) {					// 如果关键词不存在
						showIcon.error($('#keywordsTip'), msg.keywordsMissErr);
					} else {					// 如果关键词已存在
					
						// 确认弹窗
						var confirmDel = new Dialog();
						
						confirmDel.init({
							contentHtml: msg.confirmDelText,
							iconType: 'question',
							yes: function() {
								$.ajax({
									url: 'filterWords_del.action',
									type: 'GET',
									data: {
										keyWords: keywordsInput,
										filterRuleId: filterRuleId
									},
									dataType: 'json',
									cache: false,
									success: function(data) {
										if (data) {
											$('#key_textarea').html(data);
											
											confirmDel.close();
											
											var delSuccDialog = new Dialog();
											
											delSuccDialog.init({
												contentHtml: msg.delSucc,
												iconType: 'success',
												autoClose: 2000
											});
										}
									}
								})
							},
							no: function() {
								confirmDel.close();
							}
						});
						
						showIcon.correct($('#keywordsTip'), ' ');
					
						
					}
					
				} else {									// 如果输入内容为空
					showIcon.error($('#keywordsTip'), msg.keywordsEmptyErr);
				}
			})
		};
		
		return {
			init: function() {
				form();
				addKeywords();
				delKeywords();
			}
		}
		
	})();
	filter_keys.init();
	
})