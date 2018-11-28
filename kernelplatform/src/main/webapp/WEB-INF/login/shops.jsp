<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="net.ytoec.kernel.util.ConfigUtilSingle"%>
<%@ include file="/WEB-INF/common/globalAttr.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8" />
	<meta name="keywords" content="易通物流,电商版,电子商务,圆通电商物流,物流平台,电商物流平台,电子商务平台,易通电商版,电商物流网,圆通电子商务物流平台,圆通易通,电商物流诚信系统" />
	<meta name="description" content="易通诚信软件电商版由圆通速递推出，全国首个电商物流平台。易通诚信软件电商版又称电子商务物流信息平台。主要功能有问题件管理、智能查件、电子对账等物流信息化服务。021-69777830。" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/login.css?d=${str:getVersion() }" media="all" />
	<title>易通电子商务物流信息平台-圆通电商物流平台-圆通易通诚信软件电商版</title>
</head>
<script type="text/javascript">
	<c:if test="${yto:getCookie('isLogin') == 'true' && yto:getCookie('action') == 'bindedAccount'}">
		window.location.href = 'http://container.api.taobao.com/container?appkey=<%=ConfigUtilSingle.getInstance().getTOP_APPKEY()%>&encode=utf-8';
	</c:if>
</script>

<body id="login">
	<div id="contact">
			
			<span id="wangwang"><a href="http://web.im.alisoft.com/msg.aw?v=2&amp;uid=ytoyitong%20&amp;site=cnalichn&amp;s=1" target="_blank" title="点击这里给我发消息"><img src="images/single/ww_ico.png" alt="点击这里给我发消息" />
			<div class="text">旺旺客服</div>
			</a>
				
			</span>
			<span id="qq"><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2366710544&amp;site=qq&amp;menu=yes"><img src="images/single/qq_ico.png" alt="点击这里给我发消息" title="点击这里给我发消息" />
			<div class="text">QQ客服</div>
			</a>
				
			</span>
			
			
		</div>
	<!-- S Header -->
	<div id="login_header">
		<h1><a href="http://ec.yto.net.cn/login_goLogin.action" title="易通">易通</a></h1>
		<div id="quick_link">
			<span id="quick_start"><a href="javascript:;">功能介绍</a></span> 
			<span id="earth_icon"><a href="javascript:;">易通支持浏览器</a>
				<span id="browse_dialog">
					<ul id="rec_browse">
						<li><a href="http://www.firefox.com.cn/download/" title="火狐浏览器" target="_blank">火狐浏览器</a></li>
						<li><a href="http://download.microsoft.com/download/1/6/1/16174D37-73C1-4F76-A305-902E9D32BAC9/IE8-WindowsXP-x86-CHS.exe" title="IE8" target="_blank">IE8</a></li>
					</ul>
				</span>
			</span> 
			
			<span id="shortcut"><a href="javascript:;">下载快捷方式</a></span>
			<span id="fav" style="margin-right:4px;"><a href="javascript:;">收藏易通</a></span>
		</div>
	</div>
	<!-- E Header -->
	
	<!-- S Main -->
	<div id="main_wrap" style="background-color:#fff;">
		<div id="login_main" class="clearfix" >
			<div class="logolist">
				<ul class="clearfix">
					<li>
						<p><a href="http://inman.tmall.com/" target="_blank" title="茵曼旗舰店">
							<img src="${imagesPath}/single/shop_32.jpg" alt="" /></a>
						</p>
						<p>茵曼旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://qipai.tmall.com/shop/view_shop.htm" target="_blank" title="柒牌官方旗舰店">
							<img src="${imagesPath}/single/shop_7.jpg" alt="" /></a>
						</p>
						<p>柒牌官方旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://erke.tmall.com/shop/view_shop.htm" target="_blank" title="鸿星尔克官方旗舰店">
							<img src="${imagesPath}/single/shop_10.jpg" alt="" /></a>
						</p>
						<p>鸿星尔克官方旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://gxg.tmall.com/shop/view_shop.htm" target="_blank" title="gxg官方旗舰店">
							<img src="${imagesPath}/single/shop_16.jpg" alt="" /></a>
						</p>
						<p>gxg官方旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://0077.taobao.com/" target="_blank" title="柚子美衣淘宝店">
							<img src="${imagesPath}/single/shop_22.jpg" alt="" /></a>
						</p>
						<p>柚子美衣淘宝店</p>
						
					</li>
					<li>
						<p><a href="http://midea.tmall.com/shop/view_shop.htm" target="_blank" title="美的精品电器旗舰店">
							<img src="${imagesPath}/single/shop_6.jpg" alt="" /></a>
						</p>
						<p>美的电器旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://hanxuanherbal.tmall.com/" target="_blank" title="汉萱本草旗舰店">
							<img src="${imagesPath}/single/shop_34.jpg" alt="" /></a>
						</p>
						<p>汉萱本草旗舰店</p>
					</li>
					<li>
						<p><a href="http://yishion.tmall.com/shop/view_shop.htm" target="_blank" title="以纯官方旗舰店">
							<img src="${imagesPath}/single/shop_5.jpg" alt="" /></a>
						</p>
						<p>以纯官方旗舰店</p>
					</li>
					<li>
						<p><a href="http://botanicemotion.tmall.com/" target="_blank" title="品木丝序旗舰店">
							<img src="${imagesPath}/single/shop_33.jpg" alt="" /></a>
						</p>
						<p>品木丝序旗舰店</p>
					</li>
					<li>
						<p><a href="http://microsoft.tmall.com/shop/view_shop.htm" target="_blank" title="微软官方旗舰店">
							<img src="${imagesPath}/single/shop_3.jpg" alt="" /></a></p>
							<p>微软官方旗舰店</p>
					</li>
					<li>
						<p><a href="http://logitech.tmall.com/shop/view_shop.htm" target="_blank" title="罗技官方旗舰店">
							<img src="${imagesPath}/single/shop_11.jpg" alt="" /></a></p>
							<p>罗技官方旗舰店</p>
					</li>
					<li>
						<p><a href="http://changyou.tmall.com/shop/view_shop.htm" target="_blank" title="搜狐畅游官方旗舰店">
							<img src="${imagesPath}/single/shop_12.jpg" alt="" /></a>
						</p>
						<p>搜狐畅游官方旗舰店</p>
					</li>
					<li>
						<p><a href="http://hengyuanxiangty.tmall.com/shop/view_shop.htm" target="_blank" title="恒源祥腾源专卖店">
							<img src="${imagesPath}/single/shop_13.jpg" alt="" /></a>
						</p>
						<p>恒源祥腾源专卖店</p>
						
					</li>
					<li>
						<p><a href="http://kingstoncy.tmall.com/shop/view_shop.htm" target="_blank" title="金士顿从谊专卖店">
							<img src="${imagesPath}/single/shop_14.jpg" alt="" /></a>
						</p>
						<p>金士顿从谊专卖店</p>
						
					</li>
					<li>
						<p><a href="http://teclastaw.tmall.com/shop/view_shop.htm" target="_blank" title="台电艾维专卖店">
							<img src="${imagesPath}/single/shop_15.jpg" alt="" /></a>
						</p>
						<p>台电艾维专卖店</p>
						
					</li>
					<li>
						<p><a href="http://philipsxdh.tmall.com/shop/view_shop.htm" target="_blank" title="飞利浦新顶华专卖店">
							<img src="${imagesPath}/single/shop_4.jpg" alt="" /></a>
						</p>
						<p>飞利浦新顶华专卖店</p>
						
					</li>
					<li>
						<p><a href="http://2acg.taobao.com/" target="_blank" title="漫无止境动漫店">
							<img src="${imagesPath}/single/shop_19.jpg" alt="" /></a>
						</p>
						<p>漫无止境动漫店</p>
						
					</li>
					<li>
						<p><a href="http://pinkypinky.tmall.com/shop/view_shop.htm?prt=1343095014896&prc=2" target="_blank" title="pinkypinky旗舰店">
							<img src="${imagesPath}/single/shop_18.jpg" alt="" /></a>
						</p>
						<p>pinkypinky旗舰店</p>
						
					</li>
					<li>
						<a href="http://threesheepshome.tmall.com/" target="_blank" title="三羊一家旗舰店">
							<img src="${imagesPath}/single/shop_21.jpg" alt="" /></a>
						</p>
						<p>三羊一家旗舰店</p>
						
					</li>
					<li>
						<a href="http://watsons.tmall.com/shop/view_shop.htm" target="_blank" title="屈臣氏官方旗舰店">
							<img src="${imagesPath}/single/shop_8.jpg" alt="" /></a>
						</p>
						<p>屈臣氏官方旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://51damayifu.taobao.com/" target="_blank" title="韦恩男装">
							<img src="${imagesPath}/single/shop_23.jpg" alt="" /></a>
						</p>
						<p>韦恩男装</p>
						
					</li>
					<li>
						<p><a href="http://meiqifood.taobao.com" target="_blank" title="美七旗舰店">
							<img src="${imagesPath}/single/shop_30.jpg" alt="" /></a>
						</p>
						<p>美七旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://shop59425524.taobao.com" target="_blank" title="尚品茶意">
							<img src="${imagesPath}/single/shop_24.jpg" alt="" /></a>
						</p>
						<p>尚品茶意</p>
					
					</li>
					<li>
						<p><a href="http://s-t-h.taobao.com/" target="_blank" title="小树屋日韩潮流">
							<img src="${imagesPath}/single/shop_25.jpg" alt="" /></a>
						</p>
						<p>小树屋日韩潮流</p>
						
					</li>
					<li>
						<p><a href="http://jilishuma.taobao.com/" target="_blank" title="基立电器">
							<img src="${imagesPath}/single/shop_26.jpg" alt="" /></a>
						</p>
						<p>基立电器</p>
						
					</li>
					<li>
						<p><a href="http://yipinnu.tmall.com" target="_blank" title="一品奴服饰旗舰店">
							<img src="${imagesPath}/single/shop_27.jpg" alt="" /></a>
						</p>
						<p>一品奴服饰旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://beess.taobao.com/" target="_blank" title="花仙子蜂产品旗舰店">
							<img src="${imagesPath}/single/shop_31.jpg" alt="" /></a>
						</p>
						<p>花仙子蜂产品旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://duer.tmall.com" target="_blank" title="DUER旗舰店">
							<img src="${imagesPath}/single/shop_28.jpg" alt="" /></a>
						</p>
						<p>DUER旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://transshowzz.tmall.com" target="_blank" title="权尚郑州专卖店">
							<img src="${imagesPath}/single/shop_29.jpg" alt="" /></a>
						</p>
						<p>权尚郑州专卖店</p>
						
					</li>
					<li>
						<p><a href="http://sknit.tmall.com/shop/view_shop.htm" target="_blank" title="七匹狼针纺品旗舰店">
							<img src="${imagesPath}/single/shop_1.jpg" alt="" /></a>
						</p>
						<p>七匹狼针纺品旗舰店</p>
					
					</li>
					<li>
						<p><a href="http://shop60770100.taobao.com" target="_blank" title="话费综合充值中心">
							<img src="${imagesPath}/single/shop_20.jpg" alt="" /></a>
						</p>
						<p>话费综合充值中心</p>
						
					</li>
					<li>
						<p><a href="http://qiaodan.tmall.com/shop/view_shop.htm" target="_blank" title="乔丹官方旗舰店">
							<img src="${imagesPath}/single/shop_2.jpg" alt="" /></a>
						</p>
						<p>乔丹官方旗舰店</p>
						
					</li>
					<li>
						<p><a href="http://edifier.tmall.com/shop/view_shop.htm" target="_blank" title="漫步者官方旗舰店">
							<img src="${imagesPath}/single/shop_9.jpg" alt="" /></a>
						</p>
						<p>漫步者官方旗舰店</p>
						
					</li>
				</ul>
			</div>
		
		</div>
	</div>
	<!-- E Main -->
	<!-- S Footer -->
	<div id="login_footer">
		<p>圆通速递公司总部：上海青浦区华新镇华徐公路3029弄28号 邮政编码：201705 Copyright &copy; 2000-2012 All Right Reserved 沪ICP备05004632号</p>
	</div>
	<!-- E Footer -->
	
	<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
	<script type="text/javascript">
		var params = {
			quickStartAction: '${ctxPath}/login_quickRead.action',				// 功能介绍 action
			imagesPath: '${imagesPath}'		
		};
		var _ctxPath = '${ctxPath}';
	</script>
	<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/page/shop.js?d=${str:getVersion() }"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${jsPath}/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
		<script type="text/javascript" src="${jsPath}/util/position_fixed.js?d=${str:getVersion() }"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('.png');
		</script>
	<![endif]-->
</body>
</html>