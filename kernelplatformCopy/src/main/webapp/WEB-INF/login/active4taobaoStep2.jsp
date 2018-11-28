<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/m_network.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	
	<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},	// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		userType:"${yto:getCookie('userType')}",
		taobaoEncodeKey:"${yto:getCookie('taobaoEncodeKey')}",
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
	</script>
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/m_network.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
	
	<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">您的账号还没有和当地网点绑定，请联系您的网点为您创建一个客户编码</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S Box -->
                <div class="box box_a">
                    <div class="box_bd box_p">
	                     <form id="check_form" action="" method="post">
	                        <p>
	                        <span class="p_span">*客户编码</span>
	                        <input type="text" class="input_text" id="input_text_demo" name="user.userCode" />
	                        <span id="input_text_demoTip"></span>
	                        </p>
	                        <span id="new_branch_txt" style="display: none;"></span>
	                        <input type="reset" style="display: none;" id="resetForm" />
	                     </form>
                        <p>如果没有客户编码，请联系当地网点获取</p>
                        <p>
                        <a href="javascript:;" id="bind_btn_a" class="btn btn_a" title="绑定"><span>绑定</span></a>
                        <a href="javascript:;" id="bind_btn_b" class="btn btn_a" title="返回"><span>返回</span></a>
                        </p>
                    </div>
                </div>
                <!-- E Box -->
				
			</div>
		</div>
		<!-- E Content -->