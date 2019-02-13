import cn.t.tool.rmdbtool.OracleHelper;
import cn.t.tool.rmdbtool.common.constraint.Constraint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class OracleHelperTest {

    private static final Logger logger = LoggerFactory.getLogger(OracleHelperTest.class);
    private OracleHelper oracleHelper = new OracleHelper();

    @Before
    public void init() {}

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

    @Test
    public void queryConstraintTest() throws SQLException, ClassNotFoundException {
        String tableName = "student";
        Constraint constraint = oracleHelper.queryPrimaryKeyConstraint(tableName);
        System.out.println(constraint);
    }

    @Test
    public void getTableConstraintListTest() throws SQLException, ClassNotFoundException {
        String tableName = "student";
        List<Constraint> constraintList = oracleHelper.queryTableConstraintList(tableName);
        for(Constraint constraint: constraintList) {
            System.out.println("constraint: " + constraint);
        }
    }

    @Test
    public void queryTableColumnListTest() throws SQLException, ClassNotFoundException {
        String tableName = "student";
        System.out.println(oracleHelper.queryCreateTableStatement(tableName));
    }

    @Test
    public void queryAllCreateTableStatementTest() throws SQLException, ClassNotFoundException {
        List<String> statementList = oracleHelper.queryAllCreateTableStatement();
        for(String str: statementList) {
            System.out.println(str);
            System.out.println("=====================================================================");
        }
    }

    @After
    public void destroy() { }
}
