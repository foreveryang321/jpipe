package cn.tisson.jpipe.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * enable bigPipe
 *
 * @author Created by YL on 2018/8/15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({JPipeConfigurationRegistrar.class})
// @Import({JPipeConfiguration.class, JPipeConfigurationRegistrar.class})
public @interface EnableJPipeConfiguration {
    /**
     * the maximum number of threads to allow in the pool
     */
    int maxPoolSize() default 1024;

    /**
     * when the number of threads is greater than the core, this is the maximum time that excess idle threads will
     * wait for new tasks before terminating.
     * 单位：s
     */
    int keepAliveTime() default 60;

    /**
     * the queue to use for holding tasks before they are executed.  This queue will hold only the {@code Runnable}
     * tasks submitted by the {@code execute}
     * method.
     */
    int workQueueSize() default 512;
}
