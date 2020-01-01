package cn.t.tool.dnstool.protocal;


import cn.t.tool.dnstool.model.Header;
import cn.t.tool.dnstool.model.Message;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author yj
 * @since 2020-01-01 10:33
 **/
@Slf4j
public class DnsMessageDecoder {
    public Message decode(byte[] messageBytes) {
        if(messageBytes != null && messageBytes.length > 0) {
            ByteBuffer messageBuffer = ByteBuffer.wrap(messageBytes);
            //解析头部
            //报文Id
            short id = messageBuffer.getShort();
            //报文标志
            short flags = messageBuffer.getShort();
            //查询问题区域的数量
            short queryDomainCount = messageBuffer.getShort();
            //回答区域的数量
            short answerCount = messageBuffer.getShort();
            //授权区域的数量
            short authoritativeNameServerCount = messageBuffer.getShort();
            //附加区域的数量
            short additionalRecordsCount = messageBuffer.getShort();
            //解析报文体
            if(queryDomainCount > 0) {
                StringBuilder domainBuilder = new StringBuilder();
                byte count;
                byte labelCount = 0;
                while (messageBuffer.remaining() > 0 && (count = messageBuffer.get()) > 0) {
                    byte[] partDomain = new byte[count];
                    messageBuffer.get(partDomain);
                    domainBuilder.append(new String(partDomain)).append(".");
                    labelCount++;
                }
                if(domainBuilder.length() > 0) {
                    domainBuilder.deleteCharAt(domainBuilder.length() - 1);
                }
                String domain = domainBuilder.toString();
                short type = messageBuffer.getShort();
                short clazz = messageBuffer.getShort();
                //header
                Header header = new Header();
                header.setTransID(id);
                header.setQueryDomainCount(queryDomainCount);
                header.setAnswerCount(answerCount);
                header.setAuthoritativeNameServerCount(authoritativeNameServerCount);
                header.setAdditionalRecordsCount(additionalRecordsCount);
                //message
                Message message = new Message();
                message.setHeader(header);
                message.setDomain(domain);
                message.setLabelCount(labelCount);
                message.setType(type);
                message.setClazz(clazz);
                return message;
            } else {
                log.warn("query domain count is 0");
                return null;
            }
        } else {
            log.warn("handled message should not be null");
            return null;
        }
    }
}
