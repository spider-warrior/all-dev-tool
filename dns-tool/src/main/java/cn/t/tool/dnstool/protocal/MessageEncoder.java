package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.RecordType;
import cn.t.tool.dnstool.model.Header;
import cn.t.tool.dnstool.model.InternetResolveResponse;
import cn.t.tool.dnstool.model.ResolveResult;
import cn.t.tool.dnstool.model.ResolvedRecord;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-01 10:54
 **/
@Slf4j
public class MessageEncoder {
    public void encode(Context context, Object message) throws IOException {
        if(message != null) {
            if(message instanceof ResolveResult) {
                ResolveResult result = (ResolveResult)message;
                Header header = result.getHeader();
                List<ResolvedRecord> resolvedRecordList = result.getResolvedRecordList();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                //transaction id
                buffer.putShort(header.getTransID());
                //flag
                short flagToUse = (short)(header.getFlags() | 0b1000000000000000);
                buffer.putShort(flagToUse);
                //question count(固定写1)
                buffer.putShort((short) 1);
                //answer count
                buffer.putShort((short)resolvedRecordList.size());
                //authoritative count(固定写0)
                buffer.putShort((short)0);
                //additional count(固定写0)
                buffer.putShort((short)0);

                //query body
                //1.domain
                String domain = result.getDomain();
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
                buffer.putShort(result.getType().value);
                //3.class
                buffer.putShort(result.getClazz().value);

                //answer body
                for(ResolvedRecord record: resolvedRecordList) {
                    //1.offset
                    buffer.putShort(record.getOffset());
                    //2.type
                    buffer.putShort(record.getRecordType().value);
                    //3.class
                    buffer.putShort(record.getRecordClass().value);
                    //4.ttl
                    buffer.putInt(record.getTtl());
                    //5.value
                    if(RecordType.A == record.getRecordType()) {
                        //值为字符串的ip地址
                        String ip = record.getValue();
                        String[] ipElements = ip.split("\\.");
                        for(String part : ipElements) {
                            buffer.put(Byte.parseByte(part));
                        }
                    } else if(RecordType.CNAM == record.getRecordType()) {
                        String[] cNameElements = domain.split("\\.");
                        for(String cNamePart: cNameElements) {
                            //长度
                            buffer.put((byte)cNamePart.length());
                            //value
                            buffer.put(cNamePart.getBytes());
                        }
                    }
                }

                byte[] output = buffer.array();
                context.write(output);
            } else if(message instanceof InternetResolveResponse) {
                context.write(((InternetResolveResponse)message).getData());
            } else {
                log.error("为实现的编码对象: {}", message);
            }
        }
    }
}
