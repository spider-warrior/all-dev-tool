package cn.t.tool.dbtool.common;

import java.sql.*;
import java.util.Map;

public class SqlExecution {

    private final JdbcHelper jdbcHelper;

    public void executeSql(String sql, ResultSetCallback callback) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = jdbcHelper.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            callback.callback(rs);
        } finally {
            jdbcHelper.release(null, stmt, rs);
        }
    }

    public void executeSql(String sql, Map<Integer, String> param, ResultSetCallback callback) throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection conn = jdbcHelper.getConnection();
            stmt = conn.prepareStatement(sql);
            if(param != null) {
                for(Map.Entry<Integer, String> entry: param.entrySet()) {
                    stmt.setString(entry.getKey(), entry.getValue());
                }
            }
            rs = stmt.executeQuery();
            callback.callback(rs);
        } finally {
            jdbcHelper.release(null, stmt, rs);
        }
    }


    public SqlExecution(DbConfiguration configuration) {
        jdbcHelper = new JdbcHelper(configuration);
    }
}
