package cn.t.tool.rmdbtool.common;

/**
 * DB configuration
 * */
public class DbConfiguration {

    /**
     * username
     * */
    private String username;

    /**
     * password
     * */
    private String password;

    /**
     * jdbc url
     * */
    private String jdbcUrl;

    /**
     * db driver name
     * */
    private String driverName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

}
