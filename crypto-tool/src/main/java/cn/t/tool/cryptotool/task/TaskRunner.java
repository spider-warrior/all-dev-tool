package cn.t.tool.cryptotool.task;

import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.util.common.DateUtil;
import cn.t.util.common.StringUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class TaskRunner {
    public void run(Context context) throws IOException {
        Map<String, String> argMap = context.getArgMap();
        //加密
        if(argMap.containsKey("-encrypt")) {
            SimpleEncryptTask simpleEncryptTask = new SimpleEncryptTask(context.getRepositoryConfig());
            String key = argMap.get("key");
            if(StringUtil.isEmpty(key)) {
                key = DateUtil.formatLocalDate(LocalDate.now());

            }
            String sourcePath = argMap.get("sourcePath");
            if(StringUtil.isEmpty(sourcePath)) {
                throw new AppException("sourcePath must be present");
            }
            simpleEncryptTask.encrypt(key, sourcePath);
        }
    }

}
