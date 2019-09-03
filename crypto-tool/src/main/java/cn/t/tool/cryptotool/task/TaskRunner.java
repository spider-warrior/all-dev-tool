package cn.t.tool.cryptotool.task;

import cn.t.tool.cryptotool.constants.EncryptType;
import cn.t.tool.cryptotool.construct.Context;
import cn.t.tool.cryptotool.domain.EncryptResult;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.tool.cryptotool.index.IndexDao;
import cn.t.util.common.DateUtil;
import cn.t.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TimeZone;

public class TaskRunner {

    private static final Logger logger = LoggerFactory.getLogger(TaskRunner.class);

    public void run(Context context) throws IOException {
        IndexDao indexDao = new IndexDao(context.getRepositoryConfig());
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
            EncryptResult encryptResult = simpleEncryptTask.encrypt(key, sourcePath);
            indexDao.save(encryptResult);
        } else if(argMap.containsKey("-decrypt")) {
            //解密
            String key = argMap.get("key");
            if(StringUtil.isEmpty(key)) {
                throw new AppException("key must be present");
            }
            String targetDir = argMap.get("targetDir");
            if(StringUtil.isEmpty(targetDir)) {
                throw new AppException("sourcePath must be present");
            }
            EncryptResult encryptResult = indexDao.findByKey(key);
            if(encryptResult == null) {
                logger.info("searched key not exist: {}", key);
            } else {
                SimpleDecryptTask simpleDecryptTask = new SimpleDecryptTask(context.getRepositoryConfig());
                simpleDecryptTask.decrypt(encryptResult, targetDir);
            }
        } else if(argMap.containsKey("-query")) {
            //解密
            String key = argMap.get("key");
            if(StringUtil.isEmpty(key)) {
                throw new AppException("key must be present");
            }
            EncryptResult encryptResult = indexDao.findByKey(key);
            if(encryptResult == null) {
                logger.info("key不存在: {}", key);
            } else {
                logger.info("key details:"
                    + "\r\nkey: " + encryptResult.getKey()
                    + "\r\ndataFile: " + encryptResult.getDataFile()
                    + "\r\ndataFileSize: " + encryptResult.getDataFileSize()
                    + "\r\nencryptType: " + encryptResult.getEncryptType() + "(" + EncryptType.getEncryptType(encryptResult.getEncryptType()) + ")"
                    + "\r\ncreateTime: " + DateUtil.formatLocalDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(encryptResult.getCreateTime()), TimeZone.getDefault().toZoneId()))
                );
            }
        }
    }
}
