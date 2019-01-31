package cn.t.tool.rmdbtool.common;

import cn.t.tool.rmdbtool.common.constraint.Constraint;
import cn.t.tool.rmdbtool.common.constraint.ConstraintQueryParam;

import java.sql.SQLException;
import java.util.List;

public interface DbDao {

    /**
     * 查询最大主键值
     */
    long queryMaxId(String tableName, String idColumn) throws SQLException, ClassNotFoundException;

    /**
     * 获取约束
     * */
    List<Constraint> queryConstraint(ConstraintQueryParam param) throws SQLException, ClassNotFoundException;

    /**
     * 检查表是否存在
     */
    boolean checkTableExist(String tableName) throws SQLException, ClassNotFoundException;

    /**
     * 检查指定表的列是否存在
     */
    boolean checkTableColumnExist(String tableName, String columnName) throws SQLException, ClassNotFoundException;

    /**
     * 查询当前用户所有表
     */
    List<String> queryAllTables() throws SQLException, ClassNotFoundException;

    /**
     * 查询当前用户所有视图
     */
    List<String> queryAllViews() throws SQLException, ClassNotFoundException;

    /**
     * 查询建表语句
     * */
    String queryCreateTableStatement(String tableName) throws SQLException, ClassNotFoundException;

}
