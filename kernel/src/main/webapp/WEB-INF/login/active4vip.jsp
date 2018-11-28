<%--
	@2011-11-16
	@ChenRen
	
	该页面是淘宝用户激活的页面；
	因为淘宝的应用不在本机，所以用这个页面在本机测试要改版的淘宝用户激活页面。
	
	如果以后要用到vip激活页面，请直接参考 active4vip.jsp.2011-11-16.bak 
	
	
	@2011-12-20/ChenRen
		需求变更：
			要求密码必填
	
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>淘宝用户激活</title>
<link rel="stylesheet" href="css/posttemp.css?d=${str:getVersion() }" type="text/css" />
<link rel="stylesheet" href="css/layout.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/style.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link rel="stylesheet" href="css/invalid.css?d=${str:getVersion() }" type="text/css" media="screen" />
<link href="artDialog/skins/blue.css" rel="stylesheet"/>
<script src="artDialog/artDialog.js?d=${str:getVersion() }"></script>
<script src="artDialog/initArtDialog.js?d=${str:getVersion() }"></script>
<style type="text/css">
	.button {padding: 2px 5px !important}
	.button:active {padding: 2px 5px !important}
	.x_msg_title {
		PADDING-RIGHT: 0px;
		PADDING-LEFT: 0px;
		FONT-WEIGHT: bold;
		FONT-SIZE: 12px;
		PADDING-BOTTOM: 0px;
		MARGIN: 0px;
		PADDING-TOP: 0px;
		float: left;
	}
	.cont lable {font-weight: bold;}
</style>
<script type="text/javascript" src="js/jquery-1.4.2.js?d=${str:getVersion() }"></script>
</head>
<body>
	<div id="main" >
	<div id="main-content">
		<div style="color:red;">该页面是用来在本地模拟淘宝用户激活测试页面!!! 对整个系统没有实际意义!!! </div>
	<!-- 
	 -->
		<div id="midtit">
		 <span class="gnheader f14 loading">您的信息不全，请完善信息！</span></div>
			<!-- 
			<h2>
				淘宝用户激活
				<span style="font-size:12px;font-weight:normal;color:red;margin-left:30px;">您的账号未激活，请先激活！</span>
			</h2>
			 -->
			<div aclass="content-box" style="width:100%;">
				<div id="midfrom">
					<form id="mgnm" action="#"  method="post" >
					<!-- 
						<p>
							<label for="userCode" style="display:inline;">客户编码*：</label>
							<input id="userCode"  name="user.userCode" class="validate[required,ajax[ajaxUserCode]] text-input"/>
							请填写您的客户编码&nbsp;如果不知道&nbsp;<span style="cursor: help;">1.请联系网点索取&nbsp;2.<lable title="联系方式在页面底部!">联系我们客服</lable>&nbsp;3.<a href="javascript:;" id="apply4site" style="color:#555555; text-decoration: underline;" title="点击发送消息...">给管理员发送消息申请</a></span>
							<input type="hidden" id="name" name="user.shopAccount" value='<s:property value="#session.user.shopAccount"/>'>
							<input type="hidden" id="id" name="user.id" value='<s:property value="#session.user.id"/>'>
						</p>
					 -->
						<p>
							<input type="hidden" id="id" name="user.id" value='<s:property value="#session.user.id"/>'>
							<input type="hidden" id="ajaxAlertText" value='<s:property value="ajaxAlertText"/>'>
							<input type="hidden" name="user.shopAccount" value='<s:property value="#session.user.shopAccount"/>' class="text-input"/>
                  			<label style="display:inline;">登录账号&nbsp;：</label>
							<input name="user.userName" value='test_vip2' readonly class="text-input" style="cursor:default;"/>
							为得到更好的用户体验，系统自动为您生成本地账号。
                    	</p>
						<p>
                  			<label for="pwd" style="display:inline;">*设置密码：</label>
							<input id="pwd" type="password" name="user.userPassword" class="validate[required,length[6,20]] text-input" />
							请设置您的本地登录密码(请输入6-20位字母与数字组合的密码)
                    	</p>
                     	<p>
        					<label for="pwd2" style="display:inline;">*确认密码：</label>
							<input id="pwd2" type="password"  class="validate[required,confirm[pwd]] text-input"/>
                  		</p>
                  		
						<p>
							<label for="mp" style="display:inline;">*手机号码：</label>
							<input id="mp"  name="user.mobilePhone" value='<s:property value="user.mobilePhone"/>' class="text-input" style="width:150px;"/>
							请填写正确的手机号码&nbsp;（易通承诺不会将您的信息透露给第三方）
						</p>
						<p>
							<label for="tel" style="display:inline;">*固定电话：</label>
							<input id="tel"  name="user.telePhone" value='<s:property value="user.telePhone"/>' class="text-input" style="width:150px;"/>
							手机号码和固定电话必须填写一个&nbsp;（易通承诺不会将您的信息透露给第三方）
						</p>
						<p>
							<label for="email" style="display:inline;">*邮　　箱：</label>
							<input id="email"  name="user.mail" class="validate[required,custom[email]] text-input" value="<s:property value="user.mail"/>"  style="width:150px;"/>
							为了更好的为您服务，请填写正确邮箱
						</p>
						<p>
							<input type="checkbox" id="_cMz" checked >
							<label for="_cMz" style="display: inline; font-weight: normal; cursor: pointer;">同意免责协议</label>
						</p>
						<p>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input class="submit01" type="submit" value="保　存" id="_bA" style="border-radius: 4px 4px 4px 4px;"/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input class="submit01" type="reset" value="重　填" style="margin:0 40px 0 50px;border-radius: 4px 4px 4px 4px;"/>
						</p>
						<p>
							<textarea id="_tMz" rows="5" cols="50" readonly style="display:none; width: 70% !important;">
              本协议是您与易通电子商务物流服务平台所有者（以下简称为“易通”）之间就易通服务等相关事宜所订立的契约，本协议中易通电子商务物流服务平台简称“易通平台”，域名为ec.yto.net.cn。请您仔细阅读本注册协议，您点击“同意以下协议，提交”按钮后，本协议即构成对双方有约束力的法律文件。 

一、协议内容及签署    

1.   本协议内容包括协议正文及所有易通已经发布的或将来可能发布的各类规则。所有规则为本协议不可分割的组成部分，与协议正文具有同等法律效力。除另行明确声明外，任何易通提供的服务均受本协议约束。

2.   易通平台的各项电子服务的所有权和运作权归易通所有。用户同意所有注册协议条款并完成注册程序，才能成为易通平台的正式用户。用户确认：本协议条款是处理双方权利义务的契约，始终有效，法律另有强制性规定或双方另有特别约定的，依其规定或约定。 

3.   用户点击同意本协议的，即视为用户确认自己具有享受易通服务等相应的权利能力和行为能力，能够独立承担法律责任。只要您使用易通平台服务，则本协议即对您产生约束，届时您不应以未阅读本协议的内容或者未获得易通对您问询的解答等理由，主张本协议无效，或要求撤销本协议。

4.   您承诺接受并遵守本协议的约定。如果您不同意本协议的约定，您应立即停止注册程序或停止使用易通平台服务。

5.   易通有权根据需要不时地制订、修改本协议及/或各类规则，并以网站公示的方式进行公告，不再单独通知您。变更后的协议和规则一经在网站公布后，立即自动生效。如您不同意相关变更，应当立即停止使用易通服务。您继续使用易通服务的，即表示您接受经修订的协议和规则。

二、会员注册     

1.  用户应自行诚信向易通平台提供注册资料，用户同意其提供的注册资料真实、准确、完整、合法有效，用户注册资料如有变动的，应及时更新其注册资料。如果用户提供的注册资料不合法、不真实、不准确、不详尽的，用户需承担因此引起的相应责任及后果，并且易通保留终止用户使用易通平台各项服务的权利。 

2.  用户注册成功后，将产生用户名和密码等账户信息，您可以根据易通平台规定改变您的密码。用户应谨慎合理的保存、使用其用户名和密码。用户若发现任何非法使用用户账号或存在安全漏洞的情况，请立即通知易通。 

3.  在您签署本协议，完成会员注册程序或实际使用易通平台时，易通会向您提供唯一编号的易通账户（以下简称账户）。您可以对账户设置会员名和密码，通过该会员名密码或与该会员名密码关联的其它用户名密码登录易通平台。您设置的会员名不得侵犯或涉嫌侵犯他人合法权益。如您连续一年未使用您的会员名和密码登录易通平台，易通有权终止向您提供易通平台服务，注销您的账户。账户注销后，相应的会员名将开放给任意用户注册登记使用。

4.  用户不得将在易通平台注册获得的账户借给他人使用，否则用户应承担由此产生的全部责任，并与实际使用人承担连带责任。

三、   服务  

1.  通过易通及相关公司提供的易通平台服务和其它服务，用户可以在易通平台上包括但不限于发布订单信息、查询订单信息、与物流服务点进行沟通、参加易通组织的活动以及使用其它信息服务及技术服务。

2.  用户同意，易通包括但不限于通过电子邮件、手机短信、电话等形式，向易通平台用户、收货人、物流服务点等发送信息的权利。 

3.  您了解并同意，易通有权应政府部门（包括司法及行政部门）的要求，向其提供您在易通平台填写的注册信息等必要信息。如您涉嫌侵犯他人知识产权，则易通亦有权在初步判断涉嫌侵权行为存在的情况下，向权利人可提供您必要的身份信息。

四、   易通平台服务使用规范  

1. 在易通平台上使用服务过程中，您承诺遵守以下约定：

（1）  在使用易通平台服务过程中实施的所有行为均遵守国家法律、法规等规范性文件及易通平台各项规则的规定和要求，不违背社会公共利益或公共道德，不损害他人的合法权益，不违反本协议及相关规则。您如果违反前述承诺，产生任何法律后果的，您应以自己的名义独立承担所有的法律责任，并确保易通免于因此产生任何损失。

（2）  不对易通平台上的任何数据作商业性利用，包括但不限于在未经易通事先书面同意的情况下，以复制、传播等任何方式使用易通平台上展示的资料。

（3）  不使用任何装置、软件或例行程序干预或试图干预易通平台的正常运作。

（4）  您不得采取任何将导致不合理的庞大数据负载加诸易通平台网络设备的行动。

2. 您了解并同意：

（1）  易通有权对您是否违反上述承诺做出单方认定，并根据单方认定结果适用规则予以处理或终止向您提供服务，且无须征得您的同意或提前通知予您。

（2）  经国家行政或司法机关的生效法律文书确认您存在违法或侵权行为，或者易通根据自身的判断，认为您的行为涉嫌违反本协议和/或规则的条款或涉嫌违反法律法规的规定的，则易通有权在易通平台上公示您的该等涉嫌违法或违约行为及易通已对您采取的措施。

（3）  对于您在易通平台上发布的涉嫌违法或涉嫌侵犯他人合法权利或违反本协议和/或规则的信息，易通有权不经通知您即予以删除，且按照规则的规定进行处罚。

（4）  对于您在易通平台上实施的行为，包括您未在易通平台上实施但已经对易通平台及其用户产生影响的行为，易通有权单方认定您行为的性质及是否构成对本协议和/或规则的违反，并据此做出相应处罚。您应自行保存与您行为有关的全部证据，并应对无法提供充要证据而承担的不利后果。

（5）  对于您涉嫌违反承诺的行为对任意第三方造成损害的，您均应当以自己的名义独立承担所有的法律责任，并应确保易通免于因此产生损失或增加费用。

（6）  如您涉嫌违反有关法律或者本协议之规定，使易通遭受任何损失，或受到任何第三方的索赔，或受到任何行政管理部门的处罚，您应当赔偿易通因此造成的损失及/或发生的费用，包括合理的律师费用。

五、   所有权及知识产权条款

1.  用户一旦接受本协议，即表明该用户主动将其在任何时间段在本站发表的任何形式的信息内容的财产性权利等任何可转让的权利，如著作权财产权全部独家且不可撤销地转让给易通所有，用户同意易通平台有权就任何主体侵权而单独提起诉讼。 

2.  本协议已经构成《中华人民共和国著作权法》第二十五条（条文序号依照2011年版著作权法确定）及相关法律规定的著作财产权等权利转让书面协议，其效力及于用户在易通平台上发布的任何受著作权法保护的作品内容，无论该等内容形成于本协议订立前还是本协议订立后。 

3.  易通是本平台的制作者,拥有此平台内容及资源的著作权等合法权利,受国家法律保护,有权不时地对本协议及本平台的内容进行修改，并在平台张贴，无须另行通知用户。在法律允许的最大限度范围内，易通对本协议及平台内容拥有解释权。 

4.  除法律另有强制性规定外，未经易通明确的特别书面许可,任何单位或个人不得以任何方式非法地全部或部分复制、转载、引用、链接、抓取或以其他方式使用本站的信息内容，否则，易通有权追究其法律责任。 

5.  本站所刊登的资料信息（诸如文字、图表、标识、按钮图标、图像、声音文件片段、数字下载、数据编辑和软件），均是易通平台或其内容提供者的财产，受中国和国际版权法的保护。本站上所有内容的汇编是易通平台的排他财产，受中国和国际版权法的保护。本站上所有软件都是易通平台或其关联公司或其软件供应商的财产，受中国和国际版权法的保护。  

六、   特别授权

您完全理解并不可撤销地授予易通下列权利：

1.  一旦您向易通做出任何形式的承诺，且已确认您违反了该承诺，则易通有权立即按您的承诺或协议约定的方式对您的账户采取限制措施，包括中止或终止向您提供服务，并公示相关公司确认的您的违约情况。您了解并同意，易通无须就相关确认与您核对事实，或另行征得您的同意，且易通无须就此限制措施或公示行为向您承担任何的责任。

2.  对于您提供的资料及数据信息，您授予易通独家的、全球通用的、永久的、免费的许可使用权利 (并有权在多个层面对该权利进行再授权)。此外，易通及其关联公司有权全部或部份地使用、复制、修订、改写、发布、翻译、分发、执行和展示您提供的资料及数据信息或制作其派生作品，并以现在已知或日后开发的任何形式、媒体或技术，将上述信息纳入其它作品内。

七、   责任范围和责任限制   

1.  易通平台所提供的所有资料、数据及信息，仅供参考使用。任何个人、公司及单位等依据易通平台提供的资料、数据及资料进行相关活动所造成的盈亏与易通平台无关。如发现信息有误或其它问题，请及时通过信息反馈与我们联系。

2.  由于用户恶意填写寄件信息等造成与易通平台对接的快递公司工作负担，或者导致用户与快递公司纠纷的，易通平台免责。

3.  由于用户使用易通平台上寄件系统时，错误的填写信息而导致包裹无法邮寄至收件人等一系列问题，易通平台免责。

4.  用户如点击易通平台的链接站点，便离开了易通平台页面。与易通平台链接的站点将不受易通平台控制，因此易通平台对所有链接网站的内容不予承担责任。

5.  除非另有明确的书面说明,易通平台及其所包含的或以其它方式通过易通提供给您的全部信息、内容、材料、产品（包括软件）和服务，均是在“按现状”和“按现有”的基础上提供的。

6.  除非另有明确的书面说明,易通不对易通平台的运营及其包含在易通平台上的信息、内容、材料、产品（包括软件）或服务作任何形式的、明示或默示的声明或担保（根据中华人民共和国法律另有规定的以外）。 

7.  易通不担保易通平台所包含的或以其它方式通过易通平台提供给您的全部信息、内容、材料、产品（包括软件）、服务、服务器或从易通平台发出的电子信件、信息没有病毒或其他有害成分。

8.  任何由于黑客攻击、计算机病毒侵入或者发作、因政府管制而造成的暂时性关闭等影响网络正常经营的不可抗力而造成的个人资料泄露、丢失、被盗用或被篡改，易通会合理地尽力协助处理善后事宜，但不因此承担责任。

9.  因网络状况、通讯线路、第三方网站等任何原因而导致您不能正常使用易通平台，易通平台不承担任何法律责任。由于本平台连接的其它网站所造成的相关问题及由此导致的任何法律正义和后果，易通本平台当然免责。

10.      本平台如因网络维护或者升级而需暂停服务时，将事先发出公告。如果因网络线路以及本公司控制范围外的硬件故障或其它不可抗力而导致暂停服务，于暂停服务期间造成的一切不便与损失，本平台不负任何责任。

八、   协议终止   

1. 您同意，易通有权自行全权决定以任何理由不经事先通知的中止、终止向您提供部分或全部易通平台服务，暂时冻结或永久冻结（注销）您的账户，且无须为此向您或任何第三方承担任何责任。

2. 出现以下情况时，易通有权直接以注销账户的方式终止本协议:

（1）  易通终止向您提供服务后，您涉嫌再一次直接或间接或以他人名义注册为易通用户的；

（2）  您提供的电子邮箱不存在或无法接收电子邮件，且没有其他方式可以与您进行联系，或易通以其它联系方式通知您更改电子邮件信息，而您在易通通知后三个工作日内仍未更改为有效的电子邮箱的；

（3）  您注册信息中的主要内容不真实或不准确或不及时或不完整；

（4）  本协议（含规则）变更时，您明示并通知易通不愿接受新的服务协议的；

（5）  其它易通认为应当终止服务的情况。

3. 您有权向易通要求注销您的账户，经易通审核同意的，易通注销（永久冻结）您的账户，届时，您与易通基于本协议的合同关系即终止。您的账户被注销（永久冻结）后，易通没有义务为您保留或向您披露您账户中的任何信息，也没有义务向您或第三方转发任何您未曾阅读或发送过的信息。

4. 您同意，您与易通的协议关系终止后，易通仍享有下列权利

（1）  继续保存您的注册信息及您使用易通平台服务期间的所有交易信息。

（2）  您在使用易通平台服务期间存在违法行为或违反本协议和/或规则的行为的，易通仍可依据本协议向您主张权利。

九、   隐私权保护   

保护用户隐私是易通的一项基本政策，易通承诺不对外公开或向第三方提供单个用户的注册资料及用户在使用网络服务时存储在易通平台的非公开内容，但下列情况除外：

1. 事先获得用户的明确授权；

2. 根据有关的法律法规要求；

3. 按照相关政府主管部门的要求；

4. 为维护社会公众的利益；

5. 为维护易通的合法权益所进行的适当公开。

十、   法律适用、管辖与其他

1. 本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，如无相关法律规定的，则应参照通用国际商业惯例和（或）行业惯例。

2. 因本协议产生之争议，应依照中华人民共和国法律予以处理，并以易通住所所在地人民法院为第一审管辖法院。
							</textarea>
						</p>
					</form>
				</div>
				
			</div>
		
	</div>
		<script type="text/javascript">

		function killerrors() { 
			return true; 
		} 
		window.onerror = killerrors;
		
		$(function() {
		/*	
				var _ = $("#mgnm");
				$(_).validationEngine(
					{
						ajaxSubmit : true,
						ajaxSubmitFile : "noint!bindTaoBaoUserStep1.action",
						ajaxSubmitMessage : "激活成功!",
						success : true,
						myValidation : function() {
							var regT = /^\d{3,4}-\d{4}|\d{7,8}-\d{4}|\d{7,8}$/, regM = /^1[3458]\d{9}$/;
							var tv = $.trim($("#tel").val()), mv = $.trim($("#mp").val());
							var pwd = $.trim($("#pwd").val()), pwd2 = $.trim($("#pwd2").val());
							if(pwd != "" ) {
								if(pwd.length < 6) {
									art.dialog.alert("密码长度不能小于6!");
									$("#pwd").select();
									return false;
								}
								if(pwd != pwd2) {
									alert("两次密码不同!");
									$("#pwd2").select();
									return false;
								}
							}
							if (tv == "" && mv == "") {
								alert("固定电话和手机号码必须填写一个!");
								$("#mp").focus();
								return false;
							}
							if (tv != "") {
								if (!regT.test(tv)) {
									alert("固定电话格式不对!");
									$("#tel").select();
									return false;
								}
							}
							if (mv != "") {
								if (!regM.test(mv)) {
									alert("手机号码格式不对!");
									$("#mp").select();
									return false;
								}
							}
							return true;
						},
						beforeSuccess : function() {
							// 禁用提交按钮
							$(_).find(":submit").css({
								color : "gray",
								cursor : "default"
							});
						},
						afterSuccess : function() {
							$(_)[0].reset();
							$(_).find(":submit").css({
								color : "#FFFFFF",
								cursor : "pointer"
							});
							
							window.location.href = "noint!toBindTBUserStep2.action";
						},
						failure : function() {
							art.dialog.alert("激活失败!");
							$(_).find(":submit").css({
								color : "#FFFFFF",
								cursor : "pointer"
							});
						}
					});
				*/
				var $_$ = $("#ajaxAlertText").val();
				if($_$ != "") alert($_$);
				
				$("#mgnm").submit(function(){
					//noint!bindTaoBaoUserStep1.action
					var regT = /^\d{3,4}-\d{4}|\d{7,8}-\d{4}|\d{7,8}$/, regM = /^1[3458]\d{9}$/;
					var tv = $.trim($("#tel").val()), mv = $.trim($("#mp").val());
					var pwd = $.trim($("#pwd").val()), pwd2 = $.trim($("#pwd2").val());
					var flag = true;
					if(pwd == "") {
						art.dialog.alert("密码不能为空!");
						$("#pwd").select();
						flag = false;
						return false;
					}
					if(pwd != "" ) {
						if(pwd.length < 6) {
							art.dialog.alert("密码长度不能小于6!");
							$("#pwd").select();
							flag = false;
							return false;
						}
						if(pwd != pwd2) {
							art.dialog.alert("两次密码不同!");
							$("#pwd2").select();
							flag = false;
							return false;
						}
					}
					if (tv == "" && mv == "") {
						art.dialog.alert("固定电话和手机号码必须填写一个!");
						$("#mp").focus();
						flag = false;
						return false;
					}
					if (tv != "") {
						if (!regT.test(tv)) {
							art.dialog.alert("固定电话格式不对!");
							$("#tel").select();
							flag = false;
							return false;
						}
					}
					if (mv != "") {
						if (!regM.test(mv)) {
							art.dialog.alert("手机号码格式不对!");
							$("#mp").select();
							flag = false;
							return false;
						}
					}
					
					if(!flag) return false;
					
					$(this).attr("action","noint!bindTaoBaoUserStep1.action")
					$.submit();
				});
				
				// 免责协议	 
				$("#_cMz").click(function() {
					var t = $("#_tMz"), b = $("#_bA");
					if(!this.checked) {
						$(t).fadeIn("slow");
						$(b).attr("disabled", "disabled").css("cursor", "default");
					}
					else {
						$(t).fadeOut("slow");
						$(b).removeAttr("disabled").css("cursor", "pointer");
					}
				}); // 免责协议
				
			}); // $.();
			
		</script>
	<jsp:include page="/WEB-INF/googleAnalytics.jsp"></jsp:include>
	</body>
</html>