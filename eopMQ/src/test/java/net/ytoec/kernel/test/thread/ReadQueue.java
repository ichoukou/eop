package net.ytoec.kernel.test.thread;

import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.service.MQService;

public class ReadQueue extends Thread{
	private MQService mqService;
	public ReadQueue(MQService mqService) {
		this.mqService=mqService;
	}
	@Override
	public void run() {
		while (true) {
			try {
				mqService.receive(Constants.OFFLINE_TYPE);
			} catch (Exception e) {				
				e.printStackTrace();	
			}
		}
	}
}
