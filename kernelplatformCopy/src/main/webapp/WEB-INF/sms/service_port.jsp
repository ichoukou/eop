<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
	<!-- S 当前页面 CSS -->
	<link href="${cssPath}/page/note_back_new.css?d=${str:getVersion() }" rel="stylesheet" type="text/css">
	<!-- E 当前页面 CSS -->
	<title>短信套餐</title>
	
	<!-- S Main -->
		<!-- S Content -->
		<div id="content">
		<div>此功能已暂停!</div>
		
			<%-- <div id="content_hd" class="clearfix">
				<h2 id="message_icon">1.选择短信套餐>2.点击购买>3.开始使用</h2> 
			</div>
			<div id="content_bd" class="clearfix">
			
				<div class="box box_a">
					<div class="box_hd">
						<strong>购买短信套餐</strong>
					</div>
					<div class="box_bd">
						<ul class="keys clearfix">
							<li class="content">
							   <div class="title">
								   <img src="${imagesPath}/single/note_50.png" />
								</div>
								<div class="dist">
  								    <span class="type">50元超值套餐</span>
  								    <span class="notice">客户收到短信才收费</span>
									<span class="count">短信数量：600条</span>
									<span class="price">价格：<em>50元</em></span>
									<span class="reduce">折合价格：0.083元/条 </span>
								</div>
								<div class="opt">
									<a href="javascript:;" class="btn_buy">立即购买</a>
									<input class="flag" type="hidden" name="flag" value="1" />
									<!-- 返回位置 以下为返回参数 -->
									<input id="_pos" type="hidden" value="${pos}" />
									<input id="_currentPage2" type="hidden" value="${currentPage2}" />
									<!-- 短信服务设置下一步 -->
									<input id="_serviceId" type="hidden" value="${serviceId}" />
									<!-- 智能查件 -->
									<input id="_logisticsIds" type="hidden" value="${logisticsIds}" />
									<input id="_num" type="hidden" value="${num}" />
									<input id="_isCheck2" type="hidden" value="${isCheck2}" />
									<!-- 问题件 -->
									<input id="_backStratDate" type="hidden" value="${backStratDate}" />
									<input id="_backEndDate" type="hidden" value="${backEndDate}" />
									<input id="_backShopName" type="hidden" value="${backShopName}" />
									<input id="_backQType" type="hidden" value="${backQType}" />
									<input id="_backQTypeVal" type="hidden" value="${backQTypeVal}" />
									<input id="_backIsShowSigned" type="hidden" value="${backIsShowSigned}" />
									<input id="_backInput" type="hidden" value="${backInput}" />
									<input id="_backTabStatus" type="hidden" value="${backTabStatus}" />		
									
								</div>
							</li>
							<li class="content">
							   <div class="title">
								   <img src="${imagesPath}/single/note_100.png" />
								</div>
								<div class="dist">
  								    <span class="type">100元超值套餐</span>
  								    <span class="notice">客户收到短信才收费</span>
									<span class="count">短信数量：1220条</span>
									<span class="price">价格：<em>100元</em></span>
									<span class="reduce">折合价格：0.082元/条</span>
								</div>
								<div class="opt">
									<a href="javascript:;" class="btn_buy">立即购买</a>
									<input class="flag" type="hidden" name="flag" value="2" />
								</div>
							</li>
							<li class="content">
							   <div class="title">
								   <img src="${imagesPath}/single/note_200.png" />
								</div>
								<div class="dist">
  								    <span class="type">200元超值套餐</span>
  								    <span class="notice">客户收到短信才收费</span>
									<span class="count">短信数量：2500条</span>
									<span class="price">价格：<em>200元</em></span>
									<span class="reduce">折合价格：0.080元/条 </span>
								</div>
								<div class="opt">
									<a href="javascript:;" class="btn_buy">立即购买</a>
									<input class="flag" type="hidden" name="flag" value="3" />
								</div>
							</li>
							<li class="content">
							   <div class="title">
								   <img src="${imagesPath}/single/note_500.png" />
								</div>
								<div class="dist">
  								    <span class="type">500元超值套餐</span>
  								    <span class="notice">客户收到短信才收费</span>
									<span class="count">短信数量：6500条</span>
									<span class="price">价格：<em>500元</em></span>
									<span class="reduce">折合价格：0.077元/条 </span>
								</div>
								<div class="opt">
									<a href="javascript:;" class="btn_buy">立即购买</a>
									<input class="flag" type="hidden" name="flag" value="4" />
								</div>
							</li>
							<li class="content">
							   <div class="title">
								   <img src="${imagesPath}/single/note_1000.png" />
								</div>
								<div class="dist">
  								    <span class="type">1000元超值套餐</span>
  								    <span class="notice">客户收到短信才收费</span>
									<span class="count">短信数量：15000条</span>
									<span class="price">价格：<em>1000元</em></span>
									<span class="reduce">折合价格：0.067元/条 </span>
								</div>
								<div class="opt">
									<a href="javascript:;" class="btn_buy">立即购买</a>
									<input class="flag" type="hidden" name="flag" value="5" />
								</div>
							</li>
							<li class="content">
							   <div class="title">
								   <img src="${imagesPath}/single/note_2000.png" />
								</div>
								<div class="dist">
  								    <span class="type">2000元超值套餐</span>
  								    <span class="notice">客户收到短信才收费</span>
									<span class="count">短信数量：32000条</span>
									<span class="price">价格：<em>2000元</em></span>
									<span class="reduce">折合价格：0.063元/条 </span>
								</div>
								<div class="opt">
									<a href="javascript:;" class="btn_buy">立即购买</a>
									<input class="flag" type="hidden" name="flag" value="6" />
								</div>
							</li>
							<li class="content">
							   <div class="title">
								   <img src="${imagesPath}/single/note_5000.png" />
								</div>
								<div class="dist">
  								    <span class="type">5000元超值套餐</span>
  								    <span class="notice">客户收到短信才收费</span>
									<span class="count">短信数量：85000条</span>
									<span class="price">价格：<em>5000元</em></span>
									<span class="reduce">折合价格：0.059元/条</span>
								</div>
								<div class="opt">
									<a href="javascript:;" class="btn_buy">立即购买</a>
									<input class="flag" type="hidden" name="flag" value="7" />
								</div>
                        		<input type="hidden" value="5" id ="serviceId">
							</li>
						</ul>
					</div>
				</div> --%>
			</div>
		</div>
		<!-- E Content -->
		
	<script type="text/javascript">
		var params = {
				buyPortsAction: 'smsServiceMarket!buyPorts.action'				// 购买短信套餐Action
		}
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
    <script type="text/javascript" src="${jsPath}/page/note_back.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->