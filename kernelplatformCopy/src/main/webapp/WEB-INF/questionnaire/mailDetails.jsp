<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
.colspan3{width:430px;}
.colspan3 .td_a{width:140px;}
.colspan3 .td_a span{display:block;}
.colspan3 .td_b{width:130px;}
.colspan3 .td_c{width:100px;}
.colspan2{width:430px;}
.colspan2 .td_a{width:140px;}
.colspan2 .td_b{width:250px;}
#dialog .tab_b .tab_triggers{width:450px;}
.item_navi{text-align:right;line-height:22px;}
#tab_panel_track,#tab_panel_order{width:430px;height:400px;overflow-y:auto;overflow-x:hidden;}
</style>
<!-- S Tab -->
<div class="tab tab_b">
	<div class="tab_triggers">
		<ul>
			<li><a href="javascript:;">物流跟踪</a></li>
			<s:if test="orderProductList!=null && orderProductList.size>0">
				<s:if test="isDisplay==0">
					<li><a href="javascript:;">订单详情</a></li>
				</s:if>
				<li><a href="javascript:;">联系方式</a></li>
			</s:if>
		</ul>
	</div>
	<div class="tab_panels">
		<div class="tab_panel" id="tab_panel_track">
			<!-- S Table -->
			<div class="table colspan3">
				<table>
					<tbody>
						<s:if test="stepsList!=null">
							<s:if test="stepsList.size==0">
								<tr>
									<td class="td_b" colspan="2" style="width:100%;display:block;">暂无走件信息，请稍后查询</td>
								</tr>
							</s:if>
							<s:else>
								<s:iterator value="stepsList" var="steps">
									<tr>
										<td class="td_a">
											<span class="td_address"><s:property value="acceptAddress"/></span>
											<span class="td_date"><s:property value="acceptTime.substring(0,10)"/> <s:property value="acceptTime.substring(10,18)"/></span>
										</td>
										<td class="td_b"><s:property value="remark"/></td>
										<td class="td_c"><s:if test="name!=null && name!=''"> <s:property value="operate"/>:<s:property value="name"/></s:if></td>
									</tr>
								</s:iterator>
							</s:else>
						</s:if>
					</tbody>
				</table>
			</div>
			<!-- E Table -->
		</div>
		<s:if test="orderProductList!=null && orderProductList.size>0">
				<s:if test="isDisplay==0">
		<div class="tab_panel" id="tab_panel_order" style="display:none;">
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
						       			<s:if test="order.lineType==0">
							       		<s:iterator value="product" var="pro">
						       				<tr>
							       				<td><s:property value="itemName"/>（<s:property value="itemNumber"/>）</td>
						       				</tr>
							       		</s:iterator>
							       		</s:if>
							       		<s:else>
							       			<tr>
							       				<td>只显示在线下单商品信息</td>
							       			</tr>
							       		</s:else>
						       		</table>
						       	</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
			<!-- E Table -->
		</div>
		</s:if></s:if>
		<div class="tab_panel" id="tab_panel_contact" style="display:none;">
			<!-- S Table -->
			<s:iterator value="orderTraderInfoList" var="traderInfo">
				<s:if test="tradeType == 1">
					<div class="table colspan2">
						<table>
							<thead>
								<tr>
									<th colspan="2"><div class="th_title">买家联系方式</div></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="td_a">买家姓名</td>
									<td class="td_b"><s:property value="name"/></td>
								</tr>
								<tr>
									<td class="td_a">联系地址</td>
									<td class="td_b"><s:property value="prov"/><s:property value="city"/><s:property value="district"/><s:property value="address"/></td>
								</tr>
								<tr>
									<td class="td_a">联系电话</td>
									<td class="td_b">
										<s:if test="mobile!=null && mobile!=''">
			                         		<s:property value="mobile"/>
			                         	</s:if>
			                         	<s:else>
			                         		<s:property value="phone"/>
			                         	</s:else>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</s:if>
				<s:else>
					<div class="table colspan2">
						<table>
							<thead>
								<tr>
									<th colspan="2"><div class="th_title">卖家联系方式</div></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="td_a">卖家姓名</td>
									<td class="td_b"><s:property value="name"/></td>
								</tr>
								<tr>
									<td class="td_a">联系地址</td>
									<td class="td_b"><s:property value="prov"/><s:property value="city"/><s:property value="district"/><s:property value="address"/></td>
								</tr>
								<tr>
									<td class="td_a">联系电话</td>
									<td class="td_b">
										<s:if test="mobile!=null && mobile!=''">
			                         		<s:property value="mobile"/>
			                         	</s:if>
			                         	<s:else>
			                         		<s:property value="phone"/>
			                         	</s:else>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</s:else>
			</s:iterator>
		</div>
	</div>
</div>
<!-- E Tab -->