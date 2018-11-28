<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/my_accountm.css?d=${str:getVersion() }" media="all" />
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
<script src="${jsPath}/common/common.js?id=2012081301" type="text/javascript" ></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/my_accountm.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->

 
  <!-- S Content -->
  <div id="content">
    <div id="content_hd" class="clearfix">
<!--       <h2 id="message_icon">我的账号</h2> -->
      <em>设置您的账户信息！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em> </div>
    <div id="content_bd" class="clearfix">
      <div class="tab tab_a">
        <div class="tab_triggers">
          <ul>
            <li><a href="javascript:;">基本信息</a></li>
            <c:if test="${yto:getCookie('userType') != 1 || (yto:getCookie('userType') == 1 && str:isNotEmpty(yto:getCookie('childType')))}"><li><a href="javascript:;">修改密码</a></li></c:if>
            <c:if test="${yto:getCookie('userType') == 1 }">
<%--             	<c:choose> --%>
<%--             		<c:when test="${yto:getCookie('userState') == 'TBA' }"> --%>
<!-- 			            <li><a href="javascript:;">绑定用户编码</a></li> -->
<%--             		</c:when> --%>
<%--             		<c:otherwise> --%>
<!--             			<li><a href="javascript:;">修改用户编码</a></li> -->
<%--             		</c:otherwise> --%>
<%--             	</c:choose> --%>
				<li><a href="javascript:;">绑定网点</a></li>
            </c:if>
          </ul>
        </div>
        <div class="tab_panels">
          <div class="tab_panel">
			<form action="userEditPwd_edit.action" method="post" id="basic_info" class="form">
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<input type="hidden" id="edit_res_msg" name="edit_msg" value="${ajaxAlertText}" />
				<input type="hidden" name="user.id" value="${user.id}" />
				<input type="hidden" id="userType" name="user.userType" value="${user.userType }" >
                <input type="hidden" name="user.userSource" value="${user.userSource }" >
                <input type="hidden" name="user.userState" value="${user.userState }" >
                <input type="hidden" name="user.userLevel" value="${user.userLevel }" >
                <input type="hidden" name="user.site" value="${user.site }" >
                <input type="hidden" name="user.userCode" value="${user.userCode }" >
                <input type="hidden" name="user.taobaoEncodeKey" value="${user.taobaoEncodeKey }" >
                <input type="hidden" name="user.field001" value="${user.field001 }" >
                <input type="hidden" name="user.field002" value="${user.field002 }" >
                <input type="hidden" name="user.field003" value="${user.field003 }" >
                <input type="hidden" name="user.userName" value="${user.userName }" >
                <input type="hidden" name="user.userPassword" value="${user.userPassword }" >
                <input type="hidden" name="user.createUser" value="${user.createUser }" >
                <input type="hidden" name="user.userNameText" value="${user.userNameText }" >
                <input type="hidden" name="user.shopAccount" value="${user.shopAccount }" >
                <input type="hidden" name="user.addressProvince" id="addressProvince" value="${user.addressProvince}"/>
                <input type="hidden" name="user.addressCity" id="addressCity" value="${user.addressCity }"/>
                <input type="hidden" name="user.addressDistrict" id="addressDistrict" value="${user.addressDistrict }"/>
				<p>
					<label>登录账号：</label>
					<em>${user.userName }</em>
				</p>
				<c:if test="${yto:getCookie('userType') == 1 }">
				<p>
					<label>客户编码：</label>
					<em>${user.userCode}</em>
				</p>
				</c:if>
				<c:if test="${yto:getCookie('userType') == 2 }">
				<p>
					<label>网点名称：</label>
					<em>${user.userNameText}</em>
				</p>
				</c:if>
				<p>
					<label for="mobile_tel"><span class="req">*</span>手机号码：</label>
					<input type="text" id="mobile_tel" name="user.mobilePhone" class="input_text" value="${user.mobilePhone }" />
					<span id="mobile_telTip"></span>
				</p>
				<p>
					<label for="tel_part_1"><span class="req">*</span>固定电话：</label>
					<input type="text" id="tel_part_1" name="user.telAreaCode" class="input_text" value="${user.telAreaCode }" /> - <input type="text" id="tel_part_2" class="input_text" name="user.telCode" value="${user.telCode }" /> - <input type="text" id="tel_part_3" class="input_text" name="user.telExtCode" value="${user.telExtCode }" />
					<span id="telTip"></span>
				</p>
				<p>
					<label for="email"><span class="req">*</span>邮箱地址：</label>
					<input type="text" id="email" class="input_text" name="user.mail" value="${user.mail }" />
					<span id="emailTip"></span>
				</p>
				<s:fielderror cssStyle="color:red;">
					<s:param>user.mail</s:param>							
				</s:fielderror>
				<c:if test="${yto:getCookie('userType') == 1 
                                                  || yto:getCookie('userType') == 11
                                                  || yto:getCookie('userType') == 12
                                                  || yto:getCookie('userType') == 13}">
				<p>
					<label for="shop_name"><span class="req">*</span>店铺名称：</label>
					<input type="text" id="shop_name" class="input_text" name="user.shopName" value="${user.shopName }" />
					<span id="shop_nameTip"></span>
				</p>
				</c:if>
				<p>
					<label for="province"><span class="req">*</span>发货地址：</label>
					<select id="province"></select>
					<span id="area_tip"></span>
				</p>
				<p>
					<textarea cols="51" rows="2" class="textarea_text" id="detail_address" name="user.addressStreet">${user.addressStreet }</textarea>
					<span id="detail_addressTip"></span>
				</p>
				<s:fielderror cssStyle="color:red;">
					<s:param>user.addressStreet</s:param>							
				</s:fielderror>
				<p style="display:none;">
					<span id="add_confirm"></span>
				</p>
				<p>
					<a href="javascript:;" id="basic_info_submit" class="btn btn_a" title="保 存"><span>保 存</span></a>
				</p>
			</form>
          </div>
          
          <!-- 卖家不能修改密码 -->
          <c:if test="${yto:getCookie('userType') != 1 || (yto:getCookie('userType') == 1 && str:isNotEmpty(yto:getCookie('childType')))}">
          <div class="tab_panel" style="display:none;">
			<form action="#" id="modify_psw" class="form">
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
				<p>
					<label for="old_psw">原密码：</label>
					<input type="password" id="old_psw" name="oldPwd" class="input_text" />
					<span id="old_pswTip"></span>
				</p>
				<s:fielderror cssStyle="color:red;">
					<s:param>oldPwd</s:param>							
				</s:fielderror>
				<p>
					<label for="new_psw">新密码：</label>
					<input type="password" id="new_psw" name="newPwd" class="input_text" />
					<span id="new_pswTip"></span>
				</p>
				<s:fielderror cssStyle="color:red;">
					<s:param>newPwd</s:param>							
				</s:fielderror>
				<p>
					<label for="confirm_psw">确认密码：</label>
					<input type="password" id="confirm_psw" class="input_text" name="surePwd"/>
					<span id="confirm_pswTip"></span>
				</p>
				<s:fielderror cssStyle="color:red;">
					<s:param>surePwd</s:param>							
				</s:fielderror>
				<p>
					<a href="javascript:;" id="modify_psw_submit" class="btn btn_a" title="修 改"><span>修 改</span></a>
					<input type="reset" style="display: none;" id="resetButton" />
				</p>
			</form>
          </div>
          </c:if>
          
          <c:if test="${yto:getCookie('userType') == 1 }">
          	<c:choose>
           		<c:when test="${yto:getCookie('userState') == 'TBA' }">
		          <div id="network_binding" class="tab_panel" style="display:none;">
		            <p>绑定网点能使用电子对账，问题件管理等便捷功能</p>
					<a href="javascript:;" id="binding_btn" class="btn btn_b" title="绑定网点"><span>绑定网点</span></a>
		          </div>
           		</c:when>
           		<c:otherwise>
		          <div class="tab_panel" style="display:none;">
		            <!-- S Box1 -->
		                <div class="box box_a" id="div_uc_view">
		                    <div class="box_bd box_p">
		                        <p><span class="p_span2">客户编码：</span><span class="p_span3" id="uc">${user.userCode }</span></p>
		                        <p><span class="p_span2">绑定网点：</span><span class="p_span3" id="brchTxt"></span></p>
		                        <p><span class="p_span2">店铺名称：</span>
		                        	<c:forEach items="${bindUserList}" varStatus="sta" var="bindUser">
										<span class="p_span3">&nbsp;${sta.index+1}、${bindUser.shopName }</label>
									</c:forEach>
		                        </p>
		                        <p>
		                        <a href="javascript:;" class="btn btn_a" id="cancelBindBut" title="取消绑定"><span>取消绑定</span></a>
		                        <a href="javascript:;" class="btn btn_a" id="changeBindBut" title="更换网点"><span>更换网点</span></a>
		                        </p>
		                        
		                        <div class="p_color">
		                        <p>一、取消绑定影响</p>
		                        <p>1、卖家不能使用电子对账、问题件管理等需要客户编码支撑的易通系统功能</p>
		                        <p>2、网点将不能查看卖家等相关运单信息</p>
		                        <p>二、更换网点影响</p>
		                        <p>1、 更换网点后，原网点将不能查看店铺相关信息</p>
		                        </div>
		                    </div>
		                </div>
		                <!-- E Box1 -->
		                
		                <!-- S Box2 -->
		                <div class="box box_a" id="div_uc_edit" style="display:none">
		                    <div class="box_bd box_p">
		                        <form action="" id="check_form" method="post">
		                        <input type="hidden" name="menuFlag" value="${menuFlag }" />
		                        <p>
		                        <span class="p_span2"><span class="req">*</span>客户编码</span>
		                        <input type="text" class="input_text" id="input_text_demo" name="user.userCode" />
		                        <span id="input_text_demoTip"></span>
		                        </p>
		                        <input type="reset" style="display: none;" id="resetCheckForm" />
		                     	</form>
		                        <p><span class="p_span2">绑定网点：</span><span class="p_span3" id="new_branch_txt"></span></p>
		                        
		                        <p>
		                        <a href="javascript:;" id="btn_up_uc_submit" class="btn btn_a" title="修改"><span>修改</span></a>
		                        <a href="javascript:;" id="btn_up_uc_cancel" class="btn btn_a" title="取消"><span>取消</span></a>
		                        </p>
		                       
		                    </div>
		                </div>
		                <!-- E Box2 -->
		          </div>
           		</c:otherwise>
           	</c:choose>
          </c:if>
        </div>
      </div>
    </div>
  </div>
  <!-- E Content --> 
