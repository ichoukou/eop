$(function() {
	var my_clients = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		
		// 配置
		var config = {
			custCodeAction: winParams.custCodeAction,        // 新增客户Action
			site:winParams.site || '',
			getCodeUrl: winParams.getCodeUrl,                // 生成客户编码 url
			delCodeUrl: winParams.delCodeUrl,                // 删除客户编码 url
			unbindClientUrl: winParams.unbindClientUrl       // 未绑定页面 url
		};
		
		// 提示文案
		var tipsMsg = {
			custEmpty: "客户名称不得为空",
			custFormat: "客户名称格式有误"
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
		
		// 新增客户
		$('#add_cust_btn').click(function(ev){
			ev.preventDefault();
			
			var contentHtml = '<div id="add_cust">' +
				              '   <form action=' + config.custCodeAction + ' id="add_cust_form">' +
				              '      <div class="info">' +
			                  '         <label for="cust_name">客户名称：</label>' +
			                  '         <input type="text" id="cust_name" class="input_text" />' +
			                  '         <a title="生成客户编码" class="btn btn_a" id="build_btn" href="javascript:;"><span>生成客户编码</span></a>' +
			                  '         <span id="cust_nameTip"></span>' +
			                  '      </div>' +
			                  '      <div class="result">' +
			                  //'         <div class="res_code" id="clip_container"><em id="clip_btn">YTO1111111</em>' + '记下编码，把它给您的客户绑定</div>' +
			                  '      </div>' +
			                  '      <input type="hidden" id="cust_code" value="" />' +
			                  '      <input type="hidden" id="cust_id" value="" />' +
			                  '   </form>' +
			                  '</div>';
			
			var aDialog = new Dialog();
			aDialog.init({
				contentHtml: contentHtml,
				closeBtn: false,
				yes: function() {
					var _code = els.custCode.val();
					if(_code !== ''){
						aDialog.close();
						window.location.href = config.unbindClientUrl;
					}
				},
				no: function() {
					var _code = els.custCode.val();
					if(_code !== ''){
						$.ajax({
							type: 'POST',
							url: config.delCodeUrl,
							cache: false,
							data: ({
								"userThreadId" : els.custId.val()
							}),
							success: function(response) {
								if(response.status){
									var bDialog = new Dialog();
									bDialog.init({
										contentHtml: '刚刚生成的客户编码已被删除',
										closeBtn: true
									});
								}
							}
						});
					}
					aDialog.close();
				},
				yesVal: '确认',
				noVal: '取消'
			});
			
			// 元素集合
			var els = {
				custName: $('#cust_name'),                                  // 客户名称
				custNameTip: $('#cust_nameTip'),                            // 客户名称提示文案
				buildBtn: $('#build_btn'),                                  // 生成客户编码
				resultDiv: $('#add_cust_form .result'),                     // 客户编码显示
				custCode: $('#cust_code'),                                  // 客户编码 hidden
				custId: $('#cust_id'),										// 直客id hidden
				saveBtn: $('a:eq(0)', $('#add_cust').parent().next())       // 确认 button
			};
			
			els.saveBtn.removeClass('btn_d').addClass('btn_e disable');
			
			// 客户名称验证
			els.custName.blur(function(){
				
				var _val = $(this).val(),
				    reg = (regexEnum && regexEnum.accountId) || "^.{1,100}$";
				reg = new RegExp(reg, 'g');
				showIcon.correct(els.custNameTip, '');
				
				if(_val === ''){
					showIcon.error(els.custNameTip, tipsMsg.custEmpty);
				}
				else if(!reg.test(_val)){
					showIcon.error(els.custNameTip, tipsMsg.custFormat);
				}
			});
			
			els.custName.change(function(){
				els.resultDiv.html('');
				els.custCode.val('');
				els.saveBtn.removeClass('btn_d').addClass('btn_e disable');
			});
			
			els.buildBtn.click(function(ev){
				ev.preventDefault();
				
				els.custName.trigger("blur");
				
				var flag = (els.custNameTip.text().length === 0);
				
				if(flag){
					els.custName.attr("disabled","disabled");
					
					var _name = els.custName.val();
					
					$.ajax({
						type: 'POST',
						url: config.getCodeUrl,
						cache: false,
						data: ({
							"user.site":config.site,
							"user.userCode":"autoGen",
							"user.userName" : _name
						}),
						success: function(response) {
							if(response.status){
								
								els.buildBtn.unbind("click");
								els.buildBtn.removeClass('btn_a').addClass('btn_e disable');
								
								var _code = response.infoContent;
								var _custId = response.targetUrl;
								els.resultDiv.html('<div class="res_code" id="clip_container"><em id="clip_btn">' + _code + '</em>' + '记下编码，把它给您的客户绑定</div>');
								els.custCode.val(_code);
								els.custId.val(_custId);
								els.saveBtn.removeClass('btn_e disable').addClass('btn_d');
								
								var $clip_btn = $('.res_code #clip_btn', els.resultDiv);
						            //left = $clip_btn[0].offsetLeft,
							        //top = $clip_btn[0].offsetTop;
					  	
								var clip = new ZeroClipboard.Client();
								//clip.setHandCursor(true);
								clip.setText(_code);
					  	
								clip.addEventListener('complete', function (client, text) {
									//debugstr("Copied text to clipboard: " + text );
									alert("客户编码已经复制，你可以使用Ctrl+V 粘贴。");
								});
					  	
								clip.glue('clip_btn', 'clip_container');
					  	
								var clip_swf = $clip_btn.next('div');
									clip_swf.css({top:'0', left:'0', cursor:'pointer'});
									clip_swf.attr("title", "单击可复制");
							}
							else {
								showIcon.error(els.custNameTip, response.infoContent);
								els.resultDiv.html('');
								els.custCode.val('');
								els.custId.val('');
								els.saveBtn.removeClass('btn_d').addClass('btn_e disable');
							}
						},
						complete: function(){
							if(!els.buildBtn.hasClass('disable')){
								els.custName.removeAttr("disabled");
							}
						}
					});
				}
			});
		});
		
		var selectArea = function() {
			var province = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#area']
			};
					 
			var linkageSelect = new LinkageSel(province);
		};
		
		// 全选
		var checked = function() {
			$('#market_table .checked_all').checkAll( $('#market_table input[type="checkbox"]') );
		};
		
		//翻页
		pagination.live('click',function(ev){
			ev.preventDefault();
			$("#currentPage").val($(this).attr("value"));
			$("#userFrom").submit();
		});
		
		//查询
		$('#search_btn').click(function(ev){
			ev.preventDefault();
			$("#currentPage").val(1);
			var userCode = $("#userCode").val();
			var userName = $("#userName").val();
			$("#userCode").val($.trim(userCode));	// 去头尾空格
			$("#userName").val($.trim(userName));	// 去头尾空格
			$("#userFrom").attr("action","user!list.action");
			$("#userFrom").trigger("submit");
		});
		
		/** 发送消息 */
		$("#send_message").click(function(ev){
			ev.preventDefault();
			var receiveUserCodeString="",receiveUserNameString="";
			$(".msgCheckForMessage:checked").each(function(){
				receiveUserCodeString += ($(this).val()+",");
				receiveUserNameString += ($(this).attr("val")+",");
			});
			if(receiveUserCodeString!=null && receiveUserCodeString!="" && receiveUserNameString!=null && receiveUserNameString!=""){
				receiveUserCodeString = receiveUserCodeString.substring(0,receiveUserCodeString.length-1);
				receiveUserNameString = receiveUserNameString.substring(0,receiveUserNameString.length-1);
				window.location.href = "send_openUI.action?menuFlag=msg_send&receiveUserCodeString="+receiveUserCodeString+"&receiveUserNameString="+receiveUserNameString;
			}else{
				window.location.href = "send_openUI.action?menuFlag=msg_send";
			}
		});
		
		//点击电子对账
		$('#market_table .td_h #eccount').live('click',function(ev){
			ev.preventDefault();
			var _this= $(this),
				userName = $('#userNameHid',_this.parent()).val();
			
			var eccount=0;//1:关闭；0：开启
			var userThreadId = _this.val();
			var content = "";
			if(_this.is(":checked")==false){
				eccount=1;
				content = "确定关闭"+userName+"的电子对账功能吗？";
			}else{
				content = "确定开启"+userName+"的电子对账功能吗？";
			}
			var art = new Dialog();
			art.init({
				contentHtml: content,
				yesVal:'确定',
				noVal: '取消',
				lock: false,
				yes: function () {
					$.ajax({
						url:'user!switchEccount.action?eccount='+eccount+'&userThreadId='+userThreadId,
						success:function(data){
							if(_this.is(":checked")==false){
								_this.attr('checked', true);
							}else{
								_this.attr('checked', false);
							}
							art.close();
						}
					});
				},
				no: function () {
					art.close();
				}
				
			});
		});
		
		//管理运费模板
		$(".viewPTByVip").live('click',function() {
			var vipId = $(this).attr("val");
			var url = "posttemp!viewPTByVip.action?menuFlag=posttemp&vipIds="+vipId+"&url="+encodeURIComponent($("#url").val());
			window.location.href=url;
		});
		
		$(".delPTByVip").live('click',function(){
			var _this = $(this),
		        userThreadId = _this.attr("val");
			
			var delDialog = new Dialog();
			delDialog.init({
				contentHtml: "是否确定要删除此行记录？",
				yesVal:'确定',
				noVal: '取消',
				yes: function () {
				    $.ajax({
						type: 'POST',
						url: config.delCodeUrl,
						cache: false,
						data: ({
							"userThreadId" : userThreadId
						}),
						success: function(response) {
							delDialog.close();
							if(response.status){
								var bDialog = new Dialog();
								bDialog.init({
									contentHtml: '客户编码删除操作已成功！',
									yes:function(){
										window.location.reload(true);
									}
								});
							}
							else{
								var bDialog = new Dialog();
								bDialog.init({
									contentHtml: response.infoContent,
									closeBtn: true
								});
							}
						}
					});
				},
				no: function () {
					delDialog.close();
				}
				
			});
		});
		
		$('#iThink').click(function(){
			var _this = $(this);
			_this.parent().main.location = "sendMessage_openAdviseUI.action";
			_this.parent().side.layerLight("advise_li");
		});
		
		
		
		
		// 修改客户名称
		var modifyName = function() {
			
			// 点击“修改”客户名称
			$('.edit_name').live('click', function(ev) {
				ev.preventDefault();
				
				var _this = $(this);
				
				$('p', _this.closest('td')).replaceWith('<input type="text" class="input_text" id="new_name_input" />  <span id="new_name_inputTip"></span>');
				_this.replaceWith('<a href="javascript:;" class="save_name">保存</a>&nbsp;&nbsp;<a href="javascript:;" class="cancel_name">取消</a>');
				
			});
			
			// 点击“保存”客户名称
			$('.save_name').live('click', function(ev) {
				ev.preventDefault();
				
				var _this = $(this),
					newNameVal = _this.closest('td').find('#new_name_input').val(),
					userCode = _this.closest('td').find('.usercode').val();
				if ($.trim(newNameVal) == '') {
					showIcon.error(_this.closest('td').find('#new_name_inputTip'), '客户名称不能为空');
				} else {
					$.ajax({
						url: 'user!eidtUserName.action',
						type: 'POST',
						data:{
							userName:newNameVal,
							userCode:userCode
						},
						cache: false,
						success: function() {
							$('input[name=userName]',_this.closest('td')).val(newNameVal);
							_this.closest('td').find('#new_name_input').replaceWith("<p>"+newNameVal+"</p>");
							_this.parent().replaceWith('<span><a href="javascript:;" class="edit_name">修改</a></sapn>');
							_this.closest('td').find('#new_name_inputTip').html('');
						}
					});
					_this.closest('td').find('#new_name_inputTip').remove();
				}
			});
			
			//点击"取消"
			$('.cancel_name').live('click',function(){
				var _this = $(this),
				userName = $('input[name="userName"]',_this.closest('td')).val();
				
				$('#new_name_input', _this.closest('td')).replaceWith("<p>"+userName+"</p>");
				$('#new_name_inputTip', _this.closest('td')).replaceWith("");
				_this.parent().replaceWith('<span><a href="javascript:;" class="edit_name">修改</a></span>');
			});
		};
		
		
		
		
		
		
		
		return {
			init: function() {
				//ytoTab.init();
				selectArea();
				checked();
				modifyName();
			}
		}
	})();
	
	my_clients.init();
})

$(function() {
	$('#per_mesg').defaultTxt('联系人/联系电话');
	$('#textarea_demo').defaultTxt('默认短信');
})


