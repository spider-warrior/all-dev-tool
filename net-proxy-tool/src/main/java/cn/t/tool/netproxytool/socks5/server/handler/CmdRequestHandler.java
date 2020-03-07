package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.exception.ProxyException;
import cn.t.tool.netproxytool.socks5.constants.Socks5AddressType;
import cn.t.tool.netproxytool.socks5.constants.Socks5Cmd;
import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.constants.Socks5ServerConfig;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import cn.t.tool.netproxytool.socks5.server.initializer.ProxyToRemoteChannelInitializerBuilder;
import cn.t.tool.netproxytool.socks5.server.promise.Socks5ProxyForwardingResultListener;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 命令请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdRequestHandler extends SimpleChannelInboundHandler<CmdRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CmdRequest msg) throws Exception {
        if(msg.getRequestSocks5Cmd() == Socks5Cmd.CONNECT) {
            InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
            String targetHost = new String(msg.getTargetAddress());
            int targetPort = msg.getTargetPort();
            log.info("[{}]: [{}], 地址类型: {}, 地址: {}， 目标端口: {}", remoteAddress, Socks5Cmd.CONNECT, msg.getSocks5AddressType(), targetHost, targetPort);
            ProxyBuildResultListener proxyBuildResultListener = (status, sender) -> {
                Socks5CmdExecutionStatus socks5CmdExecutionStatus = Socks5CmdExecutionStatus.getSocks5CmdExecutionStatus(status);
                CmdResponse cmdResponse = new CmdResponse();
                cmdResponse.setVersion(msg.getVersion());
                cmdResponse.setExecutionStatus(socks5CmdExecutionStatus);
                cmdResponse.setRsv((byte)0);
                cmdResponse.setSocks5AddressType(Socks5AddressType.IPV4);
                cmdResponse.setTargetAddress(Socks5ServerConfig.SERVER_HOST_BYTES);
                cmdResponse.setTargetPort(Socks5ServerConfig.SERVER_PORT);
                if(Socks5CmdExecutionStatus.SUCCEEDED.value == status) {
                    log.info("[{}]: 代理客户端成功, remote: {}:{}", remoteAddress, targetHost, targetPort);
                    ChannelPromise promise = ctx.newPromise();
                    promise.addListener( new Socks5ProxyForwardingResultListener(ctx, sender, targetHost, targetPort));
                    ctx.writeAndFlush(cmdResponse, promise);
                } else {
                    log.error("[{}]: 代理客户端失败, remote: {}:{}", remoteAddress, targetHost, targetPort);
                    ctx.writeAndFlush(cmdResponse);
                }
            };
            String clientName = remoteAddress.getHostString() + ":" + remoteAddress.getPort() + " -> " + targetHost + ":" + targetPort;
            NettyChannelInitializer channelInitializer = new ProxyToRemoteChannelInitializerBuilder(ctx, proxyBuildResultListener).build();
            NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, targetHost, targetPort, channelInitializer);
            ThreadUtil.submitProxyTask(() -> nettyTcpClient.start(null));
        } else {
            throw new ProxyException("未实现的命令处理: " + msg.getRequestSocks5Cmd());
        }
    }

}
