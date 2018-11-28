package com.gui;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.YtoMain;
import com.dao.SqliteDao;
import com.model.ApiConfig;
import com.model.JdbcConfig;
import com.model.UserConfig;

public class YtoGui extends BaseGui {

	private static final long serialVersionUID = 202485399460759677L;

	private static JLabel jlable;
	private static JLabel faceBillJLabel;
	private static JLabel orderJLabel;
	private static JTextArea faceBillNum;
	private static JTextArea orderNum;
	private static JComboBox dbTypeJText;
	private static JTextField usernameJText;
	private static JTextField passwordJText;
	private static JTextField databaseJText;
	private static JTextField portJText;
	private static JTextField urlJText;
	private static JTextField uploadOrderUrlJText;
	private static JTextField synWaybillUrlJText;
	private static JComboBox userCustomerCodeJComBox;
	private static JTextField customerCodeJText;
	private static JTextField parternIdJText;
	private static JTextField clientIdJText;
	private JButton synWaybillJButton;
	private JButton uploadOrderJButton;
	private JLabel synWaybillJLabel;
	private JLabel uploadOrderJLabel;

	private static int dbNum = 1;// 编辑框可编辑计数器
	private static int urlNum = 1;// 编辑框可编辑计数器
	private static int userNum = 1;// 编辑框可编辑计数器

	private static final int DB_ENABLED = 1;
	private static final int DB_VISIBLE = 2;
	private static final int URL_ENABLED = 3;
	private static final int URL_VISIBLE = 4;
	private static final int USER_ENABLED = 5;
	private static final int USER_VISIBLE = 6;

	private static final int EDIT_DB = 1;
	private static final int SAVE_DB = 2;
	private static final int EDIT_URL = 3;
	private static final int SAVE_URL = 4;
	private static final int EDIT_USER = 5;
	private static final int SAVE_USER = 6;
	private static final int ADD_USER = 7;
	private static final int SYN_WAYBILL = 8;
	private static final int UPLOAD_ORDER = 9;
	private static final int START_SYNWAYBILL = 10;
	private static final int START_UPLOADORDER = 11;
	private static boolean IS_UPLOAD_ORDER = true;
	private static int SYNWAYBILL_STATE = 2;// 1:已经停止， 2：同步中, 3:停止中或者启动中

	private static boolean exitApp = false;

	// 配置信息
	private static JdbcConfig jdbcConfig;
	private static ApiConfig apiConfig;
	private static List<UserConfig> userConfigs;
	private static ArrayList<JdbcConfig> jdbcConfigs;// 用于jdbc配置下拉框

	private Map<String, Object> map = new HashMap<String, Object>();
	private static final String buttonKey = "button_";

	DynGifLabel exitJLabel;

	static {
		jdbcConfigs = new ArrayList<JdbcConfig>();
		JdbcConfig jdbcConfig = new JdbcConfig();
		jdbcConfig.setType("mysql");
		jdbcConfigs.add(jdbcConfig);
		jdbcConfig = new JdbcConfig();
		jdbcConfig.setType("orcle");
		jdbcConfigs.add(jdbcConfig);
		jdbcConfig = new JdbcConfig();
		jdbcConfig.setType("sql server");
		jdbcConfigs.add(jdbcConfig);
	}

	// 单例模式
	private static YtoGui ytoGui;

	public static YtoGui getInstance() {
		if (ytoGui == null) {
			ytoGui = new YtoGui();
		}
		return ytoGui;
	}

	public YtoGui() {
		super();

		// 0:初始化配置信息
		initConfig();

		setSize(1088, 600);
		// 居中
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/yuantong.png"));
		jlable = createJLabel("一: 配置jdbc信息", 35, 10, 120, 18);
		jlable.setFont(new java.awt.Font("Dialog", 1, 15));
		add(jlable, null);
		if (!jdbcConfigs.isEmpty()) {
			add(createJLabel("DBType", 34, 45, 120, 18), null);
			dbTypeJText = createDbTypeJComboBox(jdbcConfigs, 97, 45, 160, 20);
			addListenerToDbTypeJcomboBox(dbTypeJText);
			add(dbTypeJText, null);

			add(createJLabel("UserName", 34, 90, 120, 18), null);
			usernameJText = createJTextField(97, 90, 160, 20, false);
			usernameJText.setText(jdbcConfig.getUsername());
			add(usernameJText, null);

			add(createJLabel("PassWord", 34, 135, 120, 18), null);
			passwordJText = createJTextField(97, 135, 160, 20, false);
			passwordJText.setText(jdbcConfig.getPassword());
			add(passwordJText, null);

			add(createJLabel("database", 34, 180, 120, 18), null);
			databaseJText = createJTextField(97, 180, 160, 20, false);
			databaseJText.setText(jdbcConfig.getDbName());
			add(databaseJText, null);

			add(createJLabel("Port", 34, 225, 120, 18), null);
			portJText = createJTextField(97, 225, 160, 20, false);
			portJText.setText(jdbcConfig.getPort());
			add(portJText, null);

			add(createJLabel("URL", 34, 270, 120, 18), null);
			urlJText = createJTextField(97, 270, 160, 20, false);
			urlJText.setText(jdbcConfig.getIp());
			add(urlJText, null);
		}
		JButton jButton = createJButton("编辑", 97, 320, 70, 27);
		addListener(EDIT_DB, jButton);
		add(jButton, null);

		jButton = createJButton("保存", 185, 320, 70, 27);
		jButton.setEnabled(false);
		addListener(SAVE_DB, jButton);
		add(jButton, null);
		map.put(buttonKey + SAVE_DB, jButton);
		// 2:配置api请求参数
		jlable = createJLabel("二: 配置API请求URL", 334, 10, 220, 18);
		jlable.setFont(new java.awt.Font("Dialog", 1, 15));
		add(jlable, null);
		if (apiConfig != null) {
			add(createJLabel("synWaybillUrl", 334, 49, 220, 18), null);
			synWaybillUrlJText = createJTextField(432, 49, 160, 20, false);
			synWaybillUrlJText.setText(apiConfig.getSynWaybillUrl());
			add(synWaybillUrlJText, null);
			add(createJLabel("uploadOrderUrl", 334, 98, 220, 18), null);
			uploadOrderUrlJText = createJTextField(432, 98, 160, 20, false);
			uploadOrderUrlJText.setText(apiConfig.getUploadOrderUrl());
			add(uploadOrderUrlJText, null);
		}

		jButton = createJButton("编辑", 436, 147, 71, 27);
		addListener(EDIT_URL, jButton);
		add(jButton, null);

		jButton = createJButton("保存", 520, 147, 71, 27);
		jButton.setEnabled(false);
		addListener(SAVE_URL, jButton);
		add(jButton, null);
		map.put(buttonKey + SAVE_URL, jButton);

		// 3:配置账号信息
		jlable = createJLabel("三: 配置API账号信息", 634, 10, 220, 18);
		jlable.setFont(new java.awt.Font("Dialog", 1, 15));
		add(jlable, null);
		add(createJLabel("用户列表", 634, 49, 220, 18), null);

		// 用户下拉框
		setUserCustomerCodeJComBox(createUserJComboBox(userConfigs, 734, 49,
				160, 20));
		addListenerToUserJcomboBox(getUserCustomerCodeJComBox());
		add(getUserCustomerCodeJComBox(), null);

		jButton = createJButton("添加", 900, 49, 60, 20);
		addListener(ADD_USER, jButton);
		add(jButton, null);

		if (!userConfigs.isEmpty()) {
			add(createJLabel("customerCode", 634, 98, 220, 18), null);
			customerCodeJText = createJTextField(734, 98, 160, 20, false);
			add(customerCodeJText, null);
			add(createJLabel("parternId", 634, 147, 220, 18), null);
			parternIdJText = createJTextField(734, 147, 160, 20, false);
			add(parternIdJText, null);
			add(createJLabel("clientId", 634, 194, 220, 18), null);
			clientIdJText = createJTextField(734, 194, 160, 20, false);
			add(clientIdJText, null);
		}

		jButton = createJButton("修改", 736, 243, 71, 27);
		addListener(EDIT_USER, jButton);
		add(jButton, null);
		jButton = createJButton("保存", 820, 243, 71, 27);
		jButton.setEnabled(false);
		addListener(SAVE_USER, jButton);
		add(jButton, null);
		map.put(buttonKey + SAVE_USER, jButton);

		// 监控同步面单，上传订单
		jlable = createJLabel("四: 同步面单与上传订单管理", 34, 380, 220, 18);
		jlable.setFont(new java.awt.Font("Dialog", 1, 15));
		add(jlable, null);
		synWaybillJLabel = createJLabel("面单同步状态：同步中......", 34, 419, 160, 18);
		add(synWaybillJLabel, null);
		synWaybillJButton = createJButton("停止", 220, 419, 71, 27);
		addListener(SYN_WAYBILL, synWaybillJButton);
		add(synWaybillJButton, null);
		uploadOrderJLabel = createJLabel("上传订单状态：上传中......", 34, 450, 160, 18);
		add(uploadOrderJLabel, null);
		uploadOrderJButton = createJButton("停止", 220, 450, 71, 27);
		addListener(UPLOAD_ORDER, uploadOrderJButton);
		add(uploadOrderJButton, null);

		// 显示面单数和订单数
		jlable = createJLabel("五:面单和订单数", 643, 380, 220, 18);
		jlable.setFont(new java.awt.Font("Dialog", 1, 15));
		add(jlable, null);

		faceBillJLabel = createJLabel("已拉取面单数", 643, 419, 220, 18);
		add(faceBillJLabel, null);

		faceBillNum = createJTextArea("1000", 1, 1, new Font("标楷体", Font.BOLD,
				17), 750, 419, 150, 25, false);
		add(faceBillNum, null);

		orderJLabel = createJLabel("待上传订单数", 643, 450, 220, 18);
		add(orderJLabel, null);

		orderNum = createJTextArea("1000", 1, 1,
				new Font("标楷体", Font.BOLD, 17), 750, 450, 150, 25, false);
		add(orderNum, null);

		// 退出动画
		exitJLabel = new DynGifLabel(Toolkit.getDefaultToolkit().getImage(
				"image/exit.gif"));
		exitJLabel.setBounds(300, 300, 468, 60);
		exitJLabel.setVisible(false);
		this.add(exitJLabel);
		map.put("exitJLabel", exitJLabel);

		// 版权
		add(createJLabel("版权声明:  本系统专用于圆通电子面单,copyright@yto.tld", 34, 520, 400,
				18), null);
		// 标题
		setTitle("圆通电子面单业务管理系统");
		/**
		 * 设置窗口默认关闭方式 默认点击将当前窗口隐藏 我们要设置默认的关闭方式，禁止它这样做
		 */
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// 退出时关闭进程(关闭windows任务中的进程)
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit(YtoGui.this);
			}
		});

		this.setVisible(true);
	}

	public void exit(JFrame frame) {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		int value = JOptionPane.showConfirmDialog(frame, "确认离开吗？");
		if (value == JOptionPane.YES_OPTION) {
			exitJLabel.setVisible(true);
			// 退出前， 先停止同步面单和上传订单线程
			/*System.out.println("state:"
					+ YtoMain.getSynWaybillThread().getState());*/
			YtoGui.setExitApp(true);
			/*if (YtoMain.getSynWaybillThread().getState() != Thread.State.RUNNABLE) {
				YtoMain.getSynWaybillThread().setStatus(1);
			}*/
		}
	}

	/**
	 * 初始化配置信息
	 */
	private void initConfig() {
		jdbcConfig = SqliteDao.queryJdbcConfig();
		apiConfig = SqliteDao.queryApiConfig();
		userConfigs = SqliteDao.queryUserConfig();
	}

	JComboBox createUserJComboBox(List<UserConfig> users, int x, int y,
			int width, int height) {
		JComboBox userJComBox = new JComboBox();
		UserConfig defaultUser = new UserConfig();
		defaultUser.setClientId("");
		userJComBox.addItem(defaultUser);
		for (UserConfig user : users) {
			userJComBox.addItem(user);
		}
		userJComBox.setBounds(x, y, width, height);
		return userJComBox;
	}

	private JComboBox createDbTypeJComboBox(List<JdbcConfig> jdbcConfigs,
			int x, int y, int width, int height) {
		JComboBox dbList = new JComboBox();
		for (JdbcConfig db : jdbcConfigs) {
			dbList.addItem(db.getType());
		}
		dbList.setBounds(x, y, width, height);
		return dbList;
	}

	// -----------------按钮事件监听器------------------------------------- //
	private void addListener(final int buttonId, JButton jButton) {
		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// db连接按扭事件
				if (buttonId == EDIT_DB) {
					JButton button = (JButton) map.get(buttonKey + SAVE_DB);
					if (dbNum == 1) {
						enableJTextFild(DB_ENABLED);
						dbNum = 0;
						button.setEnabled(true);
					} else if (dbNum == 0) {
						enableJTextFild(DB_VISIBLE);
						dbNum = 1;
						button.setEnabled(false);
					}
				} else if (buttonId == SAVE_DB) {
					saveDbInfo();
					dispose();
					setVisible(true);
				}
				// URL请求按扭事件
				else if (buttonId == EDIT_URL) {
					JButton button = (JButton) map.get(buttonKey + SAVE_URL);
					if (urlNum == 1) {
						enableJTextFild(URL_ENABLED);
						urlNum = 0;
						button.setEnabled(true);
					} else if (urlNum == 0) {
						enableJTextFild(URL_VISIBLE);
						urlNum = 1;
						button.setEnabled(false);
					}
				} else if (buttonId == SAVE_URL) {
					saveUrl();
					dispose();
					setVisible(true);
				}
				// User请求按扭事件
				else if (buttonId == EDIT_USER) {
					JButton button = (JButton) map.get(buttonKey + SAVE_USER);
					if (userNum == 1) {
						enableJTextFild(USER_ENABLED);
						userNum = 0;
						button.setEnabled(true);
					} else if (userNum == 0) {
						enableJTextFild(USER_VISIBLE);
						userNum = 1;
						button.setEnabled(false);
					}
				} else if (buttonId == ADD_USER) {
					AddUserGui.getInstance().setVisible(true);
				} else if (buttonId == SAVE_USER) {
					UserConfig userConfig = (UserConfig) getUserCustomerCodeJComBox()
							.getSelectedItem();
					updateUserInfo(userConfig);
					dispose();
					setVisible(true);
				}
				// 线程停止或者开启
				else if (buttonId == SYN_WAYBILL) {
					if (SYNWAYBILL_STATE == 2) {// 同步面单线程同步中，现在要停止
						YtoMain.getSynWaybillThread().stopThread();
						YtoGui.getInstance().synWaybillJLabel
								.setText("面单同步状态：已停止......");
						YtoGui.getInstance().synWaybillJButton.setText("启动");
						SYNWAYBILL_STATE = 1;// 已停止
					} else if (SYNWAYBILL_STATE == 1) {// 同步面单线程已停止，现在要启动
						YtoMain.getSynWaybillThread().startThread();
						SYNWAYBILL_STATE = 2;// 启动中
						YtoGui.getInstance().synWaybillJLabel
								.setText("面单同步状态：同步中......");
						YtoGui.getInstance().synWaybillJButton.setText("停止");
					}
				} else if (buttonId == UPLOAD_ORDER) {
					if (IS_UPLOAD_ORDER) {
						YtoMain.getUploadOrderThread().stopThread();
						IS_UPLOAD_ORDER = false;
						YtoGui.getInstance().uploadOrderJButton.setText("启动");
					} else {
						YtoMain.getUploadOrderThread().startThread();
						IS_UPLOAD_ORDER = true;
						YtoGui.getInstance().uploadOrderJButton.setText("停止");
					}
				}
			}
		});
	}

	// -----------------DB配置下拉框事件监听器------------------------------------- //
	private void addListenerToDbTypeJcomboBox(final JComboBox dbTypeJComBox) {
		dbTypeJComBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dbType = dbTypeJComBox.getSelectedItem().toString();
				dbType(dbType);
			}
		});
	}

	// -----------------User配置下拉框事件监听器------------------------------------- //
	public void addListenerToUserJcomboBox(final JComboBox userJComBox) {
		userJComBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserConfig userConfig = (UserConfig) userJComBox
						.getSelectedItem();

				showUserInfo(userConfig);
			}
		});
	}

	// -----------------文本框编辑状态-------------------------------------
	protected void enableJTextFild(int status) {
		if (status == DB_ENABLED) {
			usernameJText.setEditable(true);
			passwordJText.setEditable(true);
			databaseJText.setEditable(true);
			portJText.setEditable(true);
			urlJText.setEditable(true);
		} else if (status == DB_VISIBLE) {
			usernameJText.setEditable(false);
			passwordJText.setEditable(false);
			databaseJText.setEditable(false);
			portJText.setEditable(false);
			urlJText.setEditable(false);
		} else if (status == URL_ENABLED) {
			uploadOrderUrlJText.setEditable(true);
			synWaybillUrlJText.setEditable(true);
		} else if (status == URL_VISIBLE) {
			uploadOrderUrlJText.setEditable(false);
			synWaybillUrlJText.setEditable(false);
		} else if (status == USER_ENABLED) {
			customerCodeJText.setEditable(isEnabled());
			parternIdJText.setEditable(true);
			clientIdJText.setEditable(true);
		} else if (status == USER_VISIBLE) {
			customerCodeJText.setEditable(false);
			parternIdJText.setEditable(false);
			clientIdJText.setEditable(false);
		}
	}

	// -----------------事件方法调用--------------------------------- //
	protected void dbType(String dbType) {
		try {
			JdbcConfig jdbcInfo = SqliteDao.selectByDbType(dbType);
			if (jdbcInfo != null) {
				usernameJText.setText(jdbcInfo.getUsername());
				passwordJText.setText(jdbcInfo.getPassword());
				databaseJText.setText(jdbcInfo.getDbName());
				portJText.setText(jdbcInfo.getPort());
				urlJText.setText(jdbcInfo.getIp());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void saveDbInfo() {
		JdbcConfig jdbcInfo = new JdbcConfig();
		jdbcInfo.setJdbcId(1);
		jdbcInfo.setType(dbTypeJText.getSelectedItem().toString());
		jdbcInfo.setDbName("");
		jdbcInfo.setIp(urlJText.getText());
		jdbcInfo.setPort(portJText.getText());
		jdbcInfo.setUsername(usernameJText.getText());
		jdbcInfo.setPassword(passwordJText.getText());
		jdbcInfo.setDbName(databaseJText.getText());
		boolean status = SqliteDao.updateJdbcConfig(jdbcInfo);
	}

	protected void saveUrl() {
		String num = SqliteDao.countApiConfig();
		Boolean status = false;
		if (num.equals("1")) {
			ApiConfig apiConfig = new ApiConfig();
			apiConfig.setSynWaybillUrl(synWaybillUrlJText.getText());
			apiConfig.setUploadOrderUrl(uploadOrderUrlJText.getText());
			status = SqliteDao.updateApiConfig(apiConfig);
		} else {
			ApiConfig apiConfig = new ApiConfig();
			apiConfig.setSynWaybillUrl(synWaybillUrlJText.getText());
			apiConfig.setUploadOrderUrl(uploadOrderUrlJText.getText());
			status = SqliteDao.insertApiConfig(apiConfig);
		}
	}

	protected void showUserInfo(UserConfig userConfig) {
		if (userConfig != null) {
			customerCodeJText.setText(userConfig.getCustomerCode());
			parternIdJText.setText(userConfig.getParternId());
			clientIdJText.setText(userConfig.getClientId());
		}
	}

	protected void updateUserInfo(UserConfig userConfig) {
		userConfig.setCustomerCode(customerCodeJText.getText());
		userConfig.setParternId(parternIdJText.getText());
		userConfig.setClientId(clientIdJText.getText());
		boolean status = SqliteDao.updateUserInfoByUserId(userConfig);
	}

	public List<UserConfig> getUserConfigs() {
		return userConfigs;
	}

	public void setUserConfigs(List<UserConfig> userConfigs) {
		YtoGui.userConfigs = userConfigs;
	}

	public JdbcConfig getJdbcConfig() {
		return jdbcConfig;
	}

	public ApiConfig getApiConfig() {
		return apiConfig;
	}

	public void setUserCustomerCodeJComBox(JComboBox userCustomerCodeJComBox) {
		YtoGui.userCustomerCodeJComBox = userCustomerCodeJComBox;
	}

	public JComboBox getUserCustomerCodeJComBox() {
		return userCustomerCodeJComBox;
	}

	public static void setExitApp(boolean exitApp) {
		YtoGui.exitApp = exitApp;
	}

	public static boolean isExitApp() {
		return exitApp;
	}

	public static JTextArea getFaceBillNum() {
		return faceBillNum;
	}

	public static void setFaceBillNum(JTextArea faceBillNum) {
		YtoGui.faceBillNum = faceBillNum;
	}

	public static JTextArea getOrderNum() {
		return orderNum;
	}

	public static void setOrderNum(JTextArea orderNum) {
		YtoGui.orderNum = orderNum;
	}

}
