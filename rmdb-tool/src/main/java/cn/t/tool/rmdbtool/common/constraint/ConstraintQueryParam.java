package cn.t.tool.rmdbtool.common.constraint;

public class ConstraintQueryParam {

    private String tableName;
    private String columnName;
    private String type;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ConstraintQueryParam{" + "tableName='" + tableName + '\'' +
            ", columnName='" + columnName + '\'' +
            ", type=" + type +
            '}';
    }
}
