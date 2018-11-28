package net.ytoec.kernel.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.inject.Inject;

import net.ytoec.kernel.dao.QuestionnaireDao;
import net.ytoec.kernel.dataobject.Questionnaire;
import net.ytoec.kernel.dataobject.TaobaoTask;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.parser.json.ObjectJsonParser;
import com.taobao.api.response.TradeFullinfoGetResponse;

@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ReadTaobaoOrderZipTest extends AbstractJUnit38SpringContextTests{
	
	@Inject
	private QuestionnaireDao<Questionnaire>   questionaireDao;
	
//	@Test
//	public void testUpdate(){
//		try{
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("buyerNick", "Hello");
//		List<String> quesIds = new ArrayList<String>();
//		quesIds.add("1");
//		quesIds.add("2");
//		params.put("quesIds", quesIds);
//		questionaireDao.updateQuesById(params);
//		System.out.println("end");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}

	@Test
	public void testRead(){
		try{
			File taskFiles = new File("D:\\173637514");
			BufferedReader reader = new BufferedReader(new FileReader(taskFiles));
			File outFile = new File("D:\\out.txt");
        	BufferedWriter output = new BufferedWriter(new FileWriter(outFile));
			String tempString = null;
			TaobaoTask taobaoTask = new TaobaoTask();
			taobaoTask.setUserId(48652);
			int count = 0;
			while ((tempString = reader.readLine()) != null) {
				ObjectJsonParser<TradeFullinfoGetResponse> parser = new ObjectJsonParser<TradeFullinfoGetResponse>(TradeFullinfoGetResponse.class);

		        TradeFullinfoGetResponse rsp=new TradeFullinfoGetResponse();
		        try {
		            rsp = parser.parse(tempString);
		        } catch (ApiException e) {
		            e.printStackTrace();
		        }
		        count++;
		        Trade trade = rsp.getTrade();        
//		        System.out.println("["+count+"]");
//		        System.out.println("num_iid   = "+trade.getNumIid());
//		        System.out.println("BuyerNick = "+trade.getBuyerNick());
//		        System.out.println("mobile    = "+trade.getReceiverMobile());
//		        System.out.println("phone     = "+trade.getReceiverPhone());
//		        System.out.println("payment   = "+trade.getPayment());
//		        System.out.println("created   = "+trade.getCreated());
//		        System.out.println("receive_name  = "+trade.getReceiverName());
//		        System.out.println("receive_state = "+trade.getReceiverState());
//		        System.out.println("receive_city  = "+trade.getReceiverCity());
//		        System.out.println("receive_district = "+trade.getReceiverDistrict());
//		        System.out.println("receive_address  = "+trade.getReceiverAddress());
//		        System.out.println("receive_zip      = "+trade.getReceiverZip());
//		        System.out.println("--------------------------");

		        	
	        	output.write("["+count+"]\r\n");
	        	output.write("num_iid   = "+trade.getNumIid()+"\r\n");
			    output.write("BuyerNick = "+trade.getBuyerNick()+"\r\n");
			    output.write("mobile    = "+trade.getReceiverMobile()+"\r\n");
			    output.write("phone     = "+trade.getReceiverPhone()+"\r\n");
			    output.write("payment   = "+trade.getPayment()+"\r\n");
			    output.write("created   = "+trade.getCreated()+"\r\n");
			    output.write("receive_name  = "+trade.getReceiverName()+"\r\n");
			    output.write("receive_state = "+trade.getReceiverState()+"\r\n");
			    output.write("receive_city  = "+trade.getReceiverCity()+"\r\n");
			    output.write("receive_district = "+trade.getReceiverDistrict()+"\r\n");
			    output.write("receive_address  = "+trade.getReceiverAddress()+"\r\n");
			    output.write("receive_zip      = "+trade.getReceiverZip()+"\r\n");
			    output.write("--------------------------\r\n");
			    output.newLine();
			   
		        
			}
			output.close();
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
