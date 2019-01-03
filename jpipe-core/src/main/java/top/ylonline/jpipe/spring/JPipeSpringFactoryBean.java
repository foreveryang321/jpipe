package top.ylonline.jpipe.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Created by YL on 2018/9/17
 */
public class JPipeSpringFactoryBean implements ApplicationContextAware, InitializingBean {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JPipeSpringFactory.setContext(this.context);
    }
}
