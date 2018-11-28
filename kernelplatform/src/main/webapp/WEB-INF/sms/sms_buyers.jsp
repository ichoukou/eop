<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
	<!-- S 当前页面 CSS -->
	<link rel="stylesheet" type="text/css" href="${cssPath}/page/user_manage.css?d=${str:getVersion() }" media="all" />
	<!-- E 当前页面 CSS -->

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
				<h2 id="message_icon">客户管理</h2>
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S 会员统计 -->
					<div class="statistics clearfix">
						<div class="image">
							<img id="chart" src="" />
						</div>
						<div class="user">
							<dl>
								<dt>
									<em>总会员数</em>
									<strong>
										<span class="total"><s:property value="totalBuyers"/></span>
									</strong>
									<span> 名</span>
								</dt>
								
							</dl>
						</div>
					</div>
				<!-- E 会员统计 -->
				<!-- S 会员操作 -->
					<div class="opts">
						<a href="buyers!searchBuyersList.action?menuFlag=buyers_manage" class="btn btn_a" title="会员查询">
							<span>会员查询</span>
						</a>
						<a href="javascript:;" class="btn btn_a import" title="导入会员信息">
							<span>导入会员信息</span>
						</a>
						<input type="hidden" id="bindedUserList" value="<s:property value='bindedUserList'/>" name="bindedUserList">
						<a href="buyersgetBuyerByUserId.action?menuFlag=buyers_manage" class="btn btn_a set" title="设置会员等级">
							<span>设置会员等级</span>
						</a>
						<a href="buyers!toAdd.action?menuFlag=buyers_manage" class="btn btn_a" title="添加新会员">
							<span>添加新会员</span>
						</a>
					</div>
			   <!-- E 会员操作 -->
				<!-- S 导入会员弹框 -->
					<!--form id="import_fm" action="#" method="GET">
					<div class="import">
						<p class="choose">
							<label>请选择要导入的文件</label>
                     <a href="#">怎样导入？</a>
							<a href="#">下载导入模板</a>
						</p>
						<p class="file">
							<label>上传文件：</label>
							<input type="file" name="import" />
						</p>
						<p class="shop">
							<select name="shops">
								<option value="所属店铺1">所属店铺1</option>
								<option value="所属店铺2">所属店铺2</option>
								<option value="所属店铺3">所属店铺3</option>
							</select>
						</p>
						<p class="note">
							<dl>
								<dt>当CSV文件的会员存在你的其它店铺时，请选择：</dt>
								<dd>
									<input type="radio" name="note" checked />
									<span>删除原来的会员信息，保存到现在选择的店铺</span>
								</dd>
								<dd>
									<input type="radio" name="note" />
									<span>保留原来的会员信息，不放到现在选择的店铺</span>
								</dd>
							</dl>
						</p>
					</div>
					</form-->
				<!-- E 导入会员弹框 -->
	
			</div>
			
	<script type="text/javascript">
		var params = {
			importFileErrType:'<s:property value="uploadErrType"/>',
			importFileErrList:[
				<c:forEach var ="error" items="${uploadErrlist}" varStatus="status">
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
			shopNames:'<s:property value="shopNames"/>',
			shopKey :'<s:property value="shopKey"/>',
		    downLoadTemplateUrl: 'buyersdownLoadZip.action',   // 下载导入模板 Url
			howtoImportfUrl: 'buyersdownLoadGuide.action',       // 怎样导入？Url
			importAction: 'buyersupload.action',		      // 导入会员信息 Action
			setAction: ''              // 设置会员等级 Action 
		};
	</script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#chart").attr("src","userManageViewPie.action");
		});
	</script>
<!-- 	<script type="text/javascript">
	var params = {
		onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
			? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
		userId:${yto:getCookie('id')},						//当前登录用户的id
		userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
		infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
		bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode='			// 绑定客户编码表单 action 
	}
	</script> -->

	
	<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
	<!-- S 当前页面 JS -->
	<script type="text/javascript" src="${jsPath}/page/user_manage.js?d=${str:getVersion() }"></script>
	<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->
	