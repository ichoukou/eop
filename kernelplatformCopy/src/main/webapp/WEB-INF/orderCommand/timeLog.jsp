<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <title>page1</title>
   	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
    <link href="../../js/calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css">
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="../../css/orderCommand/timeLog.css" />
    <!--------------当前页面css--------------->
    <script type="text/javascript" src="../../js/lib/jquery-1.7.min.js"></script>
    <script type="text/javascript" src="../../js/orderCommand/calendar/WdatePicker.js"></script>
    <script type="text/javascript" src="../../js/orderCommand/time.js"></script>
    <script type="text/javascript">
    	function formSubmit(){
    		$("#q_form").submit();
    	}
    </script>
</head>
<body>
<div id="content">
    <div class="clearfix" id="content_bd">
        <div>
            <!-- S Box -->
            <div id="box_form" class="box box_a">
                <div class="box_bd">
                    <form class="form" method="post" id="q_form" action="timelog_list.action?currentPage=1&menuFlag=peizhi_timerlog_list">
                        <p>
                            <label class="m-tar">创建时间：</label>
                            <input type="text" value="" name="startTime" class="Wdate" id="startTime">
                            &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
                            <input type="text" value="" name="endTime" class="Wdate" id="endTime">
                            <span id="dateTip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent; display: none;"></span>
                            <a id="sear_btn" class="btn btn_a" title="查 询" onclick="formSubmit();">
                                <span>查 询</span>
                            </a>
                        </p>
                        <p>
                            <span class="">
                                <label>操作类型：</label>
                                <select class="select_text" name="operate" >
                                    <option value="">----请选择----</option>
                                    <option value="0">0,CREATE</option>
                                    <option value="1">1,DELETE</option>
                                    <option value="2">2,UPDATE</option>
                                </select>
                            </span>
                            <span class="">
                                <label class="m-tar">部署位置：</label>
                                <select class="select_text" name="timerNO">
                                   <option value="">----请选择----</option>
                                   <option value="33_CREATE">33_CREATE</option>
                                   <option value="34_CREATE">34_CREATE</option>
                                   <option value="35_CREATE">35_CREATE</option>
                                   <option value="36_CREATE">36_CREATE</option>
                                   <option value="37_CREATE">37_CREATE</option>
                                   <option value="43_CREATE">43_CREATE</option>
                                   <option value="44_CREATE">44_CREATE</option>
                                   <option value="45_CREATE">45_CREATE</option>
                                   <option value="46_CREATE">46_CREATE</option>
                                   <option value="47_CREATE">47_CREATE</option>
                                   <option value="33_UPDATE">33_UPDATE</option>
                                   <option value="34_UPDATE">34_UPDATE</option>
                                   <option value="35_UPDATE">35_UPDATE</option>
                                   <option value="36_UPDATE">36_UPDATE</option>
                                   <option value="37_UPDATE">37_UPDATE</option>
                                   <option value="43_UPDATE">43_UPDATE</option>
                                   <option value="44_UPDATE">44_UPDATE</option>
                                   <option value="45_UPDATE">45_UPDATE</option>
                                   <option value="46_UPDATE">46_UPDATE</option>
                                   <option value="47_UPDATE">47_UPDATE</option>
                                </select>
                            </span>
                            <span class="">
                                <label class="m-tar">错误类型：</label>
                                <select class="select_text" name="isError">
                                    <option value="">----请选择----</option>
                                    <option value="1">错误</option>
                                    <option value="0">正常</option>
                                </select>
                            </span>
                            <span class="">
                                <label>总数:</label>
                                <a>${num }</a>
                            </span>
                        </p>
                    </form>
                </div>
            </div>
            <div class="table" id="table_c">
                <table>
                    <thead>
                    <tr>
                        <th class="th_b first_th">
                            <div class="th_title"><em>tableNa</em></div>
                        </th>
                        <th class="th_c">
                            <div class="th_title"><em>operate</em></div>
                        </th>
                        <th class="th_d">
                            <div class="th_title"><em>num</em></div>
                        </th>
                        <th class="th_e">
                            <div class="th_title"><em>errorNum</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>useTime</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>timerNO</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>start_time</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>endTime</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>createTime</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>isError</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>message</em></div>
                        </th>
                        <th class="th_g last_th">
                            <div class="th_title"><em>dataFrom</em></div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <s:iterator value="#timeLogList" status="stuts" var="loglist">
	                    <tr class="list_tr">
	                        <td><s:property value="#loglist.tableName"/></td>
	                        <td><s:property value="#loglist.operate"/></td>
	                        <td><s:property value="#loglist.num"/></td>
<%-- 	                        <td><s:property value="#list.errorNum"/></td> --%>
<%-- 	                        <td><s:property value="#list.useTime"/></td> --%>
<%-- 	                        <td><s:property value="#list.ltimerNO"/></td> --%>
<%-- 	                        <td><s:property value="#list.lstartTime"/></td> --%>
<%-- 	                        <td><s:property value="#list.endTime"/></td> --%>
<%-- 	                        <td><s:property value="#list.createTime"/></td> --%>
<%-- 	                        <td><s:property value="#list.isError"/></td> --%>
<%-- 	                        <td><s:property value="#list.message"/></td> --%>
<%-- 	                        <td><s:property value="#list.dataFrom"/></td> --%>
	                    </tr>
                    </s:iterator>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- S PageNavi -->
			<div class="pagenavi">
				<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
			</div>
		<!-- E PageNavi -->
    </div>
</div>
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
</body>

</html>