package cn.t.tool.netproxytool.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * 用来连接客户端和目标服务器转发流量
 *
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-17 22:50
 **/
public class SocketForwarding {
    // 客户端socket
    private Socket clientSocket;
    private String clientIp;
    // 目标地址socket
    private Socket targetSocket;
    private String targetAddress;
    private int targetPort;

    public SocketForwarding(Socket clientSocket, Socket targetSocket) {
        this.clientSocket = clientSocket;
        this.clientIp = clientSocket.getInetAddress().getHostAddress();
        this.targetSocket = targetSocket;
        this.targetAddress = targetSocket.getInetAddress().getHostAddress();
        this.targetPort = targetSocket.getPort();
    }

    public void start() {
        OutputStream clientOs = null;
        InputStream clientIs = null;
        InputStream targetIs = null;
        OutputStream targetOs = null;
        long start = System.currentTimeMillis();
        try {

            clientOs = clientSocket.getOutputStream();
            clientIs = clientSocket.getInputStream();
            targetOs = targetSocket.getOutputStream();
            targetIs = targetSocket.getInputStream();

            // 512K，因为会有很多个线程同时申请buff空间，所以不要太大以以防OOM
            byte[] buff = new byte[1024 * 512];
            while (true) {

                boolean needSleep = true;
                while (clientIs.available() != 0) {
                    int n = clientIs.read(buff);
                    targetOs.write(buff, 0, n);
                    transientLog("client to remote, bytes=%d", n);
                    needSleep = false;
                }

                while (targetIs.available() != 0) {
                    int n = targetIs.read(buff);
                    clientOs.write(buff, 0, n);
                    transientLog("remote to client, bytes=%d", n);
                    needSleep = false;
                }

                if (clientSocket.isClosed()) {
                    transientLog("client closed");
                    break;
                }

                // 会话最多30秒超时，防止有人占着线程老不释放
                if (System.currentTimeMillis() - start > 30_000) {
                    transientLog("time out");
                    break;
                }

                // 如果本次循环没有数据传输，说明管道现在不繁忙，应该休息一下把资源让给别的线程
                if (needSleep) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (IOException e) {
            transientLog("conn exception" + e.getMessage());
        } finally {
            ConnectionUtil.close(clientIs);
            ConnectionUtil.close(clientOs);
            ConnectionUtil.close(targetIs);
            ConnectionUtil.close(targetOs);
            ConnectionUtil.close(clientSocket);
            ConnectionUtil.close(targetSocket);
        }
        transientLog("done.");
    }

    private void transientLog(String format, Object... args) {
        Socks5LogUtil.log("forwarding, clientIp=" + clientIp + ", targetAddress=" + targetAddress + ", port=" + targetPort + ", " + format, args);
    }

}
