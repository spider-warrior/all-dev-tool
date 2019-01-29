package cn.t.tool.dbtool;


import cn.t.tool.dbtool.common.DbConfiguration;
import cn.t.tool.dbtool.common.DbDao;
import cn.t.tool.dbtool.common.SqlExecution;
import cn.t.tool.dbtool.oracle.OracleDaoImpl;

import java.sql.SQLException;
import java.util.List;

public class OracleHelper {

    private DbDao dbDao;

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

    public OracleHelper(DbConfiguration configuration) {
        dbDao = new OracleDaoImpl(new SqlExecution(configuration));
    }
}
