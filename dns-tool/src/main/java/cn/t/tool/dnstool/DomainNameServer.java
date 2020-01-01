package cn.t.tool.dnstool;

import cn.t.util.common.nio.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

/**
 * @author yj
 * @since 2019-12-31 20:48
 **/
@Slf4j
public class DomainNameServer {

    private static final byte[] buffer = new byte[1024];

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(53);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            log.info("==========================================================================================");
            log.info("message from: sourceIpAddr: {}:{}", packet.getAddress(), packet.getPort());
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            //解析头部
            //报文Id
            short id = byteBuffer.getShort();
            //报文标志
            short flags = byteBuffer.getShort();
            //查询问题区域的数量
            short queryDomainCount = byteBuffer.getShort();
            //回答区域的数量
            short answerCount = byteBuffer.getShort();
            //授权区域的数量
            short authoritativeNameServerCount = byteBuffer.getShort();
            //附加区域的数量
            short additionalRecords = byteBuffer.getShort();
            //解析报文体
            if(queryDomainCount > 0) {
                StringBuilder domainBuilder = new StringBuilder();
                byte count;
                while (byteBuffer.remaining() > 0 && (count = byteBuffer.get()) > 0) {
                    byte[] partDomain = new byte[count];
                    byteBuffer.get(partDomain);
                    domainBuilder.append(new String(partDomain)).append(".");
                }
                if(domainBuilder.length() > 0) {
                    domainBuilder.deleteCharAt(domainBuilder.length() - 1);
                }
                String domain = domainBuilder.toString();
                short type = byteBuffer.getShort();
                QueryType queryType = QueryType.getQueryType(type);
                short clazz = byteBuffer.getShort();
                QueryClass queryClass = QueryClass.getQueryClass(clazz);
                log.info("client: {}:{} query detail:\n domain: {}, queryType: {}, clazz: {}", packet.getAddress(), packet.getPort(), domain, queryType, queryClass);
            }
        }
    }
}
