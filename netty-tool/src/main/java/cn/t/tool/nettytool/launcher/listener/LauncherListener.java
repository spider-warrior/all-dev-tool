package cn.t.tool.nettytool.launcher.listener;

import cn.t.tool.nettytool.launcher.AbstractLauncher;

public interface LauncherListener {

    void startup(AbstractLauncher launcher);

    void close(AbstractLauncher launcher);
}
