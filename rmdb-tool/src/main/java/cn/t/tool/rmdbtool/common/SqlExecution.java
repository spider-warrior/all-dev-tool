package cn.t.tool.rmdbtool.common;

import cn.t.util.common.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SqlExecution {

    private static final Logger logger = LoggerFactory.getLogger(SqlExecution.class);

    private final JdbcHelper jdbcHelper;

    public void executeSql(String sql, ResultSetCallback callback) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = jdbcHelper.getConnection();
            stmt = conn.createStatement();
            logger.debug("\nsql: {}", sql);
            rs = stmt.executeQuery(sql);
            callback.callback(rs);
        } finally {
            jdbcHelper.release(conn, stmt, rs);
        }
    }

    public void executeSql(String sql, Map<Integer, String> param, ResultSetCallback callback) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = jdbcHelper.getConnection();
            stmt = conn.prepareStatement(sql);
            if(param != null) {
                for(Map.Entry<Integer, String> entry: param.entrySet()) {
                    stmt.setString(entry.getKey(), entry.getValue());
                }
            }
            logger.debug("\nsql: {}\nparam: {}", sql, param);
            rs = stmt.executeQuery();
            callback.callback(rs);
        } finally {
            jdbcHelper.release(conn, stmt, rs);
        }
    }

    public void executeCall(List<String> prepareSqlList, String sql, Map<Integer, String> param, ResultSetCallback callback) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = jdbcHelper.getConnection();
            if(!CollectionUtil.isEmpty(prepareSqlList)) {
                for(String str: prepareSqlList) {
                    conn.prepareCall(str).execute();
                }
            }
            stmt = conn.prepareCall(sql);
            if(param != null) {
                for(Map.Entry<Integer, String> entry: param.entrySet()) {
                    stmt.setString(entry.getKey(), entry.getValue());
                }
            }
            logger.debug("\nsql: {}\nparam: {}", sql, param);
            rs = stmt.executeQuery();
            callback.callback(rs);
        } finally {
            jdbcHelper.release(conn, stmt, rs);
        }
    }



    public SqlExecution(DbConfiguration configuration) {
        jdbcHelper = new JdbcHelper(configuration);
    }

    public JdbcHelper getJdbcHelper() {
        return jdbcHelper;
    }
}
