<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath }/page/new_subaccount.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<script type="text/javascript">
	var params = {
		addUrl:"user!toAddSubAccount.action?menuFlag=${menuFlag }",
		listUrl:"user!toSubAccountList.action?menuFlag=${menuFlag }",
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},	// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		showBindCode:true,
		userType:"${yto:getCookie('userType')}",
		userName:"${yto:getCookie('userName')}",				//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<!-- S 当前页面 JS --> 
<script type="text/javascript" src="${jsPath }/page/new_subaccount.js?d=${str:getVersion() }"></script> 
<!-- E 当前页面 JS -->

<!-- S Content -->
  <div id="content">
    <div id="content_hd" class="clearfix">
      <h2 id="message_icon">新增子账号</h2>
      <em>主账号激活后，您可以创建财务子账号、客服子账号来处理相应的业务！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em> </div>
    <div id="content_bd" class="clearfix">
      <div class="box box_a">
        <div class="box_bd">
			<form action="user_addSubAccount.action" method="post" class="form" id="new_acc_form">
				<input type="hidden" id="pageHid" value="add"/>
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<input id="isValidate" value='${isValidate}' type="hidden">
				<p>
					<label for="acc_id"><span class="req">*</span>登录账号：</label>
					<input type="text" id="acc_id" class="input_text"  name="user.userName" />
					<span id="acc_idTip"></span>
					<font color="red">
					<s:fielderror>
						<s:param>user.userName</s:param>
					</s:fielderror></font>
				</p>
				<p>
					<label for="real_name"><span class="req">*</span>真实姓名：</label>
					<input type="text" id="real_name" class="input_text" name="user.userNameText" />
					<span id="real_nameTip"></span>
					<font color="red"><s:fielderror>
						<s:param>user.userNameText</s:param>
					</s:fielderror></font>
				</p>
				<p>
					<label for="mobile_tel"><span class="req">*</span>手机号码：</label>
					<input type="text" id="mobile_tel" class="input_text" name="user.mobilePhone" />
					<span id="mobile_telTip"></span>
					<font color="red"><s:fielderror>
						<s:param>user.mobilePhone</s:param>
					</s:fielderror></font>
				</p>
				<p>
					<label for="tel_part_1"><span class="req">*</span>固定电话：</label>
					<input type="text" id="tel_part_1" class="input_text" name="user.telAreaCode" /> - <input type="text" id="tel_part_2" name="user.telCode" class="input_text" /> - <input type="text" name="user.telExtCode" id="tel_part_3" class="input_text" />
					<span id="telTip"></span>
				</p>
				<p>
					<label for="email"><span class="req">*</span>邮箱地址：</label>
					<input type="text" id="email" class="input_text" name="user.mail" />
					<span id="emailTip"></span>
					<font color="red"><s:fielderror>
						<s:param>user.mail</s:param>
					</s:fielderror></font>
				</p>
				<p>
					<label><span class="req">*</span>账号类型：</label>
					<span id="acctype_check">
						<c:choose>
							<c:when test="${yto:getCookie('userType') == 4}">
								<input name="user.userType" value='41' type="hidden">
								<label style="width:800px;">客服:问题件管理、智能查询、运单监控、我的关注、面单打印、面单模板、我要发货、运单绑定、网点查找、消息管理</label>
							</c:when>
							<c:otherwise>
								<input name="user.userType" id="userType" value="${yto:getCookie('userType')}" type="hidden">
								<label class="check_line" for="acc_type_a">
									<input type="checkbox" id="acc_type_a" class="input_checkbox T" name="range" value="${yto:getCookie('userType')}2" />
									财务:财务管理、消息管理、辅助管理、账号管理
								</label>
								<label class="check_line" for="acc_type_b">
									<input type="checkbox" id="acc_type_b" class="input_checkbox T" name="range" value="${yto:getCookie('userType')}1" />
									客服:运单管理、消息管理、辅助管理、账号管理
								</label>
								<c:if test="${yto:getCookie('userType') == 2 && str:isEmpty(yto:getCookie('parentId'))}">
                                <label class="check_line" for="lj3">
                                    <input id='lj3' class="input_checkbox T" name="range"  type="checkbox" value="2" />
                                   	承包区：所有功能
                                </label>
                                </c:if>
							</c:otherwise>
						</c:choose>
					</span>
					<span id="checkTip"></span>
					<font color="red"><s:fielderror>
						<s:param>user.userType</s:param>
					</s:fielderror></font>
				</p>
				<c:if test="${yto:getCookie('userType') == 2}">
				<p>
					<label>分配客户：</label>
					<a href="javascript:;" id="addCustomers" style="text-decoration:underline;" >选择客户</a>&nbsp;&nbsp;
					<span id="customersTip"></span>
				</p>
				</c:if>
				<p>
					<label for="init_psw"><span class="req">*</span>初始密码：</label>
					<input type="password" id="init_psw" class="input_text" name="user.userPassword" />
					<span id="init_pswTip"></span>
					<font color="red"><s:fielderror>
						<s:param>user.userPassword</s:param>
					</s:fielderror></font>
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
  <div class="J-hide" style="display:none;"></div>
  <!-- E Content --> 