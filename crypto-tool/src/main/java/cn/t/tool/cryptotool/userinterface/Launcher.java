package cn.t.tool.cryptotool.userinterface;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.init.AppInit;
import cn.t.tool.cryptotool.task.TaskRunner;
import cn.t.util.common.ArgUtil;

import java.io.IOException;
import java.util.Map;

public class Launcher {
    public static void main(String[] args) throws IOException {
//        encrypt
//        args = new String[] {"CRYPTO_HOME=/home/amen/tmp/crypto-app", "-encrypt", "sourcePath=/home/amen/tmp/yuntong-back-end.jar"};
//        encrypt
        args = new String[] {"CRYPTO_HOME=/home/amen/tmp/crypto-app", "-decrypt", "targetDir=/home/amen/tmp/encrypt-output", "key=2019-09-03"};
        //query
//        args = new String[] {"CRYPTO_HOME=/home/amen/tmp/crypto-app", "-query", "key=2019-09-03"};
        Map<String, String> argMap = ArgUtil.resolveMainArgs(args);
        AppInit appInit = new AppInit();
        Context context = appInit.init(argMap);
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.run(context);
    }
}
