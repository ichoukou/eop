<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/store_management.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script> 
<!-- S 当前页面 js -->
<script type="text/javascript" src="${jsPath}/page/store_management.js?d=${str:getVersion() }"></script> 
<!-- E 当前页面 js -->

<!-- S Content -->
  <div id="content">
    <div id="content_hd" class="clearfix">
<!--       <h2 id="message_icon">多店铺管理</h2> -->
      <em>当您在网上开设多个店铺时，你可以对多个账号关联起来，使用一个账号可以轻松管理！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
      <a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=vip_7_shops_manage" target="_blank" >
          <span>快速入门</span>
      </a>
      <p>点击关联淘宝店铺→淘宝账号登录→授权</p>
    </div>
    <div id="content_bd" class="clearfix">
      <div class="title01">关联店铺</div>
      <div class="box box_a">
        <div class="box_bd">
		 <a class="btn btn_a" title="关联淘宝店铺账号" id="bindTaoBaoAccount" href="javascript:;"><span>关联淘宝店铺账号</span></a>
         <p>关联成功后，登录账号为被关联的店铺账号，密码沿用“${yto:getCookie('userName')}”店铺的主账号密码。</p>
        </div>
      </div>
      <div class="title02">已关联店铺</div>
      <div class="table">
		<table>
			<thead>
				<tr>
					<th class="th_a">
						<div class="th_title"><em>账号名称</em></div>
					</th>
					<th class="th_b">
						<div class="th_title"><em>所属平台</em></div>
					</th>
					<th class="th_c">
						<div class="th_title"><em>激活时间</em></div>
					</th>
					<th class="th_d">
						<div class="th_title"><em>操作</em></div>
					</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach items="${associationAccountList }" var="account">
					<tr>
						<td class="td_a">${account.userName }</td>
						<td class="td_b">淘宝</td>
						<td class="td_c"><fmt:formatDate value="${account.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td class="td_d">
							<span class="btn btn_a"><input type="button" value="取消关联" id="cancelBut" class="cancel"/></span>
							<input type="hidden" name="taobaoEncodeKey" value="${account.taobaoEncodeKey}" />
						</td>
					</tr>			
				</c:forEach>			
			</tbody>
		</table>
	</div>
    </div>
  </div>
  <!-- E Content --> 


<%-- <jsp:include page="/activateVipPopup.jsp"></jsp:include> --%>



