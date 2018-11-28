$(function() {
	var activeDialog = (function(){
		
		// 缓存页面上的全局变量 params
		var winParams = window.params || {};
		
		// 配置
		var config = {
				onStep: winParams.onStep || -1,					// 用户类型
				isShowButton : winParams.isShowButton || false,
				showBindCode : winParams.showBindCode || false,
				userId : winParams.userId || 0,
				userName : winParams.userName || '',
				infoFormAction: winParams.infoFormAction || '',		// 填写信息表单 action
				bindFormAction: winParams.bindFormAction || '',		// 绑定网点表单 action
				pointsGetUrl: winParams.pointsGetUrl,               // 获取对应网点 url
				custComfUrl: winParams.bindFormAction+"autoGen&user.site=",                 // 客户编码确认 url
				tickGetUrl: winParams.pointsGetUrl+"?mailNo="                    // 运单号获取 url
				
		};
		
		// 地区多级联动
		var linkageSel;
		var selectArea = function() {
			var area = {
					data: districtData,
					selStyle: 'margin-left:3px;',
					select: ['#act_province'],
					autoLink:false
			};
			
			linkageSel = new LinkageSel(area);
		};
		
		// 步骤
		var step = {
				// 第一步弹层内容
				startContent:   '<!-- S 完善基本信息 -->' +
				'<div id="active_first">' +
				'	<div id="process">' +
				'		<ol>' +
				'			<li id="process_cur">1、完善基本信息</li>' +
				'			<li>2、绑定网点（可选）</li>' +
				'			<li id="process_last">3、开始使用易通</li>' +
				'		</ol>' +
				'	</div>' +
				'	<div id="statement">' +
				'		<ol>' +
				'			<li>1、圆通电商客户专享；</li>' +
				'			<li>2、目前仅开放给淘宝卖家和B2C电商客户。</li>' +
				'		</ol>' +
				'	</div>' +
				'	<form action="' + config.infoFormAction + '" id="act_info_form" class="form">' +
				'		<fieldset>' +
				'			<legend>基本信息</legend>' +
				'           <input type="hidden" name="user.id" id="act_id" value="'+config.userId+'" />'+
				'           <input type="hidden" name="user.userName" id="act_un" value="'+config.userName+'" />'+
				'			<p>' +
				'				<label for="act_shop_name"><span class="req">*</span>店铺名称：</label>' +
				'				<input type="text" class="input_text" id="act_shop_name" name="user.shopName" />' +
				'				<span id="act_shop_nameTip"></span>' +
				'			</p>' +
				'			<p>' +
				'				<label for="act_mobile_tel"><span class="req">*</span>手机号码：</label>' +
				'				<input type="text" class="input_text" id="act_mobile_tel" name="user.mobilePhone" />' +
				'				<span id="act_mobile_telTip" class="check_result"></span>' +
				'			</p>' +
				'			<p>' +
				'				<label for="act_province"><span class="req">*</span>公司地址：</label>' +
				'				<select id="act_province"></select>' +
				'				<span id="act_area_tip"></span>' +
				'			</p>' +
				'			<p>' +
				'				<textarea cols="51" rows="2" class="textarea_text" id="act_detail_address" name="user.addressStreet"></textarea>' +
				'				<span id="act_detail_addressTip"></span>' +
				'			</p>' +
				'		</fieldset>' +
				'	</form>' +
				'</div>' +
				'<!-- E 完善基本信息 -->',
				
				// 第二步弹层内容
				bindContent:    '<!-- S 绑定客户编码 -->' +
				'<div id="active_second">' +
				'	<div id="process">' +
				'		<ol>' +
				'			<li id="process_first">1、完善基本信息</li>' +
				'			<li id="process_cur">2、绑定网点（可选）</li>' +
				'			<li id="process_last">3、开始使用易通</li>' +
				'		</ol>' +
				'	</div>' +
				'	<div id="statement">' +
				'		<p>选择圆通服务网点后，您可以使用问题件管理等服务！</p>' +
				'	</div>' +
				'	<form action="' + config.bindFormAction + '" id="act_bind_form" class="form">' +
				'       <div class="act_info">' +
				'	       <div class="act_loading">正在获取网点信息，可能需要花费几分钟，请耐心等待！</div>' +
				'          <a title="网点信息有误，请点击这里" class="act_learn" href="javascript:;">不想等了，使用手动绑定</a>' +
				'       </div>' +
				'       <div class="act_solution">' +
				'          <div class="act_notice clearfix"><a title="上一步" class="act_back" href="javascript:;"></a>两个方法任选其一</div>' +
				'          <div class="act_ticket">' +
				'		      <label for="act_ticket_code">方法一：通过运单号获取您的网点信息</label>' +
				'             <input type="text" id="act_ticket_code" class="input_text" />' +
				'             <a title="获取" class="btn btn_a" href="javascript:;" id="info_get"><span>获取</span></a>' +
				'             <span id="act_ticket_codeTip"></span>' +
				'          </div>' +
				'          <div class="act_customer">' +
				'             <label for="act_customer_code">方法二：向你的圆通服务网点获取客户编码</label>' +
				'			  <input type="text" id="act_customer_code" class="input_text" />' +
				'             <a title="识别" class="btn btn_a" href="javascript:;" id="info_comfirm"><span>识别</span></a>' +
				'             <span id="act_customer_codeTip"></span>' +
				'		   </div>' +
				'          <div class="act_result">' +
				'          </div>' +
				'       </div>' +
				'       <input id="act_point" type="hidden" value="" />' +
				'       <input id="act_type" type="hidden" value="1" />' +
				'	</form>' +
				'</div>' +
				'<!-- E 绑定客户编码 -->',
				
				// 第三步弹层内容
				succContent: '绑定成功',
				
				
				// “开始使用”弹层
				startDialog: function() {
					// 弹层
					var sDialog = new Dialog();
					sDialog.init({
						contentHtml: step.startContent,
						closeBtn: true,
						yes: function() {
							var validateResult = $.formValidator.pageIsValid('99');
							// 如果验证通过
							if (validateResult) {
								// 异步提交表单
								$.ajax({
									type: 'POST',
									url: config.infoFormAction,
									data: ({
										//psw: $('#act_psw').val(),
										"user.id": $("#act_id").val() ,
										"user.userName": $("#act_un").val() ,
										"user.shopName": $('#act_shop_name').val(),
										"user.mobilePhone": $('#act_mobile_tel').val(),
										//"user.addressProvince": $('#act_province').val(),
										"user.addressProvince": linkageSel.getSelectedData('name',0),
										//"user.addressCity": $('#act_province').next().val(),
										"user.addressCity": linkageSel.getSelectedData('name',1),
										//"user.addressDistrict": $('#act_province').next().next().val(),
										"user.addressDistrict": linkageSel.getSelectedData('name',2),
										"user.addressStreet": $('#act_detail_address').val()
									}),
									timeout: 8000,
									success: function(data) {
										if(data.status){
											if(config.isShowButton){
												step.bindCode();
											}
											sDialog.close();
											// 弹出“绑定”弹层
											step.bindDialog();
										}
										else{
											var oDialog = new Dialog();
											oDialog.init({
												contentHtml: data.infoContent,
												yes: function() {
													oDialog.close();
												},
												closeBtn: true
											});
										}
									}
								});
							}
						},
						yesVal: '同意并下一步'
					});

					var agreement = '<div id="agreement_dialog">' +
									'	<h3>易通平台用户注册协议</h3>' +
									'	<p>本协议是您与易通电子商务物流信息服务平台所有者-上海圆通新龙电子商务有限公司（以下简称为“圆通新龙”）之间就易通服务等相关事宜所订立的契约，本协议中易通电子商务物流信息服务平台简称“易通平台”，域名为ec.yto.net.cn。请您仔细阅读本注册协议，<strong>您点击“同意以下协议，提交”按钮后，本协议即构成对双方有约束力的法律文件。</strong></p>' +
									'	<ol>' +
									'		<li>' +
									'			<h4>一、协议内容及签署</h4>' +
									'			<ol>' +
									'				<li>1.本协议内容包括协议正文及所有圆通新龙已经发布的或将来可能发布的各类规则。所有规则为本协议不可分割的组成部分，与协议正文具有同等法律效力。除另行明确声明外，任何圆通新龙提供的服务均受本协议约束。</li>' +
									'				<li>2.易通平台的各项电子服务的所有权和运作权归圆通新龙所有。用户同意所有注册协议条款并完成注册程序，才能成为易通平台的正式用户。用户确认：本协议条款是处理双方权利义务的契约，始终有效，法律另有强制性规定或双方另有特别约定的，依其规定或约定。</li>' +
									'				<li>3.用户点击同意本协议的，即视为用户确认自己具有享受圆通新龙服务等相应的权利能力和行为能力，能够独立承担法律责任。<strong>只要您使用易通平台服务，则本协议即对您产生约束，届时您不应以未阅读本协议的内容或者未获得圆通新龙对您问询的解答等理由，主张本协议无效，或要求撤销本协议。</strong></li>' +
									'				<li>4.您承诺接受并遵守本协议的约定。如果您不同意本协议的约定，您应立即停止注册程序或停止使用易通平台服务。</li>' +
									'				<li>5.圆通新龙有权根据需要不时地制订、修改本协议及/或各类规则，并以网站公示的方式进行公告，不再单独通知您。变更后的协议和规则一经在网站公布后，立即自动生效。如您不同意相关变更，应当立即停止使用圆通新龙服务。您继续使用圆通新龙服务的，即表示您接受经修订的协议和规则。</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>二、会员注册</h4>' +
									'			<ol>' +
									'				<li>1.用户应自行诚信向易通平台提供注册资料，用户同意其提供的注册资料真实、准确、完整、合法有效，用户注册资料如有变动的，应及时更新其注册资料。如果用户提供的注册资料不合法、不真实、不准确、不详尽的，用户需承担因此引起的相应责任及后果，并且圆通新龙保留终止用户使用易通平台各项服务的权利。</li>' +
									'				<li>2.用户注册成功后，将产生用户名和密码等账户信息，您可以根据易通平台规定改变您的密码。用户应谨慎合理的保存、使用其用户名和密码。用户若发现任何非法使用用户账号或存在安全漏洞的情况，请立即通知圆通新龙。</li>' +
									'				<li>3.在您签署本协议，完成会员注册程序或实际使用易通平台时，圆通新龙会向您提供唯一编号的易通账户（以下简称账户）。您可以对账户设置会员名和密码，通过该会员名密码或与该会员名密码关联的其它用户名密码登陆易通平台。您设置的会员名不得侵犯或涉嫌侵犯他人合法权益。如您连续一年未使用您的会员名和密码登录易通平台，圆通新龙有权终止向您提供易通平台服务，注销您的账户。账户注销后，相应的会员名将开放给任意用户注册登记使用。</li>' +
									'				<li>4.用户不得将在易通平台注册获得的账户借给他人使用，否则用户应承担由此产生的全部责任，并与实际使用人承担连带责任。</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>三、服务</h4>' +
									'			<ol>' +
									'				<li>1.通过圆通新龙及相关公司提供的易通平台服务和其它服务，用户可以在易通平台上包括但不限于发布订单信息、查询订单信息、与物流服务点进行沟通、参加圆通新龙组织的活动以及使用其它信息服务及技术服务。</li>' +
									'				<li>2.用户同意，圆通新龙包括但不限于通过电子邮件、手机短信、电话等形式，向易通平台用户、收货人、物流服务点等发送信息的权利。</li>' +
									'				<li>3.您了解并同意，圆通新龙有权应政府部门（包括司法及行政部门）的要求，向其提供您在易通平台填写的注册信息等必要信息。如您涉嫌侵犯他人知识产权，则圆通新龙亦有权在初步判断涉嫌侵权行为存在的情况下，向权利人可提供您必要的身份信息。</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>四、易通平台服务使用规范</h4>' +
									'			<ol>' +
									'				<li>' +
									'					<p>1.在易通平台上使用服务过程中，您承诺遵守以下约定：</p>' +
									'					<ol>' +
									'						<li>(1) 在使用易通平台服务过程中实施的所有行为均遵守国家法律、法规等规范性文件及易通平台各项规则的规定和要求，不违背社会公共利益或公共道德，不损害他人的合法权益，不违反本协议及相关规则。您如果违反前述承诺，产生任何法律后果的，您应以自己的名义独立承担所有的法律责任，并确保圆通新龙免于因此产生任何损失。</li>' +
									'						<li>(2) 不对易通平台上的任何数据作商业性利用，包括但不限于在未经圆通新龙事先书面同意的情况下，以复制、传播等任何方式使用易通平台上展示的资料。</li>' +
									'						<li>(3) 不使用任何装置、软件或例行程序干预或试图干预易通平台的正常运作。</li>' +
									'						<li>(4) 您不得采取任何将导致不合理的庞大数据负载加诸易通平台网络设备的行动。</li>' +
									'					</ol>' +
									'				</li>' +
									'				' +
									'				<li>' +
									'					<p>2.您了解并同意：</p>' +
									'					<ol>' +
									'						<li>(1) 圆通新龙有权对您是否违反上述承诺做出单方认定，并根据单方认定结果适用规则予以处理或终止向您提供服务，且无须征得您的同意或提前通知予您。</li>' +
									'						<li>(2) 经国家行政或司法机关的生效法律文书确认您存在违法或侵权行为，或者圆通新龙根据自身的判断，认为您的行为涉嫌违反本协议和/或规则的条款或涉嫌违反法律法规的规定的，则圆通新龙有权在易通平台上公示您的该等涉嫌违法或违约行为及圆通新龙已对您采取的措施。</li>' +
									'						<li>(3) 对于您在易通平台上发布的涉嫌违法或涉嫌侵犯他人合法权利或违反本协议和/或规则的信息，圆通新龙有权不经通知您即予以删除，且按照规则的规定进行处罚。</li>' +
									'						<li>(4) 对于您在易通平台上实施的行为，包括您未在易通平台上实施但已经对易通平台及其用户产生影响的行为，圆通新龙有权单方认定您行为的性质及是否构成对本协议和/或规则的违反，并据此做出相应处罚。您应自行保存与您行为有关的全部证据，并应对无法提供充要证据而承担的不利后果。</li>' +
									'						<li>(5) 对于您涉嫌违反承诺的行为对任意第三方造成损害的，您均应当以自己的名义独立承担所有的法律责任，并应确保圆通新龙免于因此产生损失或增加费用。</li>' +
									'						<li>(6) 如您涉嫌违反有关法律或者本协议之规定，使圆通新龙遭受任何损失，或受到任何第三方的索赔，或受到任何行政管理部门的处罚，您应当赔偿圆通新龙因此造成的损失及/或发生的费用，包括合理的律师费用。</li>' +
									'					</ol>' +
									'				</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>五、所有权及知识产权条款</h4>' +
									'			<ol>' +
									'				<li>1.用户一旦接受本协议，即表明该用户主动将其在任何时间段在本站发表的任何形式的信息内容的财产性权利等任何可转让的权利，如著作权财产权全部独家且不可撤销地转让给圆通新龙所有，用户同意易通平台有权就任何主体侵权而单独提起诉讼。</li>' +
									'				<li>2.本协议已经构成《中华人民共和国著作权法》第二十五条（条文序号依照2011年版著作权法确定）及相关法律规定的著作财产权等权利转让书面协议，其效力及于用户在易通平台上发布的任何受著作权法保护的作品内容，无论该等内容形成于本协议订立前还是本协议订立后。</li>' +
									'				<li>3.圆通新龙是本平台的制作者,拥有此平台内容及资源的著作权等合法权利,受国家法律保护,有权不时地对本协议及本平台的内容进行修改，并在平台张贴，无须另行通知用户。在法律允许的最大限度范围内，圆通新龙对本协议及平台内容拥有解释权。</li>' +
									'				<li>4.除法律另有强制性规定外，未经圆通新龙明确的特别书面许可,任何单位或个人不得以任何方式非法地全部或部分复制、转载、引用、链接、抓取或以其他方式使用本站的信息内容，否则，圆通新龙有权追究其法律责任。</li>' +
									'				<li>5.本站所刊登的资料信息（诸如文字、图表、标识、按钮图标、图像、声音文件片段、数字下载、数据编辑和软件），均是易通平台或其内容提供者的财产，受中国和国际版权法的保护。本站上所有内容的汇编是易通平台的排他财产，受中国和国际版权法的保护。本站上所有软件都是易通平台或其关联公司或其软件供应商的财产，受中国和国际版权法的保护。</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>六、特别授权</h4>' +
									'			<p>您完全理解并不可撤销地授予圆通新龙下列权利：</p>' +
									'			<ol>' +
									'				<li>1.一旦您向圆通新龙做出任何形式的承诺，且已确认您违反了该承诺，则圆通新龙有权立即按您的承诺或协议约定的方式对您的账户采取限制措施，包括中止或终止向您提供服务，并公示相关公司确认的您的违约情况。您了解并同意，圆通新龙无须就相关确认与您核对事实，或另行征得您的同意，且圆通新龙无须就此限制措施或公示行为向您承担任何的责任。</li>' +
									'				<li>2.对于您提供的资料及数据信息，您授予圆通新龙独家的、全球通用的、永久的、免费的许可使用权利 (并有权在多个层面对该权利进行再授权)。此外，圆通新龙及其关联公司有权全部或部份地使用、复制、修订、改写、发布、翻译、分发、执行和展示您提供的资料及数据信息或制作其派生作品，并以现在已知或日后开发的任何形式、媒体或技术，将上述信息纳入其它作品内。</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>七、责任范围和责任限制</h4>' +
									'			<ol>' +
									'				<li>1.易通平台所提供的所有资料、数据及信息，仅供参考使用。任何个人、公司及单位等依据易通平台提供的资料、数据及资料进行相关活动所造成的盈亏与易通平台无关。如发现信息有误或其它问题，请及时通过信息反馈与我们联系。</li>' +
									'				<li>2.由于用户恶意填写寄件信息等造成与易通平台对接的快递公司工作负担，或者导致用户与快递公司纠纷的，易通平台免责。</li>' +
									'				<li>3.由于用户使用易通平台上寄件系统时，错误的填写信息而导致包裹无法邮寄至收件人等一系列问题，易通平台免责。</li>' +
									'				<li>4.用户如点击易通平台的链接站点，便离开了易通平台页面。与易通平台链接的站点将不受易通平台控制，因此易通平台对所有链接网站的内容不予承担责任。</li>' +
									'				<li>5.除非另有明确的书面说明,易通平台及其所包含的或以其它方式通过圆通新龙提供给您的全部信息、内容、材料、产品（包括软件）和服务，均是在“按现状”和“按现有”的基础上提供的。</li>' +
									'				<li>6.除非另有明确的书面说明,圆通新龙不对易通平台的运营及其包含在易通平台上的信息、内容、材料、产品（包括软件）或服务作任何形式的、明示或默示的声明或担保（根据中华人民共和国法律另有规定的以外）。</li>' +
									'				<li>7.圆通新龙不担保易通平台所包含的或以其它方式通过易通平台提供给您的全部信息、内容、材料、产品（包括软件）、服务、服务器或从易通平台发出的电子信件、信息没有病毒或其他有害成分。</li>' +
									'				<li>8.任何由于黑客攻击、计算机病毒侵入或者发作、因政府管制而造成的暂时性关闭等影响网络正常经营的不可抗力而造成的个人资料泄露、丢失、被盗用或被篡改，圆通新龙会合理地尽力协助处理善后事宜，但不因此承担责任。</li>' +
									'				<li>9.因网络状况、通讯线路、第三方网站等任何原因而导致您不能正常使用易通平台，易通平台不承担任何法律责任。由于本平台连接的其它网站所造成的相关问题及由此导致的任何法律正义和后果，易通本平台当然免责。</li>' +
									'				<li>10.本平台如因网络维护或者升级而需暂停服务时，将事先发出公告。如果因网络线路以及本公司控制范围外的硬件故障或其它不可抗力而导致暂停服务，于暂停服务期间造成的一切不便与损失，本平台不负任何责任。</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>八、协议终止</h4>' +
									'			<ol>' +
									'				<li>1.您同意，圆通新龙有权自行全权决定以任何理由不经事先通知的中止、终止向您提供部分或全部易通平台服务，暂时冻结或永久冻结（注销）您的账户，且无须为此向您或任何第三方承担任何责任。</li>' +
									'				<li>' +
									'					2.出现以下情况时，圆通新龙有权直接以注销账户的方式终止本协议:' +
									'					<ol>' +
									'						<li>(1) 易通终止向您提供服务后，您涉嫌再一次直接或间接或以他人名义注册为易通用户的；</li>' +
									'						<li>(2) 您提供的电子邮箱不存在或无法接收电子邮件，且没有其他方式可以与您进行联系，或圆通新龙以其它联系方式通知您更改电子邮件信息，而您在圆通新龙通知后三个工作日内仍未更改为有效的电子邮箱的；</li>' +
									'						<li>(3) 您注册信息中的主要内容不真实或不准确或不及时或不完整；</li>' +
									'						<li>(4) 本协议（含规则）变更时，您明示并通知圆通新龙不愿接受新的服务协议的；</li>' +
									'						<li>(5) 其它圆通新龙认为应当终止服务的情况。</li>' +
									'					</ol>' +
									'				</li>' +
									'				<li>3.您有权向圆通新龙要求注销您的账户，经易通审核同意的，圆通新龙注销（永久冻结）您的账户，届时，您与圆通新龙基于本协议的合同关系即终止。您的账户被注销（永久冻结）后，圆通新龙没有义务为您保留或向您披露您账户中的任何信息，也没有义务向您或第三方转发任何您未曾阅读或发送过的信息。</li>' +
									'				<li>' +
									'					4.您同意，您与圆通新龙的协议关系终止后，圆通新龙仍享有下列权利' +
									'					<ol>' +
									'						<li>(1) 继续保存您的注册信息及您使用易通平台服务期间的所有交易信息。</li>' +
									'						<li>(2) 您在使用易通平台服务期间存在违法行为或违反本协议和/或规则的行为的，圆通新龙仍可依据本协议向您主张权利。</li>' +
									'					</ol>' +
									'				</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>九、隐私权保护</h4>' +
									'			<p>保护用户隐私是圆通新龙的一项基本政策，圆通新龙承诺不对外公开或向第三方提供单个用户的注册资料及用户在使用网络服务时存储在易通平台的非公开内容，但下列情况除外：</p>' +
									'			<ol>' +
									'				<li>1.事先获得用户的明确授权；</li>' +
									'				<li>2.根据有关的法律法规要求；</li>' +
									'				<li>3.按照相关政府主管部门的要求；</li>' +
									'				<li>4.为维护社会公众的利益；</li>' +
									'				<li>5.为维护圆通新龙的合法权益所进行的适当公开。</li>' +
									'			</ol>' +
									'		</li>' +
									'		' +
									'		<li>' +
									'			<h4>十、法律适用、管辖与其他</h4>' +
									'			<ol>' +
									'				<li>1.本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，如无相关法律规定的，则应参照通用国际商业惯例和（或）行业惯例。</li>' +
									'				<li>2.因本协议产生之争议，应依照中华人民共和国法律予以处理，并以圆通新龙住所所在地人民法院为第一审管辖法院。</li>' +
									'			</ol>' +
									'		</li>' +
									'	</ol>' +
									'</div>';
					$(agreement).insertAfter($('#dialog_ft'));
					
					
					// 多级地区
					selectArea();
					
					// 重新绑定聚焦事件
					ytoCommon.setStatus();
					
					// 元素集合
					var els = {
							// psw: $('#act_psw'),
							// repsw: $('#act_repsw'),
							shopName: $('#act_shop_name'),
							mobile: $('#act_mobile_tel'),
							act_province: $('#act_province'),
							detailAdd: $('#act_detail_address')
					};
					
					// 提示文案
					var tipsMsg = {
							// pswRule: '英文、数字、符号，6-16个字符',
							// pswFormatErr: '密码格式不正确',
							// pswErr: '两次密码不一致，请确认',
							shopErr: '店铺名称必须是4-30个字符',
							mobileErr: '手机号码格式不正确',
							provinceErr: '请选择省',
							cityErr: '请选择市',
							areaErr: '请选择区',
							detailMinErr: '街道地址不能为空',
							detailMaxErr: '街道地址超长'
					};
					
					// 完善基本信息表单
					$.formValidator.initConfig({
						validatorGroup: '99',
						formID: 'act_info_form',
						theme: 'yto',
						errorFocus: false
					});
					
					// 店铺名称
					els.shopName.
					formValidator({validatorGroup:'99', onShow: '', onFocus: '', onCorrect: ' '}).
					inputValidator({min:4, max:30, onError: tipsMsg.shopErr});
					
					// 手机号
					els.mobile.
					formValidator({validatorGroup:'99', onShow: '', onFocus: '', onCorrect: ' '}).
					regexValidator({regExp: '^1\\d{10}$', onError: tipsMsg.mobileErr});
					
					// 发货地址
					els.act_province.
					formValidator({validatorGroup:'99', tipID: 'act_area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 省
						els.act_province.next().
						formValidator({validatorGroup:'99', tipID: 'act_area_tip', onShow: '', onFocus: '', onCorrect: function() {	// 市
							els.act_province.next().next().
							formValidator({validatorGroup:'99', tipID: 'act_area_tip', onShow: '', onFocus: '', onCorrect: ' '}).	// 区
							inputValidator({min:1, onError: tipsMsg.areaErr});
							return '';
						}}).
						inputValidator({min:1, onError: tipsMsg.cityErr});
						return '';
					}}).
					inputValidator({min:1, onError: tipsMsg.provinceErr});
					
					// 街道地址
					els.detailAdd.
					formValidator({validatorGroup:'99', onShow: '', onFocus: '', onCorrect: ' '}).
					inputValidator({empty:false, min: 1, max: 100, onErrorMin: tipsMsg.detailMinErr, onErrorMax: tipsMsg.detailMaxErr});
				},
				
				// “绑定网点”弹层
				bindDialog: function() {
					var subFlag = 0,
						currentCall,
					    timer;
					
					var bDialog = new Dialog();
					var bFlag = true;
					bDialog.init({
						contentHtml: step.bindContent,
						closeBtn: true,
						yes: function() {
							// 如果方法一验证通过
							var condition1 = $("#act_type").val() == 1 && els.pointHidden.val() !== '';
							// 如果方法二验证通过
							var condition2 = $("#act_type").val() == 2 && els.pointHidden.val() !== '';
							if (condition1 || condition2) {
								// 正在进行绑定
								var loadDialog = new Dialog();
								loadDialog.init({
									contentHtml: '正在进行绑定...'
								});
								if(!bFlag){
									return;
								}
								els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
								bFlag = false;
								// 异步提交表单
								$.ajax({
									type: 'GET',
									url: condition1 ? config.custComfUrl+els.pointHidden.val() : config.bindFormAction+els.pointHidden.val(),
									dataType: "json",
									timeout: 8000,
									success: function(data) {
										
										if(data.status){
											bDialog.close();
											// 弹出“绑定成功”弹层
											step.succDialog();
											// 删除“绑定网点”按钮
											$('#act_bind_btn').remove();
											$('#guide_btn').hide();
											$('#p_fun').show();
										}
										else{
											var oDialog = new Dialog();
											oDialog.init({
												contentHtml: data.infoContent,
												yes: function() {
													oDialog.close();
												},
												closeBtn: true
											});
										}
									},
									error: function() {
//										if (typeof console !== 'undefined') {
//											console.log('Ajax Error!');
//										}
									}
								});
								
							}
						},
						no: function() {
							bDialog.close();
							
							// “开始使用”替换为“绑定网点”按钮
							$('#act_start_btn').replaceWith('<a href="javascript:;" id="act_bind_btn" class="btn btn_a" title="绑定网点"><span>绑定网点</span></a>');
						},
						yesVal: '绑定网点',
						noVal: '稍后再说'
					});
					
					// 当前 ajax 请求
					var call = {
						auto: 'auto',
						cust: 'cust',
						tick: 'tick'
					};
					
					// 显示提示
					var showIcon = {
						correct: function(el, text) {
							el.html('<span>' + text + '</span>');
						},
						error: function(el, text) {
							el.html('<span class="yto_onError">' + text + '</span>');
						}
					};
					
					// 默认文案
					var textDefault = {
						ticket: '输入最近发货并已签收的运单号',	
						customer: '输入客户编码'
					};
					
					// 元素集合
					var els = {
						infoDiv: $('#act_bind_form .act_info'),
						solutionDiv: $('#act_bind_form .act_solution'),
						ticketCode: $('#act_ticket_code'),
						ticketCodeTip: $('#act_ticket_codeTip'),
						customerCode: $('#act_customer_code'),
						customerCodeTip: $('#act_customer_codeTip'),
						getBtn: $('#info_get'),
						confirmBtn: $('#info_comfirm'),
						back: $('#act_bind_form .act_back'),
						learn: $('#act_bind_form .act_learn'),
						resultDiv: $('#act_bind_form .act_result'),
						pointHidden: $('#act_point'),
						bindBtn: $('a:eq(0)', $('#active_second').parent().next())
					};
					
					// 提示文案
					var tipsMsg = {
						customerCodeEmptyErr: '客户编码不能为空',
						customerCodeFormatErr: '客户编码格式有误',
						ticketCodeEmptyErr: '运单号不能为空',
						ticketCodeFormatErr: '运单号格式有误',
						noInfo: '无法识别到您的网点信息！请联系客服：电话：021-64703131-107 QQ群：241711549'
					};
					
					// 绑定默认提示
					els.ticketCode.defaultTxt(textDefault.ticket);
					els.customerCode.defaultTxt(textDefault.customer);
					
					// 初始化绑定按钮状态
					els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
					
					// 异步获取对应网店信息
					currentCall = call.auto;
					$.ajax({
						type: 'GET',
						url: config.pointsGetUrl,
						dataType: "json",
						success: function(data) {
							
							if($('#active_second').length === 0 || currentCall !== call.auto){
								return;
							}
							
							var _els = els;
							
							if(data.status && data.dataList.length > 0){
								_els.learn.html('网点信息不是我的，请点击这里');
								var pointsHtml = '',
								    _points =data.dataList;
								$.each(_points, function(){
									var _p = this;
									if(_points.length==1) {
										pointsHtml += '<p>' + 
							              '   <input class="point" type="radio" checked name="point" value="' + _p.code + '"/>' + '<span class="point">' +_p.text + '</span>' +
							              '</p>';
										
										_els.pointHidden.val(_p.code);
										_els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
									} else {
										pointsHtml += '<p>' + 
							              '   <input class="point" type="radio" name="point" value="' + _p.code + '"/>' + '<span class="point">' +_p.text + '</span>' +
							              '</p>';
									}
									
								});
								
								$('.act_loading').removeClass().addClass('act_points').html(pointsHtml);
								
								// 选择相应网点
								_els.infoDiv.find('input[type="radio"]').change(function(){
									//标识方法一绑定
									$("#act_type").val("1");
									var _this = $(this);
									_els.pointHidden.val(_this.parent().find('input[type="radio"]:checked').val());
									_els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
								});
								_els.infoDiv.find('span').click(function(){
									//标识方法一绑定
									$("#act_type").val("1");
									var _this = $(this),
										radio = _this.parent().find('input[type="radio"]');
									
									radio.prop("checked", true);
									_els.pointHidden.val(radio.val());
									_els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
								});
							}
							else {
								_els.learn.hide();
								$('.act_loading').html('<a class="next" href="javascript:;">无法识别出您的网点，<span class="manual">请手动绑定~</span></a>');
								$('.act_loading .next').click(function(ev){
									ev.preventDefault();
									
									_els.learn.trigger('click');
								});
								//$('.act_loading').html('无法识别出您的网点，请手动绑定~');
								//_els.back.hide();
								//_els.learn.trigger("click");
							}
						},
						error: function() {
							/*if (typeof console !== 'undefined') {
								console.log('Ajax Error!');
							}*/
							
							els.learn.hide();
							$('.act_loading').html('<a class="next" href="javascript:;">无法识别出您的网点，<span class="manual">请手动绑定~</span></a>');
							$('.act_loading .next').click(function(ev){
								ev.preventDefault();
								
								els.learn.trigger('click');
							});
							//els.back.hide();
							//els.learn.trigger("click");
						}
					});
					
					
					// 点击解决方法
					els.learn.click(function(){
						var _els = els;
						_els.infoDiv.find('input[type="radio"]').prop("checked", false);
						_els.infoDiv.hide();
						_els.solutionDiv.show();
						_els.pointHidden.val('');
						_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
						showIcon.correct(els.ticketCodeTip, '');
						showIcon.correct(els.customerCodeTip, '');
					});
					
					// 返回
					els.back.click(function(){
						var _els = els;
						_els.infoDiv.show();
						_els.solutionDiv.hide();
						_els.ticketCode.val(textDefault.ticket).addClass('default_status');
						_els.customerCode.val(textDefault.customer).addClass('default_status');
						_els.resultDiv.html('');
						_els.pointHidden.val('');
						_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
						showIcon.correct(els.ticketCodeTip, '');
						showIcon.correct(els.customerCodeTip, '');
					});
					
					// 运单号验证
					els.ticketCode.blur(function(){
						$('#act_customer_codeTip').html('');
						
						var _val = $(this).val(),
						    _els = els;
						
						showIcon.correct(_els.ticketCodeTip, '');
						
						if(_val === '' || _val === textDefault.ticket){
							showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeEmptyErr);
						} else {
							var reg = /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/g;
							if(!reg.test(_val)){
								showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeFormatErr);
							}
						}
					});
					// 重置搜索结果与按钮状态
					els.ticketCode.change(function(){
						var _els = els;
						
						_els.resultDiv.html('');
						_els.pointHidden.val('');
						_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
					});
					// 运单号获取
					els.getBtn.click(function(){
						$('#act_ticket_codeTip').html('');
						$('#act_customer_codeTip').html('');
						
						//标识方法一绑定
						$("#act_type").val("1");
						
						var flag = true,
						    _val = els.ticketCode.val(),
						    _els = els;
						showIcon.correct(_els.ticketCodeTip, '');
						
						if(_val === '' || _val === textDefault.ticket){
							flag = false;
							showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeEmptyErr);
						} else {
							var reg = /^(0|1|2|3|4|5|6|7|8|9|E|D|F|G|V|W|e|d|f|g|v|w)[0-9]{9}$/g;
							if(!reg.test(_val)){
								flag = false;
								showIcon.error(_els.ticketCodeTip, tipsMsg.ticketCodeFormatErr);
							}
						}
						
						if(!flag){
							return;
						}
						
						_els.ticketCode.attr("disabled", "disabled");
						if(timer){
							clearTimeout(timer);
						}
						timer = setTimeout(function(){
							
							_els.resultDiv.html('正在获取信息，请等待...');
							
							currentCall = call.tick;
							$.ajax({
								dataType : "json",
								cache: false,
								type:"post",
								url : config.tickGetUrl + _val,
								success : function(response){
									
									if($('#active_second').length === 0 || currentCall !== call.tick){
										return;
									}
									
									var restDiv = _els.resultDiv,
								        potHidden = _els.pointHidden,
								        bindBtn = _els.bindBtn;
									
									if(response.status){
										restDiv.html('您的网点是：' + '<em>' + response.targetUrl + '</em>');
										potHidden.val(response.infoContent);
										bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
									}
									else {
										restDiv.html(tipsMsg.noInfo);
										potHidden.val('');
										bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
									}
								},
								error : function(){
									var restDiv = _els.resultDiv,
							        potHidden = _els.pointHidden,
							        bindBtn = _els.bindBtn;
									restDiv.html(tipsMsg.noInfo);
									potHidden.val('');
									bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
								},
								complete : function(){
									_els.ticketCode.removeAttr("disabled");
								}
							});
						}, 1000);
					});
					
					// 客户编码验证
					els.customerCode.blur(function(){
						$('#act_ticket_codeTip').html('');
						
						var _val = $(this).val(),
						    _els = els;
						
						showIcon.correct(_els.customerCodeTip, '');
						
						if(_val === '' || _val === textDefault.customer){
							showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeEmptyErr);
							
						}
						else {
							var reg = /[A-Za-z0-9]{1,100}$/g;
							if(!reg.test(_val)){
								showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeFormatErr);
							}
						}
					});
					// 重置搜索结果与按钮状态
					els.customerCode.change(function(){
						var _els = els;
						
						_els.resultDiv.html('');
						_els.pointHidden.val('');
						_els.bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
					});
					// 客户编码识别
					els.confirmBtn.click(function(){
						$('#act_ticket_codeTip').html('');
						$('#act_customer_codeTip').html('');
						
						//标识方法二绑定
						$("#act_type").val("2");
						
						var flag = true,
						    _val = els.customerCode.val(),
						    _els = els;
						showIcon.correct(_els.customerCodeTip, '');
						
						if(_val === '' || _val === textDefault.customer){
							flag = false;
							showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeEmptyErr);
						}
						var reg = /[A-Za-z0-9]{1,100}$/g;
						if(!reg.test(_val)){
							flag = false;
							showIcon.error(_els.customerCodeTip, tipsMsg.customerCodeFormatErr);
						}
						
						if(!flag){
							return;
						}
						
						_els.customerCode.attr("disabled", "disabled");
						if(timer){
							clearTimeout(timer);
						}
						
						timer = setTimeout(function(){
							_els.resultDiv.html('正在识别信息，请等待...');
							
							currentCall = call.cust;
							$.ajax({
								dataType : "json",
								cache: false,
								type:"POST",
								data:{"user.userCode":_val},
								url : "user!checkNewUserCode.action" ,
								success : function(response){

									if($('#active_second').length === 0 || currentCall !== call.cust){
										return;
									}
									
									var restDiv = _els.resultDiv,
								        potHidden = _els.pointHidden,
								        bindBtn = _els.bindBtn;
									
									if(response.status){
										restDiv.html('您的网点是：' + '<em>' + response.infoContent + '</em>');
										potHidden.val(_val);
										els.bindBtn.removeClass('btn_e').addClass('btn_d').css("cursor","pointer").find('span').css("cursor","pointer");
									}
									else {
										restDiv.html(tipsMsg.noInfo);
										potHidden.val('');
										bindBtn.removeClass('btn_d').addClass('btn_e').css("cursor","not-allowed").find('span').css("cursor","not-allowed");
									}
								},
								complete : function(){
									_els.customerCode.removeAttr("disabled");
								}
							});
						}, 1000);
					});

					els.bindBtn.click(function() {
						subFlag++;
					});
					// 重新绑定聚焦事件
					ytoCommon.setStatus();
				},
				
				// “绑定成功”弹层
				succDialog: function() {
					var oDialog = new Dialog();
					oDialog.init({
						contentHtml: step.succContent,
						closeBtn: true,
						iconType: 'success',
						autoClose: 3000
					});
				},
				
				// 开始使用
				startUsing: function() {
					// 注入开始按钮
					//$('#content_hd').append('<a href="javascript:;" id="act_start_btn" class="btn btn_a" title="开始使用"><span>开始使用</span></a>');
					$('#guide_btn').append('<a href="javascript:;" id="act_start_btn" class="btn_type_blue" title="开始使用">开始使用</a>');
				},
				
				// 绑定网点
				bindCode: function() {
					$('#act_start_btn').remove();
					//$('#content_hd').append('<a href="javascript:;" id="act_bind_btn" class="btn btn_a" title="绑定网点"><span>绑定网点</span></a>');
					$('#guide_btn').append('<a href="javascript:;" id="act_bind_btn" class="btn_type_blue" title="绑定网点">绑定网点</a>');
				},
				
				// 监听按钮事件
				btnClick: function() {
					// 点击“开始使用”
					$('#act_start_btn').live('click', function(ev) {
						ev.preventDefault();
						
						step.startDialog();
					});
					
					// 点击“绑定网点”
					$('#act_bind_btn').live('click', function(ev) {
						ev.preventDefault();
						
						step.bindDialog();
					});
				}
		};
		

		//未绑定ICON
		$("#vip_icon").click(function(ev){
			ev.preventDefault();
			if(config.onStep == 1){
				step.startDialog();
			}
			else if(config.onStep == 2){
				step.bindDialog();
			}
		});
		
		
		if (config.onStep == 1) {
			if(config.isShowButton){
				step.startUsing();//显示“开始使用”按钮
			}
			step.startDialog();//弹出卖家“基本信息”填写的dialog层。
		} else if (config.onStep == 2) {
			if(config.isShowButton){
				step.bindCode();//显示“绑定网点”按钮
			}
			if(config.showBindCode){
				step.bindDialog();//弹出卖家“绑定网点”的dialog层。
			}
		}
		else{
			//显示推荐功能。
		}
		
		step.btnClick();
	})();
});
