$(function() {
	var markHome = (function() {

		// 获取 input[type="radio"]:checked
		var getChecked = function() {
			// 当前的 label 颜色为黑
			$('.temp_box .input_radio:checked').next().css('color', '#000');
			
			$('.temp_box .input_radio').change(function() {
				$('.temp_box label').css('color', '#808080');
				
				$(this).next().css('color', '#000');
			})
		};
		
		// 下一步
		var nextStepA = function() {
			$('#next_step_a').click(function(ev) {
				ev.preventDefault();
				if($('.temp_box .input_radio:checked').length==0) {
					var succDialog = new Dialog();
					succDialog.init({
						contentHtml: '请选择短信模板！',
						iconType: 'success',
						autoClose: 2000
					})
				}else {
					setTimeout(function(){$('#mark_form1').trigger('submit');},0);
				}
			})
		};
		
		//添加新模板
		$('#tempCreate_').click(function() {
			var templateId = '';
			if($('.temp_box .input_radio:checked').length>0) {
				templateId = $('.temp_box .input_radio:checked').val();
			}
			setTimeout(function(){window.location.href = "template_toAdd.action?menuFlag=sms_template_list&tips=4" + '&serviceId=' + $('#smsTypeId_').val() + '&id=' + templateId;},0);
		});
		
		return {
			init: function() {
				getChecked();
				nextStepA();
			}
		}
	})()
	
	markHome.init();
})