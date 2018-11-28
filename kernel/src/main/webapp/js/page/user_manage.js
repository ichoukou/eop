$(function() {
	var userManager = (function() {

		var winParams = window.params || {};
		
		// 配置
		var config = {
			importFileErrType:winParams.importFileErrType,
			importFileErrList:winParams.importFileErrList,
			shopNames:winParams.shopNames,
			shopKey :winParams.shopKey,
			downLoadTemplateUrl: winParams.downLoadTemplateUrl || '',	// 下载导入模板 Url
			howtoImportfUrl: winParams.howtoImportfUrl || '',			// 怎样导入？Url
			importAction: winParams.importAction || '',					// 导入会员信息 Action
			setAction: winParams.setAction || ''						// 设置会员等级 Action
		};
		
		// 导入会员信息
		var importUser = function() {
			$('.opts .import').click(function(ev) {
				ev.preventDefault();
				
				var sn = new Array();
				var sk = new Array();
				sn = config.shopNames.split(",");
				sk = config.shopKey.split(",");
				var html = "";
				for(var i=0; i<sk.length; i++){
					html += "<option value="+ sk[i]+">"+ sn[i] +"</option>";
				}
				
				var _this = $(this),
					imfmHtml = [],
				    errClass = "yto_onError",
					errMsg = [];

				imfmHtml.push('<form id="import_fm" method="POST" enctype="multipart/form-data" action="' + config.importAction + '"><div class="import">');
				imfmHtml.push('<p class="choose clearfix"><label>请选择要导入的文件</label><a href="' + config.howtoImportfUrl + '">怎样导入？</a><a href="' + config.downLoadTemplateUrl + '">下载导入模板</a></p>');
			    imfmHtml.push('<p class="file clearfix"><label>上传文件：</label><input type="file" id="upload" name="upload" /></p>');
				imfmHtml.push('<span class="errUpload"></span>');
				imfmHtml.push('<p class="shop clearfix"><label>所属店铺：</label><select id="shopName" name="id">'+html+'</select></p>');
				imfmHtml.push('<div class="note clearfix"><dl><dt>当CSV文件的会员存在你的其它店铺时，请选择：</dt><dd><input type="radio" id="a" name="note" checked value="true"/><span>删除原来的会员信息，保存到现在选择的店铺</span></dd><dd><input type="radio" id="b" name="note" value="false" /><span>保留原来的会员信息，不放到现在选择的店铺</span></dd></dl></div>');
				imfmHtml.push('</div></form>');

				var importDialog = new Dialog();
							
				importDialog.init({
					contentHtml: imfmHtml.join(""),
					closeBtn: true,
					yes: function() {
						
//						var isDel,
//							shops;
//						$('#a').attr('checked') == 'checked' ? isDel = true : isDel = false;
//						shops = $('#shops').val();
//						
//						console.log('fasdfdsdfsafds');
//						$.ajax({
//							url: config.importAction,
//							url: 'buyers_upload.action',
//							type: 'POST',
//							data: {
//								isDel:isDel,
//								shopName:shops
//							},
//							cache: false,
//							success: function(response){
//								var dialog = new Dialog();
//								$.ajax({
//									url:'buyers_toIndex.action',
//									type:'POST',
//									cache: false,
//									success:function(response){
//										if(response){
//											importDialog.close();
//											dialog.init({
//												contentHtml:'<p>会员导入成功!</p>',
//												yes:function(){
//													dialog.close();
//												}
//											});
//										}else{
//											dialog.init({
//												contentHtml:'<p>会员导入失败!请稍后重试！</p>',
//												yes:function(){
//													dialog.close();
//												}
//											});
//										}
//									}
//								});
//							}
//						});
						
						$('#import_fm .file').trigger("change");
						
						if($('#import_fm ' + "." + errClass).length == 0){
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
			
			if(config.importFileErrList.length <= 10 && config.importFileErrList != 0){
				var tableTrs = '';
				for(var i = 0,l = config.importFileErrList.length; i<1;i++){
					var importArr = config.importFileErrList[i].split(':'),
					row = importArr[1],
					col = importArr[0];
					
					tableTrs += '<tr>' +
								'	<td>' + row + '</td>' +
								'	<td>' + col + '</td>' +
								'</tr>';
				}
				var tableList = '<strong>上传失败，请重新上传</strong>' +
								'<p class="ask_show">问题出现在表格的位置：</p>' +
								'<table class="err_table">' +
								'	<tr>' +
								'		<td class="td_row">行</td>' +
								'		<td class="td_list">列</td>' +
								'	</tr>' +
									tableTrs +
								'</table>';
				var dialog = new Dialog();
				dialog.init({
					contentHtml:tableList,
					yes:function(){
						dialog.close();
					}
				});
			}else if(config.importFileErrType != ''){ //上传提示(成功/失败)
				var dialog2 = new Dialog();
				dialog2.init({
					contentHtml:config.importFileErrType,
					yes:function(){
						dialog2.close();
						$.ajax({
							url:'buyers_toIndex.action'
						});
					}
				});
			}
		};
		
//		// 设置会员等级
//		var setUser = function() {
//			$('.opts .set').click(function(ev) {
//				ev.preventDefault();
//				
//				$.ajax({
//					url:'buyersgetBuyerByUserId.action',
//					method:'post',
//					dataType:'json',
//					success:function(data){
//							var _this = $(this),
//							stfmHtml = [],
//							rules = {
//								empty: "输入框不得为空"
//							},
//							errClass = "yto_onError",
//							allowChrs = [8,37,39,46,48,49,50,51,52,53,54,55,56,57,190];
//	
//						stfmHtml.push('<form id="set_fm" method="POST" action="' + config.setAction + '"><div class="set">');
//						stfmHtml.push('<p class="common"><label>普通会员:</label><span>所有购买记录的买家</span></p>');
//						stfmHtml.push('<p class="senior"><label>高级会员:</label><span>交易额大于</span><input class="input_text" name="grade.highAccount" value=\"'+data.highAccount+'\" /><span>元，交易量大于</span><input class="input_text" name="grade.highCount" value="'+data.highCount+'" /><span>次</span></p>');
//						stfmHtml.push('<span class="errSenior"></span>');
//						stfmHtml.push('<p class="vip"><label>VIP会员:</label><span>交易额大于</span><input class="input_text" name="grade.vipAccount" value=\"'+data.vipAccount+'\" /><span>元，交易量大于</span><input class="input_text" name="grade.vipCount" value="'+data.vipCount+'" /><span>次</span></p>');
//						stfmHtml.push('<span class="errVIP"></span>');
//						stfmHtml.push('<p class="super"><label>至尊VIP会员:</label><span>交易额大于</span><input class="input_text" name="grade.vipHighAccount" value="'+data.vipHighAccount+'" /><span>元，交易量大于</span><input class="input_text" name="grade.vipHighCount" value="'+data.vipHighCount+'" /><span>次</span></p>');
//						stfmHtml.push('<span class="errSuper"></span>');
//						stfmHtml.push('<input name="grade.id" type="hidden" value="'+data.id+'">');
//						stfmHtml.push('</div></form>');
//							
//						var setDialog = new Dialog();
//						setDialog.init({
//							contentHtml: stfmHtml.join(""),
//							closeBtn: true,
//							yes: function() {
//								if($('#set_fm ' + "." + errClass).length === 0){
//									$('#set_fm .errSenior, #set_fm .errVIP, #set_fm .errSuper').hide();
//									formateValue();
//								    $('#set_fm').trigger("submit");
//								}
//	
//							},
//							no: function() {
//								setDialog.close();
//							}
//						});
//	
//						$('#set_fm p').each(function(){
//							$(this).find("input:eq(0)").bind("keydown", onlyFloat).bind("change", emptyCheck);
//							$(this).find("input:eq(1)").bind("keydown", onlyInteger).bind("change", emptyCheck);
//						});
//						
//						function onlyInteger(ev){
//							var _this = $(this);
//							if( (ev.shiftKey) || ($.inArray(ev.which, allowChrs) === -1) ) {
//								ev.preventDefault();
//							}
//						}
//	
//						function onlyFloat(ev){
//							var reg = /\./g;
//							if( (ev.shiftKey) || ($.inArray(ev.which, allowChrs) === -1) ){
//								ev.preventDefault();
//							}
//							if(reg.test($(this).val()) && ev.which === 190){
//								ev.preventDefault();
//							}
//						}
//	
//						function emptyCheck(ev){ 
//							var hasEmpty = false;
//							var _this = $(this);
//							var $textboxes = _this.parent().find("input");
//							$textboxes.each(function(){
//								if($(this).val() === ""){
//									hasEmpty = true;
//								}
//							});
//							if(hasEmpty){
//								_this.parent().next().text(rules.empty).addClass(errClass).css("display","inline-block");
//							} else {
//								_this.parent().next().removeClass(errClass).hide();
//							}
//						}
//	
//						function formateValue(){
//							$('#set_fm .money').each(function(){
//								var _this = $(this);
//								_this.val(parseFloat(_this.val(), 10));
//							});
//							$('#set_fm .time').each(function(){
//								var _this = $(this);
//								_this.val(parseInt(_this.val(), 10));
//	
//							});
//						}
//					},error:function(){
//						var errorDialog = new Dialog();
//						errorDialog.init({
//							contentHtml: '抱歉！系统繁忙，请稍后再试！',
//							closeBtn: true,
//							yes:function(){
//								errorDialog.close();
//							},
//							yesVal:'确定'
//						});
//	
//					}
//				});
//			});
//		};
		
		return {
			init: function() {
				importUser();
				//setUser();
			}
		}
	})();
	
	userManager.init();
})