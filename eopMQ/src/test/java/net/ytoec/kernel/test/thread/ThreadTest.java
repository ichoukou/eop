package net.ytoec.kernel.test.thread;

import net.ytoec.kernel.service.MQService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ThreadTest {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath*:applicationContext-mq.xml");
		
		MQService mqService = context.getBean(MQService.class);
		for(int i=0;i<50;i++){			
			new WriteQueue(mqService).start();
		}
		for(int i=0;i<20;i++){			
			new ReadQueue(mqService).start();
		}
		
	}
}
