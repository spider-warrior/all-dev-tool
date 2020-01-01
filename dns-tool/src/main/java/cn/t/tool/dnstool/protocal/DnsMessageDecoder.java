package cn.t.tool.dnstool.protocal;


import cn.t.tool.dnstool.FlagUtil;
import cn.t.tool.dnstool.ForbidServiceException;
import cn.t.tool.dnstool.RecordClass;
import cn.t.tool.dnstool.RecordType;
import cn.t.tool.dnstool.model.Header;
import cn.t.tool.dnstool.model.Request;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author yj
 * @since 2020-01-01 10:33
 **/
@Slf4j
public class DnsMessageDecoder {
    public Request decode(byte[] messageBytes) {
        if(messageBytes != null && messageBytes.length > 0) {
            ByteBuffer messageBuffer = ByteBuffer.wrap(messageBytes);
            //解析头部
            //报文Id
            short id = messageBuffer.getShort();
            //报文标志
            short flag = messageBuffer.getShort();
            //查询问题区域的数量
            short queryDomainCount = messageBuffer.getShort();
            //回答区域的数量
            short answerCount = messageBuffer.getShort();
            //授权区域的数量
            short authoritativeNameServerCount = messageBuffer.getShort();
            //附加区域的数量
            short additionalRecordsCount = messageBuffer.getShort();

            //服务检查
            serviceCheck(flag);

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
                Request request = new Request();
                request.setHeader(header);
                request.setDomain(domain);
                request.setLabelCount(labelCount);
                request.setType(RecordType.getRecordType(type));
                request.setClazz(RecordClass.getRecordClass(clazz));
                return request;
            } else {
                log.warn("query domain count is 0");
                return null;
            }
        } else {
            log.warn("handled message should not be null");
            return null;
        }
    }

    private void serviceCheck(short flag) {
        if(!FlagUtil.isQuery(flag)) {
            throw new ForbidServiceException("flag只支持查询");
        }
        if(!FlagUtil.isForwardDirection(flag)) {
            throw new ForbidServiceException("flag只支持正向查询");
        }
    }
}
