package cn.t.tool.rmdbtool.common.constraint;

public enum  ConstraintType {

    CHECK_CONSTRAINT_ON_A_TABLE("C"),
    /**
     * 主键
     * */
    PRIMARY_KEY("P"),
    /**
     * 唯一
     * */
    UNIQUE_KEY("U"),
    /**
     * 外键
     * */
    REFERENTIAL_INTEGRITY("R"),
    WITH_CHECK_OPTION_ON_A_VIEW("V"),
    /**
     * 只读视图
     * */
    WITH_READ_ONLY_ON_A_VIEW("O"),
    HASH_EXPRESSION("H"),
    CONSTRAINT_THAT_INVOLVES_A_REF_COLUMN("F"),
    SUPPLEMENTAL_LOGGING("S"),
    /**
     * 未定义
     * */
    UNDEFINED("?")
    ;
    public final String value;

    ConstraintType(String value) {
        this.value = value;
    }

    public static ConstraintType getConstraintType(String c) {
        for(ConstraintType type: values()) {
            if(type.value.equals(c)) {
                return type;
            }
        }
        return UNDEFINED;
    }
}
