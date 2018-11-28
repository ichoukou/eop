package net.ytoec.kernel.test;

import java.util.List;

import javax.inject.Inject;

import net.ytoec.kernel.dao.ErrorMessageDao;
import net.ytoec.kernel.dataobject.ErrorMessage;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

/**
 * @author Wangyong
 */
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class ErrMsgDaoTest extends AbstractJUnit38SpringContextTests {
	
	@Inject
	private ErrorMessageDao<ErrorMessage> dao;
	
	@Test
	public void testAdd(){
		ErrorMessage err = new ErrorMessage();
		err.setErrorType("type");
		err.setErrorReason("reason");
		dao.addErrorMessage(err);
	}
	
	@Test
	public void testGet(){
		for(ErrorMessage err : dao.getAllErrorMessage()){
			System.out.println(err);
		}
	}

	@Test
	public void testEdit(){
		List<ErrorMessage> list = dao.getAllErrorMessage();
		for(ErrorMessage err : list){
			err.setErrorReason("nullPoint");
			err.setErrorType("Exception");
			dao.editErrorMessage(err);
			System.out.println(dao.getErrorMessageById(err.getId()).getErrorReason());
		}
	}
	
	@Test
	public void testRemove(){
		for(ErrorMessage err : dao.getAllErrorMessage()){
			System.out.println("删除结果："+dao.removeErrorMessage(err));
		}
	}
	
}
