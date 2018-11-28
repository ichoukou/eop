/**
 * 订单导入
**/
$(function() {
	var surPrint = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			userType: winParams.userType || 0						// 1 == 卖家，2 == 网点
		};
		
		var areaErr = false;
		var uploadInterval;
		
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
			$('.check_all').checkAll( $('input[type="checkbox"]') );
		};
		
		//搜索
		var textsearch = function() {
			$('#search_txt').defaultTxt('运单号/订单号');
		};
		
		// 弹层
		var notice = function(){
			if(config.userType !== 2){
				return;
			}
			
			var n_hidden = $('#notice'),
			    n_flag = $('#flag'),
			//console.log(n_hidden, n_flag);
			    content = '<div id="notice_content">' +
			              '   <h1>通知：录单模板格式调整</h1>' +
			              '   <p class="note">' +
			              '      应总公司要求，为提升信息的准确性，易通电商版的录单模板' +
			              '      <span class="blue">原计划默认"网点地址"作为"发货地址"，现调整为：录单模板中加入"发货地址"一列</span>' +
			              '      请各位同事知悉，' +
			              '      <span class="blue">重新下载新的模板</span>' +
			              '      来完成录单工作，谢谢！' +
			              '      <span class="warning red">(注意：为方便各位过渡，旧模板可正常使用，但不推荐，使用旧模板录单带来后果由网点承当。旧模板将于2012-09-15取消使用)</span>' +
			              '   </p>' +
			              '   <p class="never">' +
			              '      <input type="checkbox" id="notice_chk" />' +
			              '      <span>不再显示</span>' +
			              '   </p>' +
			              '</div>';
			
			if(n_hidden.val() === '0' && n_flag.val() === '1'){
				var nDialog = new Dialog();
				nDialog.init({
					contentHtml: content,
					yesVal: '确定',
					yes: function(){
						var chk = $('#notice_chk');
						if(chk.prop("checked")){
							$.ajax({
								url: 'orderImport_showNotice.action',//window.loaction.href + '?check=1',
								type: 'GET',
								cache: false,
								success: function(){
									// do nothing...
								}
							});
						}
						nDialog.close();
					}
					//closeBtn: true
				});
			}
			/*notice.change(function(){
				var _this = $(this);
				
				if(_this.prop("checked")){
					// ajax;
				}
			});*/
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
					
				if (targetClass.indexOf('del_item') == -1 &&
					targetClass.indexOf('td_check') == -1
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
		};
		
		// 验证方法
		var check = {
			hasCheckedTemplate: function() {
				for (var i=0, l=$('.download_template').length; i<l; i++) {
					if ($('.download_template').eq(i).prop('checked')) {
						return true;
					}
				}
				return false;
			},
			
			
			area: function() {
				switch($('#province_p select:visible').length) {
					case 1:
						showIcon.error($('#area_tip'), '请选择省');
						areaErr = true;
					break;
					
					case 2:
						if ($('#province_p select').eq(1).val() == '') {
							showIcon.error($('#area_tip'), '请选择市');
							areaErr = true;
						} else {
							showIcon.correct($('#area_tip'), ' ');
							areaErr = false;
						}						
					break;
					
					case 3:						
						if ($('#province_p select').eq(2).val() == '') {
							showIcon.error($('#area_tip'), '请选择区');
							areaErr = true;
						} else {	
							showIcon.correct($('#area_tip'), ' ');
							areaErr = false;
						}	
					break;
				}				
			}
		};
		
		// 下载表格模板 
		var download = function() {
			var checkTemplate;
			$('.download_template').live('change', function() {
				checkTemplate = check.hasCheckedTemplate();				
				if (checkTemplate) {
					$('#dialog_ft .btn', $(this).parents('#dialog')).removeClass('btn_e').addClass('btn_d');
				} else {
					$('#dialog_ft .btn', $(this).parents('#dialog')).removeClass('btn_d').addClass('btn_e');
				}
			});
			
			$('#merger_orders_btn').click(function(ev) {
				clearInterval(uploadInterval);
				ev.preventDefault();
				var downloadDialog = new Dialog();
				var choseFileMaijia = '<form id="orderImport_downLoadZip_action" action="orderImport_downLoadZip.action" target="_parent" method="post">' +
									'	<table>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（卖家）默认订单表v2.0.csv"/>默认模板（省市区不分开）</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（卖家）默认订单表二v1.0.csv"/>默认模板二（省市区分开）</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（卖家）拍拍订单表v2.0.csv"/>拍拍模板</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（卖家）京东订单表v1.0.csv"/>京东模板</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（卖家）阿里巴巴订单表v2.0.csv"/>阿里模板</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（卖家）淘宝订单表v2.0.csv"/>淘宝模板</td>' +
									'		</tr>' +
									'	</table>' +
									'</form>';
				var choseFileWangdian = '<form id="orderImport_downLoadZip_action" action="orderImport_downLoadZip.action" target="_parent" method="post">' +
									'	<p style="line-height:1.5">1.如果您跟客户拿不到拍拍等平台数据表，建议使用默认模板<br>2.挑您喜欢的模板去使用</p>' +
									'	<table>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（网点）默认订单表v2.0.csv"/>默认模板（省市区不分开）</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（网点）默认订单表二v2.0.csv"/>默认模板二（省市区分开）</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（网点）拍拍订单表v3.0.csv"/>拍拍模板</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（网点）京东订单表v2.0.csv"/>京东模板</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（网点）阿里巴巴订单表v3.0.csv"/>阿里模板</td>' +
									'		</tr>' +
									'		<tr>' +
									'			<td><input type="checkbox" class="download_template" name="downLoadPaths" value="/template/（网点）淘宝订单表v3.0.csv"/>淘宝模板</td>' +
									'		</tr>' +
									'	</table>' +
									'</form>';
				var content = '';
				if(params.userType==1){
					content = choseFileMaijia;
				}else{
					content = choseFileWangdian;
				}
				downloadDialog.init({
					maskOpacity: 0,
					contentHtml: content,
					no: function() {
						if (checkTemplate) {	
							setTimeout(function(){
								$('#orderImport_downLoadZip_action').trigger('submit');
							},5);
						}	
					},
					noVal: '下载',
					closeBtn: true
				})
			})
		};
		
		// 批量上传
		var upload = function() {
			$('#fill_out_btn').click(function(ev) {
				ev.preventDefault();
				uploadInterval = setInterval(function() {
					if ($('#upload').val() != '') {
						$('#dialog_ft .btn', $('#upload').parents('#dialog')).removeClass('btn_e').addClass('btn_d');
						
						
						$('#dialog .btn').css('cursor', 'pointer').click(function() {
							var val = $('#upload').val(),
								valLen = val.length;
								
							if (val.substring(valLen-4, valLen) != '.csv') {
								showIcon.error($('#uploadTip'), '表格格式不正确，请使用".csv"格式~');
								$('#upload').val('');
							} else {
								showIcon.correct($('#uploadTip'), ' ');
								setTimeout(function() {
									$('#orderImport_upload_action').trigger('submit');
								}, 5)		
							}
						});
						$('#dialog .btn span').css('cursor', 'pointer');
					} else {
						$('#dialog_ft .btn', $('#upload').parents('#dialog')).removeClass('.btn_d').addClass('.btn_e');				
						
						$('#dialog .btn').unbind('click').css('cursor', 'not-allowed');
						$('#dialog .btn span').css('cursor', 'not-allowed')
					}
				}, 100);
				
				
				var uploadDialog = new Dialog();
				var uploadFile = '<form id="orderImport_upload_action" action="orderImport_upload.action" method="post" enctype="multipart/form-data">' +
									'	<table>' +
									'		<tr>' +
									'			<td>' +
									'				<input type="file" id="upload" value="" name="upload"/>' +
									'				<span style="display:block;" id="uploadTip"></span>' +
									'			</td>' +
									'		</tr>' + 
									'	</table>' +
									'   <input type="hidden" name="menuFlag" value="fahuo_orderimport" />'+
									'</form>';
				uploadDialog.init({
					contentHtml: uploadFile,
					no: function() {
						var val = $('#upload').val(),
							valLen = val.length;						
						if (val.substring(valLen-4, valLen) != '.csv') {
							showIcon.error($('#uploadTip'), '表格格式不正确，请使用".csv"格式~');
							$('#upload').val('');
						} else {
							showIcon.correct($('#uploadTip'), ' ');
							
							setTimeout(function(){
								$('#orderImport_upload_action').trigger('submit');
							},5);
						}
					},
					noVal: '上传',
					closeBtn: true
				});			
			});
			
			
			if (params.importFileErrList.length <= 10 && params.importFileErrList.length != 0) {	// 少于10个的格式错误
				var tableTrs = '';
				for (var i=0,l=params.importFileErrList.length; i<l; i++) {
					var importArr = params.importFileErrList[i].split(':'),
						row = importArr[1],
						col = importArr[0];
						
					tableTrs += '<tr>' +
								'	<td>' + row + '</td>' +
								'	<td>' + col + '</td>' +
								'</tr>';
				}
				var tableList = '<strong><center>上传失败，请重新上传</center></strong>' +
								'<p class="ask_show">易通为您定位前十个格式错误，其他错</p>' +
								'<p class="ask_show">误位置请按照模板格式认真核对</p>' +
								'<br>'+
								'<p class="ask_show">问题出现在表格的位置：</p>' +
								'<table class="err_table">' +
								'	<tr>' +
								'		<td class="td_row">行</td>' +
								'		<td class="td_list">列</td>' +
								'	</tr>' +
									tableTrs +
								'</table>';
				var errListDialog = new Dialog();
				errListDialog.init({
					contentHtml: tableList,
					yes: function() {
						errListDialog.close();
					}
				});
			} else if (params.importFileErrType != '') {	// 上传格式有错误
				var errTypeDialog = new Dialog();
				
				if(config.userType == 2){
					errTypeDialog.init({
						contentHtml: params.importFileErrType,
						yesVal:'导入金刚',
						yes: function() {
									$.ajax({
										url: 'orderImport_insertOrder.action',
										type: 'POST',
										data: {
											orderIds: params.orderIds
//											isLocal: isLocal,
//						 					pro: pro,
//											city: city,
//											area: area
										},
										cache: false,
										success: function(response) {
											errTypeDialog.close();
											if(response.status == true){
												var content = '';
												var successHtml1='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
												var successHtml2='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
												if(response.successCount=='0'){
													successHtml1=successHtml1+'</p>';
													successHtml2=successHtml2+'</p>';
													
												}else{
													successHtml1=successHtml1+'（可到运单监控中查看-财务子账号除外）</p>';
													successHtml2=successHtml2+'（可到金刚系统查看）</p>';
												}
												var failedHtml='<p>失败'+response.failedCounct+'条（原因：运单号已存在，无需重复导入。请将'+response.failedCounct+'条订单删除后再导入其它订单）</p>'
												if(config.userType==1){
													if(response.failedCounct=='0'){
														content=successHtml1;
													}else{
														content=successHtml1+'\n'+failedHtml;
													}
												}else{
													if(response.failedCounct=='0'){
														content=successHtml2;
													}else{
														content=successHtml2+'\n'+failedHtml;
													}
												}
												var aDialog = new Dialog();
												aDialog.init({
													contentHtml:content,
													yes:function(){
														aDialog.close();
														$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
														setTimeout(function(){
															$('#submitOk').trigger('submit');
														},5);
//																	$('#submitOk').trigger('submit');
													}
												});
											}
											else{
												var aDialog = new Dialog();
												aDialog.init({
													contentHtml: '导入失败!',
													yes:function(){
														aDialog.close();
														$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
														setTimeout(function(){
															$('#submitOk').trigger('submit');
														},5);
//																	$('#submitOk').trigger('submit');
													}
												});
											}
										}
									});
						},
						noVal:'稍后再说',
						no: function() {
							errTypeDialog.close();
						},
						closeBtn: true
					});
				}else{
					errTypeDialog.init({
						contentHtml: params.importFileErrType,
						yesVal:'导入易通',
						yes:function() {
							errTypeDialog.close();
							var tips = '';
							var tdA = $('.table tbody .td_a .td_check'),
							tdATemp = [];
							
							tdA.each(function() {
								if ($(this).prop('checked')) {
									tdATemp.push($(this).val())
								}
							})
							if (tdATemp.length == 0) {
								tips = '<strong>（未勾选订单，默认导入所有订单）</strong>';
							}
							
							var dDialog = new Dialog();
							
							var formlist = '<form class="form" id="form_01">'+				
							'	<strong>请确认导入订单的发货地，发货地关系到其它功能的正常使用<br></strong>'+
							tips +
							'	<p>	'+
							'		<input class="input_radio" name="gender" id="input_a" type="radio" checked value="0" />'+
							'		<span class="radio_txt">本地发货的订单</span>'+
							'		<input class="input_radio" name="gender" id="input_a" type="radio" value="1" />'+
							'		<span class="radio_txt">异地发货的订单</span>'+
							'	</p>'+
							'	<p id="province_p" style="display:none;">'+
							'		<select name="" id="province_dialog" disabled></select>'+
							'		<span id="area_tip"></span>' +
							'	</p>'+
							'</form>';
							
							dDialog.init({
								contentHtml: formlist,
								yes: function() {
									var isLocal,
									pro = linkageSel.getSelectedData('name', 0),
									city = linkageSel.getSelectedData('name', 1),
									area = linkageSel.getSelectedData('name', 2);
									$('#province_p select').live('change', function() {
										$('#area_tip').html('');
									});
									
									$('#input_a').attr('checked') == 'checked' ? isLocal = true : isLocal = false;
									function uploadProcess() {
										if ($.trim($('.table tbody').html()) == '') {
											
											dDialog.close();
											var noListDialog = new Dialog();
											noListDialog.init({
												contentHtml: '还没有上传订单哦',
												closeBtn: true
											})
										} else {
											dDialog.close();
											var loadDialog = new Dialog();
											
											loadDialog.init({
												contentHtml: '正在导入中...'
											});
											
											$.ajax({
												url: 'orderImport_insertOrder.action',
												type: 'POST',
												data: {
													orderIds: params.orderIds,
													isLocal: isLocal,
													pro: pro,
													city: city,
													area: area
												},
												cache: false,
												success: function(response) {
													loadDialog.close();													
													if(response.status == true){
														var content = '';
														var successHtml1='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
														var successHtml2='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
														if(response.successCount=='0'){
															successHtml1=successHtml1+'</p>';
															successHtml2=successHtml2+'</p>';
														}else{
															successHtml1=successHtml1+'（可到运单监控中查看-财务子账号除外）</p>';
															successHtml2=successHtml2+'（可到金刚系统查看）</p>';
														}
														var failedHtml='<p>失败'+response.failedCounct+'条（原因：运单号已存在，无需重复导入。请将'+response.failedCounct+'条订单删除后再导入其它订单）</p>'
														if(config.userType==1){
															if(response.failedCounct=='0'){
																content=successHtml1;
															}else{
																content=successHtml1+'\n'+failedHtml;
															}
														}else{
															if(response.failedCounct=='0'){
																content=successHtml2;
															}else{
																content=successHtml2+'\n'+failedHtml;
															}
														}
														var aDialog = new Dialog();
														aDialog.init({
															contentHtml:content,
															yes:function(){
																aDialog.close();
																$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
																setTimeout(function(){
																	$('#submitOk').trigger('submit');
																},5);
																//																	$('#submitOk').trigger('submit');
															}
														});
													}
													else{
														var aDialog = new Dialog();
														aDialog.init({
															contentHtml: '导入失败!',
															yes:function(){
																aDialog.close();
																$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
																setTimeout(function(){
																	$('#submitOk').trigger('submit');
																},5);
																//																	$('#submitOk').trigger('submit');
															}
														});
													}
												}
											})
										}
									}		
									if (isLocal) {
										uploadProcess();
									} else {
										check.area();
										if (!areaErr) {
											uploadProcess();
										}
										
									}
								},
								yesVal: '导入',
								no: function() {
									dDialog.close();
								},
								noVal: '返回'
							});
							
							
							var area = {
									data: districtData,
									selStyle: 'margin-left:3px;',
									select: ['#province_dialog'],
									autoLink: false
							};
							
							var linkageSel = new LinkageSel(area);
							var isLocal;
							
							$('#form_01 .input_radio').change(function() {
								if ($(this).val() == 1) {
									$('#province_p').show();
									$('#province_dialog').removeAttr('disabled');
								} else if ($(this).val() == 0) {
									$('#province_p').hide();
									$('#province_dialog').attr('disabled', 'true');
								}
							});			
						
						},
						noVal:'稍后再说',
						no:function() {
							errTypeDialog.close();
						},
						closeBtn: true
					});
				}
			}
		};
		
		// 开始导入
		var startImport = function() {
			$('.start_import_btn').click(function(ev) {
				ev.preventDefault();
				clearInterval(uploadInterval);
				if ($('.table tbody .td_a .td_check:checked').length == 0) {

					if(config.userType == 2){
						if(params.orderTempSize <= 0){
							var noListDialog = new Dialog();
							noListDialog.init({
								contentHtml: '还没有上传订单哦',
								closeBtn: true
							});
						} else {
							var loadDialog = new Dialog();
							loadDialog.init({
								contentHtml: '正在导入中...'
							});
							
							$.ajax({
								url: 'orderImport_insertOrder.action',
								type: 'POST',
								data: {
									orderIds: params.orderIds
//									isLocal: isLocal,
//				 					pro: pro,
//									city: city,
//									area: area
								},
								cache: false,
								success: function(response) {
									loadDialog.close();
									if(response.status == true){
										var content = '';
										var successHtml1='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
										var successHtml2='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
										if(response.successCount=='0'){
											successHtml1=successHtml1+'</p>';
											successHtml2=successHtml2+'</p>';
											
										}else{
											successHtml1=successHtml1+'（可到运单监控中查看-财务子账号除外）</p>';
											successHtml2=successHtml2+'（可到金刚系统查看）</p>';
										}
										var failedHtml='<p>失败'+response.failedCounct+'条（原因：运单号已存在，无需重复导入。请将'+response.failedCounct+'条订单删除后再导入其它订单）</p>'
										if(config.userType==1){
											if(response.failedCounct=='0'){
												content=successHtml1;
											}else{
												content=successHtml1+'\n'+failedHtml;
											}
										}else{
											if(response.failedCounct=='0'){
												content=successHtml2;
											}else{
												content=successHtml2+'\n'+failedHtml;
											}
										}
										var aDialog = new Dialog();
										aDialog.init({
											contentHtml:content,
											yes:function(){
												aDialog.close();
												$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
												setTimeout(function(){
													$('#submitOk').trigger('submit');
												},5);
//															$('#submitOk').trigger('submit');
											}
										});
									}
									else{
										var aDialog = new Dialog();
										aDialog.init({
											contentHtml: '导入失败!',
											yes:function(){
												aDialog.close();
												$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
												setTimeout(function(){
													$('#submitOk').trigger('submit');
												},5);
//															$('#submitOk').trigger('submit');
											}
										});
									}
								}
							});
						}
			
					} else {
						var tips = '';
						var tdA = $('.table tbody .td_a .td_check'),
						tdATemp = [];
						
						tdA.each(function() {
							if ($(this).prop('checked')) {
								tdATemp.push($(this).val())
							}
						})
						if (tdATemp.length == 0) {
							tips = '<strong>（未勾选订单，默认导入所有订单）</strong>';
						}
						
						var dDialog = new Dialog();
						
						var formlist = '<form class="form" id="form_01">'+				
						'	<strong>请确认导入订单的发货地，发货地关系到其它功能的正常使用<br></strong>'+
						tips +
						'	<p>	'+
						'		<input class="input_radio" name="gender" id="input_a" type="radio" checked value="0" />'+
						'		<span class="radio_txt">本地发货的订单</span>'+
						'		<input class="input_radio" name="gender" id="input_a" type="radio" value="1" />'+
						'		<span class="radio_txt">异地发货的订单</span>'+
						'	</p>'+
						'	<p id="province_p" style="display:none;">'+
						'		<select name="" id="province_dialog" disabled></select>'+
						'		<span id="area_tip"></span>' +
						'	</p>'+
						'</form>';
						
						dDialog.init({
							contentHtml: formlist,
							yes: function() {
								var isLocal,
								pro = linkageSel.getSelectedData('name', 0),
								city = linkageSel.getSelectedData('name', 1),
								area = linkageSel.getSelectedData('name', 2);
								$('#province_p select').live('change', function() {
									$('#area_tip').html('');
								});
								
								$('#input_a').attr('checked') == 'checked' ? isLocal = true : isLocal = false;
								function uploadProcess() {
									if (params.orderTempSize <= 0) {
										
										dDialog.close();
										var noListDialog = new Dialog();
										noListDialog.init({
											contentHtml: '还没有上传订单哦',
											closeBtn: true
										})
									} else {
										dDialog.close();
										var loadDialog = new Dialog();
										
										loadDialog.init({
											contentHtml: '正在导入中...'
										});
										
										$.ajax({
											url: 'orderImport_insertOrder.action',
											type: 'POST',
											data: {
												orderIds: params.orderIds,
												isLocal: isLocal,
												pro: pro,
												city: city,
												area: area
											},
											cache: false,
											success: function(response) {
												loadDialog.close();													
												if(response.status == true){
													var content = '';
													var successHtml1='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
													var successHtml2='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
													if(response.successCount=='0'){
														successHtml1=successHtml1+'</p>';
														successHtml2=successHtml2+'</p>';
													}else{
														successHtml1=successHtml1+'（可到运单监控中查看-财务子账号除外）</p>';
														successHtml2=successHtml2+'（可到金刚系统查看）</p>';
													}
													var failedHtml='<p>失败'+response.failedCounct+'条（原因：运单号已存在，无需重复导入。请将'+response.failedCounct+'条订单删除后再导入其它订单）</p>'
													if(config.userType==1){
														if(response.failedCounct=='0'){
															content=successHtml1;
														}else{
															content=successHtml1+'\n'+failedHtml;
														}
													}else{
														if(response.failedCounct=='0'){
															content=successHtml2;
														}else{
															content=successHtml2+'\n'+failedHtml;
														}
													}
													var aDialog = new Dialog();
													aDialog.init({
														contentHtml:content,
														yes:function(){
															aDialog.close();
															$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
															setTimeout(function(){
																$('#submitOk').trigger('submit');
															},5);
															//																	$('#submitOk').trigger('submit');
														}
													});
												}
												else{
													var aDialog = new Dialog();
													aDialog.init({
														contentHtml: '导入失败!',
														yes:function(){
															aDialog.close();
															$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
															setTimeout(function(){
																$('#submitOk').trigger('submit');
															},5);
															//																	$('#submitOk').trigger('submit');
														}
													});
												}
											}
										})
									}
								}		
								if (isLocal) {
									uploadProcess();
								} else {
									check.area();
									if (!areaErr) {
										uploadProcess();
									}
									
								}
							},
							yesVal: '导入',
							no: function() {
								dDialog.close();
							},
							noVal: '返回'
						});
						
						
						var area = {
								data: districtData,
								selStyle: 'margin-left:3px;',
								select: ['#province_dialog'],
								autoLink: false
						};
						
						var linkageSel = new LinkageSel(area);
						var isLocal;
						
						$('#form_01 .input_radio').change(function() {
							if ($(this).val() == 1) {
								$('#province_p').show();
								$('#province_dialog').removeAttr('disabled');
							} else if ($(this).val() == 0) {
								$('#province_p').hide();
								$('#province_dialog').attr('disabled', 'true');
							}
						});			
					}
					
				} else {
						var tips = '';
						var tdA = $('.table tbody .td_a .td_check'),
							tdATemp = [];
							
						tdA.each(function() {
							if ($(this).prop('checked')) {
								tdATemp.push($(this).val())
							}
						})
						if (tdATemp.length == 0) {
							tips = '<strong>（未勾选订单，默认导入所有订单）</strong>';
						}
						
						if(config.userType == 2){
							if(params.orderTempSize <= 0){
								var noListDialog = new Dialog();
								noListDialog.init({
									contentHtml: '还没有上传订单哦',
									closeBtn: true
								});
							} else {
								var loadDialog = new Dialog();

								loadDialog.init({
									contentHtml: '正在导入中...'
								});

								$.ajax({
									url: 'orderImport_insertOrder.action',
									type: 'POST',
									data: {
										orderIds: tdATemp.toString()
//										isLocal: isLocal,
//					 					pro: pro,
//										city: city,
//										area: area
									},
									cache: false,
									success: function(response) {
										loadDialog.close();													
										if(response.status == true){
											var content = '';
											var successHtml1='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
											var successHtml2='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
											if(response.successCount=='0'){
												successHtml1=successHtml1+'</p>';
												successHtml2=successHtml2+'</p>';
											}else{
												successHtml1=successHtml1+'（可到运单监控中查看-财务子账号除外）</p>';
												successHtml2=successHtml2+'（可到金刚系统查看）</p>';
											}
											var failedHtml='<p>失败'+response.failedCounct+'条（原因：运单号已存在，无需重复导入。请将'+response.failedCounct+'条订单删除后再导入其它订单）</p>'
											if(config.userType==1){
												if(response.failedCounct=='0'){
													content=successHtml1;
												}else{
													content=successHtml1+'\n'+failedHtml;
												}
											}else{
												if(response.failedCounct=='0'){
													content=successHtml2;
												}else{
													content=successHtml2+'\n'+failedHtml;
												}
											}
											var aDialog = new Dialog();
											aDialog.init({
												contentHtml:content,
												yes:function(){
													aDialog.close();
													$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
													setTimeout(function(){
														$('#submitOk').trigger('submit');
													},5);
//																	$('#submitOk').trigger('submit');
												}
											});
										}
										else{
											var aDialog = new Dialog();
											aDialog.init({
												contentHtml: '导入失败!',
												yes:function(){
													aDialog.close();
													$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
													setTimeout(function(){
														$('#submitOk').trigger('submit');
													},5);
//																	$('#submitOk').trigger('submit');
												}
											});
										}
									}
								})
							
							}
						} else {
							var dDialog = new Dialog();
							
							var formlist = '<form class="form" id="form_01">'+				
											'	<strong>请确认导入订单的发货地，发货地关系到其它功能的正常使用<br></strong>'+
											 tips +
											'	<p>	'+
											'		<input class="input_radio" name="gender" id="input_a" type="radio" checked value="0" />'+
											'		<span class="radio_txt">本地发货的订单</span>'+
											'		<input class="input_radio" name="gender" id="input_a" type="radio" value="1" />'+
											'		<span class="radio_txt">异地发货的订单</span>'+
											'	</p>'+
											'	<p id="province_p" style="display:none;">'+
											'		<select name="" id="province_dialog" disabled></select>'+
											'		<span id="area_tip"></span>' +
											'	</p>'+
											'</form>';
							dDialog.init({
								contentHtml: formlist,
								yes: function() {
									var isLocal,
										pro = linkageSel.getSelectedData('name', 0),
										city = linkageSel.getSelectedData('name', 1),
										area = linkageSel.getSelectedData('name', 2);
									$('#province_p select').live('change', function() {
										$('#area_tip').html('');
									});
									
									$('#input_a').attr('checked') == 'checked' ? isLocal = true : isLocal = false;
									function uploadProcess() {
										if ($.trim($('.table tbody').html()) == '') {

											dDialog.close();
											var noListDialog = new Dialog();
											noListDialog.init({
												contentHtml: '还没有上传订单哦',
												closeBtn: true
											})
										} else {
											dDialog.close();
											var loadDialog = new Dialog();
		
											loadDialog.init({
												contentHtml: '正在导入中...'
											});
		
											$.ajax({
												url: 'orderImport_insertOrder.action',
												type: 'POST',
												data: {
													orderIds: tdATemp.toString(),
													isLocal: isLocal,
								 					pro: pro,
													city: city,
													area: area
												},
												cache: false,
												success: function(response) {
													loadDialog.close();													
													if(response.status == true){
														var content = '';
														var successHtml1='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
														var successHtml2='<p style="margin-bottom:10px;">成功'+response.successCount+'条';
														if(response.successCount=='0'){
															successHtml1=successHtml1+'</p>';
															successHtml2=successHtml2+'</p>';
														}else{
															successHtml1=successHtml1+'（可到运单监控中查看-财务子账号除外）</p>';
															successHtml2=successHtml2+'（可到金刚系统查看）</p>';
														}
														var failedHtml='<p>失败'+response.failedCounct+'条（原因：运单号已存在，无需重复导入。请将'+response.failedCounct+'条订单删除后再导入其它订单）</p>'
														if(config.userType==1){
															if(response.failedCounct=='0'){
																content=successHtml1;
															}else{
																content=successHtml1+'\n'+failedHtml;
															}
														}else{
															if(response.failedCounct=='0'){
																content=successHtml2;
															}else{
																content=successHtml2+'\n'+failedHtml;
															}
														}
														var aDialog = new Dialog();
														aDialog.init({
															contentHtml:content,
															yes:function(){
																aDialog.close();
																$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
																setTimeout(function(){
																	$('#submitOk').trigger('submit');
																},5);
//																	$('#submitOk').trigger('submit');
															}
														});
													}
													else{
														var aDialog = new Dialog();
														aDialog.init({
															contentHtml: '导入失败!',
															yes:function(){
																aDialog.close();
																$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
																setTimeout(function(){
																	$('#submitOk').trigger('submit');
																},5);
//																	$('#submitOk').trigger('submit');
															}
														});
													}
												}
											})
										}
									}		
									if (isLocal) {
										uploadProcess();
									} else {
										check.area();
										if (!areaErr) {
											uploadProcess();
										}
				
									}
								},
								yesVal: '导入',
								no: function() {
									dDialog.close();
								},
								noVal: '返回'
							});
							
							var area = {
									data: districtData,
									selStyle: 'margin-left:3px;',
									select: ['#province_dialog'],
									autoLink: false
								};
										 
								var linkageSel = new LinkageSel(area);
								var isLocal;
									
								$('#form_01 .input_radio').change(function() {
									if ($(this).val() == 1) {
										$('#province_p').show();
										$('#province_dialog').removeAttr('disabled');
									} else if ($(this).val() == 0) {
										$('#province_p').hide();
										$('#province_dialog').attr('disabled', 'true');
									}
								});	
						}

					}
			})
		};
		// 删除
		var deltr = function() {
			// 批量删除
			$('.del_btn').click(function(ev) {
				ev.preventDefault();
				
				var param = [];
				var pageNumber = $('#per_select').val();
				var ordercheckboxs = document.getElementsByName("ordersselect");
				for(var i = 0;i < ordercheckboxs.length;i++){  
					if(ordercheckboxs[i].checked){  
						param.push(ordercheckboxs[i].value);
					}  
				} 
				
				if (param.length == 0) {
					var emptyDialog = new Dialog();
					
					emptyDialog.init({
						contentHtml: '请先勾选订单',
						yes: function() {
							emptyDialog.close();
						}
					});
				} else {					
					var delConfirm = new Dialog();
					delConfirm.init({
						contentHtml: '确定删除吗？',
						yes: function() {
							setTimeout(function() {
								window.location.href="orderImport_deleteOrderTempList.action?orderIds="+param.toString()+"&pageNm=" + pageNumber+"&menuFlag=fahuo_orderimport";
//								$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
//								$('#submitOk').trigger('submit');
							}, 5);
						},
						no: function() {
							delConfirm.close();
						}
					});
				}
			});
			
			// 单个删除
			$('.del_item').click(function(ev) {
				ev.preventDefault();
				var href = $(this).attr('href');

				var delItemDialog = new Dialog();
				delItemDialog.init({
					contentHtml: '确定删除吗？',			// 内容 HTML
					yes: function() {			// 确认按钮的回调	
						setTimeout(function(){
							window.location = href;
//							$('#submitOk').attr('action','orderImport_toOrderImoprt.action');
//							$('#submitOk').trigger('submit');
						}, 5);
					},
					no: function() {					// 取消按钮的回调
						delItemDialog.close();
					}
				});
			});
		};
		
		//翻页
		pagination.live('click',function(ev){
			ev.preventDefault();
			$("#curPage").val($(this).attr("value"));
			setTimeout(function(){
				$("#sear_form_import").submit();
			},0);
		});
		
		// 每页显示
		var perList = function() {
			$('#per_select').change(function() {
//				$("#numOfPage").val($('#per_select').val());
				setTimeout(function(){
					$('#per_form').trigger('submit');
				},5);
//				$('#per_form').trigger('submit');
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
		
		// 表单验证
		var form = function() {
			var els = {
				orderNum: $('#search_txt'),
				searBtn: $('#nub_search'),
				searForm: $('#sear_form_import')
			};
			
			var msg = {
				orderShipNum: '运单号/订单号',
				shipOrderFormatErr: '格式错误'
			}
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form_import',
				theme: 'yto',
				errorFocus: false
			});
			
			// 订单查询
			els.orderNum.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				functionValidator({
					fun: function(val) {
						if (val == msg.orderShipNum) {
							$('#search_txt').val('');
							//return false;
						}
					}//,
					//onError: msg.shipOrderFormatErr
				});
				
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();

				els.searForm.trigger('submit');
			});
		};
		
		var hideDiv = function() {
			if(params.orderTempSize <= 0){
				$('.print_box').css('display','none');
//				$('.print_box').hide();
				
				$('.check_all').attr('disabled', 'disabled');
			}
		}
		
				
		return {
			init: function() {
				textsearch();
				notice();
				checkAll();
				table();
				download();
				upload();
				startImport();
				deltr();
				perList();				
				formValidCompatible();
				form();
				hideDiv();
			}
		}
	})();
	
	surPrint.init();
});