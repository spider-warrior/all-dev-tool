package cn.t.tool.dnstool;

/**
 * @author yj
 * @since 2019-12-31 21:45
 **/
public enum QueryClass {

    IN((short)1, "IN", "指互联网地址"),
    CS((short)2, "CS", "the CSNET class (Obsolete - used only for examples in some obsolete RFCs)"),
    CH((short)3, "CH", "the CHAOS class"),
    HS((short)4, "HS", "Hesiod [Dyer 87]"),
    ANY((short)255, "*", "any class");

    ;
    public final short value;
    public final String shortName;
    public final String desc;

    QueryClass(short value, String shortName, String desc) {
        this.value = value;
        this.shortName = shortName;
        this.desc = desc;
    }

    public static QueryClass getQueryClass(short value) {
        for(QueryClass queryClass: values()) {
            if(queryClass.value == value) {
                return queryClass;
            }
        }
        return null;
    }
}
