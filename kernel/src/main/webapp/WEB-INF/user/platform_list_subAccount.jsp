<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/subaccount_management.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<script type="text/javascript">
	var params = {
		banRequest: 'user!bindSubAccount.action'			// “禁用”异步请求
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
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS --> 
<script type="text/javascript" src="${jsPath}/page/subaccount_management.js?d=${str:getVersion() }"></script> 
<!-- E 当前页面 JS -->

<!-- S Content -->
  <div id="content">
    <div id="content_hd" class="clearfix">
      <h2 id="message_icon">管理业务账号</h2>
      <em>业务账号设置及权限分配！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a> </em>
      <a title="快速入门" class="btn btn_d"  href="noint1_audio.action?jsonResult=vip_8_childAccount_manage" target="_blank" >
         <span>快速入门</span>
     </a>
     
      <p>新增→填写相关信息→点击新增账号</p>
    </div>
    <form action="user!toPlatformSubAccountList.action" name="pageForm" id="pageForm" method="post">
    	<input type="hidden" id="currentPage" name="currentPage" value="${pagination.currentPage}" />
    	<input type="hidden" name="menuFlag" value="${menuFlag }" />
    </form>
    <div id="content_bd" class="clearfix"> <span class="btn btn_a">
      <input type="button" value="新增业务账号" onclick="location.href='user!toAddPlatformSubAccount.action?menuFlag=${menuFlag }'"/>
      </span>
      <div class="table">
        <table>
          <thead>
            <tr>
              <th class="th_a"> <div class="th_title"><em>登录账号</em></div>
              </th>
              <th class="th_b"> <div class="th_title"><em>真实姓名</em>
                </div>
              </th>
              <th class="th_c"><div class="th_title"><em>账号类型</em></div></th>
              <th class="th_d"><div class="th_title"><em>联系电话</em></div></th>
              <th class="th_e"><div class="th_title"><em>邮箱</em></div></th>
              <th class="th_f"><div class="th_title"><em>最后登录时间</em></div></th>
              <th class="th_f"><div class="th_title"><em>操作</em></div></th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${associationAccountList }" var="account">
            <tr>
              	<td class="td_a">
              	<c:choose>
              		<c:when test="${account.childType == 'B' }">
              	<a href="user!toEditPlatformSubAccount.action?user.id=${account.id }&menuFlag=${menuFlag }" class="fs01">${account.userName }</a>
              		</c:when>
              		<c:otherwise>
              		${account.userName }
              		</c:otherwise>
              	</c:choose>
              	</td>
              	<td class="td_b">${account.userNameText }</td>
              	<td class="td_c">
              		<c:choose>
              			<c:when test="${account.userType == '11' || account.userType == '21' || account.userType == '41'}">客服</c:when>
              			<c:when test="${account.userType == '12' || account.userType == '22'}">财务</c:when>
              			<c:when test="${account.userType == '13' || account.userType == '23'}">财务、客服</c:when>
              			<c:when test="${account.userType == '2'}">承包区</c:when>
              			<c:when test="${account.userType == '1' && str:isNotEmpty(account.childType)}">
              				<c:choose>
              					<c:when test="${account.childType == 'B'}">公司分仓账号</c:when>
              					<c:otherwise>入驻企业账号</c:otherwise>
              				</c:choose>
						</c:when>
              		</c:choose>
				</td>
              <td class="td_d">${account.mobilePhone }</td>
              <td class="td_e">${account.mail }</td>
              <td class="td_f"><fmt:formatDate value="${account.loginTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
              <td class="td_f">
              	<input type="hidden" value="${account.id }" />
              <c:choose>
              	<c:when test="${account.userState == '0'}">
              		<a href="javascript:;" class="fs02 disable">启用</a> 
              	</c:when>
              	<c:otherwise>
	              	<a href="javascript:;" class="fs02 disable">禁用</a> 
              	</c:otherwise>
              </c:choose>
              <a href="user!toEditPlatformSubAccount.action?user.id=${account.id }&menuFlag=${menuFlag }" class="fs02">编辑</a>
              </td>
            </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
      
      <!-- S PageNavi -->
      <div class="pagenavi"><jsp:include page="/WEB-INF/page.jsp" /></div>
      <!-- E PageNavi -->
    </div>
  </div>
  <!-- E Content -->
