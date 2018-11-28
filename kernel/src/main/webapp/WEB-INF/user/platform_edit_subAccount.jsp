<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath }/page/new_subaccount.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<script type="text/javascript">
	var params = {
		addUrl:"user!toAddPlatformSubAccount.action?menuFlag=${menuFlag }",
		listUrl:"user!toPlatformSubAccountList.action?menuFlag=${menuFlag }",
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},	// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<!-- S 当前页面 JS --> 
<script type="text/javascript" src="${jsPath }/page/platform_edit_subaccount.js?d=${str:getVersion() }"></script> 
<!-- E 当前页面 JS -->

<!-- S Content -->
  <div id="content">
    <div id="content_hd" class="clearfix">
      <h2 id="message_icon">编辑业务账号</h2>
    </div>
    <div id="content_bd" class="clearfix">
      <div class="box box_a">
        <div class="box_bd">
			<form action="user!editSubAccount.action" method="post" class="form" id="new_acc_form">
				<input id="isValidate" value='${isValidate}' type="hidden">
				<input name="user.id" value='${user.id}' type="hidden">
				<input name="user.childType" value='${user.childType}' type="hidden">
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<c:if test="${yto:getCookie('userType') == 4}">
                <p>
					<label for="acc_id"><span class="req">*</span>账户类型：</label>
					<c:choose>
						<c:when test="${user.childType == 'B'}">
							<input type="text" class="input_text" readonly value="公司分仓账号"/>
						</c:when>
						<c:otherwise>
							<input type="text" class="input_text" readonly value="入驻企业账号"/>
						</c:otherwise>
					</c:choose>
				</p>
                </c:if>
				<p>
					<label for="acc_id"><span class="req">*</span>登录账号：</label>
					<input type="text" id="" class="input_text"  name="user.userName" readonly value="${user.userName}"/>
					<span id="acc_idTip"></span>
				</p>
				<p>
					<label for="init_psw"><span class="req">*</span>登录密码：</label>
					<input type="password" id="edit_psw" class="input_text" name="user.userPassword" />
					<span id="edit_pswTip">如果不填写登录密码则不修改密码</span>
				</p>
				<!-- 平台用户子账号 CustomerId 不可修改-->
                <p>
                    <label for="init_psw"><span class="req">*</span>CustomerID：</label>
                  		<input name="user.userType" value='1' type="hidden"><!-- 平台用户子账号默认为卖家 -->
                  		<input name="user.clientId" value="${user.clientId }" type="hidden">
                  		<input name="user.taobaoEncodeKey" id="taobaoEncodeKey" class="input_text" readonly
                       value='${user.taobaoEncodeKey }'>
                </p>
				<p>
					<label for="real_name"><span class="req">*</span>真实姓名：</label>
					<input type="text" id="real_name" class="input_text" name="user.userNameText" value="${user.userNameText }" />
					<span id="real_nameTip"></span>
				</p>
				<p>
					<label for="mobile_tel"><span class="req">*</span>手机号码：</label>
					<input type="text" id="mobile_tel" class="input_text" name="user.mobilePhone" value="${user.mobilePhone}" />
					<span id="mobile_telTip"></span>
				</p>
				<p>
					<label for="tel_part_1"><span class="req">*</span>固定电话：</label>
					<input type="text" id="tel_part_1" class="input_text" name="user.telAreaCode" value="${user.telAreaCode}" /> - <input type="text" id="tel_part_2" name="user.telCode" class="input_text" value="${user.telCode }" /> - <input type="text" name="user.telExtCode" id="tel_part_3" value="${user.telExtCode}" class="input_text" />
					<span id="telTip"></span>
				</p>
				<p>
					<label for="email"><span class="req">*</span>邮箱地址：</label>
					<input type="text" id="email" class="input_text" name="user.mail" value="${user.mail }" />
					<span id="emailTip"></span>
				</p>
				
				<p>
					<a href="javascript:;" id="ok_btn" class="btn btn_b" title="确定"><span>确 定</span></a>
					<a href="javascript:;" id="back_btn" class="btn btn_b" title="返回"><span>返 回</span></a>
				</p>
			</form>
        </div>
      </div>
    </div>
  </div>
  <!-- E Content --> 