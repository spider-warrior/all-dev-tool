package cn.t.tool.cryptotool.task;

import cn.t.tool.cryptotool.constants.FileType;
import cn.t.tool.cryptotool.construct.RepositoryConfig;
import cn.t.tool.cryptotool.domain.EncryptResult;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;

public class SimpleDecryptTask {

    private static final Logger logger = LoggerFactory.getLogger(SimpleDecryptTask.class);

    private RepositoryConfig repositoryConfig;

    public void decrypt(EncryptResult encryptResult, String targetDir) {
        File dataFile = new File(FileUtil.appendFilePath(repositoryConfig.calculateDataDirectoryName(), encryptResult.getDataFile()));
        if(!dataFile.exists()) {
            logger.info("key[{}] data file not found", encryptResult.getKey());
        } else {
            doDecrypt(dataFile, new File(targetDir));
        }
    }

    public void doDecrypt(File dataFile, File outputFile) {
        boolean success = FileUtil.initDirectory(outputFile);
        if(!success) {
            throw new AppException("初始化输出目录失败: " + outputFile.getAbsolutePath());
        }
        try(FileInputStream fileInputStream = new FileInputStream(dataFile)) {
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.put(bytes);
            while (buffer.remaining() > 0) {
                //file type
                byte type = buffer.get();
                FileType fileType = FileType.getFileType(type);
                if(FileType.FILE == fileType) {
                    //file name length
                    byte fileNameBytesLen = buffer.get();
                    //file name
                    byte[] fileNameBytes = new byte[fileNameBytesLen];
                    buffer.get(fileNameBytes);
                    String fileName = new String(fileNameBytes);
                    //content byte length
                    int contentLength = buffer.getInt();
                    //content
                    byte[] content = new byte[contentLength];
                    buffer.get(content);
                    decryptFile(outputFile, fileName, content);
                } else if(FileType.DIRECTORY == fileType) {

                } else {
                    throw new AppException("文件加密失败, 未处理的文件类型: " + type);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new AppException("文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException("解密文件IO异常");
        }
    }

    public void decryptFile(File parent, String fileName, byte[] content) throws IOException {
        File targetFile = new File(FileUtil.appendFilePath(parent.getPath(), fileName));
        boolean success = FileUtil.initFile(targetFile);
        if(!success) {
            throw new AppException("解密文件失败");
        }
        try(OutputStream os = new FileOutputStream(targetFile)) {
            os.write(content);
        }
    }

    public void decryptDirectory(ByteBuffer byteBuffer) {

    }

    public SimpleDecryptTask(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
}
