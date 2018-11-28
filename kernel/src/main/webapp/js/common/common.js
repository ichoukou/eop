/**
 * 易通公共方法
**/

var ytoCommon = (function() {
	// 缓存页面上的全局变量 params
	var winParams = window.params || {},
		winPath = window.path || {};

	// 配置
	var config = {
		onStep: winParams.onStep || null,				// 用户激活状态
		isShowButton : winParams.isShowButton || false,
		exitUrl: winPath.ctx +'/login_loginOut.action',				// 退出的请求地址
		userActJs: winPath.ctx +'/js/page/active_dialog.js?d=2012082801'			// 激活 js 请求地址
	};
	
	// 输入框默认提示 
	var defaultTxt = function() {
		$.fn.defaultTxt = function(txt) {
			var _this = $(this),
				defaultStatus = 'default_status',
				keypressStatus = 'keypress_status';
				
			// 设置默认文字
			_this.val(txt).addClass(defaultStatus);
			
			_this.bind('focus', function() {
				if (_this.val() == txt) {
					_this.val('');
				}
				_this.removeClass(defaultStatus);
			});
			_this.bind('blur', function() {
				if (_this.val() == '') {
					_this.val(txt);
					_this.removeClass(keypressStatus).addClass(defaultStatus)
				}
			});
		};
	};
	
	// outline:0 none;
	var oulineNone = function() {
		$('a, input[type="submit"], input[type="button"], button').live('focus', function () {
			var _this = $(this);
			if (_this.blur) {
				_this.blur();
			}
		});
	};

	// 消息
	var notice = function() {
		$('#acc_nav_msg').click(function(ev,params) {
			ev.preventDefault();
			if(params==null || params=='')
				params=2;
			showNotice(params);
		});
		
		$('#msg_box_hd .close').click(function(ev) {
			ev.preventDefault();
			$('#msg_box').slideUp();
		});
		
		$('.nav_notice_icon').click(function(ev) {
			ev.preventDefault();
			
			showNotice(1);
		});
	};
	
	//弹出消息框:消息为0时是否显示：params==1不显示；params==2显示
	var showNotice = function(param){
		$.ajax({
			url : 'noint!getUnReadNum.action',
			cache:false,
			success : function(data){
				var unReadMsgNum = data.split(",")[0],
					unReadQueNum = data.split(",")[1];
				
				var unReadMsgEl = $("#unReadMessageNum"),
					unReadQueEl = $("#unReadQuestionNum");
					
				if (unReadMsgNum == 0) {
					unReadMsgEl.hide();
				} else {
					unReadMsgEl.show().text(unReadMsgNum);
				}
				
				if (unReadQueNum == 0) {
					unReadQueEl.hide();
				} else {
					unReadQueEl.show().text(unReadQueNum);
				}
				
				
//				if(data.split(",")[0]==0){
//					if(param==2)
//						$("#unReadMessageNum_li").attr("style","display:block");
//					if(param==1)
//						$("#unReadMessageNum_li").attr("style","display:none");
//				}else{
//					$("#unReadMessageNum_li").attr("style","display:block");
//				}
//				if(data.split(",")[1]==0){
//					if(param==2)
//						$("#unReadQuestionNum_li").attr("style","display:block");
//					if(param==1)
//						$("#unReadQuestionNum_li").attr("style","display:none");
//				}else{
//					$("#unReadQuestionNum_li").attr("style","display:block");
//				}
//				if(data.split(",")[0]==0 && data.split(",")[1]==0){
//					if(param==2)
//						$('#msg_box').slideDown();
//					if(param==1)
//						$('#msg_box').slideUp();
//				}else{
//					$('#msg_box').slideDown();
//				}
			}
		});
	};


	
	// Guide
	var guide = function() {
		//$('#guide li').hover(
		//	function() {
		//		$('.guide_more', $(this)).show();
		//	},
		//	function() {
		//		$('.guide_more', $(this)).hide();
		//	}
		//);
		//
		
			$('#guide_v2').hover(
				function() {
					$(this).css('right', 0);
				},
				function() {
					$(this).css('right', -220);
				}
			);
	};
	
	// 激活账号
	var activeAcc = function() {
		// 如果用户状态未激活
		if (config.onStep === 1 || config.onStep === 2) {
			$.getScript(config.userActJs);
		}
	};
	
	// 添加到收藏夹
	var fav = function() {
		$('#menu_fav').click(function(ev) {
			ev.preventDefault();
			var ctrl = (navigator.userAgent.toLowerCase()).indexOf('mac') != -1 ? 'Command/Cmd' : 'CTRL',
				//url = window.location.href,
				//title = document.title,
				url = 'http://ec.yto.net.cn/',
				title = '易通电子商务物流信息平台-圆通电商物流平台-圆通易通诚信软件电商版',
				tips = '添加失败\n您可以尝试通过快捷键' + ctrl + ' + D 加入到收藏夹~';
			
			try {
				if (document.all) {
					window.external.addFavorite(url, title);
				} else if (window.sidebar) {
					window.sidebar.addPanel(title, url, '');
				} else {
					alert(tips);
				}
			} catch(e) {
				alert(tips);
			}
		});
	};
	
	var video = function(){
		$("#video").click(function(ev){
			ev.preventDefault();
			window.open('jiaocai');
		});
	}
	
	var help = function(){
		$("#help").click(function(ev){
			ev.preventDefault();
			window.open('noint1_toHelp.action');
		});
	}
	
	// 退出
	var exit = function() {
		$('#user_exit').click(function(ev) {
			ev.preventDefault();
			
			var exitDialog = new Dialog();
			
			exitDialog.init({
				contentHtml: '您确定要退出系统吗？',
				yes: function() {
					exitDialog.close();
					setTimeout(function(){window.location.href = config.exitUrl;},0);
				},
				no: function() {
					exitDialog.close();
				},
				closeBtn: true
			});

		});
	};
	
	// 导航
	var nav = function() {
		var enterEvent,	leaveEvent;
		
		$('#menu_level_1 li').
			// 鼠标移至主菜单显示/隐藏子菜单
			hover(
				function() {
					var _this = $(this);
					clearTimeout(leaveEvent);
					
					enterEvent = setTimeout(function() {
						var left = _this.position().left + 10 - 15,
							dataMenu = _this.data('menu'),
							thisClass = '.' + dataMenu;
						$('.menu_level_2').slideUp();
						
						$(thisClass).css('left', left).slideDown();
					}, 150);
				},
				
				function() {
					clearTimeout(enterEvent);
				}
			).
			// 点击主菜单触发其子菜单的第一个菜单
			click(function(ev) {
				var _this = $(this),
					dataMenu = _this.data('menu'),
					thisClass = '.' + dataMenu;
				
				if(!$(thisClass).first().find('li').length){
					ev.preventDefault();
					return;
				}
				setTimeout(function() {
					window.location = $('li a', $(thisClass)).first().attr('href');
				}, 0);
				
			});
	//	$('#menu_level_1 li').mouseenter(function() {
	//		var _this = $(this),
	//			left = _this.position().left + 10 - 15,
	//			dataMenu = _this.data('menu'),
	//			thisClass = '.' + dataMenu;
	//		$('.menu_level_2').slideUp();
	//		
	//		$(thisClass).css('left', left).slideDown();
	//	});
		
		$('.menu_level_2').mouseleave(function() {
			$(this).slideUp();
		});
		$('#main_nav_l').mouseleave(function() {
			$('.menu_level_2').slideUp();
		});
		
		
		// 为子菜单添加 class
		$('.menu_level_2').each(function() {
			var _this = $(this);
			$('li', _this).first().addClass('submenu_top');
			$('li', _this).last().addClass('submenu_btm');
		});
		
		$('#sub_nav li').first().addClass('nav_menu_first');
		
		$('a', $('.cur_sub_menu').next()).css('border-left-color', '#265E91');
		
		
	};
	
	return {
		// 输入框状态
		setStatus: function() {
			var focusStatus = 'focus_status',
				keypressStatus = 'keypress_status',
				inputText = 'input_text',
				textareaText = 'textarea_text';
				
			$('input[type="text"]:not(".Wdate"), input[type="password"]').live({
				focus: function() {
					$(this).removeClass().addClass(function() {
						return focusStatus + ' ' + inputText;
					});
				},
				blur: function() {
					$(this).removeClass().addClass(inputText);
				},
				keypress: function() {
					$(this).removeClass().addClass(function() {
						return keypressStatus + ' ' + inputText;
					});
				}
			});
				
			$('textarea').bind({
				focus: function() {
					$(this).removeClass().addClass(function() {
						return focusStatus + ' ' + textareaText;
					});
				},
				blur: function() {
					$(this).removeClass().addClass(function() {
						return textareaText;
					});
				},
				keypress: function() {
					$(this).removeClass().addClass(function() {
						return keypressStatus + ' ' + textareaText;
					});
				}
			});
		},
		
		// 初始化
		init: function() {
			nav();
			defaultTxt();
			oulineNone();
			notice();
//			guide();
			activeAcc();
			ytoCommon.setStatus();
			fav();
			help();
			video();
			exit();
			showNotice(1);
            $('#guide_msg_text').defaultTxt('给易通提建议');
			//viewNotice();
//			vipIcon();
			
			/**
			 * 第一次登陆进来或刷新
			 */
//			$.ajax({
//				url:'noint!firstLoginOrF5.action',
//				dataType:'json',
//				cache:false,
//				success:function(data){
//					if(data.status){
//						showNotice(1);
//					}
//				}
//			});
			/**
			 * 监听显示未读信息弹出层事件。如果弹出层关闭了，5分钟后自动弹出
			 */
			setInterval(function() {
				
				$.ajax({
					url:'noint!checkMsgAlert.action',
					dataType:'json',
					cache:false,
					success:function(data){
						//1-代表当前时间为5分钟的倍数
						if(data != null && typeof data != 'undefined' && data.status){
							showNotice(1);
						}
					}
				})
			}, 60*1000);
		}
	}
})();

ytoCommon.init();

$(function() {
	var els = {
			searBtn:$("#guide_sear_btn"),	
			searForm:$("#guide_sear_form"),
			msgBtn:$("#guide_msg_btn"),
			msgForm:$("#guide_msg_form"),
			msgText: $('#guide_msg_text')
		};
		
		els.searBtn.click(function(ev){
			ev.preventDefault();
			if($("#guide_sear_input").val()!=null && $.trim($("#guide_sear_input").val())!=""){
				els.searForm.trigger("submit");
			}else{
				window.location.href="waybill_bill.action?menuFlag=chajian_waybill";
			}
		});
		
		els.msgBtn.click(function(ev){
			ev.preventDefault();
			var msgVal = els.msgText.val();
			if ( msgVal == '给易通提建议' || $.trim(msgVal) == '') {
				/*var emptyValDialog = new Dialog();
				emptyValDialog.init({
					contentHtml: '消息内容不能为空',
					closeBtn: true
				});*/
				els.msgText.focus();
			} else {
				$.ajax({
					url: 'send_suggest.action',
					cache: false,
					type: 'POST',
					data: {
						messageTheme : $("#messageTheme").val(),
						messageContent : msgVal
					},
					success: function() {
						var msgSuccDialog = new Dialog();
						msgSuccDialog.init({
							contentHtml: '谢谢您的反馈！',
							closeBtn: true
						});
						
						els.msgText.val('');
					}	
				});
			}
		});
		
	$('#guide_v2').hover(
			function() {
				$(this).css('right', 0);
			},
			function() {
				$(this).css('right', -220);
			}
		);
	

	
	// 消息
	//var viewNotice = function() {
		//公共头部右上角查看问题件和消息
		$('.nav_num').click(function(ev) {
			ev.preventDefault();
			//查看未读问题件
			var url = "#";
			if($(this).attr("id") == 'unReadQuestionNum'){
				
				var num = $.trim($(this).text());
				if(num!=0)
					url = "questionnaire_unReadList.action?isRead=0&currentPage=1&menuFlag=chajian_question";
			}
			else if($(this).attr("id") == 'unReadMessageNum'){
				var num = $.trim($(this).text());
				if(num!=0)
					url = "message_index.action?menuFlag=msg_index";
			}
			window.location.href=url;
		});
		
	//};
});
//$(function() {
//		var menuHd = $('.menu_hd');
//		menuHd.hover(
//			function() {
//				$(this).addClass('menu_hover');
//			},
//			function() {
//				$(this).removeClass('menu_hover');
//			}
//		);
//		
//		menuHd.toggle(
//			function() {
//				$('.sub_menu', $(this).parent()).slideUp();
//			},
//			function() {
//				$('.sub_menu', $(this).parent()).slideDown();
//			}
//		)
//		
//		getNew();//获取到new.png
//		//点击后删除
//		$("#sidebar .sub_menu a").click(function(ev){
//			ev.preventDefault();
//			var _this = $(this);
//			if(_this.hasClass('new')){
//				$.ajax({
//					  url: 'noint!remove.action?newIcon='+_this.attr('name'),
//					  success: function(data) {
//						_this.removeClass('new');
//					  }
//				});
//			} else {
//				window.location.href=_this.attr('href');
//			}
//			
//		});
//	
//});
/*
	//格式化时间
	//使用方法 
	var now = new Date(); 
	var nowStr = now.format("yyyy-MM-dd hh:mm:ss"); 
	//使用方法2: 
	var testDate = new Date(); 
	var testStr = testDate.format("yyyy年MM月dd日hh小时mm分ss秒"); 
	alert(testStr); 
	//示例： 
	alert(new Date().format("yyyy年MM月dd日")); 
	alert(new Date().format("MM/dd/yyyy")); 
	alert(new Date().format("yyyyMMdd")); 
	alert(new Date().format("yyyy-MM-dd hh:mm:ss"));
*/
Date.prototype.format = function (format) {
	var o = {
		"M+" : this.getMonth() + 1, //month
		"d+" : this.getDate(), //day
		"h+" : this.getHours(), //hour
		"m+" : this.getMinutes(), //minute
		"s+" : this.getSeconds(), //second
		"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter
		"S" : this.getMilliseconds() //millisecond
	}
	
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	
	for (var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
var pagination = $(".pagenavi a");

//弹出new.png

function getNew(){
	
	$.ajax({
		  url: 'noint!getIcon.action',
		  success: function(data) {
			if(data != null){
				var $links = $("#sidebar .sub_menu a");
				//遍历两个数组，有对应相等的name的值的时候，在此值所在的元素上添加class = new
				
				$links.each(function(){
					
					var _this = $(this),
					    $name = _this.attr('name');
					
					
					if($.inArray($name, data) !== -1){
						_this.addClass('new');
						
					} else {
						_this.removeClass('new');
					}
				});
			}
		 }
	});
	
	
}


