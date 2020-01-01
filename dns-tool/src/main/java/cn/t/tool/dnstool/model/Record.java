package cn.t.tool.dnstool.model;

import cn.t.tool.dnstool.RecordClass;
import cn.t.tool.dnstool.RecordType;
import lombok.Data;

/**
 * @author yj
 * @since 2020-01-01 15:05
 **/
@Data
public class Record {
    /**
     * 偏移量
     * */
    private short offset;

    /**
     * 记录类型
     * */
    private RecordType recordType;

    /**
     * 记录class
     * */
    private RecordClass recordClass;

    /**
     * time to live(秒)
     * */
    private int ttl;

    /**
     * 值
     * */
    private String value;
}
