<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String type = request.getParameter("type");
%>

   
        <style type="text/css">
            #province,#city,#country,#address{margin-left:-6px;margin-top:10px;margin-right:5px;}
			#content_bd #province,#content_bd #city,#content_bd #country,#content_bd #address{margin-left:0;margin-top:0;margin-right:0;}
        </style>


    <script type="text/javascript">
       
    	$(function() {
            var req={
                url : 'region_show.action',
                dataType : 'json',
                success : initProvince
            }
            jQuery.ajax(req);
        });
        function initProvince(data){
            var selectObj = document.getElementById("province");
            var regionTxt = $("#province option:selected").text();
            $("#province").empty();
            var optionNull = document.createElement("option");
            optionNull.text = "请选择省份";
            optionNull.value = "";
            selectObj.options.add(optionNull);
            if(data!=null){
	            for(var i=0; i<data.length; i++){ 
	                var option = document.createElement("option"); 
	                option.text = data[i].regionName;
	                option.value = data[i].id;
	                if(data[i].regionName == regionTxt){
	                	option.selected=true;
	                }
	                selectObj.options.add(option);// 为省级列表中添加选项
	            }
            }
           var province_str = '<s:property value="user.addressProvince"/>';
           if(province_str==""){
        	   selectObj.onchange();
           }
        }
        function showCity(id) {
            var req={
                url : 'region_getCity.action?parentId='+id,
                dataType : 'json',
                success : appendCitySelect,
                async:false
            }
            jQuery.ajax(req);
        }
        function showCountry(id) {
            var req={
                url : 'region_getCountry.action?parentId='+id,
                dataType : 'json',
                async:false,
                success : appendCountrySelect
            }
            jQuery.ajax(req);
        }
		
        function appendCitySelect(obj){
        	var selectObj = document.getElementById("city");
        	var regionTxt = $("#city option:selected").text();
            removeOptions();
            for(var i=0; i<obj.length; i++){ 
                var option = document.createElement("option"); 
                option.text = obj[i].regionName;
                option.value = obj[i].id;
                if(obj[i].regionName == regionTxt){
                	option.selected=true;
                }
                selectObj.options.add(option);// 为地市列表中添加选项
            }
            setTimeout("checkCity();",20);
        }
        function appendCountrySelect(obj){
        	var selectObj = document.getElementById("country");
            // 先清除县区列表的现有内容
            while (selectObj.options.length>0){
                selectObj.removeChild(selectObj.childNodes[0]); 
            }
            var option = document.createElement("option");
            option.text = "请选择所在县区";
            option.value = "";
            selectObj.options.add(option);
            for(var i=0; i<obj.length; i++){ 
                var option = document.createElement("option"); 
                option.text = obj[i].regionName;
                option.value = obj[i].id;
                selectObj.options.add(option);// 为县区列表中添加选项
            }
           	setTimeout("checkCountry();",20);
        }
        
        function checkCountry(){
            //if(!$.formValidator.oneIsValid("country", 1).isvalid && $.formValidator.oneIsValid("city", 1).isvalid){
            // 	$("#addressSelectTip").text("请选择所在县区");
            // 	$("#addressSelectTip").attr("class","onFocus");
            //}
        }

        function checkCity(){
            //if(!$.formValidator.oneIsValid("province", 1).isvalid){
            //	$("#addressSelectTip").text("请选择所在省份");
            // 	$("#addressSelectTip").attr("class","onFocus");
           // }
           // else if(!$.formValidator.oneIsValid("city", 1).isvalid){
            // 	$("#addressSelectTip").text("请选择所在市");
            // 	$("#addressSelectTip").attr("class","onFocus");
           // }
        }
        //清除select中options内容
        function removeOptions(){
            var selectCity = document.getElementById("city");
            var selectCountry = document.getElementById("country");
            var option = document.createElement("option");
            // 先清除地市列表的现有内容
            while (selectCity.options.length>0){
                selectCity.removeChild(selectCity.childNodes[0]); 
            }
            option.text = "请选择所在市";
            option.value = "";
            selectCity.options.add(option);
            var option2 = document.createElement("option");
            // 先清除县区列表的现有内容
            while (selectCountry.options.length>0){
                selectCountry.removeChild(selectCountry.childNodes[0]); 
            }
            option2.text = "请选择所在县区";
            option2.value = "";
            selectCountry.options.add(option2);
        }
        function blurValue(obj){
//             if(obj.value == "")
//                 document.getElementById(obj.id).value = "请输入详细地址";
        }
        function focusValue(obj){
            if(obj.value == "请输入详细地址")
                document.getElementById(obj.id).value = "";
        }
    </script>  
    
        <%if("2".equals(type)){ %>
        <select disabled="disabled">
            <option><s:property value="user.addressProvince"/></option>
        </select>
        <select disabled="disabled">
            <option><s:property value="user.addressCity"/></option>
        </select>

        <div style="display:none">
            <select onChange="showCity(this.value)" id="province" x="x_p"  disabled="disabled">
                <option value=""><s:property value="user.addressProvince"/></option>
                <s:iterator value="" var="reg">
                    <option value="<s:property value='id'/>"><s:property value="regionName"/></option>
                </s:iterator>
            </select>
            <select id="city" onChange="showCountry(this.value)" x="x_c" disabled="disabled">
                <option value=""><s:property value="user.addressCity"/></option>
            </select>
        </div>
        <select id="country" x="x_d">
            <option value=""><s:property value="user.addressDistrict"/></option>
        </select>
<%--         <input id="address" title="请输入详细街道地址"  onblur="blurValue(this);" onFocus="focusValue(this);" x="x_s" value="<s:property value="user.addressStreet"/>" class="text-input" style="width:160px;margin-top:-7px;"/> --%>

			<span class="onShow" id="addressSelectTip" style="width: 160px;">请选择省、市、区（县）</span>

<p>
       		<label for="">街道地址：</label><textarea rows="2"  id="address" cols="51" x="x_s" class="text-input"><s:property value="user.addressStreet"/></textarea>

       			<span class="onShow" id="addressTip" style="width: 160px;">请输入详细街道地址</span>
</p>

        <%}else if("1".equals(type)){ %>
        <select onChange="showCity(this.value)" id="province" x="x_p" >
            <option value=""><s:property value="user.addressProvince"/></option>
            <s:iterator value="" var="reg">
                <option value="<s:property value='id'/>"><s:property value="regionName"/></option>
            </s:iterator>
        </select>
        <select id="city" onChange="showCountry(this.value)" x="x_c">
            <option value=""><s:property value="user.addressCity"/></option>
        </select>
        <select id="country" x="x_d" style="display:inline;">
            <option value=""><s:property value="user.addressDistrict"/></option>
        </select>

			<span class="onShow" id="addressSelectTip" style="width: 160px;">请选择省、市、区（县）</span>
<p>
<label for="">街道地址：</label><textarea rows="2"  id="address" cols="51" x="x_s" class="text-input" style="display:inline; height:55px;"><s:property value="user.addressStreet"/></textarea>

       			<span class="onShow" id="addressTip" style="width: 160px;">请输入详细街道地址</span>
</p>

        <%}else{ %>
        
			        <select onChange="showCity(this.value)" id="province" x="x_p" style="margin-top:0px;">
			            <option value="">请选择省份</option>
			            <s:iterator value="" var="reg">
			                <option value="<s:property value='id'/>"><s:property value="regionName"/></option>
			            </s:iterator>
			        </select>
			        <select id="city" onChange="showCountry(this.value)" x="x_c" style="margin-top:0px;">
			            <option value="">请选择所在市</option>
			        </select>
			        <select id="country" x="x_d" style="margin-top:0px;">
			            <option value="">请选择所在县区</option>
			        </select>
        		
			<span class="onShow" id="addressSelectTip">请选择省、市、区（县）</span>
<p>
       		<label for="region">*街道地址：</label><textarea rows="2"  id="address" cols="51" x="x_s" class="text-input" style="margin-top:0px;"><s:property value="user.addressStreet"/></textarea>
       			<span class="onShow" id="addressTip">请输入详细街道地址</span>
</p>

        <%} %>
    
