package net.ytoec.kernel.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取Mysql的connection ，以便于使用mysql特殊的Statement功能
 */
public class MySQLJDBCUtil {

	private String url= "jdbc:mysql://localhost:3306/test?zeroDateTimeBehavior=convertToNull";
	private String username = "root";
	private String password = "root"; 
	
	private static final Logger logger = LoggerFactory.getLogger(MySQLJDBCUtil.class);
	
	// 构造私有实例
	private static MySQLJDBCUtil instance = null;

	// 注册驱动
//	static 
//	{
//		try
//		{
//		    Class.forName("com.mysql.jdbc.Driver").newInstance();
//		} 
//		catch (Exception e) {
//			throw new ExceptionInInitializerError(e);
//		}
//	}
	
	@Resource(name = "dataSource")
    private DataSource dataSource;
	
	// 构造函数私有 
	private MySQLJDBCUtil() 
	{
		
	}

	public static MySQLJDBCUtil getInstance() {
		if (instance == null) {
			instance = new MySQLJDBCUtil();
		}
		return instance;
	}

	// 获取连接
	public Connection getConnection() {
		Connection con = null;
		logger.error("获取Connection开始------");
		{
			try
			{
				con =  DriverManager.getConnection(url, username, password);//备注：这种方式连不上正式数据库，原因不明
				logger.error("获取Connection成功------url："+url+";username:"+username+";password:"+password);
			}
			catch(Exception e)
			{
				logger.error("获取Connection失败------url："+url+";username:"+username+";password:"+password);
			}
//			finally
//			{
//				try {					
//					con = dataSource.getConnection();
//					logger.error("获取Connection成功------dataSource");
//				} catch (SQLException e) {					
//					logger.error("获取Connection失败------dataSource");
//					e.printStackTrace();
//				}				
//			}
		}
		
		logger.error("获取Connection结束------");
		return con;
	}

	// 释放资源
	public static void free(Statement stmt, Connection conn) {
		try 
		{
			if (stmt != null) {
				stmt.close();
			}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    } 
	   finally 
	   {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {				
				e.printStackTrace();
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
	
	public DataSource getDataSource() {
	    return dataSource;
    }

	public void setDataSource(DataSource dataSource) {
	   this.dataSource = dataSource;
	}
}
