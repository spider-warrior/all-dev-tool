package cn.t.tool.dnstool.protocal.handler.in;

import cn.t.tool.dnstool.Ipv4DomainHelper;
import cn.t.tool.dnstool.QueryClass;
import cn.t.tool.dnstool.QueryType;
import cn.t.tool.dnstool.model.Message;
import cn.t.tool.dnstool.protocal.MessageHandler;
import cn.t.util.common.StringUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author yj
 * @since 2020-01-01 11:37
 **/
public class InternetIpV4DomainQueryHandler implements MessageHandler {

    private Ipv4DomainHelper ipv4DomainHelper = new Ipv4DomainHelper();

    @Override
    public boolean support(Message message) {
        //class: internet && type: A
        return message != null && QueryClass.IN == message.getClazz() && QueryType.A == message.getType();
    }

    @Override
    public Message handler(Message message) throws UnknownHostException {
        String domain = message.getDomain();
        //读取配置域名
        String ip = ipv4DomainHelper.getCustomDomainMapping(domain);
        if(StringUtil.isEmpty(ip)) {
            //加载
            InetAddress[] addresses = addresses = InetAddress.getAllByName(domain);
            
        }
        return null;
    }

    public static void main(String[] args) throws UnknownHostException {
        InetAddress.getByName("www.baidu.com");
        InetAddress.getAllByName("www.baidu.com");
    }
}
