<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	
	$(function(){
		
		var linkageSel;
		var selectArea = function() {
			var area = {
				data: districtData,
				selStyle: 'margin-left:2px;',
				select: ['#province']
			};
			linkageSel = new LinkageSel(area);
		};
		selectArea();
		$("#province").unbind('change');
		
		/*
		//上一页、下一页
		$('.item_navi span').live('click', function(ev) {
			ev.preventDefault();
			
			var _this = $(this),
				curPage = _this.attr("val"),
				flag = $('.flag',_this.parents('a')).val()
				
				$.ajax({
					url: 'passManage_searchWarnValueList.action',
					post: 'GET',
					cache: false,
					dataType: 'html',
					data:{
						tips:'1',
						flag:flag,
						currentPage:curPage
					},
					success:function(data){
						$('.item_navi').parent().html(data)
					}
				})
		});
		*/
		
		//添加预警值
		$(".btn_f").click(function(){
			var province = linkageSel.getSelectedData('name',0);
			var warnValue = $('.dia_mar').val();
			// var currentPage = $('.currentPage').val();
			
			if(province == null || warnValue == '0'){
				$('#tt').html('请选择地址/预警值');
			}else{
				$.ajax({
					url:'passManage_addWarnValue.action',
					post:'GET',
					cache: false,
					data:{
						province:province,
						warnValue:warnValue						
					},
					success:function(data){
						$('#tt').html(data);
						$.ajax({
							url:"passManage_searchWarnValueList.action?tips=1",
							type:"GET",
							cache: false,
							data:{
								//currentPage:currentPage
							},
							dataType: 'html',
							success:function(data){
								//$('.item_navi').parent().html(data)
								$('#divTable').html(data)
							},
							error:function(){
								$('#tt').html('抱歉，系统繁忙，请稍后再试！');
							}
						});
					},error:function(){
						$('#tt').html('抱歉，系统繁忙，请稍后再试！');
					}
				});
			}
			
		});
		
		
		//删除预警值
		$('.del').live('click', function(ev) {
			var	_this = $(this),
				province = _this.parent().find('.destination').val(),
				sellerId = _this.parent().find('.sellerId').val();
			// var currentPage = $('.currentPage').val();
			
			$.ajax({
				url:'passManage_removeWarnValue.action',
				post:'GET',
				cache: false,
				data:{
					province:province,
					sellerId:sellerId						
				},
				dataType:'html',
				success:function(data){
				 	// $('#tt').html('删除成功');
					/* setInterval(function(){
						$('#tt').html('');
					},1000); */
					$.ajax({
						url:"passManage_searchWarnValueList.action?tips=1",
						type:"GET",
						cache: false,
						dataType: 'html',
						success:function(data){
							$('#divTable').html(data)
						},
						error:function(){
							$('#tt').html('系统繁忙，请稍后再试！');
						}
					});
				},
				error:function(){
					$('#tt').html('系统繁忙，请稍后再试！');
				}
			})
		});
		
		//点击修改
		var linkageSel2;
		$('.td_c .td_c_span').live('click',function(ev){
			ev.preventDefault();
			var _this = $(this),
				save = $('.save',_this.parent()),
				cannl = $('.cannl',_this.parent()),
				destination = $('.destination',_this.parent()).val(),
				value = $('.warnValue',_this.parents('tr')).html().split("")[0],
				span_destination = $('.destination',_this.parents('tr')),
				select_destination = $('.pro',_this.parents('tr')),
				span_value = $('.warnValue',_this.parents('tr')),
				select_value = $('.day',_this.parents('tr'));
				
			_this.attr('style','display:none');
			save.attr('style','display:inline');
			span_destination.attr('style','display:none');
			select_destination.attr('style','display:inline');
			span_value.attr('style','display:none');
			select_value.attr('style','display:inline');
			cannl.attr('style','display:inline');
			
			// 地区绑定
			var area = {
				data: districtData,
				selStyle: 'margin-left:3px;',
				select: [select_destination],
				autoLink: false,
				level: 1
			};
			 
			linkageSel2 = new LinkageSel(area);
			linkageSel2.changeValuesByName([destination]);			
			select_value.attr('value',value).attr('selected','true');
			
		});
		
		$('.save').live('click',function(ev){
			ev.preventDefault();
			var _this = $(this),
				id = $('.id',_this.parent()).val(),
				sellerId = $('.sellerId',_this.parent()).val(),
				province = linkageSel2.getSelectedData('name',0),
				warnValue = $('.day',_this.parents('tr')).val();
			
			if(province == null || warnValue == '0'){
				$('#tt').html('请选择地址/预警值');
				/* setInterval(function(){
					$('#tt').html('');
				},1000); */
			}else{
				$.ajax({
					url:'passManage_editWarnValue.action',
					post:'GET',
					cache: false,
					data:{
						province:province,
						warnValue:warnValue,
						id:id,
						sellerId:sellerId
					},
					success:function(data){
						// $('#tt').html('修改成功');
						/* setInterval(function(){
							$('#tt').html('');
						},1000); */
						$('#tt').html(data);
						$.ajax({
							url:"passManage_searchWarnValueList.action?tips=1",
							type:"GET",
							cache: false,
							data:{
								// currentPage:currentPage
							},
							dataType: 'html',
							success:function(data){
								$('#divTable').html(data)
							},
							error:function(){
								$('#tt').html('系统繁忙，请稍后再试！');
							}
						});
					},
					error:function(){
						$('#tt').html('抱歉！系统繁忙！,请稍后再试！');
					}
				})
			}
		});
		
		$('.cannl').live('click',function(ev){
			ev.preventDefault();
			var _this = $(this),
				save = $('.save',_this.parent()),
				edit = $('.td_c_span',_this.parent()),
				span_destination = $('.destination',_this.parents('tr')),
				select_destination = $('.pro',_this.parents('tr')),
				span_value = $('.warnValue',_this.parents('tr')),
				select_value = $('.day',_this.parents('tr'));
				
			_this.attr('style','display:none');
			save.attr('style','display:none');
			edit.attr('style','display:inline');
			span_destination.attr('style','display:inline');
			select_destination.attr('style','display:none');
			span_value.attr('style','display:inline');
			select_value.attr('style','display:none');
		});
	});
</script>
<div id="dialog_tab" style="width:507px;">
	<p>
		<label for="province">地址：</label>
		<select id="province"></select>
		<label>预警值：</label>
			<select class="dia_mar">
				<option value="0" selected>请选择</option>
				<option value="3">3天</option>
				<option value="4">4天</option>
				<option value="5">5天</option>
				<option value="6">6天</option>
				<option value="7">7天</option>
				<option value="8">8天</option>
				<option value="9">9天</option>
			</select> 
		<a href="javascript:;" class="btn btn_f" title="添加">
			<span>添加</span>
		</a><br>
		<span id="tt" style="color:red;font-size:12px;"></span>
	</p>
	<div  id="divTable" >
	<div class="table" style="width:507px;height:350px;overflow-y:auto;overflow-x:hidden;">
		<table>
			<thead>
				<tr>
					<th class="th_a">
						<div class="th_title">
							<em>目的地</em>
						</div>
					</th>
					<th class="th_b">
						<div class="th_title">
							<em>预警值</em>
						</div>
					</th>
					<th class="th_c">
						<div class="th_title">
							<em>操作</em>
						</div>
					</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="warnValueList" var="pv">
					<tr>
						<td class="td_a">
							<span class="destination"><s:property value="#pv.destination"/></span>
							<select class="pro" style="display:none;"></select>
						</td>
						<td class="td_b">
							<span class="warnValue"><s:property value="#pv.warnValue"/>天</span>
							<select class="day"  style="display:none;">
								<option value="0" selected>请选择</option>
								<option value="3">3天</option>
								<option value="4">4天</option>
								<option value="5">5天</option>
								<option value="6">6天</option>
								<option value="7">7天</option>
								<option value="8">8天</option>
								<option value="9">9天</option>
							</select> 
						</td>
						<td class="td_c">
							<span class="td_c_span"><a href="">修改</a></span>
							<input type="button" value="保存" class="save" style="display:none;">
							<input type="button" value="取消" class="cannl" style="display:none;">
							<span class="del"><a href="#">删除</a></span>
							<input type="hidden" class="destination" value="<s:property value="#pv.destination"/>">
							<input type="hidden" class="sellerId" value="<s:property value="#pv.sellerId"/>">
							<input type="hidden" class="id" value="<s:property value="#pv.id"/>">
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<!-- 
		<div class="item_navi">
			第 <s:property value="currentPage"/> 页<input type="hidden" class="currentPage" value="<s:property value="currentPage"/>">
			/ 共 <s:property value="totalNum"/> 页 
			<s:if test="%{currentPage > 1}">
			<a href="#">
				<span class="jian" val="${currentPage-1 }">上一页</span>
				<input type="hidden" class="flag" value="0">
			</a></s:if> | 
			<s:if test="%{currentPage < totalNum}">
			<a href="#">
				<span class="jia" val="${currentPage+1 }">下一页</span>
				<input type="hidden" class=flag value="1">
			</a></s:if>
		</div> -->
	</div>
	</div>
</div>
