/**
 * 调整运费
**/
$(function() {
	var dialog = new Dialog();
	
	//验证运单号
	var waybill =  /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/ ;			
	
	
	// 元素集合
	var els = {
		manageForm: $('#manage_form'),
		nub: $('#nub'),
		province: $('#province'),
		prov: $('#x_prov'),
		city: $('#x_city'),
		district: $('#x_district'),
		detailAdd: $('#detail_address'),
		reason: $('#reason'),
		weight: $('#weight'),
		price: $('#prices'),
		date: $('#date'),
		dateTip: $('#dateTip'),
		noticeBtn: $('#notice_btn'),
		adjustForm: $('#adjust_form'),
		searFreight: $('#textarea_freight'),
		searBtn: $('#sear_btn'),
		elseReason: $('#elseReason'),
		dateStart:$('#starttime'),
		dateEnd:$('#endtime'),
		dateTip: $('#date_tip'),
		cancel:$('#cancelAdjust'),
		freightTip:$('#textarea_freightTip'),
		currentPage: $('#currentPage'),
		adjustClass: $('.adjustClass'),
		managerClass: $('.managerClass'),
		managerTab: $('#managerTab')
	};
	
	var ytofreight = (function() {
		
		els.adjustClass.live('click',function(){
			els.managerTab.val('0');
			window.location.href = "freight!unlikefreight.action?menuFlag=caiwu_freight2";
		});
		els.managerClass.live('click',function(){
			els.managerTab.val('1');
		});
		
		var selectArea = function() {
			var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: ['#province'],
				autoLink: false
			};
			
			linkageSel = new LinkageSel(area);
			var prov = els.prov.val(), city = els.city.val(), district = els.district.val();
			linkageSel.changeValues([prov, city, district]);
		};
		
		// 表单
		var form = function() {
			// 日期的验证结果，默认是 false
			var dateResult = false;
			
			// 显示提示
			var showIcon = {
				correct: function(el, text) {
					el.html('<span class="yto_onCorrect">' + text + '</span>');
				},
				error: function(el, text) {
					el.html('<span class="yto_onError">' + text + '</span>');
				},
				show: function(el, text) {
					el.html('<span class="yto_onShow">' + text + '</span>');
				}
			};
			
			// 提示文案
			var tipsMsg = {
				freightErr: '输入的格式错误',
				nubFormatErr: '运单号格式错误',
				provinceErr: '请选择省',
				cityErr: '请选择市',
				areaErr: '请选择区',
				detailMinErr: '街道地址不能为空',
				detailMaxErr: '街道地址超长',
				reasonMinErr: '调整原因不能为空',
				reasonMaxErr: '调整原因超长',
				weightFormatErr: '重量格式不正确',
				overWeightErr: '重量范围超出限制（重量范围为0~50kg）',
				priceFormatErr: '价格格式不正确',
				overPriceErr: '价格必须大于0',
				dateErr: '请选择日期',
				rangeDateErr: '只能查30天内的数据',
				searFormatErr: '格式错误，请修改',
				//searEmptyErr: '内容不能为空',
				searLongErr: '内容超长'
			};
			
			// “管理”表单
			$.formValidator.initConfig({
				validatorGroup: '1',
				formID: 'manage_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 智能查件
			els.searFreight.
				formValidator({validatorGroup:'1', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({max: 220, onErrorMax: tipsMsg.searLongErr});
				//functionValidator({fun: validateSearch, onError: tipsMsg.searFormatErr});
				
				
			els.searFreight.blur(function(){
				if($(this).val() == ''){
					$(this).val('请输入想要调整的运单号!');
				}else{
					var mailNo = els.searFreight.val();
					if(!waybill.test(mailNo)){
						showIcon.error(els.freightTip, tipsMsg.searFormatErr);
					}
				}
				
			});
			
			
			
			if($.trim($('#textarea_freight').text()) != null && $.trim($('#textarea_freight').text()) != ''){
			}else{
				$('#textarea_freight').defaultTxt('请输入想要调整的运单号!');
			}
			
			// 点击“查询”
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				
				var userCodeForTime = $("#tzuserCodeForTime").val();
                var logisticsIds = $("#textarea_freight").val();
                
                if(logisticsIds=='请输入想要调整的运单号!')logisticsIds='';
                
                $("#textarea_freight").val(logisticsIds);
                var startTime = els.dateStart.val();
                var endTime = els.dateEnd.val();
                $('#startTmHid').val(startTime);
                $('#endTmHid').val(endTime);
                $('#userCodeHid').val(userCodeForTime);
                $('#logisticsIds').val(logisticsIds);
				$('#currentPage').val('1');
				$('#isCheck').val('0');
				$('#flag1').val('1');
				$('#managerTab').val('1');
				
                els.manageForm.attr("action","freight_unlikefreightlist.action");	
				els.manageForm.trigger('submit');
			});
			
			//点击“取消调整”
			els.cancel.live('click',function(ev){
				ev.preventDefault();
				$("#unlikeFreight_id").val($(this).attr('val'));
				
				dialog.init({
					iconType: 'warn',
	        		contentHtml: '请核对信息，确认后将通知客户!',
	        		maskOpacity: 0,
	        		yes: function() {
	        			dialog.close();
	        			
	        			var successDialog = new Dialog();
	        			$.ajax({
	        				dataType:'json',
	        				async:true,
        					type:'post',
        					url:"waybill_deleteUnlikeFreight.action",
        					data:{
        						'unlikeFreight.id':$("#unlikeFreight_id").val()
        					},
        					success:function(data){
        						dialog.init({
                            		iconType: 'success',
                            		contentHtml: '取消成功!',
                            		maskOpacity: 0,
                            		autoClose: 2000,
                            		yes: function() {
                            			dialog.close();
                            			els.manageForm.attr("action","freight_unlikefreightlist.action");	
                        				els.manageForm.trigger('submit');
                            		},
                            		yesVal: '确定'
                            	})
                            	setTimeout(function(){
                            		els.manageForm.attr("action","freight_unlikefreightlist.action");	
                    				els.manageForm.trigger('submit');
                                },2000);
        					}
	        			});
	        		},
	        		no: function() {
	        			dialog.close();
	        		},
	        		yesVal: '确定',
	        		noVal: '取消'
				});
			});
			
			// 验证日期的方法
			var check = {
				date: function(dateVal) {
					if (dateVal != '') {
						showIcon.correct(els.dateTip, '');
						dateResult = true;
					} else {
						showIcon.error(els.dateTip, tipsMsg.dateErr);
						dateResult = false;
					}
				}
			};
			
			
			// “调整”表单
			$.formValidator.initConfig({
				validatorGroup: '2',
				formID: 'adjust_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 运单号
			els.nub.
				formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'shipmentNum', dataType: 'enum', onError: tipsMsg.nubFormatErr});
				
			// 目的地
			els.province.
				formValidator({validatorGroup:'2', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 省
					els.province.next().
						formValidator({validatorGroup:'2', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 市
							els.province.next().next().
								formValidator({validatorGroup:'2', tipID: 'area_tip', onShow: '', onFocus: '', onCorrect: ' '}).	// 区
								inputValidator({min:1, onError: tipsMsg.areaErr});
							return '';
						}}).
						inputValidator({min:1, onError: tipsMsg.cityErr});
					return '';
				}}).
				inputValidator({min:1, onError: tipsMsg.provinceErr});
				
			// 街道地址
			els.detailAdd.
				formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' '}).
				inputValidator({min: 1, max: 100, onErrorMin: tipsMsg.detailMinErr, onErrorMax: tipsMsg.detailMaxErr});
			
			// 调整原因
			els.reason.
				formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' '}).
				functionValidator({
					fun: function(val, el) {
						if (val == '超区转EMS') {
							return true;
							
						}
					}
				});
			
			els.reason.change(function(){
				var obj = $(this);
				if(obj.val()=='其他原因转EMS'){
            		$("#elseReason").show();
            	}else{
            		$("#elseReason").hide();
            	}
			});
				
			// 重量
			els.weight.
				formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'num', dataType: 'enum', onError: tipsMsg.weightFormatErr}).
				functionValidator({fun: validateWeight, onError: tipsMsg.overWeightErr});
			
			// 价格
			els.price.
				formValidator({validatorGroup:'2', onShow: '', onFocus: '', onCorrect: ' '}).
				regexValidator({regExp: 'num', dataType: 'enum', onError: tipsMsg.priceFormatErr}).
				functionValidator({fun: validatePrice, onError: tipsMsg.overPriceErr});
			
			// 日期
			els.date.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择日期后执行验证
						var _this = this,
							dateVal = _this.value;
						check.date(dateVal);
						_this.blur();
					},
					isShowClear: false,
					readOnly: true,
					startDate:'%y-%M-01 00:00:00',
					dateFmt:'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate:true
				})
			});
			
			//开始时间
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择日期后执行验证
						var _this = $(this),
							dateVal = els.dateEnd.val();
						rangeDate(dateVal);
						if (dateResultB) {
							els.dateEnd.prop('disabled', false);
							$dp.$('endtime').focus();
						} else {
							_this.blur();
							_this.focus();
							els.dateEnd.prop('disabled', false);
						}
						
						_this.blur();
					},
					startDate: '#F{$dp.$D(\'endtime\')}',
					// maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
			
			//结束时间
			els.dateEnd.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择日期后执行验证
						var _this = $(this),
							dateVal = _this.val();
						rangeDate(dateVal);
						_this.blur();
					},
					startDate: '#F{$dp.$D(\'starttime\')}',
					minDate: '#F{$dp.$D(\'starttime\')}',	// 终止日期大于起始日期
					// maxDate: '%y-%M-%d',
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
				
			// 查询范围仅在一个月内
			function rangeDate(dateVal) {
				var startVal = els.dateStart.val();
				if (startVal != '') {
					var startDate = startVal.split('-'),
						startDateY = parseInt(startDate[0], 10),			// 年
						startDateM = parseInt(startDate[1], 10),			// 月
						startDateD = parseInt(startDate[2], 10),			// 日
						startUTC = Date.UTC(startDateY, startDateM, startDateD);
						
					var formatDate = dateVal.split('-'),
						formatDateY = parseInt(formatDate[0], 10),			// 年
						formatDateM = parseInt(formatDate[1], 10),			// 月
						formatDateD = parseInt(formatDate[2], 10),			// 日
						formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
						
					var dateGap = parseInt((formatUTC - startUTC) / 1000 / 60 / 60 / 24, 10);

					if (dateGap > 30) {
						showIcon.error(els.dateTip, tipsMsg.rangeDateErr);
						dateResultB = false;
					} else {
						showIcon.correct(els.dateTip, '');
						dateResultB = true;
					}
				}
			};
			
			
			//运单号check
			els.nub.blur(function(){
				var _mailNo = $.trim(els.nub.val());
				
				// 运单号
				if(!waybill.test(_mailNo)){
					return false;
				}
				
                $.ajax({
                    url:"order!orderMonitorJson.action?mailNo="+_mailNo,
                    dataType:"json",
                    cache:false,
                    success:function(obj) {
                    	
                        var data = obj.json;
                        data = eval("("+data+")");
		
                        if(data!=null && _mailNo!="" && $.trim(data.prov) !=""){
                            //赋值
                            $("#province").find(":contains('"+data.prov+"')").attr("selected","selected");
                            $("#province").change();
                            $("#province").next().find(":contains('"+data.city+"')").attr("selected","selected");
                            $("#province").next().change();
                            $("#province").next().next().find(":contains('"+data.district+"')").attr("selected","selected");
                            $("#detail_address").val(data.address);
                            $("#weight").val(data.weight);
                            var mydate = data.createTime;
                            mydate = mydate.substring(0,10)+" "+mydate.substring(11,19);
                            $("#date").val(mydate);
							$("#orderFlag").val('1');
							var userCodeVal = $("#userCode option:contains("+data.userCode+")").val();
							$("#userCode").val(userCodeVal);
							
							//非活性
							$("#province").attr("disabled","disabled").css('border', '1px solid #CBCBCB');//省
							$("#province").next().attr("disabled","disabled").css('border', '1px solid #CBCBCB');//市
							$("#province").next().next().attr("disabled","disabled").css('border', '1px solid #CBCBCB');//区
                            $("#detail_address").attr("disabled","disabled").css('border', '1px solid #CBCBCB');//详细地址
                            $("#weight").attr("disabled","disabled").css('border', '1px solid #CBCBCB');		//重量
                            $("#userCode").attr("disabled","disabled");											//客户名称
                            $("#date").attr("disabled","disabled").css('border', '1px solid #CBCBCB');			//发货日期
							
                        }else if($("#province").prop('disabled')){
                        	$("#province").find(":contains('请选择')").attr("selected","selected");
                        	$("#province").next().next().hide();
                            $("#province").next().hide();
                            $("#province").removeAttr("disabled");
                            $("#province").next().next().removeAttr("disabled");
                            $("#province").next().removeAttr("disabled");
                            
                            $("#detail_address").removeAttr("disabled");
                            $("#date").removeAttr("disabled");
                            $("#weight").removeAttr("disabled");
                            $("#userCode").removeAttr("disabled");
				
                            /*$("#province").find(":contains('请选择省份')").attr("selected","selected");
                            $("#province").next().find(":contains('请选择所在市')").attr("selected","selected");
                            $("#province").next().next().find(":contains('请选择所在县区')").attr("selected","selected");*/
                            $("#detail_address").val("");
							$("#weight").val("");
							$("#date").val("");
							$("#orderFlag").val('0');
                        } 

                        //console.log(data);
                    },
                    error:  function(XMLHttpRequest, statusText, errorThrown) {
                           
                        //art.dialog.alert("取订单信息失败! ");
                    }
                });		
				
			});
			
			
			
			
			
			// 点击“通知客户”
			els.noticeBtn.click(function(ev) {
				ev.preventDefault();
				
				var dateVal = els.date.val(),
					otherResult = $.formValidator.pageIsValid('2');
				
				// 验证日期格式
				check.date(dateVal);
				
				if (dateResult && otherResult) {
						var userCode=$("#userCode").val().split('=')[1];
						var userName = $("#userCode").val().split('=')[0];
		                var goodsTime = $("#date").val();
		                var reason = $("#reason").val();
		                var mailNo = $("#nub").val();	
		                var reason = $("#reason").val();
		                var elseReason = $("#elseReason").val();
		                if(elseReason!=null && elseReason!=''){
		                	reason = reason+elseReason;
		                }
					
						$("#unlikeFreight_address").val(els.detailAdd.val());
						
						//给省市县区赋值文字
						$("#traderInfo_prov").val(linkageSel.getSelectedData('name',0));
						$("#traderInfo_city").val(linkageSel.getSelectedData('name',1));
						$("#traderInfo_district").val(linkageSel.getSelectedData('name',2));
						
						//给省市县区赋值编码
						var selectedArr = linkageSel.getSelectedArr();
						$("#traderInfo_numProv").val(selectedArr[0]);
						$("#traderInfo_numCity").val(selectedArr[1]);
						$("#traderInfo_numDistrict").val(selectedArr[2]);
						
						var price=$("#prices").val();
						var weight=$("#weight").val();
						
		                var userCodeForTime="";
						
		                if(document.getElementById("userCodeForTime")!=null){
		                	userCodeForTime = $('#userCodeForTime').val();
		                }
					
					dialog.init({
                		iconType: 'warn',
                		contentHtml: '请核对信息，确认后将通知客户!',
                		maskOpacity: 0,
                		yes: function() {
                			$.ajax({
                					dataType: 'json',
                					type:'post',
                					url: "freight_add.action",
                					data:{
                						'userCodeForBuyer':userCode,
                						'buyerStartTime':goodsTime,
                						'buyerEndTime':reason,
                						'traderInfo.prov':$("#traderInfo_prov").val(),
                						'traderInfo.city':$("#traderInfo_city").val(),
                						'traderInfo.district':$('#traderInfo_district').val(),
                						'traderInfo.numProv':$('#traderInfo_numProv').val(),
                						'traderInfo.numCity':$('#traderInfo_numCity').val(),
                						'traderInfo.numDistrict':$('#traderInfo_numDistrict').val(),
                						'unlikeFreight.mailNo': mailNo,
                						'unlikeFreight.address':$("#unlikeFreight_address").val(),
                						'unlikeFreight.userName':userName,
                						'unlikeFreight.userCode':userCode,
                						'unlikeFreight.reason':reason,
                						'unlikeFreight.weight':weight,
                						'unlikeFreight.price':price,
                						'unlikeFreight.goodsTime':goodsTime
                					},
                					success : function(data){
                						
                						dialog.close();
                                
                						var okOrErrorDialog = new Dialog();
                						
	                                    if (data == '添加失败') {
	                                    	okOrErrorDialog.init({
	                                    		iconType: 'success',
	                                    		contentHtml: '添加失败，请重新确认后提交!',
	                                    		maskOpacity: 0,
	                                    		autoClose: 2000,
	                                    		yes: function() {
	                                    			okOrErrorDialog.close();
	                                    			els.adjustForm.attr("action","freight_unlikefreight.action");
	                                    			els.adjustForm.trigger('submit');
	                                    		},
	                                    		yesVal: '确定'
	                                    	});
	                                    } else {
	                                    	okOrErrorDialog.init({
	                                    		iconType: 'success',
	                                    		contentHtml: '添加成功!',
	                                    		maskOpacity: 0,
	                                    		autoClose: 2000,
	                                    		yes: function() {
	                                    			okOrErrorDialog.close();
	                                    			els.adjustForm.attr("action","freight_unlikefreight.action");
	                                    			els.adjustForm.trigger('submit');
	                                    		},
	                                    		yesVal: '确定'
	                                    	});
	                                    }
    	                               /* setTimeout(function(){
    	                                	els.adjustForm.attr("action","freight_unlikefreight.action");
                                			els.adjustForm.trigger('submit');
    	                                },2000);*/
    	                            }
                				
                			});
                			
                		},
                		no: function() {
                			dialog.close();
                		},
                		yesVal: '确定',
                		noVal: '取消'
                	})
					
				}
			});
			
			//小叉
			$('#yingcang').live('click',function(){
				 $("#textarea_freight").attr("value",'');
				 $("#textarea_freight").focus();
				 $('#yingcang').hide();
			});
			
			//翻页
			pagination.live('click',function(ev){
				els.currentPage.val($(this).attr("value"));
				els.manageForm.attr("action","freight_unlikefreightlist.action");
				setTimeout(function(){
					els.manageForm.trigger('submit'); 
				},0);
			});
			
		};
		
		return {
			init: function() {
				var index = els.managerTab.val();
				
				ytoTab.init(index);
				if(index == '0'){
					selectArea();
				}
				form();
				//shipNumDetail();
			}
		}
	})();
	var linkageSel;
	ytofreight.init();
})