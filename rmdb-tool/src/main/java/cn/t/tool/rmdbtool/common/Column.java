package cn.t.tool.rmdbtool.common;

public class Column {

    /**
     * 列名
     * */
    private String name;

    /**
     * 是否允许为空
     * */
    private boolean nullableValue;

    /**
     * 类型
     * */
    private String type;

    /**
     * 默认值
     * */
    private Object defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNullableValue() {
        return nullableValue;
    }

    public void setNullableValue(boolean nullableValue) {
        this.nullableValue = nullableValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "Column{" + "name='" + name + '\'' +
            ", nullableValue=" + nullableValue +
            ", type='" + type + '\'' +
            ", defaultValue=" + defaultValue +
            '}';
    }
}
