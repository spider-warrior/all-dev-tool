import cn.t.tool.rmdbtool.OracleHelper;
import cn.t.tool.rmdbtool.common.DbConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class OracleHelperTest {

    private static final Logger logger = LoggerFactory.getLogger(OracleHelperTest.class);
    private OracleHelper oracleHelper;

    @Before
    public void init() {
        String username = "dev_test";
        String password = "6sAKodk1UKqYfH7hLriR";
        String jdbcUrl = "jdbc:oracle:thin:@192.168.14.39:1521:ORCL";
        String driverName = "oracle.jdbc.OracleDriver";
        DbConfiguration configuration = new DbConfiguration();
        configuration.setUsername(username);
        configuration.setPassword(password);
        configuration.setJdbcUrl(jdbcUrl);
        configuration.setDriverName(driverName);
        oracleHelper = new OracleHelper(configuration);
    }

    @Test
    public void queryAllTablesTest() throws SQLException, ClassNotFoundException {
        List<String> tableList = oracleHelper.queryAllTables();
        for(int i=0; i<tableList.size(); i++) {
            logger.info("{}.: {}", i, tableList.get(i));
        }
    }

    @Test
    public void printAllViewsTest() throws SQLException, ClassNotFoundException {
        List<String> viewList = oracleHelper.queryAllViews();
        for(int i=0; i<viewList.size(); i++) {
            logger.info("{}.: {}", i, viewList.get(i));
        }
    }

    @Test
    public void checkTableExistTest() throws SQLException, ClassNotFoundException {
        String tableName = "XF_COMMON_USER";
        boolean exist = oracleHelper.checkTableExist(tableName);
        if(exist) {
           logger.info("表:{} 存在", tableName);
        } else {
            logger.info("表:{} 不存在", tableName);
        }
    }

    @Test
    public void checkTableColumnExistTest() throws SQLException, ClassNotFoundException {
        String tableName = "XF_COMMON_USER";
        String columnName = "ID";
        boolean exist = oracleHelper.checkTableColumnExist(tableName, columnName);
        if(exist) {
            logger.info("列:{}.{} 存在", tableName, columnName);
        } else {
            logger.info("列:{}.{} 不存在", tableName, columnName);
        }
    }

    @After
    public void destroy() {

    }
}
