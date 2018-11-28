package net.ytoec.kernel.test;

import java.io.IOException;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Session;

import net.ytoec.kernel.action.remote.xml.UpdateInfo;
import net.ytoec.kernel.mapper.base.SqlMapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 
 * @author yto
 * 
 */
public class BatchUpdateOrderStatusTest {
	
	/*public BatchUpdateOrderStatusTest(){
		ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:applicationContext-*.xml");
		sendTaskToTBService = (SendTaskToTBService<SendTaskToTB>) ctx.getBean("sendTaskToTBServiceImpl");
	}*/
	/**
	 * 批量操作
	 * @param list
	 * @throws IOException 
	 */
	public static void batchInsert(List<UpdateInfo> list, Class<? extends SqlMapper> mapper) throws IOException{
		Session session = null;
		java.io.Reader reader = Resources.getResourceAsReader("mybatis.xml");
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
		SqlSessionTemplate sessionTemplate  =  new SqlSessionTemplate(factory,ExecutorType.BATCH);
		session = (Session) factory.openSession();
		for(int i = 0;i<list.size();i++){
			sessionTemplate.update("sql");
		}
		try {
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

