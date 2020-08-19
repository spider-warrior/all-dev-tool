package cn.t.tool.netproxytool.socks5.server.analyse;

import cn.t.tool.netproxytool.socks5.constants.Socks5Method;
import cn.t.tool.netproxytool.socks5.model.NegotiateRequest;
import cn.t.tool.nettytool.analyser.ByteBufAnalyser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端协商请求解析器
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 20:53
 **/
public class NegotiateRequestAnalyse extends ByteBufAnalyser {

    @Override
    public Object analyse(ByteBuf byteBuf, ChannelHandlerContext channelHandlerContext) {
        if(byteBuf.readableBytes() < 2) {
            return null;
        }
        NegotiateRequest negotiateRequest = new NegotiateRequest();
        negotiateRequest.setVersion(byteBuf.readByte());
        byte methodLength = byteBuf.readByte();
        byte[] methodBytes = new byte[methodLength];
        byteBuf.readBytes(methodBytes);
        negotiateRequest.setSupportSocks5MethodList(Socks5Method.convertToMethod(methodBytes));
        return negotiateRequest;
    }

}
