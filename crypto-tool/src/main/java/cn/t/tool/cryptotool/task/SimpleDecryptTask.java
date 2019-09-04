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
            File outputFile = new File(targetDir);
            boolean success = FileUtil.initDirectory(outputFile);
            if(!success) {
                throw new AppException("初始化输出目录失败: " + outputFile.getAbsolutePath());
            }
            try(FileInputStream fileInputStream = new FileInputStream(dataFile)) {
                byte[] bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                buffer.put(bytes);
                buffer.flip();
                doDecrypt(buffer, outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new AppException("文件不存在");
            } catch (IOException e) {
                e.printStackTrace();
                throw new AppException("解密文件IO异常");
            }
        }
    }

    public void doDecrypt(ByteBuffer buffer, File outputFile) throws IOException {
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
                //dir name length
                byte dirNameBytesLen = buffer.get();
                //dir name
                byte[] dirNameBytes = new byte[dirNameBytesLen];
                buffer.get(dirNameBytes);
                String dirName = new String(dirNameBytes);
                //content length
                int contentLength = buffer.getInt();
                //content
                byte[] content = new byte[contentLength];
                buffer.get(content);
                decryptDirectory(outputFile, dirName, content);
            } else {
                throw new AppException("文件加密失败, 未处理的文件类型: " + type);
            }
        }
    }

    public void decryptFile(File parent, String fileName, byte[] content) throws IOException {
        File targetFile = new File(FileUtil.appendFilePath(parent.getPath(), fileName));
        logger.info("decrypting file: {}", targetFile.getAbsolutePath());
        boolean success = FileUtil.initFile(targetFile);
        if(!success) {
            throw new AppException("解密文件失败");
        }
        try(OutputStream os = new FileOutputStream(targetFile)) {
            os.write(content);
        }
    }

    public void decryptDirectory(File parent, String dirName, byte[] content) throws IOException {
        File targetDir = new File(FileUtil.appendFilePath(parent.getPath(), dirName));
        logger.info("creating dir: {}", targetDir.getAbsolutePath());
        FileUtil.initDirectory(targetDir);
        ByteBuffer buffer = ByteBuffer.allocate(content.length);
        buffer.put(content);
        buffer.flip();
        doDecrypt(buffer, targetDir);
    }

    public SimpleDecryptTask(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
}
