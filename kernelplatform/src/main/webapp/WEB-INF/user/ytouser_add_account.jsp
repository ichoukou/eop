<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	
	function checkUserType(type){
// 		alert(type);
// 		if(type==1){
			
// 		}else{
			//alert("隐藏！");
// 			$("#p_customerId").html("");
// 		}
	    location.href="ytoUser!toAddYtoUserAccount.action?&menuFlag=huiyuan_ytoUser_list&type="+type;
	}
</script>
<script src="${jsPath}/common/common.js?d=${str:getVersion() }" type="text/javascript" ></script>
<!-- S 当前页面 JS --> 
<script type="text/javascript" src="${jsPath }/page/platform_new_subaccount.js?d=${str:getVersion() }"></script> 
<!-- E 当前页面 JS -->

<!-- S Content -->
  <div id="content">
    <div id="content_hd" class="clearfix">
      <h2 id="message_icon">新增业务账号</h2>
      <em>可以创建卖家，网点，平台三种类型客户！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em> </div>
    <div id="content_bd" class="clearfix">
      <div class="box box_a">
        <div class="box_bd">
            <% 
            	String type=request.getParameter("type");
                //out.println(type);
            	String c1="";
            	String c2="";
            	String c3="";
            	if("1".equals(type)){
            		c1="checked";
            	}
            	else if("2".equals(type)){
            		c2="checked";
            	}
            	else if("4".equals(type)){
            		c3="checked";
            	}
            %>
			<form action="ytoUser!addYtoUserAccount.action" method="post" class="form" id="new_acc_form">
				<input id="isValidate" value='${isValidate}' type="hidden">
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
                <p>
                    <label for="acc_id"><span class="req">*</span>账号类型：</label>
	           		<input name="user.userType" value='1' type="radio" <%=c1%> onclick="checkUserType(1)">卖家
	           		&nbsp;&nbsp;
	           		<input name="user.userType" value="2" type="radio" <%=c2%> onclick="checkUserType(2)">网点
	           		&nbsp;&nbsp;
	           		<input name="user.userType" value="4" type="radio" <%=c3%> onclick="checkUserType(4)">平台
                </p>
				<p>
					<label for="acc_id"><span class="req">*</span>登录账号：</label>
					<input type="text" id="acc_id" class="input_text"  name="user.userName" value=""/>
					<span id="acc_idTip"></span>
				</p>
				<p>
					<label for="init_psw"><span class="req">*</span>登录密码：</label>
					<input type="password" id="init_psw" class="input_text" name="user.userPassword" value=""/>
					<span id="init_pswTip"></span>
				</p>
				<p>
					<label for="real_name"><span class="req">*</span>真实姓名：</label>
					<input type="text" id="real_name" class="input_text" name="user.userNameText" />
					<span id="real_nameTip"></span>
				</p>
				<!-- 平台用户子账号 CustomerId 不可修改-->
				<% 
					if("1".equals(type)){
				%>
	                <p id="p_customerId">
	                    <label for="init_psw"><span class="req">*</span>CustomerID：</label>
	                  		<input name="user.clientId" value="${user.clientId }" type="hidden">
	                  		<input name="user.taobaoEncodeKey" id="taobaoEncodeKey" class="input_text"
	                       value='${user.taobaoEncodeKey }'>
	                       <span id="taobaoEncodeKeyTip"></span>
	                </p>
                <%
					}
                %>
				<p>
					<span class="btn btn_b"><input id="ok_btn" value="确 定" type="submit" /></span>
					<a href="javascript:;" id="back_btn" class="btn btn_b" title="返回"><span>返 回</span></a>
				</p>
			</form>
        </div>
      </div>
    </div>
  </div>
  <!-- E Content --> 