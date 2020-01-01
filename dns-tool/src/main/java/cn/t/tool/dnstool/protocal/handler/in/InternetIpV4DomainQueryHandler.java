package cn.t.tool.dnstool.protocal.handler.in;

import cn.t.tool.dnstool.FlagUtil;
import cn.t.tool.dnstool.Ipv4DomainHelper;
import cn.t.tool.dnstool.RecordClass;
import cn.t.tool.dnstool.RecordType;
import cn.t.tool.dnstool.model.*;
import cn.t.tool.dnstool.protocal.MessageHandler;
import cn.t.util.common.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
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
    public boolean support(Request request) {
        //class: internet && type: A
        return request != null && RecordClass.IN == request.getClazz() && RecordType.A == request.getType();
    }

    @Override
    public Object handler(Request request) throws IOException {
        String domain = request.getDomain();
        if(domain.endsWith("ishansong.com")) {
            System.out.println();
        }
        //读取配置域名
        String ip = ipv4DomainHelper.getCustomDomainMapping(domain);
        if(!StringUtil.isEmpty(ip)) {
            Response response = new Response();
            response.setLabelCount(request.getLabelCount());
            response.setDomain(domain);
            response.setType(request.getType());
            response.setClazz(request.getClazz());
            List<Record> recordList = new ArrayList<>();
            response.setRecordList(recordList);
            //record
            Record record = new Record();
            record.setOffset((short)(0xC000 | 12));
            record.setRecordType(RecordType.A);
            record.setRecordClass(RecordClass.IN);
            record.setTtl(1);
            record.setValue(ip);
            recordList.add(record);

            Header header = request.getHeader();
            short flag = header.getFlags();
            flag = FlagUtil.markResponse(flag);
            //如果客户端设置建议递归查询
            if(FlagUtil.isRecursionResolve(header.getFlags())) {
                flag = FlagUtil.markRecursionSupported(flag);
            }
            header.setFlags(flag);
            header.setAnswerCount((short)recordList.size());
            response.setHeader(header);
            return response;
        } else {
            log.info("domain: {} is not config in file, use local resolver", domain);
            //加载
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            if(addresses == null || addresses.length == 0) {
                log.info("domain: {} cannot be resolved by local resolver, use 114.114.114.114", domain);
                InetAddress dnsServerAddress = InetAddress.getByName("114.114.114.114");
                DatagramSocket internetSocket = new DatagramSocket();
                byte[] data = request.toBytes();
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
                log.info("domain: {} resolved by local resolver, record size: {}", domain, addresses.length);
                //因为域名字符的限制(最大为63)所以byte字节的高两位始终为00，所以使用高两位使用11表示使用偏移量来表示对应的域名,10和01两种状态被保留
                //前面内容都是定长，所以偏移量一定是从12开始算起
                Response response = new Response();
                response.setLabelCount(request.getLabelCount());
                response.setDomain(domain);
                response.setType(request.getType());
                response.setClazz(request.getClazz());
                List<Record> recordList = new ArrayList<>();
                response.setRecordList(recordList);
                for(InetAddress inetAddress: addresses) {
                    if(inetAddress instanceof Inet4Address) {
                        Record record = new Record();
                        record.setOffset((short)(0xC000 | 12));
                        record.setRecordType(RecordType.A);
                        record.setRecordClass(RecordClass.IN);
                        record.setTtl(1);
                        record.setValue(inetAddress.getHostAddress());
                        recordList.add(record);
                    }
                }
                Header header = request.getHeader();
                short flag = header.getFlags();
                flag = FlagUtil.markResponse(flag);
                //如果客户端设置建议递归查询
                if(FlagUtil.isRecursionResolve(header.getFlags())) {
                    flag = FlagUtil.markRecursionSupported(flag);
                }
                header.setFlags(flag);
                header.setAnswerCount((short)recordList.size());
                response.setHeader(header);
                return response;
            }
        }
    }

}
