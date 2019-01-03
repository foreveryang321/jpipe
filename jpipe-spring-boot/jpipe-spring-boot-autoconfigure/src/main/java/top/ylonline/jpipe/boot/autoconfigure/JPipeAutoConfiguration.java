package top.ylonline.jpipe.boot.autoconfigure;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Version;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import top.ylonline.jpipe.boot.config.JPipeConfig;
import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.freemarker.tag.JPipeTags;
import top.ylonline.jpipe.jsp.tag.PipeTag;
import top.ylonline.jpipe.spring.JPipeSpringFactoryBean;
import top.ylonline.jpipe.threadpool.JPipeThreadPoolExecutor;
import top.ylonline.jpipe.threadpool.common.Pool;
import top.ylonline.jpipe.threadpool.util.JPipeThreadPoolBuilder;

import javax.annotation.PostConstruct;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.List;

import static top.ylonline.jpipe.boot.util.JPipeUtils.FREEMARKER_SHARED_VARIABLE;

/**
 * JPipe configuration
 *
 * @author Created by YL on 2018/8/15
 */
@Configuration
@ConditionalOnProperty(prefix = Cts.JPIPE_PREFIX, name = "enabled", matchIfMissing = true, havingValue = "true")
@ConditionalOnClass(Pool.class)
@EnableConfigurationProperties(JPipeConfig.class)
public class JPipeAutoConfiguration {
    private JPipeConfig jPipeConfig;

    public JPipeAutoConfiguration(JPipeConfig jPipeConfig) {
        this.jPipeConfig = jPipeConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public JPipeSpringFactoryBean jPipeSpringFactoryBean() {
        return new JPipeSpringFactoryBean();
    }

    @Bean
    public JPipeThreadPoolExecutor executor() {
        Pool pool = jPipeConfig.getPool();
        JPipeThreadPoolBuilder builder = new JPipeThreadPoolBuilder();
        builder.setPool(pool);
        return builder.build();
    }

    /**
     * 初始化 freemarker 使用 jsp 标签
     */
    @Configuration
    @ConditionalOnBean(
            value = {
                    freemarker.template.Configuration.class,
                    FreeMarkerConfigurer.class
            }
    )
    @ConditionalOnClass(value = {
            PipeTag.class,
            TagSupport.class
    })
    protected static class JPipeJspTagForFreemarkerConfiguration {
        private FreeMarkerConfigurer freeMarkerConfigurer;

        public JPipeJspTagForFreemarkerConfiguration(FreeMarkerConfigurer freeMarkerConfigurer) {
            this.freeMarkerConfigurer = freeMarkerConfigurer;
        }

        @SuppressWarnings("unchecked")
        @PostConstruct
        public void setConfigurer() {
            List/*<String>*/ tmpList = new ArrayList<>();
            tmpList.add("/META-INF/JPipe.tld");
            // classpathTlds.add("/META-INF/JPipe.tld");
            TaglibFactory factory = freeMarkerConfigurer.getTaglibFactory();
            List/*<String>*/ classpathTlds = factory.getClasspathTlds();
            if (classpathTlds.size() > 0) {
                tmpList.addAll(classpathTlds);
            }
            factory.setClasspathTlds(tmpList);

            Version version = freemarker.template.Configuration.getVersion();
            DefaultObjectWrapper wrapper = new DefaultObjectWrapperBuilder(version).build();
            // 不配置 ObjectWrapper 的话，会警告
            // WARN freemarker.jsp.warn(74)
            // Custom EL functions won't be loaded because no ObjectWrapper was specified for the TaglibFactory
            // (via TaglibFactory.setObjectWrapper(...), exists since 2.3.22).
            factory.setObjectWrapper(wrapper);
        }
    }

    /**
     * freemarker 原生指令初始化
     */
    @Configuration
    @ConditionalOnBean(value = freemarker.template.Configuration.class)
    @ConditionalOnClass(JPipeTags.class)
    protected static class JPipeFreemarkerConfiguration {
        private freemarker.template.Configuration configuration;

        public JPipeFreemarkerConfiguration(freemarker.template.Configuration configuration) {
            this.configuration = configuration;
        }

        @PostConstruct
        public void setConfiguration() {
            Version version = freemarker.template.Configuration.getVersion();
            DefaultObjectWrapper wrapper = new DefaultObjectWrapperBuilder(version).build();
            this.configuration.setSharedVariable(FREEMARKER_SHARED_VARIABLE, new JPipeTags(wrapper));
        }
    }
}
