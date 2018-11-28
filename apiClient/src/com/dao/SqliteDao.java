package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.model.ApiConfig;
import com.model.JdbcConfig;
import com.model.UserConfig;

/**
 * sqlite操作工具类
 * 
 * @author huangtianfu
 * 
 */
public class SqliteDao {

	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB = "yto.db";
	private static Connection conn;

	public static void main(String[] args) {
		SqliteDao.createDb();

		// 添加
		JdbcConfig jdbcConfig = new JdbcConfig();
		jdbcConfig.setJdbcId(1);
		jdbcConfig.setDriver("com.mysql.jdbc.Driver");
		jdbcConfig.setDbName("surfacebill");
		jdbcConfig.setIp("localhost");
		jdbcConfig.setPort("3306");
		jdbcConfig.setUsername("root");
		jdbcConfig.setPassword("mysql");
		jdbcConfig
				.setUrl("jdbc:mysql://localhost:3306/surfacebill?characterEncoding=utf-8&autoReconnect=true");
		jdbcConfig.setType("mysql");

		SqliteDao.insertJdbcConfig(jdbcConfig);
		ApiConfig apiConfig = new ApiConfig();
		apiConfig.setApiId(1);
		apiConfig
				.setSynWaybillUrl("http://test.eccore.ytoxl.com/service_1/api!synWaybill.action");
		apiConfig
				.setUploadOrderUrl("http://test.eccore.ytoxl.com/service_1/CommonOrderServlet.action");
		SqliteDao.insertApiConfig(apiConfig);
		UserConfig userConfig = new UserConfig();
		userConfig.setUserId(1);
		userConfig.setCustomerCode("K22002902");
		userConfig.setParternId("36s8Rg3C");
		userConfig.setClientId("K22002902");
		SqliteDao.insertUserConfig(userConfig);
		SqliteDao.queryJdbcConfig();
		SqliteDao.queryApiConfig();
		SqliteDao.queryUserConfig();

		System.out.println("upload........");
	}

	public static boolean updateJdbcConfig(JdbcConfig jdbcConfig) {
		boolean success = false;
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			String url = "jdbc:" + jdbcConfig.getType() + "://"
					+ jdbcConfig.getIp() + ":" + jdbcConfig.getPort() + "/"
					+ jdbcConfig.getDbName()
					+ "?characterEncoding=utf-8&autoReconnect=true";
			String sql = "update jdbc set db_name='" + jdbcConfig.getDbName()
					+ "',ip='" + jdbcConfig.getIp() + "',port='"
					+ jdbcConfig.getPort() + "',username='"
					+ jdbcConfig.getUsername() + "',password='"
					+ jdbcConfig.getPassword() + "',type='"
					+ jdbcConfig.getType() + "',url='" + url
					+ "'  where jdbc_id=1";
			conn.setAutoCommit(false);
			stmt.execute(sql);
			conn.setAutoCommit(true);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return success;
	}

	public static JdbcConfig selectByDbType(String DBType) throws Exception {
		JdbcConfig jdbcConfig = new JdbcConfig();
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			String sql = "select * from jdbc where type='" + DBType + "'";
			conn.setAutoCommit(false);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				jdbcConfig.setDbName(rs.getString("db_name"));
				jdbcConfig.setIp(rs.getString("ip"));
				jdbcConfig.setUsername(rs.getString("username"));
				jdbcConfig.setPassword(rs.getString("password"));
				jdbcConfig.setPort(rs.getString("port"));
				jdbcConfig.setUrl(rs.getString("url"));
				jdbcConfig.setDriver(rs.getString("driver"));
				jdbcConfig.setType(rs.getString("type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jdbcConfig;
	}

	public static boolean updateApiConfig(ApiConfig apiConfig) {
		boolean success = false;
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			String sql = "update api set syn_waybill_url='"
					+ apiConfig.getSynWaybillUrl() + "',upload_order_url='"
					+ apiConfig.getUploadOrderUrl() + "'" + " where api_id=1";
			conn.setAutoCommit(false);
			stmt.execute(sql);
			conn.setAutoCommit(true);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return success;
	}

	public static boolean updateUserConfig(UserConfig userConfig) {
		boolean success = false;
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			String sql = "update user set customer_code='"
					+ userConfig.getCustomerCode() + "',partern_id='"
					+ userConfig.getParternId() + "',client_id='"
					+ userConfig.getClientId() + "'" + " where customer_code='"
					+ userConfig.getCustomerCode() + "'";
			conn.setAutoCommit(false);
			stmt.execute(sql);
			conn.setAutoCommit(true);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return success;
	}

	public static boolean insertJdbcConfig(JdbcConfig jdbcConfig) {
		boolean success = false;
		try {
			initConnection();
			PreparedStatement prep;
			prep = conn
					.prepareStatement("insert into jdbc values (?,?,?,?,?,?,?,?,?);");

			prep.setInt(1, jdbcConfig.getJdbcId());
			prep.setString(2, jdbcConfig.getDbName());
			prep.setString(3, jdbcConfig.getIp());
			prep.setString(4, jdbcConfig.getPort());
			prep.setString(5, jdbcConfig.getUsername());
			prep.setString(6, jdbcConfig.getPassword());
			prep.setString(7, jdbcConfig.getDriver());
			prep.setString(8, jdbcConfig.getUrl());
			prep.setString(9, jdbcConfig.getType());
			prep.addBatch();

			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return success;
	}

	public static boolean insertApiConfig(ApiConfig apiConfig) {
		boolean success = false;
		try {
			initConnection();
			PreparedStatement prep;
			prep = conn.prepareStatement("insert into api values (?,?,?);");

			prep.setInt(1, apiConfig.getApiId());
			prep.setString(2, apiConfig.getSynWaybillUrl());
			prep.setString(3, apiConfig.getUploadOrderUrl());
			prep.addBatch();

			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return success;
	}

	public static String countApiConfig() {
		String num = "0";
		try {
			initConnection();
			PreparedStatement prep;
			prep = conn.prepareStatement("select count(*) from api;");
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				num = rs.getString(1);
			}

			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return num;
	}

	public static boolean insertUserConfig(UserConfig userConfig) {
		boolean success = false;
		try {
			initConnection();
			PreparedStatement prep;
			prep = conn
					.prepareStatement("insert into user(customer_code,partern_id,client_id) values (?,?,?);");

			prep.setString(1, userConfig.getCustomerCode());
			prep.setString(2, userConfig.getParternId());
			prep.setString(3, userConfig.getClientId());
			prep.addBatch();

			conn.setAutoCommit(false);
			prep.executeBatch();
			conn.setAutoCommit(true);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * 查询jdbc配置
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static JdbcConfig queryJdbcConfig() {
		JdbcConfig jdbcConfig = null;
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from jdbc;");
			while (rs != null && rs.next()) {
				jdbcConfig = new JdbcConfig();
				jdbcConfig.setDbName(rs.getString("db_name"));
				jdbcConfig.setIp(rs.getString("ip"));
				jdbcConfig.setPassword(rs.getString("password"));
				jdbcConfig.setPort(rs.getString("port"));
				jdbcConfig.setUsername(rs.getString("username"));
				jdbcConfig.setPassword(rs.getString("password"));
				jdbcConfig.setDriver(rs.getString("driver"));
				jdbcConfig.setUrl(rs.getString("url"));
				jdbcConfig.setType(rs.getString("type"));
				System.out.println("name = " + jdbcConfig.getDbName());
				System.out.println("ip = " + jdbcConfig.getIp());
			}
			rs.close();
			rs = null;
			closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return jdbcConfig;
	}

	public static ApiConfig queryApiConfig() {
		ApiConfig apiConfig = new ApiConfig();
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from api;");
			while (rs.next()) {
				apiConfig.setSynWaybillUrl(rs.getString("syn_waybill_url"));
				apiConfig.setUploadOrderUrl(rs.getString("upload_order_url"));
				System.out.println("synWaybillUrl = "
						+ apiConfig.getSynWaybillUrl());
				System.out.println("uploadOrderUrl = "
						+ apiConfig.getUploadOrderUrl());
			}
			rs.close();
			rs = null;
			closeConnection();
		} catch (SQLException e) {
			apiConfig.setSynWaybillUrl(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			apiConfig.setSynWaybillUrl(e.getMessage());
			e.printStackTrace();
		}
		return apiConfig;
	}

	public static List<UserConfig> queryUserConfig() {
		List<UserConfig> userConfigs = new ArrayList<UserConfig>();
		UserConfig userConfig;
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from user;");
			while (rs != null && rs.next()) {
				userConfig = new UserConfig();
				userConfig.setUserId(rs.getInt("user_id"));
				userConfig.setCustomerCode(rs.getString("customer_code"));
				userConfig.setParternId(rs.getString("partern_id"));
				userConfig.setClientId(rs.getString("client_id"));
				userConfigs.add(userConfig);
				System.out.println("customerCode = "
						+ userConfig.getCustomerCode());
				System.out.println("parternId = " + userConfig.getParternId());
			}
			rs.close();
			rs = null;
			closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return userConfigs;
	}

	public static UserConfig queryUserInfoByUserId(int userId) {
		UserConfig userConfig = new UserConfig();
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("select * from user where user_id=" + userId
							+ ";");
			while (rs != null && rs.next()) {
				userConfig.setUserId(userId);
				userConfig.setCustomerCode(rs.getString("customer_code"));
				userConfig.setParternId(rs.getString("partern_id"));
				userConfig.setClientId(rs.getString("client_id"));
			}
			rs.close();
			rs = null;
			closeConnection();
		} catch (SQLException e) {
			userConfig.setCustomerCode(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			userConfig.setCustomerCode(e.getMessage());
			e.printStackTrace();
		}
		return userConfig;
	}

	public static boolean updateUserInfoByUserId(UserConfig userConfig) {
		boolean success = false;
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			String sql = "update user set customer_code='"
					+ userConfig.getCustomerCode() + "',partern_id='"
					+ userConfig.getParternId() + "',client_id='"
					+ userConfig.getClientId() + "' where user_id="
					+ userConfig.getUserId();
			conn.setAutoCommit(false);
			stmt.execute(sql);
			conn.setAutoCommit(true);
			success = true;
			closeConnection();
		} catch (SQLException e) {
			userConfig.setCustomerCode(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			userConfig.setCustomerCode(e.getMessage());
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * 创建数据库
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void createDb() {
		try {
			initConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("drop table if exists jdbc;");
			stmt.executeUpdate("drop table if exists api;");
			stmt.executeUpdate("drop table if exists user;");
			stmt.executeUpdate("create table jdbc (jdbc_id, db_name, ip, port, username, password, driver, url, type);");
			stmt.executeUpdate("create table api (api_id, syn_waybill_url, upload_order_url);");
			stmt.executeUpdate("create table user (user_id integer primary key autoincrement, customer_code, partern_id, client_id);");
			closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据库连接
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void initConnection() throws SQLException,
			ClassNotFoundException {
		if (conn == null) {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection("jdbc:sqlite:" + DB);
		}
	}

	/**
	 * 关闭数据库
	 * 
	 * @throws SQLException
	 */
	private static void closeConnection() throws SQLException {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

}
