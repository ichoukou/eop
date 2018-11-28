package net.ytoec.kernel.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.remote.TaoBaoOrderAction;
import net.ytoec.kernel.action.remote.xml.PassMessageQueryOrder;
import net.ytoec.kernel.action.remote.xml.QueryOrder;
import net.ytoec.kernel.action.remote.xml.StepInfo;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dao.PassManageDao;
import net.ytoec.kernel.dataobject.AttentionMail;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.ReportIssue;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.dataobject.WarnUpOper;
import net.ytoec.kernel.dataobject.WarnValue;
import net.ytoec.kernel.dto.PassIssueDTO;
import net.ytoec.kernel.search.dto.EccoreSearchResultDTO;
import net.ytoec.kernel.search.service.EccoreSearchService;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.PassManageService;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.ConfigUtilSingle;
import net.ytoec.kernel.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PassManageServiceImpl implements PassManageService {

	private static Logger logger = LoggerFactory.getLogger(PassManageServiceImpl.class);
	
	@Inject
	private PassManageDao dao;
	
	@Inject
	private EccoreSearchService eccoreSearchService;
	
	@Inject
	private QuestionnaireService questionnaireService;
	
	@Inject
	private OrderService orderService;
	
	@Inject
	private UserService userService;
	
	@Override
	public boolean addReportIssue(ReportIssue ri) {
		
		boolean b = false;
		if(ri == null){
			logger.error("##添加上报传参为空");
			return b;
		}	
		try{
			this.dao.addReportIssue(ri);
			b = true;
		}catch(Exception e){
			logger.error("##添加上报信息记录异常");
			b = false;
		}
		return b;
	}

	@Override
	public boolean addAttentionMail(AttentionMail att) {
		boolean b = false;
		if(att == null){
			logger.error("##添加我的关注传参为空");
			return b;
		}	
		try{
			this.dao.addAttentionMail(att);
			b = true;
		}catch(Exception e){
			logger.error("##添加我的关注记录异常");
			b = false;
		}
		return b;
	}

	@Override
	public List<ReportIssue> searchReportIssueList(Map<String, Object> params,
			Pagination pagination, boolean flag) {
		if(params == null){
			logger.info("##获取[卖家]已上报、[网点]未/已回复超时件列表传参为空");
	    	params = new HashMap<String, Object>();
	    }
        if(flag){
        	params.put("startIndex", pagination.getStartIndex());
        	params.put("pageNum", pagination.getPageNum());
        }
        
		return this.dao.searchReportIssueList(params);
	}

	@Override
	public List<WarnUpOper> getOperByIssueId(Map<String, Object> params) {
		if(params == null){
			logger.error("##查询关联处理信息时传入参数为空");
			return null;
		}
		
		return this.dao.getOperByIssueId(params);
	}

	@Override
	public boolean sendToSite(WarnUpOper oper) {
		if(oper == null){
			logger.error("##发送信息时传入参数为空");
			return false;
		}
		boolean b = false;
		try{
			this.dao.sendToSite(oper);
			b = true;
		}catch(Exception e){
			logger.error("##发送信息异常:"+e);
		}
		return b;
	}

	@Override
	public boolean updateReportIssue(ReportIssue report) {
		
		if(report == null){
			logger.error("##修改超时件状态[0]传入参数为空");
			return false;
		}
		
		return this.dao.editReportIssue(report);
	}

	@Override
	public boolean reportIssue(String queryCondition, HashMap<String,Object> params
			,UserService<User> userService) throws ParseException {
		/*0-buyerName,1-buyerPhone,2-status,3-destination,4-mailNo,5-siteCode,6-customerId,7-acceptTime,8-createTime,9-lineType,10-attentionFlag,11-issueDesc*/
		logger.info("========"+queryCondition);
		String []query = queryCondition.split("\\,");
		ReportIssue ri = new ReportIssue();
		ri.setAttentionFlag(query[10]);
		ri.setBuyerMobile(query[1]);
		ri.setBuyerName(query[0]);
		//ri.setBuyerPhone(query[1]);
		ri.setIssueDesc(query[11]);
		ri.setMailNo(query[4]);
		ri.setReceiveBranchId(query[5]);
		//ri.setCreateTime(new Date());
		ri.setMsgStatus("0");
		String flag = (String) params.get("isMain");
		User curUser = (User) params.get("curUser");
		if("Y".equals(flag)){
			ri.setReportUserId(curUser.getUserName());
			ri.setCreateUserId(curUser.getId());
		}
		if("N".equals(flag)){
			User mainUser = (User) params.get("mianUser");
			ri.setReportUserId(mainUser.getUserName());
			ri.setCreateUserId(curUser.getId());
		}
		
		
		boolean b = this.addReportIssue(ri);
		boolean resutl = true;
		if(b && "1".equals(query[10])){
			AttentionMail att = new AttentionMail();
			att.setMailNo(query[4]);
			att.setDestination(query[3]);
			att.setBuyerName(query[0]);
			att.setBuyerPhone(query[1]);
			att.setStatus(query[2]);
			/*if(currentUser.getParentId() == null || "".equals(currentUser.getParentId()))//主账号
				att.setCustomerId(currentUser.getTaobaoEncodeKey());
			else{//子账号
				User user = userService.getUserById(currentUser.getParentId());
				att.setCustomerId(user.getTaobaoEncodeKey());
			}*/
			if("Y".equals(flag)){
				att.setCustomerId(curUser.getTaobaoEncodeKey());
			}
			if("N".equals(flag)){
				User mainUser = (User) params.get("mianUser");
				att.setCustomerId(mainUser.getTaobaoEncodeKey());
			}
				
			String sendTime = query[7].substring(0,query[7].indexOf("."));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhh:mm:ss");
			try {
				att.setSendTime(sdf.parse(query[8].replace(" ", "")));
				att.setAcceptTime(sdf.parse(sendTime));
			} catch (ParseException e) {
				throw new ParseException("##时间转换异常."+e, 0);
			}
			att.setCreateTime(new Date());
			att.setDateOut(2);
			att.setLineType(query[9]);
			
			boolean  b1 = this.addAttentionMail(att);
			if(b1)resutl = true;
			else resutl = false;
		}
		
		return resutl;
	}

	
	@Override
	public boolean sendMsg(WarnUpOper oper,String flag) {
		
		boolean b = this.sendToSite(oper);
		
		ReportIssue report = new ReportIssue();
		
		logger.info("信息发送成功与否："+b);
		
		boolean bool = true;
		if(b){
			//页面传过来的标示：1-卖家->网点；2-网点->卖家
			String msgStatus = "";
			String forward = "";
			if("1".equals(flag)){
				report.setMsgStatus("0");
				logger.info("##卖家  -> 网点");
			}
			if("2".equals(flag)){
				report.setMsgStatus("1");
				logger.info("##网点 -> 卖家");
			}
				
			report.setUpdateTime(new Date());
			report.setId(oper.getIssueId());
			
			boolean bo = this.updateReportIssue(report);
			
			if(bo)bool = true;
			else bool = false;
			
			logger.info("发送信息成功后修改超时件信息表状态boolean="+bo);
		}
			
		return bool;
	}
	
	@Override
	public List<PassIssueDTO> searPassIssue(String solrEccoreUrl,
			Pagination<EccoreSearchResultDTO> pagination1,List<User> shopNames,Integer userId,
			List<WarnValue> warnValueList,List<String>  bindString) throws Exception {
		try {
			if((pagination1.getParams().get("overTimeMap")) != null && !"".equals((pagination1.getParams().get("overTimeMap")))) {
				eccoreSearchService.searchOverTimeData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), pagination1);
			} else {
				eccoreSearchService.searchWayBillData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(), pagination1);
			}
		} catch (Exception e) {
			logger.error("查找超时件错误", e);
		}
		logger.info("#query recards size=" + pagination1.getRecords().size());
		
		List<PassIssueDTO> passIssueDTOList = new ArrayList<PassIssueDTO>();
		PassIssueDTO pid = null;
		Order order = null;
		String prov = "";
		String prov1 = "";
		String city = "";
		String dist = "";
		String customerId = "";
		String time = "";
		String shopName = "";
		double mins = 0.0;
		double userDay = 0.0;
		double passTime = 0.0;
		Integer d = 0;
		Integer h = 0;
		String z = "";
		String x = "";
		StringBuffer sb = new StringBuffer();
		HashMap<String,PassIssueDTO> passIssueDTOMap =new HashMap<String,PassIssueDTO>();
		for(EccoreSearchResultDTO e : pagination1.getRecords()){
			sb.setLength(0);
		    pid = new PassIssueDTO();
			pid.setMailNo(e.getMailNo());
			pid.setName(e.getName());
			pid.setPhone(e.getPhone());
			prov = e.getProv() == null ? "" : e.getProv();
			prov1 = prov;
			if (prov.equals("北京") || prov.equals("上海") || prov.equals("天津") || prov.equals("重庆")) {
				prov = "";
			}
		    city = e.getCity() == null ? "" : e.getCity();
			dist = e.getDistrict() == null ? "" : e.getDistrict();
			pid.setAddress(sb.append(prov).append(city).append(dist).append(e.getAddress()).toString().substring(0,8));
			
			// 调用问题件管理页面统计数量接口
			Pagination pagination = new Pagination(1, 10);
			List<Questionnaire> questionList = questionnaireService.vipQueryQuestionnaireManageList(bindString,null,null,null,e.getMailNo(),null,null,null,null,null,null,null,null,pagination);
			if(questionList == null || questionList.size()==0) {
			    pid.setTips("0");
			} else {
				pid.setQuestionFenQuTime(questionList.get(0).getPartitionDate());
			    pid.setTips("1");
			}
			order = (Order) orderService.getOrderByMailNo(e.getMailNo());
			if (order != null) {
				customerId = order.getCustomerId();
			}
			List<User> userList = userService.getUsersByCustomerIds(customerId);
			if (userId != null) { //店铺筛选
				shopName = userService.getUserById(userId).getShopName();
				logger.info("==店铺筛选：" + shopName);
				pid.setShopName(shopName);
			} else if(customerId !=null) {   //点击菜单进入
				for (User u : userList) {
					logger.info("==所以关联店铺：" + u.getShopName());
					if (customerId.equals(u.getTaobaoEncodeKey())) {
						pid.setShopName(u.getShopName());
					}
				}
			}
			
			if(e.getHoldTime() != null){
				time = DateUtil.format(e.getHoldTime(), "yyyy-MM-dd HH:mm:ss"); //揽收时间
				pid.setSendTime1(time.split(" ")[0]);
				pid.setSendTime2(time.split(" ")[1]);
				//揽收时间
				logger.info("===揽收时间：" + e.getHoldTime());
				mins = DateUtil.minuteInterval(new Date(), e.getHoldTime());
				d = (int) (mins / (24*60)); //天
				h = ((int) (mins % (24*60)))/60; //小时
				if(!"0".equals(h + "")) {
					pid.setUserTime(d + "天" + h + "小时");
				} else {
					pid.setUserTime(d + "天");
				}
				logger.info("===================以用时间=" + pid.getUserTime());
				userDay = Double.valueOf(d);
				for(WarnValue warnValue : warnValueList){
					if(prov1.equals(warnValue.getDestination())){
						try{
						    passTime = userDay - Double.valueOf(warnValue.getWarnValue());
							z = String.valueOf((int)passTime);
							x = String.valueOf(h.intValue());
							passTime = Double.valueOf(z + "." + x);
							String pt = String.valueOf(passTime); 
							pid.setUserTime2(passTime); // 超时时间=以用时间-预警值
							if(!"0".equals((pt.split("\\."))[1] + "")) {
								pid.setPassTime(z + "天" + x + "小时");
							} else {
								pid.setPassTime(z + "天");
							}
							logger.info("===================预警值=" + warnValue.getDestination() + "-" + warnValue.getWarnValue());
							logger.info("===================超时时间=" + pid.getPassTime());
						}catch(Exception e1){
							logger.error(e1.getMessage());
						}
					}
				}
			}
			passIssueDTOList.add(pid);
			passIssueDTOMap.put(pid.getMailNo(), pid);
		} 
		
		
//		return passIssueDTOList;
		return ordersSearchList(passIssueDTOMap,passIssueDTOList);
	}
	
	/**
	 * 查金刚获取当前位置
	 */
	public List<PassIssueDTO> ordersSearchList(HashMap<String,PassIssueDTO> PassIssueDTOMap,
								List<PassIssueDTO> passIssueDTOList){
		TaoBaoOrderAction op = new TaoBaoOrderAction();
		List<PassIssueDTO> PassIssueDTO=new ArrayList<PassIssueDTO>();
		List<String> mailNoList=new ArrayList<String>();
		StepInfo lastStep = null;
		if(passIssueDTOList !=null && passIssueDTOList.size()>0){
			String[] logisticsId=new String[passIssueDTOList.size()];
			for(int passIssue=0;passIssue<passIssueDTOList.size();passIssue++){
				logisticsId[passIssue] = new String(passIssueDTOList.get(passIssue).getMailNo());
			}
			try {
//				List<QueryOrder> logisticList = op.ordersSearch(logisticsId);// 运单物流信息
				List<PassMessageQueryOrder> logisticList = op.ManageOrdersSearch(logisticsId);
				if (logisticList != null && logisticList.size() > 0) {
					for(PassMessageQueryOrder qo:logisticList){
						//获取已经签收的订单运单号
						if(qo.getOrderStatus() != null && qo.getOrderStatus().equals("SIGNED")){
							mailNoList.add(qo.getMailNo());
						}
						
						if (qo.getSteps() != null && qo.getSteps().size() > 0) {
							lastStep = qo.getSteps().get(qo.getSteps().size() - 1);
									PassIssueDTOMap.get(qo.getMailNo()).setStepIfno(lastStep);//当前位置
							logger.info(lastStep + "########################" + lastStep.getAcceptAddress());
						}
						
					}
				}
			} catch (Exception e1) {
				logger.error("##引擎中查询出超时mailNo查询金刚获取当前位置异常.异常信息：",e1);
			}
			
			
			//过滤已经签收的单子
			for(PassIssueDTO dto:passIssueDTOList){
				if(!mailNoList.contains(dto.getMailNo())){
					PassIssueDTO.add(PassIssueDTOMap.get(dto.getMailNo()));
				}
			}
			
			
		
//			//将map转换成list后返回
//			for(PassIssueDTO dto:passIssueDTOList){
//				PassIssueDTO.add(PassIssueDTOMap.get(dto.getMailNo()));
//			}
			
		}
		return PassIssueDTO;
	}

	@Override
	public Integer searPassIssueByTotalCount(String solrEccoreUrl,
			Pagination<EccoreSearchResultDTO> pagination1) throws Exception {
		try {
			if((pagination1.getParams().get("overTimeMap")) != null && !"".equals((pagination1.getParams().get("overTimeMap")))) {
				eccoreSearchService.searchOverTimeData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),pagination1);
			} else {
				eccoreSearchService.searchWayBillData(ConfigUtilSingle.getInstance().getSolrEccoreUrl(),pagination1);
			}
		} catch (Exception e) {
			logger.error("查找超时件错误", e);
		}
		
		logger.info("#query recards size=" + pagination1.getRecords().size());
		
		return pagination1.getRecords().size();
	}

		
}
