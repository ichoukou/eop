$(function() {
	//parent.reinitIframe();
    /*var iframe = parent.$("#main");
    $("#main").height(iframe.height());
    $(".text_input_num").each(function(){$(this).numberbox({precision:2}).blur();});*/
    var db = document.body, dd = document.documentElement, $this;
    var errDialog = new Dialog();
    
    // 验证运费信息(非空、格式)
    _postInfoValidate = function() {
        var regNum = /^-?(?:\d+|\\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/;
        //  验证运费信息
        var _weightValidate = function(o) {
            var flag = true;
            $(o).each(function() {
                var fwp = $.trim( $(o).find("input[name='fwp']").val() );
                var owp = $.trim( $(o).find("input[name='owp']").val() );
                if(!regNum.test(fwp)) {
                	errDialog.init({
                		contentHtml: '格式不对! 请输入正确的数字!',
                		yes: function() {
                			errDialog.close();
                		},
                		yesVal: '确定'
                	})
                    $(o).find("input[name='fwp']").select();
                    flag = false;
                    return false;
                }
                if(fwp.length > 7) {
                	errDialog.init({
                		contentHtml: '格式不对! 请输入长度不超过6位的数字!',
                		yes: function() {
                			errDialog.close();
                		},
                		yesVal: '确定'
                	})
                    $(o).find("input[name='fwp']").select();
                    flag = false;
                    return false;
                }
			
                if(!regNum.test(owp)) {
                	errDialog.init({
                		contentHtml: '格式不对! 请输入正确的数字!',
                		yes: function() {
                			errDialog.close();
                		},
                		yesVal: '确定'
                	})
                    $(o).find("input[name='owp']").select();
                    flag = false;
                    return false;
                }
                if(fwp.length > 7) {
                	errDialog.init({
                		contentHtml: '格式不对! 请输入长度不超过6位的数字!',
                		yes: function() {
                			errDialog.close();
                		},
                		yesVal: '确定'
                	})
                    $(o).find("input[name='owp']").select();
                    flag = false;
                    return false;
                }

                flag = true;
            });
            return flag;
        }; // each
		
        if(_weightValidate($("#postinfo_other") ) ) {
            var flag = true;
            // 循环验证其他运费信息
            $(".postinfo_details:visible").each(function() {
                flag = _weightValidate(this);
                if(!flag) return false;
            });
            return flag;
        } else {
            return false;
        }
    }; // _postInfoValidate
	
    
    // 显示/隐藏背景层
    _swapBgDiv = function() {
        var bgDisp = $("#bg").css("display");
        if(bgDisp == "none") {
            $("#bg").css("display", "block");
            var h = db.offsetHeight > dd.offsetHeight ? db.offsetHeight : dd.offsetHeight;
            $("#bg").css("height", h + "px");
        } else {
            $("#bg").css("display", "none");
        }
    }; // _swapBgDiv
    
 // 打开/关闭 地区选择层
    //	@param _this 如果是编辑当前地区传this; 新增传任意非空字符; 否则可以不传
    _swapCityDiv = function(_this) {
        var _ = $("#p_city"), cityDisp = $(_).css("display");
        if(cityDisp == "none") {
            $(_).css("display", "block");
            $(_).css("left", ($("#bg").get(0).offsetWidth - $(_).get(0).offsetWidth) / 2 + "px");
            $(_).css("top", db.scrollTop + 200 + "px");
            $("#p_orther_state").html($("#p_orther_state").attr("open"));

            $(_).find(":checkbox").each(function() {
                $(this).removeAttr("checked").removeAttr("disabled");
            });
        } else {
            $(_).css("display", "none");
            $("#p_orther_state").html($("#p_orther_state").attr("close"));
        }
	
        // 根据_this中的地区选中对应的checkbox
        // 根据除_this外的地区禁用对应的checkbox
        if(_this) {
            var city = $(_this).html(), cityArr = Boolean(city) ? city.split(",") : "";
            var otherCityArr = "";
            $("div[name='prov']").each(function() {
                var _otherCity = $(this).html();
                if(city != _otherCity) otherCityArr += "," + _otherCity;
            });
            otherCityArr = otherCityArr.split(",");
		
            // 循环地区checkbox；如果checkbox的pid在当前编辑地区数组(cityArr)里，就选中；
            // 	如果在其他地区数组(otherPidArr)里就禁用
            $(_).find(":checkbox").each(function() {
                var c_city = $(this).val();
                if($.inArray(c_city, cityArr) > -1) {
                    $(this).attr("checked", "checked");
                }
                if($.inArray(c_city, otherCityArr) > -1) {
                    $(this).attr("disabled", "disabled");
                }
            });
		
            if(city) {
                // 如果编辑当前地区; 就设置model=edit
                //	同时把当前对象(_this)保存在全局变量中; 当保存重新选择的地区的时候, 更改_this的值
                $("#model").attr("model", "edit");
                $this = _this;
            }
        } // if(_this)
    }; // _swapCityDiv
	
    // 编辑当前地区
    $(".p_edit").click(function() {
        var _this = $(this).parent().parent().find("div[name='prov']");
        _swapBgDiv();
        _swapCityDiv(_this);
    });

    // 新增运费信息		(弹出层选择地区)
    $("#p_add").click(function() {
        if(_postInfoValidate()) {
            _swapBgDiv();
            _swapCityDiv("x");
        }
    });

    // 地区选择 - 取消 	(隐藏层)
    $("#p_city_cancl").click(function() {
        if($this) $this = null;	// $this保存的是当前编辑的input对象;
        _swapBgDiv();
        _swapCityDiv();
    });

    // 地区选择 - 确定
    //   隐藏层; 克隆数据模板; 拼接地区数据; 添加数据; 插入新克隆的模板
    $("#p_city_ok").click(function() {
	
        var _city = _pid = "";
        $("#p_city").find("input:checked").each(function() {
            _city += "," + this.value;
            _pid  += "," + $(this).attr("pid");
        });
        _city = _city.length > 0 ? _city.substring(1) : _city;
        _pid = _pid.length > 0 ? _pid.substring(1) : _pid;
	
        if(_pid.length < 1 ) { 
        	errDialog.init({
        		contentHtml: '请选择地区!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
        	return false; }
	
        // 编辑
        if($("#model").attr("model") == "edit" && $this) {
            $($this).html(_city).attr("title", "当前地区："+_city).attr("pid", _pid);
            $this = null;	// $this保存的是当前编辑的对象;
        }
        // 新增
        else {
            // 克隆数据模板
            var _newPostinfo = $(".postinfo_details:hidden").clone(true).css("display", "table-row");
            $(_newPostinfo).find("div[name='prov']").html(_city).attr("title", "当前地区："+_city).attr("pid", _pid);
            $("#postinfo_details_flag").before(_newPostinfo);
        }
	
        _swapBgDiv();
        _swapCityDiv();
    }); // $("#p_city_ok").click

    // 删除
    $(".postinfo_details").find(".p_del").click(function() { $(this).parent().parent().replaceWith(""); });

    // 保存
    //	将地区信息转成xml数据保存到隐藏域
    //	触发form的submit事件
    $("#f_save个yto").click(function() {
        var ptName = $.trim($("#ptName").val())
        if(ptName == "") {
        	errDialog.init({
        		contentHtml: '请填写模板名称!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#ptName").focus();
            return false;
        }
        if(ptName.length > 20) {
        	errDialog.init({
        		contentHtml: '模板名称长度不能大于20个字符!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#ptName").select();
            return false;
        }
		
        // 用户非空验证
        var vipids = $("#vipIds").val();
        if(vipids == "") {
        	errDialog.init({
        		contentHtml: '请选择运费模板关联的用户!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#vipIds").focus();
            return false;
        }
		
        // 运费信息验证
        if(!_postInfoValidate()) {
            return false;
        }
	
        var _remark = $.trim($("#remark").val() );
        if(_remark.length > 200) {
        	errDialog.init({
        		contentHtml: '备注不能超过200个字符!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#remark").select();
            return false;
        }
	
        var _x = "";
        var postinfo_temp = ";$f#$o#$p";
	
        // 拼接其他地区(默认运费则为其他地区)
        var o = $("#postinfo_other");
        _x += postinfo_temp.replace("$f", $.trim( $(o).find("input[name='fwp']").val() ) )
        .replace("$o", $.trim( $(o).find("input[name='owp']").val() ) )
        .replace("$p", "other");

        // 拼接地区/运费	prov fwp owp
        $(".postinfo_details:visible").each(function(i, o) {
            _x += postinfo_temp.replace("$f", $.trim( $(o).find("input[name='fwp']").val() ) )
            .replace("$o", $.trim( $(o).find("input[name='owp']").val() ) )
            .replace("$p", $(o).find("div[name='prov']").html() );
        });
	
        $("#postinfo").val(_x.length > 0 ? _x.substring(1) : _x);
	
        $("#p_form").submit();
    }); // $("#f_save").click

    // 选择用户
    $("#p_checkUser").click(function() {
        var f = "left=300,top=150,height=590,width=465,titlebar=no,toolbar=no,"
            +"menubar=no,location=no,directories=no,channelmode=no";
        window.open("user!listModelWindow.action", '', f);
    });

    // 清空
    $("#p_checkUser_clear").click(function() { $("#vipIds").val(""); $(".vipText").val(""); });

    // 设置用户信息
    //	vipIds和vipText都是";"连接
    $setVipInfo = function(vipIds, vipText) { $("#vipIds").val(vipIds); $(".vipText").val(vipText); }

    // 控制地区的样式(隔行变色)
    $("#selectSub > div").each(function(i, n) {
        $(this).css({"padding": "2px", "margin": "3px"} );
        if(i%2 != 0) {$(this).css("background-color", "#EFFAFF");}
    });


    // 保存
    //	将地区信息转成xml数据保存到隐藏域
    //	触发form的submit事件
    $("#f_save").click(function() {
        var ptName = $.trim($("#ptName").val())
        if(ptName == "") {
        	errDialog.init({
        		contentHtml: '请填写模板名称!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#ptName").focus();
            return false;
        }
        if(ptName.length > 20) {
        	errDialog.init({
        		contentHtml: '模板名称长度不能大于20个字符!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#ptName").select();
            return false;
        }
		
        // TODO 带完善
        // 验证运费信息
        $(".tr_sp").each(function() {
			
        });
		
        // 用户非空验证
        var vipids = $("#vipIds").val();
        if(vipids == "") {
        	errDialog.init({
        		contentHtml: '请选择运费模板关联的用户!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#vipIds").focus();
            return false;
        }
		
        var _remark = $.trim($("#remark").val() );
        if(_remark.length > 200) {
        	errDialog.init({
        		contentHtml: '备注不能超过200个字符!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $("#remark").select();
            return false;
        }
	
        var _x = ""; 
        var postinfo_temp = ";$did#$dtxt#$sid#$stxt#$fws#$fwd#$fwr#$ows#$owd#$owr";
	
        // 拼接地区/运费	prov fwp owp
        $(".tr_sp").each(function(_$_,$_$) {
            _x += postinfo_temp.replace("$did", $($_$).find(".destId").attr("value") )
            .replace("$dtxt", $.trim( $($_$).find(".destId").text() ) )
            .replace("$sid", $.trim( $($_$).find(".srcText").attr("for") ) )
            .replace("$stxt", $.trim( $($_$).find(".srcText").val() ) )
            .replace("$fws", $.trim( $($_$).find(".fws").text() ) )
            .replace("$fwd", $.trim( $($_$).find(".fwd").val() ) )
            .replace("$fwr", $.trim( $($_$).find(".fwr").val() ) )
            .replace("$ows", $.trim( $($_$).find(".fws").text() ) )
            .replace("$owd", $.trim( $($_$).find(".fwd").val() ) )
            .replace("$owr", $.trim( $($_$).find(".fwr").val() ) );
        });
	
        $("#postinfo").val(_x.length > 0 ? _x.substring(1) : _x);
        //alert(_x);
        $("#p_form").submit();
    }); // $("#f_save").click


    //$("span.srcText").html($("input.srcText").val());
    $("#main-content form :input[name!='zhongliang']").attr("readonly","readonly").css("cursor","default");

    
    var _regxNum = /^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/, 
    _s, _v, _r, _m="格式不对! 请输入正确的数字!";	
    
    
 // 计算折扣
    function ___(e,_,__) {
        //响应鼠标事件，允许左右方向键移动
        e = window.event||e;
        if (e.keyCode < 45 || e.keyCode > 57) return false;
	
        _v = $(_).val();
        if(!_regxNum.test(_v) ) {
            errDialog.init({
        		contentHtml: _m,
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $(_).select();
            return false;
        }
        if(_v > 10 || _v < 0) {
        	errDialog.init({
        		contentHtml: '折扣不能大于10或者小于0!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $(_).select();
            return false;
        }
	
        if(_v == 10) {
            _r = 0;
        }
        else {
            _s = $(_).prevAll("label.ws").html();
            _r = _v == 0 ? _s : Math.round(_s*_v*100)/1000;
        }
        $(_).nextAll(__).val(_r);
    };
    // 计算实收价
    function ____(e,_,__) {
        //响应鼠标事件，允许左右方向键移动
        e = window.event||e;
        if (e.keyCode < 45 || e.keyCode > 57) return false;
	
        _v = $(_).val();
        if(!_regxNum.test(_v) ) {
            errDialog.init({
        		contentHtml: _m,
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $(_).select();
            return false;
        }
        _s = $(_).prevAll("label.ws").html();
        if(_v > Number(_s) || _v < 0) {
        	errDialog.init({
        		contentHtml: '实收价个不能大于标准价或者小于0!',
        		yes: function() {
        			errDialog.close();
        		},
        		yesVal: '确定'
        	})
            $(_).select();
            return false;
        }
        if(_v == _s) {
            _r = 0;
        }
        else {
            _r = _v/_s*10;
            _r = _v == 0 ? 10 : Math.round(_r*100)/100;
        }
        $(_).prevAll(__).val(_r);
    };

    function clearNoNum(event,obj){
        //响应鼠标事件，允许左右方向键移动
        event = window.event||event;
        if(event.keyCode == 37 | event.keyCode == 39){
            return;
        }
	
        //先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g,"");
        //必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g,"");
        //保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g,".");
        //保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    }

    function checkNum(obj){
        //为了去除最后一个.
        obj.value = obj.value.replace(/\.$/g,"");
    }
    /* 鼠标拖动 */
    var oDrag = "";
    var ox,oy,nx,ny,dy,dx;
    function drag(e, o) {
        var e = e ? e : event;
        var mouseD = document.all ? 1 : 0;
        if(e.button == mouseD) {
            oDrag = o.parentNode;
            ox = e.clientX;
            oy = e.clientY;  
        }
    } // drag
    function dragPro(e) {
        if(oDrag != "") { 
            var e = e ? e : event;
            dx = parseInt($(oDrag).css("left"));
            dy = parseInt($(oDrag).css("top"));
            nx = e.clientX;
            ny = e.clientY;
            $(oDrag).css("left", (dx + ( nx - ox )) + "px");
            $(oDrag).css("top",  (dy + ( ny - oy )) + "px");
            ox = nx;
            oy = ny;
        }
    } // dragPro
    document.onmouseup = function() {oDrag = "";}
    document.onmousemove = function(event) {dragPro(event);}


    function jisuan(wt){
        if(wt==''){
            document.getElementById('value1').innerHTML='';
            document.getElementById('value2').innerHTML='';
            document.getElementById('value3').innerHTML='';
            return;
        }
        var a=accSubtr(wt,1);
        if(a=='NaN'){
            document.getElementById('value1').innerHTML='';
            document.getElementById('value2').innerHTML='';
            document.getElementById('value3').innerHTML='';
        }
        else if(a<=0){
            document.getElementById('value1').innerHTML=10;
            document.getElementById('value2').innerHTML=10;
            document.getElementById('value3').innerHTML=10;
        }else{
            document.getElementById('value1').innerHTML=accMul(accAdd(1,a),10);
            document.getElementById('value2').innerHTML=accMul(accAdd(1,accMul(Math.ceil(accDiv(a, 0.5)),0.5)),10);
            document.getElementById('value3').innerHTML=accMul(accAdd(1,accMul(Math.ceil(accDiv(a, 1.0)),1.0)),10);
        }
    }	

    $(document).ready(function(){
        $("#tbody tr").mouseover(function(){ //如果鼠标移到class为stripe_tb的表格的tr上时，执行函数
            //$(this).addClass("over");
            $(this).css("background","#e8f3fa");
        }).mouseout(function(){ //给这行添加class值为over，并且当鼠标一出该行时执行函数
            //$(this).removeClass("over");
            $(this).css("background","none");
        }); //移除该行的class
    });

    function showcalclate(){
		
        document.getElementById("t_area1").style.display = "none";
        document.getElementById("t_area2").style.display = "none";
        document.getElementById("t_area3").style.display = "none";
        document.getElementById("t_area4").style.display = "none";
		

	
	
        var calclateType = $("#calclateType").val();
						
        if(calclateType==1){//固定收费
            $(".tr_sp").each(function(_$_,$_$) {
                $("#firstWeight").val("");
                $("#sp_firstwidth").attr("style","display:none");
                //$("#p_module").attr("style","display:none");
                $($_$).find(".fwr").hide();				
                $($_$).find(".owr").hide();
                $($_$).find(".owe").show();
                $($_$).find(".owf").hide();
                $($_$).find(".owg").hide();
				$($_$).find(".owh").hide();
				$($_$).find(".addWeightChoice").hide();
            });
        }
						
        if(calclateType==2){//简单重量收费
            $(".tr_sp").each(function(_$_,$_$) {
                $("#sp_firstwidth").attr("style","display:none");
                //$("#p_module").attr("style","display:none");
                $($_$).find(".fwr").hide();				
                $($_$).find(".owr").hide();
                $($_$).find(".owe").hide();
                $($_$).find(".owf").show();
                $($_$).find(".owg").show();
				$($_$).find(".owh").hide();
				$($_$).find(".addWeightChoice").hide();				
            });
        }
						
        if(calclateType==3){//续重价格收费
            $(".tr_sp").each(function(_$_,$_$) {
                $("#sp_firstwidth").attr("style","display:none");
                //$("#p_module").attr("style","display:none");									
                $($_$).find(".fwr").show();				
                $($_$).find(".owr").show();
                $($_$).find(".owe").hide();
                $($_$).find(".owf").hide();
                $($_$).find(".owg").show();
				$($_$).find(".owh").show();
				$($_$).find(".addWeightChoice").hide();				
            });
        }
        if(calclateType==4){//续重统计单位收费
            $(".tr_sp").each(function(_$_,$_$) {
                $("#sp_firstwidth").attr("style","display:none");
                //$("#p_module").attr("style","display:inline");										
                $($_$).find(".fwr").show();				
                $($_$).find(".owr").show();
                $($_$).find(".owe").hide();
                $($_$).find(".owf").hide();
                $($_$).find(".owg").show();
				$($_$).find(".owh").show();
				$($_$).find(".addWeightChoice").show();				
            });
        }																								
    }

    showcalclate();
    
    
    
	//查看模板
	var lookatmb = (function() {
		//文本显示
		var textDefault = function() {
			$('#textarea_glyf').val('所有用户（all）');
			$('#textarea_mbsm').val('如果网点没有给用户分配运费模板，系统就会自动给用户分配默认运费模板。');
		};
		
		
		var posttempView = function() {
			//四种运费模板价格
			$('.t_area').live('click',function(){
				var calclateType = $("#calclateType").val();
                if(calclateType==1){//固定收费
                    $(".tr_sp").each(function(_$_,$_$) {
                        var str = $($_$).find(".owe").val();
                        if(str=="")
                            $($_$).find(".owe").val("0.00");
                    });          
                }
                
                var con = document.getElementById("t_area"+calclateType);
                con.style.display = "block";
			});
			
			//关闭运费模板价格小窗口
			$('.t_area_close').live('click',function(){
				var calclateType = $("#calclateType").val();
                var con = document.getElementById("t_area"+calclateType);
                con.style.display = "none";
			});
			
		};
	
		return {
			init: function() {
				textDefault();
				posttempView();
			}
		}
	})();
	
	lookatmb.init();
})