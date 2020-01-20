package cn.t.tool.dhcptool.protocol;

import cn.t.tool.dhcptool.constants.OperationType;
import cn.t.tool.dhcptool.constants.OptionType;
import cn.t.tool.dhcptool.model.NakMessage;
import cn.t.tool.dhcptool.model.OfferMessage;
import cn.t.util.common.ArrayUtil;
import cn.t.util.common.digital.HexUtil;
import cn.t.util.common.digital.IntUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息解码器
 *
 * @author yj
 * @since 2020-01-05 16:21
 **/
public class DhcpMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(DhcpMessageDecoder.class);

    public Object decode(ByteBuffer buffer) {
        //OP: 若是客户端送给服务端的封包 设置为1 反方向为2
        byte op = buffer.get();
        OperationType operationType = OperationType.getOperationType(op);
        if(operationType == null) {
            throw new RuntimeException("未知的操作类型：" + op);
        }
        if(OperationType.OFFER == operationType) {
            //硬件类型
            byte hardwareType = buffer.get();
            //硬件mac长度
            byte hardwareAddressLength = buffer.get();
            //DHCP报文经过的DHCP中继的数目
            byte hops = buffer.get();
            //事务ID
            int transactionId = buffer.getInt();
            //在没有获得IP地址前该字段始终为0。
            short seconds = buffer.getShort();
            //广播方式
            short broadcastType = buffer.getShort();
            //client addr
            byte[] clientIp = new byte[4];
            buffer.get(clientIp);
            //your ip
            byte[] yourIp = new byte[4];
            buffer.get(yourIp);
            //next server ip
            byte[] nextServerIp = new byte[4];
            buffer.get(nextServerIp);
            //DHCP客户端发出请求报文后经过的第一个DHCP中继的IP地址。如果没有经过DHCP中继，则显示为0
            byte[] relayAgentIp = new byte[4];
            buffer.get(relayAgentIp);
            //client mac
            byte[] clientMac = new byte[hardwareAddressLength];
            buffer.get(clientMac);
            //client mac padding
            byte[] clientMacPadding = new byte[16 - hardwareAddressLength];
            buffer.get(clientMacPadding);
            //sname
            byte[] sname = new byte[64];
            buffer.get(sname);
            //sname
            byte[] file = new byte[128];
            buffer.get(file);
            //magic
            byte[] magic = new byte[4];
            buffer.get(magic);
            Map<OptionType, byte[]> optionMap = new HashMap<>();
            byte tag;
            while (buffer.remaining() > 0 && (tag = buffer.get()) != OptionType.END.value) {
                OptionType optionType = OptionType.getOptionType(tag);
                if(optionType == null) {
                    throw new RuntimeException("未知的选项类型: " + tag);
                } else {
                    byte[] optionValueLength = new byte[buffer.get()];
                    buffer.get(optionValueLength);
                    optionMap.put(optionType, optionValueLength);
                }
            }
            byte[] messageType = optionMap.get(OptionType.DHCP_MSG_TYPE);
            if(messageType == null) {
                throw new RuntimeException("未发现消息类型: " + HexUtil.bytesToHex(buffer.array()));
            }
            OperationType oType = OperationType.getOperationType(messageType[0]);
            if(oType == OperationType.OFFER) {
                OfferMessage offerMessage = new OfferMessage();
                offerMessage.setClientIp(yourIp);
                offerMessage.setClientMac(clientMac);
                offerMessage.setDhcpServerIp(optionMap.get(OptionType.DHCP_SERVER));
                offerMessage.setLeaseTime(IntUtil.bytesToInt(optionMap.get(OptionType.ADDRESS_TIME)));
                offerMessage.setRenewTime(IntUtil.bytesToInt(optionMap.get(OptionType.RENEWAL_TIME)));
                offerMessage.setRebindingTime(IntUtil.bytesToInt(optionMap.get(OptionType.REBINDING_TIME)));
                offerMessage.setSubnetMask(optionMap.get(OptionType.SUBNET_MASK));
                offerMessage.setRouter(optionMap.get(OptionType.ROUTER));
                byte[] dnses =optionMap.get(OptionType.DOMAIN_NAME_SERVER);
                if(!ArrayUtil.isEmpty(dnses)) {
                    int ipCount = dnses.length / 4;
                    List<byte[]> dnsServerList = new ArrayList<>(ipCount);
                    for(int i=0; i<ipCount; i++) {
                        byte[] ip = new byte[4];
                        ip[i] = dnses[i+0];
                        ip[i] = dnses[i+1];
                        ip[i] = dnses[i+2];
                        ip[i] = dnses[i+3];
                        dnsServerList.add(ip);
                    }
                    offerMessage.setDnsServerList(dnsServerList);
                }
                return offerMessage;
            } else if(oType == OperationType.NAK) {
                NakMessage message = new NakMessage();
                byte[] server = optionMap.get(OptionType.DHCP_SERVER);
                message.setNextServerIp((server != null) ? server : nextServerIp);
                byte[] messageBytes = optionMap.get(OptionType.DHCP_MESSAGE);
                if(messageBytes != null) {
                    message.setMessage(new String(messageBytes));
                }
                return message;
            } else {
                throw new RuntimeException("为实现的option消息解析类型: " + oType);
            }
        } else {
            logger.warn("message is not reply, ignored! detail: {}", HexUtil.bytesToHex(buffer.array()));
            return null;
        }
    }
}
