$(function() {
	var dialog = new Dialog();
	var noteSet = (function() {
		var els = {
			setForm:$('#set_fm'),
			templateID: $('.templateID'),
			defaultID: $('#defaultID'),
			resend : $('#resend')
		}
		
		// 选择模板
		var template = function(){
			$('.template input[type="radio"]').add('.template ul label').click(function(ev){
				ev.stopPropagation();
				
				var _this = $(this);
				$('.iphone p').html((_this.is(':input')) ? _this.parent().text() : _this.text());
				$('.template li').removeClass('on');
				$(this).closest('li').addClass('on');
				els.defaultID.val($('.templateID',_this.parent()).val());
				
			});
			
			//新增模板
			$('#addTemplate').live('click',function(){
				var serviceId = $('#serviceId').val();
				setTimeout(function(){window.location.href = "template_toAdd.action?menuFlag=sms_home&serviceId="+serviceId+"&tips=1";},0);
			});
		};
		
		
        
		// 弹窗
		var pop = function() {
			$('.sendarea p').add('.sendarea a').click(function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
				    html = [],
					cities = [],
					str_cities = $('.sendarea p').text();

				html.push('<div class="cityarea"><table><tbody><tr><td>');
				html.push('<label class="all"><input type="checkbox" class="chk_all" />华东地区</label><label><input type="checkbox" name="city" value="山东" />山东</label><label><input type="checkbox" value="江苏" />江苏</label><label><input type="checkbox" value="安徽" />安徽</label><label><input type="checkbox" value="浙江" />浙江</label><label><input type="checkbox" value="福建" />福建</label><label><input type="checkbox" value="上海" />上海</label></td><td>');
				html.push('<label class="all"><input type="checkbox" class="chk_all" />西北地区</label><label><input type="checkbox" value="宁夏" />宁夏</label><label><input type="checkbox" value="新疆" />新疆</label><label><input type="checkbox" value="青海" />青海</label><label><input type="checkbox" value="陕西" />陕西</label><label><input type="checkbox" value="甘肃" />甘肃</label></td></tr><tr><td>');
				html.push('<label class="all"><input type="checkbox" class="chk_all" />华中地区</label><label><input type="checkbox" value="湖北" />湖北</label><label><input type="checkbox" value="湖南" />湖南</label><label><input type="checkbox" value="河南" />河南</label><label><input type="checkbox" value="江西" />江西</label></td><td>');
				html.push('<label class="all"><input type="checkbox" class="chk_all" />西南地区</label><label><input type="checkbox" value="四川" />四川</label><label><input type="checkbox" value="云南" />云南</label><label><input type="checkbox" value="贵州" />贵州</label><label><input type="checkbox" value="西藏" />西藏</label><label><input type="checkbox" value="重庆" />重庆</label></td></tr><tr><td>');
				html.push('<label class="all"><input type="checkbox" class="chk_all" />华北地区</label><label><input type="checkbox" value="北京" />北京</label><label><input type="checkbox" value="天津" />天津</label><label><input type="checkbox" value="河北" />河北</label><label><input type="checkbox" value="山西" />山西</label><label><input type="checkbox" value="内蒙古" />内蒙古</label></td><td>');
				html.push('<label class="all"><input type="checkbox" class="chk_all" />华南地区</label><label><input type="checkbox" value="广东" />广东</label><label><input type="checkbox" value="广西" />广西</label><label><input type="checkbox" value="海南" />海南</label></td></tr><tr class="last"><td>');
				html.push('<label class="all"><input type="checkbox" class="chk_all" />东北地区</label><label><input type="checkbox" value="辽宁" />辽宁</label><label><input type="checkbox" value="吉林" />吉林</label><label><input type="checkbox" value="黑龙江" />黑龙江</label></td><td>');
				//html.push('<label class="all"><input type="checkbox" class="chk_all" />港澳台地区</label><label><input type="checkbox" value="香港" />香港</label><label><input type="checkbox" value="澳门" />澳门</label><label><input type="checkbox" value="台湾" />台湾</label>');
				html.push('</td></tr></tbody></table><span style="display:none;position:absolute;bottom:15px;right:85px;" class="yto_onError">请选择发送区域</span></div>');
				html.push('<div id="dialog_ft">');
				html.push('<a title="保存" class="btn btn_d save" href="javascript:;"><span>保存</span></a>');
				html.push('<a title="全选" class="btn btn_e select_all" href="javascript:;"><span>全选</span></a>');
				html.push('<a title="清空" class="btn btn_e clear_all" href="javascript:;"><span>清空</span></a>');
				html.push('</div>');

				var setDialog = new Dialog();
							
				setDialog.init({
					contentHtml: html.join(""),
                    closeBtn: true
				});
				
				$('.cityarea').parent().find('.save').click(function(){
					cities = [];
					var checkedInputs = $(".cityarea input:checked");
					if( checkedInputs.length == 0 ){
				        error = $('.cityarea .yto_onError');
						error.css({"position":"absolute","bottom":"15px","right":"85px"}).show();
						return;
					}
					checkedInputs.each(function(i,city){
						var $city = $(city);
						if(!$city.parent().hasClass('all')){
							cities.push($city.val());
						}
					});
					
					var str_cities = cities.join("、");
					$('.sendarea p').text(str_cities);
					$('.sendarea input[type="hidden"]').val(str_cities);
					setDialog.close();
				});
				
				$('.cityarea').parent().find('.select_all').click(function(){
					cities = [];
					$(".cityarea input:not(.chk_all)").each(function(){
						var $city = $(this);
						cities.push($city.val());
						$city.prop("checked",true);
					});
					$(".chk_all").prop("checked",true);
					
					var str_cities = cities.join("、");
					$('.sendarea p').text(str_cities);
					$('.sendarea input[type="hidden"]').val(str_cities);
				});
				
				$('.cityarea').parent().find('.clear_all').click(function(){
					$(".cityarea input").each(function(){
						$(this).prop("checked", false);
					});
					$('.sendarea input[type="hidden"]').val("");
				});

                $('.cityarea .all').click(function(){
					var _this = $(this);
					var checked = (_this.is(':input')) ? _this.prop("checked") : _this.find('input').prop("checked");
                    _this.closest('td').find('input').prop("checked", checked);
				});

				$('.cityarea td').each(function(i, area){

					var _area = $(area);
					$("label", area).each(function(i, city){
						if(!$(city).hasClass("all")){
							refreshCity($(city));
						}
					});
					refreshAll(_area);
				}).click(function(){
					refreshAll($(this));
				});

				$('.cityarea td').each(function(){
					refreshAll($(this));
				});

				function refreshCity(city){
					var checkbox = city.find('input');
					checkbox.prop("checked", str_cities.indexOf(checkbox.val()) != -1);
				}

				function refreshAll(area){
					var all = area.find('.all');
					all.find('input').prop("checked", $('input', area).length - all.siblings().find('input:checked').length === 1);
				}

			});
		};

		// 提交表单
		var submit = function() {
			//点击“返回”按钮
			$('.returnBack').live('click',function(){
				setTimeout(function(){
					window.location.href = "smsHomeEvent_homePage.action?menuFlag=sms_home";
				},0);
			});
			
			$('#submit').click(function(ev){
				ev.preventDefault();
				//选择模板check
				var radioFlg = false;
				
				$('.template input[type="radio"]').each(function(){
					if($(this).is(':checked')){
						radioFlg = true;
					}
				});
				
				var tempListHid = $('#templateListHid').val();
				if(tempListHid == null || tempListHid.length == 0 || tempListHid == '[]'){
					dialog.init({
						contentHtml:'请添加新模板！',
						yes:function(){
							dialog.close();
						}
					});
				}else if(!radioFlg){
					dialog.init({
						contentHtml:'至少选择一个模板！',
						yes:function(){
							dialog.close();
						}
					});
				}
				
				//地区check
				else if($.trim($('.sendarea p').text()) == ''){
					dialog.init({
						contentHtml:'请选择发送区域',
						yes:function(){
							dialog.close();
						}
					});
				} else {
					//地区选择
					var area = $('.sendarea p').text();
					$('input[name="area"]').val(area);
					
					//是否自动发送
					var isChecked = els.resend.is(':checked');
					if(isChecked){
						$('input[name="resend"]').val('1');
					}else{
						$('input[name="resend"]').val('0');
					}
					
					els.setForm.attr('action','smsHomeEvent_saveSetting.action');
					els.setForm.trigger("submit");
				}
			});
		}
		
		return {
			init: function() {
				template();
				pop();
				submit();
			}
		}
	})()
	
	noteSet.init();
})