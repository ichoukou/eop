$(function() {
	var userDesc = (function() {
		// 地区多级联动
		var selectArea = function() {
			var area = {
					data: districtData,
					selStyle: 'margin-left:3px;',
					select: ['#address'],
					autoLink: false
				};
			
				var linkageSel = new LinkageSel(area);
				linkageSel.changeValuesByName([$("#receiverProvince").val(), $("#receiverCity").val(), $("#receiverDistrict").val()]);
				$('#address').attr('disabled', 'disabled');
				$('#address').next().attr('disabled', 'disabled');
				setTimeout(function() {
					$('#address').next().next().attr('disabled', 'disabled');
					
				}, 0)
		};
		return {
			init: function() {
				selectArea();
			}
		}
	})();
	
	userDesc.init();
})