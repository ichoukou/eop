$(function() {
	var newPrint = (function() {

		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		
		// 配置
		var config = {
			printsGetUrl: winParams.printsGetUrl,              // 获取打印数据 url
			customerGetUrl: winParams.customerGetUrl,          // 获取客户 url
			ticketcodeGetUrl: winParams.ticketcodeGetUrl,      // 获取相关运单号 url
			rangeSaveUrl: winParams.rangeSaveUrl,              // 保存运单号段 url
            rangGetUrl: winParams.rangGetUrl,                  // 获取运单号段 url
            userId: winParams.userId,
            appCode: winParams.appCode
		};

		var els = {
			p_form: $('#print_fm'),
			p_ie: $('#isIE'),
			codeTxt: $('.notice #code'),
			mag_btn: $('.notice .manage')
		};

		// 显示提示
		var showIcon = {
			correct: function(el, text) {
				el.html('<span>' + text + '</span>');
			},
			error: function(el, text) {
				el.html('<span class="yto_onError">' + text + '</span>');
			}
		};
					
		// 默认文案
		var textDefault = {
			start: '起始运单号',	
			end: '末尾运单号',
			code: '扫描发货单号后立即打印'
		};

		// 错误文案
		var msg = {
			codeFormate: '运单号格式错误',
			codeRange: '起始运单号不能大于末尾运单号！'
		};
		
		var browser = function(){
			if($.browser.msie){
				els.p_ie.val("1");
			}
		};
		
		var dialog = function(){
			window.newPrints = {
				showDialog: function(code, status, num, isIE){
					var _els = els;
						
					if(code === 'S0000001'){
						if(status === 'E0000002' || !num){
							var dialog = new Dialog();
							dialog.init({
								contentHtml: "没有运单号了，请到<段号管理>进行新增",
								yesVal: "进入段号管理",
								noVal: "稍后再说",
								yes: function(){
									dialog.close();
									_els.mag_btn.trigger("click");
								},
								no: function(){
									dialog.close();
								}
							});
						}
					}
					else if(code === 'E0000001'){
						var dialog = new Dialog();
						dialog.init({
							contentHtml: "该订单号未同步过来，请稍后再试",
							closeBtn: true
						});
					}
					
					if(isIE !== 1){
						var dialog = new Dialog();
						dialog.init({
							contentHtml: "此打印功能仅支持 IE 浏览器",
							closeBtn: true
						});
					}
				}	
			}
		};

		// 扫描打印
		var scan = function(){
			els.codeTxt.defaultTxt(textDefault.code);
			els.codeTxt.focus();
			els.p_form.trigger('submit');
			
			$(els.p_form).keypress(function(ev){
				if(ev.which === 13){
					ev.preventDefault();
				}
				
				var code = els.codeTxt.val();
				
				if(ev.which !== 13 || code === ''){
					return;
				}
				
				setTimeout(function(){
					els.p_form.trigger('submit');
					els.codeTxt.val('');
					els.codeTxt.focus();
				}, 0);
			});
		};
        
		// 运单号号段管理
		var manage = function() {
			els.mag_btn.click(function(ev){
				ev.preventDefault();

				var code = els.codeTxt.val();
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: '正在获取数据...'
				});

				$.ajax({
					type: 'POST',
					url: config.customerGetUrl,
					data:{"userId": config.userId, "appCode": config.appCode},
					success: function(data) {
						
						aDialog.close();

						if(!data){
							return;
						}

						var customers = data.result,
							bDialog = new Dialog(),
							contentHtml = [],
							timer = null;

						contentHtml.push('<div class="msg_dialog">');
						contentHtml.push('   <div class="info">');
						contentHtml.push('      <p>');
						contentHtml.push('         <label>运单号号段管理：</label>');
						contentHtml.push('         <span id="ticket_codeTip"></span>');
						contentHtml.push('      </p>');
						contentHtml.push('      <p>');
						contentHtml.push('         <select id="cst_sel">');
						contentHtml.push('            <option value="">请选择客户</option>');
						
						$.each(customers, function(i,customer){
							contentHtml.push('            <option value="' + customer.id + '">' + customer.userName + '</option>');
						});
						
						contentHtml.push('         </select>');
						contentHtml.push('         <a id="range_add" title="新增号段" class="btn btn_disable" href="javascript:;"><span>新增号段</span></a>');
						contentHtml.push('      </p>');
						contentHtml.push('      <input type="hidden" id="ticket_code" />');
						contentHtml.push('   </div>');
						contentHtml.push('   <div class="warning">谨慎！！填写号段区间后不能删除及修改，若填写错误，请后果自负</div>');
						contentHtml.push('   <div class="ranges">');
						contentHtml.push('      <p class="first">号段管理</p>');
						contentHtml.push('   </div>');
						contentHtml.push('</div>');
						
						bDialog.init({
							contentHtml: contentHtml.join(''),
							yesVal: '确定',
							noVal: '取消',
							yes: function() {
								bDialog.close();
							},
							no: function(){
								bDialog.close();
							}
						});

						var msgEls = {
							addBtn: $('.msg_dialog #range_add'),
							cstSelect: $('.msg_dialog #cst_sel'),
							codeTip: $('.msg_dialog #ticket_codeTip'),
							rangeDiv: $('.msg_dialog .ranges'),
							ticketCode: $('.msg_dialog #ticket_code')
							
						};

						msgEls.ticketCode.val(code);

						$('.ranges a.save').live("click", function(ev){
							
							var btn = $(this),
								newRange = btn.closest('.new'),
								start = newRange.find('input:eq(0)'),
								end = newRange.find('input:eq(1)'),
								code = msgEls.ticketCode.val(),
								id = msgEls.cstSelect.val();

							if(btn.hasClass('disabled')){
								return;
							}
							
							start.trigger("change");
							if(newRange.data("error")){
								return;
							}
							end.trigger("change");
							if(newRange.data("error")){
								return;
							}

							start.attr("disabled","disabled");
							end.attr("disabled","disabled");

							if(timer){
								clearTimeout(timer);
							}

							timer = setTimeout(function(){
								$.ajax({
									type: 'POST',
									url: config.rangeSaveUrl,
									data:{"appCode": config.appCode, "userId": id, "beginNo":start.val(),"endNo":end.val()},
									dataType: 'jsonp',
								    jsonp: 'callback',
									success: function(data) {
										if(data && data.resultCode === 'S0000001'){
											newRange.html(start.val() + ' 至 ' + end.val());
											newRange.removeClass('new');
										}
										btn.removeClass('disabled').css("cursor","pointer");
									}
								});
								btn.addClass('disabled').css('cursor','wait');
							}, 3000);	
						});

						// 选择客户
						msgEls.cstSelect.change(function(ev){
							var _this = $(this),
								id = msgEls.cstSelect.val();

							msgEls.addBtn.removeClass('btn_d').addClass('btn_disable');
							msgEls.rangeDiv.html('<p class="first">号段管理</p>');
							if(_this.val() === ''){
								return;
							}
							
							msgEls.addBtn.removeClass('btn_disable').addClass('btn_d');

							$.ajax({
								type: 'POST',
								url: config.rangGetUrl,
								data:{"userId": id, "appCode": config.appCode},
								dataType: 'jsonp',
							    jsonp: 'callback',
								success: function(data) {
									if(data && data.resultCode !== 'S0000001'){
										return;
									}
									
									var ranges = data.resultList,
									rangeDiv = msgEls.rangeDiv;

									if(ranges.length){
										var rangeHtml = [];
										rangeHtml.push('<p class="first">号段管理</p>');
										$.each(ranges, function(i, range){
											rangeHtml.push('<p class="range">' + range.startNo + ' 至 ' + range.endNo + '</p>');
										});
										rangeDiv.html(rangeHtml.join(''));
									}
									else{
										rangeDiv.html('<p class="first">号段管理</p>');
									}
								}
							});
						});

						// 新增号段
						msgEls.addBtn.click(function(ev){
							ev.preventDefault();

							var _this = $(this),
								_msgEls = msgEls,
								_msgDefult = textDefault,
							    _error = msg,
							    _showIcon = showIcon,
							    _reg = /^\d{10}$/;

							if(_this.hasClass('btn_disable')){
								return;
							}

							_msgEls.rangeDiv.append('<p class="range new last"><input type="text" class="input_text" maxlength="10" />至<input type="text" class="input_text" maxlength="10" /><a class="save" href="javascript:;">保存</a></p>');

							var newRange = _msgEls.rangeDiv.find('.new:last'),
								start = newRange.find('input:eq(0)'),
						        end = newRange.find('input:eq(1)');
							
							start.defaultTxt(_msgDefult.start);
							end.defaultTxt(_msgDefult.end);

							start.change(function(ev){
								var _this = $(this),
									_start = _this.val(),
									_end = end.val(),
									_newRange = newRange;

								if(!_reg.test(_start)){
									_showIcon.error(msgEls.codeTip, _error.codeFormate);
									_newRange.data("error","formate");
								}
								else if(parseInt(_start, 10) >= parseInt(_end, 10)){
									_showIcon.error(msgEls.codeTip, _error.codeRange);
									_newRange.data("error","range");
								}
								else{
									_showIcon.correct(msgEls.codeTip, ' ');
									_newRange.removeData("error");
								}
							});

							end.change(function(ev){
								var _this = $(this),
									_end = _this.val(),
									_start = start.val(),
									_newRange = newRange;

								if(!_reg.test(_end)){
									_showIcon.error(msgEls.codeTip, _error.codeFormate);
									_newRange.data("error","formate");
								}
								else if(parseInt(_end, 10) <= parseInt(_start, 10)){
									_showIcon.error(msgEls.codeTip, _error.codeRange);
									_newRange.data("error","range");
								}
								else{
									_showIcon.correct(msgEls.codeTip, ' ');
									_newRange.removeData("error");
								}
							});
							
						});
					}
				});
			});
		};
		
		return {
			init: function() {
				browser();
				dialog();
				scan();
				manage();
			}
		}
	})()
	
	newPrint.init();
})