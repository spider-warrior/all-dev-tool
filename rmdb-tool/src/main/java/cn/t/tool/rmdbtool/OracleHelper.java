package cn.t.tool.rmdbtool;


import cn.t.tool.rmdbtool.common.DbConfiguration;
import cn.t.tool.rmdbtool.common.DbDao;
import cn.t.tool.rmdbtool.common.SqlExecution;
import cn.t.tool.rmdbtool.common.constraint.Constraint;
import cn.t.tool.rmdbtool.common.constraint.ConstraintQueryParam;
import cn.t.tool.rmdbtool.common.constraint.ConstraintType;
import cn.t.tool.rmdbtool.oracle.OracleDaoImpl;
import cn.t.util.common.CollectionUtil;
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

    public Constraint getPrimaryKeyConstraint(String tableName) throws SQLException, ClassNotFoundException {
        ConstraintQueryParam constraintQueryParam = new ConstraintQueryParam();
        constraintQueryParam.setTableName(tableName);
        constraintQueryParam.setType(ConstraintType.PRIMARY_KEY.value);
        List<Constraint> constraintList = dbDao.queryConstraint(constraintQueryParam);
        if(CollectionUtil.isEmpty(constraintList)) {
            return null;
        } else {
            return constraintList.get(0);
        }
    }

    public List<Constraint> getTableConstraintList(String tableName) throws SQLException, ClassNotFoundException {
        ConstraintQueryParam constraintQueryParam = new ConstraintQueryParam();
        constraintQueryParam.setTableName(tableName);
        return dbDao.queryConstraint(constraintQueryParam);
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
