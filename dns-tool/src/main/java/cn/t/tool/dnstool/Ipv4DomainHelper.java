package cn.t.tool.dnstool;

import cn.t.util.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yj
 * @since 2020-01-01 11:55
 **/
@Slf4j
public class Ipv4DomainHelper {

    private final Properties properties;

    public Ipv4DomainHelper() {
        properties = tryIpv4DomainMappingConfiguration();
    }

    private static Properties tryIpv4DomainMappingConfiguration() {
        Properties properties = new Properties();
        try (
            InputStream is = FileUtil.getResourceInputStream(Ipv4DomainHelper.class, "/ipv4-domain-mapping.properties")
        ) {
            if(is == null) {
                log.error("ipv4配置文件未找到: {}", "ipv4-domain-mapping.properties");
            } else {
                properties.load(is);
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return properties;
    }

    /**
     * 根据domain查询ip
     * @param domain 域名
     * @return ip
     */
    public String getCustomDomainMapping(String domain) {
        return properties.getProperty(domain);
    }
}
