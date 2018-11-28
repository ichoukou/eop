<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>

<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/myyto.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="css/page/myyto2.css?d=${str:getVersion() }" media="all" />
	
<script type="text/javascript">
	var params = {
		chartAction:"viewTendency.action?timeLimit=",
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},	// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		site:"${yto:getCookie('site')}",
		userType:${yto:getCookie('userType')},
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		getCodeUrl: "user!generateUserCode.action",                // 生成客户编码 url
		delCodeUrl: "user!delUserCode.action",                // 删除客户编码 url
		unbindClientUrl: "user!list.action?type=0&menuFlag=my_custom&userState=TBA"       // 未绑定页面 url
	};
</script>
<!-- S CHART -->
<script type="text/javascript" src="${jsPath}/module/highcharts/highcharts.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/highcharts/exporting.js?d=${str:getVersion() }"></script>
<!-- E CHART -->

<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script type="text/javascript" src="${jsPath}/module/clipboard/zeroClipboard-1.0.7.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/myyto.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/myyto2.js?d=${str:getVersion() }"></script>

<!-- S Main -->
		<div id="content">
			<div class="content_box clearfix">
				<div class="content_l">
					<div id="start_navi">
						精彩，从现在开始
					</div>
				</div>
				 <c:if test="${str:isEmpty(yto:getCookie('parentId')) }">
					<div class="content_r">
						<a href="javascript:;" title="创建客户编码" id="create_cus_code">创建客户编码</a>
					</div>
				 </c:if>
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
										<li><a href="http://www.yto.net.cn/cn/product/Paymentcollection.html" target="_blank" ><img src="${imagesPath}/single/rec_img5.jpg" alt="" /></a></li>
										<li style="display:none;">
										<c:choose>
											<c:when test="${yto:getCookie('userType') == 2 
										   		|| yto:getCookie('userType') == 21
										   		|| yto:getCookie('userType') == 23 }">
										<a href="passManage_list_site.action?flag=0&menuFlag=chajian_passManage_list"><img src="${imagesPath}/single/rec_img4.jpg" alt="" /></a>
										</c:when>
										<c:otherwise>
										<img src="${imagesPath}/single/rec_img4.jpg" alt="" />
										</c:otherwise>
										</c:choose>
										</li>
										<li style="display:none;">
										<c:choose>
											<c:when test="${yto:getCookie('userType') == 2 
										   		|| yto:getCookie('userType') == 21
										   		|| yto:getCookie('userType') == 23 }">
										<a href="questionnaire_index.action?menuFlag=chajian_question"><img src="${imagesPath}/single/rec_img1.jpg" alt="" /></a>
											</c:when>
											<c:otherwise>
										<img src="${imagesPath}/single/rec_img1.jpg" alt="" />
											</c:otherwise>
										</c:choose>
										</li>
										<li style="display:none;">
										<c:choose>
											<c:when test="${yto:getCookie('userType') == 2 
										   		|| yto:getCookie('userType') == 21
										   		|| yto:getCookie('userType') == 23 }">
										<a href="waybill_bill.action?menuFlag=chajian_waybill"><img src="${imagesPath}/single/rec_img3.jpg" alt="" /></a>
											</c:when>
											<c:otherwise>
										<img src="${imagesPath}/single/rec_img3.jpg" alt="" />
											</c:otherwise>
										</c:choose>
										</li>
										<li style="display:none;"><a href="http://www.yto.net.cn/cn/product/Paymentcollection.html" target="_blank" ><img src="${imagesPath}/single/rec_img5.jpg" alt="" /></a></li>
									</ul>
								</div>
								<div id="rec_trigger">
									<ul class="clearfix">
										<li class="cur_trigger"><a href="javascript:;" title="代收货款">代收货款</a></li>
										<li><a href="javascript:;" title="时效提醒">时效提醒</a></li>
										<li><a href="javascript:;" title="问题件管理">问题件管理</a></li>
										<li><a href="javascript:;" title="智能查件">智能查件</a></li>
									</ul>
								</div>
							</div>
							<!-- E 轮播图 -->
						</div>
					</div>
					<!-- E 易通推荐 -->
				</div>
				
				<div class="content_r">
					<!-- S 快捷入口 -->
					<div class="box box_c">
						<div class="box_hd">
							<strong>快捷入口</strong>
						</div>
						<div class="box_bd">
							<div id="num_show">
								<ul class="clearfix">
									<li id="unReadMessageNum_li"><a href="javascript:;" title="消息">新消息（${unReadMessageNum }）</a></li>
									<c:if test="${yto:getCookie('userType') != 22}">
									<li id="unReadQuestionNum_li"><a href="javascript:;" title="问题件管理">新问题件（${unReadQuestionNum }）</a></li>
									</c:if>
									<c:if test="${yto:getCookie('userType') == 2}">
									<li><a href="user!toSubAccountList.action?menuFlag=user_sub_acc_list" title="子帐号">子账号（${userNum }）</a></li>
									</c:if>
									<c:if test="${yto:getCookie('userType') == 2}">
										<li><a href="user!list.action?type=0&menuFlag=user_my_custom" title="我的客户">我的客户（${myCustomNum }）</a></li>
									</c:if>
									<c:if test="${yto:getCookie('userType') == 21 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
										<li><font style="font:700 14px/2 SimSun;color:#333333">我的客户（${myCustomNum }）</font></li>
									</c:if>
								</ul>
							</div>
							
							<div id="home_sear" style="height:45px;">
								<form action="" id="sear_form">
									<input type="hidden" name="currentPage" value="1" />
									<input type="hidden" name="isCheck" value="0" />
									<input type="hidden" name="menuFlag" value="home_waybill" /> 
									<input type="text" name="logisticsIds" id="home_sear_input" class="input_text" /><a href="javascript:;" title="查询" id="home_sear_btn">查 询</a>
									<span id="home_sear_inputTip"></span>
								</form>
							</div>
							
							<div id="to_fun">
								<div id="p_fun_2" class="p_fun">
									<p>解决卖家催件问题，提升物流服务体验</p>
									<c:choose>
									<c:when test="${yto:getCookie('userType') != 22}">
									<a href="passManage_list_site.action?flag=0&menuFlag=chajian_passManage_list">助您提升业务量！</a>
									</c:when>
									<c:otherwise>
									助您提升业务量！
									</c:otherwise>
									</c:choose>
								</div>
								
							</div>
						</div>
					</div>
					<!-- E 我的易通 -->
				</div>
			</div>
			
			<div id="content_box_b" class="content_box clearfix">
				<div class="content_l">
					<!-- S 易通数据 -->
					<div class="box box_c">
						<div class="box_hd">
							<strong>易通数据</strong>
						</div>
						<div class="box_bd">
							<div id="line_wrapper">
								<select name="timeLimit" id="line_sel" class="site_line">
									<option value="10">10天</option>
									<option value="20">20天</option>
									<option value="30">30天</option>
								</select>
								<div id="line_chart"></div>
							</div>
							<!-- <div id="pie_wrapper">
								<select name="" id="pie_sel">
									<option value="">10 天</option>
									<option value="">20 天</option>
								</select>
								<div id="pie_chart"></div>
							</div> -->
						</div>
					</div>
					<!-- E 易通数据 -->
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
									<a href="mainPage_detailArticle.action?article.articleId=${article.articleId }">${artSt.index+1 }. ${str:multiSubStr(article.title,20)}</a>
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