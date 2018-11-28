$(function() {
	$('#dialog_demo_c').click(function() {
		var tab = '<div id="dialog_tab">' +
				  '<p><label>地址：</label><select name="" id="" class="dia_mar"><option selected>北京</select>' +
				  '<label>预警值：</label><select name="" id="" class="dia_mar"><option selected>7天</select>' +
				  '<a href="javascript:;" class="btn btn_f" title="添加"><span>添加</span></a></p>' +
				  '<div class="table">'+
	              '<table>'+
		          '<thead>'+
						'<tr>'+
							'<th class="th_a">'+
								'<div class="th_title"><em>目的地</em></div>'+
							'</th>'+
							'<th class="th_b">'+
								'<div class="th_title"><em>预警值</em></div>'+
							'</th>'+
							'<th class="th_c">'+
								'<div class="th_title"><em>操作</em></div>'+
							'</th>'+
						'</tr>'+
					'</thead>'+
					'<tbody>'+
						'<tr>'+
							'<td class="td_a">北京</td>'+
							'<td class="td_b">3天</td>'+
							'<td class="td_c"><span class="td_c_span"><a href=""#>修改</a></span><span><a href=""#>删除</a></span></td>'+
						'</tr>'+
						'<tr>'+
							'<td class="td_a">111</td>'+
							'<td class="td_b">222</td>'+
							'<td class="td_c">333</td>'+
						'</tr>'+
						'<tr>'+
							'<td class="td_a">111</td>'+
							'<td class="td_b">222</td>'+
							'<td class="td_c">333</td>'+
						'</tr>'+
					'</tbody>'+
				'</table>'+
			'</div>' +
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