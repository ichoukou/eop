<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/common/meta.jsp" %>
<meta charset="UTF-8" />
<!-- S 当前页面 CSS -->
<link rel="stylesheet" type="text/css" href="${cssPath}/page/active_branch.css?d=${str:getVersion() }" media="all" />
<!-- E 当前页面 CSS -->
<title>网点激活</title>
	<!-- S Content -->
	<div id="content">
		<div id="content_hd" class="clearfix">
			<h2 id="message_icon">您的信息不全 请完善信息！</h2>
		</div>
		<div id="content_bd" class="clearfix">
			<div class="box box_a">
				<div class="box_bd">
					<input type="hidden" id="response" value="${response }"/>
					<form action="noint2!completeSite.action" id="act_branch_form" class="form">
						<p>
							<label for="psw"><span class="req">*</span>登录密码：</label>
							<input type="password" id="psw" class="input_text" name="user.userPassword" value="${user.userPassword }" />
<%-- 							<s:fielderror> --%>
<%--         						<s:param>user.userPassword</s:param> --%>
<%--         					</s:fielderror> --%>
							<span id="pswTip"></span>
						</p>
						<p>
							<label for="repsw"><span class="req">*</span>确认密码：</label>
							<input type="password" id="repsw" class="input_text" name="rePassword" value="${user.userPassword }"/>
<%-- 							<s:fielderror> --%>
<%--         						<s:param>rePassword</s:param> --%>
<%--         					</s:fielderror> --%>
							<span id="repswTip"></span>
						</p>
						<p>
							<label for="mobile"><span class="req">*</span>手机号码：</label>
							<input type="text" id="mobile" class="input_text" name="user.mobilePhone" value="${user.mobilePhone }"/>
<%-- 							<s:fielderror> --%>
<%--         						<s:param>user.mobilePhone</s:param> --%>
<%--         					</s:fielderror> --%>
							<span id="mobileTip"></span>
						</p>
						<p>
							<label for="tel_part_1"><span class="req">*</span>固定电话：</label>
							<input type="text" class="input_text" id="tel_part_1" name="user.telAreaCode" value="${user.telAreaCode }"/> - <input type="text" class="input_text" id="tel_part_2" name="user.telCode" value="${user.telCode }"/> - <input type="text" class="input_text" id="tel_part_3" name="user.telExtCode" value="${user.telExtCode }"/>
<%-- 							<s:fielderror> --%>
<%--         						<s:param>user.telePhone</s:param> --%>
<%--         					</s:fielderror> --%>
							<span id="telTip"></span>
						</p>
						<p>
							<label for="email"><span class="req">*</span>邮箱：</label>
							<input type="text" id="email" class="input_text" name="user.mail" value="${user.mail }"/>
<%-- 							<s:fielderror> --%>
<%--         						<s:param>user.mail</s:param> --%>
<%--         					</s:fielderror> --%>
							<span id="emailTip"></span>
						</p>
						<p>
							<label for="province"><span class="req">*</span>公司地址：</label>
							<select id="province"></select>
<%-- 							<s:fielderror> --%>
<%-- 								<s:param>user.addressProvince</s:param> --%>
<%-- 							</s:fielderror> --%>
							<span id="area_tip"></span>
							<input type="hidden" id="addressProvince" value="${user.addressProvince}" name="user.addressProvince"/>
							<input type="hidden" id="addressCity" value="${user.addressCity }" name="user.addressCity"/>
							<input type="hidden" id="addressDistrict" value="${user.addressDistrict }" name="user.addressDistrict"/>
							<input type="hidden" value="${user.field001 }" name="user.field001" id="x_field"/>
						</p>
						<p>
							<textarea cols="51" rows="2" class="textarea_text" id="detail_address" name="user.addressStreet">${user.addressStreet }</textarea>
							<span id="detail_addressTip"></span>
						</p>
						<p>
							<a title="保存并同意" class="btn btn_a" id="save_submit_btn" href="javascript:;"><span>保存并同意</span></a>
						</p>
					</form>
					
					<!-- S 注册协议 -->
					<div class="box box_a" id="agreement">
						<div class="box_bd">
							<h3>易通平台用户注册协议</h3>
							<p>本协议是您与易通电子商务物流信息服务平台所有者-上海圆通新龙电子商务有限公司（以下简称为“圆通新龙”）之间就易通服务等相关事宜所订立的契约，本协议中易通电子商务物流信息服务平台简称“易通平台”，域名为ec.yto.net.cn。请您仔细阅读本注册协议，<strong>您点击“同意以下协议，提交”按钮后，本协议即构成对双方有约束力的法律文件。</strong></p>
							<ol>
								<li>
									<h4>一、协议内容及签署</h4>
									<ol>
										<li>1.本协议内容包括协议正文及所有圆通新龙已经发布的或将来可能发布的各类规则。所有规则为本协议不可分割的组成部分，与协议正文具有同等法律效力。除另行明确声明外，任何圆通新龙提供的服务均受本协议约束。</li>
										<li>2.易通平台的各项电子服务的所有权和运作权归圆通新龙所有。用户同意所有注册协议条款并完成注册程序，才能成为易通平台的正式用户。用户确认：本协议条款是处理双方权利义务的契约，始终有效，法律另有强制性规定或双方另有特别约定的，依其规定或约定。</li>
										<li>3.用户点击同意本协议的，即视为用户确认自己具有享受圆通新龙服务等相应的权利能力和行为能力，能够独立承担法律责任。<strong>只要您使用易通平台服务，则本协议即对您产生约束，届时您不应以未阅读本协议的内容或者未获得圆通新龙对您问询的解答等理由，主张本协议无效，或要求撤销本协议。</strong></li>
										<li>4.您承诺接受并遵守本协议的约定。如果您不同意本协议的约定，您应立即停止注册程序或停止使用易通平台服务。</li>
										<li>5.圆通新龙有权根据需要不时地制订、修改本协议及/或各类规则，并以网站公示的方式进行公告，不再单独通知您。变更后的协议和规则一经在网站公布后，立即自动生效。如您不同意相关变更，应当立即停止使用圆通新龙服务。您继续使用圆通新龙服务的，即表示您接受经修订的协议和规则。</li>
									</ol>
								</li>
								
								<li>
									<h4>二、会员注册</h4>
									<ol>
										<li>1.用户应自行诚信向易通平台提供注册资料，用户同意其提供的注册资料真实、准确、完整、合法有效，用户注册资料如有变动的，应及时更新其注册资料。如果用户提供的注册资料不合法、不真实、不准确、不详尽的，用户需承担因此引起的相应责任及后果，并且圆通新龙保留终止用户使用易通平台各项服务的权利。</li>
										<li>2.用户注册成功后，将产生用户名和密码等账户信息，您可以根据易通平台规定改变您的密码。用户应谨慎合理的保存、使用其用户名和密码。用户若发现任何非法使用用户账号或存在安全漏洞的情况，请立即通知圆通新龙。</li>
										<li>3.在您签署本协议，完成会员注册程序或实际使用易通平台时，圆通新龙会向您提供唯一编号的易通账户（以下简称账户）。您可以对账户设置会员名和密码，通过该会员名密码或与该会员名密码关联的其它用户名密码登陆易通平台。您设置的会员名不得侵犯或涉嫌侵犯他人合法权益。如您连续一年未使用您的会员名和密码登录易通平台，圆通新龙有权终止向您提供易通平台服务，注销您的账户。账户注销后，相应的会员名将开放给任意用户注册登记使用。</li>
										<li>4.用户不得将在易通平台注册获得的账户借给他人使用，否则用户应承担由此产生的全部责任，并与实际使用人承担连带责任。</li>
									</ol>
								</li>
								
								<li>
									<h4>三、服务</h4>
									<ol>
										<li>1.通过圆通新龙及相关公司提供的易通平台服务和其它服务，用户可以在易通平台上包括但不限于发布订单信息、查询订单信息、与物流服务点进行沟通、参加圆通新龙组织的活动以及使用其它信息服务及技术服务。</li>
										<li>2.用户同意，圆通新龙包括但不限于通过电子邮件、手机短信、电话等形式，向易通平台用户、收货人、物流服务点等发送信息的权利。</li>
										<li>3.您了解并同意，圆通新龙有权应政府部门（包括司法及行政部门）的要求，向其提供您在易通平台填写的注册信息等必要信息。如您涉嫌侵犯他人知识产权，则圆通新龙亦有权在初步判断涉嫌侵权行为存在的情况下，向权利人可提供您必要的身份信息。</li>
									</ol>
								</li>
								
								<li>
									<h4>四、易通平台服务使用规范</h4>
									<ol>
										<li>
											<p>1.在易通平台上使用服务过程中，您承诺遵守以下约定：</p>
											<ol>
												<li>(1) 在使用易通平台服务过程中实施的所有行为均遵守国家法律、法规等规范性文件及易通平台各项规则的规定和要求，不违背社会公共利益或公共道德，不损害他人的合法权益，不违反本协议及相关规则。您如果违反前述承诺，产生任何法律后果的，您应以自己的名义独立承担所有的法律责任，并确保圆通新龙免于因此产生任何损失。</li>
												<li>(2) 不对易通平台上的任何数据作商业性利用，包括但不限于在未经圆通新龙事先书面同意的情况下，以复制、传播等任何方式使用易通平台上展示的资料。</li>
												<li>(3) 不使用任何装置、软件或例行程序干预或试图干预易通平台的正常运作。</li>
												<li>(4) 您不得采取任何将导致不合理的庞大数据负载加诸易通平台网络设备的行动。</li>
											</ol>
										</li>
										
										<li>
											<p>2.您了解并同意：</p>
											<ol>
												<li>(1) 圆通新龙有权对您是否违反上述承诺做出单方认定，并根据单方认定结果适用规则予以处理或终止向您提供服务，且无须征得您的同意或提前通知予您。</li>
												<li>(2) 经国家行政或司法机关的生效法律文书确认您存在违法或侵权行为，或者圆通新龙根据自身的判断，认为您的行为涉嫌违反本协议和/或规则的条款或涉嫌违反法律法规的规定的，则圆通新龙有权在易通平台上公示您的该等涉嫌违法或违约行为及圆通新龙已对您采取的措施。</li>
												<li>(3) 对于您在易通平台上发布的涉嫌违法或涉嫌侵犯他人合法权利或违反本协议和/或规则的信息，圆通新龙有权不经通知您即予以删除，且按照规则的规定进行处罚。</li>
												<li>(4) 对于您在易通平台上实施的行为，包括您未在易通平台上实施但已经对易通平台及其用户产生影响的行为，圆通新龙有权单方认定您行为的性质及是否构成对本协议和/或规则的违反，并据此做出相应处罚。您应自行保存与您行为有关的全部证据，并应对无法提供充要证据而承担的不利后果。</li>
												<li>(5) 对于您涉嫌违反承诺的行为对任意第三方造成损害的，您均应当以自己的名义独立承担所有的法律责任，并应确保圆通新龙免于因此产生损失或增加费用。</li>
												<li>(6) 如您涉嫌违反有关法律或者本协议之规定，使圆通新龙遭受任何损失，或受到任何第三方的索赔，或受到任何行政管理部门的处罚，您应当赔偿圆通新龙因此造成的损失及/或发生的费用，包括合理的律师费用。</li>
											</ol>
										</li>
									</ol>
								</li>
								
								<li>
									<h4>五、所有权及知识产权条款</h4>
									<ol>
										<li>1.用户一旦接受本协议，即表明该用户主动将其在任何时间段在本站发表的任何形式的信息内容的财产性权利等任何可转让的权利，如著作权财产权全部独家且不可撤销地转让给圆通新龙所有，用户同意易通平台有权就任何主体侵权而单独提起诉讼。</li>
										<li>2.本协议已经构成《中华人民共和国著作权法》第二十五条（条文序号依照2011年版著作权法确定）及相关法律规定的著作财产权等权利转让书面协议，其效力及于用户在易通平台上发布的任何受著作权法保护的作品内容，无论该等内容形成于本协议订立前还是本协议订立后。</li>
										<li>3.圆通新龙是本平台的制作者,拥有此平台内容及资源的著作权等合法权利,受国家法律保护,有权不时地对本协议及本平台的内容进行修改，并在平台张贴，无须另行通知用户。在法律允许的最大限度范围内，圆通新龙对本协议及平台内容拥有解释权。</li>
										<li>4.除法律另有强制性规定外，未经圆通新龙明确的特别书面许可,任何单位或个人不得以任何方式非法地全部或部分复制、转载、引用、链接、抓取或以其他方式使用本站的信息内容，否则，圆通新龙有权追究其法律责任。</li>
										<li>5.本站所刊登的资料信息（诸如文字、图表、标识、按钮图标、图像、声音文件片段、数字下载、数据编辑和软件），均是易通平台或其内容提供者的财产，受中国和国际版权法的保护。本站上所有内容的汇编是易通平台的排他财产，受中国和国际版权法的保护。本站上所有软件都是易通平台或其关联公司或其软件供应商的财产，受中国和国际版权法的保护。</li>
									</ol>
								</li>
								
								<li>
									<h4>六、特别授权</h4>
									<p>您完全理解并不可撤销地授予圆通新龙下列权利：</p>
									<ol>
										<li>1.一旦您向圆通新龙做出任何形式的承诺，且已确认您违反了该承诺，则圆通新龙有权立即按您的承诺或协议约定的方式对您的账户采取限制措施，包括中止或终止向您提供服务，并公示相关公司确认的您的违约情况。您了解并同意，圆通新龙无须就相关确认与您核对事实，或另行征得您的同意，且圆通新龙无须就此限制措施或公示行为向您承担任何的责任。</li>
										<li>2.对于您提供的资料及数据信息，您授予圆通新龙独家的、全球通用的、永久的、免费的许可使用权利 (并有权在多个层面对该权利进行再授权)。此外，圆通新龙及其关联公司有权全部或部份地使用、复制、修订、改写、发布、翻译、分发、执行和展示您提供的资料及数据信息或制作其派生作品，并以现在已知或日后开发的任何形式、媒体或技术，将上述信息纳入其它作品内。</li>
									</ol>
								</li>
								
								<li>
									<h4>七、责任范围和责任限制</h4>
									<ol>
										<li>1.易通平台所提供的所有资料、数据及信息，仅供参考使用。任何个人、公司及单位等依据易通平台提供的资料、数据及资料进行相关活动所造成的盈亏与易通平台无关。如发现信息有误或其它问题，请及时通过信息反馈与我们联系。</li>
										<li>2.由于用户恶意填写寄件信息等造成与易通平台对接的快递公司工作负担，或者导致用户与快递公司纠纷的，易通平台免责。</li>
										<li>3.由于用户使用易通平台上寄件系统时，错误的填写信息而导致包裹无法邮寄至收件人等一系列问题，易通平台免责。</li>
										<li>4.用户如点击易通平台的链接站点，便离开了易通平台页面。与易通平台链接的站点将不受易通平台控制，因此易通平台对所有链接网站的内容不予承担责任。</li>
										<li>5.除非另有明确的书面说明,易通平台及其所包含的或以其它方式通过圆通新龙提供给您的全部信息、内容、材料、产品（包括软件）和服务，均是在“按现状”和“按现有”的基础上提供的。</li>
										<li>6.除非另有明确的书面说明,圆通新龙不对易通平台的运营及其包含在易通平台上的信息、内容、材料、产品（包括软件）或服务作任何形式的、明示或默示的声明或担保（根据中华人民共和国法律另有规定的以外）。</li>
										<li>7.圆通新龙不担保易通平台所包含的或以其它方式通过易通平台提供给您的全部信息、内容、材料、产品（包括软件）、服务、服务器或从易通平台发出的电子信件、信息没有病毒或其他有害成分。</li>
										<li>8.任何由于黑客攻击、计算机病毒侵入或者发作、因政府管制而造成的暂时性关闭等影响网络正常经营的不可抗力而造成的个人资料泄露、丢失、被盗用或被篡改，圆通新龙会合理地尽力协助处理善后事宜，但不因此承担责任。</li>
										<li>9.因网络状况、通讯线路、第三方网站等任何原因而导致您不能正常使用易通平台，易通平台不承担任何法律责任。由于本平台连接的其它网站所造成的相关问题及由此导致的任何法律正义和后果，易通本平台当然免责。</li>
										<li>10.本平台如因网络维护或者升级而需暂停服务时，将事先发出公告。如果因网络线路以及本公司控制范围外的硬件故障或其它不可抗力而导致暂停服务，于暂停服务期间造成的一切不便与损失，本平台不负任何责任。</li>
									</ol>
								</li>
								
								<li>
									<h4>八、协议终止</h4>
									<ol>
										<li>1.您同意，圆通新龙有权自行全权决定以任何理由不经事先通知的中止、终止向您提供部分或全部易通平台服务，暂时冻结或永久冻结（注销）您的账户，且无须为此向您或任何第三方承担任何责任。</li>
										<li>
											2.出现以下情况时，圆通新龙有权直接以注销账户的方式终止本协议:
											<ol>
												<li>(1) 易通终止向您提供服务后，您涉嫌再一次直接或间接或以他人名义注册为易通用户的；</li>
												<li>(2) 您提供的电子邮箱不存在或无法接收电子邮件，且没有其他方式可以与您进行联系，或圆通新龙以其它联系方式通知您更改电子邮件信息，而您在圆通新龙通知后三个工作日内仍未更改为有效的电子邮箱的；</li>
												<li>(3) 您注册信息中的主要内容不真实或不准确或不及时或不完整；</li>
												<li>(4) 本协议（含规则）变更时，您明示并通知圆通新龙不愿接受新的服务协议的；</li>
												<li>(5) 其它圆通新龙认为应当终止服务的情况。</li>
											</ol>
										</li>
										<li>3.您有权向圆通新龙要求注销您的账户，经易通审核同意的，圆通新龙注销（永久冻结）您的账户，届时，您与圆通新龙基于本协议的合同关系即终止。您的账户被注销（永久冻结）后，圆通新龙没有义务为您保留或向您披露您账户中的任何信息，也没有义务向您或第三方转发任何您未曾阅读或发送过的信息。</li>
										<li>
											4.您同意，您与圆通新龙的协议关系终止后，圆通新龙仍享有下列权利
											<ol>
												<li>(1) 继续保存您的注册信息及您使用易通平台服务期间的所有交易信息。</li>
												<li>(2) 您在使用易通平台服务期间存在违法行为或违反本协议和/或规则的行为的，圆通新龙仍可依据本协议向您主张权利。</li>
											</ol>
										</li>
									</ol>
								</li>
								
								<li>
									<h4>九、隐私权保护</h4>
									<p>保护用户隐私是圆通新龙的一项基本政策，圆通新龙承诺不对外公开或向第三方提供单个用户的注册资料及用户在使用网络服务时存储在易通平台的非公开内容，但下列情况除外：</p>
									<ol>
										<li>1.事先获得用户的明确授权；</li>
										<li>2.根据有关的法律法规要求；</li>
										<li>3.按照相关政府主管部门的要求；</li>
										<li>4.为维护社会公众的利益；</li>
										<li>5.为维护圆通新龙的合法权益所进行的适当公开。</li>
									</ol>
								</li>
								
								<li>
									<h4>十、法律适用、管辖与其他</h4>
									<ol>
										<li>1.本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，如无相关法律规定的，则应参照通用国际商业惯例和（或）行业惯例。</li>
										<li>2.因本协议产生之争议，应依照中华人民共和国法律予以处理，并以圆通新龙住所所在地人民法院为第一审管辖法院。</li>
									</ol>
								</li>
							</ol>
						</div>
					</div>
					<!-- E 注册协议 -->
				</div>
			</div>
		</div>
	</div>
	<!-- E Content -->
<script type="text/javascript">
var params = {
	response : $("#response").val(),
	userType:2
}
</script>
<script type="text/javascript" src="${jsPath}/common/common.js?d=${str:getVersion() }"></script>
<script type="text/javascript" src="${jsPath}/module/select/jquery.linkagesel-min.js?d=${str:getVersion() }"></script>
<!-- S 当前页面 JS -->
<script type="text/javascript" src="${jsPath}/page/active_branch.js?d=${str:getVersion() }"></script>
<!-- E 当前页面 JS -->