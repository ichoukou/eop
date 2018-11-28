<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>

<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/order_import.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<!-- S Content --> 
<div id="content">
	<div id="content_hd" class="clearfix">
<!-- 		<h2 id="message_icon">订单导入</h2> -->
		<em>可导入多平台订单，实现所有订单统一管理~</em>
		<a href="noint1_audio.action?jsonResult=${str:startsWith(yto:getCookie('userType'),'2')?'site_11_orderimport':'vip_11_orderimport'}" class="btn btn_d" title="快速入门" target="_blank" >
		<span>快速入门</span>
		</a>
		<div id="per_show_box"><span>每页显示:</span>
		 	<form action="orderImport_toOrderImoprt.action" method="post" id="per_form">
				<select name="numOfPage" id="per_select">
					<option value="25" <s:if test="numOfPage == 25">selected</s:if>>25单</option>
					<option value="50" <s:if test="numOfPage == 50">selected</s:if>>50单</option>
					<option value="100" <s:if test="numOfPage == 100">selected</s:if>>100单</option>
					<option value="300" <s:if test="numOfPage == 300">selected</s:if>>300单</option>
				</select>
				<input type="hidden" name="menuFlag" value="${menuFlag }" />
			 </form>
		</div>
		<div class="clearfloat"></div>
		<p style="color:#333;font-size:13px;">下载表格模板→在拍拍、淘宝后台导出订单表→按照模板规范修改订单表→上传并导入
		</p>
		<input type="hidden" id="queryMessage" value="<s:property value='queryMessage'/>" name="queryMessage"/>
		<input type="hidden" id="uploadErrType" value="<s:property value='uploadErrType'/>" name="uploadErrType"/>
		<input type="hidden" id="uploadErrlist" value="<s:property value='uploadErrlist'/>" name="uploadErrlist"/>
		<input type="hidden" id="uploadListCount" value="<s:property value='uploadListCount'/>" name="uploadListCount"/>
		<input type="hidden" id="uploadOrderTemps" value="<s:property value='uploadOrderTemps'/>" name="uploadOrderTemps"/>
		<input type="hidden" id="notice" value="<s:property value='notice'/>" name="notice"/>
		<input type="hidden" id="flag" name="flag" value="<s:property value='flag'/>" />
 	</div>
	<div id="content_bd" class="clearfix">
		<!-- S Tab -->
		<div class="box box_a">		
			<!-- S 批量打印 tab -->
			<div  class="box_bd">
				<!-- S Box -->
				<div class="box box_a">
					<div id="box_bd_b" class="box_bd" style="position:relative;">
						<div class="prompt clearfix">
							<c:if test="${yto:getCookie('userType') == 1
	        					|| yto:getCookie('userType') == 11 
       							|| yto:getCookie('userType') == 12 
       							|| yto:getCookie('userType') == 13 }">
								<dt >温馨提示</dt> 
								<dd>
									<p>1.方便导入拍拍，阿里巴巴等其它平台的订单信息</p>
									<p>2.方便代发货供应商导入发货的订单信息</p>
								</dd>
							</c:if>
							<c:if test="${yto:getCookie('userType') == 2
	        					|| yto:getCookie('userType') == 21 
       							|| yto:getCookie('userType') == 22 
       							|| yto:getCookie('userType') == 23 }">
								<dt style="font-size:16px">经验表明：</dt> 
								<dd>
									<p style="font-size:16px">office 2003兼容性差，建议使用wps或office 2007来录单<a href="http://www.wps.cn/" target="_blank"><span>(点击下载wps)</span></a>。</p>
							</c:if>
						</div>
						<p>
							<a href="javascript:;" id="merger_orders_btn" class="btn btn_a" title="下载表格模板"><span>下载表格模板</span></a>→
							<a href="javascript:;" id="fill_out_btn" class="btn btn_a" title="批量上传"><span>批量上传</span></a>→
							<a href="javascript:;" id="merger_lead_btn" class="start_import_btn btn btn_a" title="订单导入"><span>一键导入</span></a>
							<c:if test="${yto:getCookie('userType') == 1
	        					|| yto:getCookie('userType') == 11 
       							|| yto:getCookie('userType') == 12 
       							|| yto:getCookie('userType') == 13 }">
								<span style="color:#1E569E;margin-left:10px;line-height:29px;display:inline-block;height:29px;vertical-align:middle">导入后，可以使用问题件管理，运单监控等服务</span>
							</c:if>
							<c:if test="${yto:getCookie('userType') == 2
	        					|| yto:getCookie('userType') == 21 
       							|| yto:getCookie('userType') == 22 
       							|| yto:getCookie('userType') == 23 }">
								<span style="color:#1E569E;margin-left:10px;line-height:29px;display:inline-block;height:29px;vertical-align:middle">导入后，十几分钟后同步到金刚，无需重复录单，同时可以使用问题件管理等服务</span>
							</c:if>
						</p>
					</div>
							
						<div id="box_bd_c" class="box_bd">	
							<a href="javascript:;" id="btn_del" class="del_btn btn btn_a" title="批量删除"><span>批量删除</span></a>
							<div id="orderby_box">
								<form id="sear_form_import" action="orderImport_searchOrderTemp.action" method="POST">
									<input type="text" name="mailNoOrOrderNo" class="input_text" id="search_txt" value="<s:property value='mailNoOrOrderNo'/>" />
									<a href="javascript:;" id="nub_search" class="btn btn_a" title="查询"><span>查询</span></a>
									<input type="hidden" name="menuFlag" value="${menuFlag }" />
									<input type="hidden" id="pageNm" value="<s:property value='numOfPage'/>" name="pageNm"/>
									<input type="hidden" id="curPage" value="<s:property value='currentPage'/>" name="currentPage"/>
								</form>	
							</div>
						</div>
					</div>
					<!-- E Box --> 
					<!-- S Table -->
					<div class="table"> 
						<table>
							<thead>
								<tr>
									<th width="36" class="th_a">
										<div class="th_title"><em><input type="checkbox" class="check_all" /></em></div>
									</th>
									<th width="100" class="th_b">
										<div class="th_title"><em><i class="fold_unfold"></i>运单号</em></div>
									</th>
									<th width="150" class="th_c">
										<div class="th_title"><em>订单号</em></div>
									</th>
									<th width="180" class="th_d">
										<div class="th_title"><em>收件地址</em></div>
									</th>
									<th width="110" class="th_e">
										<div class="th_title"><em>联系方式</em></div>
									</th>
									<th width="100" class="th_f">
										<div class="th_title"><em>收件人姓名</em></div>
									</th>
									<th width="60" class="th_g">
										<div class="th_title"><em>操作</em></div>
									</th>
								</tr>
							</thead>
							<tbody>
								<form action="#" id="submitOk" method="post">
									<c:if test="${queryMessage eq 'have'}">
                               			<tr>
											<td colspan="7" align="center">
												查无此订单哦，请确认查询条件！
											</td>
										</tr> 
                               		</c:if>
									<s:iterator value="orderTempList" var="orderTemp" status="orderTempIndex">
										<tr class="list_tr">
											<td class="td_a">
												<input type="checkbox" class="td_check" name="ordersselect" value="<s:property value='id'/>" />
											</td>
											<td class="td_b">
												<span class="shipnum"><s:property value="mailNo"/></span>
											</td>
											<td class="td_c">
												<span class="ordernum"><s:property value="txLogisticId"/></span>
											</td>
											<td class="td_d">
												<div class="td_address">
													<s:property value="prov"/><s:property value="city"/><s:property value="district"/><s:property value="address"/>
												</div>
											</td>
											<td class="td_e">
												<span class="contact_mobile"><s:property value="mobile"/></span>
												<span class="contact_tel"><s:property value="phone"/></span>
											</td>
											<td class="td_f"><span class="td_name"><s:property value="name"/></span></td>
											<td class="td_g">
												<a class="del_item" href="orderImport_deleteOrderTemp.action?orderId=<s:property value='id'/>&pageNm=<s:property value='numOfPage'/>&menuFlag=fahuo_orderimport" title="删除" >删除</a>
											</td>
										</tr>
										<tr class="detail_tr">
											<td class="detail_td" colspan="7" style="display:none;">
												<div class="detail_box clearfix">
													<!-- S 商品信息 -->
													<div class="detail_info">
														<h4>商品信息</h4>
														<dl>
															<dt>商品名称：</dt>
															<dd>
																<s:if test="#orderTemp.goodsName == null">
																	无
																</s:if>
																<s:else>
																	<s:property value="goodsName"/>
																</s:else>
															</dd>
														</dl>
														<dl>
															<dt>价格：</dt>
															<dd>
																<s:if test="#orderTemp.totalPrice==null">
																	无
																</s:if>
																<s:else>																	
																	<s:property value="totalPrice"/>
																</s:else>
															</dd>
														</dl>
														<dl>
															<dt>邮政编码：</dt>
															<dd>
																<s:if test="#orderTemp.postCode==null">
																	无
																</s:if>
																<s:else>
																	<s:property value="postCode"/>
																</s:else>																	
															</dd>
														</dl>
													</div>
													<!-- E 商品信息 -->

													<!-- S 发件信息 -->
													<div class="detail_info">
														<h4>发件信息</h4>
														<dl>
															<dt>网店名称：</dt>
															<dd>
																<c:if test="${yto:getCookie('shopName')==null}">
																	暂无
																</c:if>
																${yto:getCookie('shopName')}
															</dd>
														</dl>
														<dl>
															<dt>发货地址：</dt>
															<s:if test="#orderTemp.sendAddress==null">
																<dd>
																	${yto:getCookie('addressProvince')}
																	${yto:getCookie('addressCity')}
																	${yto:getCookie('addressDistrict')}
																	${yto:getCookie('addressStreet')}
																</dd>
															</s:if>
															<s:else>
																<dd><s:property value="sendAddress"/></dd>
															</s:else>
														</dl>
														<dl>
															<dt>联系方式：</dt>
															<dd>
																<span class="contact_mobile"><s:property value="sendMobile"/></span>
																<span class="contact_tel"><s:property value="sendPhone"/></span>
															</dd>
														</dl>
														<dl>
															<dt>下单时间：</dt>
															<dd><s:property value="createTime"/></dd>
														</dl>
													</div>
													<!-- E 发件信息 -->
												</div>
											</td>
										</tr>
									</s:iterator>
									<!-- 左侧菜单选中样式 -->
									<input type="hidden" name="menuFlag" value="${menuFlag }" />
									<input type="hidden" id="numOfPage" value="<s:property value='numOfPage'/>" name="numOfPage"/>
								</form>
							</tbody>
						</table>
					</div>
					<!-- E Table -->
						
					<div class="table_footer clearfix">
						<div class="print_box">
							<input type="checkbox" id="select_all" class="check_all" />
							<label for="select_all">全选</label>									
							<a href="javascript:;" id="btn_del" class="del_btn btn btn_a" title="批量删除"><span>批量删除</span></a>
							<a href="javascript:;" id="btn_lead" class="start_import_btn btn btn_a" title="订单导入"><span>一键导入</span></a>										
						</div>
						<div class="pagenavi">
							<jsp:include page="/WEB-INF/page.jsp"></jsp:include>
						</div>
					</div>							
			</div>
		</div>
		<!-- E Tab -->
	</div>

	<script type="text/javascript">
		var params = {
			importFileErrType: '${uploadErrType}',// 上传文件错误类型
	// 		importFileErrList: '${uploadErrlist}'// 少于10个格式错误时的数组列表    
			importListCount: '${uploadListCount}',
			orderTempSize: '${orderTempSize}',  //当前页面上展示的订单条数
			orderIds: '${orderIds}',  //勾选的临时订单
			
			importFileErrList:[
				<c:forEach var="error" items="${uploadErrlist}" varStatus="status">		
					<c:choose>
						<c:when test="${status.last}">
							'${error}'
						</c:when>
						<c:otherwise>
							'${error}',
						</c:otherwise>
					</c:choose>
				</c:forEach>
			],
	
			<c:if test="${yto:getCookie('userType') == 2 || yto:getCookie('userType') == 22 || yto:getCookie('userType') == 23}">
				userType : 2,
			</c:if>
			<c:if test="${yto:getCookie('userType') == 1 || yto:getCookie('userType') == 12 || yto:getCookie('userType') == 13}">
				userType : 1,
			</c:if>
			
			onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
				? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},// “开始使用” == 1，“绑定网点” == 2
			userId:${yto:getCookie('id')},						//当前登录用户的id
			userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
			infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
			bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
			pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
		}

	</script>


	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
	<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>

	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/order_import.js?d=${str:getVersion() }"></script>
	<!--  E 当前页面 JS --> 
</div>
<!--  E Content -->