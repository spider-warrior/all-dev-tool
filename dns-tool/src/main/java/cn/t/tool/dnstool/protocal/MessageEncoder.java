package cn.t.tool.dnstool.protocal;

import cn.t.tool.dnstool.model.InternetResolveResponse;
import cn.t.tool.dnstool.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author yj
 * @since 2020-01-01 10:54
 **/
@Slf4j
public class MessageEncoder {
    public void encode(Context context, Object message) throws IOException {
        if(message != null) {
            if(message instanceof Response) {
                Response result = (Response)message;
                context.write(result.toBytes());
            } else if(message instanceof InternetResolveResponse) {
                context.write(((InternetResolveResponse)message).getData());
            } else {
                log.error("为实现的编码对象: {}", message);
            }
        }
    }
}
