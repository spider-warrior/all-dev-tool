package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.constants.*;
import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.model.CmdRequest;
import cn.t.tool.netproxytool.model.CmdResponse;
import cn.t.tool.netproxytool.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.promise.ChannelContextMessageSender;
import cn.t.tool.netproxytool.promise.ConnectionResultListener;
import cn.t.tool.netproxytool.server.initializer.ProxyToRemoteChannelInitializerBuilder;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;

/**
 * 命令请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdRequestHandler {
    public Object handle(CmdRequest message, ConnectionLifeCycle lifeCycle, String clientHost, int clientPort, ForwardingMessageHandler forwardingMessageHandler, ChannelContextMessageSender messageSender) {
        if(message.getRequestCmd() == Cmd.CONNECT) {
            String targetHost = new String(message.getTargetAddress());
            int targetPort = message.getTargetPort();
            log.info("处理【{}】消息， 地址类型: {}, 地址: {}， 目标端口: {}", Cmd.CONNECT, message.getAddressType(), targetHost, targetPort);
            ConnectionResultListener connectionResultListener = (status, sender) -> {
                CmdResponse cmdResponse = new CmdResponse();
                cmdResponse.setVersion(message.getVersion());
                cmdResponse.setExecutionStatus(status);
                cmdResponse.setRsv((byte)0);
                cmdResponse.setAddressType(AddressType.IPV4);
                cmdResponse.setTargetAddress(ServerConfig.SERVER_HOST_BYTES);
                cmdResponse.setTargetPort(ServerConfig.SERVER_PORT);
                forwardingMessageHandler.setMessageSender(sender);
                if(CmdExecutionStatus.SUCCEEDED == status) {
                    log.info("代理客户端成功, client: {}:{}, remote: {}:{}", clientHost, clientPort, targetHost, targetPort);
                    //切换步骤
                    lifeCycle.next(Step.FORWARDING_DATA);
                } else {
                    log.error("代理客户端失败, client: {}:{}, remote: {}:{}", clientHost, clientPort, targetHost, targetPort);
                }
                messageSender.send(cmdResponse);
            };
            String clientName = clientHost + ":" + clientPort + " -> " + targetHost + ":" + targetPort;
            NettyChannelInitializer channelInitializer = new ProxyToRemoteChannelInitializerBuilder(messageSender, connectionResultListener).build();
            NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, targetHost, targetPort, channelInitializer);
            ThreadUtil.submitTask(() -> nettyTcpClient.start(null));
            return null;
        } else {
            throw new ConnectionException("未实现的命令处理: " + message.getRequestCmd());
        }
    }
}
