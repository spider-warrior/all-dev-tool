package cn.t.tool.dnstool.model;

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
    private short type;
    private short clazz;
}
