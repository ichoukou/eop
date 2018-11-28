<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<link href="${jsPath}/zTree/css/zTreeStyle.css?d=${str:getVersion() }" type="text/css" rel="stylesheet"/>
<script src="${jsPath}/zTree/jquery.ztree.all-3.1.js?d=${str:getVersion() }"></script>

<div id="content" class="clearfix">
<form name="workForm" action="createColumn.action" method="post" class="form">
	<div align="center">
	  	<table width="100%" border="0" align="left" cellpadding="2" cellspacing="2">
        	<tr>
        		<td valign="top" align="left" width="250px">
        			<span style="padding-left:10px;font-weight:bold;">栏目层级关系编辑</span>
        			<div id=treemenu style="width:230px;" align="left">
        				<ul id="treeDemo" class="ztree"></ul>
        			</div>
        		</td>
        		<td valign="top" >
				  	<table align="left" border="0" cellpadding="6" cellspacing="6">
						<input type="hidden" name="column.parentColumnId" id="parentColumnId" value="${column != null ? column.columnId : 1}"/>
						<input type="hidden" name="column.rootId" id="rootId" value="${column != null ? column.rootId : 0}"/>
						<input type="hidden" name="column.level" id="level" value="${column != null ? column.level+1 : 1}"/>
				  		<tr>
				  			<td>栏目名称</td>
				  			<td>
				  				<input type="text" class="input_text" name="column.columnName" id="columnName" value="" style="font-size:13px;padding:3px 3px 3px 3px;"/>
				  			</td>
				  			<td>
				  				<div class="onShow" id="columnNameTip" style="width: 300px;"></div>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td>栏目代码</td>
				  			<td>
				  				<input type="text" class="input_text" name="column.columnCode" id="columnCode" value="" style="font-size:13px;padding:3px 3px 3px 3px;"/>
				  			</td>
				  			<td>
				  				<div class="onShow" id="columnCodeTip" style="width: 300px;"></div>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td>顺　　序</td>
				  			<td>
				  				<input type="text" class="input_text" name="column.seq" id="seq" value="" style="font-size:13px;padding:3px 3px 3px 3px;"/>
				  			</td>
				  			<td>
				  				<div class="onShow" id="seqTip" style="width: 300px;"></div>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td>备　　注</td>
				  			<td>
				  				<input type="text" class="input_text" name="column.remark" id="remark" value="" style="font-size:13px;padding:3px 3px 3px 3px;"/>
				  			</td>
				  			<td>&nbsp;</td>
				  		</tr>
				  		<tr>
					  		<td align="center" colspan="3">
						  		<span class="btn btn_a"><input type="button" value="提交" onclick="doSubmit();"/></span>&nbsp;&nbsp;&nbsp;&nbsp;
						  		<span class="btn btn_a"><input type="button" value="返回" onclick="history.back();"/></span>
					  		</td>
				  		</tr>
				  	</table>
        		</td>
        		
        	</tr>
        </table>
  	</div>
</form>
</div>
<SCRIPT type=text/javascript>
	var IDMark_A = "_a";
	var setting = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onCheck: onCheck
		}
	};
	var zNodes = [{id:-1,nocheck:true,open:true,name:'易通电子商务平台',icon:'${imagesPath}/tree/tree_expand_normal.gif'},${columnScpritStr}];
	function onCheck(e, treeId, treeNode) {
		$("#parentColumnId").val(treeNode.id);
		//console.log(treeNode.rootId);
	}
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});
    
</SCRIPT>
<script type="text/javascript">
$.formValidator.initConfig({formId:"workForm",debug:false,submitOnce:true,
	onError:function(msg,obj,errorlist){
	}
});

$("#columnName").formValidator({empty:false,onShow:"请输入栏目名称",onFocus:"最多输入20个字符",onCorrect:" "})
.inputValidator({min:1,max:20,empty:{leftempty:false,rightempty:false,emptyerror:"栏目名称不能有空符号"},onError:"栏目名称输入不正确,请确认"});

$("#columnCode").formValidator({empty:false,onShow:"请输入栏目代码",onFocus:"请输入栏目代码",onCorrect:" "})
.inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"栏目代码不能有空符号"},onError:"栏目代码输入不正确,请确认"})
.ajaxValidator({
	dataType : "json",
	type : "post",
	async : true,
	url : "checkColCodeColumn.action",
	success : function(res){
		return res.status;
	},
	onError : " ",
	onWait : "正在对栏目代码进行校验，请稍候..."
});

$("#seq").formValidator({onShow:"请填写栏目序号",onFocus:"请填写栏目序号",onCorrect:" "})
.inputValidator({min:0,max:10,empty:true,onError:"顺序号输入不符合规则"})
.regexValidator({regExp:"^[0-9]{0,4}$",onError:"顺序号只能输入数字，最大长度4位"});

function doSubmit(){
	if($.formValidator.pageIsValid('1')){
		$.ajax({
			type : "post",
			dataType : "json",
			data : "column.columnId=${column.columnId}"
			+"&column.parentColumnId="+$("#parentColumnId").val()
			+"&column.rootId="+$("#rootId").val()
			+"&column.level="+$("#level").val()
			+"&column.columnCode="+$("#columnCode").val()
			+"&column.seq="+$("#seq").val()
			+"&column.remark="+$("#remark").val()
			+"&column.columnName="+$("#columnName").val(),
			url : 'createColumn.action',
			cache: false,
			success:function(response){
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: response.infoContent,
					yes: function() {
						aDialog.close();
						if(response.status)
				    		location.href=response.targetUrl;
					}
				});
			}
		});
	}
}
</script>	
</html>