<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <title>timeLog</title>
   	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
   	<link rel="stylesheet" type="text/css" href="${jsPath}/orderCommand/calendar/skin/WdatePicker.css">
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="${cssPath}/orderCommand/timeLog.css?d=${str:getVersion() }" media="all"/>
    <!--------------当前页面css--------------->
</head>
<body>
<div id="content">
    <div class="clearfix" id="content_bd">
        <div>
            <!-- S Box -->
            <div id="box_form" class="box box_a">
                <div class="box_bd">
                    <form id="timelog" class="form" method="post" action="timelog_list.action?menuFlag=shujuchakan_timeLog">
                    <input  type="hidden" id="currentPage" name="currentPage" value="<s:property value='currentPage'/>"/>
                    <input  type="hidden" id="url" value='<s:property value="url"/>'>
                        <p>
                            <label class="m-tar">创建时间：</label>
                            <input type="text" value="" name="inputstartTime" class="Wdate" id="startTime">
                            &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;
                            <input type="text" value="" name="inputendTime" class="Wdate" id="endTime">
                            <span id="dateTip" style="margin: 0px; padding: 0px; background: none repeat scroll 0% 0% transparent; display: none;"></span>
                            <a id="sear_btn" class="btn btn_a" title="查 询" onclick="formSubmit();">
                                <span>查 询</span>
                            </a>
                        </p>
                        <p>
                            <span class="">
                                <label>操作类型：</label>
                                <select class="select_inputoperate" name="inputoperate" >
                                    <option value="">----请选择----</option>
                                    <option value="0">0,CREATE</option>
                                    <option value="1">1,DELETE</option>
                                    <option value="2">2,UPDATE</option>
                                </select>
                            </span>
                            <span class="">
                                <label class="m-tar">部署位置：</label>
                                <select class="select_inputtimerNO" name="inputtimerNO">
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
                                <select class="select_inputisError" name="inputisError">
                                    <option value="">----请选择----</option>
                                    <s:if test="#request.inputisError == '1' ">
                                    	<option value="1" selected="selected">错误</option>
                                     </s:if>
                                    <s:else>
                                    	<option value="1">错误</option>
                                    </s:else>
                                    <s:if test="#request.inputisError == '0' ">
                                    	<option value="0" selected="selected">正常</option>
                                    </s:if>
                                    <s:else>
                                    	<option value="0">正常</option>
                                    </s:else>
                                </select>
                            </span>
                            <span class="">
                                <a style="text-decoration:none;background:#BBFFEE;opacity:0.7;font-weight:bolder;font-size:20px;color:#003377">num总数:</a>
                                <a style="font:700 16px/1.4 Arial;color:#61B610;text-decoration: underline;font-weight: bold;">${num }</a>
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
                            <div class="th_title"><em>tabNa</em></div>
                        </th>
                        <th class="th_c">
                            <div class="th_title"><em>operate</em></div>
                        </th>
                        <th class="th_d">
                            <div class="th_title"><em>num</em></div>
                        </th>
                        <th class="th_e">
                            <div class="th_title"><em>errNum</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>useTime</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>timerNO</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>startTime</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>endTime</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>createTime</em></div>
                        </th>
                        <th class="th_f">
                            <div class="th_title"><em>isErr</em></div>
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
                    <s:if test="#request.timeLogList!=null || #request.timeLogList.size() > 0">
                    <s:iterator value="#request.timeLogList" status="stuts" var="loglist">
	                    <tr class="list_tr">
	                        <td><s:property value="#loglist.tableName"/></td>
	                        <td><s:property value="#loglist.operate"/></td>
	                        <td><s:property value="#loglist.num"/></td>
	                        <td><s:property value="#loglist.errorNum"/></td>
	                        <td><s:property value="#loglist.useTime"/></td>
	                        <td><s:property value="#loglist.timerNO"/></td>
	                        <td><s:date name="#loglist.startTime" format="yyyy/MM/dd HH:mm"/></td>
	                        <td><s:date name="#loglist.endTime" format="yyyy/MM/dd HH:mm"/></td>
	                        <td><s:date name="#loglist.createTime" format="yyyy/MM/dd HH:mm"/></td>
	                        <td><s:property value="#loglist.isError"/></td>
	                        <td id="destination_<s:property value="#loglist.id"/>" class="td_g" 
	                        	onMouseOver="show('infoDiv1_<s:property value="#loglist.id"/>');this.style.cursor= 'pointer'" 
	                        	onMouseOut="hide('infoDiv1_<s:property value="#loglist.id"/>')">
	                        	<s:if test="#loglist.message!=null&&#loglist.message.length() <= 5">
	                            	<s:property value="#loglist.message"/>
	                            </s:if>
	                            <div class="td_bd" style="position:absolute">
	                            <s:if test="#loglist.message!=null&&#loglist.message.length() > 5">
	                            	<s:property value="#loglist.message.substring(0,5)"/>...
	                            </s:if>
	                            <s:if test="#loglist.message!='' && #loglist.message.length() >0">
	                            
		                            <div id="infoDiv1_<s:property value='#loglist.id'/>"  style="display:none;">
	 	                            		<s:property value="#loglist.message"/> 
	 	                            </div>
 	                            
 	                            </s:if> 
 	                            </div>
	                        </td>
	                        <td><s:property value="#loglist.dataFrom"/></td>
	                    </tr>
                    	</s:iterator>
                    	</s:if>
                    	<s:if test="#request.timeLogList==null || #request.timeLogList.size() == 0">
			                <tr>
			                   <td colspan="12" align="center">
					                                    抱歉，没有找到数据！<br>
					                                    联系电话：021-64703131-107   旺旺群：548569297  qq群：204958092
			                   </td>
			                </tr>
		               </s:if>
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
	<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/orderCommand/calendar/WdatePicker.js?d=${str:getVersion()}"></script>
    <script type="text/javascript" src="${jsPath}/orderCommand/time.js?d=${str:getVersion() }"></script>
    <script type="text/javascript" src="${jsPath}/page/timelog.js?d=${str:getVersion() }"></script>
    <script type="text/javascript">
    window.onload = function() {
    	var inputStartTime='<%=request.getAttribute("inputstartTime")%>';
    	var inputEndTime='<%=request.getAttribute("inputendTime")%>';
    	var inputoperate='<%=request.getAttribute("inputoperate")%>';
    	var inputtimerNO='<%=request.getAttribute("inputtimerNO")%>';
    	var inputisError='<%=request.getAttribute("inputisError")%>';
       	$(".select_inputoperate").val(inputoperate);
    	$(".select_inputtimerNO").val(inputtimerNO);
    	$(".select_inputisError").val(inputisError);
    	if(inputStartTime !='null'){
    		$("#startTime").attr("value", inputStartTime);
    	}
    	if(inputEndTime !='null'){
    		 $("#endTime").attr("value", inputEndTime);
    	}
    	 
    }
    	function formSubmit(){
    		$("#timelog").submit();
    	}
    	function show(id){
    		$("#"+id).show();
    		$("#"+id).css({
    			"background":"rgb(255, 239, 178)",
    			"border":"1px solid #000000",
    			"padding":"5px",
    			"position":"absolute",
    			"top":"25px",
    			"width":"135px",
    			"z-index":"999"
           });
    	}
    	function hide(id){
    		$("#"+id).hide();
    	}
    	
    </script>
</body>

</html>