package cn.t.tool.netproxytool.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:jian.yang@liby.ltd">野生程序员-杨建</a>
 * @version V1.0
 * @since 2020-02-17 22:40
 **/
public class ClientHandler implements Runnable {

    private Socks5ProxyServer socks5ProxyServer;

    private Socket clientSocket;
    private String clientIp;
    private int clientPort;

    public ClientHandler(Socket clientSocket, Socks5ProxyServer socks5ProxyServer) {
        this.clientSocket = clientSocket;
        this.clientIp = clientSocket.getInetAddress().getHostAddress();
        this.clientPort = clientSocket.getPort();
        this.socks5ProxyServer = socks5ProxyServer;
    }

    @Override
    public void run() {
        try {
            // 协商认证方法
            negotiationCertificationMethod();
            // 开始处理客户端的命令
            handleClientCommand();
        } catch (Exception e) {
            handleLog("exception, " + e.getMessage());
        } finally {
            ConnectionUtil.close(clientSocket);
            handleLog("client dead, current client count=%s", socks5ProxyServer.decrementAndGet());
        }
    }

    // 协商与客户端的认证方法
    private void negotiationCertificationMethod() throws IOException {
        InputStream is = clientSocket.getInputStream();
        OutputStream os = clientSocket.getOutputStream();
        byte[] buff = new byte[255];
        // 接收客户端的支持的方法
        is.read(buff, 0, 2);
        int version = buff[0];
        int methodNum = buff[1];

        if (version != Socks5Constants.VERSION) {
            throw new RuntimeException("version must 0X05");
        } else if (methodNum < 1) {
            throw new RuntimeException("method num must gt 0");
        }

        is.read(buff, 0, methodNum);
        List<METHOD> clientSupportMethodList = METHOD.convertToMethod(Arrays.copyOfRange(buff, 0, methodNum));
        handleLog("version=%s, methodNum=%s, clientSupportMethodList=%s", version, methodNum, clientSupportMethodList);

        // 向客户端发送回应，这里不进行认证
        buff[0] = Socks5Constants.VERSION;
        buff[1] = METHOD.NO_AUTHENTICATION_REQUIRED.rangeStart;
        os.write(buff, 0, 2);
        os.flush();
    }

    // 认证通过，开始处理客户端发送过来的指令
    private void handleClientCommand() throws IOException {
        InputStream is = clientSocket.getInputStream();
        OutputStream os = clientSocket.getOutputStream();
        byte[] buff = new byte[255];
        // 接收客户端命令
        is.read(buff, 0, 4);
        int version = buff[0];
        COMMAND command = COMMAND.convertToCmd(buff[1]);
        int rsv = buff[2];
        AddressType addressType = AddressType.convertToAddressType(buff[3]);
        if (rsv != Socks5Constants.RSV) {
            throw new RuntimeException("RSV must 0X05");
        } else if (version != Socks5Constants.VERSION) {
            throw new RuntimeException("VERSION must 0X05");
        } else if (command == null) {
            // 不支持的命令
            sendCommandResponse(CommandStatus.COMMAND_NOT_SUPPORTED);
            handleLog("not supported command");
            return;
        } else if (addressType == null) {
            // 不支持的地址类型
            sendCommandResponse(CommandStatus.ADDRESS_TYPE_NOT_SUPPORTED);
            handleLog("address type not supported");
            return;
        }

        String targetAddress = "";
        switch (addressType) {
            case DOMAIN:
                // 如果是域名的话第一个字节表示域名的长度为n，紧接着n个字节表示域名
                is.read(buff, 0, 1);
                int domainLength = buff[0];
                is.read(buff, 0, domainLength);
                targetAddress = new String(Arrays.copyOfRange(buff, 0, domainLength));
                break;
            case IPV4:
                // 如果是ipv4的话使用固定的4个字节表示地址
                is.read(buff, 0, 4);
                targetAddress = ipAddressBytesToString(buff);
                break;
            case IPV6:
                throw new RuntimeException("not support ipv6.");
        }

        is.read(buff, 0, 2);
        int targetPort = ((buff[0] & 0XFF) << 8) | (buff[1] & 0XFF);

        StringBuilder msg = new StringBuilder();
        msg.append("version=").append(version).append(", cmd=").append(command.name())
            .append(", addressType=").append(addressType.name())
            .append(", domain=").append(targetAddress).append(", port=").append(targetPort);
        handleLog(msg.toString());

        // 响应客户端发送的命令，暂时只实现CONNECT命令
        switch (command) {
            case CONNECT:
                handleConnectCommand(targetAddress, targetPort);
            case BIND:
                throw new RuntimeException("not support command BIND");
            case UDP_ASSOCIATE:
                throw new RuntimeException("not support command UDP_ASSOCIATE");
        }

    }

    // convert ip address from 4 byte to string
    private String ipAddressBytesToString(byte[] ipAddressBytes) {
        // first convert to int avoid negative
        return (ipAddressBytes[0] & 0XFF) + "." + (ipAddressBytes[1] & 0XFF) + "." + (ipAddressBytes[2] & 0XFF) + "." + (ipAddressBytes[3] & 0XFF);
    }

    // 处理CONNECT命令
    private void handleConnectCommand(String targetAddress, int targetPort) throws IOException {
        Socket targetSocket = null;
        try {
            targetSocket = new Socket(targetAddress, targetPort);
        } catch (IOException e) {
            sendCommandResponse(CommandStatus.GENERAL_SOCKS_SERVER_FAILURE);
            return;
        }
        sendCommandResponse(CommandStatus.SUCCEEDED);
        new SocketForwarding(clientSocket, targetSocket).start();
    }

    private void sendCommandResponse(CommandStatus commandStatus) throws IOException {
        OutputStream os = clientSocket.getOutputStream();
        os.write(buildCommandResponse(commandStatus.rangeStart));
        os.flush();
    }

    private byte[] buildCommandResponse(byte commandStatusCode) {
        ByteBuffer payload = ByteBuffer.allocate(100);
        payload.put(Socks5Constants.VERSION);
        payload.put(commandStatusCode);
        payload.put(Socks5Constants.RSV);
//          payload.put(ADDRESS_TYPE.IPV4.value);
//          payload.put(SERVER_IP_ADDRESS.getBytes());
        payload.put(AddressType.DOMAIN.value);
        byte[] addressBytes = socks5ProxyServer.getServerIpAddress().getBytes();
        payload.put((byte) addressBytes.length);
        payload.put(addressBytes);
        payload.put((byte) (((socks5ProxyServer.getServiceListenerPort() & 0XFF00) >> 8)));
        payload.put((byte) (socks5ProxyServer.getServiceListenerPort() & 0XFF));
        byte[] payloadBytes = new byte[payload.position()];
        payload.flip();
        payload.get(payloadBytes);
        return payloadBytes;
    }

    private void handleLog(String format, Object... args) {
        Socks5LogUtil.log("handle, clientIp=" + clientIp + ", port=" + clientPort + ", " + format, args);
    }

}
