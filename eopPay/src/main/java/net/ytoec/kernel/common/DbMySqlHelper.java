package net.ytoec.kernel.common;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.C3P0ProxyStatement;

/**
 * mysql load data
 * @author guolongchao
 */
public final class DbMySqlHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(DbMySqlHelper.class);
	
	private static final String INFILE_MUTATOR_METHOD = "setLocalInfileInputStream";

	
    /**
     * 使用mysql load data 将文本文件导入到目标表targetTable
     * @param dataFile     文本文件
     * @param targetTable  表名
     * @param columns      入库的字段
     */
    public static final void loadDataInfile(final File dataFile, final String targetTable, final String... columns) 
    {
       loadDataInfile(dataFile, targetTable, ',', '\"', '\n', columns);      
    }
    
    /**
     * 第一步：将目标表targetTable里的数据全部删除
     * 第二步：使用mysql load data 导入到目标表targetTable
     * @param dataFile 数据文件
     * @param targetTable 表名
     * @param fieldTerminated 每行数据字段的分隔符
     * @param enclosed 字段的包围字符,例如"12","data"
     * @param lineTerminated每行数据的分隔符
     * @param columns 入库的字段
     */
    public static final void loadDataInfile(final File dataFile, final String targetTable, final Character fieldTerminated, final Character enclosed,
           final Character lineTerminated, final String... columns) {

       if (dataFile == null)
           return;

       final StringBuilder buf = new StringBuilder(32);
       buf.append("LOAD DATA LOCAL INFILE \"");
       buf.append(dataFile.getAbsolutePath().replace("\\", "\\\\"));
       buf.append("\" INTO TABLE ");
       buf.append(targetTable);
       if (null != fieldTerminated)
           buf.append(" FIELDS TERMINATED BY '").append(fieldTerminated).append('\'');
       if (null != enclosed)
           buf.append(" ENCLOSED BY '").append(enclosed).append('\'');
       if (null != lineTerminated)
           buf.append(" LINES TERMINATED BY '").append(lineTerminated).append('\'');
       if (null != columns && columns.length > 0) {
           buf.append('(').append(StringUtils.join(columns, ",")).append(')');
       }
 
       Connection conn = null;
       Statement stmt = null;
       try {
    	   
    	   logger.error("mysql load data start-------");
           conn = MySQLJDBCUtil.getInstance().getConnection();
           stmt = conn.createStatement();
           stmt.execute("delete from "+targetTable);
           stmt.execute(buf.toString());
           logger.error("mysql load data end -------");
           
       } catch (final Exception e) {
           e.printStackTrace();//for debug
       } finally {
    	   MySQLJDBCUtil.free(stmt, conn);
       }
    }
    

    private void prepareStatement(Statement statement, InputStream inputStream) throws NoSuchMethodException, InvocationTargetException, SQLException, IllegalAccessException
    {
        if (statement instanceof C3P0ProxyStatement) {
            Method m = com.mysql.jdbc.Statement.class.getMethod(INFILE_MUTATOR_METHOD, new Class[]{InputStream.class});
            C3P0ProxyStatement proxyStatement = (C3P0ProxyStatement) statement;
            proxyStatement.rawStatementOperation(m, C3P0ProxyStatement.RAW_STATEMENT, new Object[]{inputStream});
        } else if (statement instanceof com.mysql.jdbc.Statement) {
            com.mysql.jdbc.Statement mysqlStatement = (com.mysql.jdbc.Statement) statement;
            mysqlStatement.setLocalInfileInputStream(inputStream);
        }
    }
}
