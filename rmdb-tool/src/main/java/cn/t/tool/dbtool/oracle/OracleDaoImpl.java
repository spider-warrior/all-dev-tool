package cn.t.tool.dbtool.oracle;

import cn.t.tool.dbtool.common.DbDao;
import cn.t.tool.dbtool.common.ResultHolder;
import cn.t.tool.dbtool.common.SqlExecution;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleDaoImpl implements DbDao {

    private final SqlExecution sqlExecution;

    private static final String QUERY_PRIMARY_KEY_SQL = "select a.column_name from user_cons_columns a, user_constraints b where a.constraint_name = b.constraint_name  and b.constraint_type = 'P' and a.table_name = ?";
    private static final String CHECK_TABLE_EXIST_SQL = "select count(table_name) from user_tables where table_name = ?";
    private static final String CHECK_TABLE_COLUMN_EXIST_SQL = "select count(1) from user_tab_cols where table_name = ? and column_name = ?";
    private static final String QUERY_ALL_TABLE_NAME_SQL = "select table_name from user_tables";
    private static final String QUERY_ALL_VIEW_NAME_SQL = "select view_name from user_views";


    public long queryMaxId(String tableName, String idColumn) throws SQLException, ClassNotFoundException {
        StringBuilder querySql = new StringBuilder("select max(");
        querySql.append(idColumn.toUpperCase());
        querySql.append(")");
        querySql.append(" from ");
        querySql.append(tableName);
        ResultHolder<Long> resultHolder = new ResultHolder<>();
        sqlExecution.executeSql(querySql.toString(), rs -> {
            if (rs.next()) {
                resultHolder.setResult(rs.getLong(1));
            } else {
                resultHolder.setResult(0L);
            }
        });
        return resultHolder.getResult();
    }

    public String getPrimaryKeyName(String tableName) throws SQLException, ClassNotFoundException {
        Map<Integer, String> param = new HashMap<>();
        param.put(1, tableName.toUpperCase());
        ResultHolder<String> resultHolder = new ResultHolder<>();
        sqlExecution.executeSql(QUERY_PRIMARY_KEY_SQL, param, rs -> {
            if (rs.next()) {
                resultHolder.setResult(rs.getString(1));
            }
        });
        return resultHolder.getResult();

    }

    public boolean checkTableExist(String tableName) throws SQLException, ClassNotFoundException {
        Map<Integer, String> param = new HashMap<>();
        param.put(1, tableName.toUpperCase());
        ResultHolder<Boolean> resultHolder = new ResultHolder<>();
        sqlExecution.executeSql(CHECK_TABLE_EXIST_SQL, param, rs -> {
            if (rs.next()) {
                resultHolder.setResult(rs.getInt(1) != 0);
            } else {
                resultHolder.setResult(Boolean.FALSE);
            }
        });
        return resultHolder.getResult();
    }

    public boolean checkTableColumnExist(String tableName, String columnName) throws SQLException, ClassNotFoundException {
        Map<Integer, String> param = new HashMap<>();
        param.put(1, tableName.toUpperCase());
        param.put(2, columnName.toUpperCase());
        ResultHolder<Boolean> resultHolder = new ResultHolder<>();
        sqlExecution.executeSql(CHECK_TABLE_COLUMN_EXIST_SQL, param, rs -> {
            if (rs.next()) {
                resultHolder.setResult(rs.getInt(1) != 0);
            } else {
                resultHolder.setResult(Boolean.FALSE);
            }
        });
        return resultHolder.getResult();
    }

    @Override
    public List<String> queryAllTables() throws SQLException, ClassNotFoundException {
        List<String> tableNames = new ArrayList<>();
        sqlExecution.executeSql(QUERY_ALL_TABLE_NAME_SQL, rs -> {
            while (rs.next()) {
                tableNames.add(rs.getString(1));
            }
        });
        return tableNames;
    }

    @Override
    public List<String> queryAllViews() throws SQLException, ClassNotFoundException {
        List<String> viewNames = new ArrayList<>();
        sqlExecution.executeSql(QUERY_ALL_VIEW_NAME_SQL, rs -> {
            while (rs.next()) {
                viewNames.add(rs.getString(1));
            }
        });
        return viewNames;
    }


    public OracleDaoImpl(SqlExecution sqlExecution) {
        this.sqlExecution = sqlExecution;
    }
}
