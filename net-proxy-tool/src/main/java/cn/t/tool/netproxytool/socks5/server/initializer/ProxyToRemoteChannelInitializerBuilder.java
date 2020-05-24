package cn.t.tool.netproxytool.socks5.server.initializer;

import cn.t.tool.netproxytool.event.ProxyBuildResultListener;
import cn.t.tool.netproxytool.handler.DecryptedMessageEncoder;
import cn.t.tool.netproxytool.handler.EncryptMessageAnalyser;
import cn.t.tool.netproxytool.socks5.constants.Socks5ProxyConfig;
import cn.t.tool.netproxytool.socks5.server.handler.Socks5EncryptFetchMessageHandler;
import cn.t.tool.netproxytool.socks5.server.handler.Socks5FetchMessageHandler;
import cn.t.tool.nettytool.initializer.NettyChannelInitializerBuilder;
import cn.t.util.common.ArrayUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yj
 * @since 2020-01-12 16:31
 **/
public class ProxyToRemoteChannelInitializerBuilder extends NettyChannelInitializerBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ProxyToRemoteChannelInitializerBuilder.class);

    public ProxyToRemoteChannelInitializerBuilder(ChannelHandlerContext remoteChannelHandlerContext, ProxyBuildResultListener proxyBuildResultListener, byte[] security) {
        setLoggingHandlerLogLevel(Socks5ProxyConfig.LOGGING_HANDLER_LOGGER_LEVEL);
        setIdleState(Socks5ProxyConfig.SOCKS5_PROXY_READ_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_WRITE_TIME_OUT_IN_SECONDS, Socks5ProxyConfig.SOCKS5_PROXY_ALL_IDLE_TIME_OUT_IN_SECONDS);
        if(ArrayUtil.isEmpty(security)) {
            addChannelHandlerSupplier(() -> new Socks5FetchMessageHandler(remoteChannelHandlerContext, proxyBuildResultListener));
        } else {
            //消息解密
            addChannelHandlerSupplier(() -> {
                try {
                    //消息加密
                    return new Socks5EncryptFetchMessageHandler(remoteChannelHandlerContext, proxyBuildResultListener, security);
                } catch (Exception e) {
                    logger.error("构建Socks5EncryptFetchMessageHandler异常", e);
                    return null;
                }
            });
            addEncoderListsSupplier(() -> {
                try {
                    return new DecryptedMessageEncoder(security);
                } catch (Exception e) {
                    logger.error("构建DecryptedMessageHandler异常", e);
                    return null;
                }
            });
        }
    }
}
