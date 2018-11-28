package com;

import com.api.CountThread;
import com.api.ExitAppThread;
import com.api.SynWaybillThread;
import com.api.UploadOrderThread;
import com.gui.YtoGui;

public class YtoMain {

	/**
	 * 同步面单线程
	 */
	private static SynWaybillThread synWaybillThread;

	/**
	 * 上传订单线程
	 */
	private static UploadOrderThread uploadOrderThread;

	/**
	 * 统计同步的面单和上传的订单线程
	 */
	private static CountThread countThread;

	/**
	 * 退出应用线程
	 */
	private static ExitAppThread exitAppThread;

	public static void main(String[] args) {
		// 1:创建主界面
		YtoGui ytoGui = YtoGui.getInstance();

		// 2:创建并启动同步面单线程
		synWaybillThread = new SynWaybillThread(ytoGui.getJdbcConfig(),
				ytoGui.getApiConfig(), ytoGui.getUserConfigs());
		synWaybillThread.setDaemon(true);
		synWaybillThread.start();

		// 3:创建并启动同步订单线程
		uploadOrderThread = new UploadOrderThread(ytoGui.getJdbcConfig(),
				ytoGui.getApiConfig(), ytoGui.getUserConfigs());
		uploadOrderThread.setDaemon(true);
		uploadOrderThread.start();

		// 4:创建并启动统计线程
		countThread = new CountThread(ytoGui.getJdbcConfig());
		countThread.setDaemon(true);
		countThread.start();

		// 5:创建并启动退出线程
		exitAppThread = new ExitAppThread();
		exitAppThread.setDaemon(true);
		exitAppThread.start();
	}

	public static SynWaybillThread getSynWaybillThread() {
		return synWaybillThread;
	}

	public static UploadOrderThread getUploadOrderThread() {
		return uploadOrderThread;
	}

}
