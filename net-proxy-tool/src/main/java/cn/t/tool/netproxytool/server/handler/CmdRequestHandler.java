package cn.t.tool.netproxytool.server.handler;

import cn.t.tool.netproxytool.constants.AddressType;
import cn.t.tool.netproxytool.constants.Cmd;
import cn.t.tool.netproxytool.constants.CmdExecutionStatus;
import cn.t.tool.netproxytool.constants.Step;
import cn.t.tool.netproxytool.exception.ConnectionException;
import cn.t.tool.netproxytool.model.CmdRequest;
import cn.t.tool.netproxytool.model.CmdResponse;
import cn.t.tool.netproxytool.model.ConnectionLifeCycle;
import cn.t.tool.netproxytool.promise.PromiseMessage;
import cn.t.tool.nettytool.client.NettyTcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 命令请求处理器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 22:30
 **/
@Slf4j
public class CmdRequestHandler {
    public Object handle(CmdRequest message, ConnectionLifeCycle lifeCycle, ChannelHandlerContext channelHandlerContext) {
        if(message.getRequestCmd() == Cmd.CONNECT) {
            log.info("处理【{}】消息， 地址类型: {}, 地址: {}， 目标端口: {}", Cmd.CONNECT, message.getAddressType(), new String(message.getTargetAddress()), message.getTargetPort());
            byte[] targetAddress = message.getTargetAddress();
            short targetPort = message.getTargetPort();
            Channel channel = channelHandlerContext.channel();
            PromiseMessage promiseMessage = new PromiseMessage();
            NettyTcpClient nettyTcpClient = new NettyTcpClient("proxy-for: " + channel.remoteAddress(), new String(targetAddress), targetPort, new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new SimpleChannelInboundHandler<Object>() {

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println();
                        }

//                        @Override
//                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
//                            //将远端消息发往客户端
//                            byte[] bytes = new byte[msg.readableBytes()];
//                            msg.readBytes(bytes);
//                            channelHandlerContext.writeAndFlush(bytes);
//                        }
                        @Override
                        public void channelActive(ChannelHandlerContext outerContext) {
                            ProxyMessageHandler proxyMessageHandler = channel.pipeline().get(ProxyMessageHandler.class);
                            channel.pipeline().replace(proxyMessageHandler, "real-transfer-handle", new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext innerContext, ByteBuf msg) {
                                    //将客户端消息发往远端
                                    byte[] bytes = new byte[msg.readableBytes()];
                                    msg.readBytes(bytes);
                                    outerContext.writeAndFlush(bytes);
                                }
                            });
                            lifeCycle.next(Step.TRANSFERRING_DATA);
                            promiseMessage.send();
                        }
                    });
                }
            });
            new Thread(() -> nettyTcpClient.start(null)).start();
            CmdResponse cmdResponse = new CmdResponse();
            cmdResponse.setVersion(message.getVersion());
            cmdResponse.setExecutionStatus(CmdExecutionStatus.SUCCEEDED);
            cmdResponse.setRsv((byte)0);
            cmdResponse.setAddressType(AddressType.IPV4);
            cmdResponse.setTargetAddress(new byte[]{(byte)192, (byte)168, 1, 103});
            cmdResponse.setTargetPort((short)8888);
            promiseMessage.setMessage(cmdResponse);
            return promiseMessage;
        } else {
            throw new ConnectionException("未实现的命令处理: " + message.getRequestCmd());
        }
    }
}
