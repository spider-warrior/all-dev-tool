package cn.t.tool.netproxytool.server.analyse;

import cn.t.tool.netproxytool.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.model.ConnectionLifeCycledMessage;
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
    private final ConnectionLifeCycle lifeCycle = new ConnectionLifeCycle();
    private final ConnectionLifeCycledMessage connectionLifeCycledMessage = new ConnectionLifeCycledMessage();

    @Override
    public Object analyse(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        Object message;
        switch (lifeCycle.getCurrentStep()) {
            case NEGOTIATE: message = negotiateRequestAnalyse.analyse(byteBuf); break;
            case AUTHENTICATION: message = authenticationRequestAnalyse.analyse(lifeCycle.getSelectedMethod(), byteBuf); break;
            case COMMAND_EXECUTION: message = cmdRequestAnalyse.analyse(byteBuf); break;
            case TRANSFERRING_DATA: message = byteBuf;
            default: message = null;
        }
        if(message != null && !(message instanceof ByteBuf)) {
            logger.info("成功解析消息，类型为: {}", message.getClass());
            connectionLifeCycledMessage.setLifeCycle(lifeCycle);
            connectionLifeCycledMessage.setMessage(message);
            message = connectionLifeCycledMessage;
        }
        return message;
    }

}
