$(function() {
	var attention = (function() {
		var textDefault = function() {
			$('#input_text_text').defaultTxt('请输入买家姓名/买家电话/运单号');
		};
	
		var changeDownUp = function() {
			$('.th_title').has('.arrow_down').css('cursor', 'pointer').toggle(
				function() {
					$('em', $(this)).removeClass('arrow_down').addClass('arrow_up');
					sortTable();
				},
				function() {
					$('em', $(this)).removeClass('arrow_up').addClass('arrow_down');
					sortTable();
				}
			);

			//执行排序
			function sortTable(){
				
				var arr=[];
				var row=$('#tbody tr');
				$.each(row,function(i){ 
					arr[i]=row[i] ;
				});
				arr.reverse();
				var fragment=document.createDocumentFragment();
				$.each(arr,function(i){
					fragment.appendChild(arr[i]);
				})
				$('#tbody').html("");
				$('#tbody').append(fragment);
				
			}

		};
		
		// 全选
		var check = function() {
			$('.checked_all').checkAll( $('.table .input_checkbox') );
		};
		
		// 表单
		var form = function() {
			// 元素集合
			var els = {
				searForm: $('#sear_form'),
				cancelBtn : $("#cancelBtn"),
				searInput: $('#input_text_text'),
				searBtn: $('#sear_btn')
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
			
			// 提示文案
			var tipsMsg = {
				searFormatErr: '格式错误，请修改',
				searLongErr: '内容超长',
				searShortErr: '内容太短'
			};
			
			
			// “查询”表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 智能查件
			els.searInput.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
//				inputValidator({min: 4, max: 30, onErrorMin: tipsMsg.searShortErr, onErrorMax: tipsMsg.searLongErr}).
				functionValidator({
					fun: function(val, el) {
						var val = el.value = $.trim(val),
							defaultText = '请输入买家姓名/买家电话/运单号' || '',		// 输入框默认值
							output;		// 返回结果
							
						if (val == defaultText) {					// 如果是默认值
							setTimeout(function(){
								showIcon.error($("#input_text_textTip"), "请输入查询条件")
							},5);
							output = false;
						} else if (dataType.isGroup(val)) {					// 如果是运单组合
							setTimeout(function(){
								showIcon.error($("#input_text_textTip"), "请输入单个运单号")
							},5);
							output = false;
						} else {
							if (
								dataType.isMobile(val) ||
								dataType.isShipNum(val) ||
								dataType.isTel(val) ||
								dataType.isName(val)
							) {
								output = true;
							} else {
								setTimeout(function(){
									showIcon.error($("#input_text_textTip"), "请输入正确的查询条件")
								},5);
								output = false;
							}
						}
						
						
						return output;
					},
					onError: tipsMsg.searFormatErr
				});
				
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				//els.searForm.trigger('submit');
				if($.formValidator.pageIsValid("1")){
					var keywords = $.trim($("#input_text_text").val());
					var result = 0,topScroll = 0;
					$("#tbody tr").each(function(){
						if($.trim($(this).children().eq(1).text()) == keywords 
								||$.trim($(this).children().eq(4).text()) == keywords
								||$.trim($(this).children().eq(5).text()) == keywords){
							if(result == 0 && topScroll == 0){
								topScroll = $(this).position().top;
							}
							result++;
							$(this).css({background: "#7c8bd0" });
						}
						else{
							$(this).css({background: "none" });
						}
					});
					
					window.scrollTo(0,topScroll);
					if(result == 0){
						var oDialog = new Dialog();
						oDialog.init({
							contentHtml: "抱歉，没有找到你想要的结果!",
							yes: function() {
								oDialog.close();
							},
							closeBtn: true
						});
					}
				}
			});
			
			//“取消关注”按钮
			els.cancelBtn.click(function(ev){
				ev.preventDefault();
				if($("tbody input").length==0){
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "您还没有关注任何运单!",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				}
				else if($("tbody input:checked").length==0){
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: "请选择要操作的运单!",
						yes: function() {
							oDialog.close();
						},
						closeBtn: true
					});
					return;
				}
				else{
					var ids="";
			    	$("#tbody input:checked").each(function(){
			    		if(ids != ''){
			    			ids += ",";
			    		}
			    		if($(this).val()!=""){
			    			ids+=$(this).val();
			    		}
			    	});
			    	$.ajax({
			    		cache:false,
			    		type:"POST",
			    		url:"attention_delAll.action",
			    		data:{ids:ids},
			    		success:function(res){
//			    			var oDialog = new Dialog();
//							oDialog.init({
//								contentHtml: "取消关注操作已成功!",
//								yes: function() {
//									oDialog.close();
//									window.location.reload(true);
//								}
//							});
							window.location.reload(true);
			    		}
			    	});
				}
			});
			
		};
		
		
		return {
			init: function() {
				textDefault();
				changeDownUp();
				//shipNumDetail();
				check();
				form();
			}
		}
	})();
	
	attention.init();
})