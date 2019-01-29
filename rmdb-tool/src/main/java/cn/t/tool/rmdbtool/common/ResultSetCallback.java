package cn.t.tool.rmdbtool.common;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallback {
    void callback(ResultSet rs) throws SQLException;
}
