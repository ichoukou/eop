package net.ytoec.kernel.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.dao.SMSServiceDao;
import net.ytoec.kernel.dao.UserDao;
import net.ytoec.kernel.dataobject.SMSService;
import net.ytoec.kernel.dataobject.SMSTemplate;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.SMSServiceService;
import net.ytoec.kernel.service.SMSTemplateService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@SuppressWarnings("all")
public class SMSServiceServiceImpl implements SMSServiceService {

	private static final Logger logger = LoggerFactory.getLogger(SMSServiceServiceImpl.class);
	
	@Inject
	private SMSServiceDao sMSServicedao;
	@Inject
	private UserDao userDao;
	@Inject
	private SMSTemplateService smsTemplateService;
	
	@Override
	public List<SMSService> getSMSServiceAll(Integer userId) {
		return sMSServicedao.getSMSServiceAll(userId);
	}

	@Override
	public SMSService getSMSServiceByServiceName(Map<String,Object> params) {
		return sMSServicedao.getSMSServiceByServiceName(params);
	}
	
	@Override
	public boolean addSMSService(SMSService service) {
		boolean flag = false;
		if(service == null){
			logger.error("##添加短信子服务时传入参数为空！");
			return false;
		}
		return sMSServicedao.addSMSService(service);
	}

	@Override
	public boolean delSMSServiceById(Integer id) {
		if("".equals(id) || id == null){
			logger.error("##删除短信子服务时传入参数为空！");
			return false;
		}
		return sMSServicedao.delSMSServiceById(id);
	}

	@Override
	public boolean editSMSService(SMSService service) {
		if(service == null){
			logger.error("##修改短信子服务时传入参数为空！");
			return false;
		}
		return sMSServicedao.editSMSService(service);
	}

	@Override
	public SMSService getSMSServiceById(Integer id) throws Exception {
		if(id == null){
			throw new Exception("##根据ID查询短信子服务时传入参数为空！");
		}
		return (SMSService) sMSServicedao.getSMSServiceById(id);
	}

	@Override
	public List<SMSService> getSMSServicesByUserId(Integer userId) throws Exception {
		
		if(userId == null || "".equals(userId)){
			throw new Exception("##根据主账号ID下已开启的短信服务传参为空");
		}
					
		return sMSServicedao.getSMSServicesByUserId(userId);
	}
	
	@Override
	public boolean saveServiceByIsOn(Integer userId, Integer subUserId) {
		
		// 只有主帐号才会执行此方法
		// 开启短信服务时插入子服务和系统模版
		try {
			// 发货通知
			SMSService smsService = new SMSService();
			smsService.setUserId(subUserId);
			smsService.setCreateUserId(subUserId);
			smsService.setIsAutoSend("0");
			smsService.setSendTimeType("1");
			smsService.setIsOn("N");
			smsService.setImageUrl(".../../images/single/serve_icon_a.png");
			smsService.setName("发货提醒");
			smsService.setIntroduction("买家催发货，客服工作压力大，及时发货提醒告知客户已发货，提升店铺服务质量。");
			sMSServicedao.addSMSService(smsService);
			
			// 到货提醒
			SMSService smsService1 = new SMSService();
			smsService1.setUserId(subUserId);
			smsService1.setCreateUserId(subUserId);
			smsService1.setIsAutoSend("0");
			smsService1.setSendTimeType("1");
			smsService1.setIsOn("N");
			smsService1.setImageUrl(".../../images/single/serve_icon_d.png");
			smsService1.setName("派件提醒");
			smsService1.setIntroduction("派件无人签收，及时的派件提醒功能，让买家准备签收，确保派件正常签收。");
			sMSServicedao.addSMSService(smsService1);
			
			// 签收提醒
			SMSService smsService2 = new SMSService();
			smsService2.setUserId(subUserId);
			smsService2.setCreateUserId(subUserId);
			smsService2.setIsAutoSend("0");
			smsService2.setSendTimeType("1");
			smsService2.setIsOn("N");
			smsService2.setImageUrl(".../../images/single/serve_icon_b.png");
			smsService2.setName("签收提醒");
			smsService2.setIntroduction("宝贝已签收，提示买家确认收货，买家及时确认收货，提升店铺汇款速度。");
			sMSServicedao.addSMSService(smsService2);
			
			// 营销定制
		/*	SMSService smsService3 = new SMSService();
			smsService3.setUserId(subUserId);
			smsService3.setCreateUserId(subUserId);
			smsService3.setIsAutoSend("0");
			smsService3.setSendTimeType("1");
			smsService3.setIsOn("N");
			smsService3.setImageUrl(".../../images/single/serve_icon_e.png");
			smsService3.setName("营销定制");
			smsService3.setIntroduction("主动告知解决爆仓、天气延误等问题，简单温馨的短信提示，减少差评，将损失降至最低。");
			sMSServicedao.addSMSService(smsService3);
			
			// 问题件通知
			SMSService smsService4 = new SMSService();
			smsService4.setUserId(subUserId);
			smsService4.setCreateUserId(subUserId);
			smsService4.setIsAutoSend("0");
			smsService4.setSendTimeType("1");
			smsService4.setIsOn("Y");
			smsService4.setImageUrl(".../../images/single/serve_icon_c.png");
			smsService4.setName("问题件通知");
			smsService4.setIntroduction("精准的短信营销，广而告之，为你的店铺快速聚引大量的优质流量。");
			sMSServicedao.addSMSService(smsService4);*/
			
			// 平台、分仓、 入驻企业公用一套模版
			// 判断是否已经有模版，如果有则不在创建
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", userId);
			List<SMSTemplate> templateList = smsTemplateService.getSMSTemplateByUserId(params, null);
			if (templateList == null || templateList.size() == 0) {
				// 插入系统提供的默认模版
				SMSService tempService = null;
				List<SMSService> tempList = sMSServicedao.getSMSServiceAll(subUserId);
				for(int t = 0; t < tempList.size(); t++) {
					tempService = tempList.get(t);
					if (StringUtils.equals("发货提醒", tempService.getName())) {
						
						SMSTemplate smsTemplate1 = new SMSTemplate();
						
						smsTemplate1.setSmsTypeId(tempService.getId());
						smsTemplate1.setContent("亲，您的宝贝已于[发货时间]由[快递公司]发货,运单号为[运单号],请注意查收[店铺名称]！");
						smsTemplate1.setIsDefault("N");
						smsTemplate1.setStatus("S");
						smsTemplate1.setName("发货提醒系统模版一");
						smsTemplate1.setSendCount(0);
						smsTemplate1.setUserId(userId);
						smsTemplate1.setCreateUserId(subUserId);
						smsTemplateService.addSMSTemplate(smsTemplate1);
						
						SMSTemplate smsTemplate2 = new SMSTemplate();
						
						smsTemplate2.setSmsTypeId(tempService.getId());
						smsTemplate2.setIsDefault("N");
						smsTemplate2.setStatus("S");
						smsTemplate2.setSendCount(0);
						smsTemplate2.setUserId(userId);
						smsTemplate2.setCreateUserId(subUserId);
						smsTemplate2.setContent("亲，您的订单已发货,物流公司[快递公司],运单号[运单号]期待您给我们全五分[店铺名称]！");
						smsTemplate2.setName("发货提醒系统模版二");
						smsTemplateService.addSMSTemplate(smsTemplate2);
						
					} else if (StringUtils.equals("派件提醒", tempService.getName())) {
						
						SMSTemplate smsTemplate1 = new SMSTemplate();
						
						smsTemplate1.setSmsTypeId(tempService.getId());
						smsTemplate1.setContent("亲，您的宝贝已到达目的地。请保持手机畅通，注意查收！五分好评截图给客服，可返现哦，记得联系客服哦！");
						smsTemplate1.setIsDefault("N");
						smsTemplate1.setStatus("S");
						smsTemplate1.setName("派件提醒系统模版一");
						smsTemplate1.setSendCount(0);
						smsTemplate1.setUserId(userId);
						smsTemplate1.setCreateUserId(subUserId);
						smsTemplateService.addSMSTemplate(smsTemplate1);
						
						SMSTemplate smsTemplate2 = new SMSTemplate();
						
						smsTemplate2.setSmsTypeId(tempService.getId());
						smsTemplate2.setIsDefault("N");
						smsTemplate2.setStatus("S");
						smsTemplate2.setSendCount(0);
						smsTemplate2.setUserId(userId);
						smsTemplate2.setCreateUserId(subUserId);
						smsTemplate2.setContent("亲，您的宝贝已抵达目的地，派件员要派送了，请注意查收，有问题联系客服，如满意请给好评，感谢您对[店铺名称]的支持！");
						smsTemplate2.setName("派件提醒系统模版二");
						smsTemplateService.addSMSTemplate(smsTemplate2);
						
					} else if (StringUtils.equals("签收提醒", tempService.getName())) {
					
						SMSTemplate smsTemplate1 = new SMSTemplate();
						
						smsTemplate1.setSmsTypeId(tempService.getId());
						smsTemplate1.setContent("亲，您购买的宝贝已经签收啦！如果对宝贝满意，请确认好评打5分哦，期待您的下次光临~");
						smsTemplate1.setIsDefault("N");
						smsTemplate1.setStatus("S");
						smsTemplate1.setName("签收提醒系统模版一");
						smsTemplate1.setSendCount(0);
						smsTemplate1.setUserId(userId);
						smsTemplate1.setCreateUserId(subUserId);
						smsTemplateService.addSMSTemplate(smsTemplate1);
						
						SMSTemplate smsTemplate2 = new SMSTemplate();
						
						smsTemplate2.setSmsTypeId(tempService.getId());
						smsTemplate2.setIsDefault("N");
						smsTemplate2.setStatus("S");
						smsTemplate2.setSendCount(0);
						smsTemplate2.setUserId(userId);
						smsTemplate2.setCreateUserId(subUserId);
						smsTemplate2.setContent("亲，感谢您在[店铺名称]购买宝贝，签收后请仔细检查宝贝，如满意请给5分好评，有问题随时联系我们！");
						smsTemplate2.setName("签收提醒系统模版二");
						smsTemplateService.addSMSTemplate(smsTemplate2);
					} 
					/*else if (StringUtils.equals("营销定制", tempService.getName())) {
						SMSTemplate smsTemplate1 = new SMSTemplate();
						
						smsTemplate1.setSmsTypeId(tempService.getId());
						smsTemplate1.setContent("店庆月自营服装爆款低至2折再满199减50 ，399减120，千款清仓海淘9.9元起！[店铺名称]！");
						smsTemplate1.setIsDefault("N");
						smsTemplate1.setStatus("S");
						smsTemplate1.setName("营销定制系统模版一");
						smsTemplate1.setSendCount(0);
						smsTemplate1.setUserId(userId);
						smsTemplate1.setCreateUserId(subUserId);
						smsTemplateService.addSMSTemplate(smsTemplate1);
						
						SMSTemplate smsTemplate2 = new SMSTemplate();
						
						smsTemplate2.setSmsTypeId(tempService.getId());
						smsTemplate2.setIsDefault("N");
						smsTemplate2.setStatus("S");
						smsTemplate2.setSendCount(0);
						smsTemplate2.setUserId(userId);
						smsTemplate2.setCreateUserId(subUserId);
						smsTemplate2.setContent("亲，本店周年庆，全场满300减100，全国包邮，购物抽奖更有机会赢大礼，详情请[店铺名称]，老客户还有神秘礼物相送哦~");
						smsTemplate2.setName("营销定制系统模版二");
						smsTemplateService.addSMSTemplate(smsTemplate2);
					} */
				}
			}
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public List<SMSService> getSMSService(HashMap<String, Object> param,boolean bool) {
		Integer userId = (Integer) param.get("userId");
		String flag = (String) param.get("flag");
		logger.info("获取所有服务参数="+param);
		
		List<SMSService> servicesList = new ArrayList<SMSService>();
		
		//N：非分仓账号 - Y:分仓账号
		if("N".equals(flag)) servicesList = sMSServicedao.getSMSServiceAll(userId);
		if("Y".equals(flag)){
			servicesList = getSMSServiceForFengCang(param,bool);
		}
		return servicesList;
	}
	
	@Override
	public List<SMSService> getSMSServiceForFengCang(HashMap<String, Object> param,boolean bool) {
		User owner = (User) param.get("user");
		Integer userId = (Integer) param.get("userId");
		//获取分仓账号开启的服务
		List<SMSService> servicesList = sMSServicedao.getSMSServiceAll(owner.getId());
		
		//获取平台账号开启的服务
		List<SMSService> pingServices = new ArrayList<SMSService>();
		if(bool)
			pingServices = sMSServicedao.getSMSServiceAll(userId);
		else{
			if(servicesList.size() < 3){
				String[] names = new String[]{"发货提醒","派件提醒","签收提醒"};
				for(int i = 0 ; i < names.length ; i++){
					boolean flag = true;
					for(int j = 0 ; j < servicesList.size() ; j++){
						if(names[i].equals(servicesList.get(j).getName())){
							flag = false;
							break;
						}
					}
					if(flag){
						Map<String,Object> params = new HashMap<String, Object>();
						params.put("name",names[i]);
						params.put("userId", userId);
						SMSService smsService = getSMSServiceByServiceName(params);
						
						SMSService service = new SMSService();
						service.setName(names[i]);
						service.setImageUrl(smsService.getImageUrl());
						service.setIntroduction(smsService.getIntroduction());
						service.setSendTimeType(smsService.getSendTimeType());
						service.setIsAutoSend("0");
						service.setUserId(owner.getId());
						service.setCreateTime(new Date());
						service.setCreateUserId(owner.getId());
						service.setIsOn("N");

						logger.info("分仓独立开通服务不够时，需插入服务数据");
						boolean b = addSMSService(service);
						logger.info("添加成功与否："+b);
					}
				}
				servicesList = sMSServicedao.getSMSServiceAll(owner.getId());
			}
		}
		
		if(servicesList == null || servicesList.size() <= 0){
			servicesList = pingServices;
		}else{
			if(bool){
				List<SMSService> returnServiceList = new ArrayList<SMSService>();
				for(int i = 0 ; i < pingServices.size() ; i++){
					/*显示列表：
					 * 1-分仓已有该服务：需判断该服务是否开启？
					 * 			a、如果已开启则取本身；
					 * 			b、如果未开启并且平台也未开启则取本身；如果平台开启了取平台服务
					 * 2-分仓无该服务：直接取平台服务*/
					boolean flag = true;
					for(SMSService own : servicesList){
						if(pingServices.get(i).getName().trim().equals(own.getName().trim())){
							if("Y".equals(own.getIsOn())){
								returnServiceList.add(own);
							}else{
								if("Y".equals(pingServices.get(i).getIsOn())) 
									returnServiceList.add(pingServices.get(i));
								else returnServiceList.add(own);
							}
							flag = false;
							break;
						}
					}
					if(flag) returnServiceList.add(pingServices.get(i));
				}
				return returnServiceList;
			}
		}
		return servicesList;
	}
	
	@Override
	public boolean isOpeanService(Integer userId, String serivceType) { 
		
        logger.error("isOpeanService:userId:" + userId + " serivceType:"+serivceType);
		//查询参数集合
		Map<String,Object> map = new HashMap<String,Object>();
		String name = "其他类型暂不查询";
		if("GOT".equals(serivceType))name="发货提醒";
		if("SENT_SCAN".equals(serivceType))name="派件提醒";
		if("SIGNED".equals(serivceType))name="签收提醒";
		map.put("name", name);
		//查询ID   
		Integer theUserId = null;   //主账户ID
		Integer ptUserId = null;    //平台ID
		//根据用户ID,查询用户
		User user = (User)userDao.getUserById(userId); 
		if(user!=null) {
			Integer parentId = user.getParentId();
			if(parentId!=null) {    //主ID,或子ID
				theUserId = Integer.valueOf(parentId);
			}else {
				theUserId = user.getId();
			}
			//判断是否为为分仓
			if("B".equals(user.getChildType())) {  //B:分仓  使用平台ID 
				String thisUserId = user.getUserSource(); 
				if(StringUtils.isNotBlank(thisUserId)) {
					ptUserId = Integer.parseInt(thisUserId);   
				}
			}
			//主账户查询
			map.put("userId", theUserId);
			//查询服务
			SMSService service = null;
			try {
				service = getSMSServiceByServiceName(map);
			} catch (Exception e) {
				logger.error("出错的userId为:"+theUserId);
				e.printStackTrace();
			}
			
			//服务存在,且状态为Y,表示开通
			if(service!=null) {
				if("Y".equals(service.getIsOn())) { //有服务且开启,为开通
					return true;
				}
			}else {  //无服务,判读是否有平台ID,若平台ID存在,证明该用户为分仓用户,分仓可使用平台ID
				if(ptUserId!=null) {
					//平台账户查询
					map.put("userId", ptUserId);
					service = getSMSServiceByServiceName(map);
					if(service!=null&&"Y".equals(service.getIsOn())) {  //存在,且开启,表示开启
						return true;
					}
				}
			}
		}	
		logger.error("FALSE isOpeanService:userId:" + userId + " serivceType:"+serivceType);
		return false;
	}
	
	/**
	 * hufei
	 * 是否开启服务,根据 user,服务类型
	 * @param map
	 * @param user
	 * @return
	 */
	@Override
    public boolean isServiceOpean(Map map,User user) { 
        //logger.error("isOpeanService:userId:" + userId + " serivceType:"+serivceType);
		
		//查询ID   
		Integer theUserId = null;   //主账户ID
		Integer ptUserId = null;    //平台ID
		if(user!=null) {
			Integer parentId = user.getParentId();
			if(parentId!=null) {    //主ID,或子ID
				theUserId = Integer.valueOf(parentId);
			}else {
				theUserId = user.getId();
			}
			
			//判断是否为为分仓
			if("B".equals(user.getChildType())) {  //B:分仓  使用平台ID 
				String thisUserId = user.getUserSource(); 
				if(StringUtils.isNotBlank(thisUserId)) {
					ptUserId = Integer.parseInt(thisUserId);   
				}
			}
			//主账户查询
			map.put("userId", theUserId);
			//查询服务
			SMSService service = getSMSServiceByServiceName(map);
			//服务存在,且状态为Y,表示开通
			if(service!=null) {
				if("Y".equals(service.getIsOn())) { //有服务且开启,为开通
					return true;
				}
			}else {  //无服务,判读是否有平台ID,若平台ID存在,证明该用户为分仓用户,分仓可使用平台ID
				if(ptUserId!=null) {
					//平台账户查询
					map.put("userId", ptUserId);
					service = getSMSServiceByServiceName(map);
					if(service!=null&&"Y".equals(service.getIsOn())) {  //存在,且开启,表示开启
						return true;
					}
				}
			}
		}	
		//logger.error("FALSE isOpeanService:userId:" + userId + " serivceType:"+serivceType);
		return false;
	}
	@Override
	public SMSService getOpeanService(Integer userId, String serivceType) { 
		
		//查询参数集合
		Map<String,Object> map = new HashMap<String,Object>();
		String name = "其他类型暂不查询";
		if("GOT".equals(serivceType))name="发货提醒";
		if("SENT_SCAN".equals(serivceType))name="派件提醒";
		if("SIGNED".equals(serivceType))name="签收提醒";
		map.put("name", name);
		
		//查询ID   
		Integer theUserId = null;   //主账户ID
		Integer ptUserId = null;    //平台ID
		
		//根据用户ID,查询用户
		User user = (User)userDao.getUserById(userId); 
		if(user!=null) {
			Integer parentId = user.getParentId();
			if(parentId!=null) {    //主ID,或子ID
				theUserId = Integer.valueOf(parentId);
			}else {
				theUserId = user.getId();
			}
			
			//判断是否为为分仓
			if("B".equals(user.getChildType())) {  //B:分仓  使用平台ID 
				String thisUserId = user.getUserSource(); 
				if(StringUtils.isNotBlank(thisUserId)) {
					ptUserId = Integer.parseInt(thisUserId);   
				}
			}
			
			//主账户查询
			map.put("userId", theUserId);
			
			//查询服务
			SMSService service = getSMSServiceByServiceName(map);
			//服务存在,且状态为Y,表示开通
			if(service!=null) {
				if("Y".equals(service.getIsOn())) { //有服务且开启,为开通
					return service;
				}
			}else {  //无服务,判读是否有平台ID,若平台ID存在,证明该用户为分仓用户,分仓可使用平台ID
				if(ptUserId!=null) {
					//平台账户查询
					map.put("userId", ptUserId);
					service = getSMSServiceByServiceName(map);
					if(service!=null&&"Y".equals(service.getIsOn())) {  //存在,且开启,表示开启
						return service;
					}
				}
			}
			
		}	
		return null;
	}
	
	@Override
	public boolean updateServiceForFenCang(SMSService service) {
		if(service == null){
			logger.error("##分仓开启服务时传参为空");
			return false;
		}
		boolean b = false;
		try {
			b = sMSServicedao.updateServiceForFenCang(service);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return b;
	}

	@Override
	public SMSService getSMSServiceByServiceNameFenCang(Map<String, Object> params) {
		return sMSServicedao.getSMSServiceByServiceNameFenCang(params);
	}
	
	@Override
	public List<SMSService> getSMSServiceByServiceNameFenCangList(Map<String, Object> params) {
		return sMSServicedao.getSMSServiceByServiceNameFenCangList(params);
	}

	@Override
	public List<SMSService> getSMSServiceAllForToAdd(Integer userId) {
		return sMSServicedao.getSMSServiceAllForToAdd(userId);
	}
}
