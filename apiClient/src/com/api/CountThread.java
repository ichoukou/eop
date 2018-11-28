package com.api;

import com.dao.JdbcDao;
import com.gui.YtoGui;
import com.model.JdbcConfig;

/**
 * 统计同步的面单和上传的订单线程
 * 
 * @author qixiaobing
 * 
 */
public class CountThread extends Thread {
	// 配置信息
	private JdbcConfig jdbcConfig;

	public CountThread(JdbcConfig jdbcConfig) {
		this.jdbcConfig = jdbcConfig;
	}

	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("开始统计数量线程成功");
				int orderNum = JdbcDao.selectOrderUpload(jdbcConfig);
				int surfaceBillNum = JdbcDao.selectSurfaceBill(jdbcConfig);
				YtoGui.getOrderNum().removeAll();
				YtoGui.getOrderNum().setText(orderNum + "");
				YtoGui.getFaceBillNum().removeAll();
				YtoGui.getFaceBillNum().setText(surfaceBillNum + "");

				Thread.sleep(10 * 1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
