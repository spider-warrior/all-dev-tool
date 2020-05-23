package cn.t.tool.nettytool.client;

import cn.t.tool.nettytool.launcher.Launcher;
import cn.t.tool.nettytool.server.listener.DemonListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NettyTcpClient extends AbstractDaemonClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpClient.class);

    private ChannelInitializer<SocketChannel> channelInitializer;
    private List<DemonListener> demonListenerList;
    private Channel clientChannel;

    @Override
    public void doStart(Launcher launcher) {
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(channelInitializer);
        try {
            logger.info("TCP Client: [{}] has been started successfully", name);
            if(launcher != null) {
                launcher.serverStartSuccess(this);
            }
            ChannelFuture openFuture = bootstrap.connect(host, port);
            clientChannel = openFuture.channel();
            ChannelFuture closeFuture = clientChannel.closeFuture();
            closeFuture.addListener(f ->  {
                    if (demonListenerList != null && !demonListenerList.isEmpty()) {
                        for (DemonListener listener: demonListenerList) {
                            listener.close(NettyTcpClient.this);
                        }
                    }
                }
            );
            openFuture.sync();
            if (demonListenerList != null && !demonListenerList.isEmpty()) {
                for (DemonListener listener: demonListenerList) {
                    listener.startup(this);
                }
            }
            closeFuture.sync();
        } catch (Exception e) {
            logger.error(String.format("TCP Client: [%s] is Down", name), e);
        } finally {
            if(launcher != null) {
                launcher.serverShutdownSuccess(NettyTcpClient.this);
            }
            logger.info("TCP Client: [{}] is closed", name);
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void doClose(Launcher launcher) {
        if (clientChannel != null) {
            clientChannel.close();
        }
    }

    public NettyTcpClient(String name, String host, int port, ChannelInitializer<SocketChannel> channelInitializer) {
        super(name, host, port);
        this.channelInitializer = channelInitializer;
    }

    public void setDemonListenerList(List<DemonListener> demonListenerList) {
        this.demonListenerList = demonListenerList;
    }

    public void sendMsg(Object msg) {
        if(clientChannel != null && clientChannel.isOpen()) {
            clientChannel.writeAndFlush(msg);
        } else {
            logger.warn("[{}], channel is not available, msg ignored, detail: {}", name, msg);
        }
    }
}
