package cn.t.tool.cryptotool.task;

import cn.t.tool.cryptotool.construct.RepositoryConfig;
import cn.t.tool.cryptotool.domain.EncryptResult;

import java.io.IOException;

public class SimpleDecryptTask {

    private RepositoryConfig repositoryConfig;

    public void decrypt(EncryptResult encryptResult, String targetDir) throws IOException {


    }

    public SimpleDecryptTask(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
    }
}
