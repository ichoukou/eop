/**
 * 客户管理
**/
$(function() {
	var linkman = (function() {
		var textDefault = function() {
			$('#txt').defaultTxt('联系人/联系人电话');
		};
		
		var address = function() {
			var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#province']
			};
					 
			var linkageSel = new LinkageSel(area);
		};
	
		
		return {
			init: function() {
				textDefault();
				address();
			}
		}
	})();
	
	linkman.init();
})