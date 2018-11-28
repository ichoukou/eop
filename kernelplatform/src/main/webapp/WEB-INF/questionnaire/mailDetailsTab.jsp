<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="item_navi">
	<s:if test="%{currentPage > 1}">
		<a href="javascript:;" class="item_next" val="${currentPage-1 }">上一条</a>
	</s:if>
	<s:if test="%{currentPage < pagination.totalPages}">
		<a href="javascript:;" class="item_prev" val="${currentPage+1 }">下一条</a>
	</s:if>
	<span class="item_all">共<s:property value="pagination.totalRecords"/>条</span>
</div>
<!-- S Table -->
<div class="table colspan2">
	<table>
		<tbody>
			<s:iterator value="orderProductList" var="op">
				<tr>
					<td class="td_a">物流号</td>
					<td class="td_b"><s:property value="order.txLogisticId"/></td>
				</tr>
				<tr>
					<td class="td_a">运单号</td>
					<td class="td_b"><s:property value="order.mailNo"/></td>
				</tr>
				<s:if test="order.lineType==0">
					<tr>
				       	<td class="td_a">物流取货时间段</td>
				       	<td class="td_b">
				       		<s:if test="order.sendStartTime == '' || order.sendStartTime == null">任意时间</s:if>
				       		<s:else>
				       			<s:date name="order.sendStartTime" format="yyyy-MM-dd"/>至<s:date name="order.sendEndTime" format="yyyy-MM-dd"/>
				       		</s:else>
				       	</td>
					</tr>
				</s:if>
				<tr>
					<td class="td_a">重量(kg)</td>
					<td class="td_b">
						<s:if test="order.weight==0"><font title='没有称重信息'>≤1</font></s:if>
	       				<s:else><s:property value="order.weight"/></s:else>
					</td>
				</tr>
				<tr>
	       			<td class="td_a">下单渠道</td>
			       	<td class="td_b">
			       		<s:if test="order.lineType==0">线上下单</s:if>
			       		<s:elseif test="order.lineType==1">线下下单</s:elseif>
			       	</td>
		       </tr>
		       <tr>
		      		<td class="td_a">订单状态</td>
		      		<td class="td_b">
			       		<s:if test="order.status=='CONFIRM'">等待确认</s:if>
			       		<s:if test="order.status==0">
			       			<s:if test="order.lineType==0">接单中</s:if>
			       			<s:elseif test="order.lineType==1">接单中</s:elseif>
			       		</s:if>
			       		<s:if test="order.status=='UNACCEPT'">不接单</s:if>
			       		<s:if test="order.status=='ACCEPT'">接单</s:if>
			       		<s:if test="order.status=='SENT_SCAN'">派件扫描</s:if>
			       		<s:if test="order.status=='TRACKING'">流转信息</s:if>
			       		<s:if test="order.status=='GOT'">揽收成功</s:if>
			       		<s:if test="order.status=='NOT_SEND'">揽收失败</s:if>
			       		<s:if test="order.status=='FAILED'">失败</s:if>
			       		<s:if test="order.status=='SIGNED'">已签收</s:if>
			       		<s:if test="order.status=='WITHDRAW'">订单取消</s:if>
			       	</td>
		       	</tr>
				<tr>
			       	<td class="td_a">商品信息</td>
			       	<td class="td_b">
			       		<input type="hidden" value="<s:property value='product.size()'/>" id="p_size"/>
			       		<table>
				       		<s:iterator value="product" var="pro">
			       				<tr>
				       				<td><s:property value="itemName"/>（<s:property value="itemNumber"/>）</td>
			       				</tr>
				       		</s:iterator>
			       		</table>
			       	</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
<!-- E Table -->