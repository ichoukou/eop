$(function() {	
	var article = (function() {
		
		var form = function() {
			var els = {
				workForm: $('#workForm'),
				currentPage: $('#currentPage')
			};
			
			pagination.live("click",function(ev){
				ev.preventDefault();
				els.currentPage.val($(this).attr("value"));
				setTimeout(function(){
					els.workForm.trigger('submit');
				},0);
			});
		}
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	article.init();
})