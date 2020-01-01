package cn.t.tool.dnstool.model;

import cn.t.tool.dnstool.QueryClass;
import cn.t.tool.dnstool.QueryType;
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
    private QueryType type;
    private QueryClass clazz;
}
