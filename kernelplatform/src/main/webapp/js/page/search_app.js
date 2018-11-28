$(function() {	
	var ytouserlist = (function() {
		
		var form = function() {
			var els = {
				appForm: $('#appForm'),
				currentPage: $('#currentPage')
			};
			
			pagination.live("click",function(ev){
				ev.preventDefault();
				els.currentPage.val($(this).attr("value"));
				setTimeout(function(){
					els.appForm.trigger('submit');
				},0);
			});
		}
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	ytouserlist.init();
})