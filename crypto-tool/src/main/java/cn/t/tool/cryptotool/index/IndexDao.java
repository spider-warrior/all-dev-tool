package cn.t.tool.cryptotool.index;

import cn.t.tool.cryptotool.constants.EncryptType;
import cn.t.tool.cryptotool.construct.RepositoryConfig;
import cn.t.tool.cryptotool.domain.EncryptResult;
import cn.t.tool.cryptotool.exception.AppException;
import cn.t.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class IndexDao {

    private static final Logger logger = LoggerFactory.getLogger(IndexDao.class);

    private RepositoryConfig repositoryConfig;

    public void save(EncryptResult encryptResult) {
        String indexDir = repositoryConfig.calculateIndexDirectoryName();
        String indexFileName = FileUtil.appendFilePath(indexDir, calculateIndexFileName(encryptResult.getKey()));
        try(DataOutputStream os = new DataOutputStream(new FileOutputStream(indexFileName))) {
            byte[] keyBytes = encryptResult.getKey().getBytes();
            byte[] dataFileBytes = encryptResult.getDataFile().getBytes();
            long dataFileSize = encryptResult.getDataFileSize();
            byte encryptType = encryptResult.getEncryptType();
            long crTime = encryptResult.getCreateTime();
            //key
            os.writeInt(keyBytes.length);
            os.write(keyBytes);
            //data file
            os.writeInt(dataFileBytes.length);
            os.write(dataFileBytes);
            //file size
            os.writeLong(dataFileSize);
            //encrypt type
            os.writeByte(encryptType);
            //create time
            os.writeLong(crTime);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new AppException("文件不存在");
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException("存储索引失败");
        }
        if(logger.isInfoEnabled()) {
            logger.info("new index item:"
                + "\r\nkey: {}"
                + "\r\ndata file: {}"
                + "\r\nfile size: {}"
                + "\r\nencrypt typ: {}[{}]"
                + "\r\ncreate time: {}"
            , encryptResult.getKey(), encryptResult.getDataFile(), encryptResult.getDataFileSize(), encryptResult.getEncryptType(), EncryptType.getEncryptType(encryptResult.getEncryptType()), encryptResult.getCreateTime());
        }
    }

    private String calculateIndexFileName(String key) {
        return key;
    }

    public EncryptResult findByKey(String key) {
        String indexFileName = calculateIndexFileName(key);
        File indexFile = new File(FileUtil.appendFilePath(repositoryConfig.calculateIndexDirectoryName(), indexFileName));
        if(indexFile.exists()) {
            try(DataInputStream os = new DataInputStream(new FileInputStream(indexFile))) {
                //key
                int keyBytesLen = os.readInt();
                byte[] keyBytes = new byte[keyBytesLen];
                os.read(keyBytes);
                //data file
                int dataFileBytesLen = os.readInt();
                byte[] dataFileBytes = new byte[dataFileBytesLen];
                os.read(dataFileBytes);
                //file size
                long fileSize = os.readLong();
                //encrypt type
                byte encryptType = os.readByte();
                //create time
                long crTime = os.readLong();
                EncryptResult encryptResult = new EncryptResult();
                encryptResult.setKey(key);
                encryptResult.setDataFile(new String(dataFileBytes));
                encryptResult.setDataFileSize(fileSize);
                encryptResult.setEncryptType(encryptType);
                encryptResult.setCreateTime(crTime);
                return encryptResult;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new AppException("文件不存在");
            } catch (IOException e) {
                e.printStackTrace();
                throw new AppException("读取索引失败");
            }
        }
        return null;
    }

    public IndexDao(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
}
