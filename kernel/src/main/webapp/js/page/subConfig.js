/**
 * 子配置消息管理
 */
$(function(){
	
	//翻页
	/*pagination.live('click',function(ev){
		ev.preventDefault();
		var currentPage = $(this).attr("value");
		setTimeout(function(){
			window.location.href='config_list.action?menuFlag=config_index&currentPage='+ currentPage;
		},0);
	});*/
})

	function del(id,pid) {
		var dialog = new Dialog();
		dialog.init({
			contentHtml:'<p>确定要删除该项数据吗?</p>',
			yes:function(){
				$.ajax({
					type:'post',
					url : 'config_remove.action',
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
								window.location = "config_list.action?menuFlag=peizhi_config_index&pid="+pid;
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

	function showNextData(id){
		window.location="config_list.action?menuFlag=peizhi_config_index&pid="+id;
	}

	function addNext(id){
		window.location="config_addNextUI.action?menuFlag=peizhi_config_index&pid="+id;
	}
	
	function back(id){
		if(id==null || id == ""){
			window.location="config_index.action?menuFlag=peizhi_config_index";
		}else
			window.location="config_list.action?menuFlag=peizhi_config_index&pid="+id;
	}
	
	function edit(id){
		window.location="config_editNextUI.action?menuFlag=peizhi_config_index&id="+id;
	}