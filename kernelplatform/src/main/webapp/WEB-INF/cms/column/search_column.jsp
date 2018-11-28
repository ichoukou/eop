<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>研究性学习系统</title>
<%@ include file="/WEB-INF/pages/pub/meta.jsp"%>
<script type="text/javascript">
	
	$(document).ready(function(){
	$(".table-zi tr").mouseover(function(){ //如果鼠标移到class为stripe_tb的表格的tr上时，执行函数
	$(this).addClass("over");}).mouseout(function(){ //给这行添加class值为over，并且当鼠标一出该行时执行函数
	$(this).removeClass("over");}); //移除该行的class
	$(".table-zi tr:even").addClass("alt"); //给class为stripe_tb的表格的偶数行添加class值为alt
	});

	function addDo(parentColumnId){
		window.location.href="addColumn.htm?menuFlag=home_article_list&column.parentColumnId="+parentColumnId;
	}
	function del(id){
		if(confirm("确定要删除此篇文章吗？")){
			window.location.href="deleteColumn.htm?menuFlag=home_article_list&column.columnId="+id;
		}
	}
	
	function edit(id){
		window.location.href="editColumn.htm?menuFlag=home_article_list&column.columnId="+id;
	}

	function remove(id){
		if(confirm("确定要永久删除此篇文章吗(删除后将不能恢复)？")){
			window.location.href="removeColumn.htm?menuFlag=home_article_list&column.columnId="+id;
		}
	}
	
	function recovery(id){
		window.location.href="recoveryColumn.htm?menuFlag=home_article_list&column.columnId="+id;
	}
	
	function doBack(level){
		window.location.href="searchColumn.htm?menuFlag=home_article_list&column.level="+level;
	}
</script>
</head>
<body>
<form name="workForm" action="searchColumn.htm?menuFlag=home_article_list" method="post">
	<div align="center">
		<table width="100%" height="31" border="0" align="center" cellpadding="0" cellspacing="0">
             <tr>
               	<td width="5%" height="31" background="${imagesPath}/main/887_r2_c3.gif">
               		<div align="center">
               			<img src="${imagesPath}/main/file.gif" width="15" height="18"/>
               		</div>
				</td>
               	<td width="95%" height="31" background="${imagesPath}/main/887_r2_c3.gif" class="font-blue">当前位置：栏目管理</td>
             </tr>
        </table>
        <table width="98%" border="1" cellpadding="4" cellspacing="2" bordercolor="#B2C9E0" style="margin-top:3px;">
        	<tr>
        		<td>
			        <table width="100%" align="left" border="0" cellpadding="4" cellspacing="2">
				  		<tr>
					  		<td align="left">
					  			<input type="button" value="新增" onclick="javascript:addDo(${column != null ? column.parentColumnId : 0});"/>
					  		</td>
					  		<td align="right">
<!-- 					  			<input type="button" value="生成栏目树" onclick="javascript:generateTree();"/>&nbsp;&nbsp;&nbsp;&nbsp; -->
					  			<c:if test="${column.parentColumnId > 0}">
					  			<input type="button" value="返回" onclick="javascript:doBack(${column.level-1});"/>&nbsp;&nbsp;&nbsp;&nbsp;
					  			</c:if>
					  		</td>
				  		</tr>
			        </table>
        		</td>
        		<td></td>
        	</tr>
        </table>
	  	<table width="98%" border="0" cellpadding="4" cellspacing="2" bordercolor="#B2C9E0" class="table-zi" style="margin-top:3px;">
		    <tr>
			   <th>编号</th>
			   <th>栏目名称</th>
			   <th>栏目代码</th>
			   <th>顺序</th>
			   <th>栏目状态</th>
			   <th>备注</th>
			   <th>操作</th>
		    </tr>
		    <c:forEach items="${columns}" var="column" varStatus="st">
			   	<tr class="tr-zi">
			      	<td align="center">${st.index+1+(pageList.currentPage-1)*pageList.pageSize}</td>
			      	<td align="center"><a href="searchColumn.htm?menuFlag=home_article_list&column.parentColumnId=${column.columnId}">${column.columnName}</a></td>
			      	<td align="center">${column.columnCode}</td>
			      	<td align="center">${column.seq}</td>
					<td align="center">
						<c:if test="${column.status == 1}">
							未审核
						</c:if>
						<c:if test="${column.status == 2}">
							已审核
						</c:if>
						<c:if test="${column.status == 3}">
							已删除
						</c:if>
					</td>
					<td align="center">${column.remark}</td>
			      	<td align="center">
						<c:if test="${column.status < 3 }">
							<a href="javascript:edit(${column.columnId});">修改</a> 
							<a href="javascript:del(${column.columnId});">删除</a>
						</c:if>
						<c:if test="${column.status == 3 }">
							<a href="javascript:recovery(${column.columnId});">恢复</a> 
							<a href="javascript:remove(${column.columnId});">永久删除</a>
						</c:if>
					</td>
			   </tr>
			</c:forEach>
				   
	  	</table>
     	<jsp:include page="/WEB-INF/pages/pub/pagging.jsp"/>
  	</div>
</form>
	
</body>
</html>