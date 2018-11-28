<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
 <link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
 <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
 <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
 
 <!-- S 当前页面 CSS -->
 <link rel="stylesheet" type="text/css" href="${cssPath}/page/sur_print.css?d=${str:getVersion() }" media="all" />
 
 <script type="text/javascript">
		var userState="${yto:getCookie('userState')}";
		var usertype="${yto:getCookie('usertype')}";
		var userCode="${yto:getCookie('userCode')}";
		var flag = false;
		//如果是卖家登录，如果未绑绑定网点，则弹出提示
		if(userCode !=null && userCode !=""){
			 flag = true;
		}
	var params = {
			bindBranch: flag,						// 是否绑定了网点
			allowPrintUrl: 'orderPrint!isPrint.action?',		// 允许网点打印请求
			iShipNumForm: '',						// 递增运单号请求
			saveShipNum: 'orderPrint!saveMailNo.action', 			// 保存运单号请求
			getTemplate: 'orderPrint!getOrderExpress.action?customerCode=',			// 获取客户模板
			onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
				? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2 
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
      <script type="text/javascript" src="${jsPath}/module/dialog.js?d=${str:getVersion() }"></script>
      <script type="text/javascript" src="${jsPath}/module/formvalidator/formValidator-4.1.1.js?d=${str:getVersion() }"></script>
      <script type="text/javascript" src="${jsPath}/module/formvalidator/formValidatorRegex.js?d=${str:getVersion() }"></script>
      <script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>
      <!-- S 当前页面 JS -->
      <script type="text/javascript" src="${jsPath}/page/sur_print.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
 
 
        <!-- S Content -->
        <div id="content">
            <div id="content_hd" class="clearfix">
<!--                 <h2 id="message_icon">面单打印</h2> -->
                <em>可打印淘宝线上下单的订单<a href="#"></a></em>
                <div id="filter_boxes">
                    <span class="filter_box">
                        <c:if test="${yto:getCookie('userType') == 1
                                      || yto:getCookie('userType') == 12
                                      || yto:getCookie('userType') == 13
                                      || yto:getCookie('userType') == 41}">		
                            <input type="checkbox" id="allow_wd" <s:property value='checked'/> />
                            <label for="allow_wd">允许网点打印</label>
                        </c:if>
							
                    </span>
                    <span class="filter_box">
                        <label for="show_num">每页显示：</label>
                        <select name="" id="show_num">
                            <option value="25">25</option>
                            <option value="50">50</option>
                            <option value="100">100</option>
                            <option value="200">200</option>
                            <option value="300">300</option>
                            <option value="500">500</option>
                        </select>
                    
                    </span> <input type="hidden" id="showMessage" value="<s:property value='showMessage'/>" name="showMessage"/>
                </div>
            </div>
            <div id="content_bd" class="clearfix">
                <!-- S Tab -->
                <div class="tab tab_a">
			        	<div class="tab_triggers">
			        		<ul>
					        	 <li class="tab_cur"><a href="javascript:;">批量打印</a></li>
		                         <li><a href="javascript:;">待发货</a></li>
		                         <li><a href="javascript:;">已发货</a></li>
		                    </ul> 
	                    </div>
	                    
	                    <div class="tab_panels">
                    
	                        <!-- S 批量打印 tab -->
	                        <div id="tab_panel_a" class="tab_panel">
	                            <!-- S Box -->
	                            <div class="box box_a">
	                                <div id="box_bd_a" class="box_bd">
	                                    <form action="orderPrint!getBatchPrintList.action" id="batch_search" class="form">
	                                        <p>
	                                            <label>温馨提示：</label>
	                                            <input type="hidden" id="cur_tab" value="<s:property value='curTab'/>" name="curTab"/>                                          
	                                           	<strong class="form_tip">1、先设置纸张格式→易通创建面单模板→在淘宝选择圆通在线下单→易通查找数据→易通打印并发货</strong>
												<strong class="form_tip">2、B2C客户对接后即可打印发货</strong>
	                                        </p>
	                                        <p>
	                                            <label>下单时间：</label>
	                               				<label for="lastest_a" class="label_raido"><input type="radio" id="lastest_a" class="input_radio" value="" name="order_date"  <s:if test="dateSign == \"0\"">checked</s:if>/>今天</label>
												<label for="lastest_b" class="label_raido"><input type="radio" id="lastest_b" class="input_radio" value="" name="order_date" <s:if test="dateSign == \"1\"">checked</s:if>/>昨天</label>
												<label for="lastest_c" class="label_raido"><input type="radio" id="lastest_c" class="input_radio" value="" name="order_date" <s:if test="dateSign == \"2\"">checked</s:if>/>近三天</label>
	                                            <input id="date_start" class="Wdate" type="text" name="startDate" value="<s:property value='startDate'/>"/> 
	                                            至 
	                                            <input id="date_end" class="Wdate" type="text" name="endDate" value="<s:property value='endDate'/>"/>
	                                            <span id="date_tip"></span>
	                                        </p>
	                                        <p>
	                                            <label for="order_type">订单类型：</label>
	                                            <select name="orderType" id="order_type">
	                                                <option value="0" <s:if test="orderType == \"0\"">selected</s:if>>所有订单</option>
	                                                <option value="1" <s:if test="orderType == \"1\"">selected</s:if>>仅打印快递单</option>
	                                                <option value="2" <s:if test="orderType == \"2\"">selected</s:if>>仅打印发货单</option>
	                                                <option value="3" <s:if test="orderType == \"3\"">selected</s:if>>已打印两单</option>
	                                                <option value="8" <s:if test="orderType == \"8\"">selected</s:if>>未打印两单</option>
	                                                <option value="4" <s:if test="orderType == \"4\"">selected</s:if>>已录入运单号</option>
	                                                <option value="5" <s:if test="orderType == \"5\"">selected</s:if>>未录入运单号</option>
	                                                <option value="6" <s:if test="orderType == \"6\"">selected</s:if>>未合并订单</option>
	                                                <option value="7" <s:if test="orderType == \"7\"">selected</s:if>>已合并订单</option>
	                                            </select>
					
	                                        <c:if test="${yto:getCookie('userType') == 2 
	                                                      || yto:getCookie('userType') == 21
	                                                      || yto:getCookie('userType') == 22
	                                                      || yto:getCookie('userType') == 23}">											
	                                            <select name="customerCode" id="select">
													<c:if test="${fn:length(vipThreadList) == 0}"><option value="0">暂无客户</option>：</c:if>
	                                                <s:iterator value="vipThreadList">
	                                                    <option  <s:if test="customerCode == userCode">selected</s:if> value='<s:property value="userCode"/>'><s:property value="userName"/>(<s:property value="userCode"/>)</option>
	                                                </s:iterator>
	                                            </select>
	                                            <span id="selectTip"><span class="yto_onShow">1、网点打印需客户绑定 2、客户需勾选“允许网点打印”</span></span>
	                                        </c:if>		
											</p>
											<p>
											<label for="order_num">订单查询：</label>
	                                         
	                                        <input type="text" id="order_num" class="input_text" name="mailNoOrOrderNo" value="<s:property value='mailNoOrOrderNo'/>"/>
	                                        <a href="javascript:;" id="batch_sear_btn" class="btn btn_a" title="查询"><span>查 询</span></a>
	                                        <span id="order_numTip"></span>
	                                        </p>
	                                        <div id="set_print">
	                                        	<span id="print_guide"><a title="阅读《打印指南》" class="btn btn_d"  href="${domain}/noint1_eachHelp.action?article.remark=help_orderPrint&menuFlag=help_orderPrint" target="_blank" >
	                                               <span>阅读《打印指南》</span></a></span> 
	                                        </div>
	                                        <!-- S 每页显示和排序 -->
											<input type="hidden" class="orderby_val" name="orderByCol" value="<s:property value='orderByCol'/>"/>
											<input type="hidden" class="show_num_val" name="numOfPage" value="<s:property value='numOfPage'/>"/>
											<input type="hidden" class="waybill_template_val" name="orderExpId" value="<s:property value='orderExpId'/>"/>
											<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
											<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
	                                        <!-- E 每页显示和排序 -->
	                                    </form>
	                                </div>
	
	                                <div id="box_bd_b" class="box_bd">
	                                    <a href="javascript:;" id="merger_orders_btn" class="btn btn_a" title="合并订单"><span><em class="select_arrow select_down">合并订单</em></span></a>
	                                    <a href="javascript:;" id="fill_out_btn" class="btn btn_a" title="批量填写运单号"><span>批量填写运单号</span></a>
	                                    <select name="" id="waybill_template">
	                                    </select>
										<span id="waybill_templateTip"></span>
	                                    <div id="merger_select">
	                                        <form action="orderPrint!joinOrClearOrder.action" id="merger_form_a">
	                                            <a href="javascript:;" title="合并订单" id="merger_order">合并订单</a>
	                                            <a href="javascript:;" title="取消合并" id="cancel_merger">取消合并</a>
	                                            <input type="hidden" class="order_ids_manual" name="joinOrClearOrderIdsMaunal"/>
												<input type="hidden" class="order_ids_auto" name="joinOrClearOrderIdsAuto"/>
												<input type="hidden" class="start_date_val" name="startDate" value="<s:property value='startDate'/>"/>
												<input type="hidden" class="end_date_val" name="endDate" value="<s:property value='endDate'/>"/>
												<input type="hidden" class="order_type_val" name="orderType" value="<s:property value='orderType'/>"/>
												<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
												<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
												<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
												<input type="hidden" class="orderby_val" name="orderByCol" value="<s:property value='orderByCol'/>"/>
												<input type="hidden" class="show_num_val" name="numOfPage" value="<s:property value='numOfPage'/>"/>
												<input type="hidden" class="waybill_template_val" name="orderExpId" value="<s:property value='orderExpId'/>"/>
												<input type="hidden" class="order_num_val" name="mailNoOrOrderNo" value="<s:property value='mailNoOrOrderNo'/>"/>
												<!-- 左侧菜单选中样式 -->
												<input type="hidden" name="menuFlag" value="${menuFlag }" />
	                                        </form>
	                                    </div>
	                                    <!--  
	                                    <p id="txt_tip">大头笔打印即将上线，期待易通^_^~</p>
	                                    -->
	                                </div>
	
	                                <div id="box_bd_c" class="box_bd">
	                                	 <c:if test="${fn:length(batchOrderPrintList) != 0}">
		                                    <a href="javascript:;" id="print_express" class="print_express_btn btn btn_a" title="打印快递单"><span>打印快递单</span></a>
		                                    <a href="javascript:;" id="print_invoice" class="print_invoice_btn btn btn_a" title="打印发货单"><span>打印发货单</span></a>
		                                  </c:if>  
	                                    	<!-- S 禁用按钮E 禁用按钮 -->
	                                    <c:if test="${fn:length(batchOrderPrintList) == 0}">
											<a href="javascript:;" class="no_allow_btn btn btn_e" title="打印快递单"><span>打印快递单</span></a>
											<a href="javascript:;" class="no_allow_btn btn btn_e" title="打印发货单"><span>打印发货单</span></a>
										</c:if>	
	                                    <div id="orderby_box">
	                                        <label for="orderby">排序：</label>
	                                        <select name="orderByCol" id="orderby">
	                                            <option value="buyAddress" <s:if test="orderByCol == \"buyAddress\"">selected</s:if>>收货地址</option>
	                                            <option value="firstProductName" <s:if test="orderByCol == \"firstProductName\"">selected</s:if>>商品名称</option>
	                                            <option value="mobileAndTelphone" <s:if test="orderByCol == \"mobileAndTelphone\"">selected</s:if>>联系方式</option>
	                                            <option value="createDateUp" <s:if test="orderByCol == \"createDateUp\"">selected</s:if>>下单时间↑</option>
	                                            <option value="createDateDown" <s:if test="orderByCol == \"createDateDown\"">selected</s:if>>下单时间↓</option>
	                                            <option value="tradeNoUp" <s:if test="orderByCol == \"tradeNoUp\"">selected</s:if>>订单号↑</option>
	                                            <option value="tradeNoDown" <s:if test="orderByCol == \"tradeNoDown\"">selected</s:if>>订单号↓</option>
	                                            <option value="logisticUp" <s:if test="orderByCol == \"logisticUp\"">selected</s:if>>物流号↑</option>
	                                            <option value="logisticDown" <s:if test="orderByCol == \"logisticDown\"">selected</s:if>>物流号↓</option>
	                                        </select>
	                                    </div>
	                                </div>
	                            </div>
	                            <!-- E Box -->
	
	                            <!-- S Table 批量打印列表-->
	                            <div id="table_a" class="table">
	                                <table>
	                                    <thead>
	                                        <tr>
	                                            <th width="36" class="th_a">
	                                    <div class="th_title"><em><input type="checkbox" class="check_all" /></em></div>
	                                    </th>
	                                    <th width="180" class="th_b">
	                                    <div class="th_title"><em><i class="fold_unfold"></i>运单号</em></div>
	                                    </th>
	                                    <th width="170" class="th_c">
	                                    	<div class="th_title"><em>物流号 / 订单号</em></div>
	                                    </th>
	                                    <th class="th_d">
	                                    <div class="th_title"><em>收件地址</em></div>
	                                    </th>
	                                    <th width="105" class="th_e">
	                                    <div class="th_title"><em>联系方式</em></div>
	                                    </th>
	                                    <th width="75" class="th_f">
	                                    <div class="th_title"><em>收件人</em></div>
	                                    </th>
	                                    <th width="99" class="th_g">
	                                    <div class="th_title"><em>打印状态</em></div>
	                                    </th>
	                                    </tr>
	                                    </thead>
	                                    <tbody>
	                                    <c:if test="${fn:length(batchOrderPrintList) == 0}">
	<!--                                     	<tr>
												<td colspan="7" align="center">
													未找到任何订单哦╮(╯_╰)╭
												</td>
											</tr> -->
	                                    </c:if>
	                                    <c:if test="${fn:length(batchOrderPrintList) != 0}">
	                                    <s:iterator value="batchOrderPrintList" var="batchOrderPrint" status="stuts">
	                                        <tr class="list_tr">
	                                            <td class="td_a">
	                                                <input type="checkbox" class="td_check" />
	                                                <input type="hidden" class="order_id" value='<s:property value="id"/>'/>
	                                            </td>
	                                            <td class="td_b">
	                                                <s:if test="mailNo == ''">
	                                                	<input type="text" class="input_text" value="<s:property value="mailNo"/>" /><a href="javascript:;" class="save_shipnum do_link" title="保存">保存</a>
		                                            </s:if>
		                                             <s:if test="mailNo != ''">
		                                             	<span class="shipnum"><s:property value="mailNo"/></span><a href="javascript:;" class="modify_shipnum do_link" title="修改">修改</a>
		                                            </s:if>
	                                                <input class="def_shipnum" type="hidden" value='<s:property value="mailNo"/>'/>
	                                            </td>
	                                            <td class="td_c">
													<span class="logistics_num"><s:property value="txLogisticId"/></span>
	                                                <span class="ordernum"><s:property value="tradeNo"/></span>
	                                                <input type="hidden" class="merger_or_not" value="<s:property value='joinNum'/>" />
	                                            </td>
	                                            <td class="td_d">
	                                                <div class="td_address">
	                                                    <s:property value="buyFulladdress"/>
	                                                </div>
	                                            </td>
	                                            <td class="td_e">
	                                                <span class="contact_mobile"><s:property value="buyMobile"/></span>
	                                                <span class="contact_tel"><s:property value="buyTelphone"/></span>
	                                            </td>
	                                            <td class="td_f"><span class="td_name"><s:property value="buyName"/></span></td>
	                                            <td class="td_g">
	                                            	<s:if test="isPrint==\"N\"">
	                                            		<span class="print_status">未打印快递单</span>
	                                            	</s:if>
		                                            <s:else>
		                                            	<span class="print_status">已打印快递单</span>
		                                            </s:else>
		                                            <s:if test="isPrintSend==\"N\"">
	                                            		<span class="print_status">未打印发货单</span>
	                                            	</s:if>
		                                            <s:else>
		                                            	<span class="print_status">已打印发货单</span>
		                                            </s:else>
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
	                                                            <s:property value="productName" escape="false"/>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>价格：</dt>
	                                                            <dd><s:property value="goodsValue"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>邮政编码：</dt>
	                                                            <dd><s:property value="buyPostcode"/></dd>
	                                                        </dl>
	                                                    </div>
	                                                    <!-- E 商品信息 -->
	
	                                                    <!-- S 发件信息 -->
	                                                    <div class="detail_info">
	                                                        <h4>发件信息</h4>
	                                                        <dl>
	                                                            <dt>网店名称：</dt>
	                                                            <dd><s:property value="saleName"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>发货地址：</dt>
	                                                            <dd><s:property value="saleFulladdress"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>联系方式：</dt>
	                                                            <dd><s:property value="saleMobile"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>下单时间：</dt>
	                                                            <dd><s:date name="createTime" format='yyyy-MM-dd HH:mm:ss'/></dd>
	                                                        </dl>
	                                                    </div>
	                                                    <!-- E 发件信息 -->
	                                                </div>
	                                            </td>
	                                        </tr>
	                                    </s:iterator>
	                                    </c:if>
	                                    </tbody>
	                                </table>
	                            </div>
	                            <!-- E Table -->
	
	                            <div class="table_footer clearfix">
	                                <div class="print_box">
	                                    <input type="checkbox" id="select_all" class="check_all" />
	                                    <label for="select_all">全选</label>
	
	                                    <c:if test="${fn:length(batchOrderPrintList) != 0}">
		                                    <a href="javascript:;" id="print_express2" class="print_express_btn btn btn_a" title="打印快递单"><span>打印快递单</span></a>
		                                    <a href="javascript:;" id="print_invoice2" class="print_invoice_btn btn btn_a" title="打印发货单"><span>打印发货单</span></a>
		                                  </c:if>  
	                                    	<!-- S 禁用按钮E 禁用按钮 -->
	                                    <c:if test="${fn:length(batchOrderPrintList) == 0}">
											<a href="javascript:;" class="no_allow_btn btn btn_e" title="打印快递单"><span>打印快递单</span></a>
											<a href="javascript:;" class="no_allow_btn btn btn_e" title="打印发货单"><span>打印发货单</span></a>
										</c:if>	
										
										<form action="orderPrint!getPrintOrderPrintList.action" id="print_express_form" target="_blank" method="post">
											<input name="OrderExpressId" type="hidden" id="express_template" />
											<input name="orderPrintIds" type="hidden" id="express_orderid" />
											<input name="orderByCol" type="hidden" class="orderby_val"/>
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
										</form>
										<form action="orderPrint!getSendPrintOrderPrintList.action" id="print_invoice_form" target="_blank" method="post">
											<input name="orderPrintIds" type="hidden" id="invoice_orderid" />
											<input name="orderByCol" type="hidden" class="orderby_val" />
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
										</form>
	                                </div>
	
	                                <!-- S 批量打印分页 PageNavi -->
	                                <div class="pagenavi">
	                                   <form action="orderPrint!getBatchPrintList.action">
								         	 <s:if test='paginationOrderPrint.totalPages!=0'> 
									          	 <s:if test='paginationOrderPrint.totalPages > 5'>
									          	 	 <s:if test="currentPage!=1">
									          	 		<a href="javascript:;" title="上一页" class="page_txt page_prev">上一页</a>
									          	 	 </s:if>	
									          	 </s:if>
										     	<s:if test="paginationOrderPrint.totalPages > 5">
										     		 <s:if test="currentPage>3 && currentPage<paginationOrderPrint.totalPages-2">
										     		 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
										     		 	<span class="page_extend">...</span>
										     		 	<a href="javascript:;" title="<s:property value='currentPage-1'/>" class="page_num page_txt"><s:property value='currentPage-1'/></a>
											    		<span class="page_cur"><s:property value='currentPage'/></span>
											    	 	<a href="javascript:;" title="<s:property value='currentPage+1'/>" class="page_num page_txt"><s:property value='currentPage+1'/></a>
										     		 	<span class="page_extend">...</span>
										     		 	<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==1">
											    	 	<span class="page_cur">1</span>
											    	 	<a href="javascript:;" title="2" class="page_num page_txt">2</a>
											    	 	<a href="javascript:;" title="3" class="page_num page_txt">3</a>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==2">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_cur">2</span>
											    	 	<a href="javascript:;" title="3" class="page_num page_txt">3</a>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==3">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<a href="javascript:;" title="2" class="page_num page_txt">2</a>
											    	 	<span class="page_cur">3</span>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==paginationOrderPrint.totalPages">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages - 2'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages - 2'/></a>
											    	 	<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages - 1'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages - 1'/></a>
											    	 	<span class="page_cur"><s:property value='paginationOrderPrint.totalPages'/></span>
											    	 </s:if>
											    	 <s:if test="currentPage==paginationOrderPrint.totalPages-1">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages - 2'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages - 2'/></a>
											    	 	<span class="page_cur"><s:property value='paginationOrderPrint.totalPages - 1'/></span>
											    	 	<a href="javascript:;/>" title="<s:property value='paginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==paginationOrderPrint.totalPages-2">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<span class="page_cur"><s:property value='paginationOrderPrint.totalPages - 2'/></span>
											    	 	<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages - 1'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages - 1'/></a>
											    	 	<a href="javascript:;" title="<s:property value='paginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='paginationOrderPrint.totalPages'/></a>
											    	 </s:if>
										     	</s:if>
										     	<s:else>
										     		<s:iterator value="pageList" var="pa" status="status">
											     		 <s:if test="currentPage!=#pa">
												    		<a href="javascript:;" title="<s:property value='pa'/>" class="page_num page_txt"><s:property value='pa'/></a>
												    	 </s:if>
												    	 <s:else>
												    	 	<span class="page_cur"><s:property value='pa'/></span>
												    	 </s:else>
											    	 </s:iterator> 
										     	</s:else>
										    
											     <s:if test='paginationOrderPrint.totalPages > 5'>
											     	<s:if test="currentPage!=paginationOrderPrint.totalPages">
											     		<a href="javascript:;" title="下一页" class="page_txt page_next">下一页</a>
										         	</s:if>
										         </s:if>
									       </s:if> 
	                                   	   <span> 共 <em><s:property value='paginationOrderPrint.totalRecords'/></em> 条</span>
	                                   	   
											<input type="hidden" class="to_page_num page_txt" name="currentPage" value="<s:property value='currentPage'/>"/>
											<input type="hidden" class="start_date_val" name="startDate" value="<s:property value='startDate'/>"/>
											<input type="hidden" class="end_date_val" name="endDate" value="<s:property value='endDate'/>"/>
											<input type="hidden" class="order_type_val" name="orderType" value="<s:property value='orderType'/>"/>
											<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
											<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
											<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
											<input type="hidden" class="orderby_val" name="orderByCol" value="<s:property value='orderByCol'/>"/>
											<input type="hidden" class="show_num_val" name="numOfPage" value="<s:property value='numOfPage'/>"/>
											<input type="hidden" class="waybill_template_val" name="orderExpId" value="<s:property value='orderExpId'/>"/>
											<input type="hidden" class="order_num_val" name="mailNoOrOrderNo" value="<s:property value='mailNoOrOrderNo'/>"/>
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
										</form>	
	                                </div>
	                                <!-- E PageNavi -->
	                            </div>
	                        </div>
	                        <!-- E 批量打印 tab -->
	
	                        <!-- S 待发货 tab -->
	                        <div id="tab_panel_b" class="tab_panel" style="display:none;">
	                            <!-- S Box -->
	                            <div class="box box_a">
	                                <div class="box_bd" id="box_bd_f">
	                                    <form action="orderPrint!getUndeliverGoodsList.action" id="sear_ship_form" class="form">
	                                    	<p>
												<label>下单时间：</label>
												<label for="lastest_g" class="label_raido"><input type="radio" id="lastest_g" class="input_radio" name="order_date3" value="" <s:if test="dateSignUnelivered == \"0\"">checked</s:if>>今天</label>
												<label for="lastest_h" class="label_raido"><input type="radio" id="lastest_h" class="input_radio" name="order_date3" value="" <s:if test="dateSignUnelivered == \"1\"">checked</s:if>>昨天</label>
												<label for="lastest_i" class="label_raido"><input type="radio" id="lastest_i" class="input_radio" name="order_date3" value="" <s:if test="dateSignUnelivered == \"2\"">checked</s:if>>近三天</label>
												<input id="date_start3" class="Wdate" type="text" name="startDateUndeliver" value="<s:property value='startDateUndeliver'/>"> 
												至 
												<input id="date_end3" class="Wdate" type="text" name="endDateUndeliver" value="<s:property value='endDateUndeliver'/>">
												<span id="date_tip3"></span>
											</p>
											<p>
												 <c:if test="${yto:getCookie('userType') == 2 
	                                                      || yto:getCookie('userType') == 21
	                                                      || yto:getCookie('userType') == 22
	                                                      || yto:getCookie('userType') == 23}">		
	                                                <label>客户选择：</label>									
		                                            <select name="customerCode2" id="select2">
														<c:if test="${fn:length(vipThreadList) == 0}"><option value="0">暂无客户</option>：</c:if>
		                                                <s:iterator value="vipThreadList">
		                                                    <option  <s:if test="customerCode2 == userCode">selected</s:if> value='<s:property value="userCode"/>'><s:property value="userName"/>(<s:property value="userCode"/>)</option>
		                                                </s:iterator>
		                                            </select>
		                                            <span id="selectTip"><span class="yto_onShow">只能发出独立网店的订单。</span></span>
	                                       		 </c:if>		
											</p>
	                                    	<p>
		                                   		<label for="sear_ship_input">订单查询：</label>
		                                        <input type="text" class="input_text" name="mailNoOrOrderNoUndeliver" value="<s:property value='mailNoOrOrderNoUndeliver'/>" id="sear_ship_input"/>
		                                        <a href="javascript:;" id="sear_ship_btn" class="btn btn_a" title="查询"><span>查 询</span></a>
												<span id="sear_ship_inputTip"></span>
	                                    	</p>
											<!-- S 每页显示和排序 -->
											<input type="hidden" class="orderby2_val" name="undeliverOrderByCol" value="<s:property value='undeliverOrderByCol'/>"/>
											<input type="hidden" class="show_num2_val" name="numOfPage" value="<s:property value='numOfPage'/>"/>
											<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
											<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
											
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
	                                        <!-- E 每页显示和排序 -->
	                                    </form>
	                                </div>
	                                <div class="box_bd" id="box_bd_d">
	                                	<c:if test="${fn:length(undeliverOrderPrintList) != 0}">
	                                    	<a href="javascript:;" id="batch_send_btn" class="batch_btn btn btn_a" title="批量发货"><span>批量发货</span></a>
	                                    </c:if>	
	                                    <c:if test="${fn:length(undeliverOrderPrintList) == 0}">
	                                         	 <!-- S 禁用按钮E 禁用按钮 -->
											<a href="javascript:;" class="no_allow_btn btn btn_e" title="批量发货"><span>批量发货</span></a>
	                                    </c:if>
	                                    <div id="sear_ship_box">
	                                        <label for="orderby2">排序：</label>
	                                        <select id="orderby2" name="undeliverOrderByCol">
	                                        	<option value="buyAddress">收货地址</option>
												<option value="firstProductName">商品名称</option>
												<option value="mobileAndTelphone">联系方式</option>
												<option value="createDateUp">下单时间↑</option>
												<option value="createDateDown">下单时间↓</option>
												<option value="tradeNoUp">订单号↑</option>
												<option value="tradeNoDown">订单号↓</option>
												<option value="logisticUp">物流号↑</option>
	                                            <option value="logisticDown">物流号↓</option>
											</select>
	                                    </div>
	                                </div>
	                            </div>
	                            <!-- E Box -->
	
	                            <!-- S Table -->
	                            <div id="table_b" class="table">
	                                <table>
	                                    <thead>
	                                        <tr>
	                                            <th width="36" class="th_a">
	                                    			<div class="th_title"><em><input type="checkbox" class="check_all" /></em></div>
			                                    </th>
			                                    <th width="180" class="th_b">
			                                    	<div class="th_title"><em><i class="fold_unfold"></i>运单号</em></div>
			                                    </th>
			                                    <th width="170" class="th_c">
			                                    	<div class="th_title"><em>物流号 / 订单号</em></div>
			                                    </th>
			                                    <th class="th_d">
			                                    	<div class="th_title"><em>收件地址</em></div>
			                                    </th>
			                                    <th width="110" class="th_e">
			                                   		 <div class="th_title"><em>联系方式</em></div>
			                                    </th>
			                                    <th width="75" class="th_f">
			                                   	 	<div class="th_title"><em>收件人</em></div>
			                                    </th>
	                                   		 </tr>
	                                    </thead>
	
	                                    <tbody>
	                                    <c:if test="${fn:length(undeliverOrderPrintList) == 0}">
	                                      <!--     	<tr>
												<td colspan="6" align="center">
													未找到任何订单哦╮(╯_╰)╭
												</td>
											</tr>
											-->
	                                    </c:if>
	                                    <c:if test="${fn:length(undeliverOrderPrintList) != 0}">
	                                     <s:iterator value="undeliverOrderPrintList" var="undeliverOrderPrint" status="stuts">
	                                        <tr class="list_tr">
	                                            <td class="td_a">
		                                            <input type="checkbox" class="td_check" />
	                                                <input type="hidden" class="order_id" value='<s:property value="id"/>'/>
	                                            </td>
	                                            <td class="td_b">
	                                            	 <s:if test="mailNo == ''">
	                                                	<input type="text" class="input_text" value="<s:property value="mailNo"/>" /><a href="javascript:;" class="save_shipnum do_link" title="保存">保存</a>
		                                            </s:if>
		                                             <s:if test="mailNo != ''">
		                                             	<span class="shipnum"><s:property value="mailNo"/></span><a href="javascript:;" class="modify_shipnum do_link" title="修改">修改</a>
		                                            </s:if>
	                                                <input class="def_shipnum" type="hidden" value='<s:property value="mailNo"/>'/>
	                                            </td>
	                                            <td class="td_c">
													<span class="logistics_num"><s:property value="txLogisticId"/></span>
	                                                <span class="ordernum"><s:property value="tradeNo"/></span>
	                                                <input type="hidden" class="merger_or_not" value="<s:property value='joinNum'/>" />
	                                            </td>
	                                            <td class="td_d">
	                                                <div class="td_address">
	                                                    <s:property value="buyFulladdress"/>
	                                                </div>
	                                            </td>
	                                            <td class="td_e">
	                                                 <span class="contact_mobile"><s:property value="buyMobile"/></span>
	                                                <span class="contact_tel"><s:property value="buyTelphone"/></span>
	                                            </td>
	                                            <td class="td_f"><span class="td_name"><s:property value="buyName"/></span></td>
	                                        </tr>
	                                        <tr class="detail_tr">
	                                            <td class="detail_td" colspan="6" style="display:none;">
	                                                <div class="detail_box clearfix">
	                                                    <!-- S 商品信息 -->
	                                                    <div class="detail_info">
	                                                   	<h4>商品信息</h4>
	                                                        <dl>
	                                                            <dt>商品名称：</dt>
	                                                            <s:property value="productName" escape="false"/>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>价格：</dt>
	                                                            <dd><s:property value="goodsValue"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>邮政编码：</dt>
	                                                            <dd><s:property value="buyPostcode"/></dd>
	                                                        </dl>
	                                                    </div>
	                                                    <!-- E 商品信息 -->
	
	                                                    <!-- S 发件信息 -->
	                                                    <div class="detail_info">
	                                                        <h4>发件信息</h4>
	                                                        <dl>
	                                                            <dt>网店名称：</dt>
	                                                            <dd><s:property value="saleName"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>发货地址：</dt>
	                                                            <dd><s:property value="saleFulladdress"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>联系方式：</dt>
	                                                            <dd><s:property value="saleMobile"/></dd>
	                                                        </dl>
	                                                        <dl>
	                                                            <dt>下单时间：</dt>
	                                                            <dd><s:date name="createTime" format='yyyy-MM-dd HH:mm:ss'/></dd>
	                                                        </dl>
	                                                    </div>
	                                                    <!-- E 发件信息 -->
	                                                </div>
	                                            </td>
	                                        </tr>
	                                       </s:iterator> 
	                                      </c:if> 
	                                    </tbody>
	                                </table>
	                            </div>
	                            <!-- E Table -->
	
	                            <div class="table_footer clearfix">
	                                <div class="print_box">
	                                    <input type="checkbox" id="select_all2" class="check_all" />
	                                    <label for="select_all2">全选</label>
										<c:if test="${fn:length(undeliverOrderPrintList) != 0}">
	                                    	 <a href="javascript:;" id="ship_vol_btn" class="batch_btn btn btn_a" title="批量发货"><span>批量发货</span></a>
											 <a href="javascript:;" id="del_item_btn" class="btn btn_a" title="不想看到他"><span>不想看到他</span></a>
	                                    </c:if>	
	                                    <c:if test="${fn:length(undeliverOrderPrintList) == 0}">
	                                         	 <!-- S 禁用按钮E 禁用按钮 -->
										<a href="javascript:;" class="no_allow_btn btn btn_e" title="批量发货"><span>批量发货</span></a>
										<a href="javascript:;" class="no_allow_btn btn btn_e" title="不想看到他"><span>不想看到他</span></a>
	                                    </c:if>
										
										<form action="orderPrint!batchOrderPrintSend.action" id="ship_vol_form">
											<input type="hidden" class="order_ids_b"  name="orderPrintIds"/>
											<input type="hidden" class="start_date3_val" name="startDateUndeliver"/>
											<input type="hidden" class="end_date3_val" name="endDateUndeliver"/>
											<input type="hidden" class="orderby2_val" name="undeliverOrderByCol" />
											<input type="hidden" class="show_num2_val" name="numOfPage" />
											<input type="hidden" class="sear_ship_input_val" name="mailNoOrOrderNoUndeliver" />
											<input type="hidden" class="status_tip" value="<s:property value='isSuccessToTaobao'/>" />
											<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
											<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
											<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
										</form>
										<form action="orderPrint!delOrderPrintFromUndeliver.action" id="del_item_form">
											<input type="hidden" class="order_ids_b" name="delOpIds"/>
											<input type="hidden" class="start_date3_val" name="startDateUndeliver" />
											<input type="hidden" class="end_date3_val" name="endDateUndeliver" />
											<input type="hidden" class="orderby2_val" name="undeliverOrderByCol" />
											<input type="hidden" class="show_num2_val" name="numOfPage"/>
											<input type="hidden" class="sear_ship_input_val" name="mailNoOrOrderNoUndeliver" />
											<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
											<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
											<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
										</form>
	                                </div>
	
	                                <!-- S 代发货分页PageNavi -->
	                                <div class="pagenavi">
	                                    <form action="orderPrint!getUndeliverGoodsList.action">  
								          <s:if test='undeliverpaginationOrderPrint.totalPages!=0'> 
								          	 <s:if test='undeliverpaginationOrderPrint.totalPages > 5'>
								          	 	 <s:if test="currentPage!=1">
								          	 		<a href="javascript:;" title="上一页" class="page_txt page_prev">上一页</a>
								          	 	 </s:if>	
								          	 </s:if>
										     	<s:if test="undeliverpaginationOrderPrint.totalPages > 5">
										     		 <s:if test="currentPage>3 && currentPage<undeliverpaginationOrderPrint.totalPages-2">
										     		 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
										     		 	<span class="page_extend">...</span>
										     		 	<a href="javascript:;" title="<s:property value='currentPage-1'/>" class="page_num page_txt"><s:property value='currentPage-1'/></a>
											    		<span class="page_cur"><s:property value='currentPage'/></span>
											    	 	<a href="javascript:;" title="<s:property value='currentPage+1'/>" class="page_num page_txt"><s:property value='currentPage+1'/></a>
										     		 	<span class="page_extend">...</span>
										     		 	<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==1">
											    	 	<span class="page_cur">1</span>
											    	 	<a href="javascript:;" title="2" class="page_num page_txt">2</a>
											    	 	<a href="javascript:;" title="3" class="page_num page_txt">3</a>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==2">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_cur">2</span>
											    	 	<a href="javascript:;" title="3" class="page_num page_txt">3</a>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==3">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<a href="javascript:;" title="2" class="page_num page_txt">2</a>
											    	 	<span class="page_cur">3</span>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==undeliverpaginationOrderPrint.totalPages">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages - 2'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages - 2'/></a>
											    	 	<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages - 1'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages - 1'/></a>
											    	 	<span class="page_cur"><s:property value='undeliverpaginationOrderPrint.totalPages'/></span>
											    	 </s:if>
											    	 <s:if test="currentPage==undeliverpaginationOrderPrint.totalPages-1">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages - 2'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages - 2'/></a>
											    	 	<span class="page_cur"><s:property value='undeliverpaginationOrderPrint.totalPages - 1'/></span>
											    	 	<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==undeliverpaginationOrderPrint.totalPages-2">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<span class="page_cur"><s:property value='undeliverpaginationOrderPrint.totalPages - 2'/></span>
											    	 	<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages - 1'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages - 1'/></a>
											    	 	<a href="javascript:;" title="<s:property value='undeliverpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='undeliverpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
										     	</s:if>
										     	<s:else>
										     		<s:iterator value="undeliverpageList" var="pa" status="status">
											     		 <s:if test="currentPage!=#pa">
												    		<a href="javascript:;" title="<s:property value='pa'/>" class="page_num page_txt"><s:property value='pa'/></a>
												    	 </s:if>
												    	 <s:else>
												    	 	<span class="page_cur"><s:property value='pa'/></span>
												    	 </s:else>
											    	 </s:iterator> 
										     	</s:else>
										    
										     <s:if test='undeliverpaginationOrderPrint.totalPages > 5'>
										     	<s:if test="currentPage!=undeliverpaginationOrderPrint.totalPages">
										     		<a href="javascript:;" title="下一页" class="page_txt page_next">下一页</a>
									         	</s:if>
									         </s:if>
									       </s:if> 
	                                   	   <span> 共 <em><s:property value='undeliverpaginationOrderPrint.totalRecords'/></em> 条</span>
	                                   	   
											<input type="hidden" class="to_page_num page_txt" name="currentPage" value="<s:property value='currentPage'/>"/>
											<input type="hidden" class="start_date3_val" name="startDateUndeliver" value="<s:property value='startDateUndeliver'/>"/>
											<input type="hidden" class="end_date3_val" name="endDateUndeliver" value="<s:property value='endDateUndeliver'/>"/>
											<input type="hidden" class="orderby2_val" name="undeliverOrderByCol" value="<s:property value='undeliverOrderByCol'/>"/>
											<input type="hidden" class="show_num2_val" name="numOfPage" value="<s:property value='numOfPage'/>"/>
											<input type="hidden" class="sear_ship_input_val" name="mailNoOrOrderNoUndeliver" value="<s:property value='mailNoOrOrderNoUndeliver'/>"/>
											<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
											<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
											<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
	                                   	  </form>
	                                </div>
	                                <!-- E PageNavi -->
	                            </div>
	                        </div>
	                        <!-- E 待发货 tab -->
	
	                        <!-- S 已发货 tab -->
	                        <div id="tab_panel_c" class="tab_panel" style="display:none;">
	                            <!-- S Box -->
	                            <div id="date_choose" class="box box_a">
	                                <div id="box_bd_g" class="box_bd">
	                                    <form action="orderPrint!getDeliveredGoodsList.action" id="send_search" class="form">
											<p>
												<label>发货时间：</label>
												<label for="lastest_d" class="label_raido"><input type="radio" id="lastest_d" class="input_radio" name="order_date2" value="" <s:if test="dateSignDelivered == \"0\"">checked</s:if>>今天</label>
												<label for="lastest_e" class="label_raido"><input type="radio" id="lastest_e" class="input_radio" name="order_date2" value="" <s:if test="dateSignDelivered == \"1\"">checked</s:if>>昨天</label>
												<label for="lastest_f" class="label_raido"><input type="radio" id="lastest_f" class="input_radio" name="order_date2" value="" <s:if test="dateSignDelivered == \"2\"">checked</s:if>>近三天</label>
												<input id="date_start2" class="Wdate" type="text" name="startDateDelivered" value="<s:property value='startDateDelivered'/>"> 
												至 
												<input id="date_end2" class="Wdate" type="text" name="endDateDelivered" value="<s:property value='endDateDelivered'/>">
												<span id="date_tip2"></span>
											</p>
											<p>
												 <c:if test="${yto:getCookie('userType') == 2 
	                                                      || yto:getCookie('userType') == 21
	                                                      || yto:getCookie('userType') == 22
	                                                      || yto:getCookie('userType') == 23}">		
	                                                 <label>客户选择：</label>									
		                                            <select name="customerCode3" id="select3">
														<c:if test="${fn:length(vipThreadList) == 0}"><option value="0">暂无客户</option>：</c:if>
		                                                <s:iterator value="vipThreadList">
		                                                    <option  <s:if test="customerCode3 == userCode">selected</s:if> value='<s:property value="userCode"/>'><s:property value="userName"/>(<s:property value="userCode"/>)</option>
		                                                </s:iterator>
		                                            </select>
		                                            <span id="selectTip"><span class="yto_onShow">只能查看独立网点已发货订单。</span></span>
	                                       		 </c:if>		
											</p>
											<p>
												<label for="sear_ship_input2">订单查询：</label>
												<input type="text" class="input_text" id="sear_ship_input2" value="<s:property value='mailNoOrOrderNoDelivered'/>" name="mailNoOrOrderNoDelivered">
												<a title="查询" class="btn btn_a" id="sear_ship_btn2" href="javascript:;"><span>查 询</span></a>
												<span id="sear_ship_input2Tip"></span>
											</p>
											<!-- S 每页显示和排序 -->
											<input type="hidden" class="orderby3_val" name="deliveredOrderByCol" value="<s:property value='deliveredOrderByCol'/>"/>
											<input type="hidden" class="show_num3_val" name="numOfPage" value="<s:property value='numOfPage'/>"/>
											<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
											<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
											
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
											<!-- E 每页显示和排序 -->
										</form>
	                                </div>
	                            </div>
	                            <!-- E Box -->
	
	                            <!-- S Box -->
	                            <div class="box box_a">
	                                <div id="box_bd_e" class="box_bd">
	                                    <div id="sear_ship_box2">
	                                        <label for="orderby3">排序：</label>
	                                        <select id="orderby3" name="deliveredOrderByCol">
	                                            <option value="buyAddress">收货地址</option>
												<option value="firstProductName">商品名称</option>
												<option value="mobileAndTelphone">联系方式</option>
												<option value="createDateUp">下单时间↑</option>
												<option value="createDateDown">下单时间↓</option>
												<option value="tradeNoUp">订单号↑</option>
												<option value="tradeNoDown">订单号↓</option>
												<option value="logisticUp">物流号↑</option>
	                                            <option value="logisticDown">物流号↓</option>
	                                        </select>
	                                    </div>
	                                </div>
	                            </div>
	                            <!-- E Box -->
	
	                            <!-- S Table -->
	                            <div id="table_c" class="table">
	                                <table>
	                                    <thead>
	                                        <tr>
			                                    <th width="180" class="th_b">
			                                    	<div class="th_title"><em><i class="fold_unfold"></i>运单号</em></div>
			                                    </th>
			                                    <th width="170" class="th_c">
			                                    	<div class="th_title"><em>物流号 / 订单号</em></div>
			                                    </th>
			                                    
			                                    <th class="th_d">
			                                    	<div class="th_title"><em>收件地址</em></div>
			                                    </th>
			                                    <th width="110" class="th_e">
			                                    	<div class="th_title"><em>联系方式</em></div>
			                                    </th>
			                                    <th width="75" class="th_f">
			                                    	<div class="th_title"><em>收件人</em></div>
			                                    </th>
	                                    	</tr>
	                                    </thead>
	
	                                    <tbody>
	                                    <c:if test="${fn:length(deliveredOrderPrintList) == 0}">
	                                     <!--    <tr>
												<td colspan="5" align="center">
													未找到任何订单哦╮(╯_╰)╭
												</td>
											</tr>
											 --> 
	                                    </c:if>
	                                    <c:if test="${fn:length(deliveredOrderPrintList) != 0}">
	                                     <s:iterator value="deliveredOrderPrintList" var="deliverOrderPrinted" status="stuts">
	                                        <tr class="list_tr">
	                                            <td class="td_b">
	                                                <span class="shipnum"><s:property value="mailNo"/></span>
	                                            </td>
	                                            <td class="td_c">
													<span class="logistics_num"><s:property value="txLogisticId"/></span>
	                                                <span class="ordernum"><s:property value="tradeNo"/></span>
	                                                <input type="hidden" class="merger_or_not" value="<s:property value='joinNum'/>" />
	                                            </td>
	                                             <td class="td_d">
	                                                <div class="td_address">
	                                                    <s:property value="buyFulladdress"/>
	                                                </div>
	                                            </td>
	                                            <td class="td_e">
	                                                 <span class="contact_mobile"><s:property value="buyMobile"/></span>
	                                                <span class="contact_tel"><s:property value="buyTelphone"/></span>
	                                            </td>
	                                            <td class="td_f"><span class="td_name"><s:property value="buyName"/></span></td>
	                                        </tr>
	                                        <tr class="detail_tr">
	                                            <td class="detail_td" colspan="5" style="display:none;">
	                                                <div class="detail_box clearfix">
	                                                    <div class="detail_info">
		                                                   	<h4>商品信息</h4>
		                                                        <dl>
		                                                            <dt>商品名称：</dt>
		                                                            <s:property value="productName" escape="false"/>
		                                                        </dl>
		                                                        <dl>
		                                                            <dt>价格：</dt>
		                                                            <dd><s:property value="goodsValue"/></dd>
		                                                        </dl>
		                                                        <dl>
		                                                            <dt>邮政编码：</dt>
		                                                            <dd><s:property value="buyPostcode"/></dd>
		                                                        </dl>
		                                                    </div>
		                                                    <!-- E 商品信息 -->
		
		                                                    <!-- S 发件信息 -->
		                                                    <div class="detail_info">
		                                                        <h4>发件信息</h4>
		                                                        <dl>
		                                                            <dt>网店名称：</dt>
		                                                            <dd><s:property value="saleName"/></dd>
		                                                        </dl>
		                                                        <dl>
		                                                            <dt>发货地址：</dt>
		                                                            <dd><s:property value="saleFulladdress"/></dd>
		                                                        </dl>
		                                                        <dl>
		                                                            <dt>联系方式：</dt>
		                                                            <dd><s:property value="saleMobile"/></dd>
		                                                        </dl>
		                                                        <dl>
		                                                            <dt>下单时间：</dt>
		                                                            <dd><s:date name="createTime" format='yyyy-MM-dd HH:mm:ss'/></dd>
		                                                        </dl>
	                                                    	</div>
	                                                </div>
	                                            </td>
	                                        </tr>
	                                       </s:iterator> 
	                                       </c:if>
	                                    </tbody>
	                                </table>
	                            </div>
	                            <!-- E Table -->
	
	                            <div class="table_footer clearfix">
	                                <!-- S 已发货分页 PageNavi -->
	                                <div class="pagenavi">
	                                   <form action="orderPrint!getDeliveredGoodsList.action">
								          <s:if test='deliveredpaginationOrderPrint.totalPages!=0'> 
								          	 <s:if test='deliveredpaginationOrderPrint.totalPages > 5'>
								          	 	 <s:if test="currentPage!=1">
								          	 		<a href="javascript:;" title="上一页" class="page_txt page_prev">上一页</a>
								          	 	 </s:if>	
								          	 </s:if>
										     	<s:if test="deliveredpaginationOrderPrint.totalPages > 5">
										     		 <s:if test="currentPage>3 && currentPage<deliveredpaginationOrderPrint.totalPages-2">
										     		 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
										     		 	<span class="page_extend">...</span>
										     		 	<a href="javascript:;" title="<s:property value='currentPage-1'/>" class="page_num page_txt"><s:property value='currentPage-1'/></a>
											    		<span class="page_cur"><s:property value='currentPage'/></span>
											    	 	<a href="javascript:;" title="<s:property value='currentPage+1'/>" class="page_num page_txt"><s:property value='currentPage+1'/></a>
										     		 	<span class="page_extend">...</span>
										     		 	<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==1">
											    	 	<span class="page_cur">1</span>
											    	 	<a href="javascript:;" title="2" class="page_num page_txt">2</a>
											    	 	<a href="javascript:;" title="3" class="page_num page_txt">3</a>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==2">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_cur">2</span>
											    	 	<a href="javascript:;" title="3" class="page_num page_txt">3</a>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==3">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<a href="javascript:;" title="2" class="page_num page_txt">2</a>
											    	 	<span class="page_cur">3</span>
										     			<span class="page_extend">...</span>
										     			<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==deliveredpaginationOrderPrint.totalPages">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages - 2'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages - 2'/></a>
											    	 	<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages - 1'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages - 1'/></a>
											    	 	<span class="page_cur"><s:property value='deliveredpaginationOrderPrint.totalPages'/></span>
											    	 </s:if>
											    	 <s:if test="currentPage==deliveredpaginationOrderPrint.totalPages-1">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages - 2'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages - 2'/></a>
											    	 	<span class="page_cur"><s:property value='deliveredpaginationOrderPrint.totalPages - 1'/></span>
											    	 	<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
											    	 <s:if test="currentPage==deliveredpaginationOrderPrint.totalPages-2">
											    	 	<a href="javascript:;" title="1" class="page_num page_txt">1</a>
											    	 	<span class="page_extend">...</span>
											    	 	<span class="page_cur"><s:property value='deliveredpaginationOrderPrint.totalPages - 2'/></span>
											    	 	<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages - 1'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages - 1'/></a>
											    	 	<a href="javascript:;" title="<s:property value='deliveredpaginationOrderPrint.totalPages'/>" class="page_num page_txt"><s:property value='deliveredpaginationOrderPrint.totalPages'/></a>
											    	 </s:if>
										     	</s:if>
										     	<s:else>
										     		<s:iterator value="deliveredpageList" var="pa" status="status">
											     		 <s:if test="currentPage!=#pa">
												    		<a href="javascript:;" title="<s:property value='pa'/>" class="page_num page_txt"><s:property value='pa'/></a>
												    	 </s:if>
												    	 <s:else>
												    	 	<span class="page_cur"><s:property value='pa'/></span>
												    	 </s:else>
											    	 </s:iterator> 
										     	</s:else>
										    
										     <s:if test='deliveredpaginationOrderPrint.totalPages > 5'>
										     	<s:if test="currentPage!=deliveredpaginationOrderPrint.totalPages">
										     		<a href="javascript:;" title="下一页" class="page_txt page_next">下一页</a>
									         	</s:if>
									         </s:if>
									       </s:if> 
	                                   	   <span> 共 <em><s:property value='deliveredpaginationOrderPrint.totalRecords'/></em> 条</span>
	                                   	   
											<input type="hidden" class="to_page_num page_txt" name="currentPage" value="<s:property value='currentPage'/>"/>
											<input type="hidden" class="start_date2_val" name="startDateDelivered" value="<s:property value='startDateDelivered'/>"/>
											<input type="hidden" class="end_date2_val" name="endDateDelivered" value="<s:property value='endDateDelivered'/>"/>
											<input type="hidden" class="orderby3_val" name="deliveredOrderByCol" value="<s:property value='deliveredOrderByCol'/>"/>
											<input type="hidden" class="show_num3_val" name="numOfPage" value="<s:property value='numOfPage'/>"/>
											<input type="hidden" class="sear_ship_input2_val" name="mailNoOrOrderNoDelivered" value="<s:property value='mailNoOrOrderNoDelivered'/>"/>
											<input type="hidden" class="select_val" name="customerCode" value="<s:property value='customerCode'/>"/>
											<input type="hidden" class="select2_val" name="customerCode2" value="<s:property value='customerCode2'/>"/>
											<input type="hidden" class="select3_val" name="customerCode3" value="<s:property value='customerCode3'/>"/>
											<!-- 左侧菜单选中样式 -->
											<input type="hidden" name="menuFlag" value="${menuFlag }" />
	                                   	 </form>  
	                                </div>
	                                <!-- E PageNavi -->
	                            </div>
	                        </div>
	                        <!-- E 已发货 tab -->
	                        
	                    </div>
                    </div>
                <!-- E Tab -->
            </div>

        <!-- #include file="公共模块/sidebar.html" -->
        </div>
        <!-- E Main -->

	
	<%-- <jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include> --%>
