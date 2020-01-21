package cn.t.tool.dhcptool.listener;

/**
 * dhcp事件
 *
 * @author yj
 * @since 2020-01-15 20:59
 **/
public abstract class DhcpEventListener<E> {
    public abstract void onEvent(E e);
}
