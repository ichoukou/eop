<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/my_clients.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/clipboard/zeroClipboard-1.0.7.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/my_clients.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->

<script type="text/javascript">
	var params = {
		site:"${yto:getCookie('site')}",
		getCodeUrl: "user!generateUserCode.action",    // 生成客户编码 url
		delCodeUrl: "user!delUserCode.action",                // 删除客户编码 url
		unbindClientUrl: "user!list.action?type=0&menuFlag=my_custom&userState=TBA"       // 未绑定页面 url
	};
</script>

<title>我的客户</title>

<!-- S Content -->
<div id="content">
	<div id="content_hd" class="clearfix">
		<h2 id="message_icon">我的客户</h2>
		<em>助您更好的管理客户！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a>
		</em>
	</div>
	<div id="content_bd" class="clearfix">

		<!-- S Box -->
		<div class="box box_a">
			<div class="box_bd">
				<form id="userFrom" action="user!list.action" method="post">
				  <input type="hidden" name="menuFlag" value="${menuFlag }" />
				  <input type="hidden" id="url" value='user!list.action?1=1'>
			      <p>
					<span>客户编码：</span> <input type="text" id="userCode" class="input_text" name="userCode" value="<s:property value='userCode'/>" /> 
					<span>客户名称：</span> <input type="text" id="userName" class="input_text" name="userName" value="<s:property value='userName'/>" />
					<span>绑定状态：</span><select id="userState" name="userState">
										   		<option value="1" <s:if test="userState=='1'">selected</s:if>>已绑定</option>
										   		<option value="TBA" <s:if test="userState=='TBA'">selected</s:if>>未绑定</option>
										   </select>
					<input id="currentPage" value="<s:property value='currentPage'/>" name="currentPage" type="hidden"/>
					<a href="javascript:;" id="search_btn" class="btn btn_a" title="查 询"><span>查询</span></a>
					<s:if test="userState!='TBA'">
						<a href="#" id="send_message" class="btn btn_a" title="发消息"><span>发消息</span></a>
					</s:if>
					<c:if test="${str:isEmpty(yto:getCookie('parentId')) }">
						<a href="javascript:;" id="add_cust_btn" class="btn btn_a" title="新增客户"><span>新增客户</span></a>
            		</c:if>
			      </p>
				</form>
			
			</div>
			<div class="box_bd table_box">
				<!-- S Table -->
				<div id="market_table" class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a">
									<div class="th_title all_checkbox">
										<input type="checkbox" class="input_checkbox checked_all" name="range" />
									</div>
								</th>
								<th class="th_b">
									<div class="th_title">
										<em>客户编码</em>
									</div></th>
								<th class="th_c">
									<div class="th_title">
										<em>客户名称</em>
									</div></th>
								<th class="th_d">
									<div class="th_title">
										<em>已激活帐号</em>
									</div></th>
								<th class="th_e">
									<div class="th_title">
										<em>固定电话</em>
									</div></th>
								<th class="th_f">
									<div class="th_title">
										<em>手机号码</em>
									</div></th>
								<th class="th_g">
									<div class="th_title">
										<em>地址详情</em>
									</div></th>
								<th class="th_h">
									<div class="th_title">
										<em>开启电子账单</em>
									</div></th>
								<th class="th_i">
									<div class="th_title">
										<em>操作</em>
									</div></th>
							</tr>
						</thead>

						<tbody>
						<input type="hidden" name="currentPage" id="currentPage" value='<s:property value="currentPage" />'/>
						<s:iterator value="userBeanList" status="stuts" var="user">
							<tr>
								<td class="td_a">
									<input id="msgCheck" class="msgCheckForMessage" type="checkbox" value="<s:property value='userCode'/>" val="<s:property value="userName"/>"/>
								</td>
								<td class="td_b"><s:property value="userCode"/></td>
								<td class="td_c"><p><s:property value="userName"/></p>
								<input name="usercode" class="usercode" value="<s:property value='userCode'/>" type="hidden" />
								<input name="userName" class="username" value="<s:property value='userName'/>" type="hidden">
								<span><a href="javascript:;" class="edit_name">修改</a></span>
								</td>
								<td class="td_d">
									<s:iterator value="loginName" var="varLogin">
										<s:property value="varLogin"/><br>
									</s:iterator>
								</td>
								<td class="td_e"><s:property value="phone"/></td>
								<td class="td_f"><s:property value="telephone"/></td>
								<td class="td_g"><s:property value="address"/></td>
								<td class="td_h">
									<s:if test="#user.isContractUserFlg == 'isContractUser' ">
										<input type="checkbox" id="eccount" disabled="disabled" <s:if test="switchEccount==0"> checked="checked" </s:if> value="<s:property value='userThreadId'/>" /><font color='#AAAAAA'>&nbsp;电子对账</font>
									</s:if>
									<s:else>
										<input type="checkbox" id="eccount" <s:if test="switchEccount==0"> checked="checked" </s:if> value="<s:property value='userThreadId'/>" />&nbsp;电子对账
									</s:else>
									<input type="hidden" id="userNameHid" value="<s:property value="userName"/>" />
								</td>
								<td class="td_i">
									<s:if test="#user.isContractUserFlg == 'isContractUser' ">
										<!--font color='#AAAAAA'>管理运费模板</font-->
<%-- 										<a title="管理运费模板" class="btn btn_e disable" href="javascript:;"><span>管理运费模板</span></a> --%>
										该客户已分配给承包区
										<s:if test="userState =='TBA'">
											<s:if test="userCode.toUpperCase().startsWith('YT')">
												<a title="删除" val="<s:property value='userThreadId'/>" class="btn btn_d delPTByVip" href="javascript:;"><span>删除</span></a>
											</s:if>
										</s:if>
										
									</s:if>
									<s:else>
										<!--a href="javascript:;" class="viewPTByVip" target="<s:property value="userThreadId"/>" style='text-decoration:underline;'>管理运费模板</a-->
										<s:if test="userState ==1">
											<a title="管理运费模板" class="btn btn_d viewPTByVip" val="<s:property value='userThreadId'/>" href="javascript:;"><span>管理运费模板</span></a>
										</s:if>
										<s:if test="userState =='TBA'">
											<s:if test="userCode.toUpperCase().startsWith('YT')">
												<a title="删除" val="<s:property value='userThreadId'/>" class="btn btn_d delPTByVip" href="javascript:;"><span>删除</span></a>
											</s:if>
										</s:if>
										
									</s:else>
									
								</td>
					        </tr>
				         </s:iterator>
							
						</tbody>
					</table>
					
					<!-- S PageNavi -->
						<div class="pagenavi">
							<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
						</div>
					<!-- E PageNavi -->
				</div>
				<!-- E Table -->


				

			</div>
		</div>
		<!-- E Box -->

	</div>

</div>
<!-- E Content -->

