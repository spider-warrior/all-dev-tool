package cn.t.tool.dbtool.exception;

public class TableNotExistException extends Exception {

    private String tableName;

    public TableNotExistException(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public TableNotExistException setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
}
