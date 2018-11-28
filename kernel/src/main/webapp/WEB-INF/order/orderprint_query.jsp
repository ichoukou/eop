<%--
	@author 		ChenRen
	@date 			2012-02-22
	@description 	订单打印 - 订单查询
--%>
<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http:/www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>面单打印</title>
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/jquery-easyui/easyui.css?d=${str:getVersion() }" type="text/css" />
<link rel="stylesheet" href="css/jquery-easyui/icon.css?d=${str:getVersion() }" type="text/css" />
<link rel="stylesheet" href="css/layoutshow.css?d=${str:getVersion() }" type="text/css" />
<link rel="stylesheet" href="css/layout.css?d=${str:getVersion() }" media="screen" type="text/css" />
<link href="artDialog/skins/blue.css?d=${str:getVersion() }" rel="stylesheet"/>
<style type="text/css">
	.pagination a { text-decoration: none; }
	td a{text-decoration:underline; cursor:pointer;}
	.numAlignRight {text-align: center;}
	select {padding: 1px 0px;}
	form span {padding: 0px;}
	.Ticon {
		display: inline-block;
		width: 14px;
		height: 14px;
		vertical-align: middle;
		cursor: pointer;
	}
	.Ticon-expand {background: url(images/ico_jia.png) no-repeat;}
	.Ticon-shrink {background: url(images/ico_jian.png) no-repeat;}
	.Thidden {display: none;}
	.Tinline {display: inline;}
	table.Ttable td {padding: 0px;}
</style>
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery-easyui/easyui-lang-zh_CN.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/simpla.jquery.configuration.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/facebox.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/jquery.wysiwyg.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="js/validate.js?d=${str:getVersion() }"></script>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>

</head>
<body>
	<div id="main">
		<div id="main-content">
			<div id="midtit"> 
				<span class="gnheader f14 loading">面单打印</span>&nbsp;&nbsp;&nbsp;&nbsp;
				<font style="color: black; font-size: 12px; font-weight: normal;"></font>
			</div>
<p  class="ts" style="display:black;font-size:12px; height:26px;line-height:26px;background-color:#fcfae5; margin-top:10px;">设置打印机→输入运单号→选择面单模板→打印→发货</p>
			<div style="font-size: 14px; margin-top: 10px;">
				<input type="button" style="float: left; padding-top: 0px;" value="打印机设置" class="submit02" name="button" onclick="window.print();">
				<div style="float: right; width: 210px; display: inline;">
					<span style="float: left;">显示订单数：</span>
					<select id="selshouNum">
						<option value="100" <s:if test="shouNum == 100">selected</s:if>>100</option>
						<option value="50" <s:if test="shouNum == 50">selected</s:if>>50</option>
						<option value="30" <s:if test="shouNum == 30">selected</s:if>>30</option>
						<option value="10" <s:if test="shouNum == 10">selected</s:if>>10</option>
						<option value="5" <s:if test="shouNum == 5">selected</s:if>>5</option>
					</select>
				</div>
				<div class="clearfloat"></div>
			</div>
			<div style="font-size:14px; margin-top: 5px;">
			
			</div>
			<!-- 查询条件 -->
			<div id="midfrom">
			<table style="border: 0px;">
			<tr>
				<td style="width: 70px;height:20px;"><span style="font-weight:bold">温馨提示：</span></td>
				<td style="color:#f00; font-size:12px;height:20px;">1、暂不支持淘宝“自己联系物流”下的订单哦 ╯□╰</td>
			</tr>
			<tr>
				<td style="height:20px;"></td>
				<td style="color:#f00; font-size:12px;height:20px;">2、淘宝“推荐物流“下的订单,可打单哦 ^_^</td>
			</tr>
			<tr>
				<td style="height:20px;"></td>
				<td style="color:#f00; font-size:12px;height:20px;">3、支持B2C与圆通速递对接后，可打单哦 ^_^</td>
			</tr>
			</table>
				<form id='Tform' method="post" action="order!queryOrder.action">
					<input id='shouNum' name='shouNum' type="hidden" value="<s:property value="shouNum"/>">
					<input id='currentPage' name='currentPage' value="<s:property value="currentPage"/>" type="hidden">
					
					<p style="position: relative;">
						<span>发货时间：</span>
					    <input class="easyui-datebox" name="starttime" id="starttime" style="width:100px;" value="<s:property value='starttime'/>" />
					    <span>  至  </span>
						<input class="easyui-datebox" name="endtime" id="endtime" style="width:100px;" value="<s:property value='endtime'/>" />
						<!-- 
						<select name="" style="margin-left:10px;">
							<option>选择客户</option>
						</select>
						 -->
						<c:if test="${yto:getCookie('userType') == 1
			        		|| yto:getCookie('userType') == 11
			        		|| yto:getCookie('userType') == 12
			        		|| yto:getCookie('userType') == 13}">
			        	<%-- 根据后台逻辑. 卖家直接取个性化配置的信息 --%>
			        	</c:if>
						<c:if test="${yto:getCookie('userType') == 2 
					   		|| yto:getCookie('userType') == 21
					   		|| yto:getCookie('userType') == 22
					   		|| yto:getCookie('userType') == 23}">
							 &nbsp;&nbsp;
							<select id="customerCode" name="customerCode" class="easyui-combobox" style="width:150px;">
								<option value='请选择客户'>请选择客户</option>
								<s:iterator value="vipThreadList">
									<option <s:if test="%{customerCode == userCode}">selected</s:if> value="<s:property value='userCode'/>"><s:property value="userName"/>(<s:property value="userCode"/>)</option>
								</s:iterator>
							</select>
			        	</c:if>
						<select name="status" style="margin-left:10px;">
							<option value='all' <s:if test="status == \"all\"">selected</s:if>>所有订单</option>
							<option value='NOPRINT' <s:if test="status == \"NOPRINT\"">selected</s:if>>未打印</option>
							<option value='PRINTED' <s:if test="status == \"PRINTED\"">selected</s:if>>已打印</option>
							<option value='0' <s:if test="status == \"0\"">selected</s:if>>已发货</option>
						</select>
						<input type="submit" value="查询" class="submit01" style="margin-left: 10px;">
					</p>
				</form>
			</div>
			<!-- 运单设置、运单列表 -->
			<div class="content-box" style="width:100%; margin:0px;">
			<div style="padding:10px 10px;">
				<!-- 请输入初始运单号 vv6031446559-->
				<form>
				<input id='initMailNo' value="请输入初始运单号" title='初始运单号' class="text-input" />
				<select id="mailNoStep" title='递增值'>
					<!-- <option value="0">递增值</option> -->
					<option value="1" selected="selected">选择递增1</option>
					<option value="2">选择递增2</option>
					<option value="3">选择递增3</option>
					<option value="4">选择递增4</option>
					<option value="5">选择递增5</option>
				</select>
				&nbsp;&nbsp; 
				<select name="orderExpId" id="orderExpId">
					<s:if test="orderExpList.size == 0">
						<option value='0'>没有打印模板</option>
					</s:if>
					<s:else>
					<s:iterator value="orderExpList">
						<option <s:if test="%{id == orderExpId}">selected</s:if> value="<s:property value='id'/>"><s:property value="orderexpressname"/></option>
					</s:iterator>
					</s:else>
					<option onclick="openMessage()">新增模板...</option>
				</select>
				<!-- 
				<option>圆通面单</option>
				<option>申通快递</option>
				<option>佳吉快运</option>
				 -->
				&nbsp;&nbsp; 
				<input type="button" id='btn_print' value="打&nbsp;印" class="submit01">&nbsp;
				<input type="button" id='btn_send' value="发&nbsp;货" class="submit01">
				</form>
				</div>
				<!-- 数据列表 -->
				<div style="width: 100%" class="content-box">
					<div class="tab-content">
						<table>
							<thead>
								<tr id='tr_title'>
									<th width="1%"><input type="checkbox" id="chk-head"></th>
									<th align="center" width="18%">运单号</th>
									<th align="center" width="8%">订单号</th>
									<th align="center" width="29%">收件地址</th>
									<th align="center" width="8%">收件姓名</th>
									<th align="center" width="8%">打印状态</th>
								</tr>
							</thead>
							<tbody id='tb-tbd'>
			          			<s:iterator value="dtoOrderPrintList" var="dtoOrderPrint" status="stuts">
			          				<tr name='m'>
										<td width="1%"><input type="checkbox" title='选择该订单进行 打印/发货 操作' value='<s:property value="#dtoOrderPrint.order.id"/>'></td>
										<td>
											<span class="Ticon Ticon-expand" title='展开/隐藏运单详情' onclick='咪路(this,<s:property value="#stuts.index"/>)'></span>
											<input value="<s:property value="#dtoOrderPrint.order.mailNo"/>" class="mailNo" style="width:90px; cursor: default;" title='运单号' readonly>
											<div class='div_unedit Tinline'>
												<a href='javascript:;' title='编辑运单号' class="editMailNo" >编辑</a>
											</div>
											<div class="div_inedit Thidden">
												<a href='javascript:;' class="editMailNo_ok" >确定</a>
												<a href='javascript:;' class="editMailNo_cancl" >取消</a>
											</div>
										</td>
										<td class='lja'><s:property value="#dtoOrderPrint.order.txLogisticId"/></td>
										<td class='lja'>
											<s:property value="#dtoOrderPrint.bprov"/>
											<s:property value="#dtoOrderPrint.bcity"/>
											<s:property value="#dtoOrderPrint.bdistrict"/>
											<s:property value="#dtoOrderPrint.baddress"/>
										</td>
										<td class='lja'><s:property value="#dtoOrderPrint.bname"/></td>
										<td class='lja'>
											<s:if test="#dtoOrderPrint.order.status == \"PRINTED\"">已打印</s:if>
											<s:elseif test="#dtoOrderPrint.order.status == \"0\"">已发货</s:elseif>
											<s:else>未打印</s:else>
										</td>
									</tr>
									<tr id='<s:property value="#stuts.index"/>' style="display: none; width: 80%;" name='n'>
									<td colspan="6" style="padding: 0px;">
										<table class='Ttable'>
											<tr>
												<td colspan="2" style="font-weight:bold; border: 0 none;">收件信息</td>
												<td colspan="2" style="font-weight:bold; border: 0 none;">发件信息</td>
											</tr>
											<tr>
												<td style="padding: 0px; border: 0 none; height: 24px;">商品名称：</td>
												<td style="padding: 0px; border: 0 none; height: 24px;">
													<s:iterator value="#dtoOrderPrint.product" var="product">
														<label>
														<span title='商品名称'><s:property value="#product.itemName"/></span>&nbsp;&nbsp;(&nbsp;<span title='商品数量' style='text-decoration:underline; cursor:pointer;'><s:property value="#product.itemNumber"/></span>&nbsp;)
														</label>
														<br>
													</s:iterator>
												</td>
												<td style="padding: 0px; border: 0 none; height: 24px;">网点名称：</td>
												<td style="padding: 0px; border: 0 none; height: 24px;"><s:property value="#dtoOrderPrint.sshopname"/></td>
											</tr>
											<tr>
												<td style="padding: 0px; border: 0 none; height: 24px;">联系方式：</td>
												<td style="padding: 0px; border: 0 none; height: 24px;"><s:property value="#dtoOrderPrint.bmobile"/></td>
												<td style="padding: 0px; border: 0 none; height: 24px;">联系方式：</td>
												<td style="padding: 0px; border: 0 none; height: 24px;"><s:property value="#dtoOrderPrint.smobile"/></td>
											</tr>
											<tr>
												<td style="padding: 0px; border: 0 none; height: 24px;">邮政编码：</td>
												<td style="padding: 0px; border: 0 none; height: 24px;"><s:property value="#dtoOrderPrint.bpostcode"/></td>
												<td style="padding: 0px; border: 0 none; height: 24px;">发货地址：</td>
												<td style="padding: 0px; border: 0 none; height: 24px;">
													<s:property value="#dtoOrderPrint.sprov"/>
													<s:property value="#dtoOrderPrint.scity"/>
													<s:property value="#dtoOrderPrint.sdistrict"/>
													<s:property value="#dtoOrderPrint.saddress"/>
												</td>
											</tr>
											<tr>
												<td style="padding: 0px; border: 0 none; height: 24px;"></td>
												<td style="padding: 0px; border: 0 none; height: 24px;"></td>
												<td style="padding: 0px; border: 0 none; height: 24px;">发货日期：</td>
												<td style="padding: 0px; border: 0 none; height: 24px;"><s:date name="#dtoOrderPrint.order.createTime" format="yyyy-MM-dd"/></td>
											</tr>
										</table></td>
									</tr>
			          			</s:iterator>
							</tbody>
						</table>
					</div>
				</div>
		</div>
	</div>
	</div>
<script type="text/javascript">
	$(function() {
		$('#starttime').datebox({   
			    required:true,
			    missingMessage:'请选择开始发货时间',
			    validType:'dateboxCheck'
		});  
		$('#endtime').datebox({   
		    required:true,
		    missingMessage:'请选择结束发货时间',
		    validType:'dateboxLimit'
		});
		$('#customerCode').combobox({   
		    required:true,
		    missingMessage:'请选择客户',
		    validType:'xxx'
		});
		$.extend($.fn.validatebox.defaults.rules, {
		    dateboxCheck: {   
		        validator: function(value, param){
		        	var starttime = $("#starttime").datebox('getValue');
		        	var endtime = $("#endtime").datebox('getValue');
		        	if(starttime > endtime)
		        		return false;
		            return true;   
		        },
		        message: '起点时间应在止点时间之前!'
		    }   
		});
		$.extend($.fn.validatebox.defaults.rules, {
		    dateboxLimit: {   
		        validator: function(value, param){
		        	var starttime = $("#starttime").datebox('getValue');
		        	var endtime = $("#endtime").datebox('getValue');
		        	var sd = starttime.split("-"), ed = endtime.split("-");
		        	var x = Date.parse(ed[1]+"/"+ed[2]+"/"+ed[0]) - Date.parse(sd[1]+"/"+sd[2]+"/"+sd[0]),
		        		maxX = 1000 * 60 * 60 * 24 * 31;
		            return !(x > maxX);
		        },   
		        message: '请查询30天之内的数据'
		    }   
		});
		
	    $.extend($.fn.validatebox.defaults.rules, {  
	    	xxx: {
	            validator: function(value, param){
	                return value != '请选择客户';
	            },  
	            message: '请选择客户'  
	        }  
	    });  
	    
		$('#Tform').submit(function() {
			if($('#starttime').datebox('isValid') && $('#endtime').datebox('isValid') ) {
				if(checkValidate()){
					if($('#customerCode').length){
						if($('#customerCode').combobox('isValid')){
							return true;
						}
					}
					return true;
				}
			}
			return false;
		});
		
		_alert = function(msg, callback) {
			callback = typeof(callback) == 'function' ? callback : {};
			art.dialog({
			    icon: 'warning',
			    content: msg,
			    ok: callback,
			    okVal:'确定',
	          	opacity:0.1,
	          	padding:'35px 35px'
			});
		};
		
		_confirm = function(msg, okFun, canclFun) {
			okFun = typeof(okFun) == 'function' ? okFun : {};
			canclFun = typeof(canclFun) == 'function' ? canclFun : {};
			art.dialog({
			    icon: 'question',
			    content: msg,
			    okVal:'确定',
			    ok: okFun,
			    cancelVal: '关闭',
			    cancel: canclFun,
	          	opacity:0.1,
	          	padding:'35px 35px'
			});
		};
		
		_swapEditMailNo = function(_this, flag, action) {
			var pdiv = $(_this).parent('div'),
				ndiv = pdiv.next(),
				pinput = pdiv.prev('input');
				
			pdiv.css('display', 'none').before(ndiv);
			ndiv.css('display', 'inline');
			eval("pinput.focus()." + (flag ? "removeAttr('readonly').css('cursor','text');" : "attr('readonly','readonly').css('cursor','default');"));
			
			//if(action == 'cancl') pinput.val('');
		};
		
		$('.editMailNo').click(function(){
			_swapEditMailNo(this, true);	return false;
		});
		
		$('.editMailNo_cancl').click(function(){
			_swapEditMailNo(this, false, 'cancl'); 	return false;
		});
		
		$('.editMailNo_ok').click(function(){
			var _input = $(this).parent().prev(), _mailNo = _input.val().replace('请输入初始运单号',''), _msg, _this = this;
			_msg = !$.trim(_mailNo) ? '请输入运单号' : (!_mailNoRegx.test(_mailNo) ? '运单号格式不正确' : '');
			_msg ? _alert(_msg, function() {_input.focus()}) : (function(){
				_confirm('是否递增后面的运单号?',
					function() {_setNextMailNo($(_this).parent().parent().prev().find(':checkbox'),true)}
				);
				_swapEditMailNo(_this, false);
			})();
			return false;
		});
		
		$('#chk-head').click(function() {
			var _this = this;
			_checkInitMailNo(_this, function(){
				var _ck_head = _this.checked;
				$('#tb-tbd :checkbox').each(function() {
					this.checked = _ck_head;
					_ck_head ? _setMailNo(this) : 'depon';
				});
			});
		});
		
		_checkInitMailNo = function(_this, callback) {
			var _mailNo = $('#initMailNo').val().replace('请输入初始运单号','');
			var _msg = !$.trim(_mailNo) ? '请输入初始运单号' : (!_mailNoRegx.test(_mailNo) ? '运单号格式不正确' : '');
			_msg ? _alert(_msg, function() {$('#initMailNo').focus(); _this.checked = false;}) : callback();
		};

		var _mailNoRegx = /^(0|1|2|3|4|5|6|7|8|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/;
		_setMailNo = function(_checkbox, _srcCheckbox) {
			_srcCheckbox = typeof(_srcCheckbox) == 'undefined' ? _checkbox : _srcCheckbox;
			var tr = $(_checkbox).parent().parent(), prev_tr = tr.prevAll('tr:visible').first(), mailNo;
			if(prev_tr.length) {
				var prev_chk = prev_tr.find(':checkbox');
				if(prev_chk.attr("checked")) {
					mailNo = prev_tr.find('.mailNo').val();
				} else {
					return _setMailNo(prev_chk, _srcCheckbox);
				}
			} else {
				mailNo = $('#initMailNo').val();
			}
			
			if(_mailNoRegx.test(mailNo)) {
				var step = parseInt($('#mailNoStep').val());
				mailNo = mailNo.substring(0,6) + (mailNo.substring(6)-0 + step);
				$(_srcCheckbox).parent().parent().find(':text').val(mailNo);
			} else {
				_alert("运单号不对:"+mailNo);
			}
		};
		
		_setNextMailNo = function(_checkbox, flag) {
			var tr = $(_checkbox).parent().parent();
			var step = parseInt($('#mailNoStep').val());
			var next_tr = tr.nextAll('tr:visible');
			var isCheck = _checkbox.checked;
			if(typeof(flag) == 'undefined') {
				$.each(next_tr, function(i,t) {
					if($(this).find(':checkbox').attr("checked")) {
						var mailNo = $(this).find(':text').val(), mailNo_end = mailNo.substring(6);
						mailNo_end = isCheck ? mailNo_end-0 + step : mailNo_end - step;
						$(this).find(':text').val(mailNo.substring(0,6) + mailNo_end);
					}
				});
			}
			else {
				var currMailNo = tr.find(':text').val(), currMailNo_end = currMailNo.substring(6);
				isCheck = flag;
				$.each(next_tr, function(i,t) {
					if($(this).find(':checkbox').attr("checked")) {
						currMailNo_end = isCheck ? currMailNo_end-0 + step : currMailNo_end - step;
						$(this).find(':text').val(currMailNo.substring(0,6) + currMailNo_end);
					}
				});
			}
		};
		
		$('#tb-tbd :checkbox').click(function() {
			var _this = this;
			if($.trim($(this).parent().parent().children(":last-child").text())!='已打印'){
				_checkInitMailNo(_this, function(){
					_setMailNo(_this);
					_setNextMailNo(_this);
				});
			}
		});
		
		$('#initMailNo').focus(function() {
			if($(this).val() == '请输入初始运单号') {
				$(this).val('');
			}
		});
		$('#initMailNo').blur(function() {
			if(!$(this).val()) {
				$(this).val('请输入初始运单号');
			}
		});
		
		$('.lja').click(function() {
			$(this).parent().find('.Ticon').click();
		});
		
		$('#selshouNum').change(function() {
			$('#shouNum').val($(this).val());
		});
		$('#selshouNum').change();
		
		$("#tb-tbd tr[name='m']")
			.mouseover(function() {
				$(this).css("background-color", "#D1EEEE");
				$(this).next().css("background-color", "#D1EEEE");
			})
			.mouseout(function() {
				$(this).css("background-color", "#E8F3FA");
				$(this).next().css("background-color", "#E8F3FA");
			});
		$("#tb-tbd tr[name='n']")
			.mouseover(function() {
				$(this).css("background-color", "#D1EEEE");
				$(this).prev().css("background-color", "#D1EEEE");
			})
			.mouseout(function() {
				$(this).css("background-color", "#E8F3FA");
				$(this).prev().css("background-color", "#E8F3FA");
			});
		$('td label')
			.mouseover(function() {
				$(this).css("border-bottom", "1px dashed #A5BFCF");
			})
			.mouseout(function() {
				$(this).css("border-bottom", "0px dashed #A5BFCF");
			});
		
		$('#btn_print').click(function() {
			var flag = true, _mailNo = temp = oids = '', size=0;
			$('#tb-tbd :checkbox:checked').each(function() {
				//flag = true;
				//oids += ',' + $(this).val();
				var _this = this, _next = $(this).parent().next();
				_mailNo = _next.find(':text').val();
				if(!_mailNoRegx.test(_mailNo)) {
					_alert("运单号不对:"+_mailNo, function(){_next.find(':text').select();});
					return flag = false;
				}
				
				temp = $(_this).val() + "#" + _mailNo;
				oids += ',' + temp;
				size++;
			});

			if(!flag) return false;

			if(!size) {
				return _alert("请选择要打印的订单");
			}
			if(!$('#orderExpId').val()) {
				return _alert("请先设置打印模板");
			}
			var url = 'order!toOrderPrint.action?oids=' + encodeURIComponent(oids.substring(1)) + '&orderExpId=' + $('#orderExpId').val();
			window.open(url,'x','width=900,height=978,left=350,top=100,scrollbars=yes');

		});
		
		$('#btn_send').click(function() {
			if(!$('#tb-tbd :checkbox:checked').length) { return _alert("请选择要发货的订单"); }
			
			var flag = true, _mailNo = temp = oids = '';
			$('#tb-tbd :checkbox:checked').each(function() {
				var _this = this, _next = $(this).parent().next();
				_mailNo = _next.find(':text').val();
				if(!_mailNoRegx.test(_mailNo)) {
					_alert("运单号不对:"+_mailNo, function(){_next.find(':text').select();});
					return flag = false;
				}
				if($.trim($(this).parent().parent().children(":last-child").text()) == '未打印') {
					_alert("包含未打印订单:"+_mailNo, function(){_next.find(':text').select();});
					return flag = false;
				}
				
				temp = $(_this).val() + "#" + _mailNo;
				oids += ',' + temp;
			});
			
			if(!flag) return false;
			
			var url = 'order!toSendOrder.action?oids='+encodeURIComponent(oids.substring(1));
			$.post(url, function(data){
				if(data == 'success') {
					_alert('发送成功', function() { $('#Tform').submit() });
				}
				else {	// error
					_alert('发送失败!');// 请检查订单数据是否正确!
				}
			}, 'json');

		});
		
		$('.tab-content tr').live('mouseover', function() {
			$(this).addClass('tr_hover');
		});
		$('.tab-content tr').live('mouseout', function() {
			$(this).removeClass('tr_hover');
		});
	});
	
	function 咪路(_this, tr_id) {
		var _T = $('#'+tr_id);
		$(_this).hasClass('Ticon-expand') ? _T.fadeIn() : _T.fadeOut();
		$(_this).toggleClass('Ticon-expand').toggleClass('Ticon-shrink');
	}
	function openMessage(){
		parent.main.location = "waybill_orderExpress.action";
		parent.side.layerLight("li_orderprint_temp");
	}
</script>
</body>
</html>