<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>
<!-- 当前页面js -->
<script type="text/javascript" src="${jsPath}/page/yto_user_detail.js?d=${str:getVersion() }"></script>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />

<div id="content">
    <div id="content_hd" class="clearfix">
		<h2 id="message_icon">账号修改</h2>
		<em>设置您的账户信息！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>		
	</div>
    <div id="content_bd" class="clearfix">
        <input type="hidden" <c:out value="${cookie['userSource'].value}"/> />
        <div class="box box_a">
        	<div class="box_bd">	
            	<form id="userFrom" action="ytoUser!edit.action" method="post" class="form">
                	<input type="hidden" name="edit_msg" value="<s:property value="ajaxAlertText"/>">
                    <input type="hidden" name="user.id" value="<s:property value="user.id"/>" >
                    <input type="hidden" id="userType" name="user.userType" value="<s:property value="user.userType"/>" >
                    <input type="hidden" name="user.userSource" value="<s:property value="user.userSource"/>" >
                    <input type="hidden" name="user.userState" value="<s:property value="user.userState"/>" >
                    <input type="hidden" name="user.userLevel" value="<s:property value="user.userLevel"/>" >
                    <input type="hidden" name="user.site" value="<s:property value="user.site"/>" >
                    <input type="hidden" name="user.userCode" value="<s:property value="user.userCode"/>" >
                    <input type="hidden" name="user.taobaoEncodeKey" value="<s:property value="user.taobaoEncodeKey"/>" >
                    <input type="hidden" name="user.field001" value="<s:property value="user.field001"/>" >
                    <input type="hidden" name="user.field002" value="<s:property value="user.field002"/>" >
                    <input type="hidden" name="user.field003" value="<s:property value="user.field003"/>" >
                    <input type="hidden" name="user.userName" value="<s:property value="user.userName"/>" >
                    <input type="hidden" name="user.userPassword" value="<s:property value="user.userPassword"/>" >
                    <input type="hidden" name="user.createUser" value="<s:property value="user.createUser"/>" >
                    <input type="hidden" name="user.userNameText" value="<s:property value="user.userNameText"/>" >
                    <input type="hidden" name="user.shopAccount" value="<s:property value="user.shopAccount"/>" >

                    <div>
                    	<p>
                        	<label>登录账号：</label>
                            <input value="<s:property value="user.userName"/>" readonly class="input_text" id="userName">
                    	</p>
                    </div>
                    <c:if test="${yto:getCookie('userType') == 2}">
                    	<div>
                        	<p><span>网点名称：</span><input value="<s:property value="user.userNameText"/>" readonly class="input_text"></p>
                    	</div>
                    </c:if>
                    <c:if test="${yto:getCookie('userType') == 1}">
                        <div <s:if test="user.userCode==\"\" || user.userCode==null"> style='display:none;'</s:if>>
                            <p>
                                <label>客户编码：</label>
                            	<input id='uc' value="<s:property value="user.userCode"/>" readonly class="input_text">
                            </p>
                        </div>
                   	</c:if>
                    <div>
                    	<p>
                        	<label>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别&nbsp; ：</label>
                            <select name="user.sex">
                            	<option value="M" <s:if test="user.sex == \"M\"">selected</s:if> >男</option>
                                <option value="F" <s:if test="user.sex == \"F\"">selected</s:if> >女</option>
                       		</select>
                     	</p>
                    </div>
                   	<div>
                    	<p>
                        	<label for="mp">手机号码：</label>
                            <input id="mp" name="user.mobilePhone" value="<s:property value="user.mobilePhone"/>" class="input_text">
                            <span id="mpTip"></span>
                        </p>
                    </div>
                    <div>
                    	<p>
                        	<label>固定电话：</label>
                            <input id="telAreaCode" name="user.telAreaCode" value="<s:property value="user.telAreaCode" />" class="input_text" style="width: 40px;" >
								- <input id="telCode" name="user.telCode" value="<s:property value="user.telCode" />" class="input_text" style="width: 80px;" >
								- <input id="telExtCode" name="user.telExtCode" value="<s:property value="user.telExtCode" />" class="input_text" style="width: 40px;" >
							<span id="telAreaCodeTip"></span>
                    	</p>
                    </div>
                    <div>
                    	<p>
                        	<label>邮箱地址：</label>
                            <input style="margin:0;" id="yaya" name="user.mail" value="<s:property value="user.mail"/>" class="input_text">
                        	<span id="yayaTip"></span>
                    	</p>
                    </div>
                    <div>
                    	<p>
                        	<label>店铺名称：</label>
                            <input id='xx' name="user.shopName" value="<s:property value="user.shopName"/>" class="input_text">
                           	<span id="xxTip"></span>
                    	</p>
                    </div>
                    <div>
                    	<p style="overflow:hidden;">
                        	<input type="hidden" id="user_field003" value="<s:property value="user.field003" />"/>
                           	<input type="hidden" id="addressProvince" value="<s:property value="user.addressProvince" />"/>
                            <input type="hidden" id="addressCity" value="<s:property value="user.addressCity" />"/>
                            <input type="hidden" id="addressDistrict" value="<s:property value="user.addressDistrict" />"/>
                            <input type="hidden" id="addressStreet" value="<s:property value="user.addressStreet" />"/>
                            <label>发货地址：</label>
                            <span>
                            	<s:property value="user.addressProvince"/><s:property value="user.addressCity"/><s:property value="user.addressDistrict"/><s:property value="user.addressStreet"/>
                            </span>	
                            <s:hidden name="user.addressProvince" id="x_p"></s:hidden>
                            <s:hidden name="user.addressCity" id="x_c"></s:hidden>
                            <s:hidden name="user.addressDistrict" id="x_d"></s:hidden>
                            <s:hidden name="user.addressStreet" id="x_s" style="width:30px;"></s:hidden>
                    	</p>
                    </div>       
                    <div>
                    	<c:if test="${yto:getCookie('userType') == 1|| yto:getCookie('userType') == 2}">
                        	<s:if test="user.field003!='true'">
                            	<div id="div_fullAdd" class="input_text" style="display: inlineblock; background: #f9f9f9; padding:4px;" title="这里是您的完整地址..." ></div>
                            </s:if>
                    	</c:if>
                    </div>
                    <div>
                       	<div>
                        	<a href="javascript:;" id="sear_btn" title="保 存" class="btn btn_a" style="margin-left:100px;"><span>保&nbsp;存</span></a>
                    	</div>
                    </div>
					<input type="hidden" name="menuFlag" value="${menuFlag }" />
                 </form>
           	</div>                         
        </div>
	</div>
</div>

