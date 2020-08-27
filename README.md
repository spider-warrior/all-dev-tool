###开发常用工具

#### proxy 使用步骤
>1.购买国外主机

>2.安装java环境
    
    jdk, maven
    设置环境变量SOCKS5_HOME,并创建users文件, 内容格式: admin=123456:gEIZCLU+48tkSknFfuE9kBCuKyhOrwnt54mJ3KX+uWE=(username=password:key)
    
    

>3.克隆项目并打包
    
    git clone https://github.com/spider-warrior/all-dev-tool.git
    cd workspace/all-dev-tool/
    mvn -Dmaven.test.skip=true  clean install

>4.进入程序目录
    
    cd cd net-proxy-tool/target
    java -jar net-proxy.jar
 
>5.本地主机启动端口监听
     
     java -jar net-proxy.jar HttpProxyServerViaSocks5
     
>6.配置浏览器代理地址
    
    127.0.0.1:1080
    
    
    
    
    
    
    
