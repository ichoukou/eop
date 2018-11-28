/**
 * 查看消息
**/
$(function() {
	var ytoMessage = (function() {
		
		var els = {
			sendButton:$("#send_message"),
			deleteButton:$("#delete_message"),
			eachTr:$("#msg_table > table > tbody > tr"),
			replyTheme:$("#replyTheme"),
			messageContent:$("#messageContent"),
			replyNumForTheme:$("#replyNumForTheme"),
			curReplyList:$("#curReplyList"),
			messageId:$("#messageId"),
			replyButton:$("#replyButton"),
			replyContent:$("#replyContent"),
			replyForm:$("#replyForm"),
			currentPage:$("#currentPage"),
			classify:$("#classify"),
			msgType:$("#msg_type"),
			msgMark:$("#msg_mark")
		};
		
		/** 发送消息 */
		els.sendButton.click(function(ev) {
			ev.preventDefault();
			window.location.href="send_openUI.action?menuFlag=msg_index";
		});
		
		/**
		 * 回复消息
		 */
		els.replyButton.click(function(ev){
			ev.preventDefault();
			var classify = $("#classify").val();
			if((params.userType==1 || params.userType==2) && classify==2){
				var alertDialog = new Dialog();
				alertDialog.init({
					contentHtml: '系统消息请勿回复!',
					yes: function() {
						alertDialog.close();
					},
					closeBtn: true
				});
				return;
			}
			var replyContent = els.replyContent.val();
			var messageId = els.messageId.val();
			var theme = els.replyTheme.text();
			var reply_num =  els.replyNumForTheme.text();
			if(messageId!=""){
				if(replyContent!="" && replyContent!="请填写回复内容"){
					if(replyContent.length>500){
						var alertDialog = new Dialog();
						alertDialog.init({
							contentHtml: '回复内容太长，限500字!',
							yes: function() {
								alertDialog.close();
							},
							closeBtn: true
						});
						return;
					}else{
						$.ajax({
							url : 'send_reply.action',
							cache:false,
							type : 'post',
							data : {
								replyContent : replyContent,
								messageId : messageId
							},
							success : function(data){
								var replyNum = $("tr[value='"+messageId+"']").find(".cmts_num").text();
								$("tr[value='"+messageId+"']").find(".cmts_num").text(parseInt(replyNum)+1);
								showReplyList(messageId,theme,parseInt(replyNum)+1);
								els.replyContent.val("");
							}
						});
					}
				} else{
					var alertDialog = new Dialog();
					alertDialog.init({
						contentHtml: '请输入回复内容!',
						yes: function() {
							alertDialog.close();
						},
						closeBtn: true
					});
					els.replyContent.focus();
				}
			}else{
				var alertDialog = new Dialog();
				alertDialog.init({
					contentHtml: '暂无消息，无法回复!',
					yes: function() {
						alertDialog.close();
					},
					closeBtn: true
				});
			}
		});
		
		/**
		 * 默认显示当前列表中第一条消息的回复
		 */
		var defaultReplyShow = function(){
			var firstTr = els.eachTr.first();
			var messageId = firstTr.attr("value");
			//获取此行的主题
			var theme = firstTr.find("#curMessageTheme p").html();
			var reply_num = firstTr.find(".cmts_num").html();
			if(messageId!=null && messageId!="")
				showReplyList(messageId,theme,reply_num);
		};
		
		/**
		 * 点击每条消息显示该条消息的回复记录
		 */
		els.eachTr.each(function(){
			$(this).click(function(){
				var messageId = $(this).attr("value");
				//获取此行的主题
				var theme = $(this).find("#curMessageTheme p").html();
				var reply_num = $(this).find(".cmts_num").html();
				if(messageId!=null && messageId!="")
					showReplyList(messageId,theme,reply_num);
				var _thisDiv = $(this).find("#curMessageTheme");
				var curClass = _thisDiv.attr("class");
				if(curClass=="unread"){//如果消息未读，标记为已读
					$.ajax({
						url : 'message_marks.action?menuFlag=home_msg_index&messageStatus=1&messageIds='+messageId,
						cache:false,
						success : function(data){
							if(data.status==false){
								var alertDialog = new Dialog();
								alertDialog.init({
									contentHtml: data.infoContent,
									yes: function() {
										alertDialog.close();
									},
									closeBtn: true
								});
								return;
							}else{
								_thisDiv.attr("class","cmts_preview");
								$('#acc_nav_msg').trigger('click',[1]);
								var unReadNum = parseInt($('#unReadMessageNum').text(), 10);
								if ( unReadNum != 1) {
									$('#unReadMessageNum').html(unReadNum - 1);
								} else {
									$('#unReadMessageNum').hide();
								}
							}
						}
					});
				}
			});
		});
		
		/**
		 * 显示回复列表：参数为消息id,当前消息主题，当前消息的回复数目
		 */
		var showReplyList = function(messageId, theme, reply_num){
			els.messageId.val(messageId);
			$.ajax({
				url:'message_replyList.action?menuFlag=home_msg_index&messageId='+messageId,
				cache:false,
				type:'post',
				success:function(data){
					els.replyNumForTheme.html(reply_num);
					els.replyTheme.html(theme);
					//组装回复html
					var html = "";
					if(data!=null && data.length>0){
						for(var i=data.length-1; i>=0; i--){
							var replyUserMobile = data[i].replyUserMobile;
							var replyUserPhone = data[i].replyUserPhone;
							var replyTime = data[i].replyTime;
							if(replyUserMobile==null || replyUserMobile=="")
								replyUserMobile = "";
							if(replyUserPhone==null || replyUserPhone=="")
								replyUserPhone = "";
							if(replyTime!=null)
								replyTime = replyTime.replace("T"," ");
							var replyType = data[i].replyType;
							var curClass = "cmts_title";
							if(replyType==2)
								curClass = "cmts_title vip";
							html += "<li>" + 
									"<div class='"+curClass+"'><strong>"+ data[i].replyUserName +"</strong><br><span class='cmts_tel'>电话："+replyUserMobile+"；"+replyUserPhone+"</span><span class='cmts_date'>   时间："+replyTime+"</span></div>" + 
									"<div class='cmts_preview'><p>"+data[i].replyContent+"</p></div>" + 
									"</li>";
						}
					}else{
						html = "";
					}
					els.curReplyList.html(html);
					$('.cmts_list').scrollTop(999);
				}
			});
		};
		
		/**
		 * 消息类型筛选
		 */
		els.msgType.change(function(ev){
			els.classify.val($(this).val());
			var classify = els.classify.val();
			var currentPage = els.currentPage.val();
			window.location.href = 'message_list.action?menuFlag=home_msg_index&currentPage='+currentPage+"&classify="+classify;
		});
		
		/**
		 * 标记消息
		 */
		els.msgMark.change(function(ev){
			var checks = $("tbody input:checked");
			var messageStatus = $(this).val();
			var messageIds = "";
			if(messageStatus!=""){
				if(checks.length>0){
					for(var i=0; i<checks.length; i++){
						messageIds += $(checks[i]).val();
						if(i!=checks.length-1)
							messageIds += ",";
						var curStatus = $(checks[i]).parent().parent().find("#curMessageTheme").attr("class");
						var curTheme = $(checks[i]).parent().parent().find("#curMessageTheme").text();
						if(messageStatus==1){
							if(curStatus=='cmts_preview'){//如果当前是已读状态，则不可标记为已读
								var alertDialog = new Dialog();
								alertDialog.init({
									contentHtml: '消息"'+$.trim(curTheme)+'"已读，不可标记为已读',
									yes: function() {
										alertDialog.close();
									},
									closeBtn: true
								});
								return;
							}
						} else{
							if(curStatus=='unread'){//如果当前未读状态，则不可标记为未读
								var alertDialog = new Dialog();
								alertDialog.init({
									contentHtml: '消息"'+$.trim(curTheme)+'"未读，不可标记为未读',
									yes: function() {
										alertDialog.close();
									},
									closeBtn: true
								});
								return;
							}
						}
					}
					$.ajax({
						url:'message_marks.action?menuFlag=home_msg_index&messageStatus='+messageStatus+'&messageIds='+messageIds,
						cache:false,
						success:function(data){
							if(data.status==false){
								var alertDialog = new Dialog();
								alertDialog.init({
									contentHtml: data.infoContent,
									yes: function() {
										alertDialog.close();
									},
									closeBtn: true
								});
								return;
							}else{
								var classify = els.classify.val();
								var currentPage = els.currentPage.val();
								window.location.href = 'message_list.action?menuFlag=home_msg_index&currentPage='+currentPage+"&classify="+classify;
							}
						}
					});
				}else{
					var alertDialog = new Dialog();
					alertDialog.init({
						contentHtml: '请选择要标记的消息!',
						yes: function() {
							alertDialog.close();
						},
						closeBtn: true
					});
					return;
				}
			}
		});
		
		
		
		/** 删除消息 */
		els.deleteButton.click(function(ev) {
			var checks = $("tbody input:checked");
			var messageIds = "";
			if(checks.length>0){
				var alertDialog = new Dialog();
				alertDialog.init({
					contentHtml: '确定要删除吗？',
					yes: function() {
						alertDialog.close();
						for(var i=0; i<checks.length; i++){
							messageIds += $(checks[i]).val();
							if(i!=checks.length-1)
								messageIds += ",";
						}
						$.ajax({
							url : 'message_delete.action?messageIds='+messageIds,
							cache:false,
							success : function(data){
								if(data.status==false){
									var alertDialog = new Dialog();
									alertDialog.init({
										contentHtml: data.infoContent,
										yes: function() {
											alertDialog.close();
										},
										closeBtn: true
									});
									return;
								}else{
									var classify = els.classify.val();
									var currentPage = els.currentPage.val();
									window.location.href = 'message_list.action?menuFlag=home_msg_index&currentPage='+currentPage+"&classify="+classify;
								}
							}
						});
					},
					no:function(){
						alertDialog.close();
					},
					closeBtn: true
				});
			}else{
				var alertDialog = new Dialog();
				alertDialog.init({
					contentHtml: '请选择要删除的消息!',
					yes: function() {
						alertDialog.close();
					},
					closeBtn: true
				});
				return;
			}
		});
		
		/**
		 * 翻页
		 */
		pagination.click(function(){
			els.currentPage.val($(this).attr("value"));
			var classify = els.classify.val();
			var currentPage = els.currentPage.val();
			setTimeout(function(){
				window.location.href = 'message_list.action?menuFlag=home_msg_index&currentPage='+currentPage+"&classify="+classify;
			},0);
		});
		
		// 两列等高
		var autoHeight = function() {
			var tableEl = $('.content_l .table table'),
				cmtsEl = $('.content_r .cmts_detail'),
				cmtsListEl = $('.content_r .cmts_list'),
				cmtsReEl = $('.cmts_reply'),
				cmtsReTextEl = $('.cmts_reply textarea'),
				tableH = tableEl.height(),
				cmtsH = cmtsEl.height(),
				cmtsListH = cmtsListEl.height(),
				maxH = 0;
			
			if (cmtsReEl.length == 0) {
				cmtsListEl.height(510)
			} else {
				cmtsListEl.height(350)
			}
			
			if (tableH > cmtsH) {
				maxH = tableH;
			} else {
				maxH = cmtsH;
			}
			
			tableEl.height(maxH+1);
			cmtsEl.height(maxH);
			cmtsReTextEl.height(maxH - cmtsListH - 77);
			
			// for firefox
			if ($('#msg_table tbody tr').length < 10) {
				tableEl.css({
					height: 'auto'
				})
			}
		};
		
		// textarea 获得/失去焦点
		var focusTextarea = function() {
			var textareaEl = $('.cmts_reply textarea'),
				textareaVal = textareaEl.html();
				
			textareaEl.focus(function() {
				var _this = $(this);
				if (_this.html() == textareaVal) {
					_this.html('');
				}
			});
			
			textareaEl.blur(function() {
				var _this = $(this);
				if (_this.html() == '') {
					_this.html(textareaVal);
				}
			});
		};
		
		// 全选
		var checkAll = function() {
			$('#msg_table th .input_checkbox').checkAll( $('#msg_table tbody input[type="checkbox"]') );
		};
		
		return {
			init: function() {
				autoHeight();
				focusTextarea();
				checkAll();
				defaultReplyShow();
			}
		}
	})();
	
	ytoMessage.init();
});