$(function() {
	var modify_tem = (function() {
		return {
			init: function() {
				ytoTab.init();
				
			}
		}
	})();
	
	$('#textarea_demo').defaultTxt('亲，您购买的宝贝已发出请注意查收');
	modify_tem.init();
})
