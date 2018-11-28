<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<div id="content">
	<style type="text/css">
		#more_news_title{margin-bottom:20px;}
		#more_news_title h2 {font-size:20px;}
		#more_news_list{margin:0 10px;}
		#more_news_list li{margin-bottom:10px;line-height:24px;height:24px;}
		#more_news_list ul{width:500px;}
		#more_news_list li a{float:left;}
		#more_news_list li .news_date{float:right;color:#808080;}
		#back_btn{text-align:center;margin-top:10px;}
	</style>
	<div id="more_news_title">
		<h2>易通最新动态：</h2>
	</div>
	
	<div id="more_news_list">
		<ul>
			<c:forEach items="${articleList }" var="article" varStatus="artSt">
				<li>
					<a href="mainPage_detailArticle.action?article.articleId=${article.articleId }">${artSt.index+1 }. ${str:multiSubStr(article.title,60)}</a>
					<span class="news_date"><fmt:formatDate value="${article.createTime }" pattern="yyyy-MM-dd HH:mm" /></span>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="pagenavi">
		<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
    </div>
	<div id="back_btn">
		<a href="javascript:history.go(-1);" class="btn btn_a" title="返回"><span>返 回</span></a>
	</div>
	
    <script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
    <script type="text/javascript">
	    pagination.click(function(ev){
	    	ev.preventDefault();
	    	var currentPage = $(this).attr("value");
			setTimeout(function(){
				window.location.href='noint!moreNews.action?currentPage='+ currentPage;
			},0);
	    });
    </script>
</div>