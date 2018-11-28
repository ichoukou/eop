<%--
	@author ChenRen
	@date 	2011-09-07
	@description
		运费模板列表页
			入口： 电子对账/VIP账单-运费模板(order!toPosttemp.action)
			
			页面显示模板列表(是否翻页待定)
			提供模板 [新增、修改、删除] 操作链接
			
			VIP用户和网点都可以查看，如果使用同一个页面显示数据要控制权限 	// 页面和后台一起验证操作权限

--%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto"%>

<title>运费模板管理</title>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/freightmb.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>

<script type="text/javascript">
		$(function(){
			/* parent.reinitIframe();
			var iframe = parent.$("#main");
			$("#main").height(iframe.height()); */
			$("#tbTB_TBD tr")
				.mouseover(function() {$(this).css("background-color", "#ECF0F2");})
				.mouseout(function() {$(this).css("background-color", "#FFFFFF");});
			
			pagination.live('click',function(ev){
				var page = $(this).attr("value");
				setTimeout(function(){
					window.location.href='posttemp!toPosttemp.action?currentPage='+page;
				},0);
			});
			
			//点击“新增”
			$('.newPosttemp').live('click',function(){
				setTimeout(function(){
					window.location.href = "posttemp!toPosttempAdd.action?menuFlag=caiwu_posttemp";
				},0);
			});
			//点击“返回”
			$('.returnBack').live('click',function(){
				setTimeout(function(){
					history.back();
				},0);
			});
			
		});
		
</script>
<script type="text/javascript">
var params = {
		<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
			userType : 1,
		</c:if>
		<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
			userType : 2,
		</c:if>
		<c:if test="${yto:getCookie('userType') == 4 || yto:getCookie('userType') == 41}">
			userType : 3,
		</c:if>
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		showBindCode:true,
		userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
}
</script>
<div id="content">
	<div id="content_hd" class="clearfix">
<!-- 		<h2 id="message_icon">运费模板</h2> -->
<!-- 		<em>便捷、安全、可靠的消息功能，助您解决各种业务问题！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a> -->
<!-- 		</em> -->
<!-- 		<p>客户不同，价格也不同，透过运费设置为每个客户设置不同运费标准，轻松实现个性化电子对账！</p> -->
	</div>
	<div id="content_bd" class="clearfix">
		<div class="topbtn clearfix">
			<a href="javascript:;" class="btn btn_a returnBack" title="返回" ><span>返回</span></a>
		    <c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
				<a href="javascript:;" class="btn btn_a newPosttemp" title="新 增" ><span>新增</span></a> 
			</c:if>
		</div>

		<!-- S Table -->
		<div class="table">
			<table>
				<thead>
					<tr>
						<th class="th_a">
							<div class="th_title">
								<em>模板名称</em>
							</div></th>
						<c:if test="${yto:getCookie('userType') == 4}">	
						<th class="th_a">
							<div class="th_title">
								<em>分仓账号</em>
							</div></th>
						<th class="th_c">
							<div class="th_title">
								<em>始发地</em>
							</div></th>
						</c:if>	
						<%-- 网点用户显示其下直客的真实名称--%>
			        	<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
				        	<th class="th_a">
								<div class="th_title">
									<em>账户名称 </em>
								</div></th>
							<th class="th_c">
								<div class="th_title">
									<em>始发地</em>
								</div></th>
			        	</c:if>
						<th class="th_b">
							<div class="th_title">
								<em>修改时间</em>
							</div></th>
						<c:if test="${yto:getCookie('userType') != 1 && yto:getCookie('userType') != 12 }">
							<th class="th_d">
								<div class="th_title">
									<em>备注</em>
								</div></th>
						</c:if> 
							
					</tr>
				</thead>

				<tbody>
				<input type="hidden" name="currentPage" id="currentPage" value='<s:property value="currentPage" />' />
				<s:iterator value="postList" var="posttemp">
					<tr>
							<td class="td_a">
								<c:choose>
								<c:when test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
									<s:if test="ptType != 1">
										<!-- !系统模板 -->
										<a  title="点击编辑" href="javascript:location.href='posttemp!toPosttempEdit.action?menuFlag=caiwu_posttemp&posttemp.id=<s:property value='id'/>'"><s:property value="ptName"/></a>									</s:if>
									<s:else>
										<a  title="点击查看详情" href="javascript:location.href='posttemp!toPosttempView.action?menuFlag=caiwu_posttemp&posttemp.id=<s:property value='id'/>'"><s:property value="ptName"/></a>									</s:else>
								</c:when>
								<c:otherwise>
									<s:if test="ptName=='账号未激活'">
											<font color='red'><s:property value="ptName"/></font>
									</s:if>
									<s:else>
										<a  title="点击查看详情" href="javascript:location.href='posttemp!toPosttempView.action?menuFlag=caiwu_posttemp&posttemp.id=<s:property value='id'/>&posttemp.vipIds=<s:property value='vipIds'/>'"><s:property value="ptName"/></a>
									</s:else>
								</c:otherwise>
								</c:choose>
							</td>
							<td class="td_b"><s:property value="#posttemp.vipText"/></td>
							<%-- 平台用户显示其下的所有绑定了运费模板的分仓账户 --%>
							<c:if test="${yto:getCookie('userType') == 4}">	
								<td class="td_a"><s:property value="#posttemp.ptGround"/></td>
							</c:if>
							<%-- 网点用户显示其下直客的真实名称 --%>
							<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
								<td class="td_c"><s:property value="user.addressProvince"/></td>
							</c:if>
								
							<td class="td_b"><s:property value="updateTime.substring(0,updateTime.length()-2)"/></td>
							<td style="word-break: break-all;line-height:24px;" class="td_d">
				        	<c:if test="${yto:getCookie('userType') != 1 && yto:getCookie('userType') != 12}">
								<s:property value="#posttemp.remark"/>
							</c:if>
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
<!-- E Content -->