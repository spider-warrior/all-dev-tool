package cn.t.tool.dhcptool.listener;

import cn.t.util.common.reflect.GenericUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @since 2020-01-15 21:13
 **/
public class EventBroadcaster {
    private final List<DhcpEventListener> dhcpEventListenerList = new ArrayList<>();
    public void broadcast(Object event) {
        if(event != null) {
            for(DhcpEventListener listener: dhcpEventListenerList) {
                Class<?> eventClass = GenericUtil.findTypeParam(listener, DhcpEventListener.class, "E");
                if(eventClass.isAssignableFrom(event.getClass())) {
                    listener.onEvent(event);
                }
            }
        }
    }
    public void addEventListener(DhcpEventListener listener) {
        if(listener != null) {
            dhcpEventListenerList.add(listener);
        }
    }
}
