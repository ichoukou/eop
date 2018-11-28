$(function() {
	var winParams = window.params || {};
	
	var config = {
		dialogSubmit: winParams.dialogSubmit || '',
		tabIndex: winParams.tabIndex || 0,
		onStep : winParams.onStep
	};
	var problem_war = (function() {	
		var timer;
			$('#save_setting').click(function(){
				flag = false;
				var selectOption;//那个值被选中
				var selectByName;//对应的是那个
				var previous_value ;//数据库中的值
				var warn_id;//数据库中的预警值id
				var param='';
				var paramInsert='';//有0变成其他的值的    这个是需要插入的
				var paramUpdate='';//变成其他值的(不是0)，这个需要更新的
				var paramDelete='';//有其他值变成0的这个时候需要删除
				for(var i = 0;i< 31;i++){
					previous_value = $('#previous_value_'+i).val();
					selectOption = $('select[id=warnvalue_'+i+']').val();
				    selectByName = $('select[id=warnvalue_'+i+']').attr('name');
				    warn_id = $('#warn_id_'+i).val();
				    //有暂不设置，到有值，插入数据
				    if(previous_value == '0'){
				    	if(previous_value != selectOption){
				    		paramInsert += selectByName+','+selectOption+';';
				    	}
				    }else if(previous_value !='0' && previous_value != selectOption){//这个时候有可能是更新，有可能是删除，前提是指是需要变化的
				    	if(selectOption == '0'){ //有有值变成暂不设置
				    		paramDelete += selectByName+','+selectOption+','+ warn_id+';';
				    	}else{
				    		paramUpdate += selectByName+','+selectOption+','+warn_id+';';;
				    	}
				    }
				}
		        var location_win = 'passManage_saveWarnValue.action?menuFlag=chajian_passManage_warn';
		        if(paramInsert != '' && paramInsert != null){
		        	location_win += '&paramInsert='+paramInsert
		        }
		        if(paramUpdate != '' && paramUpdate != null){
		        	location_win += '&paramUpdate='+paramUpdate;
		        }
		        if(paramDelete != '' && paramDelete != null){
		        	location_win += '&paramDelete='+paramDelete
		        }
		        
		        if(timer){
		        	clearTimeout(timer);
		        }
		        timer = setTimeout(function(){
		        	window.location=location_win;
		        },1000);
			});
		
		
		$('#save_back').click(function(){
			 window.location='passManage_warnBack.action?menuFlag=chajian_passManage_warn?currentPage=1';
			 //history.go(-1);
		});
		
	})();
})

