$(function() {	
	var orderCommand = (function() {
		
		var form = function() {
			var els = {
				userForm: $('#orderCommandForm'),
				currentPage: $('#currentPage')
			};
			pagination.live("click",function(ev){
				ev.preventDefault();
				els.currentPage.val($(this).attr("value"));
				setTimeout(function(){
					els.userForm.trigger('submit');
				},0);
			});
		}
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	orderCommand.init();
})