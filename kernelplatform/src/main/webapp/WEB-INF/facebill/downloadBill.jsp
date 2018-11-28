<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <title>面单下载</title>
    <script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/css/tab.css" />
    <link rel="stylesheet" type="text/css" href="/css/base/reset.css" />
	<link rel="stylesheet" type="text/css" href="/css/common/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/module/button.css?d=v1.0.0" media="all" />
	<link rel="stylesheet" type="text/css" href="/css/module/dialog.css?d=v1.0.0" media="all" />
	<link rel="stylesheet" type="text/css" href="/css/module/box.css?d=v1.0.0" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
    <link href="${jsPath}/module/calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css">
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/bill/ord-download.css" />
    <!--------------当前页面css--------------->

<script type="text/javascript">
var boo = true;
window.onload = function(){
	$("#search_cuscode").val($("#cusCode").val());
	var msg = $("#errMsg").val();
	if(msg != null && msg != ""){
		var loadingDialog = new Dialog();
		loadingDialog.init({
			closeBtn : true,
			contentHtml : msg
		});
	}
}; 

function recordBill(cusCode){
	$("#cusRecoreCode").val(cusCode);
	$("#recordBillForm").trigger('submit');
}

function downloadBill(cusCode,cusName,curIndex,restBoxNum){
	if(boo){
		var regu = "^([0-9]*[.0-9])$";
		var re = new RegExp(regu);
		var num = $("#downNum"+curIndex).val();
		if(num.search(re) != -1){
			if(parseInt(num) < 1){
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '下载箱数必须大于等于1！'
				});
				return;
			}
			if(parseInt(num) > restBoxNum){
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '剩余箱数不足，下载箱数不能大于剩余箱数！'
				});
				return;
			}
			if(parseInt(num) > 50){
				var loadingDialog = new Dialog();
				loadingDialog.init({
					closeBtn : true,
					contentHtml : '下载箱数不能超过50箱！'
				});
				return;
			}
		}else{
			var loadingDialog = new Dialog();
			loadingDialog.init({
				closeBtn : true,
				contentHtml : '下载箱数请输入整数！'
			});
			return;
		}

		$("#cusCode").val(cusCode);
		$("#cusName").val(cusName);
		$("#downNum").val(num);
		
		var nRemind = $("#needRemind").val();
		if(nRemind == 0){
			$("#downloadBillForm").trigger('submit');
			boo = false;
			$("#downloadBtn"+curIndex).attr("style","color:#808080");
			setTimeout(function(){
				$("#downloadBtn"+curIndex).attr("style","color:#1e5691");
				boo = true;
				$("#q_form").trigger('submit');
			},30000);
		}else{
			var content = '<div id="J_tip" class="tip">' +
			  '	<h1>电子面单下载风险提示</h1>' +
			  '	<div class="con_tip">' +
			  '		<p>如果分公司申请使用电子面单号下载并交给客户自行打印模式，将会给分公司带来以下风险：</p>' +
			  '		<p>1.快件信息不能自动录单，需分公司把快件信息手工导入录单系统，否则快件在运输途中产生问题件，仲裁部不予受理；</p>' +
			  '		<p>2.面单号导出交给客户自行打印时容易造成同一个面单号重复打印和或未使用面单号遗漏使用情况；</p>' +
			  '		<p>以上风险由申请使用电子面单号下载并交给客户自行打印模式分公司自行承担，总公司不承担因分公司申请面单号导出使用所产生的任何责任。</p>' +
			  '	</div>' +
			  '	<div class="tip_var clearfix">' +
			  '		<span class="tip_fl">风险提示关乎您的切身利益，请务必认真阅读！！！</span>' +
			  '		<span class="tip_fr">' +
			  '		<input type="checkbox" id="isNeedCloseRemind"/>' +
	    	  '		<label>下次不再提示</label>' +
			  '		</span>' +
			  '	</div>' +
			  '</div>';
			var nDialog = new Dialog();
			nDialog.init({
				contentHtml: content,
				yesVal: '我知道了',
				yes: function(){
					var chk = $('#isNeedCloseRemind');
					if(chk.prop("checked")==false){//未选中
						$("#needCloseRemind").val("0");
					}else{
						$("#needCloseRemind").val("1");
					}
					
					$("#downloadBillForm").trigger('submit');
					nDialog.close();
					
					boo = false;
					$("#downloadBtn"+curIndex).attr("style","color:#808080");
					setTimeout(function(){
						$("#downloadBtn"+curIndex).attr("style","color:#1e5691");
						boo = true;
						$("#q_form").trigger('submit');
					},30000);
				},
				noVal:'暂不下载',
				no:function(){
					nDialog.close();
				}
			});
		}
	}
}

$(function(){
	$("#sear_btn").click(function(){
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
                	<input type="hidden" id="needRemind" value="${needRemind}"/>
                	<form method="post" id="downloadBillForm" action="waybillExport_waybillExport.action?currentPage=1&menuFlag=dzmd_mdxz">
                		<input type="hidden" id="cusCode" value="${customerCode}" name="customerCode"/>
                		<input type="hidden" id="cusName" name="customerName" value=""/>
                		<input type="hidden" id="downNum" name="downloadBoxNum" value=""/>
                		<input type="hidden" id="needCloseRemind" name="needCloseRemind" value="0"/>
                	</form>
                	<form method="post" id="recordBillForm" action="waybillExport_queryExportLog.action?currentPage=1&menuFlag=dzmd_mdxz">
                		<input type="hidden" id="cusRecoreCode" value="" name="customerCode"/>
                		<input type="hidden" id="cusStartPage"  value="1" name="pageSize"/>
                		<input type="hidden" id="cusLimit" value="10" name="limit"/>
                	</form>
                    <form class="form" method="post" id="q_form" action="waybillExport_userInfoLoad.action?currentPage=1&menuFlag=dzmd_mdxz">
                        <p>
                            <span class="clearfix">
                                <label>商家代码：</label>
                                <select class="select_text" id="search_cuscode" name="customerCode">
                                    <option value="0">所有商家</option>
                                    <c:forEach var="itemCode" items="${userWaybillInfoList}" varStatus="status">
                                    	<option value="${itemCode.customerCode}">${itemCode.customerName}(${itemCode.customerCode})</option>
                                    </c:forEach>
                                </select>
                            </span>
                            <a id="sear_btn" class="btn btn_a" title="查 询" href="javascript:;">
                                <span>查 询</span>
                            </a>
                        </p>
                        <p><font color="red">(提醒：如对同一商家面单进行连续两次下载，请您间隔30秒再试！)</font></p>
                    </form>
                </div>
            </div>
            <div class="table" id="table_c">
                <table>
                    <thead>
                    <tr>
                        <th class="th_a first_th">
                            <div class="th_title"><em>商家代码</em></div>
                        </th>
                        <th class="th_b">
                            <div class="th_title"><em>面单箱数</em></div>
                        </th>
                        <th class="th_c">
                            <div class="th_title"><em>面单个数</em></div>
                        </th>
                        <th class="th_d">
                            <div class="th_title"><em>剩余箱数</em></div>
                        </th>
                        <th class="th_e">
                            <div class="th_title"><em>剩余个数</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>下载箱数</em></div>
                        </th>
                        <th class="th_g last_th">
                            <div class="th_title"><em>操作</em></div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${userWaybillInfoList}" varStatus="status">
                    	<tr class="list_tr">
	                        <td><c:out value="${item.customerName}"/>(<c:out value="${item.customerCode}"/>)</td>
	                        <td><c:out value="${item.totalBoxNum}"/></td>
	                        <td><c:out value="${item.totalCount}"/></td>
	                        <td><c:out value="${item.remainBoxNum}"/></td>
	                        <td><c:out value="${item.remainCount}"/></td>
	                        <td><input type="text" class="input_text" id="downNum${status.index}" value="${item.canUsedBoxNum}" 
	                        	onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" 
                        		onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/></td>
	                        <td>
	                            <a id="downloadBtn${status.index}" onclick="downloadBill('${item.customerCode}','${item.customerName}','${status.index}','${item.remainBoxNum}')" href="javascript:;">面单下载</a>
	                            <a onclick="recordBill('${item.customerCode}')" href="javascript:">查看记录</a>
	                        </td>
	                    </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>