package cn.t.tool.dbtool.common;

import java.sql.*;

public class JdbcHelper {
    private static final ThreadLocal<Connection> tt = new ThreadLocal<>();
    private final DbConfiguration dbConfiguration;

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn = tt.get();
        if (conn == null) {
            Class.forName(dbConfiguration.getDriverName());
            conn = DriverManager.getConnection(dbConfiguration.getJdbcUrl(),
                dbConfiguration.getUsername(), dbConfiguration.getPassword());
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
    }
}
