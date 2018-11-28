/**
 * 特殊账单
 */
$(function() {
	
	$('.table tbody tr').live('mouseover', function() {
        var _this = $(this),
        reason = $('.ec_acc_tip_1', _this).val(),
        word = $('.ec_acc_tip_2', _this).val(),
        name = $('.ec_acc_tip_3', _this).val(),
        tel = $('.ec_acc_tip_4', _this).val(),
        date = $('.ec_acc_tip_5', _this).val();
        //console.log($(this).attr("id"));
		if($(this).attr("id") != "addufcj"){
			
			_this.find('.qsn').html('<i></i>' + reason);
			_this.find('.house').html('<i></i>' + word);
			_this.find('.people').html('<i></i>' + name);
			_this.find('.moble').html('<i></i>' + tel);
			_this.find('.cate').html('<i></i>' + date);
            _this.find('.ec_acc_tip').css({
                display: 'block',
                top: _this.position().top 
            });
            
            if (_this.find('.qsn').html() == '') {
            	_this.find('.ec_acc_tip').hide()
            	
            }
            _this.addClass('tr_hover');
		}
    });
    $('.table tbody tr').live('mouseout', function() {
    	var _this = $(this);
    	if($(this).attr("id") != "addufcj"){
    		_this.find('.qsn').html('');
            _this.find('.house').html('');
            _this.find('.people').html('');
            _this.find('.moble').html('');
            _this.find('.cate').html('');
            $('.ec_acc_tip').css({
                display: 'none'
            })
            if ($('#reason_icon').html() == '') {
            	_this.find('.ec_acc_tip').hide()
            	
            }
            $(this).removeClass('tr_hover');
    	}
    });
    
    $('.ec_acc_tip').hover(function() {
        $(this).css('display', 'block');
        if ($('#reason_icon').html() == '') {
        	$('.ec_acc_tip').hide()
        	
        }
    },function() {
    	if ($('#reason_icon').html() == '') {
        	$('.ec_acc_tip').hide()
        	
        }
    });
    
    $('.table tr').live('mouseover', function() {
        $(this).addClass('tr_hover');
    });
    $('.table tr').live('mouseout', function() {
        $(this).removeClass('tr_hover');
    });
	
	var specialBills = (function() {
		var textDefault = function() {
			if($.trim($('#textarea_search').text()) != null && $.trim($('#textarea_search').text()) != ''){
				
			}else{
				$('#textarea_search').defaultTxt('请输入已调整的运单号!');
			}
		};
		
		var numResult = true;
		var mailNo = '';
		// 验证运单方法
		var check = {
			num: function(numVal) {
				var logisticsIds = $("#textarea_search").val();
				if (logisticsIds == "请输入已调整的运单号!"){
					$("#textarea_search").val('');
					
					/*$("#textarea_search").focus();*/
				}
				// console.log($("#textarea_search").val());
				mailNo = $("#textarea_search").val();
				if(mailNo != '' && mailNo != null){
					if (dataType.isShipNum(mailNo)) {		// 如果填的是运单
						/*showIcon.correct(els.numTip, '');*/
						numResult = true;
					}else {												// 如果填的不是运单
						/*showIcon.error(els.numTip, tipsMsg.numFormatErr);*/
						alert('运单号格式有错误，请修改！');
						numResult = false;
					}
				}else{
					/*alert('运单号不能为空!');
					numResult = false;*/
					numResult = true;
				}
			}
		};
		
		//日期
		$('#d4311').focus(function(){
			WdatePicker({
				isShowClear:false,
				startDate: '#F{$dp.$D(\'d4312\')}',
				doubleCalendar: true,		// 双月历
				readOnly:true
			})
		});
		$('#d4312').focus(function(){
			WdatePicker({
				isShowClear:false,
				doubleCalendar: true,		// 双月历
				startDate: '#F{$dp.$D(\'d4311\')}',
				minDate: '#F{$dp.$D(\'d4311\')}',	// 终止日期大于起始日期
				maxDate: '%y-%M-%d',			// 最大时间：系统当前
				readOnly:true
				})
		});
		
		//日期
		$('#d4313').focus(function(){
			WdatePicker({
				isShowClear:false,
				startDate: '#F{$dp.$D(\'d4314\')}',
				doubleCalendar: true,		// 双月历
				readOnly:true
			})
		});
		$('#d4314').focus(function(){
			WdatePicker({
				isShowClear:false,
				doubleCalendar: true,		// 双月历
				startDate: '#F{$dp.$D(\'d4313\')}',
				minDate: '#F{$dp.$D(\'d4313\')}',	// 终止日期大于起始日期
				maxDate: '%y-%M-%d',			// 最大时间：系统当前
				readOnly:true
				})
		});
		
		
		//点击“查询”
		$('#sear_btn').click(function(ev){
			 ev.preventDefault();
			 
            /* var mailNo = $("#textarea_search").val();
             if(mailNo != '' && mailNo != null){*/
			 
            	 check.num(mailNo);
            	 
            /* }*/
             /*else{
            	 alert('运单号不能为空!');
            	 $("#textarea_search").focus();
             }*/
				
             var starttime = $("#d4311").val();
             var endtime = $("#d4312").val();
             var sd = starttime.split("-"), ed = endtime.split("-");
             var x = Date.parse(ed[1]+"/"+ed[2]+"/"+ed[0]) - Date.parse(sd[1]+"/"+sd[2]+"/"+sd[0]), // 时间差，毫秒; 参数格式为 月/日/年
             maxX = 1000 * 60 * 60 * 24 * 31;	// 最大时间差值：30天;
             if(starttime == "" || endtime == ""){
                 alert("请选择时间段!");
             }else{
                 if(starttime > endtime)
                     alert("起点时间应在止点时间之前!");
                 else if(x > maxX)
                     alert("请查询30天之内的数据");
                 else{
                	 if (numResult){
                		 $('#sear_form').trigger('submit');
                	 }
                 }
             }
		});
		
		//翻页
		pagination.click(function(ev){
			ev.preventDefault();
			$('#currentPage').val($(this).attr("value"));
			var mailNo = $("#textarea_search").val();
			 if(mailNo != '' && mailNo != null){
           	 check.num(mailNo);
            }else{
           	 alert('运单号不能为空!');
           	 $("#textarea_search").focus();
            }
				
            var starttime = $("#d4311").val();
            var endtime = $("#d4312").val();
            var sd = starttime.split("-"), ed = endtime.split("-");
            var x = Date.parse(ed[1]+"/"+ed[2]+"/"+ed[0]) - Date.parse(sd[1]+"/"+sd[2]+"/"+sd[0]), // 时间差，毫秒; 参数格式为 月/日/年
            maxX = 1000 * 60 * 60 * 24 * 31;	// 最大时间差值：30天;
            if(starttime == "" || endtime == ""){
                alert("请选择时间段!");
            }else{
                if(starttime > endtime)
                    alert("起点时间应在止点时间之前!");
                else if(x > maxX)
                    alert("请查询30天之内的数据");
                else{
               	 if (numResult){
               		setTimeout(function(){
               			$('#sear_form').trigger('submit');
        			},0);
               	 	}
                }
            }
		});

		return {
			init: function() {
				textDefault();
				//shipNumDetail();
			}
		}
		
	})();
	
	specialBills.init();
})

/*//显示略掉的信息
function Show(divid) {
	if($("#"+divid).text()!="")
		document.getElementById(divid).style.visibility = "visible";
}

//隐藏
function Hide(divid) {
	document.getElementById(divid).style.visibility = "hidden";
}*/