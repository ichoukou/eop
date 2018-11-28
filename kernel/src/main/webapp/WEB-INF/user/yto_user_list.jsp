<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- 当前页面JS -->
<script type="text/javascript" src="${jsPath}/page/yto_user_list.js?d=${str:getVersion() }"></script>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- 当前页面css -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/yto_user_list.css?d=${str:getVersion() }" media="all" />

	<script type="text/javascript">
		//全选/全不选
		function synCheckbox(obj, containerId) {
			//var obj = event.srcElement || event.target;	// 事件源对象 		
			$("#"+containerId).find(":checkbox").each(function() {
				$(this).attr("checked", obj.checked);
			});
		}
		
		//编辑易通用户
		function editYtoUser() {
			var userIdStr = "";
			var stateFlag = 0;
			var scrollTop = window.parent.document.documentElement.scrollTop;
			if(scrollTop==0){
			  	scrollTop = window.parent.document.body.scrollTop;
			  	scrollTop = scrollTop==0?100:scrollTop;
			}
			if ($("#tbTB_TBD :checkbox:checked").length == 0) {
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: '请先选择要操作的用户',
					yes: function() {
						aDialog.close();
					}
				});
				return;
			} else if ($("#tbTB_TBD :checkbox:checked").length > 1) {
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: '只能选择一个要操作的用户',
					yes: function() {
						aDialog.close();
					}
				});
				return;
			}
			$("#tbTB_TBD :checkbox:checked").each(function(){
				if(userIdStr != ''){
					userIdStr += ",";
				}
				userIdStr += $(this).val();
			});
			setTimeout(function(){
				window.location.href = "ytoUser!toEdit.action?userIdStr="+userIdStr+"&menuFlag=huiyuan_ytoUser_list";
			},0)
			
		}
		
		function toPage(cp){
			$("#currentPage").val(cp);
			$("#userFrom").submit();
		}
		
		// 更改用户状态
		function updateUserState(curUserId, userStatus) {
			$.ajax({
				type : "post",
				dataType : "json",
				data : "userIdStr=" + curUserId + "&user.userState=" + userStatus + "&menuFlag=huiyuan_ytoUser_list",
				cache: false,
				url : 'ytoUser!updateUserState.action',
				success:function(response){
					var aDialog = new Dialog();
					aDialog.init({
						contentHtml: response.infoContent,
						yes: function() {
							aDialog.close();
							if(response.status)
								window.location.reload();
						}
					});
				}
			});
		}
		
		// 初始化密码为123456
		function updatePasswordByUserId(curUserId) {
			$.ajax({
				type : "post",
				dataType : "json",
				data : "userIdStr=" + curUserId + "&menuFlag=huiyuan_ytoUser_list",
				cache: false,
				url : 'ytoUser!updatePasswordByUserId.action',
				success:function(response){
					var aDialog = new Dialog();
					aDialog.init({
						contentHtml: response.infoContent,
						yes: function() {
							aDialog.close();
							if(response.status)
								window.location.reload();
						}
					});
				}
			});
		}
		
	</script>


  <div id="content">
  	<div id="content_hd" class="clearfix">
<!-- 		<h2 id="message_icon">会员管理--易通会员信息</h2> -->
		<em>易通系统还处于试运行阶段，迫切需要您的宝贵建议！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>		
	</div>
	
	<div id="content_bd" class="clearfix">
		<div class="box box_a">
			<div class="box_bd">
				<div class="box box_a">
					<div id="box_bd_b" class="box_bd" style="position:relative;">
						<s:form id="userFrom" action="ytoUser!list.action" method="post"  theme="simple" >
							<input type="hidden" id="currentPage" value="<s:property value='currentPage'/>" name="currentPage"/>
			  				<input type="hidden" id="url" value='<s:property value="url"/>'>
		      				<p>
								<span>会员帐号：</span>
								<input id="user" type="text" name="user.userName" class="input_text"  value="<s:property value='user.userName'/>">
								<span>创建时间：</span>
								<input class="Wdate" type="text" name="startTime" id="date_start" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true})" value="<s:property value='startTime'/>">　至　
								<input class="Wdate" type="text" name="endTime" id="date_end" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true,startDate:'#F{$dp.$D(\'date_start\')}',minDate:'#F{$dp.$D(\'date_start\')}'})" value="<s:property value='endTime'/>">
								<span id="date_tip"></span>
								<a href="javascript:;" id="sear_btn" title="查 询" class="btn btn_a" onclick="toPage(1);"><span>查 询</span></a>
		      					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="javascript:;" id="sear_btn" title="创建账号" class="btn btn_a" onclick="location.href='ytoUser!toAddYtoUserAccount.action?&menuFlag=huiyuan_ytoUser_list&type=1'"><span>创建账号</span></a>
		      				</p>
		      				<!-- 左侧菜单选中样式 -->
							<input type="hidden" name="menuFlag" value="${menuFlag }" />
						</s:form>
					</div>
				</div>

			<div class="table">
				<table id="tbTB">
					<thead>
						<tr id="tbTH">
							<th class="th_a"><div class="th_title"><em><input type="checkbox" onclick="synCheckbox(this, 'tbTB_TBD')" title="全选/全不选" /></em></div></th>
							<th class="th_b"><div class="th_title"><em>会员账号</em></div></th>
							<th class="th_c"><div class="th_title"><em>编码</em></div></th>
							<th class="th_d"><div class="th_title"><em>手机号码</em></div></th>
							<th class="th_e"><div class="th_title"><em>固定电话</em></div></th>
							<th class="th_f"><div class="th_title"><em>邮箱地址</em></div></th>
							<th class="th_g"><div class="th_title"><em>发货地址</em></div></th>
							<th width="80" class="th_h"><div class="th_title"><em>创建时间</em></div></th>
							<th width="60" class="th_i"><div class="th_title"><em>状态</em></div></th>
							<th width="80" class="th_j"><div class="th_title"><em>操作</em></div></th>
						</tr>
					</thead>
					<tbody id="tbTB_TBD">
						<s:iterator value="#request.userList" status="stuts" var="usr">
							<tr>
								<td class="th_a"><input type="checkbox" value="<s:property value='#usr.id'/>"  state="<s:property value='#usr.userState'/>"/></td>
								<td class="th_b"><s:property value="#usr.userName"/></td>
								<td class="th_c"><s:property value="#usr.userCode"/></td>
								<td class="th_d"><s:property value="#usr.mobilePhone"/></td>
								<td class="th_e"><s:property value="#usr.telePhone"/></td>
								<td class="th_f"><s:property value="#usr.mail"/></td>
								<td class="th_g"><s:property value="#usr.addressProvince"/><s:property value="#usr.addressCity"/><s:property value="#usr.addressDistrict"/><s:property value="#usr.addressStreet"/></td>
								<td class="th_h"><s:date name="createTime" format="yyyy-MM-dd"/></td>
								<td class="th_i">
									<s:if test="#usr.userState == 1">活动 </s:if>
									<s:elseif test="#usr.userState == 'TBA'">未激活</s:elseif>
									<s:elseif test="#usr.userState == 0">禁用</s:elseif>
								</td>
								<td class="th_j">
									<s:if test="#usr.userState == 1"><a href="javascript:updateUserState(${usr.id}, 0);">禁用</a> </s:if>
									<s:elseif test="#usr.userState == 2"><a href="javascript:updateUserState(${usr.id}, 1);">激活</a></s:elseif>
									<s:elseif test="#usr.userState == 0"><a href="javascript:updateUserState(${usr.id}, 1);">激活</a></s:elseif><br>
									<a href="javascript:updatePasswordByUserId(${usr.id});">&nbsp;密码重置</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
			<div class="table_footer clearfix">
				<div class="print_box">								
					<a href="javascript:;" id="btn_del" class="del_btn btn btn_a" title="编辑易通用户" onclick="editYtoUser();"><span>编辑易通用户</span></a>									
				</div>
								
				<div class="pagenavi">
					<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
				</div>
			</div>
			</div>	
		</div>
	</div>
</div>	