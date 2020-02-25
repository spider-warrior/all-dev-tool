package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.socks5.constants.*;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import cn.t.tool.netproxytool.socks5.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.socks5.promise.ChannelContextMessageSender;
import cn.t.tool.netproxytool.socks5.promise.ConnectionResultListener;
import cn.t.tool.netproxytool.socks5.server.initializer.ProxyToRemoteChannelInitializerBuilder;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 命令请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdRequestHandler {
    public Object handle(CmdRequest message, ConnectionLifeCycle lifeCycle, InetSocketAddress remoteAddress, ForwardingMessageHandler forwardingMessageHandler, ChannelContextMessageSender messageSender) {
        if(message.getRequestCmd() == Cmd.CONNECT) {
            String targetHost = new String(message.getTargetAddress());
            int targetPort = message.getTargetPort();
            log.info("[{}]: [{}], 地址类型: {}, 地址: {}， 目标端口: {}", remoteAddress, Cmd.CONNECT, message.getAddressType(), targetHost, targetPort);
            ConnectionResultListener connectionResultListener = (status, sender) -> {
                CmdResponse cmdResponse = new CmdResponse();
                cmdResponse.setVersion(message.getVersion());
                cmdResponse.setExecutionStatus(status);
                cmdResponse.setRsv((byte)0);
                cmdResponse.setAddressType(AddressType.IPV4);
                cmdResponse.setTargetAddress(Socks5ServerConfig.SERVER_HOST_BYTES);
                cmdResponse.setTargetPort(Socks5ServerConfig.SERVER_PORT);
                forwardingMessageHandler.setMessageSender(sender);
                if(CmdExecutionStatus.SUCCEEDED == status) {
                    log.info("[{}]: 代理客户端成功, remote: {}:{}", remoteAddress, targetHost, targetPort);
                    //切换步骤
                    lifeCycle.next(Step.FORWARDING_DATA);
                } else {
                    log.error("[{}]: 代理客户端失败, remote: {}:{}", remoteAddress, targetHost, targetPort);
                }
                messageSender.send(cmdResponse);
            };
            String clientName = remoteAddress.getHostString() + ":" + remoteAddress.getPort() + " -> " + targetHost + ":" + targetPort;
            NettyChannelInitializer channelInitializer = new ProxyToRemoteChannelInitializerBuilder(messageSender, connectionResultListener).build();
            NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, targetHost, targetPort, channelInitializer);
            ThreadUtil.submitTask(() -> nettyTcpClient.start(null));
            return null;
        } else {
            throw new ConnectionException("未实现的命令处理: " + message.getRequestCmd());
        }
    }
}
