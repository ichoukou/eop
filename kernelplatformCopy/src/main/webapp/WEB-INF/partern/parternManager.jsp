<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="taglibs.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"s%>
<link rel="stylesheet" type="text/css" href="/css/jqgrid-3.8.2/ui.jqgrid.css?${_version}" media="all" />
<script type="text/javascript" src="/js/jqgrid-3.8.2/i18n/grid.locale-cn.js?${_version}"></script>
<script type="text/javascript" src="/js/jqgrid-3.8.2/jquery.jqGrid.min.js?${_version}"></script>

<link rel="stylesheet" type="text/css" href="/css/base/reset.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/common/common.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/jquery-ui-1.9.2/blitzer/jquery-ui-1.9.2.custom.min.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/module/button.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/module/box.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/module/table.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/jquery-ui-1.9.2/development-bundle/themes/blitzer/jquery-ui.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/module/dialog.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/module/page.css?" media="all"/>
<link rel="stylesheet" type="text/css" href="/css/page/question.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/jquery-plugins/jquery.tooltip-1.3.css?" media="all" />
<link rel="stylesheet" type="text/css" href="/css/jqgrid-3.8.2/ui.jqgrid.css?" media="all" />

<script type="text/javascript" src="/js/lib/jquery-1.7.min.js?"></script>
<script language="javascript" src="/js/util/position_fixed.js?"></script>
<script language="javascript" src="/js/module/dialog.js?"></script>
<script language="javascript" src="/js/util/DD_belatedPNG.js?"></script>
<script language="javascript" src="/js/module/heartcode-canvasloader-min-0.9.1.js?"></script>
<script language="javascript" src="/js/jquery-ui-1.9.2/jquery-ui-1.9.2.custom.min.js?"></script>
<script type="text/javascript" src="/js/jquery-plugins/jquery.all-plugin.min.js?"></script>
<script type="text/javascript" src="/js/jqgrid-3.8.2/i18n/grid.locale-cn.js?"></script>
<script type="text/javascript" src="/js/jqgrid-3.8.2/jquery.jqGrid.min.js?"></script>
<script type="text/javascript" src="/js/common/jquery.gridUtil.js?"></script>
<script language="javascript" src="/js/lib/jquery.form.js?"></script>
<script language="javascript" src="/js/jquery-plugins/jquery.blockUI-2.33.min.js?"></script>
<html>
<head>
</head>
<body>
<script type="text/javascript">
//弹出层公用部分
function dialogCommon(tab){
	var dialogD = new Dialog();
	dialogD.init({
		closeBtn: true,
		contentHtml: '<p class="pErr">'+tab+'</p>'		// 内容 HTML
	});
}



var showResponse = function(data,status) {   
	alert(data);
	return true;  
}; 

</script>
<script type="text/javascript">
$().ready(function(){
	loadGridTable();
	/* var xx= $("#gridTable").getGridParam("records");
		alert(xx); */
		
	if(re_records>0){
		$("#reset_label_first").style.visibility="hidden";
	}
});

var loadGridTable=function(){
	$("#gridTable").gridUtil().simpleGrid({
        url:"parternManager.htm",
        
        //editurl:delUrl, //设置编辑的URL地址，此处只用于删除
		width: 975,
		caption: "密钥信息记录",
		rowNum: 10,
		rownumWidth: 25,
		toolbar: [true,"top"],
		pager: "#gridPager",
		multiboxonly: true,
		autowidth: false,
		shrinkToFit:false,
		footerrow : true,
	    userDataOnFooter : true,
		gridComplete: function() {
			var ids = $("#gridTable").jqGrid('getDataIDs');
		},
		colNames:['密钥编号','用户编号', '密匙'],
		colModel:[
				{name:'pId',index:'pId', width:100,hidden:true},
				{name:'customerCode',index:'customerCode', width:100},
				{name:'parternId',index:'parternId', width:165}
		],
		loadComplete:function(data){ //完成服务器请求后，回调函数 
	        if(data.records>0){ //如果没有记录返回，追加提示信息，删除按钮不可用
	        	$("#reset_label_first").css("display","none");
	        }else{
	        	alert("请新增一个密钥");
	        }
	    }
	});
	$("#gridToolbar").appendTo($("#t_gridTable"));
	
};


</script>
       <div id="sub_nav">
        	<ul class="clearfix">
				<li id="home_index_menu_item"  menuflag="home_index">
					<a title="订单管理" href="${_ctxPath}/orderList.htm">订单管理</a>
				</li>
				
				<li id="home_index_menu_item" menuflag="home_index">
                	<a title="打印管理" href="${_ctxPath}/printAloneContentView.htm">打印管理</a>
                </li>
                
                <li id="home_index_menu_item" menuflag="home_index">
                	<a title="面单管理" href="${_ctxPath}/waybillInfo.htm">面单管理</a>
                </li>
                <li id="home_index_menu_item" class="cur_sub_menu" menuflag="home_index">
                	<a title="密钥管理" href="${_ctxPath}/searchPraternInfo.htm">密钥管理</a>
                </li>
            </ul>
        </div>
        <div id="content">
             <div id="content_bd">
            	<div>
            		<table id="gridTable"></table>
            		<div id="gridPager"></div>
            		<div id="gridToolbar" style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="reset_label_first"><a href="javascript:void(0)" class="btn btn_a"  id="gridRowAdd" title="新增"><span class="add" >新增</span></a></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		<span class="reset_label_last"><a href="javascript:void(0)" class="btn btn_a" id="gridRowUpdate" title="修改"><span class="add">修改</span></a></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		<span class="reset_label_last"><a href="javascript:synchroKey();"  class="btn btn_a" id="synchroKey" title="同步密钥"><span class="add">同步密钥</span></a></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            		</div>
             	</div>
          	</div>
     </div>
<div id="parternAddDiv"></div>
<div id="parternUpdateDiv"></div>
<script type="text/javascript">


function paddDialog(){
	var dlg ="#parternAddDiv";
	$(dlg).html("");
	$(dlg).dialog( {
		autoOpen: false,
		bgiframe:true,
		modal: true,
		resizable:false,
	    height: 330,
	    width: 600,
		title : "新建密钥",
		close:function(){
			window.location.href = 'searchPraternInfo.htm';
		}
	});
	$(dlg).load("pAdd.htm",null,function(data){return;});
	$(dlg).dialog("open");
};

function pupdateDialog(){
	var dlg ="#parternUpdateDiv";
	$(dlg).html("");
	$(dlg).dialog( {
		autoOpen: false,
		bgiframe:true,
		modal: true,
		resizable:false,
	    height: 330,
	    width: 600,
		title : "修改密钥",
		close:function(){
			window.location.href = 'searchPraternInfo.htm';
		}
	});
	
	$(dlg).load("pUpdate.htm",null,function(data){return;});
	$(dlg).dialog("open");
};

function synchroKey(){
	if(window.confirm("确定手动同步密钥信息？密钥信息默认一天自动更新一次！")){
		
		$.blockUI({           
			message: '<h1>请稍后，正在处理...</h1>',       
			css: {         
				border: 'none',     
				padding: '15px',         
				backgroundColor: '#000',        
				'-webkit-border-radius': '10px',       
				'-moz-border-radius': '10px',         
				opacity: .5,         
				color: '#fff'       
				}
		}); 
		
		$.ajax({
		 	  type: "POST",
		 	  url:_ctxPath+'/synchroKey.htm',
		 	  dataType: "json",
		 	  data:{},
		 	  success: function(result){
		 		  $.blockUI({           
					  timeout: 1
				  }); 
		 		 dialogCommon("同步成功！");
		 	  },
		   	  error: function(err) {
		   		 $.blockUI({           
					  timeout: 1
				  }); 
		   		dialogCommon("同步失败！");
		 		return false;
		   	  }
		});
		
		
		
/* 		$("#searchForm").attr('action',"synchro.htm");
		$("#searchForm").submit();
		alert(""); */
	}
}
//新增  ajax
$("#gridRowAdd").click(function(){
	paddDialog();//新增密钥弹出框 
});

//修改  ajax
$("#gridRowUpdate").click(function(){
	var selectIds = $('#gridTable').jqGrid('getGridParam', 'selarrrow');
	if(selectIds == null || selectIds.length == 0){
		dialogCommon("请选择要修改的密钥");
			return;
		}
	var pId = $('#gridTable').getRowData(selectIds[0])["pId"];
	pupdateDialog();//修改密钥弹出框 
});

</script>

	<table>
		<s:iterator value="zebraParterns" status="z">
			<tr>
				<td>客户编号</td>
				<td>密钥</td>
			</tr>
			<tr>
				<td><s:property value="customerCode"/></td>
				<td><s:property value="parternCode"/></td>
			</tr>
	</s:iterator>
	</table>
	<br/>

	
	<form action="pAdd.action" method="post">
	客户编号：<input type="text" id="zebraParterns.customerCode"/><br/>
	密          钥:<input type="text" id="zebraParterns.parternCode"/>
	<input type="submit" value="新增">
	</form>
	
</body>