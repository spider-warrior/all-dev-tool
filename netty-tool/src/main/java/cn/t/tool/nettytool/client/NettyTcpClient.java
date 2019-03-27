package cn.t.tool.nettytool.client;

import cn.t.tool.nettytool.launcher.Launcher;
import cn.t.tool.nettytool.server.listener.DemonListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NettyTcpClient extends AbstractDaemonClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpClient.class);

    private ChannelInitializer channelInitializer;
    private List<DemonListener> demonListenerList;
    private Channel clientChannel;

    @Override
    public void doStart(Launcher launcher) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(channelInitializer);
        try {
            if (demonListenerList != null && !demonListenerList.isEmpty()) {
                for (DemonListener listener: demonListenerList) {
                    listener.startup(this);
                }
            }
            logger.info(String.format("TCP Client: %s has been started successfully,host: %s port: %d", name, host, port));
            if(launcher != null) {
                launcher.serverStartSuccess(this);
            }
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            clientChannel = channelFuture.channel();
            clientChannel.closeFuture().sync().addListener(f ->  {
                    if (demonListenerList != null && !demonListenerList.isEmpty()) {
                        for (DemonListener listener: demonListenerList) {
                            listener.close(NettyTcpClient.this);
                        }
                    }
                }
            );
            clientChannel.closeFuture().sync().addListener(
                future -> {
                    if (demonListenerList != null && !demonListenerList.isEmpty()) {
                        for (DemonListener listener: demonListenerList) {
                            listener.close(NettyTcpClient.this);
                        }
                    }
                }
            );
        } catch (Exception e) {
            logger.error(String.format("TCP Client: %s Down,host: %s port: %d ", name, host, port), e);
        }finally {
            if(launcher != null) {
                launcher.serverShutdownSuccess(NettyTcpClient.this);
            }
            logger.info(String.format("[TCP Client]: %s closed, port: %d ", name, port));
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void doClose(Launcher launcher) {
        if (clientChannel != null) {
            clientChannel.close();
        }
    }

    public NettyTcpClient(String name, String host, int port, ChannelInitializer channelInitializer) {
        super(name, host, port);
        this.channelInitializer = channelInitializer;
    }

    public void setDemonListenerList(List<DemonListener> demonListenerList) {
        this.demonListenerList = demonListenerList;
    }
}
