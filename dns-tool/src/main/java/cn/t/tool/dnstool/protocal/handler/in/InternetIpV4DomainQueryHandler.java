package cn.t.tool.dnstool.protocal.handler.in;

import cn.t.tool.dnstool.Ipv4DomainHelper;
import cn.t.tool.dnstool.RecordClass;
import cn.t.tool.dnstool.RecordType;
import cn.t.tool.dnstool.model.InternetResolveResponse;
import cn.t.tool.dnstool.model.Message;
import cn.t.tool.dnstool.model.ResolveResult;
import cn.t.tool.dnstool.model.ResolvedRecord;
import cn.t.tool.dnstool.protocal.MessageHandler;
import cn.t.util.common.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-01 11:37
 **/
@Slf4j
public class InternetIpV4DomainQueryHandler implements MessageHandler {

    private Ipv4DomainHelper ipv4DomainHelper = new Ipv4DomainHelper();

    @Override
    public boolean support(Message message) {
        //class: internet && type: A
        return message != null && RecordClass.IN == message.getClazz() && RecordType.A == message.getType();
    }

    @Override
    public Object handler(Message message) throws IOException {
        String domain = message.getDomain();
        //读取配置域名
        String ip = ipv4DomainHelper.getCustomDomainMapping(domain);
        if(StringUtil.isEmpty(ip)) {
            log.info("domain: {} is not config in file, use local resolver", domain);
            //加载
            InetAddress[] addresses = addresses = InetAddress.getAllByName(domain);
            if(addresses == null || addresses.length == 0) {
                log.info("domain: {} cannot be resolved by local resolver, use 114.114.114.114", domain);
                InetAddress dnsServerAddress = InetAddress.getByName("114.114.114.114");
                DatagramSocket internetSocket = new DatagramSocket();
                byte[] data = message.toBytes();
                DatagramPacket internetSendPacket = new DatagramPacket(data, data.length, dnsServerAddress, 53);
                internetSocket.send(internetSendPacket);
                byte[] receivedData = new byte[1024];
                DatagramPacket internetReceivedPacket = new DatagramPacket(receivedData, receivedData.length);
                internetSocket.receive(internetReceivedPacket);
                InternetResolveResponse internetResolveResponse = new InternetResolveResponse();
                byte[] dataToUse = new byte[internetReceivedPacket.getLength()];
                System.arraycopy(receivedData, 0, dataToUse, 0, dataToUse.length);
                internetResolveResponse.setData(dataToUse);
                return internetResolveResponse;
            } else {
                //因为域名字符的限制(最大为63)所以byte字节的高两位始终为00，所以使用高两位使用11表示使用偏移量来表示对应的域名,10和01两种状态被保留
                //前面内容都是定长，所以偏移量一定是从12开始算起
                short offset = 12;
                ResolveResult response = new ResolveResult();
                response.setHeader(message.getHeader());
                response.setLabelCount(message.getLabelCount());
                response.setDomain(domain);
                response.setType(message.getType());
                response.setClazz(message.getClazz());
                List<ResolvedRecord> resolvedRecordList = new ArrayList<>();
                response.setResolvedRecordList(resolvedRecordList);
                for(InetAddress inetAddress: addresses) {
                    ResolvedRecord record = new ResolvedRecord();
                    record.setOffset(offset);
                    record.setRecordType(RecordType.A);
                    record.setRecordClass(RecordClass.IN);
                    record.setTtl(1);
                    record.setValue(inetAddress.getHostAddress());
                    resolvedRecordList.add(record);
                }
                return response;
            }
        }
        return null;
    }

    public static void main(String[] args) throws UnknownHostException {
        InetAddress.getByName("www.baidu.com");
        InetAddress.getAllByName("www.baidu.com");
    }
}
