/**
 * 运单监控
**/
$(function() {
	$('#content_tab li').click(function() {
		var _this = $(this),
			index = _this.index();
		
		var thisSrc = $('img', _this).attr('src');

		if (thisSrc.indexOf('click') == -1) {
			thisSrc = thisSrc.replace('.jpg', '_click.jpg');
			
			
			$('img', _this).attr('src', thisSrc);
			
			$('img', _this.siblings()).each(function() {
				$(this).attr('src', $(this).attr('src').replace('_click', ''))
				
			})
		}
		
		$('.content_tab_c').eq(index).show().siblings().hide();
	})
	
	/**
	 * 运单监控开始使用
	 */
	$(".content_btn").click(function(){
		$('#formIKnow').submit();
	});
	
	/**
	 * 卖家问题件开始使用
	 */
	$("#vip_start").click(function(){
		$('#vip_start_form').submit();
	});
	
	/**
	 * 网点问题件开始使用
	 */
	$("#site_start").click(function(){
		$('#site_start_form').submit();
	});
})