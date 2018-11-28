package com.api;

import java.util.List;

import com.gui.YtoGui;
import com.model.ApiConfig;
import com.model.JdbcConfig;
import com.model.UserConfig;

/**
 * 同步面单线程
 * 
 * @author huangtianfu
 * 
 */
public class SynWaybillThread extends Thread {

	// 配置信息
	private JdbcConfig jdbcConfig;
	private ApiConfig apiConfig;
	private List<UserConfig> userConfigs;

	// 是否停止线程
	private boolean stopThread = false;

	// 线程状态
	private int status = 0;// 0:运行中，1：已经停止

	public SynWaybillThread(JdbcConfig jdbcConfig, ApiConfig apiConfig,
			List<UserConfig> userConfigs) {
		this.jdbcConfig = jdbcConfig;
		this.apiConfig = apiConfig;
		this.userConfigs = userConfigs;
	}

	@Override
	public void run() {
		System.out.println("开始线程成功");
		long useTime = 0L;
		long loopStartTime = 0L;
		long loopEndTime = 0L;
		while (true) {
			try {
				// 应用退出
				if (YtoGui.isExitApp()) {
					break;
				}
				loopStartTime = System.currentTimeMillis();
				if (!YtoGui.isExitApp() && !stopThread) {
					for (UserConfig user : userConfigs) {
						// 退出for循环
						if (YtoGui.isExitApp() || stopThread) {
							break;
						}
						System.out.println("同步面单中.....");
						YtoApiClient.synWaybillFromYto(
								apiConfig.getSynWaybillUrl(),
								user.getCustomerCode(), user.getParternId(),
								user.getClientId(), jdbcConfig);
					}
				}
				loopEndTime = System.currentTimeMillis();
				useTime = loopEndTime - loopStartTime;
				/*
				 * if (!YtoGui.isExitApp() && !stopThread) { if (useTime < 1 *
				 * 60000) {// 小于1分钟,休眠一会，使一次循环的时间为1分钟
				 * System.out.println("wait start1..."); try { Thread.sleep(1 *
				 * 60000 - useTime); } catch (InterruptedException e) {
				 * e.printStackTrace(); } System.out.println("wait end1..."); }
				 * }
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("退出线程成功");
		status = 1;
	}

	// 停止线程
	public void stopThread() {
		stopThread = true;
	}

	// 启动线程
	public void startThread() {
		stopThread = false;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
