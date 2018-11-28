<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../include/taglibs.jsp"%>
<html>
<head>
<style type="text/css">
body {
	text-align: center;
}

.graph {
	width: 450px;
	border: 1px solid #F8B3D0;
	height: 25px;
}

#bar {
	display: block;
	background: #FFE7F4;
	float: left;
	height: 100%;
	text-align: center;
}

#barNum {
	position: absolute;
}
</style>

</head>
<body>
	<br />
	<!--<s:property value="importActionUrl"/>-->
	<div id="block"></div>
	<s:form id="importCommon" name="importCommon" method="post">
		<table border="0" align="center">
			<tr>
				<td>密钥编号:</td>
				<td><input type="text" id="parternId" name="parternId">
				</td>
			</tr>
			<tr>
				<td>
					<div id="fileMsg" style="font-color: red">
						<span class="reset_label_last"><a href="javascript:void(0)"
							class="btn btn_a" id="parternAdd" title="新增"><span	 class="add">新增</span>
						</a>
						</span>
					</div></td>
			</tr>
		</table>

	</s:form>
<script type="text/javascript">

/**
 * 单击 新建密钥按钮 
 */
$("#parternAdd").click(function(){
	var customerCode=$("#customerCode").val();
	var parternId=$("#parternId").val();
	$.ajax({
	 	  type: "POST",
	 	  url:_ctxPath+'/parternAdd.htm',
	 	  dataType: "json",
	 	  data:{"partern.customerCode":customerCode,"partern.parternId":parternId},
	 	  success: function(result){
	 		 alert("添加密钥成功!");
	 		window.location.href = 'searchPraternInfo.htm';
	 	  },
	   	  error: function(err) {
	   		dialogCommon("新增密钥异常");
	 		return false;
	   	  }
	});
});
document.onkeydown = function(e){
    if(!e) e = window.event;//火狐中是 window.event
    if(e.keyCode == 13){
    	var customerCode=$("#customerCode").val();
    	var parternId=$("#parternId").val();
    	$.ajax({
    	 	  type: "POST",
    	 	  url:_ctxPath+'/parternAdd.htm',
    	 	  dataType: "json",
    	 	  data:{"partern.customerCode":customerCode,"partern.parternId":parternId},
    	 	  success: function(result){
    	 		alert("添加密钥成功!");
    	 		window.location.href = 'searchPraternInfo.htm';
    	 	  },
    	   	  error: function(err) {
    	   		dialogCommon("新增密钥异常");
    	 		return false;
    	   	  }
    	});
    	return false;
    }
 } 
</script>
</body>
</html>