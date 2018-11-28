<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
#readerTypeTD input{vertical-align:middle;}
</style>
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script type="text/javascript" src="${jsPath}/ckeditor/ckeditor.js?d=${str:getVersion() }"></script>
<script type="text/javascript">
var editor;
var articleId = ${article.articleId};
function check(){
	var content = editor.getData();
	// console.log("content=="+content);
	var title = $("#title").val();
	if(!title){
		alert("文章标题不能为空！");
		return;
	}
	else if(!content){
		alert("文章内容不能为空！");
		return;
	}
	else{
		//$("#workForm").submit();
		$.ajax({
			type : "post",
			dataType : "json",
			data : "article.articleId="+articleId+"&article.columnId="+$("#column").val()+"&article.status="+$("#status").val()
			+"&article.readerType="+$("#readerTypeTD input:checked").val()+"&article.title="+$("#title").val()
			+"&article.seq="+$("#seq").val()+"&article.content="+encodeURIComponent(content)+"&article.remark="+$("#remark").val(),
			cache: false,
			url : 'updateArticle.action',
			success:function(response){
				// console.log("发送参数："+this.data);
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


	<div id="content" class="clearfix">
		<form id="workForm" name="workForm" action="updateArticle.action" method="post" class="form">
			<input type="hidden" name="menuFlag" value="${menuFlag }" />
			<input type="hidden" name="article.articleId" value="${article.articleId}" />
			<input type="hidden" id="status" name="article.status" value="${article.status}" />
			<p>
				<label for="title">标题：</label>
				<input type="text" id="title" class="input_text" name="article.title" value="${article.title }" />
			</p>
			
			<p>
				<label for="column">栏目：</label>
				<select name="article.columnId" id="column">
					<option>-请选择栏目-</option>
					<c:forEach items="${columns }" var="column" varStatus="status">
						<option value="${column.columnId }" <c:if test="${article.columnId == column.columnId}">selected</c:if> >
							<c:if test="${column.level > 1}">
								<c:forEach begin="1" end="${column.level-1 }">--</c:forEach>>
							</c:if>${column.columnName }
						</option>
					</c:forEach>
				</select>
			</p>
			
			<p>
				<label for="">查看类型：</label>
				<span id="readerTypeTD">
						<input type="radio" name="article.readerType" value="0" ${(article.readerType ==0 || article.readerType == null || article.readerType =='') ? 'checked' : ''} /> 所有用户
						<input type="radio" name="article.readerType" value="1" ${article.readerType == '1' ? 'checked' : ''} /> 卖家
						<input type="radio" name="article.readerType" value="2" ${article.readerType == '2' ? 'checked' : ''} /> 网点
						<input type="radio" name="article.readerType" value="3" ${article.readerType == '3' ? 'checked' : ''} /> 大商家
				</span>
				
			</p>
			
			<p>
				<label for="seq">顺序：</label>
				<input type="text" class="input_text" id="seq" name="article.seq" value="${article.seq }" />
			</p>
			
			<p>
				<label for="editor">内容：</label>
				<textarea name="article.content" id="editor" cols="72" rows="10">${article.content}</textarea>
				<script type="text/javascript">
					editor = CKEDITOR.replace('editor');
				</script>
			</p>
			
			<p>
				<label for="remark">备注：</label>
				<input type="text" class="input_text" id="remark" name="article.remark" value="${article.remark }" />
			</p>
			
			<p>
				<a href="javascript:;" onclick="javascript:check();" class="btn btn_a" title="提交"><span>提 交</span></a>
				<a href="javascript:;" onclick="history.back();" class="btn btn_a" title="返回"><span>返 回</span></a>
			</p>
		</form>
  	</div>

