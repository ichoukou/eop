
$(function() {
	var ytoLogin = (function() {
		var winParams = window.params || {};

		var quickAjaxAllow = true;
		// 全局配置
		var config = {
			quickStartAction: winParams.quickStartAction || '',	// 功能介绍 action
			imagesPath: winParams.imagesPath || ''		
		};
		
		// 添加快捷方式
		var shorCut = function() {
			$('#shortcut a').click(function(ev) {
				ev.preventDefault();
				window.location.href = _ctxPath + "/%E6%98%93%E9%80%9A%E5%BF%AB%E6%8D%B7%E6%96%B9%E5%BC%8F.url";
			});
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
				shorCut();
				fav();
				ytoBrowse();
				quickStart();
			}
		}
	})();
	ytoLogin.init();
})