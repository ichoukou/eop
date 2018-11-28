<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<META http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />

<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/question.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/page/cod_s.css" media="all" />
<!-- E 当前页面 CSS -->
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript">
$(document).ready(function() {
	if(${panelIndex}=='1'){
		showDetail();
	}else if(${panelIndex}=='2'){
		showDiff();
	}
//判断IE文档模式
/*  if(document.documentMode!=9){
	$('#searcsearchTypehType').css('display':'none');
}else{
	
}  */
//选择开始时间
$('#start_date1').focus(function() {
	WdatePicker({
		onpicked: function() {		// 选择起始日期后触发终止日期
			$('#end_date1').prop('disabled', false);
			$dp.$('end_date1').focus();
		},
		startDate: '#F{$dp.$D(\'end_date1\')}',
		maxDate: '%y-%M-%d',			// 最大时间：系统当前
		isShowClear: false,
		readOnly: true,
		doubleCalendar: true,		// 双月历
		dateFmt: 'yyyy-MM-dd 00:00:00'
	});
})

//	选择结束时间
$('#end_date1').focus(function() {
	WdatePicker({
		startDate: '#F{$dp.$D(\'start_date1\')}',
		minDate: '#F{$dp.$D(\'start_date1\')}',	// 终止日期大于起始日期
		maxDate: '%y-%M-%d',			// 最大时间：系统当前
		isShowClear: false,
		readOnly: true,
		doubleCalendar: true,		// 双月历
		dateFmt: 'yyyy-MM-dd 23:59:59'
	});
})
//翻页
pagination.live("click",function(ev){
	//ev.preventDefault();
	$("#autoSkip").val(0);
	$("#currentPage").val($(this).attr("value"));
	setTimeout(function(){
		//document.getElementById('q_form').submit();
		$('#q_form').trigger('submit');
	},0);
});
//导出订单
 $('#exportBill').click(function(){
	var waybillNos = getExportWaybillNos();
	if ("" == waybillNos) {
		$("#ship_numTip").html('<span class="yto_onError">没有数据，不能导出！</span>');
		setTimeout(function(){
			$("#ship_numTip").html('');
		},5000);
		return;
	}
	$("#errorMsg").text("");
//		window.open("codBill_exportCustomerBillDetails.action?");
	location.href="${pageContext.request.contextPath}/codBillPay_exportCodBills.action?waybillNos=" + waybillNos;
});

// 得到要导出的运单号
function getExportWaybillNos(){
	var waybillNos = "";
	$(".mailno").each(function(i){
		if (0 == i) {
			waybillNos += $(this).text();
		} else {
			waybillNos += "," + $(this).text();	
		}
	});
	return waybillNos;
}
//	查询提交，验证表单
$("#sear_btn").click(function(){
//如果运单号输入框为空或输入合法则提交页面
		var waybillNo= $('#ship_num').val();
		var start = $("#start_date1").val();
		var end = $("#end_date1").val();
		if(waybillNo=="" && start!="" || end!=""){
			if(waybillNo==""){
				if(start=="" && end!=""){
					$("#dateTip").html('<span class="yto_onError">请选择开始时间！</span>');
					return false;
				}else if(start!="" && end==""){
					$("#dateTip").html('<span class="yto_onError">请选择结束时间！</span>');
					return false;
				}
			}
		}
						if(isShipNumValid()){
							setTimeout(function(){
								var loadingDialog = new Dialog();
								
								loadingDialog.init({
									contentHtml: '加载中...'
								});
							$("#q_form").trigger('submit');
							}, 0);
						}
});
//	验证运单字符串格式
function isShipNumValid(){
	var reg = /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/;
	var shipNum = $('#ship_num').val();
	var errorShipMsg = $('#ship_numTip').val();
	
//		去除所有空格
	shipNum = shipNum.replace(/\s+/g,"");

//		如果运单号值为空，return false，不为空，则将字符串转换为大写，验证运单号的格式是否正确，正确返回true，并修改输入框的值为标准格式（没有空格，字母大写，用半角逗号隔开）
	if(shipNum != null && shipNum != ""){
//			将字母转换为大写
		shipNum = shipNum.toUpperCase();
//			将用户输入的值转换为去除空格和字母大写的值
//			alert(shipNum);
//			$("#ship_numTip").value = shipNum;
//			检查是否有多个运单号，并判断每个运单号的格式
		$("#ship_num").val(shipNum);
		if(shipNum.indexOf(",") > 0 || shipNum.indexOf("，") > 0){
//				转换全角逗号为半角逗号
			shipNum = shipNum.replace("，",",");
//				用逗号将字符串分割为数组
			var arr = shipNum.split(",");
//				遍历数组，判断每个运单号格式是否正确
			for(var i in arr){
//					判断每个运单号的格式是否正确
				if(!reg.test(arr[i])){
//						判断用户浏览器，选择设置值得方式
					$("#ship_numTip").html('<span class="yto_onError">运单号格式错误！</span>')
					return false;
				}
				$("#ship_numTip").html('<span class="yto_onCorrect"></span>');
			}
			return true;
//				运单号不含逗号时，格式正确return true 否则return false
		}else{
			if(reg.test(shipNum)){
				$("#ship_numTip").html('<span class="yto_onCorrect"></span>');
				return true;
			}else{
				$("#ship_numTip").html('<span class="yto_onError">运单号格式错误！</span>')
				return false;
			}
		}
//			运单号为空时，return false,去除提示
	} else {
		$("#ship_numTip").html("");
		return true;
	}
}
});
//全选/全不选
function check_all(obj){
    var checkboxs = document.getElementsByName('c');
    for(var i=0;i<checkboxs.length;i++){checkboxs[i].checked = obj.checked;}
}
//确认账单明细
$("#batchConfirmDetails").live("click", function() {
	var detailIds = getSelectedIds();
	 if ("" == detailIds) {
		$("#errorMsg").text("请选择需要确认的账单");
		return;
	}else{
		$("#errorMsg").text("");
	}
	if (confirm("您确定要确认选中的账单明细吗？")) {
		location.href='${pageContext.request.contextPath}/codBillPay_checkCodBills.action?detailIds='+detailIds;
	} 
});
//得到选中的账单明细ID
function getSelectedIds() {
	var detailIds = "";
	$("input[name='c']:checked").each(function(i) {
		if (0 == i) {
			detailIds += $(this).val();
		} else {
			detailIds += "," + $(this).val();
			//alert(detailIds);
		}
	});
	return detailIds;
}
//上报差异
function topDiff(waybillNo,sendOrgName,desOrgName,codMoneyReal,sendOrgCode,desOrgCode,codMoney){
	var dialogD = new Dialog();
	var tab = '<div id="dialog_pop"> <meta charset="UTF-8" />' +
    '<form id="sear_form3" class="form" action="codBillPay_topDiff.action" method="post"><input value="'+waybillNo+'" name="" id="c_id" type="hidden" />' +
    '<input type="hidden" id="tabStatus" name="tabStatus" value="${tabStatus == null || '' ? 1 : tabStatus }" />'+
	'<input type="hidden" name="menuFlag" value="${menuFlag }" />'+
	'<input type="hidden" id="currentPage" name="currentPage" value="1" />'+
	'<input type="hidden" id="autoSkip" name="autoSkip" value="${autoSkip }" />'+

	'<input type="hidden" name="confirmLetter.sendOrgCode" value="'+sendOrgCode+'" />'+
	'<input type="hidden" name="confirmLetter.desOrgCode" value="'+desOrgCode+'"/>'+
	'<input type="hidden" name="confirmLetter.initialCodAmount" value="'+codMoney+'"/>'+
	
    '<p><label for="c_name"><span class="req"></span>运单号码：</label><input type="text" id="c_name" name="confirmLetter.waybillNo" value="'+waybillNo+'" class="input_text" readonly="readonly"/></p>'+
	  '<p><label for="mcontact"><span class="req"></span>始发网点：</label><input type="text" id="mcontact" name="confirmLetter.sendOrgName" value="'+sendOrgName+'" class="input_text" readonly="readonly"/></p>'+
	  '<p><label for="s_tel"><span class="req"></span>派件网点：</label><input type="text" id="s_tel" name="confirmLetter.desOrgName" value="'+desOrgName+'" class="input_text" readonly="readonly"/></p>'+
	  '<p><label for="ques_tel"><span class="req"></span>代收金额：</label><input type="text" id="ques_tel" name="confirmLetter.actualCodAmount" value="'+codMoneyReal+'" class="input_text" readonly="readonly"/></p>'+
	  '<p><label for="dialog_textarea3"><span class="req">*</span>上报代收金额：</label><input type="text" id="top_Money" name="confirmLetter.informValue"  class="input_text" /></p>'+
	  '<p><label for="dialog_textarea1"><span class="req">*</span>上报原因：</label><textarea name="confirmLetter.informReason" id="top_Alone" class="textarea_text" cols="100" rows="5"></textarea></p>'+
	  '<span id="dialog_textarea1Tip" style="color:red"></span>'+
	  '<div class="dialog_btn" style="padding-top:10px;"><a href="javascript:;" class="btn btn_e" title="取消" id="dialog_c"><span>取消</span></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" class="btn btn_d" style="margin-right:10px;" title="保存" id="dialog_save"><span>上报差异</span></a></div>'+
	  '</form>' +
	  '	</div>' ;
	dialogD.init({
		closeBtn: true,
		maskOpacity: 0.2,			// 遮罩层的透明度
		contentHtml: tab			// 内容 HTML
	});

	//弹出框信息表单
	$.formValidator.initConfig({
		validatorGroup: '3',
		formID: 'sear_form3',
		theme: 'yto',
		errorFocus: false
	});
	//取消
	$('#dialog_c').live('click',function(ev) {
		dialogD.close();
	});
	//点击弹出框“保存”
	$('#dialog_save').live('click',function(ev) {
		ev.preventDefault();
		
		var topMoney = $('#top_Money').val();
		var topAlone = $('#top_Alone').val();
		if(topMoney=='' || isNaN(topMoney) || topMoney<=0){
			$('#dialog_textarea1Tip').html('请输入正确的金额格式');
			return false;
		}else{
			$('#dialog_textarea1Tip').html('');
		}
		if(topMoney>2500){
			$('#dialog_textarea1Tip').html('输入金额不能大于2500');
			return false;
		}else{
			$('#dialog_textarea1Tip').html('');
		}
		if(topAlone==''){
			$('#dialog_textarea1Tip').html('请输入上报原因 ');
			return false;
		}
		 var intLength=0;
		    for (var i=0;i<topAlone.length;i++)
		    {
		        if ((topAlone.charCodeAt(i) < 0) || (topAlone.charCodeAt(i) > 255))
		            intLength=intLength+2;
		        else
		            intLength=intLength+1;
		    } 
		if(intLength/2>66){
			$('#dialog_textarea1Tip').html('最多只能输入66个汉字 ');
			return false;
		}else{
			$('#dialog_textarea1Tip').html('');
		}
		//$('#sear_form3').trigger('submit');
		setTimeout(function(){
								dialogD.close();
								var loadingDialog = new Dialog();
								
								loadingDialog.init({
									contentHtml: '加载中...'
								});
							
		$.ajax({
    		type : "POST",
    		url : "codBillPay_topDiff.action",
    		dataType : "json",
    		data : {'confirmLetter.waybillNo' : waybillNo,'confirmLetter.sendOrgName':sendOrgName,'confirmLetter.deliveryOrgName':desOrgName,'confirmLetter.actualCodAmount':codMoneyReal,
    			'confirmLetter.informValue':topMoney,'confirmLetter.informReason':topAlone,'confirmLetter.sendOrgCode':sendOrgCode,'confirmLetter.deliveryOrgCode':desOrgCode,'confirmLetter.initialCodAmount':codMoney},
    		success : function(result) {
    			loadingDialog.close();
    			var sndDialog = new Dialog();
    			if(result.jsonResult.undoReason=="success"){
					sndDialog.init({
						contentHtml: "<div id='box_form' style='font:700 15px/1.4 SimSun;color:#F2F2F2;width:260px;padding:8px;background:#3D78AD' >上报信息</div><hr/><div style='padding:10px;height:50px;'>运单号:"+waybillNo+"上报成功</div><a href='codBillPay_toBillDetailSearch.action?menuFlag=caiwu_codpay' id='sear_btn' class='btn btn_a' title='确定' style='margin-left:100px'><span>确  定</span></a><div><hr/></div>",
						closeBtn: false
					})
    			}else if(result.jsonResult.undoReason=="error"){
    				sndDialog.init({
						contentHtml: "<div id='box_form' style='font:700 15px/1.4 SimSun;color:#F2F2F2;width:260px;padding:8px;background:#3D78AD' >上报信息</div><hr/><div style='padding:10px;height:50px;'>运单号:"+waybillNo+"上报失败</div><a href='codBillPay_toBillDetailSearch.action?menuFlag=caiwu_codpay' id='sear_btn' class='btn btn_a' title='确定' style='margin-left:100px'><span>确  定</span></a><div><hr/></div>",
						closeBtn: false
					})
    			}else if(result.jsonResult.undoReason=="report"){
    				sndDialog.init({
    				contentHtml: "<div id='box_form' style='font:700 15px/1.4 SimSun;color:#F2F2F2;width:260px;padding:8px;background:#3D78AD' >上报信息</div><hr/><div style='padding:10px;height:50px;'>运单号:"+waybillNo+"已经上报</div><a href='codBillPay_toBillDetailSearch.action?menuFlag=caiwu_codpay' id='sear_btn' class='btn btn_a' title='确定' style='margin-left:100px'><span>确  定</span></a><div><hr/></div>",
					closeBtn: false
    			})
    			}
    		} ,
    		error : function(err) {
    			alert("出现异常");	
    		}
    	});
	}, 0);
	})
}
function showDetail(){
	$('#tab_panel_a').show();
	$('#tab_panel_b').hide();
	$('#bbb').addClass('tab_cur');
	$('#aaa').removeClass('tab_cur');
	
}
function showDiff(){
	$('#tab_panel_a').hide();
	$('#tab_panel_b').show();
	$('#aaa').addClass('tab_cur');
	$('#bbb').removeClass('tab_cur');
}

</script>
<!-- S Content -->
  <div id="content"> 
    <div id="content_bd" class="clearfix">
      <!-- S Box -->
                <div class="tab tab_a">
			        	<div class="tab_triggers">
			        		<ul>
					        	 <li class="tab_cur" id="bbb"><a href="javascript:;" onclick="showDetail()">未确认账单明细</a></li>
		                         <li id="aaa"><a href="javascript:;" onclick="showDiff()">未确认差异</a></li>
		                    </ul> 
	                    </div>
	                    
	                    <div class="tab_panels">
	                        <!-- S 未确认账单明细tab -->
	                        <div id="tab_panel_a" class="tab_panel">
	                        	
	                        	<div id="box_form" class="box box_a">
      							  <div class="box_bd">
	                        		<form action="codBillPay_searchCODList.action?panelIndex=1" id="q_form" class="form" method="post">
	                        			<input type="hidden" id="tabStatus" name="tabStatus" value="${tabStatus == null || '' ? 1 : tabStatus }" />
										<input type="hidden" name="menuFlag" value="${menuFlag }" />
										<input type="hidden" id="currentPage" name="currentPage" value="1" />
										<input type="hidden" id="autoSkip" name="autoSkip" value="${autoSkip }" />
											<p>
												<label for="q_type">运单号：</label>
												<input type="text" class="input_text" title="可输入多个运单号,英文','隔开" value="${waybillNo}" name="waybillNo" id="ship_num" onkeydown="if(event.keyCode==32||event.keyCode==222){return false;}"/>
												<!-- <span style="color:red">[可输入多个运单号,英文','隔开]</span> -->
												<span id="ship_numTip"></span>
											</p>
											
											<p>
												<label for="start_date" class="">发件时间：从</label>
												<input type="text" class="Wdate" value="${detailStartTime }"  name="detailStartTime" id="start_date1" title="起始日期"/> 
												到 
												<input type="text" class="Wdate" value="${detailEndTime }" id="end_date1" name="detailEndTime" title="截止日期"/>
												<span id="dateTip"></span>
											</p>
											<!-- 以下标签隐藏 -->
														<div style="display:none">
														<input type="text"  name="startTime" value="${startTime}"   /> 
														<input type="text"  name="endTime" value="${endTime}" />
														<input type="text" name="waybillNo2" value="${waybillNo2 }"/>
														<input type="text" name="searchType" value="${searchType }"/>
														</div>
											<p>				<!-- enterSearch() -->
												<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查 询"><span>查 询</span></a>
												<a href="javascript:;"  id="exportBill" class="btn btn_a" title="导出账单"><span>导出账单</span></a>
												<a href="javascript:void(0)" id="batchConfirmDetails" class="btn btn_a" title="确认账单"><span>确认账单</span></a>
												<span id="errorMsg" style="color:red"></span>
											</p>
											
									</form>
								 </div>
      							</div>
										<!-- S Table -->
						                <div class="table">
						                	<div id="show_total">
													<div id="amount_confirmed"  >
														<div >
														<span class="font_style_a">总票数（票）:</span>
														<span class="font_style_b">${countBill}</span>&nbsp;&nbsp;
														</div>
														<div >
														<span class="font_style_a">总代收金额（元）:</span>
														<span class="font_style_b">${count }</span>&nbsp;&nbsp;
														</div>
													</div>
								            </div>
						                  <table>
						                    <thead>
						                      <tr>
						                        <th class="th_a"> <div class="th_title"><em>
						                            <input type="checkbox" name="all" onclick="check_all(this)" class="checked_all" />
						                            </em></div>
						                        </th>
						                        <th class="th_b"> <div class="th_title"><em>运单号</em></div>
						                        </th>
						                        <th class="th_c"> <div class="th_title"><em>发件时间</em></div>
						                        </th>
						                        <th class="th_d"> <div class="th_title"><em>揽件日期</em></div>
						                        </th>
						                        <th class="th_e"> <div class="th_title"><em>代收金额</em></div>
						                        </th>
						                        <th class="th_f"> <div class="th_title"><em>发件网点名称</em></div>
						                        </th>
						                        <th class="th_g"> <div class="th_title"><em>快件状态</em></div>
						                        </th>
						                        <th class="th_h"> <div class="th_title"><em>差异信息</em></div>
						                        </th>
						                        <th class="th_j"> <div class="th_title"><em>操作</em></div>
						                        </th>
						                      <!--   <th class="th_k"> <div class="th_title"><em>收款类型</em></div>
						                        </th> -->
						                      </tr>
						                    </thead>
						                   <tbody>
						                   		
							                    <c:if test="${(billPrintList==null || fn:length(billPrintList)<1)}">
								                    <tr>
								                    	<td colspan="10">无账单记录</td>
								                    </tr>
								                </c:if>
							                   	<c:forEach items="${billPrintList}" var="bill">
								                    <tr>				<!-- 待确认-->
								                    	<td><c:if test="${ bill.confirmFlg=='5' || bill.confirmFlg=='0' || bill.confirmFlg=='1' || bill.confirmFlg=='2' }"><input type="checkbox" name="c" value="${bill.detailId}"/></c:if></td>
								                    	<td><a href="javascript:;"  class="mailno" >${bill.waybillNo}</a></td>
								                    	<td>${bill.recordTime}</td>
								                    	<td>${bill.collectInTime}</td>
								                    	<td>${bill.codMoneyReal}</td>
								                    	<td>${bill.sendOrgName}</td>
								                    	<td>
									                    	<c:if test="${bill.signoffFlg=='0' }">未签收</c:if>
								                    	</td>
								                    	<td>
								                    		<c:if test="${bill.confirmFlg=='4' }"><a class="subcompanyUndo" title="查看驳回原因" href="javascript:void(0)">被驳回</a></c:if>
								                    		<c:if test="${bill.confirmFlg=='7' }"><a class="companyUndo" title="查看驳回原因" href="javascript:void(0)">被驳回</a></c:if>
								                    		<c:if test="${bill.confirmFlg=='6' }">处理中</c:if>
								                    		<c:if test="${bill.confirmFlg=='8' }">已处理</c:if>
								                    		<c:if test="${bill.confirmFlg=='3' }">已上报</c:if>
								                    		<c:if test="${bill.confirmFlg=='5' || bill.confirmFlg=='0' || bill.confirmFlg=='1' || bill.confirmFlg=='2'}">待确认</c:if>
								                    	</td> <!-- 未签收和无差异[待确认]  才能够上报差异 -->
								                    	<td><c:if test="${bill.signoffFlg=='0' && (bill.confirmFlg=='5' || bill.confirmFlg=='4' || bill.confirmFlg=='7' || bill.confirmFlg=='8'|| bill.confirmFlg=='0' || bill.confirmFlg=='1' || bill.confirmFlg=='2'  )}"><a href="javascript:;" id="dialog_demo_my" onclick="topDiff('${bill.waybillNo}','${bill.sendOrgName }','${bill.desOrgName }','${bill.codMoneyReal}','${bill.sendOrgCode }','${bill.desOrgCode}','${bill.codMoney}')">上报差异</a></c:if></td>
								                    	<%-- <td>
								                    		<c:if test="${bill.chargeType =='0' }">现金</c:if>
								                    		<c:if test="${bill.chargeType =='2' }">月结</c:if>
								                    	</td> --%>
								                    </tr>
							                 </c:forEach>
						                 </tbody>
						                 </table>
						                  	<!-- S PageNavi 分页 -->
									                <div class="pagenavi">
									                	<jsp:include page="/WEB-INF/page.jsp" />
									                </div>
									         <!-- E PageNavi -->
						                </div>
						                 
	                        </div>
	
	                 <!-- S 未确认差异 tab -->
	                 <div id="tab_panel_b" class="tab_panel" style="display:none;">
							<!-- S Box -->
											      <div id="box_form" class="box box_a">
											        <div class="box_bd">
														<form action="codBillPay_toSearchDiff.action?panelIndex=2" id="q_form2" class="form" method="post">
															<input type="hidden" id="tabStatus" name="tabStatus" value="${tabStatus == null || '' ? 1 : tabStatus }" />
															<input type="hidden" name="menuFlag" value="${menuFlag }" />
															<input type="hidden" id="autoSkip" name="autoSkip" value="${autoSkip }" />
															<!-- 以下标签隐藏 -->
																<div style="display:none">
																<input type="text" value="${waybillNo}" name="waybillNo" />
																<input type="text"  value="${detailStartTime }" name="detailStartTime" /> 
																<input type="text" value="${detailEndTime }"  name="detailEndTime"/>
																<input type="text" value="${pagination.startIndex }" name="StartIndex"/>
																<input type="text" value="${pageNum }" name="pageNum"/>
																<input type="text" value="${currentPage }" name="currentPage"/>
																<input type="text" value="${count }" name="count"/>
																</div>
															<p id="timeFile">
																<c:if test="${ searchType=='0'}">
																<label for="start_date" class="">上报时间段：</label>
																<input type="text" class="Wdate" name="startTime" value="${startTime}"  title="起始日期" id="start_date2"  /> 
																到 
																<input type="text" class="Wdate"  name="endTime" value="${endTime}"  title="截止日期" id="end_date2"/>
																</c:if>
																<c:if test="${ searchType=='1'}">
																<label for="q_type">运单号：</label>
	      														<input type="text" class="input_text" name="waybillNo2" id="waybillNo" value="${ waybillNo2}" onkeydown="if(event.keyCode==32||event.keyCode==222){return false;}"/>
																</c:if>
																<span id="ship_numTip2"></span>
																<span id="dateTip2"></span>
														   </p>
															<p>
															    <span class="addresstitle" style="width:75px;">查询类型：</span>
															    <s:select list="#{'0':'按时间','1':'按运单号' }" id="searchType"  name="searchType"  cssStyle="height:26px;line-height:26px;" ></s:select>
											  	    		    <%-- <select id="searchType"  name="searchTypeTemp">
											  	    		    	<option value="0">按时间</option>
											  	    		    	<option value="1">按运单号</option>
											  	    		    	<c:if test="${ searchType} == 0">
											  	    		    		<option value="0" selected="selected">按时间</option>
											  	    		    	</c:if>
											  	    		    	<c:if test="${ searchType} == 1">
											  	    		    		<option value="1" selected="selected">按运单号</option>
											  	    		    	</c:if>
											  	    		    </select> --%>
											  	    		  
																<a href="javascript:;" class="btn btn_a" id="sear_btn2" title="查 询"><span>查 询</span></a>
															</p>
														</form>
														
														
											        </div>
											      </div>
											      <!-- E Box -->
											      <!-- S Box -->
											      <div class="box box_a ">
											        <div class="box_bd" id="box_bd">
											          <!-- S Tab -->
											            	<!-- 未确认差异 -->
											              <div id="tab_panel_a" class="tab_panel clearfix" >
											                <!-- S Table -->
											                <div class="table">
											                  <table>
											                    <thead>
											                      <tr>
											                        <th class="th_b"> <div class="th_title"><em>运单号</em></div>
											                        </th>
											                        <th class="th_c"> <div class="th_title"><em>上报时间</em></div>
											                        </th>
											                        <th class="th_d"> <div class="th_title"><em>代收金额</em></div>
											                        </th>
											                        <th class="th_e"> <div class="th_title"><em>上报代收金额</em></div>
											                        </th>
											                        <th class="th_f"> <div class="th_title"><em>状态</em></div>
											                        </th>
											                    </thead>
											                    <tbody>
											                    <c:if test="conPrintList==null || conPrintList.size() == 0">
												                    <tr>
												                    	<td colspan="5">无账单记录</td>
												                    </tr>
												                </c:if>
											                   	<c:forEach items="${conPrintList}" var="re">
												                    <tr>
												                    	<td><a href="javascript:;"  class="mailnos">${re.waybillNo}</a></td>
												                    	<td>${re.informTime}</td>
												                    	<td>${re.initialValue}</td>
												                    	<td>${re.informValue}</td>
												                    	<td>
												                    		<c:if test="${re.confirmFlg == '0' }">处理中</c:if>
												                    		<c:if test="${re.confirmFlg == '1' }">已处理</c:if>
												                    		<c:if test="${re.confirmFlg=='2' }"><a class="companyUndos" title="查看驳回原因" href="javascript:void(0)">被驳回</a></c:if>
												                    		<c:if test="${re.confirmFlg=='3' }">已上报</c:if>
																			<c:if test="${re.confirmFlg=='4' }"><a class="subcompanyUndos" title="查看驳回原因" href="javascript:void(0)">被驳回</a></c:if>
																		</td>
												                    </tr>
											                 </c:forEach>
											                 </tbody>
											                 <c:if test="${(conPrintList==null || fn:length(conPrintList)<1)}">
											                    <tr>
											                    	<td colspan="10">无差异记录</td>
											                    </tr>
											                </c:if>
											                  </table>
																<a href="#" style='float:right'>↑回到顶层</a>
												                </div>
											                
											                
											              </div>
											            </div>
											          </div>
							 <!-- E Tab -->
	                 </div>
	                 <!-- E 未确认差异 tab -->
	
        </div>
        									
      </div>
      <!-- E Box -->
     
    </div>
    <script>
$(document).ready(function() {
//		查看驳回理由
	$(".companyUndo").live("click", function() {
    	var waybillNo = $(this).parents("tr").find(".mailno").text();
    	setTimeout(function(){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				contentHtml: '加载中...'
			});
    	$.ajax({
    		type : "POST",
    		url : "codBill_queryCompanyUndoReason.action",
    		dataType : "json",
    		data : {waybillNo : waybillNo},
    		success : function(result) {
    			loadingDialog.close();
    			if (null != result.jsonResult && null != result.jsonResult.undoReason) {
//	    				alert(result.jsonResult.undoReason);
//	    				$.boxUtil.info(result.jsonResult.undoReason, {title: '驳回原因'});
					var sndDialog = new Dialog();
					sndDialog.init({
						contentHtml: "<div style='width:350px;height:200px;padding:20px;'><label style='font:700 15px/1.4 SimSun;color:#333;'>驳回原因：</label><hr/><div style='padding:10px;height:130px;'>"+result.jsonResult.undoReason+"</div><div><hr/></div></div>",
						closeBtn: true
					})
				}
    		},
    		error : function(err) {
    			alert("error");
    		}
    	});
    	}, 0);
    });
	$(".subcompanyUndo").live("click", function() {
    	var waybillNo = $(this).parents("tr").find(".mailno").text();
    	setTimeout(function(){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				contentHtml: '加载中...'
			});
    	$.ajax({
    		type : "POST",
    		url : "codBill_querySubcompanyUndoReason.action",
    		dataType : "json",
    		data : {waybillNo : waybillNo},
    		success : function(result) {
    			loadingDialog.close();
    			if (null != result.jsonResult && null != result.jsonResult.undoReason) {
//	    				alert(result.jsonResult.undoReason);
//	    				$.boxUtil.info(result.jsonResult.undoReason, {title: '驳回原因'});
					var sndDialog = new Dialog();
					sndDialog.init({
						contentHtml: "<div style='width:350px;height:200px;padding:20px;'><label style='font:700 15px/1.4 SimSun;color:#333;'>驳回原因：</label><hr/><div style='padding:10px;height:130px;'>"+result.jsonResult.undoReason+"</div><div><hr/></div></div>",
						closeBtn: true
					})
				}
    		},
    		error : function(err) {
    			alert("error");
    		}
    	});
    	}, 0);
    });
	
	$(".companyUndos").live("click", function() {
    	var waybillNo = $(this).parents("tr").find(".mailnos").text();
    	setTimeout(function(){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				contentHtml: '加载中...'
			});
    	$.ajax({
    		type : "POST",
    		url : "codBill_queryCompanyUndoReason.action",
    		dataType : "json",
    		data : {waybillNo : waybillNo},
    		success : function(result) {
    			loadingDialog.close();
    			if (null != result.jsonResult && null != result.jsonResult.undoReason) {
//	    				alert(result.jsonResult.undoReason);
//	    				$.boxUtil.info(result.jsonResult.undoReason, {title: '驳回原因'});
					var sndDialog = new Dialog();
					sndDialog.init({
						contentHtml: "<div style='width:350px;height:200px;padding:20px;'><label style='font:700 15px/1.4 SimSun;color:#333;'>驳回原因：</label><hr/><div style='padding:10px;height:130px;'>"+result.jsonResult.undoReason+"</div><div><hr/></div></div>",
						closeBtn: true
					})
				}
    		},
    		error : function(err) {
    			alert("error");
    		}
    	});
    	}, 0);
    });
	$(".subcompanyUndos").live("click", function() {
    	var waybillNo = $(this).parents("tr").find(".mailnos").text();
    	setTimeout(function(){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				contentHtml: '加载中...'
			});
    	$.ajax({
    		type : "POST",
    		url : "codBill_querySubcompanyUndoReason.action",
    		dataType : "json",
    		data : {waybillNo : waybillNo},
    		success : function(result) {
    			loadingDialog.close();
    			if (null != result.jsonResult && null != result.jsonResult.undoReason) {
//	    				alert(result.jsonResult.undoReason);
//	    				$.boxUtil.info(result.jsonResult.undoReason, {title: '驳回原因'});
					var sndDialog = new Dialog();
					sndDialog.init({
						contentHtml: "<div style='width:350px;height:200px;padding:20px;'><label style='font:700 15px/1.4 SimSun;color:#333;'>驳回原因：</label><hr/><div style='padding:10px;height:130px;'>"+result.jsonResult.undoReason+"</div><div><hr/></div></div>",
						closeBtn: true
					})
				}
    		},
    		error : function(err) {
    			alert("error");
    		}
    	});
    	}, 0);
    });
	
	$("#searchType").bind("change", function(){
		switch ($(this).val()) {
		case "0":
			$("#timeFile").empty();
			var str = '<label for="start_da	te" class="">确认时间段：</label>';
	      	str += '<input type="text" class="Wdate" name="startTime" value="${startTime}" id="start_date2" title="起始日期"/> ';
	       	str += '到 <input type="text" class="Wdate"  name="endTime" value="${endTime}"  id="end_date2" title="截止日期"/><span id="dateTip2"></span>';
	       	$("#timeFile").append(str);
	       	$('#start_date2').focus(function() {
	       		WdatePicker({
	       			onpicked: function() {		// 选择起始日期后触发终止日期
	       				$('#end_date2').prop('disabled', false);
	       				$dp.$('end_date2').focus();
	       			},
	       			//startDate: '#F{$dp.$D(\'end_date2\')}',
	       			maxDate: '%y-%M-%d',			// 最大时间：系统当前
	       			isShowClear: false,
	       			readOnly: true,
	       			doubleCalendar: true,		// 双月历
	       			dateFmt: 'yyyy-MM-dd 00:00:00'
	       		});
	       	})
	       	$('#end_date2').focus(function() {
				WdatePicker({
					//startDate: '#F{$dp.$D(\'start_date2\')}',
					minDate: '#F{$dp.$D(\'start_date2\')}',	// 终止日期大于起始日期
					maxDate: '%y-%M-%d',			// 最大时间：系统当前
					isShowClear: false,
					readOnly: true,
					doubleCalendar: true,		// 双月历
					dateFmt: 'yyyy-MM-dd 23:59:59'
				});
			});
			break;
		case "1":
			$("#timeFile").empty();
			var str = '<label for="q_type">运单号：</label>';
	      	str += '<input type="text" class="input_text" name="waybillNo2" id="waybillNo" value="${waybillNo2}" onkeydown="if(event.keyCode==32||event.keyCode==222){return false;}"/><span id="ship_numTip2"></span>';
	      	$("#timeFile").append(str);
			break;
		}
	});
	
	//选择开始时间

	$('#start_date2').focus(function() {
		WdatePicker({
			onpicked: function() {		// 选择起始日期后触发终止日期
				$('#end_date2').prop('disabled', false);
				$dp.$('end_date2').focus();
			},
			//startDate: '#F{$dp.$D(\'end_date2\')}',
			maxDate: '%y-%M-%d',			// 最大时间：系统当前
			isShowClear: false,
			readOnly: true,
			doubleCalendar: true,		// 双月历
			dateFmt: 'yyyy-MM-dd 00:00:00'
		});
	});

//		选择结束时间
	$('#end_date2').focus(function() {
		WdatePicker({
			//startDate: '#F{$dp.$D(\'start_date2\')}',
			minDate: '#F{$dp.$D(\'start_date2\')}',	// 终止日期大于起始日期
			maxDate: '%y-%M-%d',			// 最大时间：系统当前
			isShowClear: false,
			readOnly: true,
			doubleCalendar: true,		// 双月历
			dateFmt: 'yyyy-MM-dd 23:59:59'
		});
	});
});

//验证运单字符串格式
function isShipNumValids(){
	var reg = /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/;
	var shipNum = $('#waybillNo').val();
	var errorShipMsg = $('#ship_numTip2').val();
	
//		去除所有空格
	shipNum = shipNum.replace(/\s+/g,"");
	$("#waybillNo").val(shipNum);
//		如果运单号值为空，return false，不为空，则将字符串转换为大写，验证运单号的格式是否正确，正确返回true，并修改输入框的值为标准格式（没有空格，字母大写，用半角逗号隔开）
	if(shipNum != null && shipNum != ""){
//			将字母转换为大写
		shipNum = shipNum.toUpperCase();
//			将用户输入的值转换为去除空格和字母大写的值
//			alert(shipNum);
//			$("#ship_numTip").value = shipNum;
			if(reg.test(shipNum)){
				$("#ship_numTip2").html('<span class="yto_onCorrect"></span>');
				return true;
			}else{
				$("#ship_numTip2").html('<span class="yto_onError">运单号格式错误！</span>')
				return false;
			}
//			运单号为空时，return false,去除提示
	} else {
		$("#ship_numTip2").html("");
		return true;
	}
}

//查询提交，验证表单
$("#sear_btn2").click(function(){
//如果运单号输入框为空或输入合法则提交页面
		var waybillNo= $('#waybillNo').val();
		var start = $("#start_date2").val();
		var end = $("#end_date2").val();
		var queryType = $("#searchType").val();
		
		if("0"==queryType){ //根据时间
			var start = $("#start_date2").val();
			var end = $("#end_date2").val();
			if(start=="" && end==""){
				$("#dateTip2").html('<span class="yto_onError">请选择确认时间段</span>');
				return;
			}
			if(!start=="" && end==""){
				$("#dateTip2").html('<span class="yto_onError">请选择结束时间</span>');
				return;
			}
			if(start=="" && !end==""){
				$("#dateTip2").html('<span class="yto_onError">请选择开始时间</span>');
				return;
			}
			//$('#searchType').html('0');

			document.getElementById('q_form2').submit();
		}
		if("1" == queryType) { //根据运单号
			var waybillNo = $('#waybillNo').val();
			if(waybillNo==""){
				$("#ship_numTip2").html('<span class="yto_onError">请输入运单号</span>');
				return;
			}
//				如果运单号输入框为空或输入合法则提交页面
				if(isShipNumValids()){
					//$('#searchType').html('1');
					$("#q_form2").trigger('submit');
				}
			}
});

</script>
	<script type="text/javascript">
		var params = {
			userType:2
		};
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<%-- <script type="text/javascript" src="${jsPath}/page/cod_s.js?d=${str:getVersion() }"></script>  --%>
	<script type="text/javascript" src="${jsPath}/common/mailDetails.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->

	
	<a href="javascript:;" id="to_top" title="回到顶部">回到顶部</a>
  </div>
  <!-- E Content -->