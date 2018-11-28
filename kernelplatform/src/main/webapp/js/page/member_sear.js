$(function() {
	var memberSear = (function() {
		var winParams = window.params || {};

		// 全局配置
		var config = {
			downLoadTemplateUrl: winParams.downLoadTemplateUrl || '',		// 下载导入模板 Url
			howtoImportfUrl: winParams.howtoImportfUrl || '',				// 怎样导入？Url
			importDataAction: winParams.importDataAction || '',				// 导入数据 Action
			saveSearchAction: winParams.saveSearchAction || '',				// 保存搜索条件 Action
			delMemberAction: winParams.delMemberAction || ''				// 删除会员 Action
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
		//地区
		var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#at_province'],
				autoLink: false
			};
		
			var linkageSel = new LinkageSel(area);
		// 表单
		var form = function() {
			// 元素集合
			var els = {
				member: $('#member'),
				markForm2: $('#mark_form2'),
				tradeAmountA: $('#trade_amount_a'),
				tradeAmountB: $('#trade_amount_b'),
				tradeAmountTip: $('#trade_amountTip'),
				tradeCountA: $('#trade_count_a'),
				tradeCountB: $('#trade_count_b'),
				tradeCountTip: $('#trade_countTip'),
				searBtn: $('#sear_btn'),
				saveSear: $('#save_btn'),
				saveSearInput: $('#save_sear'),
				saveSearTip: $('#save_searTip'),
				searName: $('#sear_name'),
				tradeTime: $('#trade_time'),
				lastTrade: $('#last_trade'),
				atProvince: $('#at_province'),
				memLevel: $('#member_level'),
				errTip: $('#errTip'),
				smsBuyersSearchId:$('#smsBuyersSearchId'),
				orderByCol:$('#orderByCol'),
				member:$('#member'),
				shopName:$('#shopName'),
				searchByShopName:$('#searchByShopName'),
				currentPage: $('#currentPage'),
				searForm: $('#pageForm')
			};
			
			// 文案
			var msg = {
				memberDef: '联系人/手机号/会员名',
				memberFormatErr: '您输入的内容格式有误',
				tradeAmountFormatErr: '请输入正整数',
				tradeAmountRangeErr: '请输入正确的交易量范围',
				tradeCountFormatErr: '您输入的交易额格式有误',
				tradeCountRangeErr: '请输入正确的交易额范围',
				saveSearErrLen: '搜索器名称为 2-20 字符',
				saveSearFormatErr: '格式错误'
			};
			
			if (els.member.val() == '') {
				els.member.defaultTxt(msg.memberDef);
			}
			
			// 表单验证初始化
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'mark_form2',
				theme: 'yto',
				errorFocus: false
			});
			
			// 输入框
			els.member.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				functionValidator({
					fun: function(val) {
						if (val == msg.memberDef) {
							els.member.val('');
						}
					}
				}).
				regexValidator({regExp: ['empty', 'name', 'accountId', 'mobile'], dataType: 'enum', onError: msg.memberFormatErr});
			
			// 交易量
			els.tradeAmountA.
				formValidator({validatorGroup:'1', tipID: 'trade_amountTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1'], dataType: 'enum', onError: msg.tradeAmountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val != '' &&　els.tradeAmountB.val() != '') {
							return parseInt(val) <= parseInt(els.tradeAmountB.val());
						}
					},
					onError: msg.tradeAmountRangeErr
				});
				
			els.tradeAmountB.
				formValidator({validatorGroup:'1', tipID: 'trade_amountTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1'], dataType: 'enum', onError: msg.tradeAmountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val != '' && els.tradeAmountA.val() != '') {
							return parseInt(val) >= parseInt(els.tradeAmountA.val());
						}
					},
					onError: msg.tradeAmountRangeErr
				});
				
				
			// 交易额
			els.tradeCountA.
				formValidator({validatorGroup:'1', tipID: 'trade_countTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1', 'decmal4'], dataType: 'enum', onError: msg.tradeCountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val != '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
							return parseFloat(val) <= parseFloat(els.tradeCountB.val());
						}
					},
					onError: msg.tradeCountRangeErr
				});
				
				
			els.tradeCountB.
				formValidator({validatorGroup:'1', tipID: 'trade_countTip', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				regexValidator({regExp: ['empty', 'intege1', 'decmal4'], dataType: 'enum', onError: msg.tradeCountFormatErr}).
				functionValidator({
					fun: function(val, el) {
						if (val != '') {
							var formatPrice = parseFloat(val, 10).toFixed(2);
							el.value = formatPrice;
							return parseFloat(val) >= parseFloat(els.tradeCountA.val());
						}
					},
					onError: msg.tradeCountRangeErr
				});
				
				//保存的搜索器名称
				//els.saveSearInput.
				//	formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' ', leftTrim: true, rightTrim: true}).
				//	inputValidator({min: 2, max: 20, onError: msg.saveSearErrLen});
			
			//翻页
			pagination.live('click',function(){
				els.currentPage.val($(this).attr("value"));
				//为解决IE6问题而修改
				setTimeout(function(){els.searForm.trigger('submit');},0);
//				els.searForm.trigger('submit');
			});

			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				
				els.markForm2.trigger('submit');
			});
			
			//排序
			var orderByCol=$('#orderby');
			orderByCol.change(function(ev){
				$('#orderByCol').val(orderByCol.val());
				ev.preventDefault();
				els.markForm2.trigger('submit');
			});
			//通过店铺名称搜索
			var shopName=$('#searchByShopName');
			els.searchByShopName.click(function(){
				$('#shopName').val(shopName.children().html());
				els.markForm2.trigger('submit');
			});
			//当搜索器名称选择：‘请选择’时清空form表单
			els.searName.change(function(){
				//清空form
				if ($(this).val()=='') {
					$(':input','#mark_form2')  
					 .not(':button, :submit, :reset')  
					 .val('')  
					 .removeAttr('checked')  
					 .removeAttr('selected'); 
					$('#smsBuyersSearchId').val('');
					$('#save_sear').val('');
					$('#shopName').val('');
					$('#at_province').next().hide();
					$('#at_province').next().next().hide();
					$('#orderByCol').val('tradeAmount');
					$('.yto_onError').hide();
					return;
				}
				
				//请求数据
				$.get('buyers!findSmsBuyersSearch.action?searName='+$(this).val(),function(data){
					
					var str=data.split('|');
					//拼接规则tradeTime,province,city,area,memLevel,tradeAmountA,tradeAmountB,tradeCountA,tradeCountB,member
					$('#trade_time').val(str[0]);
					$("#province").val($.trim(str[2]));
					$("#city").val($.trim(str[3]));
					$("#area").val($.trim(str[4]));
					linkageSel.changeValues([str[2],str[3],str[4]]);
					$('#member_level').val(str[5]);
					$('#trade_amount_a').val(str[6]);
					$('#trade_amount_b').val(str[7]);
					$('#trade_count_a').val(str[8]);
					$('#trade_count_b').val(str[9]);
					$('#member').val(str[10]);
					$('#smsBuyersSearchId').val(str[11]);
					$('#save_sear').val(str[12]);
					$('.yto_onError').hide();
				});
			});
			
			
			
			els.saveSearInput.blur(function() {
				var searInputVal = els.saveSearInput.val(),
					searInputValLen = searInputVal.length;
				
				if (searInputValLen >= 2 && searInputValLen <= 20) {
					showIcon.correct($('#save_searTip'), ' ');
				} else {
					showIcon.error($('#save_searTip'), msg.saveSearErrLen);
				}
			});
			
			
			// 点击“保存”
			els.saveSear.click(function(ev) {
				
				ev.preventDefault();
				var searInputVal = els.saveSearInput.val(),
					searInputValLen = searInputVal.length;
				// 先校验搜索器
				if (searInputValLen >= 2 && searInputValLen <= 20) {
					
					// 验证表单
					if ($.formValidator.pageIsValid(1)) {
						//修改省市区的值
						$("#province").val(els.atProvince.val())
						$("#city").val(els.atProvince.next().val())
						$("#area").val(els.atProvince.next().next().val())	
						$.ajax({
							url: config.saveSearchAction,
							cache: false,
							type: 'POST',
							data: {
								searName: els.searName.val(),
								tradeTime: els.tradeTime.val(),
								lastTrade: els.lastTrade.val(),
								province: els.atProvince.val(),
								city: els.atProvince.next().val(),
								area: els.atProvince.next().next().val(),
								memLevel: els.memLevel.val(),
								member: els.member.val(),
								tradeAmountA: els.tradeAmountA.val(),
								tradeAmountB: els.tradeAmountB.val(),
								tradeCountA: els.tradeCountA.val(),
								tradeCountB: els.tradeCountB.val(),
								saveSearInput: els.saveSearInput.val(),
								smsBuyersSearchId:els.smsBuyersSearchId.val()
							},
							success: function(msg) {
								//消息提示
								var succDialog = new Dialog();
								succDialog.init({
									contentHtml: '保存成功！',
									iconType: 'success',
									autoClose: 1000
								})
								var str=msg.split(',');//id,name,add/edit
								//给隐藏域搜索器ID赋值
								$('#smsBuyersSearchId').val(str[0]);
								//新的option
								var option='<option value='+str[0]+' selected=selected>';
								option+=str[1];
								option+='</option>';
								//先判断是否是修改
								if (str[2]=='edit') {
									//修改:删除原来的option，替换新的option
									$("#sear_name option[value='"+str[0]+"']").remove();//根据value移除
								}
								$("#sear_name").append(option); //append新的option
							}
						})
					}
					
					showIcon.correct(els.saveSearTip, ' ');
				} else {
					showIcon.error(els.saveSearTip, msg.saveSearErrLen);
				}


			});
		};
		
		// 选择用户
		var checkedUser = function() {
			var curCheck = $('.cur_check'),
				checkMarkA = $('.check_mark_a'),
				checkMarkB = $('.check_mark_b'),
				tableCheckbox = $('.table tbody .input_checkbox');
				
			
			// 勾选当前页
			curCheck.change(function() {
				var propChecked = $(this).prop('checked');
				if (propChecked) {			// 如果勾选中
					checkMarkA.prop('checked', true);
				} else {					// 如果不勾选
					checkMarkA.prop('checked', false);
				}
			});
			
			
			// 勾选单个
			tableCheckbox.change(function() {
				var allChecked = true;
				//var propChecked = $(this).prop('checked');
				/*if (!propChecked) {
					curCheck.prop('checked', false);
				}*/
				
				$(this).closest('tbody').find('input[type="checkbox"]').each(function(){
					if(!$(this).prop("checked")){
						allChecked = false;
						return false;
					}
				});
				
				if(allChecked){
					$('.checkall').prop("checked",true);
				} else{
					$('.checkall').prop("checked",false);
				}
			});
		};
		
		
		// 导入数据
		var importData = function() {
			$('.import_data').click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					imfmHtml = [],
				    errClass = "yto_onError",
					errMsg = [];

				imfmHtml.push('<form id="import_fm" method="POST" enctype="multipart/form-data" action="' + config.importDataAction + '" method="POST" enctype="multipart/form-data"><div class="import">');
				imfmHtml.push('<p class="choose clearfix"><label>请选择要导入的文件</label><a href="' + config.howtoImportfUrl + '">怎样导入？</a><a href="' + config.downLoadTemplateUrl + '">下载导入模板</a></p>');
			    imfmHtml.push('<p class="file clearfix"><label>上传文件：</label><input type="file" name="import" /></p>');
				imfmHtml.push('<span class="errUpload"></span>');
				imfmHtml.push('<p class="shop clearfix"><label>所属店铺：</label><select name="shops"><option value="所属店铺1">所属店铺1</option><option value="所属店铺2">所属店铺2</option><option value="所属店铺3">所属店铺3</option></select></p>');
				imfmHtml.push('<div class="note clearfix"><dl><dt>当CSV文件的会员存在你的其它店铺时，请选择：</dt><dd><input type="radio" name="note" checked /><span>删除原来的会员信息，保存到现在选择的店铺</span></dd><dd><input type="radio" name="note" /><span>保留原来的会员信息，不放到现在选择的店铺</span></dd></dl></div>');
				imfmHtml.push('</div></form>');

				var importDialog = new Dialog();
							
				importDialog.init({
					contentHtml: imfmHtml.join(""),
					closeBtn: true,
					yes: function() {
						$('#import_fm .file').trigger("change");

						if($('#import_fm ' + "." + errClass).length === 0){
							$('.errUpload').hide();
							$('#import_fm').trigger("submit");
						}
					},
					no: function() {
						importDialog.close();
					}
				});

				$('.note span').click(function(){
					var $radio = $(this).prev();
					$radio.prop("checked", !$radio.prop("checked"));
				});

				$('#import_fm .file').bind("change", checkUpload);

				function checkUpload(ev){
					errMsg = [];
					var upload = $('#import_fm .file input')[0];
						file = upload.value;

					if(!file){
						errMsg.push('请选择上传文件');
						$('.errUpload').text(errMsg.join("")).addClass(errClass).css("display","inline-block");
						return;
					}
					if(!checkFileFormate(file)){
						errMsg.push('上传文件的格式应该.xls和.csv');
					}
					if(!checkFileSize(upload)){
						errMsg.push('上传文件大小不得超过2M');
					}
					if(errMsg.length > 0){
						$('.errUpload').text(errMsg.join(", ")).addClass(errClass).css("display","inline-block");
					} else {
						$('.errUpload').hide();
					}
				}

				function checkFileFormate(file){
					var reg = /(\.)(csv|xls)$/g;
					return reg.test(file);
				}

				function checkFileSize(upload){
					var size = 0;
					if(window.ActiveXObject){
						var fso = new ActiveXObject("Scripting.FileSystemObject"),
						    filepath = upload.value,
						    thefile = fso.getFile(filepath);

						size = parseInt(thefile.size) / 1024;
					} else {
						size = parseInt(upload.files[0].size) / 1024;
					}

					return size <= 2048;
				}
			});
		};
		
		// 删除会员
		var delContact = function() {
			// 删除单个
			$('.del_contact').click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					memberId = $('.member_id', _this.parent()).val();
				
				var confirmDel = new Dialog();
				
				confirmDel.init({
					contentHtml: '你确定要删除吗？',
					iconType: 'question',
					yes: function() {
						$.ajax({
							url: config.delMemberAction + '?memberId=' + memberId,
							cache: false,
							type: 'GET',
							dataType: 'json',
							success: function() {
								_this.parents('tr').hide(300, function() {
									_this.parents('tr').remove();		// 删除当前行
									confirmDel.close();					// 关闭弹层
								});
							}
						});
					},
					no: function() {
						confirmDel.close();
					}
				});
			});
			
			// 删除多个
			$('.del_member').click(function(ev) {
				ev.preventDefault();
				var memberArr = [];
				$('.table tbody input:checked').each(function() {
					memberArr.push($('.member_id', $(this).parents('tr')).val())
				})
				
				var errerDel=new Dialog();
				if (memberArr.length==0) {
					errerDel.init({
						contentHtml: '请选择要删除的项！',
						iconType: 'errer',
						autoClose: 2000
					});
					return;
				}
				
				var multDel = new Dialog();
				
				multDel.init({
					contentHtml: '你确定要删除吗？',
					iconType: 'question',
					yes: function() {
						$.ajax({
							url: config.delMemberAction + '?memberId=' + memberArr.join(),
							cache: false,
							type: 'GET',
							dataType: 'json',
							success: function() {
								for (var i=0,l=memberArr.length; i<l; i++) {
									$('.table tbody input:checked').eq(i).parents('tr').hide(300, function() {
										$('.table tbody input:checked').eq(i).parents('tr').remove();		// 删除当前行
									});
								}
								
								multDel.close();
							}
						});
					},
					no: function() {
						multDel.close();
					}
				})
			});
			
		};
		// 地区多级联动
		var selectArea = function() {
				linkageSel.changeValues([$("#province").val(),$("#city").val(),$("#area").val()]);
		};
		return {
			init: function() {
				form();
				checkedUser();
				importData();
				delContact();
				selectArea();
			}
		}
	})()
	
	memberSear.init();
})