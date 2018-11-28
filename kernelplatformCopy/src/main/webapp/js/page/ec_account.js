/**
 * 电子对账
**/
$(function() {
	var ec_account = (function() {
		
		var textDefault = function() {
			if($('#input_text_txt').val()=="" || $('#input_text_txt').val()=="请输入买家姓名/买家电话/运单号")
				$('#input_text_txt').defaultTxt('请输入买家姓名/买家电话/运单号');
		};
		
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
		
		// 元素集合
		var els = {
			searForm: $('#sear_form'),
			dateStart: $('#date_start'),
			dateEnd: $('#date_end'),
			dateTip: $('#date_tip'),
			searBtn: $('#sear_btn'),
			searText: $('#input_text_txt'),
			exportBtn: $('#export_btn'),
			lastestA: $('#lastest_a'),
			lastestB: $('#lastest_b'),
			lastestC: $('#lastest_c'),
			clientSel: $('#all_per'),
			clientTip: $('#all_per_tip'),
			inputRadio: $('.input_radio'),
			posttempId: $('#posttempId'),
			Tusercode: $('#Tusercode'),
			tableTip: $('#table_tip'),
			prov: $('#x_prov'),
			city: $('#x_city'),
			district: $('#x_district'),
			currentPage: $('#currentPage'),
			errorOrder: $('.errorOrder'),//无法计费订单
			adjustOrder: $('.ufOrder'),//调整计费订单
			returnedOrder: $('.thOrder'),//退货订单
			tempKey: $('#tempKey'),
			sendProv: $('#sendProv'),
			thead_select: $('.thead_select'),//店铺筛选
			bindUserId : $("#bindUserId")/*,
			queryCondition:$('#input_text_text')//统计条件
*/		};
		
		// 表单
		var form = function() {
			// 验证初始值
			var dateResult = true,			// 日期的验证结果
				searResult = true;			// 运单号验证结果
				clientResult = true;        // 客户选择框验证结果
			
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
				dateErr: '请选择日期',
				maxDateErr: '只能查近三个月的数据',
				rangeDateErr: '只能查30天内的数据',
				dateEmptyErr: '请选择开始日期',
				searFormatErr: '格式错误，请修改',
				searLongErr: '内容超长',
				clientSelSiteErr:'暂无绑定客户',
				clientSelPlatErr:'没有激活的业务账号'
			};
			
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
				},
				maxDate: function(dateVal) {
					if (dateVal == '') {
						showIcon.error(els.dateTip, tipsMsg.dateEmptyErr);
						dateResult = false;
					} else {
						// 系统时间
						var sysDate = new Date(),
							sysDateY = parseInt(sysDate.getFullYear(), 10),
							sysDateM = parseInt(sysDate.getMonth(), 10) + 1,
							sysDateD = parseInt(sysDate.getDate(), 10),
							sysUTC = Date.UTC(sysDateY, sysDateM, sysDateD);
						
						
						// 用户选择时间
						var formatDate = dateVal.split('-'),
							formatDateY = parseInt(formatDate[0], 10),			// 年
							formatDateM = parseInt(formatDate[1], 10),			// 月
							formatDateD = parseInt(formatDate[2], 10),			// 日
							formatUTC = Date.UTC(formatDateY, formatDateM, formatDateD);
							
						
						// 用户选择时间离系统时间的天数
						var dateGap = parseInt((sysUTC - formatUTC) / 1000 / 60 / 60 / 24, 10);
						
						
						// 如果超过前3个月
						if (dateGap > 31*3) {
							showIcon.error(els.dateTip, tipsMsg.maxDateErr);
							dateResult = false;
						} else {
							showIcon.correct(els.dateTip, '');
							dateResult = true;
						}
					}
				},
				rangeDate: function(dateVal) {
					var startVal = els.dateStart.val();
					this.maxDate(startVal);
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

						if (dateGap > 31) {
							showIcon.error(els.dateTip, tipsMsg.rangeDateErr);
							dateResult = false;
						} else {
							showIcon.correct(els.dateTip, '');
							dateResult = true;
						}
					}
				}
			};
			
			//客户选择框的验证
			var clientSelCheck = {
				isSel:function(optionVal){
					if(optionVal == ''){
						clientResult = false;
						if(params.userType==1)//网点
							showIcon.error(els.clientTip, tipsMsg.clientSelSiteErr);
						if(params.userType==3)//平台用户
							showIcon.error(els.clientTip, tipsMsg.clientSelPlatErr);
					}else{
						clientResult = true;
					}
				}
			};
			
			// “查询”表单 不使用验证方法，只是为了加载 yto 的验证皮肤
			$.formValidator.initConfig({
				formID: 'sear_form',
				theme: 'yto',
				errorFocus: false
			});
			
			// 查询最近 N 天的数据
			var lastestDays = function(days) {
				var sysDate = new Date(),
					latestDate = new Date(),
					sysDateY = parseInt(latestDate.getFullYear(), 10),
					sysDateM = parseInt(latestDate.getMonth(), 10) + 1,
					sysDateD = parseInt(latestDate.getDate(), 10),
					newtimems = latestDate.getTime() - (days * 24 * 60 * 60 * 1000);
					
				latestDate.setTime(newtimems);

				
				// 填开始时间
				els.dateStart.val(latestDate.format('yyyy-MM-dd'));
				
				// 填结束时间
				els.dateEnd.val(sysDate.format('yyyy-MM-dd'));
				
				//check.maxDate(els.dateStart.val());
				//check.rangeDate(els.dateStart.val());
			};
			
			// 近三天
			els.lastestA.change(function() {
				lastestDays(2);
			});
			
			// 近七天
			els.lastestB.change(function() {
				lastestDays(6);
			});
			
			// 近一个月
			els.lastestC.change(function() {
				lastestDays(29);
			});
			
			// 日期
			els.dateStart.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择起始日期后触发终止日期
						var _this = this,
							dateVal = _this.value;
							
						check.maxDate(dateVal);
						
						
						if (dateResult) {
							els.dateEnd.prop('disabled', false);
							$dp.$('date_end').focus();
						} else {
							_this.blur();
							_this.focus();
							els.dateEnd.prop('disabled', true);
						}
						
						
						els.inputRadio.prop('checked', false)
					},
					startDate: '#F{$dp.$D(\'date_end\')}',
//					minDate: '%y-%M-{%d-31*3}',		// 最小时间：3个月前
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
			els.dateEnd.focus(function() {
				WdatePicker({
					onpicked: function() {		// 选择终止日期后触发校验
						var _this = this,
							dateVal = _this.value;
						//check.date(dateVal);
						
						check.rangeDate(dateVal);
						
						_this.blur();
						
						els.inputRadio.prop('checked', false)
					},
					startDate: '#F{$dp.$D(\'date_start\')}',
					minDate: '#F{$dp.$D(\'date_start\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true		// 双月历
				})
			});
			
			//改变用户时需要重新定位模板
			els.clientSel.change(function(){
				if(params.userType==1){//网点
					assignPosttempId();
				}
			});
			
			//统计
			els.searBtn.click(function(ev) {
				ev.preventDefault();
				var startVal = els.dateStart.val(),
					endVal = els.dateEnd.val(),
					clientSelVal = els.clientSel.val();
				check.rangeDate(endVal);
				if(dateResult)
					check.maxDate(startVal);
				//给省市县区赋值
				var selectedArr = linkageSel.getSelectedArr();
				els.prov.val(selectedArr[0]);
				els.city.val(selectedArr[1]);
				els.district.val(selectedArr[2]);
				clientSelCheck.isSel(clientSelVal);
				els.currentPage.val(1);
				if (dateResult && clientResult) {
					var loadingDialog = new Dialog();
					loadingDialog.init({
						contentHtml: '正在加载中……'
					});
					els.searForm.trigger('submit');
				}
			});
			
			//店铺筛选
			els.thead_select.each(function(){
				$(this).children().each(function(){
					$(this).click(function(){
						if($(this).val()==0){
							window.location.href = 'associationAccount_toBindeAccountCustom.action';
						}else{
							els.bindUserId.val($(this).val());
							$("#action").val("shopFitler");
							var startVal = els.dateStart.val(),
							endVal = els.dateEnd.val(),
							clientSelVal = els.clientSel.val();
							check.rangeDate(endVal);
							check.maxDate(startVal);
							//给省市县区赋值
							var selectedArr = linkageSel.getSelectedArr();
							els.prov.val(selectedArr[0]);
							els.city.val(selectedArr[1]);
							els.district.val(selectedArr[2]);
							clientSelCheck.isSel(clientSelVal);
							els.currentPage.val(1);
							if (dateResult && clientResult) {
								els.searForm.trigger('submit');
							}
						}
					});
				});
			});
			
			
			//翻页
			pagination.click(function(){
				els.currentPage.val($(this).attr("value"));
				var startVal = els.dateStart.val(),
					endVal = els.dateEnd.val(),
					clientSelVal = els.clientSel.val();
				check.rangeDate(endVal);
				check.maxDate(startVal);
				//给省市县区赋值
				var selectedArr = linkageSel.getSelectedArr();
				els.prov.val(selectedArr[0]);
				els.city.val(selectedArr[1]);
				els.district.val(selectedArr[2]);
				clientSelCheck.isSel(clientSelVal);
				if (dateResult && clientResult) {
					setTimeout(function(){
						els.searForm.trigger('submit');
					},0);
					
				}
			});
			
			//导出：卖家 todo缺少省市条件
			els.exportBtn.click(function(ev){
				ev.preventDefault();
				var startVal = els.dateStart.val(),
					endVal = els.dateEnd.val(),
					clientSelVal = els.clientSel.val();
				clientSelCheck.isSel(clientSelVal);
				check.rangeDate(endVal);
				check.maxDate(startVal);
				//给省市县区赋值
				var selectedArr = linkageSel.getSelectedArr();
				els.prov.val(selectedArr[0]);
				els.city.val(selectedArr[1]);
				els.district.val(selectedArr[2]);
				els.currentPage.val(1);
				var posttempId = els.posttempId.val(),
					prov = els.prov.val(),
					city = els.city.val(),
					district = els.district.val(),
					queryCondition = els.searText.val();
				if(dateResult){
					var params = "customerCode="+clientSelVal+"&prov="+prov+"&city="+city+"&district="+district+"&queryCondition="+queryCondition
								 +"&starttime="+startVal+"&endtime="+endVal+"&posttempId="+posttempId+"&outputCode=GBK";
					window.open("order!exportOrder.action?"+encodeURI(params));
				}
			});
		};
		
		/**
		 * 初始化弹出订单信息
		 */
		var triggerParticular = function(requestUrl) {
			var f = "channelmode=no,height=650,width=952,titlebar=no,toolbar=no,"
				+"menubar=no,location=no,directories=no,left=300,top=150,scrollbars=yes";
			window.open(requestUrl,'',f);
		};
		
		var particularList = function() {
			// 查看无法计费订单
			els.errorOrder.click(function() {
				if($(this).text()=='0') {
					return;
				}
				var tempKey = els.tempKey.val();
				var sendProv = els.sendProv.val();
				// 请求地址
				var requestUrl = 'order!toErrorOrderView.action?sendProv='+sendProv+'&tempKey='+tempKey;
				triggerParticular(requestUrl);
			});
			
			// 查看调整计费运单
			els.adjustOrder.click(function() {
				if($(this).text()=='0'){
					return;
				}
				var tempKey = els.tempKey.val();
				// 请求地址
				var requestUrl = 'order!toufOrderView.action?tempKey='+tempKey;
				triggerParticular(requestUrl);
			});
			
			// 查看退货运单
			els.returnedOrder.click(function() {
				if($(this).text()=='0') {
					return;
				}
				var tempKey = els.tempKey.val();
				// 请求地址
				var requestUrl = 'order!tothOrderView.action?tempKey='+tempKey;
				triggerParticular(requestUrl);
			});
			
		};
		
		
		//网点或者卖家给模板赋值
		var assignPosttempId = function(){
			if(params.userType==1){//网点
				var customerCode = $('#all_per').val();
				$.ajax({
					url : 'order!getPosttempByVipJSON.action?customerCode='+customerCode,
					success:function(data){
						$("#posttempId").val(data);
					}
				});
			}
			if(params.userType==2){//卖家
				var customerCode = $("#Tusercode").val();
				$.ajax({
					url : 'order!getPosttempByVipJSON.action?customerCode='+customerCode,
					success:function(data){
						$("#posttempId").val(data);
						if(data==0){
							$("#table_tip").css('display','inline');
						}
					}
				});
			}
		};
		
		return {
			init: function() {	
				ytoTab.init();
				textDefault();
				selectArea();
				form();
				assignPosttempId();
				//shipNumDetail();
				particularList();
			}
		}
	})();
	var linkageSel;
	ec_account.init();
})

function xslay(id){
	$('#tlayer'+id).fadeIn();
}
function gblay(id){
	$('#tlayer'+id).fadeOut();
}
