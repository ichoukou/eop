package net.ytoec.kernel.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.mapper.QuestionnaireMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 测试问题单mapper
 * @author Wangyong
 * @2011-8-1
 * net.ytoec.kernel.test
 */
public class QuestionnaireMapperTest {
	
	private static QuestionnaireMapper<Questionnaire> mapper;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		Thread.sleep(2000);
		mapper = (QuestionnaireMapper) ctx.getBean("questionnaireMapper");
	}

//	@Test
//	public void testAdd() {
//		Questionnaire question = new Questionnaire();
//		question.setBranchId(1);
//		question.setDealStatus("未处理");
//		question.setMailNO("010107");
//		question.setMailType(1);
//		question.setSenderTime(new Date());
//		question.setVipCellphone("13900000000");
//		question.setVipId(1);
//		question.setVipName("zhangfei");
//		question.setVipPhone("021-00000000");
//		question.setVipTextname("张飞");
//		question.setFeedbackInfo("包装破损");
//		mapper.add(question);
//	}
	
//	@Test
//	public void testGet() {
//		List<Questionnaire> list = mapper.getAllQuestionnaire();
//		for(Questionnaire question : list){
//			System.out.println(mapper.get(question));
//		}
//	}
	
//	@Test
//	public void testGetByBranchId(){
//		Questionnaire ques = new Questionnaire();
//		ques.setBranchId(1);
//		List<Questionnaire> list = mapper.getQuestionnaireListByBranchId(ques);
//		System.out.println(list.size());
//	}
//	@Test
//	public void getQuestionnaireListByMapSearch() throws ParseException{
//		String datestr = "2011-08-01";
//		String datestr2 = "2011-08-03";
//		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println(sim.parse(datestr));
//		Map map = new HashMap();
//		map.put("branchId", 1);
//		map.put("vipId", 1);
//		map.put("dealStatus", "待处理");
//		map.put("startTime", sim.parse(datestr));
//		map.put("endTime", sim.parse(datestr2));
//		map.put("mailNO", "010103");
//		List<Questionnaire> list = mapper.getQuestionnaireListByMapSearch(map);
//		for(Questionnaire q : list){
//			System.out.println(q.getId());
//		}
//		System.out.println(list.size());
//	}
	
//	@Test
//	public void testRemove() {
//		List<Questionnaire> list = mapper.getAllQuestionnaire();
//		if(list.size()>0){
//			mapper.remove(list.get(list.size()-1));
//		}
//	}
	
}
