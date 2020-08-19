package cn.t.tool.nettytool.daemon.server;

import cn.t.tool.nettytool.daemon.listener.DaemonListener;
import cn.t.util.common.CollectionUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyTcpServer extends AbstractDaemonServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyTcpServer.class);

    private ChannelInitializer<SocketChannel> channelInitializer;
    private Channel serverChannel;
    private final Map<AttributeKey<?>, Object> childAttrs = new ConcurrentHashMap<>();

    public void doStart() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new DefaultThreadFactory("NettyServerWorker", true));
        ServerBootstrap bootstrap = new ServerBootstrap();

        //具体配置参考io.netty.channel.ChannelConfig(for SocketChannel)和io.netty.channel.socket.ServerSocketChannelConfig(for ServerSocketChannel)说明。

        //ChannelOption.TCP_NODELAY
        //延时要求较高时开启此选项，用来禁用Nagle算法

        //ChannelOption.SO_REUSEADDR
        //kill掉进程后端口会进入短暂的TIME_WAIT状态, 开启此选项果此端口正在使用的话，bind就会把端口“抢”过来

        //ChannelOption.SO_BACKLOG
        //TCP下分为syns queue（半连接队列）与accept queue（全连接队列），backlog的定义是已连接但未进行accept处理的socket队列大小，如果这个队列满了，将会发送一个ECONNREFUSED错误信息给到客户端
        //Accept queue 队列长度由 /proc/sys/net/core/somaxconn 和使用listen函数时传入的参数，二者取最小值。默认为128。

        //ChannelOption.SO_KEEPALIVE
        //鸡肋，该选项依赖系统内核，不容易修改，不推荐使用，使用IdleHandler代替即可
        //当server检测到超过一定时间(/proc/sys/net/ipv4/tcp_keepalive_time 7200 即2小时)没有数据传输,那么会向client端发送一个keepalive packet
        bootstrap.group(bossGroup,workerGroup)
            .channel(NioServerSocketChannel.class)
            .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
            .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
            .option(ChannelOption.SO_BACKLOG,1024)
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childHandler(channelInitializer);
        if(!CollectionUtil.isEmpty(childAttrs)) {
            for(Map.Entry<AttributeKey<?>, Object> entry: childAttrs.entrySet()) {
                @SuppressWarnings("unchecked")
                AttributeKey<Object> key = (AttributeKey<Object>) entry.getKey();
                bootstrap.childAttr(key, entry.getValue());
            }
        }
        try {
            logger.info("TCP Server: [{}] is going start", name);
            ChannelFuture openFuture = bootstrap.bind(port);
            serverChannel = openFuture.channel();
            ChannelFuture closeFuture = serverChannel.closeFuture();
            closeFuture.addListener(f -> {
                if (!CollectionUtil.isEmpty(daemonListenerList)) {
                    for (DaemonListener listener: daemonListenerList) {
                        listener.close(this);
                    }
                }
            });
            openFuture.sync();
            logger.info("TCP Server: {} has been started successfully, port: {}", name, port);
            if (!CollectionUtil.isEmpty(daemonListenerList)) {
                for (DaemonListener listener: daemonListenerList) {
                    listener.startup(this);
                }
            }
            closeFuture.sync();
        } catch (Exception e) {
            logger.error(String.format("TCP Server: [%s] is Down", name), e);
        } finally {
            logger.info(String.format("TCP Server: [%s] is closed, port: %d ", name, port));
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void doClose() {
        if(serverChannel != null) {
            serverChannel.close();
        }
    }

    public NettyTcpServer(String name, int port, ChannelInitializer<SocketChannel> channelInitializer) {
        super(name, port);
        this.channelInitializer = channelInitializer;
    }

    public NettyTcpServer setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
        return this;
    }

    public <T> NettyTcpServer childAttr(AttributeKey<T> childKey, T value) {
        childAttrs.put(childKey, value);
        return this;
    }
}
