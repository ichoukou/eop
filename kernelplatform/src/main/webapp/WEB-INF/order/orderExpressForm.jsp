<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
 <title>面单模板</title>
<%@ page import  = "javax.xml.parsers.DocumentBuilder,
         javax.xml.parsers.DocumentBuilderFactory,
         javax.xml.parsers.*,
         javax.xml.transform.*,
         javax.xml.transform.dom.DOMSource,
         javax.xml.transform.stream.StreamResult,
         org.w3c.dom.*,
         java.io.*
         "%>
<style>
    form label{display:inline;}
	.combo-panel{height:auto !important;}
#orderExpressaddFrom .form_row{overflow:hidden;clear:both;*zoom:1;margin-bottom:15px;}
#orderExpressaddFrom .form_row_l select{width:150px;margin-right:10px;}
#orderExpressaddFrom .form_row_l .btn{padding-right:0;}
#orderExpressaddFrom .form_row label{padding:0;color:#333;font-weight:400;vertical-align:middle;cursor:pointer;}
#orderExpressaddFrom .form_row #lock_bg{vertical-align:middle;}
#orderExpressaddFrom .form_row_l{width:40%;float:left;}
#orderExpressaddFrom .form_row_r{width:60%;float:right;}
#orderExpressaddFrom .form_row_r .word_icon{width:22px;height:22px;display:inline-block;margin-right:2px;line-height:100px;overflow:hidden;background:url(../images/word_icon.png) no-repeat;vertical-align:middle;cursor:pointer;}
#orderExpressaddFrom .form_row_r select{vertical-align:middle;}
#orderExpressaddFrom .form_row_r #sysiconImg_b{background-position:0 0;}
#orderExpressaddFrom .form_row_r #sysiconImg_i{background-position:0 -22px;}
#orderExpressaddFrom .form_row_r #editor-icon-left{background-position:0 -44px;}
#orderExpressaddFrom .form_row_r #editor-icon-middel{background-position:0 -66px;}
#orderExpressaddFrom .form_row_r #editor-icon-right{background-position:0 -88px;}
.dialog_box{border:1px solid #A3C2F6;padding:10px;background:#fff;-moz-border-radius:5px;-khtml-border-radius:5px;-webkit-border-radius:5px;border-radius:5px;-moz-box-shadow:0 0px 5px #A3C2F6;-webkit-box-shadow:0 0px 5px #A3C2F6;box-shadow:0 0px 5px #A3C2F6;}
#express_tpl_editor{width:390px;position:absolute;right:244px;top:150px;}
#express_tpl_editor ul{overflow:hidden;clear:both;*zoom:1;}
#express_tpl_editor ul li{width:130px;line-height:1.4;float:left;margin-bottom:8px;}
#express_tpl_editor ul li input,#express_tpl_editor ul li label,#express_tpl_editor ul li .checkbox_fake_label{vertical-align:middle;float:none;margin:0;padding:0;}
#express_tpl_editor .dialog_ft{text-align:center;margin-top:10px;}
#confirm_btn .btn{*display:inline;}
#content_hd p.ts{background: url(".../../images/single/btn_ts.png") no-repeat scroll 0 8px #fcfae5;color:#333;}
</style>
<link rel="stylesheet" type="text/css" href="js/printtemp/easyui.css?d=${str:getVersion() }">
<link rel="stylesheet" type="text/css" href="js/printtemp/icon.css?d=${str:getVersion() }">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/yto.tld" prefix="yto" %>
 <meta charset="UTF-8" />
 <link rel="stylesheet" type="text/css" href="${cssPath}/module/tab.css?d=${str:getVersion() }" media="all" />
 <link rel="stylesheet" type="text/css" href="${cssPath}/module/table.css?d=${str:getVersion() }" media="all" />
 <link rel="stylesheet" type="text/css" href="${cssPath}/module/pagenavi.css?d=${str:getVersion() }" media="all" />
  <script type="text/javascript">
	var params = {
			onStep: ${(yto:getCookie('userType') == 1 && yto:getCookie('userState') == 'TBA') 
				? (yto:getCookie('infostate') != 1 ? 1 : (yto:getCookie('infostate') == 1 ? 2 : 3)) : 3},								// “开始使用” == 1，“绑定网点” == 2
			userId:${yto:getCookie('id')},						//当前登录用户的id
			userName:"${yto:getCookie('userName')}",			//当前登录用户的账号
			infoFormAction: '${ctxPath}/noint!bindTaoBaoUserStep1.action',			// 填写信息表单 action
			bindFormAction: '${ctxPath}/noint!bindTaoBaoUserStep2.action?user.userCode=',			// 绑定客户编码表单 action
			pointsGetUrl: '${ctxPath}/user!getSiteInfo.action'// 获取对应网点 url
	};
</script>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/dialog.js?d=${str:getVersion() }"></script>
 <script type="text/javascript" src="${jsPath}/printtemp/jquery.easyui.min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/printtemp/swfobject.1-1-1.min.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/printtemp/swfobject.js?d=${str:getVersion() }"></script>

    <div id="content">
        <c:set var="xmlData">
            <printer picposition="0:0"><item><name>发货人-姓名</name><ucode>dly_name</ucode><font></font><fontsize>14</fontsize><fontspace>0</fontspace><border>1</border><italic>0</italic><align>left</align><position>132:126:91:24</position></item><item><name>网店名称</name><ucode>shop_name</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>189:154:219:23</position></item><item><name>发货人-地址</name><ucode>dly_address</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>143:180:266:68</position></item><item><name>发货人-邮编</name><ucode>dly_zip</ucode><font></font><fontsize>12</fontsize><fontspace>8</fontspace><border>0</border><italic>0</italic><align>left</align><position>323:249:91:20</position></item><item><name>√</name><ucode>tick</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>181:270:26:21</position></item><item><name>收货人-姓名</name><ucode>ship_name</ucode><font></font><fontsize>14</fontsize><fontspace>0</fontspace><border>1</border><italic>0</italic><align>left</align><position>488:126:101:24</position></item><item><name>收货人-地址</name><ucode>ship_addr</ucode><font></font><fontsize>14</fontsize><fontspace>0</fontspace><border>1</border><italic>0</italic><align>left</align><position>490:181:293:68</position></item><item><name>收货人-电话</name><ucode>ship_tel</ucode><font></font><fontsize>14</fontsize><fontspace>0</fontspace><border>1</border><italic>0</italic><align>left</align><position>658:124:122:20</position></item><item><name>订单-物品数量</name><ucode>order_count</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>339:316:75:54</position></item><item><name>测试内容-物品名称</name><ucode>text</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>center</align><position>75:330:207:21</position></item><item><name>订单-备注</name><ucode>order_memo</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>483:393:289:32</position></item><item><name>收货人-地区2级</name><ucode>ship_area_2</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>1</border><italic>0</italic><align>left</align><position>480:251:73:21</position></item><item><name>当日日期-年</name><ucode>date_y</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>474:371:42:22</position></item><item><name>当日日期-月</name><ucode>date_m</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>532:371:29:20</position></item><item><name>当日日期-日</name><ucode>date_d</ucode><font></font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>584:371:26:21</position></item><item><name>收货人-邮编</name><ucode>ship_zip</ucode><font></font><fontsize>12</fontsize><fontspace>8</fontspace><border>0</border><italic>0</italic><align>left</align><position>672:251:112:21</position></item><item><name>发货人-电话</name><ucode>dly_tel</ucode><font>undefined</font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>289:122:120:20</position></item><item><name>发货人-手机</name><ucode>dly_mobile</ucode><font>undefined</font><fontsize>12</fontsize><fontspace>0</fontspace><border>0</border><italic>0</italic><align>left</align><position>289:138:120:20</position></item><item><name>收货人-手机</name><ucode>ship_mobile</ucode><font>undefined</font><fontsize>14</fontsize><fontspace>0</fontspace><border>1</border><italic>0</italic><align>left</align><position>658:144:124:20</position></item></printer>
        </c:set>
        
		<div class="clearfix" id="content_hd">
<!-- 		  <h2 id="message_icon">面单模板</h2> -->
		  <em>自定义调整模板格式！</em>
		  <p style="display:black;font-size:12px; height:26px;line-height:26px;background-color:#fcfae5; margin-top:10px;" class="ts">填写面单名称→选择背景图→添加内容项→保存</p>
		</div>
        

        <div id="midfrom">

            <form id="orderExpressaddFrom" name="orderExpressaddFrom" action="waybill_orderExpress.action" method="post">
            	<!-- 左侧菜单选中样式 -->
				<input type="hidden" name="menuFlag" value="${menuFlag}" />
                <input type="hidden" id="id" name="orderExpress.id" value='<s:property value="orderExpress.id"/>'/> 
                <input type="hidden" name="width" id="width" value="1000"/> 
                <input type="hidden" name="height" id="height" value="600"/> 
                <input id="templateData" name="orderExpress.templatedata" type="hidden" value=''/>

                <div class="form_row">
                    <div class="form_row_l">
                        <select id="orderExpressName" name="orderExpressName" class="" style="">
                            <s:iterator value="orderExpList">
                                <option <s:if test="%{id == orderExpId}">selected</s:if> value="<s:property value='id'/>"><s:property value="orderexpressname"/></option>
                            </s:iterator>
                        </select>
                        <a href="waybill_orderExpress.action?new_template=1&menuFlag=${menuFlag}">新增模板</a>
                    </div>
                    <div class="form_row_r">
                        <input type="hidden" name="backgroundImageUrl" id="expressEditorBg" value="<s:property value='orderExpress.backgroundimageurl'/>" />
                        <select type="button" name="addbg" id="addbgid" value="选择背景图片" class="text-input">
	                        <option value="1" name="menubg">圆通速递（默认）</option>
	                        <option value="2" name="menubg">圆通速递（到付）</option>
	                        <option value="3" name="menubg">圆通速递（VIP）</option>
	                        <option value="4" name="menubg">圆通速递（国际）</option>
	                        <option value="5" name="menubg">圆通速递（2013）</option>
                        </select>
                        <input type="checkbox" id="lock_bg" checked="true" /> <span class="checkbox_fake_label">锁定背景图</span>
                    </div>
                </div>
                <div class="form_row">
                    <div class="form_row_l">
                        <span id="add_item_btn" class="btn btn_a"><input type="button" value="添加内容项"></span>
                        <span id="del_item_btn" class="btn btn_a"><input type="button" value="删除选中项"></span>
                    </div>
                    <div class="form_row_r">
                        <label>字体调整：</label>

                        <select	onchange="if(this.value!='--')ExpressTplEditor.setFontSize(this.value);"  id="fontSize" name="fontSize">
                            <option value="--">
                                大小
                            </option>
                            <option value="10">
                                10
                            </option>
                            <option value="12">
                                12
                            </option>
                            <option value="14">
                                14
                            </option>
                            <option value="18">
                                18
                            </option>
                            <option value="20">
                                20
                            </option>
                            <option value="24">
                                24
                            </option>
                            <option value="27">
                                27
                            </option>
                            <option value="30">
                                30
                            </option>
                            <option value="36">
                                36
                            </option>
                        </select>
                        <select onchange="if(this.value!='--') ExpressTplEditor.setFont(this.value);" id="font">
                            <option value="--">
                                字体
                            </option>
                            <option value="宋体">
                                宋体
                            </option>
                            <option value="黑体">
                                黑体
                            </option>
                            <option value="Arial">
                                Arial
                            </option>
                            <option value="Verdana">
                                Verdana
                            </option>
                            <option value="Serif">
                                Serif
                            </option>
                            <option value="Cursive">
                                Cursive
                            </option>
                            <option value="Fantasy">
                                Fantasy
                            </option>
                            <option value="Sans-Serif">
                                Sans-Serif
                            </option>

                        </select>
                        <select onchange="if(this.value!='--') ExpressTplEditor.setFontSpace(this.value);" id="fontSpace" name="fontSpace">
                            <option selected="selected" value="--">
                                间距
                            </option>
                            <option value="-4">
                                -4
                            </option>
                            <option value="-2">
                                -2
                            </option>
                            <option value="0">
                                0
                            </option>
                            <option value="2">
                                2
                            </option>
                            <option value="4">
                                4
                            </option>
                            <option value="6">
                                6
                            </option>
                            <option value="8">
                                8
                            </option>
                            <option value="10">
                                10
                            </option>
                            <option value="12">
                                12
                            </option>
                            <option value="14">
                                14
                            </option>
                            <option value="16">
                                16
                            </option>
                            <option value="18">
                                18
                            </option>
                            <option value="20">
                                20
                            </option>
                            <option value="22">
                                22
                            </option>
                            <option value="24">
                                24
                            </option>
                            <option value="26">
                                26
                            </option>
                            <option value="28">
                                28
                            </option>
                            <option value="30">
                                30
                            </option>
                        </select>
                        <span onclick="ExpressTplEditor.setBorder()" class="sysiconBtnNoIcon word_icon" id="sysiconImg_b">B</span>
                        <span onclick="ExpressTplEditor.setItalic()" class="sysiconBtnNoIcon word_icon" id="sysiconImg_i"><i>I</i></span>
                        <span class="sysiconImg_l word_icon" id="editor-icon-left" onclick="ExpressTplEditor.setAlign('left')">左对齐</span>
                        <span class="sysiconImg_m word_icon" id="editor-icon-middel" onclick="ExpressTplEditor.setAlign('center')">居中</span>
                        <span class="sysiconImg_r word_icon" id="editor-icon-right" onclick="ExpressTplEditor.setAlign('right')">右对齐</span>
                    </div>
                </div>
            </form>
        </div>
        
        <div style="position:relative">
			<span id="no_flash" style="display:none;font:700 14px/2 SimSun;margin:10px auto;">
				您还没有安装flash播放器,请点击<a target="_blank" href="http://www.adobe.com/go/getflash">这里</a>安装
			</span>
	        <div id="expressTplEditor" class="express_tpl_editor" style="width:840px;overflow:hidden"></div>
	        <div id="confirm_btn" class="btn_box" style="text-align:center;">
	        	<span id="add_item_btn" class="btn btn_a" onclick="ExpressTplEditor.save()"><input type="button" value="保 存 " /></span>
	            <span id="add_item_btn" class="btn btn_a" onclick="ExpressTplEditor.deleteOrderExpress()"><input type="button" value="删 除 " /></span>
	        </div>
	
	        <div id="express_tpl_editor" class="dialog_box" style="display:none;">
	            <div class="dialog_bd">
	                <ul class="clearfix">
	                    <li>
	                        <input type="checkbox" id="store_name" />
	                        <label for="store_name">网店名称</label>
	                    </li>
	                     <li>
	                        <input type="checkbox" id="dly_name" />
	                        <label for="dly_name">发货人-姓名</label>
	                    </li>
	                     <li>
	                        <input type="checkbox" id="shipment_no" />
	                        <label for="shipment_no">商品名称</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="ship_name" />
	                        <label for="ship_name">收货人-姓名</label>
	                    </li>
	                     <li>
	                        <input type="checkbox" id="dly_regionFullName" />
	                        <label for="dly_regionFullName">发货人-地区</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="order_no" />
	                        <label for="order_no">订单号</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="ship_tel" />
	                        <label for="ship_tel">收货人-电话</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="dly_tel" />
	                        <label for="dly_tel">发货人-电话</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="mail_no" />
	                        <label for="mail_no">物流号</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="ship_zip" />
	                        <label for="ship_zip">收货人-邮编</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="dly_zip" />
	                        <label for="dly_zip">发货人-邮编</label>
	                    </li>
	                     <li>
	                        <input type="checkbox" id="shipment_item_count" />
	                        <label for="shipment_item_count">发货单-物品数量</label>
	                    </li>
	                      <li>
	                        <input type="checkbox" id="ship_regionFullName" />
	                        <label for="ship_regionFullName">收货人-地区</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="dly_address" />
	                        <label for="dly_address">发货人-地址</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="tick" />
	                        <label for="tick">√</label>
	                    </li>
	                    <li>
	                    	<input type="checkbox" id="ship_address">
	                    	 <label for="ship_address">收货人-地址</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="dly_mobile" />
	                        <label for="dly_mobile">发货人-手机</label>
	                    </li>
	                     <li>
	                        <input type="checkbox" id="date_year" />
	                        <label for="date_year">当前日期-年</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="date_moth" />
	                        <label for="date_moth">当前日期-月</label>
	                    </li>
	                    <li>
	                        <input type="checkbox" id="date_day" />
	                        <label for="date_day">当前日期-日</label>
	                    </li>
	                     <li>
	                        <input type="checkbox" id="text" />
	                        <label for="text">自定义内容</label>
	                    </li>
	                </ul>
	            </div>
	            <div class="dialog_ft">
	                <span id="btn_y" class="btn btn_a"><input type="button" value="确 定" /></span>
	                <span id="btn_n" class="btn btn_a"><input type="button" value="取 消" /></span>
	            </div>
	        </div>
        </div>
<script type="text/javascript">

    //art.dialog.alert(isCheck);
    var ExpressTplEditor = {
        tplEditor:null,
        screenDPI:96,
        setBorder:function(){
            this.tplEditor.flash( function() { this.setBorder(); } );
        },
        setItalic:function(){
            this.tplEditor.flash( function() { this.setItalic(); } );
        },
        setFontSize:function(fontSize){        
            this.tplEditor.flash( function() { this.setFontSize(fontSize); } ); 
        },
        setAlign:function(align){
            this.tplEditor.flash( function() { this.setAlign(align); } ); 
        },
        setFontSpace:function(fontSpace){
            this.tplEditor.flash( function() { this.setFontSpace(fontSpace); } ); 
        },
        delItem:function(){
            this.tplEditor.flash( function() { this.delItem(); } ); 
        },
        resize:function(){
            var size = {width:$('ipt_prt_tmpl_width').value.toInt()*this.screenDPI/25.4,height:$('ipt_prt_tmpl_height').value.toInt()*this.screenDPI/25.4};
            this.tplEditor.flash( function() { this.setStyles(size); } ); 
        },
        setPicture:function(url){
            this.tplEditor.flash( function() { 
			
			try {
				this.setBg(url); 
			} catch(e) {
			
			}
				
			
			} ); 
        },
        addElement:function(itemCode, itemName){
            this.tplEditor.flash( function() { this.addElement(itemCode, itemName); } ); 
            $('#print_item_box').hide();
        },
        setData:function() {
            return this.tplEditor.flash( function() { document.getElementById('templateData').value = this.getData(); });
        },
        deleteOrderExpress:function(){
        	
            //artDialog.confirm("您确定要删除吗？",);
             var comfirmDialog = new Dialog();
             comfirmDialog.init({
            		yes: function() {
            			 $("#orderExpressaddFrom").form('submit', {
                             url: "waybill_deleteOrderExpress.action",
                             onSubmit: function(){
                             },
                             success : function(){
                                //  art.dialog.alert('删除成功!');
                                 var delDialog = new Dialog();
                                 delDialog.init({
                             		contentHtml: '删除成功!',
                             		autoClose: 3000
                             	});
                                 window.location="waybill_orderExpress.action?menuFlag=${menuFlag}";
                             }
                         });
            		},
            		no: function() {	
            			comfirmDialog.close();
        			},
        			closeBtn: true,
            		contentHtml: '您确定要删除吗？'
            });
           
        

        },
        edit:function(text){
            $.ajax({
                url : "waybill_orderExpress.action?isCheck=1&mailNum=9&orderExpress.id="+$("#id").val()+'&r=' + Math.random(),
                dataType : 'json',
				type: 'GET',
                success : function(data){
                    var xbg= data.backgroundimageurl;
					if (xbg == 'images/printtemp/template_a.jpg') {
						$($('#addbgid option')[0]).attr("selected", "selected");
					} else if (xbg == 'images/printtemp/template_d.jpg') {
						$($('#addbgid option')[1]).attr("selected", "selected");
					} else if (xbg == 'images/printtemp/template_b.jpg') {
						$($('#addbgid option')[2]).attr("selected", "selected");
					} else if (xbg == 'images/printtemp/template_c.jpg') {
						$($('#addbgid option')[3]).attr("selected", "selected");
					} else if (xbg == 'images/printtemp/yto_2013.jpg') {
						$($('#addbgid option')[4]).attr("selected", "selected");    
					} 
                    var xdata=  data.templatedata; 
                    //art.dialog.alert(ss);
                    if (typeof text != 'undefined') {
                    	$('.combo .input_text').val(text);
                    }
                    
                    

                    $("#id").val(data.id);
                    ExpressTplEditor.tplEditor = $("#expressTplEditor");
                    $('#expressTplEditor').css('height',470*1);
                    //$('#expressTplEditor').css('width',966*1);
                    uploadBgImagehandler(xbg);
                    ExpressTplEditor.tplEditor.flash({
                        swf: './images/printtemp/printer.swf?'+new Date(),
                        width: '840',
                        height: '100%',
                        border:'1',
                        // wmode: false,
                        wmode: 'opaque',
                        flashvars: {
                            xml:'',
                            order_number:'1',
                            data:xdata,
                            bg:"./"+xbg,
                            copyright:'probiz'
                        }
                    });		          		
                }
            });
                    	
        },	
        save:function(){
            <!--    	if(!validateOrderExpress($('orderExpress'))){-->
            <!--    		return;-->
            <!--    	}-->
			//var orderExpressNameText = $(".combo-text").val();
			var orderExpressNameText = $('.combo-text').val();
            if(orderExpressNameText == ''){
               //  art.dialog({icon: 'succeed',content: '请填写模板名称!',time: 2, opacity:0.1});
               	var modelDialog = new Dialog();
               	modelDialog.init({
            		contentHtml: '请填写模板名称!',
            		autoClose: 1000
            	});
                return;
            }
            // if(!$('expressEditorBg').value){
            if(!$('#expressEditorBg').val()){
                //art.dialog.alert($('#expressEditorBg').val());
                // alert('请上传背景图片');
            	var modelDialog = new Dialog();
               	modelDialog.init({
            		contentHtml: '请上传背景图片!',
            		autoClose: 1000
            	});
                return;
            }
            try
            {
                this.tplEditor.flash( function() { this.getData(); });;
            }
            catch(err)
            {
                // art.dialog.alert('请至少添加一个打印内容项');
                var modelDialog = new Dialog();
               	modelDialog.init({
            		contentHtml: '请至少添加一个打印内容项!',
            		autoClose: 1000
            	});
                return;
            }
            this.setData();
            //   art.dialog.alert(document.getElementById('templateData').value);
            var isCheck="0";
            var id = $("#id").val();
            var orderExpressName = $(".input_text").val();
            if (orderExpressName == '') {
            	orderExpressName  = orderExpressNameText;          	
            }
   /*          var count = 0;
            $('#orderExpressName option').each(function(i, n) {
            	if ($(this).val() == orderExpressName) {
            		count = count + 1;
            		return;
            	}
            });
            alert(count);
			if (count > 0) {
				orderExpressName = orderExpressNameText;
			} */
            if(id!="") isCheck  = "2";
            
            if(id==""){
                if($.trim(orderExpressName)=="请输入模板名称"){
                    // art.dialog({icon: 'succeed',content: '请填写模板名称!',time: 2, opacity:0.1});
                    var modelDialog = new Dialog();
	               	modelDialog.init({
	            		contentHtml: '请填写模板名称!',
	            		autoClose: 1000
	            	});
                    return;
                }
                
                var arrName = $("#orderExpressName").combobox("getData");
                
                var info ="";
                var flag = false;
                $.each(arrName, function(index,callback){
                    if((flag = false) && ($.trim(orderExpressName)==$.trim(arrName[index].text))){
                        flag = true;
                    }
                }
            );
			
                //if(flag==false){
                //   art.dialog({icon: 'succeed',content: '模板名称有复复,修改模板名称!',time: 2, opacity:0.1});
                 //   return;
                //}  

            }
            
            var templateData=document.getElementById('templateData').value;
            //var orderExpressName=document.getElementById('orderExpressName').value;
            
            //alert(orderExpressName);
            //return;
            var expressEditorBg=document.getElementById('expressEditorBg').value;
            //        art.dialog.alert(templateData);
            //        art.dialog.alert(orderExpressName);
            //        art.dialog.alert(expressEditorBg);
            if(isCheck!=0){
                //        	art.dialog.alert(isCheck);
                //        	art.dialog.alert("xg");
                $("#orderExpressaddFrom").form('submit', {
                    url: "waybill_orderExpress.action?mailNum=9&isCheck="+isCheck+"&startTime="+orderExpressName+"&logisticsIds="+expressEditorBg,
                    //       data: "userCodeForTime"+userCode,
                    onSubmit: function(){
                        //art.dialog.alert("添加成功");
                        //					return true;
                    },
                    success : function(date){
                    	
                    	var modifyDialog = new Dialog();
                    	modifyDialog.init({
                    		contentHtml: '修改成功',
                    		autoClose: 3000
                    	});
                    	//art.dialog.alert("修改成功");
						$("#id").val($('.combo-value').val());
                        ExpressTplEditor.edit();
                        window.location="waybill_orderExpress.action?menuFlag=${menuFlag}";
                    }
                });
                	//window.location=encodeURIComponent("waybill_orderExpress.action?mailNum=9&isCheck="+isCheck+"&logisticsIds="+templateData+"&startTime="+orderExpressName);
                
            }else{
                //  art.dialog.alert("新增");
                //window.location=encodeURIComponent("waybill_orderExpress.action?mailNum=9&logisticsIds="+templateData+"&startTime="+orderExpressName);
                $("#orderExpressaddFrom").form('submit', {
                    url: "waybill_orderExpress.action?mailNum=9&startTime="+orderExpressName+"&logisticsIds="+expressEditorBg,
                    //       data: "userCodeForTime"+userCode,
                    onSubmit: function(){
                        //					return true;
                    },
                    success : function(date){
                    	// art.dialog.alert("添加成功");
                    	var addDialog = new Dialog();
                    	addDialog.init({
                    		contentHtml: '添加成功',
                    		autoClose: 3000
                    	});
                        window.location="waybill_orderExpress.action?menuFlag=${menuFlag}";
                    }
                });

                //window.location="waybill_orderExpress.action?mailNum=9&startTime="+orderExpressName+"&logisticsIds="+templateData;	
        	
            }
            //        window.location="waybill_orderExpress.action";
            //		return fnDoSave(this);    



			ExpressTplEditor.edit();
        },
        setFont:function(font){
            this.tplEditor.flash( function() { this.setFont(font); } ); 
        },
        lockbg:function(checked){
            this.tplEditor.flash( function() { this.lockBg(); } ); 
        },
        delBg:function(){
            if($('#expressEditorBg').length!=0){
                $('#expressEditorBg').val('__none__');
            }else{
                $("#orderExpress").add('<input id="expressEditorBg" name="backgroundImageUrl" type="hidden" value="__none__" />');
            }
            this.tplEditor.flash( function() { this.delBg(); } ); 
        }
    };




    var setTplItem = (function() {
        var config = {
            checkedEls: []
        };
	
        var clearCheckedEls = function() {
            config.checkedEls = [];
        };
	
        var getCheckedEls = function() {
            $('#express_tpl_editor .dialog_bd input').each(function() {
                var _this = $(this);
                if ($(_this).prop('checked') == true) {
                    config.checkedEls.push(_this);
                }
            });
		
            return config.checkedEls;
        };
	
        var addElems = function() {
            for (var i = 0, len = config.checkedEls.length; i < len; i++) {
                (function(el) {
                    var elementId = el.attr('id'),
                    elementName = $.trim($('label', el.parent()).html());
				
                    if (ExpressTplEditor) {
                        ExpressTplEditor.addElement(elementId, elementName);
                    }
                })($(config.checkedEls[i]))
            }
        };
	
        var dialogDisplay = {
            show: function() {
                $('#express_tpl_editor').show(300);		
            },
            hide: function(){
                $('#express_tpl_editor').hide(300);
            }
        };
	
        return {
            init: function() {
                $('#express_tpl_editor .dialog_ft #btn_y').click(function() {
                    clearCheckedEls();
                    getCheckedEls();
                    addElems();
                    dialogDisplay.hide();
                });
			
                $('#add_item_btn').click(function(){
                    dialogDisplay.show();
                });
			
                $('#del_item_btn').click(function() {
                    if (ExpressTplEditor.delItem) {
                        ExpressTplEditor.delItem();
                    }
                });
			
                $('#express_tpl_editor .dialog_ft #btn_n').click(function() {
                    dialogDisplay.hide();
                });
			
                $('#lock_bg').click(function() {
                    if (ExpressTplEditor.lockbg) {
                        ExpressTplEditor.lockbg($(this).attr('checked'))
                    }
                });
            }
        }
    })();

    setTplItem.init();
</script>

<script type="text/javascript">
    function uploadBgImagehandler(mediaUrl){

        ExpressTplEditor.setPicture(mediaUrl);	
        //	$("expressEditorBg").value =mediaUrl;
        //   art.dialog.alert($('#expressEditorBg').val());
        document.getElementById('expressEditorBg').value=mediaUrl;

        //	art.dialog.alert("4"+document.getElementById('expressEditorBg').value);
        //	art.dialog.alert("5"+document.getElementById('backgroundImageUrl').value);

        $('#print_bg_box').hide();
    }
    //$(document).ready(function() {
    $(function() {
        var xbg= "<s:property value='orderExpress.backgroundimageurl'/>";

        var xdata="<s:property value='orderExpress.templatedata'/>";
        //art.dialog.alert(ss);
        ExpressTplEditor.tplEditor = $("#expressTplEditor");
        $('#expressTplEditor').css('height',470*1);
        //$('#expressTplEditor').css('width',966*1);
        uploadBgImagehandler(xbg);
        ExpressTplEditor.tplEditor.flash({
            swf: './images/printtemp/printer.swf?'+new Date(),
            width: '840',
            height: '100%',
            border:'1',
            // wmode: false,
            wmode: 'opaque',
            flashvars: {
                xml:'',
                order_number:'1',
                //data:'%3Cprinter%20picposition%3D%220%3A0%22%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Eship_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E487%3A114%3A73%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Eship_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E607%3A171%3A172%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u7535%u8BDD%3C/name%3E%3Cucode%3Eship_tel%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E665%3A114%3A105%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Eship_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E693%3A229%3A88%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u6536%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Eship_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E501%3A172%3A80%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u59D3%u540D%3C/name%3E%3Cucode%3Edly_name%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E127%3A98%3A81%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u5740%3C/name%3E%3Cucode%3Edly_address%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E229%3A159%3A162%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u5730%u533A%3C/name%3E%3Cucode%3Edly_regionFullName%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E134%3A161%3A68%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u90AE%u7F16%3C/name%3E%3Cucode%3Edly_zip%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E307%3A228%3A93%3A20%3C/position%3E%3C/item%3E%3Citem%3E%3Cname%3E%u53D1%u8D27%u4EBA-%u624B%u673A%3C/name%3E%3Cucode%3Edly_mobile%3C/ucode%3E%3Cfont%3Enull%3C/font%3E%3Cfontsize%3E12%3C/fontsize%3E%3Cfontspace%3E0%3C/fontspace%3E%3Cborder%3E0%3C/border%3E%3Citalic%3E0%3C/italic%3E%3Calign%3Eleft%3C/align%3E%3Cposition%3E142%3A223%3A108%3A20%3C/position%3E%3C/item%3E%3C/printer%3E',
                data:xdata,
                //bg:'',
                bg:"./"+xbg,
                copyright:'probiz'
            }
        });
		
			
    });
    function myshowDiv(){
        $("#print_item_box").toggle();
    }
    function myshowbgDiv(){
        $("#print_bg_box").toggle();		
    }
		
    $(function(){
	
		var comboText = '请输入模板名称';
		
		
		$('.combo-text').live('focus', function() {
			if ($(this).val() == comboText) {
				$(this).val('').css('color', '#000');
			}
		});
		$('.combo-text').live('blur', function() {
			if ($(this).val() == '') {
				$(this).val(comboText).css('color', '#999');
			}
		});
        $(".panel-body").css("overflow","auto");
		
        $("#orderExpressName").combobox({
            onSelect:function(param){
               
            	$("#id").val(param.value);
                //alert(param1+"    "+ param2);
                
                ExpressTplEditor.edit(param.text);
					
            }
        });		
        
        
        if (!$('.combo input').attr('maxlength')) {
        	$('.combo input').keypress(function() {
        		 $('.combo input').attr('maxlength', '20');
        	})
        }
       
        
        if ($('.combo-text').val() == comboText) {
        	$('.combo-text').css('color', '#999');
        }
        
       // $('.combo-text').val(comboText);
       if (window.location.search.indexOf('new_template') != -1) {
    	  $('.combo-text').val(comboText).css('color','#999');
       } else {
    	   
       		if ($('#orderExpressName option').length > 0) {
 	    	   var optionVal = $('#orderExpressName option')[0].value;
	    	   $("#id").val(optionVal);
	    	   ExpressTplEditor.edit($("#id").val());
       		} else {
       			$('.combo-text').val(comboText).css('color','#999');
       		}
       }
       
		$('#addbgid').change(function() {
			var _this = $(this),
				val = _this.val().toString();

			switch(val) {
				case '1':
					uploadBgImagehandler('images/printtemp/template_a.jpg');
				break;
				
				case '2':
					uploadBgImagehandler('images/printtemp/template_d.jpg');
				break;
				
				case '3':
					uploadBgImagehandler('images/printtemp/template_b.jpg');
				break;
				
				case '4':
					uploadBgImagehandler('images/printtemp/template_c.jpg');
				break;	
				
				case '5':
					uploadBgImagehandler('images/printtemp/yto_2013.jpg');
				break; 
			}
		});
		
		uploadBgImagehandler('images/printtemp/template_a.jpg');
		
		// 判断是否支持 flash
		var isIE = !-[1,];
		if(isIE){
			try{
				var swf1 = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
				$('#no_flash').hide();
			}
			catch(e){
				$('#no_flash').show();
			}
		}
		else {
			try{
				var swf2 = navigator.plugins['Shockwave Flash'];
				if(swf2 == undefined){
					$('#no_flash').show();
				}
				else {
					$('#no_flash').hide();
				}
			}
			catch(e){
				$('#no_flash').show();
			}
		}
    });
		
	


</script>
<%-- <jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
<jsp:include page="/activateVipPopup.jsp"></jsp:include> --%>
    </div>

