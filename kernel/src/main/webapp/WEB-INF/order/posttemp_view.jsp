<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/lookatmb.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>默认模板查看</title>

<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">模板查看</h2>
	</div>
	<div id="content_bd" class="clearfix">
		<!-- S Box -->
		<div class="box box_a">
			<div class="box_bd clearfix">
			<form id="p_form" action="posttemp!posttempEdit.action" method="post">
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<p>
					<label for="ptName" style="display: inline; font-weight: bold;">模板名称：</label> 
					<input id="ptName" name="posttemp.ptName" value="<s:property value="posttemp.ptName"/>" class="text-input" style="background-color: transparent; margin: 0px; border: none;" />
					<!-- 卖家版去除计费说明  -->

					<label for="calclateType">计费方式：</label>
					<s:if test="posttemp.calclateType==1">固定收费</s:if>
					<s:if test="posttemp.calclateType==2">简单重量收费</s:if>
					<s:if test="posttemp.calclateType==3">续重价格收费</s:if>
					<s:if test="posttemp.calclateType==4||posttemp.calclateType==null">续重统计单位收费</s:if>

					<select id="calclateType" name="posttemp.calclateType" onChange="showcalclate()" style="display: none">
						<option value="1" <s:if test="posttemp.calclateType==1">selected</s:if>>固定收费</option>
						<option value="2" <s:if test="posttemp.calclateType==2">selected</s:if>>简单重量收费</option>
						<option value="3" <s:if test="posttemp.calclateType==3">selected</s:if>>续重价格收费</option>
						<option value="4" <s:if test="posttemp.calclateType==4||posttemp.calclateType==null">selected</s:if>>续重统计单位收费</option>
					</select> 
					<span id="sp_firstwidth" style="display: none"> 
					<label for="firstWeight" style="display: inline; font-weight: bold;">首重重量：</label>
						<s:property value="posttemp.firstWeight" /> 
						<input id="firstWeight" class="text-input" name="posttemp.firstWeight" type="hidden" size="10" onKeyUp="__(event,this),____(event,this,'')" onBlur="checkNum(this)" value="<s:property value="posttemp.firstWeight"/>"> kg </span> 
						<span id="p_module" style="font-weight: normal;display:none" > 续重精确到： 
						<font style="font-weight: normal"> 
							<s:if test="posttemp.module==0.01">0.01kg</s:if> 
							<s:if test="posttemp.module==0.5">0.5kg</s:if> 
							<s:if test="posttemp.module==1">1.0kg</s:if> 
						</font> </span>

					<input type="hidden" name="posttemp.createUser" value="<s:property value="posttemp.createUser"/>" /> 
					<input type="hidden" id="postinfo" name="posttemp.postinfo" value="<s:property value="posttemp.postinfo"/>"> 
					<input type="hidden" name="posttemp.id" value="<s:property value="posttemp.id"/>">
				</p>
				
				
				
				<!-- 四种类型模板的计费 -->
				<div class="t_area" id="t_area1" onClick="showarea()" style="display:none;">
                        <a class="t_area_close">X</a>
                        <table>
                            <thead>
                                <tr>
                                    <td colspan="3">
                                        <h4>固定价格规则计费说明：</h4>
                                        <p>固定价格：5元</p>
                                    </td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td width="65">重量（kg）</td>
                                    <td>实际收费计算公式</td>
                                    <td>实际收费（元）</td>
                                </tr>
                                <tr>
                                    <td>0.2</td>
                                    <td>实际收费 = 固定价格</td>
                                    <td>5.00</td>
                                </tr>
                                <tr>
                                    <td>1.6</td>
                                    <td>实际收费 = 固定价格</td>
                                    <td>5.00</td>
                                </tr>
                                <tr>
                                    <td>3.0</td>
                                    <td>实际收费 = 固定价格</td>
                                    <td>5.00</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="t_area" id="t_area2" onClick="showarea()" style="display:none;">
                        <a class="t_area_close">X</a>
                        <table>
                            <thead>
                                <tr>
                                    <td colspan="3">
                                        <h4>简单重量规则计费说明：</h4>
                                        <p>重量单价：8元 &nbsp;&nbsp;&nbsp;&nbsp;最低收费：10元</p>
                                    </td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td width="65">重量（kg）</td>
                                    <td>实际收费计算公式</td>
                                    <td>实际收费（元）</td>
                                </tr>
                                <tr>
                                    <td>0.2</td>
                                    <td>实际收费 = 最低收费</td>
                                    <td>10.00</td>
                                </tr>
                                <tr>
                                    <td>1.6</td>
                                    <td>实际收费 = 重量单价 * 重量</td>
                                    <td>1.6 * 8 = 10.8</td>
                                </tr>
                                <tr>
                                    <td>3.0</td>
                                    <td>实际收费 = 重量单价 * 重量</td>
                                    <td>3 * 8 = 24</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="t_area" id="t_area3" onClick="showarea()" style="display:none;">
                        <a class="t_area_close">X</a>
                        <table>
                            <thead>
                                <tr>
                                    <td colspan="3">
                                        <h4>续重价格规则计费说明：</h4>
                                        <p>首重：0.5kg&nbsp;&nbsp;首费：5元&nbsp;&nbsp;最低收费：12元&nbsp;&nbsp;续重重量单价：5元</p>
                                    </td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td width="65">重量（kg）</td>
                                    <td>实际收费计算公式</td>
                                    <td>实际收费（元）</td>
                                </tr>
                                <tr>
                                    <td>0.2</td>
                                    <td>实际收费 = 最低收费</td>
                                    <td>12.00</td>
                                </tr>
                                <tr>
                                    <td>1.6</td>
                                    <td>实际收费 = 最低收费</td>
                                    <td>12</td>
                                </tr>
                                <tr>
                                    <td>3.0</td>
                                    <td>实际收费 = 首费 + (重量 - 首重) * 续重重量单价</td>
                                    <td>5 + (3-0.5) * 5 = 5 + 12.5 = 17.5</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="t_area" id="t_area4" onClick="showarea()" style="display:none;">
                        <a class="t_area_close">X</a>
                        <table>
                            <thead>
                                <tr>
                                    <td colspan="3">
                                        <h4>续重统计单位规则计费说明：</h4>
                                        <p>首重：0.5kg&nbsp;&nbsp;首费：10元&nbsp;&nbsp;续费：5元&nbsp;&nbsp;续重统计单位：0.5kg</p>										</td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td width="65">重量（kg）</td>
                                    <td>实际收费计算公式</td>
                                    <td>实际收费（元）</td>
                                </tr>
                                <tr>
                                    <td>0.5以下</td>
                                    <td>实际收费 = 首费</td>
                                    <td>10</td>
                                </tr>
                                <tr>
                                    <td>0.5以上</td>
                                    <td>实际收费 = 首费 + 续重费用<br>
                                        <br>
                                        续重为实际总量减去首重，续 重费用每 0.5kg收 5 元，不足0.5kg的重量按5元计算 </td>
                                    <td><p><span style="color: #FF0000">例1：</span><br>
                                            实际重量1.6kg<br>
                                            实际收费=10+15=25<br>
                                            <span style="color: #FF0000">例2：</span><br>
                                            实际重量：3.0kg<br>
                                            实际收费=10+25=35 </p>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
				
				
				
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
											<em>续重单价(元/kg)</em>
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
                                    <td class="destId" value="<s:property value='#postinfo.destId'/>">
                                        <span><s:property value="#postinfo.destText"/></span>
                                        <input class="srcText" type="hidden" id="<s:property value='#postinfo.sourceId'/>" value="<s:property value='#postinfo.srcText'/>"/>
                                    </td>
                                    <td  class="ow">
	                                    <div>
	                                        <label for="h<s:property value="#st.index"/>"></label>
	                                        <input class="text-inputx owh numAlignRight text_input_num" value="<s:property value="#postinfo.firstWeight"/>" id="h<s:property value="#st.index"/>"  onBlur="checkNum(this)" readonly="readonly">&nbsp;&nbsp;
	                                    </div>
                                	</td>
                                    <td  class="fw">
                                		<input class="text-inputx fwr numAlignRight text_input_num" value="<s:property value="#postinfo.fwRealPirce"/>"  id="b<s:property value="#st.index"/>" readonly="readonly">&nbsp;&nbsp;
                                	</td>
	                                <td  class="ow" style="text-align: center;">
	                                	<input class="text-inputx owr numAlignRight text_input_num" value="<s:property value="#postinfo.owRealPirce"/>"  id="d<s:property value="#st.index"/>" readonly="readonly">&nbsp;&nbsp;
	                                </td>
	                                <td  class="ow" >
	                                    <div>
	                                        <label for="e<s:property value="#st.index"/>"></label>
	                                        <input class="text-inputx owe numAlignRight text_input_num" value="<s:property value="#postinfo.fixedPirce"/>"    id="e<s:property value="#st.index"/>" onkeyup="__(event,this),____(event,this,'.owe')" onBlur="checkNum(this)" readonly="readonly">&nbsp;&nbsp;
	                                    </div>
	
	                                </td>
	                                <td  class="ow">
	                                    <div>
	                                        <label for="f<s:property value="#st.index"/>"></label>
	                                        <input class="text-inputx owf numAlignRight text_input_num" value="<s:property value="#postinfo.weightPirce"/>"    id="f<s:property value="#st.index"/>" onkeyup="__(event,this),____(event,this,'.owf')" onBlur="checkNum(this)" readonly="readonly">&nbsp;&nbsp;
	                                    </div>
	                                </td>
	                                <td  id="th_5">
	                                	<label>
	                                		<s:property value="#postinfo.addWeightChoice"/>
	                                	</label>
							                        
							        </td>
	                                <td  class="ow">
	                                    <div>
	                                        <label for="g<s:property value="#st.index"/>"></label>
	
	
	                                        <c:choose>
	                                            <c:when test="${posttemp.id==0}">	
	                                                <input class="text-inputx owg numAlignRight text_input_num" style="background-color:transparent;width:45px;display:none" value="<s:property value="#postinfo.fwRealPirce"/>" id="g<s:property value="#st.index"/>" onkeyup="__(event,this),____(event,this,'.owg')" onBlur="checkNum(this)" readonly="readonly">&nbsp;&nbsp;
	                                            </c:when>
	                                            <c:otherwise>		
	                                                <input class="text-inputx owg numAlignRight text_input_num" style="background-color:transparent;width:45px;"  value="<s:property value="#postinfo.floorPirce"/>"  id="g<s:property value="#st.index"/>" onkeyup="__(event,this),____(event,this,'.owg')" onBlur="checkNum(this)" readonly="readonly">&nbsp;&nbsp;
	                                            </c:otherwise>	
	                                        </c:choose>						
	                                    </div>
	
	                                </td>		
                                </tr>
							</s:iterator>
								
							</tbody>
						</table>
					</div>
					<!-- E Table -->
				</div>
				
				
				<c:if test="${yto:getCookie('userType') == 2
                                  || yto:getCookie('userType') == 21
                                  || yto:getCookie('userType') == 22
                                  || yto:getCookie('userType') == 23
                                  || yto:getCookie('userType') == 3
                                  || yto:getCookie('userType') == 4 }">
                              
                			  <div class="txt_a clearfix">
                			  	  <div><label for="textarea_glyf" style="display: inline; font-weight: bold;">关联用户：</label></div>
	                              <c:choose>
	                                  <c:when test="${yto:getCookie('userType') == 3}">
	                                      <!-- 管理员 -->
	                                      <textarea id="textarea_glyf" class="textarea_allUser vipText" readonly>所有用户(all)</textarea>
	                                      <input type="hidden" id="vipIds" name="posttemp.vipIds" value="all">
	                                      <input type="hidden" id="posttemp.ptType" name="posttemp.ptType" value="1">
	                                  </c:when>
	                                  <c:otherwise>
	                                      <textarea id="textarea_glyf" class="textarea_allUser vipText" readonly><s:property value="posttemp.vipText"/></textarea>
	                                      <input type="hidden" id="vipIds" name="posttemp.vipIds" value="<s:property value="posttemp.vipIds"/>">
	                                      <input type="hidden" id="posttemp.ptType" name="posttemp.ptType" value="<s:property value="posttemp.ptType"/>">
	                                  </c:otherwise>
	                              </c:choose>
                              </div>
                        	  <div class="txt_b clearfix">
                        	  	  <div><label for="textarea_mbsm" style="display: inline; font-weight: bold;">模板说明：</label></div>
                        		  <div><textarea id="textarea_mbsm" class="textarea_allUser" readonly><s:property value="posttemp.remark"/></textarea></div>
                              </div>
                </c:if>
                
			</form>
			
            <div class="txt_c">
					<a href="javascript:;" class="btn btn_a" title="返回" onClick="javascript: history.go(-1);"><span>返回</span></a>
			</div>
			
			</div>
		</div>
		<!-- E Box -->
	</div>
</div>
<!-- E Content -->

<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		showBindCode:true,
		userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	}
</script>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/lookatmb.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->

