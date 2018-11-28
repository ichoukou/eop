// 单例方式

package net.ytoec.kernel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库操作公共类 单例模式 虚拟机里只存在一个这样的实例 通过getInstance方式获取对象
 * 
 * @author MGL
 */
public class OrderJDBCUtilSingle {

	private String url;
	private String username;
	private String password;

	// 构造私有实例
	private static OrderJDBCUtilSingle instance = null;

	// 注册驱动
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	// 构造函数私有 不允许外部调用构造
	private OrderJDBCUtilSingle() {
	}

	public synchronized static OrderJDBCUtilSingle getInstance() {
		// //延迟加载
		// if (instance == null) {
		// //加锁 防止线程并发
		// synchronized (JDBCUtilSingle.class) {
		// 必须有的判断
		if (instance == null) {
			instance = new OrderJDBCUtilSingle();
		}
		// }
		// }
		return instance;
	}

	// 获取连接
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

	// 释放资源
	public void free(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {

		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {

			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {

				}
			}
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
