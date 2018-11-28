$(function() {
	
		var dialog = new Dialog();
		
		$(".text_input_num").blur(function(){
			var num = ($(this).val()*1).toFixed(2);
			if(!isNaN(num))
				$(this).val(num);
		});
		
		var db = document.body, dd = document.documentElement, $this;
		
        __popUserWindow = function() {
            var f = "left=300,top=150,height=590,width=470,titlebar=no,toolbar=no,"
                +"menubar=no,location=no,directories=no,channelmode=no";
            window.open("user!listPosttempUser.action", '', f);
        };
        // 选择用户
        $("#p_checkUser").click(function() {
            __popUserWindow();
        });
        // 选择用户
        $(".vipText").dblclick(function() {
            __popUserWindow();
        });

        // 清空
        $("#p_checkUser_clear").click(function() { 
        	dialog.init({
        		iconType: 'warn',
        		contentHtml: '确定清空? 被清空的用户将无法看到该模板! 请谨慎操作!',
        		maskOpacity: 0,
        		yes: function() {
        			dialog.close();
        			$("#vipIds").val(""); $(".vipText").val(""); 
        		},
        		no:function(){
        			dialog.close();
        		},
        		yesVal: '确定',
        		noVal: '取消'
        	})
        	
        });
	
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
            var scrollTop = window.parent.document.documentElement.scrollTop;
            if(scrollTop==0){
                scrollTop = window.parent.document.body.scrollTop;
            }
            var ptName = $.trim($("#ptName").val())
            if(ptName == "") {
            	dialog.init({
            		iconType: 'warn',
            		contentHtml: '请填写模板名称!(本提示5秒后自动关闭)',
            		maskOpacity: 0,
            		autoClose: 5000,
            		yes: function() {
            			dialog.close();
            			$("#ptName").focus();
            		},
            		yesVal: '确定'
            	})
            	setTimeout(function(){
            		$("#ptName").focus();
                },5000);
                return false;
            }
            if(ptName.length > 20) {
            	dialog.init({
            		iconType: 'warn',
            		contentHtml: '模板名称长度不能大于20个字符!(本提示5秒后自动关闭)',
            		maskOpacity: 0,
            		autoClose: 5000,
            		yes: function() {
            			dialog.close();
            			$("#ptName").select();
            		},
            		yesVal: '确定'
            	})
            	setTimeout(function(){
            		$("#ptName").select();
                },5000);
                return false;
            }
  		
            
            var calclateType = $("#calclateType").val();
            var firstWeight = $("#firstWeight").val();
            
            
            var flag = true;
            // 验证运费信息
            var _ts ;
            $(".tr_sp").each(function(_$_,$_$) {
            	//各种价格的格式check----------------------------------------------------
				
				 if(calclateType==1){	
					_v = $.trim( $($_$).find("input:eq(4)").val() );  //固定价格
					_ts = $($_$).find("input:eq(4)");
					var area = $.trim($($_$).find("p:eq(0)").text());
					if(!_regxNum.test(_v) ) {
						dialog.init({
	                		iconType: 'warn',
	                		contentHtml: area+'数字格式有误！',
	                		maskOpacity: 0,
	                		yes: function() {
	                			dialog.close();
	                			$(_ts).focus();
	                		},
	                		yesVal: '确定'
	                	})
						flag = false;
						return false;
					}	
				}			
				
				 if(calclateType==2){
					_v = $.trim( $($_$).find("input:eq(5)").val() );  //重量单价
					_ts = $($_$).find("input:eq(5)");
					var area = $.trim($($_$).find("p:eq(0)").text());
					if(!_regxNum.test(_v) ) {
						dialog.init({
	                		iconType: 'warn',
	                		contentHtml: area+'数字格式有误！',
	                		maskOpacity: 0,
	                		yes: function() {
	                			dialog.close();
	                			$(_ts).focus();
	                		},
	                		yesVal: '确定'
	                	})
						
						flag = false;
						return false;
					}	
					_v = $.trim( $($_$).find("input:eq(6)").val() ); //最低收费价格
					_ts = $($_$).find("input:eq(6)");
					if(!_regxNum.test(_v) ) {
						dialog.init({
	                		iconType: 'warn',
	                		contentHtml: area+'数字格式有误！',
	                		maskOpacity: 0,
	                		yes: function() {
	                			dialog.close();
	                			$(_ts).focus();
	                		},
	                		yesVal: '确定'
	                	})
						
						flag = false;
						return false;
					}
				}			
						
				 if(calclateType==3 || calclateType==4){

					_v = $.trim( $($_$).find("input:eq(2)").val() ); //首重价格
					_ts = $($_$).find("input:eq(2)");
					var area = $.trim($($_$).find("p:eq(0)").text());
					if(!_regxNum.test(_v) ) {
						dialog.init({
	                		iconType: 'warn',
	                		contentHtml: area+'数字格式有误！',
	                		maskOpacity: 0,
	                		yes: function() {
	                			dialog.close();
	                			$(_ts).focus();
	                		},
	                		yesVal: '确定'
	                	})
						
						flag = false;
						return false;
					}	
					_v = $.trim( $($_$).find("input:eq(3)").val() );   //续重价格
					_ts = $($_$).find("input:eq(3)");
					if(!_regxNum.test(_v) ) {
						dialog.init({
	                		iconType: 'warn',
	                		contentHtml: area+'数字格式有误！',
	                		maskOpacity: 0,
	                		yes: function() {
	                			dialog.close();
	                			$(_ts).focus();
	                		},
	                		yesVal: '确定'
	                	})
						
						flag = false;
						return false;
					}							 
				 
					_v = $.trim( $($_$).find("input:eq(6)").val() ); //最低收费价格
					_ts = $($_$).find("input:eq(6)");
					if(!_regxNum.test(_v) ) {
						dialog.init({
	                		iconType: 'warn',
	                		contentHtml: area+'数字格式有误！',
	                		maskOpacity: 0,
	                		yes: function() {
	                			dialog.close();
	                			$(_ts).focus();
	                		},
	                		yesVal: '确定'
	                	})
						
						flag = false;
						return false;
					}							 
					_v = $.trim( $($_$).find("input:eq(1)").val() ); //首重重量
					_ts = $($_$).find("input:eq(1)");
					if(!_regxNum.test(_v) ) {
						if(_v==""){
							dialog.init({
		                		iconType: 'warn',
		                		contentHtml: area+'请填写首重',
		                		maskOpacity: 0,
		                		yes: function() {
		                			dialog.close();
		                			$(_ts).focus();
		                		},
		                		yesVal: '确定'
		                	})
							
						}else{
							dialog.init({
		                		iconType: 'warn',
		                		contentHtml: area+'首重'+_m,
		                		maskOpacity: 0,
		                		yes: function() {
		                			dialog.close();
		                			$(_ts).focus();
		                		},
		                		yesVal: '确定'
		                	})
							
						}
						flag = false;
						return false;
					}	
					
					
					//续重统计单位的格式check
    				if(calclateType==4) {
    					_v = $.trim( $($_$).find(".addWeightChoice").val() );   //续重统计单位
        				_ts = $($_$).find(".addWeightChoice");
        				if(!_regxNum.test(_v) ) {
        					if(_v==""){
        						dialog.init({
        		            		iconType: 'warn',
        		            		contentHtml: '请填写续重统计单位',
        		            		yes: function() {
        		            			dialog.close();
        		            			$(_ts).focus();
        		            		},
        		            		yesVal: '确定'
        		            	})
        					}else{
        						dialog.init({
        		            		iconType: 'warn',
        		            		contentHtml: '请填写续重统计单位'+_m,
        		            		yes: function() {
        		            			dialog.close();
        		            			$(_ts).focus();
        		            		},
        		            		yesVal: '确定'
        		            	})
        					}
        					flag = false;
        					return false;
        				}
    				}
				}	
				
				//数字范围check----------------------------------------------------
	                // 需求说：值要大于0，小于100
	                if(calclateType == 1){
	                	_v = $.trim( $($_$).find("input:eq(4)").val() );      
	                    _ts = $($_$).find("input:eq(4)");
	                    var area = $.trim($($_$).find("p:eq(0)").text());
	                	if(_v <= 0 || _v >= 100) {
	                    	dialog.init({
	                    		iconType: 'warn',
	                    		contentHtml: area+'固定价格必须大于0小于100!(本提示5秒后自动关闭)',
	                    		maskOpacity: 0,
	                    		autoClose: 5000,
	                    		yes: function() {
	                    			dialog.close();
	                    			$(_ts).focus();
	                    		},
	                    		yesVal: '确定'
	                    	})
	                    	setTimeout(function(){
	                    		$(_ts).focus();
	                    	},5000);
	                    	
	                        flag = false;
	                        return false;
	                    }
	                }
	                if(calclateType == 2){
	                	_v = $.trim( $($_$).find("input:eq(6)").val() );      
	                    _ts = $($_$).find("input:eq(6)");
	                    var area = $.trim($($_$).find("p:eq(0)").text());
	                	if(_v <= 0 || _v >= 100) {
	                    	dialog.init({
	                    		iconType: 'warn',
	                    		contentHtml: area+'最低收费价格必须大于0小于100!(本提示5秒后自动关闭)',
	                    		maskOpacity: 0,
	                    		autoClose: 5000,
	                    		yes: function() {
	                    			dialog.close();
	                    			$(_ts).focus();
	                    		},
	                    		yesVal: '确定'
	                    	})
	                    	setTimeout(function(){
	                    		$(_ts).focus();
	                    	},5000);
	                    	
	                        flag = false;
	                        return false;
	                    }
	                }
	                if(calclateType == 3 || calclateType == 4){
	                	_v = $.trim( $($_$).find("input:eq(1)").val() );      
	                    _ts = $($_$).find("input:eq(1)");
	                    var area = $.trim($($_$).find("p:eq(0)").text());
	                	if(_v <= 0 || _v >= 100) {
	                    	dialog.init({
	                    		iconType: 'warn',
	                    		contentHtml: area+'首重重量必须大于0小于100!(本提示5秒后自动关闭)',
	                    		maskOpacity: 0,
	                    		autoClose: 5000,
	                    		yes: function() {
	                    			dialog.close();
	                    			$(_ts).focus();
	                    		},
	                    		yesVal: '确定'
	                    	})
	                    	setTimeout(function(){
	                    		$(_ts).focus();
	                    	},5000);
	                    	
	                        flag = false;
	                        return false;
	                    }
	                	
	                	_v = $.trim( $($_$).find("input:eq(3)").val() );      
	                    _ts = $($_$).find("input:eq(3)");
	                	if(_v <= 0 || _v >= 100) {
	                    	dialog.init({
	                    		iconType: 'warn',
	                    		contentHtml: area+'续重价格必须大于0小于100!(本提示5秒后自动关闭)',
	                    		maskOpacity: 0,
	                    		autoClose: 5000,
	                    		yes: function() {
	                    			dialog.close();
	                    			$(_ts).focus();
	                    		},
	                    		yesVal: '确定'
	                    	})
	                    	setTimeout(function(){
	                    		$(_ts).focus();
	                    	},5000);
	                    	
	                        flag = false;
	                        return false;
	                    }
	                	
	                	
	                	_v = $.trim( $($_$).find("input:eq(6)").val() );      
	                    _ts = $($_$).find("input:eq(6)");
	                	if(_v <= 0 || _v >= 100) {
	                    	dialog.init({
	                    		iconType: 'warn',
	                    		contentHtml: area+'最低收费价格必须大于0小于100!(本提示5秒后自动关闭)',
	                    		maskOpacity: 0,
	                    		autoClose: 5000,
	                    		yes: function() {
	                    			dialog.close();
	                    			$(_ts).focus();
	                    		},
	                    		yesVal: '确定'
	                    	})
	                    	setTimeout(function(){
	                    		$(_ts).focus();
	                    	},5000);
	                    	
	                        flag = false;
	                        return false;
	                    }
	                	
	                	
	                	_v = $.trim( $($_$).find(".addWeightChoice").val() );      
	                    _ts = $($_$).find(".addWeightChoice");
	                	if(_v <= 0 || _v >= 50) {
	                    	dialog.init({
	                    		iconType: 'warn',
	                    		contentHtml: area+'续重统计单位必须大于0小于50!(本提示5秒后自动关闭)',
	                    		maskOpacity: 0,
	                    		autoClose: 5000,
	                    		yes: function() {
	                    			dialog.close();
	                    			$(_ts).focus();
	                    		},
	                    		yesVal: '确定'
	                    	})
	                    	setTimeout(function(){
	                    		$(_ts).focus();
	                    	},5000);
	                    	
	                        flag = false;
	                        return false;
	                    }
	                }
                
                
            });//each
		
            if(!flag) return false;
            // 用户非空验证
            var vipids = $("#vipIds").val();
            if(vipids == "") {
            	dialog.init({
            		iconType: 'warn',
            		contentHtml: '请选择运费模板关联的用户!(本提示5秒后自动关闭)',
            		maskOpacity: 0,
            		autoClose: 5000,
            		yes: function() {
            			dialog.close();
            			$("#vipIds").focus();
            		},
            		yesVal: '确定'
            	})
            	setTimeout(function(){
            		$("#vipIds").focus();
                },5000);
            	
                return false;
            }
  		
            var _remark = $.trim($(".remark").val() );
            if(_remark.length > 200) {
            	dialog.init({
            		iconType: 'warn',
            		contentHtml: '备注不能超过200个字符!(本提示5秒后自动关闭)',
            		maskOpacity: 0,
            		autoClose: 5000,
            		yes: function() {
            			dialog.close();
            			$(".remark").select();
            		},
            		yesVal: '确定'
            	})
            	setTimeout(function(){
            		$(".remark").select();
                },5000);
            	
                return false;
            }
		
            var _x = ""; 
            var _x = "", postinfo_temp = ";$did#$dtxt#$sid#$stxt#$fws#$fwd#$fwr#$ows#$owd#$owr#$owe#$owf#$owg#$owh#$addWeightChoice";
            // 拼接地区/运费	prov fwp owp
            $(".tr_sp").each(function(_$_,$_$) {
                _x += postinfo_temp.replace("$did", $($_$).find(".destId").attr("value") )
                .replace("$dtxt", $.trim( $($_$).find(".destId").find(".dtxt").text() ) )
                .replace("$sid", $.trim( $($_$).find(".srcText").attr("id") ) )
                .replace("$stxt", $.trim( $($_$).find(".srcText").val() ) )
                .replace("$fws",  $($_$).find(".fws").val()+" " )
                .replace("$fwd",  $($_$).find(".fwd").val()+" " )
                .replace("$fwr",  $($_$).find("input:eq(2)").val()+" " )
                .replace("$ows",  $($_$).find(".ows").val()+" " )
                .replace("$owd",  $($_$).find(".owd").val()+" " )
                .replace("$owr",  $($_$).find("input:eq(3)").val()+" " )
                .replace("$owe",  $($_$).find("input:eq(4)").val()+" " )
                .replace("$owf",  $($_$).find("input:eq(5)").val()+" " )
                .replace("$owg",  $($_$).find("input:eq(6)").val()+" " )
				.replace("$owh",  $($_$).find("input:eq(1)").val()+" " )
				.replace("$addWeightChoice",  $($_$).find(".addWeightChoice").val()+" " );
            });
            $("#postinfo").val(_x.length > 0 ? _x.substring(1) : _x);
            $("#p_form").submit();
        }); // $("#f_save").click
        
        //$("span.srcText").html($("input.srcText").val());
        
        
       
        	var _regxNum = /^(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/, 
            _s, _v, _r, _m="格式不对! 请输入正确的数字!";	
        	
        	
		
            function __(event,obj){
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
		
            $('.blurCheckNum1').blur(function(ev){
            	//为了去除最后一个.
                $(this).val($(this).val().replace(/\.$/g,""));
            	
            });
            
            $('.blurCheckNum').blur(function(ev){
            	//为了去除最后一个.
                $(this).val($(this).val().replace(/\.$/g,""));
            	
            });
            
            
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
		
            // 删除
            $('#deletePosttemp').live('click',function(){
            	 // 			if(confirm("确实删除?!\r\n如果删除, 关联的用户将无法使用该模板对账!")) {
                // 				location.href='posttemp!toPosttempDel.action?posttemp.id='+id+"&url="+encodeURIComponent($("#url").val() );
                // 			}
                var dialog = new Dialog();
                var id = $('#delId').val();
                dialog.init({
                		contentHtml: '确实删除?\r\n如果删除, 关联的用户将无法使用该模板对账!',
                		yes: function() {
                			dialog.close();
                			$.ajax({
                				url:'posttemp!toPosttempDel.action?posttemp.id='+id+"&url="+encodeURIComponent($("#url").val() ),
                				dataType:'json',
                				success:function(){
                					window.location.href = "posttemp!toPosttemp.action?currentPage=1&menuFlag=caiwu_posttemp";
                				}
                			});
                			
                			
                		},
                		no: function(){
                			dialog.close();
                		},
                		yesVal: '确定',
                		noVal:'取消'
                	})
            });

            
          //续费统计单位的选择
        	/*$('.addWeightChoice').live('click',function(){
            	var choice = $(this).val();
            	$(this).find('option[value=""]').text("手动输入");
            	//$('.addWeightChoice option[value=""]').text("手动输入");
        		if(choice == ''){
        			choice = '手动输入';
        		}
        		$(this).find(":contains('"+choice+"')").attr("selected","selected");
            				 
            });
        	
        	$('.addWeightChoice').live('change',function(){
            	var choice = $(this).val();
            	$(this).empty();
            	
            	var content = '<option value="0.01">0.01</option>' +
            				  '<option value="0.5">0.5</option>' +
            				  '<option value="1">1</option>' +
            				  '<option value=""></option>';
            	$(this).append(content);
            	$('.addWeightChoice option[value=""]').text("");
            	if(choice == '手动输入'){
        			choice = '';
        		}
            	$(this).find(":contains('"+choice+"')").attr("selected","selected");
            		
            });
        	
        	$('.addWeightChoice').live('blur',function(){ 
        		var choice = $(this).val();
        		if(choice == ''){
        			$('.addWeightChoice option[value=""]').text("");
        		}
        	});*/
            
            
          //计费说明
            $('.t_area').click(function(){
            	var calclateType = $("#calclateType").val();
                
                if(calclateType=='1'){//固定收费
                    $(".tr_sp").each(function(_$_, $_$) {
                        var str = $($_$).find(".owe").val();
                        if(str=="") {
							$($_$).find(".owe").val("0.00");
						}
                            
                    });          
                }
                var content = '';
                if(calclateType == '1'){
                	content = 	'<div class="table">'+
								'<table>'+
								'	 <thead>'+
								'		<tr>'+
								'			<th colspan="3"><h4>固定价格规则计费说明：</h4><p>固定价格：5元</p></th>'+
								'		</tr>'+
								'	 </thead>'+
								'	 <tbody>'+
								'		<tr>'+
								'			<td width="65">重量（kg）</td><td>实际收费计算公式</td><td>实际收费（元）</td>'+
								'		</tr>'+
								'		<tr>'+
								'			<td>0.2</td><td>实际收费 = 固定价格</td><td>5.00</td>'+
								'		</tr>'+
								'		<tr>'+
								'			<td>1.6</td><td>实际收费 = 固定价格</td><td>5.00</td>'+
								'		</tr>'+
								'		<tr>'+
								'			<td>3.0</td><td>实际收费 = 固定价格</td><td>5.00</td>'+
								'		</tr>'+
								'		<tr>'+
								'			<td colspan=3>输入重量：<input id=type1 class=input_text type=text>&nbsp;kg&nbsp;&nbsp;&nbsp;<a class="btn btn_a" onclick=calculateFreight(1) value=计算运费><span>&nbsp;计算运费&nbsp;</span></a>&nbsp;&nbsp;&nbsp;应收运费：<label id=type1_></label></td>'+
								'		</tr>'+
								'	</tbody>'+
								'</table>';
								
                }
                if(calclateType == '2'){
                	content = '<div class="table">'+
			                    '<table>'+
									'<thead>'+
										'<tr>'+
											'<th colspan="3">'+
												'<h4>简单重量规则计费说明：</h4>'+
												'<p>重量单价：8元 &nbsp;&nbsp;&nbsp;&nbsp;最低收费：10元</p>'+
											'</th>'+
										'</tr>'+
									'</thead>'+
									'<tbody>'+
										'<tr>'+
											'<td width="65">重量（kg）</td>'+
											'<td>实际收费计算公式</td>'+
											'<td>实际收费（元）</td>'+
										'</tr>'+
										'<tr>'+
											'<td>0.2</td>'+
											'<td>实际收费 = 最低收费</td>'+
											'<td>10.00</td>'+
										'</tr>'+
										'<tr>'+
											'<td>1.6</td>'+
											'<td>实际收费 = 重量单价 * 重量</td>'+
											'<td>1.6 * 8 = 12.8</td>'+
										'</tr>'+
										'<tr>'+
											'<td>3.0</td>'+
											'<td>实际收费 = 重量单价 * 重量</td>'+
											'<td>3 * 8 = 24</td>'+
										'</tr>'+
										'<tr>'+
										'	<td colspan=3>输入重量：<input id=type2 class=input_text type=text>&nbsp;kg&nbsp;&nbsp;&nbsp;<a class="btn btn_a" onclick=calculateFreight(2) value=计算运费><span>&nbsp;计算运费&nbsp;</span></a>&nbsp;&nbsp;&nbsp;应收运费：<label id=type2_></label></td>'+
										'</tr>'+
									'</tbody>'+
			                    '</table>'+
			                  '</div>';
                
				}
                if(calclateType == '3'){
					content = '<div class="table">'+
				                    '<table>'+
										'<thead>'+
											'<tr>'+
												'<th colspan="3">'+
													'<h4>续重价格规则计费说明：</h4>'+
													'<p>首重重量：0.5kg&nbsp;&nbsp;首重价格：10元&nbsp;&nbsp;最低收费价格：12元&nbsp;&nbsp;续重价格：5元/kg</p>'+
												'</th>'+
											'</tr>'+
										'</thead>'+
										'<tbody>'+
											'<tr>'+
												'<td width="65">重量（kg）</td>'+
												'<td>实际收费计算公式</td>'+
												'<td>实际收费（元）</td>'+
											'</tr>'+
											'<tr>'+
												'<td>0.2</td>'+
												'<td>实际收费 = 最低收费</td>'+
												'<td>12.00</td>'+
											'</tr>'+
											'<tr>'+
												'<td>1.6</td>'+
												'<td>实际收费 = 首费 + (重量 - 首重) * 续重重量单价</td>'+
												'<td>10 + (1.6-0.5) * 5 = 15.5</td>'+
											'</tr>'+
											'<tr>'+
												'<td>3.0</td>'+
												'<td>实际收费 = 首费 + (重量 - 首重) * 续重重量单价</td>'+
												'<td>10 + (3-0.5) * 5 = 5 + 12.5 = 22.5</td>'+
											'</tr>'+
											'<tr>'+
											'	<td colspan=3>输入重量：<input id=type3 class=input_text type=text>&nbsp;kg&nbsp;&nbsp;&nbsp;<a class="btn btn_a" onclick=calculateFreight(3) value=计算运费><span>&nbsp;计算运费&nbsp;</span></a>&nbsp;&nbsp;&nbsp;应收运费：<label id=type3_></label></td>'+
											'</tr>'+
										'</tbody>'+
				                    '</table>'+
				                '</div>';
                
				}
                if(calclateType == '4'){
					content = '<div class="table">'+
				                    '<table>'+
				                        '<thead>'+
				                            '<tr>'+
				                                '<th colspan="3">'+
				                                    '<h4>续重统计单位规则计费说明：</h4>'+
				                                    '<p>首重重量:0.5kg&nbsp;&nbsp;首重价格:10元&nbsp;&nbsp;续重单价:5元/kg&nbsp;&nbsp;最低收费价格:12元'+
				                                    '</p>'+										
												'</th>'+
				                            '</tr>'+
				                        '</thead>'+
				                        '<tbody>'+
				                            '<tr>'+
				                                '<td width="65">重量（kg）</td>'+
				                                '<td>实际收费计算公式</td>'+
				                                '<td>实际收费（元）</td>'+
				                            '</tr>'+
				                            '<tr>'+
				                                '<td>0.5以下</td>'+
				                                '<td>实际收费 = 最低收费价格</td>'+
				                                '<td>12.00</td>'+
				                            '</tr>'+
				                            '<tr>'+
				                                '<td>0.5以上</td>'+
				                                '<td> 续重费用=续重重量*续重价格<br>'+
				'续重重量为实际重量减去首重重量，续重重量不足0.5kg，按0.5kg算。<br><font style="color: #FF0000">例：续重重量为1.6kg时按2.0kg计算。续重重量为1.1kg时按1.5kg计算</font> </td>'+
				                                '<td><p><span style="color: #FF0000">例1：</span><br>'+
				                                        '实际重量1.6kg<br>'+
				                                        '实际收费:10+7.5=17.5<br>'+
				                                        '<span style="color: #FF0000">例2：</span><br>'+
				                                        '实际重量：3.0kg<br>'+
				                                        '实际收费:10+12.5=22.5</p>'+
				                                '</td>'+
				                            '</tr>'+
				                            '<tr>'+
											'	<td colspan=3>输入重量：<input id=type4 class=input_text type=text>&nbsp;kg&nbsp;&nbsp;&nbsp;<a class="btn btn_a" onclick=calculateFreight(4) value=计算运费><span>&nbsp;计算运费&nbsp;</span></a>&nbsp;&nbsp;&nbsp;应收运费：<label id=type4_></label></td>'+
											'</tr>'+
				                        '</tbody>'+
				                    '</table>'+
				                '</div>';
				}
                
                var msDialog = new Dialog();
                msDialog.init({
                //	closeBtn: true,
        		//	maskOpacity: 0.2,			// 遮罩层的透明度
                	contentHtml:content,
                	noVal: '关闭',
                	no: function(){
                		msDialog.close();                		
                	}
                });
            	window.ytoTable && ytoTable.init();
            });
		
            $(document).ready(function(){
                $("#tbody tr").mouseover(function(){ //如果鼠标移到class为stripe_tb的表格的tr上时，执行函数
                    //$(this).addClass("over");
                    $(this).css("background","#e8f3fa");
                }).mouseout(function(){ //给这行添加class值为over，并且当鼠标一出该行时执行函数
                    //$(this).removeClass("over");
                    $(this).css("background","none");
                }); //移除该行的class
            });
			
            
            
            //四种计费类型的选择
            $('#calclateType').change(function(){
			
            	var calclateType = $("#calclateType").val();
				
                if(calclateType==1){//固定收费
                    $(".tr_sp").each(function(_$_,$_$) {
                        $("#firstWeight").val("");
                        //$("#sp_firstwidth").attr("style","display:none");
                       // $("#p_module").attr("style","display:none");
                        //$("#p_module").hide();
                        //$($_$).find(".fwr").hide();				
                        $($_$).find("input:eq(2)").hide();
                        //$($_$).find(".owr").hide();
                        $($_$).find("input:eq(3)").hide();
                        //$($_$).find(".owe").show();
                        $($_$).find("input:eq(4)").show();
                        //$($_$).find(".owf").hide();
                        $($_$).find("input:eq(5)").hide();
                        //$($_$).find(".owg").hide();
                        $($_$).find("input:eq(6)").hide();
                        //$($_$).find(".owg").val('');
    					//$($_$).find("input:eq(6)").val('');
    					//$($_$).find(".owh").hide();
    					$($_$).find("input:eq(1)").hide();
    					$($_$).find(".addWeightChoice").hide();
                    });
       
                    
                }
    							
                if(calclateType==2){//简单重量收费
                    $(".tr_sp").each(function(_$_,$_$) {
                        //$("#sp_firstwidth").attr("style","display:none");
                      //  $("#p_module").attr("style","display:none");
                        //$("#p_module").hide();
                        //$($_$).find(".fwr").hide();
                        $($_$).find("input:eq(2)").hide();
                        //$($_$).find(".owr").hide();
                        $($_$).find("input:eq(3)").hide();
                        //$($_$).find(".owe").hide();
                        $($_$).find("input:eq(4)").hide();
                        //$($_$).find(".owf").show();
                        $($_$).find("input:eq(5)").show();
                        //$($_$).find(".owg").show();
                        $($_$).find("input:eq(6)").show();
    					//$($_$).find(".owg").val('');
                        //$($_$).find("input:eq(6)").val('');
    					//$($_$).find(".owh").hide();
                        $($_$).find("input:eq(1)").hide();
                        $($_$).find(".addWeightChoice").hide();
    									
                    });
                }
    							
                if(calclateType==3){//续重价格收费
                    $(".tr_sp").each(function(_$_,$_$) {
                        //$("#sp_firstwidth").attr("style","display:inline");
                      //  $("#p_module").attr("style","display:none");									
                        //$("#p_module").hide();									
                        //$($_$).find(".fwr").show();
                        $($_$).find("input:eq(2)").show();
                        //$($_$).find(".owr").show();
                        $($_$).find("input:eq(3)").show();
                        //$($_$).find(".owe").hide();
                        $($_$).find("input:eq(4)").hide();
                        //$($_$).find(".owf").hide();
                        $($_$).find("input:eq(5)").hide();
                        //$($_$).find(".owg").show();
                        $($_$).find("input:eq(6)").show();
    					//$($_$).find(".owg").val($($_$).find(".fwr").val());
                        //$($_$).find("input:eq(6)").val($($_$).find("input:eq(2)").val());
    					//$($_$).find(".owh").show();				
                        $($_$).find("input:eq(1)").show();
                        $($_$).find(".addWeightChoice").hide();
                    });
                }
                if(calclateType==4){//续重统计单位收费
                    $(".tr_sp").each(function(_$_,$_$) {
                       // $("#sp_firstwidth").attr("style","display:inline");
                        //$("#p_module").attr("style","display:inline");										
                        //$("#p_module").show();										
                        //$($_$).find(".fwr").show();				
                        $($_$).find("input:eq(2)").show();
                        //$($_$).find(".owr").show();
                        $($_$).find("input:eq(3)").show();
                        //$($_$).find(".owe").hide();
                        $($_$).find("input:eq(4)").hide();
                        //$($_$).find(".owf").hide();
                        $($_$).find("input:eq(5)").hide();
                        //$($_$).find(".owg").show();
                        $($_$).find("input:eq(6)").show();
    					//$($_$).find(".owg").val($($_$).find(".fwr").val());
                        //$($_$).find("input:eq(6)").val($($_$).find("input:eq(2)").val());
    					//$($_$).find(".owh").show();								
                        $($_$).find("input:eq(1)").show();
                        $($_$).find(".addWeightChoice").show();
                        var addWeight = $($_$).find("input[name='addWeight']").val();
                       
                        //续重统计单位的处理   by yuyuezhong  2012-09-14 start
                        /*if(addWeight != 0.01 && addWeight != 0.5 && addWeight != 1.0  ){
                        	addWeight = '';
                        }*/
                        if(addWeight == 1.0){
                        	addWeight = 1;
                        }
                        $($_$).find(".addWeightChoice").find(":contains('"+addWeight+"')").attr("selected","selected");
                      //续重统计单位的处理    by yuyuezhong  2012-09-14 end
                    });
                }								
    		});
            
            $('#calclateType').change();
	
});
function calculateFreight(type){
	if(type==1){//固定价格
		var weight = $("#type1").val();
		if(weight>0){
			$("#type1_").text("5元");
		} else{
			$("#type1_").text("");
		}
	}else if(type==2){//简单重量
		var weight = $("#type2").val();
		if(weight>0){
			var price=8*weight<10?10:(8*weight);
			$("#type2_").text(price+"元");
		} else{
			$("#type2_").text("");
		}
	}else if(type==3){//续重价格
		var weight = $("#type3").val();
		if(weight>0){
			var price = (weight>0.5?(10+(weight-0.5)*5):10)>12?(weight>0.5?(10+(weight-0.5)*5):10):12;
			$("#type3_").text(price+"元");
		} else{
			$("#type3_").text("");
		}
	}else if(type==4){//续重统计单位
		var weight = $("#type4").val();
		if(weight>0){
			var price = 0;
			if(weight<0.5)
				price = 12;
			else{
				price=(10+((Math.ceil((weight-0.5)/0.5)))*0.5*5)>12?(10+((Math.ceil((weight-0.5)/0.5)))*0.5*5):12;
			}
			$("#type4_").text(price+"元");
		} else{
			$("#type4_").text("");
		}
	}
	
}