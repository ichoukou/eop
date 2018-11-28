<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/tab.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/table.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog_dev.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/calendar/WdatePicker.js?d=${str:getVersion() }"></script>

<link rel="stylesheet" type="text/css" href="${cssPath}/base/reset.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/common/common.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/page/complaint.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/button.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/box.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
<link rel="stylesheet" type="text/css" href="${cssPath}/module/dialog.css?d=${str:getVersion() }" media="all" />

<!-- 当前页面css -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/user_customer_list.css" media="all" />
<script type="text/javascript">
	var aDialog;
	
	//全选全不选
	function synCheckbox(obj, containerId) {
		$("#"+containerId).find("input[type=checkbox]").each(function(i) {
			$(this).attr("checked", obj.checked);
		});
	}
	
	//翻页
	function toPage(cp){
		$("#currentPage").val(cp);
		$("#customerFrom").submit();
	}
	
	//查询
	function queryUserThread(){
		$("#currentPage").val(1);
		/* var reg =/^\d+$/g;
		if($("#siteCode").val()){
			if(!reg.test($("#siteCode").val())){
				//alert("请输入正确的网点代码,只能是数字！");
				aDialog = new Dialog();
				aDialog.init({
					closeBtn : true,
					contentHtml : '请输入正确的网点代码,只能是数字！',
					no:	function(){
						aDialog.close();
						$("#siteCode").val("");
						$("#siteCode").focus();
					}
				});

			//	$("#siteCode").val("");
			//	$("#siteCode").focus();
				return;
			}
		}
		var reg2 =/^[0-9a-zA-Z]*$/g;
		if($("#cusCode").val()){
			if(!reg2.test($("#cusCode").val())){
			//	alert("请输入正确的商家代码,只能是数字,字母或者数字和字母组成！");
				aDialog = new Dialog();
				aDialog.init({
					closeBtn : true,
					contentHtml : '请输入正确的商家代码,只能是数字,字母或者数字和字母组成！',
					no:	function(){
						aDialog.close();
						$("#cusCode").val("");
						$("#cusCode").focus();
					}
				});
				return;
			}
		} */
		
		$("#customerFrom").submit();
	}
	
	/**批量授权操作权限*/
	function operateBatchOpen(isOperate){
		var ids = "";
		var checkNum = 0;
		var noCheck = 0;
		var noOpen = 0;
		$("#tbTB_TBD").find("input[type=checkbox]").each(function(i) {
			if(this.checked){
				checkNum++;
				/**批量打开时，判断是否选择到已经开通的记录*/
				if(isOperate){
					if("1" != $(this).attr("state")){
						ids += this.value + ",";
						noCheck++;
					}
				}else{
					/**批量关闭时，判断是否选择到未开通的记录*/
					if("0"==$(this).attr("state")){
						noOpen++;
					}
					if("1"== $(this).attr("state")){
						ids += this.value + ",";
						noCheck++;
					}
				}
			}
		});
		
		var message= "请先选择要操作的商家！";
		if(checkNum>0){
			if(isOperate){
				//您本次想开通电子面单下载权限的N个商家中包含X个已开通状态的商家，故本次实际帮您开通Y个
				message = "您本次想开通电子面单下载权限的"+checkNum+"个商家中包含"+(checkNum-noCheck)+"个已开通状态的商家，故本次实际帮您开通"+noCheck+"个";
			}else{
				//您本次想关闭电子面单下载权限的N个商家中包含X个已关闭、Y个未开通状态的商家，故本次实际帮您关闭Z个”
				message = "您本次想关闭电子面单下载权限的"+checkNum+"个商家中包含"+(checkNum-noCheck-noOpen)+"个已关闭、"+noOpen+"个未开通状态的商家，故本次实际帮您关闭"+noCheck+"个";
			}
		}
		aDialog = new Dialog();
		aDialog.init({
			contentHtml: message,
			yes: function() {
				if(checkNum==0 ||noCheck==0){
					aDialog.close();
					return;
				}
				var isCanDownload = 2;
				if(isOperate){
					isCanDownload = 1;
				}
				$.post("user!updateIsCanDownload.action",{customerIDs:ids,isCanDownload:isCanDownload},function(response){
					aDialog.close();
					
					aDialog = new Dialog();
					aDialog.init({
						contentHtml: response.infoContent,
						yes: function() {
							aDialog.close();
							if(response.status){
								$("#customerFrom").submit();
							}
						}
					});
				}); 
			},
			no:	function(){
				aDialog.close();
			}
		});
	}
	
	/**单个开通/关闭权限*/
	function operateSimpleCustomer(customerId,isCanDownload){
		$.post("user!updateIsCanDownload.action",{customerIDs:customerId,isCanDownload:isCanDownload},function(response){
		 	var aDialog = new Dialog();
			aDialog.init({
				contentHtml: response.infoContent,
				yes: function() {
					aDialog.close();
					if(response.status){
						$("#customerFrom").submit();
					}
				}
			});
		}); 
		
		/* $.ajax({
			type : "post",
			dataType : "json",
			data : "{customerIDs:" + ids + ",isCanDownload:" + isCanDownload+"}",
			async:true,
			cache: false,
			url : 'user!updateIsCanDownload.action',
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
		}); */
	}
</script>

<div id="content">
	<div id="content_hd" class="clearfix">
		<em>易通系统还处于试运行阶段，迫切需要您的宝贵建议！<a href="send_openAdviseUI.action?menuFlag=msg_advise">我有想法</a></em>		
	</div>
	
	
	<div id="content_bd" class="clearfix">
		<div>
			<!-- S Box -->
            <div id="box_form" class="box box_a">
                <div class="box_bd">
					<s:form id="customerFrom" action="user!queryUserThread.action" method="post" theme="simple" >
						<input type="hidden" id="currentPage" value="<s:property value='currentPage'/>" name="currentPage"/>
		  				<input type="hidden" id="url" value='<s:property value="url"/>'>
	      				<p class="clearfix">
	                        <label>权限状态：</label>
	                        <select name="queryUserCondition.isCanDownload" class="select_text">
	                            <option value="-1" <s:if test="queryUserCondition.isCanDownload==-1">selected="selected"</s:if>>全部</option>
	                            <option value="0" <s:if test="queryUserCondition.isCanDownload==0">selected="selected"</s:if>>未开通</option>
	                            <option value="1" <s:if test="queryUserCondition.isCanDownload==1">selected="selected"</s:if>>已开通</option>
	                            <option value="2" <s:if test="queryUserCondition.isCanDownload==2">selected="selected"</s:if>>已关闭</option>
	                        </select>
						</p>
						<p class="clearfix">
							<label>网点代码：</label>
							<input id="siteCode" type="text" name="queryUserCondition.siteCode" class="input_text"  value="<s:property value='queryUserCondition.siteCode'/>">
							<label class="m-tar">商家代码：</label>
							<input id="cusCode" type="text" name="queryUserCondition.customerCode" class="input_text"  value="<s:property value='queryUserCondition.customerCode'/>">
							<label class="m-tar">商家名称：</label>
							<input id="cusName" type="text" name="queryUserCondition.customerName" class="input_text"  value="<s:property value='queryUserCondition.customerName'/>">
						</p>
						<p class="">
							<label>操作时间：</label>
							<input class="Wdate" type="text" name="queryUserCondition.startDate" id="date_start" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true})" value="<s:date format="yyyy-MM-dd" name="queryUserCondition.startDate"/>">　至　
							<input class="Wdate" type="text" name="queryUserCondition.endDate" id="date_end" onfocus="WdatePicker({maxDate:'%y-%M-%d',isShowClear:false,readOnly:true,doubleCalendar:true,startDate:'#F{$dp.$D(\'date_start\')}',minDate:'#F{$dp.$D(\'date_start\')}'})" value="<s:date format="yyyy-MM-dd" name="queryUserCondition.endDate"/>">
							<span id="date_tip"></span>
                           	<a id="sear_btn" class="btn btn_a" title="查 询" href="javascript:queryUserThread();"><span>查 询</span></a>
                            <a class="btn btn_a" title="批量开通" href="javascript:operateBatchOpen(true);"><span>批量开通</span></a>
                            <a class="btn btn_a" title="批量关闭" href="javascript:operateBatchOpen(false);"><span>批量关闭</span></a>
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
							<th class="th_a first_th"><div class="th_title"><em><input type="checkbox" onclick="synCheckbox(this, 'tbTB_TBD')" title="全选/全不选" /></em></div></th>
							<th class="th_b"><div class="th_title"><em>商家代码</em></div></th>
							<th class="th_c"><div class="th_title"><em>商家名称</em></div></th>
							<th class="th_d"><div class="th_title"><em>所属网点代码</em></div></th>
							<th class="th_e"><div class="th_title"><em>权限状态</em></div></th>
							<th class="th_f"><div class="th_title"><em>操作</em></div></th>
							<th class="th_g last_th"><div class="th_title"><em>最后操作时间</em></div></th>
						</tr>
					</thead>
					<tbody id="tbTB_TBD">
						<s:iterator value="#request.userThreadList" status="stuts" var="usrThread">
							<tr class="list_tr">
								<td class="th_a"><input type="checkbox" value="<s:property value='#usrThread.id'/>"  state="<s:property value="#usrThread.isCanDownload"/>"/></td>
								<td class="th_b"><s:property value="#usrThread.userCode"/></td>
								<td class="th_c"><s:property value="#usrThread.userName"/></td>
								<td class="th_d"><s:property value="#usrThread.siteCode"/></td>
								<td class="th_e">
									<s:if test="#usrThread.isCanDownload!=null && #usrThread.isCanDownload==1">已开通</s:if>
									<s:if test="#usrThread.isCanDownload!=null && #usrThread.isCanDownload==2">已关闭</s:if>
									<s:if test="#usrThread.isCanDownload==null || #usrThread.isCanDownload==0">未开通</s:if>
								</td>
								<td class="th_f">
									<s:if test="#usrThread.isCanDownload!=null && #usrThread.isCanDownload==1">
										<a href="javascript:operateSimpleCustomer('<s:property value="#usrThread.id"/>','2');">关闭</a>
									</s:if>
									<s:else>
										<a href="javascript:operateSimpleCustomer('<s:property value="#usrThread.id"/>','1');">开通</a>
									</s:else>
								</td>
								<td class="th_g"><s:date format="yyyy-MM-dd HH:mm:ss" name="#usrThread.operateDate"/></td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
			</div>
			
			<div class="table_footer clearfix">
				<div class="pagenavi">
					<s:if test="userPagination.totalPages > 1">
						<a value="1" href="javascript:toPage(1);" class="page_txt">&laquo; 首页</a>
					</s:if>
					
					<s:if test="%{currentPage > 1}"><a value='${currentPage-1 }' href="javascript:toPage(${currentPage-1});" class="page_txt">上一页</a></s:if>
					
					<s:if test="%{userPagination.pageIndex.startIndex > 1}">...</s:if>
					
					<s:iterator begin="userPagination.pageIndex.startIndex" end="userPagination.pageIndex.endIndex" var="vp">
						<s:if test="currentPage == #vp">
							<span class="page_cur" title="<s:property value="#vp"/>"><s:property value="#vp"/></span>
						</s:if>
						<s:else>
							<a value=<s:property value="#vp"/> href="javascript:toPage(<s:property value="#vp"/>);" class="page_num" title="<s:property value="#vp"/>"><s:property value="#vp"/></a>
						</s:else>
					</s:iterator>
					
					<s:if test="%{userPagination.pageIndex.endIndex < userPagination.totalPages}">...</s:if>
					
					<s:if test="%{currentPage < userPagination.totalPages}"><a value='${currentPage+1 }' href="javascript:toPage(${currentPage+1});" class="page_txt">下一页</a></s:if>
					
					<s:if test="userPagination.totalPages > 1">
						<a value=<s:property value='userPagination.totalPages'/> href="javascript:toPage(<s:property value='userPagination.totalPages'/>);" class="page_txt">末页 &raquo;</a>
					</s:if>
					
					<span class="page_total">共 <em><s:property value="userPagination.totalPages"/></em> 页/<em><s:property value="userPagination.totalRecords"/></em> 条</span>
				</div>
			</div>
		</div>
	</div>
</div>	

