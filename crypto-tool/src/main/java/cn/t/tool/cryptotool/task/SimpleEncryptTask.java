package cn.t.tool.cryptotool.task;

import cn.t.tool.cryptotool.constants.EncryptType;
import cn.t.tool.cryptotool.constants.FileType;
import cn.t.tool.cryptotool.construct.RepositoryConfig;
import cn.t.tool.cryptotool.domain.EncryptResult;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.util.common.ArrayUtil;
import cn.t.util.common.digital.IntUtil;
import cn.t.util.io.FileUtil;

import java.io.*;
import java.nio.ByteOrder;

public class SimpleEncryptTask {

    private RepositoryConfig repositoryConfig;

    public EncryptResult encrypt(String key, String path) throws IOException {
        String dataDir = repositoryConfig.calculateDataDirectoryName();
        File dataFile = new File(FileUtil.appendFilePath(dataDir, key));
        boolean success = FileUtil.initFile(dataFile);
        if(!success) {
            throw new AppException("创建存储文件失败: " + dataFile.getCanonicalPath());
        }
        byte[] encryptBytes = doEncrypt(new File(path));
        try (OutputStream os = new FileOutputStream(dataFile)) {
            os.write(encryptBytes);
        }
        EncryptResult encryptResult = new EncryptResult();
        encryptResult.setKey(key);
        encryptResult.setDataFile(key);
        encryptResult.setDataFileSize(encryptBytes.length);
        encryptResult.setEncryptType(EncryptType.SIMPLE.value);
        encryptResult.setCreateTime(System.currentTimeMillis());
        return encryptResult;
    }

    private byte[] doEncrypt(File file) throws IOException {
        if(file.isFile()) {
            return wrapFile(file);
        } else if (file.isDirectory()) {
            return wrapDirectory(file);
        } else {
            throw new AppException("文件加密失败, 未处理的文件类型, 文件地址: " + file.getCanonicalPath());
        }
    }

    private byte[] wrapFile(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] fileNameBytes = file.getName().getBytes();
            byte[] bs = new byte[1 + 1 + fileNameBytes.length + inputStream.available()];
            int index = 0;
            //type
            bs[index++] = FileType.文件.value;
            //length
            bs[index++] = (byte)fileNameBytes.length;
            //file name
            System.arraycopy(fileNameBytes, 0, bs, index, fileNameBytes.length);
            index+=fileNameBytes.length;
            //content
            int len = inputStream.read(bs, index, inputStream.available());
            if(len != bs.length - index) {
                throw new AppException("文件: " + file.getCanonicalPath() + "预计读取字节: " + (bs.length - index) + ", 实际读取字节: " + len);
            }
            return bs;
        }
    }

    private byte[] wrapDirectory(File file) throws IOException {
        byte[] subFileContents = new byte[0];
        for(File sub: file.listFiles()) {
            byte[] subContent = doEncrypt(sub);
            subFileContents = ArrayUtil.combine(subFileContents, subContent);
        }
        byte[] dirNameBytes = file.getName().getBytes();
        byte[] bs = new byte[1 + 4 + dirNameBytes.length + subFileContents.length];
        int index = 0;
        //type
        bs[index++] = FileType.文件夹.value;
        //length
        System.arraycopy(IntUtil.intToBytes((dirNameBytes.length + subFileContents.length), ByteOrder.BIG_ENDIAN), 0, bs, index, 4);
        index+=4;
        //content
        System.arraycopy(subFileContents, 0, bs, index, subFileContents.length);
        return bs;
    }


    public SimpleEncryptTask(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
}
