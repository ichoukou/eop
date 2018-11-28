<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<script type="text/javascript">
	var params = {
		chartAction:"viewPiePingTai.action?",
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},	// “开始使用” == 1，“绑定网点” == 2
		isShowButton:true,
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
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

function showJfreeChart(val){
	$("#chart").attr("src","viewPie.action?someDay="+val);
}


</script>

<!-- S Main -->
		<div id="content">
			<div class="content_box clearfix">
				<div class="content_l">
					<div id="start_navi">
						<em>使用易通，体验物流增值服务，让易通与您一起成长！</em>
					</div>
				</div>
				
				<!-- <div class="content_r">
					<a href="javascript:;" title="创建客户编码" id="create_cus_code">创建客户编码</a>
				</div> -->
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
<%-- 										<c:choose> --%>
<%-- 											<c:when test="${yto:getCookie('userType') == 4 --%>
<!-- 					        				|| yto:getCookie('userType') == 41}"> -->
<!-- 											<a href="questionnaire_index.action?menuFlag=chajian_question"> -->
<%-- 											<img src="${imagesPath}/single/rec_img1.jpg" alt="" /></a> --%>
<%-- 											</c:when> --%>
<%-- 											<c:otherwise> --%>
<%-- 											<img src="${imagesPath}/single/rec_img1.jpg" alt="" /></a> --%>
<%-- 											</c:otherwise> --%>
<%-- 										</c:choose> --%>
										
										<li style="display:none;"><a href="passManage_warnningIndex.action?menuFlag=chajian_passManage_warn"><img src="${imagesPath}/single/rec_img4.jpg" alt="" /></a></li>
										<li style="display:none;"><img src="${imagesPath}/single/rec_img1.jpg" alt="" /></li>
										<li style="display:none;">
										<c:choose>
											<c:when test="${yto:getCookie('userType') == 4}">
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
									<li id="unReadMessageNum_li"><a href="javascript:;" title="新消息">新消息（<span>${unReadMessageNum}</span>）</a></li>
<%-- 									<li id="unReadQuestionNum_li"><a href="javascript:;" title="新消息">新问题件（<span>${unReadQuestionNum}</span>）</a></li> --%>
									<c:if test="${yto:getCookie('userType') == 4}">
										<li id='plantKefu'><a href="user!toSubAccountList.action?menuFlag=user_sub_acc_list" title="客服账号">客服账号（${fn:length(associationAccountList)}）</a></li>
										<li id='plantYewu'><a href="user!toPlatformSubAccountList.action?menuFlag=user_platf_acc_list" title="业务账号">业务账号（${fn:length(fencangList)}）</a></li>
									</c:if>
								</ul>
							</div>
							
							<div id="home_sear">
								<form action="" id="sear_form" method="post">
									<input type="hidden" name="currentPage" value="1" />
									<input type="hidden" name="isCheck" value="0" />
									<input type="hidden" name="menuFlag" value="chajian_waybill" />

									<input type="text" id="home_sear_input" class="input_text" name="logisticsIds" /><a href="javascript:;" title="查询" id="home_sear_btn">查 询</a>
									<span id="home_sear_inputTip"></span>
								</form>
							</div>
							
							<div id="to_fun">
								<div id="p_fun_3" class="p_fun">
									<p>短信服务最及时的物流状态</p>
									<a href="smsHomeEvent_homePage.action?menuFlag=sms_home">全自动的物流提醒</a>
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
							<!-- S 物流状态 -->
							<div id="line_wrapper">
								<div id="line_selects" class="clearfix">
										<select name="someDay" id="line_sel2">
											<s:iterator value="recentFiveDay" var="recent">
								        		<option value="<s:property value='recent'/>"><s:property value="recent"/></option>
								        	</s:iterator>
										</select>
									<c:if test="${ yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
										<select name="vipName" id="line_selIsFencang" >
											<option value=0>所有业务分仓</option>
											<s:iterator value="vipList">
												<option <s:if test="%{vipName == id}">selected</s:if> value="<s:property value='id'/>"><s:property value="userNameText"/>(<s:property value="userName"/>)</option>
											</s:iterator>
										</select>
									</c:if>	
								</div>
								<div id="pie_chart"></div>
								<!-- <div id="line_mask"></div>
								<div id="line_chart"></div> -->
					           
							</div>
							<!-- E 物流状态 -->
							
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