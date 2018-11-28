$(function() {
	$('#dialog_demo_c2').click(function() {
		var tab = '<div class="dialog_c">' +
				  '<p><span class="dialog_p">运单号：</span><span>8000120004</span></p>'+
				  '<p><span class="dialog_p">接受网点名称：</span><span>北京市东城区圆通速递有限公司</span></p>'+
				  '<p><span class="dialog_p">问题件描述：</span><span><textarea name="" class="textarea_text" id="textarea_demo" cols="100" rows="5"></textarea></span></p>'+
				  '<p class="dialog_btn"><a href="javascript:;" class="btn btn_a" title="提交"><span>提交</span></a><a href="javascript:;" class="btn btn_a" title="取消"><span>取消</span></a></p>'+
				  '</div>';
		var dialogD = new Dialog();
		dialogD.init({
			closeBtn: true,
			//maskOpacity: 0.2,			// 遮罩层的透明度
			contentHtml: tab			// 内容 HTML
		});
		
		// 重新绑定 tab 事件
		ytoTab.init();
	});
})