package cn.t.tool.rmdbtool.oracle;

import cn.t.tool.rmdbtool.common.DbDao;
import cn.t.tool.rmdbtool.common.ResultHolder;
import cn.t.tool.rmdbtool.common.SqlExecution;
import cn.t.tool.rmdbtool.common.constraint.Constraint;
import cn.t.tool.rmdbtool.common.constraint.ConstraintQueryParam;
import cn.t.util.common.StringUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleDaoImpl implements DbDao {

    private final SqlExecution sqlExecution;

    private static final String CHECK_TABLE_EXIST_SQL = "select count(table_name) from user_tables where table_name = ?";
    private static final String CHECK_TABLE_COLUMN_EXIST_SQL = "select count(1) from user_tab_cols where table_name = ? and column_name = ?";
    private static final String QUERY_ALL_TABLE_NAME_SQL = "select table_name from user_tables";
    private static final String QUERY_ALL_VIEW_NAME_SQL = "select view_name from user_views";
    private static final String QUERY_CONSTRAINTS_SQL = "select a.*, b.constraint_type from user_cons_columns a, user_constraints b where a.constraint_name = b.constraint_name and b.status = 'ENABLED'";


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

    @Override
    public List<Constraint> queryConstraint(ConstraintQueryParam queryParam) throws SQLException, ClassNotFoundException {
        StringBuilder builder = new StringBuilder(QUERY_CONSTRAINTS_SQL);
        Map<Integer, String> param = new HashMap<>();
        if(queryParam != null) {
            int index = 1;
            if(!StringUtil.isEmpty(queryParam.getTableName())) {
                builder.append(" and a.table_name = ?");
                param.put(index++, queryParam.getTableName().toUpperCase());
            }
            if(!StringUtil.isEmpty(queryParam.getColumnName())) {
                builder.append(" and a.column_name = ?");
                param.put(index++, queryParam.getColumnName().toUpperCase());
            }
            if(!StringUtil.isEmpty(queryParam.getType())) {
                builder.append(" and b.constraint_type = ?");
                param.put(index, queryParam.getType().toUpperCase());
            }
        }
        List<Constraint> constraintList = new ArrayList<>();
        sqlExecution.executeSql(builder.toString(), param, rs -> {
            while (rs.next()) {
                Constraint constraint = new Constraint();
                constraint.setOwner(rs.getString(1));
                constraint.setConstraintName(rs.getString(2));
                constraint.setTableName(rs.getString(3));
                constraint.setColumnName(rs.getString(4));
                constraint.setPosition(rs.getString(5));
                constraint.setType(rs.getString(6));
                constraintList.add(constraint);
            }
        });
        return constraintList;
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

    @Override
    public String queryCreateTableStatement(String tableName) throws SQLException, ClassNotFoundException {
        String prepareSql =
            "BEGIN " +
            "DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM,'STORAGE', true);" +
            "DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM,'SEGMENT_ATTRIBUTES', false);" +
            "DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM,'SQLTERMINATOR', true);" +
            "DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM,'PRETTY', true);" +
            "DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM,'TABLESPACE', true);" +
            "END;";
        Map<Integer, String> param = new HashMap<>();
        param.put(1, tableName.toUpperCase());
        ResultHolder<String> resultHolder = new ResultHolder<>();
        sqlExecution.executeSql("SELECT DBMS_METADATA.GET_DDL('TABLE', ?) FROM DUAL", param, rs -> {
            if (rs.next()) {
                resultHolder.setResult(rs.getString(1));
            }
        }, prepareSql);
        if(resultHolder.getResult() != null) {
            StringBuilder commentBuilder = new StringBuilder();
//            sqlExecution.executeSql("SELECT TABLE_NAME, COLUMN_NAME, COMMENTS FROM USER_COL_COMMENTS WHERE TABLE_NAME = ?", param, rs -> {
//                while (rs.next()) {
//                    String tName = rs.getString(1);
//                    String cName = rs.getString(2);
//                    String comment = rs.getString(3);
//                    if(!StringUtil.isEmpty(comment)) {
//                        commentBuilder.append("COMMENT ON COLUMN ");
//                        commentBuilder.append(tName).append(".").append(cName).append(" ");
//                        commentBuilder.append("IS '").append(comment).append("';");
//                        commentBuilder.append("\n");
//                    }
//                }
//            });
            if(commentBuilder.length() > 0) {
                return resultHolder.getResult().concat("\n").concat(commentBuilder.toString());
            } else {
                return resultHolder.getResult();
            }
        } else {
            return null;
        }
    }

    public OracleDaoImpl(SqlExecution sqlExecution) {
        this.sqlExecution = sqlExecution;
    }
}
