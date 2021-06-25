package cn.t.tool.netproxytool.socks5.server.handler;

import cn.t.tool.netproxytool.event.ProxyConnectionBuildResultListener;
import cn.t.tool.netproxytool.exception.ProxyException;
import cn.t.tool.netproxytool.http.constants.ProxyBuildExecutionStatus;
import cn.t.tool.netproxytool.socks5.config.ServerConfig;
import cn.t.tool.netproxytool.socks5.config.UserConfig;
import cn.t.tool.netproxytool.socks5.constants.Socks5AddressType;
import cn.t.tool.netproxytool.socks5.constants.Socks5Cmd;
import cn.t.tool.netproxytool.socks5.constants.Socks5CmdExecutionStatus;
import cn.t.tool.netproxytool.socks5.constants.Socks5ServerDaemonConfig;
import cn.t.tool.netproxytool.socks5.model.CmdRequest;
import cn.t.tool.netproxytool.socks5.model.CmdResponse;
import cn.t.tool.netproxytool.socks5.server.listener.Socks5ProxyServerConnectionReadyListener;
import cn.t.tool.netproxytool.util.InitializerBuilder;
import cn.t.tool.netproxytool.util.NetProxyUtil;
import cn.t.tool.netproxytool.util.ThreadUtil;
import cn.t.tool.nettytool.daemon.client.NettyTcpClient;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import cn.t.util.common.ArrayUtil;
import cn.t.util.common.StringUtil;
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
    protected void channelRead0(ChannelHandlerContext ctx, CmdRequest msg) {
        String username = ctx.channel().attr(Socks5ServerDaemonConfig.CHANNEL_USERNAME).get();
        byte[] security = null;
        if(!StringUtil.isEmpty(username)) {
            ServerConfig serverConfig = ctx.channel().attr(Socks5ServerDaemonConfig.SERVER_CONFIG_KEY).get();
            if(serverConfig != null) {
                UserConfig userConfig = serverConfig.getUserConfigMap().get(username);
                if(userConfig != null) {
                    security = userConfig.getSecurity();
                }
            }
        }
        byte[] securityBytes = ArrayUtil.isEmpty(security) ? null : security;
        if(msg.getRequestSocks5Cmd() == Socks5Cmd.CONNECT) {
            InetSocketAddress clientAddress = (InetSocketAddress)ctx.channel().remoteAddress();
            String targetHost = new String(msg.getTargetAddress());
            int targetPort = msg.getTargetPort();
            log.info("[{}]: [{}], 地址类型: {}, 地址: {}:{}", clientAddress, Socks5Cmd.CONNECT, msg.getSocks5AddressType(), targetHost, targetPort);
            String clientName = NetProxyUtil.buildProxyConnectionName(clientAddress.getHostString(), clientAddress.getPort(), targetHost, targetPort);
            ProxyConnectionBuildResultListener proxyConnectionBuildResultListener = (status, remoteChannelHandlerContext) -> {
                CmdResponse cmdResponse = new CmdResponse();
                cmdResponse.setVersion(msg.getVersion());
                cmdResponse.setRsv((byte)0);
                cmdResponse.setSocks5AddressType(Socks5AddressType.IPV4.value);
                cmdResponse.setTargetAddress(Socks5ServerDaemonConfig.SERVER_HOST_BYTES);
                cmdResponse.setTargetPort(Socks5ServerDaemonConfig.SERVER_PORT);
                if(ProxyBuildExecutionStatus.SUCCEEDED.value == status) {
                    cmdResponse.setExecutionStatus(Socks5CmdExecutionStatus.SUCCEEDED.value);
                    log.info("[client: {}] -> [local: {}] -> [remote: {}]: 代理创建成功", ctx.channel().remoteAddress(), remoteChannelHandlerContext.channel().localAddress(), remoteChannelHandlerContext.channel().remoteAddress());
                    ChannelPromise promise = ctx.newPromise();
                    promise.addListener(new Socks5ProxyServerConnectionReadyListener(ctx, remoteChannelHandlerContext, clientName, securityBytes));
                    ctx.writeAndFlush(cmdResponse, promise);
                } else {
                    cmdResponse.setExecutionStatus(Socks5CmdExecutionStatus.GENERAL_SOCKS_SERVER_FAILURE.value);
                    log.error("[{}]: 代理客户端失败, remote: {}:{}", clientAddress, targetHost, targetPort);
                    ctx.writeAndFlush(cmdResponse);
                }
            };
            NettyChannelInitializer channelInitializer = InitializerBuilder.buildSocks5ProxyServerClientChannelInitializer(ctx, proxyConnectionBuildResultListener);
            NettyTcpClient nettyTcpClient = new NettyTcpClient(clientName, targetHost, targetPort, channelInitializer);
            ThreadUtil.submitProxyTask(nettyTcpClient::start);
        } else {
            throw new ProxyException("未实现的命令处理: " + msg.getRequestSocks5Cmd());
        }
    }

}
