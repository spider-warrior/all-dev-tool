package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.constants.AddressType;
import cn.t.tool.netproxytool.constants.Cmd;
import cn.t.tool.netproxytool.constants.CmdExecutionStatus;
import cn.t.tool.netproxytool.constants.Step;
import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.model.CmdRequest;
import cn.t.tool.netproxytool.model.CmdResponse;
import cn.t.tool.netproxytool.model.ConnectionLifeCycle;
import cn.t.tool.nettytool.client.NettyTcpClient;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

/**
 * 命令请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
public class CmdRequestHandler {
    public Object handle(CmdRequest message, ConnectionLifeCycle lifeCycle, ChannelHandlerContext channelHandlerContext) {
        if(message.getRequestCmd() == Cmd.CONNECT) {
            byte[] targetAddress = message.getTargetAddress();
            short targetPort = message.getTargetPort();
            Channel channel = channelHandlerContext.channel();
            NettyTcpClient nettyTcpClient = new NettyTcpClient("proxy-for: " + channel.remoteAddress(), new String(targetAddress), targetPort, new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new ChannelDuplexHandler() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            channelHandlerContext.writeAndFlush(msg);
                        }
                    });
                }

                @Override
                public void channelActive(ChannelHandlerContext ctx) {
                    lifeCycle.next(Step.TRANSFERRING_DATA);
                    CmdResponse cmdResponse = new CmdResponse();
                    cmdResponse.setVersion(message.getVersion());
                    cmdResponse.setExecutionStatus(CmdExecutionStatus.SUCCEEDED);
                    cmdResponse.setRsv((byte)0);
                    cmdResponse.setAddressType(AddressType.IPV4);
                    cmdResponse.setTargetAddress(new byte[]{127, 0, 0, 1});
                    cmdResponse.setTargetPort((short)8888);
                    channelHandlerContext.writeAndFlush(cmdResponse);
                }
            });
            nettyTcpClient.start(null);
            channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                    nettyTcpClient.sendMsg(msg);
                }
            });
            return null;
        } else {
            throw new ConnectionException("未实现的命令处理: " + message.getRequestCmd());
        }
    }
}
