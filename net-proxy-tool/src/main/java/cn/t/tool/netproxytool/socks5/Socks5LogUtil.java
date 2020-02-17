package cn.t.tool.netproxytool.socks5;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-17 22:47
 **/
public class Socks5LogUtil {
    public synchronized static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
