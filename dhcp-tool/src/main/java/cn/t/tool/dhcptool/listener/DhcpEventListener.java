package cn.t.tool.dhcptool.listener;

/**
 * dhcp事件
 *
 * @author yj
 * @since 2020-01-15 20:59
 **/
public interface DhcpEventListener<E> {
    void onEvent(E e);
}
