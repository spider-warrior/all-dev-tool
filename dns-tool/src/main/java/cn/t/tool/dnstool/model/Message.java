package cn.t.tool.dnstool.model;

import cn.t.tool.dnstool.RecordClass;
import cn.t.tool.dnstool.RecordType;
import lombok.Data;

/**
 * @author yj
 * @since 2020-01-01 10:45
 **/
@Data
public class Message {
    private Header header;
    private String domain;
    private byte labelCount;
    private RecordType type;
    private RecordClass clazz;

    public byte[] toBytes() {
        return new byte[0];
    }
}
