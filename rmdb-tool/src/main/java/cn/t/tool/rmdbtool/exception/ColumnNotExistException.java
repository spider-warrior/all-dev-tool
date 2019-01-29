package cn.t.tool.rmdbtool.exception;

public class ColumnNotExistException extends Exception {

    private String columnName;

    public ColumnNotExistException(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public ColumnNotExistException setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }
}
