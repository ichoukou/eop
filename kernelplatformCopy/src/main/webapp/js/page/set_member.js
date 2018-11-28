$(function() {
	var setMember = (function() {
		
		// 元素集合
		var els = {
			setForm: $('#set_fm'),
			setDiv: $('#set_fm .set'),
			saveBtn: $('#set_fm .opts .save'),
			resetBtn: $('#set_fm .opts .reset')
		};
		
		// 表单
		var form = function() {

			var msg = {
					greateThan: "本级别设置的值必须大于之前设置过的值"
				},
				errClass = "yto_onError",
				allowChrs = [8,13,37,39,46,48,49,50,51,52,53,54,55,56,57,96,97,98,99,100,101,102,103,104,105,190];

			// 保存会员等级设置
			els.saveBtn.click(function(ev){
				ev.preventDefault();

				els.setDiv.find('input').each(function(){
					$(this).trigger("change");
				});

				if(els.setDiv.find('span[class^="err"]:visible').length === 0){
					els.setForm.trigger("submit");
				}
			});

			// 重置会员等级设置
			els.resetBtn.click(function(ev){
				ev.preventDefault();

				els.setDiv.find('input').val('');
				els.setDiv.find('.error').removeClass('error');
				els.setDiv.find('.' + errClass).removeClass(errClass).hide();
			});

			// 设置验证
			els.setDiv.find('p').each(function(){
				var _this = $(this);
				_this.find("input:eq(0)").bind("keydown", onlyFloat).bind("change", inputCheck);
				_this.find("input:eq(1)").bind("keydown", onlyInteger).bind("change", inputCheck);

			});

			// 过滤正整数
			function onlyInteger(ev){
				if( (ev.shiftKey) || ($.inArray(ev.which, allowChrs) === -1) ) {
					ev.preventDefault();
				}
			}

			// 过滤正实数
			function onlyFloat(ev){
				var reg = /\./g;
				if( (ev.shiftKey) || ($.inArray(ev.which, allowChrs) === -1) ){
					ev.preventDefault();
				}
				if(reg.test($(this).val()) && ev.which === 190){
					ev.preventDefault();
				}
			}

			// 设置值大小验证
			function inputCheck(ev){
				var _this = $(this),
				    _val = $.trim(_this.val()),
					_cIndex = _this.closest('p').find('input').index(_this),
					_rIndex = $('p', els.setDiv).index(_this.closest('p')),
					_prev = 0;

				if(_this.closest('p').prev('p').find('input').length){

					$('p', els.setDiv).each(function(i, item){
						
						if(i === _rIndex){
							return false;
						}

						var _pVal = $.trim($(item).find('input:eq(' + _cIndex + ')').val());
						if(i === 0 || _pVal === '0' || _pVal === ''){
							return;
						}
						_prev = _pVal;
					});

					_prev = (_prev === 0) ? -1 : _prev;

					if(parseFloat(_val) === 0 || _val === '' || _val === '.' || parseFloat(_prev) < parseFloat(_val)){
						_this.parent().removeClass('error');
						if(_this.closest('p').find('.error').length === 0){
							_this.closest('p').find('span[class^="err"]').removeClass(errClass).hide();
						}
					} else {
						_this.parent().addClass('error');
						_this.closest('p').find('span[class^="err"]').text(msg.greateThan).addClass(errClass).css("display","inline-block");
					}
				}

				if(_this.closest('p').next('p').length){
					_this.closest('p').next('p').find('input:eq(' + _cIndex + ')').trigger("change");
				}

				formateValue();
			}

			// 格式化
			function formateValue(){
				els.setForm.find('p').each(function(){
					var _textbox1 = $(this).find('input:eq(0)');
					if(_textbox1.val() !== ''){
						_textbox1.val(isNaN(_textbox1.val()) ? '0' : _textbox1.val());
						_textbox1.val(parseFloat(_textbox1.val(), 10).toFixed(2));
					}

					var _textbox2 = $(this).find('input:eq(1)');
					if(_textbox2.val() !== ''){
						_textbox2.val(parseInt(_textbox2.val(), 10));
					}
				});
			}
			//保存
			
		};
		
		return {
			init: function() {
				form();
			}
		}
	})();
	
	setMember.init();
})