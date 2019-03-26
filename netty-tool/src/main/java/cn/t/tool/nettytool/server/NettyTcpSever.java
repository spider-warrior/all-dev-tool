package cn.t.tool.nettytool.server;

import cn.t.tool.nettytool.launcher.Launcher;
import cn.t.tool.nettytool.server.listener.DemonListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NettyTcpSever extends AbstractDaemonServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpSever.class);

    private ChannelInitializer channelInitializer;
    private List<DemonListener> demonListenerList;
    private Channel serverChannel;

    public void doStart(Launcher launcher) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future = bootstrap.bind(getPort()).sync();
            logger.info(String.format("TCP Server: %s has been started successfully, port: %d", name, port));
            if (demonListenerList != null && !demonListenerList.isEmpty()) {
                for (DemonListener listener: demonListenerList) {
                    listener.startup(this);
                }
            }
            if(launcher != null) {
                launcher.serverStartSuccess(this);
            }
            serverChannel = future.channel();
            serverChannel.closeFuture().sync().addListener(f -> {
                if (demonListenerList != null && !demonListenerList.isEmpty()) {
                    for (DemonListener listener: demonListenerList) {
                        listener.close(NettyTcpSever.this);
                    }
                }
            });
        } catch (Exception e) {
            logger.error(String.format("TCP Server: %s Down, port: %d ", name, port), e);
        } finally {
            if(launcher != null) {
                launcher.serverShutdownSuccess(NettyTcpSever.this);
            }
            logger.info(String.format("[TCP Server]: %s closed, port: %d ", name, port));
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void doClose(Launcher launcher) {
        if(serverChannel != null) {
            serverChannel.close();
        }
    }

    public NettyTcpSever(String name, int port, ChannelInitializer channelInitializer) {
        super(name, port);
        this.channelInitializer = channelInitializer;
    }


    public NettyTcpSever setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
        return this;
    }

    public NettyTcpSever setDemonListenerList(List<DemonListener> demonListenerList) {
        this.demonListenerList = demonListenerList;
        return this;
    }
}
