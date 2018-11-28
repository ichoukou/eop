<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/lookatmb.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>运费模板编辑</title>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/editmb.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->

<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">模板编辑</h2>
	</div>
	<div id="content_bd" class="clearfix">
		<!-- S Box -->
		<div class="box box_a">
			<div class="box_bd clearfix">
			<form id="p_form" action="posttemp!posttempEdit.action" method="post">
			<input type="hidden" name="menuFlag" value="${menuFlag }" />
			<input id="url" name="url" type="hidden" value="<s:property value="url"/>">
				<p style="position:relative;*zoom:1;">
                   <label for="ptName" style="display: inline; font-weight: bold;"><span class="req">*</span>模板名称：</label>
                   <input id="ptName" name="posttemp.ptName" value="<s:property value="posttemp.ptName"/>" class="text-input input_text" style="width:130px;" /> 
                   <a class="submit02 tp0  btn btn_a" title="请谨慎操作! 删除后关联用户将无法使用该模板对账!" id="deletePosttemp" value="删除该模板"><span>删除该模板</span></a>
                   <input type="hidden" id="delId" value="<s:property value='posttemp.id'/>"/>
                </p>
                <p><br></p>
                
                <p style="position:relative;*zoom:1;">
                        <label for="calclateType" style="display:inline;font-weight:bold;"><span class="req">*</span>计费类型：</label>
                        <select id="calclateType" name="posttemp.calclateType">
                            <option value="1" <s:if test="posttemp.calclateType==1">selected</s:if>>固定收费</option>
                            <option value="2" <s:if test="posttemp.calclateType==2">selected</s:if>>简单重量收费</option>
                            <option value="3" <s:if test="posttemp.calclateType==3">selected</s:if>>续重价格收费</option>
                            <option value="4" <s:if test="posttemp.calclateType==4">selected</s:if>>续重统计单位收费</option>
                        </select>
 					<!-- 卖家版去除计费说明  --> 
                    <span id="p_module" style="display:none">
						<c:if test="${yto:getCookie('userType') != 1 && yto:getCookie('userType') != 11 && yto:getCookie('userType') != 12 && yto:getCookie('userType') != 13 && yto:getCookie('userType') != 4}">	
		         			<span style="margin-left:30px;"><span class="req">*</span>续重精确到：</span>
							
							<span class="m_05">
								<input type="radio"  name="posttemp.module" value="0.01" <s:if test="posttemp.module==0.01">checked</s:if>>
								<label for="dskl" class="aoweif">0.01kg</label>
							</span>
							
							<span class="m_05">
								<input type="radio" name="posttemp.module" value="0.5" <s:if test="posttemp.module==0.5">checked</s:if>>
								<label for="oiu" class="aoweif">0.5kg</label>
							</span>
							
							<span class="m_05">
								<input type="radio" name="posttemp.module" value="1.0" <s:if test="posttemp.module==1.0">checked</s:if>>
								<label for="ert" class="aoweif">1kg</label>
							</span>
			 			</c:if>
                    </span>	
                    <span id="sp_firstwidth" style="display:none">
                                <label for="firstWeight" style="display:inline;font-weight:bold;"><span class="req">*</span>首重重量：</label>
                                <input  id="firstWeight" class="text-input input_text blurCheckNum1"  name="posttemp.firstWeight" type="text" size="10"  value="<s:property value="posttemp.firstWeight"/>" >
                            kg
                    </span>	

                        <a id="t_con" class="t_area" href="javascript:;">计费说明</a>
                    </p>
			
					<input type="hidden" name="posttemp.createUser" value="<s:property value="posttemp.createUser"/>" /> 
					<input type="hidden" id="postinfo" name="posttemp.postinfo" value="<s:property value="posttemp.postinfo"/>"> 
					<input type="hidden" name="posttemp.id" value="<s:property value="posttemp.id"/>">
				
				<!-- 四种类型模板的计费 -->
				
				<p><br></p>
				
				<!-- S Box -->
				<div class="clearfix">
					<div class="box box_a">
						<div class="box_bd" id="sfd">
							<p>始发地：<span class="srcText"><s:property value="user.addressProvince"/></span></p>
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
							<s:iterator value="posttemp.postinfoList" status="st" var="postinfo">
                                        <tr class="postinfo_details yto tr_sp">
                                        
                                            <td class="destId" value="<s:property value="#postinfo.destId"/>">
                                            	<p style="display:none"><s:property value="#postinfo.destText"/></p>
	                                            <span class="dtxt"><s:property value="#postinfo.destText" /></span>
	                                            <input class="srcText" type="hidden" id="<s:property value="#postinfo.srcId"/>" value="<s:property value="#postinfo.srcText"/>">
                                           </td>
                                             
                                           <td  class="ow">
                                                <div style="display:inline; width:100px;">
                                                    <label for="h<s:property value="#st.index"/>"></label>
                                                    <input class="text-inputx owh numAlignRight text_input_num input_text blurCheckNum" style="width:45px;text-align:right;" <s:if test="#postinfo.firstWeight > 0"> value="<s:property value="#postinfo.firstWeight"/>" </s:if> id="h<s:property value="#st.index"/>" >&nbsp;&nbsp;
                                                </div>

                                            </td>	
                                            <td class="fw">
                                                <div style="display: inline; width: 100px;">
                                                    <label for="b<s:property value="#st.index"/>"></label> <input
                                                        class="text-inputx fwr numAlignRight text_input_num input_text blurCheckNum"
                                                        style="width: 45px;text-align:right;"
														<s:if test="#postinfo.fwRealPirce > 0">
                                                        value="<s:property value="#postinfo.fwRealPirce"/>"
														</s:if>
                                                        id="b<s:property value="#st.index"/>"
                                                        >&nbsp;&nbsp;
                                                </div></td>
                                            <td class="ow">
                                                <div style="display: inline; width: 100px;">
                                                    <label for="d<s:property value="#st.index"/>"></label> <input
                                                        class="text-inputx owr numAlignRight text_input_num input_text blurCheckNum"
                                                        style="width: 45px;text-align:right;"
														<s:if test="#postinfo.owRealPirce > 0">
                                                        value="<s:property value="#postinfo.owRealPirce"/>"
														</s:if>
                                                        id="d<s:property value="#st.index"/>"
                                                       
                                                        >&nbsp;&nbsp;
                                                </div></td>

                                            <td  class="ow">
                                                <div style="display:inline; width:100px;">
                                                    <label for="e<s:property value="#st.index"/>"></label>
                                                    <input class="text-inputx owe numAlignRight text_input_num input_text blurCheckNum" style="width:45px;text-align:right;" <s:if test="#postinfo.fixedPirce > 0"> value="<s:property value="#postinfo.fixedPirce"/>" </s:if> id="e<s:property value="#st.index"/>" >&nbsp;&nbsp;
                                                </div>

                                            </td>
                                            <td  class="ow">
                                                <div style="display:inline; width:100px;">
                                                    <label for="f<s:property value="#st.index"/>"></label>
                                                    <input class="text-inputx owf numAlignRight text_input_num input_text blurCheckNum" style="width:45px;text-align:right;" <s:if test="#postinfo.weightPirce > 0"> value="<s:property value="#postinfo.weightPirce"/>" </s:if> id="f<s:property value="#st.index"/>" >&nbsp;&nbsp;
                                                </div>
                                            </td>
                                            <td  class="ow">
							                        <select class="addWeightChoice" style="width:90px;">
							                            <option value="0.01">0.01</option>
							                            <option value="0.5">0.5</option>
							                            <option value="1">1</option>
							                            <!-- <option value=""></option> -->
							                        </select>
                                                </td>
                                            <td  class="ow">
                                                <div style="display:inline; width:100px;">
                                                    <label for="g<s:property value="#st.index"/>"></label>
                                                    <input class="text-inputx owg numAlignRight text_input_num input_text blurCheckNum" style="width:45px;text-align:right;" <s:if test="#postinfo.floorPirce > 0"> value="<s:property value="#postinfo.floorPirce"/>" </s:if> id="g<s:property value="#st.index"/>" >&nbsp;&nbsp;
                                                </div>

                                            </td>	
                                            <input type="hidden" name="addWeight" value="<s:property value="#postinfo.addWeightChoice"/>" />
                                        </tr>
                                    </s:iterator>
								
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
                              <textarea id="textarea_glyf" class="textarea_allUser vipText" readonly title="双击选择用户"><s:property value="posttemp.vipText" /></textarea>
                              <div style="float:right;display:inline;margin-top:0px;padding-top:0px;">
                              <input type="hidden" id="vipIds" name="posttemp.vipIds" value="<s:property value="posttemp.vipIds"/>">
                              <input type="hidden" id="vipIds_temp" value="<s:property value="posttemp.vipIds"/>">
                              <input type="hidden" id="posttemp.ptType" name="posttemp.ptType" value="<s:property value="posttemp.ptType"/>">
                              <c:if test="${yto:getCookie('userType') != 3}">
                                <div style="float:right;width:80px"> 
                                	<a id="p_checkUser" class="btn btn_a" value="选择用户" style="margin-bottom:10px;"><span>选择用户</span></a>
                                 	<a id="p_checkUser_clear" class="btn btn_a" value="清空用户"><span>清空用户</span></a>
                                </div>
                              </c:if>
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

