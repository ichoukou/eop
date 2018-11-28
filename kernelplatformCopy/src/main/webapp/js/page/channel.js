/**
 * 渠道消息管理
 */
$(function(){
	
	//翻页
	pagination.live('click',function(ev){
		ev.preventDefault();
		var currentPage = $(this).attr("value");
		setTimeout(function(){
			window.location.href='channel_list.action?menuFlag=peizhi_channel_list&currentPage='+ currentPage;
		},0);
	});
})

	function edit(id) {
		window.location.href = 'channel_editUI.action?menuFlag=peizhi_channel_list&id=' + id;
	}

	function del(id) {
		var dialog = new Dialog();
		dialog.init({
			contentHtml:'<p>确定要删除该项数据吗?</p>',
			yes:function(){
				$.ajax({
					type:'post',
					url : 'channel_remove.action',
					data : {
						id : id
					},
					cache:false,
					success:function(response){
						dialog.close();
						var dialog2 = new Dialog();
						dialog2.init({
							contentHtml: response,
							yes:function(){
								dialog2.close();
								window.location = "channel_list.action?menuFlag=peizhi_channel_list";
							}
						});
					}
				});
			},
			no:function(){
				dialog.close();
			},
			closeBtn:true
		});
	}
	
	/*function add() {
		window.location = "channel_addUI.action";
	}*/