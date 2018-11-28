<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/personalized_settings.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->
	
<script type="text/javascript">
	var params = {
		userType: "${yto:getCookie('userType')}"
		,userState:"${yto:getCookie('userState')}"
		,infoState:"${yto:getCookie('infostate')}"
		,userField003:"${yto:getCookie('field003')}"
		,onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
	<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
	<script src="${jsPath}/page/personalized_settings.js?d=${str:getVersion() }" type="text/javascript" ></script>
	
	<!-- S Content -->
	<div id="content">
		<div id="content_hd" class="clearfix">
<!-- 			<h2 id="message_icon">个性化配置</h2> -->
			<em>个性化配置 让会员自由配置自己的舞台!<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
		</div>
		<div id="content_bd" class="clearfix">
			<div class="box box_a">
                   <div class="box_bd">
                    <p class="title01">你需要管理的店铺</p>
                    <p>淘宝店：</p>
                    <ul class="list01 clearfix">
                    	<c:forEach items="${accountCustomList}" var="accountCustom">
                    		<c:choose>
                    			<c:when test="${accountCustom.isRelated == 1}">
			                     <li><input  name='relatQuery' type="checkbox" value="${accountCustom.userName}" id="lj1" checked="checked"><label for="lj1">${accountCustom.shopName}</label></li>
                    			</c:when>
                    			<c:otherwise>
			                     <li><input name="relatQuery" type="checkbox" value="${accountCustom.userName}" id="lj2"><label for="lj2">${accountCustom.shopName}</label></li>
                    			</c:otherwise>
                    		</c:choose>
                    	</c:forEach>
                    </ul>
                    <p class="btn_box"><span class="btn btn_a"><input value="保 存" id="save" type="button"></span><span class="btn btn_a"><input value="返 回" id="back" type="button"></span></p>
                   </div>
               </div>
		</div>
	</div>
	<!-- E Content -->