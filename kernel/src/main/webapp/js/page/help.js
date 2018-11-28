/**
 * 帮助中心
**/
$(function() {
	var ytoHelp = (function() {
		// 两列等高
		var autoHeight = function() {
			var navTreeEl = $('#nav_tree .box_bd'),
				helpContentEl = $('#help_content .box_bd'),
				navTreeH = navTreeEl.height(),
				helpContentH = helpContentEl.height(),
				maxH = navTreeH;
			
			navTreeEl.height(maxH);
			helpContentEl.height(maxH);
		};
		
		// 导航树
		var navTree = function() {
			var navTreeEl = $('#nav_tree'),
				menuItemEl = $('.menu_item', navTreeEl),
				firstMenuItemEl = $('.menu_item:first', navTreeEl),
				lastMenuItemEl = $('.menu_item:last', navTreeEl),
				lastSubMenuItemEl = $('.sub_menu_item:last-child', navTreeEl),
				subMenuItemEl = $('.sub_menu_item', menuItemEl),
				strongEl = $('strong', menuItemEl),
				navBtn = $('#nav_btn a');
			
			var menuItemElLen = menuItemEl.length,
				subMenuItemElLen = subMenuItemEl.length;

			var className = {
				fold: 'fold',
				firstFold: 'first_fold',
				unfold: 'unfold',
				lastFold: 'last_fold',
				firstUnfold: 'first_unfold',
				lastUnfold: 'last_unfold',
				subLast: 'sub_menu_item_last',
				curSubMenuItem: 'cur_sub_menu_item',
				curMenuItem: 'cur_menu_item'
			};
			
			// 添加 class
			menuItemEl.addClass(className.fold);
			firstMenuItemEl.addClass(className.firstFold);
			lastMenuItemEl.addClass(className.lastFold);
			lastSubMenuItemEl.addClass(className.subLast);
			
			var goTo = function(index) {
				var goSubMenuItemEl = subMenuItemEl.eq(index),
					goMenuItemEl = goSubMenuItemEl.parents('.menu_item'),
					goIndex = goMenuItemEl.index('.menu_item'),
					gotoHash = $('a', goSubMenuItemEl).attr('href');
				
				if (menuItemEl.eq(goIndex).hasClass(className.fold)) {
					strongEl.eq(goIndex).trigger('click');
				}
				subMenuItemEl.eq(index).trigger('click');
				
				window.location.hash = gotoHash;
			};
			
			strongEl.click(function() {
				var _this = $(this),
					thisMenuItem = _this.parent(),
					index = thisMenuItem.index();
				
				if (index == 0) {		// 如果是第一个分类
					if (thisMenuItem.hasClass(className.firstFold)) {
						thisMenuItem.removeClass(function() {
							return className.firstFold + ' ' + className.fold;
						}).addClass(function() {
							return className.firstUnfold + ' ' + className.unfold;
						});
					} else if (thisMenuItem.hasClass(className.firstUnfold)) {
						thisMenuItem.removeClass(function() {
							return className.firstUnfold + ' ' + className.unfold;
						}).addClass(function() {
							return className.firstFold + ' ' + className.fold;
						});
					}
				} else if (index == menuItemElLen -1) {		// 如果是最后一个分类
					if (thisMenuItem.hasClass(className.lastFold)) {
						thisMenuItem.removeClass(function() {
							return className.lastFold + ' ' + className.fold;
						}).addClass(function() {
							return className.lastUnfold + ' ' + className.unfold;
						});
					} else if (thisMenuItem.hasClass(className.lastUnfold)) {
						thisMenuItem.removeClass(function() {
							return className.lastUnfold + ' ' + className.unfold;
						}).addClass(function() {
							return className.lastFold + ' ' + className.fold;
						});
					}
				} else {		// 如果是其他分类
					if (thisMenuItem.hasClass(className.fold)) {
						thisMenuItem.removeClass(className.fold).addClass(className.unfold);
					} else if (thisMenuItem.hasClass(className.unfold)) {
						thisMenuItem.removeClass(className.unfold).addClass(className.fold);
					}
				}
			});
			
			subMenuItemEl.click(function() {
				var _this = $(this),
					subMenuItem = $('a', _this),
					menuItem = $('strong a', _this.parents('.menu_item')),
					subMenuItemTitle = subMenuItem.html(),
					menuItemTitle = menuItem.html();
					
				$('a', subMenuItemEl).removeClass(className.curSubMenuItem);
				subMenuItem.addClass(className.curSubMenuItem);
				
				$('a', strongEl).removeClass(className.curMenuItem);
				menuItem.addClass(className.curMenuItem);
				
				$('#cur_menu').html(menuItemTitle);
				$('#cur_sub_menu').html(subMenuItemTitle);
			});
			
			navBtn.click(function(ev) {
				ev.preventDefault();
				var prev = $('#nav_prev'),
					next = $('#nav_next'),
					curSubMenuItemEl = $('.cur_sub_menu_item', subMenuItemEl),
					curSubMenuItemLi = curSubMenuItemEl.parent(),
					curMenuItemLi = curSubMenuItemEl.parents('.menu_item'),
					curMenuItemEl = $('strong', curMenuItemLi),
					curSubIndex = curSubMenuItemLi.index('.sub_menu_item'),
					btnId = ev.target.id;
				
				
				switch(btnId) {
					case 'nav_prev':
						var goSubIndex = curSubIndex - 1;
						if (goSubIndex >= 0) {
							goTo(goSubIndex);
						}
					break;
					
					case 'nav_next':
						var goSubIndex = curSubIndex + 1;
						if (goSubIndex < subMenuItemElLen) {
							goTo(goSubIndex);
						}
					break;
				}
			});
			
			
			strongEl.eq(0).trigger('click');
			subMenuItemEl.eq(0).trigger('click');
		};
		
		
		// 点击按钮交替显示隐藏侧边栏
		var arrowBtn = function() {
			var mainEl = $('#main'),
				contentEl = $('#content'),
				sidebarEl = $('#sidebar'),
				arrowEl = $('.arrow_btn'),
				mainW = mainEl.width(),
				contentW = contentEl.width(),
				sidebarW = sidebarEl.width(),
				arrowL = arrowEl.position().left;
			
			var setBox = function(contentTargetW, sidebarTargetW, arrowL, sidebarCallback, arrowCallback) {
				// 修改 #content 宽度
				contentEl.animate({
					width: contentTargetW
				}, 500);
				// 修改 #sidebar 宽度
				sidebarEl.animate({
					width: sidebarTargetW
				}, 500, sidebarCallback);
				// 修改 .arrow_btn 位置
				arrowEl.animate({
					left: arrowL
				}, 500, arrowCallback);
			};
			
			arrowEl.toggle(
				function() {
					setBox(mainW, 0, 0, function() {
						sidebarEl.hide();
					}, function() {
						arrowEl.css('background-position', '-8px 0');
					});
				},
				function() {
					sidebarEl.show();
					setBox(contentW, sidebarW, arrowL, null, function() {
						arrowEl.css('background-position', '0 0');
					});
				}
			)
		};
		
		return {
			init: function() {
				autoHeight();
				navTree();
				arrowBtn();
			}
		}
	})();
	
	ytoHelp.init();
});