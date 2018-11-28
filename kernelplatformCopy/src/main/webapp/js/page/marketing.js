$(function() {
	var marketing = (function() {
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
			var checkedAll = {
				on: function(els) {
					els.prop('checked', true);
				},
				off: function(els) {
					els.prop('checked', false);
				}
			};
			
			var checkedChange = function(_this) {
				var checkedVal = _this.prop('checked'),
					targetEls = $('#market_table .input_checkbox');
					
				if (checkedVal) {	// 如果全选
					checkedAll.on(targetEls);
				} else {			// 如果不全选
					checkedAll.off(targetEls);
				}
			};
			$('.all_checkbox .checked_all').change(function() {
				checkedChange($(this));
			});
		};
		
		
		return {
			init: function() {
				ytoTab.init();
				selectArea();
				checked();
			}
		}
	})();
	
	marketing.init();
})

$(function() {
	$('#per_mesg').defaultTxt('联系人/联系电话');
	$('#textarea_demo').defaultTxt('默认短信');
})
