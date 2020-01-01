package cn.t.tool.dnstool.model;

import cn.t.tool.dnstool.RecordClass;
import cn.t.tool.dnstool.RecordType;
import lombok.Data;

import java.nio.ByteBuffer;

/**
 * @author yj
 * @since 2020-01-01 10:45
 **/
@Data
public class Request {
    private Header header;
    private String domain;
    private byte labelCount;
    private RecordType type;
    private RecordClass clazz;

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //1.transaction id
        buffer.putShort(header.getTransID());
        //2.flag
        buffer.putShort(header.getFlags());
        //3.question count(固定写1)
        buffer.putShort((short) 1);
        //4.answer count
        buffer.putShort((short) 0);
        //5.authoritative count(固定写0)
        buffer.putShort((short)0);
        //6.additional count(固定写0)
        buffer.putShort((short)0);
        //7.query body
        String[] elements = domain.split("\\.");
        for(String ele: elements) {
            //长度
            buffer.put((byte)ele.length());
            //value
            buffer.put(ele.getBytes());
        }
        //结束补0
        buffer.put((byte)0);
        //2.type
        buffer.putShort(type.value);
        //3.class
        buffer.putShort(clazz.value);
        return buffer.array();
    }
}
