<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">

</style>


    <script type="text/javascript">
       
    	$(function() {
    		
            var req={
                url : 'region_show.action',
                dataType : 'json',
                success : initProvinceActVip
            }
            jQuery.ajax(req);
        });
        function initProvinceActVip(data){
            var selectObj = document.getElementById("provinceActVip");
            var regionTxt = $("#provinceActVip option:selected").text();
            $("#provinceActVip").empty();
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
        function showCityActVip(id) {
            var req={
                url : 'region_getCity.action?parentId='+id,
                dataType : 'json',
                success : appendCitySelectActVip,
                async:false
            }
            jQuery.ajax(req);
        }
        function showCountryActVip(id) {
            var req={
                url : 'region_getCountry.action?parentId='+id,
                dataType : 'json',
                async:false,
                success : appendCountrySelectActVip
            }
            jQuery.ajax(req);
        }
        function appendCitySelectActVip(id) {
            var req={
                url : 'region_getCountry.action?parentId='+id,
                dataType : 'json',
                async:false,
                success : appendCountrySelectActVip
            }
            jQuery.ajax(req);
        }
		
        function appendCitySelectActVip(obj){
        	var selectObj = document.getElementById("cityActVip");
        	var regionTxt = $("#cityActVip option:selected").text();
            removeOptionsActVip();
            for(var i=0; i<obj.length; i++){ 
                var option = document.createElement("option"); 
                option.text = obj[i].regionName;
                option.value = obj[i].id;
                if(obj[i].regionName == regionTxt){
                	option.selected=true;
                }
                selectObj.options.add(option);// 为地市列表中添加选项
            }
            if($("#countryActVip").css("display") == 'none'){
            	$("#countryActVip").css("display","");
            }
        }
        function appendCountrySelectActVip(obj){
        	var selectObj = document.getElementById("countryActVip");
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
            if(selectObj.options.length == 1 && $("#cityActVip").val()!=''){
            	$("#countryActVip").css("display","none");
            }
        }
        
        //清除select中options内容
        function removeOptionsActVip(){
            var selectCity = document.getElementById("cityActVip");
            var selectCountry = document.getElementById("countryActVip");
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
    </script>  
       <select onChange="showCityActVip(this.value)" id="provinceActVip" x="x_p" style="margin-top:0px;">
           <option value="">请选择省份</option>
           <s:iterator value="" var="reg">
               <option value="<s:property value='id'/>"><s:property value="regionName"/></option>
           </s:iterator>
       </select>
       <select id="cityActVip" onChange="showCountryActVip(this.value)" x="x_c" style="margin-top:0px;">
           <option value="">请选择所在市</option>
       </select>
       <select id="countryActVip" x="x_d" style="margin-top:0px;">
           <option value="">请选择所在县区</option>
       </select>
        </p>
       	<p>
   		<label for="region" style="display:inline-block;padding-top:10px;vertical-align:top;padding:0;"><span class="req">*</span>街道地址：</label><textarea rows="2"  id="addressActVip" cols="51" x="x_s" class="text-input" style="margin-top:0px;vertical-align:top;"><s:property value="user.addressStreet"/></textarea>
