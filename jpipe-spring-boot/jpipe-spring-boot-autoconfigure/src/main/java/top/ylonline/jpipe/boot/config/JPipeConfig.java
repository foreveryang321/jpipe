package top.ylonline.jpipe.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.threadpool.common.Pool;

/**
 * JPipe 配置
 *
 * @author Created by YL on 2018/9/11
 */
@ConfigurationProperties(prefix = Cts.JPIPE_PREFIX)
@Data
public class JPipeConfig {
    private boolean enabled = true;

    @NestedConfigurationProperty
    private Pool pool = new Pool();
}