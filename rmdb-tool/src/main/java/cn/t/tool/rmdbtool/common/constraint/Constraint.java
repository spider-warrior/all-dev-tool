package cn.t.tool.rmdbtool.common.constraint;

/**
 * 约束
 * */
public class Constraint {
    private String owner;
    private String constraintName;
    private String tableName;
    private String columnName;
    private String position;
    private String type;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Constraint{");
        sb.append("owner='").append(owner).append('\'');
        sb.append(", constraintName='").append(constraintName).append('\'');
        sb.append(", tableName='").append(tableName).append('\'');
        sb.append(", columnName='").append(columnName).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
