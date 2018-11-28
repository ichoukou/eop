<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<!-- S CHART -->
<script type="text/javascript" src="${jsPath}/module/highcharts/highcharts.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/highcharts/exporting.js?d=${str:getVersion() }"></script>
<!-- E CHART -->

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/myyto.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/page/myyto2.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath }/page/myyto2.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<script type="text/javascript">
	var params = {
		userType: 3,							// 用户类型
		userState: 'TBA',						// 用户状态，TBA 是未激活，1 是已激活
		userField003: 9,						// 用户状态
		childType: '',							// 区分卖家和业务账号（卖 === '', 业 !== ''）
		//loginId: 'login id test',				// 登录账号
		infoFormAction: '?action=info',			// 填写信息表单 action
		bindFormAction: '?action=bind'			// 绑定客户编码表单 action
	};

// 	function showJfreeChart(val){
// 		$("#chart").attr("src","viewPie.action?someDay="+val);
// 	}
</script>

<!-- S Main -->
<div id="content">
	<div class="content_box clearfix">
		<div class="content_l">
			<div id="start_navi">
				精彩，从现在开始！
			</div>
		</div>			
	</div>
	
	<div id="content_box_a" class="content_box clearfix">
		<div class="content_l">
			<!-- S 易通推荐 -->
			<div class="box box_c">
				<div class="box_hd">
					<strong>易通推荐</strong>
				</div>
				<div class="box_bd">
					<!-- S 轮播图 -->
					<div id="rec_slide">
						<div id="rec_panel">
							<ul>
								<li><img src="${imagesPath}/single/rec_img4.jpg" alt="" /></li>
								<li  style="display:none;"><img src="${imagesPath}/single/rec_img1.jpg" alt="" /></li>
								<li style="display:none;"><img src="${imagesPath}/single/rec_img3.jpg" alt="" /></li>
								<li style="display:none;"><img src="${imagesPath}/single/banner6.jpg" alt="" /></li>
							</ul>
						</div>
						<div id="rec_trigger">
							<ul class="clearfix">
								<li class="cur_trigger"><a href="javascript:;" title="时效提醒">时效提醒</a></li>
								<li><a href="javascript:;" title="问题件管理">问题件管理</a></li>
								<li><a href="javascript:;" title="智能查件">智能查件</a></li>
								<li><a href="javascript:;" title="快速发货">快速发货</a></li>
							</ul>
						</div>
					</div>
					<!-- E 轮播图 -->
				</div>
			</div>
			<!-- E 易通推荐 -->
		</div>
		
		<div class="content_r">
			<!-- S 易通最新消息 -->
			<div class="box box_c">
				<div class="box_hd">
					<strong>易通最新消息</strong>
					<a href="noint!moreNews.action" class="box_more">更多</a>
				</div>
				<div class="box_bd">
					<ol id="yto_news">
						<c:forEach items="${newsList }" var="article" varStatus="artSt">
							<li>
								<a href="detailArticle.action?article.articleId=${article.articleId }" title="${article.title }">${artSt.index+1 }. ${str:multiSubStr(article.title,20)}</a>
								<span class="news_date"><fmt:formatDate value="${article.createTime }" pattern="yyyy-MM-dd" /></span>
							</li>
						</c:forEach>
					</ol>
				</div>
			</div>
			<!-- E 易通最新消息 -->
		</div>
	</div>
</div>
<!-- E Main -->
	

