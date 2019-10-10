package cn.t.tool.nettytool.server;

import cn.t.tool.nettytool.launcher.Launcher;
import cn.t.tool.nettytool.server.listener.DemonListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NettyTcpServer extends AbstractDaemonServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpServer.class);

    private ChannelInitializer channelInitializer;
    private List<DemonListener> demonListenerList;
    private Channel serverChannel;

    public void doStart(Launcher launcher) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new DefaultThreadFactory("NettyServerWorker", true));
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            //ChannelOption.TCP_NODELAY
            //延时要求较高时开启此选项，用来禁用Nagle算法

            //ChannelOption.SO_REUSEADDR
            //kill掉进程后端口会进入短暂的TIME_WAIT状态, 开启下面选项果此端口正在使用的话，bind就会把端口“抢”过来

            //ChannelOption.SO_BACKLOG
            //TCP下分为syns queue（半连接队列）与accept queue（全连接队列），backlog的定义是已连接但未进行accept处理的socket队列大小，如果这个队列满了，将会发送一个ECONNREFUSED错误信息给到客户端
            //Accept queue 队列长度由 /proc/sys/net/core/somaxconn 和使用listen函数时传入的参数，二者取最小值。默认为128。

            //ChannelOption.SO_KEEPALIVE
            //当server检测到超过一定时间(/proc/sys/net/ipv4/tcp_keepalive_time 7200 即2小时)没有数据传输,那么会向client端发送一个keepalive packet
            bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
//                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(channelInitializer);
            ChannelFuture future = bootstrap.bind(getPort()).sync();
            logger.info("TCP Server: {} has been started successfully, port: {}", name, port);
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
                        listener.close(NettyTcpServer.this);
                    }
                }
            });
        } catch (Exception e) {
            logger.error(String.format("TCP Server: %s Down, port: %d ", name, port), e);
        } finally {
            if(launcher != null) {
                launcher.serverShutdownSuccess(NettyTcpServer.this);
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

    public NettyTcpServer(String name, int port, ChannelInitializer channelInitializer) {
        super(name, port);
        this.channelInitializer = channelInitializer;
    }


    public NettyTcpServer setChannelInitializer(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
        return this;
    }

    public NettyTcpServer setDemonListenerList(List<DemonListener> demonListenerList) {
        this.demonListenerList = demonListenerList;
        return this;
    }
}
