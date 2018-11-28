<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <title>电子面单操作记录</title>
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/css/tab.css" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/css/complaint.css" />
    <link rel="stylesheet" type="text/css" href="/css/base/reset.css" />
	<link rel="stylesheet" type="text/css" href="/css/common/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/module/button.css?d=v1.0.0" media="all" />
	<link rel="stylesheet" type="text/css" href="/css/module/dialog.css?d=v1.0.0" media="all" />
	<link rel="stylesheet" type="text/css" href="/css/module/box.css?d=v1.0.0" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
    <link href="${jsPath}/module/calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/facebill/recordBill.js"></script>
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/bill/ord-record.css" />
    <!--------------当前页面css--------------->
<script type="text/javascript">
window.onload = function(){
	var msg = $("#errMsg").val();
	if(msg != null && msg != ""){
		var loadingDialog = new Dialog();
		loadingDialog.init({
			closeBtn : true,
			contentHtml : msg
		});
	}
}; 
function repeatDown(cusName,seqId,cusCode,downBoxNum){
	$("#sequenceId").val(seqId);
	$("#cusRecoreCode").val(cusCode);
	$("#cusName").val(cusName);
	$("#downNum").val(parseInt(downBoxNum));
	$("#repeatDownForm").trigger('submit');
}

function toPage(curPage){
	$("#q_cusStartPage").val(curPage);
	$("#q_form").trigger('submit');
}

$(function(){
	$("#sear_btn").click(function(){
		var seqId = $("#seqId").val();
		//批次号输入数字检查
		var reg = new RegExp("^[0-9]*$");
		if(!reg.test(seqId)){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '批次号必须为数字！'
			});
			return;
		}
		
		//批次号长度限制
		if(seqId.length > 9){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '批次号长度不能超过9位！'
			});
			return;
		}
		
		//商家代码为空检查
		var cusCode = $("#search_cusCode").val();
		if(cusCode == null || cusCode == ""){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '请输入一个商家代码！'
			});
			return;
		}
		
		//商家代码特殊字符检查
		var pattern = new RegExp("[`~!@%#$^&*()=|{}':;',　\\[\\]<>/? ／＼］\\.；：%……+ ￥（）【】‘”“'．。，、？＝＿＋～？！＠＃＄％＾＆＊）]");
		if(pattern.test(cusCode)){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '商家代码不能输入特殊符号！'
			});
			return;
		}
		
		//时间段为空检查
		var start = $("#startTime").val();
		var end = $("#endTime").val();
		if(start == null || end == null || start == "" || end == ""){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '请选择创建时间的范围！'
			});
			return;
		}
		
		//结束时间不能早于起始时间
		if(end < start){
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '结束时间不能早于起始时间！'
			});
			return;
		}
		
		// 用户选择时间
		var formatDate_start = start.split('-');
		formatDateY_start = parseInt(formatDate_start[0], 10);			// 年
		formatDateM_start = parseInt(formatDate_start[1], 10);			// 月
		formatDateD_start = parseInt(formatDate_start[2], 10);			// 日
		formatUTC_start = Date.UTC(formatDateY_start, formatDateM_start-1, formatDateD_start);
		
		var formatDate_end = end.split('-');
		formatDateY_end = parseInt(formatDate_end[0], 10);			// 年
		formatDateM_end = parseInt(formatDate_end[1], 10);			// 月
		formatDateD_end = parseInt(formatDate_end[2], 10);			// 日
		formatUTC_end = Date.UTC(formatDateY_end, formatDateM_end-1, formatDateD_end);
		
		// 用户选择起始时间和结束时间间隔的天数
		var dateGap = parseInt((formatUTC_end - formatUTC_start) / 1000 / 60 / 60 / 24, 10);
		
		// 如果超过3个月
		if (dateGap > (31*3)) {
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '查询起始时间与结束时间间隔不能超过3个月！'
			});
			return;
		}
		
		$("#q_form").trigger('submit');
	});	
});	
</script>
</head>
<body>
<div id="content">
    <div class="clearfix" id="content_bd">
        <div>
            <!-- S Box -->
            <div id="box_form" class="box box_a">
                <div class="box_bd">
                	<input type="hidden" id="errMsg" value="${errorMsg}"/>
                	<form method="post" id="repeatDownForm" action="waybillExport_waybillExport.action?currentPage=1&menuFlag=dzmd_mdxz">
                		<input type="hidden" id="cusStartPage"  value="1" name="pageSize"/>
                		<input type="hidden" id="cusLimit" value="10" name="limit"/>
                		<input type="hidden" id="cusRecoreCode" value="" name="customerCode"/>
                		<input type="hidden" id="cusName" name="customerName" value=""/>
                		<input type="hidden" id="sequenceId" value="" name="sequenceId"/>
                		<input type="hidden" id="downNum" value="" name="downloadBoxNum"/>
                	</form>
                	
                    <form class="form" method="post" id="q_form" action="waybillExport_queryExportLog.action?currentPage=1&menuFlag=dzmd_mdxz">
                    	<input type="hidden" id="q_cusStartPage"  value="1" name="pageSize"/>
                		<input type="hidden" id="q_cusLimit" value="10" name="limit"/>
                        <p>
                            <span>
                                <label>批次号：</label>
                               <input class="input_text" type="text" value="${sequenceId }" name="sequenceId" id="seqId"/>
                            </span>
                            <span>
                                <label class="m-tar">商家代码：</label>
                               <input class="input_text" type="text" value="${customerCode}" name="customerCode" id="search_cusCode"/>&nbsp;<font color="red">(*必填)</font>
                            </span>
                        </p>
                        <p>
                            <label>下载时间：</label>
                            <input type="text" value="<s:date format="yyyy-MM-dd" name="startDate"/>" name="startDate" class="Wdate" id="startTime">
                            &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
                            <input type="text" value="<s:date format="yyyy-MM-dd" name="endDate"/>" name="endDate" class="Wdate" id="endTime">
                            <span id="dateTip"></span>
                            <a id="sear_btn" class="btn btn_a" title="查 询" href="javascript:;">
                                <span>查 询</span>
                            </a>
                        </p>
                    </form>
                </div>
            </div>
            <br>
            <div class="table" id="table_c">
                <div class="record-bar">
                    <p>下载记录</p>
                    <ul class="record-slid">
                        <li>
                            <label>商家总面单数：</label>
                            <span class="blue">${userWaybillInfo.totalCount}</span>
                        </li>
                        <li>
                            <label>已下载面单数：</label>
                            <span class="blue">${userWaybillInfo.totalCount-userWaybillInfo.remainCount}</span>
                        </li>
                        <li>
                            <label>剩余面单数：</label>
                            <span class="red">${userWaybillInfo.remainCount}</span>
                        </li>
                    </ul>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th class="th_b first_th">
                                <div class="th_title"><em>商家代码</em></div>
                            </th>
                            <th class="th_c">
                                <div class="th_title"><em>下载面单个数</em></div>
                            </th>
                            <th class="th_d">
                                <div class="th_title"><em>下载箱数</em></div>
                            </th>
                            <th class="th_e">
                                <div class="th_title"><em>下载批次号</em></div>
                            </th>
                            <th class="th_f">
                                <div class="th_title"><em>下载时间</em></div>
                            </th>
                            <th class="th_g last_th">
                                <div class="th_title"><em>操作</em></div>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    	<s:iterator value="#request.zebrasequencePagination.records" status="stuts" var="usrThread">
                 			<tr class="list_tr">
	                            <td>${userWaybillInfo.customerName}(<s:property value="#usrThread.customerCode"/>)</td>
	                            <td><s:property value="#usrThread.totalCount"/></td>
	                            <td><s:property value="#usrThread.totalBoxNum"/></td>
	                            <td><s:property value="#usrThread.sequenceId"/></td>
	                            <td><s:date format="yyyy-MM-dd HH:mm:ss" name="#usrThread.createTime"/></td>
	                            <td><a href="javascript:" onclick="repeatDown('${userWaybillInfo.customerName}','<s:property value="#usrThread.sequenceId"/>','<s:property value="#usrThread.customerCode"/>','<s:property value="#usrThread.totalBoxNum"/>')">重新下载</a></td>
	                        </tr>
                 		</s:iterator>  		
                    </tbody>
                </table>
            </div>
            <div class="table_footer clearfix">
				<div class="pagenavi">
					<s:if test="zebrasequencePagination.totalPages > 1">
						<a value="1" href="javascript:toPage(1);" class="page_txt">&laquo; 首页</a>
					</s:if>
					
					<s:if test="%{zebrasequencePagination.currentPage > 1}"><a value='${zebrasequencePagination.currentPage-1 }' href="javascript:toPage(${zebrasequencePagination.currentPage-1});" class="page_txt">上一页</a></s:if>
					
					<s:if test="%{zebrasequencePagination.pageIndex.startIndex > 1}">...</s:if>
					
					<s:iterator begin="zebrasequencePagination.pageIndex.startIndex" end="zebrasequencePagination.pageIndex.endIndex" var="vp">
						<s:if test="zebrasequencePagination.currentPage == #vp">
							<span class="page_cur" title="<s:property value="#vp"/>"><s:property value="#vp"/></span>
						</s:if>
						<s:else>
							<a value=<s:property value="#vp"/> href="javascript:toPage(<s:property value="#vp"/>);" class="page_num" title="<s:property value="#vp"/>"><s:property value="#vp"/></a>
						</s:else>
					</s:iterator>
					
					<s:if test="%{zebrasequencePagination.pageIndex.endIndex < zebrasequencePagination.totalPages}">...</s:if>
					
					<s:if test="%{zebrasequencePagination.currentPage < zebrasequencePagination.totalPages}"><a value='${zebrasequencePagination.currentPage+1 }' href="javascript:toPage(${zebrasequencePagination.currentPage+1});" class="page_txt">下一页</a></s:if>
					
					<s:if test="zebrasequencePagination.totalPages > 1">
						<a value=<s:property value='zebrasequencePagination.totalPages'/> href="javascript:toPage(<s:property value='zebrasequencePagination.totalPages'/>);" class="page_txt">末页 &raquo;</a>
					</s:if>
					
					<span class="page_total">共 <em><s:property value="zebrasequencePagination.totalPages"/></em> 页/<em><s:property value="zebrasequencePagination.totalRecords"/></em> 条</span>
				</div>
			</div>
        </div>
    </div>
</div>

</body>
</html>