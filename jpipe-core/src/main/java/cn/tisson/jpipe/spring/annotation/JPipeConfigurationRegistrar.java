package cn.tisson.jpipe.spring.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Created by YL on 2018/8/16
 */
@Slf4j
public class JPipeConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attr = AnnotationAttributes.fromMap(metadata
                .getAnnotationAttributes(EnableJPipeConfiguration.class.getName()));
        if (attr != null) {
            int maxPoolSize = attr.getNumber("maxPoolSize");
            int keepAliveTime = attr.getNumber("keepAliveTime");
            int workQueueSize = attr.getNumber("workQueueSize");

            log.info("maxPoolSize: {}", maxPoolSize);
            log.info("keepAliveTime: {}", keepAliveTime);
            log.info("workQueueSize: {}", workQueueSize);
        }
    }
}
