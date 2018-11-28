var pagination = $(".pagenavi a");
$(function() {
	var $t = window, $t0 = $t.opener, $tlds, $tvtx;
	var dialog =  new Dialog();
	
	//全选
	$('.synCheckbox').click(function(ev){
		var obj = $(this);
		var _thd = $("#tbTB_TBD"), _thd_ckb_en = $(_thd).find(":checkbox:enabled");
		$(_thd_ckb_en).each(function() {
			if ($(this).prop("disabled")==false) {
				var _ck = $(this).is(":checked");
				if(_ck != obj.is(':checked')) {
					$(this).click();
				}
			}
		});
	});
	
	//翻页
	pagination.live('click',function(ev){
		$("#currentPage").val($(this).attr("value"));
		var nv = $("#nameText").val();
		$("#nameText").val($.trim(nv));	// 去头尾空格
		setTimeout(function(){
			$("#userFrom").submit();
		},0);
	});
	
	//查询按钮
	$('#search_btn').live('click',function(ev){
		$("#currentPage").val('1');
		var nv = $("#nameText").val();
		$("#nameText").val($.trim(nv));	// 去头尾空格
		$("#userFrom").submit();
	});
	

	//确定按钮
	$('#subWinOK').click(function(){
		var c = $("td :checkbox:checked"), cs = c.size();
		if(cs < 1) {
			dialog.init({
        		contentHtml: '请选择用户!',
        		yes: function() {
        			dialog.close();
        		},
        		yesVal: '确定'
        	})
			
			return false;
		}
		
		var temp1 = $tlds.split(";"), temp2 = $tvtx.split(";"), x="", j="";
		$.each(temp1, function(i ,v) {
			if(v != "") x += ";" + v;
		});
		$.each(temp2, function(i ,v) {
			if(v != "") j += ";" + v;
		});
		
		$t0.$setVipInfo(x.substring(1), j.substring(1) );
		$t.close();
	});
	
	
	var _tbd_ckb = $("#tbTB_TBD :checkbox"), _uid, _utx;
	$tlds = $t0.$("#vipIds").val(),
	$tlds_temp = $t0.$("#vipIds_temp").val(),
	$tvtx = $t0.$(".vipText").val().replace('请双击选择使用该模板的用户',''),
	$tlds = $tlds == "" ? "" : ";" + $tlds + ";", 
	$tvtx = $tvtx == "" ? "" : ";" + $tvtx + ";";
	
	if($tlds_temp) {
		var arr1ds = $tlds_temp.split(";"), i;
		$(_tbd_ckb).each(function() {
			_uid = $(this).attr("userId");
			for(i in arr1ds) {
				if(arr1ds[i] == _uid)  $(this).removeAttr("disabled").attr('title','');
			}
		});
	}
	if($tlds) {
		var arr1ds = $tlds.split(";"), i;
		$(_tbd_ckb).each(function() {
			_uid = $(this).attr("userId");
			for(i in arr1ds) {
				if(arr1ds[i] == _uid)  $(this).attr("checked", "true").removeAttr("disabled").attr('title','');
			}
		});
	}
	
	_tbd_ckb_en = $("#tbTB_TBD :checkbox:enabled");
	$(_tbd_ckb_en).click(function() {
		if(!this.disabled) {
			_uid = ";" + $(this).attr("userId") + ";";
			_utx = ";" + $(this).attr("text") + ";";
			if($tlds.indexOf(_uid) > -1) {
				$tlds = $tlds.replace(_uid, ";");
				$tvtx = $tvtx.replace(_utx, ";");
			}
			else {
				$tlds += _uid;
				$tvtx += _utx;
			}
		}
	})
	
	$("#tbTB_TBD tr")
		.mouseover(function() {$(this).css("background-color", "#ECF0F2");})
		.mouseout(function() {$(this).css("background-color", "#FFFFFF");});
	
	var _$_ = $(_tbd_ckb_en).size(),
		$_$ = $("#tbTB_TBD :checkbox:enabled:checked").size();
	if(_$_==$_$ && $_$!=0) $("#thd_ckb").attr("checked",true);
})