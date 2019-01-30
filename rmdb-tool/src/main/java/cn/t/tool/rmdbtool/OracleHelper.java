package cn.t.tool.rmdbtool;


import cn.t.tool.rmdbtool.common.DbConfiguration;
import cn.t.tool.rmdbtool.common.DbDao;
import cn.t.tool.rmdbtool.common.SqlExecution;
import cn.t.tool.rmdbtool.oracle.OracleDaoImpl;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class OracleHelper {

    private static final Logger logger = LoggerFactory.getLogger(OracleHelper.class);

    private DbDao dbDao;
    private final String dbName;


    public String getPrimaryKeyName(String tableName) throws SQLException, ClassNotFoundException {
        return dbDao.getPrimaryKeyName(tableName);
    }

    public boolean checkTableExist(String tableName) throws SQLException, ClassNotFoundException {
        return dbDao.checkTableExist(tableName);
    }

    public boolean checkTableColumnExist(String tableName, String columnName) throws SQLException, ClassNotFoundException {
        return dbDao.checkTableColumnExist(tableName, columnName);
    }

    public List<String> queryAllTables() throws SQLException, ClassNotFoundException {
        return dbDao.queryAllTables();
    }

    public List<String> queryAllViews() throws SQLException, ClassNotFoundException {
        return dbDao.queryAllViews();
    }

    public String getDbName() {
        return dbName;
    }

    public long queryMaxId(String tableName, String idColumn) throws SQLException, ClassNotFoundException {
        return dbDao.queryMaxId(tableName, idColumn);
    }

    public OracleHelper(DbConfiguration configuration) {
        SqlExecution sqlExecution = new SqlExecution(configuration);
        dbDao = new OracleDaoImpl(sqlExecution);
        dbName = sqlExecution.getJdbcHelper().getDbName();
    }

    public OracleHelper() {
        this(tryOracleConfiguration());
    }

    private static DbConfiguration tryOracleConfiguration() {
        Properties properties = new Properties();
        try (
            InputStream is = FileUtil.getResourceInputStream(OracleHelper.class, "/oracle.properties")
        ) {
            if(is == null) {
                logger.error("oracle数据库配置文件未找: {}", "oracle.properties");
            } else {
                properties.load(is);
            }
        } catch (IOException e) {
            logger.error("", e);
        }
        DbConfiguration dbConfiguration = new DbConfiguration();
        dbConfiguration.setUsername(properties.getProperty("username"));
        dbConfiguration.setPassword(properties.getProperty("password"));
        dbConfiguration.setJdbcUrl(properties.getProperty("jdbcUrl"));
        dbConfiguration.setDriverName(properties.getProperty("driverName"));
        return dbConfiguration;
    }
}
