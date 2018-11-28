<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<div class="table" style="width:500px;height:350px;overflow-y:auto;overflow-x:hidden;">
		<table>
			<thead>
				<tr>
					<th class="th_a">
						<div class="th_title">
							<em>目的地</em>
						</div>
					</th>
					<th class="th_b">
						<div class="th_title">
							<em>预警值</em>
						</div>
					</th>
					<th class="th_c">
						<div class="th_title">
							<em>操作</em>
						</div>
					</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="warnValueList" var="pv">
					<tr>
						<td class="td_a">
							<span class="destination"><s:property value="#pv.destination"/></span>
							<select class="pro" style="display:none;"></select>
						</td>
						<td class="td_b">
							<span class="warnValue"><s:property value="#pv.warnValue"/>天</span>
							<select class="day"  style="display:none;">
								<option value="0" selected>请选择</option>
								<option value="3">3天</option>
								<option value="4">4天</option>
								<option value="5">5天</option>
								<option value="6">6天</option>
								<option value="7">7天</option>
								<option value="8">8天</option>
								<option value="9">9天</option>
							</select> 
						</td>
						<td class="td_c">
							<span class="td_c_span"><a href="">修改</a></span>
							<input type="button" value="保存" class="save" style="display:none;">
							<input type="button" value="取消" class="cannl" style="display:none;">
							<span class="del"><a href="#">删除</a></span>
							<input type="hidden" class="destination" value="<s:property value="#pv.destination"/>">
							<input type="hidden" class="sellerId" value="<s:property value="#pv.sellerId"/>">
							<input type="hidden" class="id" value="<s:property value="#pv.id"/>">
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<!-- 
		<div class="item_navi">
			第 <s:property value="currentPage"/> 页<input type="hidden" class="currentPage" value="<s:property value="currentPage"/>">
			/ 共 <s:property value="totalNum"/> 页 
			<s:if test="%{currentPage > 1}">
			<a href="#">
				<span class="jian" val="${currentPage-1 }">上一页</span>
				<input type="hidden" class="flag" value="0">
			</a></s:if> | 
			<s:if test="%{currentPage < totalNum}">
			<a href="#">
				<span class="jia" val="${currentPage+1 }">下一页</span>
				<input type="hidden" class=flag value="1">
			</a></s:if>
		</div> -->
	</div>
