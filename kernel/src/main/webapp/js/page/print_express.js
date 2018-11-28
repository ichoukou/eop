$(function() {
	var printExpress = (function() {
		var winParams = window.params || {};
		// 配置
		var config = {
			json: winParams.json,
			markPrint: winParams.markPrint,					// 标记已打印
			ids: winParams.ids								// 勾选的 ID
		};
		
		var num = 0;
		
		// 处理数据
		var processData = function() {
			// 转为 json
			var jsonData = config.json;
			
			var picPos = jsonData.picposition.split(','),		// 背景图位置
				backgroundClass = jsonData.background,	// 背景图类型
				item = jsonData.item,		// 数据
				temPicPos = [],			// 临时变量
				templates = $('.template');	// 当前的容器
				
			// 后台在只有一个数据的时候，不能输出数组，只能用JS把对象转成数组，然后继续
			if (!(item instanceof Array)) {
				var tempItem = [];
				tempItem.push(item);
				item = tempItem;
			}
			
			// 处理面单背景图片
			for (var x=0, y=picPos.length; x<y; x++) {
				temPicPos.push(picPos[x] + 'px');
			}
			
			templates.addClass(backgroundClass).css({
				backgroundPosition: temPicPos.join(' ')
			});
			
			
			for (var t=0, tl=templates.length; t<tl; t++) {
				// 处理数据格式
				for (var i=0, l=item.length; i<l; i++) {
					var curItem = item[i],
						
						name = curItem.name,
						ucode = curItem.ucode,
						font = curItem.font == null ? 'Times New Roman' : curItem.font,
						fontsize = curItem.fontsize,
						fontspace = curItem.fontspace,
						fontweight = curItem.border == '0' ? 400 : 700,
						italic = curItem.italic == '0' ? 'normal' : 'italic',
						align = curItem.align,
						position = curItem.position,
						
						left = position[0],
						top = position[1],
						width = position[2],
						height = position[3];
					
					//$('<div class="' + ucode + ' item">' + name + '</div>').appendTo('.template');
					
					$('.item', templates.eq(t)).eq(i).css({
						left: left + 'px',
						top: top + 'px',
						width: width + 'px',
						height: height + 'px',
						fontSize: fontsize + 'px',
						fontStyle: italic,
						align: align,
						fontWeight: fontweight,
						fontFamily: font,
						letterSpacing: fontspace + 'px'
					});
				}
				// 累加 num
				
				num++;

			}
		};
		
		// 打印
		var print = function() {
			$('.print_btn a').click(function(ev) {
				ev.preventDefault();
				
				if (config.ids.length == num) {		// id号数量与累加num相等，说明已经处理完了
					// 发异步请求，标记已打印
					$.ajax({
						type: 'POST',
						url: config.markPrint,
						data: {
							markPrint: config.ids.join(',')
						},
						success: function() {
							window.print();
						}
					});
				}
			});
		};
		
		// 分页
		var pagenavi = function() {
			var els = {
				prevPageBtn: $('#prev_page'),
				nextPageBtn: $('#next_page'),
				currPage: $('#curr_page'),
				totalPage: $('#total_page'),
				table: $('table')
			};
			
			var currPageVal = parseInt($.trim(els.currPage.html()), 10),
				totalPageVal = parseInt($.trim(els.totalPage.html()), 10);
				
			if (currPageVal == totalPageVal == 1) {		// 如果只有1页
				els.prevPageBtn.hide();
				els.nextPageBtn.hide();
			} else {
				els.prevPageBtn.hide();
			}
			
			// 点击下一页
			els.nextPageBtn.click(function(ev) {
				ev.preventDefault();
				// 当前页递增
				currPageVal++;
				els.currPage.html(currPageVal)
				els.prevPageBtn.show();
				els.table.hide();
				els.table.eq(currPageVal-1).show();
				// 当前页递增至总数时
				if (currPageVal == totalPageVal) {
					$(this).hide();
				}
			});
			
			// 点击上一页
			els.prevPageBtn.click(function(ev) {
				ev.preventDefault();
				// 当前页递减
				currPageVal--;
				els.currPage.html(currPageVal)
				els.nextPageBtn.show();
				els.table.hide();
				els.table.eq(currPageVal-1).show();
				// 当前页递增至1时
				if (currPageVal == 1) {
					$(this).hide();
				}
			});
		};
		
		return {
			init: function() {
				processData();
				//getData();
				print();
				pagenavi();
			}
		}
	})();
	
	printExpress.init();
})