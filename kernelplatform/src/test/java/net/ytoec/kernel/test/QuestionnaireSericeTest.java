package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.QuestionnaireDeal;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.QuestionnaireService;
import net.ytoec.kernel.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class QuestionnaireSericeTest {

	private static QuestionnaireService<Questionnaire> qns;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:applicationContext-*.xml");
		qns = (QuestionnaireService) ctx.getBean("questionnaireServiceImpl");

	}

	/*@Test
	public void testGetQuestionnaireByMapSearch() {
		String branchId = "755063";
		String startTime = "";
		String endTime = "";
		String userCode = "";
		String mailNO = "";
		String dealStatus = "";
		Pagination<Questionnaire> pagination = new Pagination<Questionnaire>(1, 10);
		boolean flag = true;
		List<Questionnaire> qnList = qns.getQuestionnaireByMapSearch(branchId, startTime, endTime, userCode, mailNO, dealStatus, pagination, flag);
		System.out.println("查询到的问题件数量："+qnList.size());
		for(Questionnaire qn : qnList){
			System.out.println("运单号:"+qn.getMailNO());
			System.out.println("类型:"+qn.getFeedbackInfo());
			System.out.println("处理状态:"+qn.getDealStatus());
			
			List<QuestionnaireDeal> quesDealList = qn.getQuesDealList();
			String intro = "暂无问题描述";
			if(quesDealList.size() > 0){
				intro = StringUtils.defaultIfBlank(quesDealList.get(0).getDealContent(), "暂无问题描述");
			}
			System.out.println("问题描述:"+intro);
			System.out.println("问题上报人:"+qn.getMailNO());
			
			StringBuilder response = new StringBuilder("网点尚未反馈");
			if(quesDealList.size() > 1){
				for(int i=1;i<quesDealList.size();i++){
					response.append(DateUtil.format(quesDealList.get(i).getDealTime(), "yyyy-MM-dd HH:mm:SS反馈：")+
							StringUtils.defaultIfBlank(quesDealList.get(i).getDealContent()+"\r", "\r"));
				}
			}
			System.out.println("网点反馈信息:"+response.toString());
			
			User customer = qn.getCustomer();
			System.out.println("客户信息:"+customer.getShopName()+" 电话："+customer.getMobilePhone());
		}
	}*/

}
