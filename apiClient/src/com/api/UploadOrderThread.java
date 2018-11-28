package com.api;

import java.util.ArrayList;
import java.util.List;

import com.dao.JdbcDao;
import com.gui.YtoGui;
import com.model.ApiConfig;
import com.model.JdbcConfig;
import com.model.UserConfig;

/**
 * 上传订单线程
 * 
 * @author huangtianfu
 * 
 */
public class UploadOrderThread extends Thread {

	// 配置信息
	private JdbcConfig jdbcConfig;
	private ApiConfig apiConfig;
	private List<UserConfig> userConfigs;

	// 是否停止线程
	private boolean stopThread = false;

	public UploadOrderThread(JdbcConfig jdbcConfig, ApiConfig apiConfig,
			List<UserConfig> userConfigs) {
		this.jdbcConfig = jdbcConfig;
		this.apiConfig = apiConfig;
		this.userConfigs = userConfigs;
	}

	@Override
	public void run() {
		List<OrderUpload> orderUploads = new ArrayList<OrderUpload>();
		OrderUploadResponse orderUploadResponse;
		int uploadCounts = 0;
		while (true) {
			try {
				// 应用退出
				if (YtoGui.isExitApp()) {
					break;
				}

				if (!YtoGui.isExitApp() && !stopThread) {
					for (UserConfig user : userConfigs) {

						// 退出for循环
						if (stopThread) {
							break;
						}

						// 查询用户的待上传订单
						orderUploads = JdbcDao.selectOrderUploadsOfUser(
								user.getCustomerCode(), jdbcConfig);
						for (OrderUpload orderUpload : orderUploads) {

							// 退出for循环
							if (stopThread) {
								break;
							}
							
							System.out.println("上传订单中......");

							uploadCounts = 0;
							while (true) {
								// 同一订单最多上传三次
								if (uploadCounts == 3) {
									break;
								}
								orderUploadResponse = YtoApiClient
										.uploadOrderToYto(orderUpload,
												apiConfig.getUploadOrderUrl(),
												user.getParternId(),
												user.getClientId(), jdbcConfig);
								// 保存订单上传失败原因,上传次数加1
								if (orderUploadResponse != null
										&& !orderUploadResponse.isSuccess()) {
									// 上传次数加1
									uploadCounts++;
									// 保存上传失败原因
									JdbcDao.saveMessage(
											"订单"
													+ orderUpload
															.getTxLogisticId()
													+ "上传失败，电子面单号:"
													+ orderUpload.getMailNo()
													+ ",失败原因:"
													+ orderUploadResponse
															.getReason(), user
													.getCustomerCode(), null,
											"uploadOrderToYto", jdbcConfig);

								}
								// 上传成功，设置订单上传状态为已上传成功
								else {
									uploadCounts = 3;
									JdbcDao.updateUploadOrderState(
											orderUpload.getTxLogisticId(), "Y",
											jdbcConfig);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 停止线程
	public void stopThread() {
		stopThread = true;
	}

	// 启动线程
	public void startThread() {
		stopThread = false;
		Thread.currentThread().run();
	}

}
