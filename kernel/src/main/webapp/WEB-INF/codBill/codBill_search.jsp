<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table_cod_search.css" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/cod_s.css" media="all" />
<!-- E 当前页面 CSS -->


<!-- S Content -->
  <div id="content">
    <div id="content_bd" class="clearfix">
        <div id="search_box">
			<form action="codBill_queryBillDetailCountAndAmount.action" id="q_form" class="form" method="post" style="height:100px;">
				<input type="hidden" id="tabStatus" name="tabStatus" value="${tabStatus == null || '' ? 1 : tabStatus }" />
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<input type="hidden" id="currentPage" name="currentPage" value="1" />
				<input type="hidden" id="autoSkip" name="autoSkip" value="${autoSkip }" />
				<p>
					<label for="timeType">处理时间：</label>
					<s:select list="#{'2':'支付时间','1':'确认时间','0':'录单时间'}" name="timeType" id="timeType" cssStyle="height: 26px;"></s:select>
					<input type="text" class="Wdate" name="detailStartTime" id="start_date" value="${detailStartTime }" /> 
					至 
					<input type="text" class="Wdate" id="end_date" name="detailEndTime" value="${detailEndTime }"/>
					<span id="dateTip"></span>
				</p>
				
				<p>
					<label for="confirmFlg">确认状态：</label>
					<s:select list="#{'1':'已确认','0':'未确认' }" headerKey="" headerValue="全部" id="confirmFlg" name="billDetail.customerConfirmFlg" ></s:select>
					<label for="signoffFlg" class="reset_label">快件状态：</label>
<!-- 					<span style="vertical-align:middle;">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp快件状态：</span> -->
					<s:select list="#{'1':'已签收','0':'未签收' }" headerKey="" headerValue="全部" id="signoffFlg" name="billDetail.signoffFlg"></s:select>
				</p>
				<p>
					<label for="q_type">运单号：</label>
					<input type="text" class="input_text" name="billDetail.waybillNo" id="ship_num" title="输入多个运单号用逗号隔开" value="${billDetail.waybillNo }"/>
					<a id="sear_btn"  class="btn btn_a" href="javascript:;" title="查询" class="red_btn"><span>查 询</span></a>
		  			<a id="exportBill" class="btn btn_a" href="javascript:;" title="导出当前页的账单" class="red_btn"><span>导出账单</span></a>
		  			<span id="ship_numTip"></span>
		  			<div id="errorMsg" class="error_msg" style="float: right;"></div>
				</p>
				
				
				
			</form>
        </div>
        
                <div id="search_detail" class="table" style="margin-bottom:10px;border:1px solid #8DB2E3">
                <div id="show_total">
                <c:choose>
					<c:when test="${ timeType=='1' &&billPrintList!=null }">
						<div id="amount_confirmed"  >
						<div >
						<span class="font_style_a">未确认总票数（票）:</span>
						<span class="font_style_b">${result.unconfirmedNumAmount }</span>&nbsp;&nbsp;
						</div>
						<div >
						<span class="font_style_a">未确认总金额（元）:</span>
						<span class="font_style_b">${result.unconfirmedMoneyAmount }</span>&nbsp;&nbsp;
						</div>
						<div >
						<span class="font_style_a">已确认总票数（票）:</span>
						<span class="font_style_b">${result.confirmedNumAmount }</span>&nbsp;&nbsp;
						</div>
						<div >
						<span class="font_style_a">已确认总金额（元）:</span>
						<span class="font_style_b">${result.confirmedMoneyAmount }</span>&nbsp;&nbsp;
						</div>
					</div>
					</c:when>
					<c:when test="${(timeType=='0' || timeType == '2') && billPrintList!=null }">
						<div id="amount_confirmed"  >
						<div >
						<span class="font_style_a">未支付总票数（票）: </span>
						<span class="font_style_b">${result.unpaidNumAmount }</span>&nbsp;&nbsp;
						</div>
						<div >
						<span class="font_style_a">未支付总金额（元）:</span>
						<span class="font_style_b">${result.unpaidMoneyAmount }</span>&nbsp;&nbsp;
						</div>
						<div >
						<span class="font_style_a">已支付总票数（票）: </span>
						<span class="font_style_b">${result.paidNumAmount }</span>&nbsp;&nbsp;
						</div>
						<div >
						<span class="font_style_a">已支付总金额（元）: </span>
						<span class="font_style_b">${result.paidMoneyAmount }</span>&nbsp;&nbsp;
						</div>
					</div>
					</c:when>
					</c:choose>
                </div>
                  <table >
                  <colgroup>
                  <col width="8%" /> 
                  <col width="8.8%" /> 
                  <col width="8.8%" /> 
                  <col width="8.8%" /> 
                  <col width="7.5%" /> 
                  <col width="11.8%" /> 
                  <col width="7.5%" /> 
                  <col width="7.5%" /> 
                  <col width="7.5%" /> 
                  <col width="8.8%" /> 
                  <col width="7.5%" /> 
                  <col width="7.8%" />
                  
                  </colgroup>
                    <thead>
                      <tr >
                        <th class="first_th"> <div class="th_title"><em>运单号</em></div>
                        </th>
                        <th class="th_b"> <div class="th_title"><em>揽件日期</em></div>
                        </th>
                        <th class="th_c"> <div class="th_title"><em>发件时间</em></div>
                        </th>
                        <th class="th_d"> <div class="th_title"><em>签收日期</em></div>
                        </th>
                        <th class="th_e"> <div class="th_title"><em>代收金额</em></div>
                        </th>
                        <th class="th_f"> <div class="th_title"><em>发件网点名称</em></div>
                        </th>
                        <th class="th_g"> <div class="th_title"><em>快件状态</em></div>
                        </th>
                        <th class="th_h"> <div class="th_title"><em>确认状态</em></div>
                        </th>
                        <th class="th_i"> <div class="th_title"><em>支付状态</em></div>
                        </th>
                        <th class="th_j"> <div class="th_title"><em>支付时间</em></div>
                        </th>
                        <th class="th_k"> <div class="th_title"><em>差异信息</em></div>
                        </th>
                        <th class="last_th"> <div class="th_title"><em>收款类型</em></div>
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                    	<c:choose>
                    		<c:when test="${index==1 && (billPrintList==null || fn:length(billPrintList)<1) }">
                    			<tr align="center">
                    				<td colspan="12">抱歉，暂无您所查找的数据</td>
                    			</tr>
                    		</c:when>
                    		<c:otherwise>
                      <s:iterator value="billPrintList" id="codBill">
                      	<tr>
                        <td class="td_a">
                        <a href="javascript:;" title="查看订单详情" style="text-decoration:underline;color:blue" class="mailno" val="${codBill.waybillNo}">
                        ${codBill.waybillNo}</a></td>
                        <td class="td_b"><s:property value="collectInTime"/></td>
                        <td class="td_c">${codBill.sendTime }</td>
                        <td class="td_d">${codBill.signoffTime}</td>
                        <td class="td_e">${codBill.codMoneyReal}</td>
                        <td class="td_f">${codBill.sendOrgName}</td>
                        <td class="td_g">
                        <s:if test="signoffFlg==1">已签收</s:if>
                        <s:if test="signoffFlg==0">未签收</s:if>
                        </td>
                        <td class="td_h">
                        
                        <s:if test="customerConfirmFlg==1">已确认</s:if>
                        <s:if test="customerConfirmFlg==0">未确认</s:if>
                        </td>
                        <td class="td_i">
                        <s:if test="%{customerPayFlg==1}">已支付</s:if>
                        <s:if test="%{customerPayFlg==0}">未支付</s:if>
                        </td>
                        <td class="td_j">${codBill.customerPayTime}</td>
                        <td class="td_k">
<%--                         <s:if test="%{confirmFlg==4}"><a class="subcompanyUndo" title="查看驳回原因" href="javascript:void(0)">被驳回</a></s:if> --%>
                        <s:if test="%{confirmFlg==4}">被驳回</s:if>
                        <s:elseif test="%{confirmFlg==6}">处理中</s:elseif>
<%--                         <s:elseif test="%{confirmFlg==7}"><a class="companyUndo" title="查看驳回原因" href="javascript:void(0)">被驳回</a></s:elseif> --%>
                        <s:elseif test="%{confirmFlg==7}">被驳回</s:elseif>
                        <s:elseif test="%{confirmFlg==8}">已处理</s:elseif>
                        <s:elseif test="%{confirmFlg==3}">已上报</s:elseif>
                        <s:else>无差异</s:else>
                        </td>
                        <td class="td_l">
                        <s:if test="%{chargeType==0}">现金</s:if>
                        <s:if test="%{chargeType==2}">刷卡</s:if>
                        </td>
<%--                       </c:forEach> --%>
                      </s:iterator>
                    		</c:otherwise>
                    	</c:choose>
                    </tbody>
                  </table>
                
                 <div class="pagenavi" style="height:40px;line-height:40px;overflow:hidden;padding-right:10px;"><jsp:include page="/WEB-INF/page.jsp" /></div>
                </div>

               
              
    </div>
    
	<script type="text/javascript">
	$(function (){
		
// 		查看驳回理由
		$(".companyUndo").live("click", function() {
	    	var waybillNo = $(this).parents("tr").find(".mailno").text();
	    	waybillNo=waybillNo.trim()
// 	    	alert(waybillNo);
	    	$.ajax({
	    		type : "POST",
	    		url : "codBill_queryCompanyUndoReason.action",
	    		dataType : "json",
	    		data : {waybillNo : waybillNo},
	    		success : function(result) {
	    			if (null != result.jsonResult && null != result.jsonResult.undoReason) {
// 	    				alert(result.jsonResult.undoReason);
// 	    				$.boxUtil.info(result.jsonResult.undoReason, {title: '驳回原因'});
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
	    });
	    
	    $(".subcompanyUndo").live("click", function() {
	    	var waybillNo = $(this).parents("tr").find(".mailno").text();
	    	waybillNo=waybillNo.trim()
// 	    	alert(waybillNo);
	    	$.ajax({
	    		type : "POST",
	    		url : "codBill_querySubcompanyUndoReason.action",
	    		dataType : "json",
	    		data : {waybillNo : waybillNo},
	    		success : function(result) {
	    			if (null != result.jsonResult && null != result.jsonResult.undoReason) {
// 	    				alert(result.jsonResult.undoReason);
// 	    				$.boxUtil.info(result.jsonResult.undoReason, {title: '驳回原因'});
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
	    });
	    
		
// 		导出订单
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
// 			window.open("codBill_exportCustomerBillDetails.action?");
			location.href="${pageContext.request.contextPath}/codBill_exportCodBills.action?waybillNos=" + waybillNos;
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

		

		
// 		显示总数统计
// 		function showAmount(){
// 	 		用户查询的时间类型
// 			var timeType = $('#timeType').val();
// 			if(timeType == "1"){
// 				alert(timeType);
// 				document.getElementById('amount_confirmed').style.display='block';
// 				flag_confirmed_show =true;
// 			}
// 		}
		
// 		翻页
		pagination.live("click",function(ev){
// 				ev.preventDefault();
				$("#autoSkip").val(0);
				$("#currentPage").val($(this).attr("value"));
				setTimeout(function(){
					$("#q_form").trigger('submit');
				},0);
			});
		
// 		查询提交，验证表单
		$("#sear_btn").click(function(){
// 			如果运单号输入框为空或输入合法则提交页面
			if(isShipNumValid()){
				$("#q_form").trigger('submit');
// 				showAmount();
			}
		});

// 		选择开始时间
		$('#start_date').focus(function() {
			WdatePicker({
				onpicked: function() {		// 选择起始日期后触发终止日期
					$('#end_date').prop('disabled', false);
					$dp.$('end_date').focus();
				},
// 				startDate: '#F{$dp.$D(\'end_date\')}',
				maxDate: '%y-%M-%d',			// 最大时间：结束日期
				isShowClear: false,
				readOnly: true,
				doubleCalendar: true,		// 双月历
				dateFmt: 'yyyy-MM-dd 00:00:00'
			});
		});
		
// 		选择结束时间
		$('#end_date').focus(function() {
			WdatePicker({
// 				startDate: '#F{$dp.$D(\'start_date\')}',
				minDate: '#F{$dp.$D(\'start_date\')}',	// 终止日期大于起始日期
				maxDate: '%y-%M-%d',			// 最大时间：系统当前
				isShowClear: false,
				readOnly: true,
				doubleCalendar: true,		// 双月历
				dateFmt: 'yyyy-MM-dd 23:59:59'
			});
		});
		
		$('#ship_num').blur(function (){
			isShipNumValid();
		});
		
// 		验证运单字符串格式
		function isShipNumValid(){
			var reg = /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/;
			var shipNum = $('#ship_num').val();
			var errorShipMsg = $('#ship_numTip').val();
			
// 			去除所有空格
			shipNum = shipNum.replace(/\s+/g,"");

// 			如果运单号值为空，return false，不为空，则将字符串转换为大写，验证运单号的格式是否正确，正确返回true，并修改输入框的值为标准格式（没有空格，字母大写，用半角逗号隔开）
			if(shipNum != null && shipNum != ""){
// 				将字母转换为大写
				shipNum = shipNum.toUpperCase();
// 				将用户输入的值转换为去除空格和字母大写的值
// 				alert(shipNum);
// 				$("#ship_numTip").value = shipNum;
				$("#ship_num").val(shipNum);
// 				检查是否有多个运单号，并判断每个运单号的格式
				if(shipNum.indexOf(",") > 0 || shipNum.indexOf("，") > 0){
// 					转换全角逗号为半角逗号
					shipNum = shipNum.replace("，",",");
					// 去除最后一个多余的逗号
					if(shipNum.charAt(shipNum.length - 1) == ','){
						shipNum = shipNum.substr(0,shipNum.length - 1);
					}
					$("#ship_num").val(shipNum);
// 					用逗号将字符串分割为数组
					var arr = shipNum.split(",");
// 					遍历数组，判断每个运单号格式是否正确
					for(var i in arr){
// 						判断每个运单号的格式是否正确
						if(!reg.test(arr[i])){
// 							判断用户浏览器，选择设置值得方式
							$("#ship_numTip").html('<span class="yto_onError">运单号格式错误！</span>')
							return false;
						}
						$("#ship_numTip").html('<span class="yto_onCorrect"></span>');
					}
					return true;
// 					运单号不含逗号时，格式正确return true 否则return false
				}else{
					if(reg.test(shipNum)){
						$("#ship_numTip").html('<span class="yto_onCorrect"></span>');
						return true;
					}else{
						$("#ship_numTip").html('<span class="yto_onError">运单号格式错误！</span>')
						return false;
					}
				}
// 				运单号为空时，return false,去除提示
			} else {
				$("#ship_numTip").html("");
				return true;
			}
			
		}
		
		
// 		弹出运单详情
// 		shipNumDetail().init();

		

		$('.mailno').click(function(ev) {
			ev.preventDefault();
			var mailNo = $(this).text();
			// 请求地址
			var requestUrl = 'logistic_queryInfo.action?mailNo='+$.trim(mailNo);
			
			// 正在加载
			var loadDialog = new Dialog();
			loadDialog.init({
				contentHtml: '正在加载...'
			});
			// 发送异步请求
			$.ajax({
				url: requestUrl,
				type: 'GET',
				cache: false,
				dataType: 'html',
				success: function(data) {
					// 关闭弹层
					loadDialog.close();
					var sndDialog = new Dialog();
					sndDialog.init({
						contentHtml: data,
						closeBtn: true
					})
					
					ytoTab.init(0, $('#dialog'));
				}
			})
		});
		
		$('.item_navi a').live('click', function(ev) {
			ev.preventDefault();
			var curPage = $(this).attr("val");
			// 请求地址
			var requestUrl = 'logistic_paginationQuery.action?currentPage='+curPage;
			// 发送异步请求
			$.ajax({
				url: requestUrl,
				type: 'GET',
				cache: false,
				dataType: 'html',
				success: function(data) {
					$('.item_navi').parent().html(data)
				}
			})
		});

		$('.mailnos').live('click',function(ev) {
			ev.preventDefault();
			
			var mailNo = $(this).text();
			// 请求地址
			var requestUrl = 'logistic_queryInfo.action?mailNo='+$.trim(mailNo);
			
			// 正在加载
			var loadDialog = new Dialog();
			loadDialog.init({
				contentHtml: '正在加载...'
					
			});
			// 发送异步请求
			$.ajax({
				url: requestUrl,
				type: 'GET',
				cache: false,
				dataType: 'html',
				success: function(data) {
					// 关闭弹层
					loadDialog.close();
					var sndDialog = new Dialog();
					sndDialog.init({
						contentHtml: data,
						closeBtn: true
					})
					
					ytoTab.init(0, $('#dialog'));
				}
			})
		});

		

		
		
	});
		
		
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
<%-- 	<script type="text/javascript" src="${jsPath}/page/cod_s.js?d=${str:getVersion() }"></script> --%>
<%-- 	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script> --%>
	<script type="text/javascript" src="${jsPath}/common/jquery.boxUtil.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->

	
<!-- 	<a href="javascript:;" id="to_top" title="回到顶部">回到顶部</a> -->
  </div>
  <!-- E Content -->
