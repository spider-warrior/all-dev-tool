package cn.t.tool.dnstool.model;

import cn.t.tool.dnstool.RecordType;
import cn.t.util.common.CollectionUtil;
import lombok.Data;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-01 15:01
 **/
@Data
public class Response extends Request {

    private List<Record> recordList;

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Header header = getHeader();
        //1.transaction id
        buffer.putShort(header.getTransID());
        //2.flag
        buffer.putShort(header.getFlags());
        //3.question count(固定写1)
        buffer.putShort((short) 1);
        //4.answer count
        buffer.putShort((short)(CollectionUtil.isEmpty(recordList) ? 0 : recordList.size()));
        //5.authoritative count(固定写0)
        buffer.putShort((short)0);
        //6.additional count(固定写0)
        buffer.putShort((short)0);
        //7.query body
        //7.1 domain
        String[] elements = getDomain().split("\\.");
        for(String ele: elements) {
            //长度
            buffer.put((byte)ele.length());
            //value
            buffer.put(ele.getBytes());
        }
        //结束补0
        buffer.put((byte)0);
        //7.2 type
        buffer.putShort(getType().value);
        //7.3 class
        buffer.putShort(getClazz().value);

        //8.answer body
        for(Record record: recordList) {
            //8.1 offset
            buffer.putShort(record.getOffset());
            //8.2 type
            buffer.putShort(record.getRecordType().value);
            //8.3 class
            buffer.putShort(record.getRecordClass().value);
            //8.4 ttl
            buffer.putInt(record.getTtl());
            //8.5 value
            if(RecordType.A == record.getRecordType()) {
                //值为字符串的ip地址
                buffer.putShort((short)4);
                String ip = record.getValue();
                String[] ipElements = ip.split("\\.");
                for(String part : ipElements) {
                    try {
                        buffer.put((byte)Short.parseShort(part));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else if(RecordType.CNAM == record.getRecordType()) {
                //值为别名
                ByteBuffer cnameBuffer = ByteBuffer.allocate(64);
                String[] cNameElements = record.getValue().split("\\.");
                for(String cNamePart: cNameElements) {
                    //长度
                    cnameBuffer.put((byte)cNamePart.length());
                    //value
                    cnameBuffer.put(cNamePart.getBytes());
                }
                buffer.put(cnameBuffer.array());
            }
        }
        buffer.flip();
        int len = buffer.limit() - buffer.position();
        byte[] bytes = new byte[len];
        buffer.get(bytes);
        return bytes;
    }
}
