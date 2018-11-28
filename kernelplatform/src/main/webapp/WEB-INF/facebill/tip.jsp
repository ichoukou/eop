<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<%@ include file="/WEB-INF/common/taglibs.jsp"%>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>电子面单下载风险提示</title>
    <script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/css/tab.css" />
    <link rel="stylesheet" type="text/css" href="/css/base/reset.css" />
	<link rel="stylesheet" type="text/css" href="/css/common/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/module/button.css?d=v1.0.0" media="all" />
	<link rel="stylesheet" type="text/css" href="/css/module/dialog.css?d=v1.0.0" media="all" />
	<link rel="stylesheet" type="text/css" href="/css/module/box.css?d=v1.0.0" media="all" />
    <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
	<link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
    <link href="${jsPath}/module/calendar/skin/WdatePicker.css" rel="stylesheet" type="text/css">
    <!--------------当前页面css--------------->
    <link rel="stylesheet" type="text/css" href="${cssPath}/facebill/bill/tips.css" />
    <!--------------当前页面css--------------->
<script type="text/javascript">
	function formSubmit(){
		if($("#isNeedCloseRemind").prop("checked")==false){//未选中
			$("#needRemind").val("0");
		}else{
			$("#needRemind").val("1");
		}
		
		$("#alertMsgForm").trigger('submit');
		//$("#closeRemind").trigger('click');
	}
</script>
</head>
<body>
    <div  id="content">
        <div class="tip_content">
            <div class="tip_title clearfix">
                <h2 class="title">电子面单下载风险提示</h2>
                <a id="closeRemind" href="waybillExport_userInfoLoad.action?currentPage=1&menuFlag=dzmd_mdxz"><span class="close">关闭</span></a>
            </div>
            <div class="tip_text">
                <p> 如果分公司申请使用电子面单号下载并交给客户自行打印模式，将会给分公司带来以下风险：</p>
                <p>1.快件信息不能自动录单，需分公司把快件信息手工导入录单系统，否则快件在运输途中产生问题件，仲裁部不予受理；</p>
                <p>2.面单号导出交给客户自行打印时容易造成同一个面单号重复打印和或未使用面单号遗漏使用情况；</p>
                <p>以上风险由申请使用电子面单号下载并交给客户自行打印模式分公司自行承担，总公司不承担因分公司申请面单号导出使用所产生的任何责任。</p>
            </div>
            <div class="tip_bar clearfix">
                <div class="tip_fl red">风险提示关乎您的切身利益，请务必认真阅读！！！</div>
                <div class="tip_fr">
                <form id="alertMsgForm" method="post" action="waybillExport_waybillExport.action?currentPage=1&menuFlag=dzmd_mdxz">
                	<input type="hidden" id="cusCode" name="customerCode" value="${customerCode}"/>
                	<input type="hidden" id="cusName" name="customerName" value="${customerName}"/>
                	<input type="hidden" id="downNum" name="downloadBoxNum" value="${downloadBoxNum}"/>
                	<input type="hidden" id="isFromRemind" name="isFromRemindPage" value="1">
                	<input type="hidden" id="needRemind" name="needCloseRemind" value=""/>
                    <p>
                        <input type="checkbox" id="isNeedCloseRemind"/>
                        <label>下次不再提示</label>
                    </p>
                </form>
                    <p>
                        <a href="javascript:" onclick="formSubmit();" class="btn btn_a">
                            <span>我知道了</span>
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>