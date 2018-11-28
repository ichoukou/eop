<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
   <link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
    <!--link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css" media="all" /-->
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link href="${cssPath}/page/print_new.css?d=${str:getVersion() }" rel="stylesheet" type="text/css">
	<!-- E 当前页面 CSS -->
	<title>打印内容</title>
	<style>
		#content{padding:0;background:#fff;}
	</style>

</head>
<body id="content">
	<table class="prints">
                       <tr>
                         <td colspan="4" class="date" align="right">
                         <div class="td">
                         上联：此联由圆通速递留存
 						 </div>
                         </td>
                       </tr>
                       <tr>
							<td colspan="3">
								<div class="td txt_middle" id="key_num">
									 <c:if test="${str:isNotEmpty(printInvoice.mailNo)}">
									 	<p class="keycode">*${printInvoice.mailNo}*</p>
									 </c:if>
									
									 <c:if test="${str:isNotEmpty(printInvoice.mailNo)}">
									 	<p id="number">*${printInvoice.mailNo}*</p>
									 </c:if>
									
							  </div>
							</td>
							<td width="181" class="area">
								<c:set var="bigpenlen" value="${fn:length(printInvoice.bigPen)}" scope="request"/>
								<c:choose>
									<c:when test="${bigpenlen == 2}">
										<div class="td txt_middle rtd two">
									</c:when>
									<c:when test="${bigpenlen == 3}">
										<div class="td txt_middle rtd three">
									</c:when>
									<c:when test="${bigpenlen == 4}">
										<div class="td txt_middle rtd four">
									</c:when>
									<c:when test="${bigpenlen == 5}">
										<div class="td txt_middle rtd five">
									</c:when>
									<c:when test="${bigpenlen == 6}">
										<div class="td txt_middle rtd six">
									</c:when>
								</c:choose>
									<span>${printInvoice.bigPen}</span> 
								</div>
							</td>
                       </tr>
                       <tr class="rb">
							<td width="21" rowspan="2" class="title">
								<div class="td txt_middle">收件人</div>
							</td>
							<td colspan="2" class="nobd add">
								<div class="td">
									<p>${printInvoice.buyProv}-${printInvoice.buyCity}-${printInvoice.buyDistrict}</p>
									<p>${printInvoice.buyAddress}</p>
								</div>
							</td>
							<td class="rb nobd">
								<div class="td txt_right">邮编：${printInvoice.buyPostCode}</div>
							</td>
                       </tr>
                       <tr class="rb">
							<td width="242" class="nobd">
								<div class="name">${printInvoice.buyName}</div>
							</td>
							<td colspan="2" class="nobd rb">
								<div>电话：${printInvoice.buyMobile != '' ? printInvoice.buyMobile : printInvoice.buyTelPhone != '' ? printInvoice.buyTelPhone : ''}</div>
							</td>
                       </tr>
                       <tr>                       
							<td rowspan="5" class="title">
								<div class="td txt_middle">详细内容</div>
							</td>
							<td colspan="2" class="info td">
								<div>订单号：<span class="ticket_code">${printInvoice.txLogisticId}</span></div>
							</td>
							<td rowspan="5" class="rb">
								<div class="autograph td"> 
									<p>收件人签名：<span class="use"></span></p>
                                    <p class="zjh">证件号：<span class="number"></span></p>
                                    <p class="nyr"><span>年</span><span>月</span><span>日</span></p>
								</div>
								<div class="allograph td"> 
									<p>收件人签名：<span class="use"></span></p>
                                    <p class="zjh">证件号：<span class="number"></span></p>
                                    <p class="nyr"><span>年</span><span>月</span><span>日</span></p>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<div>发货单号：<span class="send_code">${printInvoice.deliverNo}</span></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<div>发件人：<span class="sender">${printInvoice.userName}</span></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<div>内容品名：<span class="weight">${printInvoice.itemName}</span></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<span>数量：${printInvoice.itemNumber}份</span>
								<span class="case">重量：${printInvoice.weight}千克</span>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<div class="explan">
									<span class="note">圆通速递将快件送达收件人地址，经收件人或收件人(寄件人)允许的代收人签字，视为送达。
									</span>
								</div>
								<div class="price">
									<p class="money">代收款金额(小写)：￥${printInvoice.agencyFund}元</p>
									<p class="total" id="total">(大写)：${printInvoice.agencyFund}</p>
								</div>
							</td>
						</tr>
                       <tr>
							<td colspan="4" class="spt">
								<div class="txt_middle">
									<span>--------------------------------------</span>
								</div>
							</td>
                       </tr>
                       <tr>
                         <td colspan="4" class="date" align="right">
                         <div class="td">
                         下联：此联收件人留存
 						 </div>
                         </td>
                       </tr>
                       <tr>
							<td colspan="4" class="date">
								<div class="td logo"> </div>
							</td>
                       </tr>
                       <tr>
                        <td colspan="4">
							<div class="td" id="key_num2">
								<p align="center">
									<span class="c_nub">运单号：</span>
									 <c:if test="${str:isNotEmpty(printInvoice.mailNo)}">
									 	<span id="number">*${printInvoice.mailNo}*</span>
									 </c:if>
									 <c:if test="${str:isNotEmpty(printInvoice.mailNo)}">
									 	<span id="number"></span>
									 </c:if>
								</p>
							</div>
						 </td>
                       </tr>
						<tr class="rb">
							<td width="21" rowspan="2" class="title">
								<div class="td txt_middle">收件人</div>
							</td>
							<td colspan="2" class="nobd add">
								<div class="td">
									<p>${printInvoice.buyProv}-${printInvoice.buyCity}-${printInvoice.buyDistrict}</p>
									<p>${printInvoice.buyAddress}</p>
								</div>
							</td>
							<td class="rb nobd">
								<div class="td txt_right">邮编：${printInvoice.buyPostCode}</div>
							</td>
						</tr>
						<tr class="rb">
							<td width="242" class="ad_na nobd">
							<div class="name">${printInvoice.buyName}</div>
							</td>
							<td colspan="2" class="ad_na nobd rb">
								<div>电话：${printInvoice.buyMobile != '' ? printInvoice.buyMobile : printInvoice.buyTelPhone != '' ? printInvoice.buyTelPhone : ''}</div>
							</td>
						</tr>
						<tr>                       
							<td rowspan="5" class="title">
								<div class="td txt_middle">详细内容</div>
							</td>
							<td colspan="2" class="info td">
								<div>订单号：<span class="ticket_code">${printInvoice.txLogisticId}</span></div>
							</td>
							<td rowspan="5">
								<div class="td appoiut"> 
									<span style="">此运单仅供上海圆通速递签约客户使用，相关责任义务以双方合作合同为准。</span> 
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<div>发货单号：<span class="send_code">${printInvoice.deliverNo}</span></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<div>发件人：<span class="sender">${printInvoice.userName}</span></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<div>内容品名：<span class="weight">${printInvoice.itemName}</span></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="info td">
								<span>数量：${printInvoice.itemNumber}份</span>
								<span class="case">重量：${printInvoice.weight}千克</span>
							</td>
						</tr>
                     </table>
					<object classid="clsid:AF33188F-6656-4549-99A6-E394F0CE4EA4" id="pazu" name="pazu" width="0" height="0">  
   						<param name="License" value="2AE816BA3A24A9BA3F01162E7BF420F4"/>  
  				    </object>
	<script type="text/javascript">
		var params = {
			print: ${print} && ${isIE},	  // 是否需要打印
			code: '${resultCode}',
			status: '${printInvoice.status}',
			num: '${printInvoice.mailNo}',
			isIE: ${isIE}
		};
	</script>
	<script type="text/javascript">
		var format = function(num) {  
			var strOutput = "";  
			var strUnit = '仟佰拾亿仟佰拾万仟佰拾元角分';  
			num += "00";  
			var intPos = num.indexOf('.');  
			if (intPos >= 0){
				num = num.substring(0, intPos) + num.substr(intPos + 1, 2);
			}
			strUnit = strUnit.substr(strUnit.length - num.length);  
			for (var i=0; i < num.length; i++){
				strOutput += '零壹贰叁肆伍陆柒捌玖'.substr(num.substr(i,1),1) + strUnit.substr(i,1);  
			}
			return strOutput.replace(/零角零分$/, '整').replace(/零[仟佰拾]/g, '零').replace(/零{2,}/g, '零').replace(/零([亿|万])/g, '$1').replace(/零+元/, '元').replace(/亿零{0,3}万/, '亿').replace(/^元/, "零元");  
		};
		
		window.onload = function(){
		    var winParams = window.params || {};
		    
		    var total = document.getElementById('total'),
		        val = total.innerHTML.replace("(大写)：","");
		    
		    if(!isNaN(parseFloat(val))){
		    	total.innerHTML = '(大写)：' + format(val);
		    }
		
		    // 配置
		    var config = {
		   	   print: winParams.print,              // 是否需要打印
		   	   code: winParams.code,
			   status: winParams.status,
			   num: winParams.num,
			   isIE: winParams.isIE
		    };

			var topFrame = window.top
			var newPrinters = topFrame.newPrints;
			newPrinters.showDialog(config.code, config.status, config.num, config.isIE);
			
			//topFrame.deliverNo.value = '';
			//topFrame.deliverNo.focus();
			
		    if(!config.print){
				return;
			}

			var pazu = document.getElementById('pazu');
		     pazu.TPrinter.marginTop = 0;                    //属性 上边距
		     pazu.TPrinter.marginBottom = 0;                 //属性 下边距
		     pazu.TPrinter.marginLeft = 0;                   //属性  左边距
		     pazu.TPrinter.marginRight = 0;                  //属性  右边距
		     pazu.TPrinter.footer='';                  //属性 页脚
		     pazu.TPrinter.header='';                  //属性  页眉
		     pazu.TPrinter.orientation = 1;                   //属性 整型：纸张方向 1=纵向  2=横向
		     pazu.TPrinter.isPrintBackground = true;    //属性  是否打印背景 true / false
		     pazu.TPrinter.isZoomOutToFit = false;           //属性   是否缩放以适应大小打印 true / false
		     pazu.TPrinter.printTemplate = '';                 //属性   打印模板的URL
		     pazu.TPrinter.copies = 1;               //属性   打印份数
		     
		     topFrame.frames["content_ifm"].focus();
		     setTimeout(function(){
		    	 pazu.TPrinter.doPrint(false);
		     }, 0);
		};  
	</script>
</body>
</html>