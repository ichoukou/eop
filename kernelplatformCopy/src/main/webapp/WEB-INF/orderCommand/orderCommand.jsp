<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<%@ include file="/WEB-INF/common/meta.jsp" %>
    <%@ include file="/WEB-INF/common/taglibs.jsp"%>
    <meta charset="utf-8"/>
    <title>page2</title>
    <<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
    <link href="../../js/orderCommand/calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css">
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="../../css/orderCommand/orderCommand.css" />
    <!--------------当前页面css--------------->
    <script type="text/javascript" src="../../js/lib/jquery-1.4.3.js"></script>
    <script type="text/javascript" src="../../js/lib/jquery-1.7.min.js"></script>
    <script type="text/javascript" src="../../js/orderCommand/calendar/WdatePicker.js"></script>
    <script type="text/javascript" src="../../js/orderCommand/time.js"></script>
</head>
<body>
<div id="content">
    <div class="clearfix" id="content_bd">
        <div>
            <!-- S Box -->
            <div id="box_form" class="box box_a">
                <div class="box_bd">
                    <form class="form" method="post" id="q_form" action="">
                        <p>
                             <span class="">
                                <label>订单类型：</label>
                                <select class="select_text">
                                    <option>请选择</option>
                                </select>
                            </span>
                            <span>
                                 <label class="m-tar">创建时间：</label>
                                <input type="text" value="2013-10-15" name="startDateDelivered" class="Wdate" id="startTime">
                                &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
                                <input type="text" value="2013-10-15" name="endDateDelivered" class="Wdate" id="endTime">
                                <span id="dateTip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent; display: none;"></span>
                            </span>
                        </p>
                        <p>
                            <span class="">
                                <label>线上/线下：</label>
                                <select class="select_text">
                                    <option>请选择</option>
                                </select>
                            </span>
                            <span class="">
                                <label class="m-tar">订单状态：</label>
                                <select class="select_text">
                                    <option>请选择</option>
                                </select>
                            </span>
                            <a id="sear_btn" class="btn btn_a" title="查 询" href="javascript:;">
                                <span>查 询</span>
                            </a>
                        </p>
                    </form>
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
                    <tr class="list_tr">
                        <td>10</td>
                        <td>2013-10-28 16:00:54</td>
                        <td>LP00018249041740</td>
                        <td>TAOBAO_STD</td>
                        <td>1</td>
                        <td>1</td>
                        <td>0</td>
                        <td><a href="javascript:">{"ip":"110.7......</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
</html>
