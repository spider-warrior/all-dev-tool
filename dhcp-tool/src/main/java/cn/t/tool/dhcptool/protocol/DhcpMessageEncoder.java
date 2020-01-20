package cn.t.tool.dhcptool.protocol;

import cn.t.tool.dhcptool.constants.HardwareType;
import cn.t.tool.dhcptool.constants.OperationType;
import cn.t.tool.dhcptool.constants.OptionType;
import cn.t.tool.dhcptool.model.DiscoverMessage;
import cn.t.util.common.ArrayUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * dhcp消息编码器
 *
 * @author yj
 * @since 2020-01-05 15:31
 **/
public class DhcpMessageEncoder {

    public byte[] encode(DiscoverMessage message) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //OP: 若是客户端送给服务端的封包 设置为1 反方向为2
        buffer.put(OperationType.DISCOVER.value);
        //Htype: 硬件类型， ethernet为1
        buffer.put(HardwareType.ETHER_NET.value);
        //Hlen: 硬件长度， ethernet为6
        buffer.put((byte) 6);
        //Hops: DHCP请求报文每经过一个DHCP中继，该字段就会增加1。没有经过DHCP中继时值为0。(若数据包需经过router传送，每站加1，若在同一网内，为0。)
        buffer.put((byte) 0);
        //Transaction ID ： 事物ID是个随机数，用于客户端和服务器之间匹配请求和相应信息
        buffer.putInt(message.getTxId());
        //DHCP客户端从获取到IP地址或者续约过程开始到现在所消耗的时间，以秒为单位。在没有获得IP地址前该字段始终为0。(DHCP客户端开始DHCP请求后所经过的时间。目前尚未使用，固定为0。)
        buffer.putShort((short) 0);
        //Flags： 从0-15bits 最高位为1时表示server将以广播方式传递封包给client, 0 表示表示server将以单播方式传递封包给client，其余尚未使用(0 or 0x8000)。
        //注意：在客户端正式分配了IP地址之前的第一次IP地址请求过程中，所有DHCP报文都是以广播方式发送的，包括客户端发送的DHCP Discover和DHCP Request报文，以及DHCP服务器发送的DHCP Offer、DHCP ACK和DHCP NAK报文。当然，如果是由DHCP中继器转的报文，则都是以单播方式发送的。另外，IP地址续约、IP地址释放的相关报文都是采用单播方式进行发送的。
        //一定要使用1，否则程序接收不到消息
        buffer.putShort((short) 0b1000000000000000);
        //ciaddr，DHCP客户端的IP地址，因为刚开始不知道IP，所以填充0。客户端会在发送请求时将自己的ip地址放在此处，仅在DHCP服务器发送的ACK报文中显示，因为在得到DHCP服务器确认前，DHCP客户端是还没有分配到IP地址的。
        buffer.put(new byte[4]);
        //yiaddr, DHCP服务器分配给客户端的IP地址。仅在DHCP服务器发送的Offer和ACK报文中显示，其他报文中显示为0。
        buffer.put(new byte[4]);
        //next server ip(siaddr), 下一个为DHCP客户端分配IP地址等信息的DHCP服务器IP地址。下一个为DHCP客户端分配IP地址等信息的DHCP服务器IP地址。用于bootstrap过程中的IP地址(服务器的IP地址)
        //一般来说是服务器的ip地址.但是注意！根据openwrt源码给出的注释，当报文的源地址、siaddr、option.server_id字段不一致（有经过跨子网转发）时，通常认为option.srever_id字段为真正的服务器ip，siaddr有可能是多次路由跳转中的某一个路由的ip
        buffer.put(new byte[4]);
        //relay agent ip, DHCP客户端发出请求报文后经过的第一个DHCP中继的IP地址。如果没有经过DHCP中继，则显示为0。(转发代理（网关）IP地址)
        buffer.put(new byte[4]);
        //client的硬件地址(16字节, mac + 补零)
        buffer.put(Arrays.copyOf(message.getMac(), 16));
        //Sname:  为DHCP客户端分配IP地址的DHCP服务器名称（DNS域名格式）。在Offer和ACK报文中显示发送报文的DHCP服务器名称，其他报文显示为0。
        buffer.put(new byte[64]);
        //File: DHCP服务器为DHCP客户端指定的启动配置文件名称及路径信息。仅在DHCP Offer报文中显示，其他报文中显示为空。
        buffer.put(new byte[128]);

        //magic cookie(固定值)
        //了让server或者client来判别后续数据流的解释模式，即当server看到这个magic cookie固定字节流后，就能知道后面是options field（1字节tag、1字节len，根据这个mode来处理后续数据）
        buffer.put(new byte[] {99, (byte)130, 83, 99});
        //tlv格式 t: 1字节 l: 1字节
        //1.设置 dhcp message type 为request
        buffer.put(OptionType.DHCP_MSG_TYPE.value);
        buffer.put((byte)1);
        buffer.put(OperationType.DISCOVER.value);
        //2.设置request list
        List<Byte> parameterList = new ArrayList<>();
        parameterList.add(OptionType.SUBNET_MASK.value);
        parameterList.add(OptionType.CLASSLESS_STATIC.value);
        parameterList.add(OptionType.ROUTER.value);
        parameterList.add(OptionType.DOMAIN_NAME_SERVER.value);
        parameterList.add(OptionType.DOMAIN_NAME.value);
        parameterList.add(OptionType.DOMAIN_SEARCH.value);
        parameterList.add(OptionType.PRIVATE_PROXY_AUTO_DISCOVERY.value);
        parameterList.add(OptionType.LDAP.value);
        parameterList.add(OptionType.NET_BIOS_NAME_SRV.value);
        parameterList.add(OptionType.NET_BIOS_NODE_TYPE.value);
        buffer.put(OptionType.PARAMETER_LIST.value);
        buffer.put((byte)parameterList.size());
        for(Byte b: parameterList) {
            buffer.put(b);
        }
        //3.设置最大dhcp消息大小(单位: 字节)
        buffer.put(OptionType.DHCP_MAX_MSG_SIZE.value);
        buffer.put((byte)2);
        buffer.putShort((short)1500);
        //4.设置请求IP选项
        if(!ArrayUtil.isEmpty(message.getExpectIp())) {
            buffer.put(OptionType.ADDRESS_REQUEST.value);
            buffer.put((byte)4);
            buffer.put(message.getExpectIp());
        }
        //5.设置IP地址租约时间选项
        buffer.put(OptionType.ADDRESS_TIME.value);
        buffer.put((byte)4);
        //单位: 秒
        buffer.putInt((int) TimeUnit.SECONDS.convert(90, TimeUnit.DAYS));
        //6.end
        buffer.put(OptionType.END.value);
        buffer.flip();
        byte[] bytes = new byte[buffer.limit() - buffer.position()];
        buffer.get(bytes);
        return bytes;
    }
}
