<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>订单信息</title>
</head>
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<style type="text/css"> 
form textarea { width: 55% !important; font-family: Arial, Helvetica, sans-serif; }
.querybutton { font-family: Verdana, Arial, sans-serif; display: inline-block; background: #188cc9 url('../images/bg-button-green.gif') top left repeat-x !important; border: 1px solid #105d88 !important; padding: 1px 10px 0px 10px !important; color: #fff !important; font-size: 12px !important; cursor: pointer; *top:-4px; margin:0 4px;}
#tb { background-color:#fafdfe; border:1px #fff solid; line-height:22px; font-size:12px; color:#666; padding:0px 0px; }
form span{padding-bottom:0px;}

#test td{border-bottom:1px dashed #ccc;line-height:30px;}
.show{ position:relative; } 
.show a{ color:#fff;width:100px; line-height:20px;text-align:left; } 
.hid{ position:absolute; top:20px; left:0; background:#eee; border:1px solid #859ab7 } 
.hid a{ display:inline-block; color:#2f2f2f;display:block;padding:4px 10px; font-weight:normal;} 
.hid a:hover { background-color:#316ac4; color:#fff;}

</style>
<body>
<script type="text/javascript">
	var orderPage=<%=request.getParameter("orderPage") %>;
	if(orderPage!=null && orderPage==1){
		$("#a2").addClass("current");
		$("#a1").removeClass("current");
		$("#orderInfo").attr('style','display:block');
		$("#logistic").attr('style','display:none');
		$("#tab3").attr('style','display:none');
	}else{
		$("#a1").addClass("current");
		$("#a2").removeClass("current");
		$("#logistic").attr('style','display:block');
		$("#orderInfo").attr('style','display:none');
		$("#tab3").attr('style','display:none');
	}
	$(".content-box-header ul li a").click(function(){
		var id = $(this).attr("id");
		if(id=="a1"){
			$("#a1").addClass("current");
			$("#a2").removeClass("current");
			$("#a3").removeClass("current");
			$("#logistic").attr('style','display:block');
			$("#orderInfo").attr('style','display:none');
			$("#tab3").attr('style','display:none');
		}
		if(id=="a2"){
			$("#a2").addClass("current");
			$("#a1").removeClass("current");
			$("#a3").removeClass("current");
			$("#orderInfo").attr('style','display:block');
			$("#logistic").attr('style','display:none');
			$("#tab3").attr('style','display:none');
		}
		if(id=="a3"){
			$("#a3").addClass("current");
			$("#a1").removeClass("current");
			$("#a2").removeClass("current");
			$("#tab3").attr('style','display:block');
			$("#orderInfo").attr('style','display:none');
			$("#logistic").attr('style','display:none');
		}
	});
	
</script>
<!-- 此处的修改请注意对应父页面中div中的js代码-->
<div class="content-box fl" style="width:450px;height:400px;overflow-x:hidden;overflow-y:scroll;margin-left:9px;margin-top:9px;overflow: -moz-scrollbars-vertical;" id="logisticOrderInfoDiv">
	<div class="content-box-header">
		<ul class="content-box-tabs02" style="float:left">
	    	<li><a href="#" class="default-tab" id="a1">物流跟踪</a></li>
			<s:if test="orderProductList!=null && orderProductList.size>0">
				<s:if test="isDisplay==0">
					<li><a href="#" id="a2">订单详情</a></li>
				</s:if>
					<li><a href="#" id="a3">联系方式</a></li>
			</s:if>
		</ul>
	</div>
	<div class="content-box" style="width:96%;margin:10px auto;" id="content_box">
		<div class="tab-content default-tab" id="logistic">
			<div id="main-content">
			<table id="logistic_table">
				<tbody>
					<s:if test="stepsList!=null">
						<s:if test="stepsList.size==0">
						<tr>
							<td>
								暂无走件信息，请稍后查询
							</td>
						</tr>
						</s:if>
						<s:else>
						<s:iterator value="stepsList" var="steps">
	
							<tr>
						<td  width="40%"><p style=" line-height:16px;"><s:property value="acceptAddress"/></p>
						<p style="font-size:11px;"><s:property value="acceptTime.substring(0,10)"/>
									<s:property value="acceptTime.substring(10,18)"/></p></td>
                        <td width="36%"><s:property value="remark"/></td> 
                        <td width="24%" ><s:if test="name!=null && name!=''"> <s:property value="operate"/>:<s:property value="name"/></s:if> </td>
					</tr>
						</s:iterator>
						</s:else>
					</s:if>
					<s:else>
						<tr>
							<td colspan="2">
								暂无走件信息，请稍后查询
							</td>
						</tr>
					</s:else>
				</tbody>
			</table>
			</div>
		</div>
		<s:if test="orderProductList!=null && orderProductList.size>0">
		<s:if test="isDisplay==0">
		<div class="tab-content" id="orderInfo">
			<div class="pagination_"><jsp:include page="/WEB-INF/page_.jsp"></jsp:include></div>
			<br>
			<div id="main-content">
			<table id="order_table">
				<tbody>
					<s:iterator value="orderProductList" var="op">
					   <tr>
				    	<td width="35%">物流号</td>
				       	<td><s:property value="order.txLogisticId"/></td>
					   </tr>
				       <tr>
				       	<td width="35%">运单号</td>
				       	<td><s:property value="order.mailNo"/></td>
				       </tr>
				       <s:if test="order.lineType==0">
				       <tr>
				       	<td width="35%">物流取货时间段</td>
				       	<td>
				       		<s:if test="order.sendStartTime == '' || order.sendStartTime == null">任意时间</s:if>
				       		<s:else>
				       			<s:date name="order.sendStartTime" format="yyyy-MM-dd"/>至<s:date name="order.sendEndTime" format="yyyy-MM-dd"/>
				       		</s:else>
				       	</td>
				       </tr>
				       </s:if>
				       <tr>
				       	<td width="35%">重量(kg)</td>
				       	<td>
				       		<s:if test="order.weight==0"><font title='没有称重信息'>≤1</font></s:if>
				       		<s:else><s:property value="order.weight"/></s:else>
				       	</td>
				       </tr>
				       <tr>
				       <tr>
				       	<td width="35%">下单渠道</td>
				       	<td>
				       		<s:if test="order.lineType==0">线上下单</s:if>
				       		<s:elseif test="order.lineType==1">线下下单</s:elseif>
				       	</td>
				       </tr>
				       <tr>
				       	<td width="35%">订单状态</td>
				       	<td>
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
				       	<td width="35%">商品信息</td>
				       	<td>
				       		<input type="hidden" value="<s:property value='product.size()'/>" id="p_size"/>
				       		<table>
					       		<s:iterator value="product" var="pro">
				       				<tr>
				       					<s:if test="itemNumber==0">
				       						<td>只显示在线下单商品信息</td>
				       					</s:if>
				       					<s:else>
				       						<td><s:property value="itemName"/>（<s:property value="itemNumber"/>）</td>
				       					</s:else>
				       				</tr>
					       		</s:iterator>
				       		</table>
				       	</td>
				       </tr>
					</s:iterator>
				</tbody>
			</table>
			</div>
		</div>
		</s:if>
		
		    <div class="tab-content" id="tab3">
		    	<div id="main-content">
		    	<s:iterator value="orderTraderInfoList" var="traderInfo">
		    	<s:if test="tradeType == 1">
					<h2 style="font-size:15px; font-weight:600;line-height:30px;">买家联系方式</h2>
<table width="96%">
   <tbody id="test" valign="top">
   					<tr>
                         <td>买家姓名</td>
                         <td><s:property value="name"/></td>
					</tr>
					<tr>
                        <td width="40%">联系地址</td> 
                        <td width="60%" ><s:property value="prov"/><s:property value="city"/><s:property value="district"/><s:property value="address"/> </td>
					</tr>
					<tr>
                         <td>联系电话</td>
                         <td>
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
</s:if><s:else>
<h2 style="font-size:15px; font-weight:600;line-height:30px;">卖家联系方式</h2>

						<table width="96%">
   <tbody id="test" valign="top">
   					<tr>
                         <td>卖家姓名</td>
                         <td><s:property value="name"/></td>
					</tr>
					<tr>
                        <td width="40%">联系地址</td> 
                        <td width="60%" ><s:property value="prov"/><s:property value="city"/><s:property value="district"/><s:property value="address"/> </td>
					</tr>
					<tr>
                         <td>联系电话</td>
                         <td>
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
			</s:else>
	</s:iterator>
				<script type="text/javascript">
					$("tr").not(':first').hover(
					  function () {
					    $(this).css("background","#f1f1f1");
					  },
					  function () {
					    $(this).css("background","");
					  }
					);
				</script>		
					</div>
					</div></s:if>
	</div>
</div>
</body>
</html>
