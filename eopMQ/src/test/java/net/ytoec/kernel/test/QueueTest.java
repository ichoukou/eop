package net.ytoec.kernel.test;

import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.service.MQService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class QueueTest extends BaseTest{
	@Autowired
	private MQService mqService;
	@Test
	public void write() throws Exception{
		for(int i=0;i<5;i++){
			mqService.send("aaa", Constants.OFFLINE_TYPE);
		}
	}
	@Test
	public void read() throws Exception{
		mqService.receive(Constants.OFFLINE_TYPE);
	}
}
