package net.ytoec.kernel.action.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientThreadTest extends Thread {
    private final HttpChannel sender;
    
    
    
    private static Logger logger = LoggerFactory.getLogger(HttpMessageSender.class);
    
    public HttpClientThreadTest(HttpChannel sender) {
        this.sender = sender;
    }
    
    @Override
    public void run() {
    	
    	  
        try {
        	long start=	System.currentTimeMillis();        	
    		Object res = null;
    		for (int i = 0; i < 500; ++i) {
    			res = sender.sendPostRequest();
    			
    			if (res == null || res.toString().equals("") || "false".equalsIgnoreCase(res.toString()))
    				logger.debug(Thread.currentThread().getName()+"-------------null-----------------");
    			
    		}
    		logger.debug(Thread.currentThread().getName()+" total time:"+(System.currentTimeMillis()-start)/1000);
    		logger.info("end...");
        	
        } catch (Exception ex) {
        }
    }
}
