$(function() {
	var winParams = window.params || {};
	
	var config = {
		dialogSubmit: winParams.dialogSubmit || '',
		tabIndex: winParams.tabIndex || 0
	};
	var timeout = (function() {	
		var textDefault = function() {
			$('#input_text_text').defaultTxt('买家姓名/电话/运单号');
			$('#input_text_text2').defaultTxt('买家姓名/电话/运单号');
			
			if($("#returnQueryCondition").val()!="")
				$('#input_text_text').val($("#returnQueryCondition").val());
		};
	
		var changeDownUp = function() {
			$('.th_title').has('.arrow_down').css('cursor', 'pointer').toggle(
				function() {
					$('em', $(this)).removeClass('arrow_down').addClass('arrow_up');
				},
				function() {
					$('em', $(this)).removeClass('arrow_up').addClass('arrow_down');
				}
			);
		};
		
		// 全选
		var check = function() {
			$('.table thead .input_checkbox').checkAll( $('.table tbody .input_checkbox') );
		};
		
		// 表单
		var form = function() {
			// 元素集合
			var els = {
				searForm: $('#sear_form'),
				searInput: $('#input_text_text'),
				searBtn: $('#sear_btn'),
				searForm2: $('#sear_form2'),
				searInput2: $('#input_text_text2'),
				searBtn2: $('#sear_btn2')
			};
			
			// 提示文案
			var tipsMsg = {
				searFormatErr: '格式错误，请修改',
				searLongErr: '内容超长',
				searShortErr: '内容太短'
			};
			
			
			// "查询"表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 智能查件
			els.searInput.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min: 4, max: 30, onErrorMin: tipsMsg.searShortErr, onErrorMax: tipsMsg.searLongErr});
			
			
			// 点击"筛选"
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				var validatorResult = $.formValidator.pageIsValid(1);
				if(validatorResult){
					var condition = $("#input_text_text").val();
					var flag = $("#flag").val();
					
					window.location.href="passManage_list_site.action?menuFlag=chajian_passManage_list&tips=0&flag="+flag+"&queryCondition="+condition;
				}
			});
		};
			
		// 发送消息
		var sendMsg = function() {
			$('.send_msg1, .send_msg2').click(function(ev) {
				var sendDialog = new Dialog();
				ev.preventDefault();
				var _this = $(this),
					msg = $('.msgbg textarea', _this.parent().parent()).val(),	// 消息内容
					msgLen = msg.length,	// 消息内容字数
					tip = '',				// 消息提示文案
					perMsgLen = 200,			// 每条消息长度
					noCallback = null;		// 弹窗取消回调
					tabNum = $('.tabNum', _this.parents('form')).val();	// flag值
					curPage = $('#currentPage').val();
				
					/*var realLength = 0, len = msg.length, charCode = -1;
					
				    for (var i = 0; i < len; i++) {
				        charCode = msg.charCodeAt(i);
				        if (charCode >= 0 && charCode <= 128) realLength += 1;
				        else realLength += 2;
				    }*/	
					var realLength = msg.length;	
				if (realLength <= perMsgLen && realLength >= 4) {
					
					$.ajax({
						url: 'passManage_sendToSite.action',
						type: 'POST',
						data: {
							operMsg: msg,
							issueId: $('.issueId',_this.parents('form')).val(),
							flag: '2'
						},
						success: function() {
							var succDialog = new Dialog();
							succDialog.init({
								contentHtml: '您的消息已成功发送给卖家！',
								yes: function() {			// 确认按钮的回调
									succDialog.close();
									
									window.location.href="passManage_list_site.action?menuFlag=chajian_passManage_list&flag="+tabNum+"&currentPage=1";
								}
							})
						},
						error: function(){
							var succDialog = new Dialog();
							succDialog.init({
								contentHtml: '抱歉！系统繁忙，请稍后再试！',
								yes: function() {			// 确认按钮的回调
									succDialog.close();
								}
							})
						}
					})
				} else {
					
					if (msgLen == 0) {
						tip = '尚未输入内容';
					}else if((realLength > 0 && realLength < 4) || (realLength > perMsgLen)){
						tip = '对不起！消息内容长度必须为4-200个字符';
					} 
					
					sendDialog.init({
						yes: function() {			// 确认按钮的回调
							sendDialog.close();
						},
						no: noCallback,
						maskOpacity: 0,					// 遮罩层的透明度
						contentHtml: tip
					});
				}

			})
		};
			
		var shipNum = function() {
			$('.table table tbody tr .table_tr').mouseover(function() {
				$('.war_c1', $(this).parent()).show();
			});
			$('.table table tbody tr .table_tr').mouseout(function() {
				$('.war_c1', $(this).parent()).hide();
			});
		};
		
		/**
		 * 展开DIV
		 */
		/*var openDiv = function(){
			$('.open').live('click',function(){
				var _this = $(this),
					openDiv = $('.openDiv',_this.parents('.td_d3')),
					closeDiv = $('.closeDiv',_this.parents('.td_d3'));
				openDiv.attr('style','display:none');
				closeDiv.attr('style','display:inline');
			});
		};*/
		
		/**
		 * 收起DIV
		 */
		/*var closeDiv = function(){
			$('.close').live('click',function(){
				var _this = $(this),
					openDiv = $('.openDiv',_this.parents('.td_d3')),
					closeDiv = $('.closeDiv',_this.parents('.td_d3'));
				openDiv.attr('style','display:inline');
				closeDiv.attr('style','display:none');
			});
		};*/
		
		// 展开收拢
		var fold = function() {
			
			// 点击展开
			$('.fold_desc').live('click', function(ev) {
				ev.preventDefault();

				var _this = $(this),
				open = _this.parent().find('.openDiv');
				
				open.html(open.data("orgin"));
				//open.css({'height':'auto','overflow':'visible'});
				_this.replaceWith('<a href="javascript:;" class="unfold_desc">收拢</a>');
			});
			
			// 点击收拢
			$('.unfold_desc').live('click', function(ev) {
				ev.preventDefault();

				var _this = $(this), 
				open = _this.parent().find('.openDiv');
				
				open.html(open.data("cut"));
				//open.css({'height':'180px','overflow':'hidden'});
				
				_this.replaceWith('<a href="javascript:;" class="fold_desc">展开</a>');
			});
			
			$('.openDiv').each(function(){
				
				var _this = $(this),
				    reg = /<\/?[^>]+>/gi,
				    o_html = _this.html(),
				    o_val = o_html.replace(reg,'');
				    
				if(o_val.length > 70){
					
					o_val = o_html.split(reg)[0];
					o_val = (o_val.length > 70 ? o_val.substr(0,70) : o_val) + '...';
					//_this.css({'height':'180px','overflow':'hidden'});
					
					$('<a class="fold_desc" href="javascript:;">展开</a>').insertAfter(_this);
					_this.data("cut", o_val);
					_this.data("orgin", o_html);
					_this.html(_this.data("cut"));
				}
				
				_this.show();
			});
		};
		
		return {
			init: function() {
				ytoTab.init(config.tabIndex);
				textDefault();
				changeDownUp();
				check();
				form();
				sendMsg();
				shipNum();
				//shipNumDetail();
				//openDiv();
				//closeDiv();
				fold();
				//[已回复列表]
				$("#yiHui").click(function(){
					$('#tips2').html('正在查询，请稍后...');
					window.location.href="passManage_list_site.action?menuFlag=chajian_passManage_list&flag=1"; 
				});
				
				//未回复
				$("#weiHui").click(function(){
					$('#tips1').html('正在查询，请稍后...');
					window.location.href="passManage_list_site.action?menuFlag=chajian_passManage_list&flag=0";
				});
				
				//翻页
				pagination.click(function(){
					
					$('#currentPage').val($(this).attr("value"));
					var tabNum = $('#tabNum').val();
					if(tabNum == '0')$('#myform').attr('action','passManage_list_site.action?menuFlag=chajian_passManage_list&flag=0&tips=0');
					if(tabNum == '1')$('#myform').attr('action','passManage_list_site.action?menuFlag=chajian_passManage_list&flag=1&tips=0');
					setTimeout(function(){
						$("#myform").submit();
					},0);
				});
				
			}
		}
		
	})();

	timeout.init();
})

