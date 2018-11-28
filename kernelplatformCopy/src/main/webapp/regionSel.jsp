<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>add</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  	
	<script type="text/javascript">
		function showCity_(id) {
			var req={
					url : 'region_getCity.action?parentId='+id,
					dataType : 'json',
					success : appendCitySelect_,
					async:false
			}
			jQuery.ajax(req);
		}
		function showCountry_(id) {
			var req={
					url : 'region_getCountry.action?parentId='+id,
					dataType : 'json',
					async:false,
					success : appendCountrySelect_
			}
			jQuery.ajax(req);
		}
		
		function appendCitySelect_(obj){
			var selectObj = document.getElementById("city_");
			removeOptions_();
			for(var i=0; i<obj.length; i++){ 
				var option = document.createElement("option"); 
				option.text = obj[i].regionName;
				option.value = obj[i].id;
				selectObj.options.add(option);// 为地市列表中添加选项
			}
		}
		function appendCountrySelect_(obj){
			var selectObj = document.getElementById("country_");
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
		}
		//清除select中options内容
		function removeOptions_(){
			var selectCity = document.getElementById("city_");
			var selectCountry = document.getElementById("country_");
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
  <body >
  	<select onchange="showCity_(this.value)" id="province_" x="x_p">
		<option value="">请选择省份</option>
		<s:iterator value="" var="reg">
			<option value="<s:property value='id'/>"><s:property value="regionName"/></option>
		</s:iterator>
	</select>
	<select id="city_" onchange="showCountry_(this.value)" x="x_c">
		<option value="">请选择所在市</option>
	</select>
	<select id="country_" x="x_d">
		<option value="">请选择所在县区</option>
	</select>
  </body>
</html>
