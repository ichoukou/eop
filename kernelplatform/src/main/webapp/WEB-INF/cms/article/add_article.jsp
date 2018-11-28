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
	var title = $("#title").val();
	if(!title){
		var aDialog = new Dialog();
		aDialog.init({
			contentHtml: "报道标题不能为空！",
			yes: function() {
				aDialog.close();
			}
		});
		return;
	}
	else if(!content){
		var aDialog = new Dialog();
		aDialog.init({
			contentHtml: "报道内容不能为空！",
			yes: function() {
				aDialog.close();
			}
		});
		return;
	}
	else{
		//$("#workForm").submit();
		$.ajax({
			type : "post",
			dataType : "json",
			data : "article.articleId="+articleId+"&article.columnId="+$("#column").val()
			+"&article.readerType="+$("#readerTypeTD input:checked").val()+"&article.title="+$("#title").val()
			+"&article.seq="+$("#seq").val()+"&article.content="+encodeURIComponent(content)+"&article.remark="+$("#remark").val(),
			cache: false,
			url : 'createArticle.action',
			success:function(response){
				//console.log(response);
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
	<form name="workForm" id="workForm" action="createArticle.action" method="post" class="form">
		<input type="hidden" name="menuFlag" value="${menuFlag }" />
						<p>
							<label for="title">标　　题</label>
							<input type="text" size="80" id="title" name="article.title"  class="input_text" />
						</p>
						<p>
							<label for="column">栏　　目</label>
							<select name="article.columnId" id="column" style="font-size:13px;">
								<option>-请选择栏目-</option>
								<c:forEach items="${columns }" var="column" varStatus="status">
									<c:choose>
										<c:when test="${article != null && article.columnId != null && (column.columnId ==article.columnId)}">
											<option value="${column.columnId }" selected="selected">
												<c:if test="${column.level > 1}">
													<c:forEach begin="1" end="${column.level-1 }">--</c:forEach>>
												</c:if>${column.columnName }
											</option>
										</c:when>
										<c:otherwise>
											<option value="${column.columnId }">
												<c:if test="${column.level > 1}">
													<c:forEach begin="1" end="${column.level-1 }">--</c:forEach>>
												</c:if>${column.columnName }
											</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</p>
						<p>
							<label for="readerTypeTD">查看类型</label>
							<span id="readerTypeTD">
								<input type="radio" name="article.readerType" value="0" />所有用户
								<input type="radio" name="article.readerType" value="1" />卖家
								<input type="radio" name="article.readerType" value="2" />网点
								<input type="radio" name="article.readerType" value="3" />大商家
							</span>
						</p>
						<p>
							<label for="seq">顺　　序</label>
							<input type="text" size="3" id="seq" name="article.seq"  class="input_text"/>
						</p>
						<p>
							<label for="editor">内　　容</label>
							<textarea  cols="72" rows="10" name="article.content" id="editor">
							</textarea>
							<script type="text/javascript">
							editor = CKEDITOR.replace('editor');
							</script>
						</p>
						<p>
							<label for="remark">备　　注</label>
							<input type="text" size="80" id="remark" name="article.remark"  class="input_text"/>
						</p>
				  		<p>
					  		<a href="javascript:;" onclick="javascript:check();" class="btn btn_a" title="提交"><span>提 交</span></a>
							<a href="javascript:;" onclick="history.back();" class="btn btn_a" title="返回"><span>返 回</span></a>
				  		</p>
  	
	</form>
</div>