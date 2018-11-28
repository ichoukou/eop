/**
 *在StoreAdmin常用的脚本
 */
function getCurrentUserId(){
	return getCookie("UID")||-2;
}
/**不严格的简单判断用户是否登录的方法，性能较好，缺省设置为5分钟更新一次*/
function isLogined() {
	return getCurrentUserId()>0;
}
function getCurrentUserName(loginRequired) {
	return (!loginRequired||isLogined())&&getCookie("UNAME")||"";
}


function checkAll(theForm) { // check all the checkboxes in the list
	for (var i=0;i<theForm.elements.length;i++) {
    	var e = theForm.elements[i];
		var eName = e.name;
    	if (eName != 'allbox' && (e.type.indexOf("checkbox") == 0)) {
        	e.checked = theForm.allbox.checked;		
		}
	} 
}
/*  This function is to select all options in a multi-valued <select> */
function selectAll(listId) {
	var list = document.getElementById(listId);
	for (var i = 0; i < list.options.length; i++) {
		list.options[i].selected = true;
	}
}

/**
 * when an input field value changed, check the coresponding multiIds checkbox.
 */
function fnSelectItemById(idValue) {
	if (idValue) {
		var idCheckboxs=document.getElementsByName("multiIds");
		for (i=0;i<idCheckboxs.length;i++) {
			idCheckboxs[i].checked=idCheckboxs[i].value==idValue;
		}
	}
}
/*批量操作的时候选择的记录的名称，需要设置title*/
function fnGetSelectedItemNames() {
	var itemNames="";
	var tmpDlm="";
	var wrapCount = 0;
	var idCheckboxs=document.getElementsByName("multiIds");
	for (i=0;i<idCheckboxs.length;i++) {
		if (idCheckboxs[i].checked) {
			itemNames+=tmpDlm+(idCheckboxs[i].title?idCheckboxs[i].title:idCheckboxs[i].value);
			wrapCount++;
			if(wrapCount >= 5){
				tmpDlm=",\n";
				wrapCount = 0;
			}else{
				tmpDlm=",";
			}
			
		}
	}
	return itemNames;
}

function fnOpenPrintPage(curActionName) {
	if ($j("form.mainForm")) {
		var frm = $j("form.mainForm").get(0);
		var frmAction = frm.action;
		frm.target = "_blank";
		frm.action = frmAction + (frmAction.match(/\?/) ? "&" : "?") + "printable=true";
		fnSubmitActionForm(frm, curActionName);
		frm.target = "";
		frm.action = frmAction;
	}
}

function fnSubmitActionForm(oForm,sActionName) {
	//先过滤在form.action上的doAction参数。
	oForm.action = removeParamFromUrl(oForm.action,"doAction");
	if (oForm.doAction) {
		oForm.doAction.value = sActionName;
	} else {
		fnAddHiddenField("doAction",sActionName,oForm);
	}
	oForm.submit();
}

function fnChangeLanguage(Lcode){
	location.href=fnAppendUrl(location.href,"preferredLocale",Lcode);
}

function fnGetCheckedValue(radioName) { 
	var radios=document.getElementsByName(radioName);
	var val="";
  for (var i=0;i<radios.length;i++) {
  	if(radios[i].checked){
		val=radios[i].value;
		break;
  	}
  }
	return val;  
}

function checkAllByName(theForm,idName) { // check all the checkboxes in the list
	for (var i=0;i<theForm.elements.length;i++) {
    	var e = theForm.elements[i];
		var eName = e.name;
    	if (eName != 'allbox' && (e.type.indexOf("checkbox") == 0) && eName == idName) {
        	e.checked = theForm.allbox.checked;		
		}
	} 
}

function highlightTableRows(tableId) {
	$j('#'+tableId+' tr').hover(
		function(){
			$j(this).addClass("on");
			$j(this).next('.sub').addClass("on");
			if($j(this).hasClass("sub")){
				$j(this).prev().addClass("on");
			}
		},
		function(){
			$j(this).removeClass("on");
			$j(this).next('.sub').removeClass("on");
			if($j(this).hasClass("sub")){
				$j(this).prev().removeClass("on");
			}
		}
	);
}
//处理查询
function doSearchHandler() {
	var searchForm=document.getElementById("searchForm");
	if (validateForm(searchForm))
	{
		searchForm.submit();
		return true;
	}
	else
	{
		alert(__vaMsg.notPass);
		return false;
	}
}

function doSearchHandlerWhenEnter(ievent){
	var event = window.event||ievent;
	if( event.keyCode == 13 ){
	 	doSearchHandler();
  	}
  	return false;
}

//以下是新框架的Form提交方法
/* 通过doAction提交的方法,暂时obj是没有用的 */
function fnDoAction(obj,actionName,confirmMsg) {
	alert("ss");
	var form=$("form.mainForm").get(0);	
	if (form && !bCancel) {
		var __onsubmit= form.onsubmit;
		var result=true;
		if (typeof __onsubmitt=="string") {
			result=eval(__onsubmit);
		} else if (typeof __onsubmit=="function") {
			result=__onsubmit.call(form);
		}
		if (result==false) {
			return false;
		}
	}	
	if (!confirmMsg) {
		fnSubmitActionForm(form,actionName);
	}else{
		jConfirm(confirmMsg, null, function(r){if(r) fnSubmitActionForm(form,actionName);});
	}
	return false;
}

function fnDoSave(obj,nameFieldId) {
	bCancel=false;
	var entityName="";
	if (nameFieldId && $(nameFieldId) && $(nameFieldId).value) {
		entityName = "："+$(nameFieldId).value;
	}
	return fnDoAction(obj, "save"/*, __FMT.common_message_confirmSaveThis+entityName+"?"*/);
}

function fnDoSaveAndNext(obj,nameFieldId) {
	bCancel=false;
	var entityName="";
	if (nameFieldId && $(nameFieldId) && $(nameFieldId).value) {
		entityName = $(nameFieldId).value;
	}
	return fnDoAction(obj, "saveAndNext"/*, __FMT.common_message_confirmSaveThis+entityName+"?"*/);
}

function fnDoDelete(obj,nameFieldId) {
	bCancel=true;
	var entityName="";
	if (nameFieldId && $(nameFieldId) && $(nameFieldId).value) {
		entityName = $(nameFieldId).value;
	}
	return fnDoAction(obj, "delete", __FMT.common_message_confirmDeleteThis+ " " + entityName+"?");
}

function fnDoSimpleAction(obj,actionName,idValue) {
	bCancel=true;
	fnSelectItemById(idValue);
	return fnDoAction(obj, actionName);
}

function fnDoCancelForm(obj) {
	bCancel=true;
	return fnDoAction(obj, "cancelForm");
}

function fnDoUpToParent(objOrUrl) {
	bCancel=true;
	if (typeof objOrUrl=="string") {
		location.href=objOrUrl;
		return false;
	}
	return fnDoAction(objOrUrl, "upToParent");
}

function fnDoAdd(obj) {
	return fnDoAction(obj, "add");
}
function fnDoMultiSave(obj) {
	return fnDoAction(obj, "multiSave", __FMT.common_message_confirmSave);
}
function fnDoMultiDelete(obj, showItemNames/*true or false, default is true*/) {
	var itemNames = fnGetSelectedItemNames();
	if (itemNames=="") {
		jAlert(__FMT.common_multiDelete_pleaseSelect);
		return false;
	}
	if(showItemNames==null || showItemNames)
		return fnDoAction(obj, "multiDelete", __FMT.common_message_confirmDeleteThis+ " " + itemNames+"?");
	else
		return fnDoAction(obj, "multiDelete", __FMT.common_message_confirmDeleteThis + "?");
		
}
function fnDoReturnToSearch(obj) {
	bCancel=true;
	//document.forms[0].action=obj.getAttribute("savedUri");
	return fnDoAction(obj, "returnToSearch");
}
function fnDoNextItem(obj) {
	bCancel=true;
	return fnDoAction(obj, "nextItem");
}
function fnDoPrevItem(obj) {
	bCancel=true;
	return fnDoAction(obj, "prevItem");
}

//set dwr Loading Msg.
var loadingMessage = "Loading...";
 
/**
 * 当ajax在加载的时候，会提示这个loading，并锁屏。
 */
function fnShowLoading()
{

	var disabledZone = $('disabledZone');
    if (!disabledZone) {
      disabledZone = document.createElement('div');
      disabledZone.setAttribute('id', 'disabledZone');
      disabledZone.style.position = "absolute";
      disabledZone.style.zIndex = "1000";
      disabledZone.style.left = "0px";
      disabledZone.style.top = "0px";
      disabledZone.style.width = "100%";
      disabledZone.style.height = "100%";
      document.body.appendChild(disabledZone);
      var messageZone = document.createElement('div');
      messageZone.setAttribute('id', 'messageZone');
      messageZone.style.position = "absolute";
      messageZone.style.top = "0px";
      messageZone.style.right = "0px";
      messageZone.style.background = "red";
      messageZone.style.color = "white";
      messageZone.style.fontFamily = "Arial,Helvetica,sans-serif";
      messageZone.style.padding = "4px";
      disabledZone.appendChild(messageZone);
      var text = document.createTextNode(loadingMessage);
      messageZone.appendChild(text);
    }
    else {
      $('messageZone').innerHTML = loadingMessage;
      disabledZone.style.visibility = 'visible';
    }
}
function fnHideLoading()
{
	var disabledZone = document.getElementById('disabledZone');
    if (disabledZone) {
		disabledZone.style.visibility = 'hidden';
	}
}

//显示提示信息,V3.0及以前版本，isErrMsg参数对应persist, persist代表是否加入最近“浏览历史”
var sysMsgTimeOutHandler;
function sysMsg(sMsg/*String*/,isErrMsg/*Boolean*/) {
	clearTimeout(sysMsgTimeOutHandler);
	if(isErrMsg){
		$j('#errorMsgBar').empty().append("<p class='cont'>"+sMsg+"</p>").show();
		msgTimeOutHandler = setTimeout(sysMsgFadeOut,8000);
	}else{
		$j('#successMsgBar').empty().append("<p class='cont'>"+sMsg+"</p>").show();
		msgTimeOutHandler = setTimeout(sysMsgFadeOut,8000);
	}
	
}
function sysMsgFadeOut(){
	$j('#errorMsgBar,#successMsgBar').fadeOut(2000);
}


/*
disable toolbar button
*/
function disableIconBtn(id/*iconBtn  id, not null*/){
	if(id){
		var btnObj = $j("#"+id);
		if(btnObj.attr("onclick"))
			btnObj.removeAttr("onclick").removeAttr("onmouseover").removeAttr("onmouseout").removeAttr("onmousedown").removeAttr("onmouseup");
		btnObj.removeClass("on").addClass("disabled").unbind("click").unbind("mouseover").unbind("mouseout").unbind("mousedown").unbind("mouseup");
	}
}
/*
enable toolbar button
*/
function enableIconBtn(id/*iconBtn  id, not null*/){
	if(id){
		var btnObj = $j("#"+id);
		if(btnObj.hasClass("disabled")){
			btnObj.removeClass("disabled")
					.bind("click", function(){eval(btnObj.attr("originalOnclick"))})
					.bind("mouseover", function(){btnObj.addClass("on")})
					.bind("mouseout", function(){btnObj.removeClass('on')})
					.bind("mousedown", function(){btnObj.addClass("click")})
					.bind("mouseup", function(){btnObj.removeClass('click');btnObj.addClass('on')});
		}
	}
}

function isIconBtnEnable(id){
	return $j("#"+id).hasClass("disabled");
}

/*
 * 重置form表单里的元素
 * @param formWrapId 要清里的表单元素外层元素ID，如from的id或div的id（div里面包含有表单元素）
 * @since V3.5
 */
function fnResetForm(formWrapId){
		$j('#'+formWrapId+' :input').each(function() {
				var type = this.type, tag = this.tagName.toLowerCase();
				if (type == 'hidden' || type == 'text' || type == 'password' || tag == 'textarea'){
					this.value = '';
				}else if (type == 'checkbox' || type == 'radio'){
			       this.checked = false;
			    }else if (tag == 'select'){
			        if(this.size && this.size>=2)
			        	this.selectedIndex = -1;
			        else
			        	this.selectedIndex = 0;
			    }
		});
}


/*
 * 功能菜单显示与隐藏
 */
function fnToggleMenu(){
	if($j("body").hasClass("close_left")){
		$j("body").removeClass('close_left');
		setCookie("SIDE_MENU_SHOW","1");
	}else{
		$j("body").addClass('close_left');
		setCookie("SIDE_MENU_SHOW","0");
	}
}

function fnUpdateSize(){
	//调整底部高度
	var height= $j(document).height()-$j("#header").height()-$j("#footer").height()-17;
	$j("#sidebar").css("height", height+"px");
	$j("#footer").show();
	//调整主区宽度
	var width =$j(window).width() - 157;
	if(width <=1150){
		$j('.workground_wrap').css('width','1150px');
		$j('#header').css('width','1317px');
		$j('#footer').css('width','1317px');
	}else{
		$j('.workground_wrap').css('width','');
		$j('#header').css('width','');
		$j('#footer').css('width','');
	}
	//显示按钮
	$j('.toolbar .content').show();
}

/*在原有url添加参数的简便方法*/
function fnAppendUrl(url,paramName,paramValue) {
	if (!url || !paramName || !paramValue) {
		return url;
	}
	var tmpUrl = removeParamFromUrl(url,paramName);
	return tmpUrl + (tmpUrl.indexOf('?') > -1 ? '&' : '?') + paramName+"="+paramValue;
}
/*在原有url删除指定参数的简便方法*/
function removeParamFromUrl(url, paramName) {
	return url.replace(new RegExp("("+paramName+"=[^&]*[&])|([?|&]"+paramName+"=[^&]*$)"), "");
}
/*动态创建指定类型的组件的简便方法*/
function createFormElement(parent,tagName, id, name, type, value) {
    var e = document.createElement(tagName);
    e.setAttribute("id", id||name);
    e.setAttribute("name", name||id);
    e.setAttribute("type", type);
    e.setAttribute("value", value);
    parent.appendChild(e);
    return e;
}
/*在指定或第一个form添加一个隐藏字段的简便方法*/
function fnAddHiddenField(fieldName,fieldValue,form) {
	for (var i = 0; i < form.elements.length; i++) {
		if (form[i].type in {button:0,submit:0,reset:0,image:0,file:0}) continue;
      	if (fieldName == form[i].name) {
        	form[i].value=fieldValue;
			return form[i];
      	} 
	}
	return createFormElement(form||document.forms[0],"INPUT",null,fieldName,"hidden",fieldValue);
}

/* 分析日期,并返加日期对象 */
function fnPaserDate($str)
{
	var var_d1 = new Date();
	var_d1.setMonth($str.substr(__defDatePattern.indexOf("MM"), 2) - 1);
	var_d1.setDate($str.substr(__defDatePattern.indexOf("dd"), 2));
	var_d1.setFullYear($str.substr(__defDatePattern.indexOf("yyyy"), 4));
	var_d1.setHours(0);
	var_d1.setMinutes(0);
	var_d1.setSeconds(0);
	var_d1.setMilliseconds(0);
	return var_d1;	
}
/*This function is used to compare date*/
function fnCompareDate(d1,d2){
	if(!d1 || !d2) {
		return;
	}
	var dd1=fnPaserDate(d1).getTime();
	var dd2=fnPaserDate(d2).getTime();
	return dd1-dd2;
}

function getCurrListHeight(){
	var height = getCookie("LIST_PAGE_HEIGHT");
	$('sidebar').style.height =height;
}

function setCurrListHeight(){
	setCookie("LIST_PAGE_HEIGHT",$j("#sidebar").css("height"));
}
