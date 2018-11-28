package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dataobject.ErrorMessage;
import net.ytoec.kernel.mapper.ErrorMessageMapper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ErrMsgMapperTest {
	private static ErrorMessageMapper<ErrorMessage> mapper;
	private static int id = 1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		Thread.sleep(2000);
		mapper = (ErrorMessageMapper) ctx.getBean("errorMessageMapper");
	}

//	@Test
//	public void testAdd() {
//		for(int i=0; i<5; i++){
//			ErrorMessage msg = new ErrorMessage();
//			msg.setErrorType("type"+i);
//			msg.setRemark("remark"+i);
//			mapper.add(msg);
//		}
//	}
//	
//	@Test
//	public void testGet() {
//		for(ErrorMessage err : mapper.getAllErrorMessage()){
//			System.out.println(mapper.get(err));
//		}
//	}
	
	@Test
	public void testGetByType(){
		List<ErrorMessage> list = mapper.getErrMsgListByType("4");
		System.out.println(list.size());
	}

//	@Test
//	public void testEdit() {
//		for(ErrorMessage err : mapper.getAllErrorMessage()){
//			err.setErrorReason("no reason");
//			err.setErrorType("errorType_");
//			err.setRemark("remark_");
//			mapper.edit(err);
//			System.out.println(mapper.get(err).getErrorReason());
//		}
//	}
//
//	@Test
//	public void testRemoveErrt() {
//		for(ErrorMessage err : mapper.getAllErrorMessage()){
//			mapper.remove(err);
//		}
//	}
}
