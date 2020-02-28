package cn.t.tool.netproxytool.socks5.server.analyse;

import cn.t.tool.netproxytool.socks5.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.socks5.model.ConnectionLifeCycledMessage;
import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yj
 * @since 2020-01-12 16:26
 **/
public class ProxyMessageAnalyser extends ByteBufAnalyser {

    private static final Logger logger = LoggerFactory.getLogger(ProxyMessageAnalyser.class);
    private final NegotiateRequestAnalyse negotiateRequestAnalyse = new NegotiateRequestAnalyse();
    private final AuthenticationRequestAnalyse authenticationRequestAnalyse = new AuthenticationRequestAnalyse();
    private final CmdRequestAnalyse cmdRequestAnalyse = new CmdRequestAnalyse();
    private final  ConnectionLifeCycle lifeCycle = new ConnectionLifeCycle();
    private final ConnectionLifeCycledMessage connectionLifeCycledMessage = new ConnectionLifeCycledMessage();

    @Override
    public Object analyse(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        Object message;
        switch (lifeCycle.getCurrentSocks5Step()) {
            case NEGOTIATE: message = negotiateRequestAnalyse.analyse(byteBuf); break;
            case AUTHENTICATION: message = authenticationRequestAnalyse.analyse(lifeCycle.getSelectedSocks5Method(), byteBuf); break;
            case COMMAND_EXECUTION: message = cmdRequestAnalyse.analyse(byteBuf); break;
            case FORWARDING_DATA: {
                message = byteBuf.retainedDuplicate();
                byteBuf.skipBytes(byteBuf.readableBytes());
                break;
            }
            default: message = null;
        }
        if(message != null && !(message instanceof ByteBuf)) {
            logger.info("[{}]: 成功解析消息: {}", channelHandlerContext.channel().remoteAddress(), message.getClass().getSimpleName());
            connectionLifeCycledMessage.setLifeCycle(lifeCycle);
            connectionLifeCycledMessage.setMessage(message);
            message = connectionLifeCycledMessage;
        }
        return message;
    }

}
