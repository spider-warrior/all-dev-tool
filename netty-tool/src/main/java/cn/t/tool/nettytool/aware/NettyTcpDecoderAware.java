package cn.t.tool.nettytool.aware;

import cn.t.tool.nettytool.decoder.NettyTcpDecoder;

/**
 * NettyTcpDecoderAware
 * 1.在ByteBufAnalyser类型生效要使用NettyChannelInitializerBuilder
 * 2.在handler类型生效要使用NettyChannelInitializer
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-03-07 16:07
 **/
public interface NettyTcpDecoderAware {
    void setNettyTcpDecoder(NettyTcpDecoder nettyTcpDecoder);
}
