package cn.t.tool.netproxytool.http.server.initializer;

import cn.t.tool.netproxytool.http.constants.HttpServerConfig;
import cn.t.tool.netproxytool.http.server.handler.HttpRequestHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializer;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class HttpProxyChannelInitializer extends NettyChannelInitializer {

    public HttpProxyChannelInitializer() {
        super(HttpServerConfig.LOGGER_LEVEL,
            null,
            () ->{return new IdleStateHandler(HttpServerConfig.HTTP_PROXY_READ_TIME_OUT_IN_SECONDS, HttpServerConfig.HTTP_PROXY_WRITE_TIME_OUT_IN_SECONDS, HttpServerConfig.HTTP_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS);},
            null,
            null,
            () -> {
                List<ChannelInboundHandler> channelInboundHandlerList = new ArrayList<>();
                channelInboundHandlerList.add(new HttpServerCodec());
                channelInboundHandlerList.add(new HttpObjectAggregator(1024 * 1024));
                channelInboundHandlerList.add(new HttpRequestHandler());
                return channelInboundHandlerList;
            });
    }

}
