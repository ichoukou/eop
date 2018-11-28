/**
 * 易通登录页面
**/

$(function() {
	var ytoLogin = (function() {
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		var subFlag = 0;
		var allowLogin = false;
		var quickAjaxAllow = true;
		// 全局配置
		var config = {
			cookieUsername:winParams.cookieUserName || '',
			cookiePassword:winParams.cookiePassword || '',
			checkcodeImg: winParams.checkcodeImg || '',			// 验证码图片
			checkcodeUrl: winParams.checkcodeUrl || '',			// 验证码请求地址
			showCheckcode: winParams.showCheckcode || false,		// 是否显示验证码
			quickStartAction: winParams.quickStartAction || '',	// 功能介绍 action
			imagesPath: winParams.imagesPath || ''		
		};
		
		// 输入框默认提示 
		var inputStatus = function() {
			$.fn.defaultTxt = function(txt) {
				var _this = $(this),
					focusClass = 'focus_status',
					blurClass = 'blur_status';
					
				_this.focus(function() {
					if (_this.val() == txt) {
						_this.val('');
					}
					_this.addClass(focusClass).removeClass(blurClass);
				});
				_this.blur(function() {
					if (_this.val() == '') {
						_this.val(txt);
						_this.addClass(blurClass).removeClass(focusClass);
					}
				});
				
			};
		};
		
		// 验证码
		var checkCode = {
			// 初使化
			init: function() {
				this.update();
			},
			// 改变验证码
			update: function() {
				var now = new Date();
				$('#checkcode_img').attr('src', config.checkcodeImg + '?t=' + now);
				
				// 输入框回到初始状态
				$('#checkcode').val('');
				$('#checkcode').prev().html('验证码不分大小写');
			}
		};
		
		// 添加到收藏夹
		var fav = function() {
			$('#fav a').click(function(ev) {
				ev.preventDefault();
				var ctrl = (navigator.userAgent.toLowerCase()).indexOf('mac') != -1 ? 'Command/Cmd' : 'CTRL',
					url = window.location.href,
					title = document.title,
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
		
		// 添加快捷方式
		var shorCut = function() {
			$('#shortcut a').click(function(ev) {
				ev.preventDefault();
				window.location.href = _ctxPath + "/%E6%98%93%E9%80%9A%E5%BF%AB%E6%8D%B7%E6%96%B9%E5%BC%8F.url";
			});
		};
		
		// 输入框默认文字
		var defText = function() {
			$("#mark_psw,#account_id").attr('disabled','disabled');
			
			// 填充 label 文案
			$('#login_form p label').each(function() {
				
				var _this = $(this),
					attr = _this.attr('data-text'),
					input = $('input', _this.parent());
					
				if(config.cookieUsername && input.attr("id")=='account_id'){
					input.val(config.cookieUsername);
					$("#mark_psw").attr("checked","checked");
				}
				else if(config.cookiePassword && input.attr("id")=='psw'){
					input.val(config.cookiePassword);
					$("#mark_psw").attr("checked","checked");
				}
				if (input.val() == '') {
					_this.html(attr);
				} else if (input.val() != attr) {
					_this.html('');
					input.css('color', '#000');
				}
				/*setTimeout(function() {
					if(config.cookieUsername && input.attr("id")=='account_id'){
						input.val(config.cookieUsername);
						$("#mark_psw").attr("checked","checked");
					}
					else if(config.cookiePassword && input.attr("id")=='psw'){
						input.val(config.cookiePassword);
						$("#mark_psw").attr("checked","checked");
					}
					if (input.val() == '') {
						_this.html(attr);
					} else if (input.val() != attr) {
						_this.html('');
						input.css('color', '#000');
					}

				}, 800);*/
			});
			
			// 鼠标聚焦 input
//			$('#login_form p input').focus(function() {
//				var _this = $(this),
//					label = $('label', _this.parent()),
//					dataText = label.attr('data-text');
//				
//				if (_this.val() != dataText) {
//					label.html('');
//				}
//			});
			
			$('#account_id').focus(function(){
				var _this = $(this),
					label = $('label', _this.parent()),
					dataText = label.attr('data-text');
				
				if (_this.val() != dataText) {
					label.html('');
					$('#pwdStr').html('');
				}
			});
			
			$('#psw').focus(function(){
				var _this = $(this),
					label = $('label', _this.parent()),
					dataText = label.attr('data-text');
			
				if (_this.val() != dataText) {
					label.html('');
				}
			});
			
			// 鼠标移开 input
//			$('#login_form p input').blur(function() {
//				var _this = $(this),
//					label = $('label', _this.parent()),
//					dataText = label.attr('data-text');
//				
//				console.log($('#account_id').val());
//				if (_this.val() == '') {
//					label.html(dataText);
//				}
//			});
			
			$('#account_id').blur(function() {
				var _this = $(this),
				label = $('label', _this.parent()),
				dataText = label.attr('data-text');
				
				if (_this.val() == '' && $('#pwdStr').val() == '') {
					label.html(dataText);
					$('#pwdStr').html('请输入密码');
				}else if($('#account_id').val() != '' && $('#psw').val() != ''){
					label.html('');
					$('#pwdStr').html('');
				}else if($('#account_id').val() != '' && $('#pwdStr').val() == ''){
					$('#pwdStr').html('请输入密码');
				}
			});
			
			$('#psw').blur(function(){
				var _this = $(this),
				label = $('label', _this.parent()),
				dataText = label.attr('data-text');
				
				if (_this.val() == '') {
					label.html(dataText);
				}
			});
			
			// 回车提交表单
			$('body').keypress(function(ev) {
				if (ev.keyCode == 13) {
					subFlag++;
//					$('#login_form').trigger('submit');

					var submitInterval = setInterval(function() {
						if (allowLogin) {
							$('#login_form').trigger('submit');
							
							clearInterval(submitInterval);
						}
					}, 150);
					
					//setTimeout(function(){$('#login_btn').click();},0);
				}
			});
			
			$("#mark_psw,#account_id").removeAttr('disabled');
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
		
		// 表单验证
		var form = function() {
			if (config.showCheckcode) {
				checkCode.init();
			}
			
			
			// 更新验证码
			$('.change_code').click(function(ev) {
				ev.preventDefault();
				
				checkCode.update();
			});
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
			
			var els = {
					accId: $('#account_id'),
					psw: $('#psw'),
					checkcode: $('#checkcode'),
					loginBtn: $('#login_btn'),
					loginForm: $('#login_form')
				};
			
			var msg = {
				accIdEmpytErr: '用户名不能为空',
				accIdFormatErr: '用户名格式错误',
				pswEmpytErr: '请输入密码',
				pswFormatErr: '密码格式错误',
				checkErr: '验证码错误',
				checkEmptyErr: '验证码不能为空',
				accLenErr: '用户名长度为 3-20 字符'
			};
			
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'login_form',
				theme: 'yto',
				errorFocus: false,
				submitAfterAjaxPrompt : '有数据正在异步验证，请稍等...'
			});
				
			// 用户名
			els.accId.
				formValidator({validatorGroup:'1', tipID: 'error_msg', onShow: '', onFocus: '', onCorrect: ''}).
				inputValidator({min: 1, onErrorMin: msg.accIdEmpytErr}).
				regexValidator({regExp: 'accountId', dataType: 'enum', onError: msg.accIdFormatErr});
				
			// 密码
			els.psw.
				formValidator({validatorGroup:'1', tipID: 'error_msg', onShow: '', onFocus: '', onCorrect: ''}).
				inputValidator({min: 1, onErrorMin: msg.pswEmpytErr}).
				regexValidator({regExp: 'password', dataType: 'enum', onError: msg.pswFormatErr});
			
			// 显示验证码的时候才校验
			if (config.showCheckcode) {
				// 验证码
				els.checkcode.
					formValidator({validatorGroup:'1', tipID: 'error_msg', onShow: '', onFocus: '', onCorrect: ''}).
					inputValidator({min: 1, max: 4, onErrorMin: msg.checkEmptyErr, onErrorMax: msg.checkErr}).
					ajaxValidator({
						cache:false,
						dataType:"json",
						url: config.checkcodeUrl,
						success: function(data) {
							if (!data.status) {
								setTimeout(function(){showIcon.error($('#error_msg'), "验证码错误");},5);
								allowLogin = true;
								$('.change_code').click();
							}
							if(data.status && subFlag > 0 && subFlag < 5){
								setTimeout(function(){els.loginForm.trigger('submit');},5);
							}
							
							return data.status;
						},
						onError: ' ',
						onWait : "正在校验验证码，请稍候..."
					});
			}
			
			if ($('#error_msg').html() !== '') { $('#error_msg').show();}
			if($('.errorMessage span').html()!==''){
				var $errorMessage = $('.errorMessage span').html();
					$('.errorMessage').remove();
				if ($('#error_msg').html() == '') { 
					$('#error_msg').text($errorMessage);
					$('#error_msg').show();
				}
				
			}
			
			// 点击“登录”
			els.loginBtn.live("click",function(ev) {
				subFlag++;
				ev.preventDefault();
				els.loginForm.trigger('submit');
			});
		};
		
		// 轮播图
		var slide = function() {
			var gap = -855;
			var timer;

			var $slideDiv = $('#slide_box');
			var $slides = $('#slide_box ul');
            var $n_btn = $('#slide_next');
			var $p_btn = $('#slide_prev');
			$slides.canMove = true;

			
			$n_btn.click(function(ev) {
				ev.preventDefault();

				if($slides.canMove){
                    $slides.canMove = false;
					$slides.animate({
						marginLeft: gap
					}, 100, function(){
							var n = $slides.find('li').clone().toArray();
							var k = $.grep(n, function(i, j) { return j > 4 });
							var temp = $.grep(n, function(i, j) { return j > 4 }, true);
							$slides.html("");
							$(k).each(function(i, j) { 
								$slides.append(j);
							});
							$slides.append(temp);
							$slides.css("margin-left", 0);
							$slides.canMove = true;
					});
				}
				

			});
			
			$p_btn.click(function(ev) {
				ev.preventDefault();

				if($slides.canMove){
					$slides.canMove = false;
					var n = $slides.find('li').clone().toArray();
					var k = $.grep(n, function(i, j) { return j < n.length - 5; });
					var temp = $.grep(n, function(i, j) { return j < n.length - 5; }, true);
					$slides.html("").append(temp);
					$(k).each(function(i, j){
						$slides.append(j);
					});
					$slides.css("margin-left", gap);
					$slides.animate({ marginLeft: 0 }, 100, function(){ $slides.canMove = true; });
				}
			});
			
			$slideDiv.mouseover(function(){
				clearInterval(timer);
			});
			
			$slideDiv.mouseout(function(){
				timer = setInterval(function() {
					$n_btn.trigger('click');
				}, 3000);
			});
			
			$('#slide_bd').hover(
				function() {
					$('#slide_navi').fadeIn();
				},
				function() {
					$('#slide_navi').fadeOut();
				}
			);
			
			timer = setInterval(function() {
				$n_btn.trigger('click');
			}, 3000);
		};
		
		// 是否显示验证码
		var showCheck = function() {
			if (config.showCheckcode) {
				$('.checkcode_line').show();
				
				$('#login_box').removeClass('no_checkcode_login');
			} else {
				$('.checkcode_line').hide();
				
				$('#login_box').addClass('no_checkcode_login');
			}
		};
		// 功能介绍
		var quickStart = function() {
			$('#quick_start').click(function(ev) {
				ev.preventDefault();
				if (quickAjaxAllow && $('#quick_start_dialog').length == 0) {
					quickAjaxAllow = false;
					$.ajax({
						url: config.quickStartAction,
						type: 'GET',
						success: function(data) {
							var imgPath = config.imagesPath;
							var loginLink = $('#login_type_b a').attr('href');
							var startDialogHtml = '<div id="quick_start_dialog">' +
												'	<div id="img_list">' +
												'		<ul class="clearfix">' +
												'			<li>' +
												'				<img src="' + imgPath + '/single/rumen_01.jpg" alt="" />' +
												'			</li>' +
												'			<li>' +
												'				<img src="' + imgPath + '/single/rumen_02.jpg" alt="" />' +
												'			</li>' +
												'			<li>' +
												'				<img src="' + imgPath + '/single/rumen_03.jpg" alt="" />' +
												'			</li>' +
												'			<li>' +
												'				<img src="' + imgPath + '/single/rumen_04.jpg" alt="" />' +
												'			</li>' +
												'			<li>' +
												'				<img src="' + imgPath + '/single/rumen_05.jpg" alt="" />' +
												'				<a href="' + loginLink + '" class="img_list_ru"></a>' +
												'			</li>' +
												'		</ul>' +
												'	</div>' +
												'	<div id="quick_navi">' +
												'		<a href="javascript:;" id="quick_prev" class="png">Prev</a>' +
												'		<a href="javascript:;" id="quick_next" class="png">Next</a>' +
												'	</div>' +
												'	<a href="javascript:;" id="close_quick" class="png">关闭</a>' +
												'</div>';
							// 注入遮罩层
							var maskHtml = '<div id="quick_mask"></div>';
							
							// 插入元素
							$('body').prepend(maskHtml).append(startDialogHtml);
							var winEl = $(window),
								quickStartEl = $('#quick_start_dialog'),
								posLeft = (winEl.outerWidth() - quickStartEl.outerWidth()) / 2,
								posTop = (winEl.outerHeight() - quickStartEl.outerHeight()) / 2;
							
							
							
							// 主弹层样式
							quickStartEl.css({
								left: posLeft,
								top: posTop
							});
							
							// 遮罩层样式
							$('#quick_mask').css({
								width: winEl.outerWidth(true),
								height: winEl.outerHeight(true),
								opacity: 0.4
							});
							var gap = -849;
							var $slides = $('#img_list ul');
							var $n_btn = $('#quick_next');
							var $p_btn = $('#quick_prev');
							$slides.canMove = true;
			
			
							// 点击“关闭”按钮
							$('#close_quick').click(function(ev) {
								ev.preventDefault();
								
								$('#quick_mask').remove();
								$('#quick_start_dialog').remove();
							});
							
							// 隐藏/显示按钮
							quickStartEl.hover(
								function() {
									$('#quick_navi').fadeIn(300);
									$('#close_quick').fadeIn(300);
								},
								function() {
									$('#quick_navi').fadeOut(300);
									$('#close_quick').fadeOut(300);
								}
							);
							
							
							$n_btn.click(function(ev) {
								ev.preventDefault();

								if($slides.canMove){
									$slides.canMove = false;
									$slides.animate({
										marginLeft: gap
									}, 600, function(){
											var n = $slides.find('li').clone().toArray();
											var k = $.grep(n, function(i, j) { return j > 0 });
											var temp = $.grep(n, function(i, j) { return j > 0 }, true);
											$slides.html("");
											$(k).each(function(i, j) { 
												$slides.append(j);
											});
											$slides.append(temp);
											$slides.css("margin-left", 0);
											$slides.canMove = true;
									});
								}
								

							});
							
							$p_btn.click(function(ev) {
								ev.preventDefault();

								if($slides.canMove){
									$slides.canMove = false;
									var n = $slides.find('li').clone().toArray();
									var k = $.grep(n, function(i, j) { return j < n.length - 1; });
									var temp = $.grep(n, function(i, j) { return j < n.length - 1; }, true);
									$slides.html("").append(temp);
									$(k).each(function(i, j){
										$slides.append(j);
									});
									$slides.css("margin-left", gap);
									$slides.animate({ marginLeft: 0 }, 600, function(){ $slides.canMove = true; });
								}
							});
							
							// 允许再次点击
							quickAjaxAllow = true;
						}
					});
				}
				

			})
		};
		
		// 易通推荐浏览器
		var ytoBrowse = function() {
			$('#earth_icon').mouseenter(function() {
				$('#browse_dialog').show(300);
			});
			$('#earth_icon').mouseleave(function() {
				$('#browse_dialog').hide(300);
			});
		};
		
		
		return {
			init: function() {
				defText();
				//inputStatus();
				//defaultText();
				ytoBrowse();
				shorCut();
				fav();
				oulineNone();
				form();
				slide();
				showCheck();
				quickStart();
			}
		}
	})();
	
	ytoLogin.init();
})