<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/monitor_n.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>运单监控</title>
<!-- S Content -->
<div id="content2">
	<form action="noint!iKnow.action" id="formIKnow">
		<!-- 需要跳转的action名字 -->
	   <input name="nextAction" value="monitor_index" type="hidden"/>
	   <input type="hidden" name="menuFlag" value="chajian_monitor_index" />
	   <!-- 需要更新字段对应的set方法 -->
	   <input name="methodName" value="setField002" type="hidden"/>
	</form>
      <a href="#"><img src="images/single/star_btn.jpg" class="content_btn"/></a>
      
      <!-- S content_tab -->
      <div id="content_tab">
     <ul>
        <li id="jjbutton_1"><img src="images/single/c_tab1_click.jpg"/></li>
        <li id="jjbutton_2"><img src="images/single/c_tab2.jpg"/></li>
        <li id="jjbutton_3"><img src="images/single/c_tab3.jpg"/></li>
        <li id="jjbutton_4"><img src="images/single/c_tab4.jpg"/></li>
        <li id="jjbutton_5"><img src="images/single/c_tab5.jpg"/></li>
        </ul>
        <div id="panels">
            <div class="content_tab_c"  id="jjlist_1"><img src="images/single/c_tab_c1.jpg"/></div>
            <div class="content_tab_c"  id="jjlist_2" style="display:none"><img src="images/single/c_tab_c2.jpg"/></div>
            <div class="content_tab_c"  id="jjlist_3" style="display:none"><img src="images/single/c_tab_c3.jpg"/></div>
            <div class="content_tab_c"  id="jjlist_4" style="display:none"><img src="images/single/c_tab_c4.jpg"/></div>
            <div class="content_tab_c"  id="jjlist_5" style="display:none"><img src="images/single/c_tab_c5.jpg"/></div>
        </div>
</div>
   <!-- E content_tab -->
   
<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	}
</script>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/monitor_n.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->

   
</div>
<!-- E Content -->
