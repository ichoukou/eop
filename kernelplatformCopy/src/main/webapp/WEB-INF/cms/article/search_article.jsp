<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${jsPath}/zTree/css/zTreeStyle.css" />

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }" ></script>
<script type="text/javascript" src="${jsPath}/zTree/jquery.ztree.all-3.1.js?d=${str:getVersion() }"></script>
<!-- 当前页面JS -->
<script type="text/javascript" src="${jsPath}/page/article.js?d=${str:getVersion() }"></script>
<!-- 当前页面JS -->
<style>
.table tbody tr td{vertical-align:top;}
</style>
<script type="text/javascript">
	$(document).ready(function(){
	$(".table-zi tr").mouseover(function(){ //如果鼠标移到class为stripe_tb的表格的tr上时，执行函数
	$(this).addClass("over");}).mouseout(function(){ //给这行添加class值为over，并且当鼠标一出该行时执行函数
	$(this).removeClass("over");}); //移除该行的class
	$(".table-zi tr:even").addClass("alt"); //给class为stripe_tb的表格的偶数行添加class值为alt
	});

	//新增文章
	function addDo(){
		window.location.href="addArticle.action?menuFlag=home_article_list&article.columnId="+$("#checkedColId").val();
	}
	
	//逻辑删除文章
	function del(id){
		var cDialog = new Dialog();
		cDialog.init({
			content: "确定要删除此篇文章吗？",
			yes: function() {
				cDialog.close();
				$.ajax({
					type : "post",
					dataType : "json",
					data : "menuFlag=home_article_list&article.articleId="+id,
					cache: false,
					url : 'removeArticle.action',
					success:function(response){
						var oDialog = new Dialog();
						oDialog.init({
							contentHtml: response.infoContent,
							yes: function() {
								oDialog.close();
								window.location.reload(true);
							}
						});
					}
				});
			},
			no:function(){
				cDialog.close();
			},
			closeBut:true
		});
	}
	
	function edit(id){
		window.location.href="editArticle.action?menuFlag=home_article_list&article.articleId="+id;
	}

	//永久删除文章
	function remove(id){
		var cDialog = new Dialog();
		cDialog.init({
			contentHtml: "确定要永久删除此篇文章吗(删除后将不能恢复)？",
			yes: function() {
				cDialog.close();
				$.ajax({
					type : "post",
					dataType : "json",
					data : {"article.articleId":id,"menuFlag":"home_article_list"},
					cache: false,
					url : 'removeArticle.action',
					success:function(response){
						//console.log(response);
						var oDialog = new Dialog();
						oDialog.init({
							contentHtml: response.infoContent,
							yes: function() {
								oDialog.close();
								window.location.reload(true);
							}
						});
					}
				});
			},
			no:function(){
				cDialog.close();
			},
			closeBut:true
		});
	}
	
	//恢复文章
	function recovery(id){
		//window.location.href="recoveryArticle.action?article.articleId="+id;
		$.ajax({
			type : "post",
			dataType : "json",
			data : "menuFlag=home_article_list&article.articleId="+id,
			cache: false,
			url : 'recoveryArticle.action',
			success:function(response){
				//console.log(response);
				var oDialog = new Dialog();
				oDialog.init({
					contentHtml: response.infoContent,
					yes: function() {
						oDialog.close();
						window.location.reload(true);
					}
				});
			}
		});
	}
	
	$("#addcol").click(function(ev) {
		ev.preventDefault();
		var colId = $("#checkedColId").val();
		window.location.href="addColumn.action?menuFlag=home_article_list&column.parentColumnId="+colId;
	});
	
	//添加栏目
	function addCol(){
		var colId = $("#checkedColId").val();
		window.location.href = "addColumn.action?menuFlag=home_article_list&column.parentColumnId="+colId;
	}
	
	//修改栏目
	function editCol(){
		var colId = $("#checkedColId").val();
		window.location.href = "editColumn.action?menuFlag=home_article_list&column.columnId="+colId;
	}
	
	//删除栏目
	function delCol(){
		var colId = $("#checkedColId").val();
		var cDialog = new Dialog();
		cDialog.init({
			contentHtml: "确定要删除此栏目吗？",
			yes: function() {
				cDialog.close();
				$.ajax({
					type : "post",
					dataType : "json",
					data : "menuFlag=home_article_list&column.columnId="+colId,
					cache: false,
					url : 'removeColumn.action',
					success:function(response){
						//console.log(response);
						var oDialog = new Dialog();
						oDialog.init({
							contentHtml: response.infoContent,
							yes: function() {
								oDialog.close();
								window.location.reload(true);
							}
						});
					}
				});
			},
			no:function(){
				cDialog.close();
			},
			closeBut:true
		});
	}
	
</script>

<!-- S Main -->
<div id="content" class="clearfix table">
	<form id="workForm" name="workForm" action="searchArticle.action" method="post">
		<table width="100%" border="0" align="left" cellpadding="2" cellspacing="0"  bordercolor="#B2C9E0" style="margin-top:3px;">
        	<tr>
	        	<!-- 左侧栏目树 -->
	        	<td width="240px" valign="top">
	        		<table width="240px" border="1" align="center" cellpadding="1" cellspacing="2" bordercolor="#B2C9E0">
	        			<tr>
	        				<td style="font-weight:bold;">栏目管理</td>
	        				<td>
	        					<a href="javascript:void(0)" onclick="addCol();return false;" title="新增栏目" id="addcol"><img width="12px" height="12px" src="${imagesPath}/icons/new.gif" />新增</a>
	        				</td>
	        				<td>
	        					<a href="javascript:void(0)" onclick="editCol();return false;" title="修改栏目" id="editcol"><img width="12px" height="12px" src="${imagesPath}/icons/modify.gif" />修改</a>
	        				</td>
	        				<td>
	        					<a href="javascript:void(0)" onclick="delCol();return false;" title="删除栏目" id="delcol"><img width="12px" height="12px" src="${imagesPath}/icons/delete.gif" />删除</a>
	        				</td>
	        			</tr>
	        			<tr>
	        				<td colspan="4">
	        					<input type="hidden" id="checkedColId" value="${str:isNotEmpty(article.columnId) ? article.columnId : 1}"/>
			        			<div id=treemenu style="width:230px;" align="left">
			        				<ul id="treeDemo" class="ztree"></ul>
			        			</div>
	        				</td>
	        			</tr>
	        		</table>
	        	</td>
	        	<!-- 右侧文章列表 -->
	        	<td valign="top">
	        		<input type="hidden" id="currentColumnId" name="article.columnId" value="${str:isNotEmpty(article.columnId) ? article.columnId : 1}"/>
	        		<input type="hidden" id="currentPage" value="<s:property value='currentPage'/>" name="currentPage"/>
	        		<input type="hidden" name="menuFlag" value="${menuFlag }" />
			        <table width="98%" border="0" cellpadding="0" cellspacing="0" >
			        	<tr>
			        		<td>
						        <table width="100%" align="left" border="0" cellpadding="0" cellspacing="0">
							  		<tr>
								  		<td align="left">
								  			<span class="btn btn_a"><input type="button" value="新增" onclick="javascript:addDo();"/></span>
								  		</td>
								  		<td align="center">
								  			<span class="btn btn_a"><input type="button" value="更新索引" onclick="javascript:updateSearch();"/></span>
								  		</td>
								  		<td align="right">
								  			<span class="btn btn_a"><input type="button" value="返回" onclick="javascript:history.back();"/></span>&nbsp;&nbsp;&nbsp;&nbsp;
								  		</td>
							  		</tr>
						        </table>
			        		</td>
			        	</tr>
			        </table>
				  	<table width="98%" border="0" cellpadding="0" cellspacing="0" bordercolor="#B2C9E0" class="table-zi" style="margin-top:3px;">
					    <thead>
					    <tr>
						   <th width="40px">编 号</th>
						   <th>文章标题</th>
						   <th width="70px">所属栏目</th>
						   <th width="60px">发布时间</th>
						   <th width="60px">状 态</th>
						   <th width="100px">操 作</th>
					    </tr>
					    </thead>
					    <tbody>
					    <c:forEach items="${articles}" var="article" varStatus="st">
						   	<tr class="tr-zi">
						      	<td align="center">${st.index+1+(pageList.currentPage-1)*pageList.pageSize}</td>
						      	<td align="left" style="text-align:left;">${str:multiSubStr(article.title,50)}</td>
						      	<td align="center">
						      		<c:forEach items="${columns }" var="column" varStatus="status">
										<c:if test="${article.columnId == column.columnId}">
											${column.columnName }
										</c:if>
									</c:forEach>
						      	</td>
								<td align="center">
								<fmt:formatDate value="${article.createTime}" pattern="yyyy-MM-dd" />
								</td>
						      	<td align="center">
						      		<c:if test="${article.status == 1}">
						      			未审核
						      		</c:if>
						      		<c:if test="${article.status == 2}">
						      			已审核
						      		</c:if>
						      		<c:if test="${article.status == 3}">
						      			已删除
						      		</c:if>
						      	</td>
						      	<td align="center">
						      		<c:if test="${article.status < 3 }">
							      		<a href="javascript:void(0)" onclick="edit(${article.articleId});return false;">修改</a> 
							      		<a href="javascript:void(0)" onclick="remove(${article.articleId});return false;">删除</a>
						      		</c:if>
						      		<c:if test="${article.status == 3 }">
							      		<a href="javascript:void(0)" onclick="recovery(${article.articleId});return false;">恢复</a> 
							      		<a href="javascript:void(0)" onclick="remove(${article.articleId});return false;">永久删除</a>
						      		</c:if>
								</td>
						   </tr>
						</c:forEach>
					    </tbody>
				  	</table>
			     	<!-- S PageNavi -->
                	<div class="pagenavi">
                		<jsp:include page="/WEB-INF/page.jsp" />
                	</div>
                	<!-- E PageNavi -->
		     	</td>
	     	</tr>
	     	
        </table>
	</form>
</div>	
<script type=text/javascript>
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
	var zNodes = [{id:-1,nocheck:true,open:true,name:'易通电子商务平台',icon:'${imagesPath}/tree/tree_expand_normal.gif'},${colScriptStr}];
	function onCheck(e, treeId, treeNode) {
		$("#checkedColId").val(treeNode.id);
		//console.log(treeNode.rootId);
	}
	
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});
    
</script>	