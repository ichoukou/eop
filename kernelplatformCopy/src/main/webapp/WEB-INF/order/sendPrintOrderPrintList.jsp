<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<%@ taglib uri="/WEB-INF/tlds/str.tld" prefix="str" %>
<!DOCTYPE HTML>
<html lang="zh-CN">
    <head>
        <meta charset="UTF-8" />
        <link rel="stylesheet" type="text/css" href="css/base/reset.css?d=${str:getVersion() }" media="all" />
        <link rel="stylesheet" type="text/css" href="css/module/button.css?d=${str:getVersion() }" media="all" />
        <link rel="stylesheet" type="text/css" href="css/page/print_invoice.css?d=${str:getVersion() }" media="all" />
        <link rel="stylesheet" type="text/css" href="css/page/invoice_for_printer.css?d=${str:getVersion() }" media="print" />
        <title>打印发货单</title>
    </head>
    <body>
        <div class="print_btn">
            <a href="javascript:;" class="btn btn_a" title="打印"><span>打 印</span></a>
        </div>

    <s:iterator value="clickBatchOrderPrintSendList" status="topidx">
        <div class="invoice_preview">
            <table class="table_a">
                <thead>
                    <tr>
                        <th colspan="4">发 货 单</th>
                    </tr>
                </thead>

                <tbody>
                    <tr class="order_info">
                        <td width="20%">店铺名称：<s:property value="saleName"/></td>
                <td width="33%">运单号：<s:property value="mailNo"/></td>
                <td width="22%">订单号：<s:property value="tradeNo"/></td>
                <td width="25%">购买时间：<s:date name="createTime" format='yyyy-MM-dd'/></td>
                </tr>
                <tr class="contact_info">
                    <td>收件人：<s:property value="buyName"/></td>
                <td>联系方式：<s:property value="buyMobile"/>  <s:property value="buyTelphone"/></td>
                <td colspan="2">收货地址：<s:property value="buyFulladdress"/></td>
                </tr>
                </tbody>
            </table>

            <table class="table_b">
                <tbody>
                    <tr class="goods_info">
                        <td width="10%">序号</td>
                        <td width="60%">商品名称</td>
                        <td width="15%">数量</td>
                        <td width="15%">总额（元）</td>
                    </tr>
                <s:set name="product_name" value="productName.split(\"</dd>\")"></s:set>
                <s:iterator value="product_name" status="idx" var="goods">
                    <tr class="goods_detail">
                        <td><s:property value="#idx.index+1"/></td>
                         <s:set name="tmp" value="#goods.replace('<dd>','')"></s:set>
                         <s:set name="tmp1" value="#tmp.replaceAll(',,,','`')"></s:set>
                	    <s:generator separator="`" val="#tmp1">
                        <s:iterator>
                            <td> <s:property/></td>
                        </s:iterator>
                      </tr>
                    </s:generator>
                </s:iterator>

                </tbody>
            </table>
        </div>

        <!-- S 打印分割线 -->
		<s:if test="#topidx.index < clickBatchOrderPrintSendList.size()-1"><span class="page_break"></span></s:if>
		
        
        <!-- E 打印分割线 -->

    </s:iterator>



    <div class="print_btn">
        <a href="javascript:;" class="btn btn_a" title="打印"><span>打 印</span></a>
    </div>
    <script type="text/javascript" src="js/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
    <script type="text/javascript">
        $(function() {
            $('.print_btn a').click(function(ev) {
                ev.preventDefault();
            	$.ajax({
            		type: 'POST',
            		data: {
            			markPrintSend: '<s:property value="orderPrintIds"/>'
            		},
    				url: 'orderPrint!updateIsPrintSendBatch.action',
    				success: function() {
    					window.print();
    				}
    			});
            });
        });
    </script>
</body>
</html>