$(document).ready(function() {
	

	$('.mailno').click(function(ev) {
		ev.preventDefault();
		var mailNo = $(this).text();
		// 请求地址
		var requestUrl = 'logistic_queryInfo.action?mailNo='+$.trim(mailNo);
		
		// 正在加载
		var loadDialog = new Dialog();
		loadDialog.init({
			contentHtml: '正在加载...'
		});
		// 发送异步请求
		$.ajax({
			url: requestUrl,
			type: 'GET',
			cache: false,
			dataType: 'html',
			success: function(data) {
				// 关闭弹层
				loadDialog.close();
				var sndDialog = new Dialog();
				sndDialog.init({
					contentHtml: data,
					closeBtn: true
				})
				
				ytoTab.init(0, $('#dialog'));
			}
		})
	});
	
	$('.item_navi a').live('click', function(ev) {
		ev.preventDefault();
		var curPage = $(this).attr("val");
		// 请求地址
		var requestUrl = 'logistic_paginationQuery.action?currentPage='+curPage;
		// 发送异步请求
		$.ajax({
			url: requestUrl,
			type: 'GET',
			cache: false,
			dataType: 'html',
			success: function(data) {
				$('.item_navi').parent().html(data)
			}
		})
	});

	$('.mailnos').live('click',function(ev) {
		ev.preventDefault();
		
		var mailNo = $(this).text();
		// 请求地址
		var requestUrl = 'logistic_queryInfo.action?mailNo='+$.trim(mailNo);
		
		// 正在加载
		var loadDialog = new Dialog();
		loadDialog.init({
			contentHtml: '正在加载...'
				
		});
		// 发送异步请求
		$.ajax({
			url: requestUrl,
			type: 'GET',
			cache: false,
			dataType: 'html',
			success: function(data) {
				// 关闭弹层
				loadDialog.close();
				var sndDialog = new Dialog();
				sndDialog.init({
					contentHtml: data,
					closeBtn: true
				})
				
				ytoTab.init(0, $('#dialog'));
			}
		})
	});
});