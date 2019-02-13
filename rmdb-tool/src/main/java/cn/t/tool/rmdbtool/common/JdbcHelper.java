package cn.t.tool.rmdbtool.common;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcHelper {
    private static final ThreadLocal<Connection> tt = new ThreadLocal<>();
    private final BasicDataSource dataSource;
    private final DbConfiguration dbConfiguration;

    public Connection getConnection() throws SQLException {
        Connection conn = tt.get();
        if (conn == null) {
            conn = dataSource.getConnection();
            tt.set(conn);
        }
        return conn;
    }

    public void release(Connection con, Statement sta, ResultSet rs) {
        if (con != null) {
            try {
                con.close();
                tt.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (sta != null) {
            try {
                sta.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeCurrentConnection() {
        Connection conn = tt.get();
        if (conn != null) {
            try {
                tt.remove();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String getDbName() {

        String url = dbConfiguration.getJdbcUrl();
        //mysql
        if (url.toLowerCase().contains("mysql")) {
            return url.substring((url.lastIndexOf("/") + 1),
                    (url.lastIndexOf("?") == -1 ? url.length() : url.lastIndexOf("?"))).trim();
        }
        //oracle
        else if (url.toLowerCase().contains("oracle")) {
            int atIndex = url.indexOf("@");
            int colonAfterAtIndex = url.indexOf(':', atIndex);
            if (colonAfterAtIndex > 0) {
                int slashIndex = url.indexOf('/', colonAfterAtIndex);
                if (slashIndex < 0) {
                    return url.substring((url.lastIndexOf(":") + 1)).trim();
                } else {
                    return url.substring((url.lastIndexOf("/") + 1)).trim();
                }
            } else {
                int serviceNameIndex = url.toUpperCase().indexOf("SERVICE_NAME", atIndex);
                int equalIndex = url.indexOf('=', serviceNameIndex);
                return url.substring(equalIndex + 1, url.indexOf(')', equalIndex)).trim();
            }
        }
        //others
        else {
            return url.trim();
        }
    }

    public JdbcHelper(DbConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
        dataSource = new BasicDataSource();
        dataSource.setUsername(dbConfiguration.getUsername());
        dataSource.setPassword(dbConfiguration.getPassword());
        dataSource.setUrl(dbConfiguration.getJdbcUrl());
        dataSource.setDriverClassName(dbConfiguration.getDriverName());
    }
}
