<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/delivery.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->

<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">我要发货</h2> -->
				<em>在线下单，方便快捷</em>	
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S Tab -->
				<div class="tab tab_a">
					<div class="tab_triggers">
						<ul>
							<li><a href="javascript:;" class="sendTab">我要发货</a></li>
							<li><a href="javascript:;" class="recordTab">发货记录</a></li>
						</ul>
					</div>
					
					<div class="tab_panels">
						<div id="tab_panel_a" class="tab_panel">
							<form action="orderPlaceSubmit_orderCreate.action" method="post" id="send_form" name="send_form" class="form">
								<input type="hidden" name="currentUserId" id="currentUserId" value="<s:property value="currentUserId"/>" />
								<input value='<s:property value="processResult"/>' type="hidden" id="processResultValue"/>
								
								<div class="tab_panel_c pad1">
									<h5>温馨提示</h5>
									<ol>
										<li>在淘宝“推荐物流”和“自己联系物流”发货的订单，无需在这里重新发货</li>
										<li>补货，换货在这里填写信息并确认发货→就近网点联系您并确认发货信息<span class="fahuo">这里还可以<a href="orderImport_toOrderImoprt.action?menuFlag=fahuo_orderimport">批量发货</a>哦</span></li>					
									</ol>
								</div>
								<div class="tab_panel_c">
									<h5>发货信息</h5>
									<input type="hidden" id="tempId" value="<s:property value='tempId'/>">
									<p>
										<label>发货人：</label>
										<em>
										<s:property value='senderName'/>
										</em>
										<input name="senderName" id="senderName" value="<s:property value='senderName'/>" type="hidden">
										<span><a href="javascript:;" class="edit_name">修改</a></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>senderName</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label>手机号码：</label>
										<em>
										<s:property value='senderMobile'/>
										</em>
										<input name="senderMobile" id="senderMobile" value="<s:property value='senderMobile'/>" type="hidden">
										<span><a href="javascript:;" class="edit_mobile">修改</a></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>senderMobile</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label>发货地址：</label>
										<em class='modifyEm'>
										<span class="modifyAddressHid"><s:property value='senderAddress'/></span>&nbsp;
										</em>
										<a href="#" id="edit_add">修改地址</a>
										<input name="senderAddress" id='senderAddress' value="<s:property value='senderAddress'/>" type="hidden">
									</p>
									
									<div  id='modifyAddress' style="display: none">
									  <p>
										 <select id="area2"></select>
										 <span id="area_tip2"></span></p>
									  <p>
						              	<textarea id="senderChangeAddress" class="textarea_text" style="height:70px;width:278px;" name="senderChangeAddress" onFocus='if (this.value == "街道/路/详细地址") this.value=""' value='街道/路/详细地址' style="width:120px;" ><s:property value='senderDetail'/></textarea>
						              	<span id="address_detailTip2"></span></p>
						              <p>
						              	<a class="btn btn_a" value="保存" id="save_add"><span>保存</span></a>&nbsp;<a class="btn btn_a" value="取消" id="cancel_add"><span>取消</span></a></p>
						           
						           		<input type="hidden" name="senderProv" id="x_prov2" value='<s:property value="senderProv"/>'/>
										<input type="hidden" name="senderCity" id="x_city2" value='<s:property value="senderCity"/>'/>
										<input type="hidden" name="senderDistrict" id="x_district2" value='<s:property value="senderDistrict"/>'/>
        								<input type="hidden" name="senderDetail" id='address_detail2' value="<s:property value='senderDetail'/>" >
						           </div>
								</div>
								
								<div class="tab_panel_c pad1">
									<h5>收货信息</h5>
									<p>
										<label for="contact" class="form_l"><span class="req">*</span>收货联系人：</label>
										<input class="input_text" name="receiverName" id="contact" type="text" <s:if test="processResult==0"> value='<s:property value="receiverName"/>'</s:if> >
										<span id="contactTip"></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>receiverName</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="waybill" class="form_l">运单号：</label>
										<input class="input_text yundanhao" name="mailNo" maxlength="10" validType="mailCombination" id="waybill" type="text"  <s:if test="processResult==0">value='<s:property value="mailNo"/>'</s:if> >
										<span id="waybillTip"></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>mailNo</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label class="form_l"><span class="req">*</span>收货地址：</label>
										<select id="area" subId="selCity" inputId="x_prov"></select>
										<span id="area_tip"></span>
										<input type="hidden" name="receiverProv" id="x_prov" value='<s:property value="prov"/>'/>
										<input type="hidden" name="receiverCity" id="x_city" value='<s:property value="city"/>'/>
										<input type="hidden" name="receiverDistrict" id="x_district" value='<s:property value="district"/>'/>
										
									</p>
									<p>
										<textarea name="" id="address_detail" name="receiverAddress" class="textarea_text" cols="51" rows="2"></textarea>
										<span id="address_detailTip"></span>
										<input type="hidden" id="receiverAddressHid" name="receiverAddress" value="<s:property value='receiverAddress' />" />
									</p>
									<p>
										<label for="mobile" class="form_l"><span class="req">*</span>手机号码：</label>
										<input class="input_text" maxlength="11" name="receiverMobile" id="mobile" type="text" validType="mobileCheck"   <s:if test="processResult==0">value='<s:property value="receiverMobile"/>'</s:if> >
										<span id="mobileTip"></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>receiverMobile</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="fixed1" class="form_l"><span class="req">*</span>固定电话：</label>
										<input class="input_text" validType="numberType" maxlength="4" name="receiverPhone1"  id="fixed1" type="text" <s:if test="processResult==0">value='<s:property value="receiverPhone1"/>'</s:if>  > - 
								        <input class="input_text" validType="numberType"  maxlength="8" name="receiverPhone2" id="fixed2" type="text" <s:if test="processResult==0">value='<s:property value="receiverPhone2"/>'</s:if>  > - 
								        <input class="input_text" validType="telExNumberType" maxlength="5" name="telExtCode" id="fixed3" <s:if test="processResult==0">value='<s:property value="telExtCode"/>'</s:if> >
										<span id="telTip"></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>receiverPhone1</s:param>							
										</s:fielderror>
										<s:fielderror cssStyle="color:red;">
											<s:param>receiverPhone2</s:param>							
										</s:fielderror>
									</p>
								</div>
								
								<div class="tab_panel_c pad1 tab_last">
									<h5>货物信息</h5>
									<p>
										<label for="cart_name" class="form_l"><span class="req">*</span>货物名称：</label>
										<input class="input_text" maxlength=200 name="itemName" id="cart_name" type="text" style="width:100px"   <s:if test="processResult==0">value='<s:property value="itemName"/>'</s:if> > 
										<a id="ban_count" >禁运品说明</a>
										<span id="cart_nameTip"></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>itemName</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="quantity" class="form_l">货物数量：</label>
										<input id="quantity" class="input_text" maxLength="5" name="itemNumber" type="text"  <s:if test="processResult==0">value='<s:property value="itemNumber"/>'</s:if> >
										<span id="quantityTip"></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>itemNumber</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="weight" class="form_l">货物重量：</label>
										<input class="input_text" name="itemWeight" maxlength="10" id="weight" type="text" <s:if test="processResult==0">value='<s:property value="itemWeight"/>'</s:if> > Kg 
										<span id="weightTip"></span>
										<s:fielderror cssStyle="color:red;">
											<s:param>itemWeight</s:param>							
										</s:fielderror>
									</p>
									<p>
										<label for="attention" class="form_l">注意事项：</label>
										<textarea id="attention" class="textarea_text" cols="51" rows="2" name="remark"><s:if test="processResult==0"><s:property value="remark"/></s:if></textarea>
										<span id="attentionTip"></span>
									</p>
									<p>
										<label for="start" class="form_l"><span class="req">*</span>预约取货时间：</label>
										<input type="text" id="start" class="Wdate" name="sendTimeStart" value="<s:property value='sendTimeStart' />" /> 
										至 
										<input type="text" id="end" class="Wdate" name="sendTimeEnd" value="<s:property value='sendTimeEnd' />" />
										<span id="dateTip"></span>
									</p>
									<p class="p_mar">
										<a href="javascript:;" id="send_btn" class="btn btn_b" title="确认发货"><span>确认发货</span></a>
									</p>
								</div>
							</form>
						</div>
						<div id="tab_panel_b" class="tab_panel" style="display:none;">
						<!-- S Box -->
						<div class="box box_a">
							<div class="box_bd">
								<form id="record_form" class="form" action="orderPlace!toMailNoBind.action" method="post">
									<input type="hidden" name="menuFlag" value="${menuFlag }" />
									<input type="hidden" class="checkTab" name="checkTab" value="<s:property value='checkTab'/>" />
									<p>
										<label for="date_start">发货日期：</label>
										<input type="text" id="date_start" class="Wdate" /> 
										至 
										<input type="text" id="date_end" class="Wdate" /> 
										<span id="date_tip"></span>
									</p>
									<p>
										<label for="consignee">收货人：</label>
										<input type="text" id="consignee" class="input_text" value="<s:property value='receiverName'/>" />
										<a href="javascript:;" id="sear_btn" class="btn btn_a" title="查 询"><span>查 询</span></a>
										<span id="consigneeTip"></span>
									</p>
								</form>
							</div>
						</div>
						<!-- E Box -->
						
						<!-- S Table -->
				<div class="table">
					<table>
						<thead>
							<tr>
								<th class="th_a">
									<div class="th_title"><em>运单号</em></div>
								</th>
								<th class="th_b">
									<div class="th_title"><em>状态</em></div>
								</th>
								<th class="th_c">
									<div class="th_title"><em>下单时间</em></div>
								</th>
								<th class="th_d">
									<div class="th_title"><em>收货人</em></div>
								</th>
								<th class="th_e">
									<div class="th_title"><em>收件地址</em></div>
								</th>
								<th class="th_f">
									<div class="th_title"><em>电话</em></div>
								</th>
								<th class="th_g">
									<div class="th_title"><em>操作</em></div>
								</th>
							</tr>
						</thead>
						
						<tbody>
						  <form action="bindedOrUpd!bindedOrUpdateMailNo.action" method="post" id="recordForm" name="recordForm" >
						    <input type="hidden" name="menuFlag" value="${menuFlag }" />
      					    <input type="hidden" name="currentPage" id="currentPage" value='<s:property value="currentPage" />'/>
      					    <input type="hidden" class="checkTab" name="checkTab" value="<s:property value='checkTab'/>" />
      					    <s:if test="orderFormList.size==0"><td colspan="7" align="center">对不起,找不到您要查询的记录</td></s:if>
      					    <s:iterator value="orderFormList" id="orderForm" status="st">
      					    <tr  style="cursor:pointer;">
					          <input type="hidden" class="ordId" id="id<s:property value="#st.index"/>" name="id<s:property value="#st.index"/>" value="<s:property value='id'/>"/>
					          <input type="hidden" class="txLogisticId" id="txLogisticId<s:property value="#st.index"/>" name="txLogisticId<s:property value="#st.index"/>" value="<s:property value='txLogisticId'/>"/>
					          <input type="hidden" class="clientId" id="clientId<s:property value="#st.index"/>" name="clientId<s:property value="#st.index"/>" value="<s:property value='clientId'/>"/>
					          <input type="hidden" class="mailNo" id="mailNo<s:property value="#st.index"/>" name="mailNo<s:property value="#st.index"/>" value="<s:property value='mailNo'/>"/>
					          <td class="td_a">
					          <s:if test="%{mailNo == ''}">
					            <input type="text" class="input_text" value="" maxlength="10" size="10" /><a href="#" class="fs02">保存</a>
					          </s:if>
					          <s:else>
					             <a href="#" class="mailno" val="<s:property value='mailNo'/>"><s:property value='mailNo'/></a><a href="#" class="fs01">编辑</a>
					          </s:else>
					          	 <input type="hidden" value="<s:property value="#st.index"/>" class="index" />
					          </td>
					          <td class="td_b" onClick="ctab('detail_<s:property value='remark'/>');">
								<s:if test="%{serviceType == 0}">
						   			<s:if test="%{lineType == 0}">接单中</s:if>
						   			<s:elseif test="%{lineType == 1}">揽收中</s:elseif>
								</s:if>
								<s:elseif test="%{serviceType == 'UPDATE'}">更新</s:elseif>
								<s:elseif test="%{serviceType == 'ACCEPT'}">接单</s:elseif>
								<s:elseif test="%{serviceType == 'UNACCEPT'}">不接单</s:elseif>
								<s:elseif test="%{serviceType == 'GOT'}">揽收成功</s:elseif>
								<s:elseif test="%{serviceType == 'NOT_SEND'}">揽收失败</s:elseif>
								<s:elseif test="%{serviceType == 'SENT_SCAN'}">派件扫描</s:elseif>
								<s:elseif test="%{serviceType == 'FAILED'}">失败</s:elseif>
								<s:elseif test="%{serviceType == 'SIGNED'}">已签收</s:elseif>
							  </td>
					          
					          <td class="td_c" onClick="ctab('detail_<s:property value='remark'/>');"><s:property value='receiver.createTimeStr'/></td>
					          <td class="td_d" onClick="ctab('detail_<s:property value='remark'/>');"><s:property value='receiver.name'/></td>
					          <td class="td_e" onClick="ctab('detail_<s:property value='remark'/>');"><s:property value='receiver.prov'/> <s:property value='receiver.city'/> <s:property value='receiver.district'/> <s:property value='receiver.address'/></td>
					          <td class="td_f" style="padding:0 !important;" align="center" onClick="ctab('detail_<s:property value='remark'/>');">
					              <s:if test="%{receiver.mobile != '' }" >
					              	  <s:property value='receiver.mobile'/><br/>
					              </s:if>
					              <s:if test="%{receiver.phone != '' }" >
					                  <s:property value='receiver.phone'/>
					              </s:if>
					          		
					          </td>
					          <td class="td_g">
					          	<a class="btn btn_a mailNo_cancel" value="撤&nbsp;销" /><span>撤&nbsp;销</span></a>
					          	<input type="hidden" value="<s:property value="startTime"/>" id="startHid<s:property value='#st.index'/>" class="startTime" />
					          	<input type="hidden" value="<s:property value="endTime"/>" id="endHid<s:property value='#st.index'/>" class="endTime" />
					          	<input type="hidden" value="<s:property value="receiverName"/>" id="nameHid<s:property value='#st.index'/>" class="receiverName" />
					          	<input type="hidden" value="<s:property value="#st.index"/>" class="index" />
					          </td>
					        </tr>
					        
					         <tr  id="detail_<s:property value='remark'/>" style="display:none;">
						          <td colspan="4" valign="top"><h2 style="font-size:15px;font-weight: bold">发件信息</h2>
						            <table border="0" id="lebo">
						              <tr>
						                <td width="73">店铺名称：</td>
						                <td width="170"><s:property value='shopName'/></td>
						              </tr>
						              <tr>
						                <td>发货地址：</td>
						                <td><s:property value='sender.prov'/> <s:property value='sender.city'/> <s:property value='sender.district'/> <s:property value='sender.address'/></td>
						              </tr>
						              <tr>
						                <td>联系方式：</td>
						                <td><s:property value='sender.phone'/> <s:property value='sender.mobile'/></td>
						              </tr>
						              <tr>
						                <td>发货日期：</td>
						                <td>20<s:property value='sender.partitionDate'/></td>
						              </tr>
						            </table></td>
						          <td colspan="3" valign="top"><h2 style="font-size:15px;font-weight:bold">收件信息</h2>
						            <table border="0" id="lebo">
						              <tr>
						                <td width="65" >商品名称：</td>
						                <td width="151" ><s:property value='product'/></td>
						              </tr>
						              <tr>
						                <td >商品数量：</td>
						                <td ><s:property value='num'/></td>
						              </tr>
						              <tr>
						                <td >联系方式：</td>
						                <td><s:property value='receiver.phone'/> <s:property value='receiver.mobile'/></td>
						              </tr>
						              <tr>
						                <td >邮政编码：</td>
						                <td ><s:property value='receiver.postCode'/></td>
						              </tr>
						            </table></td>
						        </tr>
							</s:iterator>
						  </form>
						  
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
				</div>
				<!-- E Tab -->
			</div>
			
<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action',// 获取对应网点 url		
		userType: "${yto:getCookie('userType')}",
		userState: "${yto:getCookie('userState')}",
		userField003:"${yto:getCookie('userField003')}",
		pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url		
	}
	
	function g(o){
		return document.getElementById(o);
		}
	
	function ctab(m){
		   if(g(m).style.display=='none'){   
			   $('#'+m).fadeIn('normal');
		   }else{
			   $('#'+m).fadeOut('normal');
		    }
		}

</script>
	
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/district-all.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/delivery.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
			
</div>
<!-- E Content -->
