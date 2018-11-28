package com.api;

import com.YtoMain;
import com.gui.YtoGui;

/**
 * 退出应用线程
 * 
 * @author huangtianfu
 * 
 */
public class ExitAppThread extends Thread {

	public ExitAppThread() {
		while (true) {
			if (YtoMain.getSynWaybillThread().getStatus() == 1) {
				break;
			} else {
				try {
					Thread.sleep(1 * 1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		YtoGui.getInstance().setVisible(false);
		System.exit(0);// 结束java虚拟机
	}

	@Override
	public void run() {
		System.out.println("开始线程成功");
	}

}
