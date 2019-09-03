package cn.t.tool.cryptotool.userinterface;

import cn.t.tool.cryptotool.init.AppInit;

public class Launcher {
    public static void main(String[] args) {
        args = new String[] {"CRYPTO_HOME=/home/amen/tmp/crypto-app"};
        AppInit appInit = new AppInit();
        appInit.init(args);
    }
}
