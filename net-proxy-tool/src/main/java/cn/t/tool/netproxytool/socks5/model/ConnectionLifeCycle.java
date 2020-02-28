package cn.t.tool.netproxytool.socks5.model;

import cn.t.tool.netproxytool.socks5.constants.Socks5Method;
import cn.t.tool.netproxytool.socks5.constants.Socks5Step;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 21:13
 **/
public class ConnectionLifeCycle {

    /**
     * 阶段
     */
    private volatile Socks5Step currentSocks5Step;

    /**
     * 认证方式
     */
    private Socks5Method selectedSocks5Method;

    public Socks5Step getCurrentSocks5Step() {
        return currentSocks5Step;
    }

    public Socks5Method getSelectedSocks5Method() {
        return selectedSocks5Method;
    }

    public void setSelectedSocks5Method(Socks5Method selectedSocks5Method) {
        this.selectedSocks5Method = selectedSocks5Method;
    }

    public ConnectionLifeCycle() {
        currentSocks5Step = Socks5Step.NEGOTIATE;
    }

    public ConnectionLifeCycle(Socks5Step socks5Step) {
        if(socks5Step == null) {
            throw new NullPointerException("step cannot be null");
        }
        currentSocks5Step = socks5Step;
    }


    public void next(Socks5Step socks5Step) {
        if(socks5Step == null) {
            throw new NullPointerException();
        }
        this.currentSocks5Step = socks5Step;
    }
}
