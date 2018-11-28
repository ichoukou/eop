<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<!-- 当前页面js -->
<script type="text/javascript" src="${jsPath}/page/eop_user_list.js?d=${str:getVersion() }"></script>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- 当前页面css -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/eop_user_list.css?d=${str:getVersion() }" media="all" />

	<script type="text/javascript">
		function synCheckbox(obj, containerId) {
			//var obj = event.srcElement || event.target;	// 事件源对象 		
			$("#"+containerId).find(":checkbox").each(function() {
				$(this).attr("checked", obj.checked);
			});
		}
		
		//禁用、还原用户
		function editUser(state) {
			var userIdStr = "";
			var stateMsg = state == 0 ? "只能禁用“活动”状态的用户" : "只能还原“禁用”状态的用户";
			var stateFlag = 0;
			var scrollTop = window.parent.document.documentElement.scrollTop;
			if(scrollTop==0){
			  	scrollTop = window.parent.document.body.scrollTop;
			  	scrollTop = scrollTop==0?100:scrollTop;
			}
			$("#tbTB_TBD :checkbox:checked").each(function(){
				if($(this).attr("state") != (state == 0 ? 1 : 0)){
					stateFlag++;
				}
				if(userIdStr != ''){
					userIdStr += ",";
				}
				userIdStr += $(this).val();
			});
			if(stateFlag > 0){
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: stateMsg,
					yes: function() {
						aDialog.close();
					}
				});
				return;
			}
			if(userIdStr == ''){
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: "请先选择要操作的用户",
					yes: function() {
						aDialog.close();
					}
				});
				return;
			}
			$.ajax({
				type : "post",
				dataType : "json",
				data : "user.remark="+userIdStr+"&user.userState="+state + "&menuFlag=huiyuan_eopUser_list",
				cache: false,
				url : 'eopUser!updateState.action',
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
		
		function toPage(cp){
			$("#currentPage").val(cp);
			$("#userFrom").submit();
		}
		
		function sendMsg() {
			var obj = $("#tbTB_TBD :checkbox:checked"), num = obj.size(),  msg = "请至少选择一个用户!", names = "";
			if(num < 1){
				var scrollTop = window.parent.document.documentElement.scrollTop;
				if(scrollTop==0){
				  	scrollTop = window.parent.document.body.scrollTop;
				  	scrollTop = scrollTop==0?100:scrollTop;
				}
				var aDialog = new Dialog();
				aDialog.init({
					contentHtml: msg,
					yes: function() {
						aDialog.close();
					}
				});
				return;
			} 
			
			$(obj).each(function() {
				names += "," + this.value;
			});
			names = names.substring(1);
			setTimeout(function(){
				window.location.href = "send_openUI.action?menuFlag=msg_send";
			},0)
		}
		
		function openMessage(){
			parent.main.location = "sendMessage_openAdviseUI.action";
			parent.side.layerLight("advise_li");
		}
		
	</script>


	<div id="content">
		<div id="content_hd" class="clearfix">
<!--   			<h2 id="message_icon">会员管理--EOP会员信息</h2> -->
			<em>易通系统还处于试运行阶段，迫切需要您的宝贵建议！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>
		</div>
		<div id="content_bd" class="clearfix">
			<div class="box box_a">
				<div class="box_bd">
					<div class="box box_a">
						<div id="box_bd_b" class="box_bd" style="position:relative;">
							<s:form id="userFrom" action="eopUser!list.action" method="post"  theme="simple" >
			  					<input type="hidden" id="url" value='<s:property value="url"/>'>
			  					<input type="hidden" id="currentPage" value="<s:property value='currentPage'/>" name="currentPage"/>
		      					<p>
									<span>创建时间：</span>
									<input class="Wdate" type="text" name="startTime" id="starttime" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true})" value="<s:property value='startTime'/>">　至　
									<input class="Wdate" type="text" name="endTime" id="endtime" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true,startDate:'#F{$dp.$D(\'starttime\')}',minDate:'#F{$dp.$D(\'starttime\')}'})" value="<s:property value='endTime'/>">
									<select id="userType" name="user.userType" style="height:26px;">
										<option <s:if test="%{user.userType == -1}">selected</s:if> value="-1">所有会员类型</option>
										<option <s:if test="%{user.userType == 1}">selected</s:if> value="1">电子商务服务平台</option>
										<option <s:if test="%{user.userType == 2}">selected</s:if> value="2">软件服务商</option>
										<option <s:if test="%{user.userType == 3}">selected</s:if> value="3">个人开发者</option>
<!-- 									<option <s:if test="%{user.userType == 4}">selected</s:if> value="4">卖家</option> -->
<!-- 									<option <s:if test="%{user.userType == 5}">selected</s:if> value="5">网点</option> -->
									</select>
									<select id="userState" name="user.userState" style="height:26px;">
										<option <s:if test="%{user.userState == -1}">selected</s:if> value="-1">所有状态</option>
										<option <s:if test="%{user.userState == 0}">selected</s:if> value="0">禁用</option>
										<option <s:if test="%{user.userState == 1}">selected</s:if> value="1">活动</option>
										<option <s:if test="%{user.userState == 2}">selected</s:if> value="2">未激活</option>
									</select>
									<a href="javascript:;" id="sear_btn" title="查 询" class="btn btn_a" onclick="toPage(1);"><span>查 询</span></a>
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
			        				<th><div class="th_title"><em><input type="checkbox" onclick="synCheckbox(this, 'tbTB_TBD')" title="全选/全不选" /></em></div></th>
			        				<th><div class="th_title"><em>会员名</em></div></th>
			        				<th><div class="th_title"><em>会员类型</em></div></th>
									<th><div class="th_title"><em>真实姓名</em></div></th>
									<th><div class="th_title"><em>注册时间</em></div></th>
									<th><div class="th_title"><em>联系方式</em></div></th>
									<th><div class="th_title"><em>公司名称</em></div></th>
									<th><div class="th_title"><em>状态</em></div></th>
			        			</tr>
			    			</thead>
			    			<tbody id="tbTB_TBD">
			        			<s:iterator value="#request.userList" status="stuts" var="usr">
									<tr>
										<td><input type="checkbox" value="<s:property value='#usr.id'/>"  state="<s:property value='#usr.userState'/>"/></td>
										<td><a href="eopUser!view.action?user.id=<s:property value='#usr.id'/>&menuFlag=huiyuan_eopUser_list"><s:property value="#usr.userName"/></a></td>
										<td>
											<s:if test="#usr.appProvider.servicesType == 1">电子商务服务平台</s:if>
											<s:elseif test="#usr.appProvider.servicesType == 2">软件服务商</s:elseif>
											<s:elseif test="#usr.appProvider.servicesType == 3">个人开发者</s:elseif>
										</td>
										<td><s:property value="#usr.userNameText"/></td>
										<td><s:date name="createTime" format="yyyy-MM-dd"/></td>
										<td><s:property value="#usr.mobilePhone"/></td>
										<td><s:property value="#usr.appProvider.companyName"/></td>
										<td>
											<s:if test="#usr.userState == 1">活动</s:if>
											<s:elseif test="#usr.userState == 2">未激活</s:elseif>
											<s:elseif test="#usr.userState == 0">禁用</s:elseif>
										</td>
				        			</tr>
			        			</s:iterator>
			    			</tbody>
						</table>
						<div class="table_footer clearfix">
							<div class="print_box">								
								<a href="javascript:;" id="btn_send" class="btn btn_a" title="发送消息" onclick="sendMsg();"><span>发送消息</span></a>
								<a href="javascript:;" id="btn_disable" class="btn btn_a" title="禁用会员" onclick="editUser(0);"><span>禁用会员</span></a>	
								<a href="javascript:;" id="btn_revert" class="btn btn_a" title="还原会员" onclick="editUser(1);"><span>还原会员</span></a>										
							</div>
								
							<div class="pagenavi">
								<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
							</div>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</div>
