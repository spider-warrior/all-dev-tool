package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.model.CmdRequest;
import cn.t.tool.netproxytool.model.NegotiateRequest;
import cn.t.tool.netproxytool.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.model.ConnectionLifeCycledMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yj
 * @since 2020-01-12 16:28
 **/
public class ProxyMessageHandler extends SimpleChannelInboundHandler<ConnectionLifeCycledMessage> {

    private final NegotiateRequestHandler negotiateRequestHandler = new NegotiateRequestHandler();
    private final AuthenticationRequestHandler authenticationRequestHandler = new AuthenticationRequestHandler();
    private final CmdRequestHandler cmdRequestHandler = new CmdRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ConnectionLifeCycledMessage lifeCycledMessage) {
        ConnectionLifeCycle lifeCycle = lifeCycledMessage.getLifeCycle();
        Object message;
        switch (lifeCycle.getCurrentStep()) {
            case NEGOTIATE: message = negotiateRequestHandler.handle((NegotiateRequest)lifeCycledMessage.getMessage(), lifeCycle); break;
            case AUTHENTICATION: message = authenticationRequestHandler.handle(lifeCycledMessage.getMessage(), lifeCycle); break;
            case COMMAND_EXECUTION: message = cmdRequestHandler.handle((CmdRequest)lifeCycledMessage.getMessage(), lifeCycle, channelHandlerContext); break;
            default: message = null;
        }
        if(message != null) {
            channelHandlerContext.writeAndFlush(message);
        }
    }

}
