<%--
	@author ChenRen
	@date 	2011-07-26
	@description
		用户新增的页面
		
	流程：
		用户登录填写必填信息提交
		提交前在页面进行字段验证
		成功跳转到列表页
		失败在本页，并提示错误信息
		
	@2011-08-19 By ChenRen
		
--%>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<!-- base href="<%=basePath%>"-->
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户新增</title>
	<style type="text/css">
		/*form label {display: inline;}*/
	</style>
	<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
	<link href="css/validationEngine.jquery.css?d=${str:getVersion() }" rel="stylesheet" type="text/css" />
	<link href="css/template.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="css/jquery-easyui/easyui.css?d=${str:getVersion() }">
	<link rel="stylesheet" type="text/css" href="css/jquery-easyui/icon.css?d=${str:getVersion() }">
	<link rel="stylesheet" type="text/css" href="css/jquery-easyui/demo.css?d=${str:getVersion() }">
	<link rel="stylesheet" type="text/css" href="css/layoutshow.css?d=${str:getVersion() }">
	<link href="artDialog/skins/blue.css" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
</head>

<body>
	<div id="main-content">
		<h2>新增VIP</h2>
		<br />
		<div class="content-box">
        	<div class="tab-content default-tab" style="display: block;">
        		<form id="userFrom" action="user!add.action" method="post">
        		  <div><!--  class="tab-content default-tab" style="display: block;" -->
        			<table>
        				<tr>
        					<td>
	        					<label for="name" style="display:inline;">用户账号*：</label>
								<input id="name"  name="user.userName" class="validate[required,custom[onlyLetter],ajax[ajaxName]]"  />
								
	        					<label for="userCode" style="display:inline;">用户编码*：</label>
								<input id="userCode"  name="user.userCode" class="validate[required,ajax[ajaxUserCode]]"  />
        					</td>
        					<td>
        						<label for="nameText" style="display:inline;">用户姓名*：</label>
								<input id="nameText"  name="user.userNameText" class="validate[required]"  />
        					</td>
        				</tr>
        				<tr>
        					<td>
        						<label for="pwd" style="display:inline;">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码*：</label>
								<input id="pwd" type="password" name="user.userPassword" class="validate[required,length[6,20]]"  />
							</td>
        					<td>
	        					<label for="pwd2" style="display:inline;">确认密码*：</label>
								<input id="pwd2" type="password" name="userPassword2" class="validate[required,confirm[pwd]]"  />
							</td>
        				</tr>
        				<tr>
        					<td>
								<label for="tel" style="display:inline;">固定电话*：</label>
								<!-- class="validate[custom[telephone]]"  -->
								<input id="tel"  name="user.telePhone" />
							</td>
        					<td>
								<label for="mp" style="display:inline;">手机号码*：</label>
								<!-- class="validate[custom[mobilenumber]]" -->
								<input id="mp"  name="user.mobilePhone"/>
							</td>
        				</tr>
        				<tr>
        					<td colspan="4">
								<label for="tel" style="display:inline;">发货地址*：</label>
								<jsp:include page="/region.jsp"></jsp:include>
								<div id="div_fullAdd" style="display: inline; background: #B2C9E0; margin-left: 10px;" title="这里是您的完整地址..."></div>
								
								<%--
								隐藏域.
								地区的显示值通过隐藏域传到后台. id是region.jsp里地区select里面关联的id
								--%>
								<s:hidden name="user.addressProvince" id="x_p"></s:hidden>
								<s:hidden name="user.addressCity" id="x_c"></s:hidden>
								<s:hidden name="user.addressDistrict" id="x_d"></s:hidden>
								<s:hidden name="user.addressStreet" id="x_s"></s:hidden>
							</td>
        				</tr>
        				<tr>
        					<td colspan="4" style="padding: 0; margin: 0; height: 12px;">
        						<a href="javascript:;" onclick="swapMore()" style="font-size: 12px; margin-left: 10px;">[更多详细信息]</a>
        					</td>
        				</tr>
        			</table>
       				<div id="tab_moreInfo" style="display: none;">
       					<table>
	       					<tr>
	        					<td>
									<label for="x" style="display:inline;">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</label>
									<select id="x" name="user.sex">
										<option value="N">&nbsp;-&nbsp;</option>
										<option value="M">男</option>
										<option value="F">女</option>
									</select>
								</td>
	        					<td>
									<label for="yaya" style="display:inline;">&nbsp;&nbsp;&nbsp;&nbsp;E-Mail：</label>
									<input id="yaya"  name="user.mail"/><!-- class="validate[custom[email]]" -->
								</td>
	        				</tr>
	       					<tr>
	        					<td>
									<label for="d" style="display:inline;">店铺名称：</label>
									<input id="d"  name="user.shopName" />
								</td>
	        					<td>
									<label for="d_d" style="display:inline;">店铺账号：</label>
									<input id="d_d"  name="user.shopAccount" />
								</td>
	        				</tr>
	       					<tr>
	        					<td>
									<label for="tel" style="display:inline;">证件类型：</label>
									<select id="x" name="user.cardType">
										<option value="-1">&nbsp;&nbsp;-&nbsp;&nbsp;</option>
										<option value="1">身份证</option>
										<option value="2">其他</option>
									</select>
								</td>
	        					<td>
									<label for="s" style="display:inline;">证件号码：</label>
									<input id="s"  name="user.cardNo" />
								</td>
	        				</tr>
	        				<tr>
	        					<td colspan="4">
	        						备注：
	        						<textarea name="user.remark" rows="2" cols="50"></textarea>
	        					</td>
	        				</tr>
        				</table>
       				</div>
       				<div align="center">
	  					<input type="submit" value="保存" class="button" >
	   					<input type="reset" value="清空" class="button" style="margin: 0 20px 0 20px;">
	   					<input type="button" value="返回" class="button" onclick="javascript: history.go(-1);">
       				</div>
        		  </div>
				</form>
        	</div>
        </div>
   	</div>

	<script src="js/jquery-1.4.2.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script src="js/jquery.validationEngine-cn.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script src="js/jquery.validationEngine.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script type="text/javascript" src="js/jquery-easyui/jquery.easyui.min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/jquery-easyui/easyui-lang-zh_CN.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/simpla.jquery.configuration.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/facebox.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="js/jquery.wysiwyg.js?d=${str:getVersion() }"></script>
	<script type="text/javascript">
		// 初始化验证插件
		$(function() {
			$("#userFrom").validationEngine();
			
			// 将用户输入的省、市、县/区、街道地址 拼接完整显示在div里
			var addressIds = ["province", "city", "country", "address"], idsL = addressIds.length;
			// 将input的值显示到div里
			var addrVal2Div = function () {
				$("#div_fullAdd").text(function() {
					var _t = "", _j, _ii;	// _t(text缩写), div的显示值
					for(_j = 0; _j < idsL; _j++) {
						_ii = $("#"+addressIds[_j]).attr("x");	// 当前select关联的隐藏域的id
						_t += "," + $("#"+_ii).val();	// div的显示值是取的隐藏域的值
					} // for j
					return "您的地址是："+(_t.length > 1 ? _t.substring(1) : _t);
				});
			};
			
			for(var i = 0; i < idsL; i++) {
				// 给省、市、县/区、街道地址 4个标签绑定 onchang事件
				$("#"+addressIds[i]).change(function() {
					
					// 改变隐藏域的值
					// _v当前对象的值；_i当前对象关联的input对象
					var _v = $(this).val(), _i = $("#"+$(this).attr("x")); 
					if ($(this)[0].tagName == "SELECT" && _v != "") {
						_i.val($(this).find("option:selected").text());
					}
					else {
						_i.val(_v);
					}
					
					// 在select触发onchange事件的时候改变div的值
					addrVal2Div();
				}); // change event
				// 页面加载完成的时候将隐藏域的值读出来显示在div里
				//addrVal2Div();
			} // for i
			
			// 提交前进行地区非空验证
			$("#userFrom").submit(function() {
				var regT = /^\d{3,4}-\d{4}|\d{7,8}-\d{4}|\d{7,8}$/, regM = /^1[358]\d{9}$/;
				var tv = $.trim($("#tel").val()), mv = $.trim($("#mp").val());
				if(tv == "" && mv == "") {
					art.dialog.alert("固定电话和手机号码必须填写一个!");
					return false;
				}
				if(tv != "") {
					if(!regT.test(tv) ) {
						art.dialog.alert("固定电话格式不对!");
						return false;
					}
				}
				if(mv != "") {
					if(!regM.test(mv) ) {
						art.dialog.alert("手机号码格式不对!");
						return false;
					}
				}
				
				for(var x = 0; x < idsL; x++) {
					var x_id = $("#"+addressIds[x]).attr("x"),	// 当前select关联的隐藏域的id
						x_v  = $.trim($("#"+x_id).val());
					if(x_v == "") {
						art.dialog.alert("地区为必填项! 请选择完整的地址!");
						return false;
					}
				} // for x
				return true;
			});
			
			swapMore = function() {
				$("#tab_moreInfo").slideToggle("norme");
			};
		});
	</script>
</body>
</html>
<%--

<!-- 
<p>
	<label for="name" style="display:inline;">用户账号*：</label>
	<input id="name"  name="user.userName" class="validate[required,custom[onlyLetter],ajax[ajaxName]]"  />
</p>
<p>
	<label for="nameText" style="display:inline;">用户名称*：</label>
	<input id="nameText"  name="user.userNameText" class="validate[required]"  />
</p>
<p>
	<label for="pwd" style="display:inline;">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码*：</label>
	<input id="pwd"  name="user.userPassword" class="validate[required,length[6,20]]"  />
</p>
<p>
	<label for="pwd2" style="display:inline;">确认密码*：</label>
	<input id="pwd2"  name="userPassword2" class="validate[required,confirm[pwd]]"  />
</p>
<p>
	<label for="tel" style="display:inline;">固定电话*：</label>
	<input id="tel"  name="user.telePhone" class="validate[required,custom[telephone]]"  />
</p>
<p>
	<label for="mp" style="display:inline;">手机号码*：</label>
	<input id="mp"  name="user.mobilePhone" class="validate[required,custom[mobilenumber]]"  />
</p>

<p>
	<label for="" >发货地址：</label>
	<select name="dropdown">
		<option value="option1">湖北</option>
	</select>
	省
	<select name="dropdown">
		<option value="option1">武汉</option>
	</select>
	市
	<select name="dropdown">
	   <option value="option1">江岸</option>
	</select>
	市
	<input onblur="" id="" tabindex="" onchange=";" maxlength="12" size="24" name="" />
	 路 
</p>
<p>
	<label for="rm" style="display:inline;">备注：</label>
	<textarea id="rm" rows="3" cols="80" name="user.remark"></textarea>
</p>
<input class="button" type="submit" value="保 存" />
 -->


<s:form id="userFrom" action="user!add.action" method="post">
	<s:textfield name="user.userName" cssClass="validate[required,custom[onlyLetter],ajax[ajaxName]]" label="用户账号*" value="zhangsan"/>
	<s:textfield name="user.userNameText" cssClass="validate[required]" label="用户名称*" value="张三"/>
	<s:password name="user.userPassword" id="pwd" cssClass="validate[required,length[6,20]]" label="密码*" value="zhangsan"/>
	<s:password name="userPassword2" cssClass="validate[required,confirm[pwd]]" label="确认密码*" value="zhangsan"/>
	<s:textfield name="user.telePhone" cssClass="validate[required,custom[telephone]]" label="固定电话*" value="010101010"/>
	<s:textfield name="user.mobilePhone" cssClass="validate[required,custom[mobilenumber]]" label="手机号码*" value="18812346789"/>
	<s:hidden name="user.addressProvince" id="x_p"></s:hidden>
	<s:hidden name="user.addressCity" id="x_c"></s:hidden>
	<s:hidden name="user.addressDistrict" id="x_d"></s:hidden>
	<s:hidden name="user.addressStreet" id="x_s"></s:hidden>
	<div id="div_fullAdd" style="border:1px dotted #999999; background: #ECF0F2; width: 300px;" title="这里是你的完整地址..."></div>
   	
	<%--
	<s:textfield name="user.addressProvince" cssClass="validate[required]" label="省*" value="上海市"/>
	<s:textfield name="user.addressCity" cssClass="validate[required]" label="市*" value="上海市"/>
	<s:textfield name="user.addressDistrict" cssClass="validate[required]" label="区/县*" value="清浦区"/>
	<s:textfield name="user.addressStreet" label="详细地址*" value="华新镇YTO总部!"/>
	-->%
	<s:textarea name="user.remark" label="备注" />
	<s:submit value="提交"></s:submit>
</s:form>
--%>