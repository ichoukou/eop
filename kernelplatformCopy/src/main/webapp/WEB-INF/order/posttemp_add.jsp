<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/lookatmb.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>运费模板新增</title>

<!--[if IE 6]>
	<script type="text/javascript" src="${jsPath}/util/position_fixed.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		DD_belatedPNG.fix('.png');
	</script>
<![endif]-->
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/addmb.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->

<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">新增模板</h2>
	</div>
	<p class="ts" style="display:black;font-size:12px; height:26px;line-height:26px;background-color:#fcfae5; margin-top:10px;">新增→填写模板名称→选择续重统计单位→填写模板价格→关联客户→保存</p>
	
	<div id="content_bd" class="clearfix">
		<!-- S Box -->
		<div class="box box_a">
			<div class="box_bd clearfix">
			<form id="p_form" action="posttemp!posttempSave.action" method="post" style="width: 100%;">
			<input type="hidden" name="menuFlag" value="${menuFlag }" />
			<input name="url" type="hidden" value="<s:property value="url"/>">
				<p>
					<label for="ptName" style="display:inline;font-weight:bold;"><span class="req">*</span>模板名称：</label>
                    <input id="ptName"  name="posttemp.ptName" class="text-input input_text" style="margin:0px;"/>
                    <input type="hidden" id="postinfo" name="posttemp.postinfo" value="0.1">
                </p>
                <p><br></p>
                <p style="position:relative;">   
                        
                        <label for="calclateType" style="display:inline;font-weight:bold;"><span class="req">*</span>计费类型：</label>
                        <select id="calclateType" name="posttemp.calclateType">
                            <option value="1">固定收费</option>
                            <option value="2">简单重量收费</option>
                            <option value="3" selected>续重价格收费</option>
                            <option value="4">续重统计单位收费</option>
                        </select>

                        <span >
                        <span id="sp_firstwidth" style="display:none">
                            <label for="firstWeight" style="display:inline;font-weight:bold;"><span class="req">*</span>首重重量：</label>
                            <input  id="firstWeight" class="text-input onBlurCheckNum1"  name="posttemp.firstWeight" type="text" size="10"  >
                            kg
                        </span>					
                        <c:if test="${yto:getCookie('userType') != 1 && yto:getCookie('userType') != 11 && yto:getCookie('userType') != 12 && yto:getCookie('userType') != 13 && yto:getCookie('userType') != 4}"> &nbsp;&nbsp;</c:if>

                        <c:if test="${yto:getCookie('userType') != 1 && yto:getCookie('userType') != 11 && yto:getCookie('userType') != 12 && yto:getCookie('userType') != 13 && yto:getCookie('userType') != 4}"> <a class="t_area" style="color:#2623ba; cursor:pointer; text-decoration:underline;position:absolute;left:745px;">计费说明</a> </c:if>
                     
                        <!-- 卖家版去除计费说明  --> 
                        <span id="p_module" style="display:none">
                        <c:if test="${yto:getCookie('userType') != 1 && yto:getCookie('userType') != 11 && yto:getCookie('userType') != 12 && yto:getCookie('userType') != 13 && yto:getCookie('userType') != 4}">
                            &nbsp;&nbsp;续重精确到：
                             
                            <input type="radio" name="posttemp.module"  value="0.01" checked>0.01千克
                            &nbsp;&nbsp;
                            <input type="radio" name="posttemp.module"  value="0.5">0.5千克
                            &nbsp;&nbsp;
                            <input type="radio" name="posttemp.module"  value="1.0">1.0千克
                        
                            </c:if>
                            
                            <!-- 卖家版去除计费说明  -->  
                            </span>
                          </span>
                        </p>
			
					<input type="hidden" name="posttemp.createUser" value="<s:property value="posttemp.createUser"/>" />
					<input type="hidden" id="postinfo" name="posttemp.postinfo" value="<s:property value="posttemp.postinfo"/>">
					<input type="hidden" name="posttemp.id" value="<s:property value="posttemp.id"/>">
				
				
				<p><br></p> 
				<!-- S Box -->
				<div class="clearfix">
					<div class="box box_a">
						<div class="box_bd" id="sfd">
							<p>始发地：<span class="srcText"><s:property value="user.addressProvince"/></span>
							<span style="margin-left:300px;font-size:12px;">标准价仅供参考，不会显示给客户看到</span></p>
						</div>
					</div>
					<!-- E Box -->

					<!-- S Table -->
					<div class="table">
						<table>
							<thead>
								<tr>
									<th class="th_a">
										<div class="th_title">
											<em>目的地</em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>首重重量</em>
										</div></th>
									<th class="th_b">
										<div class="th_title">
											<em>首重价格</em>
										</div></th>
									<th class="th_c">
										<div class="th_title">
											<em>续重价格(元/kg)</em>
										</div></th>
									<th class="th_d">
										<div class="th_title">
											<em>固定价格</em>
										</div></th>
									<th class="th_e">
										<div class="th_title">
											<em>重量单价</em>
										</div></th>
									<th class="th_e">
										<div class="th_title">
											<em>续重统计单位(kg)</em>
										</div></th>
									<th class="th_f">
										<div class="th_title">
											<em>最低收费价格</em>
										</div></th>
								</tr>
							</thead>
							<tbody>
							<s:if test="spList.size == 0">
                                <tr>
                                    <td colspan="6">没有运费信息... （原因可能是您没有填写地区。如有疑问请联系管理员。）</td>
                                </tr>
                            </s:if>
                            <s:else>
							<s:iterator value="spList" status="st" var="standardPosttemp">
                                            <tr class="postinfo_details yto tr_sp">
                                                <td class="destId" value="<s:property value="#standardPosttemp.destId"/>">
                                                    <p style="display:none"><s:property value="#standardPosttemp.destText"/></p>
                                                    <span class="dtxt"><s:property value="#standardPosttemp.destText"/></span>
                                                    <input class="srcText" type="hidden" id="<s:property value="#standardPosttemp.sourceId"/>" value="<s:property value="#standardPosttemp.srcText"/>">
                                                </td>
                                                <td  id="th_6"  class="ow">
                                                    <div id="div_h" style="display:inline; width:100px;">
                                                        <label for="h<s:property value="#st.index"/>"></label>
                                                        <input class="text-inputx owh numAlignRight text_input_num input_text onBlurCheckNum" style="width:45px;text-align:right;" value="" id="h<s:property value="#st.index"/>" >&nbsp;&nbsp;
                                                    </div>

                                                </td>		
                                                <td  id="th_1" class="fw">
                                                    <div id="div_b" style="display:inline; width:100px;">
                                                        <label for="b<s:property value="#st.index"/>"></label>
                                                        <input class="text-inputx fwr numAlignRight text_input_num input_text" style="width:45px;text-align:right;" value="<s:property value="#standardPosttemp.standardPrice"/>" id="b<s:property value="#st.index"/>" >&nbsp;&nbsp;
                                                    </div>
                                                </td>
                                                <td id="th_2"  class="ow">
                                                    <div id="div_d" style="display:inline; width:100px;">
                                                        <label for="d<s:property value="#st.index"/>"></label>
                                                        <input class="text-inputx owr numAlignRight text_input_num input_text onBlurCheckNum" style="width:45px;text-align:right;" value="<s:property value="#standardPosttemp.continuationPrice"/>" id="d<s:property value="#st.index"/>"  >&nbsp;&nbsp;
                                                    </div>
                                                </td>
                                                <td id="th_3"  class="ow">
                                                    <div id="div_e" style="display:inline; width:100px;">
                                                        <label for="e<s:property value="#st.index"/>"></label>
                                                        <input class="text-inputx owe numAlignRight text_input_num input_text onBlurCheckNum" style="text-align:right;width:45px; display:none" value="<s:property value="#standardPosttemp.fixedPirce"/>" id="e<s:property value="#st.index"/>"  >&nbsp;&nbsp;
                                                    </div>

                                                </td>
                                                <td id="th_4"   class="ow">
                                                    <div id="div_f" style="display:inline; width:100px;">
                                                        <label for="f<s:property value="#st.index"/>"></label>
                                                        <input class="text-inputx owf numAlignRight text_input_num input_text onBlurCheckNum" style="text-align:right;width:45px; display:none" value="<s:property value="#standardPosttemp.weightPirce"/>" id="f<s:property value="#st.index"/>"  >&nbsp;&nbsp;
                                                    </div>
                                                </td>
                                                
                                                <td  id="th_5">
							                        <select class="addWeightChoice" style="width:90px;">
							                            <option value="0.01">0.01</option>
							                            <option value="0.5">0.5</option>
							                            <option value="1">1</option>
							                            <!-- <option value=""></option> -->
							                        </select>
                                                </td>
                                                
                                                <td  id="th_5"  class="ow">
                                                    <div id="div_g" style="display:inline; width:100px;">
                                                        <label for="g<s:property value="#st.index"/>"></label>
                                                        <input class="text-inputx owg numAlignRight text_input_num input_text onBlurCheckNum" style="width:45px;text-align:right;" value="<s:property value="#standardPosttemp.standardPrice"/>" id="g<s:property value="#st.index"/>"  >&nbsp;&nbsp;
                                                    </div>
                                                </td>
                                            </tr>
                                        </s:iterator>
                                    </s:else>
								
							</tbody>
							
							<%-- <tr id="postinfo_details_flag" style="display: none;"></tr>
                                    <tr style="display: none;">
                                        <td colspan="5"><a href="javascript:;" id="p_add"
                                                           style="color: #666666;"> &nbsp;[为指定地区设置运费 ] <span
                                                    id="p_orther_state" open="↑" close="↓"> ↓ </span> </a></td>
                            </tr> --%>
						</table>
					</div>
					<!-- E Table -->
				</div>
				
                              
                			  <div class="txt_a clearfix">
                			  	  <div><label for="textarea_glyf" style="display:inline;font-weight:bold;"><span class="req">*</span>关联用户：</label></div>
	                              <c:choose>
			                            <c:when test="${yto:getCookie('userType') == 3}">
			                                <!-- 管理员 -->
			                                <textarea id="textarea_glyf" class="textarea_allUser vipText" readonly>所有用户(all)</textarea>
			                                <input type="hidden" id="vipIds" name="posttemp.vipIds" value="all">
			                                <input type="hidden" id="posttemp.ptType" name="posttemp.ptType" value="1">
			                            </c:when>
			                            <c:otherwise>
											<textarea id="textarea_glyf" class="textarea_allUser vipText" readonly title="双击选择用户"><s:if test="posttemp != null"><s:property value="posttemp.vipText"/></s:if><s:else>请双击选择使用该模板的用户</s:else></textarea>
			                                <div style="float:right;">
		                                        <input type="hidden" id="vipIds" name="vipIds" <s:if test="posttemp != null">value="<s:property value="posttemp.vipIds"/>"</s:if>>
			                                    <input type="hidden" id="posttemp.ptType" name="posttemp.ptType" value="">
			                                    <div style="float:right;width:80px" >
				                                    <a id="p_checkUser" class="btn btn_a" value="选择用户" style="margin-bottom:10px;"><span>选择用户</span></a>
				                                    <a id="p_checkUser_clear" class="btn btn_a" value="清空用户"><span>清空用户</span></a>
			                                    </div>
	                                		</div>
			                            </c:otherwise>
                        		</c:choose>
                              </div>
                              
                        	  <div class="txt_b clearfix">
                        	  	  <div><label for="textarea_mbsm" style="display:inline;font-weight:bold;">模板说明：</label></div>
                        	  	  <div>
                        	  	  	<textarea id="textarea_mbsm" class="textarea_allUser remark" name="posttemp.remark"><s:property value="posttemp.remark"/></textarea>
                        	  	  </div>
                        		  
                              </div>
               
                
			</form>
			
            <div style="text-align: center;margin-top:10px;">
					<a class="btn btn_a" id="f_save"><span>保 存</span></a>
					<a class="btn btn_a" onClick="history.go(-1);" value="取 消"><span>取 消</span></a>
			</div>
			
			</div>
			
		</div>
		<!-- E Box -->
	</div>
</div>
<!-- E Content -->

