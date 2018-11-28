<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />	
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- 当前页面js -->
	<script type="text/javascript" src="${jsPath}/page/set_warnValue.js?d=${str:getVersion() }"></script>
	<style>
	td{border:1px solid #A5BFCF}
	.table thead tr th{ border-width: 1px;}
	.table table{ border-width: 1px;}
	</style>
<div id="content">
	<div class="table">
	  <table>
	 	<thead>
		 	<tr><th colspan="6"><div class="th_title">
		 	
		 	<s:if test="addressCity== null || addressCity== '' " >
		 		<em>发货地：暂无设置</em>
		 	</s:if>	
		 	<s:else>
		 	  <em>发货地：<s:property value="addressCity" /></em>
		 	</s:else>	
		 	
		 	</div></th></tr>
		    <tr>
		      <th><div class="th_title"><em>收件地</em></div></th>
		      <th><div class="th_title"><em>预警值</em></div></th>
		      <th><div class="th_title"><em>收件地</em></div></th>
		      <th><div class="th_title"><em>预警值</em></div></th>
		      <th><div class="th_title"><em>收件地</em></div></th>
		      <th><div class="th_title"><em>预警值</em></div></th>
		    </tr>
	    </thead>
	   <s:iterator id="warValue" value = "warnValueList" status="st">
	      <s:if test="#st.index % 3 ==0">
	        <tr>
	      </s:if>
	        <td class="td_a"><span class="warn_destin"><s:property value="destination"/></span></td>
	        <td class="td-b">
	        <span class="warn_value">
	            <input type="hidden" id="warn_id_<s:property value="#st.index"/>" value="<s:property value="id"/>">
	            <input type="hidden" id="previous_value_<s:property value="#st.index"/>" value="<s:property value="warnValue"/>">
	        	<select id="warnvalue_<s:property value="#st.index"/>" name="<s:property value="destination" />">
	        	  <option value="0" name="warnvalue" <s:if test='warnValue =="0"'>selected</s:if>>暂不设置</option>
	        	  <option value="1" name="warnvalue" <s:if test='warnValue =="1"'>selected</s:if>>1天</option>
	        	  <option value="2" name="warnvalue" <s:if test='warnValue =="2"'>selected</s:if>>2天</option>
	        	  <option value="3" name="warnvalue" <s:if test='warnValue =="3"'>selected</s:if>>3天</option>
	        	  <option value="4" name="warnvalue" <s:if test='warnValue =="4"'>selected</s:if>>4天</option>
	        	  <option value="5" name="warnvalue" <s:if test='warnValue =="5"'>selected</s:if>>5天</option>
	        	  <option value="6" name="warnvalue" <s:if test='warnValue =="6"'>selected</s:if>>6天</option>
	        	  <option value="7" name="warnvalue" <s:if test='warnValue =="7"'>selected</s:if>>7天</option>
	        	  <option value="8" name="warnvalue" <s:if test='warnValue =="8"'>selected</s:if>>8天</option>
	        	  <option value="9" name="warnvalue" <s:if test='warnValue =="9"'>selected</s:if>>9天</option>
	        	</select>
	        </span>
	        </td>
	     <s:if test="(#st.index % 3 +1) == 0">
	     	 </tr>
	      </s:if>
	   </s:iterator>
	  </table>
	  <div class="btn_box" style="margin-top:10px">	
	  <a href="javascript:;" title="保存设置" id="save_setting" class="btn btn_a"><span>保存设置</span></a>
	  <a href="javascript:;" title="返回" id="save_back" class="btn btn_a"><span>返回</span></a>
	  </div>
	</div>
</div>
