package cn.t.tool.cryptotool.userinterface;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.init.AppInit;
import cn.t.tool.cryptotool.task.TaskRunner;
import cn.t.util.common.ArgUtil;

import java.io.IOException;
import java.util.Map;

public class Launcher {
    public static void main(String[] args) throws IOException {
        args = new String[] {"CRYPTO_HOME=/home/amen/tmp/crypto-app", "-encrypt", "sourcePath=/home/amen/tmp/yuntong-back-end.jar"};
        Map<String, String> argMap = ArgUtil.resolveMainArgs(args);
        AppInit appInit = new AppInit();
        Context context = appInit.init(argMap);
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.run(context);
    }
}
