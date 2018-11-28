package net.ytoec.kernel.test;

import java.util.List;

import net.ytoec.kernel.dao.ServerThreadDao;
import net.ytoec.kernel.dataobject.ServerThread;
import net.ytoec.kernel.service.ServerThreadService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ServerThreadServiceTest {

	private static ServerThreadService<ServerThread> service;
	private static int id;

	private static ServerThreadDao<ServerThread> dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"classpath*:applicationContext-*.xml");
		service = (ServerThreadService) ctx.getBean("serverThreadServiceImpl");

		dao = (ServerThreadDao) ctx.getBean("serverThreadDaoImpl");
	}

	@Test
	public void testAdd() {
		ServerThread obj = new ServerThread();
		obj.setEndNum(10);
		obj.setEndTaskId(20);
		obj.setIp("192.168.4.152");
		obj.setPort("3306");
		obj.setRemark("我了个去");
		obj.setStartNum(1);
//		obj.setStartTaskId(2);
//		dao.addServerThread(obj);
		service.addServerThread(obj);
	}
	
	@Test
	public void testDel() {
		ServerThread obj = service.getLastServerThread();
		System.out.println("最新记录："+obj.getId());
//		service.removeServerThread(obj);
		dao.removeServerThread(obj);
	}
	
	@Test
	public void testGetLast(){
		List<ServerThread> list = dao.getLastServerThread();
		for(ServerThread s : list){
			System.out.println("============"+s.getId());
		}
		System.out.println("============");
		ServerThread obj = service.getLastServerThread();
//		System.out.println("最新记录："+obj.getId());
	}
	
}
