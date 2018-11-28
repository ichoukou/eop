<%--
	@author ChenRen
	@date 	2011-09-05
	@description
		快递单打印模板编辑/设计页面
		
		该功能采用 电子商务系统-单据打印模板设计 的实现
	
	
	printtemp!toPrinttemp.action
--%>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="javax.xml.parsers.DocumentBuilder,
javax.xml.parsers.DocumentBuilderFactory,
javax.xml.parsers.*,
javax.xml.transform.*,
javax.xml.transform.dom.DOMSource,
javax.xml.transform.stream.StreamResult,
org.w3c.dom.*,
java.io.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">

<head>
	<!-- base href="<%=basePath%>"-->
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>单据模板设计</title>

	<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<script src="js/jquery-1.4.2.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/printtemp/swfobject.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/printtemp/swfobject.1-1-1.min.js?d=${str:getVersion() }"></script>	
	<link href="artDialog/skins/blue.css" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
</head>

<body>
<div id="main">
	<div id="main-content">
	<div id="midtit"> <span class="gnheader f14 loading">快递单模板详细信息</span>&nbsp;&nbsp;&nbsp;&nbsp;<font style="color:black;font-size: 12px;"  >温提高快递单各项内容打印位置的准确性，快递单的背景图片制作请使用扫描仪进行1：1比例扫描。<a href="javascript:openMessage();" style="color:#0000ff;text-decoration: underline;" target="main"> 我有想法</a></font></div>
		<div class="midfrom">
			<form method="post" id="orderExpress" 
					action="${ctxPath}/order/orderExpress.html" onsubmit="return validateOrderExpress(this);">
				<input type="hidden" name="orderExpressId" value="${orderExpress.orderExpressId}"/> 
				<input type="hidden" name="width" id="width" value="1000"/> 
				<input type="hidden" name="height" id="height" value="600"/> 
				<input id="templateData" name="templateData" type="hidden" value=""/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="com_modi_table">
				
				<tr>
					<td>	
						<table cellSpacing="0" cellPadding="0" border="0" class="orderExpressToolArea">
							<tr>
								<th><label class="required" for="orderExpressName">模板名称 (<span class="red">*</span>)</label>：</th>
								<td><input id="orderExpressName" type="text"  name="orderExpressName" value="${orderExpress.orderExpressName}" class="inputtxt" style="width:200px"/></td>
								<%-- 
								<th>配送方式：</th>
								<td>
								<select style="width:150px" name="shippingMethodId" id="shippingMethodId">
									<option value=""><fmt:message key="orderExpressDetail.shippingMethod.pleasSelect"/></option>
									<c:forEach var="shippingMethod" items="${shippingMethodList}">
										<option value="${shippingMethod.shippingMethodId}" <c:if test="${orderExpress.shippingMethodId eq shippingMethod.shippingMethodId}">selected="true"</c:if>>${shippingMethod.shippingMethodName}</option>
									</c:forEach>
								</select>
								</td>
								--%>
								<th><StoreAdmin:label key="orderExpress.status" />：</th>
								<td>
								<input type="checkbox" name="isShowCheck" id="isShowCheck" <c:if test="${empty orderExpress.status || orderExpress.status == 1}">checked</c:if>  onclick="if(this.checked){$('status').value=1;}else{$('status').value=0}"> 激活
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<StoreAdmin:label key="orderExpress.sortOrder" />
								<input type="hidden" name="status" id="status" value="${(empty orderExpress.status)?1:orderExpress.status}">
								<input type="text" value="${(empty orderExpress.sortOrder)?1:orderExpress.sortOrder}" style="width: 50px;" class="inputtxt" name="sortOrder" id="sortOrder" validconf="integer"/></td>
							</tr>
							<tr>
								<th>背景图片(<span class="red">*</span>)：</th>
								<td colspan="5"><input type="hidden" name="backgroundImageUrl" id="expressEditorBg" value="${orderExpress.backgroundImageUrl}" />
									<%-- 
									<input type="button" onclick="ExpressTplEditor.delBg()" value="删除" class="btn3"/>
									--%>
		                    		<input type="checkbox" onclick="ExpressTplEditor.lockbg(this.checked)" checked="" id="lock_bg">
		                    		<label for="lock_bg">锁定</label>
		                    	</td>
							</tr>					
							<tr>
								<th>打印内容项(<span class="red">*</span>)：</th>
								<td>
									<div id="box-print-ctrl">
										<input type="button" name="addPrintContent" value="添加" class="btn3"/> <input type="button" onClick="ExpressTplEditor.delItem()" value="删除" class="btn3"/>
										<div style="display:none;" id="print_item_box">
											<%
											//读取xml的内容
											DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
											dbf.setIgnoringElementContentWhitespace(true);
											DocumentBuilder db = dbf.newDocumentBuilder();
											Document doc = db.parse(pageContext.getServletContext().getResourceAsStream("/printElements.xml"));// /pages/order/printElements.xml
											doc.normalize();
											NodeList items = doc.getElementsByTagName("item");
											for(int it=0;it<items.getLength();it++){
												NodeList subitems  = items.item(it).getChildNodes();
												String itemName=null;
												String itemCode=null;
												for(int i=0;i<subitems.getLength();i++){
													String nodeName1=subitems.item(i).getNodeName();
													if(nodeName1.equals("name")){
														itemName=subitems.item(i).getFirstChild().getNodeValue();
													}
													if(nodeName1.equals("ucode")){
														itemCode=subitems.item(i).getFirstChild().getNodeValue();
														out.print("<span onclick=\"ExpressTplEditor.addElement('"+itemCode+"','"+itemName+"')\" name=\"menuitem\">"+itemName+"</span>");
													}	
												}									
											}
											%>	
										</div>
									</div>							
								</td>
								<th>字体调整：</th>
							</tr>
						</table> 
						
						<div id="expressTplEditor" class="express_tpl_editor">
						</div>
					</td>
				</tr>						
		   	</table>
		</form>
		</div>
		<!-- 
		<div id="x" class="express_tpl_editor">
	     	<object id="dly_printer_flash" type="application/x-shockwave-flash" data="printer.swf" height="100%" width="100%">
	           	<param name="quality" value="high">
				<param name="allowScriptAccess" value="always">
			    <param name="movie" value="printer.swf">
			    <param name="swLiveConnect" value="true">
			    <param name="flashVars" value="%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Email_no%3C/ucode%3E%3Cfont%3Ehttp%3A//static.test.yto56.com.cn/media/apple/order/110822/_-9050425655993134703.jpg%3C/font%3E%3Cfontsize%3E18%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A11%3A276%3A72%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Eorder_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A105%3A120%3A40%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E2%3A106%3A60%3A38%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A19%3A52%3A36%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A162%3A65%3A41%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Eshipment_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E80%3A162%3A120%3A40%3C/position%3E%3C/item%3E%3C/printer%3E">
			</object>
	    </div>
		 -->
	</div>
	</div>
	<script type="text/javascript">
	var ExpressTplEditor = {
	        tplEditor: null,
	        screenDPI: 96,
	        setBorder: function(){
	            this.tplEditor.flash(function(){
	                this.setBorder();
	            });
	        },
	        setItalic: function(){
	            this.tplEditor.flash(function(){
	                this.setItalic();
	            });
	        },
	        setFontSize: function(fontSize){
	            this.tplEditor.flash(function(){
	                this.setFontSize(fontSize);
	            });
	        },
	        setAlign: function(align){
	            this.tplEditor.flash(function(){
	                this.setAlign(align);
	            });
	        },
	        setFontSpace: function(fontSpace){
	            this.tplEditor.flash(function(){
	                this.setFontSpace(fontSpace);
	            });
	        },
	        delItem: function(){
	            this.tplEditor.flash(function(){
	                this.delItem();
	            });
	        },
	        resize: function(){
	            var size = {
	                width: $('ipt_prt_tmpl_width').value.toInt() * this.screenDPI / 25.4,
	                height: $('ipt_prt_tmpl_height').value.toInt() * this.screenDPI / 25.4
	            };
	            this.tplEditor.flash(function(){
	                this.setStyles(size);
	            });
	        },
	        setPicture: function(url){
	            this.tplEditor.flash(function(){
	                this.setBg(url);
	            });
	        },
	        addElement: function(itemCode, itemName){
	            this.tplEditor.flash(function(){
	                this.addElement(itemCode, itemName);
	            });
	            $j('#print_item_box').hide();
	        },
	        setData: function(){
	            return this.tplEditor.flash(function(){
	                document.getElementById('templateData').value = this.getData();
	            });
	        },
	        save: function(){
	            if (!validateOrderExpress($('orderExpress'))) {
	                return;
	            }
	            if (!$('expressEditorBg').value) {
	            	art.dialog.alert('请上传背景图片');
	                return;
	            }
	            try {
	                this.tplEditor.flash(function(){
	                    this.getData();
	                });
	                ;
	            } 
	            catch (err) {
	            	art.dialog.alert('请至少添加一个打印内容项');
	                return;
	            }
	            this.setData();
	            return fnDoSave(this);
	        },
	        setFont: function(font){
	            this.tplEditor.flash(function(){
	                this.setFont(font);
	            });
	        },
	        lockbg: function(checked){
	            this.tplEditor.flash(function(){
	                this.lockBg();
	            });
	        },
	        delBg: function(){
	            if ($j('#expressEditorBg').length != 0) {
	                $j('#expressEditorBg').val('__none__');
	            }
	            else {
	                $j("#orderExpress").add('<input id="expressEditorBg" name="backgroundImageUrl" type="hidden" value="__none__" />');
	            }
	            this.tplEditor.flash(function(){
	                this.delBg();
	            });
	        }
	    };
	    
		$(function() {
			/*
			<object id="dly_printer_flash" type="application/x-shockwave-flash" data="printer.swf" height="100%" width="100%">
		   	<param name="quality" value="high">
			<param name="allowScriptAccess" value="always">
		    <param name="movie" value="printer.swf">
		    <param name="swLiveConnect" value="true">
		    <param name="flashVars" value="%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Email_no%3C/ucode%3E%3Cfont%3Ehttp%3A//static.test.yto56.com.cn/media/apple/order/110822/_-9050425655993134703.jpg%3C/font%3E%3Cfontsize%3E18%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A11%3A276%3A72%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Eorder_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A105%3A120%3A40%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E2%3A106%3A60%3A38%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A19%3A52%3A36%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A162%3A65%3A41%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Eshipment_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E80%3A162%3A120%3A40%3C/position%3E%3C/item%3E%3C/printer%3E">
			</object>
			*/
			ExpressTplEditor.tplEditor = $("#expressTplEditor");
		    ExpressTplEditor.tplEditor.flash({
		        swf: 'printer.swf',
		        width: '100%',
		        height: '100%',
		        wmode: 'opaque',
		        flashvars: {
		            data: "%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Email_no%3C/ucode%3E%3Cfont%3Ehttp%3A//test.ec.yto56.net.cn/kernel/images/printtemp/yto_08.jpg%3C/font%3E%3Cfontsize%3E18%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A11%3A276%3A72%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Eorder_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E71%3A105%3A120%3A40%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u8BA2%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E2%3A106%3A60%3A38%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u9762%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A19%3A52%3A36%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Etext%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E5%3A162%3A65%3A41%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u5355%u53F7%3C/name%3E%3Cucode%3Eshipment_no%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E80%3A162%3A120%3A40%3C/position%3E%3C/item%3E%3C/printer%3E",
		            bg: null,
		            copyright: 'probiz'
		        }
		    });
		});
	</script>
</body>
</html>