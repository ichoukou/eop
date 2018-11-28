package net.ytoec.kernel.test.thread;

import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.service.MQService;

public class WriteQueue extends Thread {
	private MQService mqService;

	public WriteQueue(MQService mqService) {
		this.mqService = mqService;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100000; i++) {
			try {
				mqService.send("11", Constants.OFFLINE_TYPE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
