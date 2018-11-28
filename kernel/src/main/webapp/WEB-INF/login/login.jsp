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

<!-- 
<div style="height:30px;line-height:30px;background-color:#ff0000;color:#000;text-align:center;font-weight:bold">亲爱的用户，因系统升级维护
中，数据可能有延迟，我们将以最快的速度恢复系统，感谢您的支持与理解！</div> -->


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
		<h1><a href="#" title="易通">易通</a></h1>
		<div id="quick_tel">
			<span id="tel" style="float:right;font-weight:bold；display:block;height:20px;line-height:20px">客服电话：021-69773517</span>
		</div>
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
	<div id="main_wrap">
		<div id="login_main" class="clearfix">
			<div id="tags_cloud">
				<div id="div1">
					<a href="javascript:;" class="yellow blod">快递透明化</a>
					<a href="javascript:;" class="yellow blod">问题件提前预知 </a>
					<a href="javascript:;" class="yellow blod">提升用户忠诚度 </a>
					<a href="javascript:;" class="yellow blod">留住老客户</a>
					<a href="javascript:;" class="green">发货打印</a>
					<a href="javascript:;" class="green">营销活动</a>
					<a href="javascript:;" class="yellow blod">电子账单</a>
					<a href="javascript:;" class="yellow blod">全网物流监控</a>
					<a href="javascript:;" class="green">简单便捷</a>
					<a href="javascript:;" class="green">电商专享</a>
					<a href="javascript:;" class="green">个性化定制</a>
					<a href="javascript:;" class="green">轻松</a>
					<a href="javascript:;" class="green">省人力</a>
					<a href="javascript:;" class="yellow blod">时效提醒</a>
					<a href="javascript:;" class="green blod">智能查件</a>
				</div>
			</div>

			<div id="login_box">
				<div id="login_box_hd"></div>
				<div id="login_box_bd">
					<!-- S 登录方式 B -->
					<div id="login_type_b">
						<p><span class="login_yt">登录易通平台</span> |<a href="http://container.api.taobao.com/container?appkey=<%=ConfigUtilSingle.getInstance().getTOP_APPKEY()%>&encode=utf-8">淘宝卖家登录</a></p>
					</div>
					<!-- E 登录方式 B -->
					<!-- S 登录方式 A -->
					<div id="login_type_a">
						<form name="login_form" action="login_doLogin.action" id="login_form" method="post">
							<p>
								<label for="account_id" id="idStr" data-text="请输入账号"></label>
								<input type="text" class="input_text input_text_a use_ico" id="account_id" name="userName" <c:if test="${userName!=null }">value="${escape:escapeHtml(userName) }"</c:if> />
								<s:fielderror>
								<s:param>userName</s:param>							
								</s:fielderror>
							</p>
							<p>
								<label for="psw" id="pwdStr" data-text="请输入密码"></label>
								<input type="password" title="注意密码大小写（网点初始密码YTO为大写）" class="input_text input_text_a psw_ico" id="psw" name="userPassword" <c:if test="${userPassword!=null }">value="${escape:escapeHtml(userPassword) }"</c:if> />
								<s:fielderror>
								<s:param>userPassword</s:param>							
								</s:fielderror>
							</p>
							<p class="checkcode_line" style="display:none;">
								<label for="checkcode" data-text="验证码不分大小写"></label>
								<input type="text" class="input_text input_text_b yzm_ico" id="checkcode" name="codeString" />
								<img class="change_code" id="checkcode_img" src="#" alt="" />
								<a href="javascript:;" class="change_code" id="change_code">看不清换一张</a>
								<s:fielderror>
								<s:param>code</s:param>							
								</s:fielderror>
							</p>
							<div id="submit_box">
<!-- 								<a href="#" id="login_btn" title="登录易通">登录易通</a> -->
								<input type="submit" id="login_btn" value=""/>
								<input type="checkbox" class="input_checkbox" id="mark_psw" name="isRemeber" value="1" /><label for="mark_psw">记住密码</label>
								<a href="login_showPasswordUI.action" target="_blank">找回密码</a>
							</div>
						</form>
						<div id="error_msg" style="display:block;">${loginMessage}</div>
					</div>
					<!-- E 登录方式 A -->
					
					
				</div>
				<div id="login_box_ft"></div>
			</div>
		</div>
	</div>
	<!-- E Main -->
	
	<!-- S Slide -->
	<div id="slide">
		<div id="slide_hd" >
			<h4>他们都在用易通      </h4>  
			<a href="${ctxPath}/login_goShops.action"><span class="more">more</span></a>
		</div>
		<div id="slide_bd">
			<div id="slide_box">
				<ul class="clearfix">
					<li>
						<a href="http://inman.tmall.com/" target="_blank" title="茵曼旗舰店">
							<img src="${imagesPath}/single/shop_32.jpg" alt="" />
							<em>茵曼旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://qipai.tmall.com/shop/view_shop.htm" target="_blank" title="柒牌官方旗舰店">
							<img src="${imagesPath}/single/shop_7.jpg" alt="" />
							<em>柒牌官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://erke.tmall.com/shop/view_shop.htm" target="_blank" title="鸿星尔克官方旗舰店">
							<img src="${imagesPath}/single/shop_10.jpg" alt="" />
							<em>鸿星尔克官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://gxg.tmall.com/shop/view_shop.htm" target="_blank" title="gxg官方旗舰店">
							<img src="${imagesPath}/single/shop_16.jpg" alt="" />
							<em>gxg官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://0077.taobao.com/" target="_blank" title="柚子美衣淘宝店">
							<img src="${imagesPath}/single/shop_22.jpg" alt="" />
							<em>柚子美衣淘宝店</em>
						</a>
					</li>
					<li>
						<a href="http://midea.tmall.com/shop/view_shop.htm" target="_blank" title="美的精品电器旗舰店">
							<img src="${imagesPath}/single/shop_6.jpg" alt="" />
							<em>美的精品电器旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://hanxuanherbal.tmall.com/" target="_blank" title="汉萱本草旗舰店">
							<img src="${imagesPath}/single/shop_34.jpg" alt="" />
							<em>汉萱本草旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://yishion.tmall.com/shop/view_shop.htm" target="_blank" title="以纯官方旗舰店">
							<img src="${imagesPath}/single/shop_5.jpg" alt="" />
							<em>以纯官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://botanicemotion.tmall.com/" target="_blank" title="品木丝序旗舰店">
							<img src="${imagesPath}/single/shop_33.jpg" alt="" />
							<em>品木丝序旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://microsoft.tmall.com/shop/view_shop.htm" target="_blank" title="微软官方旗舰店">
							<img src="${imagesPath}/single/shop_3.jpg" alt="" />
							<em>微软官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://logitech.tmall.com/shop/view_shop.htm" target="_blank" title="罗技官方旗舰店">
							<img src="${imagesPath}/single/shop_11.jpg" alt="" />
							<em>罗技官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://changyou.tmall.com/shop/view_shop.htm" target="_blank" title="搜狐畅游官方旗舰店">
							<img src="${imagesPath}/single/shop_12.jpg" alt="" />
							<em>搜狐畅游官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://hengyuanxiangty.tmall.com/shop/view_shop.htm" target="_blank" title="恒源祥腾源专卖店">
							<img src="${imagesPath}/single/shop_13.jpg" alt="" />
							<em>恒源祥腾源专卖店</em>
						</a>
					</li>
					<li>
						<a href="http://kingstoncy.tmall.com/shop/view_shop.htm" target="_blank" title="金士顿从谊专卖店">
							<img src="${imagesPath}/single/shop_14.jpg" alt="" />
							<em>金士顿从谊专卖店</em>
						</a>
					</li>
					<li>
						<a href="http://teclastaw.tmall.com/shop/view_shop.htm" target="_blank" title="台电艾维专卖店">
							<img src="${imagesPath}/single/shop_15.jpg" alt="" />
							<em>台电艾维专卖店</em>
						</a>
					</li>
					<li>
						<a href="http://philipsxdh.tmall.com/shop/view_shop.htm" target="_blank" title="飞利浦新顶华专卖店">
							<img src="${imagesPath}/single/shop_4.jpg" alt="" />
							<em>飞利浦新顶华专卖店</em>
						</a>
					</li>
					<li>
						<a href="http://2acg.taobao.com/" target="_blank" title="漫无止境动漫店">
							<img src="${imagesPath}/single/shop_19.jpg" alt="" />
							<em>漫无止境动漫店</em>
						</a>
					</li>
					<li>
						<a href="http://pinkypinky.tmall.com/shop/view_shop.htm?prt=1343095014896&prc=2" target="_blank" title="pinkypinky旗舰店">
							<img src="${imagesPath}/single/shop_18.jpg" alt="" />
							<em>pinkypinky旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://threesheepshome.tmall.com/" target="_blank" title="三羊一家旗舰店">
							<img src="${imagesPath}/single/shop_21.jpg" alt="" />
							<em>三羊一家旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://watsons.tmall.com/shop/view_shop.htm" target="_blank" title="屈臣氏官方旗舰店">
							<img src="${imagesPath}/single/shop_8.jpg" alt="" />
							<em>屈臣氏官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://51damayifu.taobao.com/" target="_blank" title="韦恩男装">
							<img src="${imagesPath}/single/shop_23.jpg" alt="" />
							<em>韦恩男装</em>
						</a>
					</li>
					<li>
						<a href="http://meiqifood.taobao.com" target="_blank" title="美七旗舰店">
							<img src="${imagesPath}/single/shop_30.jpg" alt="" />
							<em>美七旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://shop59425524.taobao.com" target="_blank" title="尚品茶意">
							<img src="${imagesPath}/single/shop_24.jpg" alt="" />
							<em>尚品茶意</em>
						</a>
					</li>
					<li>
						<a href="http://s-t-h.taobao.com/" target="_blank" title="小树屋日韩潮流">
							<img src="${imagesPath}/single/shop_25.jpg" alt="" />
							<em>小树屋日韩潮流</em>
						</a>
					</li>
					<li>
						<a href="http://jilishuma.taobao.com/" target="_blank" title="基立电器">
							<img src="${imagesPath}/single/shop_26.jpg" alt="" />
							<em>基立电器</em>
						</a>
					</li>
					<li>
						<a href="http://yipinnu.tmall.com" target="_blank" title="一品奴服饰旗舰店">
							<img src="${imagesPath}/single/shop_27.jpg" alt="" />
							<em>一品奴服饰旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://beess.taobao.com/" target="_blank" title="花仙子蜂产品旗舰店">
							<img src="${imagesPath}/single/shop_31.jpg" alt="" />
							<em>花仙子蜂产品旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://duer.tmall.com" target="_blank" title="DUER旗舰店">
							<img src="${imagesPath}/single/shop_28.jpg" alt="" />
							<em>DUER旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://transshowzz.tmall.com" target="_blank" title="权尚郑州专卖店">
							<img src="${imagesPath}/single/shop_29.jpg" alt="" />
							<em>权尚郑州专卖店</em>
						</a>
					</li>
					<li>
						<a href="http://sknit.tmall.com/shop/view_shop.htm" target="_blank" title="七匹狼针纺品旗舰店">
							<img src="${imagesPath}/single/shop_1.jpg" alt="" />
							<em>七匹狼针纺品旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://shop60770100.taobao.com" target="_blank" title="话费综合充值中心">
							<img src="${imagesPath}/single/shop_20.jpg" alt="" />
							<em>话费综合充值中心</em>
						</a>
					</li>
					<li>
						<a href="http://qiaodan.tmall.com/shop/view_shop.htm" target="_blank" title="乔丹官方旗舰店">
							<img src="${imagesPath}/single/shop_2.jpg" alt="" />
							<em>乔丹官方旗舰店</em>
						</a>
					</li>
					<li>
						<a href="http://edifier.tmall.com/shop/view_shop.htm" target="_blank" title="漫步者官方旗舰店">
							<img src="${imagesPath}/single/shop_9.jpg" alt="" />
							<em>漫步者官方旗舰店</em>
						</a>
					</li>
				</ul>
			</div>
			
			<div id="slide_navi" style="display:none;">
				<a href="javascript:;" id="slide_prev">Prev</a>
				<a href="javascript:;" id="slide_next">Next</a>
			</div>
		</div>
	</div>
	<!-- E Slide -->
	
	<!-- S Footer -->
	<div id="login_footer">
		<p>圆通速递公司总部：上海青浦区华新镇华徐公路3029弄28号 邮政编码：201705 沪ICP备05004632号</p>
		
		<p>Copyright &copy; 2000-2012 All Right Reserved</p>
	</div>
	<!-- E Footer -->
	
	<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
	
	<script type="text/javascript">
		var params = {
			checkcodeImg: '${ctxPath}/CheckCode',		// 验证码图片
			checkcodeUrl: '${ctxPath}/login_checkVldCode.action',									// 验证码请求地址
			cookieUserName:'${yto:getCookie("userName")}',
			cookiePassword:'${yto:getCookie("password")}',
			showCheckcode: ${yto:getCookie("errorTimes")>1},
			quickStartAction: '${ctxPath}/login_quickRead.action',				// 功能介绍 action
			imagesPath: '${imagesPath}'		
		};
		var _ctxPath = '${ctxPath}';
	</script>
	<script type="text/javascript" src="${jsPath}/lib/jquery-1.7.min.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tags_cloud.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/page/login.js?d=${str:getVersion() }"></script>
	<!--[if IE 6]>
		<script type="text/javascript" src="${jsPath}/util/DD_belatedPNG.js?d=${str:getVersion() }"></script>
		<script type="text/javascript" src="${jsPath}/util/position_fixed.js?d=${str:getVersion() }"></script>
		<script type="text/javascript">
			DD_belatedPNG.fix('.png');
		</script>
	<![endif]-->
</body>
</html>