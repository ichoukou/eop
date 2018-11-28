<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--<%@ include file="/WEB-INF/common/meta.jsp" %>-->
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <title>commandOrder</title>
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${jsPath}/orderCommand/calendar/skin/WdatePicker.css">
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="${cssPath}/orderCommand/orderCommand.css?d=${str:getVersion() }" media="all"/>
    <!--------------当前页面css--------------->
    <style type="text/css">
    	.command{display:none;}
    	.commandContent{width:700px;word-break:break-all;word-wrap:break-word;}
    </style>
 
</head>
<body>
<div id="content">
    <div class="clearfix" id="content_bd">
        <div>
            <!-- S Box -->
            <div id="box_form" class="box box_a">
                <div class="box_bd">
                    <form id="orderCommandForm" class="form" method="post" id="q_form" action="ordercommand_list.action?menuFlag=shujuchakan_ordercommand">
                    <input  type="hidden" id="currentPage" name="currentPage" value="<s:property value='currentPage'/>"/>
                    <input  type="hidden" id="url" value='<s:property value="url"/>'>
                        <p>
                             <span class="">
                                <label>订单类型：</label>
                                <select class="select_inputcommandType" name="inputcommandType">
                                 	<option value="">----请选择----</option>
                                 	<s:if test="#request.inputcommandType == 10">
                                    	<option value="10" selected="selected">10.订单创建</option>
                                    </s:if>
                                    <s:else>
                                    	<option value="10">10.订单创建</option>
                                    </s:else>
                                    
                                    <s:if test="#request.inputcommandType == 20">
                                    	<option value="20" selected="selected">20.订单合并</option>
                                    </s:if>
                                    <s:else>
                                    	<option value="20">20.订单合并</option>
                                    </s:else>
                                    
                                    <s:if test="#request.inputcommandType == 30">
                                    	<option value="30" selected="selected">30.订单绑定</option>
                                    </s:if>
                                    <s:else>
                                    	<option value="30">30.订单绑定</option>
                                    </s:else>
                                    
                                    <s:if test="#request.inputcommandType == 40">
                                    	<option value="40" selected="selected">40.订单取消</option>
                                    </s:if>
                                    <s:else>
                                    	<option value="40">40.订单取消</option>
                                    </s:else>
                                    
                                    <s:if test="#request.inputcommandType == 50">
                                    	<option value="50" selected="selected">50.订单修改</option>
                                    </s:if>
                                    <s:else>
                                    	<option value="50">50.订单修改</option>
                                    </s:else>
                                    
                                    <s:if test="#request.inputcommandType == 60">
                                    	<option value="60" selected="selected">60.订单查询</option>
                                    </s:if>
                                    <s:else>
                                    	<option value="60">60.订单查询</option>
                                    </s:else>
                                </select>
                            </span>
                            <span>
                                 <label class="m-tar">创建时间：</label>
                                <input type="text"  name="inputStartTime" class="Wdate" id="startTime">
                                &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
                                <input type="text"  name="inputEndTime" class="Wdate" id="endTime">
                                <span id="dateTip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent; display: none;"></span>
                            </span>
                        </p>
                        <p>
                            <span class="">
                                <label>线上/线下：</label>
                                <select class="select_inputIsOffline" name="inputIsOffline">
                                    <option value="">----请选择----</option>
                                    <option value="0">线上</option>
                                    <option value="1">线下</option>
                                </select>
                            </span>
                            <span class="">
                                <label class="m-tar" >订单状态：</label>
                                <select class="select_inputOrderType" name="inputOrderType">
                                   <option value="">----请选择----</option>
                                   <option value="1">异常订单</option>
                                   <option value="0">正常订单</option>
                                </select>
                            </span>
                            <a id="sear_btn" class="btn btn_a" title="查 询" href="javascript:;" onclick="formSubmit();">
                                <span>查 询</span>
                            </a>
                        </p>
                    </form>
                </div>
            </div>
           
        </div>
		 <div class="table" id="table_c">
                <table>
                    <thead>
                    <tr>
                        <th class="th_b first_th">
                            <div class="th_title"><em>commandType</em></div>
                        </th>
                        <th class="th_c">
                            <div class="th_title"><em>createTime</em></div>
                        </th>
                        <th class="th_d">
                            <div class="th_title"><em>logisticsCode</em></div>
                        </th>
                        <th class="th_e">
                            <div class="th_title"><em>channelCode</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>status</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>isOffline</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>orderType</em></div>
                        </th>
                        <th class="th_g last_th">
                            <div class="th_title"><em>commandContent</em></div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <s:if test="#request.jgOrderDTOList!=null || #request.jgOrderDTOList.size() > 0">
                    <s:iterator value="#request.jgOrderDTOList" status="stuts" var="jgDTO">
	                    <tr class="list_tr">
	                        <td><s:property value="#jgDTO.commandType"/></td>
	                        <td><s:property value="#jgDTO.createTime.substring(0,19)"/></td>
	                        <td><s:property value="#jgDTO.orderLogisticsCode"/></td>
	                        <td><s:property value="#jgDTO.orderChannelCode"/></td>
	                        <td><s:property value="#jgDTO.status"/></td>
	                        <td><s:property value="#jgDTO.isOffline"/></td>
	                        <td><s:property value="#jgDTO.orderType"/></td>
	                        <td>
		                        <s:if test="#jgDTO.commendContent!='' && #jgDTO.commendContent!=null">
			                        <a class="a_commandContent" href="javascript:;">
			                        		<s:if test="#jgDTO.commendContent!=null && #jgDTO.commendContent.length() < 5">
				                            	<s:property value="#jgOrderDTO.commendContent"/>
				                            </s:if>
				                            <s:if test="#jgDTO.commendContent!=null && #jgDTO.commendContent.length() >= 5">
				                            	<s:property value="#jgDTO.commendContent.substring(7,20)"/>
				                            </s:if>
				                    </a>
				                    <div class="command"> 
				                    	<div class="commandContent"><s:property value="#jgDTO.commendContent"/> </div>
				                    </div>
				                </s:if>
		                    </td>
	                    </tr>
                    </s:iterator>
                    </s:if>
                    	<s:if test="#request.jgOrderDTOList==null || #request.jgOrderDTOList.size() == 0">
			                <tr>
			                   <td colspan="8" align="center">
					                                    抱歉，没有找到数据！<br>
					                                    联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
			                   </td>
			                </tr>
		               </s:if>	
                    </tbody>
                </table>
            </div>
             <!-- S PageNavi -->
			<div class="pagenavi">
				<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
			</div>
		<!-- E PageNavi -->
    </div>
</div>
</body>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/orderCommand/calendar/WdatePicker.js"></script>
    <script type="text/javascript" src="${jsPath}/orderCommand/time.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/page/orderCommand.js?d=${str:getVersion() }"></script>
       <script type="text/javascript">
       window.onload = function() {
       	var inputStartTime='<%=request.getAttribute("inputStartTime")%>';
       	var inputEndTime='<%=request.getAttribute("inputEndTime")%>';
       	var inputOrderType='<%=request.getAttribute("inputOrderType")%>';
       	var inputcommandType='<%=request.getAttribute("inputcommandType")%>';
       	var inputIsOffline='<%=request.getAttribute("inputIsOffline")%>';
       	$(".select_inputOrderType").val(inputOrderType);
       	$(".select_inputcommandType").val(inputcommandType);
    	$(".select_inputIsOffline").val(inputIsOffline);
       	if(inputStartTime !='null'){
       		$("#startTime").attr("value", inputStartTime);
       	}
       	if(inputEndTime !='null'){
       		 $("#endTime").attr("value", inputEndTime);
       	}
       }
    	function formSubmit(){
    		$("#orderCommandForm").submit();
    	}
    	$(function(){
        	$(".a_commandContent").click(function(){
        		var html=$(this).next().html();
        		var loahtdingDialog = new Dialog();
        		loahtdingDialog.init({
    	        	closeBtn : true,
    	        	contentHtml: html
    	        });
        	});
    	})
    </script>
</html>
