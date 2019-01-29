package cn.t.tool.rmdbtool;


import cn.t.tool.rmdbtool.common.DbConfiguration;
import cn.t.tool.rmdbtool.common.DbDao;
import cn.t.tool.rmdbtool.common.SqlExecution;
import cn.t.tool.rmdbtool.oracle.OracleDaoImpl;

import java.sql.SQLException;
import java.util.List;

public class OracleHelper {

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
}
