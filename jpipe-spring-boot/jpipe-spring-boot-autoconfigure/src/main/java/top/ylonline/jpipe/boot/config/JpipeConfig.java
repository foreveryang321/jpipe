package top.ylonline.jpipe.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.threadpool.common.Pool;

/**
 * Jpipe 配置
 *
 * @author YL
 */
@ConfigurationProperties(prefix = Cts.JPIPE_PREFIX)
@Data
public class JpipeConfig {
    private boolean enabled = true;

    @NestedConfigurationProperty
    private Pool pool = new Pool();
}
