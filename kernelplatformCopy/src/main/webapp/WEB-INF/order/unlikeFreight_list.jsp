<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />

<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/tzfreight.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>运费调整</title>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/tzfreight.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
<script type="text/javascript">
function Show(divid) {
    if($("#"+divid).text()!="")
        document.getElementById(divid).style.visibility = "visible";
}
function Hide(divid) {
    document.getElementById(divid).style.visibility = "hidden";
}
</script>

        <!-- S Content -->
        <div id="content">
            <div id="content_hd" class="clearfix">
<!--                 <h2 id="message_icon">运费调整</h2> -->
                <em><font style="color:black;font-size: 12px;"  >转EMS？调价格？来这里！ <a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></font> </em>
                <a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=site_7_carriage_change" target="_blank" >
	                <span>快速入门</span>
	            </a>
               
                <p>填写运单号→查找出订单→填写价格调整原因→填写调整后价格→通知客户</p>
            </div>
            <div id="content_bd" class="clearfix">
                <!-- S Tab -->
                <div class="tab tab_a">
                    <div class="tab_triggers">
                        <ul>
                            <li><a href="javascript:;" class="adjustClass" >调整</a></li>
                            <li>
                            <a href="javascript:;" class="managerClass">管理</a>
                            </li>
                        </ul>
                    </div>
                    <div class="tab_panels">
                    	<c:if test="${managerTab == '0'}">
                        <div class="tab_panel">
                            <form class="form" action="#" id="adjust_form" method="post" >
                            	<input type="hidden" name="menuFlag" value="${menuFlag }" />
                            	<input type="hidden" id="flag" value="0"/>
								<input type="hidden" id="traderInfo_prov" name="traderInfo.prov">
								<input type="hidden" id="traderInfo_city" name="traderInfo.city">
								<input type="hidden" id="traderInfo_district" name="traderInfo.district">

								<input type="hidden" id="traderInfo_numProv" name="traderInfo.numProv">
								<input type="hidden" id="traderInfo_numCity" name="traderInfo.numCity">
								<input type="hidden" id="traderInfo_numDistrict" name="traderInfo.numDistrict">
								<p>
									<label for="nub" style="text-align:left;"><span class="req">*</span>运单号：</label>
									<input id="nub" class="input_text" type="text"/>
									<span id="nubTip"></span>
									<input type="hidden" id="editmailNo" name="unlikeFreight.mailNo">
								</p>
								<p>
									<label for="province" style="text-align:left;"><span class="req">*</span>目的地：</label>
									<select id="province"></select>
									<span id="area_tip"></span>
									<input type="hidden" name="prov" id="x_prov" value='<s:property value="prov"/>'/>
									<input type="hidden" name="city" id="x_city" value='<s:property value="city"/>'/>
									<input type="hidden" name="district" id="x_district" value='<s:property value="district"/>'/>
								</p>
								<p>
									<label for="detail_address" style="text-align:left;"><span class="req">*</span>街道地址：</label>
									<textarea cols="51" rows="2" class="textarea_text" id="detail_address"></textarea>
									<span id="detail_addressTip"></span>
									<input  type="hidden" id="unlikeFreight_address" name="unlikeFreight.address" />
								</p>
								<p>
									<label for="username" style="text-align:left;"><span class="req">*</span>客户名称：</label>
									<s:property value="userName"/>
									<input  type="hidden" id="unlikeFreight_userName" name="unlikeFreight.userName"/>
									<select id="userCode" name="unlikeFreight.userCode">
                                    	<c:if test="${fn:length(vipThreadList) == 0}"><option value="暂无客户">暂无客户</option>：</c:if>
                                        <s:iterator value="vipThreadList">
                                           <option optionLabel="<s:property value='userName'/>" <s:if test="unlikeFreight.userCode == userCode">selected</s:if>  value="<s:property value='userName'/>=<s:property value='userCode'/>"><s:property value="userName"/>(<s:property value="userCode"/>)</option>
                                        </s:iterator>
                                    </select>
								</p>
								<p>
									<label for="reason" style="text-align:left;"><span class="req">*</span>调整原因：</label>
									<select id="reason" name="unlikeFreight.reason">
                                        <option value="超区转EMS">超区转EMS</option>
                                        <option value="其他原因转EMS">其他原因转EMS</option>									
                                    </select>
                                    <input id="elseReason" style="display:none" class="input_text" type="text" maxlength="200" />
                                    <span id="reasonTip"></span>
								</p>
								<p>
									<label for="weight" style="text-align:left;"><span class="req">*</span>重量：</label>
									<input id="weight" name="unlikeFreight.weight" class="input_text" type="text" > kg
									<span id="weightTip"></span>
								</p>
								<p>
									<label for="prices" style="text-align:left;"><span class="req">*</span>调整后总价：</label>
									<input id="prices" maxlength="7" name="unlikeFreight.price" class="input_text" type="text" > 元
									
									<span id="pricesTip"></span>
								</p>
								<p>
									<label for="date" style="text-align:left;"><span class="req">*</span>发货日期：</label>
									<input id="date" name="unlikeFreight.goodsTime" class="Wdate" type="text" />
									<span id="dateTip"></span>
								</p>
								<p><a class="btn btn_b" id="notice_btn" title="通知客户" href="javascript:;"><span>通知客户</span></a></p>
							</form>
                            <!-- E Form Validator -->
                        </div>
                        </c:if>

                        <div class="tab_panel">
	                            <form id='manage_form' class="form" action='#' method='post'>
	                            	<input type="hidden" name="menuFlag" value="${menuFlag }" />
	                            	<input type="hidden" id="managerTab" name="managerTab" value="<s:property value='managerTab' />" />
	                            	<input type="hidden" id="currentPage" name="currentPage"  value='<s:property value="currentPage" />'/>
		                            <input type="hidden" id="flag1" name="flag" value="<s:property value='flag'/>" />
									<input type="hidden" id="orderFlag" name="orderFlag" value="0"/>
	                                <input type="hidden" id="unlikeFreight_id" name="unlikeFreight.id" value=""/>
									<c:if test="${yto:getCookie('userType') == 2
                                              || yto:getCookie('userType') == 21
                                              || yto:getCookie('userType') == 22
                                              || yto:getCookie('userType') == 23}">
                                      <p>
                                          <span>发货时间：</span>
                                          <input type="text" id="starttime" class="Wdate" value="<s:property value='startTime'/>">　至　
                                          <input type="text" id="endtime" class="Wdate" value="<s:property value='endTime'/>">&nbsp;
                                          <span id="date_tip"></span>
                                          <input type="hidden" id="startTmHid" name="startTime" value="<s:property value='startTime'/>" />
                                          <input type="hidden" id="endTmHid" name="endTime" value="<s:property value='endTime'/>" />
                               		  </p>
                               		  <p>
                               		  	  <span>客户账号：</span>
                                          <select id="tzuserCodeForTime" >
										       <c:if test="${fn:length(vipThreadList) == 0}"><option value="暂无客户">暂无客户</option>：</c:if>
                                              <s:iterator value="vipThreadList">
                                                  <option <s:if test="%{userCodeForTime == userCode}">selected</s:if>  value="<s:property value='userCode'/>"><s:property value="userName"/>(<s:property value="userCode"/>)</option>
                                              </s:iterator>
                                          </select>
                                          <input type="hidden" id="userCodeHid" name="userCodeForTime" value="<s:property value='userCodeForTime'/>"/>
                               		  </p>
                                	</c:if>
									<p style="position:relative;">
										<textarea id="textarea_freight" class="textarea_text" onfocus='if (this.value == "请输入想要调整的运单号!") this.value = "";'><s:if test="logisticsIds==null"></s:if><s:if test="logisticsIds!=null&&logisticsIds!=''"><s:property value="logisticsIds"/></s:if></textarea>
										<a id="yingcang" href="#" style="position:absolute;top:48px;left:588px;"><img style="padding-left:4px" src="${imagesPath}/single/closebtn.png" /></a>
										<span id="textarea_freightTip"></span>
										<input type="hidden" id="isCheck" name="isCheck" value="<s:property value='isCheck'/>" />
										<input type="hidden" name="logisticsIds" id="logisticsIds" value="<s:property value='logisticsIds'/>" />
									</p>
									<p>
										<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查 询"><span>查 询</span></a> (支持多单号查询,输入数字与字母组合运单号,用斜杠“/”分隔)
									</p>
								</form>
                                <p>
                                <div class="table" >
                                    <table>
                                        <thead>
                                            <tr>
                                                <th class="th_title th_id"><div><em>运单号</em></div></th>
                                                <th class="th_title th_d"><div><em>目的地</em></div></th>
                                                <th class="th_title th_n"><div><em>客户名称</em></div></th>
                                                <th class="th_title th_p"><div><em>价格调整原因</em></div></th>
                                                <th class="th_title th_w"><div><em>重量</em></div></th>
                                                <th class="th_title th_s"><div><em>发货日期</em></div></th>
                                                <th class="th_title th_c"><div><em>调整后价格</em></div></th>
                                                <th class="th_title th_o"><div><em>操作</em></div></th>
                                            </tr>
                                        </thead>
                                        <tbody>

                                        <s:iterator value="unlikeFreightOrderList" var="unlikeFreight" status="orderIndex">
                                            <tr>
                                                <td><a href="javascript:;" title="查看运单详情" class="mailno" val="<s:property value='mailNo'/>" style="text-decoration:underline;color:blue" ><s:property value="mailNo"/></a> </td>
                                                <td onMouseOver="Show('orderIndex_<s:property value="id"/>'); this.style.cursor= 'pointer'" onMouseOut="Hide('orderIndex_<s:property value="id"/>')">
                                                	<s:if test="%{address.length() <= 16}" >
	                                                        <s:property value="address"/>
	                                                    </s:if>
	                                                    <s:if test="%{address.length() > 16}">
	                                                        <s:property value="address.substring(0,10)"/>...
	                                                    </s:if>
                                                	<div id="orderIndex_<s:property value='id'/>" class="article" style="width:140px;"><s:property value="address"/></div>
                                                </td>
                                                <td>
													<span class="customer_name">
														<s:property value="userName"/>
													</span>
												</td>
                                                <td><span class="reason_block"><s:property value="reason"/></span></td>
                                                <td><s:if test="weight == 0">≤1</s:if><s:else><s:property value="weight"/></s:else></td>
                                                <td><s:date name="goodsTime" format="yyyy-MM-dd"/></td>
                                                <td><s:property value="price"/></td>
                                                <td><a href="javascript:;" id="cancelAdjust" class="btn btn_a" value="取消调整" val="<s:property value='id'/>" ><span>取消调整</span></a></td>
                                            </tr>
                                        </s:iterator>
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
                    </div>
                    <!-- E Tab -->
                </div>

            </div>
            <!-- E Content -->
            
            