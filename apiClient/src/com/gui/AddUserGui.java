package com.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.dao.SqliteDao;
import com.model.UserConfig;

public class AddUserGui extends BaseGui {

	private static final long serialVersionUID = 5038377599120509760L;

	private static JTextField customerCode;
	private static JTextField parternId;
	private static JTextField clientId;

	private static final String SAVE_USER = "saveUser";
	private static final String CANCEL = "cancel";
	private static JLabel message;// 显示错误信息

	// 单例模式
	private static AddUserGui addUserFrame;

	public static AddUserGui getInstance() {
		if (addUserFrame == null) {
			addUserFrame = new AddUserGui();
		}
		return addUserFrame;
	}

	public AddUserGui() {
		init();// 初始化界面
	}

	/**
	 * 界面初始化方法
	 */
	private void init() {
		// 设置窗口标题
		setTitle("添加用户");
		// 设置窗口大小
		setSize(330, 250);
		setIconImage(Toolkit.getDefaultToolkit().getImage("png/yuantong.png"));
		getContentPane().setLayout(null);
		// 居中
		setLocationRelativeTo(null);

		add(createJLabel("customerCode", 10, 40, 120, 18), null);
		customerCode = createJTextField(90, 40, 160, 20, true);
		customerCode.setSize(170, 25);
		add(customerCode, null);

		add(createJLabel("clientId", 10, 75, 120, 18), null);
		clientId = createJTextField(90, 75, 160, 20, true);
		clientId.setSize(170, 25);
		add(clientId, null);

		add(createJLabel("parternId", 10, 110, 120, 18), null);
		parternId = createJTextField(90, 110, 160, 20, true);
		parternId.setSize(170, 25);
		add(parternId, null);

		JButton jButton = createJButton(SAVE_USER, 80, 160, 70, 27);
		addListener(SAVE_USER, jButton);
		add(jButton, null);

		jButton = createJButton(CANCEL, 180, 160, 70, 27);
		addListener(CANCEL, jButton);
		add(jButton, null);
	}

	private void addListener(final String buttonId, JButton jButton) {
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 保存
				if (buttonId.equals(SAVE_USER)) {
					UserConfig userConfig = new UserConfig();
					userConfig.setCustomerCode(customerCode.getText());
					userConfig.setParternId(parternId.getText());
					userConfig.setClientId(clientId.getText());
					SqliteDao.insertUserConfig(userConfig);

					// 1: 刷新主界面用户列表下拉框
					YtoGui ytoGui = YtoGui.getInstance();
					ytoGui.getUserCustomerCodeJComBox().removeAllItems();
					List<UserConfig> userConfigs = ytoGui.getUserConfigs();
					userConfigs.add(userConfig);
					for (UserConfig user : userConfigs) {
						ytoGui.getUserCustomerCodeJComBox().addItem(user);
					}
					// 2: 关闭
					addUserFrame.dispose();
				}
				// 退出
				else if (buttonId.equals(CANCEL)) {
					setVisible(false);
				}
			}
		});
	}
}
