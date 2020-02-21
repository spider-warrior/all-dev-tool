package cn.t.tool.netproxytool.model;

import cn.t.tool.netproxytool.constants.Method;
import cn.t.tool.netproxytool.constants.Step;
import cn.t.tool.nettytool.client.NettyTcpClient;

import static cn.t.tool.netproxytool.constants.Step.*;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-20 21:13
 **/
public class ConnectionLifeCycle {

    /**
     * 阶段
     */
    private volatile Step currentStep;

    /**
     * 认证方式
     */
    private Method selectedMethod;

    /**
     * 远程客户端
     */
    private NettyTcpClient nettyTcpClient;

    public Step getCurrentStep() {
        return currentStep;
    }

    public Method getSelectedMethod() {
        return selectedMethod;
    }

    public void setSelectedMethod(Method selectedMethod) {
        this.selectedMethod = selectedMethod;
    }

    public ConnectionLifeCycle() {
        currentStep = Step.NEGOTIATE;
    }

    public ConnectionLifeCycle(Step step) {
        if(step == null) {
            throw new NullPointerException("step cannot be null");
        }
        currentStep = step;
    }

    public NettyTcpClient getNettyTcpClient() {
        return nettyTcpClient;
    }

    public void setNettyTcpClient(NettyTcpClient nettyTcpClient) {
        this.nettyTcpClient = nettyTcpClient;
    }

    //    /**
//     * 下一步
//     */
//    public void next() {
//        switch (currentStep) {
//            case NEGOTIATE: this.currentStep = AUTHENTICATION; break;
//            case AUTHENTICATION: this.currentStep = COMMAND_EXECUTION; break;
//            case COMMAND_EXECUTION: this.currentStep = TRANSFERRING_DATA; break;
//            case TRANSFERRING_DATA:
//            default:
//        }
//    }

    public void next(Step step) {
        if(step == null) {
            throw new NullPointerException();
        }
        this.currentStep = step;
    }
}
