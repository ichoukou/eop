/**
 * 易通选项卡方法
 * @author 朱一成 i@wange.im
**/
var ytoTab = (function() {
	/**
	 * 选项卡显示方法
	 * @param switchIndex {Number} 选项卡切换至索引
	 * @param triggerElements {Element} 选项卡触发器集合
	 * @param panelElements {Element} 选项卡面板集合
	 * @param currentClass {String} 选项卡触发器当前 class
	**/
	var tabShow = function(switchIndex, triggerElements, panelElements, currentClass) {
		triggerElements.eq(switchIndex).addClass(currentClass).siblings().removeClass(currentClass);
		panelElements.eq(switchIndex).show().siblings().hide();
	};
	
	/**
	 * 选项卡切换方法
	 * @param triggerElements {Element} 选项卡触发器集合
	 * @param panelElements {Element} 选项卡面板集合
	 * @param triggerEvent {String} 选项卡触发器动作 'click' / 'mouseover'
	 * @param currentClass {String} 选项卡触发器当前 class
	**/
	var tabSwitch = function(triggerElements, panelElements, triggerEvent, currentClass) {
		triggerElements.bind(triggerEvent, function() {
			var _this = $(this),
				index = _this.index();
			
			tabShow(index, triggerElements, panelElements, currentClass);
		});
	};
	
	return {
		init: function(index, parentEl) {
			$('.tab', parentEl).each(function() {
				var _this = $(this),
					triggerEls = $('.tab_triggers li', _this),
					panelEls = $('.tab_panel', _this),
					triggerEv = 'click',
					curClass = 'tab_cur',
					gotoIndex = index || 0;
					
				tabShow(gotoIndex, triggerEls, panelEls, curClass);
				
				tabSwitch(triggerEls, panelEls, triggerEv, curClass);
			});
		}
	}
})();

//ytoTab.init();