<%@ page language="java" pageEncoding="UTF-8"contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp"%>
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/filter_keys.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>修改短信模板</title>

		<!-- S Content -->
		<div id="content">
			<div id="content_hd" class="clearfix">
<!-- 				<h2 id="message_icon">过滤关键词</h2> -->
			</div>
			<div id="content_bd" class="clearfix">
				<!-- S Box -->
				<div class="box box_a">
					<div class="box_bd">
						<div id="filter_form">
							<p>
								<input type="hidden" class="input_text" id="filterRuleId" value="${filterRule.filterRuleId }"/>
								<input type="text" class="input_text" id="keywords" />
								<span id="keywordsTip"></span>
							</p>
							<p>
								<a href="javascript:;" id="add_keyword" class="btn btn_a" title="添加"><span>添 加</span></a>
								<a href="javascript:;" id="del_keyword" class="btn btn_a" title="删除"><span>删 除</span></a>
							</p>

							<h6>过滤关键词：</h6>
							<div class="box box_a">
								<div class="box_bd" id="key_textarea">
									${filterRule.words }
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- E Box -->
			</div>
			

<script>
	var params = {
		dialogSubmit : '?dialogSubmit',
		keywordsAction: ''				// 添加/删除关键词
	}
</script>

<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/common/mailDetail.js?d=${str:getVersion() }"></script>

<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/filter_keys.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->
			
		</div>
		<!-- E Content -->

