/**
 * 易通表格方法
 * @author 朱一成 i@wange.im
**/

	var ytoTable = (function() {
		/**
		 * 表格边框
		 * @param thEls {Element} th 表格元素集合
		 * @param trEls {Element} tr 表格元素集合
		**/
		var fixBorder = function(thEls, trEls) {
			thEls.first().addClass('first_th');
			thEls.last().addClass('last_th');
			trEls.last().addClass('last_tr');
		};
		
		/**
		 * 表格背景换色
		 * @param trEls {Element} tr 表格元素集合
		**/
		var trHover = function(trEls) {
			trEls.live('mouseover', function() {
				$(this).addClass('tr_hover');
			});
			trEls.live('mouseout', function() {
				$(this).removeClass('tr_hover');
			});
		};
		
		/**
		 * 模拟下拉
		 * @param trEls {Element} tr 表格元素集合
		**/
		var theadSelect = function(trEls) {
			// 找到有下拉的表头元素
			var thTitleEm = $('em', $('.th_title', trEls).has('.thead_select'));
			thTitleEm.addClass('arrow table_down');
			
			// 点击下拉箭头
			thTitleEm.toggle(
				function() {
					var _this = $(this);
					_this.removeClass('table_down').addClass('table_up');
					$('.thead_select', _this.parent()).slideDown();
				},
				function() {
					var _this = $(this);
					_this.removeClass('table_up').addClass('table_down');
					$('.thead_select', _this.parent()).slideUp();
				}
			);
			
			// 鼠标移至下拉选项
			$('.thead_select li', trEls).mouseover(function() {
				var _this = $(this);
				_this.addClass('thead_li_hover').siblings().removeClass('thead_li_hover');
			});
			
			// 点击下拉选项
			$('.thead_select li', trEls).click(function() {
				var _this = $(this);
				$('.th_title em', _this.parents('th')).html(_this.html());
				$('em', _this.parents('th')).trigger('click');
			});
		};
		
		// 全选
		var selectAll = function() {
			/**
			 * input 全选方法
			 * 使用方法（伪代码）：$('全选框元素').checkAll( $('复选框元素集合') );
			 * @param els {Element} 需要全选的 input[type="checkbox"] 复选框元素集合
			**/
			$.fn.checkAll = function(els) {
				var _this = $(this);
				
				// 勾选全选框
				_this.change(function() {
					var propChecked = $(this).prop('checked');
					if (propChecked) {			// 如果勾选中
						els.prop('checked', true);
					} else {					// 如果不勾选
						els.prop('checked', false);
					}
				});
				
				// 勾选其他 input 复选框
				els.change(function() {
					var el = $(this);
					// 如果全选框勾中，但当前复选框不勾中
					if (_this.prop('checked') && !el.prop('checked')) {
						_this.prop('checked', false);
					}
				});
			};
		};
		
		return {
			init: function() {
				$('.table').each(function() {
					var _this = $(this),
						thEls = $('thead tr th', _this),
						trEls = $('tbody tr', _this);
					
					fixBorder(thEls, trEls);
					trHover(trEls);
					
					theadSelect(thEls);
				});
				
				selectAll();
			}
		}
	})();
	$(function() {
		ytoTable.init();
	})
	
